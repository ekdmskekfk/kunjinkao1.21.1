package dev.modmind.kunjinkao;

import dev.modmind.kunjinkao.block.TimeAcceleratorBlock;
import dev.modmind.kunjinkao.block.TimeAcceleratorBlockEntity;
import dev.modmind.kunjinkao.entity.DiamondProjectile;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SwordRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, KunJinKaoEntry.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KunJinKaoEntry.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, KunJinKaoEntry.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, KunJinKaoEntry.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, KunJinKaoEntry.MOD_ID);

    public static final Holder<? extends Item> KUN_JIN_KAO_SWORD = ITEMS.register("kun_jin_kao",
        () -> new KunJinKaoSwordItem(Tiers.DIAMOND, 3, -2.4F,
            new Item.Properties().component(DataComponents.UNBREAKABLE, new Unbreakable(false)))
    );

    // ===== 时间加速器方块 =====

    public static final Holder<Block> TIME_ACCELERATOR = BLOCKS.register("time_accelerator",
        () -> new TimeAcceleratorBlock(BlockBehaviour.Properties.of()
                .strength(3.0F)
                .requiresCorrectToolForDrops())
    );

    public static final Holder<BlockEntityType<?>> TIME_ACCELERATOR_BE =
        BLOCK_ENTITIES.register("time_accelerator",
            () -> BlockEntityType.Builder.of(TimeAcceleratorBlockEntity::new, TIME_ACCELERATOR.value()).build(null));

    public static final Holder<Item> TIME_ACCELERATOR_ITEM = ITEMS.register("time_accelerator",
        () -> new BlockItem(TIME_ACCELERATOR.value(), new Item.Properties())
    );

    public static final Holder<? extends EntityType<?>> DIAMOND_PROJECTILE =
        ENTITY_TYPES.register("diamond_projectile", () -> EntityType.Builder.<DiamondProjectile>of(DiamondProjectile::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build("diamond_projectile"));

    public static final Holder<CreativeModeTab> KUN_JIN_KAO_TAB = CREATIVE_MODE_TABS.register("kun_jin_kao_tab",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.kunjinkao.kun_jin_kao"))
            .icon(() -> new ItemStack(KUN_JIN_KAO_SWORD.value()))
            .displayItems((params, output) -> {
                output.accept(KUN_JIN_KAO_SWORD.value());
                output.accept(TIME_ACCELERATOR_ITEM.value());
            })
            .build()
    );

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
    }
}
