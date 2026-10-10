package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.event.KunJinKaoQolHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 选区挖掘（127）设置一个角点（C2S）。
 * <p>
 * 线格式：{@code writeVarInt(cornerIndex) | writeBlockPos(pos)}。
 * cornerIndex 用 0/1 而不是枚举：两个角点不值得为它定一个枚举，
 * 而且越界序号在这里是"什么都不做"，比抛异常更合适（客户端版本错位时不该打断连接）。
 * <p>
 * 坐标由服务端重新校验（距离、维度、单边上限），客户端说在哪就信哪是不行的 ——
 * 一个改过的客户端可以把角点设到任意远处。
 */
public record ZoneSelectPayload(int cornerIndex, BlockPos pos) implements CustomPacketPayload {

    public static final int CORNER_A = 0;
    public static final int CORNER_B = 1;

    public static final Type<ZoneSelectPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "zone_select"));

    public static final StreamCodec<FriendlyByteBuf, ZoneSelectPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeVarInt(p.cornerIndex); buf.writeBlockPos(p.pos); },
                    buf -> new ZoneSelectPayload(buf.readVarInt(), buf.readBlockPos()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ZoneSelectPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> KunJinKaoQolHandler.handleZoneCorner(
                context.player(), payload.cornerIndex(), payload.pos()));
    }
}