/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.registry;

import cyou.rimuru.shootingstardemo.mc1211.registry.b;
import cyou.rimuru.shootingstardemo.mc1211.registry.f;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class e {
    public static final Item a = new cyou.rimuru.shootingstardemo.mc1211.magic.b(cyou.rimuru.shootingstardemo.mc1211.registry.f.a, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
    public static final Item b = new BlockItem(cyou.rimuru.shootingstardemo.mc1211.registry.b.c, new Item.Properties());
    public static final Item c = new BlockItem(cyou.rimuru.shootingstardemo.mc1211.registry.b.hull, new Item.Properties());
    public static final Item d = new BlockItem(cyou.rimuru.shootingstardemo.mc1211.registry.b.b, new Item.Properties());
    public static final Item e = new BlockItem(cyou.rimuru.shootingstardemo.mc1211.registry.b.d, new Item.Properties());
    public static final Item f = new BlockItem(cyou.rimuru.shootingstardemo.mc1211.registry.b.e, new Item.Properties());

    private e() {
    }

    public static void a(RegisterEvent registerEvent) {
        registerEvent.register(Registries.ITEM, registerHelper -> {
            registerHelper.register(ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"stellar_remote"), a);
            registerHelper.register(ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"star_core"), b);
            registerHelper.register(ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"gungnir_hull"), c);
            registerHelper.register(ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"gungnir_seam"), d);
            registerHelper.register(ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"star_trace"), e);
            registerHelper.register(ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"lattice_edge"), f);
        });
        registerEvent.register(Registries.CREATIVE_MODE_TAB, registerHelper -> registerHelper.register(ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"shooting_star_demo"), CreativeModeTab.builder().title((Component)Component.translatable((String)"itemGroup.shooting_star_demo")).icon(() -> new ItemStack((ItemLike)a)).displayItems((itemDisplayParameters, output) -> {
            output.accept((ItemLike)a);
            output.accept((ItemLike)b);
            output.accept((ItemLike)c);
            output.accept((ItemLike)d);
            output.accept((ItemLike)e);
            output.accept((ItemLike)f);
        }).build()));
    }

    public static void a(BuildCreativeModeTabContentsEvent buildCreativeModeTabContentsEvent) {
        if (buildCreativeModeTabContentsEvent.getTabKey() == CreativeModeTabs.COMBAT) {
            buildCreativeModeTabContentsEvent.accept((ItemLike)a);
        }
    }
}

