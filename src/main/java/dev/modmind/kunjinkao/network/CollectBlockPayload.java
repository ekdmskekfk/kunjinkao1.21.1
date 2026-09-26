package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * C2S：请求把"玩家正看着的那个方块"直接收进物品栏。
 * <p>
 * 刻意不带坐标 —— 服务端用自己的射线检测（{@code player.pick}）确定目标，
 * 客户端只负责说"我按了 R"。这样客户端无法指定任意坐标去挖区块外的方块。
 */
public record CollectBlockPayload() implements CustomPacketPayload {

    public static final CollectBlockPayload INSTANCE = new CollectBlockPayload();

    public static final Type<CollectBlockPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "collect_block"));

    public static final StreamCodec<FriendlyByteBuf, CollectBlockPayload> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CollectBlockPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> dev.modmind.kunjinkao.event.SwordAutoPickupHandler.collectTargetedBlock(context.player()));
    }
}