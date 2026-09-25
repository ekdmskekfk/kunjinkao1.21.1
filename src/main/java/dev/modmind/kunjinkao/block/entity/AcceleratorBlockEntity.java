package dev.modmind.kunjinkao.block.entity;

import dev.modmind.kunjinkao.AcceleratorRegistry;
import dev.modmind.kunjinkao.config.AdminToolConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * 时间加速器方块实体。
 * <p>
 * 每个游戏刻按设定倍率对周围范围内的方块进行"加速"：
 * <ul>
 *   <li>有方块实体（熔炉、漏斗、信标等机器）的方块，额外调用其原版 ticker 若干次；</li>
 *   <li>没有方块实体但会随机刻（作物、树苗、草方块等）的方块，额外调用其 randomTick 若干次。</li>
 * </ul>
 * 倍率与范围通过右键打开的 GUI 调节，并持久化到 NBT。
 */
public class AcceleratorBlockEntity extends BlockEntity {

    /** 可选加速倍率：4 / 8 / 16 / 32 / 64 / 128 / 256 / 512 / 1024 倍。 */
    public static final int[] MULTIPLIERS = {4, 8, 16, 32, 64, 128, 256, 512, 1024};

    /** 可选范围（半径的一半）：1 → 3x3x3，2 → 5x5x5，3 → 7x7x7，4 → 9x9x9。 */
    public static final int[] RADII = {1, 2, 3, 4};

    /**
     * 每个方块实体每刻允许执行的额外 tick 次数上限。
     * <p>
     * 1024 倍 × 9×9×9 理论上每刻要做约 74.5 万次 tick，足以拖垮 TPS。这里给出硬预算：
     * 预算耗尽后本刻不再继续加速（下一 tick 继续），把最坏情况限制在可控范围内。
     */
    public static final int MAX_EXTRA_TICKS_PER_TICK = 4096;

    /** 允许修改配置的最大距离（格）的平方：约 8 格。 */
    public static final double MAX_EDIT_DISTANCE_SQR = 64.0D;

    /** 过滤列表槽位数（GUI 里排成一行）。 */
    public static final int FILTER_SLOTS = 9;

    private static final String TAG_MULTIPLIER = "Multiplier";
    private static final String TAG_RADIUS = "Radius";
    private static final String TAG_SHOW_RANGE = "ShowRange";
    private static final String TAG_FILTER = "Filter";
    private static final String TAG_WHITELIST = "FilterWhitelist";
    private static final String TAG_MATCH_NBT = "FilterMatchNbt";

    private int multiplier = MULTIPLIERS[0];
    private int radius = RADII[0];
    private boolean showRange = false;

    /** 过滤列表：只放方块物品；空槽为 EMPTY。列表里一个都没有时视为"不过滤"。 */
    private final NonNullList<ItemStack> filter = NonNullList.withSize(FILTER_SLOTS, ItemStack.EMPTY);
    /** true = 白名单（只加速命中的），false = 黑名单（命中的不加速）。 */
    private boolean whitelist = true;
    /** 是否要求命中项的 NBT 与过滤项记录的基准一致。 */
    private boolean matchNbt = false;

    public AcceleratorBlockEntity(BlockPos pos, BlockState state) {
        super(AcceleratorRegistry.ACCELERATOR_BE.get(), pos, state);
    }

    /** 由方块 {@code getTicker} 注册的服务端 ticker。 */
    public static void serverTick(Level level, BlockPos pos, BlockState state, AcceleratorBlockEntity self) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        self.accelerateSurroundings(serverLevel, pos);
    }

    private void accelerateSurroundings(ServerLevel serverLevel, BlockPos pos) {
        accelerateArea(serverLevel, pos, this.radius, this.multiplier, this);
    }

    /**
     * 对一个立方区域做时间加速：区域内每个方块被额外 tick（倍率 - 1）次。
     * <p>
     * 独立成公开静态方法，是为了让剑的"时间加速"复用<b>同一套</b>逻辑 ——
     * 否则两份实现早晚会在每刻预算与过滤器判定上跑偏。
     *
     * @param filterOwner 持过滤器的一方；传 {@code null} 表示不过滤（剑的加速没有过滤列表）
     */
    public static void accelerateArea(ServerLevel serverLevel, BlockPos pos, int radius, int multiplier,
                                      @Nullable AcceleratorBlockEntity filterOwner) {
        int half = radius;
        int extra = multiplier - 1;
        if (extra <= 0) {
            return;
        }
        RandomSource random = serverLevel.random;
        int budget = MAX_EXTRA_TICKS_PER_TICK;

        for (int dx = -half; dx <= half; dx++) {
            for (int dy = -half; dy <= half; dy++) {
                for (int dz = -half; dz <= half; dz++) {
                    if (budget <= 0) {
                        return;
                    }
                    BlockPos target = pos.offset(dx, dy, dz);
                    if (target.equals(pos)) {
                        continue;
                    }
                    BlockState targetState = serverLevel.getBlockState(target);
                    if (targetState.isAir()) {
                        continue;
                    }
                    BlockEntity targetEntity = serverLevel.getBlockEntity(target);
                    // 过滤：列表为空直接放行；否则按黑白名单决定是否加速
                    if (filterOwner != null && !filterOwner.matchesFilter(serverLevel, targetState, targetEntity)) {
                        continue;
                    }
                    if (targetEntity != null) {
                        budget -= tickBlockEntityExtra(serverLevel, target, targetState, targetEntity, extra, budget);
                    } else if (targetState.isRandomlyTicking()) {
                        int calls = Math.min(extra, budget);
                        for (int i = 0; i < calls; i++) {
                            targetState.randomTick(serverLevel, target, random);
                        }
                        budget -= calls;
                    }
                }
            }
        }
    }

    /**
     * 这个位置"能不能被加速"：有会 tick 的方块实体，或者方块本身会随机 tick。
     * <p>
     * 判定标准与 {@link #accelerateArea} 实际会处理的对象完全一致 ——
     * 所以"可以加速的东西"在剑和加速器两边含义相同，不会出现
     * "右键说命中了、实际却什么都没加速"的情况。
     */
    public static boolean isAcceleratable(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) {
            return false;
        }
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity == null) {
            return state.isRandomlyTicking();
        }
        // 加速器自己不再被加速：tickBlockEntityExtra 里同样把它排除掉了。
        if (entity instanceof AcceleratorBlockEntity) {
            return false;
        }
        return state.getBlock() instanceof EntityBlock entityBlock
                && entityBlock.getTicker(level, state, entity.getType()) != null;
    }

    /**
     * 额外调用目标方块实体的原版 ticker（使用原始类型规避泛型捕获问题）。
     *
     * @return 实际执行的 tick 次数，供调用方扣减每刻预算
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static int tickBlockEntityExtra(Level level, BlockPos pos, BlockState state, BlockEntity entity, int extra, int budget) {
        if (entity instanceof AcceleratorBlockEntity) {
            return 0;
        }
        Block block = state.getBlock();
        if (!(block instanceof EntityBlock entityBlock)) {
            return 0;
        }
        BlockEntityTicker ticker = entityBlock.getTicker(level, state, entity.getType());
        if (ticker == null) {
            return 0;
        }
        int calls = Math.min(extra, budget);
        for (int i = 0; i < calls; i++) {
            ticker.tick(level, pos, state, entity);
        }
        return calls;
    }

    /**
     * 加速方块配置包的共用闸门：授权 + 交互距离。
     * <p>
     * 加速倍率直接决定方块 tick 次数，是"能拖垮服务器"的配置，因此只允许通过管理员验证的玩家修改；
     * 距离校验用于阻止改造客户端远程改写任意已加载区块里的加速器。
     *
     * @return true 表示允许继续修改
     */
    public static boolean mayEdit(ServerPlayer player, BlockPos pos) {
        if (!AdminToolConfig.isAuthorized(player.getUUID())) {
            player.displayClientMessage(Component.translatable("message.kunjinkao.admin_required"), true);
            return false;
        }
        return player.distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(pos)) <= MAX_EDIT_DISTANCE_SQR;
    }


    public ItemStack getFilterEntry(int slot) {
        return slot >= 0 && slot < FILTER_SLOTS ? this.filter.get(slot) : ItemStack.EMPTY;
    }

    /** 过滤列表里是否至少有一项。 */
    public boolean hasFilterEntries() {
        for (int i = 0; i < FILTER_SLOTS; i++) {
            if (!this.filter.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean isWhitelist() {
        return this.whitelist;
    }

    public boolean isMatchNbt() {
        return this.matchNbt;
    }

    /**
     * 服务端校验后写入整份过滤状态（客户端发来的列表不可信）。
     * <p>
     * 只接受方块物品、数量归 1、只取前 {@link #FILTER_SLOTS} 项；返回是否真的有改动。
     */
    public boolean applyFilter(java.util.List<ItemStack> incoming, boolean whitelist, boolean matchNbt) {
        boolean changed = false;
        for (int i = 0; i < FILTER_SLOTS; i++) {
            ItemStack wanted = i < incoming.size() ? incoming.get(i) : ItemStack.EMPTY;
            ItemStack sanitized = ItemStack.EMPTY;
            if (!wanted.isEmpty() && wanted.getItem() instanceof BlockItem) {
                sanitized = wanted.copy();
                sanitized.setCount(1);
            }
            if (!ItemStack.matches(this.filter.get(i), sanitized)) {
                this.filter.set(i, sanitized);
                changed = true;
            }
        }
        if (this.whitelist != whitelist) {
            this.whitelist = whitelist;
            changed = true;
        }
        if (this.matchNbt != matchNbt) {
            this.matchNbt = matchNbt;
            changed = true;
        }
        return changed;
    }

    /** 过滤项记录的 NBT 基准（方块物品的 block_entity_data 组件）；没有记录则返回 null。 */
    private static CompoundTag filterNbtBasis(ItemStack entry) {
        CustomData data = entry.get(DataComponents.BLOCK_ENTITY_DATA);
        return data == null ? null : data.copyTag();
    }

    /**
     * 目标方块是否通过过滤。
     * <p>
     * 语义（与 GUI 上的两个开关对应）：
     * <ul>
     *   <li>列表为空 → 一律通过（不过滤）；</li>
     *   <li>白名单 → 只有命中列表的才加速；黑名单 → 命中列表的反而不加速；</li>
     *   <li>开启"匹配 NBT"时，命中项还要求目标方块实体的 NBT 与过滤项记录的基准**完全相同**；
     *       过滤项本身没有记录 NBT 时只按方块类型判定（该项不受开关影响）。</li>
     * </ul>
     */
    public boolean matchesFilter(Level level, BlockState state, BlockEntity targetEntity) {
        if (!hasFilterEntries()) {
            return true;
        }
        boolean matched = false;
        for (int i = 0; i < FILTER_SLOTS; i++) {
            ItemStack entry = this.filter.get(i);
            if (entry.isEmpty() || !(entry.getItem() instanceof BlockItem blockItem)) {
                continue;
            }
            if (blockItem.getBlock() != state.getBlock()) {
                continue;
            }
            if (this.matchNbt) {
                CompoundTag basis = filterNbtBasis(entry);
                if (basis != null) {
                    if (targetEntity == null) {
                        continue;
                    }
                    CompoundTag actual = targetEntity.saveWithoutMetadata(level.registryAccess());
                    if (!basis.equals(actual)) {
                        continue;
                    }
                }
            }
            matched = true;
            break;
        }
        return this.whitelist == matched;
    }

    public int getMultiplier() {
        return this.multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = clampMultiplier(multiplier);
    }

    /** 返回范围半径的一半（1..4），对应 3x3x3 ~ 9x9x9。 */
    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = clampRadius(radius);
    }

    /** 是否显示蓝色半透明加速范围框。 */
    public boolean shouldShowRange() {
        return this.showRange;
    }

    public void setShowRange(boolean showRange) {
        this.showRange = showRange;
    }

    public static int clampMultiplier(int value) {
        for (int candidate : MULTIPLIERS) {
            if (candidate == value) {
                return value;
            }
        }
        return MULTIPLIERS[0];
    }

    public static int clampRadius(int value) {
        for (int candidate : RADII) {
            if (candidate == value) {
                return value;
            }
        }
        return RADII[0];
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt(TAG_MULTIPLIER, this.multiplier);
        tag.putInt(TAG_RADIUS, this.radius);
        tag.putBoolean(TAG_SHOW_RANGE, this.showRange);
        ListTag filterTag = new ListTag();
        for (int i = 0; i < FILTER_SLOTS; i++) {
            ItemStack entry = this.filter.get(i);
            if (entry.isEmpty()) {
                continue;
            }
            CompoundTag slotTag = new CompoundTag();
            slotTag.putByte("Slot", (byte) i);
            slotTag.put("Item", entry.save(registries));
            filterTag.add(slotTag);
        }
        tag.put(TAG_FILTER, filterTag);
        tag.putBoolean(TAG_WHITELIST, this.whitelist);
        tag.putBoolean(TAG_MATCH_NBT, this.matchNbt);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.multiplier = clampMultiplier(tag.getInt(TAG_MULTIPLIER));
        this.radius = clampRadius(tag.getInt(TAG_RADIUS));
        this.showRange = tag.getBoolean(TAG_SHOW_RANGE);
        for (int i = 0; i < FILTER_SLOTS; i++) {
            this.filter.set(i, ItemStack.EMPTY);
        }
        ListTag filterTag = tag.getList(TAG_FILTER, CompoundTag.TAG_COMPOUND);
        for (int index = 0; index < filterTag.size(); index++) {
            CompoundTag slotTag = filterTag.getCompound(index);
            int slot = slotTag.getByte("Slot") & 0xFF;
            if (slot >= FILTER_SLOTS) {
                continue;
            }
            this.filter.set(slot, ItemStack.parseOptional(registries, slotTag.getCompound("Item")));
        }
        this.whitelist = !tag.contains(TAG_WHITELIST) || tag.getBoolean(TAG_WHITELIST);
        this.matchNbt = tag.getBoolean(TAG_MATCH_NBT);
    }
}