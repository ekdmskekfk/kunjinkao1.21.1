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
    public static final String PROTOCOL_VERSION = "22";

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
        reg.playToClient(HudEntityListPayload.TYPE, HudEntityListPayload.STREAM_CODEC, HudEntityListPayload::handle);
        reg.playToServer(ManageHudEntityPayload.TYPE, ManageHudEntityPayload.STREAM_CODEC, ManageHudEntityPayload::handle);
        reg.playToClient(HudEntityActionResultPayload.TYPE, HudEntityActionResultPayload.STREAM_CODEC, HudEntityActionResultPayload::handle);
        reg.playToClient(HudTrueInvisibilityVisualPayload.TYPE, HudTrueInvisibilityVisualPayload.STREAM_CODEC, HudTrueInvisibilityVisualPayload::handle);
        reg.playToClient(HudShieldHitPayload.TYPE, HudShieldHitPayload.STREAM_CODEC, HudShieldHitPayload::handle);
        // 原 4 个 (hand, boolean) 开关包 + 5 个 (hand, int) 设置包共九条注册合并为这一条，由 settingId 判别。
        reg.playToServer(SwordSettingPayload.TYPE, SwordSettingPayload.STREAM_CODEC, SwordSettingPayload::handle);
        reg.playToClient(AdminEyeStatePayload.TYPE, AdminEyeStatePayload.STREAM_CODEC, AdminEyeStatePayload::handle);
        reg.playToClient(SwordDrawAnimationPayload.TYPE, SwordDrawAnimationPayload.STREAM_CODEC, SwordDrawAnimationPayload::handle);
        reg.playToServer(SubmitAdminPasswordPayload.TYPE, SubmitAdminPasswordPayload.STREAM_CODEC, SubmitAdminPasswordPayload::handle);
        reg.playToClient(AdminPasswordResultPayload.TYPE, AdminPasswordResultPayload.STREAM_CODEC, AdminPasswordResultPayload::handle);
        reg.playToClient(ExcludedPlayersPayload.TYPE, ExcludedPlayersPayload.STREAM_CODEC, ExcludedPlayersPayload::handle);
        reg.playToServer(ManageExcludedPlayerPayload.TYPE, ManageExcludedPlayerPayload.STREAM_CODEC, ManageExcludedPlayerPayload::handle);
        reg.playToClient(TimeAccelStatusPayload.TYPE, TimeAccelStatusPayload.STREAM_CODEC, TimeAccelStatusPayload::handle);
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