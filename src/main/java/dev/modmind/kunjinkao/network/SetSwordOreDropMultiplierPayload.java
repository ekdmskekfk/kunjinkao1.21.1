package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetSwordOreDropMultiplierPayload(InteractionHand hand, int multiplier) implements CustomPacketPayload {

    public static final Type<SetSwordOreDropMultiplierPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "set_sword_ore_drop_multiplier"));

    public static final StreamCodec<FriendlyByteBuf, SetSwordOreDropMultiplierPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeEnum(p.hand); buf.writeInt(p.multiplier); },
                    buf -> new SetSwordOreDropMultiplierPayload(NetworkHandler.readEnumSafe(buf, InteractionHand.class), buf.readInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(SetSwordOreDropMultiplierPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> dev.modmind.kunjinkao.event.KunJinKaoColdDataEffectsHandler.handleSetSwordOreDropMultiplier(
                context.player(), payload.hand, payload.multiplier));
    }
}