package dev.modmind.kunjinkao;

import dev.modmind.kunjinkao.block.AcceleratorBlock;
import dev.modmind.kunjinkao.block.entity.AcceleratorBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * 加速方块相关注册：方块、方块实体以及方块物品。
 */
public class AcceleratorRegistry {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(BuiltInRegistries.BLOCK, KunJinKaoEntry.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, KunJinKaoEntry.MOD_ID);

    public static final DeferredHolder<Block, AcceleratorBlock> ACCELERATOR_BLOCK =
            BLOCKS.register("accelerator", AcceleratorBlock::new);

    public static final DeferredItem<Item> ACCELERATOR_ITEM =
            SwordRegistry.ITEMS.register("accelerator",
                    () -> new BlockItem(ACCELERATOR_BLOCK.get(), new Item.Properties()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AcceleratorBlockEntity>> ACCELERATOR_BE =
            BLOCK_ENTITY_TYPES.register("accelerator",
                    () -> BlockEntityType.Builder.of(AcceleratorBlockEntity::new, ACCELERATOR_BLOCK.get()).build(null));

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
    }
}