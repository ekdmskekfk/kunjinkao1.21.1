package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetSwordMiningSpeedPayload(InteractionHand hand, int speed) implements CustomPacketPayload {

    public static final Type<SetSwordMiningSpeedPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "set_sword_mining_speed"));

    public static final StreamCodec<FriendlyByteBuf, SetSwordMiningSpeedPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeEnum(p.hand); buf.writeInt(p.speed); },
                    buf -> new SetSwordMiningSpeedPayload(buf.readEnum(InteractionHand.class), buf.readInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(SetSwordMiningSpeedPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> dev.modmind.kunjinkao.event.KunJinKaoColdDataEffectsHandler.handleSetSwordMiningSpeed(
                context.player(), payload.hand, payload.speed));
    }
}