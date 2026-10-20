package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.event.InjectionHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * C2S：玩家按下了注入键，请求开始一次注入。
 *
 * <p>只有 {@code hand} —— 服务端一律以"服务端自己看到的这只手"为准，
 * 不信任客户端报上来的物品内容（客户端报什么都不影响结果）。
 */
public record InjectionStartPayload(InteractionHand hand) implements CustomPacketPayload {

    public static final Type<InjectionStartPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "injection_start"));

    public static final StreamCodec<FriendlyByteBuf, InjectionStartPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> buf.writeEnum(p.hand),
                    buf -> new InjectionStartPayload(
                            NetworkHandler.readEnumSafe(buf, InteractionHand.class)));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * 处理入口：解析出手是哪只，然后全部交给服务端的 {@code InjectionHandler}。
     *
     * <p>这里刻意不做任何业务校验（授权、黑名单、冷却都在那边），
     * 因为这个方法会在网络线程被调到，必须立刻 {@code enqueueWork} 切回主线程。</p>
     */
    public static void handle(InjectionStartPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                InjectionHandler.start(player, payload.hand());
            }
        });
    }
}
