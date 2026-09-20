package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.clientbridge.ClientHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record SwordDrawAnimationPayload(UUID playerUuid, InteractionHand hand) implements CustomPacketPayload {

    public static final Type<SwordDrawAnimationPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "sword_draw_animation"));

    public static final StreamCodec<FriendlyByteBuf, SwordDrawAnimationPayload> STREAM_CODEC =
            StreamCodec.of((buf, payload) -> {
                buf.writeUUID(payload.playerUuid);
                buf.writeEnum(payload.hand);
            }, buf -> new SwordDrawAnimationPayload(buf.readUUID(), buf.readEnum(InteractionHand.class)));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SwordDrawAnimationPayload payload, IPayloadContext context) {
        ClientHooks.handleClientPayload(payload, context);
    }
}
