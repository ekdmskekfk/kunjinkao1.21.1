package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.KunJinKaoPalette;
import dev.modmind.kunjinkao.SwordRegistry;
import dev.modmind.kunjinkao.config.AdminToolConfig;
import dev.modmind.kunjinkao.config.QolStateSavedData;
import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.network.QolStatePayload;
import dev.modmind.kunjinkao.network.ZoneStatusPayload;
import dev.modmind.kunjinkao.qol.QolSetting;
import dev.modmind.kunjinkao.qol.QolSettings;
import dev.modmind.kunjinkao.world.ZoneExcavationJob;
import dev.modmind.kunjinkao.world.ZoneSelection;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Rotations;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能面板（QoL）的服务端权威实现：接收改动、校验、落库、生效、回执。
 * <p>
 * <b>所有判定都在这里。</b> 客户端只发"我想改成什么"，授权、取值范围、前置依赖
 * 一律在服务端重算一遍 —— 面板上的置灰只是提示，不是防线。
 * <p>
 * 生效策略分两类：
 * <ul>
 *   <li><b>状态型</b>（夜视、村庄英雄、长臂善舞）：把状态挂在玩家身上，
 *       由 {@link #onPlayerTick} 定期校正。这样死亡重生、换维度、被别的模组清掉之后
 *       都能自己回来，不需要为每种情况写一条恢复逻辑。</li>
 *   <li><b>查询型</b>（凌空采掘）：没有"挂上去"的动作，由消费方
 *       （{@code SwordMiningPenaltyHandler}）在读的时候查一次当前值。</li>
 * </ul>
 */
public final class KunJinKaoQolHandler {

    private static final Logger LOGGER = LogManager.getLogger("KunJinKao");

    // ===== 星瞳夜视（80）=====
    /**
     * 我们自己施加的夜视时长（tick）。
     * <p>
     * 这个数值同时是"这条夜视是不是我们放的"的判据：剩余时长比它长，
     * 就说明是玩家自己喝的药水或别的模组给的，我们只续期不覆盖、关闭时也不摘。
     * <p>
     * <b>它和 {@link #NIGHT_VISION_RENEW_BELOW}、{@link #NIGHT_VISION_INTERVAL} 三者必须满足
     * 「续期阈值 − 检查间隔 &gt; 0」且留足余量。</b> 这里 1200 − 100 = 1100 的余量下，
     * 效果最低也有 700 tick，永远不会掉到 0。
     */
    private static final int NIGHT_VISION_DURATION = 1200;
    /**
     * 剩余时长低于这个值就补一次。
     * <p>
     * <b>必须显著大于检查间隔</b>：早先这两个值都是 100，于是每次检查时剩余时长刚好等于阈值、
     * 判定不成立，下一次检查时它已经掉到 0 —— 表现就是夜视每隔一会儿真的消失一下再被加上，
     * 客户端看到「移除 + 重新施加」两个包，图标闪一下。这不是显示问题，是效果真的断过。
     */
    private static final int NIGHT_VISION_RENEW_BELOW = 800;

    // ===== 万民敬仰（88）=====
    private static final int HERO_DURATION = 300;
    private static final int HERO_RENEW_BELOW = 100;
    /** 参考实现换算：等级 N 对应 amplifier = N × 4 - 1（原版每级 +4 点好感）。 */
    private static final int HERO_AMPLIFIER_PER_LEVEL = 4;

    // ===== 长臂善舞（89）=====
    private static final ResourceLocation REACH_BLOCK_MODIFIER =
            ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "qol_reach_block");
    private static final ResourceLocation REACH_ENTITY_MODIFIER =
            ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "qol_reach_entity");

    /** 校正周期（tick）。20 是 1 秒，够快又完全不值得优化。 */
    private static final int RECONCILE_INTERVAL = 20;
    /** 夜视续期周期：比 {@link #RECONCILE_INTERVAL} 慢一档，因为它的续期窗口本来就很长。 */
    private static final int NIGHT_VISION_INTERVAL = 100;
    /** 改服务器级设置所需的最低权限等级。2 = 原版 OP，与"能用命令方块"同一档。 */
    private static final int SERVER_SETTING_PERMISSION_LEVEL = 2;

    // ===== 选区作业（127）=====
    /**
     * 两次按键之间的确认窗口（毫秒）。
     * <p>
     * 二次确认做成"再按一次同一个键"而不是弹一个界面：这个操作本来就是键盘操作，
     * 中间插一个 GUI 反而会让人下意识地点"确定"。窗口给 10 秒 —— 够读一遍提示，
     * 又短到不会在他已经忘了这回事的时候突然开始挖。
     */
    private static final long ZONE_CONFIRM_WINDOW_MS = 10_000L;
    /** 角点最远能设多远（格）。防止改过的客户端把角点设到天边，框出一整片地形。 */
    private static final double ZONE_MAX_CORNER_DISTANCE = 128.0D;

    /** 待确认的执行请求：第一次按的时刻，以及那次请求的是哪种作业。 */
    private record PendingConfirm(long at, int mode) {
    }

    /** 待确认的执行请求，按玩家存。 */
    private static final Map<UUID, PendingConfirm> ZONE_PENDING_CONFIRM = new ConcurrentHashMap<>();

    // ===================== 管理员装饰（201–203）=====================
    /*
     * 三项装饰（乱码称号 / 浮游挂件 / 光环拖尾）共用一套载体：**隐形的盔甲架**。
     *
     * 为什么不是 BlockDisplay：1.21 把 Display 的全部 setter 都收成了 private
     * （setBlockState、setTransformation、setPosRotInterpolationDuration …），代码里配不出来，
     * 只能走 /summon 那种 NBT 路子；而盔甲架是原版实体、API 全公开，
     * **别的玩家不装这个模组也看得见** —— 这是本次需求里最硬的一条，载体必须是原版就能渲染的东西。
     *
     * 三处关键细节都对着 1.21.1 的渲染代码确认过：
     *   - Invisible 只让"身体"不画。LivingEntityRenderer 里 render layers（手持物）在身体可见性
     *     之外单独跑，所以"隐形盔甲架举着剑"，看到的只有剑；
     *   - ArmorStandRenderer#shouldShowName 只判 isCustomNameVisible，不看隐形，
     *     所以隐形盔甲架的名字照样浮在头顶 —— 称号牌就是这么来的；
     *   - 玩家自己的名牌改不了：Player#getDisplayName 走的是 profile 名 + 队伍前缀，
     *     完全不看 CustomName，setCustomName 到玩家身上对别人来说什么都不会发生。
     *
     * 全部设成 Marker：没有碰撞箱、不可拾取、不受活塞推动 —— 装饰不该被玩家点掉、
     * 也不该抢走玩家准星原来的目标。
     */

    /** 装饰实体身上的标记，排查问题时报得出是谁的。 */
    private static final String COSMETIC_ENTITY_TAG = "kunjinkao_cosmetic";

    /** 剑的个数。 */
    private static final int ORBIT_BIT_COUNT = 3;
    /** 排在身后多远（格）。 */
    private static final double ORBIT_BACK_DISTANCE = 0.95D;
    /** 左右两把各偏出去多少（格）；中间那把正好在正中。 */
    private static final double ORBIT_SIDE_SPREAD = 0.55D;
    /** 剑柄高度（相对脚底，格）。 */
    private static final double ORBIT_HEIGHT = 1.2D;
    /** 左右轻摆的幅度（格）。<b>三把同步摆</b>，所以间距不变；见 tickOrbit 里的说明。 */
    private static final double ORBIT_SWAY = 0.1D;
    /** 上下浮动的幅度（格）。每把有自己的相位差，看起来才不是一整块。 */
    private static final double ORBIT_BOB = 0.05D;
    /** 摆动/浮动速度（弧度每 tick）。 */
    private static final double ORBIT_SWAY_SPEED = 0.06D;
    /**
     * 举剑时，持物渲染在实体原点上方多高（格）。
     * <p>
     * 这是整套装饰里唯一只能"看着调"的数：它是"0.5 缩放的盔甲架手部变换"叠上物品的
     * thirdperson_righthand 变换算出来的，不值得在代码里复算一遍。剑看起来偏高就调大它。
     */
    private static final double BIT_HAND_OFFSET = 0.5D;
    /** 举剑的手臂姿势：原版盔甲架的默认右手姿势，剑尖朝上。 */
    private static final Rotations SWORD_POSE_IDLE = new Rotations(-15.0F, 0.0F, 10.0F);
    /**
     * 向下刺的手臂姿势：整个手部坐标系绕 X 翻 180°。
     * <p>
     * 手持物的渲染是"先用手臂变换、再绕 X 转 -90°"，所以把手翻过来，剑就朝下了。
     * 这也是唯一能让剑朝下的办法：盔甲架的身体俯仰不参与渲染，只有各部位的姿势参与。
     */
    private static final Rotations SWORD_POSE_STAB = new Rotations(180.0F, 0.0F, 0.0F);
    /** 刺击姿势下，剑尖相对实体原点的偏移（和 {@link #BIT_HAND_OFFSET} 一起调）。 */
    private static final double STAB_HAND_OFFSET = 0.5D;

    // ===== 反击（202 附带）=====
    /** 一次反击持续多久（tick）。100 = 5 秒。 */
    private static final int STRIKE_DURATION = 100;
    /**
     * 两下之间的间隔（tick）。
     * <p>
     * 10 不是随手挑的：玩家受击后有 20 tick 无敌帧，而 {@code LivingEntity#hurt} 的判定是
     * {@code invulnerableTime > 10} 才丢弃这一下 —— 间隔取 10 tick 时，下一次伤害正好落在
     * 无敌帧的第 10 tick 上，每一击都能真的打出去，同时完全不用去动玩家的无敌时间
     * （清掉它会让别人打这个玩家的伤害也一起变实，反击就不"干净"了）。
     */
    private static final int STRIKE_DAMAGE_INTERVAL = 10;
    /** 每一击的伤害点数。 */
    private static final float STRIKE_DAMAGE = 1.0F;
    /**
     * 低于这个血量就不再打。
     * <p>
     * 3.0 = 一颗心（2.0）+ 这一击（1.0）：打完至少还剩一颗心。
     * 这套东西是"打管理员会还手"的惩罚，不是处决。
     */
    private static final float STRIKE_MIN_HEALTH = 3.0F;
    /** 追击速度：每 tick 靠近剩余距离的这个比例。越近越慢，停在头顶时不会抖。 */
    private static final double STRIKE_CHASE = 0.45D;
    /** 离头顶这么近才算"飞到了"，到了才开始扣血。 */
    private static final double STRIKE_REACH = 0.55D;
    /** 剑尖停在头顶上方多高（格）。 */
    private static final double STRIKE_HOVER = 0.45D;
    /** 两下之间抬起多少（格）：每一下都是先抬起来再刺下去，不然看起来只是贴着不动。 */
    private static final double STRIKE_RETRACT = 0.4D;
    /** 离下一击还有几 tick 之内开始压下去。要留够追击收敛的时间，否则刺击和伤害对不上。 */
    private static final int STRIKE_THRUST_WINDOW = 3;

    // ===== 光环与拖尾（203）=====
    /** 光环每几 tick 撒一圈。 */
    private static final int AURA_INTERVAL = 2;
    /** 光环半径（格）。 */
    private static final double AURA_RADIUS = 0.9D;
    /** 光环离脚底多高（格）。 */
    private static final double AURA_HEIGHT = 0.15D;
    /** 一圈的点数。 */
    private static final int AURA_POINTS = 10;
    /** 每 tick 转过的弧度：一圈约 4 秒。 */
    private static final double AURA_SPIN = 0.05D;
    /** 拖尾：走过这么远才补一段粒子，站着不动就不刷。 */
    private static final double TRAIL_STEP = 0.55D;
    /** 一次最多补几个点：瞬移几千格时用它封顶，免得一 tick 撒满整条线上。 */
    private static final int TRAIL_MAX_POINTS = 8;
    /** 称号牌浮在头顶多高（格）。正好落在原版名牌上方一点。 */
    private static final double TITLE_HEIGHT = 0.45D;

    /**
     * 光环与拖尾的淡蓝。
     * <p>
     * 它不占 {@link KunJinKaoPalette} 的槽位：那四个颜色是<b>剑的语义色</b>
     * （常态/加速/损坏/错误），装饰是另一条线，硬塞进去只会让"按含义定色"这条规矩失效。
     */
    private static final int AURA_COLOR = 0xFF9ED8FF;
    private static final DustParticleOptions AURA_PARTICLE = cosmeticDust(AURA_COLOR, 1.05F);
    /** 拖尾用同一个淡蓝，只是更小更暗一档：同色系里靠尺寸分层，否则两团粒子会糊在一起。 */
    private static final DustParticleOptions TRAIL_PARTICLE = cosmeticDust(AURA_COLOR, 0.75F);

    private static DustParticleOptions cosmeticDust(int argb, float scale) {
        return new DustParticleOptions(new Vector3f(
                KunJinKaoPalette.red(argb), KunJinKaoPalette.green(argb), KunJinKaoPalette.blue(argb)), scale);
    }

    /*
     * 关于"挂件要不要连主人自己也看不见"：现在的答案是**不隐藏**，只把它们排在身后。
     *
     * 记一笔当时的取舍，免得以后又有人想加一遍"客户端不画自己那份"：
     * 隐藏是能做到的（实体名里带主人 UUID，客户端在 RenderLivingEvent.Pre 里取消渲染），
     * 但那只在"自己第一人称不想看到"这一条上有价值 —— 而位置本来就在镜头后面，
     * 第一人称根本看不到。真正会露出来的只有 F5 第三人称，而管理员自己开第三人称时
     * 看到自己的挂件是合理的（那是他的装饰）。所以为此多一条"名字当同步通道 + 客户端特判"
     * 的链路不划算，删掉了。
     */

    /** 每个在线管理员一份装饰状态，键是玩家 UUID。 */
    private static final Map<UUID, Cosmetics> COSMETICS = new ConcurrentHashMap<>();

    /** 一个管理员的装饰状态：实体句柄 + 反击进度。 */
    private static final class Cosmetics {
        /** 身后的三把剑；索引 0 兼作反击时飞出去的那一把。 */
        final List<CosmeticBit> bits = new ArrayList<>(ORBIT_BIT_COUNT);
        /** 头顶称号牌。 */
        CosmeticBit title;
        /** 正在被反击的目标；null 表示没有反击在进行。 */
        UUID strikeTarget;
        /** 反击已经持续了多少 tick。 */
        int strikeTicks;
        /** 下一击在 strikeTicks 到达这个值时落地。 */
        int nextStrikeTick;
        /** 反击结束后，索引 0 那一把正在飞回背后。 */
        boolean returning;
        /** 上一次撒拖尾粒子的位置；null 表示"刚开，还没有上一处"。 */
        Vec3 trailFrom;

        /** 结束反击。{@code returning} 由调用方决定：正常打完要飞回去，挂件被关掉则不用。 */
        void endStrike() {
            strikeTarget = null;
            strikeTicks = 0;
            nextStrikeTick = 0;
        }
    }

    /**
     * 装饰用的隐形盔甲架。
     * <p>
     * 覆写 {@link #shouldBeSaved()} 返回 false 是关键的一步：这些实体只在"主人挂着装饰"的这段时间里
     * 存在，写进存档只会在服务器崩过之后留下一堆没人认领的浮空剑。
     * 不落盘 = 世界重载/重启之后一定不残留，比事后清扫可靠得多。
     */
    private static final class CosmeticBit extends ArmorStand {
        CosmeticBit(ServerLevel level) {
            super(EntityType.ARMOR_STAND, level);
        }

        @Override
        public boolean shouldBeSaved() {
            return false;
        }

        /**
         * 收到位置更新时直接落位，不做插值。
         * <p>
         * {@code LivingEntity#lerpTo} 只是把目标位置记下来（{@code lerpSteps = 3}），
         * 再由 {@code LivingEntity#tick} 用 3 个 tick 慢慢挪过去 —— 那是给怪物/动物准备的：
         * 它们的移动是服务端算的，插值能把 20 Hz 的更新抹成平滑的走位。
         * <p>
         * 这几个装饰是<b>贴在玩家身上</b>的，套用同一套插值的结果就是：玩家一走，它们
         * 拖在后面 3 个 tick（冲刺时快 0.8 格），看起来就是"慢半拍地跟着"。
         * 直接落位之后，渲染仍然会按 {@code xo/yo/zo → getX()} 做逐帧插值，所以画面依旧平滑，
         * 只是不再落后。这也是原版 {@code Entity#lerpTo} 的默认行为（我们只是不继承
         * {@code LivingEntity} 那份平滑）。
         */
        @Override
        public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
            this.setPos(x, y, z);
            this.setRot(yRot, xRot);
        }
    }

    // ===================== 网络入口 =====================

    /**
     * 客户端请求改一项设置。
     * <p>
     * 未知 settingId 直接忽略：新旧端版本号已经隔开了，但多一层防御不花钱，
     * 免得将来有人手改包体时在这里抛异常。
     */
    public static void handleSettingChange(Player source, int settingId, int value) {
        if (!(source instanceof ServerPlayer player)) {
            return;
        }
        QolSetting setting = QolSetting.byId(settingId);
        if (setting == null) {
            return;
        }

        if (setting.scope() == QolSetting.Scope.SERVER) {
            // 服务器级：只有 OP（权限等级 2）能改，值直接写回 toml。
            // 注意这里不是"个人设置的另一种存法" —— 它影响全服，所以既不进玩家存档，也不做全量广播之外的事。
            if (!player.hasPermissions(SERVER_SETTING_PERMISSION_LEVEL)) {
                player.displayClientMessage(Component.translatable("message.kunjinkao.qol_denied_op"), true);
                sendState(player, setting, false);
                return;
            }
            int clamped = QolSettings.clamp(setting, value);
            AdminToolConfig.setQolServerValue(setting, clamped);
            // 服务器级值所有人看到的都一样，改完要广播：别人开着的面板上那一行也得跟着变。
            broadcastState(setting);
            return;
        }

        if (!AdminToolConfig.isAuthorized(player.getUUID())) {
            // 未授权：拒绝改动，但仍然回一条状态，让面板把这一行刷新成服务端的真实值，
            // 而不是停在客户端自己以为的那个值上。
            player.displayClientMessage(Component.translatable("message.kunjinkao.qol_denied"), true);
            sendState(player, setting, false);
            return;
        }

        int clamped = QolSettings.clamp(setting, value);
        QolStateSavedData.get(player.server).setValue(player.getUUID(), setting, clamped);
        applyTo(player, setting);
        sendState(player, setting, true);
    }

    // ===================== 选区作业（127）=====================

    /**
     * 设一个角点（木棍右键的唯一入口）。
     * <p>
     * 四道判定都在这里：总闸、授权、作业占用、距离。抽成一个入口而不是各处各判一次，
     * 是因为"改客户端就能框到天边"这种事只要漏掉一处就等于没有防线；
     * 复制出来的第二份判定迟早会有一边改了另一边没改。
     * <p>
     * 坐标不信任客户端：改过的客户端可以把角点设到任意远处，框出一整片地形再执行。
     * 这里按玩家眼睛到目标方块的距离卡一道，和原版交互距离同一个量级但宽得多
     * （框选本来就要点远处的方块）。
     *
     * @return 是否真的设下了角点；被拒绝时原因已经发给玩家
     */
    public static boolean trySetZoneCorner(ServerPlayer player, BlockPos pos) {
        if (!canUseZoneExcavation(player)) {
            return false;
        }
        if (ZoneExcavationJob.isRunning(player)) {
            player.displayClientMessage(Component.translatable("message.kunjinkao.zone_busy"), true);
            return false;
        }
        if (player.getEyePosition().distanceToSqr(Vec3.atCenterOf(pos))
                > ZONE_MAX_CORNER_DISTANCE * ZONE_MAX_CORNER_DISTANCE) {
            player.displayClientMessage(Component.translatable("message.kunjinkao.zone_too_far"), true);
            return false;
        }
        int corner = ZoneSelection.setNextCorner(player, pos.immutable());
        // 改了选区就作废上一次的待确认 —— 否则"框 A 按一次、改成框 B 再按一次"
        // 会在玩家以为只是改了选区的时候直接开工。
        ZONE_PENDING_CONFIRM.remove(player.getUUID());
        player.displayClientMessage(Component.translatable(
                corner == ZoneSelection.CORNER_A
                        ? "message.kunjinkao.zone_set_corner_a"
                        : "message.kunjinkao.zone_set_corner_b",
                pos.toShortString()), true);
        sendZoneStatus(player);
        return true;
    }

    /**
     * 请求执行。
     * <p>
     * 第一次按只给一句确认提示，窗口内再按一次才真的开始。地形是不可逆的，
     * 所以这里宁可让玩家多按一次，也不做"按了就挖"。
     * <p>
     * 确认文案按当前模式分开：破坏与放置的后果完全不同（一个是地形没了，
     * 一个是材料没了），用同一句话会让玩家在按第二下之前还想确认一次自己选的是哪个。
     */
    public static void handleZoneExecute(Player source, int mode) {
        if (!(source instanceof ServerPlayer player) || !canUseZoneExcavation(player)) {
            return;
        }
        if (ZoneExcavationJob.isRunning(player)) {
            player.displayClientMessage(Component.translatable("message.kunjinkao.zone_busy"), true);
            return;
        }
        ZoneSelection.Corners corners = currentCorners(player);
        if (corners == null || corners.a() == null || corners.b() == null) {
            player.displayClientMessage(Component.translatable("message.kunjinkao.zone_need_corners"), true);
            // 再说一句"怎么框"：角点已经不是按键设的了，玩家按下执行键时最缺的就是这句。
            player.displayClientMessage(Component.translatable("message.kunjinkao.zone_need_stick"), true);
            return;
        }
        long now = System.currentTimeMillis();
        PendingConfirm pending = ZONE_PENDING_CONFIRM.get(player.getUUID());
        // 确认必须"同一个键连按两次"：破坏与放置各有自己的执行键，
        // 不比对模式的话，先按破坏再按放置会被当成对破坏的确认 —— 玩家以为要填墙，结果墙没了。
        boolean confirmed = pending != null
                && pending.mode() == mode
                && now - pending.at() <= ZONE_CONFIRM_WINDOW_MS;
        if (!confirmed) {
            ZONE_PENDING_CONFIRM.put(player.getUUID(), new PendingConfirm(now, mode));
            int sizeX = Math.abs(corners.a().getX() - corners.b().getX()) + 1;
            int sizeY = Math.abs(corners.a().getY() - corners.b().getY()) + 1;
            int sizeZ = Math.abs(corners.a().getZ() - corners.b().getZ()) + 1;
            player.displayClientMessage(Component.translatable(
                    mode == ZoneExcavationJob.MODE_PLACE
                            ? "message.kunjinkao.zone_confirm_place"
                            : "message.kunjinkao.zone_confirm",
                    sizeX, sizeY, sizeZ, sizeX * sizeY * sizeZ), false);
            return;
        }
        ZONE_PENDING_CONFIRM.remove(player.getUUID());
        if (ZoneExcavationJob.start(player, corners.a(), corners.b(), mode)) {
            ZoneSelection.clear(player);
            sendZoneStatus(player);
        }
    }

    /** 取消：正在跑就停作业，否则清掉当前选区。 */
    public static void handleZoneCancel(Player source) {
        if (!(source instanceof ServerPlayer player)) {
            return;
        }
        ZONE_PENDING_CONFIRM.remove(player.getUUID());
        if (ZoneExcavationJob.cancel(player)) {
            sendZoneStatus(player);
            return;
        }
        if (currentCorners(player) != null) {
            ZoneSelection.clear(player);
            player.displayClientMessage(Component.translatable("message.kunjinkao.zone_cleared"), true);
            sendZoneStatus(player);
        }
    }

    /**
     * 缩放当前选区：木棍 + Shift + 滚轮。
     * <p>
     * <b>轴向与正负由服务端按玩家朝向算，不信客户端。</b> 客户端只发「往上滚了几格」，
     * 这里取 {@code getLookAngle()}：绝对值最大的那个分量决定动哪根轴，
     * 它的正负决定动哪一侧 —— 抬头看 = 往上扩、低头看 = 往下扩，朝 +X 看 = 往 +X 扩，
     * 上/下/左/右/前/后都从这一条规则里自然长出来，不需要为每个方向各写一个分支。
     * <p>
     * 权限与总闸复用 {@link #canUseZoneExcavation}：缩放改的是同一块选区，
     * 判定标准必须和执行键完全一致，否则会出现「框得动、挖不动」或者反过来。
     * <p>
     * <b>作业在跑时不让改。</b> 选区在 {@code ZoneExcavationJob.start} 那一刻就已经快照进作业了，
     * 这时改角点只会让屏幕上的框和实际在挖的范围变成两回事 —— 玩家看着框调整，
     * 以为挖的是新范围，实际挖的还是旧范围。
     *
     * @param delta 滚轮格数：正数扩大、负数缩小
     */
    public static void handleZoneAdjust(Player source, int delta) {
        if (!(source instanceof ServerPlayer player) || !canUseZoneExcavation(player)) {
            return;
        }
        if (delta == 0) {
            return;
        }
        if (ZoneExcavationJob.isRunning(player)) {
            player.displayClientMessage(Component.translatable("message.kunjinkao.zone_busy"), true);
            return;
        }

        Vec3 look = player.getLookAngle();
        double absX = Math.abs(look.x);
        double absY = Math.abs(look.y);
        double absZ = Math.abs(look.z);
        int axis;
        double along;
        if (absY >= absX && absY >= absZ) {
            // 抬头看天 = 往上扩、低头看地 = 往下扩。俯仰优先于水平，是因为「往上抬一点」
            // 在屏幕上就是高度在变，而水平分量此刻本来就很小。
            axis = ZoneSelection.AXIS_Y;
            along = look.y;
        } else if (absX >= absZ) {
            axis = ZoneSelection.AXIS_X;
            along = look.x;
        } else {
            axis = ZoneSelection.AXIS_Z;
            along = look.z;
        }
        // 被选中的分量不可能为 0（除非朝向向量整个是零向量），真出现 0 时按正向算，
        // 至少给出一个确定的行为而不是随机的。
        int direction = along < 0.0D ? -1 : 1;

        ZoneSelection.AdjustResult result = ZoneSelection.adjust(player, axis, direction, delta);
        switch (result) {
            case NO_CORNERS -> {
                player.displayClientMessage(
                        Component.translatable("message.kunjinkao.zone_adjust_need_corners"), true);
                return;
            }
            case TOO_BIG -> {
                // 报出上限本身：只说「太大了」的话，玩家会以为是自己滚得太快，
                // 于是继续滚、继续被拒，而真正的原因（服务器的单边上限）他看不到。
                player.displayClientMessage(Component.translatable(
                        "message.kunjinkao.zone_adjust_too_big",
                        AdminToolConfig.qolServerValue(QolSetting.ZONE_MAX_SIDE)), true);
                return;
            }
            case OK -> {
            }
        }

        ZoneSelection.Corners corners = currentCorners(player);
        if (corners != null && corners.a() != null && corners.b() != null) {
            player.displayClientMessage(Component.translatable("message.kunjinkao.zone_adjusted",
                    Math.abs(corners.a().getX() - corners.b().getX()) + 1,
                    Math.abs(corners.a().getY() - corners.b().getY()) + 1,
                    Math.abs(corners.a().getZ() - corners.b().getZ()) + 1), false);
        }
        // 线框立刻跟着变：不然玩家滚了半天，屏幕上的框还停在旧尺寸，只能靠猜。
        sendZoneStatus(player);
    }

    /** 权限与总闸。两道都过了才谈得上框选。 */
    private static boolean canUseZoneExcavation(ServerPlayer player) {
        if (AdminToolConfig.qolServerValue(QolSetting.ZONE_EXCAVATE_SERVER) <= 0) {
            player.displayClientMessage(Component.translatable("message.kunjinkao.zone_disabled"), true);
            return false;
        }
        if (!AdminToolConfig.isAuthorized(player.getUUID())) {
            player.displayClientMessage(Component.translatable("message.kunjinkao.qol_denied"), true);
            return false;
        }
        return true;
    }

    /** 取当前维度下的选区；选区绑在别的维度时视为没有（坐标没有可比性）。 */
    private static ZoneSelection.Corners currentCorners(ServerPlayer player) {
        ZoneSelection.Corners corners = ZoneSelection.get(player);
        if (corners == null || !corners.dimension().equals(player.level().dimension().location())) {
            return null;
        }
        return corners;
    }

    /** 把选区与进度推给本人。别人不需要知道他框了哪块地。 */
    public static void sendZoneStatus(ServerPlayer player) {
        ZoneSelection.Corners corners = currentCorners(player);
        boolean hasA = corners != null && corners.a() != null;
        boolean hasB = corners != null && corners.b() != null;
        int[] progress = ZoneExcavationJob.progress(player);
        NetworkHandler.sendToPlayer(player, new ZoneStatusPayload(
                hasA, hasA ? corners.a() : BlockPos.ZERO,
                hasB, hasB ? corners.b() : BlockPos.ZERO,
                progress != null, progress == null ? 0 : progress[0], progress == null ? 0 : progress[1],
                player.level().dimension().location()));
    }

    /** 打开面板时把每一行都发一遍。 */    public static void sendFullState(ServerPlayer player) {
        boolean authorized = AdminToolConfig.isAuthorized(player.getUUID());
        for (QolSetting setting : QolSetting.values()) {
            sendState(player, setting, authorized);
        }
    }

    /**
     * 客户端请求全量状态（打开面板时发一次）。
     * <p>
     * 这里没有额外鉴权：全量状态里只有"这一项现在开着还是关着、值是多少"，本来就允许玩家看到自己的设置；
     * 未授权时每条回执里的 {@code authorized} 是 false，客户端据此把整行置灰。
     * 真正的拦截在 {@link #handleSettingChange} —— 看得见不等于改得动。
     */
    public static void handleRequestFullState(Player source) {
        if (source instanceof ServerPlayer player) {
            sendFullState(player);
        }
    }

    /** 发某一行的状态。范围和作用域取自服务端的表，客户端算不出来（上限含服务器配置）。 */    public static void sendState(ServerPlayer player, QolSetting setting, boolean authorized) {
        int value = valueOf(player, setting);
        NetworkHandler.sendToPlayer(player, QolStatePayload.of(
                setting, value > 0, value, setting.min(), QolSettings.maxOf(setting), authorized));
    }

    /** 按 UUID 读当前值；没写过就是该项默认值。服务器级项读的是 toml。 */
    public static int valueOf(ServerPlayer player, QolSetting setting) {
        if (setting.scope() == QolSetting.Scope.SERVER) {
            // 服务器级与玩家无关，每个人看到的都该是同一个值。
            return AdminToolConfig.qolServerValue(setting);
        }
        return QolStateSavedData.get(player.server).getValue(player.getUUID(), setting);
    }

    /**
     * 把某一行的新状态广播给所有在线玩家。
     * <p>
     * 只有服务器级项需要：它的值是全服共享的，一个人改了别人面板上的数字就旧了。
     * 个人级项不广播 —— 那是别人的隐私设置，而且广播一遍纯属浪费。
     */
    private static void broadcastState(QolSetting setting) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }
        for (ServerPlayer online : server.getPlayerList().getPlayers()) {
            sendState(online, setting, setting.scope() == QolSetting.Scope.SERVER
                    ? online.hasPermissions(SERVER_SETTING_PERMISSION_LEVEL)
                    : AdminToolConfig.isAuthorized(online.getUUID()));
        }
    }

    public static boolean isOn(ServerPlayer player, QolSetting setting) {
        return valueOf(player, setting) > 0;
    }

    /**
     * 夜视开关的共享入口：战术 HUD 的按钮与功能面板的那一行都走这里。
     * <p>
     * 两个入口必须操作同一份状态（存在 QolStateSavedData 里），否则会出现
     * "HUD 说开着、面板说关着"这种两套开关各记一份的局面。
     *
     * @return 切换后的状态
     */
    public static boolean toggleNightVision(ServerPlayer player) {
        boolean now = !isOn(player, QolSetting.NIGHT_VISION);
        QolStateSavedData.get(player.server).setValue(
                player.getUUID(), QolSetting.NIGHT_VISION, now ? 1 : 0);
        applyNightVision(player, now);
        return now;
    }

    // ===================== 周期校正 =====================

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        int tick = player.tickCount;
        // 经验飞涨每 tick 都要给，所以它不放在下面那些"每 20 tick 校正一次"的分支里。
        // 它没有需要维持的状态，就是"开着就一直给"，因此也不需要校正。
        applyXpRain(player);
        if (tick % RECONCILE_INTERVAL == 0) {
            applyWaterBreath(player);
            applyVillageHero(player);
            reconcileReach(player);
        }
        if (tick % NIGHT_VISION_INTERVAL == 0) {
            renewNightVision(player);
        }
        // 选区作业的作业由拥有者自己的 tick 驱动（见 ZoneExcavationJob#tick 的说明）。
        boolean wasRunning = ZoneExcavationJob.isRunning(player);
        if (wasRunning) {
            ZoneExcavationJob.tick(player);
            if (!ZoneExcavationJob.isRunning(player)) {
                // 刚好这一 tick 做完了：补一条状态让客户端的进度条归位。
                sendZoneStatus(player);
            } else if (tick % RECONCILE_INTERVAL == 0) {
                // 进度每 20 tick 回一次就够看了，每 tick 发纯属刷包。
                sendZoneStatus(player);
            }
        }
        // 装饰（201–203）每 tick 都要推进：背后三把剑、刺击、光环都是"看起来在动"的东西，
        // 20 tick 一档的话看起来就是一张一张地跳。
        tickCosmetics(player);
    }

    /** 玩家退出时清掉他的选区、作业与撤销记录。 */
    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ZONE_PENDING_CONFIRM.remove(player.getUUID());
            ZoneSelection.forget(player.getUUID());
            ZoneExcavationJob.forget(player.getUUID());
            forgetCosmetics(player);
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            applyAll(player);
            sendFullState(player);
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // 死亡会清掉药水效果与 transient 属性修饰符，重生后必须整体重挂一次。
            applyAll(player);
            sendFullState(player);
        }
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // 换维度同样会丢 transient 修饰符；药水效果会保留，但重挂一次也无害（幂等）。
            applyAll(player);
        }
    }

    // ===================== 各项生效逻辑 =====================

    private static void applyAll(ServerPlayer player) {
        for (QolSetting setting : QolSetting.values()) {
            applyTo(player, setting);
        }
    }

    private static void applyTo(ServerPlayer player, QolSetting setting) {
        switch (setting) {
            case NIGHT_VISION -> applyNightVision(player, isOn(player, setting));
            case WATER_BREATH -> applyWaterBreath(player);
            case VILLAGE_HERO -> applyVillageHero(player);
            case REACH -> reconcileReach(player);
            // 经验飞涨：没有"挂上去"的状态要维持，但开关一打开就立刻给一级 ——
            // 等下一个 tick 再给也不会错，只是玩家点完开关要愣一下才看到数字动。
            case XP_BOOST -> applyXpRain(player);
            // 下面这些没有"挂到玩家身上"的动作：
            //   83 凌空采掘 由 SwordMiningPenaltyHandler 现查现用；
            //   84 烈焰不侵 由 EntityFireImmuneMixin 现查现用；
            //   70 万载不磨 由 ItemStackDurabilityMixin 现查现用；
            //   94/95 只在交易发生的那一刻判定；
            //   77 与 127 的其余项是服务器级，值在 toml 里，消费方各自去读。
            // 保留这些分支而不是删掉，是为了让"新增一项时该不该写生效逻辑"这个问题
            // 在编译期就逼着人想一遍 —— 枚举加了项而这里没加，switch 会报错。
            case FLY_MINING, FIRE_IMMUNE, UNLIMITED_TRADES, VILLAGER_MASTER,
                 ANVIL_UNBREAKABLE, ANVIL_XP_COST, DURABILITY_PROTECTION,
                 MACHINE_LOOT, FOG_VISION, TREASURE_HUNTER_SERVER, TREASURE_HUNTER_RADIUS,
                 TREASURE_HUNTER, ZONE_EXCAVATE_SERVER, ZONE_MAX_SIDE, ZONE_DROP_POLICY,
                 ZONE_TICK_BUDGET, ZONE_BATCH_SIDE, ZONE_UNDO_MAX,
                 // 两行作业入口也没有"挂上去"的动作：它们没有值，生效点就是按下执行键那一刻。
                 ZONE_BREAK, ZONE_PLACE -> {
            }
            // 装饰（201 称号牌 / 202 浮游挂件 / 203 光环拖尾）同样没有"挂到玩家身上"的动作：
            // 它们由 tickCosmetics 每 tick 推进，开关一改下一 tick（50 ms）就出现或消失，
            // 不需要在这里补一次，也不该在这里另写一份 —— 两份逻辑迟早在某一边漏掉一处。
            case COSMETIC_TITLE, COSMETIC_ORBIT, COSMETIC_AURA -> {
            }
        }
    }

    /**
     * 经验飞涨（72）：开着的时候每一 tick 直接加一个经验等级。
     * <p>
     * <b>不走"给经验点"那条路。</b> {@code giveExperiencePoints} 要按等级曲线折算，
     * 而"每 tick 一级"是直接改等级计数 —— 用前者的话这个效果的强度会随等级升高而急剧衰减，
     * 表现成"一开始涨得飞快、后来几乎不动"，与"每 tick 一级"这句话对不上。
     * <p>
     * 也不做上限：这是管理员工具，想停就关掉开关。每秒 20 级，开着不管一分钟就是 1200 级。
     */
    private static void applyXpRain(ServerPlayer player) {
        if (!isOn(player, QolSetting.XP_BOOST)) {
            return;
        }
        player.giveExperienceLevels(1);
    }

    /**
     * 星瞳夜视（80）。
     * <p>
     * 刻意只在"没有效果"或"效果不长于我们自己那一档"时才写入：
     * 玩家自己喝的夜视药水通常比 300 tick 长，覆盖它等于把他的药水时长砍短，
     * 属于"抢玩家的药水"。反向也成立 —— 关闭时只摘不长于我们那一档的那条。
     */
    private static void applyNightVision(ServerPlayer player, boolean enabled) {
        MobEffectInstance current = player.getEffect(MobEffects.NIGHT_VISION);
        boolean looksLikeOurs = current == null || current.getDuration() <= NIGHT_VISION_DURATION;
        if (enabled) {
            if (looksLikeOurs) {
                player.addEffect(new MobEffectInstance(
                        MobEffects.NIGHT_VISION, NIGHT_VISION_DURATION, 0, false, false, false));
            }
        } else if (looksLikeOurs && current != null) {
            player.removeEffect(MobEffects.NIGHT_VISION);
        }
    }

    /** 续期：只在快到期时补，长于我们那一档的一律不碰。 */
    private static void renewNightVision(ServerPlayer player) {
        if (!isOn(player, QolSetting.NIGHT_VISION)) {
            return;
        }
        MobEffectInstance current = player.getEffect(MobEffects.NIGHT_VISION);
        if (current == null || current.getDuration() < NIGHT_VISION_RENEW_BELOW) {
            applyNightVision(player, true);
        }
    }

    /** 鲛人之息（85）：直接把氧气补满。刻意不用水肺药水 —— 那会在 HUD 上多一个图标，还要续期。 */
    private static void applyWaterBreath(ServerPlayer player) {
        if (!isOn(player, QolSetting.WATER_BREATH)) {
            return;
        }
        if (player.getAirSupply() < player.getMaxAirSupply()) {
            player.setAirSupply(player.getMaxAirSupply());
        }
    }

    /** 万民敬仰（88）：常驻村庄英雄，等级换算成 amplifier。 */
    private static void applyVillageHero(ServerPlayer player) {
        int level = isOn(player, QolSetting.VILLAGE_HERO) ? valueOf(player, QolSetting.VILLAGE_HERO) : 0;
        MobEffectInstance current = player.getEffect(MobEffects.HERO_OF_THE_VILLAGE);

        if (level <= 0) {
            // 只摘我们自己那一档：玩家刚打完袭击拿到的村庄英雄不能顺手清掉。
            if (current != null && current.getDuration() <= HERO_DURATION) {
                player.removeEffect(MobEffects.HERO_OF_THE_VILLAGE);
            }
            return;
        }

        int amplifier = Math.min(255, level * HERO_AMPLIFIER_PER_LEVEL - 1);
        // 玩家有更强的（比如袭击给的），不抢；只在没有、快过期、或我们该给得更高时才写。
        if (current != null && current.getAmplifier() > amplifier) {
            return;
        }
        if (current == null || current.getDuration() < HERO_RENEW_BELOW || current.getAmplifier() < amplifier) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.HERO_OF_THE_VILLAGE, HERO_DURATION, amplifier, false, false, false));
        }
    }

    /** 长臂善舞（89）：两个交互距离各加 N 格。用 transient 修饰符，不落盘，重挂靠生命周期事件。 */
    private static void reconcileReach(ServerPlayer player) {
        int blocks = isOn(player, QolSetting.REACH) ? valueOf(player, QolSetting.REACH) : 0;
        applyReachModifier(player, Attributes.BLOCK_INTERACTION_RANGE, REACH_BLOCK_MODIFIER, blocks);
        applyReachModifier(player, Attributes.ENTITY_INTERACTION_RANGE, REACH_ENTITY_MODIFIER, blocks);
    }

    private static void applyReachModifier(ServerPlayer player, Holder<Attribute> attribute,
                                           ResourceLocation modifierId, int amount) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) {
            return;
        }
        if (amount <= 0) {
            instance.removeModifier(modifierId);
            return;
        }
        AttributeModifier existing = instance.getModifier(modifierId);
        // 已经是目标值就别动：每 20 tick 摘一次再挂一次会让属性面板上的数字反复闪。
        if (existing != null && existing.amount() == amount) {
            return;
        }
        instance.removeModifier(modifierId);
        instance.addTransientModifier(new AttributeModifier(
                modifierId, amount, AttributeModifier.Operation.ADD_VALUE));
    }

    // ===================== 管理员装饰的实现 =====================

    /**
     * 装饰的总入口：每 tick 走一遍。
     * <p>
     * 三项共用一个入口是有意的 —— 它们的状态（实体句柄、反击进度）都在同一份 {@link Cosmetics} 里，
     * 拆成三条独立路径的话，"关掉挂件时顺手结束反击"这种跨项动作就没地方放了。
     */
    private static void tickCosmetics(ServerPlayer player) {
        // 先判授权再读设置：未授权的玩家是绝大多数，不该为这三项每 tick 去翻一次玩家存档。
        boolean authorized = AdminToolConfig.isAuthorized(player.getUUID());
        boolean titleOn = authorized && isOn(player, QolSetting.COSMETIC_TITLE);
        boolean orbitOn = authorized && isOn(player, QolSetting.COSMETIC_ORBIT);
        boolean auraOn = authorized && isOn(player, QolSetting.COSMETIC_AURA);
        boolean anyOn = titleOn || orbitOn || auraOn;

        Cosmetics state = COSMETICS.get(player.getUUID());
        if (state == null) {
            if (!anyOn) {
                return;
            }
            state = new Cosmetics();
            COSMETICS.put(player.getUUID(), state);
        }

        tickTitle(player, state, titleOn);
        tickOrbit(player, state, orbitOn);
        tickAura(player, state, auraOn);

        // 三项都关掉（或者被取消了授权）就不要再留这份状态：实体在上面各自已经收走了。
        if (!anyOn) {
            COSMETICS.remove(player.getUUID(), state);
        }
    }

    /** 掉线：装饰实体跟着一起收走。它们不落盘，所以这里必须清干净，否则会一直飘在原地。 */
    private static void forgetCosmetics(ServerPlayer player) {
        Cosmetics state = COSMETICS.remove(player.getUUID());
        if (state == null) {
            return;
        }
        clearBits(state);
        if (state.title != null) {
            state.title.discard();
            state.title = null;
        }
    }

    /**
     * 造一个隐形的小号盔甲架。
     * <p>
     * Small / Marker / Invisible 三个标记是先写进 NBT 再 {@code load} 进去的：1.21 里
     * {@code ArmorStand#setSmall} 与 {@code setMarker} 都是 private，而 NBT 读写是公开的
     * （/summon 走的就是这条路）。为两个布尔量写一个 mixin 不划算。
     * <p>
     * public 的 setter 一律放在 {@code load} 之后：load 会把 NBT 里的默认值一并写回实体，
     * 顺序反了就会被覆盖回去。
     */
    private static CosmeticBit spawnCosmeticBit(ServerLevel level, ItemStack held) {
        CosmeticBit bit = new CosmeticBit(level);
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Small", true);
        tag.putBoolean("Marker", true);
        tag.putBoolean("Invisible", true);
        tag.putBoolean("NoBasePlate", true);
        tag.putBoolean("ShowArms", false);
        bit.load(tag);
        bit.setInvulnerable(true);
        bit.setNoGravity(true);
        bit.setSilent(true);
        bit.setCustomNameVisible(false);
        if (!held.isEmpty()) {
            // 手里永远举着这一件。隐形/Marker 的盔甲架 hurt 会直接返回 false，所以它不会被抢走。
            bit.setItemInHand(InteractionHand.MAIN_HAND, held);
        }
        bit.addTag(COSMETIC_ENTITY_TAG);
        level.addFreshEntity(bit);
        return bit;
    }

    /**
     * 设剑的持握姿势。
     * <p>
     * 姿势走实体数据同步（{@code ArmorStand#setRightArmPose} → {@code DATA_RIGHT_ARM_POSE}），
     * 所以每 tick 设一遍也不会多发包：值没变就不会标脏。每 tick 重设的好处是自愈 ——
     * 反击结束后不需要专门找一个"该把剑翻回来了"的时机。
     */
    private static void setSwordPose(CosmeticBit bit, boolean stabbing) {
        bit.setRightArmPose(stabbing ? SWORD_POSE_STAB : SWORD_POSE_IDLE);
    }

    /**
     * 摆一个装饰实体。
     *
     * @param y       剑尖/牌子**看起来**应该在的位置，不是实体原点
     * @param yOffset 实体渲染中心相对原点的偏移：手持物要往下补，名牌本身不用
     */
    private static void placeCosmetic(CosmeticBit bit, double x, double y, double z, float yaw, double yOffset) {
        bit.setPos(x, y - yOffset, z);
        // 剑跟着玩家朝向转：不只是自转，整队剑是"跟着人身后"一起转向的。
        bit.setYRot(yaw);
        // 每 tick 都是直接摆位置，残留的速度会让客户端看到抖动，顺手清掉。
        bit.setDeltaMovement(Vec3.ZERO);
    }

    /** 当前维度；装饰实体只能加进服务端世界。 */
    private static ServerLevel serverLevelOf(ServerPlayer player) {
        return player.level() instanceof ServerLevel level ? level : null;
    }

    /**
     * 头顶称号牌（201）。
     * <p>
     * 用一块隐形盔甲架的名字来显示，而不是改玩家自己的 CustomName：
     * 玩家名牌渲染走的是 {@code Player#getDisplayName()}（profile 名 + 队伍前缀），CustomName 不参与，
     * 改了对别人来说什么都不会发生。盔甲架不是玩家，名字就照原样渲染，
     * 而且 {@code ArmorStandRenderer} 判名字时只看 CustomNameVisible、不看隐形，
     * 于是牌子浮在头顶、架子本身看不见。
     */
    private static void tickTitle(ServerPlayer player, Cosmetics state, boolean on) {
        if (!on) {
            if (state.title != null) {
                state.title.discard();
                state.title = null;
            }
            return;
        }
        ServerLevel level = serverLevelOf(player);
        if (level == null) {
            return;
        }
        // 换维度、被 /kill @e[type=armor_stand] 扫掉之后自愈：下一 tick 重新长一块出来。
        if (state.title == null || state.title.isRemoved() || state.title.level() != level) {
            if (state.title != null) {
                state.title.discard();
            }
            state.title = spawnCosmeticBit(level, ItemStack.EMPTY);
            state.title.setCustomName(titleText(player));
            state.title.setCustomNameVisible(true);
        }
        placeCosmetic(state.title,
                player.getX(), player.getY() + player.getBbHeight() + TITLE_HEIGHT, player.getZ(),
                player.getYRot(), 0.0D);
    }

    /**
     * 称号牌的内容：乱码前缀 + 真名。
     * <p>
     * 前缀用的是主题里那个替换字符，和剑上、HUD 上的记号是同一个 —— 这是"数据损坏"这条线自己的符号。
     * 名字取 profile 名而不是 {@code getName()}：玩家的显示名会受队伍前缀影响，摆两次会越接越长。
     */
    private static Component titleText(ServerPlayer player) {
        return Component.literal(KunJinKaoPalette.MOJIBAKE + KunJinKaoPalette.MOJIBAKE)
                .withStyle(ChatFormatting.LIGHT_PURPLE)
                .append(Component.literal(" ").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(player.getGameProfile().getName()).withStyle(ChatFormatting.WHITE));
    }

    /** 身后三把剑（202），顺带推进正在进行的反击。 */
    private static void tickOrbit(ServerPlayer player, Cosmetics state, boolean on) {
        ServerLevel level = serverLevelOf(player);
        if (!on || level == null) {
            clearBits(state);
            return;
        }
        // 自愈：换维度、实体被别的东西清掉之后，这里下一 tick 就能补回来。
        for (int i = state.bits.size() - 1; i >= 0; i--) {
            CosmeticBit bit = state.bits.get(i);
            if (bit.isRemoved() || bit.level() != level) {
                bit.discard();
                state.bits.remove(i);
            }
        }
        while (state.bits.size() < ORBIT_BIT_COUNT) {
            // 用本模组自己的剑。空手的一把是"未验证的合成成品"才有的状态，新 ItemStack 是正常剑，
            // 物品模型谓词会给出 -1（完整剑模型），不会渲染成编译阶段的小方块。
            state.bits.add(spawnCosmeticBit(level,
                    new ItemStack(SwordRegistry.KUN_JIN_KAO_SWORD.get())));
        }

        // 位置按玩家朝向算，而不是绕圈：朝向的水平分量是 (-sin, cos)，
        // 身后就是它的反向，身侧是 (cos, sin)。三把剑排成**一条直线**（与肩同向、离背等距），
        // 随朝向一起转 —— 转视角的时候它们是"在背后"，而不是"围着人转"。
        // 刻意不做成弧线：弧线上等角放三把，弦距就不等了，看起来就是"间隔不一致"。
        double yaw = Math.toRadians(player.getYRot());
        double behindX = Math.sin(yaw);
        double behindZ = -Math.cos(yaw);
        double sideX = Math.cos(yaw);
        double sideZ = Math.sin(yaw);
        int tick = player.tickCount;
        // 左右摆动的相位【不带】每把自己的偏移：三把同步摆，间距才是恒定的 0.55 格。
        // 早先每把各用一个相位，三把的横向偏移各走各的，间距就在 0.35–0.75 格之间来回变，
        // 看起来就是"三把剑的间隔不一样" —— 那不是错觉，是真的在变。
        // 想有生气就只把相位差放到上下浮动那一项上：高度差不会让人对间距产生误判。
        double sway = Math.sin(tick * ORBIT_SWAY_SPEED) * ORBIT_SWAY;
        for (int i = 0; i < state.bits.size(); i++) {
            // 反击中的那一把由 tickStrike 定位（索引 0）：这里不跳过的话两处会每 tick 互相拉扯。
            if (i == 0 && state.strikeTarget != null) {
                continue;
            }
            CosmeticBit bit = state.bits.get(i);
            setSwordPose(bit, false);
            // slot：-1 / 0 / +1，正好左、中、右。
            double slot = i - (ORBIT_BIT_COUNT - 1) / 2.0D;
            double bob = Math.sin(tick * ORBIT_SWAY_SPEED + i * 1.7D) * ORBIT_BOB;
            double targetX = player.getX() + behindX * ORBIT_BACK_DISTANCE
                    + sideX * (slot * ORBIT_SIDE_SPREAD + sway);
            double targetY = player.getY() + ORBIT_HEIGHT + bob;
            double targetZ = player.getZ() + behindZ * ORBIT_BACK_DISTANCE
                    + sideZ * (slot * ORBIT_SIDE_SPREAD + sway);
            if (i == 0 && state.returning) {
                // 飞回来：和追人一样用指数逼近，别从目标头顶"啪"一下跳回背后。
                Vec3 from = bit.position().add(0.0D, BIT_HAND_OFFSET, 0.0D);
                Vec3 to = new Vec3(targetX, targetY, targetZ);
                Vec3 next = from.add(to.subtract(from).scale(STRIKE_CHASE));
                if (next.distanceTo(to) < 0.25D) {
                    state.returning = false;
                }
                placeCosmetic(bit, next.x, next.y, next.z, player.getYRot(), BIT_HAND_OFFSET);
                continue;
            }
            placeCosmetic(bit, targetX, targetY, targetZ, player.getYRot(), BIT_HAND_OFFSET);
        }
        tickStrike(player, state, level);
    }

    /** 收走三把剑并结束反击。关掉挂件的路上也要走这一步 —— 否则剑没了，人还在挨打。 */
    private static void clearBits(Cosmetics state) {
        for (CosmeticBit bit : state.bits) {
            bit.discard();
        }
        state.bits.clear();
        state.endStrike();
        state.returning = false;
    }

    /**
     * 玩家打了管理员 → 起一次反击。
     * <p>
     * 这里只记下"追谁"，真正的追击与扣血交给 {@link #tickStrike} 按 tick 走：
     * 在事件里把整段反击做完会把主线程按住 5 秒，那就是一次卡服。
     * <p>
     * 只认"打进来的一方是玩家"：这条装饰的语义是"打管理员会还手"。
     * 让僵尸也能触发的话，管理员挂机一晚就会被自己的剑拖着满地图追怪。
     * 一次只追一个人：被几个人围住时先把第一个打回去，比三把剑各飞一边清楚得多。
     */
    @SubscribeEvent
    public void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer admin)) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof ServerPlayer attacker) || attacker == admin) {
            return;
        }
        if (!AdminToolConfig.isAuthorized(admin.getUUID()) || !isOn(admin, QolSetting.COSMETIC_ORBIT)) {
            return;
        }
        Cosmetics state = COSMETICS.get(admin.getUUID());
        if (state == null || state.bits.isEmpty() || state.strikeTarget != null) {
            // 挂件还没长出来，或者已经在追别人了 —— 不叠加。
            return;
        }
        state.strikeTarget = attacker.getUUID();
        state.strikeTicks = 0;
        state.nextStrikeTick = 0;
    }

    /** 反击：索引 0 的那把剑脱离身后，飞到目标头顶向下刺；打完飞回去。 */
    private static void tickStrike(ServerPlayer admin, Cosmetics state, ServerLevel level) {
        if (state.strikeTarget == null) {
            return;
        }
        if (state.bits.isEmpty()) {
            // 剑被清掉了就没得追了（下一 tick 会重新长出来）。
            finishStrike(state);
            return;
        }
        ServerPlayer victim = level.getServer().getPlayerList().getPlayer(state.strikeTarget);
        if (victim == null || !victim.isAlive() || victim.level() != level) {
            // 目标下线、死了、或者换维度了：这次反击到此为止，剑飞回身后。
            // 全服找不到人的"跨维度追击"没有意义，挨打的人已经不在管理员眼前了。
            finishStrike(state);
            return;
        }
        state.strikeTicks++;

        CosmeticBit bit = state.bits.get(0);
        // 追人的这一把翻成"剑尖朝下"。
        setSwordPose(bit, true);
        // 每一下之间先抬起来再压下去：只压着不动看起来是"贴着"，不是"刺"。
        // 判定用"离下一击还剩几 tick"，这样刺下去的时机和伤害落地是同一下。
        boolean thrusting = state.nextStrikeTick - state.strikeTicks <= STRIKE_THRUST_WINDOW;
        double hover = STRIKE_HOVER + (thrusting ? 0.0D : STRIKE_RETRACT);
        Vec3 head = new Vec3(victim.getX(), victim.getY() + victim.getBbHeight() + hover, victim.getZ());
        // 用"剑尖看起来的位置"算距离：实体原点和剑尖差着半格，混着算会让剑停在头顶上方半格。
        Vec3 from = bit.position().add(0.0D, STAB_HAND_OFFSET, 0.0D);
        Vec3 next = from.add(head.subtract(from).scale(STRIKE_CHASE));
        placeCosmetic(bit, next.x, next.y, next.z, victim.getYRot(), STAB_HAND_OFFSET);

        // 飞到了才开始打：还没到就扣血，看起来像隔着半条街在放技能。
        if (next.distanceTo(head) < STRIKE_REACH && state.strikeTicks >= state.nextStrikeTick) {
            state.nextStrikeTick = state.strikeTicks + STRIKE_DAMAGE_INTERVAL;
            strikeDamage(admin, victim, bit);
        }
        if (state.strikeTicks >= STRIKE_DURATION) {
            finishStrike(state);
        }
    }

    /** 收尾：清掉目标，并让剑走"飞回身后"的分支，不要从头顶瞬间跳回去。 */
    private static void finishStrike(Cosmetics state) {
        state.endStrike();
        state.returning = true;
    }

    /**
     * 反击的一击。
     * <p>
     * 伤害用 {@code indirectMagic}：魔法伤害不吃护甲，否则一身好装备的玩家会把这套反击
     * 减到看不见数字，那这条装饰就等于没做。附魔保护仍然有效 —— 那属于"他就是很硬"。
     * <p>
     * 低于 {@link #STRIKE_MIN_HEALTH} 就停手：这是惩罚，不是处决。
     * 命中反馈同时发给围观的人（sendParticles / playSound 都是服务端广播），
     * 不然只有被打的人自己知道发生了什么。
     */
    private static void strikeDamage(ServerPlayer admin, ServerPlayer victim, CosmeticBit bit) {
        if (victim.getHealth() <= STRIKE_MIN_HEALTH) {
            return;
        }
        ServerLevel level = serverLevelOf(victim);
        if (level == null) {
            return;
        }
        victim.hurt(admin.damageSources().indirectMagic(bit, admin), STRIKE_DAMAGE);
        level.sendParticles(ParticleTypes.CRIT,
                victim.getX(), victim.getY() + victim.getBbHeight() * 0.85D, victim.getZ(),
                6, 0.25D, 0.25D, 0.25D, 0.02D);
        level.sendParticles(AURA_PARTICLE,
                victim.getX(), victim.getY() + victim.getBbHeight() + STRIKE_HOVER, victim.getZ(),
                8, 0.18D, 0.12D, 0.18D, 0.0D);
        level.playSound(null, victim.getX(), victim.getY(), victim.getZ(),
                SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 0.55F, 1.6F);
    }

    /**
     * 光环与拖尾（203）。
     * <p>
     * 粒子一律由服务端发（{@code ServerLevel#sendParticles}）：客户端自己 spawn 的粒子只有本人看得见，
     * 而这条需求的前提正是"别的玩家也看得见"。
     */
    private static void tickAura(ServerPlayer player, Cosmetics state, boolean on) {
        ServerLevel level = serverLevelOf(player);
        if (!on || level == null) {
            state.trailFrom = null;
            return;
        }

        // 脚下光环：一圈点按 tick 转，看起来在转而不是闪。
        if (player.tickCount % AURA_INTERVAL == 0) {
            double phase = player.tickCount * AURA_SPIN;
            for (int i = 0; i < AURA_POINTS; i++) {
                double angle = phase + i * (Math.PI * 2.0D / AURA_POINTS);
                level.sendParticles(AURA_PARTICLE,
                        player.getX() + Math.cos(angle) * AURA_RADIUS,
                        player.getY() + AURA_HEIGHT,
                        player.getZ() + Math.sin(angle) * AURA_RADIUS,
                        1, 0.0D, 0.01D, 0.0D, 0.0D);
            }
        }

        // 拖尾按"走过的距离"补点，不按 tick：站着不动不该在原地堆一团粒子，
        // 而跑起来时两点之间要补满，不然速度快了尾迹会断成一节一节。
        Vec3 now = player.position();
        Vec3 from = state.trailFrom;
        state.trailFrom = now;
        if (from == null) {
            return;
        }
        double distance = now.distanceTo(from);
        if (distance < TRAIL_STEP) {
            return;
        }
        // 瞬移（回城、传送门、/tp 几千格）时按上限截断，否则一 tick 就在整条线上撒满粒子。
        int points = (int) Math.min(TRAIL_MAX_POINTS, Math.ceil(distance / TRAIL_STEP));
        for (int i = 1; i <= points; i++) {
            double t = i / (double) points;
            level.sendParticles(TRAIL_PARTICLE,
                    from.x + (now.x - from.x) * t,
                    from.y + (now.y - from.y) * t + 0.25D,
                    from.z + (now.z - from.z) * t,
                    1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }
}