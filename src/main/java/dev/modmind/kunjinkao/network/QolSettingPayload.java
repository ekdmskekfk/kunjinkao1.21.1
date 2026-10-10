package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.event.KunJinKaoQolHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 功能面板的改动请求（C2S）。
 * <p>
 * 线格式：{@code writeVarInt(settingId) | writeVarInt(value)}。
 * 开关类的 value 统一是 0/1，数值类是真的数值 —— 与 {@code SwordSettingPayload} 同一套做法，
 * 解码路径只有一条。
 * <p>
 * <b>这里不做任何判定。</b> 客户端说自己是谁、值是多少都不可信，
 * 授权、范围、前置依赖全部在 {@link KunJinKaoQolHandler#handleSettingChange} 里重新算一遍。
 * 判别 id 的含义见 {@code qol/QolSetting}，只能往后追加，不能改动已有取值。
 */
public record QolSettingPayload(int settingId, int value) implements CustomPacketPayload {

    public static final Type<QolSettingPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "qol_setting"));

    public static final StreamCodec<FriendlyByteBuf, QolSettingPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeVarInt(p.settingId); buf.writeVarInt(p.value); },
                    buf -> new QolSettingPayload(buf.readVarInt(), buf.readVarInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(QolSettingPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> KunJinKaoQolHandler.handleSettingChange(
                context.player(), payload.settingId(), payload.value()));
    }
}