package dev.modmind.kunjinkao.world;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Method;

/**
 * 从 <b>AE2 的无线终端 / 便携元件</b>里取材。
 * <p>
 * 对应参考实现的 HandlerWirelessTerminal 与 HandlerPortableCell：
 * 它们把网络存储当作"容器"，从里面抽取方块。
 * <pre>
 *   MEStorage storage = host.getInventory();
 *   AEItemKey key = AEItemKey.of(itemStack);
 *   storage.extract(key, amount, Actionable.MODULATE, IActionSource.ofPlayer(player));
 * </pre>
 * 参考实现走的是 new 一个 WirelessTerminalMenuHost / PortableCellMenuHost，
 * 这里换成更短的一条路：AE2 的物品只要实现 IMenuItem，就能
 * {@code getMenuHost(player, MenuLocators.forStack(stack), callback)} 拿到 Host，
 * 再从 Host 取 MEStorage —— 两次反射调用，少一层泛型构造。
 * <p>
 * 全部走反射，没装 AE2 时整条路径静默关闭。
 */
public final class Ae2Materials {

    private static final String MENU_ITEM = "appeng.api.implementations.menuobjects.IMenuItem";
    private static final String MENU_LOCATORS = "appeng.menu.locator.MenuLocators";
    private static final String AE_ITEM_KEY = "appeng.api.stacks.AEItemKey";
    private static final String ACTION_SOURCE = "appeng.api.networking.security.IActionSource";
    private static final String ACTIONABLE = "appeng.api.config.Actionable";

    private static boolean resolved;
    private static Method menuLocatorsForStack;
    private static Method getMenuHost;

    private static Method aeItemKeyOf;
    private static Method extract;
    private static Method actionSourceOfPlayer;
    private static Object actionableModulate;
    private static Object actionableSimulate;

    private Ae2Materials() {
    }

    private static synchronized boolean ensureResolved() {
        if (resolved) {
            return extract != null;
        }
        resolved = true;
        try {
            Class<?> menuItem = Class.forName(MENU_ITEM);
            // 签名核实过：getMenuHost(Player, ItemMenuHostLocator, BlockHitResult)
            // —— 第三个参数是 BlockHitResult，不是 BiConsumer。
            getMenuHost = menuItem.getMethod("getMenuHost", Player.class,
                    Class.forName("appeng.menu.locator.ItemMenuHostLocator"),
                    net.minecraft.world.phys.BlockHitResult.class);
            menuLocatorsForStack = Class.forName(MENU_LOCATORS).getMethod("forStack", ItemStack.class);
            // getInventory 不在基类 ItemMenuHost 上，而在
            // WirelessTerminalMenuHost / PortableCellMenuHost 这些子类上 ——
            // 所以只能到运行时类上去找，不能在这里预先解析。
            aeItemKeyOf = Class.forName(AE_ITEM_KEY).getMethod("of", ItemStack.class);
            Class<?> storage = Class.forName("appeng.api.storage.MEStorage");
            Class<?> actionable = Class.forName(ACTIONABLE);
            extract = storage.getMethod("extract", Class.forName("appeng.api.stacks.AEKey"), long.class,
                    actionable, Class.forName(ACTION_SOURCE));
            actionSourceOfPlayer = Class.forName(ACTION_SOURCE).getMethod("ofPlayer", Player.class);
            actionableModulate = java.util.Arrays.stream(actionable.getEnumConstants())
                    .filter(c -> "MODULATE".equals(((Enum<?>) c).name()))
                    .findFirst().orElse(null);
            actionableSimulate = java.util.Arrays.stream(actionable.getEnumConstants())
                    .filter(c -> "SIMULATE".equals(((Enum<?>) c).name()))
                    .findFirst().orElse(null);
        } catch (ReflectiveOperationException | LinkageError e) {
            extract = null;
        }
        return extract != null;
    }

    /** 这一叠物品是不是 AE2 的"终端类"（实现了 IMenuItem，能拿出网络存储）。 */
    public static boolean isTerminal(ItemStack stack) {
        if (!ensureResolved() || stack.isEmpty()) {
            return false;
        }
        try {
            return Class.forName(MENU_ITEM).isInstance(stack.getItem());
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 从这一叠终端里试着抽出 {@code amount} 个该物品。
     *
     * @return 实际抽到的数量（0 表示这个终端里没有或抽不动）
     */
    public static long extract(Player player, ItemStack terminal, Item item, long amount) {
        return transfer(player, terminal, item, amount, actionableModulate);
    }

    /** 非破坏性计数：用 SIMULATE 干跑一次，看这个终端里有多少。 */
    public static long count(Player player, ItemStack terminal, Item item, long amount) {
        return transfer(player, terminal, item, amount, actionableSimulate);
    }

    private static long transfer(Player player, ItemStack terminal, Item item, long amount, Object actionable) {
        if (!ensureResolved() || amount <= 0 || actionable == null) {
            return 0L;
        }
        try {
            Object locator = menuLocatorsForStack.invoke(null, terminal);
            Object host = getMenuHost.invoke(terminal.getItem(), player, locator, null);
            if (host == null) {
                return 0L;
            }
            // 从运行时类上取 getInventory（基类没有这个方法）。
            Object storage = host.getClass().getMethod("getInventory").invoke(host);
            if (storage == null) {
                return 0L;
            }
            Object key = aeItemKeyOf.invoke(null, new ItemStack(item));
            Object source = actionSourceOfPlayer.invoke(null, player);
            Object taken = extract.invoke(storage, key, amount, actionable, source);
            return taken instanceof Long value ? value : 0L;
        } catch (ReflectiveOperationException | RuntimeException e) {
            return 0L;
        }
    }
}