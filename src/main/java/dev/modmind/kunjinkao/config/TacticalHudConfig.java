package dev.modmind.kunjinkao.config;

import java.util.List;
import java.util.UUID;

import net.neoforged.neoforge.common.ModConfigSpec;

/** Global server-controlled access list for the tactical HUD. */
public final class TacticalHudConfig {

    public static final ModConfigSpec SPEC;
    private static final ModConfigSpec.ConfigValue<List<? extends String>> ADMIN_UUIDS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        ADMIN_UUIDS = builder
            .comment("Minecraft UUIDs allowed to use the tactical HUD.",
                "Example: admin_uuids = [\"00000000-0000-0000-0000-000000000000\"]")
            .defineList("admin_uuids", List.of(), TacticalHudConfig::isUuid);
        SPEC = builder.build();
    }

    private TacticalHudConfig() {
    }

    public static boolean isAuthorized(UUID playerUuid) {
        return ADMIN_UUIDS.get().stream().anyMatch(value -> playerUuid.toString().equalsIgnoreCase(value));
    }

    private static boolean isUuid(Object value) {
        if (!(value instanceof String text)) {
            return false;
        }
        try {
            UUID.fromString(text);
            return true;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }
}
