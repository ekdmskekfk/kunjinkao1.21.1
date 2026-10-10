package dev.modmind.kunjinkao.world;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.config.AdminToolConfig;
import dev.modmind.kunjinkao.qol.QolSetting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 选区挖掘（127）：把框选的一块区域分批挖空。
 * <p>
 * 这是全模组唯一一个<b>不可逆</b>的功能，所以四道保险一道都不能省：
 * 服务器总闸（默认关）、只有通过密码授权的人能用、单边上限、以及执行前的二次确认
 * （确认在 {@code KunJinKaoQolHandler} 里做，这里只管拿到"已经确认过"的请求）。
 * <p>
 * <b>分批是硬要求，不是优化。</b> 64×64×全高最多二十六万方块，一口气挖完就是一次
 * 几十秒的卡死。作业按 {@code zoneTickBudgetMs} 的时间预算切分，每 tick 只干预算内的活，
 * 于是无论选区多大，MSPT 的增量都是有上限的。
 */
public final class ZoneExcavationJob {

    private static final Logger LOGGER = LogManager.getLogger("KunJinKao");

    /**
     * 不挖的方块，做成方块标签而不是写死一串 id：服主想加自己的保护方块时
     * 只要往数据包的同名标签里追加，不用改代码也不用等模组更新。
     */
    public static final TagKey<Block> BLACKLIST = TagKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "zone_excavation_blacklist"));

    /** 掉落策略：进玩家背包。 */
    public static final int POLICY_INVENTORY = 0;
    /** 掉落策略：生成掉落物。 */
    public static final int POLICY_DROP = 1;
    /** 掉落策略：直接销毁。 */
    public static final int POLICY_DESTROY = 2;

    /** 每处理这么多方块检查一次时间预算。每块都看的话，System.nanoTime 自己就成了开销。 */
    private static final int BUDGET_CHECK_INTERVAL = 64;

    private static final Map<UUID, Job> JOBS = new HashMap<>();
    /** 每人最近一次完成的作业，供撤销用。只留一步 —— 这和放置撤销的十步不是一个量级。 */
    private static final Map<UUID, Completed> LAST_COMPLETED = new HashMap<>();

    private ZoneExcavationJob() {
    }

    private static final class Job {
        final UUID owner;
        final ServerLevel level;
        final ResourceLocation dimension;
        final BlockPos min;
        final int sizeX;
        final int sizeY;
        final int sizeZ;
        final int total;
        int cursor;
        /** 撤销快照；为 null 表示这次不记（超出上限或服主关了）。 */
        final List<PlacementUndoHistory.Change> undo;
        /** 策略 0 下真正进了背包的东西，撤销时要按这份清单收回来。 */
        final List<ItemStack> granted = new ArrayList<>();
        /** 同一 BlockState 的掉落只掷一次骰子。见 processOne 里的说明。 */
        final Map<BlockState, List<ItemStack>> dropCache = new HashMap<>();

        Job(UUID owner, ServerLevel level, BlockPos min, int sizeX, int sizeY, int sizeZ,
            int total, List<PlacementUndoHistory.Change> undo) {
            this.owner = owner;
            this.level = level;
            this.dimension = level.dimension().location();
            this.min = min;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            this.sizeZ = sizeZ;
            this.total = total;
            this.undo = undo;
        }
    }

    /** 一次已完成作业的撤销数据。 */
    private record Completed(ServerLevel level, ResourceLocation dimension,
                             List<PlacementUndoHistory.Change> changes, List<ItemStack> granted) {
    }

    public static boolean isRunning(ServerPlayer player) {
        return JOBS.containsKey(player.getUUID());
    }

    /** 作业进度：[已处理, 总数]；没在跑返回 null。 */
    public static int[] progress(ServerPlayer player) {
        Job job = JOBS.get(player.getUUID());
        return job == null ? null : new int[]{job.cursor, job.total};
    }

    /**
     * 开始一次作业。调用方必须先确认过权限与二次确认。
     *
     * @return 是否真的开始（没开始时会自己给玩家发原因）
     */
    public static boolean start(ServerPlayer player, BlockPos a, BlockPos b) {
        if (JOBS.containsKey(player.getUUID())) {
            player.displayClientMessage(Component.translatable("message.kunjinkao.zone_busy"), true);
            return false;
        }
        if (!(player.level() instanceof ServerLevel level)) {
            return false;
        }
        int maxSide = AdminToolConfig.qolServerValue(QolSetting.ZONE_MAX_SIDE);
        int sizeX = Math.abs(a.getX() - b.getX()) + 1;
        int sizeY = Math.abs(a.getY() - b.getY()) + 1;
        int sizeZ = Math.abs(a.getZ() - b.getZ()) + 1;
        if (sizeX > maxSide || sizeY > maxSide || sizeZ > maxSide) {
            player.displayClientMessage(Component.translatable("message.kunjinkao.zone_too_big", maxSide), true);
            return false;
        }
        long totalLong = (long) sizeX * sizeY * sizeZ;
        if (totalLong > Integer.MAX_VALUE) {
            player.displayClientMessage(Component.translatable("message.kunjinkao.zone_too_big", maxSide), true);
            return false;
        }
        int total = (int) totalLong;

        int undoCap = AdminToolConfig.qolServerValue(QolSetting.ZONE_UNDO_MAX);
        List<PlacementUndoHistory.Change> undo =
                undoCap > 0 && total <= undoCap ? new ArrayList<>(total) : null;
        if (undo == null) {
            // 明确告知，而不是默默不记 —— 玩家按了撤销却发现没反应才是最糟的。
            player.displayClientMessage(Component.translatable("message.kunjinkao.zone_no_undo"), true);
        }

        BlockPos min = new BlockPos(Math.min(a.getX(), b.getX()),
                Math.min(a.getY(), b.getY()), Math.min(a.getZ(), b.getZ()));
        JOBS.put(player.getUUID(), new Job(player.getUUID(), level, min, sizeX, sizeY, sizeZ, total, undo));
        player.displayClientMessage(Component.translatable("message.kunjinkao.zone_started", total), true);
        return true;
    }

    /**
     * 推进作业。由拥有者自己的 {@code PlayerTickEvent.Post} 驱动。
     * <p>
     * 用拥有者的 tick 而不是全局 ServerTickEvent：作业本来就只在他在线时有意义，
     * 挂在他身上，退出登录自然就停了，不需要额外写一套"玩家离线要清理什么"的逻辑。
     */
    public static void tick(ServerPlayer player) {
        Job job = JOBS.get(player.getUUID());
        if (job == null) {
            return;
        }
        int budgetMs = AdminToolConfig.qolServerValue(QolSetting.ZONE_TICK_BUDGET);
        int batch = AdminToolConfig.qolServerValue(QolSetting.ZONE_BATCH_SIDE);
        int policy = AdminToolConfig.qolServerValue(QolSetting.ZONE_DROP_POLICY);
        long deadline = System.nanoTime() + budgetMs * 1_000_000L;

        int processed = 0;
        while (job.cursor < job.total && processed < batch) {
            processOne(job, player, policy);
            job.cursor++;
            processed++;
            if (processed % BUDGET_CHECK_INTERVAL == 0 && System.nanoTime() >= deadline) {
                break;
            }
        }
        if (job.cursor >= job.total) {
            finish(job, player);
        }
    }

    private static void processOne(Job job, ServerPlayer player, int policy) {
        int index = job.cursor;
        int x = index % job.sizeX;
        int z = (index / job.sizeX) % job.sizeZ;
        int y = index / (job.sizeX * job.sizeZ);
        BlockPos pos = job.min.offset(x, y, z);

        BlockState state = job.level.getBlockState(pos);
        if (state.isAir() || state.is(BLACKLIST)) {
            return;
        }
        BlockEntity blockEntity = job.level.getBlockEntity(pos);

        // 掉落骰子按 BlockState 缓存，只在"没有方块实体"时用。
        // 带方块实体的方块（箱子、桶）掉落取决于里面的东西，按状态缓存会把第一个箱子的
        // 内容复制给后面所有同状态的箱子 —— 那是刷物品，不是优化。
        List<ItemStack> drops;
        if (blockEntity == null) {
            drops = job.dropCache.computeIfAbsent(state,
                    cached -> Block.getDrops(cached, job.level, pos, null, player, player.getMainHandItem()));
        } else {
            drops = Block.getDrops(state, job.level, pos, blockEntity, player, player.getMainHandItem());
        }

        if (job.undo != null) {
            // returned 一律为空：挖掘不"退还"任何东西，撤销时该做的是把已经拿到手的收回来，
            // 那件事由下面的 granted 清单负责，不走 PlacementUndoHistory 的退还通道。
            job.undo.add(new PlacementUndoHistory.Change(pos.immutable(), state, ItemStack.EMPTY));
        }

        job.level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

        if (policy == POLICY_DESTROY) {
            return;
        }
        for (ItemStack drop : drops) {
            if (drop.isEmpty()) {
                continue;
            }
            ItemStack copy = drop.copy();
            if (policy == POLICY_INVENTORY) {
                int before = copy.getCount();
                if (!player.getInventory().add(copy)) {
                    player.drop(copy, false);
                }
                int added = before - copy.getCount();
                if (added > 0) {
                    job.granted.add(drop.copyWithCount(added));
                }
            } else {
                Block.popResource(job.level, pos, copy);
            }
        }
    }

    private static void finish(Job job, ServerPlayer player) {
        JOBS.remove(job.owner);
        if (job.undo != null && !job.undo.isEmpty()) {
            LAST_COMPLETED.put(job.owner, new Completed(job.level, job.dimension, job.undo, job.granted));
        }
        player.displayClientMessage(Component.translatable("message.kunjinkao.zone_done", job.total), true);
        LOGGER.info("[Kunjinkao] 选区挖掘完成：{} 格，玩家 {}", job.total, player.getName().getString());
    }

    /** 取消正在跑的作业。已经挖掉的部分不会还原（要还原请用撤销）。 */
    public static boolean cancel(ServerPlayer player) {
        Job job = JOBS.remove(player.getUUID());
        if (job == null) {
            return false;
        }
        player.displayClientMessage(Component.translatable("message.kunjinkao.zone_cancelled", job.cursor), true);
        return true;
    }

    /**
     * 撤销最近一次完成的选区挖掘。
     * <p>
     * 比放置撤销多一步：把当时塞进背包的东西按清单收回来。不收的话，
     * 撤销就成了刷物品 —— 地形回来了，东西还在手里。
     * <p>
     * 收不回来时（玩家已经把东西用掉/丢掉）只还原地形并如实提示，
     * 不做"反正是管理员"这种假设。
     *
     * @return 还原的方块数；没有可撤销的作业返回 0
     */
    public static int undoLast(ServerPlayer player) {
        Completed completed = LAST_COMPLETED.remove(player.getUUID());
        if (completed == null) {
            return 0;
        }
        if (!(player.level() instanceof ServerLevel level)
                || !level.dimension().location().equals(completed.dimension())) {
            // 换了维度：坐标对不上，直接作废而不是在错误的世界里乱挖。
            player.displayClientMessage(Component.translatable("message.kunjinkao.zone_undo_wrong_dim"), true);
            return 0;
        }
        int restored = 0;
        List<PlacementUndoHistory.Change> changes = completed.changes();
        for (int i = changes.size() - 1; i >= 0; i--) {
            PlacementUndoHistory.Change change = changes.get(i);
            level.setBlock(change.pos(), change.previousState(), 3);
            restored++;
        }
        int missing = revokeGranted(player, completed.granted());
        if (missing > 0) {
            player.displayClientMessage(Component.translatable("message.kunjinkao.zone_undo_partial", missing), true);
        }
        player.displayClientMessage(Component.translatable("message.kunjinkao.zone_undo_done", restored), true);
        return restored;
    }

    /** 按清单从背包里收回物品；返回没能收回的件数。 */
    private static int revokeGranted(ServerPlayer player, List<ItemStack> granted) {
        int missing = 0;
        for (ItemStack want : granted) {
            int remaining = want.getCount();
            for (int slot = 0; slot < player.getInventory().getContainerSize() && remaining > 0; slot++) {
                ItemStack inSlot = player.getInventory().getItem(slot);
                if (inSlot.isEmpty() || !ItemStack.isSameItemSameComponents(inSlot, want)) {
                    continue;
                }
                int take = Math.min(remaining, inSlot.getCount());
                inSlot.shrink(take);
                remaining -= take;
            }
            missing += remaining;
        }
        player.getInventory().setChanged();
        return missing;
    }

    /** 玩家退出时清掉作业与撤销记录。 */
    public static void forget(UUID uuid) {
        JOBS.remove(uuid);
        LAST_COMPLETED.remove(uuid);
    }
}