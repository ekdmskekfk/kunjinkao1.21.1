package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.network.SwordDrawAnimationPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** 服务端检测“未持剑→持剑”的切换，并同步第三人称动画给跟踪者。 */
public final class SwordDrawAnimationSyncHandler {
    private final Map<UUID, Boolean> wasHolding = new HashMap<>();

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()
                || !(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        InteractionHand hand = findVisibleSwordHand(player);
        boolean holding = hand != null;
        boolean previouslyHolding = wasHolding.getOrDefault(player.getUUID(), false);
        if (holding && !previouslyHolding) {
            NetworkHandler.sendToAllTracking(player,
                    new SwordDrawAnimationPayload(player.getUUID(), hand));
        }
        wasHolding.put(player.getUUID(), holding);
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        wasHolding.remove(event.getEntity().getUUID());
    }

    private static InteractionHand findVisibleSwordHand(ServerPlayer player) {
        if (isVisibleSword(player.getMainHandItem())) {
            return InteractionHand.MAIN_HAND;
        }
        return isVisibleSword(player.getOffhandItem()) ? InteractionHand.OFF_HAND : null;
    }

    private static boolean isVisibleSword(ItemStack stack) {
        return stack.getItem() instanceof KunJinKaoSwordItem && !KunJinKaoSwordItem.isDisguised(stack);
    }
}
