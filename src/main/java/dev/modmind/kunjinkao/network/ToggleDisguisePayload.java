package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import dev.modmind.kunjinkao.KunJinKaoSwordItem;

public record ToggleDisguisePayload(InteractionHand hand) implements CustomPacketPayload {

    public static final Type<ToggleDisguisePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "toggle_disguise"));

    public static final StreamCodec<FriendlyByteBuf, ToggleDisguisePayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> buf.writeEnum(p.hand),
                    buf -> new ToggleDisguisePayload(buf.readEnum(InteractionHand.class)));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ToggleDisguisePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                ItemStack stack = player.getItemInHand(payload.hand);
                if (stack.getItem() instanceof KunJinKaoSwordItem sword) {
                    KunJinKaoSwordItem.toggleDisguise(stack);
                }
            }
        });
    }
}