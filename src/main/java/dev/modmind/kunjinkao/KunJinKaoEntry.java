package dev.modmind.kunjinkao;

import dev.modmind.kunjinkao.config.AdminToolConfig;
import dev.modmind.kunjinkao.event.AdminAdventureSwordGrantHandler;
import dev.modmind.kunjinkao.event.AdminEyeSyncHandler;
import dev.modmind.kunjinkao.event.AdminSwordCraftingHandler;
import dev.modmind.kunjinkao.event.CommandProtectedSwordHandler;
import dev.modmind.kunjinkao.event.KunJinKaoColdDataEffectsHandler;
import dev.modmind.kunjinkao.event.KunJinKaoDeathEventHandler;
import dev.modmind.kunjinkao.event.KunJinKaoOreDropHandler;
import dev.modmind.kunjinkao.event.KunJinKaoProtectionHandler;
import dev.modmind.kunjinkao.event.KunJinKaoTooltipHandler;
import dev.modmind.kunjinkao.event.KunJinKaoUnbreakableBlockHandler;
import dev.modmind.kunjinkao.event.SwordDrawAnimationSyncHandler;
import dev.modmind.kunjinkao.event.TacticalHudMagnetHandler;
import dev.modmind.kunjinkao.event.TacticalHudTrueInvisibilityHandler;
import dev.modmind.kunjinkao.event.UltimateDeathHandler;
import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.overwrite.KunJinKaoOverwriteHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

/**
 * 锟斤拷烫烫烫 - 管理员剑 / 战术 HUD / 加速器 / 密码权限 等
 *  移植自 Forge 1.20.1,运行在 NeoForge 1.21.1。
 */
@Mod(KunJinKaoEntry.MOD_ID)
public final class KunJinKaoEntry {

    public static final String MOD_ID = "kunjinkao";

    public KunJinKaoEntry(IEventBus modEventBus, ModContainer container) {
        modEventBus.addListener(NetworkHandler::register);
        SwordRegistry.register(modEventBus);
        AcceleratorRegistry.register(modEventBus);
        WorldGateRegistry.register(modEventBus);
        container.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON,
                AdminToolConfig.COMMON_SPEC, "kunjinkao-admin.toml");

        NeoForge.EVENT_BUS.register(new KunJinKaoDeathEventHandler());
        NeoForge.EVENT_BUS.register(new KunJinKaoTooltipHandler());
        NeoForge.EVENT_BUS.register(new KunJinKaoProtectionHandler());
        NeoForge.EVENT_BUS.register(new KunJinKaoOverwriteHandler());
        NeoForge.EVENT_BUS.register(new TacticalHudTrueInvisibilityHandler());
        NeoForge.EVENT_BUS.register(new TacticalHudMagnetHandler());
        NeoForge.EVENT_BUS.register(new SwordDrawAnimationSyncHandler());
        NeoForge.EVENT_BUS.register(new UltimateDeathHandler());
        NeoForge.EVENT_BUS.register(new AdminAdventureSwordGrantHandler());
        NeoForge.EVENT_BUS.register(new KunJinKaoUnbreakableBlockHandler());
        NeoForge.EVENT_BUS.register(new KunJinKaoOreDropHandler());
        NeoForge.EVENT_BUS.register(new KunJinKaoColdDataEffectsHandler());
        NeoForge.EVENT_BUS.register(AdminSwordCraftingHandler.class);
        NeoForge.EVENT_BUS.register(CommandProtectedSwordHandler.class);
        NeoForge.EVENT_BUS.register(AdminEyeSyncHandler.class);


        System.out.println("[Kunjinkao] NeoForge 1.21.1 锟斤拷烫烫烫 initialized");
    }

}