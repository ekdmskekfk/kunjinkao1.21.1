package dev.modmind.kunjinkao;

import java.util.List;

import dev.modmind.kunjinkao.entity.DiamondProjectile;
import dev.modmind.kunjinkao.event.KunJinKaoDeathEventHandler;
import dev.modmind.kunjinkao.event.KunJinKaoProtectionHandler;
import dev.modmind.kunjinkao.overwrite.KunJinKaoOverwriteHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class KunJinKaoSwordItem extends SwordItem {

    private static final String LOOTING_MODE_KEY = "LootingMode";
    private static final String OVERWRITE_KEY = "OverwriteEnabled";
    private static final String THEME_KEY = "OverwriteTheme";

    public KunJinKaoSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, properties.attributes(SwordItem.createAttributes(tier, attackDamageModifier, attackSpeedModifier)));
    }

    /**
     * 读取物品附加 NBT 标签（Data Components 的 CUSTOM_DATA 组件）。
     */
    public static CompoundTag getModTag(ItemStack stack) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return data.copyTag();
    }

    /**
     * 将附加 NBT 标签写回物品的 CUSTOM_DATA 组件。
     */
    public static void setModTag(ItemStack stack, CompoundTag tag) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    // ===== 伪装状态（NBT 标记，CustomModelData 驱动模型 override） =====

    public static boolean isDisguised(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.DEFAULT).value() == 1;
    }

    public static void setDisguised(ItemStack stack, boolean disguised) {
        // 伪装状态只通过 CUSTOM_MODEL_DATA 组件驱动模型 override（1.21.1 的物品模型谓词只读该组件）。
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(disguised ? 1 : 0));
    }

    public static void toggleDisguise(ItemStack stack) {
        setDisguised(stack, !isDisguised(stack));
    }

    // ===== 覆写流程开关（OverwriteEnabled，默认开启） =====

    public static boolean isOverwriteEnabled(ItemStack stack) {
        CompoundTag tag = getModTag(stack);
        return !tag.contains(OVERWRITE_KEY) || tag.getBoolean(OVERWRITE_KEY);
    }

    public static void setOverwriteEnabled(ItemStack stack, boolean enabled) {
        CompoundTag tag = getModTag(stack);
        tag.putBoolean(OVERWRITE_KEY, enabled);
        setModTag(stack, tag);
    }

    public static void toggleOverwrite(ItemStack stack) {
        setOverwriteEnabled(stack, !isOverwriteEnabled(stack));
    }

    // ===== 异象主题（OverwriteTheme，0..4，P 键循环切换） =====

    public static int getTheme(ItemStack stack) {
        int theme = getModTag(stack).getInt(THEME_KEY);
        return Math.floorMod(theme, KunJinKaoTheme.COUNT);
    }

    public static void setTheme(ItemStack stack, int theme) {
        CompoundTag tag = getModTag(stack);
        tag.putInt(THEME_KEY, Math.floorMod(theme, KunJinKaoTheme.COUNT));
        setModTag(stack, tag);
    }

    public static void cycleTheme(ItemStack stack) {
        setTheme(stack, getTheme(stack) + 1);
    }

    @Override
    public Component getName(ItemStack stack) {
        if (isDisguised(stack)) {
            return Component.translatable("item.minecraft.diamond_sword");
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (isDisguised(stack)) {
            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
            return;
        }
        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.translatable("item.modifiers.mainhand").withStyle(ChatFormatting.GRAY));

        MutableComponent damageLine = Component.literal("");
        damageLine.append(Component.literal("∞"));
        damageLine.append(Component.literal(" 攻击伤害").withStyle(ChatFormatting.DARK_GREEN));
        tooltipComponents.add(damageLine);

        MutableComponent speedLine = Component.literal("-2.4").withStyle(ChatFormatting.DARK_GREEN);
        speedLine.append(Component.literal(" 攻击速度").withStyle(ChatFormatting.DARK_GREEN));
        tooltipComponents.add(speedLine);

        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.literal("上古代码洪流中遗落的碎片所铸，").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal("剑身无锋，却刻满流动的乱码铭文。").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal("此剑同时承载两种互斥的法则：").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.literal("覆写 —— 强制修改对手在“世界系统”中的底层属性。").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal("防御、速度、抗性、乃至“存在”本身，").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal("在剑锋触及的瞬间，全部被覆盖成剑主定义的数值。").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.literal("断未 —— 追加一击，不伤实体，只清除目标的“定义”。").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal("你不是被削弱、被封印、被击败，").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal("你只是变成系统无法识别的“未定义项”，").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal("系统会因无法处理你而自行将你忽略、遗忘、清零。").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.literal("敌人的苦修、装备、Buff，在覆写面前").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal("只是一行可被 Ctrl+C 覆盖的文本；").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal("敌人引以为傲的底牌，在断未之后连“被记住”的资格都被剥夺。").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal("对手不是在对抗一个剑客，").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal("而是在对抗一个手握“编辑世界源代码权限”的疯子。").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.literal("覆写流程：").withStyle(ChatFormatting.DARK_GRAY)
                .append(isOverwriteEnabled(stack)
                        ? Component.literal("开启（无条件覆写+断未）").withStyle(ChatFormatting.DARK_GREEN)
                        : Component.literal("关闭（瞬杀）").withStyle(ChatFormatting.DARK_RED)));
        tooltipComponents.add(Component.literal("异象主题：").withStyle(ChatFormatting.DARK_GRAY)
                .append(Component.literal(KunJinKaoTheme.displayName(getTheme(stack))).withStyle(ChatFormatting.LIGHT_PURPLE)));
        tooltipComponents.add(Component.literal("按键 P 循环切换主题").withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (isDisguised(player.getItemInHand(hand))) {
            return super.use(level, player, hand);
        }
        if (player.isShiftKeyDown()) {
            return cycleMode(level, player, hand);
        }
        if (!level.isClientSide()) {
            DiamondProjectile projectile = new DiamondProjectile(level, player);
            projectile.setLootingMode(getLootingMode(player.getItemInHand(hand)));
            projectile.setOwnerId(player.getUUID());
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(projectile);
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (isDisguised(stack)) {
            return super.hurtEnemy(stack, target, attacker);
        }
        if (target instanceof Player) {
            return super.hurtEnemy(stack, target, attacker);
        }
        if (!attacker.level().isClientSide()) {
            if (isOverwriteEnabled(stack)) {
                KunJinKaoOverwriteHandler.startOverwrite(attacker, target, stack, (ServerLevel) attacker.level());
                return true;
            }
            applyKunJinKaoMark(target, stack);
            target.kill();
        }
        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (isDisguised(stack)) {
            return super.getDestroySpeed(stack, state);
        }
        return Tiers.GOLD.getSpeed();
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        if (isDisguised(stack)) {
            return super.isCorrectToolForDrops(stack, state);
        }
        return true;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (level.isClientSide()) {
            return;
        }

        if (stack.getDamageValue() > 0) {
            stack.setDamageValue(0);
        }

        if (entity instanceof Player player) {
            removeHarmfulEffects(player);
        }
    }

    private static void removeHarmfulEffects(Player player) {
        for (MobEffectInstance effect : List.copyOf(player.getActiveEffects())) {
            if (effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                player.removeEffect(effect.getEffect());
            }
        }
    }

    private InteractionResultHolder<ItemStack> cycleMode(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            return InteractionResultHolder.success(stack);
        }

        CompoundTag tag = getModTag(stack);
        int currentMode = tag.getInt(LOOTING_MODE_KEY);
        int newMode = (currentMode + 1) % 3;
        tag.putInt(LOOTING_MODE_KEY, newMode);
        setModTag(stack, tag);

        String modeText = switch (newMode) {
            case 1 -> "§6抢夺 25 级";
            case 2 -> "§6抢夺 50 级";
            default -> "§7无抢夺";
        };
        player.displayClientMessage(Component.literal("§e覆写·断未 - 当前模式: " + modeText), true);

        return InteractionResultHolder.consume(stack);
    }

    /**
     * 将锟斤拷击杀标记与当前抢夺模式写入目标实体持久 NBT，供掉落处理器应用加成。
     */
    public static void applyKunJinKaoMark(LivingEntity target, ItemStack stack) {
        CompoundTag data = target.getPersistentData();
        data.putBoolean(KunJinKaoDeathEventHandler.MARK_KEY, true);
        data.putInt(KunJinKaoDeathEventHandler.LOOTING_MODE_ENTITY_KEY, getLootingMode(stack));
        data.putBoolean(KunJinKaoProtectionHandler.KILL_BY_OVERWRITE_KEY, true);
    }

    public static int getLootingMode(ItemStack stack) {
        return getModTag(stack).getInt(LOOTING_MODE_KEY);
    }
}
