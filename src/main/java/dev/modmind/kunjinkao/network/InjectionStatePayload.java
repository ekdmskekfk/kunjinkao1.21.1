package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.clientbridge.ClientHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * S2C：注入进度状态。客户端只负责画进度条，判定全部在服务端。
 *
 * <p>定长线格式（int + int + int）——这个模组历史上被"变长解码端字段错位"
 * 坑过一次（见 {@link NetworkHandler#PROTOCOL_VERSION} 注释里 25→26 那条），
 * 所以新包一律用定长、不用可选的 VarInt 列表。</p>
 */
public record InjectionStatePayload(int state, int durationTicks, int hand) implements CustomPacketPayload {

    /** 开始计时：客户端按 durationTicks 自行插值画进度条。 */
    public static final int START = 0;
    /** 中断：移动 / 受伤 / 换手 / 物品变了。客户端进度条立刻消失。 */
    public static final int ABORT = 1;
    /** 完成：物品已被服务端写入注入标记。 */
    public static final int DONE = 2;

    public static final Type<InjectionStatePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "injection_state"));

    public static final StreamCodec<FriendlyByteBuf, InjectionStatePayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> {
                        buf.writeInt(p.state);
                        buf.writeInt(p.durationTicks);
                        buf.writeInt(p.hand);
                    },
                    buf -> new InjectionStatePayload(buf.readInt(), buf.readInt(), buf.readInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /** 客户端只做表现：进度条开始 / 消失 / 完成提示。 */
    public static void handle(InjectionStatePayload payload, IPayloadContext context) {
        ClientHooks.handleClientPayload(payload, context);
    }
}
