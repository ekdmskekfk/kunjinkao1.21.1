package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.network.PlacementSyncPayload;
import dev.modmind.kunjinkao.world.SwordAreaFields;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.UUID;

/**
 * 区域场的每 tick 执行部分：暂停场 + 范围加速。
 * <p>
 * 生物与玩家分开处理，原因见 {@link SwordAreaFields} 的类注释：
 * 生物直接取消 tick，玩家只能把位置钉回去（移动是网络包驱动的，不在 tick 里）。
 */
public final class SwordAreaFieldHandler {

    /**
     * 每服务器 tick 做三件事：
     * <ol>
     *   <li>刷新放置记录缓存 —— 查询（每个方块实体、每个生物每 tick 都会调）只扫缓存，不碰 SavedData；</li>
     *   <li>推进范围加速；</li>
     *   <li>把最新的表推给所有客户端，菜单第二页才有东西可显示。</li>
     * </ol>
     */
    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        SwordAreaFields.refreshCache(server);
        SwordAreaFields.tickAreaAcceleration(server);

        // 每 20 tick 推一次就够：表很小，但没必要每 tick 都发。
        if (server.getTickCount() % 20 == 0) {
            PlacementSyncPayload.broadcast(server);
        }
    }

    /** 服务器停了就清干净，免得状态跨局残留。 */
    @SubscribeEvent
    public void onServerStopped(ServerStoppedEvent event) {
        SwordAreaFields.clearRuntime();
    }

    /**
     * 生物 / 掉落物 / 抛射物：落在暂停场里就整 tick 跳过。
     * <p>
     * 玩家不在这里取消 —— 取消玩家 tick 既挡不住移动，又会连带跳过背包与连接维护。
     */
    @SubscribeEvent
    public void onEntityTick(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            return;
        }
        if (SwordAreaFields.isPaused(entity)) {
            event.setCanceled(true);
        }
    }

    /**
     * 玩家：被冻住时把位置钉回进场的瞬间。
     * <p>
     * 只改位置不够 —— 必须同时清速度并标记 {@code hurtMarked}，
     * 否则客户端会继续按自己的预测往前跑，表现为"能走但一直被拉回来"的抖动。
     */
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        UUID id = player.getUUID();
        if (!SwordAreaFields.shouldFreeze(player)) {
            if (SwordAreaFields.pinOf(id) != null) {
                SwordAreaFields.unpin(id);
            }
            return;
        }
        Vec3 pin = SwordAreaFields.pinOf(id);
        if (pin == null) {
            SwordAreaFields.pin(id, player.position());
            return;
        }
        if (player.position().distanceToSqr(pin) > 1.0E-4D) {
            player.connection.teleport(pin.x, pin.y, pin.z, player.getYRot(), player.getXRot());
            player.setDeltaMovement(Vec3.ZERO);
            player.hurtMarked = true;
        }
    }
}