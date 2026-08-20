package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.SwordRegistry;
import dev.modmind.kunjinkao.config.AdminToolConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public final class KunJinKaoAdminCraftHandler {

    public static final String PENDING_ADMIN_CRAFT_KEY = "PendingAdminCraft";

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide() || !isHiddenRecipeInput(event.getInventory())) {
            return;
        }

        if (AdminToolConfig.isAuthorized(player.getUUID())) {
            clearPendingCraftMarker(event.getCrafting());
            forEachPendingCraftResult(player, KunJinKaoAdminCraftHandler::clearPendingCraftMarker);
        } else {
            event.getCrafting().setCount(0);
            forEachPendingCraftResult(player, stack -> stack.setCount(0));
        }
    }

    private static boolean isHiddenRecipeInput(Container input) {
        boolean hasAmethyst = false;
        boolean hasDiamondSword = false;
        boolean hasEchoShard = false;

        for (int index = 0; index < input.getContainerSize(); index++) {
            ItemStack stack = input.getItem(index);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(Items.AMETHYST_SHARD) && !hasAmethyst) {
                hasAmethyst = true;
            } else if (stack.is(Items.DIAMOND_SWORD) && !hasDiamondSword) {
                hasDiamondSword = true;
            } else if (stack.is(Items.ECHO_SHARD) && !hasEchoShard) {
                hasEchoShard = true;
            } else {
                return false;
            }
        }

        return hasAmethyst && hasDiamondSword && hasEchoShard;
    }

    private static void forEachPendingCraftResult(Player player, java.util.function.Consumer<ItemStack> action) {
        for (int index = 0; index < player.getInventory().getContainerSize(); index++) {
            ItemStack stack = player.getInventory().getItem(index);
            if (isPendingCraftResult(stack)) {
                action.accept(stack);
            }
        }

        ItemStack carried = player.containerMenu.getCarried();
        if (isPendingCraftResult(carried)) {
            action.accept(carried);
        }
    }

    private static boolean isPendingCraftResult(ItemStack stack) {
        return stack.is(SwordRegistry.KUN_JIN_KAO_SWORD.value())
            && KunJinKaoSwordItem.getModTag(stack).getBoolean(PENDING_ADMIN_CRAFT_KEY);
    }

    private static void clearPendingCraftMarker(ItemStack stack) {
        if (!isPendingCraftResult(stack)) {
            return;
        }

        CompoundTag tag = KunJinKaoSwordItem.getModTag(stack);
        tag.remove(PENDING_ADMIN_CRAFT_KEY);
        KunJinKaoSwordItem.setModTag(stack, tag);
    }
}
