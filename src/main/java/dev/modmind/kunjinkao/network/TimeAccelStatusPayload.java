package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.clientbridge.ClientHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 加速场状态 S2C 包：把"当前有哪些方块正在被加速、倍率多少、还剩多久"告诉客户端，
 * 由客户端在方块上方画悬浮提示。
 * <p>
 * 只发本维度内玩家的所在维度（服务端按维度分发），所以线格式里不带维度字段。
 * 剩余时间以毫秒下发，客户端用自己的时钟往前推，避免两边计时器慢慢跑偏。
 */
public record TimeAccelStatusPayload(List<Entry> entries) implements CustomPacketPayload {

    /** 一个正在被加速的位置。{@code remainingMillis} 为负表示无限。 */
    public record Entry(BlockPos pos, int multiplier, long remainingMillis) {
    }

    public static final Type<TimeAccelStatusPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "time_accel_status"));

    public static final StreamCodec<FriendlyByteBuf, TimeAccelStatusPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
                buf.writeVarInt(payload.entries.size());
                for (Entry entry : payload.entries) {
                    buf.writeBlockPos(entry.pos());
                    buf.writeVarInt(entry.multiplier());
                    buf.writeVarLong(entry.remainingMillis());
                }
            },
            buf -> {
                int size = buf.readVarInt();
                List<Entry> entries = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    entries.add(new Entry(buf.readBlockPos(), buf.readVarInt(), buf.readVarLong()));
                }
                return new TimeAccelStatusPayload(List.copyOf(entries));
            });

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(TimeAccelStatusPayload payload, IPayloadContext context) {
        // 与其它 S2C 包同一入口：先过 common 侧网关，保证服务端不会加载 client 包下的类。
        ClientHooks.handleClientPayload(payload, context);
    }
}