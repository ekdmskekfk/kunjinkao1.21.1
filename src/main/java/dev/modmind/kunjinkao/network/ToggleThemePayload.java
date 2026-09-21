package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.config.AdminToolConfig;
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
                // 与 withSword 一致的授权闸门：改剑的设置（覆写开关 / 主题 / 伪装）只对通过密码验证的玩家开放。
                // 复核发现这三个包走的是各自的 handle，并不经过 withSword，所以必须单独补上。
                if (!AdminToolConfig.isAuthorized(player.getUUID())) {
                    player.displayClientMessage(Component.translatable("message.kunjinkao.admin_required"), true);
                    return;
                }
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