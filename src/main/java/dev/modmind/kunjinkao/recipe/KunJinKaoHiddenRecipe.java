package dev.modmind.kunjinkao.recipe;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.SwordRegistry;
import dev.modmind.kunjinkao.event.KunJinKaoAdminCraftHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Hidden administrator recipe: eight sticks surrounding a cobblestone block.
 * CustomRecipe is special, so it is excluded from the recipe book and normal JEI display.
 */
public final class KunJinKaoHiddenRecipe extends CustomRecipe {

    public KunJinKaoHiddenRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.width() != 3 || input.height() != 3 || input.ingredientCount() != 9) {
            return false;
        }

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                ItemStack stack = input.getItem(x, y);
                if (x == 1 && y == 1) {
                    if (!stack.is(Items.COBBLESTONE)) {
                        return false;
                    }
                } else if (!stack.is(Items.STICK)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack result = new ItemStack(SwordRegistry.KUN_JIN_KAO_SWORD.value());
        CompoundTag tag = KunJinKaoSwordItem.getModTag(result);
        tag.putBoolean(KunJinKaoAdminCraftHandler.PENDING_ADMIN_CRAFT_KEY, true);
        KunJinKaoSwordItem.setModTag(result, tag);
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int index = 0; index < input.size(); index++) {
            remaining.set(index, input.getItem(index).copyWithCount(1));
        }
        return remaining;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return KunJinKaoRecipes.KUN_JIN_KAO_HIDDEN.value();
    }
}
