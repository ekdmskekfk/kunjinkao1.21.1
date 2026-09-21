package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.List;

/**
 * 在原版矿石掉落完成后补充额外掉落，使管理员剑的矿石掉落达到设置的倍数。
 * 不取消原有破坏流程，因此经验、进度、保护插件和其他方块逻辑保持原样。
 */
public final class KunJinKaoOreDropHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.isCanceled() || !(event.getLevel() instanceof ServerLevel level)
                || !(event.getPlayer() instanceof ServerPlayer player)) {
            return;
        }

        ItemStack tool = player.getMainHandItem();
        if (!(tool.getItem() instanceof KunJinKaoSwordItem)) {
            return;
        }

        BlockState state = event.getState();
        if (!state.is(Tags.Blocks.ORES)) {
            return;
        }

        int multiplier = KunJinKaoSwordItem.getOreDropMultiplier(tool);
        if (multiplier <= 1) {
            return;
        }

        BlockPos pos = event.getPos().immutable();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        List<ItemStack> baseDrops = Block.getDrops(state, level, pos, blockEntity, player, tool);
        if (baseDrops.isEmpty()) {
            return;
        }

        // BreakEvent 在原版放出掉落前触发；排到同一服务器任务队列中，
        // 仅当该方块确实已被破坏时再补足 multiplier - 1 份掉落。
        level.getServer().execute(() -> {
            // 有意收紧：只认"目标位置已经变成空气"这一种破坏结果。
            // 原来写的是"不再是原方块就算破坏"，于是方块被换成别的方块（其它 mod/玩家顺手放上方块、
            // 替换成流体等）时也会补发矿石，属于越权补发。代价是这类"替换式破坏"不再加倍，
            // 但原版正常挖掘最终留下的都是空气，实际玩法不受影响。
            if (!level.getBlockState(pos).isAir()) {
                return;
            }
            for (ItemStack baseDrop : baseDrops) {
                spawnExtraStacks(level, pos, baseDrop, multiplier - 1);
            }
        });
    }

    private static void spawnExtraStacks(ServerLevel level, BlockPos pos, ItemStack baseDrop, int copies) {
        int totalCount = baseDrop.getCount() * copies;
        while (totalCount > 0) {
            int count = Math.min(totalCount, baseDrop.getMaxStackSize());
            ItemStack extra = baseDrop.copy();
            extra.setCount(count);
            Block.popResource(level, pos, extra);
            totalCount -= count;
        }
    }
}
