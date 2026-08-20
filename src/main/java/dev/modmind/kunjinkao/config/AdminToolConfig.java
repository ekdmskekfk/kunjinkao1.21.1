package dev.modmind.kunjinkao.config;

import java.util.List;
import java.util.UUID;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class AdminToolConfig {

    public static final ModConfigSpec SPEC;
    private static final ModConfigSpec.ConfigValue<List<? extends String>> ADMIN_UUIDS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        ADMIN_UUIDS = builder
            .comment("Minecraft UUIDs allowed to claim the hidden Kunjinkao admin recipe.",
                "Example: admin_tool_uuids = [\"00000000-0000-0000-0000-000000000000\"]")
            .defineList("admin_tool_uuids", List.of(), AdminToolConfig::isUuid);
        SPEC = builder.build();
    }

    private AdminToolConfig() {
    }

    public static boolean isAuthorized(UUID playerUuid) {
        return ADMIN_UUIDS.get().stream().anyMatch(value -> playerUuid.toString().equalsIgnoreCase(value));
    }

    private static boolean isUuid(Object value) {
        if (!(value instanceof String uuidText)) {
            return false;
        }

        try {
            UUID.fromString(uuidText);
            return true;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }
}
