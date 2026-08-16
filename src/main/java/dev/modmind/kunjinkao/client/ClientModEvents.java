package dev.modmind.kunjinkao.client;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.SwordRegistry;
import dev.modmind.kunjinkao.entity.DiamondProjectile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

public class ClientModEvents {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // DiamondProjectile 使用默认投掷物渲染器；不注册则世界渲染时 EntityRenderDispatcher 拿到 null 渲染器导致 NPE 崩溃
        event.registerEntityRenderer((EntityType<DiamondProjectile>)(EntityType<?>) SwordRegistry.DIAMOND_PROJECTILE.value(), ThrownItemRenderer::new);
    }

    @SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> 0xFFFFFF, SwordRegistry.KUN_JIN_KAO_SWORD.value());
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
                ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "overwrite_hud"),
                (guiGraphics, deltaTracker) -> {
                    Minecraft mc = Minecraft.getInstance();
                    KunJinKaoOverwriteHudOverlay.render(
                            guiGraphics, 0.0F,
                            mc.getWindow().getGuiScaledWidth(),
                            mc.getWindow().getGuiScaledHeight());
                });
    }
}
