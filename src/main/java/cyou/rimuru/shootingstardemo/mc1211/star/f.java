/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.star;

import io.netty.buffer.Unpooled;
import net.minecraft.core.IdMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.PalettedContainer;

public final class f {
    private f() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static PalettedContainer<BlockState> a(PalettedContainer<BlockState> palettedContainer) {
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer((int)palettedContainer.getSerializedSize()));
        try {
            palettedContainer.write(friendlyByteBuf);
            PalettedContainer palettedContainer2 = new PalettedContainer((IdMap)Block.BLOCK_STATE_REGISTRY, (Object)Blocks.AIR.defaultBlockState(), PalettedContainer.Strategy.SECTION_STATES);
            palettedContainer2.read(friendlyByteBuf);
            PalettedContainer palettedContainer3 = palettedContainer2;
            return palettedContainer3;
        }
        finally {
            friendlyByteBuf.release();
        }
    }
}

