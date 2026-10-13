/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.star;

import cyou.rimuru.shootingstardemo.mc1211.star.a;
import cyou.rimuru.shootingstardemo.mc1211.star.b;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;

public final class h {
    private final ServerLevel a;
    private final Long2ObjectMap<LevelChunk> b = new Long2ObjectOpenHashMap();
    private final Long2ObjectMap<boolean[]> c = new Long2ObjectOpenHashMap();
    private long d = Long.MIN_VALUE;
    private LevelChunk e;
    private boolean[] f;

    public h(ServerLevel serverLevel) {
        this.a = serverLevel;
    }

    public void a(int n2, int n3, int n4, BlockState blockState) {
        if (n3 < this.a.getMinBuildHeight() || n3 > this.a.getMaxBuildHeight() - 1) {
            return;
        }
        long l3 = ChunkPos.asLong((int)(n2 >> 4), (int)(n4 >> 4));
        if (l3 != this.d) {
            this.d = l3;
            if (!this.b.containsKey(l3)) {
                cyou.rimuru.shootingstardemo.mc1211.star.b.d(this.a, l3);
            }
            this.e = (LevelChunk)this.b.computeIfAbsent(l3, l2 -> this.a.getChunk(n2 >> 4, n4 >> 4));
            this.f = (boolean[])this.c.computeIfAbsent(l3, l2 -> new boolean[this.e.getSections().length]);
        }
        int n5 = this.e.getSectionIndex(n3);
        LevelChunkSection levelChunkSection = this.e.getSections()[n5];
        BlockState blockState2 = levelChunkSection.setBlockState(n2 & 0xF, n3 & 0xF, n4 & 0xF, blockState, false);
        if (blockState2 != blockState) {
            this.f[n5] = true;
            if (blockState2.hasBlockEntity()) {
                this.e.removeBlockEntity(new BlockPos(n2, n3, n4));
            }
        }
    }

    public BlockState a(int n2, int n3, int n4) {
        long l3 = ChunkPos.asLong((int)(n2 >> 4), (int)(n4 >> 4));
        LevelChunk levelChunk = l3 == this.d ? this.e : (LevelChunk)this.b.computeIfAbsent(l3, l2 -> this.a.getChunk(n2 >> 4, n4 >> 4));
        return levelChunk.getBlockState(new BlockPos(n2, n3, n4));
    }

    public boolean a() {
        return this.b.isEmpty();
    }

    public long[] b() {
        ThreadedLevelLightEngine threadedLevelLightEngine = this.a.getChunkSource().getLightEngine();
        LongArrayList longArrayList = new LongArrayList();
        for (Long2ObjectMap.Entry entry : this.b.long2ObjectEntrySet()) {
            LevelChunk levelChunk = (LevelChunk)entry.getValue();
            boolean[] blArray = (boolean[])this.c.get(entry.getLongKey());
            if (blArray == null) continue;
            LevelChunkSection[] levelChunkSectionArray = levelChunk.getSections();
            boolean bl = false;
            for (int i2 = 0; i2 < levelChunkSectionArray.length; ++i2) {
                if (!blArray[i2]) continue;
                bl = true;
                if (levelChunkSectionArray[i2].hasOnlyAir()) continue;
                threadedLevelLightEngine.updateSectionStatus(SectionPos.of((ChunkPos)levelChunk.getPos(), (int)this.a.getSectionYFromSectionIndex(i2)), false);
            }
            if (!bl) continue;
            cyou.rimuru.shootingstardemo.mc1211.star.a.a(this.a, levelChunk);
            longArrayList.add(entry.getLongKey());
            cyou.rimuru.shootingstardemo.mc1211.star.b.d(this.a, entry.getLongKey());
        }
        this.b.clear();
        this.c.clear();
        this.d = Long.MIN_VALUE;
        this.e = null;
        return longArrayList.toLongArray();
    }
}

