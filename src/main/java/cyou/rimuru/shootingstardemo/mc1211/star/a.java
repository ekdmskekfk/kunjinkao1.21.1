/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.star;

import cyou.rimuru.shootingstardemo.mc1211.ShootingStarDemo;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.IdMap;
import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.phys.Vec3;

public final class a {
    public static final int a = 12;
    public static final int b = 32;
    private static final int e = 64;
    private static final int f = 600;
    private static final BlockState g = Blocks.AIR.defaultBlockState();
    private static final List<a> h = new ArrayList<a>();
    private static final int i = Math.max(2, Math.min(8, Runtime.getRuntime().availableProcessors() - 2));
    private static final ExecutorService j = Executors.newFixedThreadPool(i, runnable -> {
        Thread thread = new Thread(runnable, "Carving builder");
        thread.setDaemon(true);
        thread.setPriority(4);
        return thread;
    });
    private static final Long2ObjectOpenHashMap<a> k = new Long2ObjectOpenHashMap();
    public static final int c = Integer.MIN_VALUE;
    public static final boolean d = Boolean.getBoolean("shooting_star_demo.carve.verify");
    private final ServerLevel l;
    private String m;
    private boolean n;
    private final d o;
    private final BlockPos p;
    private g q;
    private Floors r;
    private long s;
    private long t;
    private long u;
    private long v;
    private final List<ChunkPos> w = new ArrayList<ChunkPos>();
    private final List<Double> x = new ArrayList<Double>();
    private int y;
    private int z = 12;
    private final List<Integer> A = new ArrayList<Integer>();
    private final Set<Integer> B = new HashSet<Integer>();
    private h C;
    private int D;
    private boolean E;
    private long F;
    private int G;
    private long H = -1L;
    private long I;
    private long J;
    private long K;
    private long L;
    private long M;
    private long N;
    private final Long2ObjectOpenHashMap<b> O = new Long2ObjectOpenHashMap();
    private long P;
    private long Q;
    private long R;
    private long S;
    private long T;
    private long U;
    private long V;
    private long W;
    private long X;
    private long Y;
    private long Z;

    public static boolean a(BlockState blockState) {
        return blockState.is(cyou.rimuru.shootingstardemo.mc1211.registry.b.c);
    }

    private a(ServerLevel serverLevel, String string, d d2, BlockPos blockPos) {
        this.l = serverLevel;
        this.m = string;
        this.o = d2;
        this.p = blockPos;
    }

    private static d a(ServerLevel serverLevel, f f2) {
        int n2 = serverLevel.getMinBuildHeight() - 1;
        return (n3, n4) -> f2.inside((double)n3 + 0.5, (double)n4 + 0.5) ? n2 : Integer.MIN_VALUE;
    }

    public a(ServerLevel serverLevel, Vec3 vec3, double d2, BlockPos blockPos) {
        this(serverLevel, "The Shooting Star", cyou.rimuru.shootingstardemo.mc1211.star.a.a(serverLevel, (double d3, double d4) -> cyou.rimuru.shootingstardemo.mc1211.star.a.c(d3 - vec3.x) + cyou.rimuru.shootingstardemo.mc1211.star.a.c(d4 - vec3.z) <= d2 * d2), blockPos);
        int n2 = SectionPos.blockToSectionCoord((double)(vec3.x - d2));
        int n3 = SectionPos.blockToSectionCoord((double)(vec3.x + d2));
        int n4 = SectionPos.blockToSectionCoord((double)(vec3.z - d2));
        int n5 = SectionPos.blockToSectionCoord((double)(vec3.z + d2));
        for (int i2 = n2; i2 <= n3; ++i2) {
            for (int i3 = n4; i3 <= n5; ++i3) {
                double d5;
                double d6 = Math.max((double)i2 * 16.0, Math.min(vec3.x, (double)i2 * 16.0 + 16.0)) - vec3.x;
                if (!(d6 * d6 + (d5 = Math.max((double)i3 * 16.0, Math.min(vec3.z, (double)i3 * 16.0 + 16.0)) - vec3.z) * d5 <= d2 * d2)) continue;
                this.a(new ChunkPos(i2, i3), Math.sqrt(cyou.rimuru.shootingstardemo.mc1211.star.a.c((double)i2 * 16.0 + 8.0 - vec3.x) + cyou.rimuru.shootingstardemo.mc1211.star.a.c((double)i3 * 16.0 + 8.0 - vec3.z)));
            }
        }
        this.k();
    }

    public static a a(ServerLevel serverLevel, Vec3 vec3, double d2, d d3, g g2, BlockPos blockPos) {
        a a2 = new a(serverLevel, "Seven Stars", d3, blockPos);
        a2.q = g2;
        int n2 = SectionPos.blockToSectionCoord((double)(vec3.x - d2));
        int n3 = SectionPos.blockToSectionCoord((double)(vec3.x + d2));
        int n4 = SectionPos.blockToSectionCoord((double)(vec3.z - d2));
        int n5 = SectionPos.blockToSectionCoord((double)(vec3.z + d2));
        for (int i2 = n2; i2 <= n3; ++i2) {
            for (int i3 = n4; i3 <= n5; ++i3) {
                double d4;
                double d5 = Math.max((double)i2 * 16.0, Math.min(vec3.x, (double)i2 * 16.0 + 16.0)) - vec3.x;
                double d6 = Math.sqrt(d5 * d5 + (d4 = Math.max((double)i3 * 16.0, Math.min(vec3.z, (double)i3 * 16.0 + 16.0)) - vec3.z) * d4);
                if (!(d6 <= d2)) continue;
                a2.a(new ChunkPos(i2, i3), d6);
            }
        }
        a2.k();
        return a2;
    }

    public static void a(ServerLevel serverLevel, Map<BlockPos, BlockState> map) {
        Long2ObjectOpenHashMap<List<Map.Entry<BlockPos, BlockState>>> long2ObjectOpenHashMap = new Long2ObjectOpenHashMap<List<Map.Entry<BlockPos, BlockState>>>();
        for (Map.Entry<BlockPos, BlockState> entry2 : map.entrySet()) {
            BlockPos blockPos2 = entry2.getKey();
            if (blockPos2.getY() < serverLevel.getMinBuildHeight() || blockPos2.getY() > serverLevel.getMaxBuildHeight() - 1) continue;
            long2ObjectOpenHashMap.computeIfAbsent(ChunkPos.asLong(blockPos2), l2 -> new ArrayList<Map.Entry<BlockPos, BlockState>>()).add(entry2);
        }
        ThreadedLevelLightEngine threadedLevelLightEngine = serverLevel.getChunkSource().getLightEngine();
        for (Long2ObjectMap.Entry<List<Map.Entry<BlockPos, BlockState>>> chunkEntry : long2ObjectOpenHashMap.long2ObjectEntrySet()) {
            cyou.rimuru.shootingstardemo.mc1211.star.a.a(chunkEntry.getLongKey());
            cyou.rimuru.shootingstardemo.mc1211.star.b.d(serverLevel, chunkEntry.getLongKey());
            ChunkPos chunkPos = new ChunkPos(chunkEntry.getLongKey());
            LevelChunk levelChunk = serverLevel.getChunk(chunkPos.x, chunkPos.z);
            LevelChunkSection[] levelChunkSectionArray = levelChunk.getSections();
            boolean[] blArray = new boolean[levelChunkSectionArray.length];
            for (Map.Entry<BlockPos, BlockState> entry : chunkEntry.getValue()) {
                BlockPos blockPos3 = entry.getKey();
                int n2 = levelChunk.getSectionIndex(blockPos3.getY());
                if (levelChunk.getBlockEntity(blockPos3) != null) {
                    levelChunk.removeBlockEntity(blockPos3);
                }
                BlockState blockState = cyou.rimuru.shootingstardemo.mc1211.registry.a.a((BlockState)entry.getValue(), blockPos3, (BlockPos blockPos) -> map.containsKey(blockPos) ? (BlockState)map.get(blockPos) : serverLevel.getBlockState(blockPos));
                levelChunkSectionArray[n2].setBlockState(blockPos3.getX() & 0xF, blockPos3.getY() & 0xF, blockPos3.getZ() & 0xF, blockState, false);
                blArray[n2] = true;
            }
            for (int i2 = 0; i2 < levelChunkSectionArray.length; ++i2) {
                if (!blArray[i2] || levelChunkSectionArray[i2].hasOnlyAir()) continue;
                threadedLevelLightEngine.updateSectionStatus(SectionPos.of((ChunkPos)chunkPos, (int)serverLevel.getSectionYFromSectionIndex(i2)), false);
            }
            cyou.rimuru.shootingstardemo.mc1211.star.a.a(serverLevel, levelChunk);
        }
    }

    public a a(String string) {
        this.m = string;
        return this;
    }

    public a a() {
        this.n = true;
        return this;
    }

    public a a(Floors a2) {
        this.r = a2;
        return this;
    }

    public void a(boolean bl) {
        ++this.v;
        this.u += bl ? 1L : 0L;
    }

    public a a(int n2) {
        this.z = Math.max(1, n2);
        return this;
    }

    public a b() {
        this.E = true;
        return this;
    }

    public a a(h h2) {
        this.C = h2;
        this.E = true;
        return this;
    }

    public static void a(a a2) {
        if (a2 != null && !a2.i() && !h.contains(a2)) {
            h.add(a2);
        }
    }

    public static void c() {
        h.removeIf(a2 -> {
            a2.a(Double.MAX_VALUE);
            return a2.i();
        });
    }

    public static int d() {
        return h.size();
    }

    public static void e() {
        for (a a2 : h) {
            a2.f();
        }
        h.clear();
    }

    public void f() {
        if (this.i()) {
            return;
        }
        if (this.w.size() - this.y + this.A.size() <= 600) {
            this.h();
            return;
        }
        this.l();
        while (this.y < this.w.size()) {
            this.A.add(this.y++);
        }
        int n2 = 0;
        for (int n3 : this.A) {
            ChunkPos chunkPos = this.w.get(n3);
            LevelChunk levelChunk = this.l.getChunkSource().getChunkNow(chunkPos.x, chunkPos.z);
            if (levelChunk != null) {
                this.a(levelChunk, null, 0, false);
                continue;
            }
            ++n2;
        }
        this.A.clear();
        for (int n3 : this.B) {
            this.C.b(this.w.get(n3));
        }
        this.B.clear();
        ShootingStarDemo.LOGGER.a("{} stopped with the world: {} of its {} chunks were never loaded and stay as they were", this.m, n2, this.w.size());
    }

    public List<ChunkPos> g() {
        return Collections.unmodifiableList(this.w);
    }

    public void h() {
        this.E = false;
        while (!this.i()) {
            this.a(Double.MAX_VALUE);
        }
        for (int n2 : this.B) {
            this.C.b(this.w.get(n2));
        }
        this.B.clear();
    }

    private void a(ChunkPos chunkPos, double d2) {
        this.w.add(chunkPos);
        this.x.add(d2);
    }

    private void k() {
        Integer[] integerArray = new Integer[this.w.size()];
        for (int i2 = 0; i2 < integerArray.length; ++i2) {
            integerArray[i2] = i2;
        }
        Arrays.sort(integerArray, Comparator.comparingDouble(this.x::get));
        ArrayList<ChunkPos> arrayList = new ArrayList<ChunkPos>(this.w);
        ArrayList<Double> arrayList2 = new ArrayList<Double>(this.x);
        this.w.clear();
        this.x.clear();
        Integer[] integerArray2 = integerArray;
        int n2 = integerArray2.length;
        for (int i3 = 0; i3 < n2; ++i3) {
            int n3 = integerArray2[i3];
            this.w.add((ChunkPos)arrayList.get(n3));
            this.x.add((Double)arrayList2.get(n3));
        }
    }

    public boolean i() {
        return this.y >= this.w.size() && this.A.isEmpty();
    }

    public void j() {
        this.a(Double.MAX_VALUE);
    }

    public void a(double d2) {
        Object object;
        long l2 = System.nanoTime();
        if (this.H < 0L) {
            this.H = l2;
        }
        int n2 = this.z;
        ArrayList<LevelChunk> arrayList = new ArrayList<LevelChunk>(n2);
        ArrayList<Integer> arrayList2 = new ArrayList<Integer>();
        boolean bl = !this.E || this.C == null || this.D++ % 20 == 0;
        Iterator<Integer> pendingIterator = this.A.iterator();
        while (pendingIterator.hasNext() && n2 > 0) {
            int n3 = pendingIterator.next();
            if (!bl && !this.B.contains(n3)) continue;
            ChunkPos chunkPos2 = this.w.get(n3);
            LevelChunk levelChunk = this.E ? this.l.getChunkSource().getChunkNow(chunkPos2.x, chunkPos2.z) : this.l.getChunk(chunkPos2.x, chunkPos2.z);
            if (levelChunk == null) continue;
            arrayList.add(levelChunk);
            pendingIterator.remove();
            arrayList2.add(n3);
            --n2;
        }
        while (n2 > 0 && this.y < this.w.size() && this.x.get(this.y) <= d2) {
            LevelChunk levelChunk;
            ChunkPos chunkPos = this.w.get(this.y);
            LevelChunk levelChunk2 = levelChunk = this.E ? this.l.getChunkSource().getChunkNow(chunkPos.x, chunkPos.z) : this.l.getChunk(chunkPos.x, chunkPos.z);
            if (levelChunk == null) {
                this.A.add(this.y);
            } else {
                arrayList.add(levelChunk);
                --n2;
            }
            ++this.y;
        }
        long l3 = System.nanoTime();
        this.I += l3 - l2;
        long l4 = 0L;
        if (!this.O.isEmpty()) {
            boolean bl2 = true;
            Iterator<LevelChunk> iterator2 = arrayList.iterator();
            while (iterator2.hasNext()) {
                c c2;
                b b2;
                LevelChunk levelChunk;
                block20: {
                    levelChunk = iterator2.next();
                    ChunkPos chunkPos3 = levelChunk.getPos();
                    long l5 = ChunkPos.asLong((int)chunkPos3.x, (int)chunkPos3.z);
                    b2 = (b)this.O.remove(l5);
                    if (b2 == null) continue;
                    k.remove(l5);
                    boolean bl3 = cyou.rimuru.shootingstardemo.mc1211.star.b.c(this.l, l5) == b2.c();
                    cyou.rimuru.shootingstardemo.mc1211.star.b.b(this.l, l5);
                    c2 = null;
                    if (b2.a() == levelChunk && bl3 && b2.b().isDone() && !b2.b().isCancelled()) {
                        try {
                            c2 = b2.b().join();
                        }
                        catch (RuntimeException runtimeException) {
                            if (this.Z++ != 0L) break block20;
                            ShootingStarDemo.LOGGER.b("{}: a chunk built ahead failed, cutting it on the spot", this.m, runtimeException);
                        }
                    }
                }
                if (c2 == null) {
                    b2.b().cancel(false);
                    ++this.Q;
                    continue;
                }
                iterator2.remove();
                l4 += this.a(levelChunk, c2, bl2 && d);
                bl2 = false;
            }
            l3 = System.nanoTime();
        }
        int[] nArray = null;
        if (this.r != null && !arrayList.isEmpty()) {
            int[] nArray2 = new int[arrayList.size() * 2];
            for (int i2 = 0; i2 < arrayList.size(); ++i2) {
                nArray2[i2 * 2] = ((LevelChunk)arrayList.get((int)i2)).getPos().x;
                nArray2[i2 * 2 + 1] = ((LevelChunk)arrayList.get((int)i2)).getPos().z;
            }
            nArray = new int[arrayList.size() * 256];
            if (!this.r.floors(nArray2, arrayList.size(), nArray)) {
                nArray = null;
            }
            this.J += System.nanoTime() - l3;
        }
        for (int i3 = 0; i3 < arrayList.size(); ++i3) {
            this.a((LevelChunk)arrayList.get(i3), nArray, i3 * 256, i3 == 0);
        }
        Iterator iterator = arrayList2.iterator();
        while (iterator.hasNext()) {
            int n4 = (Integer)iterator.next();
            this.b(n4);
        }
        if (this.C != null && this.E) {
            for (int i4 = 0; i4 < this.A.size() && this.B.size() < 64; ++i4) {
                int n5 = this.A.get(i4);
                if (!this.B.add(n5)) continue;
                this.C.a(this.w.get(n5));
            }
        }
        this.N = Math.max(this.N, System.nanoTime() - l2 - l4);
        if (this.i()) {
            this.l();
            ShootingStarDemo.LOGGER.a("{} cut {} whole sections and {} rim blocks out of {} chunks in {} ms", this.m, this.G, this.F, this.w.size(), (System.nanoTime() - this.H) / 1000000L);
            ShootingStarDemo.LOGGER.a("[carve-timer] {}: load {} ms, floor {} ms, write {} ms, surface {} ms, relight {} ms, worst tick {} ms{}", this.m, this.I / 1000000L, this.J / 1000000L, this.K / 1000000L, this.L / 1000000L, this.M / 1000000L, this.N / 1000000L, this.r == null ? "" : (this.t == 0L ? " (batched)" : " (batched, " + this.s + " of " + this.t + " checked columns differ" + (String)(this.v == 0L ? ")" : ", " + this.u + " of " + this.v + " checked surface blocks)")));
            if (this.P + this.Q > 0L) {
                ShootingStarDemo.LOGGER.a("[carve-timer] {} built ahead: {} chunks swapped in, {} cut on the spot; server thread copies {} ms, swaps {} ms; builders floor {} ms, write {} ms, surface {} ms (wall){}", this.m, this.P, this.Q, this.R / 1000000L, this.S / 1000000L, this.T / 1000000L, this.U / 1000000L, this.V / 1000000L, this.X == 0L ? "" : ", verify " + this.Y + " of " + this.X + " blocks differ (" + this.W / 1000000L + " ms, left out)");
            }
        }
    }

    public void b(double d2) {
        int n2 = this.z * 2 - this.O.size();
        for (int i2 = this.y; n2 > 0 && i2 < this.w.size() && this.x.get(i2) <= d2; ++i2) {
            LevelChunk levelChunk;
            ChunkPos chunkPos = this.w.get(i2);
            long l2 = ChunkPos.asLong((int)chunkPos.x, (int)chunkPos.z);
            if (this.O.containsKey(l2) || k.containsKey(l2) || (levelChunk = this.l.getChunkSource().getChunkNow(chunkPos.x, chunkPos.z)) == null) continue;
            long l3 = System.nanoTime();
            LevelChunkSection[] levelChunkSectionArray = levelChunk.getSections();
            ArrayList<PalettedContainer<BlockState>> arrayList = new ArrayList<PalettedContainer<BlockState>>(levelChunkSectionArray.length);
            ArrayList<PalettedContainerRO<Holder<Biome>>> arrayList2 = new ArrayList<PalettedContainerRO<Holder<Biome>>>(levelChunkSectionArray.length);
            for (LevelChunkSection levelChunkSection : levelChunkSectionArray) {
                arrayList.add(levelChunkSection.hasOnlyAir() ? null : cyou.rimuru.shootingstardemo.mc1211.star.f.a((PalettedContainer<BlockState>)levelChunkSection.getStates()));
                arrayList2.add(levelChunkSection.getBiomes());
            }
            this.O.put(l2, new b(levelChunk, CompletableFuture.supplyAsync(() -> this.a(chunkPos, arrayList, arrayList2), j), cyou.rimuru.shootingstardemo.mc1211.star.b.a(this.l, l2)));
            k.put(l2, this);
            this.R += System.nanoTime() - l3;
            --n2;
        }
    }

    private c a(ChunkPos chunkPos, List<PalettedContainer<BlockState>> list, List<PalettedContainerRO<Holder<Biome>>> list2) {
        long l2 = System.nanoTime();
        LevelChunkSection[] levelChunkSectionArray = new LevelChunkSection[list.size()];
        for (int i2 = 0; i2 < levelChunkSectionArray.length; ++i2) {
            PalettedContainer<BlockState> palettedContainer = list.get(i2);
            if (palettedContainer == null) continue;
            levelChunkSectionArray[i2] = new LevelChunkSection(palettedContainer, list2.get(i2));
        }
        int[] nArray = null;
        if (this.r != null && !this.r.floors(new int[]{chunkPos.x, chunkPos.z}, 1, nArray = new int[256])) {
            nArray = null;
        }
        e e3 = this.a(chunkPos, nArray, 0, false);
        long[] lArray = new long[3];
        lArray[0] = System.nanoTime() - l2;
        boolean[] blArray = new boolean[levelChunkSectionArray.length];
        boolean[] blArray2 = new boolean[levelChunkSectionArray.length];
        long[] lArray2 = e3.b() == 0 ? new long[2] : this.a(chunkPos, levelChunkSectionArray, e3, blArray, blArray2, lArray);
        return new c(e3.b() > 0, levelChunkSectionArray, blArray, blArray2, (int)lArray2[0], lArray2[1], lArray);
    }

    private long a(LevelChunk levelChunk, c c2, boolean bl) {
        ChunkPos chunkPos = levelChunk.getPos();
        long l2 = 0L;
        if (bl) {
            l2 = this.a(levelChunk, c2);
        }
        long l3 = System.nanoTime();
        ++this.P;
        this.T += c2.g()[0];
        this.U += c2.g()[1];
        this.V += c2.g()[2];
        this.G += c2.e();
        this.F += c2.f();
        if (!c2.a()) {
            this.S += System.nanoTime() - l3;
            return l2;
        }
        cyou.rimuru.shootingstardemo.mc1211.star.b.d(this.l, ChunkPos.asLong((int)chunkPos.x, (int)chunkPos.z));
        LevelChunkSection[] levelChunkSectionArray = levelChunk.getSections();
        for (int i2 = 0; i2 < levelChunkSectionArray.length; ++i2) {
            if (!c2.d()[i2]) continue;
            levelChunkSectionArray[i2] = c2.b()[i2];
        }
        ThreadedLevelLightEngine threadedLevelLightEngine = this.l.getChunkSource().getLightEngine();
        for (int i3 = 0; i3 < levelChunkSectionArray.length; ++i3) {
            if (!c2.c()[i3] || levelChunkSectionArray[i3].hasOnlyAir()) continue;
            threadedLevelLightEngine.updateSectionStatus(SectionPos.of((ChunkPos)chunkPos, (int)this.l.getSectionYFromSectionIndex(i3)), false);
        }
        this.a(levelChunk);
        long l4 = System.nanoTime();
        this.S += l4 - l3;
        cyou.rimuru.shootingstardemo.mc1211.star.a.a(this.l, levelChunk);
        this.M += System.nanoTime() - l4;
        return l2;
    }

    private long a(LevelChunk levelChunk, c c2) {
        long l2 = System.nanoTime();
        ChunkPos chunkPos = levelChunk.getPos();
        LevelChunkSection[] levelChunkSectionArray = levelChunk.getSections();
        LevelChunkSection[] levelChunkSectionArray2 = new LevelChunkSection[levelChunkSectionArray.length];
        for (int i2 = 0; i2 < levelChunkSectionArray.length; ++i2) {
            levelChunkSectionArray2[i2] = levelChunkSectionArray[i2].hasOnlyAir() ? null : new LevelChunkSection(cyou.rimuru.shootingstardemo.mc1211.star.f.a((PalettedContainer<BlockState>)levelChunkSectionArray[i2].getStates()), levelChunkSectionArray[i2].getBiomes());
        }
        boolean[] blArray = new boolean[levelChunkSectionArray.length];
        boolean[] blArray2 = new boolean[levelChunkSectionArray.length];
        e e2 = this.a(chunkPos, null, 0, false);
        if (e2.b() > 0) {
            this.a(chunkPos, levelChunkSectionArray2, e2, blArray, blArray2, new long[3]);
        }
        for (int i3 = 0; i3 < levelChunkSectionArray.length; ++i3) {
            if (!blArray2[i3] && !c2.d()[i3]) continue;
            LevelChunkSection levelChunkSection = c2.d()[i3] ? c2.b()[i3] : levelChunkSectionArray[i3];
            LevelChunkSection levelChunkSection2 = blArray2[i3] ? levelChunkSectionArray2[i3] : levelChunkSectionArray[i3];
            for (int i4 = 0; i4 < 16; ++i4) {
                for (int i5 = 0; i5 < 16; ++i5) {
                    for (int i6 = 0; i6 < 16; ++i6) {
                        ++this.X;
                        this.Y += levelChunkSection.getBlockState(i6, i4, i5) != levelChunkSection2.getBlockState(i6, i4, i5) ? 1L : 0L;
                    }
                }
            }
        }
        long l3 = System.nanoTime() - l2;
        this.W += l3;
        return l3;
    }

    private static void a(long l2) {
        b b2;
        a a2 = (a)k.remove(l2);
        if (a2 != null && (b2 = (b)a2.O.remove(l2)) != null) {
            b2.b().cancel(false);
            cyou.rimuru.shootingstardemo.mc1211.star.b.b(a2.l, l2);
            ++a2.Q;
        }
    }

    private void l() {
        for (Long2ObjectMap.Entry entry : this.O.long2ObjectEntrySet()) {
            ((b)entry.getValue()).b().cancel(false);
            k.remove(entry.getLongKey());
            cyou.rimuru.shootingstardemo.mc1211.star.b.b(this.l, entry.getLongKey());
        }
        this.O.clear();
    }

    private void b(int n2) {
        if (this.B.remove(n2)) {
            this.C.b(this.w.get(n2));
        }
    }

    private int a(int n2, int n3) {
        return this.b(n2, n3) ? Integer.MIN_VALUE : this.o.floor(n2, n3);
    }

    private boolean b(int n2, int n3) {
        return this.p != null && Math.abs(n2 - this.p.getX()) <= 2 && Math.abs(n3 - this.p.getZ()) <= 2;
    }

    private void a(LevelChunk levelChunk, int[] nArray, int n2, boolean bl) {
        long l2 = System.nanoTime();
        ChunkPos chunkPos = levelChunk.getPos();
        cyou.rimuru.shootingstardemo.mc1211.star.a.a(ChunkPos.asLong((int)chunkPos.x, (int)chunkPos.z));
        e e2 = this.a(chunkPos, nArray, n2, bl);
        if (e2.b() > 0) {
            cyou.rimuru.shootingstardemo.mc1211.star.b.d(this.l, ChunkPos.asLong((int)chunkPos.x, (int)chunkPos.z));
        }
        long l3 = System.nanoTime();
        this.J += l3 - l2;
        if (e2.b() == 0) {
            return;
        }
        LevelChunkSection[] levelChunkSectionArray = levelChunk.getSections();
        boolean[] blArray = new boolean[levelChunkSectionArray.length];
        long[] lArray = new long[3];
        long[] lArray2 = this.a(chunkPos, levelChunkSectionArray, e2, blArray, new boolean[levelChunkSectionArray.length], lArray);
        this.G += (int)lArray2[0];
        this.F += lArray2[1];
        ThreadedLevelLightEngine threadedLevelLightEngine = this.l.getChunkSource().getLightEngine();
        for (int i2 = 0; i2 < levelChunkSectionArray.length; ++i2) {
            if (!blArray[i2] || levelChunkSectionArray[i2].hasOnlyAir()) continue;
            threadedLevelLightEngine.updateSectionStatus(SectionPos.of((ChunkPos)chunkPos, (int)this.l.getSectionYFromSectionIndex(i2)), false);
        }
        this.K += lArray[1];
        long l4 = System.nanoTime();
        this.a(levelChunk);
        long l5 = System.nanoTime();
        this.L += lArray[2] + l5 - l4;
        cyou.rimuru.shootingstardemo.mc1211.star.a.a(this.l, levelChunk);
        this.M += System.nanoTime() - l5;
    }

    private void a(LevelChunk levelChunk) {
        for (BlockPos blockPos : new ArrayList<BlockPos>(levelChunk.getBlockEntitiesPos())) {
            int n2 = this.a(blockPos.getX(), blockPos.getZ());
            if (n2 == Integer.MIN_VALUE || blockPos.getY() <= n2) continue;
            levelChunk.removeBlockEntity(blockPos);
        }
    }

    private e a(ChunkPos chunkPos, int[] nArray, int n2, boolean bl) {
        int[] nArray2 = new int[256];
        int n3 = 0;
        int n4 = Integer.MIN_VALUE;
        for (int i2 = 0; i2 < 16; ++i2) {
            for (int i3 = 0; i3 < 16; ++i3) {
                int n5;
                int n6 = chunkPos.getMinBlockX() + i2;
                int n7 = chunkPos.getMinBlockZ() + i3;
                if (nArray == null) {
                    n5 = this.a(n6, n7);
                } else {
                    int n8 = n5 = this.b(n6, n7) ? Integer.MIN_VALUE : nArray[n2 + i2 * 16 + i3];
                    if (bl && d) {
                        ++this.t;
                        if (n5 != this.a(n6, n7)) {
                            ++this.s;
                        }
                    }
                }
                nArray2[i2 * 16 + i3] = n5;
                if (n5 == Integer.MIN_VALUE) continue;
                ++n3;
                n4 = Math.max(n4, n5);
            }
        }
        return new e(nArray2, n3, n4);
    }

    private long[] a(ChunkPos chunkPos, LevelChunkSection[] levelChunkSectionArray, e e2, boolean[] blArray, boolean[] blArray2, long[] lArray) {
        int n2;
        int n3;
        int n4;
        int n5;
        long l2 = System.nanoTime();
        int[] nArray = e2.a();
        int n6 = e2.b();
        int n7 = e2.c();
        int n8 = 0;
        long l3 = 0L;
        for (int i2 = 0; i2 < levelChunkSectionArray.length; ++i2) {
            LevelChunkSection levelChunkSection = levelChunkSectionArray[i2];
            if (levelChunkSection == null || levelChunkSection.hasOnlyAir()) continue;
            n5 = SectionPos.sectionToBlockCoord((int)this.l.getSectionYFromSectionIndex(i2));
            int n9 = n4 = this.n && levelChunkSection.maybeHas(cyou.rimuru.shootingstardemo.mc1211.star.a::a) ? 1 : 0;
            if (n6 == 256 && n5 > n7 && n4 == 0) {
                ++n8;
                levelChunkSectionArray[i2] = new LevelChunkSection(new PalettedContainer((IdMap)Block.BLOCK_STATE_REGISTRY, (Object)g, PalettedContainer.Strategy.SECTION_STATES), levelChunkSection.getBiomes());
            } else {
                n3 = 0;
                for (n2 = 0; n2 < 16; ++n2) {
                    for (int i3 = 0; i3 < 16; ++i3) {
                        int n10 = nArray[n2 * 16 + i3];
                        if (n10 == Integer.MIN_VALUE || n10 >= n5 + 15) continue;
                        for (int i4 = Math.max(0, n10 - n5 + 1); i4 < 16; ++i4) {
                            if (n4 != 0 && cyou.rimuru.shootingstardemo.mc1211.star.a.a(levelChunkSection.getBlockState(n2, i4, i3)) || levelChunkSection.setBlockState(n2, i4, i3, g, false).isAir()) continue;
                            ++l3;
                            n3 = 1;
                        }
                    }
                }
                if (n3 == 0) continue;
            }
            blArray[i2] = true;
            blArray2[i2] = true;
        }
        long l4 = System.nanoTime();
        lArray[1] = lArray[1] + (l4 - l2);
        if (this.q != null) {
            for (n5 = 0; n5 < 16; ++n5) {
                for (n4 = 0; n4 < 16; ++n4) {
                    BlockState blockState;
                    LevelChunkSection levelChunkSection;
                    n3 = nArray[n5 * 16 + n4];
                    if (n3 == Integer.MIN_VALUE || n3 < this.l.getMinBuildHeight() || n3 > this.l.getMaxBuildHeight() - 1 || (levelChunkSection = levelChunkSectionArray[n2 = this.l.getSectionIndex(n3)]) == null || levelChunkSection.hasOnlyAir()) continue;
                    BlockState blockState2 = levelChunkSection.getBlockState(n5, n3 & 0xF, n4);
                    BlockState blockState3 = blockState = blockState2.isAir() || this.n && cyou.rimuru.shootingstardemo.mc1211.star.a.a(blockState2) ? null : this.q.at(chunkPos.getMinBlockX() + n5, n3, chunkPos.getMinBlockZ() + n4, blockState2);
                    if (blockState == null || blockState == blockState2) continue;
                    levelChunkSection.setBlockState(n5, n3 & 0xF, n4, blockState, false);
                    blArray2[n2] = true;
                }
            }
        }
        lArray[2] = lArray[2] + (System.nanoTime() - l4);
        return new long[]{n8, l3};
    }

    static void a(ServerLevel serverLevel, LevelChunk levelChunk) {
        ChunkPos chunkPos = levelChunk.getPos();
        ThreadedLevelLightEngine threadedLevelLightEngine = serverLevel.getChunkSource().getLightEngine();
        Heightmap.primeHeightmaps((ChunkAccess)levelChunk, EnumSet.allOf(Heightmap.Types.class));
        levelChunk.initializeLightSources();
        threadedLevelLightEngine.propagateLightSources(chunkPos);
        levelChunk.setUnsaved(true);
        ClientboundLevelChunkWithLightPacket clientboundLevelChunkWithLightPacket = new ClientboundLevelChunkWithLightPacket(levelChunk, (LevelLightEngine)threadedLevelLightEngine, null, null);
        for (ServerPlayer serverPlayer : serverLevel.getChunkSource().chunkMap.getPlayers(chunkPos, false)) {
            serverPlayer.connection.send((Packet)clientboundLevelChunkWithLightPacket);
        }
    }

    private static double c(double d2) {
        return d2 * d2;
    }

    public static interface d {
        public int floor(int var1, int var2);
    }

    private static interface f {
        public boolean inside(double var1, double var3);
    }

    public static interface g {
        public BlockState at(int var1, int var2, int var3, BlockState var4);
    }

    public static interface Floors {
        public boolean floors(int[] var1, int var2, int[] var3);
    }

    public static interface h {
        public void a(ChunkPos var1);

        public void b(ChunkPos var1);
    }

    private static final class b
{
        private final LevelChunk a;
        private final CompletableFuture<c> b;
        private final int c;

        private b(LevelChunk chunk, CompletableFuture<c> job, int stamp) {
            this.a = chunk;
            this.b = job;
            this.c = stamp;
        }

        @Override
        public final String toString() {
            return cyou.rimuru.shootingstardemo.mc1211.RecordBridge.toString(this);
        }

        @Override
        public final int hashCode() {
            return (int)cyou.rimuru.shootingstardemo.mc1211.RecordBridge.hashCode(this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)cyou.rimuru.shootingstardemo.mc1211.RecordBridge.equals(this, object);
        }

        public LevelChunk a() {
            return this.a;
        }

        public CompletableFuture<c> b() {
            return this.b;
        }

        public int c() {
            return this.c;
        }
    }

    private static final class c
{
        private final boolean a;
        private final LevelChunkSection[] b;
        private final boolean[] c;
        private final boolean[] d;
        private final int e;
        private final long f;
        private final long[] g;

        private c(boolean any, LevelChunkSection[] sections, boolean[] lit, boolean[] dirty, int swapped, long removed, long[] ns) {
            this.a = any;
            this.b = sections;
            this.c = lit;
            this.d = dirty;
            this.e = swapped;
            this.f = removed;
            this.g = ns;
        }

        @Override
        public final String toString() {
            return cyou.rimuru.shootingstardemo.mc1211.RecordBridge.toString(this);
        }

        @Override
        public final int hashCode() {
            return (int)cyou.rimuru.shootingstardemo.mc1211.RecordBridge.hashCode(this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)cyou.rimuru.shootingstardemo.mc1211.RecordBridge.equals(this, object);
        }

        public boolean a() {
            return this.a;
        }

        public LevelChunkSection[] b() {
            return this.b;
        }

        public boolean[] c() {
            return this.c;
        }

        public boolean[] d() {
            return this.d;
        }

        public int e() {
            return this.e;
        }

        public long f() {
            return this.f;
        }

        public long[] g() {
            return this.g;
        }
    }

    private static final class e
{
        private final int[] a;
        private final int b;
        private final int c;

        private e(int[] floor, int count, int highest) {
            this.a = floor;
            this.b = count;
            this.c = highest;
        }

        @Override
        public final String toString() {
            return cyou.rimuru.shootingstardemo.mc1211.RecordBridge.toString(this);
        }

        @Override
        public final int hashCode() {
            return (int)cyou.rimuru.shootingstardemo.mc1211.RecordBridge.hashCode(this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)cyou.rimuru.shootingstardemo.mc1211.RecordBridge.equals(this, object);
        }

        public int[] a() {
            return this.a;
        }

        public int b() {
            return this.b;
        }

        public int c() {
            return this.c;
        }
    }
}

