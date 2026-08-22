package dev.modmind.kunjinkao.client;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.ModelEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KunJinKaoClientSetup {

    private static final Logger LOGGER = LoggerFactory.getLogger(KunJinKaoClientSetup.class);

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(KunJinKaoKeyBindings::registerKeys);
        modEventBus.addListener(TacticalHudKeyMappings::register);
        modEventBus.register(ClientModEvents.class);
        KunJinKaoClientEvents.register();
        NeoForge.EVENT_BUS.addListener(TacticalHudClientEvents::onClientTick);

        // 启动诊断：记录剑模型与纹理图集状态（在每次会话均输出，用于定位渲染环境差异）
        modEventBus.addListener(ModelEvent.BakingCompleted.class, event -> {
            ModelManager mm = event.getModelManager();
            try {
                BakedModel model = mm.getModel(ModelResourceLocation.inventory(ResourceLocation.parse("kunjinkao:kun_jin_kao")));
                TextureAtlas atlas = mm.getAtlas(TextureAtlas.LOCATION_BLOCKS);
                boolean texInAtlas = atlas.getTextures().containsKey(ResourceLocation.parse("kunjinkao:item/kun_jin_kao"));
                boolean vanillaInAtlas = atlas.getTextures().containsKey(ResourceLocation.parse("minecraft:item/diamond_sword"));
                LOGGER.info("[KunJinKao] 剑渲染状态: modelClass={}, customRenderer={}, 自定义纹理在图集={}, 钻石剑纹理在图集={}",
                        model.getClass().getName(), model.isCustomRenderer(), texInAtlas, vanillaInAtlas);
            } catch (Exception e) {
                LOGGER.warn("[KunJinKao] 剑渲染状态检查失败: {}", e.toString());
            }
        });
    }
}
