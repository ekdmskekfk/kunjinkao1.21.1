package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.entity.DiamondProjectile;
import dev.modmind.kunjinkao.overwrite.KunJinKaoOverwriteHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class KunJinKaoProtectionHandler {

    public static final String KILL_BY_OVERWRITE_KEY = "KunJinKaoKillByOverwrite";
    private static final double VOID_RESPAWN_Y = 320.0D;

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide() || player.isSpectator()) return;
        boolean hasSword = hasSwordInInventory(player);
        var abilities = player.getAbilities();
        if (hasSword) {
            if (!abilities.mayfly) { abilities.mayfly = true; player.onUpdateAbilities(); }
            if (player.getY() < -64.0D) player.teleportTo(player.getX(), VOID_RESPAWN_Y, player.getZ());
        } else if (abilities.mayfly && !player.isCreative()) {
            abilities.mayfly = false; abilities.flying = false; player.onUpdateAbilities();
        }
        CompoundTag data = player.getPersistentData();
        if (data.getBoolean(KILL_BY_OVERWRITE_KEY) && player.getHealth() > 0.0F) {
            data.remove(KILL_BY_OVERWRITE_KEY);
            data.remove(KunJinKaoDeathEventHandler.MARK_KEY);
            data.remove(KunJinKaoDeathEventHandler.LOOTING_MODE_ENTITY_KEY);
        }
    }

    @SubscribeEvent
    public void onLivingAttack(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        Entity direct = event.getSource().getDirectEntity();
        if (direct instanceof DiamondProjectile) return;
        if (!(direct instanceof LivingEntity livingAttacker) || !isHoldingKunJinKaoSword(livingAttacker)) {
            if (target instanceof Player player && hasSwordInInventory(player)) event.setCanceled(true);
            return;
        }
        ItemStack sword = livingAttacker.getMainHandItem();
        if (KunJinKaoSwordItem.isOverwriteEnabled(sword)) {
            event.setCanceled(true);
            if (!target.level().isClientSide() && target.level() instanceof ServerLevel serverLevel) {
                KunJinKaoOverwriteHandler.startOverwrite(livingAttacker, target, sword, serverLevel);
            }
            return;
        }
        KunJinKaoSwordItem.applyKunJinKaoMark(target, sword);
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            boolean overwriteKill = player.getPersistentData().getBoolean(KILL_BY_OVERWRITE_KEY);
            if (overwriteKill) { player.getPersistentData().remove(KILL_BY_OVERWRITE_KEY); return; }
            if (hasSwordInInventory(player)) {
                event.setCanceled(true);
                player.setHealth(player.getMaxHealth());
                player.setRemainingFireTicks(0);
            }
        }
    }

    public static boolean hasSwordInInventory(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof KunJinKaoSwordItem && !KunJinKaoSwordItem.isDisguised(stack)) return true;
        }
        for (ItemStack stack : player.getInventory().offhand) {
            if (stack.getItem() instanceof KunJinKaoSwordItem && !KunJinKaoSwordItem.isDisguised(stack)) return true;
        }
        return false;
    }

    private static boolean isHoldingKunJinKaoSword(LivingEntity living) {
        ItemStack held = living.getMainHandItem();
        return held.getItem() instanceof KunJinKaoSwordItem && !KunJinKaoSwordItem.isDisguised(held);
    }
}
