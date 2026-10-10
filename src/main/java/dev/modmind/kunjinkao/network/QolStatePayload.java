package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.clientbridge.ClientHooks;
import dev.modmind.kunjinkao.qol.QolSetting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 功能面板一行的状态回执（S2C）。
 * <p>
 * 线格式：{@code writeVarInt(settingId) | writeBoolean(enabled) | writeVarInt(value)
 * | writeVarInt(min) | writeVarInt(max) | writeBoolean(serverScope) | writeBoolean(authorized)}。
 * <p>
 * 一条包只描述<b>一行</b>：打开面板时服务端按行连发若干条，等同于"全量同步"；
 * 之后每次改动只回一条。没有做"全量包 + 增量包"两种类型，是因为行数只有十几，
 * 连发的开销可以忽略，而少一种包就少一处能对不上的地方。
 * <p>
 * 特意把 {@code enabled} 与 {@code value} 分成两个字段：开关类两者等价（enabled == value != 0），
 * 但后面要加的"总闸 + 参数"型功能（选区挖掘、寻宝大师）里它们是两件事。
 * <p>
 * {@code min}/{@code max} 由服务端下发而不是客户端自己写死：上限里含服务器配置，
 * 客户端算不出来。{@code authorized} 为 false 时客户端把整行置灰，
 * 但仍然会显示当前值 —— 让玩家看得见"这项存在、但你没权限"。
 */
public record QolStatePayload(int settingId, boolean enabled, int value, int min, int max,
                              boolean serverScope, boolean authorized) implements CustomPacketPayload {

    public static final Type<QolStatePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "qol_state"));

    public static final StreamCodec<FriendlyByteBuf, QolStatePayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> {
                        buf.writeVarInt(p.settingId);
                        buf.writeBoolean(p.enabled);
                        buf.writeVarInt(p.value);
                        buf.writeVarInt(p.min);
                        buf.writeVarInt(p.max);
                        buf.writeBoolean(p.serverScope);
                        buf.writeBoolean(p.authorized);
                    },
                    buf -> new QolStatePayload(buf.readVarInt(), buf.readBoolean(), buf.readVarInt(),
                            buf.readVarInt(), buf.readVarInt(), buf.readBoolean(), buf.readBoolean()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    /** 组装一条回执；范围与作用域都取自服务端自己的表，不接受调用方随手给。 */
    public static QolStatePayload of(QolSetting setting, boolean enabled, int value,
                                     int min, int max, boolean authorized) {
        return new QolStatePayload(setting.id(), enabled, value, min, max,
                setting.scope() == QolSetting.Scope.SERVER, authorized);
    }

    public static void handle(QolStatePayload payload, IPayloadContext context) {
        // 与 HudStatePayload 同一个入口：先过 common 侧网关照发，
        // 保证服务端不会因为加载本类而碰到 net.minecraft.client。
        ClientHooks.handleClientPayload(payload, context);
    }
}