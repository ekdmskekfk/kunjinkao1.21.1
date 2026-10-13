/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import cyou.rimuru.shootingstardemo.mc1211.ShootingStarDemo;
import cyou.rimuru.shootingstardemo.mc1211.client.render.e;
import cyou.rimuru.shootingstardemo.mc1211.client.render.f;
import cyou.rimuru.shootingstardemo.mc1211.client.render.g;
import cyou.rimuru.shootingstardemo.mc1211.client.render.j;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

public final class d {
    private final Map<String, g> a = new HashMap<String, g>();
    private final Set<String> b = new HashSet<String>();
    private static final int c = 5;
    private static final double d = 2300000.0;
    private final int[] e = new int[2];
    private final int[] f = new int[2];
    private final int[] g = new int[5];
    private final int[] h = new int[5];
    private final int[] i = new int[5];
    private final int[] j = new int[5];
    private final int[][] k = new int[5][2];
    private final FloatBuffer l = BufferUtils.createFloatBuffer((int)16);
    private final Pass m = new Pass();
    private static final int n = 64;
    private static final long o = 33000000L;
    private final int[] p = new int[64];
    private final long[] q = new long[64];
    private int r;
    private int s;
    private int t;
    private int u;
    private int v;
    private long w;
    private int x;
    private int y;
    private int z;
    private int A;
    private int B;
    private int C;
    private int D;
    private int E;
    private int F;
    private int G;
    private boolean H;
    private int I;
    private int J;
    private int K;
    private int L;
    private int M;
    private int N;
    private int O;
    private int P;
    private int Q;
    private int R;
    private boolean S;
    private float T;
    private boolean U;
    private int V;
    private final int[] W = new int[2];
    private final int[] X = new int[2];
    private double Y = 1.0;
    private double Z = 1.0;
    private double aa;
    private long ab;
    private int ac;
    private int ad;
    private boolean ae;
    private int af;
    private int ag;
    private boolean ah;
    private long ai;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void a(List<e> list, f f2, Matrix4f matrix4f, Matrix4f matrix4f2, float f3) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderTarget renderTarget = minecraft.getMainRenderTarget();
        int n2 = renderTarget.getColorTextureId();
        int n3 = renderTarget.getDepthTextureId();
        if (n2 <= 0 || n3 <= 0) {
            return;
        }
        RenderSystem.assertOnRenderThread();
        this.T = f3;
        GlState b2 = cyou.rimuru.shootingstardemo.mc1211.client.render.d.GlState.a();
        try {
            int n4;
            boolean bl;
            double d2;
            boolean bl2;
            long l2 = System.nanoTime();
            boolean bl3 = bl2 = f2.y > 0.5f && this.ah && this.J == renderTarget.width && this.K == renderTarget.height;
            if (bl2 && l2 - this.ai < (long)(1.0E9 / (double)f2.y)) {
                GL30.glBindFramebuffer((int)36009, (int)this.y);
                GL30.glFramebufferTexture2D((int)36009, (int)36064, (int)3553, (int)n2, (int)0);
                GL30.glBindFramebuffer((int)36008, (int)this.af);
                GL30.glBlitFramebuffer((int)0, (int)0, (int)this.J, (int)this.K, (int)0, (int)0, (int)this.J, (int)this.K, (int)16384, (int)9728);
                return;
            }
            if (f2.y <= 0.5f) {
                this.a(!list.isEmpty());
            }
            if ((d2 = Math.ceil(this.Y * 16.0) / 16.0) != this.Z) {
                this.Z = d2;
                ShootingStarDemo.LOGGER.a("Spell FX budget {}%", Math.round(d2 * 100.0));
            }
            double d3 = 2300000.0 * d2;
            double d4 = Math.min(1.0, Math.sqrt(d3 / Math.max(1.0, (double)renderTarget.width * (double)renderTarget.height)));
            this.a(renderTarget.width, renderTarget.height, Math.max(2, (int)((double)renderTarget.width * d4)), Math.max(2, (int)((double)renderTarget.height * d4)), n2, n3);
            boolean bl4 = this.L != this.J || this.M != this.K;
            this.b(n3);
            if (!this.U) {
                this.U = true;
                this.a(n2, n3);
            }
            this.b(!list.isEmpty());
            GL11.glViewport((int)0, (int)0, (int)this.L, (int)this.M);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            GL11.glDisable((int)3042);
            GL11.glDisable((int)2884);
            GL11.glDisable((int)3089);
            GL11.glColorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
            GL33.glBindSampler((int)0, (int)0);
            GL33.glBindSampler((int)1, (int)0);
            GL33.glBindSampler((int)2, (int)0);
            GL33.glBindSampler((int)3, (int)0);
            GL33.glBindSampler((int)4, (int)0);
            GL33.glBindSampler((int)5, (int)0);
            GL30.glBindVertexArray((int)this.I);
            Matrix4f matrix4f3 = new Matrix4f((Matrix4fc)matrix4f).invert();
            Matrix4f matrix4f4 = new Matrix4f((Matrix4fc)matrix4f2).invert();
            Matrix4f matrix4f5 = new Matrix4f((Matrix4fc)matrix4f).mul((Matrix4fc)matrix4f2);
            int n5 = this.A;
            int n6 = 0;
            boolean bl5 = bl = f2.s > 0.5f && this.S;
            if (bl) {
                n5 = this.R;
                list = List.of();
            }
            for (e object2 : list) {
                g g2 = this.b(object2.a());
                if (g2 == null) continue;
                GL30.glBindFramebuffer((int)36009, (int)this.e[n6]);
                this.a(g2, n5, matrix4f3, matrix4f4, matrix4f5);
                object2.b().accept(this.m);
                GL11.glDrawArrays((int)4, (int)0, (int)3);
                n5 = this.f[n6];
                n6 ^= 1;
            }
            if (f2.s > 0.5f && !this.S) {
                boolean bl6 = n5 == this.A;
                GL30.glBindFramebuffer((int)36008, (int)(bl6 ? this.z : this.e[n6 ^ 1]));
                GL30.glBindFramebuffer((int)36009, (int)this.Q);
                GL30.glBlitFramebuffer((int)0, (int)0, (int)(bl6 ? this.J : this.L), (int)(bl6 ? this.K : this.M), (int)0, (int)0, (int)this.L, (int)this.M, (int)16384, (int)9729);
                this.S = true;
            } else if (f2.s <= 0.5f && f2.t <= 0.001f) {
                this.S = false;
            }
            int n7 = n4 = f2.m > 0.001f && !bl ? this.a(n5, f2, matrix4f3, matrix4f4, matrix4f5) : 0;
            if (bl4) {
                GL30.glBindFramebuffer((int)36008, (int)this.z);
                GL30.glBindFramebuffer((int)36009, (int)this.N);
                GL30.glBlitFramebuffer((int)0, (int)0, (int)this.J, (int)this.K, (int)0, (int)0, (int)this.L, (int)this.M, (int)16384, (int)9729);
            }
            g g3 = this.b("composite");
            GL30.glBindFramebuffer((int)36009, (int)this.y);
            GL11.glViewport((int)0, (int)0, (int)this.J, (int)this.K);
            if (g3 != null) {
                this.a(g3, n5, matrix4f3, matrix4f4, matrix4f5);
                this.a(n4);
                this.a(1, this.A, "SceneSampler");
                this.a(3, this.O, "SceneLowSampler");
                this.a(4, n4 != 0 ? this.h[4] : 0, "ExposureSampler");
                this.a(5, this.R, "HoldSampler");
                this.m.b("AutoExposure", n4 != 0 ? 1.0f : 0.0f);
                this.m.b("Upscaled", bl4 && !bl ? 1.0f - Math.min(1.0f, f2.x) : 0.0f);
                this.m.a("Grade", f2.u, f2.v, this.S && !bl ? f2.t : 0.0f, f2.w);
                this.m.a("OutSize", this.J, this.K);
                this.m.b("Bloom", n4 != 0 ? f2.m : 0.0f);
                this.m.a("Impact", f2.o, f2.p, f2.q, f2.r);
                this.m.b("Shake", f2.a);
                this.m.b("Aberration", f2.b);
                this.m.b("Desaturate", f2.c);
                this.m.b("ZoomBlur", f2.d);
                this.m.a("Flash", f2.e, f2.f, f2.g, f2.h);
                this.m.a("Vignette", f2.i, f2.j, f2.k, f2.l);
                GL11.glDrawArrays((int)4, (int)0, (int)3);
            } else if (n5 != this.A) {
                GL30.glBindFramebuffer((int)36008, (int)this.e[n6 ^ 1]);
                GL30.glBlitFramebuffer((int)0, (int)0, (int)this.L, (int)this.M, (int)0, (int)0, (int)this.J, (int)this.K, (int)16384, (int)9729);
            }
            this.c(!list.isEmpty());
            this.a(f2, l2, matrix4f3, matrix4f4, matrix4f5);
            if (f2.y > 0.5f) {
                GL30.glBindFramebuffer((int)36008, (int)this.y);
                GL30.glBindFramebuffer((int)36009, (int)this.af);
                GL30.glBlitFramebuffer((int)0, (int)0, (int)this.J, (int)this.K, (int)0, (int)0, (int)this.J, (int)this.K, (int)16384, (int)9728);
                this.ah = true;
                this.ai = l2;
            } else {
                this.ah = false;
            }
        }
        catch (RuntimeException runtimeException) {
            ShootingStarDemo.LOGGER.c("The Shooting Star post-processing failed", runtimeException);
            cyou.rimuru.shootingstardemo.mc1211.client.a.a("the effects renderer", String.valueOf(runtimeException));
        }
        finally {
            b2.b();
        }
    }

    private void a(f f2, long l2, Matrix4f matrix4f, Matrix4f matrix4f2, Matrix4f matrix4f3) {
        if (f2.z < 0.5f) {
            if (this.r != 0 && ++this.x > 600) {
                this.d();
            }
            return;
        }
        this.x = 0;
        int n2 = Math.max(2, this.J / 4);
        int n3 = Math.max(2, this.K / 4);
        if (this.r == 0 || this.s != n2 || this.t != n3) {
            this.d();
            this.r = GL30.glGenFramebuffers();
            for (int i2 = 0; i2 < 64; ++i2) {
                this.p[i2] = GL11.glGenTextures();
                cyou.rimuru.shootingstardemo.mc1211.client.render.d.b(this.p[i2], n2, n3);
            }
            this.s = n2;
            this.t = n3;
            this.u = 0;
            this.v = 0;
        }
        if (f2.A >= 0.0f && this.v > 0) {
            g g2 = this.b("ton_rewind");
            if (g2 != null) {
                int n4 = this.a(f2.A);
                GL30.glBindFramebuffer((int)36009, (int)this.y);
                GL11.glViewport((int)0, (int)0, (int)this.J, (int)this.K);
                this.a(g2, this.p[n4], matrix4f, matrix4f2, matrix4f3);
                this.m.a("R0", this.T, f2.B, f2.A, 0.0f);
                this.m.a("OutSize", this.J, this.K);
                GL11.glDrawArrays((int)4, (int)0, (int)3);
            }
            return;
        }
        if (l2 - this.w < 33000000L) {
            return;
        }
        this.w = l2;
        GL30.glBindFramebuffer((int)36008, (int)this.y);
        GL30.glBindFramebuffer((int)36009, (int)this.r);
        GL30.glFramebufferTexture2D((int)36009, (int)36064, (int)3553, (int)this.p[this.u], (int)0);
        GL30.glBlitFramebuffer((int)0, (int)0, (int)this.J, (int)this.K, (int)0, (int)0, (int)this.s, (int)this.t, (int)16384, (int)9729);
        this.q[this.u] = l2;
        this.u = (this.u + 1) % 64;
        this.v = Math.min(this.v + 1, 64);
    }

    private int a(float f2) {
        int n2;
        int n3 = (this.u - 1 + 64) % 64;
        long l2 = this.q[n3] - (long)((double)f2 * 1.0E9);
        int n4 = n3;
        for (int i2 = 0; i2 < this.v && this.q[n2 = (n3 - i2 + 64) % 64] >= l2; ++i2) {
            n4 = n2;
        }
        return n4;
    }

    private void d() {
        if (this.r != 0) {
            GL30.glDeleteFramebuffers((int)this.r);
            for (int i2 = 0; i2 < 64; ++i2) {
                GL11.glDeleteTextures((int)this.p[i2]);
                this.p[i2] = 0;
            }
            this.r = 0;
            this.v = 0;
        }
    }

    private void a(boolean bl) {
        if (!bl) {
            this.Y = Math.min(1.0, this.Y * 1.05);
            return;
        }
        if (this.aa > 0.0) {
            double d2 = Mth.clamp((double)(this.Y * 16.0 / this.aa), (double)0.25, (double)1.0);
            this.Y = Mth.clamp((double)(d2 < this.Y ? d2 : this.Y + (d2 - this.Y) * 0.33), (double)0.25, (double)1.0);
            if (++this.ad % 6 == 0) {
                ShootingStarDemo.LOGGER.a("Spell FX gpu {} ms at {}% budget", (double)Math.round(this.aa * 10.0) / 10.0, Math.round(this.Y * 100.0));
            }
            this.aa = 0.0;
        }
    }

    private void b(boolean bl) {
        boolean bl2 = this.ae = bl && ++this.ac % 8 == 0;
        if (this.ae) {
            GL11.glFinish();
            this.ab = System.nanoTime();
        }
    }

    private void c(boolean bl) {
        if (this.ae) {
            GL11.glFinish();
            this.aa = (double)(System.nanoTime() - this.ab) / 1000000.0;
            this.ae = false;
        }
    }

    private int a(int n2, f f2, Matrix4f matrix4f, Matrix4f matrix4f2, Matrix4f matrix4f3) {
        int n3;
        g g2 = this.b("bloom_down");
        g g3 = this.b("bloom_up");
        if (g2 == null || g3 == null) {
            return 0;
        }
        int n4 = n2;
        int n5 = this.L;
        int n6 = this.M;
        for (n3 = 0; n3 < 5; ++n3) {
            GL30.glBindFramebuffer((int)36009, (int)this.g[n3]);
            GL11.glViewport((int)0, (int)0, (int)this.k[n3][0], (int)this.k[n3][1]);
            this.a(g2, n4, matrix4f, matrix4f2, matrix4f3);
            this.a(this.A);
            this.m.a("TexelSize", 1.0f / (float)n5, 1.0f / (float)n6);
            this.m.b("Prefilter", n3 == 0 ? 1.0f : 0.0f);
            this.m.b("Synthetic", f2.x);
            this.m.b("MaskAlpha", n2 != this.A ? 1.0f : 0.0f);
            GL11.glDrawArrays((int)4, (int)0, (int)3);
            n4 = this.h[n3];
            n5 = this.k[n3][0];
            n6 = this.k[n3][1];
        }
        for (n3 = 3; n3 >= 0; --n3) {
            GL30.glBindFramebuffer((int)36009, (int)this.i[n3]);
            GL11.glViewport((int)0, (int)0, (int)this.k[n3][0], (int)this.k[n3][1]);
            this.a(g3, n4, matrix4f, matrix4f2, matrix4f3);
            this.a(this.h[n3]);
            this.m.a("TexelSize", 1.0f / (float)n5, 1.0f / (float)n6);
            this.m.b("Radius", Math.max(1.0f, f2.n));
            GL11.glDrawArrays((int)4, (int)0, (int)3);
            n4 = this.j[n3];
            n5 = this.k[n3][0];
            n6 = this.k[n3][1];
        }
        return n4;
    }

    private void a(int n2, int n3, String string) {
        GL13.glActiveTexture((int)(33984 + n2));
        GL11.glBindTexture((int)3553, (int)n3);
        GL13.glActiveTexture((int)33984);
        int n4 = this.m.b.a(string);
        if (n4 >= 0) {
            GL20.glUniform1i((int)n4, (int)n2);
        }
    }

    private void a(int n2) {
        GL13.glActiveTexture((int)33986);
        GL11.glBindTexture((int)3553, (int)n2);
        GL13.glActiveTexture((int)33984);
        int n3 = this.m.b.a("AuxSampler");
        if (n3 >= 0) {
            GL20.glUniform1i((int)n3, (int)2);
        }
    }

    private void a(int n2, int n3) {
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer((int)4);
        GL30.glBindFramebuffer((int)36008, (int)this.y);
        GL11.glReadPixels((int)(this.J / 2), (int)(this.K / 3), (int)1, (int)1, (int)6402, (int)5126, (FloatBuffer)floatBuffer);
        float f2 = floatBuffer.get(0);
        GL30.glBindFramebuffer((int)36008, (int)this.z);
        floatBuffer.clear();
        GL11.glReadPixels((int)(this.J / 2), (int)(this.K / 3), (int)1, (int)1, (int)6402, (int)5126, (FloatBuffer)floatBuffer);
        float f3 = floatBuffer.get(0);
        floatBuffer.clear();
        GL11.glReadPixels((int)(this.J / 2), (int)(this.K / 3), (int)1, (int)1, (int)6408, (int)5126, (FloatBuffer)floatBuffer);
        int n4 = GL11.glGetError();
        ShootingStarDemo.LOGGER.a("Spell FX diagnostic: {}x{} fx {}x{} depthFormat=0x{} mainDepth={} snapshotDepth={} snapshotColor=[{}, {}, {}] glError=0x{}", this.J, this.K, this.L, this.M, Integer.toHexString(this.P), Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(floatBuffer.get(0)), Float.valueOf(floatBuffer.get(1)), Float.valueOf(floatBuffer.get(2)), Integer.toHexString(n4));
        if (Math.abs(f2 - f3) > 0.001f) {
            cyou.rimuru.shootingstardemo.mc1211.client.a.a("the depth copy", "main " + f2 + ", copy " + f3 + ", glError 0x" + Integer.toHexString(n4));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean a(String string) {
        GlState b2 = cyou.rimuru.shootingstardemo.mc1211.client.render.d.GlState.a();
        try {
            g g2 = this.b(string);
            if (g2 == null) {
                boolean bl = false;
                return bl;
            }
            if (this.V == 0) {
                this.V = GL30.glGenVertexArrays();
                for (int i2 = 0; i2 < 2; ++i2) {
                    this.W[i2] = GL30.glGenFramebuffers();
                    this.X[i2] = GL11.glGenTextures();
                    if (i2 == 0) {
                        cyou.rimuru.shootingstardemo.mc1211.client.render.d.a(this.X[i2], 1, 1);
                    } else {
                        cyou.rimuru.shootingstardemo.mc1211.client.render.d.b(this.X[i2], 1, 1);
                    }
                    cyou.rimuru.shootingstardemo.mc1211.client.render.d.b(this.W[i2], this.X[i2]);
                }
            }
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            GL11.glDisable((int)3042);
            GL11.glDisable((int)2884);
            GL11.glDisable((int)3089);
            GL11.glColorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
            GL11.glViewport((int)0, (int)0, (int)1, (int)1);
            GL30.glBindVertexArray((int)this.V);
            GL20.glUseProgram((int)g2.b);
            for (int n2 : this.W) {
                GL30.glBindFramebuffer((int)36009, (int)n2);
                GL11.glDrawArrays((int)4, (int)0, (int)3);
            }
            GL11.glFinish();
            boolean bl = true;
            return bl;
        }
        finally {
            b2.b();
        }
    }

    public void a() {
        this.a.values().forEach(cyou.rimuru.shootingstardemo.mc1211.client.render.g::a);
        this.a.clear();
        this.b.clear();
    }

    private g b(String string) {
        g g2 = this.a.get(string);
        if (g2 != null || this.b.contains(string)) {
            return g2;
        }
        try {
            g2 = cyou.rimuru.shootingstardemo.mc1211.client.render.g.a(string, cyou.rimuru.shootingstardemo.mc1211.client.render.g.b(string));
            this.a.put(string, g2);
            ShootingStarDemo.LOGGER.a("Compiled spell shader {}", string);
        }
        catch (Exception exception) {
            this.b.add(string);
            ShootingStarDemo.LOGGER.c("Could not build spell shader {}: {}", string, exception.getMessage());
            cyou.rimuru.shootingstardemo.mc1211.client.a.a("effect " + string, String.valueOf(exception.getMessage()));
        }
        return g2;
    }

    private void a(g g2, int n2, Matrix4f matrix4f, Matrix4f matrix4f2, Matrix4f matrix4f3) {
        GL20.glUseProgram((int)g2.b);
        this.m.b = g2;
        this.m.c = 6;
        GL13.glActiveTexture((int)33984);
        GL11.glBindTexture((int)3553, (int)n2);
        GL13.glActiveTexture((int)33985);
        GL11.glBindTexture((int)3553, (int)this.B);
        GL13.glActiveTexture((int)33984);
        cyou.rimuru.shootingstardemo.mc1211.client.render.d.c(g2.a("DiffuseSampler"), 0);
        cyou.rimuru.shootingstardemo.mc1211.client.render.d.c(g2.a("DepthSampler"), 1);
        this.a(g2.a("InvProj"), matrix4f);
        this.a(g2.a("InvView"), matrix4f2);
        this.a(g2.a("ViewProj"), matrix4f3);
        this.m.a("OutSize", this.L, this.M);
        this.m.b("GTime", this.T);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void a(int n2, int n3, int n4, int n5, int n6, int n7) {
        int n8 = GL11.glGetInteger((int)35055);
        GL15.glBindBuffer((int)35052, (int)0);
        try {
            int n9;
            if (this.I == 0) {
                this.I = GL30.glGenVertexArrays();
                for (n9 = 0; n9 < 2; ++n9) {
                    this.e[n9] = GL30.glGenFramebuffers();
                    this.f[n9] = GL11.glGenTextures();
                }
                this.y = GL30.glGenFramebuffers();
                this.z = GL30.glGenFramebuffers();
                this.A = GL11.glGenTextures();
                this.B = GL11.glGenTextures();
                this.C = GL33.glGenSamplers();
                GL33.glSamplerParameteri((int)this.C, (int)10241, (int)9728);
                GL33.glSamplerParameteri((int)this.C, (int)10240, (int)9728);
                GL33.glSamplerParameteri((int)this.C, (int)10242, (int)33071);
                GL33.glSamplerParameteri((int)this.C, (int)10243, (int)33071);
                GL33.glSamplerParameteri((int)this.C, (int)34892, (int)0);
                this.N = GL30.glGenFramebuffers();
                this.O = GL11.glGenTextures();
                this.Q = GL30.glGenFramebuffers();
                this.R = GL11.glGenTextures();
                for (n9 = 0; n9 < 5; ++n9) {
                    this.g[n9] = GL30.glGenFramebuffers();
                    this.h[n9] = GL11.glGenTextures();
                    this.i[n9] = GL30.glGenFramebuffers();
                    this.j[n9] = GL11.glGenTextures();
                }
            }
            GL11.glBindTexture((int)3553, (int)n7);
            n9 = GL11.glGetTexLevelParameteri((int)3553, (int)0, (int)4099);
            if (n2 != this.J || n3 != this.K || n9 != this.P || n4 != this.L || n5 != this.M) {
                this.J = n2;
                this.K = n3;
                this.L = n4;
                this.M = n5;
                this.P = n9;
                this.e();
            }
            GL30.glBindFramebuffer((int)36160, (int)this.y);
            GL30.glFramebufferTexture2D((int)36160, (int)36064, (int)3553, (int)n6, (int)0);
            cyou.rimuru.shootingstardemo.mc1211.client.render.d.a(n7, cyou.rimuru.shootingstardemo.mc1211.client.render.d.c(this.P));
            GL11.glDrawBuffer((int)36064);
            GL11.glReadBuffer((int)36064);
            cyou.rimuru.shootingstardemo.mc1211.client.render.d.c("main");
        }
        finally {
            GL15.glBindBuffer((int)35052, (int)n8);
        }
    }

    private void e() {
        int n2;
        int n3;
        for (n3 = 0; n3 < 2; ++n3) {
            cyou.rimuru.shootingstardemo.mc1211.client.render.d.b(this.f[n3], this.L, this.M);
            GL30.glBindFramebuffer((int)36160, (int)this.e[n3]);
            GL30.glFramebufferTexture2D((int)36160, (int)36064, (int)3553, (int)this.f[n3], (int)0);
            GL11.glDrawBuffer((int)36064);
            cyou.rimuru.shootingstardemo.mc1211.client.render.d.c("ping-pong");
        }
        for (n3 = 0; n3 < 5; ++n3) {
            n2 = Math.max(1, this.L >> n3 + 1);
            int n4 = Math.max(1, this.M >> n3 + 1);
            this.k[n3][0] = n2;
            this.k[n3][1] = n4;
            cyou.rimuru.shootingstardemo.mc1211.client.render.d.a(this.h[n3], n2, n4);
            cyou.rimuru.shootingstardemo.mc1211.client.render.d.b(this.g[n3], this.h[n3]);
            cyou.rimuru.shootingstardemo.mc1211.client.render.d.a(this.j[n3], n2, n4);
            cyou.rimuru.shootingstardemo.mc1211.client.render.d.b(this.i[n3], this.j[n3]);
        }
        cyou.rimuru.shootingstardemo.mc1211.client.render.d.b(this.O, this.L, this.M);
        cyou.rimuru.shootingstardemo.mc1211.client.render.d.b(this.N, this.O);
        cyou.rimuru.shootingstardemo.mc1211.client.render.d.b(this.R, this.L, this.M);
        cyou.rimuru.shootingstardemo.mc1211.client.render.d.b(this.Q, this.R);
        if (this.af == 0) {
            this.af = GL30.glGenFramebuffers();
            this.ag = GL11.glGenTextures();
        }
        cyou.rimuru.shootingstardemo.mc1211.client.render.d.b(this.ag, this.J, this.K);
        cyou.rimuru.shootingstardemo.mc1211.client.render.d.b(this.af, this.ag);
        this.ah = false;
        this.S = false;
        cyou.rimuru.shootingstardemo.mc1211.client.render.d.b(this.A, this.J, this.K);
        n3 = cyou.rimuru.shootingstardemo.mc1211.client.render.d.c(this.P) ? 1 : 0;
        switch (this.P) {
            case 33189: 
            case 33190: 
            case 33191: 
            case 35056: 
            case 36012: 
            case 36013: {
                n2 = this.P;
                break;
            }
            default: {
                n2 = 36012;
            }
        }
        if (Boolean.getBoolean("shooting_star_demo.mismatchdepth") && n3 == 0) {
            n2 = n2 == 33190 ? 36012 : 33190;
        }
        GL11.glBindTexture((int)3553, (int)this.B);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10242, (int)33071);
        GL11.glTexParameteri((int)3553, (int)10243, (int)33071);
        GL11.glTexParameteri((int)3553, (int)34892, (int)0);
        if (n3 != 0) {
            GL11.glTexImage2D((int)3553, (int)0, (int)n2, (int)this.J, (int)this.K, (int)0, (int)34041, (int)(n2 == 36013 ? 36269 : 34042), (long)0L);
        } else {
            GL11.glTexImage2D((int)3553, (int)0, (int)n2, (int)this.J, (int)this.K, (int)0, (int)6402, (int)5126, (long)0L);
        }
        GL30.glBindFramebuffer((int)36160, (int)this.z);
        GL30.glFramebufferTexture2D((int)36160, (int)36064, (int)3553, (int)this.A, (int)0);
        cyou.rimuru.shootingstardemo.mc1211.client.render.d.a(this.B, n3 != 0);
        GL11.glDrawBuffer((int)36064);
        cyou.rimuru.shootingstardemo.mc1211.client.render.d.c("snapshot");
    }

    private static void a(int n2, int n3, int n4) {
        GL11.glBindTexture((int)3553, (int)n2);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10242, (int)33071);
        GL11.glTexParameteri((int)3553, (int)10243, (int)33071);
        GL11.glTexImage2D((int)3553, (int)0, (int)34842, (int)n3, (int)n4, (int)0, (int)6408, (int)5126, (long)0L);
    }

    private static void b(int n2, int n3) {
        GL30.glBindFramebuffer((int)36160, (int)n2);
        GL30.glFramebufferTexture2D((int)36160, (int)36064, (int)3553, (int)n3, (int)0);
        GL11.glDrawBuffer((int)36064);
        cyou.rimuru.shootingstardemo.mc1211.client.render.d.c("bloom");
    }

    private static void b(int n2, int n3, int n4) {
        GL11.glBindTexture((int)3553, (int)n2);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10242, (int)33071);
        GL11.glTexParameteri((int)3553, (int)10243, (int)33071);
        GL11.glTexImage2D((int)3553, (int)0, (int)32856, (int)n3, (int)n4, (int)0, (int)6408, (int)5121, (long)0L);
    }

    private void b(int n2) {
        GL30.glBindFramebuffer((int)36008, (int)this.y);
        GL30.glBindFramebuffer((int)36009, (int)this.z);
        GL30.glBlitFramebuffer((int)0, (int)0, (int)this.J, (int)this.K, (int)0, (int)0, (int)this.J, (int)this.K, (int)16384, (int)9728);
        boolean bl = this.H && this.F == this.J && this.G == this.K;
        this.c(bl ? this.E : n2, this.J, this.K);
        this.H = false;
    }

    public void b() {
        this.H = false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void c() {
        RenderTarget renderTarget = Minecraft.getInstance().getMainRenderTarget();
        int n2 = renderTarget.getDepthTextureId();
        if (n2 <= 0 || this.I == 0 || this.b("depth_copy") == null) {
            return;
        }
        GlState b2 = cyou.rimuru.shootingstardemo.mc1211.client.render.d.GlState.a();
        int n3 = GL11.glGetInteger((int)35055);
        GL15.glBindBuffer((int)35052, (int)0);
        try {
            if (this.D == 0) {
                this.D = GL30.glGenFramebuffers();
                this.E = GL11.glGenTextures();
            }
            if (this.F != renderTarget.width || this.G != renderTarget.height) {
                this.F = renderTarget.width;
                this.G = renderTarget.height;
                GL11.glBindTexture((int)3553, (int)this.E);
                GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
                GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
                GL11.glTexParameteri((int)3553, (int)10242, (int)33071);
                GL11.glTexParameteri((int)3553, (int)10243, (int)33071);
                GL11.glTexParameteri((int)3553, (int)34892, (int)0);
                GL11.glTexImage2D((int)3553, (int)0, (int)36012, (int)this.F, (int)this.G, (int)0, (int)6402, (int)5126, (long)0L);
                GL30.glBindFramebuffer((int)36160, (int)this.D);
                GL30.glFramebufferTexture2D((int)36160, (int)36096, (int)3553, (int)this.E, (int)0);
                GL11.glDrawBuffer((int)0);
                GL11.glReadBuffer((int)0);
                cyou.rimuru.shootingstardemo.mc1211.client.render.d.c("kept depth");
            }
            GL30.glBindFramebuffer((int)36009, (int)this.D);
            this.c(n2, this.F, this.G);
            this.H = true;
        }
        catch (RuntimeException runtimeException) {
            ShootingStarDemo.LOGGER.c("The Shooting Star could not keep the scene's depth", runtimeException);
        }
        finally {
            GL15.glBindBuffer((int)35052, (int)n3);
            b2.b();
        }
    }

    private void c(int n2, int n3, int n4) {
        g g2 = this.b("depth_copy");
        if (g2 == null) {
            GL30.glBlitFramebuffer((int)0, (int)0, (int)n3, (int)n4, (int)0, (int)0, (int)n3, (int)n4, (int)256, (int)9728);
            return;
        }
        int n5 = GL11.glGetInteger((int)2932);
        GL11.glViewport((int)0, (int)0, (int)n3, (int)n4);
        GL11.glColorMask((boolean)false, (boolean)false, (boolean)false, (boolean)false);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2884);
        GL11.glDisable((int)3089);
        GL11.glEnable((int)2929);
        GL11.glDepthFunc((int)519);
        GL11.glDepthMask((boolean)true);
        GL20.glUseProgram((int)g2.b);
        GL13.glActiveTexture((int)33985);
        GL11.glBindTexture((int)3553, (int)n2);
        GL33.glBindSampler((int)1, (int)this.C);
        GL13.glActiveTexture((int)33984);
        cyou.rimuru.shootingstardemo.mc1211.client.render.d.c(g2.a("DepthSampler"), 1);
        GL30.glBindVertexArray((int)this.I);
        GL11.glDrawArrays((int)4, (int)0, (int)3);
        GL33.glBindSampler((int)1, (int)0);
        GL11.glDepthFunc((int)n5);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
    }

    private static boolean c(int n2) {
        return n2 == 35056 || n2 == 36013;
    }

    private static void a(int n2, boolean bl) {
        if (bl) {
            GL30.glFramebufferTexture2D((int)36160, (int)36096, (int)3553, (int)0, (int)0);
            GL30.glFramebufferTexture2D((int)36160, (int)33306, (int)3553, (int)n2, (int)0);
        } else {
            GL30.glFramebufferTexture2D((int)36160, (int)33306, (int)3553, (int)0, (int)0);
            GL30.glFramebufferTexture2D((int)36160, (int)36096, (int)3553, (int)n2, (int)0);
        }
    }

    private static void c(String string) {
        int n2 = GL30.glCheckFramebufferStatus((int)36160);
        if (n2 != 36053) {
            throw new IllegalStateException("Incomplete " + string + " framebuffer: " + n2);
        }
    }

    private static void c(int n2, int n3) {
        if (n2 >= 0) {
            GL20.glUniform1i((int)n2, (int)n3);
        }
    }

    private void a(int n2, Matrix4f matrix4f) {
        if (n2 >= 0) {
            this.l.clear();
            matrix4f.get(this.l);
            GL20.glUniformMatrix4fv((int)n2, (boolean)false, (FloatBuffer)this.l);
        }
    }

    private final class Pass
    implements j {
        static final int a = 6;
        g b;
        int c;

        private Pass() {
        }

        @Override
        public void a(String string, String string2) {
            int n2 = this.b.a(string);
            if (n2 < 0 || this.c >= 16) {
                return;
            }
            int n3 = this.c++;
            GL13.glActiveTexture((int)(33984 + n3));
            GL11.glBindTexture((int)3553, (int)cyou.rimuru.shootingstardemo.mc1211.client.render.a.b(string2));
            GL33.glBindSampler((int)n3, (int)0);
            GL13.glActiveTexture((int)33984);
            GL20.glUniform1i((int)n2, (int)n3);
        }

        @Override
        public void a(String string, int n2) {
            int n3 = this.b.a(string);
            if (n3 < 0 || this.c >= 16 || n2 == 0) {
                return;
            }
            int n4 = this.c++;
            GL13.glActiveTexture((int)(33984 + n4));
            GL11.glBindTexture((int)3553, (int)n2);
            GL33.glBindSampler((int)n4, (int)0);
            GL13.glActiveTexture((int)33984);
            GL20.glUniform1i((int)n3, (int)n4);
        }

        @Override
        public void a(String string, float f2) {
            this.a(string, d.this.r != 0 && d.this.v > 0 ? d.this.p[d.this.a(f2)] : d.this.A);
        }

        @Override
        public void b(String string, float f2) {
            int n2 = this.b.a(string);
            if (n2 >= 0) {
                GL20.glUniform1f((int)n2, (float)f2);
            }
        }

        @Override
        public void a(String string, float f2, float f3) {
            int n2 = this.b.a(string);
            if (n2 >= 0) {
                GL20.glUniform2f((int)n2, (float)f2, (float)f3);
            }
        }

        @Override
        public void a(String string, float f2, float f3, float f4) {
            int n2 = this.b.a(string);
            if (n2 >= 0) {
                GL20.glUniform3f((int)n2, (float)f2, (float)f3, (float)f4);
            }
        }

        @Override
        public void a(String string, float f2, float f3, float f4, float f5) {
            int n2 = this.b.a(string);
            if (n2 >= 0) {
                GL20.glUniform4f((int)n2, (float)f2, (float)f3, (float)f4, (float)f5);
            }
        }

        @Override
        public void a(String string, float[] fArray) {
            int n2 = this.b.a(string + "[0]");
            if (n2 < 0) {
                n2 = this.b.a(string);
            }
            if (n2 >= 0) {
                GL20.glUniform4fv((int)n2, (float[])fArray);
            }
        }
    }

    static final class GlState
{
        private final int b;
        private final int c;
        private final int d;
        private final int e;
        private final int f;
        private final int[] g;
        private final int[] h;
        private final int[] i;
        private final boolean j;
        private final boolean k;
        private final boolean l;
        private final boolean m;
        private final boolean n;
        private final boolean[] o;
        static final int a = 16;

        GlState(int drawFbo, int readFbo, int program, int vao, int activeTexture, int[] textures, int[] samplers, int[] viewport, boolean depthTest, boolean blend, boolean cull, boolean scissor, boolean depthMask, boolean[] colorMask) {
            this.b = drawFbo;
            this.c = readFbo;
            this.d = program;
            this.e = vao;
            this.f = activeTexture;
            this.g = textures;
            this.h = samplers;
            this.i = viewport;
            this.j = depthTest;
            this.k = blend;
            this.l = cull;
            this.m = scissor;
            this.n = depthMask;
            this.o = colorMask;
        }

        static GlState a() {
            IntBuffer intBuffer = BufferUtils.createIntBuffer((int)4);
            GL11.glGetIntegerv((int)2978, (IntBuffer)intBuffer);
            int n2 = GL11.glGetInteger((int)34016);
            int[] nArray = new int[16];
            int[] nArray2 = new int[16];
            for (int i2 = 0; i2 < 16; ++i2) {
                GL13.glActiveTexture((int)(33984 + i2));
                nArray[i2] = GL11.glGetInteger((int)32873);
                nArray2[i2] = GL11.glGetInteger((int)35097);
            }
            GL13.glActiveTexture((int)n2);
            ByteBuffer byteBuffer = BufferUtils.createByteBuffer((int)16);
            GL11.glGetBooleanv((int)3107, (ByteBuffer)byteBuffer);
            return new GlState(GL11.glGetInteger((int)36006), GL11.glGetInteger((int)36010), GL11.glGetInteger((int)35725), GL11.glGetInteger((int)34229), n2, nArray, nArray2, new int[]{intBuffer.get(0), intBuffer.get(1), intBuffer.get(2), intBuffer.get(3)}, GL11.glIsEnabled((int)2929), GL11.glIsEnabled((int)3042), GL11.glIsEnabled((int)2884), GL11.glIsEnabled((int)3089), GL11.glGetBoolean((int)2930), new boolean[]{byteBuffer.get(0) != 0, byteBuffer.get(1) != 0, byteBuffer.get(2) != 0, byteBuffer.get(3) != 0});
        }

        void b() {
            cyou.rimuru.shootingstardemo.mc1211.client.render.d.GlState.a(2929, this.j);
            cyou.rimuru.shootingstardemo.mc1211.client.render.d.GlState.a(3042, this.k);
            cyou.rimuru.shootingstardemo.mc1211.client.render.d.GlState.a(2884, this.l);
            cyou.rimuru.shootingstardemo.mc1211.client.render.d.GlState.a(3089, this.m);
            GL11.glDepthMask((boolean)this.n);
            GL11.glColorMask((boolean)this.o[0], (boolean)this.o[1], (boolean)this.o[2], (boolean)this.o[3]);
            GL20.glUseProgram((int)this.d);
            GL30.glBindVertexArray((int)this.e);
            for (int i2 = 0; i2 < 16; ++i2) {
                GL13.glActiveTexture((int)(33984 + i2));
                GL11.glBindTexture((int)3553, (int)this.g[i2]);
                GL33.glBindSampler((int)i2, (int)this.h[i2]);
            }
            GL13.glActiveTexture((int)this.f);
            GL30.glBindFramebuffer((int)36009, (int)this.b);
            GL30.glBindFramebuffer((int)36008, (int)this.c);
            GL11.glViewport((int)this.i[0], (int)this.i[1], (int)this.i[2], (int)this.i[3]);
        }

        private static void a(int n2, boolean bl) {
            if (bl) {
                GL11.glEnable((int)n2);
            } else {
                GL11.glDisable((int)n2);
            }
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

        public int c() {
            return this.b;
        }

        public int d() {
            return this.c;
        }

        public int e() {
            return this.d;
        }

        public int f() {
            return this.e;
        }

        public int g() {
            return this.f;
        }

        public int[] h() {
            return this.g;
        }

        public int[] i() {
            return this.h;
        }

        public int[] j() {
            return this.i;
        }

        public boolean k() {
            return this.j;
        }

        public boolean l() {
            return this.k;
        }

        public boolean m() {
            return this.l;
        }

        public boolean n() {
            return this.m;
        }

        public boolean o() {
            return this.n;
        }

        public boolean[] p() {
            return this.o;
        }
    }
}

