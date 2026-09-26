package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 两件与"挖到的方块去哪"有关的事：
 * <ol>
 *   <li><b>R 键收集</b>：开着"可破坏不可破坏方块"时，对着方块按 R 直接把方块收进物品栏
 *       （带着方块实体数据），而不是掉在地上。服务端自己射线检测，客户端无法指定坐标。</li>
 *   <li><b>自动入包</b>：用剑挖到的任何方块，掉落算完之后立刻进背包，不在世界里弹一地。</li>
 * </ol>
 */
public final class SwordAutoPickupHandler {

    /** R 键收集的交互距离；与属性里的方块交互距离一致，回退到 4.5。 */
    private static final double DEFAULT_REACH = 4.5D;


    /** R 键：把正看着的方块直接收进物品栏。 */
    public static void collectTargetedBlock(net.minecraft.world.entity.player.Player player) {
        if (!(player instanceof ServerPlayer serverPlayer) || !(player.level() instanceof ServerLevel level)) {
            return;
        }
        ItemStack sword = player.getMainHandItem();
        if (!(sword.getItem() instanceof KunJinKaoSwordItem)
                || !KunJinKaoSwordItem.canBreakUnbreakableBlocks(sword)) {
            serverPlayer.displayClientMessage(
                    Component.translatable("message.kunjinkao.collect_requires_unbreakable"), true);
            return;
        }
        double reach = serverPlayer.getAttributeValue(
                net.minecraft.world.entity.ai.attributes.Attributes.BLOCK_INTERACTION_RANGE);
        if (!(reach > 0.0D)) {
            reach = DEFAULT_REACH;
        }
        HitResult hit = serverPlayer.pick(reach, 1.0F, false);
        if (hit.getType() != HitResult.Type.BLOCK || !(hit instanceof BlockHitResult blockHit)) {
            return;
        }
        BlockPos pos = blockHit.getBlockPos();
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || state.getDestroySpeed(level, pos) < -1.0F) {
            // 空气，或硬到原版根本不允许破坏（-1 是基岩/屏障，-0.9 之类仍可收）
            return;
        }
        collectIntoInventory(serverPlayer, level, pos, state);
    }

    /** 取掉落（含方块实体数据），塞进背包，然后清掉方块。 */
    private static void collectIntoInventory(ServerPlayer player, ServerLevel level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        ItemStack tool = player.getMainHandItem();
        List<ItemStack> drops = new ArrayList<>(Block.getDrops(state, level, pos, blockEntity, player, tool));

        // 战利品表为空的方块（基岩、屏障、命令方块等）回退为方块自身，
        // 并在移除之前把方块实体数据写进物品，这样机器里的东西不会丢。
        if (drops.isEmpty() && state.getBlock().asItem() != Items.AIR) {
            ItemStack fallback = new ItemStack(state.getBlock().asItem());
            if (blockEntity != null) {
                blockEntity.saveToItem(fallback, level.registryAccess());
            }
            drops.add(fallback);
        }
        if (drops.isEmpty()) {
            return;
        }

        state.getBlock().playerWillDestroy(level, pos, state, player);
        // 与剑的其它破坏路径一致：不走 Level.destroyBlock（它的返回值会被 setBlock 的结果影响）。
        level.levelEvent(2001, pos, Block.getId(state));
        level.setBlock(pos, level.getFluidState(pos).createLegacyBlock(), 3);
        level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(player, state));

        giveOrDrop(player, drops);
    }

    /**
     * 用剑挖到的方块：掉落算完就进背包。
     * <p>
     * 在 {@link BlockDropsEvent} 里把已经生成好的掉落实体收走 ——
     * 它们已经带着完整的组件数据（战利品表、copy_components 等都已生效），
     * 直接取 {@code getItem()} 即可，不需要自己重算一遍掉落。
     */
    @SubscribeEvent
    public void onBlockDrops(BlockDropsEvent event) {
        if (!(event.getBreaker() instanceof ServerPlayer player)) {
            return;
        }
        ItemStack tool = event.getTool();
        if (!(tool.getItem() instanceof KunJinKaoSwordItem)) {
            return;
        }
        List<ItemEntity> drops = event.getDrops();
        if (drops.isEmpty()) {
            return;
        }
        List<ItemStack> stacks = new ArrayList<>(drops.size());
        for (ItemEntity entity : drops) {
            stacks.add(entity.getItem().copy());
        }
        drops.clear();                       // 清空之后这些实体不会再被加入世界
        giveOrDrop(player, stacks);
    }

    /** 先进背包，装不下的部分照原版做法丢在脚下。 */
    private static void giveOrDrop(ServerPlayer player, List<ItemStack> stacks) {
        for (ItemStack stack : stacks) {
            if (stack.isEmpty()) {
                continue;
            }
            if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
            }
        }
    }
}