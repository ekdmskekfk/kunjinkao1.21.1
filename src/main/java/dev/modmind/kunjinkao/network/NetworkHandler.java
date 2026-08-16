package dev.modmind.kunjinkao.network;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public final class NetworkHandler {

    private NetworkHandler() {
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(ToggleDisguisePayload.TYPE, ToggleDisguisePayload.STREAM_CODEC, ToggleDisguisePayload::handle);
        registrar.playToServer(ToggleOverwritePayload.TYPE, ToggleOverwritePayload.STREAM_CODEC, ToggleOverwritePayload::handle);
        registrar.playToServer(ToggleThemePayload.TYPE, ToggleThemePayload.STREAM_CODEC, ToggleThemePayload::handle);
        registrar.playToClient(OverwriteEffectPayload.TYPE, OverwriteEffectPayload.STREAM_CODEC, OverwriteEffectPayload::handle);
        registrar.playToClient(TimeAcceleratorOpenPayload.TYPE, TimeAcceleratorOpenPayload.STREAM_CODEC, TimeAcceleratorOpenPayload::handle);
        registrar.playToServer(TimeAcceleratorConfigPayload.TYPE, TimeAcceleratorConfigPayload.STREAM_CODEC, TimeAcceleratorConfigPayload::handle);
    }

    public static void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    public static void sendToPlayer(net.minecraft.world.entity.player.Player player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer((net.minecraft.server.level.ServerPlayer) player, payload);
    }
}
