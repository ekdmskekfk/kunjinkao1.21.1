package dev.modmind.kunjinkao.tactical.common;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

/** Owns only the invisibility effect created by the tactical HUD. */
public final class TacticalHudInvisibilityService {

    private static final String MARKER = KunJinKaoEntry.MOD_ID + ":tactical_invisibility";
    private static final int DURATION_TICKS = 36_000;

    private TacticalHudInvisibilityService() {
    }

    public static boolean isActive(ServerPlayer player) {
        return player.getPersistentData().getBoolean(MARKER) && player.hasEffect(MobEffects.INVISIBILITY);
    }

    public static boolean clearExpiredMarker(ServerPlayer player) {
        if (player.getPersistentData().getBoolean(MARKER) && !player.hasEffect(MobEffects.INVISIBILITY)) {
            player.getPersistentData().remove(MARKER);
            return true;
        }
        return false;
    }

    public static ToggleResult toggle(ServerPlayer player) {
        if (player.getPersistentData().getBoolean(MARKER)) {
            player.removeEffect(MobEffects.INVISIBILITY);
            player.getPersistentData().remove(MARKER);
            return new ToggleResult(true, "Invisibility disabled");
        }
        if (player.hasEffect(MobEffects.INVISIBILITY)) {
            return new ToggleResult(false, "Invisibility is controlled by another source");
        }
        // No ambient effect, particles, or inventory icon.
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, DURATION_TICKS, 0, false, false, false));
        player.getPersistentData().putBoolean(MARKER, true);
        return new ToggleResult(true, "Invisibility enabled");
    }

    public record ToggleResult(boolean success, String message) {
    }
}
