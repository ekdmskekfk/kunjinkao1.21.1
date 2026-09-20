package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.clientbridge.ClientHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record HudShieldHitPayload(UUID playerUuid, float relativeImpactYaw, float impactHeight) implements CustomPacketPayload {

    public static final Type<HudShieldHitPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "hud_shield_hit"));

    public static final StreamCodec<FriendlyByteBuf, HudShieldHitPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeUUID(p.playerUuid); buf.writeFloat(p.relativeImpactYaw); buf.writeFloat(p.impactHeight); },
                    buf -> new HudShieldHitPayload(buf.readUUID(), buf.readFloat(), buf.readFloat()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(HudShieldHitPayload payload, IPayloadContext context) {
        ClientHooks.handleClientPayload(payload, context);
    }
}
