package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RequestHudEntityListPayload() implements CustomPacketPayload {

    public static final Type<RequestHudEntityListPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "request_hud_entity_list"));

    public static final StreamCodec<FriendlyByteBuf, RequestHudEntityListPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> {}, buf -> new RequestHudEntityListPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(RequestHudEntityListPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> dev.modmind.kunjinkao.event.KunJinKaoColdDataEffectsHandler.handleRequestEntityList(
                net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer(), context.player()));
    }
}