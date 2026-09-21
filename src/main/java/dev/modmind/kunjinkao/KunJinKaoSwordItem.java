package dev.modmind.kunjinkao;

import java.util.List;

import dev.modmind.kunjinkao.config.AdminToolConfig;
import dev.modmind.kunjinkao.event.KunJinKaoDeathEventHandler;
import dev.modmind.kunjinkao.event.KunJinKaoProtectionHandler;
import dev.modmind.kunjinkao.overwrite.KunJinKaoOverwriteHandler;
import dev.modmind.kunjinkao.recipe.AdminSwordRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class KunJinKaoSwordItem extends SwordItem {

    private static final String LOOTING_MODE_KEY = "LootingMode";
    private static final String OVERWRITE_KEY = "OverwriteEnabled";
    private static final String THEME_KEY = "OverwriteTheme";
    private static final String BLUE_SCREEN_ATTACK_KEY = "BlueScreenAttackEnabled";
    private static final String BREAK_UNBREAKABLE_BLOCKS_KEY = "BreakUnbreakableBlocksEnabled";
    private static final String MINING_SPEED_KEY = "MiningSpeed";
    private static final String ORE_DROP_MULTIPLIER_KEY = "OreDropMultiplier";
    private static final String AREA_CLEAR_TARGET_MODE_KEY = "AreaClearTargetMode";
    private static final String ATTACK_DAMAGE_LIMIT_KEY = "AttackDamageLimit";
    private static final String ULTIMATE_DEATH_KEY = "UltimateDeathEnabled";
    private static final String QUIT_STRIKE_KEY = "QuitStrikeEnabled";

    /**
     * 击杀时写在受害者身上的标记，供 UltimateDeathHandler 判断这一刀用的是哪个开关。
     * 范围清除这类路径没有伤害来源，只能靠标记把开关状态传给死亡处理。
     */
    public static final String ULTIMATE_DEATH_MARK = "KunJinKaoUltimateDeath";
    public static final String QUIT_STRIKE_MARK = "KunJinKaoQuitStrike";

    public static final int MIN_MINING_SPEED = 1;
    public static final int MAX_MINING_SPEED = 100;
    public static final int DEFAULT_MINING_SPEED = 12;
    public static final int MIN_ORE_DROP_MULTIPLIER = 1;
    public static final int MAX_ORE_DROP_MULTIPLIER = 1000;
    public static final int DEFAULT_ORE_DROP_MULTIPLIER = 1;
    public static final int MAX_ATTACK_DAMAGE_LIMIT = 1000;
    public static final int DEFAULT_ATTACK_DAMAGE_LIMIT = MAX_ATTACK_DAMAGE_LIMIT;

    public KunJinKaoSwordItem(Tier tier, Properties properties) {
        super(tier, properties.attributes(SwordItem.createAttributes(tier, 3, -2.4F)));
    }

    // ===== DataComponent 读写辅助：所有非 CustomModelData 字段都存放在 CUSTOM_DATA 的 NBT 内 =====

    private static CompoundTag dataTag(ItemStack stack) {
        CustomData cd = stack.get(DataComponents.CUSTOM_DATA);
        return cd != null ? cd.copyTag() : new CompoundTag();
    }

    private static void writeDataTag(ItemStack stack, CompoundTag tag) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    // ===== 伪装状态：使用 DataComponents.CUSTOM_MODEL_DATA =====

    public static boolean isDisguised(ItemStack stack) {
        CustomModelData cmd = stack.get(DataComponents.CUSTOM_MODEL_DATA);
        return cmd != null && cmd.value() == 1;
    }

    public static void setDisguised(ItemStack stack, boolean disguised) {
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(disguised ? 1 : 0));
    }

    public static void toggleDisguise(ItemStack stack) {
        setDisguised(stack, !isDisguised(stack));
    }

    // ===== 覆写流程开关 =====

    public static boolean isOverwriteEnabled(ItemStack stack) {
        return dataTag(stack).getBoolean(OVERWRITE_KEY);
    }

    public static void setOverwriteEnabled(ItemStack stack, boolean enabled) {
        CompoundTag tag = dataTag(stack);
        tag.putBoolean(OVERWRITE_KEY, enabled);
        writeDataTag(stack, tag);
    }

    public static void toggleOverwrite(ItemStack stack) {
        setOverwriteEnabled(stack, !isOverwriteEnabled(stack));
    }

    // ===== 异象主题 =====

    public static int getTheme(ItemStack stack) {
        int theme = dataTag(stack).getInt(THEME_KEY);
        return Math.floorMod(theme, KunJinKaoTheme.COUNT);
    }

    public static void setTheme(ItemStack stack, int theme) {
        CompoundTag tag = dataTag(stack);
        tag.putInt(THEME_KEY, Math.floorMod(theme, KunJinKaoTheme.COUNT));
        writeDataTag(stack, tag);
    }

    public static void cycleTheme(ItemStack stack) {
        setTheme(stack, getTheme(stack) + 1);
    }

    public static boolean isBlueScreenAttackEnabled(ItemStack stack) {
        return dataTag(stack).getBoolean(BLUE_SCREEN_ATTACK_KEY);
    }

    public static void setBlueScreenAttackEnabled(ItemStack stack, boolean enabled) {
        CompoundTag tag = dataTag(stack);
        tag.putBoolean(BLUE_SCREEN_ATTACK_KEY, enabled);
        writeDataTag(stack, tag);
    }

    public static boolean canBreakUnbreakableBlocks(ItemStack stack) {
        return dataTag(stack).getBoolean(BREAK_UNBREAKABLE_BLOCKS_KEY);
    }

    public static void setBreakUnbreakableBlocks(ItemStack stack, boolean enabled) {
        CompoundTag tag = dataTag(stack);
        tag.putBoolean(BREAK_UNBREAKABLE_BLOCKS_KEY, enabled);
        writeDataTag(stack, tag);
    }

    public static int getMiningSpeed(ItemStack stack) {
        CompoundTag tag = dataTag(stack);
        if (!tag.contains(MINING_SPEED_KEY)) return DEFAULT_MINING_SPEED;
        return clampMiningSpeed(tag.getInt(MINING_SPEED_KEY));
    }

    public static void setMiningSpeed(ItemStack stack, int speed) {
        CompoundTag tag = dataTag(stack);
        tag.putInt(MINING_SPEED_KEY, clampMiningSpeed(speed));
        writeDataTag(stack, tag);
    }

    public static int getOreDropMultiplier(ItemStack stack) {
        CompoundTag tag = dataTag(stack);
        if (!tag.contains(ORE_DROP_MULTIPLIER_KEY)) return DEFAULT_ORE_DROP_MULTIPLIER;
        return clampOreDropMultiplier(tag.getInt(ORE_DROP_MULTIPLIER_KEY));
    }

    public static void setOreDropMultiplier(ItemStack stack, int multiplier) {
        CompoundTag tag = dataTag(stack);
        tag.putInt(ORE_DROP_MULTIPLIER_KEY, clampOreDropMultiplier(multiplier));
        writeDataTag(stack, tag);
    }

    public static AreaClearTargetMode getAreaClearTargetMode(ItemStack stack) {
        int id = dataTag(stack).getInt(AREA_CLEAR_TARGET_MODE_KEY);
        return AreaClearTargetMode.byId(id);
    }

    public static void setAreaClearTargetMode(ItemStack stack, AreaClearTargetMode mode) {
        CompoundTag tag = dataTag(stack);
        tag.putInt(AREA_CLEAR_TARGET_MODE_KEY, mode.id());
        writeDataTag(stack, tag);
    }

    public static int getAttackDamageLimit(ItemStack stack) {
        CompoundTag tag = dataTag(stack);
        if (!tag.contains(ATTACK_DAMAGE_LIMIT_KEY)) return DEFAULT_ATTACK_DAMAGE_LIMIT;
        return clampAttackDamageLimit(tag.getInt(ATTACK_DAMAGE_LIMIT_KEY));
    }

    public static void setAttackDamageLimit(ItemStack stack, int damageLimit) {
        CompoundTag tag = dataTag(stack);
        tag.putInt(ATTACK_DAMAGE_LIMIT_KEY, clampAttackDamageLimit(damageLimit));
        writeDataTag(stack, tag);
    }

    // ===== 终极死亡 / 退出打击 =====

    /** 终极死亡：击杀玩家后将其加入排除名单，使其无法再进入服务器，直到管理员解除。 */
    public static boolean isUltimateDeathEnabled(ItemStack stack) {
        return dataTag(stack).getBoolean(ULTIMATE_DEATH_KEY);
    }

    public static void setUltimateDeathEnabled(ItemStack stack, boolean enabled) {
        CompoundTag tag = dataTag(stack);
        tag.putBoolean(ULTIMATE_DEATH_KEY, enabled);
        writeDataTag(stack, tag);
    }

    /** 退出打击：击杀玩家后立即把他踢出服务器，但仍可重新进入。 */
    public static boolean isQuitStrikeEnabled(ItemStack stack) {
        return dataTag(stack).getBoolean(QUIT_STRIKE_KEY);
    }

    public static void setQuitStrikeEnabled(ItemStack stack, boolean enabled) {
        CompoundTag tag = dataTag(stack);
        tag.putBoolean(QUIT_STRIKE_KEY, enabled);
        writeDataTag(stack, tag);
    }

    public static void setLootingMode(ItemStack stack, int mode) {
        CompoundTag tag = dataTag(stack);
        tag.putInt(LOOTING_MODE_KEY, Math.max(0, Math.min(2, mode)));
        writeDataTag(stack, tag);
    }

    /**
     * 剑是否处于"惰性"状态：伪装中，或仍是未经验证的合成成品（pending 标记）。
     * <p>
     * pending 剑必须在能力入口就被拒绝，不能只靠 {@code inventoryTick} 事后销毁 ——
     * 对抗性复核指出"捡起 → 下一 tick 销毁"之间存在窗口（ItemEntity 不 tick，
     * 同刻丢回地上还能保住标记），期间 pending 剑是一把满配武器。
     * 惰性的剑在这里一律退化成普通剑行为（等同于伪装分支的处理）。
     */
    public static boolean isInert(ItemStack stack) {
        return isDisguised(stack) || AdminSwordRecipe.isPendingAdminSword(stack);
    }

    /** 是否仍是"未验证的合成成品"（Crafter 等绕过 ItemCraftedEvent 的路径留下的标记）。 */
    public static boolean isPendingCraft(ItemStack stack) {
        return AdminSwordRecipe.isPendingAdminSword(stack);
    }

    private static int clampMiningSpeed(int speed) {
        return Math.max(MIN_MINING_SPEED, Math.min(MAX_MINING_SPEED, speed));
    }

    private static int clampOreDropMultiplier(int multiplier) {
        return Math.max(MIN_ORE_DROP_MULTIPLIER, Math.min(MAX_ORE_DROP_MULTIPLIER, multiplier));
    }

    private static int clampAttackDamageLimit(int damageLimit) {
        return Math.max(0, Math.min(MAX_ATTACK_DAMAGE_LIMIT, damageLimit));
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
        tooltipComponents.add(Component.literal("∞ ")
                .append(Component.translatable("tooltip.kunjinkao.kun_jin_kao.attack_damage").withStyle(ChatFormatting.DARK_GREEN)));
        tooltipComponents.add(Component.literal("+2.0 ")
                .append(Component.translatable("tooltip.kunjinkao.kun_jin_kao.attack_speed").withStyle(ChatFormatting.DARK_GREEN)));
        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.line_1").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.line_2").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.line_3").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.line_4").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.line_5").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.line_6").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.line_7").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.line_8").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.line_9").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.line_10").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.line_11").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.line_12").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.line_13").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.line_14").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.line_15").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.overwrite_state").withStyle(ChatFormatting.DARK_GRAY)
                .append(isOverwriteEnabled(stack)
                        ? Component.translatable("tooltip.kunjinkao.kun_jin_kao.overwrite_on").withStyle(ChatFormatting.DARK_GREEN)
                        : Component.translatable("tooltip.kunjinkao.kun_jin_kao.overwrite_off").withStyle(ChatFormatting.DARK_RED)));
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.theme").withStyle(ChatFormatting.DARK_GRAY)
                .append(Component.literal(KunJinKaoTheme.displayName(getTheme(stack))).withStyle(ChatFormatting.LIGHT_PURPLE)));
        tooltipComponents.add(Component.translatable("tooltip.kunjinkao.kun_jin_kao.theme_hint").withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (isInert(stack)) {
            return super.use(level, player, hand);
        }
        if (player.isShiftKeyDown()) {
            return cycleMode(level, player, hand);
        }
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            int removed = clearAreaTargets(serverLevel, player, stack, getAreaClearTargetMode(stack));
            player.displayClientMessage(Component.translatable("message.kunjinkao.area_clear_result", removed), true);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private static int clearAreaTargets(ServerLevel level, Player player, ItemStack stack, AreaClearTargetMode mode) {
        double halfWidth = 50.0D;
        AABB area = new AABB(player.getX() - halfWidth, level.getMinBuildHeight(), player.getZ() - halfWidth,
                player.getX() + halfWidth, level.getMaxBuildHeight(), player.getZ() + halfWidth);
        int removed = 0;
        for (Mob mob : level.getEntitiesOfClass(Mob.class, area,
                mob -> mob.isAlive() && mode.matches(mob))) {
            mob.kill();
            removed++;
        }
        if (mode.targetsPlayers()) {
            for (ServerPlayer target : level.players()) {
                if (target != player && target.isAlive() && area.contains(target.position())) {
                    // 范围清除没有伤害来源，处决开关只能通过击杀标记传递。
                    // 玩家用只含处决信息的标记，避免把对方背包按抢夺等级复制。
                    applyExecutionMark(target, stack);
                    target.kill();
                    removed++;
                }
            }
        }
        return removed;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (isInert(stack)) {
            return super.hurtEnemy(stack, target, attacker);
        }
        if (target instanceof Player) {
            // 玩家目标：砍中即秒杀。覆写流程要 40 tick，不符合"砍中即秒杀"，因此对玩家不启用。
            // 伤害上限被人为调低时按普通伤害结算，与生物目标的行为保持一致。
            if (getAttackDamageLimit(stack) < MAX_ATTACK_DAMAGE_LIMIT) {
                return super.hurtEnemy(stack, target, attacker);
            }
            if (!attacker.level().isClientSide()) {
                LOGGER.debug("[HURT-ENEMY] player target -> instant kill target={}", target.getName().getString());
                applyExecutionMark(target, stack);
                target.kill();
            }
            return true;
        }
        if (getAttackDamageLimit(stack) < MAX_ATTACK_DAMAGE_LIMIT) {
            return true;
        }
        if (!attacker.level().isClientSide()) {
            if (isOverwriteEnabled(stack)) {
                LOGGER.debug("[HURT-ENEMY] overwriteEnabled=true -> startOverwrite target={}", target.getType());
                KunJinKaoOverwriteHandler.startOverwrite(attacker, target, stack, (ServerLevel) attacker.level());
                return true;
            }
            LOGGER.debug("[HURT-ENEMY] overwriteEnabled=false -> instant kill target={}", target.getType());
            applyKunJinKaoMark(target, stack);
            target.kill();
        }
        return true;
    }

    private static final org.apache.logging.log4j.Logger LOGGER =
            org.apache.logging.log4j.LogManager.getLogger("KunJinKao");

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (isInert(stack)) {
            return super.getDestroySpeed(stack, state);
        }
        return getMiningSpeed(stack);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        if (isInert(stack)) {
            return super.isCorrectToolForDrops(stack, state);
        }
        return true;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (level.isClientSide()) {
            return;
        }
        if (stack.getDamageValue() > 0) {
            stack.setDamageValue(0);
        }
        settlePendingCraft(stack, entity);
    }

    /**
     * 结算"未验证的成品"。
     * <p>
     * 自定义配方产出的剑带 {@code kunjinkao:pending_admin_sword_craft} 标记，正常情况下由
     * {@code AdminSwordCraftingHandler} 在 {@code ItemCraftedEvent} 里处理。但 1.21 的 Crafter
     * 方块走 {@code RecipeManager.getRecipeFor} 合成，不会触发该事件，未授权玩家因此可以绕过授权拿到剑。
     * 这里在剑进入任何生物/玩家背包后逐 tick 兜底：授权玩家放行并清标记，未授权玩家的剑直接销毁。
     */
    private static void settlePendingCraft(ItemStack stack, net.minecraft.world.entity.Entity holder) {
        if (!AdminSwordRecipe.isPendingAdminSword(stack)) {
            return;
        }
        if (holder instanceof Player owner && AdminToolConfig.isAuthorized(owner.getUUID())) {
            AdminSwordRecipe.clearPendingCraftTag(stack);
            return;
        }
        stack.setCount(0);
    }

    private InteractionResultHolder<ItemStack> cycleMode(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            return InteractionResultHolder.success(stack);
        }
        CompoundTag tag = dataTag(stack);
        int currentMode = tag.getInt(LOOTING_MODE_KEY);
        int newMode = (currentMode + 1) % 3;
        setLootingMode(stack, newMode);

        Component modeText = switch (newMode) {
            case 1 -> Component.translatable("message.kunjinkao.looting_mode_25");
            case 2 -> Component.translatable("message.kunjinkao.looting_mode_50");
            default -> Component.translatable("message.kunjinkao.looting_mode_none");
        };
        player.displayClientMessage(Component.translatable("message.kunjinkao.looting_mode_current", modeText), true);
        return InteractionResultHolder.consume(stack);
    }

    public static void applyKunJinKaoMark(LivingEntity target, ItemStack stack) {
        CompoundTag data = target.getPersistentData();
        data.putBoolean(KunJinKaoDeathEventHandler.MARK_KEY, true);
        data.putInt(KunJinKaoDeathEventHandler.LOOTING_MODE_ENTITY_KEY, getLootingMode(stack));
        // 同时记下写入时刻：这次伤害可能被无敌帧整段丢弃，Entity 会带着标记继续活着，
        // 死亡处理器只认可 MARK_VALID_TICKS 内的标记，这样之后的无关死亡不会也吃到 25/50 倍掉落。
        data.putLong(KunJinKaoDeathEventHandler.MARK_TICK_KEY, target.level().getGameTime());
        data.putBoolean(KunJinKaoProtectionHandler.KILL_BY_OVERWRITE_KEY, true);
        data.putBoolean(ULTIMATE_DEATH_MARK, isUltimateDeathEnabled(stack));
        data.putBoolean(QUIT_STRIKE_MARK, isQuitStrikeEnabled(stack));
    }

    /**
     * 处决标记：只写"这次击杀已获授权"与两个处决开关，不写掉落增强标记。
     * <p>
     * 玩家目标必须用这个而不是 {@link #applyKunJinKaoMark}：
     * 后者会带上抢夺模式，而掉落增强是按抢夺等级把掉落物复制 25/50 份，
     * 用在玩家身上等于把对方整个背包复制几十份。
     */
    public static void applyExecutionMark(LivingEntity target, ItemStack stack) {
        CompoundTag data = target.getPersistentData();
        data.putBoolean(KunJinKaoProtectionHandler.KILL_BY_OVERWRITE_KEY, true);
        // 处决标记同样带时效：写标记与 target.kill() 之间可能因为无敌帧或保护逻辑没死成，
        // 过期后必须失效，否则残留的开关标记会让之后的死亡被误当作处决。
        data.putLong(KunJinKaoDeathEventHandler.MARK_TICK_KEY, target.level().getGameTime());
        data.putBoolean(ULTIMATE_DEATH_MARK, isUltimateDeathEnabled(stack));
        data.putBoolean(QUIT_STRIKE_MARK, isQuitStrikeEnabled(stack));
    }

    public static int getLootingMode(ItemStack stack) {
        int mode = dataTag(stack).getInt(LOOTING_MODE_KEY);
        return Math.max(0, Math.min(2, mode));
    }

    public enum AreaClearTargetMode {
        HOSTILE(0, "screen.kunjinkao.area_clear_hostile") {
            @Override
            boolean matches(Mob mob) { return mob instanceof Enemy; }
        },
        FRIENDLY(1, "screen.kunjinkao.area_clear_friendly") {
            @Override
            boolean matches(Mob mob) { return !(mob instanceof Enemy); }
        },
        ALL(2, "screen.kunjinkao.area_clear_all") {
            @Override
            boolean matches(Mob mob) { return true; }
        },
        PLAYERS(3, "screen.kunjinkao.area_clear_players") {
            @Override
            boolean matches(Mob mob) { return false; }
            @Override
            boolean targetsPlayers() { return true; }
        };

        private final int id;
        private final String translationKey;

        AreaClearTargetMode(int id, String translationKey) {
            this.id = id;
            this.translationKey = translationKey;
        }

        public int id() { return id; }
        public String translationKey() { return translationKey; }
        abstract boolean matches(Mob mob);
        boolean targetsPlayers() { return false; }

        public static AreaClearTargetMode byId(int id) {
            for (AreaClearTargetMode mode : values()) {
                if (mode.id == id) return mode;
            }
            return HOSTILE;
        }
    }
}