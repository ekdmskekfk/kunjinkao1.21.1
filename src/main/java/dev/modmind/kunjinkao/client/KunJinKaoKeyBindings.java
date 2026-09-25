package dev.modmind.kunjinkao.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KunJinKaoKeyBindings {
    public static final String CATEGORY = "key.categories.kunjinkao";
    // K 让给了"撤销"（放置分区里开启后可用），伪装挪到相邻的 J。
    // 两个键都可以在"选项 → 控制"里自行改绑。
    public static final KeyMapping TOGGLE_DISGUISE = new KeyMapping(
            "key.kunjinkao.toggle_disguise",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
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

    /** 撤销最近一步放置/破坏（需在 . 菜单的「放置」分区里先开启撤销）。 */
    public static final KeyMapping UNDO_PLACEMENT = new KeyMapping(
            "key.kunjinkao.undo_placement",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            CATEGORY
    );
}
