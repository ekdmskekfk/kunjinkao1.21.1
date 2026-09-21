package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleOverwritePayload(InteractionHand hand) implements CustomPacketPayload {

    public static final Type<ToggleOverwritePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "toggle_overwrite"));

    public static final StreamCodec<FriendlyByteBuf, ToggleOverwritePayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> buf.writeEnum(p.hand),
                    buf -> new ToggleOverwritePayload(NetworkHandler.readEnumSafe(buf, InteractionHand.class)));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ToggleOverwritePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                ItemStack stack = player.getItemInHand(payload.hand);
                if (stack.getItem() instanceof KunJinKaoSwordItem) {
                    KunJinKaoSwordItem.toggleOverwrite(stack);
                }
            }
        });
    }
}