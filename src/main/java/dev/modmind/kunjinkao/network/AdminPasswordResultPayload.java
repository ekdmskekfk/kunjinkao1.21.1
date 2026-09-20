package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.clientbridge.ClientHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AdminPasswordResultPayload(boolean authorized) implements CustomPacketPayload {

    public static final Type<AdminPasswordResultPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "admin_password_result"));

    public static final StreamCodec<FriendlyByteBuf, AdminPasswordResultPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> buf.writeBoolean(p.authorized),
                    buf -> new AdminPasswordResultPayload(buf.readBoolean()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(AdminPasswordResultPayload payload, IPayloadContext context) {
        ClientHooks.handleClientPayload(payload, context);
    }
}
