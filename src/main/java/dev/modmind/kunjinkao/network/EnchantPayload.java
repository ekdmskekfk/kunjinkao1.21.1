package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.config.AdminToolConfig;
import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 「附魔工坊」的执行包（C2S）：把某个附魔按指定等级，做成附魔书或直接写到手持物上。
 *
 * <p><b>为什么执行放在服务端</b>：附魔是物品数据的一部分，客户端改了不落盘、也不可靠；
 * 而且工坊本身是管理员功能，必须在这边校验授权（{@link AdminToolConfig#isAuthorized}），
 * 不能只靠客户端把按钮藏起来。</p>
 *
 * <p><b>为什么只传附魔 id 而不是整个附魔对象</b>：1.21.1 的附魔是数据驱动的
 * {@link Holder}，两端注册表不同步时传对象等于传一个可能对不上的引用；
 * 传 {@code namespace:path} 再由服务端自己的注册表解析，是最稳的做法。</p>
 */
public record EnchantPayload(String enchantId, int level, int mode) implements CustomPacketPayload {

    /** 做成附魔书。 */
    public static final int MODE_BOOK = 0;
    /** 直接写到手持的剑/注入物上。 */
    public static final int MODE_ITEM = 1;

    public static final Type<EnchantPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "enchant"));

    public static final StreamCodec<FriendlyByteBuf, EnchantPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, EnchantPayload::enchantId,
            ByteBufCodecs.VAR_INT, EnchantPayload::level,
            ByteBufCodecs.VAR_INT, EnchantPayload::mode,
            EnchantPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(EnchantPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> apply(context.player(), payload));
    }

    private static void apply(Player player, EnchantPayload payload) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        if (!AdminToolConfig.isAuthorized(serverPlayer.getUUID())) {
            serverPlayer.displayClientMessage(Component.translatable("message.kunjinkao.admin_required"), true);
            return;
        }
        ResourceLocation id = ResourceLocation.tryParse(payload.enchantId());
        if (id == null) {
            return;
        }
        Holder<Enchantment> holder = serverPlayer.getServer().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolder(id)
                .orElse(null);
        if (holder == null) {
            serverPlayer.displayClientMessage(Component.translatable("message.kunjinkao.enchant_unknown"), true);
            return;
        }
        // 原版上限是 255（短整型存进 NBT 也安全），超出的直接夹住而不是拒绝，
        // 免得滑条拖到 300 时玩家以为功能坏了。
        int level = Math.max(1, Math.min(255, payload.level()));

        if (payload.mode() == MODE_BOOK) {
            ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
            ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
            mutable.set(holder, level);
            book.set(DataComponents.STORED_ENCHANTMENTS, mutable.toImmutable());
            if (!serverPlayer.getInventory().add(book)) {
                serverPlayer.drop(book, false);
            }
        } else {
            ItemStack stack = serverPlayer.getMainHandItem();
            if (!KunJinKaoSwordItem.isSwordLike(stack)) {
                serverPlayer.displayClientMessage(Component.translatable("message.kunjinkao.enchant_need_sword"), true);
                return;
            }
            stack.enchant(holder, level);
            // 改的是服务端这份栈，必须主动同步，否则客户端要等下一次容器刷新才看得到。
            serverPlayer.inventoryMenu.broadcastChanges();
        }

        Component name = holder.value().description().copy()
                .append(Component.literal(" " + level));
        serverPlayer.displayClientMessage(
                Component.translatable("message.kunjinkao.enchant_applied", name), true);
    }
}
