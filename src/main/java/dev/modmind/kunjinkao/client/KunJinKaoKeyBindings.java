package dev.modmind.kunjinkao.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KunJinKaoKeyBindings {
    public static final String CATEGORY = "key.categories.kunjinkao";
    public static final KeyMapping TOGGLE_DISGUISE = new KeyMapping(
            "key.kunjinkao.toggle_disguise",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            CATEGORY
    );

    public static final KeyMapping TOGGLE_OVERWRITE = new KeyMapping(
            "key.kunjinkao.toggle_overwrite",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            CATEGORY
    );

    public static final KeyMapping CYCLE_THEME = new KeyMapping(
            "key.kunjinkao.cycle_theme",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            CATEGORY
    );

    public static final KeyMapping TOGGLE_TACTICAL_HUD = new KeyMapping(
            "key.kunjinkao.toggle_hud",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            CATEGORY
    );

    public static final KeyMapping OPEN_ADMIN_PASSWORD = new KeyMapping(
            "key.kunjinkao.open_admin_password",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_BRACKET,
            CATEGORY
    );

    public static final KeyMapping OPEN_SWORD_OPTIONS = new KeyMapping(
            "key.kunjinkao.open_sword_options",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_PERIOD,
            CATEGORY
    );
}
