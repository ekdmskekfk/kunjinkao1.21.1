package dev.modmind.kunjinkao.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KunJinKaoKeyBindings {

    private static final Logger LOGGER = LoggerFactory.getLogger(KunJinKaoKeyBindings.class);

    public static final String CATEGORY = "key.categories.kunjinkao";
    public static final String KEY_OVERWRITE = "key.kunjinkao.overwrite";
    public static final String KEY_DISGUISE = "key.kunjinkao.disguise";
    public static final String KEY_THEME = "key.kunjinkao.theme";

    public static KeyMapping keyOverwrite;
    public static KeyMapping keyDisguise;
    public static KeyMapping keyTheme;

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        LOGGER.info("[KunJinKao] RegisterKeyMappingsEvent fired, registering O/G/T");
        keyOverwrite = new KeyMapping(KEY_OVERWRITE, InputConstants.Type.KEYSYM, InputConstants.KEY_O, CATEGORY);
        keyDisguise = new KeyMapping(KEY_DISGUISE, InputConstants.Type.KEYSYM, InputConstants.KEY_G, CATEGORY);
        keyTheme = new KeyMapping(KEY_THEME, InputConstants.Type.KEYSYM, InputConstants.KEY_T, CATEGORY);
        event.register(keyOverwrite);
        event.register(keyDisguise);
        event.register(keyTheme);
    }

    public static void tick() {
        if (keyOverwrite == null || keyDisguise == null || keyTheme == null) {
            return;
        }
        while (keyOverwrite.consumeClick()) {
            KunJinKaoClientEvents.onOverwriteToggle();
        }
        while (keyDisguise.consumeClick()) {
            KunJinKaoClientEvents.onDisguiseToggle();
        }
        while (keyTheme.consumeClick()) {
            KunJinKaoClientEvents.onThemeToggle();
        }
    }
}