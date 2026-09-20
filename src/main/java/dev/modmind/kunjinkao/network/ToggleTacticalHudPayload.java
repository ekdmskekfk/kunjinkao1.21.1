package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleTacticalHudPayload(boolean requestedEnabled) implements CustomPacketPayload {

    public static final Type<ToggleTacticalHudPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "toggle_tactical_hud"));

    public static final StreamCodec<FriendlyByteBuf, ToggleTacticalHudPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> buf.writeBoolean(p.requestedEnabled),
                    buf -> new ToggleTacticalHudPayload(buf.readBoolean()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ToggleTacticalHudPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> dev.modmind.kunjinkao.event.KunJinKaoColdDataEffectsHandler.handleToggleTacticalHudRequest(
                net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer(), context.player(), payload.requestedEnabled));
    }
}