package dev.modmind.kunjinkao.block.entity;

import dev.modmind.kunjinkao.AcceleratorRegistry;
import dev.modmind.kunjinkao.config.AdminToolConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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

    private static final String TAG_MULTIPLIER = "Multiplier";
    private static final String TAG_RADIUS = "Radius";
    private static final String TAG_SHOW_RANGE = "ShowRange";

    private int multiplier = MULTIPLIERS[0];
    private int radius = RADII[0];
    private boolean showRange = false;

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
        int half = this.radius;
        int extra = this.multiplier - 1;
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
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.multiplier = clampMultiplier(tag.getInt(TAG_MULTIPLIER));
        this.radius = clampRadius(tag.getInt(TAG_RADIUS));
        this.showRange = tag.getBoolean(TAG_SHOW_RANGE);
    }
}