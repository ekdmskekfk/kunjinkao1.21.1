package dev.modmind.kunjinkao.block;

import dev.modmind.kunjinkao.SwordRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 时间加速器方块实体：保存加速倍率与加速范围，并在服务端刻中对周围方块进行加速。
 */
public class TimeAcceleratorBlockEntity extends BlockEntity {

    /** 可选加速倍率：4/8/16/32/64/128/256/512/1024 */
    public static final int[] MULTIPLIERS = {4, 8, 16, 32, 64, 128, 256, 512, 1024};
    /** 可选加速范围（立方体边长）：3/5/7/9 */
    public static final int[] SIZES = {3, 5, 7, 9};

    private int multiplierIndex = 0; // 默认 4 倍
    private int sizeIndex = 0;       // 默认 3x3x3

    public TimeAcceleratorBlockEntity(BlockPos pos, BlockState state) {
        super(SwordRegistry.TIME_ACCELERATOR_BE.value(), pos, state);
    }

    public int getMultiplier() {
        return MULTIPLIERS[Math.floorMod(multiplierIndex, MULTIPLIERS.length)];
    }

    public int getSize() {
        return SIZES[Math.floorMod(sizeIndex, SIZES.length)];
    }

    public int getMultiplierIndex() {
        return multiplierIndex;
    }

    public int getSizeIndex() {
        return sizeIndex;
    }

    public void setMultiplierIndex(int index) {
        multiplierIndex = Math.floorMod(index, MULTIPLIERS.length);
        setChanged();
        sync();
    }

    public void setSizeIndex(int index) {
        sizeIndex = Math.floorMod(index, SIZES.length);
        setChanged();
        sync();
    }

    private void sync() {
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    /**
     * 服务端每刻调用：对范围内方块执行（倍率-1）次额外随机刻与额外方块实体刻。
     */
    public void tickServer() {
        if (level == null || level.isClientSide()) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel) level;
        int multiplier = getMultiplier();
        if (multiplier <= 1) {
            return;
        }
        int extra = multiplier - 1;
        int radius = (getSize() - 1) / 2;
        RandomSource random = serverLevel.random;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos p = worldPosition.offset(dx, dy, dz);
                    BlockState s = level.getBlockState(p);
                    // 额外随机刻：作物生长、树苗、草/菌丝传播、冰霜等
                    if (s.isRandomlyTicking()) {
                        for (int i = 0; i < extra; i++) {
                            s.randomTick(serverLevel, p, random);
                        }
                    }
                    // 额外方块实体刻：熔炉/漏斗/刷怪笼/酿造台等（跳过自身）
                    if (!p.equals(worldPosition)) {
                        BlockEntity be = level.getBlockEntity(p);
                        if (be != null) {
                            @SuppressWarnings({"unchecked", "rawtypes"})
                            BlockEntityTicker ticker = s.getTicker(level, be.getType());
                            if (ticker != null) {
                                for (int i = 0; i < extra; i++) {
                                    ticker.tick(level, p, s, be);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("MultiplierIndex", multiplierIndex);
        tag.putInt("SizeIndex", sizeIndex);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        multiplierIndex = Math.floorMod(tag.getInt("MultiplierIndex"), MULTIPLIERS.length);
        sizeIndex = Math.floorMod(tag.getInt("SizeIndex"), SIZES.length);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.handleUpdateTag(tag, registries);
        loadAdditional(tag, registries);
    }
}