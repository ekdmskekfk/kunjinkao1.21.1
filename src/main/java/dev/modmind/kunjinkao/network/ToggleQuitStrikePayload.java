package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleQuitStrikePayload(InteractionHand hand, boolean enabled) implements CustomPacketPayload {

    public static final Type<ToggleQuitStrikePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "toggle_quit_strike"));

    public static final StreamCodec<FriendlyByteBuf, ToggleQuitStrikePayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeEnum(p.hand); buf.writeBoolean(p.enabled); },
                    buf -> new ToggleQuitStrikePayload(NetworkHandler.readEnumSafe(buf, InteractionHand.class), buf.readBoolean()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ToggleQuitStrikePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> dev.modmind.kunjinkao.event.KunJinKaoColdDataEffectsHandler.handleToggleQuitStrike(
                context.player(), payload.hand, payload.enabled));
    }
}