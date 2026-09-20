package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetSwordLootingModePayload(InteractionHand hand, int mode) implements CustomPacketPayload {

    public static final Type<SetSwordLootingModePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "set_sword_looting_mode"));

    public static final StreamCodec<FriendlyByteBuf, SetSwordLootingModePayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeEnum(p.hand); buf.writeInt(p.mode); },
                    buf -> new SetSwordLootingModePayload(buf.readEnum(InteractionHand.class), buf.readInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(SetSwordLootingModePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> dev.modmind.kunjinkao.event.KunJinKaoColdDataEffectsHandler.handleSetSwordLootingMode(
                context.player(), payload.hand, payload.mode));
    }
}