package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.clientbridge.ClientHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record HudTrueInvisibilityVisualPayload(UUID playerUuid, boolean enabled) implements CustomPacketPayload {

    public static final Type<HudTrueInvisibilityVisualPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "hud_true_invisibility_visual"));

    public static final StreamCodec<FriendlyByteBuf, HudTrueInvisibilityVisualPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeUUID(p.playerUuid); buf.writeBoolean(p.enabled); },
                    buf -> new HudTrueInvisibilityVisualPayload(buf.readUUID(), buf.readBoolean()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(HudTrueInvisibilityVisualPayload payload, IPayloadContext context) {
        ClientHooks.handleClientPayload(payload, context);
    }
}
