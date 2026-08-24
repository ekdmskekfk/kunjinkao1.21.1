package dev.modmind.kunjinkao.tactical.common;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** Shared server-side definition of a real, non-disguised administrator sword. */
public final class SwordPresence {
    private SwordPresence() { }

    public static boolean hasRealSwordInInventory(Player player) {
        for (ItemStack stack : player.getInventory().items) if (isRealSword(stack)) return true;
        for (ItemStack stack : player.getInventory().offhand) if (isRealSword(stack)) return true;
        return false;
    }

    public static boolean isRealSword(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof KunJinKaoSwordItem && !KunJinKaoSwordItem.isDisguised(stack);
    }
}
