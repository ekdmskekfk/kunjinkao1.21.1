package dev.modmind.kunjinkao;

import dev.modmind.kunjinkao.config.AdminToolConfig;
import dev.modmind.kunjinkao.event.AdminAdventureSwordGrantHandler;
import dev.modmind.kunjinkao.event.AdminCommandHandler;
import dev.modmind.kunjinkao.event.AdminEyeSyncHandler;
import dev.modmind.kunjinkao.event.AdminSwordCraftingHandler;
import dev.modmind.kunjinkao.event.CommandProtectedSwordHandler;
import dev.modmind.kunjinkao.event.KunJinKaoColdDataEffectsHandler;
import dev.modmind.kunjinkao.event.KunJinKaoDeathEventHandler;
import dev.modmind.kunjinkao.event.KunJinKaoOreDropHandler;
import dev.modmind.kunjinkao.event.KunJinKaoProtectionHandler;
import dev.modmind.kunjinkao.event.KunJinKaoTooltipHandler;
import dev.modmind.kunjinkao.event.KunJinKaoExtraLootHandler;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 锟斤拷烫烫烫 - 管理员剑 / 战术 HUD / 加速器 / 密码权限 等
 *  移植自 Forge 1.20.1,运行在 NeoForge 1.21.1。
 */
@Mod(KunJinKaoEntry.MOD_ID)
public final class KunJinKaoEntry {

    public static final String MOD_ID = "kunjinkao";
    private static final Logger LOGGER = LogManager.getLogger("KunJinKao");

    public KunJinKaoEntry(IEventBus modEventBus, ModContainer container) {
        modEventBus.addListener(NetworkHandler::register);
        SwordRegistry.register(modEventBus);
        AcceleratorRegistry.register(modEventBus);
        WorldGateRegistry.register(modEventBus);
        dev.modmind.kunjinkao.sound.KunJinKaoSounds.register(modEventBus);
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
        NeoForge.EVENT_BUS.register(new KunJinKaoExtraLootHandler());
        NeoForge.EVENT_BUS.register(new KunJinKaoOreDropHandler());
        NeoForge.EVENT_BUS.register(new dev.modmind.kunjinkao.event.KunJinKaoAutoSmeltHandler());
        NeoForge.EVENT_BUS.register(new dev.modmind.kunjinkao.event.SwordAutoPickupHandler());
        NeoForge.EVENT_BUS.register(new dev.modmind.kunjinkao.event.SwordAreaFieldHandler());
        NeoForge.EVENT_BUS.register(new dev.modmind.kunjinkao.event.SwordMiningPenaltyHandler());
        NeoForge.EVENT_BUS.register(new KunJinKaoColdDataEffectsHandler());
        NeoForge.EVENT_BUS.register(new dev.modmind.kunjinkao.event.KunJinKaoQolHandler());
        // 选区作业（127）的框选：木棍右键设角点。与上面那个面板处理器分开注册，
        // 是因为它只订阅交互事件、也不参与面板的任何判定。
        NeoForge.EVENT_BUS.register(new dev.modmind.kunjinkao.event.ZoneSelectionHandler());
        // 经验飞涨原来在这里注册了一个独立处理器（击杀经验倍率）。改成「每 tick 加一级」之后
        // 它变成挂在玩家 tick 上的被动效果，已经并进 KunJinKaoQolHandler，处理器本身删掉了。
        NeoForge.EVENT_BUS.register(new dev.modmind.kunjinkao.event.KunJinKaoAnvilHandler());
        NeoForge.EVENT_BUS.register(new dev.modmind.kunjinkao.event.KunJinKaoTradeHandler());
        NeoForge.EVENT_BUS.register(AdminSwordCraftingHandler.class);
        NeoForge.EVENT_BUS.register(CommandProtectedSwordHandler.class);
        NeoForge.EVENT_BUS.register(AdminEyeSyncHandler.class);
        NeoForge.EVENT_BUS.register(AdminCommandHandler.class);


        LOGGER.info("[Kunjinkao] NeoForge 1.21.1 锟斤拷烫烫烫 initialized");
    }

}