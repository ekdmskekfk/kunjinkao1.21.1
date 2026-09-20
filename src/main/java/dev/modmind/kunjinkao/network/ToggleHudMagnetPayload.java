package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleHudMagnetPayload() implements CustomPacketPayload {

    public static final Type<ToggleHudMagnetPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "toggle_hud_magnet"));

    public static final StreamCodec<FriendlyByteBuf, ToggleHudMagnetPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> {}, buf -> new ToggleHudMagnetPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ToggleHudMagnetPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> dev.modmind.kunjinkao.event.KunJinKaoColdDataEffectsHandler.handleToggleHudMagnet(
                net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer(), context.player()));
    }
}