package dev.modmind.kunjinkao.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public final class TacticalHudKeyMappings {

    public static final String CATEGORY = "key.category.kunjinkao.tactical";
    public static final String TOGGLE_KEY = "key.kunjinkao.toggle_tactical_hud";
    public static KeyMapping TOGGLE_HUD;

    private TacticalHudKeyMappings() {
    }

    public static void register(RegisterKeyMappingsEvent event) {
        TOGGLE_HUD = new KeyMapping(TOGGLE_KEY, InputConstants.Type.KEYSYM, InputConstants.KEY_H, CATEGORY);
        event.register(TOGGLE_HUD);
    }
}
