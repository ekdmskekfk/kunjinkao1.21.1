package dev.modmind.kunjinkao;

import dev.modmind.kunjinkao.block.WorldGateBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 主世界之门方块相关注册：方块与方块物品。
 * 物品沿用 {@link SwordRegistry#ITEMS}，方块单独一个 DeferredRegister。
 */
public class WorldGateRegistry {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(BuiltInRegistries.BLOCK, KunJinKaoEntry.MOD_ID);

    public static final DeferredHolder<Block, WorldGateBlock> WORLD_GATE_BLOCK =
            BLOCKS.register("world_gate", WorldGateBlock::new);

    public static final DeferredItem<Item> WORLD_GATE_ITEM =
            SwordRegistry.ITEMS.register("world_gate",
                    () -> new BlockItem(WORLD_GATE_BLOCK.get(), new Item.Properties()));

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}