package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.tactical.common.TacticalHudInvisibilityService;
import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.network.TacticalHudPayloads;
import dev.modmind.kunjinkao.network.TacticalHudServerHandlers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/** Prevents monsters from acquiring or retaining a tactical-invisible player as their target. */
public final class TacticalHudInvisibilityHandler {

    @SubscribeEvent
    public void onMonsterTargetChange(LivingChangeTargetEvent event) {
        if (event.getEntity() instanceof Monster && event.getNewAboutToBeSetTarget() instanceof ServerPlayer player
                && TacticalHudInvisibilityService.isActive(player)) {
            event.setNewAboutToBeSetTarget(null);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (TacticalHudInvisibilityService.clearExpiredMarker(player)) {
            TacticalHudServerHandlers.broadcastInvisibilityState(player, false);
        }
        if (!TacticalHudInvisibilityService.isActive(player)) return;
        for (var entity : player.serverLevel().getAllEntities()) {
            if (entity instanceof Monster monster && monster.getTarget() == player) {
                monster.setTarget(null);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer joiningPlayer)) return;
        for (ServerPlayer player : joiningPlayer.server.getPlayerList().getPlayers()) {
            if (TacticalHudInvisibilityService.isActive(player)) {
                NetworkHandler.sendToPlayer(joiningPlayer, new TacticalHudPayloads.InvisibilityStatePayload(player.getUUID(), true));
            }
        }
    }
}
