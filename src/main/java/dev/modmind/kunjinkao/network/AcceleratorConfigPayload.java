package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.block.entity.AcceleratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AcceleratorConfigPayload(BlockPos pos, int multiplier, int radius) implements CustomPacketPayload {

    public static final Type<AcceleratorConfigPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "accelerator_config"));

    public static final StreamCodec<FriendlyByteBuf, AcceleratorConfigPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeBlockPos(p.pos); buf.writeInt(p.multiplier); buf.writeInt(p.radius); },
                    buf -> new AcceleratorConfigPayload(buf.readBlockPos(), buf.readInt(), buf.readInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(AcceleratorConfigPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (!AcceleratorBlockEntity.mayEdit(player, payload.pos)) return;
                Level level = player.level();
                if (level == null || !level.hasChunkAt(payload.pos)) return;
                BlockEntity be = level.getBlockEntity(payload.pos);
                if (!(be instanceof AcceleratorBlockEntity accelerator)) return;
                accelerator.setMultiplier(payload.multiplier);
                accelerator.setRadius(payload.radius);
                accelerator.setChanged();
                BlockState state = level.getBlockState(payload.pos);
                // sendBlockUpdated 已把方块实体数据同步给追踪该区块的玩家，无需再手动重发一次。
                level.sendBlockUpdated(payload.pos, state, state, 2);
                int size = accelerator.getRadius() * 2 + 1;
                player.displayClientMessage(Component.literal(
                        "§b[加速方块] §f倍率 " + accelerator.getMultiplier() + "x · 范围 " + size + "x" + size + "x" + size), true);
            }
        });
    }
}