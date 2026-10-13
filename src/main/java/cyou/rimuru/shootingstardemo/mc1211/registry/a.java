/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.registry;

import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class a
extends Block {
    public a(BlockBehaviour.Properties properties) {
        super(properties);
        BlockState blockState = (BlockState)this.stateDefinition.any();
        for (BooleanProperty booleanProperty : PipeBlock.PROPERTY_BY_DIRECTION.values()) {
            blockState = (BlockState)blockState.setValue((Property)booleanProperty, (Comparable)Boolean.valueOf(false));
        }
        this.registerDefaultState(blockState);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add((Property[])PipeBlock.PROPERTY_BY_DIRECTION.values().toArray(new BooleanProperty[0]));
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return a.a(this.defaultBlockState(), blockPlaceContext.getClickedPos(), arg_0 -> ((Level)blockPlaceContext.getLevel()).getBlockState(arg_0));
    }

    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return (BlockState)blockState.setValue((Property)PipeBlock.PROPERTY_BY_DIRECTION.get(direction), (Comparable)Boolean.valueOf(blockState2.is((Block)this)));
    }

    public static BlockState a(BlockState blockState, BlockPos blockPos, Function<BlockPos, BlockState> function) {
        if (!(blockState.getBlock() instanceof a)) {
            return blockState;
        }
        for (Map.Entry entry : PipeBlock.PROPERTY_BY_DIRECTION.entrySet()) {
            blockState = (BlockState)blockState.setValue((Property)entry.getValue(), (Comparable)Boolean.valueOf(function.apply(blockPos.relative((Direction)entry.getKey())).is(blockState.getBlock())));
        }
        return blockState;
    }
}

