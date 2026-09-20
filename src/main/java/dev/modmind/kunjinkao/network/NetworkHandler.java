package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class NetworkHandler {

    public static final String PROTOCOL_VERSION = "19";

    private NetworkHandler() {
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar reg = event.registrar(PROTOCOL_VERSION);
        reg.playToServer(ToggleDisguisePayload.TYPE, ToggleDisguisePayload.STREAM_CODEC, ToggleDisguisePayload::handle);
        reg.playToClient(OverwriteEffectPayload.TYPE, OverwriteEffectPayload.STREAM_CODEC, OverwriteEffectPayload::handle);
        reg.playToServer(ToggleOverwritePayload.TYPE, ToggleOverwritePayload.STREAM_CODEC, ToggleOverwritePayload::handle);
        reg.playToServer(ToggleThemePayload.TYPE, ToggleThemePayload.STREAM_CODEC, ToggleThemePayload::handle);
        reg.playToServer(AcceleratorConfigPayload.TYPE, AcceleratorConfigPayload.STREAM_CODEC, AcceleratorConfigPayload::handle);
        reg.playToServer(AcceleratorShowRangePayload.TYPE, AcceleratorShowRangePayload.STREAM_CODEC, AcceleratorShowRangePayload::handle);
        reg.playToServer(ToggleTacticalHudPayload.TYPE, ToggleTacticalHudPayload.STREAM_CODEC, ToggleTacticalHudPayload::handle);
        reg.playToClient(TacticalHudStatePayload.TYPE, TacticalHudStatePayload.STREAM_CODEC, TacticalHudStatePayload::handle);
        reg.playToServer(ToggleHudNightVisionPayload.TYPE, ToggleHudNightVisionPayload.STREAM_CODEC, ToggleHudNightVisionPayload::handle);
        reg.playToClient(HudNightVisionStatePayload.TYPE, HudNightVisionStatePayload.STREAM_CODEC, HudNightVisionStatePayload::handle);
        reg.playToServer(RequestHudEntityListPayload.TYPE, RequestHudEntityListPayload.STREAM_CODEC, RequestHudEntityListPayload::handle);
        reg.playToClient(HudEntityListPayload.TYPE, HudEntityListPayload.STREAM_CODEC, HudEntityListPayload::handle);
        reg.playToServer(ManageHudEntityPayload.TYPE, ManageHudEntityPayload.STREAM_CODEC, ManageHudEntityPayload::handle);
        reg.playToClient(HudEntityActionResultPayload.TYPE, HudEntityActionResultPayload.STREAM_CODEC, HudEntityActionResultPayload::handle);
        reg.playToServer(ToggleHudTrueInvisibilityPayload.TYPE, ToggleHudTrueInvisibilityPayload.STREAM_CODEC, ToggleHudTrueInvisibilityPayload::handle);
        reg.playToClient(HudTrueInvisibilityStatePayload.TYPE, HudTrueInvisibilityStatePayload.STREAM_CODEC, HudTrueInvisibilityStatePayload::handle);
        reg.playToClient(HudTrueInvisibilityVisualPayload.TYPE, HudTrueInvisibilityVisualPayload.STREAM_CODEC, HudTrueInvisibilityVisualPayload::handle);
        reg.playToClient(HudShieldHitPayload.TYPE, HudShieldHitPayload.STREAM_CODEC, HudShieldHitPayload::handle);
        reg.playToServer(ToggleBlueScreenAttackPayload.TYPE, ToggleBlueScreenAttackPayload.STREAM_CODEC, ToggleBlueScreenAttackPayload::handle);
        reg.playToServer(SetSwordMiningSpeedPayload.TYPE, SetSwordMiningSpeedPayload.STREAM_CODEC, SetSwordMiningSpeedPayload::handle);
        reg.playToServer(ToggleUnbreakableBlockBreakingPayload.TYPE, ToggleUnbreakableBlockBreakingPayload.STREAM_CODEC, ToggleUnbreakableBlockBreakingPayload::handle);
        reg.playToServer(SetAreaClearTargetModePayload.TYPE, SetAreaClearTargetModePayload.STREAM_CODEC, SetAreaClearTargetModePayload::handle);
        reg.playToServer(SetSwordAttackDamageLimitPayload.TYPE, SetSwordAttackDamageLimitPayload.STREAM_CODEC, SetSwordAttackDamageLimitPayload::handle);
        reg.playToClient(AdminEyeStatePayload.TYPE, AdminEyeStatePayload.STREAM_CODEC, AdminEyeStatePayload::handle);
        reg.playToServer(SetSwordOreDropMultiplierPayload.TYPE, SetSwordOreDropMultiplierPayload.STREAM_CODEC, SetSwordOreDropMultiplierPayload::handle);
        reg.playToServer(SetSwordLootingModePayload.TYPE, SetSwordLootingModePayload.STREAM_CODEC, SetSwordLootingModePayload::handle);
        reg.playToServer(ToggleHudMagnetPayload.TYPE, ToggleHudMagnetPayload.STREAM_CODEC, ToggleHudMagnetPayload::handle);
        reg.playToClient(HudMagnetStatePayload.TYPE, HudMagnetStatePayload.STREAM_CODEC, HudMagnetStatePayload::handle);
        reg.playToClient(SwordDrawAnimationPayload.TYPE, SwordDrawAnimationPayload.STREAM_CODEC, SwordDrawAnimationPayload::handle);
        reg.playToServer(SubmitAdminPasswordPayload.TYPE, SubmitAdminPasswordPayload.STREAM_CODEC, SubmitAdminPasswordPayload::handle);
        reg.playToClient(AdminPasswordResultPayload.TYPE, AdminPasswordResultPayload.STREAM_CODEC, AdminPasswordResultPayload::handle);
        reg.playToServer(ToggleUltimateDeathPayload.TYPE, ToggleUltimateDeathPayload.STREAM_CODEC, ToggleUltimateDeathPayload::handle);
        reg.playToServer(ToggleQuitStrikePayload.TYPE, ToggleQuitStrikePayload.STREAM_CODEC, ToggleQuitStrikePayload::handle);
        reg.playToServer(RequestExcludedPlayersPayload.TYPE, RequestExcludedPlayersPayload.STREAM_CODEC, RequestExcludedPlayersPayload::handle);
        reg.playToClient(ExcludedPlayersPayload.TYPE, ExcludedPlayersPayload.STREAM_CODEC, ExcludedPlayersPayload::handle);
        reg.playToServer(ManageExcludedPlayerPayload.TYPE, ManageExcludedPlayerPayload.STREAM_CODEC, ManageExcludedPlayerPayload::handle);
    }

    public static void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    public static void sendToAll(CustomPacketPayload payload) {
        PacketDistributor.sendToAllPlayers(payload);
    }

    public static void sendToAllTracking(ServerPlayer entity, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayersTrackingEntity(entity, payload);
    }

    public static String modId() {
        return KunJinKaoEntry.MOD_ID;
    }
}
