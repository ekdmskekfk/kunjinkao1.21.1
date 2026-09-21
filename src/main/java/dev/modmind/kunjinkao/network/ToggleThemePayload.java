package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.KunJinKaoTheme;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleThemePayload(InteractionHand hand, int theme) implements CustomPacketPayload {

    public static final Type<ToggleThemePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "toggle_theme"));

    public static final StreamCodec<FriendlyByteBuf, ToggleThemePayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeEnum(p.hand); buf.writeInt(p.theme); },
                    buf -> new ToggleThemePayload(NetworkHandler.readEnumSafe(buf, InteractionHand.class), buf.readInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ToggleThemePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                ItemStack stack = player.getItemInHand(payload.hand);
                if (stack.getItem() instanceof KunJinKaoSwordItem) {
                    KunJinKaoSwordItem.setTheme(stack, payload.theme);
                    player.displayClientMessage(
                            Component.literal("§d异象主题：" + KunJinKaoTheme.displayName(KunJinKaoSwordItem.getTheme(stack))),
                            true);
                }
            }
        });
    }
}