package dev.modmind.kunjinkao.world;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

/**
 * 从<b>容器</b>里取材。
 * <p>
 * 对应建筑手杖的 containers 包（IContainerHandler / ContainerManager / HandlerShulkerbox）：
 * 那边把"容器"抽象成一个接口，凡是能装东西的物品都能注册进去（潜影盒、收纳袋，
 * 以及 AE2 无线终端、植物魔法、ProjectE 等等模组兼容）。
 * 这里只实现<b>原版</b>的两种 —— 潜影盒与收纳袋，其余模组兼容不做。
 * <p>
 * 关键点是：光"找到"不够，还得能<b>取走</b>。背包里的那一叠可以直接 shrink，
 * 装进潜影盒里的不行 —— 必须把潜影盒的 CONTAINER 组件重新写回去。
 */
public final class ContainerMaterials {

    private ContainerMaterials() {
    }

    /** 玩家身上（背包 + 副手 + 潜影盒 + 收纳袋）共有多少个这一种可放置方块。 */
    public static int count(Player player, Item item) {
        int total = 0;
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            total += countIn(inventory.getItem(i), item);
        }
        for (ItemStack offhand : player.getInventory().offhand) {
            total += countIn(offhand, item);
        }
        return total;
    }

    /** 某一个物品里有多少个目标方块：它自己算一份，容器再往里翻一层。 */
    private static int countIn(ItemStack stack, Item item) {
        if (stack.isEmpty()) {
            return 0;
        }
        int total = stack.getItem() == item ? stack.getCount() : 0;
        for (ItemStack inner : contentsOf(stack)) {
            if (inner.getItem() == item) {
                total += inner.getCount();
            }
        }
        return total;
    }

    /** 上述任意位置是否存在这一种方块。 */
    public static boolean has(Player player, Item item) {
        return count(player, item) > 0;
    }

    /**
     * 取走一个目标方块：先背包、再副手、最后容器。
     *
     * @return 真取到了才返回 true
     */
    public static boolean consumeOne(Player player, Item item) {
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (shrinkDirect(inventory.getItem(i), item)) {
                return true;
            }
        }
        for (ItemStack offhand : player.getInventory().offhand) {
            if (shrinkDirect(offhand, item)) {
                return true;
            }
        }
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (takeFromContainer(inventory.getItem(i), item)) {
                return true;
            }
        }
        for (ItemStack offhand : player.getInventory().offhand) {
            if (takeFromContainer(offhand, item)) {
                return true;
            }
        }
        return false;
    }

    /** 这一叠本身就是目标方块时直接减一。 */
    private static boolean shrinkDirect(ItemStack stack, Item item) {
        if (!stack.isEmpty() && stack.getItem() == item) {
            stack.shrink(1);
            return true;
        }
        return false;
    }

    /** 从这一叠所代表的容器里取一个；取到了就把容器的内容写回去。 */
    private static boolean takeFromContainer(ItemStack container, Item item) {
        if (container.isEmpty()) {
            return false;
        }
        ItemContainerContents contents = container.get(DataComponents.CONTAINER);
        var bundle = container.get(DataComponents.BUNDLE_CONTENTS);
        if (contents == null && bundle == null) {
            return false;
        }
        // 潜影盒：27 格，用 CONTAINER 组件。
        if (contents != null) {
            NonNullList<ItemStack> slots = NonNullList.withSize(27, ItemStack.EMPTY);
            int index = 0;
            for (ItemStack inner : contents.nonEmptyItems()) {
                if (index < slots.size()) {
                    slots.set(index++, inner.copy());
                }
            }
            for (int i = 0; i < slots.size(); i++) {
                ItemStack inner = slots.get(i);
                if (!inner.isEmpty() && inner.getItem() == item) {
                    inner.shrink(1);
                    container.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(slots));
                    return true;
                }
            }
        }
        // 收纳袋：BundleContents 只有无参的 removeOne()（拿最上面那个），
        // 想取指定物品得自己遍历重建一份。
        if (bundle != null) {
            java.util.List<ItemStack> items = new java.util.ArrayList<>();
            for (ItemStack inner : bundle.items()) {
                items.add(inner.copy());
            }
            for (int i = 0; i < items.size(); i++) {
                ItemStack inner = items.get(i);
                if (!inner.isEmpty() && inner.getItem() == item) {
                    inner.shrink(1);
                    if (inner.isEmpty()) {
                        items.remove(i);
                    }
                    container.set(DataComponents.BUNDLE_CONTENTS,
                            new net.minecraft.world.item.component.BundleContents(items));
                    return true;
                }
            }
        }
        return false;
    }

    /** 取这一叠容器里所有能看到的物品（只用于计数）。 */
    private static Iterable<ItemStack> contentsOf(ItemStack stack) {
        ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
        if (contents != null) {
            return contents.nonEmptyItems();
        }
        var bundle = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (bundle != null) {
            return bundle.items();
        }
        return java.util.List.of();
    }

    /**
     * 在玩家身上的容器（潜影盒 / 收纳袋）里找一叠可用的方块。
     *
     * @param required 只接受这一种物品；为 null 表示任意可放置的方块
     * @return 找到的那一叠（<b>容器内</b>的栈，只用于读出物品与数量，不要直接 shrink）
     */
    public static ItemStack findInContainers(Player player, Item required) {
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack found = findIn(inventory.getItem(i), required);
            if (!found.isEmpty()) {
                return found;
            }
        }
        for (ItemStack offhand : player.getInventory().offhand) {
            ItemStack found = findIn(offhand, required);
            if (!found.isEmpty()) {
                return found;
            }
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack findIn(ItemStack container, Item required) {
        if (container.isEmpty() || !isContainer(container)) {
            return ItemStack.EMPTY;
        }
        for (ItemStack inner : contentsOf(container)) {
            if (inner.isEmpty()) {
                continue;
            }
            boolean placeable = inner.getItem() instanceof BlockItem;
            if (placeable && (required == null || inner.getItem() == required)) {
                return inner;
            }
        }
        return ItemStack.EMPTY;
    }

    /** 这一叠是不是"能让建筑手杖取材"的容器（潜影盒或收纳袋）。 */
    public static boolean isContainer(ItemStack stack) {
        return !stack.isEmpty()
                && (stack.has(DataComponents.CONTAINER) || stack.has(DataComponents.BUNDLE_CONTENTS));
    }

    /** 供外部判断：这一叠里是否存在可放置的方块（任意一种）。 */
    public static boolean hasAnyPlaceable(Player player) {
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (containsPlaceable(inventory.getItem(i))) {
                return true;
            }
        }
        for (ItemStack offhand : player.getInventory().offhand) {
            if (containsPlaceable(offhand)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsPlaceable(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (stack.getItem() instanceof BlockItem) {
            return true;
        }
        for (ItemStack inner : contentsOf(stack)) {
            if (inner.getItem() instanceof BlockItem) {
                return true;
            }
        }
        return false;
    }
}