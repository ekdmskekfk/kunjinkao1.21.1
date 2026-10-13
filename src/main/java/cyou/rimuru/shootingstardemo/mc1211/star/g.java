/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.star;

import cyou.rimuru.shootingstardemo.mc1211.star.b;
import cyou.rimuru.shootingstardemo.mc1211.star.f;
import cyou.rimuru.shootingstardemo.mc1211.star.m;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.IdMap;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;

final class g
implements m.c,
m.d {
    private static final BlockState a = Blocks.AIR.defaultBlockState();
    private final int b;
    private final int c;
    private final Long2ObjectOpenHashMap<LevelChunkSection[]> d = new Long2ObjectOpenHashMap();
    private final Long2ObjectOpenHashMap<LevelChunkSection[]> e = new Long2ObjectOpenHashMap();
    private final Long2ObjectOpenHashMap<boolean[]> f = new Long2ObjectOpenHashMap();
    private final Long2ObjectOpenHashMap<boolean[]> g = new Long2ObjectOpenHashMap();
    private final int h;
    private final int i;
    private final int j;
    private final int k;
    private final int l;
    private final int m;
    private int n;
    private final Long2IntOpenHashMap o = new Long2IntOpenHashMap();
    private boolean p;
    private long q = Long.MIN_VALUE;
    private LevelChunkSection[] r;
    private long s = Long.MIN_VALUE;
    private LevelChunkSection[] t;
    private boolean[] u;

    private g(ServerLevel serverLevel, int n2, int n3, int n4, int n5, int n6) {
        this.b = serverLevel.getMinBuildHeight();
        this.c = serverLevel.getMaxBuildHeight() - 1;
        this.h = n2;
        this.i = n3;
        this.j = n2 + n4;
        this.k = n3 + n4;
        this.l = n5;
        this.m = n6;
    }

    static g a(ServerLevel serverLevel, Long2ObjectMap<int[]> long2ObjectMap, int n2, int n3, int n4, int n5, int n6) {
        g g2 = new g(serverLevel, n2, n3, n4, n5, n6);
        for (Long2ObjectMap.Entry entry : long2ObjectMap.long2ObjectEntrySet()) {
            long l2 = entry.getLongKey();
            LevelChunk levelChunk = serverLevel.getChunkSource().getChunkNow(ChunkPos.getX((long)l2), ChunkPos.getZ((long)l2));
            LevelChunkSection[] levelChunkSectionArray = levelChunk.getSections();
            LevelChunkSection[] levelChunkSectionArray2 = new LevelChunkSection[levelChunkSectionArray.length];
            int n7 = Math.max(0, serverLevel.getSectionIndex(Math.max(((int[])entry.getValue())[0], g2.b)));
            int n8 = Math.min(levelChunkSectionArray.length - 1, serverLevel.getSectionIndex(Math.min(((int[])entry.getValue())[1], g2.c)));
            for (int i2 = n7; i2 <= n8; ++i2) {
                levelChunkSectionArray2[i2] = new LevelChunkSection(cyou.rimuru.shootingstardemo.mc1211.star.f.a((PalettedContainer<BlockState>)levelChunkSectionArray[i2].getStates()), levelChunkSectionArray[i2].getBiomes());
            }
            g2.d.put(l2, levelChunkSectionArray2);
            g2.o.put(l2, cyou.rimuru.shootingstardemo.mc1211.star.b.a(serverLevel, l2));
        }
        return g2;
    }

    boolean a(ServerLevel serverLevel) {
        for (Long2IntMap.Entry entry : this.o.long2IntEntrySet()) {
            if (cyou.rimuru.shootingstardemo.mc1211.star.b.c(serverLevel, entry.getLongKey()) == entry.getIntValue()) continue;
            return true;
        }
        return false;
    }

    void b(ServerLevel serverLevel) {
        if (!this.p) {
            this.p = true;
            LongIterator longIterator = this.o.keySet().iterator();
            while (longIterator.hasNext()) {
                long l2 = (Long)longIterator.next();
                cyou.rimuru.shootingstardemo.mc1211.star.b.b(serverLevel, l2);
            }
        }
    }

    void a() {
        for (Long2ObjectMap.Entry entry : this.d.long2ObjectEntrySet()) {
            long l2 = entry.getLongKey();
            LevelChunkSection[] levelChunkSectionArray = (LevelChunkSection[])entry.getValue();
            LevelChunkSection[] levelChunkSectionArray2 = (LevelChunkSection[])levelChunkSectionArray.clone();
            boolean[] blArray = new boolean[levelChunkSectionArray.length];
            boolean[] blArray2 = new boolean[levelChunkSectionArray.length];
            int n2 = ChunkPos.getX((long)l2) << 4;
            int n3 = ChunkPos.getZ((long)l2) << 4;
            boolean bl = n2 >= this.h && n2 + 16 <= this.j && n3 >= this.i && n3 + 16 <= this.k;
            for (int i2 = 0; i2 < levelChunkSectionArray.length; ++i2) {
                int n4 = this.b + (i2 << 4);
                if (!bl || levelChunkSectionArray[i2] == null || n4 < this.l || n4 + 15 > this.m) continue;
                levelChunkSectionArray2[i2] = new LevelChunkSection(new PalettedContainer((IdMap)Block.BLOCK_STATE_REGISTRY, (Object)a, PalettedContainer.Strategy.SECTION_STATES), levelChunkSectionArray[i2].getBiomes());
                blArray[i2] = !levelChunkSectionArray[i2].hasOnlyAir();
                blArray2[i2] = true;
            }
            this.e.put(l2, levelChunkSectionArray2);
            this.f.put(l2, blArray);
            this.g.put(l2, blArray2);
        }
    }

    @Override
    public BlockState a(int n2, int n3, int n4) {
        LevelChunkSection levelChunkSection;
        if (n3 < this.b || n3 > this.c) {
            return a;
        }
        long l2 = ChunkPos.asLong((int)(n2 >> 4), (int)(n4 >> 4));
        if (l2 != this.q) {
            this.q = l2;
            this.r = (LevelChunkSection[])this.d.get(l2);
        }
        LevelChunkSection levelChunkSection2 = levelChunkSection = this.r == null ? null : this.r[n3 - this.b >> 4];
        if (levelChunkSection == null) {
            ++this.n;
            return a;
        }
        return levelChunkSection.getBlockState(n2 & 0xF, n3 & 0xF, n4 & 0xF);
    }

    @Override
    public void put(int n2, int n3, int n4, BlockState blockState) {
        LevelChunkSection levelChunkSection;
        if (n3 < this.b || n3 > this.c) {
            return;
        }
        long l2 = ChunkPos.asLong((int)(n2 >> 4), (int)(n4 >> 4));
        if (l2 != this.s) {
            this.s = l2;
            this.t = (LevelChunkSection[])this.e.get(l2);
            this.u = (boolean[])this.f.get(l2);
        }
        int n5 = n3 - this.b >> 4;
        LevelChunkSection levelChunkSection2 = levelChunkSection = this.t == null ? null : this.t[n5];
        if (levelChunkSection == null) {
            ++this.n;
            return;
        }
        if (levelChunkSection.setBlockState(n2 & 0xF, n3 & 0xF, n4 & 0xF, blockState, false) != blockState) {
            this.u[n5] = true;
        }
    }

    @Override
    public void a(int n2, int n3, int n4, int n5) {
        boolean[] blArray = (boolean[])this.g.get(ChunkPos.asLong((int)(n2 >> 4), (int)(n3 >> 4)));
        int n6 = Math.max(n4, this.b);
        int n7 = Math.min(n5, this.c);
        while (n6 <= n7) {
            int n8 = n6 - this.b >> 4;
            int n9 = Math.min(n7, this.b + (n8 << 4) + 15);
            if (blArray == null || !blArray[n8]) {
                for (int i2 = n6; i2 <= n9; ++i2) {
                    this.put(n2, i2, n3, a);
                }
            }
            n6 = n9 + 1;
        }
    }

    BlockState b(int n2, int n3, int n4) {
        LevelChunkSection[] levelChunkSectionArray = (LevelChunkSection[])this.e.get(ChunkPos.asLong((int)(n2 >> 4), (int)(n4 >> 4)));
        LevelChunkSection levelChunkSection = levelChunkSectionArray == null || n3 < this.b || n3 > this.c ? null : levelChunkSectionArray[n3 - this.b >> 4];
        return levelChunkSection == null ? null : levelChunkSection.getBlockState(n2 & 0xF, n3 & 0xF, n4 & 0xF);
    }

    int b() {
        return this.n;
    }

    long[] c(ServerLevel serverLevel) {
        this.b(serverLevel);
        ThreadedLevelLightEngine threadedLevelLightEngine = serverLevel.getChunkSource().getLightEngine();
        LongArrayList longArrayList = new LongArrayList();
        for (Long2ObjectMap.Entry entry : this.f.long2ObjectEntrySet()) {
            long l2 = entry.getLongKey();
            boolean[] blArray = (boolean[])entry.getValue();
            LevelChunk levelChunk = serverLevel.getChunkSource().getChunkNow(ChunkPos.getX((long)l2), ChunkPos.getZ((long)l2));
            if (levelChunk == null) continue;
            LevelChunkSection[] levelChunkSectionArray = levelChunk.getSections();
            LevelChunkSection[] levelChunkSectionArray2 = (LevelChunkSection[])this.e.get(l2);
            boolean bl = false;
            for (int i2 = 0; i2 < blArray.length; ++i2) {
                if (!blArray[i2]) continue;
                bl = true;
                levelChunkSectionArray[i2] = levelChunkSectionArray2[i2];
                if (levelChunkSectionArray2[i2].hasOnlyAir()) continue;
                threadedLevelLightEngine.updateSectionStatus(SectionPos.of((ChunkPos)levelChunk.getPos(), (int)serverLevel.getSectionYFromSectionIndex(i2)), false);
            }
            if (!bl) continue;
            if (!levelChunk.getBlockEntities().isEmpty()) {
                ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
                for (BlockPos blockPos : levelChunk.getBlockEntities().keySet()) {
                    int n2 = levelChunk.getSectionIndex(blockPos.getY());
                    if (n2 < 0 || n2 >= blArray.length || !blArray[n2] || levelChunkSectionArray[n2].getBlockState(blockPos.getX() & 0xF, blockPos.getY() & 0xF, blockPos.getZ() & 0xF).hasBlockEntity()) continue;
                    arrayList.add(blockPos);
                }
                for (BlockPos blockPos : arrayList) {
                    levelChunk.removeBlockEntity(blockPos);
                }
            }
            longArrayList.add(l2);
            cyou.rimuru.shootingstardemo.mc1211.star.b.d(serverLevel, l2);
        }
        return longArrayList.toLongArray();
    }
}

