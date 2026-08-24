package dev.modmind.kunjinkao.network;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public final class NetworkHandler {

    private NetworkHandler() {
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("4");
        registrar.playToServer(ToggleDisguisePayload.TYPE, ToggleDisguisePayload.STREAM_CODEC, ToggleDisguisePayload::handle);
        registrar.playToServer(ToggleOverwritePayload.TYPE, ToggleOverwritePayload.STREAM_CODEC, ToggleOverwritePayload::handle);
        registrar.playToServer(ToggleThemePayload.TYPE, ToggleThemePayload.STREAM_CODEC, ToggleThemePayload::handle);
        registrar.playToClient(OverwriteEffectPayload.TYPE, OverwriteEffectPayload.STREAM_CODEC, OverwriteEffectPayload::handle);
        registrar.playToClient(TimeAcceleratorOpenPayload.TYPE, TimeAcceleratorOpenPayload.STREAM_CODEC, TimeAcceleratorOpenPayload::handle);
        registrar.playToServer(TimeAcceleratorConfigPayload.TYPE, TimeAcceleratorConfigPayload.STREAM_CODEC, TimeAcceleratorConfigPayload::handle);
        registrar.playToServer(TacticalHudPayloads.ToggleHudPayload.TYPE, TacticalHudPayloads.ToggleHudPayload.STREAM_CODEC, TacticalHudPayloads.ToggleHudPayload::handle);
        registrar.playToClient(TacticalHudPayloads.HudStatePayload.TYPE, TacticalHudPayloads.HudStatePayload.STREAM_CODEC, TacticalHudPayloads.HudStatePayload::handle);
        registrar.playToServer(TacticalHudPayloads.ToggleNightVisionPayload.TYPE, TacticalHudPayloads.ToggleNightVisionPayload.STREAM_CODEC, TacticalHudPayloads.ToggleNightVisionPayload::handle);
        registrar.playToServer(TacticalHudPayloads.ToggleInvisibilityPayload.TYPE, TacticalHudPayloads.ToggleInvisibilityPayload.STREAM_CODEC, TacticalHudPayloads.ToggleInvisibilityPayload::handle);
        registrar.playToClient(TacticalHudPayloads.InvisibilityStatePayload.TYPE, TacticalHudPayloads.InvisibilityStatePayload.STREAM_CODEC, TacticalHudPayloads.InvisibilityStatePayload::handle);
        registrar.playToServer(TacticalHudPayloads.RequestEntityListPayload.TYPE, TacticalHudPayloads.RequestEntityListPayload.STREAM_CODEC, TacticalHudPayloads.RequestEntityListPayload::handle);
        registrar.playToClient(TacticalHudPayloads.EntityListPayload.TYPE, TacticalHudPayloads.EntityListPayload.STREAM_CODEC, TacticalHudPayloads.EntityListPayload::handle);
        registrar.playToServer(TacticalHudPayloads.ManageEntityPayload.TYPE, TacticalHudPayloads.ManageEntityPayload.STREAM_CODEC, TacticalHudPayloads.ManageEntityPayload::handle);
        registrar.playToClient(TacticalHudPayloads.EntityActionResultPayload.TYPE, TacticalHudPayloads.EntityActionResultPayload.STREAM_CODEC, TacticalHudPayloads.EntityActionResultPayload::handle);
        registrar.playToClient(ShieldHitPayload.TYPE, ShieldHitPayload.STREAM_CODEC, ShieldHitPayload::handle);
    }

    public static void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    public static void sendToPlayer(net.minecraft.world.entity.player.Player player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer((net.minecraft.server.level.ServerPlayer) player, payload);
    }
}
