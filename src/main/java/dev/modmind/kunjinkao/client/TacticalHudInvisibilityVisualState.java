package dev.modmind.kunjinkao.client;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/** Client-side server-synced state used to hide true-invisible player equipment. */
public final class TacticalHudInvisibilityVisualState {

    private static final Set<UUID> TRUE_INVISIBLE_PLAYERS = new HashSet<>();

    private TacticalHudInvisibilityVisualState() {
    }

    public static void setTrueInvisible(UUID playerUuid, boolean enabled) {
        if (enabled) {
            TRUE_INVISIBLE_PLAYERS.add(playerUuid);
        } else {
            TRUE_INVISIBLE_PLAYERS.remove(playerUuid);
        }
    }

    public static boolean isTrueInvisible(UUID playerUuid) {
        return TRUE_INVISIBLE_PLAYERS.contains(playerUuid);
    }
}
