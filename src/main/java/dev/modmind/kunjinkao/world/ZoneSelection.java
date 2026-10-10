package dev.modmind.kunjinkao.world;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 选区作业（127）里"正在框的那块区域"。
 * <p>
 * <b>刻意不做持久化。</b> 开发计划建议把选区存进 SavedData，但选区本质上是
 * "我正准备处理这一块"的临时状态，和剪贴板一样。把它存进存档的后果是：
 * 玩家框了一半就下线，几天后上线按一下执行键，一块 64×64 的地形就没了 ——
 * 那时他多半已经忘了自己框过什么。重启即清空反而是安全特性。
 * <p>
 * 记住维度：选区是绑在某个维度上的，玩家换维度后旧的角点不该被拿来计算。
 */
public final class ZoneSelection {

    private static final Map<UUID, Corners> SELECTIONS = new HashMap<>();

    /** 一名玩家当前的两个角点；未设置的那个为 null。 */
    public record Corners(ResourceLocation dimension, BlockPos a, BlockPos b) {
    }

    private ZoneSelection() {
    }

    /**
     * 按「A → B → 重新从 A 开始」的顺序设一个角点。
     * <p>
     * <b>顺序由已有角点推出来，不额外记一个"下一个是谁"的标志位。</b> 记标志位就要考虑
     * 它和实际角点不一致的情形（取消了一个角点、换了维度、作业跑完清空），
     * 而这里"A 空着就设 A、A 有了 B 空着就设 B、两个都有了就从 A 重来"本身就定义清楚了顺序，
     * 没有第二份状态可以走岔。
     *
     * @return 这次设下的是哪个角点：{@link #CORNER_A} 或 {@link #CORNER_B}
     */
    public static int setNextCorner(ServerPlayer player, BlockPos pos) {
        ResourceLocation dimension = player.level().dimension().location();
        Corners current = SELECTIONS.get(player.getUUID());
        if (current == null || !dimension.equals(current.dimension())) {
            // 换维度了：上一组角点在别的世界里，坐标没有可比性，直接重开一组。
            current = new Corners(dimension, null, null);
        }
        BlockPos cornerA = current.a();
        BlockPos cornerB = current.b();
        int corner;
        if (cornerA == null) {
            cornerA = pos;
            corner = CORNER_A;
        } else if (cornerB == null) {
            cornerB = pos;
            corner = CORNER_B;
        } else {
            // 两个角点都已经设过：第三次右键的意思是"重新框一块"，
            // 而不是把 A 挪走却留着 B —— 那会框出一块玩家没打算选的地。
            cornerA = pos;
            cornerB = null;
            corner = CORNER_A;
        }
        SELECTIONS.put(player.getUUID(), new Corners(dimension, cornerA, cornerB));
        return corner;
    }

    /** 角点序号：与 {@code ZoneStatusPayload} 里的 A/B 对应。 */
    public static final int CORNER_A = 0;
    public static final int CORNER_B = 1;

    /** 取当前角点；没有则返回 null。 */
    public static Corners get(ServerPlayer player) {
        return SELECTIONS.get(player.getUUID());
    }

    public static void clear(ServerPlayer player) {
        SELECTIONS.remove(player.getUUID());
    }

    /** 玩家退出时清掉，别为离线玩家长期持有坐标。 */
    public static void forget(UUID uuid) {
        SELECTIONS.remove(uuid);
    }
}