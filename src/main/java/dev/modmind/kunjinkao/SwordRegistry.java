package dev.modmind.kunjinkao;

import dev.modmind.kunjinkao.entity.DiamondProjectile;
import dev.modmind.kunjinkao.recipe.AdminSwordRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;

public class SwordRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(KunJinKaoEntry.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KunJinKaoEntry.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, KunJinKaoEntry.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, KunJinKaoEntry.MOD_ID);

    public static final DeferredItem<Item> KUN_JIN_KAO_SWORD = ITEMS.register("kun_jin_kao",
        () -> new KunJinKaoSwordItem(Tiers.DIAMOND, new Item.Properties())
    );

    /** 仅供客户端拔剑编译动画使用，不加入创造模式物品栏。 */
    public static final List<DeferredItem<Item>> KUN_JIN_KAO_COMPILE_STAGES = List.of(
            ITEMS.register("kun_jin_kao_compile_0", () -> new Item(new Item.Properties())),
            ITEMS.register("kun_jin_kao_compile_1", () -> new Item(new Item.Properties())),
            ITEMS.register("kun_jin_kao_compile_2", () -> new Item(new Item.Properties())),
            ITEMS.register("kun_jin_kao_compile_3", () -> new Item(new Item.Properties())),
            ITEMS.register("kun_jin_kao_compile_4", () -> new Item(new Item.Properties())),
            ITEMS.register("kun_jin_kao_compile_5", () -> new Item(new Item.Properties())),
            ITEMS.register("kun_jin_kao_compile_6", () -> new Item(new Item.Properties())),
            ITEMS.register("kun_jin_kao_compile_7", () -> new Item(new Item.Properties()))
    );

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AdminSwordRecipe>> ADMIN_SWORD_RECIPE =
            RECIPE_SERIALIZERS.register("admin_sword", () -> AdminSwordRecipe.SERIALIZER);

    public static final DeferredHolder<EntityType<?>, EntityType<DiamondProjectile>> DIAMOND_PROJECTILE =
        ENTITY_TYPES.register("diamond_projectile", () -> EntityType.Builder.<DiamondProjectile>of(DiamondProjectile::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build("diamond_projectile"));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> KUN_JIN_KAO_TAB = CREATIVE_MODE_TABS.register("kun_jin_kao_tab",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.kunjinkao.kun_jin_kao"))
            .icon(() -> new ItemStack(KUN_JIN_KAO_SWORD.get()))
            .displayItems((params, output) -> {
                output.accept(KUN_JIN_KAO_SWORD.get());
                output.accept(AcceleratorRegistry.ACCELERATOR_ITEM.get());
                output.accept(WorldGateRegistry.WORLD_GATE_ITEM.get());
            })
            .build()
    );

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
    }
}