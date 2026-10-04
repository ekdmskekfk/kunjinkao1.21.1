package dev.modmind.kunjinkao.world;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 暂停场：以某一格为中心的一块球形范围。
 * <p>
 * 范围里的<b>方块实体</b>与<b>生物</b>停止 tick，<b>玩家</b>被钉在原地动弹不得。
 * 两条豁免：
 * <ul>
 *   <li>通过密码验证的<b>管理员</b>永远不会被自己的或别人的暂停场冻住；</li>
 *   <li>立起这个场的<b>场主</b>也不会被自己立的场冻住 —— 否则一放下就寸步难行。</li>
 * </ul>
 * <p>
 * 玩家与生物的处理方式刻意不同：
 * <ul>
 *   <li><b>生物</b>走 {@code EntityTickEvent.Pre} 直接取消 tick，连 AI 带移动一起停；</li>
 *   <li><b>玩家</b>不能取消 tick（会连带跳过背包、状态、连接等一堆事情），
 *       而是每 tick 把位置钉回进场的瞬间，并清掉速度 —— 客户端会被拉回去，表现为"动不了"。</li>
 * </ul>
 * 方块实体没有对应的 NeoForge 事件，只能由 {@code LevelPauseMixin} 在
 * {@code Level.tickBlockEntities} 里跳过，否则机器照转。
 */
public final class SwordPauseField {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final int DEFAULT_RADIUS = 12;
    public static final int MIN_RADIUS = 4;
    public static final int MAX_RADIUS = 48;

    /** 同一个位置短时间内重复触发时忽略后一次，避免一次右键被两条路径各处理一遍。 */
    private static final long TOGGLE_DEBOUNCE_MILLIS = 300L;

    private record BlockKey(ResourceKey<Level> dimension, BlockPos pos) {
    }

    /** 一个暂停场。{@code owner} 是立起它的人，他自己不会被这个场冻住。 */
    public record Field(BlockPos center, int radius, UUID owner) {
        public boolean contains(double x, double y, double z) {
            double dx = x - (center.getX() + 0.5D);
            double dy = y - (center.getY() + 0.5D);
            double dz = z - (center.getZ() + 0.5D);
            return dx * dx + dy * dy + dz * dz <= (double) radius * (double) radius;
        }
    }

    private static final Map<BlockKey, Field> FIELDS = new HashMap<>();
    private static final Map<UUID, Long> LAST_TOGGLE = new HashMap<>();
    /** 被冻住的玩家 -> 冻住那一刻的位置。每 tick 钉回去。 */
    private static final Map<UUID, Vec3> PLAYER_PINS = new HashMap<>();

    private SwordPauseField() {
    }

    public static int clampRadius(int radius) {
        return Math.max(MIN_RADIUS, Math.min(MAX_RADIUS, radius));
    }

    private static BlockKey key(ResourceKey<Level> dimension, BlockPos pos) {
        return new BlockKey(dimension, pos.immutable());
    }

    public static int fieldCount() {
        return FIELDS.size();
    }

    // ===================== 判定 =====================

    /** 这个坐标是否落在某个暂停场里。 */
    public static boolean isPaused(ResourceKey<Level> dimension, double x, double y, double z) {
        if (FIELDS.isEmpty()) {
            return false;
        }
        for (Map.Entry<BlockKey, Field> entry : FIELDS.entrySet()) {
            if (!entry.getKey().dimension().equals(dimension)) {
                continue;
            }
            if (entry.getValue().contains(x, y, z)) {
                return true;
            }
        }
        return false;
    }

    /** 方块位置是否暂停。用方块中心点判定。 */
    public static boolean isPaused(Level level, BlockPos pos) {
        if (FIELDS.isEmpty() || level.isClientSide()) {
            return false;
        }
        return isPaused(level.dimension(), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
    }

    /** 实体是否该停止 tick（不含玩家）。 */
    public static boolean isPaused(Entity entity) {
        Level level = entity.level();
        if (FIELDS.isEmpty() || level.isClientSide()) {
            return false;
        }
        return isPaused(level.dimension(), entity.getX(), entity.getY(), entity.getZ());
    }

    /**
     * 这个玩家是否该被冻住。
     * <p>
     * 管理员与场主都豁免 —— 前者是"这台机器的操作者不该被自己的能力关在外面"，
     * 后者是"自己立的场不该把自己钉死"。
     */
    public static boolean shouldFreeze(Player player) {
        if (FIELDS.isEmpty()) {
            return false;
        }
        // 通过密码验证的管理员不会被暂停。
        if (dev.modmind.kunjinkao.config.AdminToolConfig.isAuthorized(player.getUUID())) {
            return false;
        }
        Level level = player.level();
        if (level.isClientSide()) {
            return false;
        }
        ResourceKey<Level> dimension = level.dimension();
        for (Map.Entry<BlockKey, Field> entry : FIELDS.entrySet()) {
            if (!entry.getKey().dimension().equals(dimension)) {
                continue;
            }
            Field field = entry.getValue();
            if (!field.contains(player.getX(), player.getY(), player.getZ())) {
                continue;
            }
            if (field.owner().equals(player.getUUID())) {
                continue;
            }
            return true;
        }
        return false;
    }

    /** 列出所有暂停场，供 HUD 之类的展示用。 */
    public static List<Field> fields() {
        return new ArrayList<>(FIELDS.values());
    }

    // ===================== 玩家的"钉住"状态 =====================

    public static Vec3 pinOf(UUID playerId) {
        return PLAYER_PINS.get(playerId);
    }

    public static void pin(UUID playerId, Vec3 pos) {
        PLAYER_PINS.put(playerId, pos);
    }

    public static void unpin(UUID playerId) {
        PLAYER_PINS.remove(playerId);
    }

    public static Set<UUID> pinnedPlayers() {
        return new HashSet<>(PLAYER_PINS.keySet());
    }

    // ===================== 入口 =====================

    /**
     * shift + 右键方块且「暂停场」开关开着时走这里：在该位置开/关一个暂停场。
     *
     * @return 是否处理了这次右键（始终 true —— 吃掉这次右键，别漏回原版）
     */
    public static boolean tryToggleBlock(Player player, Level level, BlockPos pos) {
        if (level.isClientSide()) {
            return true;
        }
        long now = System.currentTimeMillis();
        Long last = LAST_TOGGLE.get(player.getUUID());
        if (last != null && now - last < TOGGLE_DEBOUNCE_MILLIS) {
            return true;
        }
        LAST_TOGGLE.put(player.getUUID(), now);

        Field previous = FIELDS.remove(key(level.dimension(), pos));
        if (previous == null) {
            Field field = new Field(pos.immutable(), DEFAULT_RADIUS, player.getUUID());
            FIELDS.put(key(level.dimension(), pos), field);
            LOGGER.info("[PAUSE] {} 在 {} 立起暂停场（半径 {}），当前共 {} 个",
                    player.getName().getString(), pos, field.radius(), FIELDS.size());
            player.displayClientMessage(Component.translatable(
                    "message.kunjinkao.pause_field_started", field.radius()), true);
        } else {
            LOGGER.info("[PAUSE] {} 在 {} 关闭暂停场，剩余 {} 个",
                    player.getName().getString(), pos, FIELDS.size());
            player.displayClientMessage(Component.translatable(
                    "message.kunjinkao.pause_field_stopped"), true);
        }
        return true;
    }

    /** 服务器停机或玩家退出时清干净，避免状态跨局残留。 */
    public static void clear() {
        FIELDS.clear();
        PLAYER_PINS.clear();
        LAST_TOGGLE.clear();
    }
}