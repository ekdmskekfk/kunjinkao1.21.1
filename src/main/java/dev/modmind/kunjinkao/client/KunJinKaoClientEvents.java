package dev.modmind.kunjinkao.client;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.KunJinKaoTheme;
import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.network.SimpleActionPayload;
import dev.modmind.kunjinkao.network.SwordSettingPayload;
import dev.modmind.kunjinkao.world.SwordTimeAcceleration;
import net.neoforged.neoforge.client.event.InputEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import dev.modmind.kunjinkao.network.ToggleDisguisePayload;
import dev.modmind.kunjinkao.network.ToggleOverwritePayload;
import dev.modmind.kunjinkao.network.ToggleThemePayload;
import dev.modmind.kunjinkao.network.ToggleTacticalHudPayload;
import dev.modmind.kunjinkao.network.ZoneSelectPayload;
import dev.modmind.kunjinkao.client.hud.HudScreen;
import dev.modmind.kunjinkao.client.hud.HudEntityScreen;
import dev.modmind.kunjinkao.client.gui.SwordOptionsScreen;
import dev.modmind.kunjinkao.client.gui.AdminPasswordScreen;
import dev.modmind.kunjinkao.client.gui.QolControlScreen;
import dev.modmind.kunjinkao.client.qol.QolClientState;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class KunJinKaoClientEvents {

    /**
     * 选区挖掘（127）：下一次按 B 键设的是 A 还是 B 角点。
     * <p>
     * 记在客户端而无所谓"服务端那个角点是不是还在"——两个角点各自独立，
     * 服务端那边取消掉一个也不影响这边的交替节奏，玩家永远是"A、B、A、B"地按，
     * 不需要看着屏幕上的框来判断该按第几下。
     */
    private static boolean zoneNextCornerIsB = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
                KunJinKaoClientOverwriteEffects.tick();
        KunJinKaoClientSwordVisuals.tick();
        RemoteSwordDrawVisualState.tick();
        ClientHudState.tick();
        for (int entityId : KunJinKaoClientOverwriteEffects.getActiveEntityIds()) {
            KunJinKaoClientOverwriteEffects.tickSounds(entityId);
        }
        handleToggleDisguise();
        handleToggleOverwrite();
        handleCycleTheme();
        handleToggleTacticalHud();
        handleOpenSwordOptions();
        handleOpenQolPanel();
        // 面板里绑的开关快捷键：按键格只在面板里改，实际触发在游戏里，
        // 所以放在这一串 tick 处理里，与其它界面开关同一时机。边界检测在 QolKeybinds 里做。
        // 用全限定名而不是加 import：本文件只允许在 onClientTick 里动一行。
        dev.modmind.kunjinkao.client.qol.QolKeybinds.tick(Minecraft.getInstance());
        // 寻宝大师（96）的扫描：每 client tick 调一次，内部自己按 tick 计数决定何时真扫，
        // 并且把区块分批摊到多次扫描里，避免单帧卡顿。
        dev.modmind.kunjinkao.client.qol.QolTreasureScanner.tick(Minecraft.getInstance());
        handleOpenAdminPassword();
        handleUndoPlacement();
        handleCycleAreaMode();
        handleToggleWrench();
        handleCollectBlock();
        handleZoneCorner();
        handleZoneExecute();
    }

    @SubscribeEvent
    public static void onLocalSwordAttack(AttackEntityEvent event) {
        KunJinKaoClientSwordVisuals.onLocalAttack(event);
    }

    @SubscribeEvent
    public static void onLocalSwordRightClick(PlayerInteractEvent.RightClickItem event) {
        KunJinKaoClientSwordVisuals.onLocalRightClick(event.getEntity(), event.getHand());
    }

    /**
     * 伪装切换：J 键（可在按键设置中自定义）。
     * <p>
     * 原本绑在 K 上，K 已经让给"撤销放置"，伪装挪到相邻的 J。
     */
    private static void handleToggleDisguise() {
        if (!KunJinKaoKeyBindings.TOGGLE_DISGUISE.consumeClick()) {
            return;
        }
        Player player = getLocalPlayer();
        if (player == null) {
            return;
        }
        InteractionHand targetHand = findSwordHand(player);
        if (targetHand == null) {
            return;
        }

        ItemStack stack = player.getItemInHand(targetHand);
        KunJinKaoSwordItem.toggleDisguise(stack);
        NetworkHandler.sendToServer(new ToggleDisguisePayload(targetHand));

        boolean disguised = KunJinKaoSwordItem.isDisguised(stack);
        player.displayClientMessage(
                Component.literal(disguised ? "§7已伪装为钻石剑" : "§e已解除伪装"),
                true
        );
    }

    /**
     * 覆写流程开关切换：I 键（可在按键设置中自定义）。
     * 开启 → 无条件覆写+断未；关闭 → 瞬杀。
     */
    private static void handleToggleOverwrite() {
        if (!KunJinKaoKeyBindings.TOGGLE_OVERWRITE.consumeClick()) {
            return;
        }
        Player player = getLocalPlayer();
        if (player == null) {
            return;
        }
        InteractionHand targetHand = findSwordHand(player);
        if (targetHand == null) {
            return;
        }

        ItemStack stack = player.getItemInHand(targetHand);
        if (KunJinKaoSwordItem.isDisguised(stack)) {
            return; // 伪装时不切换覆写流程，二者互不干扰
        }

        KunJinKaoSwordItem.toggleOverwrite(stack);
        NetworkHandler.sendToServer(new ToggleOverwritePayload(targetHand));

        boolean enabled = KunJinKaoSwordItem.isOverwriteEnabled(stack);
        player.displayClientMessage(
                Component.literal(enabled ? "§a覆写流程已开启（无条件覆写+断未）" : "§c覆写流程已关闭（瞬杀）"),
                true
        );
    }

    /**
     * 异象主题循环切换：P 键（0→1→2→3→4→0），伪装时不切换。
     * 本地更新物品 NBT 用于渲染预览，并向服务端发送目标主题号。
     */
    private static void handleCycleTheme() {
        if (!KunJinKaoKeyBindings.CYCLE_THEME.consumeClick()) {
            return;
        }
        Player player = getLocalPlayer();
        if (player == null) {
            return;
        }
        InteractionHand targetHand = findSwordHand(player);
        if (targetHand == null) {
            return;
        }

        ItemStack stack = player.getItemInHand(targetHand);
        if (KunJinKaoSwordItem.isDisguised(stack)) {
            return;
        }

        int newTheme = (KunJinKaoSwordItem.getTheme(stack) + 1) % KunJinKaoTheme.COUNT;
        KunJinKaoSwordItem.setTheme(stack, newTheme);
        NetworkHandler.sendToServer(new ToggleThemePayload(targetHand, newTheme));
        player.displayClientMessage(
                Component.literal("§d异象主题：" + KunJinKaoTheme.displayName(newTheme)),
                true
        );
    }

    /**
     * 战术 HUD 开关：使用 consumeClick，按住 H 不会重复切换。
     */
    private static void handleToggleTacticalHud() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) {
            ClientHudState.reset();
            return;
        }
        if (minecraft.screen != null && !(minecraft.screen instanceof HudScreen)
                && !(minecraft.screen instanceof HudEntityScreen)) {
            return;
        }
        if (!KunJinKaoKeyBindings.TOGGLE_TACTICAL_HUD.consumeClick()) {
            return;
        }
        // HUD 是否打开由服务端白名单决定；客户端只提交请求，不自行切换状态。
        NetworkHandler.sendToServer(new ToggleTacticalHudPayload(!ClientHudState.isEnabled()));
    }

    /** 】键打开管理员密码窗口；所有密码校验均由服务端完成。 */
    /**
     * 按住 shift 滚动滚轮：快速调节时间加速倍率。
     * <p>
     * 接管条件刻意收得很紧 —— 必须同时满足"没开任何界面 + 按着 shift + 手里是管理员剑 +
     * 时间加速不是关闭"。只要有一条不满足就完全放行，不抢正常的快捷栏切换。
     */
    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.screen != null || !player.isShiftKeyDown()) {
            return;
        }
        InteractionHand hand = findSwordHand(player);
        if (hand == null) {
            return;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (KunJinKaoSwordItem.getTimeAccelMode(stack) == SwordTimeAcceleration.MODE_OFF) {
            return;
        }
        int next = SwordTimeAcceleration.stepMultiplier(KunJinKaoSwordItem.getTimeAccelMultiplier(stack),
                event.getScrollDeltaY() > 0.0D ? 1 : -1);
        // 本地先改，滚轮手感才跟得上；服务端那份由下面的包同步。
        KunJinKaoSwordItem.setTimeAccelMultiplier(stack, next);
        NetworkHandler.sendToServer(new SwordSettingPayload(hand,
                SwordSettingPayload.TIME_ACCEL_MULTIPLIER, next));
        player.displayClientMessage(Component.translatable("message.kunjinkao.time_accel_multiplier", next), true);
        event.setCanceled(true);
    }

    private static void handleOpenAdminPassword() {
        Minecraft minecraft = Minecraft.getInstance();
        if (KunJinKaoKeyBindings.OPEN_ADMIN_PASSWORD.consumeClick() && minecraft.player != null
                && minecraft.screen == null) {
            minecraft.setScreen(new AdminPasswordScreen());
        }
    }

    /**
     * 句号键只在手持管理员剑且没有其他界面占用输入时打开设置界面。
     */
    private static void handleOpenSwordOptions() {
        Minecraft minecraft = Minecraft.getInstance();
        if (!KunJinKaoKeyBindings.OPEN_SWORD_OPTIONS.consumeClick()
                || minecraft.player == null
                || minecraft.screen != null) {
            return;
        }
        InteractionHand hand = findSwordHand(minecraft.player);
        if (hand != null) {
            minecraft.setScreen(new SwordOptionsScreen(hand));
        }
    }

    /**
     * G 键：打开「功能控制台」面板。
     * <p>
     * 不要求手持剑 —— 面板管的是玩家自己的功能开关（夜视、呼吸、交易…），
     * 跟手上拿什么没关系。全量状态由面板自己在 init 里要一次。
     */
    private static void handleOpenQolPanel() {
        Minecraft minecraft = Minecraft.getInstance();
        if (!KunJinKaoKeyBindings.OPEN_QOL_PANEL.consumeClick()
                || minecraft.player == null
                || minecraft.screen != null) {
            return;
        }
        minecraft.setScreen(new QolControlScreen());
    }

    /**
     * 断开连接时清掉面板的客户端镜像。
     * <p>
     * 不清的话，退出去再进另一个服务器，面板会先画上一局的旧状态，
     * 直到新的全量同步到达 —— 那一瞬间看起来就是"我明明没开夜视，它显示开着"。
     */
    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        QolClientState.clear();
    }

    /**
     * 快速开关扳手模式：L 键。
     * <p>
     * 扳手状态平时在游戏里没有任何显示，所以切换后必须给一句提示，
     * 否则玩家不知道 shift+右键 现在是用于加速还是让给扳手。
     */
    /**
     * R 键：把正看着的方块直接收进物品栏。
     * <p>
     * 这里刻意不做客户端射线检测、也不带坐标 —— 只发一个"我按了 R"的空包，
     * 由服务端用自己的射线重新确定目标，客户端无法指定任意坐标。
     */
    private static void handleCollectBlock() {
        if (!KunJinKaoKeyBindings.COLLECT_BLOCK.consumeClick()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.screen != null) {
            return;
        }
        if (findSwordHand(player) == null) {
            return;
        }
        NetworkHandler.sendToServer(new dev.modmind.kunjinkao.network.CollectBlockPayload());
    }

    /**
     * 选区挖掘（127）：B 键把准星指着的那一格设成选区的一个角点。
     * <p>
     * 坐标由客户端用自己的射线取，服务端会重新校验（距离、维度、单边上限）——
     * 这里取到的坐标只是「玩家看着的那一格」，不是权威值。
     */
    private static void handleZoneCorner() {
        if (!KunJinKaoKeyBindings.ZONE_CORNER.consumeClick()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.screen != null || minecraft.level == null) {
            return;
        }
        HitResult hit = minecraft.hitResult;
        // 只认方块命中：MISS 没有坐标，ENTITY 的位置属于实体而不是方块，
        // 两者都不该被当成"我要框这一格"。
        if (hit == null || hit.getType() != HitResult.Type.BLOCK || !(hit instanceof BlockHitResult blockHit)) {
            return;
        }
        BlockPos pos = blockHit.getBlockPos();
        // 命中水面、岩浆面时，真正被打到的其实是那一格液体：把角点设在液体上没有意义，
        // 所以这里要求命中的是方块本体，而不是流体或空气。
        BlockState state = minecraft.level.getBlockState(pos);
        if (state.isAir() || !state.getFluidState().isEmpty()) {
            return;
        }

        int cornerIndex = zoneNextCornerIsB ? ZoneSelectPayload.CORNER_B : ZoneSelectPayload.CORNER_A;
        NetworkHandler.sendToServer(new ZoneSelectPayload(cornerIndex, pos));
        // 翻转放在发包之后而不是之前：没打中时不该白白吃掉一次 A，
        // 否则玩家点空一下，下一次按下去设的就变成 B 了，而且屏幕上完全看不出原因。
        zoneNextCornerIsB = !zoneNextCornerIsB;
    }

    /**
     * 选区挖掘（127）：N 键确认执行；按住 Shift 再按是取消。
     * <p>
     * 二次确认、权限、服务器总闸全在服务端（见 {@code KunJinKaoQolHandler} 里的三个 handleZoneXxx），
     * 客户端只把按键翻译成一条动作包，从不自己改世界 —— 所以这里连"选区够不够大"都不判，
     * 判了也只是把服务端的话提前说一遍，还会两边不一致。
     */
    private static void handleZoneExecute() {
        if (!KunJinKaoKeyBindings.ZONE_EXCAVATE.consumeClick()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.screen != null) {
            return;
        }
        NetworkHandler.sendToServer(new SimpleActionPayload(player.isShiftKeyDown()
                ? SimpleActionPayload.ZONE_CANCEL
                : SimpleActionPayload.ZONE_EXECUTE));
    }
    private static void handleCycleAreaMode() {
        if (!KunJinKaoKeyBindings.CYCLE_AREA_MODE.consumeClick()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.screen != null) {
            return;
        }
        InteractionHand hand = findSwordHand(player);
        if (hand == null) {
            return;
        }
        ItemStack stack = player.getItemInHand(hand);
        int mode = KunJinKaoSwordItem.nextAreaMode(KunJinKaoSwordItem.getAreaMode(stack));
        // 本地先改，按键反馈才跟得上；服务端那份由下面的包同步。
        KunJinKaoSwordItem.setAreaMode(stack, mode);
        NetworkHandler.sendToServer(new SwordSettingPayload(hand, SwordSettingPayload.AREA_MODE, mode));
        player.displayClientMessage(Component.translatable(switch (mode) {
            case 1 -> "message.kunjinkao.area_mode_pause";
            case 2 -> "message.kunjinkao.area_mode_accel";
            default -> "message.kunjinkao.area_mode_off";
        }), true);
    }

    private static void handleToggleWrench() {
        if (!KunJinKaoKeyBindings.TOGGLE_WRENCH.consumeClick()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.screen != null) {
            return;
        }
        InteractionHand hand = findSwordHand(player);
        if (hand == null) {
            return;
        }
        ItemStack stack = player.getItemInHand(hand);
        boolean enabled = !KunJinKaoSwordItem.isWrenchEnabled(stack);
        // 本地先改，按键反馈才跟得上；服务端那份由下面的包同步。
        KunJinKaoSwordItem.setWrenchEnabled(stack, enabled);
        NetworkHandler.sendToServer(new SwordSettingPayload(hand, SwordSettingPayload.WRENCH,
                enabled ? 1 : 0));
        player.displayClientMessage(Component.translatable(enabled
                ? "message.kunjinkao.wrench_enabled" : "message.kunjinkao.wrench_disabled"), true);
    }

    /**
     * 撤销最近一步放置/破坏：K 键。
     * <p>
     * 是否真的执行由服务端判定 —— 只有手里的剑开着"撤销"才生效，
     * 这里只负责把按键转成一条动作包，避免在客户端改动世界。
     */
    private static void handleUndoPlacement() {
        if (KunJinKaoKeyBindings.UNDO_PLACEMENT.consumeClick()
                && Minecraft.getInstance().player != null
                && Minecraft.getInstance().screen == null) {
            NetworkHandler.sendToServer(new SimpleActionPayload(SimpleActionPayload.UNDO_PLACEMENT));
        }
    }

    @Nullable
    private static Player getLocalPlayer() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player;
    }

    /**
     * 查找手持锟斤拷之剑的手（主手优先，副手其次）。
     */
    @Nullable
    private static InteractionHand findSwordHand(Player player) {
        for (InteractionHand hand : InteractionHand.values()) {
            if (player.getItemInHand(hand).getItem() instanceof KunJinKaoSwordItem) {
                return hand;
            }
        }
        return null;
    }
}