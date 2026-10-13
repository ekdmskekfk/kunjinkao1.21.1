/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.registry;

import cyou.rimuru.shootingstardemo.mc1211.registry.h;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class b {
    public static final Block hull = new h(BlockBehaviour.Properties.of().strength(150.0f, 3600.0f).mapColor(MapColor.COLOR_BLACK).sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops().isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false).pushReaction(PushReaction.BLOCK));
    public static final Block b = new cyou.rimuru.shootingstardemo.mc1211.registry.a(BlockBehaviour.Properties.of().strength(50.0f, 1200.0f).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.NETHERITE_BLOCK).lightLevel(blockState -> 12).emissiveRendering((blockState, blockGetter, blockPos) -> true).requiresCorrectToolForDrops().isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false).pushReaction(PushReaction.BLOCK));
    public static final Block c = new cyou.rimuru.shootingstardemo.mc1211.registry.a(BlockBehaviour.Properties.of().strength(50.0f, 1200.0f).mapColor(MapColor.COLOR_PURPLE).sound(SoundType.AMETHYST).lightLevel(blockState -> 15).emissiveRendering((blockState, blockGetter, blockPos) -> true).requiresCorrectToolForDrops().isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false).pushReaction(PushReaction.BLOCK));
    public static final Block d = new Block(BlockBehaviour.Properties.of().strength(50.0f, 1200.0f).mapColor(MapColor.COLOR_PURPLE).sound(SoundType.AMETHYST).lightLevel(blockState -> 10).emissiveRendering((blockState, blockGetter, blockPos) -> true).requiresCorrectToolForDrops().isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false).pushReaction(PushReaction.BLOCK));
    public static final Block e = new a(BlockBehaviour.Properties.of().strength(50.0f, 1200.0f).mapColor(blockState -> cyou.rimuru.shootingstardemo.mc1211.registry.b.a.b[(Integer)blockState.getValue((Property)cyou.rimuru.shootingstardemo.mc1211.registry.b.a.a)]).sound(SoundType.AMETHYST).lightLevel(blockState -> 12).emissiveRendering((blockState, blockGetter, blockPos) -> true).requiresCorrectToolForDrops().isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false).pushReaction(PushReaction.BLOCK));

    private b() {
    }

    public static void a() {
    }

    public static void a(RegisterEvent registerEvent) {
        registerEvent.register(Registries.BLOCK, registerHelper -> {
            registerHelper.register(ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"gungnir_hull"), hull);
            registerHelper.register(ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"gungnir_seam"), b);
            registerHelper.register(ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"star_core"), c);
            registerHelper.register(ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"star_trace"), d);
            registerHelper.register(ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"lattice_edge"), e);
        });
    }

    public static final class a
    extends Block {
        public static final IntegerProperty a = IntegerProperty.create((String)"hue", (int)0, (int)6);
        static final MapColor[] b = new MapColor[]{MapColor.COLOR_RED, MapColor.COLOR_ORANGE, MapColor.COLOR_YELLOW, MapColor.COLOR_LIGHT_GREEN, MapColor.COLOR_LIGHT_BLUE, MapColor.COLOR_BLUE, MapColor.COLOR_PURPLE};

        public a(BlockBehaviour.Properties properties) {
            super(properties);
            this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)a, (Comparable)Integer.valueOf(0)));
        }

        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(new Property[]{a});
        }

        public static BlockState a(int n2) {
            return (BlockState)e.defaultBlockState().setValue((Property)a, (Comparable)Integer.valueOf(Math.floorMod(n2, 7)));
        }
    }
}

