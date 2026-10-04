package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.world.SwordPauseField;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.UUID;

/**
 * 暂停场的每 tick 执行部分。
 * <p>
 * 生物与玩家分开处理，原因见 {@link SwordPauseField}：
 * 生物直接取消 tick，玩家只能把位置钉回去。
 */
public final class SwordPauseHandler {

    /**
     * 生物 / 掉落物 / 抛射物：落在暂停场里就整 tick 跳过。
     * <p>
     * 玩家不在这里取消 —— 玩家的移动是网络包驱动的，不在 tick 里，
     * 取消 tick 既挡不住移动，又会连带跳过背包与连接维护。
     */
    @SubscribeEvent
    public void onEntityTick(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            return;
        }
        if (SwordPauseField.isPaused(entity)) {
            event.setCanceled(true);
        }
    }

    /**
     * 玩家：被冻住时把位置钉回进场的瞬间。
     * <p>
     * 只改位置不够 —— 必须同时把速度清零并标记 {@code hurtMarked}，
     * 否则客户端会继续按自己的预测往前跑，表现为"能走但一直被拉回来"的抖动。
     */
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        UUID id = player.getUUID();
        if (!SwordPauseField.shouldFreeze(player)) {
            if (SwordPauseField.pinOf(id) != null) {
                SwordPauseField.unpin(id);
            }
            return;
        }
        Vec3 pin = SwordPauseField.pinOf(id);
        if (pin == null) {
            // 刚进场：记下这一刻的位置，从下一 tick 开始钉。
            SwordPauseField.pin(id, player.position());
            return;
        }
        if (player.position().distanceToSqr(pin) > 1.0E-4D) {
            player.connection.teleport(pin.x, pin.y, pin.z, player.getYRot(), player.getXRot());
            player.setDeltaMovement(Vec3.ZERO);
            player.hurtMarked = true;
        }
    }
}