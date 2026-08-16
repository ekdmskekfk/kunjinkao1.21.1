package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.block.TimeAcceleratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 客户端 -> 服务端：更新时间加速器的倍率与范围设置。
 */
public record TimeAcceleratorConfigPayload(BlockPos pos, int multiplierIndex, int sizeIndex) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TimeAcceleratorConfigPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "time_accelerator_config"));

    public static final StreamCodec<FriendlyByteBuf, TimeAcceleratorConfigPayload> STREAM_CODEC =
            StreamCodec.of(TimeAcceleratorConfigPayload::write, TimeAcceleratorConfigPayload::read);

    public static void write(FriendlyByteBuf buf, TimeAcceleratorConfigPayload payload) {
        buf.writeBlockPos(payload.pos);
        buf.writeInt(payload.multiplierIndex);
        buf.writeInt(payload.sizeIndex);
    }

    public static TimeAcceleratorConfigPayload read(FriendlyByteBuf buf) {
        return new TimeAcceleratorConfigPayload(buf.readBlockPos(), buf.readInt(), buf.readInt());
    }

    public static void handle(TimeAcceleratorConfigPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player
                    && player.level().getBlockEntity(payload.pos) instanceof TimeAcceleratorBlockEntity be) {
                be.setMultiplierIndex(payload.multiplierIndex);
                be.setSizeIndex(payload.sizeIndex);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}