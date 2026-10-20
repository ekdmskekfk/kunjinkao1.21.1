package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.inject.InjectionData;
import dev.modmind.kunjinkao.world.SwordTimeAcceleration;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * 让「被注入的任意物品」拥有原管理员剑的行为。
 *
 * <p>原剑的绝大部分行为写在物品类的 override 里（{@code use} / {@code useOn} /
 * {@code onItemUseFirst} / {@code getDestroySpeed} / {@code isCorrectToolForDrops}）。
 * 一个泥土方块不是那个类，原版永远不会调这些方法 —— 所以对注入物品改由本处理器在
 * 事件里转调 {@code KunJinKaoSwordItem} 里抽出来的同名静态实现。</p>
 *
 * <p><b>不会重复执行</b>：{@link #applies} 明确排除了剑本体
 * （{@code !(stack.getItem() instanceof KunJinKaoSwordItem)}）。剑仍然走它自己的
 * override，注入物品走这里，两条路互斥。</p>
 *
 * <p>事件顺序与解释：</p>
 * <ul>
 *   <li>{@code RightClickBlock} 对应原版的 {@code onItemUseFirst → useOn}；</li>
 *   <li>{@code useOn} 返回 PASS 时不取消事件，原版会继续调到 {@code use()}，
 *       于是由 {@code RightClickItem} 接管 —— 这样范围清除（对着空气右键）自然生效，
 *       而不会在点到方块时误触发（{@code useImpl} 内部自己挡了这一下）。</li>
 * </ul>
 */
public final class InjectedItemBehaviorHandler {

    private InjectedItemBehaviorHandler() {
    }

    /** 只处理「注入过、且不是剑本体」的栈。 */
    private static boolean applies(ItemStack stack) {
        return InjectionData.isInjected(stack) && !(stack.getItem() instanceof KunJinKaoSwordItem);
    }

    // ===== 右键方块：onItemUseFirst + useOn =====

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (!applies(stack)) {
            return;
        }
        Player player = event.getEntity();
        if (player == null) {
            return;
        }

        // ---- 1) 潜行路径：原版在 useOn 之前会先调 onItemUseFirst ----
        // 语义完全照搬 onItemUseFirst：扳手模式开着就整体让给别的模组（不消费），
        // 否则这一次 shift+右键 由本模组吃掉（区域场优先，其次开关机器加速场）。
        if (player.isShiftKeyDown() && !KunJinKaoSwordItem.isWrenchEnabled(stack)) {
            if (KunJinKaoSwordItem.tryPlaceAreaField(player, event.getLevel(), event.getPos(), stack)) {
                consume(event, InteractionResult.sidedSuccess(event.getLevel().isClientSide()));
                return;
            }
            if (event.getLevel() instanceof ServerLevel serverLevel) {
                SwordTimeAcceleration.tryToggleBlock(player, serverLevel, event.getPos(), stack);
            }
            consume(event, InteractionResult.sidedSuccess(event.getLevel().isClientSide()));
            return;
        }

        // ---- 2) useOn：放置类核心 / 时间加速 / 刷子 / 锄头铲子 / 避雷针 ----
        UseOnContext context = new UseOnContext(player, event.getHand(), event.getHitVec());
        InteractionResult result = KunJinKaoSwordItem.useOnImpl(stack, context);
        if (result != InteractionResult.PASS) {
            consume(event, result);
        }
        // PASS：刻意不取消。原版会接着调 use()，由下面的 onRightClickItem 接住 ——
        // 这样"点了个用不上的方块之后仍然能触发范围清除"这个原版行为保持不变。
    }

    // ===== 右键（空气）：use =====

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        if (!applies(stack)) {
            return;
        }
        Player player = event.getEntity();
        if (player == null) {
            return;
        }
        InteractionResultHolder<ItemStack> result =
                KunJinKaoSwordItem.useImpl(stack, player.level(), player, event.getHand());
        if (result.getResult() != InteractionResult.PASS) {
            event.setCancellationResult(result.getResult());
            event.setCanceled(true);
        }
    }

    private static void consume(PlayerInteractEvent.RightClickBlock event, InteractionResult result) {
        event.setCancellationResult(result);
        event.setCanceled(true);
    }

    // ===== 挖掘：getDestroySpeed / isCorrectToolForDrops =====

    /**
     * 挖掘速度。原剑的 {@code getDestroySpeed} 直接返回设置值（1..100），这里照搬。
     *
     * <p>用 {@code BreakSpeed} 而不是 {@code getDestroySpeed}：后者是物品方法，
     * 注入物品拿不到；前者是事件，能把"这一下的破坏速度"直接覆盖掉。</p>
     */
    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        ItemStack stack = event.getEntity().getMainHandItem();
        if (!applies(stack)) {
            return;
        }
        event.setNewSpeed(KunJinKaoSwordItem.getMiningSpeed(stack));
    }

    /** 原剑的 {@code isCorrectToolForDrops} 恒为 true，这里等价地强制可掉落。 */
    @SubscribeEvent
    public static void onHarvestCheck(PlayerEvent.HarvestCheck event) {
        ItemStack stack = event.getEntity().getMainHandItem();
        if (!applies(stack)) {
            return;
        }
        event.setCanHarvest(true);
    }
}
