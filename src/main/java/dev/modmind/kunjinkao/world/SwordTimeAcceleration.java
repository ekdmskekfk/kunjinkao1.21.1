package dev.modmind.kunjinkao.world;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.block.entity.AcceleratorBlockEntity;
import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.network.TimeAccelStatusPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 剑的时间加速。
 * <p>
 * 两种用法：
 * <ul>
 *   <li><b>shift + 右键方块</b>：在那个位置立起一个局部加速场，额外 tick 范围内的
 *       方块实体与<b>生物</b>（{@link AcceleratorBlockEntity#accelerateArea} 那套逻辑，
 *       与加速方块共用同一个每刻预算）；</li>
 *   <li><b>shift + 右键天空</b>：加速<b>整个世界的时间</b> —— 走原版 {@code /tick rate} 的
 *       {@code TickRateManager}，把每秒 tick 数整体调高。</li>
 * </ul>
 * 后者是唯一能让 <b>AE2 这类"按游戏时间算进度"的机器</b>真正提速的办法：
 * 那些机器在同一个游戏刻里被多调几次 ticker 也不会前进（它们拿 gameTime 做差值，
 * 同刻内差值为 0），只有让 gameTime 真正走快才有效。所以局部加速负责"某一处"，
 * 时间加速负责"全都快"。
 * <p>
 * 两种模式（在 . 菜单的「加速」分区里选）：
 * <ul>
 *   <li><b>限时加速</b>：{@link #TIMED_DURATION_MILLIS}（30 秒）后自动结束；</li>
 *   <li><b>无限加速</b>：一直持续，直到再 shift+右键 同一位置手动停掉。</li>
 * </ul>
 * 计时用墙上时钟而不是 gameTime：时间加速本身会改变 gameTime 的推进速度，
 * 用它计时会让"30 秒"变成不确定的长度。
 */
@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class SwordTimeAcceleration {

    /** 限时加速的持续时间：30 秒（墙上时钟）。 */
    public static final long TIMED_DURATION_MILLIS = 30_000L;
    /**
     * 机器加速只作用于<b>点中的那一格</b>，不扩散到周围 —— 一个加速场就是一台机器。
     */
    public static final int BLOCK_RADIUS = 0;
    /**
     * 生物加速的作用半径。
     * <p>
     * 与机器不同，这里保留一点范围：{@code useOn} 只能点方块，玩家没法"点中一只鸡"，
     * 所以按点到的那一格为中心取一个小范围，站在那儿的生物才会被加速。
     */
    public static final int ENTITY_RADIUS = 2;
    /** 时间加速把 tick 率压到的上限，避免把服务器直接跑爆（400 tps = 20 倍速）。 */
    public static final float MAX_TICKRATE = 400.0F;
    /** 加速场状态广播间隔（tick）。10 tick = 0.5 秒，客户端的倒计时读起来是连续的。 */
    private static final int STATUS_INTERVAL_TICKS = 10;
    /** 原版基准 tick 率。 */
    public static final float BASE_TICKRATE = 20.0F;

    /** 加速模式：关。 */
    public static final int MODE_OFF = 0;
    /** 加速模式：限时，shift+右键 后持续 30 秒。 */
    public static final int MODE_TIMED = 1;
    /** 加速模式：无限，直到手动停掉。 */
    public static final int MODE_INFINITE = 2;
    /** 模式取值个数，用于循环切换。 */
    public static final int MODE_COUNT = 3;

    /**
     * 一个加速场。
     *
     * @param pos           局部加速场的中心；{@code null} 表示这是全局时间加速
     * @param multiplier    倍率
     * @param expiresAt     结束时刻（墙上时钟毫秒），负值表示无限
     * @param dimension     所在维度；时间加速用主世界做代表
     */
    private record Session(@Nullable BlockPos pos, int multiplier, long expiresAt,
                           net.minecraft.resources.ResourceKey<Level> dimension) {
        boolean isTimeAcceleration() {
            return pos == null;
        }

        boolean expired(long now) {
            return expiresAt >= 0L && now >= expiresAt;
        }
    }

    private static final Map<Long, Session> BLOCK_SESSIONS = new HashMap<>();
    private static final List<Session> TIME_SESSIONS = new ArrayList<>();
    /** 开启时间加速之前的 tick 率，停掉时恢复。 */
    private static float previousTickrate = BASE_TICKRATE;
    /** 状态广播节拍计数，见 {@link #STATUS_INTERVAL_TICKS}。 */
    private static int statusTicker;

    private SwordTimeAcceleration() {
    }

    public static int clampMode(int mode) {
        return mode < 0 || mode >= MODE_COUNT ? MODE_OFF : mode;
    }

    /** 在倍率档位表里前后挪一格。倍率是固定档位（4/8/…/1024），滚轮走的是"档"。 */
    public static int stepMultiplier(int current, int direction) {
        int[] table = AcceleratorBlockEntity.MULTIPLIERS;
        int index = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] == current) {
                index = i;
                break;
            }
        }
        index = Math.max(0, Math.min(table.length - 1, index + (direction > 0 ? 1 : -1)));
        return table[index];
    }

    /** 菜单里的"循环切倍率"：到最大档之后回到最小档。滚轮用的是 {@link #stepMultiplier}，到顶即停。 */
    public static int nextMultiplier(int current) {
        int[] table = AcceleratorBlockEntity.MULTIPLIERS;
        int index = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] == current) {
                index = i;
                break;
            }
        }
        return table[(index + 1) % table.length];
    }

    private static long blockKey(net.minecraft.resources.ResourceKey<Level> dimension, BlockPos pos) {
        return ((long) dimension.location().hashCode() << 32) ^ (pos.asLong() & 0xFFFFFFFFL);
    }

    // ===================== 入口 =====================

    /**
     * shift + 右键<b>方块</b>：在该位置开/关一个局部加速场。
     *
     * @return 是否处理了这次右键
     */
    public static boolean tryToggleBlock(Player player, Level level, BlockPos pos, ItemStack stack) {
        int mode = clampMode(KunJinKaoSwordItem.getTimeAccelMode(stack));
        if (mode == MODE_OFF || !AcceleratorBlockEntity.isAcceleratable(level, pos)) {
            return false;
        }
        if (level instanceof ServerLevel serverLevel) {
            MinecraftServer server = serverLevel.getServer();
            captureBaseline(server);
            Session previous = BLOCK_SESSIONS.remove(blockKey(serverLevel.dimension(), pos));
            if (previous == null) {
                startBlockSession(serverLevel, pos, KunJinKaoSwordItem.getTimeAccelMultiplier(stack), mode);
            }
            refreshTickrate(server);
            player.displayClientMessage(Component.translatable(previous != null
                    ? "message.kunjinkao.time_accel_stopped"
                    : mode == MODE_TIMED
                            ? "message.kunjinkao.time_accel_started_timed"
                            : "message.kunjinkao.time_accel_started_infinite"), true);
        }
        return true;
    }

    /**
     * shift + 右键<b>天空</b>：整体提高服务器的每秒 tick 数，也就是加速时间。
     * <p>
     * 这是唯一能真正带动 AE2 那类"按 gameTime 算进度"的机器的办法。
     *
     * @return 是否处理了这次右键
     */
    public static boolean tryToggleTime(Player player, MinecraftServer server, ItemStack stack) {
        int mode = clampMode(KunJinKaoSwordItem.getTimeAccelMode(stack));
        if (mode == MODE_OFF) {
            return false;
        }
        boolean stopping = !TIME_SESSIONS.isEmpty();
        captureBaseline(server);
        if (stopping) {
            TIME_SESSIONS.clear();
            refreshTickrate(server);
            player.displayClientMessage(Component.translatable("message.kunjinkao.time_accel_time_stopped"), true);
        } else {
            int multiplier = KunJinKaoSwordItem.getTimeAccelMultiplier(stack);
            TIME_SESSIONS.add(new Session(null, multiplier, expiresAt(mode), server.overworld().dimension()));
            refreshTickrate(server);
            player.displayClientMessage(Component.translatable("message.kunjinkao.time_accel_time_started",
                    (int) targetTickrate(multiplier)), true);
        }
        return true;
    }

    private static long expiresAt(int mode) {
        return mode == MODE_TIMED ? System.currentTimeMillis() + TIMED_DURATION_MILLIS : -1L;
    }

    private static float targetTickrate(int multiplier) {
        return Math.min(BASE_TICKRATE * multiplier, MAX_TICKRATE);
    }

    private static void applyTickrate(MinecraftServer server, float tickrate) {
        // 原版 setTickRate 会自动同步给客户端（ServerTickRateManager 里会发包），
        // 所以这里不需要自己再补一条 S2C。
        server.tickRateManager().setTickRate(tickrate);
    }

    /** 建立第一个加速场之前先记下原本的 tick 率，之后全部结束时要复原成它。 */
    private static void captureBaseline(MinecraftServer server) {
        if (BLOCK_SESSIONS.isEmpty() && TIME_SESSIONS.isEmpty()) {
            previousTickrate = server.tickRateManager().tickrate();
        }
    }

    /**
     * 按当前所有加速场重算目标 tick 率。
     * <p>
     * 只有 {@code TIME_SESSIONS}（对着天空的那种）参与计算。局部加速是"某一台机器多跑几轮"，
     * 与全局时间无关 —— AE2 的机器本身注册了原版 ticker，多调几次就会真的前进，
     * 不需要靠拔快整个世界的时间来带。
     * 一个加速场都不剩时复原成介入之前的 tick 率。
     */
    private static void refreshTickrate(MinecraftServer server) {
        if (BLOCK_SESSIONS.isEmpty() && TIME_SESSIONS.isEmpty()) {
            applyTickrate(server, previousTickrate);
            return;
        }
        // 只有"时间加速"才动 tick 率。局部加速是单台机器的额外 tick，
        // 不该把整个世界的时间一起拔快 —— 那是另一件事，由对着天空的加速负责。
        float desired = BASE_TICKRATE;
        for (Session session : TIME_SESSIONS) {
            desired = Math.max(desired, targetTickrate(session.multiplier()));
        }
        applyTickrate(server, desired);
    }

    private static void startBlockSession(ServerLevel level, BlockPos pos, int multiplier, int mode) {
        BLOCK_SESSIONS.put(blockKey(level.dimension(), pos),
                new Session(pos.immutable(), multiplier, expiresAt(mode), level.dimension()));
    }

    /** 该位置当前是否有局部加速场。 */
    public static boolean isActive(Level level, BlockPos pos) {
        return BLOCK_SESSIONS.containsKey(blockKey(level.dimension(), pos));
    }

    /** 当前加速场数量，用于诊断。 */
    public static int activeCount() {
        return BLOCK_SESSIONS.size() + TIME_SESSIONS.size();
    }

    // ===================== 每刻推进 =====================

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        long now = System.currentTimeMillis();
        MinecraftServer server = event.getServer();
        expireTimedSessions(server, now);
        tickBlockSessions(server);
        if (++statusTicker >= STATUS_INTERVAL_TICKS) {
            statusTicker = 0;
            broadcastStatus(server);
        }
    }

    /**
     * 把当前加速场推给客户端，用于在方块上方画悬浮提示。
     * <p>
     * 每个维度都发一份，哪怕那一维没有加速场 —— 空列表就是"清空提示"的信号。
     * 否则最后一个加速场结束后，客户端屏幕上的倒计时会一直挂着不消失。
     * 包体很小（没有加速场时只有一个 VarInt），按固定间隔心跳式发送最省心。
     */
    private static void broadcastStatus(MinecraftServer server) {
        long now = System.currentTimeMillis();
        Map<net.minecraft.server.level.ServerLevel, List<TimeAccelStatusPayload.Entry>> byLevel = new HashMap<>();
        for (Session session : BLOCK_SESSIONS.values()) {
            if (session.pos() == null) {
                continue;
            }
            net.minecraft.server.level.ServerLevel level = server.getLevel(session.dimension());
            if (level == null) {
                continue;
            }
            long remaining = session.expiresAt() < 0L ? -1L : Math.max(0L, session.expiresAt() - now);
            byLevel.computeIfAbsent(level, key -> new ArrayList<>())
                    .add(new TimeAccelStatusPayload.Entry(session.pos(), session.multiplier(), remaining));
        }
        for (net.minecraft.server.level.ServerLevel level : server.getAllLevels()) {
            NetworkHandler.sendToPlayersInDimension(level, new TimeAccelStatusPayload(
                    byLevel.getOrDefault(level, List.of())));
        }
    }

    private static void expireTimedSessions(MinecraftServer server, long now) {
        boolean expiredBlocks = BLOCK_SESSIONS.values().removeIf(session -> session.expired(now));
        boolean expiredTime = !TIME_SESSIONS.isEmpty() && TIME_SESSIONS.get(0).expired(now);
        if (expiredTime) {
            TIME_SESSIONS.clear();
        }
        if (expiredBlocks || expiredTime) {
            refreshTickrate(server);
        }
    }

    private static void tickBlockSessions(MinecraftServer server) {
        if (BLOCK_SESSIONS.isEmpty()) {
            return;
        }
        for (Session session : BLOCK_SESSIONS.values()) {
            ServerLevel level = server.getLevel(session.dimension());
            if (level == null || session.pos() == null) {
                continue;
            }
            // 方块部分：与加速方块共用同一套逻辑和每刻预算。
            // 单台：半径 0，只动点中的那一格。
            AcceleratorBlockEntity.accelerateArea(level, session.pos(), BLOCK_RADIUS, session.multiplier(), null);
            // 生物部分：范围内的生物额外 tick，让它们真的"快起来"。
            accelerateEntities(level, session.pos(), session.multiplier());
        }
    }

    /**
     * 加速范围内生物的<b>周期行为</b>：下蛋、繁殖冷却、成长这类每刻自减的计时器。
     * <p>
     * 做法是额外 tick 若干次，再把<b>位移与朝向原样还原</b>。
     * 原因：这些计时器都写在 {@code tick()} 里，只能靠多调 tick 推进；
     * 但 {@code tick()} 同时会跑 AI 与移动，直接把副作用留在外面就成了"鸡满场乱飞"。
     * 把位置、朝向、速度还原回去，留下的正好是想要的那部分 —— 计时器变快，走位不变。
     * <p>
     * 玩家自己被排除：把玩家 tick 多次会让移动、攻击判定在同一刻重复触发。
     */
    private static void accelerateEntities(ServerLevel level, BlockPos center, int multiplier) {
        int extra = multiplier - 1;
        if (extra <= 0) {
            return;
        }
        AABB box = new AABB(center).inflate(ENTITY_RADIUS);
        int budget = AcceleratorBlockEntity.MAX_EXTRA_TICKS_PER_TICK;
        for (Entity entity : level.getEntities((Entity) null, box,
                candidate -> !(candidate instanceof Player) && candidate.isAlive())) {
            if (budget <= 0) {
                return;
            }
            int calls = Math.min(extra, budget);
            double x = entity.getX();
            double y = entity.getY();
            double z = entity.getZ();
            float yaw = entity.getYRot();
            float pitch = entity.getXRot();
            float headYaw = entity.getYHeadRot();
            Vec3 motion = entity.getDeltaMovement();
            for (int i = 0; i < calls && !entity.isRemoved(); i++) {
                entity.tick();
            }
            entity.setPos(x, y, z);
            entity.setYRot(yaw);
            entity.setXRot(pitch);
            entity.setYHeadRot(headYaw);
            entity.setDeltaMovement(motion);
            // 同步"上一帧位置"，否则客户端会朝新位置插值，看起来像瞬移了一下再弹回来。
            entity.setOldPosAndRot();
            budget -= calls;
        }
    }
}