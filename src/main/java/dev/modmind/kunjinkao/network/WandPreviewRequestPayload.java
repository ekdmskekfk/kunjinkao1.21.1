package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.wand.WandPreviewServer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 预览请求 C2S：客户端把"我正看着哪一格、看的哪一面"发上来。
 * <p>
 * 线格式：{@code writeVarInt(seq) | writeBlockHitResult(hit)}。
 * <p>
 * 两个刻意的设计（都跟参考实现不同，理由写在 {@link WandPreviewServer} 上）：
 * <ul>
 *   <li>带 {@code seq}：结果可能因为网络乱序而晚到，客户端靠它丢掉过期的结果；</li>
 *   <li><b>不</b>随包发物品栈：服务端用自己那份主手物品算，客户端无从伪造。</li>
 * </ul>
 */
public record WandPreviewRequestPayload(int seq, BlockHitResult hit) implements CustomPacketPayload {

    public static final Type<WandPreviewRequestPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "wand_preview_request"));

    public static final StreamCodec<FriendlyByteBuf, WandPreviewRequestPayload> STREAM_CODEC =
            StreamCodec.of((buf, payload) -> {
                        buf.writeVarInt(payload.seq);
                        buf.writeBlockHitResult(payload.hit);
                    },
                    buf -> new WandPreviewRequestPayload(buf.readVarInt(), buf.readBlockHitResult()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(WandPreviewRequestPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }
            if (!WandPreviewServer.allow(player)) {
                return;
            }
            WandPreviewServer.serve(player, payload.seq(), payload.hit());
        });
    }
}