/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.registry;

import cyou.rimuru.shootingstardemo.mc1211.registry.d;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class h
extends Block {
    public h(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static boolean a(ItemStack itemStack) {
        return itemStack.is(d.a);
    }

    public float getDestroyProgress(BlockState blockState, Player player, BlockGetter blockGetter, BlockPos blockPos) {
        float f2 = blockState.getDestroySpeed(blockGetter, blockPos);
        if (f2 == -1.0f) {
            return 0.0f;
        }
        boolean bl = player.hasCorrectToolForDrops(blockState) && h.a(player.getMainHandItem());
        int n2 = bl ? 30 : 100;
        return player.getDestroySpeed(blockState) / f2 / (float)n2;
    }
}

