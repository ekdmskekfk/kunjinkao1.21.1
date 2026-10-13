package cyou.rimuru.shootingstardemo.mc1211.star;

import cyou.rimuru.shootingstardemo.mc1211.ShootingStarDemo;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.server.level.TicketType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class m extends cyou.rimuru.shootingstardemo.mc1211.spell.a {
   public static final int k = 5;
   public static final int l = 7;
   public static final int m = 9;
   public static final int n = 290;
   public static final int o = 398;
   public static final int p = 426;
   public static final int q = 438;
   public static final int r = 462;
   public static final int s = 474;
   public static final int t = 20;
   public static final int u = 4;
   public static final int v = 562;
   public static final int w = 682;
   public static final int x = 256;
   public static final int y = 32;
   public static final int z = 8;
   public static final int A = 32;
   public static final int B = 20;
   public static final float C = 0.4F;
   public static final double D = 200.0;
   public static final double E = 320.0;
   public static final int F = 0;
   public static final int G = 1;
   public static final int H = 2;
   public static final int I = 3;
   private static final int N = 3;
   private static final long O = 1500000L;
   private static final long P = 20000000L;
   public static final int J = 30;
   private static final int Q = 6000;
   private static final boolean R = !"false".equals(System.getProperty("shooting_star_demo.tess.fast"));
   private static final boolean S = Boolean.getBoolean("shooting_star_demo.tess.verify");
   private static final int T = Mth.clamp(Runtime.getRuntime().availableProcessors() - 2, 2, 8);
   private static final ExecutorService U = Executors.newFixedThreadPool(T, var0 -> {
      Thread var1 = new Thread(var0, "Tesseract builder");
      var1.setDaemon(true);
      var1.setPriority(4);
      return var1;
   });
   private static final TicketType<ChunkPos> V = TicketType.create("shooting_star_demo:tesseract", Comparator.comparingLong(ChunkPos::toLong));
   private final ChunkPos W;
   private long X;
   private long Y;
   private long Z;
   private long aa;
   private long ab;
   private long ac;
   private long ad;
   private long ae;
   private long af;
   private long ag;
   private long ah;
   private long ai;
   private int aj;
   private int ak;
   private int al;
   private int am;
   private int an;
   private int ao;
   private static final ThreadMXBean ap = ManagementFactory.getThreadMXBean();
   private long aq;
   private long ar;
   private long as;
   private long at;
   private final boolean au;
   private final m.a[] av = new m.a[64];
   private final List<Integer> aw = new ArrayList<>();
   private final LongArrayFIFOQueue ax = new LongArrayFIFOQueue();
   private final LongOpenHashSet ay = new LongOpenHashSet();
   private static final long az = 25000000L;
   private final boolean[] aA = new boolean[64];
   private final boolean[] aB = new boolean[4];
   private final int aC;
   private final int aD;
   private final int aE;
   private final int[] aF = new int[64];
   private final boolean[] aG = new boolean[64];
   private final boolean[] aH = new boolean[64];
   private boolean aI = true;
   private final ArrayDeque<Integer> aJ = new ArrayDeque<>();
   private final LongOpenHashSet[] aK = new LongOpenHashSet[64];
   private final Long2ObjectOpenHashMap<CompletableFuture<?>> aL = new Long2ObjectOpenHashMap();
   private final Long2IntOpenHashMap aM = new Long2IntOpenHashMap();
   private long aN = Long.MIN_VALUE;
   private final double aO;
   private final int aP;
   private final int aQ;
   private final int aR;
   private final int aS;
   private static final int aT = 4;
   private static final int aU = 6;
   public static final int K = 16;
   public static final int L = 48;
   public static volatile int M;

   public m(ServerPlayer var1, Vec3 var2, long var3, cyou.rimuru.shootingstardemo.mc1211.magic.f var5) {
      super(cyou.rimuru.shootingstardemo.mc1211.star.l.d, var1, var1.position(), var2, var3, false, var5);
      this.aO = a(var5.b());
      this.aP = b(this.aO);
      this.aQ = this.aP / 8;
      this.aR = c(this.aO);
      this.aS = Math.min((int)Math.ceil(((double)this.aP * 0.72 + 32.0) / 16.0), 32);
      this.aC = a(var2.x, this.aP);
      this.aD = a(var2.z, this.aP);
      this.au = R;
      M = 0;
      this.aE = a(Mth.floor(var2.y), this.aO, this.b.getMinBuildHeight());
      this.W = new ChunkPos(BlockPos.containing(var2));
      this.b.getChunkSource().addRegionTicket(V, this.W, this.aS, this.W);
      System.arraycopy(j(), 0, this.aF, 0, this.aF.length);
      this.aM.defaultReturnValue(Integer.MAX_VALUE);
   }

   public static void i() {
   }

   @Override
   public double e() {
      return Math.max(super.e(), (double)this.aP * 0.75 + 400.0);
   }

   public static double a(float var0) {
      return (double)(var0 / 0.4F);
   }

   public static int b(double var0) {
      short var2 = 128;
      int var3 = Math.max(16, (int)Math.round(256.0 * var0 / 8.0) * 8);
      return var3 < var2 ? var3 : (int)Math.round(256.0 * var0 / (double)var2) * var2;
   }

   public static int a(double var0, int var2) {
      return Math.floorDiv(Mth.floor(var0) + 8, 16) * 16 - var2 / 2;
   }

   public static int c(double var0) {
      return b(var0) / 8;
   }

   public static int a(int var0, double var1, int var3) {
      return Math.max(var0 - 1 - c(var1), var3 + 1);
   }

   public static int a(int var0, int var1, int var2, double var3, int var5) {
      int var6 = (4 - a(var1, var2)) * (int)Math.round(20.0 * var3);
      return Math.max(var0 - var6, Math.min(var0, var5 + 4));
   }

   public static int d(double var0) {
      return c(var0) + (int)Math.round(40.0 * var0);
   }

   public static int[] a(long var0, int var2, int var3, double var4, int var6, int var7, int var8) {
      int var9 = var8 - 2 - var6 - var7;
      int var10 = d(var4);
      if (var9 >= var10) {
         return new int[]{var6 + Math.max(Math.min(a(var0, var2, var3, var4), var9), var10), var7};
      } else {
         int var11 = Math.max(var6 + var9, var6 + c(var4) + 20);
         return new int[]{var11, Mth.clamp(var8 - 2 - var11, 0, var7)};
      }
   }

   public static List<int[]> a(long var0, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      ArrayList var11 = new ArrayList();
      if (var6 >= var5) {
         return var11;
      } else {
         byte var12 = 16;
         int var13 = (var4 + var12 - 1) / var12;
         int var14 = (var5 - var6 + var12 - 1) / var12;
         ArrayList<double[]> var15 = new ArrayList<double[]>();

         for (int var16 = 0; var16 < var14; var16++) {
            for (int var17 = 0; var17 < var13; var17++) {
               for (int var18 = 0; var18 < var13; var18++) {
                  var15.add(
                     new double[]{
                        c(var0, var2 * 131 + var17 * 7 + var16 * 977, var3 * 137 + var18 * 13 + var16 * 31), (double)var17, (double)var16, (double)var18
                     }
                  );
               }
            }
         }

         var15.sort((var0x, var1) -> Double.compare(var0x[0], var1[0]));
         double var40 = (double)var7 + (double)var4 * 0.5;
         double var41 = (double)var8 + (double)var4 * 0.5;
         int var20 = var10 - 2 - var12;
         int var21 = Math.max(1, var20 - (var9 + 16));

         for (int var22 = 0; var22 < Math.min(48, var15.size()); var22++) {
            int var23 = (int)((double[])var15.get(var22))[1];
            int var24 = (int)((double[])var15.get(var22))[2];
            int var25 = (int)((double[])var15.get(var22))[3];
            int var26 = var24 * 4099 + var23 * 61 + var25;
            double var27 = (double)(var23 * var12) + (double)var12 * 0.5 - (double)var4 * 0.5;
            double var29 = (double)(var25 * var12) + (double)var12 * 0.5 - (double)var4 * 0.5;
            double var31 = var27 * var27 + var29 * var29 < 1.0 ? c(var0, var26, 1) * Math.PI * 2.0 : Math.atan2(var29, var27);
            double var33 = var31 + (double)(var24 + 1) * 0.9 + (c(var0, var26, 2) - 0.5) * 0.6;
            double var35 = (double)var4 * 0.71 + 10.0 + (double)(var24 * var12) * 0.75 + c(var0, var26, 3) * 14.0;
            int var37 = Mth.floor(var40 + Math.cos(var33) * var35 - (double)var12 * 0.5) + (int)(c(var0, var26, 4) * 11.0) - 5;
            int var38 = Mth.floor(var41 + Math.sin(var33) * var35 - (double)var12 * 0.5) + (int)(c(var0, var26, 5) * 11.0) - 5;
            int var39 = var20 - (int)(((double)(var24 * var12) * 0.8 + c(var0, var26, 6) * (double)var12 * 2.0) % (double)var21);
            var11.add(new int[]{var23 * var12, var6 + var24 * var12, var25 * var12, var37, var39, var38, (int)(c(var0, var26, 7) * 4.0), var24});
         }

         return var11;
      }
   }

   public static double e(double var0) {
      return Mth.clamp(var0, 0.5, 2.0);
   }

   public static int[] j() {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.IllegalStateException: No common supertype for ternary expression
      //   at org.jetbrains.java.decompiler.modules.decompiler.exps.FunctionExprent.getExprType(FunctionExprent.java:224)
      //   at org.jetbrains.java.decompiler.modules.decompiler.exps.FunctionExprent.checkExprTypeBounds(FunctionExprent.java:370)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarTypeProcessor.checkTypeExpr(VarTypeProcessor.java:140)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarTypeProcessor.checkTypeExprent(VarTypeProcessor.java:121)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarTypeProcessor.lambda$processVarTypes$2(VarTypeProcessor.java:114)
      //   at org.jetbrains.java.decompiler.modules.decompiler.flow.DirectGraph.iterateExprents(DirectGraph.java:107)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarTypeProcessor.processVarTypes(VarTypeProcessor.java:114)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarTypeProcessor.calculateVarTypes(VarTypeProcessor.java:44)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarVersionsProcessor.setVarVersions(VarVersionsProcessor.java:68)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarProcessor.setVarVersions(VarProcessor.java:47)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:227)
      //
      // Bytecode:
      // 00: bipush 64
      // 02: newarray 10
      // 04: astore 0
      // 05: bipush 4
      // 06: newarray 10
      // 08: astore 1
      // 09: bipush 0
      // 0a: istore 2
      // 0b: iload 2
      // 0c: bipush 8
      // 0e: if_icmpge 58
      // 11: bipush 0
      // 12: istore 3
      // 13: iload 3
      // 14: bipush 8
      // 16: if_icmpge 52
      // 19: iload 3
      // 1a: iload 2
      // 1b: invokestatic cyou/rimuru/shootingstardemo/mc1211/star/m.a (II)I
      // 1e: istore 4
      // 20: aload 1
      // 21: iload 4
      // 23: dup2
      // 24: iaload
      // 25: dup_x2
      // 26: bipush 1
      // 27: iadd
      // 28: iastore
      // 29: istore 5
      // 2b: aload 0
      // 2c: iload 2
      // 2d: bipush 8
      // 2f: imul
      // 30: iload 3
      // 31: iadd
      // 32: sipush 474
      // 35: iload 4
      // 37: bipush 20
      // 39: imul
      // 3a: iadd
      // 3b: iload 4
      // 3d: bipush 2
      // 3e: if_icmpge 46
      // 41: iload 5
      // 43: goto 4a
      // 46: iload 5
      // 48: bipush 2
      // 49: idiv
      // 4a: iadd
      // 4b: iastore
      // 4c: iinc 3 1
      // 4f: goto 13
      // 52: iinc 2 1
      // 55: goto 0b
      // 58: aload 0
      // 59: areturn
      int[] var0 = new int[64];
      int[] var1 = new int[4];
      for (int var2 = 0; var2 < 8; var2++) {
         for (int var3 = 0; var3 < 8; var3++) {
            int var4 = a(var3, var2);
            int var5 = var1[var4]++;
            var0[var2 * 8 + var3] = 474 + var4 * 20 + (var4 < 2 ? var5 : var5 / 2);
         }
      }
      return var0;
   }

   public static int a(int var0, int var1) {
      double var2 = 3.5;
      return (int)Math.floor(Math.max(Math.abs((double)var0 - var2), Math.abs((double)var1 - var2)));
   }

   public static int a(long var0, int var2, int var3) {
      double var4 = c(var0, var2 * 7 + 3, var3 * 13 + 5);
      return var4 < 0.42 ? 0 : (var4 < 0.62 ? 1 : (var4 < 0.8 ? 2 : 3));
   }

   public static int b(long var0, int var2, int var3) {
      return a(var0, var2, var3, 1.0);
   }

   public static int a(long var0, int var2, int var3, double var4) {
      return (int)Math.round((double)(90 + (int)(c(var0, var3 * 5 - 1, var2 * 11 + 9) * 160.0)) * var4);
   }

   static double c(long var0, int var2, int var3) {
      long var4 = var0 ^ (long)var2 * -7046029254386353131L ^ (long)var3 * -4417276706812531889L;
      var4 = (var4 ^ var4 >>> 31) * -4658895280553007687L;
      var4 ^= var4 >>> 29;
      return (double)(var4 >>> 11) / 9.007199E15F;
   }

   @Override
   protected void a(int var1) {
      if (this.i != null && var1 % 10 == 0) {
         cyou.rimuru.shootingstardemo.mc1211.spell.d.a(this.i);
      }

      if (var1 == 462) {
         this.l();
      }

      int var2 = this.au ? 30 : 0;

      for (int var3 = 0; var3 < this.aF.length; var3++) {
         if (!this.aH[var3] && var1 >= this.aF[var3] - var2) {
            this.aH[var3] = true;
            this.aJ.add(var3);
         }
      }

      long var6 = this.b.getGameTime();
      if (var6 != this.aN) {
         this.aN = var6;
         this.c(var1);
      }

      for (int var5 = 0; var5 < 4; var5++) {
         if (var1 == 474 + var5 * 20 + 8) {
            this.h(var5);
         }
      }

      if (var1 == 562) {
         cyou.rimuru.shootingstardemo.mc1211.star.a.a(this.b, this.n());
      }

      if (this.aI && var1 > 582 && this.aL.isEmpty() && this.ay.isEmpty()) {
         this.o();
      }
   }

   @Override
   protected boolean b() {
      return super.b() && (this.aJ.isEmpty() && this.aw.isEmpty() && this.ay.isEmpty() && this.aL.isEmpty() || this.g() > 6682);
   }

   private void c(int var1) {
      this.ao = var1;
      long var2 = System.nanoTime();
      this.ai = 0L;
      this.aL.long2ObjectEntrySet().removeIf(var0 -> ((CompletableFuture)var0.getValue()).isDone());
      if (this.au) {
         Iterator var4 = this.aw.iterator();

         while (var4.hasNext()) {
            int var5 = (Integer)var4.next();
            m.a var6 = this.av[var5];
            if (var1 >= this.aF[var5] && var6.d().isDone()) {
               if (System.nanoTime() - var2 > 20000000L) {
                  break;
               }

               var4.remove();
               this.av[var5] = null;
               this.a(var5, var6);
            } else {
               this.am = this.am + (var1 >= this.aF[var5] ? 1 : 0);
            }
         }
      }

      long var13 = System.nanoTime();

      while (!this.ax.isEmpty() && System.nanoTime() - var13 < 25000000L) {
         long var14 = this.ax.dequeueLong();
         this.ay.remove(var14);
         LevelChunk var8 = this.b.getChunkSource().getChunkNow(ChunkPos.getX(var14), ChunkPos.getZ(var14));
         if (var8 != null) {
            long var9 = System.nanoTime();
            cyou.rimuru.shootingstardemo.mc1211.star.a.a(this.b, var8);
            this.ab = this.ab + (System.nanoTime() - var9);
            this.a(new long[]{var14});
         }
      }

      long var15 = 0L;
      int var16 = 0;
      Iterator var17 = this.aJ.iterator();

      while (var17.hasNext() && var16 < 3) {
         int var10 = (Integer)var17.next();
         var16++;
         if (this.au && !this.aA[var10] ? System.nanoTime() - var2 <= 20000000L : var15 < 1500000L) {
            long var11 = this.au && !this.aA[var10] ? this.g(var10) : this.d(var10);
            if (var11 >= 0L) {
               var17.remove();
               var15 += var11;
               var16--;
            }
         }
      }

      for (int var18 = 0; var18 < this.aK.length; var18++) {
         if (this.aK[var18] != null && this.aG[var18] && this.b(this.aK[var18])) {
            this.f(var18);
         }
      }

      this.af = Math.max(this.af, System.nanoTime() - var2 - this.ai);
   }

   private long d(int var1) {
      int var2 = var1 % 8;
      int var3 = var1 / 8;
      int var4 = this.aC + var2 * this.aQ;
      int var5 = this.aD + var3 * this.aQ;
      LongOpenHashSet var6 = new LongOpenHashSet();
      a(var4, var5, this.aQ, var6);
      if (!this.a(var1, var6)) {
         return -1L;
      } else {
         m.b var7 = this.c(var2, var3);
         if (var7 == null) {
            this.e(var1);
            return 0L;
         } else {
            for (int[] var9 : var7.f()) {
               a(var9[3], var9[5], 16, var6);
            }

            if (!this.a(var1, var6)) {
               return -1L;
            } else if (this.a(var6)) {
               this.al++;
               return -1L;
            } else {
               long[] var10 = this.a(var2, var3, var7);
               this.a(var10);
               this.e(var1);
               return (long)this.aQ * (long)this.aQ * (long)var7.a();
            }
         }
      }
   }

   private void a(long[] var1) {
      ThreadedLevelLightEngine var2 = this.b.getChunkSource().getLightEngine();

      for (long var6 : var1) {
         this.aL.put(var6, var2.waitForPendingTasks(ChunkPos.getX(var6), ChunkPos.getZ(var6)));
      }
   }

   private void e(int var1) {
      this.aG[var1] = true;
      this.an = Math.max(this.an, this.ao - this.aF[var1]);
      long var2 = System.nanoTime();
      if (this.aq == 0L) {
         this.aq = var2;
      }

      this.ar = var2;
   }

   private static void a(int var0, int var1, int var2, LongOpenHashSet var3) {
      for (int var4 = var0 >> 4; var4 <= var0 + var2 - 1 >> 4; var4++) {
         for (int var5 = var1 >> 4; var5 <= var1 + var2 - 1 >> 4; var5++) {
            var3.add(ChunkPos.asLong(var4, var5));
         }
      }
   }

   private boolean a(int var1, LongOpenHashSet var2) {
      LongIterator var3 = var2.iterator();

      while (var3.hasNext()) {
         long var4 = (Long)var3.next();
         if (this.b.getChunkSource().getChunkNow(ChunkPos.getX(var4), ChunkPos.getZ(var4)) == null) {
            this.b(var1, var2);
            this.aj++;
            return false;
         }
      }

      boolean var6 = this.b(var2);
      this.ak += var6 ? 0 : 1;
      return var6;
   }

   private void b(int var1, LongOpenHashSet var2) {
      if (this.aK[var1] == null) {
         this.aK[var1] = new LongOpenHashSet();
      }

      LongIterator var3 = var2.iterator();

      while (var3.hasNext()) {
         long var4 = (Long)var3.next();
         if (this.aK[var1].add(var4)) {
            this.b.getChunkSource().addRegionTicket(V, new ChunkPos(var4), 1, new ChunkPos(var4));
         }
      }
   }

   private boolean a(LongOpenHashSet var1) {
      for (int var3 : this.aw) {
         LongOpenHashSet var4 = this.av[var3].e();
         LongIterator var5 = var1.iterator();

         while (var5.hasNext()) {
            long var6 = (Long)var5.next();
            if (var4.contains(var6)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean b(LongOpenHashSet var1) {
      LongIterator var2 = var1.iterator();

      while (var2.hasNext()) {
         long var3 = (Long)var2.next();
         if (this.ay.contains(var3)) {
            return false;
         }

         CompletableFuture var5 = (CompletableFuture)this.aL.get(var3);
         if (var5 != null && !var5.isDone()) {
            return false;
         }
      }

      return true;
   }

   private void f(int var1) {
      LongIterator var2 = this.aK[var1].iterator();

      while (var2.hasNext()) {
         long var3 = (Long)var2.next();
         this.b.getChunkSource().removeRegionTicket(V, new ChunkPos(var3), 1, new ChunkPos(var3));
      }

      this.aK[var1] = null;
   }

   private m.b c(int var1, int var2) {
      int var3 = this.aC + var1 * this.aQ;
      int var4 = this.aD + var2 * this.aQ;
      int var5 = this.aE;
      int var6 = Math.max(4, this.aQ / 16);
      int var7 = this.b.dimensionType().hasCeiling() ? this.b.getMinBuildHeight() + this.b.dimensionType().logicalHeight() - 6 : Integer.MAX_VALUE;

      for (int var8 = var3; var8 < var3 + this.aQ; var8 += var6) {
         for (int var9 = var4; var9 < var4 + this.aQ; var9 += var6) {
            int var10 = Math.min(this.b.getHeight(Types.MOTION_BLOCKING, var8, var9) + 1, var7);
            int var11 = this.aM.get(d(var8, var9));
            if (var10 > var11) {
               var10 = this.b(var8, var9, var11);
            }

            var5 = Math.max(var5, var10);
         }
      }

      int var13 = Math.min(var5 - this.aE, this.b.getMaxBuildHeight() - 1 - this.aE);
      if (var13 <= 1) {
         return null;
      } else {
         int var14 = a(this.f, var1, var2);
         if (var14 == 3 && var13 > this.aQ + (int)Math.round(16.0 * this.aO)) {
            var14 = 2;
         }

         int var15 = var14 == 3 ? this.aQ : var13;
         int[] var16 = a(this.f, var1, var2, this.aO, this.aE, var15, this.b.getMaxBuildHeight() - 1);
         List var12 = a(this.f, var1, var2, this.aQ, var15, var16[1], var3, var4, this.aE + this.aR, this.b.getMaxBuildHeight() - 1);
         return new m.b(var13, var14, var15, var16[0], var16[1], var12);
      }
   }

   private int b(int var1, int var2, int var3) {
      MutableBlockPos var4 = new MutableBlockPos();

      for (int var5 = var3 - 1; var5 > this.aE; var5--) {
         if (!this.b.getBlockState(var4.set(var1, var5, var2)).isAir()) {
            return var5 + 1;
         }
      }

      return this.aE;
   }

   private static long d(int var0, int var1) {
      return (long)var0 << 32 | (long)var1 & 4294967295L;
   }

   private long g(int var1) {
      int var2 = var1 % 8;
      int var3 = var1 / 8;
      int var4 = this.aC + var2 * this.aQ;
      int var5 = this.aD + var3 * this.aQ;
      LongOpenHashSet var6 = new LongOpenHashSet();
      a(var4, var5, this.aQ, var6);
      if (!this.a(var1, var6)) {
         return -1L;
      } else {
         m.b var7 = this.c(var2, var3);
         if (var7 == null) {
            this.e(var1);
            return 0L;
         } else {
            for (int[] var9 : var7.f()) {
               a(var9[3], var9[5], 16, var6);
            }

            if (!this.a(var1, var6)) {
               return -1L;
            } else if (this.a(var6)) {
               this.al++;
               return -1L;
            } else {
               long var23 = System.nanoTime();
               int var10 = a(this.aE, var2, var3, this.aO, this.b.getMinBuildHeight());
               int var11 = var7.a();
               Long2ObjectOpenHashMap var12 = new Long2ObjectOpenHashMap();
               int var13 = Math.max(this.aE + var11 - 1, var7.d() + var7.e() - 1);
               LongOpenHashSet var14 = new LongOpenHashSet();
               a(var4, var5, this.aQ, var14);
               LongIterator var15 = var14.iterator();

               while (var15.hasNext()) {
                  long var16 = (Long)var15.next();
                  a(var12, var16, var10, var13);
               }

               for (int[] var26 : var7.f()) {
                  LongOpenHashSet var17 = new LongOpenHashSet();
                  a(var26[3], var26[5], 16, var17);
                  LongIterator var18 = var17.iterator();

                  while (var18.hasNext()) {
                     long var19 = (Long)var18.next();
                     a(var12, var19, var26[4], var26[4] + 16 - 1);
                  }
               }

               g var25 = cyou.rimuru.shootingstardemo.mc1211.star.g.a(this.b, var12, var4, var5, this.aQ, var10 + 1, this.aE + var11 - 1);
               Long2ObjectOpenHashMap var27 = null;
               int var28 = a(var2, var3);
               long var29 = System.nanoTime();
               if (S && !this.aB[var28]) {
                  this.aB[var28] = true;
                  Long2ObjectOpenHashMap var20 = new Long2ObjectOpenHashMap();
                  int var21 = this.b.getMinBuildHeight();
                  int var22 = this.b.getMaxBuildHeight() - 1;
                  this.a(var2, var3, var7, var10, this.k(), (var3x, var4x, var5x, var6x) -> {
                     if (var4x >= var21 && var4x <= var22) {
                        var20.put(BlockPos.asLong(var3x, var4x, var5x), var6x);
                     }
                  }, new long[4]);
                  var27 = var20;
               }

               long var30 = System.nanoTime() - var29;
               this.ah += var30;
               this.ai += var30;
               this.b(var1, var6);
               CompletableFuture var31 = CompletableFuture.supplyAsync(() -> {
                  long[] var6x = new long[5];
                  long var7x = ap.getCurrentThreadCpuTime();
                  var25.a();
                  this.a(var2, var3, var7, var10, var25, var25, var6x);
                  var6x[4] = ap.getCurrentThreadCpuTime() - var7x;
                  return var6x;
               }, U);
               this.av[var1] = new m.a(var7, var10, var25, var31, var6, var27);
               this.aw.add(var1);
               this.ad = this.ad + (System.nanoTime() - var23 - var30);
               return 0L;
            }
         }
      }
   }

   private static void a(Long2ObjectOpenHashMap<int[]> var0, long var1, int var3, int var4) {
      int[] var5 = (int[])var0.get(var1);
      if (var5 == null) {
         var0.put(var1, new int[]{var3, var4});
      } else {
         var5[0] = Math.min(var5[0], var3);
         var5[1] = Math.max(var5[1], var4);
      }
   }

   private void a(int var1, m.a var2) {
      int var3 = var1 % 8;
      int var4 = var1 / 8;

      long[] var5;
      try {
         var5 = var2.d().join();
      } catch (RuntimeException var17) {
         ShootingStarDemo.LOGGER.c("[carve-timer] Tesseract: cell {} failed to build, carving it block by block", var1, var17);
         var2.c().b(this.b);
         this.aA[var1] = true;
         this.aJ.addFirst(var1);
         return;
      }

      if (var2.c().a(this.b)) {
         ShootingStarDemo.LOGGER.a("[carve-timer] Tesseract: cell {}'s land changed while it was built, carving it block by block", var1);
         var2.c().b(this.b);
         this.aA[var1] = true;
         this.aJ.addFirst(var1);
      } else {
         long var6 = System.nanoTime();
         this.X = this.X + var5[0];
         this.Y = this.Y + var5[1];
         this.Z = this.Z + var5[2];
         this.aa = this.aa + var5[3];
         this.ag = this.ag + var5[4];
         long var8 = System.nanoTime();
         if (var2.f() != null) {
            int var10 = 0;
            ObjectIterator var11 = var2.f().long2ObjectEntrySet().iterator();

            while (var11.hasNext()) {
               Entry var12 = (Entry)var11.next();
               long var13 = var12.getLongKey();
               if (var2.c().b(BlockPos.getX(var13), BlockPos.getY(var13), BlockPos.getZ(var13)) != var12.getValue()) {
                  var10++;
               }
            }

            this.as = this.as + (long)var2.f().size();
            this.at += (long)var10;
            ShootingStarDemo.LOGGER.a("[carve-timer] Tesseract verify: cell {} ({}, {}), {} of {} blocks differ", var1, var3, var4, var10, var2.f().size());
         }

         long var18 = System.nanoTime() - var8;
         this.ah += var18;
         this.ai += var18;
         if (var2.c().b() > 0) {
            ShootingStarDemo.LOGGER.b("[carve-timer] Tesseract: cell {} reached {} blocks outside what it took up", var1, var2.c().b());
         }

         for (long var15 : var2.c().c(this.b)) {
            if (this.ay.add(var15)) {
               this.ax.enqueue(var15);
            }
         }

         this.a(var2.a().f());
         this.c(this.aC + var3 * this.aQ, this.aD + var4 * this.aQ, var2.b());
         this.e(var1);
         long var20 = System.nanoTime() - var6 - var18;
         this.ae += var20;
         this.ac = Math.max(this.ac, var20);
      }
   }

   private m.c k() {
      return new m.c() {
         private long b = Long.MIN_VALUE;
         private LevelChunk c;

         @Override
         public BlockState a(int var1, int var2, int var3) {
            long var4 = ChunkPos.asLong(var1 >> 4, var3 >> 4);
            if (var4 != this.b) {
               this.b = var4;
               this.c = m.this.b.getChunkSource().getChunkNow(var1 >> 4, var3 >> 4);
            }

            return this.c.getSection(this.c.getSectionIndex(var2)).getBlockState(var1 & 15, var2 & 15, var3 & 15);
         }
      };
   }

   private long[] a(int var1, int var2, m.b var3) {
      long var4 = System.nanoTime();
      int var6 = a(this.aE, var1, var2, this.aO, this.b.getMinBuildHeight());
      h var7 = new h(this.b);
      long[] var8 = new long[4];
      this.a(var1, var2, var3, var6, this.k(), var7::a, var8);
      this.a(var3.f());
      long var9 = System.nanoTime();
      long[] var11 = var7.b();
      long var12 = System.nanoTime();
      this.X = this.X + var8[0];
      this.Y = this.Y + var8[1];
      this.Z = this.Z + var8[2];
      this.aa = this.aa + var8[3];
      this.ab += var12 - var9;
      this.ac = Math.max(this.ac, var12 - var4);
      this.c(this.aC + var1 * this.aQ, this.aD + var2 * this.aQ, var6);
      return var11;
   }

   private void a(int var1, int var2, m.b var3, int var4, m.c var5, m.d var6, long[] var7) {
      long var8 = System.nanoTime();
      int var10 = this.aC + var1 * this.aQ;
      int var11 = this.aD + var2 * this.aQ;
      int var12 = var3.a();
      char[] var13 = new char[this.aQ * var12 * this.aQ];
      ArrayList var14 = new ArrayList();
      var14.add(null);
      Object2IntOpenHashMap var15 = new Object2IntOpenHashMap();

      for (int var16 = 0; var16 < this.aQ; var16++) {
         for (int var17 = 0; var17 < this.aQ; var17++) {
            for (int var18 = 0; var18 < var12; var18++) {
               BlockState var19 = var5.a(var10 + var16, this.aE + var18, var11 + var17);
               if (!var19.isAir() && !var19.hasBlockEntity() && !var19.is(Blocks.BEDROCK)) {
                  int var20 = var15.getInt(var19);
                  if (var20 == 0) {
                     var20 = var14.size();
                     var14.add(var19);
                     var15.put(var19, var20);
                  }

                  var13[(var16 * var12 + var18) * this.aQ + var17] = (char)var20;
               }
            }
         }
      }

      long var44 = System.nanoTime();
      int var45 = this.b.getMinBuildHeight() + 4;
      BlockState var46 = Blocks.AIR.defaultBlockState();

      for (int var47 = 0; var47 < this.aQ; var47++) {
         for (int var21 = 0; var21 < this.aQ; var21++) {
            int var22 = var10 + var47;
            int var23 = var11 + var21;

            for (int var24 = var4 + 1; var24 <= Math.min(var45, this.aE + var12 - 1); var24++) {
               if (!var5.a(var22, var24, var23).is(Blocks.BEDROCK)) {
                  var6.put(var22, var24, var23, var46);
               }
            }

            var6.a(var22, var23, Math.max(var4 + 1, var45 + 1), this.aE + var12 - 1);
            if (!var5.a(var22, var4, var23).is(Blocks.BEDROCK)) {
               boolean var51 = var47 == 0 || var21 == 0 || var47 == this.aQ - 1 || var21 == this.aQ - 1;
               double var25 = c(this.f, var22, var23);
               var6.put(
                  var22,
                  var4,
                  var23,
                  var51
                     ? a(var1 * this.aQ + var47, var2 * this.aQ + var21, this.aP)
                     : (
                        var25 < 0.4
                           ? Blocks.SMOOTH_BASALT.defaultBlockState()
                           : (
                              var25 < 0.7
                                 ? Blocks.CALCITE.defaultBlockState()
                                 : (var25 < 0.9 ? Blocks.TUFF.defaultBlockState() : Blocks.TINTED_GLASS.defaultBlockState())
                           )
                     )
               );
            }
         }
      }

      long var48 = System.nanoTime();
      int var49 = var3.b();
      int var50 = var3.d();
      int var52 = var3.e();
      BlockState[] var53 = new BlockState[var14.size()];

      for (int var26 = 1; var26 < var53.length; var26++) {
         BlockState var27 = a((BlockState)var14.get(var26));

         var53[var26] = switch (var49) {
            case 1 -> var27.rotate(Rotation.CLOCKWISE_90);
            case 2 -> b(var27);
            case 3 -> c(var27);
            default -> var27;
         };
      }

      HashMap var54 = new HashMap();
      List<int[]> var55 = var3.f();

      for (int[] var29 : var55) {
         var54.put(d(var29[0], var29[1] - var52, var29[2]), var29);
      }

      BlockState[][] var56 = new BlockState[4][];
      int var57 = Math.min(var12, this.aQ);
      int var30 = var49 == 3 ? (this.aQ - var57) / 2 : 0;

      for (int var31 = 0; var31 < this.aQ; var31++) {
         for (int var32 = 0; var32 < var12; var32++) {
            for (int var33 = 0; var33 < this.aQ; var33++) {
               char var34 = var13[(var31 * var12 + var32) * this.aQ + var33];
               if (var34 != 0 && (var49 != 3 || var32 >= var12 - var57)) {
                  int var35;
                  int var36;
                  int var37;
                  switch (var49) {
                     case 1:
                        var35 = this.aQ - 1 - var33;
                        var36 = var32;
                        var37 = var31;
                        break;
                     case 2:
                        var35 = var31;
                        var36 = var12 - 1 - var32;
                        var37 = this.aQ - 1 - var33;
                        break;
                     case 3:
                        var35 = var31;
                        var36 = var33;
                        var37 = var12 - 1 - var32 + var30;
                        break;
                     default:
                        var35 = var31;
                        var36 = var32;
                        var37 = var33;
                  }

                  if (var36 < var52) {
                     var6.put(var10 + var35, var50 + var36, var11 + var37, var53[var34]);
                  } else {
                     int[] var38 = (int[])var54.get(d(var35, var36 - var52, var37));
                     if (var38 != null) {
                        int var39 = var35 - var38[0];
                        int var40 = var36 - var38[1];
                        int var41 = var37 - var38[2];
                        byte var42 = 15;
                        BlockState[] var43 = var56[var38[6]];
                        if (var43 == null) {
                           var43 = var56[var38[6]] = a(var53, var38[6]);
                        }

                        switch (var38[6]) {
                           case 1:
                              var6.put(var38[3] + var42 - var41, var38[4] + var40, var38[5] + var39, var43[var34]);
                              break;
                           case 2:
                              var6.put(var38[3] + var39, var38[4] + var42 - var40, var38[5] + var42 - var41, var43[var34]);
                              break;
                           case 3:
                              var6.put(var38[3] + var39, var38[4] + var41, var38[5] + var42 - var40, var43[var34]);
                              break;
                           default:
                              var6.put(var38[3] + var39, var38[4] + var40, var38[5] + var41, var43[var34]);
                        }
                     }
                  }
               }
            }
         }
      }

      long var58 = System.nanoTime();

      for (int[] var61 : var55) {
         BlockState var62 = cyou.rimuru.shootingstardemo.mc1211.registry.b.a.a(Math.floorMod(var61[7] + var1 + var2, 7));

         for (int var63 = 0; var63 < 16; var63++) {
            for (int var64 = 0; var64 < 4; var64++) {
               int var65 = (var64 & 1) == 0 ? 0 : 15;
               int var66 = (var64 & 2) == 0 ? 0 : 15;
               var6.put(var61[3] + var63, var61[4] + var65, var61[5] + var66, var62);
               var6.put(var61[3] + var65, var61[4] + var63, var61[5] + var66, var62);
               var6.put(var61[3] + var65, var61[4] + var66, var61[5] + var63, var62);
            }
         }
      }

      long var60 = System.nanoTime();
      var7[0] += var44 - var8;
      var7[1] += var48 - var44;
      var7[2] += var58 - var48;
      var7[3] += var60 - var58;
   }

   private static BlockState[] a(BlockState[] var0, int var1) {
      BlockState[] var2 = new BlockState[var0.length];

      for (int var3 = 1; var3 < var2.length; var3++) {
         BlockState var4 = var0[var3];

         var2[var3] = switch (var1) {
            case 1 -> var4.rotate(Rotation.CLOCKWISE_90);
            case 2 -> b(var4);
            case 3 -> c(var4);
            default -> var4;
         };
      }

      return var2;
   }

   private void a(List<int[]> var1) {
      M = M + var1.size();

      for (int[] var3 : var1) {
         for (int var4 = var3[3]; var4 < var3[3] + 16; var4++) {
            for (int var5 = var3[5]; var5 < var3[5] + 16; var5++) {
               long var6 = d(var4, var5);
               this.aM.put(var6, Math.min(this.aM.get(var6), var3[4]));
            }
         }
      }
   }

   private void c(int var1, int var2, int var3) {
      for (Entity var5 : this.b
         .getEntities(
            (Entity)null,
            new AABB(
               (double)var1, (double)(var3 - 4), (double)var2, (double)(var1 + this.aQ), (double)(this.b.getMaxBuildHeight() - 1), (double)(var2 + this.aQ)
            ),
            var0 -> !(var0 instanceof LivingEntity)
         )) {
         if (cyou.rimuru.shootingstardemo.mc1211.star.k.a(var5, this.i)) {
            cyou.rimuru.shootingstardemo.mc1211.star.k.a(this.b, var5, this.i, this.h.c());
         }
      }
   }

   private static long d(int var0, int var1, int var2) {
      return ((long)(var0 / 16) * 65536L + (long)(var1 / 16)) * 65536L + (long)(var2 / 16);
   }

   static BlockState b(int var0, int var1) {
      return a(var0, var1, 256);
   }

   static BlockState a(int var0, int var1, int var2) {
      return cyou.rimuru.shootingstardemo.mc1211.registry.b.a.a(Math.min((var0 + var1) * 7 / (2 * var2), 6));
   }

   public static BlockState a(BlockState var0) {
      if (!var0.getFluidState().isEmpty()) {
         if (!var0.hasProperty(BlockStateProperties.WATERLOGGED)) {
            return var0.getFluidState().is(FluidTags.LAVA) ? Blocks.MAGMA_BLOCK.defaultBlockState() : Blocks.PACKED_ICE.defaultBlockState();
         }

         var0 = (BlockState)var0.setValue(BlockStateProperties.WATERLOGGED, false);
      }

      Block var1 = var0.getBlock();
      if (var1 == Blocks.SAND) {
         return Blocks.SANDSTONE.defaultBlockState();
      } else if (var1 == Blocks.RED_SAND) {
         return Blocks.RED_SANDSTONE.defaultBlockState();
      } else if (var1 == Blocks.GRAVEL) {
         return Blocks.ANDESITE.defaultBlockState();
      } else if (var1 == Blocks.POINTED_DRIPSTONE) {
         return Blocks.DRIPSTONE_BLOCK.defaultBlockState();
      } else if (var1 instanceof ConcretePowderBlock) {
         ResourceLocation var2 = BuiltInRegistries.BLOCK.getKey(var1);
         ResourceLocation var3 = ResourceLocation.fromNamespaceAndPath(var2.getNamespace(), var2.getPath().replace("_concrete_powder", "_concrete"));
         return BuiltInRegistries.BLOCK.getOptional(var3).<BlockState>map(Block::defaultBlockState).orElse(Blocks.TUFF.defaultBlockState());
      } else if (var0.is(BlockTags.ANVIL)) {
         return Blocks.IRON_BLOCK.defaultBlockState();
      } else {
         return var1 instanceof FallingBlock && var1 != Blocks.DRAGON_EGG ? Blocks.TUFF.defaultBlockState() : var0;
      }
   }

   public static BlockState b(BlockState var0) {
      BlockState var1 = var0.mirror(Mirror.LEFT_RIGHT);
      if (var1.hasProperty(BlockStateProperties.HALF)) {
         var1 = (BlockState)var1.setValue(BlockStateProperties.HALF, var1.getValue(BlockStateProperties.HALF) == Half.TOP ? Half.BOTTOM : Half.TOP);
      }

      if (var1.hasProperty(BlockStateProperties.SLAB_TYPE)) {
         SlabType var2 = (SlabType)var1.getValue(BlockStateProperties.SLAB_TYPE);
         var1 = (BlockState)var1.setValue(
            BlockStateProperties.SLAB_TYPE, var2 == SlabType.TOP ? SlabType.BOTTOM : (var2 == SlabType.BOTTOM ? SlabType.TOP : var2)
         );
      }

      if (var1.hasProperty(BlockStateProperties.FACING)) {
         Direction var3 = (Direction)var1.getValue(BlockStateProperties.FACING);
         if (var3.getAxis() == Axis.Y) {
            var1 = (BlockState)var1.setValue(BlockStateProperties.FACING, var3.getOpposite());
         }
      }

      return var1;
   }

   private static BlockState c(BlockState var0) {
      BlockState var1 = var0;
      if (var0.hasProperty(BlockStateProperties.AXIS)) {
         Axis var2 = (Axis)var0.getValue(BlockStateProperties.AXIS);
         var1 = (BlockState)var0.setValue(BlockStateProperties.AXIS, var2 == Axis.Y ? Axis.Z : (var2 == Axis.Z ? Axis.Y : var2));
      }

      if (var1.hasProperty(BlockStateProperties.FACING)) {
         Direction var4 = (Direction)var1.getValue(BlockStateProperties.FACING);

         Direction var3 = switch (var4) {
            case UP -> Direction.NORTH;
            case DOWN -> Direction.SOUTH;
            case SOUTH -> Direction.UP;
            case NORTH -> Direction.DOWN;
            default -> var4;
         };
         var1 = (BlockState)var1.setValue(BlockStateProperties.FACING, var3);
      }

      return var1;
   }

   private void l() {
      for (Entity var2 : this.b
         .getEntities((Entity)null, this.m(), var1 -> var1 instanceof LivingEntity && cyou.rimuru.shootingstardemo.mc1211.spell.d.a(var1, this.i))) {
         double var3 = (1.6 + c(this.f, var2.getId(), 3) * 1.2) * Math.max(1.0, Math.sqrt(this.aO));
         var2.setDeltaMovement(var2.getDeltaMovement().add(0.0, var3, 0.0));
         var2.hurtMarked = true;
      }
   }

   private void h(int var1) {
      for (Entity var3 : this.b.getEntities((Entity)null, this.m())) {
         int var4 = Mth.floor((var3.getX() - (double)this.aC) / (double)this.aQ);
         int var5 = Mth.floor((var3.getZ() - (double)this.aD) / (double)this.aQ);
         if (var4 >= 0 && var5 >= 0 && var4 < 8 && var5 < 8 && a(var4, var5) <= var1 && cyou.rimuru.shootingstardemo.mc1211.star.k.a(var3, this.i)) {
            cyou.rimuru.shootingstardemo.mc1211.star.k.a(this.b, var3, this.i, this.h.c());
         }
      }
   }

   private AABB m() {
      return new AABB(
         (double)this.aC,
         (double)(this.b.getMinBuildHeight() - 16),
         (double)this.aD,
         (double)(this.aC + this.aP),
         (double)(this.b.getMaxBuildHeight() - 1 + 64),
         (double)(this.aD + this.aP)
      );
   }

   private Map<BlockPos, BlockState> n() {
      HashMap var1 = new HashMap();
      int var2 = this.aC + this.aP / 2;
      int var3 = this.aD + this.aP / 2;
      int var4 = Math.min(this.aE + this.aR + 150, this.b.getMaxBuildHeight() - 1 - 30);
      int var5 = (int)Math.round(13.0 * e(this.aO));
      int var6 = (int)Math.round(6.0 * e(this.aO));
      ArrayList var7 = new ArrayList();
      ArrayList var8 = new ArrayList();

      for (int var9 = 0; var9 < 8; var9++) {
         int var10 = (var9 & 1) == 0 ? -1 : 1;
         int var11 = (var9 & 2) == 0 ? -1 : 1;
         int var12 = (var9 & 4) == 0 ? -1 : 1;
         var7.add(new int[]{var2 + var10 * var5, var4 + var11 * var5, var3 + var12 * var5});
         var8.add(new int[]{var2 + var10 * var6, var4 + var11 * var6, var3 + var12 * var6});
      }

      for (int var13 = 0; var13 < 8; var13++) {
         for (int var14 = var13 + 1; var14 < 8; var14++) {
            if (Integer.bitCount(var13 ^ var14) == 1) {
               a(var1, (int[])var7.get(var13), (int[])var7.get(var14), var4, var5);
               a(var1, (int[])var8.get(var13), (int[])var8.get(var14), var4, var5);
            }
         }

         a(var1, (int[])var7.get(var13), (int[])var8.get(var13), var4, var5);
      }

      return var1;
   }

   private static void a(Map<BlockPos, BlockState> var0, int[] var1, int[] var2, int var3, int var4) {
      int var5 = Math.max(Math.max(Math.abs(var2[0] - var1[0]), Math.abs(var2[1] - var1[1])), Math.abs(var2[2] - var1[2]));

      for (int var6 = 0; var6 <= var5; var6++) {
         double var7 = var5 == 0 ? 0.0 : (double)var6 / (double)var5;
         int var9 = (int)Math.round(Mth.lerp(var7, (double)var1[1], (double)var2[1]));
         var0.put(
            new BlockPos(
               (int)Math.round(Mth.lerp(var7, (double)var1[0], (double)var2[0])), var9, (int)Math.round(Mth.lerp(var7, (double)var1[2], (double)var2[2]))
            ),
            cyou.rimuru.shootingstardemo.mc1211.registry.b.a.a(Mth.clamp((var9 - var3 + var4) * 7 / (2 * var4 + 1), 0, 6))
         );
      }
   }

   private void o() {
      if (this.aI) {
         this.aI = false;
         this.b.getChunkSource().removeRegionTicket(V, this.W, this.aS, this.W);
      }
   }

   @Override
   protected void f() {
      long var1 = 1000000L;
      if (this.au) {
         ShootingStarDemo.LOGGER
            .a(
               "[carve-timer] Tesseract (fast, {} builders): server thread snapshot {} ms, apply {} ms (relight {} ms), worst cell {} ms, worst tick {} ms; builders read {} ms, clear {} ms, turn {} ms, frame {} ms (wall, {} ms CPU); first to last cell {} ms{}",
               T,
               this.ad / var1,
               this.ae / var1,
               this.ab / var1,
               this.ac / var1,
               this.af / var1,
               this.X / var1,
               this.Y / var1,
               this.Z / var1,
               this.aa / var1,
               this.ag / var1,
               (this.ar - this.aq) / var1,
               this.as > 0L ? ", verify " + this.at + " of " + this.as + " blocks differ (" + this.ah / var1 + " ms, left out)" : ""
            );
      } else {
         ShootingStarDemo.LOGGER
            .a(
               "[carve-timer] Tesseract (block by block): read {} ms, clear {} ms, turn {} ms, frame {} ms, flush {} ms, worst cell {} ms, worst tick {} ms, first to last cell {} ms",
               this.X / var1,
               this.Y / var1,
               this.Z / var1,
               this.aa / var1,
               this.ab / var1,
               this.ac / var1,
               this.af / var1,
               (this.ar - this.aq) / var1
            );
      }

      ShootingStarDemo.LOGGER
         .a(
            "[carve-timer] Tesseract waits (cell-ticks): land loading {}, light {}, a neighbour building {}, own build {}; latest cell {} ticks late",
            this.aj,
            this.ak,
            this.al,
            this.am,
            this.an
         );

      while (!this.ax.isEmpty()) {
         long var3 = this.ax.dequeueLong();
         LevelChunk var5 = this.b.getChunkSource().getChunkNow(ChunkPos.getX(var3), ChunkPos.getZ(var3));
         if (var5 != null) {
            cyou.rimuru.shootingstardemo.mc1211.star.a.a(this.b, var5);
         }
      }

      this.ay.clear();

      for (int var4 : this.aw) {
         this.av[var4].d().cancel(false);
         this.av[var4].c().b(this.b);
         this.av[var4] = null;
      }

      this.aw.clear();
      this.aJ.clear();

      for (int var7 = 0; var7 < this.aK.length; var7++) {
         if (this.aK[var7] != null) {
            this.f(var7);
         }
      }

      this.o();
   }

   private static record a(m.b a, int b, g c, CompletableFuture<long[]> d, LongOpenHashSet e, Long2ObjectOpenHashMap<BlockState> f) {
   }

   private static record b(int a, int b, int c, int d, int e, List<int[]> f) {
   }

   interface c {
      BlockState a(int var1, int var2, int var3);
   }

   interface d {
      void put(int var1, int var2, int var3, BlockState var4);

      default void a(int var1, int var2, int var3, int var4) {
         BlockState var5 = Blocks.AIR.defaultBlockState();

         for (int var6 = var3; var6 <= var4; var6++) {
            this.put(var1, var6, var2, var5);
         }
      }
   }
}
