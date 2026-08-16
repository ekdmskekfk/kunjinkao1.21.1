package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record ToggleOverwritePayload(InteractionHand hand) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ToggleOverwritePayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "toggle_overwrite"));

    public static final StreamCodec<FriendlyByteBuf, ToggleOverwritePayload> STREAM_CODEC =
            StreamCodec.of(ToggleOverwritePayload::write, ToggleOverwritePayload::read);

    private static final Logger LOGGER = LoggerFactory.getLogger(ToggleOverwritePayload.class);

    public static void write(FriendlyByteBuf buf, ToggleOverwritePayload payload) {
        buf.writeEnum(payload.hand);
    }

    public static ToggleOverwritePayload read(FriendlyByteBuf buf) {
        return new ToggleOverwritePayload(buf.readEnum(InteractionHand.class));
    }

    public static void handle(ToggleOverwritePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                ItemStack stack = player.getItemInHand(payload.hand);
                if (stack.getItem() instanceof KunJinKaoSwordItem) {
                    KunJinKaoSwordItem.toggleOverwrite(stack);
                    boolean enabled = KunJinKaoSwordItem.isOverwriteEnabled(stack);
                    boolean disguised = KunJinKaoSwordItem.isDisguised(stack);
                    int theme = KunJinKaoSwordItem.getTheme(stack);
                    LOGGER.info("[KunJinKao] Server toggle overwrite -> enabled={}, disguised={}, theme={}",
                            enabled, disguised, theme);
                    player.displayClientMessage(
                            Component.literal("§e覆写·断未 §7| §f覆写流程：" + (enabled ? "§a开启" : "§c关闭")),
                            true
                    );
                } else {
                    LOGGER.warn("[KunJinKao] Server received toggle_overwrite but hand item is not the sword: {}", stack);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
