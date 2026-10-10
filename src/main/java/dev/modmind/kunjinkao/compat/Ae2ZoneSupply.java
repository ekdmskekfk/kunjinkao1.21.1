package dev.modmind.kunjinkao.compat;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 选区作业（127）放置模式的最后一级货源：从玩家附近的 ME 网络里抽物品。
 * <p>
 * <b>实现程度：完整支持。</b> 依据的是 {@code appliedenergistics2-19.2.17}（AE2 的 1.21.1 分支）
 * 的公开 API，方法签名逐个用 javap 从 jar 里核对过，不是照着记忆写的：
 * {@code IInWorldGridNodeHost.getGridNode(Direction)} → {@code IGridNode.getGrid()} →
 * {@code IGrid.getService(IStorageService.class)} → {@code IStorageService.getInventory()} →
 * {@code MEStorage.extract(AEKey, long, Actionable, IActionSource)}。
 * 密钥用 {@code AEItemKey.of(ItemStack)} 现造，动作来源用 {@code IActionSource.ofPlayer(Player)}。
 * <p>
 * 注意 {@code IActionSource} 在 {@code appeng.api.networking.security} 包里，
 * 不在 {@code appeng.api.stacks} —— 这两个包名很容易混，写错了只会在真装了 AE2 的服务器上炸。
 * <p>
 * <b>全部走反射，不引入对 AE2 的编译期依赖</b>，与 {@link Ae2GridAcceleration} 同一个模式：
 * 解析失败（没装 AE2、版本对不上）就永久关掉这条路径，并只记一次日志 ——
 * 每格方块都 ClassNotFound 一次，日志会被刷爆，性能也没了。
 * <p>
 * <b>只从"活着的"网络里取。</b> 取节点后要 {@code isActive()} 为真才用它的存储：
 * 没通电、没连上网的 ME 网络本来就不该被外部抽走东西，把这种情况算成"取不到"，
 * 让作业按缺料停下并如实告诉玩家，而不是绕开 AE2 的能源规则偷偷拿货。
 * <p>
 * <b>刻意不走 {@code StorageHelper.poweredExtraction}。</b> 那条路要多解析
 * {@code IEnergyService} / {@code IEnergySource} 两层接口，多一层反射就多一处会随 AE2 版本失效的地方，
 * 而它换来的只是"抽一个方块扣一点 AE"这种量级的效果。这里的取物发生在服务端、由已授权玩家发起，
 * 电量消耗的取舍写在这里，不藏着。
 * <p>
 * <b>任何异常都不许冒出这个类。</b> 调用方是服务端的作业 tick：一个反射异常漏出去就是服务器崩溃，
 * 而"AE 取不到东西"本来的正确表现只是让作业停下。
 */
public final class Ae2ZoneSupply {

    private static final Logger LOGGER = LogManager.getLogger("KunJinKao");

    /**
     * 搜索半径（格）。
     * <p>
     * 取 32 是"站在机器旁边"的量级：ME 驱动器、接口这些通常就摆在作业现场附近。
     * 半径一大，扫描的区块数（二次增长）与误取别人网络货物的概率都会跟着涨。
     */
    public static final double SEARCH_RADIUS = 32.0D;

    private static final String NODE_HOST = "appeng.api.networking.IInWorldGridNodeHost";
    private static final String GRID_NODE = "appeng.api.networking.IGridNode";
    private static final String GRID = "appeng.api.networking.IGrid";
    private static final String STORAGE_SERVICE = "appeng.api.networking.storage.IStorageService";
    private static final String ME_STORAGE = "appeng.api.storage.MEStorage";
    private static final String AE_ITEM_KEY = "appeng.api.stacks.AEItemKey";
    private static final String AE_KEY = "appeng.api.stacks.AEKey";
    private static final String ACTIONABLE = "appeng.api.config.Actionable";
    private static final String ACTION_SOURCE = "appeng.api.networking.security.IActionSource";

    /** 解析结果：只解析一次，失败就永久关闭这条路径，避免每格方块反复 ClassNotFound。 */
    private static boolean resolved;
    private static Class<?> nodeHostClass;
    private static Method getGridNode;
    private static Method getGrid;
    private static Method isActive;
    private static Method getService;
    private static Method getInventory;
    private static Method itemKeyOf;
    private static Method extract;
    private static Method actionSourceOfPlayer;
    private static Object actionableModulate;
    /** {@code IStorageService} 的类型对象：{@code IGrid.getService} 要拿它当参数。 */
    private static Class<?> storageServiceClass;

    private Ae2ZoneSupply() {
    }

    private static synchronized boolean ensureResolved() {
        if (resolved) {
            return getGridNode != null;
        }
        resolved = true;
        try {
            nodeHostClass = Class.forName(NODE_HOST);
            Class<?> gridNodeClass = Class.forName(GRID_NODE);
            Class<?> gridClass = Class.forName(GRID);
            storageServiceClass = Class.forName(STORAGE_SERVICE);
            Class<?> meStorageClass = Class.forName(ME_STORAGE);
            Class<?> aeItemKeyClass = Class.forName(AE_ITEM_KEY);
            Class<?> aeKeyClass = Class.forName(AE_KEY);
            Class<?> actionableClass = Class.forName(ACTIONABLE);
            Class<?> actionSourceClass = Class.forName(ACTION_SOURCE);

            getGridNode = nodeHostClass.getMethod("getGridNode", Direction.class);
            getGrid = gridNodeClass.getMethod("getGrid");
            isActive = gridNodeClass.getMethod("isActive");
            getService = gridClass.getMethod("getService", Class.class);
            getInventory = storageServiceClass.getMethod("getInventory");
            itemKeyOf = aeItemKeyClass.getMethod("of", ItemStack.class);
            actionSourceOfPlayer = actionSourceClass.getMethod("ofPlayer", net.minecraft.world.entity.player.Player.class);
            // 取参数类型用 AEKey（接口里声明的那一个），不是 AEItemKey：
            // 拿实现类去找方法会 NoSuchMethodException，而接口方法才是我们真正要调的。
            extract = meStorageClass.getMethod("extract", aeKeyClass, long.class, actionableClass, actionSourceClass);
            actionableModulate = actionableClass.getField("MODULATE").get(null);
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException
                 | IllegalAccessException | LinkageError e) {
            // 没装 AE2（或版本对不上）：关掉这条路径即可，不是错误。
            getGridNode = null;
            LOGGER.info("[Kunjinkao] 未接入 AE2 库存：选区作业的放置模式只用副手与背包（原因：{}）", e.toString());
        }
        return getGridNode != null;
    }

    /**
     * 找出玩家附近所有可用的 ME 网络存储，按离玩家的距离从近到远。
     * <p>
     * <b>返回列表而不是单个。</b> 现场可能摆着不止一张网络，只挑最近的那张会出现
     * "旁边那张有货却报缺料"；按距离排序后由调用方逐个试，行为才对得上玩家的直觉。
     * <p>
     * 只扫<b>已经加载</b>的区块（{@code requireChunk = false}）：为了找货源而把远处区块加载出来，
     * 等于替服主决定要加载什么，而且那一瞬间的开销比整个作业都大。
     *
     * @return 找到的存储句柄（不透明的 {@code MEStorage} 实例）；一张网络都没有时返回空列表
     */
    public static List<Object> findStorages(ServerLevel level, Vec3 center) {
        if (!ensureResolved()) {
            return List.of();
        }
        double radiusSqr = SEARCH_RADIUS * SEARCH_RADIUS;
        int minChunkX = ((int) Math.floor(center.x - SEARCH_RADIUS)) >> 4;
        int maxChunkX = ((int) Math.floor(center.x + SEARCH_RADIUS)) >> 4;
        int minChunkZ = ((int) Math.floor(center.z - SEARCH_RADIUS)) >> 4;
        int maxChunkZ = ((int) Math.floor(center.z + SEARCH_RADIUS)) >> 4;

        List<Object[]> found = new ArrayList<>(2);
        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                LevelChunk chunk = level.getChunkSource().getChunk(chunkX, chunkZ, false);
                if (chunk == null) {
                    continue;
                }
                for (BlockEntity entity : chunk.getBlockEntities().values()) {
                    if (!nodeHostClass.isInstance(entity)) {
                        continue;
                    }
                    double distanceSqr = center.distanceToSqr(Vec3.atCenterOf(entity.getBlockPos()));
                    if (distanceSqr > radiusSqr) {
                        continue;
                    }
                    Object storage = storageOf(entity);
                    if (storage != null) {
                        found.add(new Object[]{distanceSqr, storage});
                    }
                }
            }
        }
        found.sort(Comparator.comparingDouble(entry -> (Double) entry[0]));
        List<Object> storages = new ArrayList<>(found.size());
        for (Object[] entry : found) {
            // 同一张网络会被多个方块实体指向（驱动器、接口、终端都是节点），
            // 去重按引用比：同一个 MEStorage 实例重复出现只会让后面白试几次。
            if (!containsSame(storages, entry[1])) {
                storages.add(entry[1]);
            }
        }
        return storages;
    }

    /**
     * 从这些网络里抽走一个指定物品。
     *
     * @return 是否真的抽到了；抽到就说明调用方可以放下这一格
     */
    public static boolean extractOne(List<Object> storages, ItemStack template, ServerPlayer player) {
        if (!ensureResolved() || storages.isEmpty() || template.isEmpty()) {
            return false;
        }
        for (Object storage : storages) {
            if (extractOne(storage, template, player)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从一张网络里抽一个物品。
     * <p>
     * 数量固定为 1：调用方是逐格放置，一次一格，多抽出来的部分没有地方放。
     * 用 {@code MODULATE} 而不是 {@code SIMULATE}：这一步要的是真的把货拿走，
     * 拿不走（返回 0）就当没抽到，作业会据此停下。
     * <p>
     * 动作来源带上真实玩家（{@code IActionSource.ofPlayer}）：AE2 会拿它做安全审计与
     * "谁动了网络里的东西"的记录。传一个空来源虽然也能抽，但那等于把所有取货都记成匿名操作。
     */
    private static boolean extractOne(Object storage, ItemStack template, ServerPlayer player) {
        Object key = call(itemKeyOf, null, template);
        if (key == null) {
            return false;
        }
        Object source = call(actionSourceOfPlayer, null, player);
        if (source == null) {
            return false;
        }
        Object amount = call(extract, storage, key, 1L, actionableModulate, source);
        return amount instanceof Long taken && taken > 0L;
    }

    /**
     * 取一个方块实体背后的 ME 存储；它不是网络节点、或网络没有存储服务时返回 null。
     * <p>
     * 六个方向都试：同一个节点在不同面上都答得出来，谁先给非空就用谁 ——
     * 我们问的是"这台机器挂在哪个网络上"，方向对这件事没有意义。
     */
    @Nullable
    private static Object storageOf(Object host) {
        for (Direction direction : Direction.values()) {
            Object node = call(getGridNode, host, direction);
            if (node == null) {
                continue;
            }
            Object grid = call(getGrid, node);
            if (grid == null || !Boolean.TRUE.equals(call(isActive, node))) {
                continue;
            }
            Object service = call(getService, grid, storageServiceClass);
            if (service == null) {
                continue;
            }
            Object inventory = call(getInventory, service);
            if (inventory != null) {
                return inventory;
            }
        }
        return null;
    }

    private static boolean containsSame(List<Object> storages, Object candidate) {
        for (Object existing : storages) {
            if (existing == candidate) {
                return true;
            }
        }
        return false;
    }

    /**
     * 调一次反射方法，任何失败都收成 null。
     * <p>
     * 不在这里永久关闭这条路径：解析已经在 {@link #ensureResolved} 里做过一次，
     * 走到这里还抛异常的是运行期状态问题（网络正在重建之类），下一次多半就好了；
     * 而真正致命的"类不存在"在解析那一步就被拦下并只记一次日志了。
     */
    @Nullable
    private static Object call(@Nullable Method method, @Nullable Object target, Object... args) {
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(target, args);
        } catch (ReflectiveOperationException | RuntimeException | LinkageError e) {
            return null;
        }
    }
}