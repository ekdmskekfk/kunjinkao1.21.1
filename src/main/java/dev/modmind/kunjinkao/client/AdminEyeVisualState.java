package dev.modmind.kunjinkao.client;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Client copy of the server-authoritative administrator eye-display state. */
public final class AdminEyeVisualState {
    private static final Set<UUID> ENABLED_PLAYERS = ConcurrentHashMap.newKeySet();

    private AdminEyeVisualState() {
    }

    public static void setEnabled(UUID playerUuid, boolean enabled) {
        if (enabled) {
            ENABLED_PLAYERS.add(playerUuid);
        } else {
            ENABLED_PLAYERS.remove(playerUuid);
        }
    }

    public static boolean isEnabled(UUID playerUuid) {
        return ENABLED_PLAYERS.contains(playerUuid);
    }
}
