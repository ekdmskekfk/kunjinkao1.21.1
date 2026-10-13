/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.magic;

import cyou.rimuru.shootingstardemo.mc1211.magic.d;
import cyou.rimuru.shootingstardemo.mc1211.magic.e;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class b
extends Item {
    public static BiConsumer<Player, b> a = (player, b2) -> {};
    public static Function<d, String> b = d2 -> "";
    public static Supplier<String> c = () -> "?";
    public final e d;

    public b(e e2, Item.Properties properties) {
        super(properties);
        this.d = e2;
    }

    public static b a(Player player) {
        b b2;
        if (player == null) {
            return null;
        }
        Item item = player.getMainHandItem().getItem();
        if (item instanceof b) {
            b b3 = (b)item;
            return b3;
        }
        item = player.getOffhandItem().getItem();
        return item instanceof b ? (b2 = (b)item) : null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean a(Player player, e e2) {
        if (player == null) return false;
        Item item = player.getMainHandItem().getItem();
        if (item instanceof b) {
            b b2 = (b)item;
            if (b2.d == e2) return true;
        }
        if (!((item = player.getOffhandItem().getItem()) instanceof b)) return false;
        b b3 = (b)item;
        if (b3.d != e2) return false;
        return true;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (level.isClientSide()) {
            a.accept(player, this);
        }
        return InteractionResultHolder.success(player.getItemInHand(interactionHand));
    }

    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        Consumer<Component> consumer = list::add;
        consumer.accept((Component)Component.translatable((String)(this.getDescriptionId() + ".lore")).withStyle(ChatFormatting.WHITE));
        consumer.accept((Component)Component.translatable((String)(this.getDescriptionId() + ".hint"), (Object[])new Object[]{c.get()}).withStyle(ChatFormatting.GRAY));
    }
}

