package dev.modmind.kunjinkao.inject;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.Unbreakable;

/**
 * 「注入」标记与其数据表现：一个普通物品被注入之后，靠什么被认出来、看起来什么样、数值变成什么样。
 *
 * <p>设计要点：注入状态<b>只存在物品自己的 {@code CUSTOM_DATA} 里</b>，不引入新的物品类型。
 * 因此它天然跟着物品走 —— 丢在地上、放进箱子、被别的模组搬运、跨维度，标记都不会丢；
 * 也不需要在服务端维护"哪些物品被注入过"的旁路表。</p>
 *
 * <p>和 {@link KunJinKaoSwordItem} 的关系：那边约 120 个 static 方法本来就是「读写某个
 * ItemStack 的 NBT」，与物品类型无关，所以那些能力开关（范围清除、时间加速、建筑手杖…）
 * 在注入物品上<b>直接可用</b>，不需要改写。</p>
 */
public final class InjectionData {

    /** 注入标记的 NBT 键名。存进 {@code CUSTOM_DATA}。 */
    public static final String INJECTED_KEY = "KunjinKaoInjected";

    /** 注入物品多出来的那一行注释。 */
    public static final Component LORE_LINE =
            Component.translatable("lore.kunjinkao.injected");

    /**
     * 管理员剑的攻击力加成。
     *
     * <p>原剑是 {@code SwordItem.createAttributes(Tiers.DIAMOND, 3, -2.4F)}：
     * 钻石阶的攻击力加成是 3，再额外 +3，合计 +6（玩家基础 1 点 → 面板 7 点，
     * 与钻石剑一致）。剑本体删掉之后这个数字没有地方可读，所以在这里写死，
     * 并在注释里留下推导过程，避免以后被当成魔数。</p>
     */
    private static final double INJECTED_ATTACK_DAMAGE = 6.0D;

    /** 攻击速度加成，同样来自原剑的 -2.4（ADD_VALUE，即面板 1.6）。 */
    private static final double INJECTED_ATTACK_SPEED = -2.4D;

    private static final ResourceLocation DAMAGE_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath("kunjinkao", "injected_attack_damage");
    private static final ResourceLocation SPEED_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath("kunjinkao", "injected_attack_speed");

    private InjectionData() {
    }

    /**
     * 这个物品栈是否被注入过。
     *
     * <p>刻意不检查物品类型：任何物品都可能被注入。空栈永远不算。
     */
    public static boolean isInjected(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        CompoundTag tag = KunJinKaoSwordItem.dataTag(stack);
        return tag.getBoolean(INJECTED_KEY);
    }

    /**
     * 把注入效果落到物品上：标记 + 注释 + 数值 + 无法破坏。
     *
     * <p>幂等：重复调用不会叠加第二行注释。
     *
     * @return true 表示这次确实写入了（之前没被注入过）
     */
    public static boolean apply(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        boolean already = isInjected(stack);

        CompoundTag tag = KunJinKaoSwordItem.dataTag(stack);
        tag.putBoolean(INJECTED_KEY, true);
        KunJinKaoSwordItem.writeDataTag(stack, tag);

        appendLore(stack);
        applyCombatStats(stack);
        // 原剑的 isDamageable 恒为 true 但挂着 UNBREAKABLE，等于"能显示耐久条、但永远不掉"。
        // 任意物品这里只要"打不坏"，直接上 UNBREAKABLE 就够。
        if (!stack.has(DataComponents.UNBREAKABLE)) {
            stack.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
        }
        return !already;
    }

    /**
     * 撤掉注入效果（给未来的"解除注入"留的入口，目前没有调用点）。
     */
    public static void clear(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        CompoundTag tag = KunJinKaoSwordItem.dataTag(stack);
        tag.remove(INJECTED_KEY);
        if (tag.isEmpty()) {
            stack.remove(DataComponents.CUSTOM_DATA);
        } else {
            KunJinKaoSwordItem.writeDataTag(stack, tag);
        }
        removeLore(stack);
        stack.remove(DataComponents.ATTRIBUTE_MODIFIERS);
        stack.remove(DataComponents.UNBREAKABLE);
    }

    /** 追加那行注释；已有同样文案就不再追加。 */
    private static void appendLore(ItemStack stack) {
        ItemLore lore = stack.getOrDefault(DataComponents.LORE, ItemLore.EMPTY);
        for (Component line : lore.lines()) {
            if (sameLine(line, LORE_LINE)) {
                return;
            }
        }
        stack.set(DataComponents.LORE, lore.withLineAdded(LORE_LINE));
    }

    /** 去掉那行注释；其它注释保持原样、原顺序。 */
    private static void removeLore(ItemStack stack) {
        ItemLore lore = stack.get(DataComponents.LORE);
        if (lore == null) {
            return;
        }
        java.util.List<Component> kept = new java.util.ArrayList<>();
        boolean removed = false;
        for (Component line : lore.lines()) {
            if (!removed && sameLine(line, LORE_LINE)) {
                removed = true;
                continue;
            }
            kept.add(line);
        }
        if (!removed) {
            return;
        }
        if (kept.isEmpty()) {
            stack.remove(DataComponents.LORE);
        } else {
            stack.set(DataComponents.LORE, new ItemLore(kept));
        }
    }

    /** 文案比较：用空组件包一层，避免父样式差异导致误判。 */
    private static boolean sameLine(Component a, Component b) {
        return Component.empty().append(a).equals(Component.empty().append(b));
    }

    /**
     * 把攻击力 / 攻速拉齐到原管理员剑的水平。
     *
     * <p>用 {@code ATTRIBUTE_MODIFIERS} 组件而不是改 item 类：这是唯一能给"泥土"这种
     * 任意物品挂上武器数值的途径，效果与 {@code SwordItem} 自带的加成等价。</p>
     */
    private static void applyCombatStats(ItemStack stack) {
        ItemAttributeModifiers modifiers = ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(DAMAGE_MODIFIER_ID, INJECTED_ATTACK_DAMAGE,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(SPEED_MODIFIER_ID, INJECTED_ATTACK_SPEED,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, modifiers);
    }
}
