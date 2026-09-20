package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.config.AdminToolConfig;
import dev.modmind.kunjinkao.network.AdminEyeStatePayload;
import dev.modmind.kunjinkao.network.NetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;

/** Synchronizes whitelist display state to every connected client. */
public final class AdminEyeSyncHandler {
    private AdminEyeSyncHandler() {
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer joining)) {
            return;
        }
        for (ServerPlayer player : joining.server.getPlayerList().getPlayers()) {
            NetworkHandler.sendToPlayer(joining,
                    new AdminEyeStatePayload(player.getUUID(), AdminToolConfig.isAuthorized(player.getUUID())));
        }
        NetworkHandler.sendToAll(
                new AdminEyeStatePayload(joining.getUUID(), AdminToolConfig.isAuthorized(joining.getUUID())));
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer leaving) {
            NetworkHandler.sendToAll(new AdminEyeStatePayload(leaving.getUUID(), false));
        }
    }
}
