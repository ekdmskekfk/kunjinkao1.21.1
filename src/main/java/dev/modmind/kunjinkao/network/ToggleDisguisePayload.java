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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record ToggleDisguisePayload(InteractionHand hand) implements CustomPacketPayload {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToggleDisguisePayload.class);

    public static final CustomPacketPayload.Type<ToggleDisguisePayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "toggle_disguise"));

    public static final StreamCodec<FriendlyByteBuf, ToggleDisguisePayload> STREAM_CODEC =
            StreamCodec.of(ToggleDisguisePayload::write, ToggleDisguisePayload::read);

    public static void write(FriendlyByteBuf buf, ToggleDisguisePayload payload) {
        buf.writeEnum(payload.hand);
    }

    public static ToggleDisguisePayload read(FriendlyByteBuf buf) {
        return new ToggleDisguisePayload(buf.readEnum(InteractionHand.class));
    }

    public static void handle(ToggleDisguisePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                ItemStack stack = player.getItemInHand(payload.hand);
                if (stack.getItem() instanceof KunJinKaoSwordItem) {
                    float before = stack.getOrDefault(
                            net.minecraft.core.component.DataComponents.CUSTOM_MODEL_DATA,
                            net.minecraft.world.item.component.CustomModelData.DEFAULT).value();
                    KunJinKaoSwordItem.toggleDisguise(stack);
                    float after = stack.getOrDefault(
                            net.minecraft.core.component.DataComponents.CUSTOM_MODEL_DATA,
                            net.minecraft.world.item.component.CustomModelData.DEFAULT).value();
                    LOGGER.info("[KunJinKao] Server toggle disguise hand={} before={} after={} item={}",
                            payload.hand, before, after, stack.getItem());
                } else {
                    LOGGER.warn("[KunJinKao] Server received toggle_disguise but hand item is not the sword: {}",
                            stack.getItem());
                }
            } else {
                LOGGER.warn("[KunJinKao] Server received toggle_disguise but context.player() is not ServerPlayer: {}",
                        context.player());
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
