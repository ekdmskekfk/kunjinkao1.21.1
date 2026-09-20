package dev.modmind.kunjinkao.client.function;

public enum HudFunction {
    SCAN("01", "hud.kunjinkao.function.scan"),
    TARGET_LOCK("02", "hud.kunjinkao.function.target_lock"),
    ENTITY_INFO("03", "hud.kunjinkao.function.entity_info"),
    NIGHT_VISION("04", "hud.kunjinkao.function.night_vision"),
    TRUE_INVISIBILITY("05", "hud.kunjinkao.function.true_invisibility"),
    MAGNET("06", "hud.kunjinkao.function.magnet"),
    EXCLUSION_LIST("07", "hud.kunjinkao.function.exclusion_list");

    private final String id;
    private final String translationKey;

    HudFunction(String id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
    }

    public String id() {
        return id;
    }

    public String translationKey() {
        return translationKey;
    }
}
