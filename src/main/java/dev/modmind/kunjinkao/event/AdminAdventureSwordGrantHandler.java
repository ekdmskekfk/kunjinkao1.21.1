package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.SwordRegistry;
import dev.modmind.kunjinkao.config.AdminToolConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** 白名单玩家进入冒险模式时，服务端自动补发一把管理员剑。 */
public final class AdminAdventureSwordGrantHandler {
    private final Map<UUID, Boolean> wasInAdventure = new HashMap<>();

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()
                || !(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        boolean inAdventure = player.gameMode.getGameModeForPlayer() == GameType.ADVENTURE;
        if (inAdventure && !wasInAdventure.getOrDefault(player.getUUID(), false)) {
            grantIfAuthorized(player);
        }
        wasInAdventure.put(player.getUUID(), inAdventure);
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        wasInAdventure.remove(event.getEntity().getUUID());
    }

    public static void grantIfAuthorized(ServerPlayer player) {
        if (!AdminToolConfig.isAuthorized(player.getUUID())
                || KunJinKaoProtectionHandler.hasSwordInInventory(player)) {
            return;
        }
        ItemStack sword = new ItemStack(SwordRegistry.KUN_JIN_KAO_SWORD.get());
        if (!player.getInventory().add(sword)) {
            player.drop(sword, false);
        }
        player.containerMenu.broadcastChanges();
        player.displayClientMessage(Component.literal("§b已发放管理员剑：冒险模式授权生效"), true);
    }
}