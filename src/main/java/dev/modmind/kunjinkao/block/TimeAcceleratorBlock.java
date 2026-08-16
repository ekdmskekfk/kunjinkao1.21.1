package dev.modmind.kunjinkao.block;

import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.network.TimeAcceleratorOpenPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * 时间加速器方块：每服务端刻对周围区域内的作物/方块实体进行额外 tick（倍率加速）。
 * 右键打开配置 GUI（加速倍率 + 加速范围两个滑轮）。
 */
public class TimeAcceleratorBlock extends Block implements EntityBlock {

    public TimeAcceleratorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TimeAcceleratorBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return (l, p, s, be) -> {
            if (be instanceof TimeAcceleratorBlockEntity accelerator) {
                accelerator.tickServer();
            }
        };
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            NetworkHandler.sendToPlayer(serverPlayer, new TimeAcceleratorOpenPayload(pos));
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}