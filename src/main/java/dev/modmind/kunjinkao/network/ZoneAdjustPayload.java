package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.event.KunJinKaoQolHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 选区作业（127）的缩放请求（C2S）：木棍 + Shift + 滚轮。
 * <p>
 * <b>包里只有一个滚轮格数，没有轴、也没有方向。</b> 轴向与正负由服务端拿
 * {@code player.getLookAngle()} 现算（见 {@code KunJinKaoQolHandler#handleZoneAdjust}）：
 * 客户端只负责说「我往上滚了几格」。这样即使客户端被改过，它能谎报的也只有格数，
 * 而格数在两端都会被夹回 ±16；「朝玩家面朝的方向扩」这条规则始终由服务端说了算。
 * <p>
 * 线格式：<b>定长，一个字段</b> —— {@code writeVarInt(delta)}。
 * 编码端写一个 VarInt，解码端读一个 VarInt，逐字段一一对应，没有
 * 「下一个字段写不写」这种分支，因此不会出现 {@link ZoneStatusPayload} 里那种
 * 构造实参从左到右求值把流读串的情形。
 * <p>
 * 正数 = 滚轮向上 = 扩大，负数 = 滚轮向下 = 缩小，绝对值是这一次要动的格数。
 */
public record ZoneAdjustPayload(int delta) implements CustomPacketPayload {

    /**
     * 一次请求最多调整多少格。
     * <p>
     * 正常滚轮一格就是 1（触控板的小数滚动由客户端取整后至少算一格），
     * 这个上限存在的意义只是兜住改过的客户端 —— 一次滚轮就让选区涨 1000 格没有任何对应操作。
     */
    public static final int MAX_STEPS = 16;

    public static final Type<ZoneAdjustPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "zone_adjust"));

    /**
     * 夹取写在构造器里，于是编码与解码两条路都会经过它：客户端保证发出去的值合法，
     * 服务端保证收进来的值也合法。不去指望对端守规矩，是这条包唯一需要防的东西。
     */
    public ZoneAdjustPayload {
        delta = Math.max(-MAX_STEPS, Math.min(MAX_STEPS, delta));
    }

    public static final StreamCodec<FriendlyByteBuf, ZoneAdjustPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> buf.writeVarInt(p.delta),
                    buf -> new ZoneAdjustPayload(buf.readVarInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ZoneAdjustPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> KunJinKaoQolHandler.handleZoneAdjust(context.player(), payload.delta()));
    }
}
