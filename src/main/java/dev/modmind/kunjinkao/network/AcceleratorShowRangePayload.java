package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.block.entity.AcceleratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AcceleratorShowRangePayload(BlockPos pos, boolean showRange) implements CustomPacketPayload {

    public static final Type<AcceleratorShowRangePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "accelerator_show_range"));

    public static final StreamCodec<FriendlyByteBuf, AcceleratorShowRangePayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeBlockPos(p.pos); buf.writeBoolean(p.showRange); },
                    buf -> new AcceleratorShowRangePayload(buf.readBlockPos(), buf.readBoolean()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(AcceleratorShowRangePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                Level level = player.level();
                if (level == null || !level.hasChunkAt(payload.pos)) return;
                BlockEntity be = level.getBlockEntity(payload.pos);
                if (!(be instanceof AcceleratorBlockEntity accelerator)) return;
                accelerator.setShowRange(payload.showRange);
                accelerator.setChanged();
                BlockState state = level.getBlockState(payload.pos);
                level.sendBlockUpdated(payload.pos, state, state, 2);
                if (level.getServer() != null) {
                    for (ServerPlayer other : level.getServer().getPlayerList().getPlayers()) {
                        other.connection.send(ClientboundBlockEntityDataPacket.create(accelerator));
                    }
                }
                player.displayClientMessage(Component.literal(payload.showRange
                        ? "§b[加速方块] §f已显示加速范围"
                        : "§b[加速方块] §f已隐藏加速范围"), true);
            }
        });
    }
}