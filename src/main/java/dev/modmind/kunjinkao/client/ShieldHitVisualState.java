package dev.modmind.kunjinkao.client;

import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Client-side expiry state for the short shield flash shown after a mob attack. */
public final class ShieldHitVisualState {

    private static final long SHIELD_DURATION_TICKS = 20L;
    private static final int MAX_ACTIVE_SHIELDS_PER_PLAYER = 12;
    private static final Map<UUID, List<ShieldHit>> ACTIVE_SHIELDS = new HashMap<>();

    private ShieldHitVisualState() {
    }

    public static void trigger(UUID playerUuid, float relativeImpactYaw, float impactHeight) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null) {
            long now = minecraft.level.getGameTime();
            List<ShieldHit> shields = ACTIVE_SHIELDS.computeIfAbsent(playerUuid, ignored -> new ArrayList<>());
            shields.removeIf(shield -> shield.expiryTick() <= now);
            if (shields.size() >= MAX_ACTIVE_SHIELDS_PER_PLAYER) {
                shields.remove(0);
            }
            shields.add(new ShieldHit(now, now + SHIELD_DURATION_TICKS, relativeImpactYaw, impactHeight));
        }
    }

    public static List<ShieldVisual> getVisuals(UUID playerUuid) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return List.of();
        }
        List<ShieldHit> shields = ACTIVE_SHIELDS.get(playerUuid);
        if (shields == null) {
            return List.of();
        }
        long now = minecraft.level.getGameTime();
        shields.removeIf(shield -> shield.expiryTick() <= now);
        if (shields.isEmpty()) {
            ACTIVE_SHIELDS.remove(playerUuid);
            return List.of();
        }

        List<ShieldVisual> visuals = new ArrayList<>(shields.size());
        for (ShieldHit shield : shields) {
            long remaining = shield.expiryTick() - now;
            visuals.add(new ShieldVisual(shield.relativeImpactYaw(), shield.impactHeight(),
                    Math.min(1.0F, remaining / (float) SHIELD_DURATION_TICKS),
                    now - shield.startTick()));
        }
        return visuals;
    }

    private record ShieldHit(long startTick, long expiryTick, float relativeImpactYaw, float impactHeight) {
    }

    public record ShieldVisual(float relativeImpactYaw, float impactHeight, float alpha, long ageTicks) {
    }
}
