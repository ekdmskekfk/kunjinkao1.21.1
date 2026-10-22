package dev.modmind.kunjinkao;

import dev.modmind.kunjinkao.recipe.AdminSwordRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

public class SwordRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(KunJinKaoEntry.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KunJinKaoEntry.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, KunJinKaoEntry.MOD_ID);

    public static final DeferredItem<Item> KUN_JIN_KAO_SWORD = ITEMS.register("kun_jin_kao",
        () -> new KunJinKaoSwordItem(Tiers.DIAMOND, new Item.Properties())
    );

    /**
     * 拟态刷：注入物品开启刷子功能时换成的物品。
     *
     * <p>刻意<b>不放进创造模式物品栏</b> —— 它只是"拟态"的产物，正常途径不该拿得到。
     * 继承原版 {@code BrushItem}，所以刷取行为与动画完全是原版的。</p>
     */
    public static final DeferredItem<Item> MIMIC_BRUSH = ITEMS.register("mimic_brush",
        () -> new dev.modmind.kunjinkao.inject.MimicBrushItem(new Item.Properties())
    );

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AdminSwordRecipe>> ADMIN_SWORD_RECIPE =
            RECIPE_SERIALIZERS.register("admin_sword", () -> AdminSwordRecipe.SERIALIZER);


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> KUN_JIN_KAO_TAB = CREATIVE_MODE_TABS.register("kun_jin_kao_tab",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.kunjinkao.kun_jin_kao"))
            .icon(() -> new ItemStack(KUN_JIN_KAO_SWORD.get()))
            .displayItems((params, output) -> {
                output.accept(KUN_JIN_KAO_SWORD.get());
                output.accept(AcceleratorRegistry.ACCELERATOR_ITEM.get());
                output.accept(WorldGateRegistry.WORLD_GATE_ITEM.get());
                // 锟斤拷之手的刷怪蛋（实体在 KunJinKaoEntities 注册；引用写在 lambda 里，
                // 不参与本类的静态初始化，所以没有初始化顺序问题）。
                output.accept(dev.modmind.kunjinkao.entity.KunJinKaoEntities.HAND_SPAWN_EGG.get());
            })
            .build()
    );

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
    }
}