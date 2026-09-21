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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    static {
        ClientHooks.registerClientHandlers(ClientPayloadHandlers::handle, ClientBlockScreens::openAcceleratorScreen);
        // 显式注册，而不是依赖 @EventBusSubscriber 对"嵌套类"的扫描：
        // 这个订阅负责客户端静态状态的生命周期清理，万一没被扫到就会静默失效（状态永不清理）。
        NeoForge.EVENT_BUS.register(ClientStateCleanup.class);
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

    /**
     * 客户端静态状态的生命周期清理入口。
     *
     * <p>本模组的客户端状态全部是进程级 static 字段（State/Visual 工具类），断线、退出世界、
     * 单机退回主菜单时都不会自动归零；而多处状态以 {@code level.getGameTime()} 或上个存档的
     * UUID 集合为基准，跨存档串味会产生可见错误（例如 ShieldHitVisualState 的护盾定格）。
     * 因此在这里统一清空，而不是让每个读取点各自兜底。</p>
     *
     * <p>NeoForge 21.1 的 {@code ClientPlayerNetworkEvent.LoggingOut} 挂在游戏总线
     * （{@code NeoForge.EVENT_BUS}）上，而外层类的 {@code @EventBusSubscriber} 声明的是 MOD 总线，
     * 所以放在这个客户端专用的嵌套类里，并由外层 static 块显式注册
     * （不依赖注解扫描，避免嵌套类万一没被扫到导致清理静默失效）。</p>
     */
    public static final class ClientStateCleanup {

        private ClientStateCleanup() {
        }

        @SubscribeEvent
        public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
            ClientHudState.reset();
            AdminEyeVisualState.reset();
            TacticalHudInvisibilityVisualState.reset();
            ShieldHitVisualState.reset();
            RemoteSwordDrawVisualState.clear();
            KunJinKaoClientOverwriteEffects.reset();
            KunJinKaoClientSwordVisuals.clear();
        }
    }
}
