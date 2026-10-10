package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.config.AdminToolConfig;
import dev.modmind.kunjinkao.config.QolStateSavedData;
import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.network.QolStatePayload;
import dev.modmind.kunjinkao.network.ZoneStatusPayload;
import dev.modmind.kunjinkao.qol.QolSetting;
import dev.modmind.kunjinkao.qol.QolSettings;
import dev.modmind.kunjinkao.world.ZoneExcavationJob;
import dev.modmind.kunjinkao.world.ZoneSelection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    /** 待确认的执行请求：玩家 UUID -> 第一次按键的时刻。 */
    private static final Map<UUID, Long> ZONE_PENDING_CONFIRM = new ConcurrentHashMap<>();

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
        if (setting == QolSetting.ZONE_MODE) {
            // 模式是"下一次按下执行键要干什么"，改完立刻回一句当前是什么：
            // 选项名写在面板那一行上，可玩家按执行键时通常已经离开面板了。
            player.displayClientMessage(Component.translatable(
                    clamped == ZoneExcavationJob.MODE_PLACE
                            ? "message.kunjinkao.zone_mode_place"
                            : "message.kunjinkao.zone_mode_break"), true);
        }
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
    public static void handleZoneExecute(Player source) {
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
        Long firstPress = ZONE_PENDING_CONFIRM.get(player.getUUID());
        if (firstPress == null || now - firstPress > ZONE_CONFIRM_WINDOW_MS) {
            ZONE_PENDING_CONFIRM.put(player.getUUID(), now);
            int sizeX = Math.abs(corners.a().getX() - corners.b().getX()) + 1;
            int sizeY = Math.abs(corners.a().getY() - corners.b().getY()) + 1;
            int sizeZ = Math.abs(corners.a().getZ() - corners.b().getZ()) + 1;
            boolean placing = valueOf(player, QolSetting.ZONE_MODE) == ZoneExcavationJob.MODE_PLACE;
            player.displayClientMessage(Component.translatable(
                    placing ? "message.kunjinkao.zone_confirm_place" : "message.kunjinkao.zone_confirm",
                    sizeX, sizeY, sizeZ, sizeX * sizeY * sizeZ), false);
            return;
        }
        ZONE_PENDING_CONFIRM.remove(player.getUUID());
        if (ZoneExcavationJob.start(player, corners.a(), corners.b())) {
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
    }

    /** 玩家退出时清掉他的选区、作业与撤销记录。 */
    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ZONE_PENDING_CONFIRM.remove(player.getUUID());
            ZoneSelection.forget(player.getUUID());
            ZoneExcavationJob.forget(player.getUUID());
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
            // 下面这些没有"挂到玩家身上"的动作：
            //   83 凌空采掘 由 SwordMiningPenaltyHandler 现查现用；
            //   84 烈焰不侵 由 EntityFireImmuneMixin 现查现用；
            //   70 万载不磨 由 ItemStackDurabilityMixin 现查现用；
            //   94/95 只在交易发生的那一刻判定；
            //   72/77 是服务器级，值在 toml 里，消费方各自去读。
            // 保留这些分支而不是删掉，是为了让"新增一项时该不该写生效逻辑"这个问题
            // 在编译期就逼着人想一遍 —— 枚举加了项而这里没加，switch 会报错。
            case FLY_MINING, FIRE_IMMUNE, UNLIMITED_TRADES, VILLAGER_MASTER,
                 XP_BOOST, ANVIL_UNBREAKABLE, ANVIL_XP_COST, DURABILITY_PROTECTION,
                 MACHINE_LOOT, FOG_VISION, TREASURE_HUNTER_SERVER, TREASURE_HUNTER_RADIUS,
                 TREASURE_HUNTER, ZONE_EXCAVATE_SERVER, ZONE_MAX_SIDE, ZONE_DROP_POLICY,
                 ZONE_TICK_BUDGET, ZONE_BATCH_SIDE, ZONE_UNDO_MAX,
                 // 模式（ZONE_MODE）也没有"挂上去"的动作：它是作业开工那一刻读一次的选择，
                 // 换句话说它的生效点就是 ZoneExcavationJob.start。
                 ZONE_MODE -> {
            }
        }
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
}