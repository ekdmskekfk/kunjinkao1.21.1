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

    /**
     * 快速开关"扳手模式"：打开后 shift+右键 整个让给扳手，本模组不消费，
     * AE2 那类绑在 shift+右键 上的模组扳手逻辑才能被触发。
     */
    public static final KeyMapping TOGGLE_WRENCH = new KeyMapping(
            "key.kunjinkao.toggle_wrench",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_L,
            CATEGORY
    );

    /**
     * R 键：把正看着的方块直接收进物品栏。
     * <p>
     * 需要先在 . 菜单的「挖掘与战利品」里开启"可破坏不可破坏方块"；
     * 收进来的物品保留方块实体数据（机器里装的东西不会丢）。
     */
    public static final KeyMapping COLLECT_BLOCK = new KeyMapping(
            "key.kunjinkao.collect_block",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            CATEGORY
    );

    /**
     * V 键：循环切换「区域模式」：关 -> 暂停场 -> 范围加速 -> 关。
     * <p>
     * 选中的那一种会占住 shift+右键：在那格立/撤一个立方体区域场
     * （暂停场让生物与机器停下、玩家被钉住；范围加速让区域里的机器加速）。
     * 管理员与场主不会被暂停场冻住。尺寸在菜单第二页调。
     */
    public static final KeyMapping CYCLE_AREA_MODE = new KeyMapping(
            "key.kunjinkao.cycle_area_mode",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            CATEGORY
    );

    /** 撤销最近一步放置/破坏（需在 . 菜单的「放置」分区里先开启撤销）。 */
    public static final KeyMapping UNDO_PLACEMENT = new KeyMapping(
            "key.kunjinkao.undo_placement",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            CATEGORY
    );

    /**
     * G 键：打开「功能控制台」面板（15 项 QoL 功能的统一入口）。
     * <p>
     * 开发计划里建议默认 {@code J}，但 {@code J} 早就给了伪装，所以改用 {@code G}
     * （原版未占用，也对应"功能"）。与其它按键一样可以在"选项 → 控制"里改绑。
     */
    public static final KeyMapping OPEN_QOL_PANEL = new KeyMapping(
            "key.kunjinkao.open_qol_panel",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            CATEGORY
    );

    /**
     * B 键：选区挖掘（127）—— 把准星指着的那一格设成选区的一个角点。
     * <p>
     * 选 B/N 是因为 {@code J I P H ] . L R V K G} 都已经被本模组占掉了；
     * 这两个键在原版里没有默认用途，不会和别的操作抢。
     * 按一次设 A、再按一次设 B、再按一次又回到 A，不需要第三个键来切换角点。
     */
    public static final KeyMapping ZONE_CORNER = new KeyMapping(
            "key.kunjinkao.zone_corner",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            CATEGORY
    );

    /**
     * N 键：选区挖掘（127）—— 确认执行当前选区。
     * <p>
     * 按住 Shift 再按这个是取消（停作业或清选区）。取消刻意不另给按键：
     * 每多一个按键就多一个和别的模组抢默认键的机会，而"执行/取消"本来就是一正一反的一对，
     * 用同一个键加修饰键比两个独立的键更好记。
     */
    public static final KeyMapping ZONE_EXCAVATE = new KeyMapping(
            "key.kunjinkao.zone_excavate",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            CATEGORY
    );
}
