package dev.modmind.kunjinkao.client;

import dev.modmind.kunjinkao.AcceleratorRegistry;
import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.SwordRegistry;
import dev.modmind.kunjinkao.clientbridge.ClientHooks;
import dev.modmind.kunjinkao.client.render.AcceleratorBlockEntityRenderer;
import dev.modmind.kunjinkao.client.render.EyeHudLayer;
import dev.modmind.kunjinkao.client.render.HoneycombShieldLayer;
import dev.modmind.kunjinkao.client.render.KunJinKaoThirdPersonGrabLayer;
import dev.modmind.kunjinkao.client.hud.HudOverlayRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    static {
        ClientHooks.registerClientHandlers(ClientPayloadHandlers::handle, ClientBlockScreens::openAcceleratorScreen);
    }

    public static ResourceLocation compileModelLocation(int stage) {
        return ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "item/kun_jin_kao_compile_" + stage);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(
                SwordRegistry.KUN_JIN_KAO_SWORD.get(),
                ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "draw_compile"),
                (stack, level, entity, seed) -> KunJinKaoClientSwordVisuals.getDrawCompileModelStage(stack, entity)
        ));
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(SwordRegistry.DIAMOND_PROJECTILE.get(), ThrownItemRenderer::new);
        event.registerBlockEntityRenderer(AcceleratorRegistry.ACCELERATOR_BE.get(), AcceleratorBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        for (int stage = 0; stage < 8; stage++) {
            event.register(ModelResourceLocation.standalone(compileModelLocation(stage)));
        }
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KunJinKaoKeyBindings.TOGGLE_DISGUISE);
        event.register(KunJinKaoKeyBindings.TOGGLE_OVERWRITE);
        event.register(KunJinKaoKeyBindings.CYCLE_THEME);
        event.register(KunJinKaoKeyBindings.TOGGLE_TACTICAL_HUD);
        event.register(KunJinKaoKeyBindings.OPEN_SWORD_OPTIONS);
        event.register(KunJinKaoKeyBindings.OPEN_ADMIN_PASSWORD);
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "kun_jin_kao_overwrite"),
                (guiGraphics, deltaTracker) -> {
                    float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
                    int screenWidth = guiGraphics.guiWidth();
                    int screenHeight = guiGraphics.guiHeight();
                    KunJinKaoOverwriteHudOverlay.render(guiGraphics, partialTick, screenWidth, screenHeight);
                    KunJinKaoClientSwordVisuals.renderCompileModel(guiGraphics, screenWidth, screenHeight);
                    KunJinKaoClientSwordVisuals.renderAttackData(guiGraphics, screenWidth, screenHeight);
                });
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "tactical_eye_hud"),
                (guiGraphics, deltaTracker) -> {
                    HudOverlayRenderer.render(guiGraphics,
                            deltaTracker.getGameTimeDeltaPartialTick(false),
                            guiGraphics.guiWidth(), guiGraphics.guiHeight());
                });
    }

    @SubscribeEvent
    public static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        EyeHudLayer.addToPlayerRenderers(event);
        HoneycombShieldLayer.addToPlayerRenderers(event);
        KunJinKaoThirdPersonGrabLayer.addToPlayerRenderers(event);
    }
}
