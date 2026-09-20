package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.clientbridge.ClientHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record HudTrueInvisibilityStatePayload(boolean enabled, boolean authorized) implements CustomPacketPayload {

    public static final Type<HudTrueInvisibilityStatePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "hud_true_invisibility_state"));

    public static final StreamCodec<FriendlyByteBuf, HudTrueInvisibilityStatePayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeBoolean(p.enabled); buf.writeBoolean(p.authorized); },
                    buf -> new HudTrueInvisibilityStatePayload(buf.readBoolean(), buf.readBoolean()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(HudTrueInvisibilityStatePayload payload, IPayloadContext context) {
        ClientHooks.handleClientPayload(payload, context);
    }
}
