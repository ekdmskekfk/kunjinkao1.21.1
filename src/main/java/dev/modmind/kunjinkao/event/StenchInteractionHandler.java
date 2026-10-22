package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.SwordRegistry;
import dev.modmind.kunjinkao.entity.KunJinKaoEntities;
import dev.modmind.kunjinkao.entity.KunJinKaoHandEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * 与君君的两条交互：挤尿（玻璃瓶 → 尿液瓶）和「双君仪式」（尿液瓶 → 两只君君 → 生 50 只）。
 *
 * <p>仪式做成"先标记一只、再对第二只用"的两步式，而不是要求玩家同时点两只：
 * 那样需要额外的选择 UI，而且第二只稍微挪一下就会失败。标记有 30 秒时效、
 * 距离要求 16 格内，两个条件都写清楚，避免"很久以前标记的某只突然响应"。</p>
 */
public final class StenchInteractionHandler {

    /** 第二只君君必须离第一只多近。 */
    private static final double MARK_RANGE = 16.0D;
    /** 标记多久后失效（tick）。 */
    private static final long MARK_TIMEOUT_TICKS = 20L * 30L;
    /** 仪式一次生成多少只。 */
    private static final int RITUAL_SPAWN_COUNT = 50;

    private record Mark(net.minecraft.resources.ResourceKey<Level> dimension, Vec3 pos, long gameTime) {
    }

    /** 已被尿液瓶标记的君君。键是实体 UUID。 */
    private static final Map<UUID, Mark> MARKS = new HashMap<>();

    private StenchInteractionHandler() {
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof KunJinKaoHandEntity hand)) {
            return;
        }
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (stack.is(Items.GLASS_BOTTLE)) {
            if (!(hand.level() instanceof ServerLevel)) {
                return;
            }
            // 玻璃瓶 → 尿液瓶，1:1。
            stack.shrink(1);
            ItemStack urine = new ItemStack(SwordRegistry.URINE_BOTTLE.get());
            if (!player.getInventory().add(urine)) {
                player.drop(urine, false);
            }
            player.displayClientMessage(
                    Component.translatable("message.kunjinkao.urine_collected"), true);
            cancel(event);
            return;
        }

        if (stack.is(SwordRegistry.URINE_BOTTLE.get())) {
            if (!(hand.level() instanceof ServerLevel serverLevel)) {
                return;
            }
            handleUrineOnHand(serverLevel, player, stack, hand);
            cancel(event);
        }
    }

    private static void handleUrineOnHand(ServerLevel level, Player player, ItemStack stack,
                                          KunJinKaoHandEntity hand) {
        long now = level.getGameTime();
        UUID partner = findPartner(level, hand, now);

        if (partner == null) {
            MARKS.put(hand.getUUID(), new Mark(level.dimension(), hand.position(), now));
            player.displayClientMessage(
                    Component.translatable("message.kunjinkao.urine_marked_first"), true);
            spawnParticles(level, hand.position().add(0.0D, 0.4D, 0.0D), 12);
            consumeOne(player, stack);
            return;
        }

        // 找到配对了：两只都聚到中点，然后原地生 50 只。
        Mark mark = MARKS.remove(partner);
        MARKS.remove(hand.getUUID());
        net.minecraft.world.entity.Entity other = level.getEntity(partner);
        Vec3 mid = hand.position().add(0.0D, 0.0D, 0.0D);
        if (other != null) {
            mid = mid.add(other.position()).scale(0.5D);
        }
        hand.moveTo(mid.x, mid.y, mid.z, hand.getYRot(), hand.getXRot());
        if (other instanceof KunJinKaoHandEntity otherHand) {
            otherHand.moveTo(mid.x, mid.y, mid.z, otherHand.getYRot(), otherHand.getXRot());
        }
        // mark 只是为了让编译器知道配对记录被取走了；位置信息这里用不上。
        if (mark == null) {
            return;
        }

        spawnParticles(level, mid.add(0.0D, 0.6D, 0.0D), 80);
        for (int i = 0; i < RITUAL_SPAWN_COUNT; i++) {
            KunJinKaoHandEntity born = KunJinKaoEntities.HAND.get().create(level);
            if (born == null) {
                continue;
            }
            double dx = (level.random.nextDouble() - 0.5D) * 8.0D;
            double dz = (level.random.nextDouble() - 0.5D) * 8.0D;
            born.moveTo(mid.x + dx, mid.y, mid.z + dz,
                    level.random.nextFloat() * 360.0F, 0.0F);
            level.addFreshEntity(born);
        }
        level.playSound(null, mid.x, mid.y, mid.z,
                net.minecraft.sounds.SoundEvents.SLIME_SQUISH, net.minecraft.sounds.SoundSource.HOSTILE,
                1.0F, 0.6F);
        player.displayClientMessage(
                Component.translatable("message.kunjinkao.urine_ritual_done"), true);
        consumeOne(player, stack);
    }

    /** 找一只同样被标记、同维度、16 格内、且标记没过期的君君。 */
    private static UUID findPartner(ServerLevel level, KunJinKaoHandEntity hand, long now) {
        Iterator<Map.Entry<UUID, Mark>> it = MARKS.entrySet().iterator();
        UUID found = null;
        while (it.hasNext()) {
            Map.Entry<UUID, Mark> entry = it.next();
            Mark mark = entry.getValue();
            if (now - mark.gameTime() > MARK_TIMEOUT_TICKS) {
                it.remove();
                continue;
            }
            if (entry.getKey().equals(hand.getUUID())) {
                continue;
            }
            if (!mark.dimension().equals(level.dimension())) {
                continue;
            }
            if (mark.pos().distanceToSqr(hand.position()) > MARK_RANGE * MARK_RANGE) {
                continue;
            }
            found = entry.getKey();
        }
        return found;
    }

    private static void spawnParticles(ServerLevel level, Vec3 pos, int count) {
        level.sendParticles(ParticleTypes.FALLING_HONEY, pos.x, pos.y, pos.z, count, 0.6D, 0.4D, 0.6D, 0.02D);
    }

    private static void consumeOne(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
    }

    private static void cancel(PlayerInteractEvent.EntityInteract event) {
        event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide()));
        event.setCanceled(true);
    }
}
