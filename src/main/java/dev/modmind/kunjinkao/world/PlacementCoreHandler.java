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
     * 基础建造模式：生存下单次最多放置 32 个方块。
     * <p>
     * 这是建筑手杖本身的规定（"默认情况下，生存模式下的手杖单次最多放置32个方块"），
     * 创造模式不受这个限制，但仍留一个安全上限，免得一次把整个区块铺满。
     */
    private static final int MAX_PLACE_SURVIVAL = 32;
    /** 创造模式下的安全上限。 */
    private static final int MAX_PLACE_CREATIVE = 1024;
    /**
     * 破坏模式：单次最多破坏 4 个方块。
     * <p>
     * 同样来自建筑手杖的规定（"各种材质的手杖单次最多破坏4个方块"）。
     */
    private static final int MAX_DESTROY = 4;
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
        return ItemStack.EMPTY;
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
     * 基础建造模式：在被点击的那一面所在的平面上，向外铺开一片方块。
     * <p>
     * 两条规则（都来自用户说明）：
     * <ul>
     *   <li><b>材料</b>：副手拿着一种和点击方块<b>不同</b>的方块时，视作显式指定材料 ——
     *       用它，并且不受限制；否则材料必须<b>和点击的方块同种</b>
     *       （副手优先、其次背包；找不到就一格都不放）。
     *       也就是「如果不是点击的方块就不要放置」。</li>
     *   <li><b>依托</b>：只要<b>相邻</b>就行 —— 目标格子周围六个方向里有任意一个是实心即可，
     *       不要求正后方那一格。这样墙沿、地板边缘、台阶边上都能正常铺。</li>
     * </ul>
     * 面平面上的两个切向轴由 face 的法线轴排除而来，所以墙、地面、天花板都成立。
     * 上限仍是说明里的生存 32 / 创造 1024。
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
        // 副手拿着"另一种"方块 = 显式指定材料，跳过"必须同种"这条。
        ItemStack offhand = player.getOffhandItem();
        boolean override = isPlaceable(offhand) && offhand.getItem() != originItem;
        ItemStack material = override ? offhand : findMaterial(player, originItem);
        if (!isPlaceable(material)) {
            return changes;
        }
        BlockPos first = clickedPos.relative(face);
        Direction.Axis normalAxis = face.getAxis();
        List<Direction> tangents = new ArrayList<>(2);
        for (Direction.Axis axis : Direction.Axis.values()) {
            if (axis != normalAxis) {
                tangents.add(Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE));
            }
        }
        Direction ta = tangents.get(0);
        Direction tb = tangents.get(1);
        int limit = player.isCreative() ? MAX_PLACE_CREATIVE : MAX_PLACE_SURVIVAL;
        int placed = 0;
        int maxRing = 32;
        for (int r = 0; r <= maxRing && placed < limit; r++) {
            for (int i = -r; i <= r && placed < limit; i++) {
                for (int j = -r; j <= r && placed < limit; j++) {
                    if (Math.max(Math.abs(i), Math.abs(j)) != r) {
                        continue;
                    }
                    ItemStack current = override ? player.getOffhandItem() : findMaterial(player, originItem);
                    if (!isPlaceable(current)) {
                        return changes;
                    }
                    BlockPos target = first.relative(ta, i).relative(tb, j);
                    if (!level.getBlockState(target).canBeReplaced()) {
                        continue;
                    }
                    Change change = tryPlaceAdjacent(level, player, target);
                    if (change != null) {
                        changes.add(change);
                        placed++;
                    }
                }
            }
        }
        return changes;
    }

    /**
     * 放置一格，依托只要求"相邻"：在目标周围六个方向里找第一个实心的邻居当依托，
     * 朝向就是"从依托指向目标"。找不到任何相邻实心方块就放弃这一格。
     */
    private static Change tryPlaceAdjacent(Level level, Player player, BlockPos target) {
        for (Direction side : Direction.values()) {
            BlockPos support = target.relative(side);
            BlockState supportState = level.getBlockState(support);
            if (supportState.canBeReplaced()) {
                continue;
            }
            Change change = tryPlaceAt(level, player, support, side.getOpposite(), target);
            if (change != null) {
                return change;
            }
        }
        return null;
    }

    // ===================== 天使核心 =====================

    /**
     * 把方块放到所视方块的背面。若背面已被占用，就沿同一方向继续穿透，
     * 最多 ANGEL_DISTANCE 格再找落脚点 —— 对应官方的"天使距离"。
     */
    public static List<Change> placeAngel(Player player, Level level, BlockPos clickedPos, Direction face) {
        List<Change> changes = new ArrayList<>();
        if (!isPlaceable(findMaterial(player))) {
            return changes;
        }
        Direction through = face.getOpposite();
        for (int i = 1; i <= ANGEL_DISTANCE; i++) {
            BlockPos target = clickedPos.relative(through, i);
            Change change = tryPlaceAt(level, player, target.relative(face), through, target);
            if (change != null) {
                changes.add(change);
                break;
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
            Change change = tryPlaceAt(level, player, supportPos, support.getOpposite(), target);
            if (change != null) {
                changes.add(change);
                break;
            }
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
    private static Change tryPlaceAt(Level level, Player player, BlockPos support, Direction face, BlockPos target) {
        ItemStack material = findMaterial(player);
        if (!isPlaceable(material) || !level.getBlockState(target).canBeReplaced()) {
            return null;
        }
        if (!level.getBlockState(support).isFaceSturdy(level, support, face)) {
            return null;
        }
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
        material.shrink(1);
        // 放置音效。
        // BlockItem.place 自己会播一次，但它的第一个参数是"要排除的玩家"，
        // 传的是放置者本人 —— 结果只有旁人听得到，放的人反而没声音。
        // 这里用 null 明确播给所有人，音色/音量取自这个方块的 SoundType。
        BlockState placed = level.getBlockState(target);
        net.minecraft.world.level.block.SoundType sound = placed.getSoundType();
        level.playSound(null, target, sound.getPlaceSound(), net.minecraft.sounds.SoundSource.BLOCKS,
                (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
        return new Change(target.immutable(), previous, refund);
    }
}