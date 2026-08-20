package dev.modmind.kunjinkao.recipe;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class KunJinKaoRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, KunJinKaoEntry.MOD_ID);

    public static final Holder<RecipeSerializer<?>> KUN_JIN_KAO_HIDDEN =
        RECIPE_SERIALIZERS.register("kun_jin_kao_hidden",
            () -> new SimpleCraftingRecipeSerializer<>(KunJinKaoHiddenRecipe::new));

    private KunJinKaoRecipes() {
    }

    public static void register(IEventBus modEventBus) {
        RECIPE_SERIALIZERS.register(modEventBus);
    }
}
