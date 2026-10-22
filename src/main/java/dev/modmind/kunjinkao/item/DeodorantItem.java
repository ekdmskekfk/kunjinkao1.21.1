package dev.modmind.kunjinkao.item;

import dev.modmind.kunjinkao.entity.KunJinKaoEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 除臭剂：右键使用即可解除自己身上的「骚臭」。
 *
 * <p>设计上只解自己、不解别人：骚臭是会传染式地让周围生物逃跑的负面状态，
 * 做成"帮别人擦"会变成 PvP 工具；而"自救"正好对应它唯一的作用场景 ——
 * 被君君喷到之后想办法活下来。</p>
 *
 * <p>创造模式不消耗，生存模式每次用掉一个。</p>
 */
public class DeodorantItem extends Item {

    public DeodorantItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            return InteractionResultHolder.sidedSuccess(stack, true);
        }
        if (!player.hasEffect(KunJinKaoEffects.STENCH)) {
            player.displayClientMessage(
                    Component.translatable("message.kunjinkao.deodorant_not_stinky"), true);
            return InteractionResultHolder.pass(stack);
        }
        player.removeEffect(KunJinKaoEffects.STENCH);
        player.displayClientMessage(
                Component.translatable("message.kunjinkao.deodorant_used"), true);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, false);
    }
}
