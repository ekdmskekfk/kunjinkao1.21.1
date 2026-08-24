package dev.modmind.kunjinkao.client;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/** Registers the layer for both vanilla player model variants on the physical client. */
public final class HoneycombShieldRendererRegistration {
    private HoneycombShieldRendererRegistration() { }
    public static void register(EntityRenderersEvent.AddLayers event) {
        for (var skin : event.getSkins()) {
            PlayerRenderer renderer = event.getSkin(skin);
            if (renderer != null) renderer.addLayer(new HoneycombShieldLayer(renderer));
        }
    }
}
