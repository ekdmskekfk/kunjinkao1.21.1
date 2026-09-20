package dev.modmind.kunjinkao.world;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * 「主世界之门」的传送逻辑。
 * <p>
 * 目标维度 {@code kunjinkao:main_world} 是本模组用数据包单独添加的主世界
 * （{@code data/kunjinkao/dimension/main_world.json}），与 {@code minecraft:overworld} 无关。
 * <p>
 * 规则：
 * <ul>
 *   <li>在任意非目标维度右键门方块 → 传送到自建主世界的固定落点，并记住当前坐标；</li>
 *   <li>在自建主世界里右键门方块 → 传送回进入前的位置（若没有记录则退回原版主世界出生点）。</li>
 * </ul>
 * 首次到达时会在落点铺一块 5x5 石台并清出站立空间；该操作是幂等的，之后进入不会再改动世界。
 */
public final class MainWorldGate {

    /** 本模组自带的主世界维度。 */
    public static final ResourceKey<Level> MAIN_WORLD = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "main_world"));

    /** 自建主世界中的固定落点水平坐标。 */
    private static final int SPAWN_X = 0;
    private static final int SPAWN_Z = 0;

    /** 安全平台半径：2 表示 5x5。 */
    private static final int PLATFORM_RADIUS = 2;

    /** 平台净空高度（脚下 + 头顶）。 */
    private static final int HEAD_ROOM = 2;

    private static final String RETURN_X = "KunJinKaoGateReturnX";
    private static final String RETURN_Y = "KunJinKaoGateReturnY";
    private static final String RETURN_Z = "KunJinKaoGateReturnZ";
    private static final String RETURN_YAW = "KunJinKaoGateReturnYaw";
    private static final String RETURN_PITCH = "KunJinKaoGateReturnPitch";

    private MainWorldGate() {
    }

    /** 由门方块右键调用；只应在服务端执行。 */
    public static void use(ServerPlayer player) {
        MinecraftServer server = player.server;
        if (player.level().dimension().equals(MAIN_WORLD)) {
            leave(player, server);
        } else {
            enter(player, server);
        }
    }

    private static void enter(ServerPlayer player, MinecraftServer server) {
        ServerLevel destination = server.getLevel(MAIN_WORLD);
        if (destination == null) {
            // 维度 JSON 未加载（数据包被禁用或加载失败）时明确告知，不静默失败。
            player.displayClientMessage(Component.translatable("message.kunjinkao.world_gate_missing"), true);
            return;
        }
        rememberReturnPosition(player);
        BlockPos landing = prepareLanding(destination, SPAWN_X, SPAWN_Z);
        player.teleportTo(destination,
                landing.getX() + 0.5D, landing.getY(), landing.getZ() + 0.5D,
                player.getYRot(), player.getXRot());
        player.displayClientMessage(Component.translatable("message.kunjinkao.world_gate_entered"), true);
    }

    private static void leave(ServerPlayer player, MinecraftServer server) {
        ServerLevel overworld = server.overworld();
        CompoundTag data = player.getPersistentData();
        if (data.contains(RETURN_X) && data.contains(RETURN_Y) && data.contains(RETURN_Z)) {
            double x = data.getDouble(RETURN_X);
            double y = data.getDouble(RETURN_Y);
            double z = data.getDouble(RETURN_Z);
            float yaw = data.getFloat(RETURN_YAW);
            float pitch = data.getFloat(RETURN_PITCH);
            clearReturnPosition(data);
            player.teleportTo(overworld, x, y, z, yaw, pitch);
        } else {
            BlockPos spawn = overworld.getSharedSpawnPos();
            player.teleportTo(overworld, spawn.getX() + 0.5D, spawn.getY(), spawn.getZ() + 0.5D,
                    player.getYRot(), player.getXRot());
        }
        player.displayClientMessage(Component.translatable("message.kunjinkao.world_gate_returned"), true);
    }

    private static void rememberReturnPosition(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        data.putDouble(RETURN_X, player.getX());
        data.putDouble(RETURN_Y, player.getY());
        data.putDouble(RETURN_Z, player.getZ());
        data.putFloat(RETURN_YAW, player.getYRot());
        data.putFloat(RETURN_PITCH, player.getXRot());
    }

    private static void clearReturnPosition(CompoundTag data) {
        data.remove(RETURN_X);
        data.remove(RETURN_Y);
        data.remove(RETURN_Z);
        data.remove(RETURN_YAW);
        data.remove(RETURN_PITCH);
    }

    /**
     * 取得落点并保证其安全可站立：先强制加载目标区块，再取地表高度，
     * 然后铺一层 5x5 石台并清出站立空间。幂等——第二次进入时无需再改动任何方块。
     */
    private static BlockPos prepareLanding(ServerLevel level, int x, int z) {
        level.getChunk(x >> 4, z >> 4);
        int feetY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        feetY = Mth.clamp(feetY, level.getMinBuildHeight() + 1, level.getMaxBuildHeight() - HEAD_ROOM - 1);
        BlockPos feet = new BlockPos(x, feetY, z);
        for (int dx = -PLATFORM_RADIUS; dx <= PLATFORM_RADIUS; dx++) {
            for (int dz = -PLATFORM_RADIUS; dz <= PLATFORM_RADIUS; dz++) {
                BlockPos floor = feet.offset(dx, -1, dz);
                if (!level.getBlockState(floor).isFaceSturdy(level, floor, Direction.UP)) {
                    level.setBlockAndUpdate(floor, Blocks.SMOOTH_STONE.defaultBlockState());
                }
                for (int dy = 0; dy <= HEAD_ROOM; dy++) {
                    BlockPos space = feet.offset(dx, dy, dz);
                    if (!level.getBlockState(space).isAir()) {
                        level.setBlockAndUpdate(space, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
        return feet;
    }
}