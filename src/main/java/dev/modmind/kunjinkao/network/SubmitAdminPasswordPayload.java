package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.config.AdminToolConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SubmitAdminPasswordPayload(String password) implements CustomPacketPayload {

    public static final Type<SubmitAdminPasswordPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "submit_admin_password"));

    public static final StreamCodec<FriendlyByteBuf, SubmitAdminPasswordPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> buf.writeUtf(p.password, 64),
                    buf -> new SubmitAdminPasswordPayload(buf.readUtf(64)));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(SubmitAdminPasswordPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof net.minecraft.server.level.ServerPlayer sp) {
                boolean authorized = AdminToolConfig.authorize(sp, payload.password);
                NetworkHandler.sendToPlayer(sp, new AdminPasswordResultPayload(authorized));
            }
        });
    }
}