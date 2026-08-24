package dev.modmind.kunjinkao.network;

import java.util.UUID;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Server-authoritative visual-only notification for a sword shield impact. */
public record ShieldHitPayload(UUID playerUuid, float relativeImpactYaw, float impactHeight) implements CustomPacketPayload {
    public static final Type<ShieldHitPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "shield_hit"));
    public static final StreamCodec<FriendlyByteBuf, ShieldHitPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> { buf.writeUUID(payload.playerUuid); buf.writeFloat(payload.relativeImpactYaw); buf.writeFloat(payload.impactHeight); },
            buf -> new ShieldHitPayload(buf.readUUID(), buf.readFloat(), buf.readFloat()));

    public static void handle(ShieldHitPayload payload, IPayloadContext context) {
        if (context.flow().isClientbound()) context.enqueueWork(() -> TacticalHudClientPayloadBridge.enqueue(payload));
    }

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
