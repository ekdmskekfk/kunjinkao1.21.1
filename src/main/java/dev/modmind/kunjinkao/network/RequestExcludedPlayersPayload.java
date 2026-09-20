package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RequestExcludedPlayersPayload() implements CustomPacketPayload {

    public static final Type<RequestExcludedPlayersPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "request_excluded_players"));

    public static final StreamCodec<FriendlyByteBuf, RequestExcludedPlayersPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> {}, buf -> new RequestExcludedPlayersPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(RequestExcludedPlayersPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> dev.modmind.kunjinkao.event.KunJinKaoColdDataEffectsHandler.handleRequestExcludedPlayers(
                net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer(), context.player()));
    }
}