package dev.modmind.kunjinkao.client;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

/** Client-only render suppression hides the invisible player's armour and held sword. */
public final class TacticalHudInvisibilityClientEvents {

    private TacticalHudInvisibilityClientEvents() {
    }

    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        if (TacticalHudClientState.shouldHide(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    public static void onRenderHand(RenderHandEvent event) {
        if (Minecraft.getInstance().player != null && TacticalHudClientState.shouldHide(Minecraft.getInstance().player)) {
            event.setCanceled(true);
        }
    }
}
