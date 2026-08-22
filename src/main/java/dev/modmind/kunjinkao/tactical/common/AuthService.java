package dev.modmind.kunjinkao.tactical.common;

import java.util.UUID;

import dev.modmind.kunjinkao.config.TacticalHudConfig;

/** Server-side authorization boundary for every tactical HUD action. */
public final class AuthService {

    private AuthService() {
    }

    public static boolean isAuthorized(UUID playerUuid) {
        return TacticalHudConfig.isAuthorized(playerUuid);
    }
}
