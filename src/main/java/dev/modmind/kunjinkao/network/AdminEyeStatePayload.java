package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.clientbridge.ClientHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record AdminEyeStatePayload(UUID playerUuid, boolean enabled) implements CustomPacketPayload {

    public static final Type<AdminEyeStatePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "admin_eye_state"));

    public static final StreamCodec<FriendlyByteBuf, AdminEyeStatePayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeUUID(p.playerUuid); buf.writeBoolean(p.enabled); },
                    buf -> new AdminEyeStatePayload(buf.readUUID(), buf.readBoolean()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(AdminEyeStatePayload payload, IPayloadContext context) {
        ClientHooks.handleClientPayload(payload, context);
    }
}
