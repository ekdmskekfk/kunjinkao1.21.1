package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleHudNightVisionPayload() implements CustomPacketPayload {

    public static final Type<ToggleHudNightVisionPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "toggle_hud_night_vision"));

    public static final StreamCodec<FriendlyByteBuf, ToggleHudNightVisionPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> {}, buf -> new ToggleHudNightVisionPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ToggleHudNightVisionPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> dev.modmind.kunjinkao.event.KunJinKaoColdDataEffectsHandler.handleToggleHudNightVision(
                net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer(), context.player()));
    }
}