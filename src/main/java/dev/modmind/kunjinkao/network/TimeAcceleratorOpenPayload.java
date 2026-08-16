package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.block.TimeAcceleratorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 服务端 -> 客户端：请求打开时间加速器配置 GUI。
 */
public record TimeAcceleratorOpenPayload(BlockPos pos) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TimeAcceleratorOpenPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "time_accelerator_open"));

    public static final StreamCodec<FriendlyByteBuf, TimeAcceleratorOpenPayload> STREAM_CODEC =
            StreamCodec.of(TimeAcceleratorOpenPayload::write, TimeAcceleratorOpenPayload::read);

    public static void write(FriendlyByteBuf buf, TimeAcceleratorOpenPayload payload) {
        buf.writeBlockPos(payload.pos);
    }

    public static TimeAcceleratorOpenPayload read(FriendlyByteBuf buf) {
        return new TimeAcceleratorOpenPayload(buf.readBlockPos());
    }

    public static void handle(TimeAcceleratorOpenPayload payload, IPayloadContext context) {
        if (!context.flow().isClientbound()) {
            return;
        }
        context.enqueueWork(() -> Minecraft.getInstance().setScreen(new TimeAcceleratorScreen(payload.pos())));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}