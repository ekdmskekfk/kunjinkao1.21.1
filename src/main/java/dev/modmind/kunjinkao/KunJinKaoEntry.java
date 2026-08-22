package dev.modmind.kunjinkao;

import dev.modmind.kunjinkao.client.KunJinKaoClientSetup;
import dev.modmind.kunjinkao.client.KunJinKaoTooltipColorHandler;
import dev.modmind.kunjinkao.config.AdminToolConfig;
import dev.modmind.kunjinkao.config.TacticalHudConfig;
import dev.modmind.kunjinkao.event.KunJinKaoAdminCraftHandler;
import dev.modmind.kunjinkao.event.KunJinKaoDeathEventHandler;
import dev.modmind.kunjinkao.event.KunJinKaoProtectionHandler;
import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.overwrite.KunJinKaoOverwriteHandler;
import dev.modmind.kunjinkao.recipe.KunJinKaoRecipes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;

@Mod(KunJinKaoEntry.MOD_ID)
public final class KunJinKaoEntry {

    public static final String MOD_ID = "kunjinkao";

    public KunJinKaoEntry(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.SERVER, AdminToolConfig.SPEC, "kunjinkao-admin.toml");
        modContainer.registerConfig(ModConfig.Type.COMMON, TacticalHudConfig.SPEC, "tactical-hud-admin.toml");
        modEventBus.addListener(NetworkHandler::register);
        SwordRegistry.register(modEventBus);
        KunJinKaoRecipes.register(modEventBus);

        NeoForge.EVENT_BUS.register(new KunJinKaoDeathEventHandler());
        NeoForge.EVENT_BUS.register(new KunJinKaoProtectionHandler());
        NeoForge.EVENT_BUS.register(new KunJinKaoOverwriteHandler());
        NeoForge.EVENT_BUS.register(new KunJinKaoAdminCraftHandler());

        if (FMLLoader.getDist().isClient()) {
            NeoForge.EVENT_BUS.register(KunJinKaoTooltipColorHandler.class);
            KunJinKaoClientSetup.init(modEventBus);
        }
        System.out.println("[Kunjinkao] NeoForge 1.21.1 覆写·断未 初始化完成");
    }
}
