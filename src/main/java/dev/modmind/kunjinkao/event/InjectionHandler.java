package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.config.AdminToolConfig;
import dev.modmind.kunjinkao.inject.InjectionData;
import dev.modmind.kunjinkao.network.InjectionStatePayload;
import dev.modmind.kunjinkao.network.NetworkHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 「注入」的服务端权威实现：校验、计时、中断、落盘。
 *
 * <p>为什么整段判定都放服务端：注入的产物是<b>一件永久改变数值的物品</b>，
 * 这是玩家能拿到的最高价值对象。客户端只负责"按键"和"画进度条"，
 * 任何一次开始/完成都由服务端重新校验一次手持物，客户端说什么都不作数。</p>
 *
 * <p>打断条件（任意一条成立即中断，客户端进度条立刻消失）：
 * <ul>
 *   <li>手持物品换了、没了、或者数量变了；</li>
 *   <li>位移超过 {@link #MAX_DRIFT} 格（含被击退、被传送）；</li>
 *   <li>生命值下降（受到伤害）；</li>
 *   <li>主手注入时切换了快捷栏槽位；</li>
 *   <li>死亡、掉线、换维度。</li>
 * </ul>
 */
public final class InjectionHandler {

    /** 进行中的注入。键是玩家 UUID —— 同一玩家同时只允许一条。 */
    private static final Map<UUID, Job> JOBS = new HashMap<>();

    /**
     * 允许的位移（格）。
     *
     * <p>取 0.75 而不是 0：玩家站在原地转头、被自己的坐骑轻微推动、
     * 或者客户端插值抖动，都会产生很小的坐标差；卡死为 0 会让注入永远无法完成。
     */
    private static final double MAX_DRIFT = 0.75D;

    private InjectionHandler() {
    }

    private static final class Job {
        final InteractionHand hand;
        final int totalTicks;
        final double startX;
        final double startY;
        final double startZ;
        /** 主手注入时记下快捷栏槽位；副手为 -1。 */
        final int hotbarSlot;
        final ResourceLocation itemId;
        final int startCount;
        final float startHealth;

        int ticks;

        Job(InteractionHand hand, int totalTicks, ServerPlayer player) {
            this.hand = hand;
            this.totalTicks = totalTicks;
            this.startX = player.getX();
            this.startY = player.getY();
            this.startZ = player.getZ();
            this.hotbarSlot = hand == InteractionHand.MAIN_HAND
                    ? player.getInventory().selected : -1;
            ItemStack stack = player.getItemInHand(hand);
            this.itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
            this.startCount = stack.getCount();
            this.startHealth = player.getHealth();
        }
    }

    // ===== 入口 =====

    /** 客户端按下了注入键。所有校验在这里重做一次。 */
    public static void start(ServerPlayer player, InteractionHand hand) {
        if (!AdminToolConfig.injectionEnabled()) {
            player.displayClientMessage(
                    Component.translatable("message.kunjinkao.injection_disabled"), true);
            return;
        }
        if (AdminToolConfig.injectionRequiresAuthorization()
                && !AdminToolConfig.isAuthorized(player.getUUID())) {
            player.displayClientMessage(
                    Component.translatable("message.kunjinkao.admin_required"), true);
            return;
        }
        if (JOBS.containsKey(player.getUUID())) {
            return;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) {
            player.displayClientMessage(
                    Component.translatable("message.kunjinkao.injection_empty_hand"), true);
            return;
        }
        if (InjectionData.isInjected(stack)) {
            player.displayClientMessage(
                    Component.translatable("message.kunjinkao.injection_already"), true);
            return;
        }
        if (isBlacklisted(stack)) {
            player.displayClientMessage(
                    Component.translatable("message.kunjinkao.injection_blacklisted"), true);
            return;
        }
        int duration = AdminToolConfig.injectionDurationTicks();
        JOBS.put(player.getUUID(), new Job(hand, duration, player));
        NetworkHandler.sendToPlayer(player,
                new InjectionStatePayload(InjectionStatePayload.START, duration, hand.ordinal()));
    }

    // ===== 推进 =====

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        Job job = JOBS.get(player.getUUID());
        if (job == null) {
            return;
        }
        String abortReason = invalidReason(player, job);
        if (abortReason != null) {
            JOBS.remove(player.getUUID());
            NetworkHandler.sendToPlayer(player,
                    new InjectionStatePayload(InjectionStatePayload.ABORT, job.totalTicks,
                            job.hand.ordinal()));
            player.displayClientMessage(
                    Component.translatable("message.kunjinkao.injection_aborted",
                            Component.translatable(abortReason)), true);
            return;
        }
        if (++job.ticks >= job.totalTicks) {
            JOBS.remove(player.getUUID());
            finish(player, job);
        }
    }

    /** @return null 表示可以继续；否则返回中断原因的翻译键 */
    private static String invalidReason(ServerPlayer player, Job job) {
        if (!player.isAlive()) {
            return "message.kunjinkao.injection_abort_dead";
        }
        if (player.getHealth() < job.startHealth - 1.0E-4F) {
            return "message.kunjinkao.injection_abort_hurt";
        }
        if (job.hotbarSlot >= 0 && player.getInventory().selected != job.hotbarSlot) {
            return "message.kunjinkao.injection_abort_switched";
        }
        ItemStack stack = player.getItemInHand(job.hand);
        if (stack.isEmpty()
                || stack.getCount() != job.startCount
                || !BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(job.itemId)) {
            return "message.kunjinkao.injection_abort_item_changed";
        }
        double dx = player.getX() - job.startX;
        double dy = player.getY() - job.startY;
        double dz = player.getZ() - job.startZ;
        if (dx * dx + dy * dy + dz * dz > MAX_DRIFT * MAX_DRIFT) {
            return "message.kunjinkao.injection_abort_moved";
        }
        return null;
    }

    private static void finish(ServerPlayer player, Job job) {
        ItemStack held = player.getItemInHand(job.hand);
        if (held.isEmpty()) {
            return;
        }
        // 关键语义：注入只作用于「手里这一个物品」，不是整组、更不是这一种物品。
        // 手持一组 64 个时，拆出 1 个来注入，剩下的 63 个原样退回背包；
        // 这样不可能出现"点一次，一组全变成神器"。
        if (held.getCount() > 1) {
            ItemStack target = held.copyWithCount(1);
            ItemStack rest = held.copy();
            rest.shrink(1);
            InjectionData.apply(target);
            player.setItemInHand(job.hand, target);
            if (!rest.isEmpty() && !player.getInventory().add(rest)) {
                // 背包塞不下就吐在脚下，绝不静默丢弃。
                player.drop(rest, false);
            }
        } else {
            InjectionData.apply(held);
        }
        player.displayClientMessage(
                Component.translatable("message.kunjinkao.injection_done"), true);
        NetworkHandler.sendToPlayer(player,
                new InjectionStatePayload(InjectionStatePayload.DONE, job.totalTicks,
                        job.hand.ordinal()));
        player.level().playSound(null, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE,
                SoundSource.PLAYERS, 1.0F, 1.0F);
        // 物品变了（多了注释/属性），要让客户端刷新一次手持物的显示。
        player.containerMenu.broadcastChanges();
    }

    // ===== 黑名单 =====

    /**
     * 黑名单匹配：既能写完整物品 id（{@code minecraft:bedrock}），
     * 也能写模组 id（{@code createdeco}）一次封掉整个模组的物品。
     */
    private static boolean isBlacklisted(ItemStack stack) {
        List<? extends String> blacklist = AdminToolConfig.injectionBlacklist();
        if (blacklist.isEmpty()) {
            return false;
        }
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String full = id.toString();
        String namespace = id.getNamespace();
        for (String entry : blacklist) {
            if (entry == null || entry.isBlank()) {
                continue;
            }
            String trimmed = entry.trim();
            if (trimmed.equals(full) || trimmed.equals(namespace)) {
                return true;
            }
        }
        return false;
    }

    // ===== 清理 =====

    /** 掉线/换维度时把进行中的注入丢掉，避免行程残留。 */
    @SubscribeEvent
    public static void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            JOBS.remove(player.getUUID());
        }
    }

    @SubscribeEvent
    public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Job job = JOBS.remove(player.getUUID());
            if (job != null) {
                NetworkHandler.sendToPlayer(player,
                        new InjectionStatePayload(InjectionStatePayload.ABORT, job.totalTicks,
                                job.hand.ordinal()));
            }
        }
    }
}
