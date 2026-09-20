package dev.modmind.kunjinkao.client.render;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.client.TacticalHudInvisibilityVisualState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

/** Hides armour and held items by suppressing rendering of true-invisible players. */
@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class TrueInvisibilityRenderHandler {

    private TrueInvisibilityRenderHandler() {
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        if (TacticalHudInvisibilityVisualState.isTrueInvisible(event.getEntity().getUUID())) {
            // PlayerRenderer includes armour and third-person held-item layers.
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && TacticalHudInvisibilityVisualState.isTrueInvisible(player.getUUID())) {
            // First-person hand rendering would otherwise leave the sword visible.
            event.setCanceled(true);
        }
    }
}
