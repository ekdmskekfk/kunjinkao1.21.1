package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.clientbridge.ClientHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 选区挖掘（127）的状态回执（S2C）：两个角点 + 作业进度。
 * <p>
 * 一条包同时管"选区"和"进度"，是因为它们在客户端只服务于同一件事 ——
 * 画出那个框、以及在框旁边显示还剩多少。分成两条包只会让两边的状态有机会对不上。
 * <p>
 * 维度要一起发：选区是绑在某个维度上的，玩家换维度之后旧的框不该跟着飘过来。
 * 客户端拿它和自己的维度比一下就知道该不该画。
 */
public record ZoneStatusPayload(boolean hasCornerA, BlockPos cornerA,
                                boolean hasCornerB, BlockPos cornerB,
                                boolean running, int done, int total,
                                ResourceLocation dimension) implements CustomPacketPayload {

    public static final Type<ZoneStatusPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "zone_status"));

    public static final StreamCodec<FriendlyByteBuf, ZoneStatusPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> {
                        buf.writeBoolean(p.hasCornerA);
                        if (p.hasCornerA) {
                            buf.writeBlockPos(p.cornerA);
                        }
                        buf.writeBoolean(p.hasCornerB);
                        if (p.hasCornerB) {
                            buf.writeBlockPos(p.cornerB);
                        }
                        buf.writeBoolean(p.running);
                        buf.writeVarInt(p.done);
                        buf.writeVarInt(p.total);
                        buf.writeBoolean(p.dimension != null);
                        if (p.dimension != null) {
                            buf.writeResourceLocation(p.dimension);
                        }
                    },
                    buf -> new ZoneStatusPayload(
                            buf.readBoolean(), buf.readBoolean() ? buf.readBlockPos() : BlockPos.ZERO,
                            buf.readBoolean(), buf.readBoolean() ? buf.readBlockPos() : BlockPos.ZERO,
                            buf.readBoolean(), buf.readVarInt(), buf.readVarInt(),
                            buf.readBoolean() ? buf.readResourceLocation() : null));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ZoneStatusPayload payload, IPayloadContext context) {
        ClientHooks.handleClientPayload(payload, context);
    }
}