package dev.modmind.kunjinkao.recipe;

import dev.modmind.kunjinkao.SwordRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

public final class AdminSwordRecipe extends CustomRecipe {
    public static final String PENDING_CRAFT_TAG = "kunjinkao:pending_admin_sword_craft";
    public static final SimpleCraftingRecipeSerializer<AdminSwordRecipe> SERIALIZER =
            new SimpleCraftingRecipeSerializer<>(AdminSwordRecipe::new);

    public AdminSwordRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput crafting, Level level) {
        return isPattern(crafting);
    }

    public static boolean isPattern(CraftingInput crafting) {
        if (crafting.width() != 3 || crafting.height() != 3) {
            return false;
        }
        for (int slot = 0; slot < crafting.size(); slot++) {
            ItemStack stack = crafting.getItem(slot);
            if (slot == 4) {
                if (!stack.is(Items.COBBLESTONE)) {
                    return false;
                }
            } else if (!stack.is(Items.STICK)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput crafting, HolderLookup.Provider registries) {
        ItemStack result = new ItemStack(SwordRegistry.KUN_JIN_KAO_SWORD.get());
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(PENDING_CRAFT_TAG, true);
        result.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width == 3 && height == 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public static boolean isPendingAdminSword(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        return stack.is(SwordRegistry.KUN_JIN_KAO_SWORD.get())
                && customData != null && customData.copyTag().getBoolean(PENDING_CRAFT_TAG);
    }

    public static void clearPendingCraftTag(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return;
        }
        CompoundTag tag = customData.copyTag();
        tag.remove(PENDING_CRAFT_TAG);
        if (tag.isEmpty()) {
            stack.remove(DataComponents.CUSTOM_DATA);
        } else {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
    }
}
