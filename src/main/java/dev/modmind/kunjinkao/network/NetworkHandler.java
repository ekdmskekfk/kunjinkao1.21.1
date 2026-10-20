package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class NetworkHandler {

    // 从 20 提到 21：本次把 18 个包合并成 3 个（判别 id 线格式），注册表与线格式都变了，
    // 必须让版本号跟着变，否则新旧端会在握手时被当成兼容，然后在解码时错位。
    // 22 提到 23：新增功能面板两条包（QolSettingPayload C2S / QolStatePayload S2C），注册表又变了。
    // 23 提到 24：新增选区挖掘两条包（ZoneSelectPayload C2S / ZoneStatusPayload S2C）。
    // 24 提到 25：框选改用木棍右键（服务端直接接交互事件），ZoneSelectPayload 随之删除，
    // 注册表少了一条 —— 注册表变了就要提版本号，否则持旧包的客户端会在握手之后才炸在解码上。
    // 25 提到 26：ZoneStatusPayload 改成定长线格式（修掉解码端字段错位 —— 那一版只要一设角点
    // 就会因为"读串了"被踢出存档）。线格式变了，同样必须提版本号。
    // 26 提到 27：选区作业的破坏与放置拆成两条动作 id（7 从"按模式执行"改为专指破坏，新增 9 = 放置）。
    // 包格式没变、注册表也没变，但动作 id 的含义变了：不提版本号的话，
    // 旧客户端按执行键会被新服务端当成"破坏" —— 不崩，但行为静默地不是他要的那个。
    // 27 提到 28：新增选区缩放的 C2S 包（ZoneAdjustPayload，木棍 + Shift + 滚轮）。
    // 注册表变了，线格式也新增了一条：旧客户端不认识这条注册，必须在握手时就被挡下。
    // 28 提到 29：新增建筑手杖预览两条包（WandPreviewRequestPayload C2S / WandPreviewResultPayload S2C），
    // 注册表又变了。**只要动注册表就必须再 +1**，否则新旧端会握手成功然后解码错位。
    // 29 提到 30：新增装饰状态包（CosmeticStatePayload S2C）。装饰从"服务端实体"改成"客户端渲染"
    // 之后，服务端只推状态；旧客户端没有这条注册，正是要在握手时被挡下的那种情况。
    // 30 提到 31：新增注入两条包（InjectionStartPayload C2S / InjectionStatePayload S2C）。
    // 注入是用来替代原「管理员剑」的机制，注册表变了就 +1。
    public static final String PROTOCOL_VERSION = "31";

    private NetworkHandler() {
    }

    /**
     * 21 条注册 = 原来 36 条 - 被合并掉的 18 条 + 新的 3 条：
     * <ul>
     *     <li>{@link SwordSettingPayload}（C2S）替掉 9 个剑设置包；</li>
     *     <li>{@link SimpleActionPayload}（C2S）替掉 5 个空载荷动作包；</li>
     *     <li>{@link HudStatePayload}（S2C）替掉 4 个 HUD 状态回执包。</li>
     * </ul>
     * 其余 18 个包按原样保留，注册方向（playToServer / playToClient）也一概不变。
     */
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar reg = event.registrar(PROTOCOL_VERSION);
        reg.playToServer(ToggleDisguisePayload.TYPE, ToggleDisguisePayload.STREAM_CODEC, ToggleDisguisePayload::handle);
        reg.playToClient(OverwriteEffectPayload.TYPE, OverwriteEffectPayload.STREAM_CODEC, OverwriteEffectPayload::handle);
        reg.playToServer(ToggleOverwritePayload.TYPE, ToggleOverwritePayload.STREAM_CODEC, ToggleOverwritePayload::handle);
        reg.playToServer(ToggleThemePayload.TYPE, ToggleThemePayload.STREAM_CODEC, ToggleThemePayload::handle);
        reg.playToServer(AcceleratorConfigPayload.TYPE, AcceleratorConfigPayload.STREAM_CODEC, AcceleratorConfigPayload::handle);
        reg.playToServer(AcceleratorShowRangePayload.TYPE, AcceleratorShowRangePayload.STREAM_CODEC, AcceleratorShowRangePayload::handle);
        reg.playToServer(AcceleratorFilterPayload.TYPE, AcceleratorFilterPayload.STREAM_CODEC, AcceleratorFilterPayload::handle);
        reg.playToServer(ToggleTacticalHudPayload.TYPE, ToggleTacticalHudPayload.STREAM_CODEC, ToggleTacticalHudPayload::handle);
        // 原 TacticalHudStatePayload / HudNightVisionStatePayload / HudTrueInvisibilityStatePayload /
        // HudMagnetStatePayload 四条注册合并为这一条，由 stateId 判别。
        reg.playToClient(HudStatePayload.TYPE, HudStatePayload.STREAM_CODEC, HudStatePayload::handle);
        // 原 RequestHudEntityListPayload / RequestExcludedPlayersPayload / ToggleHudNightVisionPayload /
        // ToggleHudTrueInvisibilityPayload / ToggleHudMagnetPayload 五条注册合并为这一条，由 actionId 判别。
        reg.playToServer(SimpleActionPayload.TYPE, SimpleActionPayload.STREAM_CODEC, SimpleActionPayload::handle);
        // 放置记录：收回（C2S）与整表同步（S2C）
        reg.playToServer(RemovePlacementPayload.TYPE, RemovePlacementPayload.STREAM_CODEC,
                RemovePlacementPayload::handle);
        reg.playToClient(PlacementSyncPayload.TYPE, PlacementSyncPayload.STREAM_CODEC,
                PlacementSyncPayload::handle);
        reg.playToClient(HudEntityListPayload.TYPE, HudEntityListPayload.STREAM_CODEC, HudEntityListPayload::handle);
        reg.playToServer(ManageHudEntityPayload.TYPE, ManageHudEntityPayload.STREAM_CODEC, ManageHudEntityPayload::handle);
        reg.playToClient(HudEntityActionResultPayload.TYPE, HudEntityActionResultPayload.STREAM_CODEC, HudEntityActionResultPayload::handle);
        reg.playToClient(HudTrueInvisibilityVisualPayload.TYPE, HudTrueInvisibilityVisualPayload.STREAM_CODEC, HudTrueInvisibilityVisualPayload::handle);
        reg.playToClient(HudShieldHitPayload.TYPE, HudShieldHitPayload.STREAM_CODEC, HudShieldHitPayload::handle);
        // 原 4 个 (hand, boolean) 开关包 + 5 个 (hand, int) 设置包共九条注册合并为这一条，由 settingId 判别。
        reg.playToServer(SwordSettingPayload.TYPE, SwordSettingPayload.STREAM_CODEC, SwordSettingPayload::handle);
        reg.playToServer(CollectBlockPayload.TYPE, CollectBlockPayload.STREAM_CODEC, CollectBlockPayload::handle);
        reg.playToClient(AdminEyeStatePayload.TYPE, AdminEyeStatePayload.STREAM_CODEC, AdminEyeStatePayload::handle);
        reg.playToClient(SwordDrawAnimationPayload.TYPE, SwordDrawAnimationPayload.STREAM_CODEC, SwordDrawAnimationPayload::handle);
        reg.playToServer(SubmitAdminPasswordPayload.TYPE, SubmitAdminPasswordPayload.STREAM_CODEC, SubmitAdminPasswordPayload::handle);
        reg.playToClient(AdminPasswordResultPayload.TYPE, AdminPasswordResultPayload.STREAM_CODEC, AdminPasswordResultPayload::handle);
        reg.playToClient(ExcludedPlayersPayload.TYPE, ExcludedPlayersPayload.STREAM_CODEC, ExcludedPlayersPayload::handle);
        reg.playToServer(ManageExcludedPlayerPayload.TYPE, ManageExcludedPlayerPayload.STREAM_CODEC, ManageExcludedPlayerPayload::handle);
        reg.playToClient(TimeAccelStatusPayload.TYPE, TimeAccelStatusPayload.STREAM_CODEC, TimeAccelStatusPayload::handle);
        // 功能面板（QoL）：改动请求（C2S）与逐行状态回执（S2C）。
        reg.playToServer(QolSettingPayload.TYPE, QolSettingPayload.STREAM_CODEC, QolSettingPayload::handle);
        reg.playToClient(QolStatePayload.TYPE, QolStatePayload.STREAM_CODEC, QolStatePayload::handle);
        // 选区作业（127）：选区与进度回执（S2C）。设角点不再走网络（木棍右键在服务端直接处理）。
        reg.playToClient(ZoneStatusPayload.TYPE, ZoneStatusPayload.STREAM_CODEC, ZoneStatusPayload::handle);
        // 选区作业（127）：缩放请求（C2S）。木棍 + Shift + 滚轮，服务端按玩家朝向决定轴向与正负。
        reg.playToServer(ZoneAdjustPayload.TYPE, ZoneAdjustPayload.STREAM_CODEC, ZoneAdjustPayload::handle);
        // 建筑手杖预览：客户端报"看着哪一格"，服务端算完把坐标发回来（内容由 WandJob 决定）。
        reg.playToServer(WandPreviewRequestPayload.TYPE, WandPreviewRequestPayload.STREAM_CODEC,
                WandPreviewRequestPayload::handle);
        reg.playToClient(WandPreviewResultPayload.TYPE, WandPreviewResultPayload.STREAM_CODEC,
                WandPreviewResultPayload::handle);
        // 装饰（201–203）：服务端只推"谁戴着什么、在打谁"，画面全部由客户端自己画。
        reg.playToClient(CosmeticStatePayload.TYPE, CosmeticStatePayload.STREAM_CODEC,
                CosmeticStatePayload::handle);
        // 注入（替代原「管理员剑」）：开始注入（C2S）+ 进度/中断/完成（S2C）。
        reg.playToServer(InjectionStartPayload.TYPE, InjectionStartPayload.STREAM_CODEC,
                InjectionStartPayload::handle);
        reg.playToClient(InjectionStatePayload.TYPE, InjectionStatePayload.STREAM_CODEC,
                InjectionStatePayload::handle);
    }

    public static void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    public static void sendToAll(CustomPacketPayload payload) {
        PacketDistributor.sendToAllPlayers(payload);
    }

    public static void sendToAllTracking(ServerPlayer entity, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayersTrackingEntity(entity, payload);
    }

    /** 按维度广播：加速场提示只需要发给同维度的客户端。 */
    public static void sendToPlayersInDimension(net.minecraft.server.level.ServerLevel level, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayersInDimension(level, payload);
    }

    /**
     * 原生 {@code FriendlyByteBuf#readEnum} 的安全版本：线格式完全一致（VarInt 序号），
     * 只在序号越界时回落到 {@code values()[0]}。
     * <p>
     * 原生 readEnum 是直接拿序号当下标取数组元素，改造过的客户端发一个越界序号，
     * 就会在 netty 解码线程抛 ArrayIndexOutOfBoundsException 把连接打断；这里做越界保护，
     * 合法输入（序号在范围内）的解析结果与原生完全一致。
     */
    public static <E extends Enum<E>> E readEnumSafe(FriendlyByteBuf buf, Class<E> type) {
        int ordinal = buf.readVarInt();
        E[] values = type.getEnumConstants();
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Enum type has no constants: " + type.getName());
        }
        return ordinal >= 0 && ordinal < values.length ? values[ordinal] : values[0];
    }

}