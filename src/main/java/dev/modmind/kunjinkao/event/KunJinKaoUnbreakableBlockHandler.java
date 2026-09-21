package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.config.AdminToolConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.List;

/**
 * Server-side removal of blocks whose destroy speed is negative, such as bedrock.
 * The normal Forge break hook is called first so protection handlers can veto it.
 */
public final class KunJinKaoUnbreakableBlockHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.isCanceled() || !(event.getLevel() instanceof ServerLevel level)
                || !(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)
                || !KunJinKaoSwordItem.canBreakUnbreakableBlocks(stack)) {
            return;
        }

        BlockState state = level.getBlockState(event.getPos());
        if (state.isAir() || state.getDestroySpeed(level, event.getPos()) >= 0.0F) {
            return;
        }

        // 放行条件：本模组的管理员（密码授权）或原版游戏管理员方块权限。
        // 只认 canUseGameMasterBlocks() 会把"生存模式 OP"和"创造但未开作弊的单机玩家"一起挡掉，
        // 与本模组"密码即权限"的模型自相矛盾（审计指出）；但也不能让普通持剑者绕过原版对
        // 命令方块/屏障的保护，所以两条满足其一即可。
        // 不满足时给一句提示并放行原版逻辑：fireBlockBreak 的探针会让别的保护插件
        // 看到一次并未真正发生的破坏。
        if (!player.canUseGameMasterBlocks() && !AdminToolConfig.isAuthorized(player.getUUID())) {
            player.displayClientMessage(Component.translatable("message.kunjinkao.admin_required"), true);
            return;
        }

        BlockPos pos = event.getPos().immutable();
        // A negative result means an existing Forge protection or mod handler denied the break.
        if (CommonHooks.fireBlockBreak(level, player.gameMode.getGameModeForPlayer(), player, pos, state).isCanceled()) {
            event.setCanceled(true);
            return;
        }

        event.setCanceled(true);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        List<ItemStack> drops = Block.getDrops(state, level, pos, blockEntity, player, stack);

        // 基岩、屏障、命令方块等不可破坏方块的原版战利品表为空；此时回退为方块自身。
        // 若存在方块实体，则在移除前把其数据写进物品 NBT。
        if (drops.isEmpty() && state.getBlock().asItem() != Items.AIR) {
            ItemStack fallback = new ItemStack(state.getBlock().asItem());
            if (blockEntity != null) {
                blockEntity.saveToItem(fallback, level.registryAccess());
            }
            drops = List.of(fallback);
        }

        state.getBlock().playerWillDestroy(level, pos, state, player);
        if (!level.destroyBlock(pos, false, player)) {
            return;
        }
        for (ItemStack drop : drops) {
            if (!drop.isEmpty()) {
                Block.popResource(level, pos, drop.copy());
            }
        }
    }
}
