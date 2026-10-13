/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import cyou.rimuru.shootingstardemo.mc1211.ShootingStarDemo;
import cyou.rimuru.shootingstardemo.mc1211.client.render.a;
import cyou.rimuru.shootingstardemo.mc1211.client.render.d;
import cyou.rimuru.shootingstardemo.mc1211.client.render.g;
import cyou.rimuru.shootingstardemo.mc1211.magic.b;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

public final class k {
    private static final String b = "misc/watermark.png";
    private static final float c = 0.003921569f;
    public static boolean a = true;
    private static boolean d;
    private static g e;
    private static int f;
    private static int g;
    private static int h;
    private static int i;
    private static int j;
    private static int k;
    private static int l;
    private static int m;
    private static int n;

    private k() {
    }

    public static int[] a(int n2, int n3, int n4, int n5) {
        int n6 = Math.max(1, Math.round(Math.min(0.45000002f * (float)n3, 0.7f * (float)n2)));
        int n7 = Math.max(1, Math.round((float)n6 * (float)n5 / (float)n4));
        return new int[]{(n2 - n6) / 2, (n3 - n7) / 2, n6, n7};
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void a() {
        if (!a || d) {
            return;
        }
        if (cyou.rimuru.shootingstardemo.mc1211.magic.b.a((Player)Minecraft.getInstance().player) == null) {
            return;
        }
        RenderTarget renderTarget = Minecraft.getInstance().getMainRenderTarget();
        int n2 = renderTarget.getColorTextureId();
        if (n2 <= 0) {
            return;
        }
        int n3 = cyou.rimuru.shootingstardemo.mc1211.client.render.a.b(b);
        if (n3 == 0) {
            return;
        }
        RenderSystem.assertOnRenderThread();
        d.GlState b2 = cyou.rimuru.shootingstardemo.mc1211.client.render.d.GlState.a();
        try {
            if (l != n3) {
                l = n3;
                GL11.glBindTexture((int)3553, (int)n3);
                m = Math.max(1, GL11.glGetTexLevelParameteri((int)3553, (int)0, (int)4096));
                n = Math.max(1, GL11.glGetTexLevelParameteri((int)3553, (int)0, (int)4097));
            }
            int[] nArray = cyou.rimuru.shootingstardemo.mc1211.client.render.k.a(renderTarget.width, renderTarget.height, m, n);
            cyou.rimuru.shootingstardemo.mc1211.client.render.k.a(nArray[2], nArray[3]);
            GL30.glBindFramebuffer((int)36160, (int)g);
            GL30.glFramebufferTexture2D((int)36160, (int)36064, (int)3553, (int)n2, (int)0);
            GL30.glBindFramebuffer((int)36008, (int)g);
            GL30.glBindFramebuffer((int)36009, (int)h);
            GL30.glBlitFramebuffer((int)nArray[0], (int)nArray[1], (int)(nArray[0] + nArray[2]), (int)(nArray[1] + nArray[3]), (int)0, (int)0, (int)nArray[2], (int)nArray[3], (int)16384, (int)9728);
            GL30.glBindFramebuffer((int)36160, (int)g);
            GL11.glViewport((int)nArray[0], (int)nArray[1], (int)nArray[2], (int)nArray[3]);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            GL11.glDisable((int)3042);
            GL11.glDisable((int)2884);
            GL11.glDisable((int)3089);
            GL11.glColorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
            GL20.glUseProgram((int)cyou.rimuru.shootingstardemo.mc1211.client.render.k.e.b);
            GL30.glBindVertexArray((int)f);
            GL33.glBindSampler((int)0, (int)0);
            GL33.glBindSampler((int)1, (int)0);
            GL13.glActiveTexture((int)33984);
            GL11.glBindTexture((int)3553, (int)i);
            GL13.glActiveTexture((int)33985);
            GL11.glBindTexture((int)3553, (int)n3);
            GL13.glActiveTexture((int)33984);
            GL20.glUniform1i((int)e.a("DiffuseSampler"), (int)0);
            GL20.glUniform1i((int)e.a("AuxSampler"), (int)1);
            GL20.glUniform4f((int)e.a("Box"), (float)nArray[0], (float)nArray[1], (float)nArray[2], (float)nArray[3]);
            GL20.glUniform1f((int)e.a("Strength"), (float)0.003921569f);
            GL11.glDrawArrays((int)4, (int)0, (int)3);
        }
        catch (RuntimeException runtimeException) {
            d = true;
            ShootingStarDemo.LOGGER.c("Frame mark disabled: {}", String.valueOf(runtimeException));
        }
        finally {
            b2.b();
        }
    }

    private static void a(int n2, int n3) {
        if (e == null) {
            try {
                e = cyou.rimuru.shootingstardemo.mc1211.client.render.g.a("watermark", cyou.rimuru.shootingstardemo.mc1211.client.render.g.b("watermark"));
            }
            catch (IOException iOException) {
                throw new IllegalStateException(iOException);
            }
            f = GL30.glGenVertexArrays();
            g = GL30.glGenFramebuffers();
            h = GL30.glGenFramebuffers();
            i = GL11.glGenTextures();
        }
        if (n2 == j && n3 == k) {
            return;
        }
        j = n2;
        k = n3;
        int n4 = GL11.glGetInteger((int)35055);
        GL15.glBindBuffer((int)35052, (int)0);
        GL11.glBindTexture((int)3553, (int)i);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GL11.glTexParameteri((int)3553, (int)33085, (int)0);
        GL11.glTexImage2D((int)3553, (int)0, (int)32856, (int)n2, (int)n3, (int)0, (int)6408, (int)5121, (ByteBuffer)null);
        GL15.glBindBuffer((int)35052, (int)n4);
        GL30.glBindFramebuffer((int)36160, (int)h);
        GL30.glFramebufferTexture2D((int)36160, (int)36064, (int)3553, (int)i, (int)0);
        GL11.glDrawBuffer((int)36064);
        GL11.glReadBuffer((int)36064);
        if (GL30.glCheckFramebufferStatus((int)36160) != 36053) {
            throw new IllegalStateException("copy target incomplete");
        }
    }

    static {
        m = 1;
        n = 1;
    }
}

