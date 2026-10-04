package dev.modmind.kunjinkao.world;

import dev.modmind.kunjinkao.world.PlacementUndoHistory.Change;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

/**
 * 放置类核心：建筑手杖 / 天使核心 / 破坏核心。
 * <p>
 * 行为对齐 Construction Wand（建筑手杖）模组的三种核心：
 * <ul>
 *   <li><b>建筑手杖</b>：朝你面向的那一面延伸建造，一次放一排。</li>
 *   <li><b>天使核心</b>：放在你所视方块的<b>背面</b>（可穿透若干格）；
 *       对着空气右键则在半空放置，条件与官方一致 —— 副手要有方块，
 *       且下落不超过 10 格（避免拿它从虚空里自救）。</li>
 *   <li><b>破坏核心</b>：破坏你所视那一侧的方块，<b>直接消失不留掉落</b>，
 *       且不处理带方块实体的方块（官方同样排除，避免产生幽灵方块）。</li>
 * </ul>
 * 方块取自<b>副手</b>：主手被剑占着，这与建筑手杖"副手优先"的规则一致。
 * 生存模式下会被正常消耗，走的是原版 BlockItem.place，所以音效、朝向、
 * 可替换判定与方块实体初始化都沿用原版行为。
 * <p>
 * 所有改动都<b>逐步记录</b>成 {@link Change}，交给 {@link PlacementUndoHistory} 存档，
 * 这样按撤销键就能把这一步整体还原（放置退物品、破坏还原方块）。
 */
public final class PlacementCoreHandler {

    private static final org.apache.logging.log4j.Logger LOGGER =
            org.apache.logging.log4j.LogManager.getLogger("KunJinKao");

    /** 单次最多连带处理多少格。1024 对应建筑手杖无限手杖的批量上限。 */
    /**
    /**
     * 基础建造模式单次最多放置多少个方块。
     * <p>
     * 建筑手杖原版的说明写的是「生存模式下单次最多放置32个方块」，
     * 这里按用户要求不沿用那个 32，改为与破坏模式一致的 1024。
     */
    public static final int MAX_PLACE_SURVIVAL = 1024;
    /** 创造模式同样按上限走（保留常量是为了将来要区分时方便）。 */
    public static final int MAX_PLACE_CREATIVE = 1024;
    /**
     * 破坏模式：单次最多破坏 4 个方块。
     * <p>
     * 同样来自建筑手杖的规定（"各种材质的手杖单次最多破坏4个方块"）。
     */
    public static final int MAX_DESTROY = 1024;
    /** 天使核心最多能穿透几格去找落脚点。 */
    private static final int ANGEL_DISTANCE = 4;
    /** 对空右键时，在半空中离眼睛多远放置。 */
    private static final double AIR_PLACE_DISTANCE = 3.0D;
    /** 下落超过这个格数就不允许空中放置。 */
    private static final float MAX_FALL_DISTANCE = 10.0F;

    private PlacementCoreHandler() {
    }

    /** 副手里是否拿着可以放置的方块。客户端也用它决定要不要吃掉这次右键。 */
    /**
     * 背包里是否还有可放置的方块。
     * <p>
     * 说明里写的是"右键即可消耗背包中的方块进行批量放置"——所以材料取自背包，
     * 不是副手。副手只在天使模式的空中放置那一条里才有意义，说明单独写明了。
     */
    public static boolean hasOffhandBlock(Player player) {
        return isPlaceable(findMaterial(player));
    }

    /**
     * 整批放置结束后播一次放置音效。
     * <p>
     * BlockItem.place 自己会播，但它的第一个参数是"要排除的玩家"、传的正是放置者本人 ——
     * 结果只有旁人听得到、放的人反而没声音。而如果改成逐格自己播，
     * 一次建造会连续放很多格，又吵成一片 —— 所以统一在整批结束时播一次。
     */
    public static void playPlaceSound(Level level, List<Change> changes) {
        if (changes.isEmpty()) {
            return;
        }
        BlockPos at = changes.get(0).pos();
        net.minecraft.world.level.block.SoundType sound = level.getBlockState(at).getSoundType();
        level.playSound(null, at, sound.getPlaceSound(), net.minecraft.sounds.SoundSource.BLOCKS,
                (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
    }

    /**
     * 还能不能拿出这一种方块 —— 背包 / 副手 / 容器（潜影盒·收纳袋）/ AE2 终端 / ProjectE 的 EMC。
     * <p>
     * 取材顺序与参考实现的 ContainerManager 一致：先身上，再容器，最后模组兼容。
     */
    public static boolean hasMaterial(Player player, net.minecraft.world.item.Item item) {
        if (isPlaceable(findMaterial(player, item))) {
            return true;
        }
        if (ContainerMaterials.has(player, item)) {
            return true;
        }
        if (countFromTerminals(player, item) > 0L) {
            return true;
        }
        return EmcMaterials.canSupply(player, item);
    }

    /**
     * 这一种方块现在究竟还能拿出多少个 —— 把四条取材路径全算上：
     * 背包与副手、容器（潜影盒·收纳袋）、AE2 终端、ProjectE 的 EMC。
     * <p>
     * 预览要靠它决定"画几格"：背包里只有 32 个泥土时，就不该画 1024 个。
     * 执行侧本身会在材料用尽时停下，所以这里主要是给预览用的；
     * 放在本类里是为了两边共用同一个口径，不会各算一套。
     */
    public static long availableMaterialCount(Player player, net.minecraft.world.item.Item item) {
        long total = ContainerMaterials.count(player, item);
        total += countFromTerminals(player, item);
        total += EmcMaterials.availableEmcCount(player, item);
        return total;
    }

    /** 从玩家身上的 AE2 终端类物品里统计某种方块共有多少（非破坏性，SIMULATE）。 */
    private static long countFromTerminals(Player player, net.minecraft.world.item.Item item) {
        net.minecraft.world.entity.player.Inventory inventory = player.getInventory();
        long total = 0L;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (Ae2Materials.isTerminal(stack)) {
                total += Ae2Materials.count(player, stack, item, Integer.MAX_VALUE);
            }
        }
        for (ItemStack offhand : player.getInventory().offhand) {
            if (Ae2Materials.isTerminal(offhand)) {
                total += Ae2Materials.count(player, offhand, item, Integer.MAX_VALUE);
            }
        }
        return total;
    }

    /**
     * 取走一个：背包 -> 副手 -> 容器 -> AE2 终端 -> ProjectE 的 EMC。
     *
     * @return 真取到了才返回 true
     */
    public static boolean consumeMaterial(Player player, net.minecraft.world.item.Item item) {
        if (ContainerMaterials.consumeOne(player, item)) {
            return true;
        }
        net.minecraft.world.entity.player.Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (Ae2Materials.isTerminal(stack) && Ae2Materials.extract(player, stack, item, 1L) > 0L) {
                return true;
            }
        }
        for (ItemStack offhand : player.getInventory().offhand) {
            if (Ae2Materials.isTerminal(offhand) && Ae2Materials.extract(player, offhand, item, 1L) > 0L) {
                return true;
            }
        }
        return EmcMaterials.spend(player, item, 1);
    }

     /**
     * 找这次要用的方块：<b>副手优先</b>，副手没东西才用背包。
     * <p>
     * 说明是"要是副手有东西，就在点击的方块上放置副手上的方块"——
     * 副手因此是"指定材料"的手段；没指定时退回背包里的方块（快捷栏 0..8 天然优先）。
     */
    public static ItemStack findMaterial(Player player) {
        return findMaterial(player, null);
    }

    /**
     * 找这次要用的方块：<b>副手优先</b>，副手没有就翻背包（快捷栏 0..8 天然优先）。
     * <p>
     * 说明：「要是副手有东西，就在点击的方块上放置副手上的方块」「包里要是有也放」。
     *
     * @param required 只接受这一种物品；为 null 表示任意可放置的方块
     */
    public static ItemStack findMaterial(Player player, net.minecraft.world.item.Item required) {
        ItemStack offhand = player.getOffhandItem();
        if (isPlaceable(offhand) && (required == null || offhand.getItem() == required)) {
            return offhand;
        }
        net.minecraft.world.entity.player.Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (isPlaceable(stack) && (required == null || stack.getItem() == required)) {
                return stack;
            }
        }
        // 再翻一层容器（潜影盒 / 收纳袋）—— 对应参考实现的容器取材。
        return ContainerMaterials.findInContainers(player, required);
    }

    /**
     * 该面平面内的 8 个方向：4 个正向 + 4 条对角线。
     * <p>
     * 与 constructionwand 里 ActionConstruction / ActionDestruction 用的方向表一致 ——
     * 对角线是必需的，否则在拐角处扩散不过去。
     */
    public static List<Direction> planeDirections(Direction face) {
        List<Direction> cardinals = new ArrayList<>(4);
        for (Direction dir : Direction.values()) {
            if (dir.getAxis() != face.getAxis()) {
                cardinals.add(dir);
            }
        }
        List<Direction> all = new ArrayList<>(8);
        all.addAll(cardinals);
        for (int i = 0; i < cardinals.size(); i++) {
            for (int j = i + 1; j < cardinals.size(); j++) {
                Direction a = cardinals.get(i);
                Direction b = cardinals.get(j);
                if (a.getAxis() == b.getAxis()) {
                    continue;
                }
                all.add(Direction.getNearest(a.getStepX() + b.getStepX(),
                        a.getStepY() + b.getStepY(), a.getStepZ() + b.getStepZ()));
            }
        }
        return all;
    }

    private static boolean isPlaceable(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof BlockItem;
    }

    // ===================== 建筑手杖 =====================

    /**
    /**
    /**
    /**
    /**
    /**
     * 基础建造模式：从被点击面的外侧起，沿该面所在的平面做<b>洪泛填充</b>，把连成一片的
     * 同种材质面上铺满方块。
     * <p>
     * 这是照建筑手杖（constructionwand）的 ActionConstruction 做的：
     * 它不是"按某个方向排一排"，而是从起点出发一格一格扩散，
     * 每一步都要 <b>与点击方块同类</b> 才继续 —— 于是自然形成"沿着同种材质的面延伸"，
     * 遇到别的方块就停，也不会跨到旁边去。
     *
     * @return 本次放下的所有改动（空列表表示一格都没放）
     */
    public static List<Change> placeConstructionRow(Player player, Level level, BlockPos clickedPos, Direction face) {
        List<Change> changes = new ArrayList<>();
        BlockState origin = level.getBlockState(clickedPos);
        if (origin.isAir()) {
            return changes;
        }
        net.minecraft.world.item.Item originItem = origin.getBlock().asItem();
        ItemStack offhand = player.getOffhandItem();
        boolean override = isPlaceable(offhand) && offhand.getItem() != originItem;
        // 本次到底要放哪一种物品，从头到尾只在这里决定一次：
        // 副手指定了别的方块就用副手那个，否则必须是与点击方块同种的那种。
        net.minecraft.world.item.Item intendedItem = override ? offhand.getItem() : originItem;
        if (!isPlaceable(findMaterial(player, intendedItem))) {
            return changes;
        }
        int limit = player.isCreative() ? MAX_PLACE_CREATIVE : MAX_PLACE_SURVIVAL;
        // 该面平面内的四个基本方向；对角线由两步基本方向自然覆盖，不必单列。
        List<Direction> planeDirs = planeDirections(face);
        BlockPos first = clickedPos.relative(face);
        Set<BlockPos> seen = new HashSet<>();
        java.util.ArrayDeque<BlockPos> queue = new java.util.ArrayDeque<>();
        seen.add(first);
        queue.add(first);
        while (!queue.isEmpty() && changes.size() < limit) {
            BlockPos pos = queue.poll();
            // 洪泛只在"身后那一格与点击方块同类"的位置上继续 —— 这就是
            // 「如果不是点击的方块就不要放置」；副手指定了别的材料时跳过这条。
            BlockPos support = pos.relative(face.getOpposite());
            if (!override && level.getBlockState(support).getBlock() != origin.getBlock()) {
                continue;
            }
            ItemStack current = override ? player.getOffhandItem() : findMaterial(player, originItem);
            if (!isPlaceable(current)) {
                break;
            }
            // 只放在"能放"的位置上：空气，或替换模式下的可替换方块。
            if (level.getBlockState(pos).canBeReplaced()) {
                Change change = tryPlaceAt(level, player, support, face, pos, intendedItem);
                if (change != null) {
                    changes.add(change);
                }
            }
            for (Direction dir : planeDirs) {
                BlockPos next = pos.relative(dir);
                if (seen.add(next)) {
                    queue.add(next);
                }
            }
        }
        if (!level.isClientSide()) {
            playPlaceSound(level, changes);
        }
        return changes;
    }

    /**
     * 放置一格，依托只要求"相邻"：在目标周围六个方向里找第一个实心的邻居当依托，
     * 朝向就是"从依托指向目标"。找不到任何相邻实心方块就放弃这一格。
     */
    private static Change tryPlaceAdjacent(Level level, Player player, BlockPos target, net.minecraft.world.item.Item materialItem) {
        for (Direction side : Direction.values()) {
            BlockPos support = target.relative(side);
            BlockState supportState = level.getBlockState(support);
            if (supportState.canBeReplaced()) {
                continue;
            }
            Change change = tryPlaceAt(level, player, support, side.getOpposite(), target, materialItem);
            if (change != null) {
                return change;
            }
        }
        return null;
    }

    // ===================== 天使核心 =====================

    /**
    /**
     * 天使核心：在<b>点击面的背面</b>放一格。
     * <p>
     * 照 constructionwand 的 ActionAngel 做的 —— 它只算一个位置：
     * {@code origin.offset(face.getOpposite().getNormal())}，即被点击方块的另一侧。
     * （原先这里写成"沿背面穿透最多 ANGEL_DISTANCE 格"，与参考实现不符。）
     */
    public static List<Change> placeAngel(Player player, Level level, BlockPos clickedPos, Direction face) {
        List<Change> changes = new ArrayList<>();
        BlockState origin = level.getBlockState(clickedPos);
        if (origin.isAir() || !isPlaceable(findMaterial(player))) {
            return changes;
        }
        Direction through = face.getOpposite();
        BlockPos target = clickedPos.relative(through);
        Change change = tryPlaceAt(level, player, clickedPos, through, target, findMaterial(player).getItem());
        if (change != null) {
            changes.add(change);
            if (!level.isClientSide()) {
                playPlaceSound(level, changes);
            }
        }
        return changes;
    }

    /**
     * 对着空气右键时的半空放置：在视线前方找一格可替换的位置，
     * 并借用它任意一侧的实心方块作为放置面。
     */
    public static List<Change> placeInAir(Player player, Level level) {
        List<Change> changes = new ArrayList<>();
        if (!isPlaceable(player.getOffhandItem())) {
            return changes;
        }
        if (player.fallDistance > MAX_FALL_DISTANCE) {
            return changes;
        }
        Vec3 eye = player.getEyePosition();
        BlockPos target = BlockPos.containing(eye.add(player.getLookAngle().scale(AIR_PLACE_DISTANCE)));
        if (!level.getBlockState(target).canBeReplaced()) {
            return changes;
        }
        for (Direction support : Direction.values()) {
            BlockPos supportPos = target.relative(support);
            if (level.getBlockState(supportPos).canBeReplaced()) {
                continue;
            }
            Change change = tryPlaceAt(level, player, supportPos, support.getOpposite(), target,
                    player.getOffhandItem().getItem());
            if (change != null) {
                changes.add(change);
                break;
            }
        }
        if (!level.isClientSide()) {
            // 整批放完只播一次：逐格播会吵成一片。
            playPlaceSound(level, changes);
        }
        return changes;
    }

    // ===================== 破坏核心 =====================

    /**
     * 破坏所视那一侧的一排方块，且不掉落任何物品。
     *
     * @return 本次连带破坏的所有改动（不含玩家手动挖掉的那一格）
     */
    /**
    /**
    /**
     * 破坏核心：在被点击那一格所在的面上，洪泛破坏连成一片的<b>同种</b>方块。
     * <p>
     * 照 constructionwand 的 ActionDestruction 做的：扩散方向取该面平面内的 4 个正向
     * <b>加上 4 条对角线</b>（参考实现是 8 个方向），只在"下一个方块与原始方块同类"时继续；
     * 被破坏的方块直接消失、不掉落。
     */
    public static List<Change> destroyFrom(Player player, Level level, BlockPos clickedPos, Direction face) {
        List<Change> changes = new ArrayList<>();
        BlockState origin = level.getBlockState(clickedPos);
        if (origin.isAir()) {
            return changes;
        }
        List<Direction> dirs = planeDirections(face);
        Set<BlockPos> seen = new HashSet<>();
        java.util.ArrayDeque<BlockPos> queue = new java.util.ArrayDeque<>();
        seen.add(clickedPos);
        queue.add(clickedPos);
        while (!queue.isEmpty() && changes.size() < MAX_DESTROY) {
            BlockPos pos = queue.poll();
            BlockState state = level.getBlockState(pos);
            if (!state.isAir() && state.getBlock() == origin.getBlock()
                    && state.getDestroySpeed(level, pos) >= 0.0F && !state.hasBlockEntity()) {
                changes.add(new Change(pos.immutable(), state, ItemStack.EMPTY));
                level.destroyBlock(pos, false, player);
            }
            for (Direction dir : dirs) {
                BlockPos next = pos.relative(dir);
                if (!seen.add(next)) {
                    continue;
                }
                BlockState nextState = level.getBlockState(next);
                if (!nextState.isAir() && nextState.getBlock() == origin.getBlock()) {
                    queue.add(next);
                }
            }
        }
        return changes;
    }

    /**
     * 破坏所视那一侧连续的一排<b>相同</b>方块，且不掉落任何物品。
     * <p>
     * 说明里的上限：「各种材质的手杖单次最多破坏 4 个方块」。
     *
     * @return 本次连带破坏的所有改动（不含玩家手动挖掉的那一格）
     */
    public static List<Change> destroyRow(Player player, Level level, BlockPos brokenPos) {
        List<Change> changes = new ArrayList<>();
        Vec3 look = player.getLookAngle();
        Direction face = Direction.getNearest(look.x, look.y, look.z);
        Direction extend = extensionDirection(player, face);
        BlockState origin = level.getBlockState(brokenPos);
        for (int i = 1; i <= MAX_DESTROY; i++) {
            BlockPos target = brokenPos.relative(extend, i);
            BlockState state = level.getBlockState(target);
            // 只破坏"连续的相同方块"：碰上别的方块或空气就停。
            if (state.isAir() || state.getBlock() != origin.getBlock()) {
                break;
            }
            // 基岩一类硬度为负的方块不动；带方块实体的也跳过（否则容易留下幽灵方块）。
            if (state.getDestroySpeed(level, target) < 0.0F || state.hasBlockEntity()) {
                break;
            }
            // 先记下原状再破坏：撤销时靠它把方块原样放回去。
            // destroyBlock(..., false) = 不掉落，对应说明里的"会直接消失，不会掉落"。
            changes.add(new Change(target.immutable(), state, ItemStack.EMPTY));
            level.destroyBlock(target, false, player);
        }
        return changes;
    }

    // ===================== 内部工具 =====================

    /**
     * 与面法线垂直、且最贴近玩家朝向的那条轴。
     * 俯视顶面时会挑出你正对的那条水平方向，于是得到"朝面向的那一侧延伸一排"。
     */
    public static Direction extensionDirection(Player player, Direction face) {
        Vec3 look = player.getLookAngle();
        Direction.Axis faceAxis = face.getAxis();
        Direction best = null;
        double bestDot = -2.0D;
        for (Direction candidate : Direction.values()) {
            if (candidate.getAxis() == faceAxis) {
                continue;
            }
            double dot = candidate.getStepX() * look.x
                    + candidate.getStepY() * look.y
                    + candidate.getStepZ() * look.z;
            if (dot > bestDot) {
                bestDot = dot;
                best = candidate;
            }
        }
        return best == null ? player.getDirection() : best;
    }

    /**
     * 借用原版放置流程放一格：support 是作为依托的方块，
     * face 是从 support 指向 target 的方向。
     * 用 BlockItem.place 而不是直接 setBlock，音效、可替换判定、
     * 方块实体初始化与生存消耗都由原版负责。
     *
     * @return 成功时返回这一步的可撤销记录；失败返回 null
     */
    /**
     * 借用原版放置流程放一格：support 是作为依托的方块，face 是从 support 指向 target 的方向。
     * <p>
     * 材料取自<b>背包</b>（说明里写的是"消耗背包中的方块"）。但 BlockItem.place 只会从手里扣物品，
     * 所以把要用的那一叠临时换到副手，放完立刻换回，真正 shrink 的是背包里那一叠。
     *
     * @return 成功时返回这一步的可撤销记录；失败返回 null
     */
    private static Change tryPlaceAt(Level level, Player player, BlockPos support, Direction face, BlockPos target,
                                     net.minecraft.world.item.Item materialItem) {
        // 【必须】由调用方指定要放哪一种物品。
        // 之前这里自己调 findMaterial(player)（不带过滤）取"背包里第一件可放置的方块"，
        // 于是判定时说"要灵魂沙"，真正放下去时却可能是凋零骷髅头 ——
        // 副手 override 那条路也一样被无视。现在材料只由一个地方决定。
        ItemStack material = materialItem == null ? findMaterial(player) : findMaterial(player, materialItem);
        if (!isPlaceable(material) || !level.getBlockState(target).canBeReplaced()) {
            return null;
        }
        // 刻意不检查"依托是否实心"：建筑手杖的 isPositionPlaceable 只看
        // "目标是空气、或替换模式下可替换"，并不要求依托实心。
        // BlockItem.place 自己会在放不下时返回失败，交给它判断即可。
        BlockState previous = level.getBlockState(target);
        // 归还物品要复制"真正被放下去的那个"，而不是 new ItemStack(它的物品类型)：
        // 后者只保留物品 id，机器里的东西、设置、附魔都会丢，撤销时给回的是一台空机器。
        ItemStack refund = material.copyWithCount(1);
        ItemStack offhandBackup = player.getOffhandItem();
        ItemStack one = material.copyWithCount(1);
        InteractionResult result;
        player.setItemInHand(InteractionHand.OFF_HAND, one);
        try {
            BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(support), face, support, false);
            BlockPlaceContext context = new BlockPlaceContext(player, InteractionHand.OFF_HAND, one, hit);
            result = ((BlockItem) one.getItem()).place(context);
        } finally {
            player.setItemInHand(InteractionHand.OFF_HAND, offhandBackup);
        }
        if (!result.consumesAction()) {
            return null;
        }
        // 材料可能来自潜影盒/收纳袋，那种情况下 shrink 背包里那一叠是无效的，
        // 统一交给 ContainerMaterials.consumeOne（它自己按 背包 -> 副手 -> 容器 的顺序取）。
        consumeMaterial(player, material.getItem());
        // 音效不在这里播：一次建造会连续放很多格，逐格播会吵成一片。
        // 由调用方在整批结束后调 playPlaceSound 播一次。
        return new Change(target.immutable(), previous, refund);
    }
}