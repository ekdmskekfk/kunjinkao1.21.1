package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 挖掘熔炼：开着这个开关时，剑挖出来的掉落会自动过一遍熔炉（熔炼）配方 ——
 * 原矿出锅成锭、沙子成玻璃、圆石成石头、黏土球成砖。
 * <p>
 * <b>不写死映射表，而是去问配方表。</b> 这是刻意的：写死一张表就得替每个原版方块
 * 抄一遍，装了模组又要再抄一遍；而熔炉配方本来就是数据包可以改的东西，
 * 直接查 {@link RecipeType#SMELTING} 意味着数据包加一条配方，这里就自动认。
 * 精准采集那条线（真的挂原版附魔）也是同一个理由 —— 让原版自己的规则干活。
 * <p>
 * <b>与精准采集互斥。</b> 两者同时开着时熔炼不生效：精准采集给你的就是方块本身，
 * 再把它烧成锭等于精准采集白开。判定看的是剑上<b>实际有没有精准采集附魔</b>，
 * 而不是只看菜单开关 —— 自己附魔上去的那把也走同一条规则。
 * <p>
 * <b>不给熔炼经验。</b> 方块自己的经验（{@code BlockDropsEvent.getDroppedExperience}）
 * 原样保留，不额外补熔炉那份 XP。
 */
public final class KunJinKaoAutoSmeltHandler {

    public KunJinKaoAutoSmeltHandler() {
    }

    /**
     * 挖掉方块、掉落已经算完但还没进世界时换掉它们。
     * <p>
     * 用 {@link BlockDropsEvent} 而不是 {@code BreakEvent}：这个点拿到的正是
     * 原版战利品表跑完的结果（含时运、精准采集、其它模组的战利品修改器），
     * 换起来不会漏掉任何一路来源。
     */
    @SubscribeEvent
    public void onBlockDrops(BlockDropsEvent event) {
        ItemStack tool = event.getTool();
        if (!isActive(tool)) {
            return;
        }
        ServerLevel level = event.getLevel();
        List<ItemEntity> drops = event.getDrops();
        // 遍历快照：拆堆时要往 drops 里补新实体，不能边遍历边扩。
        for (ItemEntity entity : List.copyOf(drops)) {
            ItemStack smelted = smeltAll(level, entity.getItem());
            if (smelted.isEmpty()) {
                continue;
            }
            int max = smelted.getMaxStackSize();
            if (smelted.getCount() <= max) {
                entity.setItem(smelted);
                continue;
            }
            // 配方产量 × 掉落数超过单堆上限：留满一堆在原实体上，其余在原位置补成新实体。
            int remaining = smelted.getCount() - max;
            smelted.setCount(max);
            entity.setItem(smelted);
            while (remaining > 0) {
                int count = Math.min(remaining, max);
                ItemStack extra = smelted.copy();
                extra.setCount(count);
                drops.add(new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), extra));
                remaining -= count;
            }
        }
    }

    /**
     * 开关是否该生效：手上是管理员剑、开了挖掘熔炼、且剑上没有精准采集。
     * <p>
     * 挖矿倍数补发的那条路（{@code KunJinKaoOreDropHandler}）也走这里，
     * 免得"正常掉落是锭、倍数补发却是原矿"。
     */
    public static boolean isActive(ItemStack tool) {
        return tool.getItem() instanceof KunJinKaoSwordItem
                && KunJinKaoSwordItem.isAutoSmeltEnabled(tool)
                && !hasSilkTouch(tool);
    }

    /**
     * 一整堆掉落的熔炼产物：数量 = 配方产量 × 输入数量。
     * 没有熔炼配方（或输入为空）返回 {@link ItemStack#EMPTY}，调用方保留原掉落。
     * <p>
     * 返回的堆叠数量可能超过单堆上限，由调用方决定怎么拆。
     */
    public static ItemStack smeltAll(ServerLevel level, ItemStack input) {
        if (input.isEmpty()) {
            return ItemStack.EMPTY;
        }
        SingleRecipeInput recipeInput = new SingleRecipeInput(input);
        ItemStack result = level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, recipeInput, level)
                .map(holder -> holder.value().assemble(recipeInput, level.registryAccess()))
                .orElse(ItemStack.EMPTY);
        if (result.isEmpty()) {
            return ItemStack.EMPTY;
        }
        result.setCount(result.getCount() * input.getCount());
        return result;
    }

    /** 剑上是否真的挂了精准采集附魔 —— 菜单开关写的是真附魔，玩家自己附的也算。 */
    private static boolean hasSilkTouch(ItemStack stack) {
        ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        for (Holder<Enchantment> holder : enchantments.keySet()) {
            if (holder.is(Enchantments.SILK_TOUCH)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 把一整批掉落换成熔炼产物，没有配方的原样保留；开关没生效时直接把原列表还回去（不做拷贝）。
     * <p>
     * 给两条绕开 {@link BlockDropsEvent} 的补发路径复用：挖矿倍数补发、
     * 不可破坏方块（基岩/屏障/命令方块）的自算掉落。少了这个，
     * 同一个开关在不同挖掘路径下的表现会不一致。
     */
    public static List<ItemStack> smeltList(ServerLevel level, ItemStack tool, List<ItemStack> drops) {
        if (!isActive(tool)) {
            return drops;
        }
        List<ItemStack> smelted = new ArrayList<>(drops.size());
        for (ItemStack drop : drops) {
            ItemStack result = smeltAll(level, drop);
            smelted.add(result.isEmpty() ? drop : result);
        }
        return smelted;
    }
}