/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.b;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.c;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.d;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.f;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.i;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.k;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.n;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.o;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.u;
import cyou.rimuru.shootingstardemo.mc1211.star.l;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector4f;

public final class e {
    private static final int a = 24;
    private static final List<n> b = new ArrayList<n>();
    private static final List<a> c = new ArrayList<a>();
    private static final cyou.rimuru.shootingstardemo.mc1211.client.render.d d = new cyou.rimuru.shootingstardemo.mc1211.client.render.d();
    private static final cyou.rimuru.shootingstardemo.mc1211.client.render.f e = new cyou.rimuru.shootingstardemo.mc1211.client.render.f();
    private static final Matrix4f f = new Matrix4f();
    private static final Matrix4f g = new Matrix4f();
    private static final Matrix4f h = new Matrix4f();
    private static Vec3 i = Vec3.ZERO;
    private static float j;
    private static float k;
    private static boolean l;

    private e() {
    }

    public static void a(cyou.rimuru.shootingstardemo.mc1211.net.e e2) {
        cyou.rimuru.shootingstardemo.mc1211.magic.d d2 = cyou.rimuru.shootingstardemo.mc1211.magic.e.a(e2.b());
        if (d2 instanceof l) {
            l l2 = (l)d2;
            if (l2 == cyou.rimuru.shootingstardemo.mc1211.star.l.b) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(l2, e2);
            } else if (l2 == cyou.rimuru.shootingstardemo.mc1211.star.l.c) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(l2, e2);
            } else if (l2 == cyou.rimuru.shootingstardemo.mc1211.star.l.d) {
                u.a(l2, e2);
            } else {
                o.a(l2, e2);
            }
        }
    }

    public static void a(n n2) {
        while (b.size() >= 24) {
            n n3 = b.remove(0);
            n3.h();
        }
        b.add(n2);
    }

    public static void a(Minecraft minecraft) {
        ClientLevel clientLevel = minecraft.level;
        if (clientLevel == null) {
            cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a();
            return;
        }
        Iterator<n> iterator = b.iterator();
        while (iterator.hasNext()) {
            n n2 = iterator.next();
            n2.a(minecraft, clientLevel);
            if (!n2.k()) continue;
            iterator.remove();
            n2.h();
        }
        c.removeIf(a2 -> {
            a2.d[0] = a2.d[0] + 1;
            return a2.d[0] > 45;
        });
        boolean bl = false;
        for (n n3 : b) {
            bl |= n3.b(i);
        }
        cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(bl);
        j = Math.max(0.0f, j - 0.035f);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.a.a();
    }

    public static void a() {
        cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.e();
        for (n n2 : List.copyOf(b)) {
            n2.h();
        }
        b.clear();
        c.clear();
        cyou.rimuru.shootingstardemo.mc1211.client.fx.c.a();
        cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(false);
        j = 0.0f;
        k = 0.0f;
    }

    private static void a(boolean bl) {
        if (bl == l) {
            return;
        }
        l = bl;
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getSoundManager().updateSourceVolume(SoundSource.AMBIENT, minecraft.options.getSoundSourceVolume(SoundSource.AMBIENT));
    }

    public static boolean b() {
        return l;
    }

    public static b a(int n2, float f2) {
        for (int i2 = b.size() - 1; i2 >= 0; --i2) {
            b b2;
            n n3 = b.get(i2);
            if (n3.m() != n2 || (b2 = n3.c(f2)) == null) continue;
            return b2;
        }
        return null;
    }

    public static boolean c() {
        return !b.isEmpty();
    }

    public static n a(cyou.rimuru.shootingstardemo.mc1211.magic.d d2) {
        for (int i2 = b.size() - 1; i2 >= 0; --i2) {
            if (cyou.rimuru.shootingstardemo.mc1211.client.fx.e.b.get((int)i2).m != d2) continue;
            return b.get(i2);
        }
        return null;
    }

    public static void a(Vec3 vec3, float f2, double d2) {
        double d3 = i.distanceTo(vec3);
        float f3 = (float)Mth.clamp((double)(1.0 - d3 / d2), (double)0.0, (double)1.0);
        j = Math.min(1.0f, j + f2 * f3);
    }

    public static void a(String string, Vec3 vec3, int n2) {
        c.add(new a(string, vec3, n2, new int[1]));
    }

    public static void a(Matrix4f matrix4f, Matrix4f matrix4f2) {
        f.set((Matrix4fc)matrix4f);
        g.set((Matrix4fc)matrix4f2);
        d.b();
    }

    public static void d() {
        if (Minecraft.useShaderTransparency() && (!b.isEmpty() || j > 0.0f || cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.g())) {
            d.c();
        }
    }

    public static void a(Camera camera, float f2) {
        Minecraft minecraft = Minecraft.getInstance();
        i = camera.getPosition();
        h.set((Matrix4fc)g).mul((Matrix4fc)f);
        if (b.isEmpty() && j <= 0.0f && !cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.g()) {
            k = 0.0f;
            return;
        }
        d d2 = new d(minecraft, f2, i);
        ArrayList<cyou.rimuru.shootingstardemo.mc1211.client.render.e> arrayList = new ArrayList<cyou.rimuru.shootingstardemo.mc1211.client.render.e>();
        e.a();
        for (n n2 : b) {
            if (n2.l().distanceTo(i) > n2.a()) continue;
            n2.a(d2, arrayList);
            n2.a(d2, e);
        }
        k = cyou.rimuru.shootingstardemo.mc1211.client.fx.e.e.a;
        cyou.rimuru.shootingstardemo.mc1211.client.fx.e.e.a = Math.min(1.0f, cyou.rimuru.shootingstardemo.mc1211.client.fx.e.e.a * 0.4f + j * j * 0.35f);
        if (arrayList.isEmpty() && e.b()) {
            return;
        }
        float f3 = ((float)(minecraft.level == null ? 0L : minecraft.level.getGameTime() % 24000L) + f2) / 20.0f;
        d.a(arrayList, e, g, f, f3);
    }

    public static void a(PoseStack poseStack, float f2) {
        float f3;
        float f4 = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.i();
        if (Math.abs(f4) > 0.001f) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(f4));
        }
        if ((f3 = j * j + k * 0.25f) <= 1.0E-4f) {
            return;
        }
        float f5 = (float)(System.nanoTime() % 100000000000L) / 1.0E9f;
        float f6 = (float)(Math.sin((double)f5 * 37.0) * 0.6 + Math.sin((double)f5 * 61.0) * 0.4) * 2.6f * f3;
        float f7 = (float)(Math.sin((double)f5 * 43.0 + 1.3) * 0.6 + Math.sin((double)f5 * 71.0) * 0.4) * 2.2f * f3;
        float f8 = (float)Math.sin((double)f5 * 29.0 + 0.7) * 3.0f * f3;
        poseStack.mulPose(Axis.ZP.rotationDegrees(f8));
        poseStack.mulPose(Axis.XP.rotationDegrees(f7));
        poseStack.mulPose(Axis.YP.rotationDegrees(f6));
    }

    public static void a(GuiGraphics guiGraphics, float f2) {
        Minecraft minecraft = Minecraft.getInstance();
        d d2 = new d(minecraft, f2, i);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.i.a(guiGraphics);
        for (n n2 : b) {
            if (n2 instanceof o) {
                o o2 = (o)n2;
                o2.b(guiGraphics, d2);
                continue;
            }
            if (n2 instanceof f) {
                f f3 = (f)n2;
                f3.b(guiGraphics, d2);
                continue;
            }
            if (n2 instanceof k) {
                k k2 = (k)n2;
                k2.b(guiGraphics, d2);
                continue;
            }
            if (!(n2 instanceof u)) continue;
            u u2 = (u)n2;
            u2.b(guiGraphics, d2);
        }
        cyou.rimuru.shootingstardemo.mc1211.client.fx.i.b(guiGraphics);
    }

    public static void b(GuiGraphics guiGraphics, float f2) {
        Minecraft minecraft = Minecraft.getInstance();
        d d2 = new d(minecraft, f2, i);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.i.a(guiGraphics);
        for (n object : b) {
            object.a(guiGraphics, d2);
        }
        cyou.rimuru.shootingstardemo.mc1211.client.fx.i.b(guiGraphics);
        for (a a2 : c) {
            int n2;
            float[] fArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(a2.b.add(0.0, (double)a2.d[0] * 0.03, 0.0), guiGraphics);
            if (fArray == null || (n2 = (int)(255.0 * Mth.clamp((double)(1.0 - (double)(a2.d[0] - 25) / 20.0), (double)0.0, (double)1.0))) < 8) continue;
            int n3 = minecraft.font.width(a2.a);
            guiGraphics.drawString(minecraft.font, a2.a, (int)fArray[0] - n3 / 2, (int)fArray[1], n2 << 24 | a2.c & 0xFFFFFF, true);
        }
    }

    public static float[] a(Vec3 vec3, GuiGraphics guiGraphics) {
        Vec3 vec32 = vec3.subtract(i);
        Vector4f vector4f = h.transform(new Vector4f((float)vec32.x, (float)vec32.y, (float)vec32.z, 1.0f));
        if (vector4f.w <= 0.05f) {
            return null;
        }
        float f2 = (vector4f.x / vector4f.w * 0.5f + 0.5f) * (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.i.c(guiGraphics);
        float f3 = (1.0f - (vector4f.y / vector4f.w * 0.5f + 0.5f)) * (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.i.d(guiGraphics);
        return new float[]{f2, f3};
    }

    public static float[] a(Vec3 vec3) {
        Vec3 vec32 = vec3.subtract(i);
        Vector4f vector4f = h.transform(new Vector4f((float)vec32.x, (float)vec32.y, (float)vec32.z, 1.0f));
        if (vector4f.w <= 0.05f) {
            return new float[]{0.5f, 0.5f};
        }
        return new float[]{Mth.clamp((float)(vector4f.x / vector4f.w * 0.5f + 0.5f), (float)0.0f, (float)1.0f), Mth.clamp((float)(vector4f.y / vector4f.w * 0.5f + 0.5f), (float)0.0f, (float)1.0f)};
    }

    public static Vec3 e() {
        return i;
    }

    public static boolean a(String string) {
        return d.a(string);
    }

    public static void f() {
        d.a();
    }

    private static final class a
{
        private final String a;
        private final Vec3 b;
        private final int c;
        private final int[] d;

        private a(String text, Vec3 at, int color, int[] age) {
            this.a = text;
            this.b = at;
            this.c = color;
            this.d = age;
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

        public String a() {
            return this.a;
        }

        public Vec3 b() {
            return this.b;
        }

        public int c() {
            return this.c;
        }

        public int[] d() {
            return this.d;
        }
    }
}

