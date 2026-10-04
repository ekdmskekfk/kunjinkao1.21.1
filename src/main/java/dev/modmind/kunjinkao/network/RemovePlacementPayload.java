package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.world.SwordAreaFields;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

/**
 * C2S：从放置记录里收回一条。
 * <p>
 * 线格式：{@code writeVarInt(kind) | writeResourceLocation(dimension) | writeBlockPos(pos)}。
 * <p>
 * 授权照旧落在服务端：这里不信任客户端传来的任何东西，
 * 收回是否真的发生由 {@link SwordAreaFields#revoke} 说了算。
 */
public record RemovePlacementPayload(int kind, ResourceLocation dimension, BlockPos pos)
        implements CustomPacketPayload {

    public static final Type<RemovePlacementPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "remove_placement"));

    public static final StreamCodec<FriendlyByteBuf, RemovePlacementPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, p) -> {
                        buf.writeVarInt(p.kind);
                        buf.writeResourceLocation(p.dimension);
                        buf.writeBlockPos(p.pos);
                    },
                    buf -> new RemovePlacementPayload(buf.readVarInt(), buf.readResourceLocation(), buf.readBlockPos()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(RemovePlacementPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server == null) {
                return;
            }
            ResourceLocation dimensionId = payload.dimension();
            server.getAllLevels().forEach(level -> {
                if (level.dimension().location().equals(dimensionId)) {
                    SwordAreaFields.revoke(server, payload.kind(), level.dimension(), payload.pos());
                }
            });
            // 收回之后立刻把最新表推回客户端，避免列表还显示已经收掉的条目。
            PlacementSyncPayload.sendTo(context.player());
        });
    }
}