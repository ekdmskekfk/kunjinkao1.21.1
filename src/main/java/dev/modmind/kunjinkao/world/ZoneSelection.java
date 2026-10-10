package dev.modmind.kunjinkao.world;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 选区挖掘（127）里"正在框的那块区域"。
 * <p>
 * <b>刻意不做持久化。</b> 开发计划建议把选区存进 SavedData，但选区本质上是
 * "我正准备挖这一块"的临时状态，和剪贴板一样。把它存进存档的后果是：
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

    /** 同一个角点连按两次表示取消设置 —— 比再加一个"清空"按键省事。 */
    public static void setCorner(ServerPlayer player, int cornerIndex, BlockPos pos) {
        ResourceLocation dimension = player.level().dimension().location();
        Corners current = SELECTIONS.get(player.getUUID());
        if (current == null || !dimension.equals(current.dimension())) {
            // 换维度了：上一组角点在别的世界里，坐标没有可比性，直接重开一组。
            current = new Corners(dimension, null, null);
        }
        BlockPos cornerA = current.a();
        BlockPos cornerB = current.b();
        if (cornerIndex == 0) {
            cornerA = pos.equals(cornerA) ? null : pos;
        } else {
            cornerB = pos.equals(cornerB) ? null : pos;
        }
        SELECTIONS.put(player.getUUID(), new Corners(dimension, cornerA, cornerB));
    }

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