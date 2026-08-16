package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.KunJinKaoTheme;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleThemePayload(InteractionHand hand, int theme) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ToggleThemePayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "toggle_theme"));

    public static final StreamCodec<FriendlyByteBuf, ToggleThemePayload> STREAM_CODEC =
            StreamCodec.of(ToggleThemePayload::write, ToggleThemePayload::read);

    public static void write(FriendlyByteBuf buf, ToggleThemePayload payload) {
        buf.writeEnum(payload.hand);
        buf.writeInt(payload.theme);
    }

    public static ToggleThemePayload read(FriendlyByteBuf buf) {
        return new ToggleThemePayload(buf.readEnum(InteractionHand.class), buf.readInt());
    }

    public static void handle(ToggleThemePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                ItemStack stack = player.getItemInHand(payload.hand);
                if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
                    return;
                }
                KunJinKaoSwordItem.setTheme(stack, payload.theme);
                player.displayClientMessage(
                        Component.literal("§d异象主题：" + KunJinKaoTheme.displayName(KunJinKaoSwordItem.getTheme(stack))),
                        true
                );
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
