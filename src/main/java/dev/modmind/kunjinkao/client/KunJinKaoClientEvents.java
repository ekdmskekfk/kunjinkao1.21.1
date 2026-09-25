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
import dev.modmind.kunjinkao.client.hud.HudScreen;
import dev.modmind.kunjinkao.client.hud.HudEntityScreen;
import dev.modmind.kunjinkao.client.gui.SwordOptionsScreen;
import dev.modmind.kunjinkao.client.gui.AdminPasswordScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class KunJinKaoClientEvents {

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
        handleOpenAdminPassword();
        handleUndoPlacement();
        handleToggleWrench();
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
     * 快速开关扳手模式：L 键。
     * <p>
     * 扳手状态平时在游戏里没有任何显示，所以切换后必须给一句提示，
     * 否则玩家不知道 shift+右键 现在是用于加速还是让给扳手。
     */
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