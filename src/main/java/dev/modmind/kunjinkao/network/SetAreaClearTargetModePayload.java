package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetAreaClearTargetModePayload(InteractionHand hand, int modeId) implements CustomPacketPayload {

    public static final Type<SetAreaClearTargetModePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "set_area_clear_target_mode"));

    public static final StreamCodec<FriendlyByteBuf, SetAreaClearTargetModePayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeEnum(p.hand); buf.writeInt(p.modeId); },
                    buf -> new SetAreaClearTargetModePayload(buf.readEnum(InteractionHand.class), buf.readInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(SetAreaClearTargetModePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> dev.modmind.kunjinkao.event.KunJinKaoColdDataEffectsHandler.handleSetAreaClearTargetMode(
                context.player(), payload.hand, payload.modeId));
    }
}