package dev.modmind.kunjinkao.client;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.awt.Color;
import java.util.List;

public class KunJinKaoTooltipColorHandler {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (!(event.getItemStack().getItem() instanceof KunJinKaoSwordItem)) return;
        if (KunJinKaoSwordItem.isDisguised(event.getItemStack())) return;
        float hue = (System.currentTimeMillis() % 3600L) / 3600.0F;
        int rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F) & 0xFFFFFF;
        TextColor color = TextColor.fromRgb(rgb);
        List<Component> tooltip = event.getToolTip();
        for (int i = 0; i < tooltip.size(); i++) {
            Component line = tooltip.get(i);
            if (containsInfinity(line)) tooltip.set(i, recolorInfinity(line, color));
        }
    }

    private static boolean containsInfinity(Component component) {
        if (component.getString().contains("\u221e")) return true;
        for (Component sibling : component.getSiblings()) {
            if (containsInfinity(sibling)) return true;
        }
        return false;
    }

    private static Component recolorInfinity(Component component, TextColor color) {
        String text = component.getString();
        if ("\u221e".equals(text)) return Component.literal("\u221e").withStyle(style -> style.withColor(color).withObfuscated(false));
        MutableComponent result = component.copy();
        List<Component> siblings = result.getSiblings();
        for (int i = 0; i < siblings.size(); i++) {
            Component sibling = siblings.get(i);
            if (containsInfinity(sibling)) siblings.set(i, recolorInfinity(sibling, color));
        }
        return result;
    }
}
