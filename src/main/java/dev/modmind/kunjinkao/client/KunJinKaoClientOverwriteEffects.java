package dev.modmind.kunjinkao.client;

import dev.modmind.kunjinkao.KunJinKaoTheme;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class KunJinKaoClientOverwriteEffects {

    public static final int END_MESSAGE_TICKS = 60;
    public static final int RESIDUE_TICKS = 200;
    public static final int FADEOUT_TICKS = 20;

    private static final Map<Integer, ClientOverwriteState> CLIENT_STATES = new HashMap<>();
    private static final Map<Integer, ResidueMarker> RESIDUE_MARKERS = new HashMap<>();
    private static int endMessageTicks = 0;
    private static String lastEndName = "";
    private static int fadeOutTicks = 0;
    private static boolean overwriteActive = false;
    private static int maxRemainingTicks = 0;

    @OnlyIn(Dist.CLIENT)
    public static class ClientOverwriteState {
        final int entityId;
        int phase;
        int remainingTicks;
        int theme;
        final List<String> keyboardLines = new ArrayList<>();
        boolean[] keyboardPlayed;

        ClientOverwriteState(int entityId, int phase, int remainingTicks, int theme) {
            this.entityId = entityId;
            this.phase = phase;
            this.remainingTicks = remainingTicks;
            this.theme = theme;
            this.keyboardPlayed = new boolean[10];
        }
    }

    @OnlyIn(Dist.CLIENT)
    public record ResidueMarker(int entityId, double x, double y, double z, int theme, int ageTicks) {}

    public static boolean isOverwriteActive() {
        return overwriteActive;
    }

    public static int getMaxRemainingTicks() {
        return maxRemainingTicks;
    }

    public static int[] getActiveEntityIds() {
        int[] ids = new int[CLIENT_STATES.size()];
        int i = 0;
        for (ClientOverwriteState state : CLIENT_STATES.values()) {
            ids[i++] = state.entityId;
        }
        return ids;
    }

    public static int getCurrentPhase(int entityId) {
        ClientOverwriteState state = CLIENT_STATES.get(entityId);
        return state != null ? state.phase : 0;
    }

    public static int getRemainingTicks(int entityId) {
        ClientOverwriteState state = CLIENT_STATES.get(entityId);
        return state != null ? state.remainingTicks : 0;
    }

    public static int getEntityTheme(int entityId) {
        ClientOverwriteState state = CLIENT_STATES.get(entityId);
        return state != null ? state.theme : 0;
    }

    public static void markKeyboardLinePlayed(int entityId, int lineIndex) {
        ClientOverwriteState state = CLIENT_STATES.get(entityId);
        if (state != null && lineIndex >= 0 && lineIndex < state.keyboardPlayed.length) {
            state.keyboardPlayed[lineIndex] = true;
        }
    }

    public static Map<Integer, ResidueMarker> getResidueMarkers() {
        return new HashMap<>(RESIDUE_MARKERS);
    }

    public static int getResidueTicks() {
        return RESIDUE_TICKS;
    }

    public static int getEndMessageTicks() {
        return endMessageTicks;
    }

    public static String getLastEndName() {
        return lastEndName;
    }

    public static int getFadeOutTicks() {
        return fadeOutTicks;
    }

    public static void start(int entityId, int remainingTicks, int theme) {
        overwriteActive = true;
        ClientOverwriteState state = new ClientOverwriteState(
                entityId, 0, Math.max(1, remainingTicks), Math.floorMod(theme, KunJinKaoTheme.COUNT));
        CLIENT_STATES.put(entityId, state);
        maxRemainingTicks = Math.max(maxRemainingTicks, state.remainingTicks);
    }

    public static void startFlash(int entityId, int phase, int theme) {
        start(entityId, 40, theme);
    }

    public static void update(int entityId, int remainingTicks) {
        ClientOverwriteState state = CLIENT_STATES.get(entityId);
        if (state != null) {
            state.remainingTicks = Math.max(0, remainingTicks);
            maxRemainingTicks = Math.max(maxRemainingTicks, state.remainingTicks);
        }
    }

    public static void endFlash(int entityId, BlockPos position) {
        ClientOverwriteState state = CLIENT_STATES.remove(entityId);
        String name = "目标";
        int theme = state != null ? state.theme : 0;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            Entity entity = mc.level.getEntity(entityId);
            if (entity != null) {
                name = entity.getDisplayName().getString();
            }
        }
        if (position != null) {
            RESIDUE_MARKERS.put(entityId, new ResidueMarker(
                    entityId, position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.5D, theme, 0));
        }
        lastEndName = name;
        endMessageTicks = END_MESSAGE_TICKS;
        fadeOutTicks = FADEOUT_TICKS;
        if (CLIENT_STATES.isEmpty()) {
            overwriteActive = false;
            maxRemainingTicks = 0;
        }
    }

    public static void cancel(int entityId) {
        CLIENT_STATES.remove(entityId);
        if (CLIENT_STATES.isEmpty()) {
            overwriteActive = false;
            maxRemainingTicks = 0;
        }
    }

    public static void startDecision(int entityId, int remainingTicks) {
        ClientOverwriteState state = CLIENT_STATES.get(entityId);
        if (state != null) {
            state.phase = 1;
            state.remainingTicks = Math.max(1, remainingTicks);
        }
    }

    public static void startDecision(int entityId) {
        startDecision(entityId, 40);
    }

    public static void update() {
        for (ClientOverwriteState state : new ArrayList<>(CLIENT_STATES.values())) {
            if (state.remainingTicks > 0) {
                state.remainingTicks--;
                if (state.remainingTicks == 0) {
                    CLIENT_STATES.remove(state.entityId);
                }
            }
        }
        if (CLIENT_STATES.isEmpty()) {
            overwriteActive = false;
            maxRemainingTicks = 0;
        }
        if (endMessageTicks > 0) endMessageTicks--;
        if (fadeOutTicks > 0) fadeOutTicks--;
        Map<Integer, ResidueMarker> aged = new HashMap<>();
        for (Map.Entry<Integer, ResidueMarker> entry : RESIDUE_MARKERS.entrySet()) {
            ResidueMarker marker = entry.getValue();
            if (marker.ageTicks() < RESIDUE_TICKS) {
                aged.put(entry.getKey(), new ResidueMarker(
                        marker.entityId(), marker.x(), marker.y(), marker.z(), marker.theme(), marker.ageTicks() + 1));
            }
        }
        RESIDUE_MARKERS.clear();
        RESIDUE_MARKERS.putAll(aged);
    }

    public static void endFlash(String entityName) {
        lastEndName = entityName;
        endMessageTicks = END_MESSAGE_TICKS;
        fadeOutTicks = FADEOUT_TICKS;
    }

    public static void cancel() {
        CLIENT_STATES.clear();
        overwriteActive = false;
        maxRemainingTicks = 0;
        endMessageTicks = 0;
        fadeOutTicks = 0;
        RESIDUE_MARKERS.clear();
    }
}
