/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.magic;

import com.mojang.blaze3d.platform.InputConstants;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.f;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.g;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.j;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.k;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.l;
import cyou.rimuru.shootingstardemo.mc1211.magic.d;
import cyou.rimuru.shootingstardemo.mc1211.magic.e;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public final class h {
    static final int a = 10;
    static final int b = 8;
    static final int c = 14;
    static final int d = 13;
    static final int e = 8;
    static final int f = 5;
    static final List<cyou.rimuru.shootingstardemo.mc1211.magic.b> g = new ArrayList<cyou.rimuru.shootingstardemo.mc1211.magic.b>();
    static final List<ItemStack> h = new ArrayList<ItemStack>();
    static boolean[] i = new boolean[0];
    static Phase j = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.a;
    static int k;
    static int l;
    static float m;
    static float n;
    static float o;
    static float p;
    static int q;
    static float r;
    static float s;
    static int t;
    static float u;
    static a v;
    static a w;
    static d x;
    static String y;
    private static String O;
    private static int P;
    static final Map<String, float[]> z;
    static String A;
    static int B;
    static String C;
    static int D;
    private static int Q;
    static String E;
    static int F;
    static String G;
    private static final float[] R;
    private static double S;
    private static String T;
    static int H;
    private static int U;
    static float I;
    static float J;
    static float K;
    static float L;
    static boolean M;
    private static double V;
    private static int W;
    private static ClientLevel X;
    private static LocalPlayer Y;
    private static float Z;
    private static int aa;
    private static boolean ab;
    static float N;
    private static final float ac = 55.0f;
    private static final float ad = 45.0f;

    private h() {
    }

    public static boolean a() {
        return j == cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.b || j == cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.c;
    }

    public static boolean b() {
        return j != cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.a;
    }

    public static boolean c() {
        return j == cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.c || j == cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.d && k - l < 3;
    }

    public static void a(Minecraft minecraft) {
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null || j != cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.a || localPlayer.isDeadOrDying()) {
            return;
        }
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.g();
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(localPlayer);
        j = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.b;
        k = 0;
        l = 0;
        m = localPlayer.getYRot();
        n = localPlayer.getXRot();
        X = minecraft.level;
        Y = localPlayer;
        Z = localPlayer.getHealth();
        aa = localPlayer.hurtTime;
        q = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.c(localPlayer);
        r = 13.0f;
        s = 13.0f;
        t = -1;
        v = null;
        w = null;
        x = null;
        y = null;
        O = null;
        z.clear();
        A = null;
        C = null;
        E = null;
        G = null;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.R[1] = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b() ? 1.0f : 0.0f;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.R[0] = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.R[1];
        Q = -1;
        K = 0.0f;
        J = 0.0f;
        I = 0.0f;
        W = -100;
        V = 0.0;
        M = true;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a();
        KeyMapping.releaseAll();
        ab = minecraft.options.hideGui;
        minecraft.options.hideGui = false;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.Q, 1.0f, 0.65f);
    }

    static void d() {
        if (!cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a()) {
            return;
        }
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.p();
        j = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.d;
        l = k;
        x = null;
        G = null;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.g.a();
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q();
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.R, 1.0f, 0.6f);
    }

    private static void a(LocalPlayer localPlayer) {
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.p();
        j = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.e;
        l = k;
        x = null;
        G = null;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.g.a();
        float f2 = localPlayer.getHurtDir();
        N = Mth.clamp((float)((float)(-Math.sin(Math.toRadians(f2)))), (float)-1.0f, (float)1.0f) * 0.5f;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q();
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.Z, 1.0f, 0.85f);
    }

    public static void e() {
        boolean bl = j != cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.a;
        j = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.a;
        v = null;
        w = null;
        x = null;
        G = null;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.g.a();
        X = null;
        Y = null;
        if (bl) {
            Minecraft.getInstance().options.hideGui = ab;
        }
    }

    private static void c(Minecraft minecraft) {
        j = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.a;
        G = null;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.g.a();
        v = null;
        w = null;
        X = null;
        Y = null;
        KeyMapping.setAll();
        minecraft.options.hideGui = ab;
    }

    private static void p() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            o = Mth.wrapDegrees((float)(minecraft.player.getViewYRot(1.0f) - m));
            p = minecraft.player.getViewXRot(1.0f);
        }
    }

    private static void q() {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null) {
            return;
        }
        localPlayer.setYRot(m);
        localPlayer.setXRot(n);
        localPlayer.yRotO = m;
        localPlayer.xRotO = n;
        localPlayer.yHeadRot = m;
        localPlayer.yHeadRotO = m;
        localPlayer.yBob = m;
        localPlayer.yBobO = m;
        localPlayer.xBob = n;
        localPlayer.xBobO = n;
    }

    public static void b(Minecraft minecraft) {
        if (j == cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.a) {
            return;
        }
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null || localPlayer != Y || minecraft.level != X) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.e();
            return;
        }
        ++k;
        for (Map.Entry<String, float[]> entry : z.entrySet()) {
            float[] fArray2 = entry.getValue();
            fArray2[0] = fArray2[1];
            float f2 = entry.getKey().equals(y) && cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a() ? 1.0f : 0.0f;
            fArray2[1] = fArray2[1] + (f2 - fArray2[1]) * 0.5f;
        }
        if (y != null && cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a()) {
            z.computeIfAbsent(y, string -> new float[2]);
        }
        z.values().removeIf(fArray -> fArray[0] < 0.002f && fArray[1] < 0.002f && !cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a());
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.R[0] = R[1];
        R[1] = R[1] + ((cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b() ? 1.0f : 0.0f) - R[1]) * 0.6f;
        J = I;
        I += (K - I) * 0.45f;
        if (Math.abs(K - I) < 0.05f) {
            I = K;
        }
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(localPlayer);
        if (G != null && !minecraft.isWindowActive()) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(0);
        }
        if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a()) {
            boolean bl;
            boolean bl2 = bl = localPlayer.hurtTime > aa || localPlayer.getHealth() < Z - 0.01f || localPlayer.isDeadOrDying();
            if (bl || minecraft.screen instanceof DeathScreen) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(localPlayer);
            } else if (minecraft.screen != null) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.d();
            } else if (Q >= 0 && k >= Q) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.d();
            }
        }
        Z = localPlayer.getHealth();
        aa = localPlayer.hurtTime;
        if (y != null && !y.equals(O) && cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a() && k - P >= 2 && !y.equals("info")) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.S, 1.0f, 0.25f);
            P = k;
        }
        O = y;
        if (q >= 0 && cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a() && k == (int)Math.ceil(r)) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.T, 1.0f, 0.45f);
        }
        if (w != null && k - cyou.rimuru.shootingstardemo.mc1211.client.magic.h.w.j >= 5) {
            w = null;
        }
        if (j == cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.b && k - l >= 10) {
            j = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.c;
            l = k;
        } else if (j == cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.d && k - l >= 8 || j == cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.e && k - l >= 14) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.c(minecraft);
        }
    }

    public static void a(int n2, int n3, int n4) {
        if (!cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a()) {
            return;
        }
        if (x != null) {
            if (n2 == 259 || n2 == 261) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.f.a(x, InputConstants.UNKNOWN);
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.X, 0.8f, 0.45f);
            } else if (n2 != 256) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.f.a(x, InputConstants.getKey((int)n2, (int)n3));
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.X, 1.0f, 0.45f);
            }
            x = null;
            return;
        }
        if (C != null) {
            return;
        }
        if (n2 == 256) {
            if (v != null) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.r();
            } else {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.d();
            }
            return;
        }
        if (cyou.rimuru.shootingstardemo.mc1211.client.magic.f.a.matches(n2, n3)) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.d();
            return;
        }
        if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(y) && v == null && (n2 == 263 || n2 == 262)) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(y, n2 == 262 ? 1 : -1);
            return;
        }
        if (L > 0.0f && v == null && (n2 == 265 || n2 == 264)) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.c(n2 == 264 ? 1 : -1);
            return;
        }
        if (L > 0.0f && v == null && (n2 == 266 || n2 == 267)) {
            int n5 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(K);
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.c(n2 == 267 ? n5 : -n5);
            return;
        }
        if (q >= 0) {
            for (d d2 : cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b((int)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q).c) {
                KeyMapping keyMapping = cyou.rimuru.shootingstardemo.mc1211.client.magic.f.a(d2);
                if (keyMapping == null || !keyMapping.matches(n2, n3)) continue;
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(d2, "skill:" + d2.a());
                return;
            }
        }
    }

    public static void a(int n2, int n3) {
        String string;
        if (!cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a()) {
            return;
        }
        int n4 = n2;
        if (n4 == 0 && Util.getPlatform() == Util.OS.OSX && (n3 & 2) != 0) {
            n4 = 1;
        }
        if (x != null) {
            if (n4 > 2) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.f.a(x, InputConstants.Type.MOUSE.getOrCreate(n4));
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.X, 1.0f, 0.45f);
            }
            x = null;
            return;
        }
        if (C != null) {
            return;
        }
        if (cyou.rimuru.shootingstardemo.mc1211.client.magic.f.a.matchesMouse(n4)) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.d();
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        y = string = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight(), 1.0f);
        A = string;
        B = k;
        if (n4 == 0) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.e(string);
        } else if (n4 == 1) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.f(string);
        }
    }

    private static void e(String string) {
        if (string == null) {
            if (v != null) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.r();
            }
            return;
        }
        if (string.equals("switch:cutscenes")) {
            boolean bl = !cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b();
            cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(bl);
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(bl ? cyou.rimuru.shootingstardemo.mc1211.registry.g.X : cyou.rimuru.shootingstardemo.mc1211.registry.g.W, bl ? 1.2f : 0.9f, 0.4f);
            return;
        }
        if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(string)) {
            G = string;
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(string, Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight(), 1.0f));
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.W, 1.3f, 0.3f);
            return;
        }
        if (string.startsWith("item:")) {
            int n2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.c(string.substring(5));
            if (n2 < 0 || n2 == q && t != n2) {
                return;
            }
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.d(n2);
        } else if (string.startsWith("skill:")) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.b b2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.b.a(string.substring(6));
            if (b2 != null) {
                E = string;
                F = k;
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.Y, 1.0f, 0.35f);
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(b2, q, string);
                return;
            }
            d d2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.g(string.substring(6));
            if (d2 != null) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(d2, string);
            }
        } else if (string.equals("info:setkey") && cyou.rimuru.shootingstardemo.mc1211.client.magic.h.s()) {
            x = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.v.a;
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.W, 1.0f, 0.45f);
        } else if (string.equals("info:select") && cyou.rimuru.shootingstardemo.mc1211.client.magic.h.s()) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.v.a, string);
        } else if (string.equals("info:close")) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.r();
        }
    }

    private static void f(String string) {
        if (string == null) {
            if (v != null) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.r();
            }
            return;
        }
        if (string.startsWith("item:")) {
            int n2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.c(string.substring(5));
            if (n2 >= 0) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(null, n2, string);
            }
        } else if (string.startsWith("skill:")) {
            d b2;
            cyou.rimuru.shootingstardemo.mc1211.client.magic.b b3 = cyou.rimuru.shootingstardemo.mc1211.client.magic.b.a(string.substring(6));
            d d2 = b2 = b3 != null ? b3 : cyou.rimuru.shootingstardemo.mc1211.client.magic.h.g(string.substring(6));
            if (b2 != null) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(b2, q, string);
            }
        }
    }

    public static void a(int n2) {
        if (G != null && n2 == 0) {
            G = null;
            cyou.rimuru.shootingstardemo.mc1211.client.magic.g.a();
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.X, 1.2f, 0.3f);
        }
    }

    public static void a(double d2) {
        if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a() && G == null && v == null && L > 0.0f && (!cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(y) || k - W < 10)) {
            int n2 = (int)(V += d2);
            V -= (double)n2;
            if (n2 != 0) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.c(-n2);
            }
            return;
        }
        if (!(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a() && G == null && v == null && cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(y) && y.equals(T))) {
            S = 0.0;
            T = y;
        }
        if (!cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a() || G != null || v != null || !cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(y)) {
            return;
        }
        int n3 = (int)(S += d2);
        S -= (double)n3;
        for (int i2 = 0; i2 < Math.min(Math.abs(n3), 5); ++i2) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(y, n3 > 0 ? 1 : -1);
        }
    }

    private static void c(int n2) {
        W = k;
        float f2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(K, n2);
        if (f2 != K) {
            K = f2;
            if (k - U >= 1) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.S, 1.0f, 0.18f);
                U = k;
            }
        }
    }

    static float a(float f2) {
        return Mth.lerp((float)f2, (float)R[0], (float)R[1]);
    }

    static boolean a(String string) {
        return string != null && (string.startsWith("power:") || string.startsWith("damage:") || string.startsWith("speed:"));
    }

    static d b(String string) {
        return cyou.rimuru.shootingstardemo.mc1211.client.magic.h.g(string.substring(string.indexOf(58) + 1));
    }

    static void b(float f2) {
        if (G == null || Float.isNaN(f2)) {
            return;
        }
        d d2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(G);
        if (d2 == null || d2.j() == null) {
            return;
        }
        boolean bl = G.startsWith("power:") ? cyou.rimuru.shootingstardemo.mc1211.client.magic.g.b(d2, d2.j().c(f2)) : (G.startsWith("speed:") ? cyou.rimuru.shootingstardemo.mc1211.client.magic.g.a(d2, cyou.rimuru.shootingstardemo.mc1211.magic.f.c.c(f2)) : cyou.rimuru.shootingstardemo.mc1211.client.magic.g.a(d2, Math.round(f2 * (float)(cyou.rimuru.shootingstardemo.mc1211.magic.f.b.length - 1))));
        if (bl) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.e(f2);
        }
    }

    private static void a(String string, int n2) {
        d d2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(string);
        if (d2 == null || d2.j() == null) {
            return;
        }
        boolean bl = string.startsWith("power:") ? cyou.rimuru.shootingstardemo.mc1211.client.magic.g.b(d2, d2.j().a(cyou.rimuru.shootingstardemo.mc1211.client.magic.g.b(d2), n2)) : (string.startsWith("speed:") ? cyou.rimuru.shootingstardemo.mc1211.client.magic.g.a(d2, cyou.rimuru.shootingstardemo.mc1211.magic.f.c.a(cyou.rimuru.shootingstardemo.mc1211.client.magic.g.e(d2), n2)) : cyou.rimuru.shootingstardemo.mc1211.client.magic.g.a(d2, cyou.rimuru.shootingstardemo.mc1211.magic.f.a(cyou.rimuru.shootingstardemo.mc1211.client.magic.g.d(d2)) + n2));
        if (bl) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.e(cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(string));
            cyou.rimuru.shootingstardemo.mc1211.client.magic.g.a();
        }
    }

    private static void e(float f2) {
        H = k;
        if (k - U >= 1) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.S, 0.8f + 0.8f * Mth.clamp((float)f2, (float)0.0f, (float)1.0f), 0.22f);
            U = k;
        }
    }

    private static void d(int n2) {
        float f2 = k;
        t = q;
        u = f2;
        q = n2;
        r = f2;
        s = t >= 0 ? f2 + 3.0f : f2;
        K = 0.0f;
        J = 0.0f;
        I = 0.0f;
    }

    private static void a(d d2, String string) {
        int n2 = g.indexOf((Object)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.magic.e.b(d2)));
        if (n2 < 0 || !i[n2]) {
            E = string;
            F = k;
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.Y, 1.0f, 0.5f);
            return;
        }
        cyou.rimuru.shootingstardemo.mc1211.client.magic.a.a(d2);
        C = string;
        D = k;
        Q = k + 4;
    }

    private static void a(d d2, int n2, String string) {
        l.a a2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(string);
        Vector3f vector3f = a2 != null ? new Vector3f((Vector3fc)a2.b) : cyou.rimuru.shootingstardemo.mc1211.client.magic.l.a(0.0f, 0.0f, 1.88f);
        int n3 = a2 != null ? a2.e : 40;
        int n4 = a2 != null ? a2.f : 20;
        Minecraft minecraft = Minecraft.getInstance();
        float f2 = Mth.wrapDegrees((float)(minecraft.player.getViewYRot(1.0f) - m));
        float f3 = minecraft.player.getViewXRot(1.0f);
        float f4 = Mth.clamp((float)(f2 * 0.7f), (float)-24.0f, (float)24.0f) / 0.17f;
        float f5 = Mth.clamp((float)(-f3 * 0.7f), (float)-10.0f, (float)10.0f) / 0.17f;
        if (v != null) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.v.j = k;
            w = v;
        }
        v = new a(d2, n2, string, vector3f, n3, n4, f4, f5, k);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.U, 1.0f, 0.45f);
    }

    private static void r() {
        if (v == null) {
            return;
        }
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.v.j = k;
        w = v;
        v = null;
        x = null;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.V, 1.0f, 0.4f);
    }

    public static void f() {
        if (j == cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.e) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.j.a();
        }
    }

    public static void a(GuiGraphics guiGraphics, float f2) {
        if (j == cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.a) {
            return;
        }
        cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(guiGraphics, f2);
    }

    static float[] c(float f2) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (!cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a() || localPlayer == null) {
            return new float[]{o, p};
        }
        return new float[]{Mth.wrapDegrees((float)(localPlayer.getViewYRot(f2) - m)), localPlayer.getViewXRot(f2)};
    }

    public static double[] a(double d2, double d3) {
        float f2;
        double d4;
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null || !cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a()) {
            return new double[]{d2, d3};
        }
        float f3 = Mth.wrapDegrees((float)(localPlayer.getYRot() - m));
        double d5 = (double)f3 + d2 * 0.15;
        double d6 = Math.sqrt(d5 * d5 / 3025.0 + (d4 = (double)(f2 = localPlayer.getXRot()) + d3 * 0.15) * d4 / 2025.0);
        if (d6 > 1.0) {
            d5 /= d6;
            d4 /= d6;
        }
        return new double[]{(d5 - (double)f3) / 0.15, (d4 - (double)f2) / 0.15};
    }

    static float a(String string, float f2) {
        float[] fArray = z.get(string);
        return fArray == null ? 0.0f : Mth.lerp((float)f2, (float)fArray[0], (float)fArray[1]);
    }

    static List<cyou.rimuru.shootingstardemo.mc1211.magic.b> g() {
        if (g.isEmpty()) {
            for (Item item : BuiltInRegistries.ITEM) {
                if (!(item instanceof cyou.rimuru.shootingstardemo.mc1211.magic.b)) continue;
                cyou.rimuru.shootingstardemo.mc1211.magic.b b2 = (cyou.rimuru.shootingstardemo.mc1211.magic.b)item;
                g.add(b2);
                h.add(new ItemStack((ItemLike)b2));
            }
            i = new boolean[g.size()];
        }
        return g;
    }

    static e b(int n2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.magic.h.g.get((int)n2).d;
    }

    private static cyou.rimuru.shootingstardemo.mc1211.magic.b a(e e2) {
        for (cyou.rimuru.shootingstardemo.mc1211.magic.b b2 : g) {
            if (b2.d != e2) continue;
            return b2;
        }
        return null;
    }

    static int c(String string) {
        for (int i2 = 0; i2 < g.size(); ++i2) {
            if (!cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b((int)i2).a.getPath().equals(string)) continue;
            return i2;
        }
        return -1;
    }

    private static boolean s() {
        return v != null && cyou.rimuru.shootingstardemo.mc1211.client.magic.h.v.a != null && !(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.v.a instanceof cyou.rimuru.shootingstardemo.mc1211.client.magic.b);
    }

    private static d g(String string) {
        if (q < 0) {
            return null;
        }
        for (d d2 : cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b((int)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q).c) {
            if (!d2.a().equals(string)) continue;
            return d2;
        }
        return null;
    }

    private static void b(LocalPlayer localPlayer) {
        Arrays.fill(i, false);
        for (int i2 = 0; i2 < localPlayer.getInventory().getContainerSize(); ++i2) {
            cyou.rimuru.shootingstardemo.mc1211.magic.b b2;
            int n2;
            ItemStack itemStack = localPlayer.getInventory().getItem(i2);
            Item item = itemStack.getItem();
            if (!(item instanceof cyou.rimuru.shootingstardemo.mc1211.magic.b) || (n2 = g.indexOf((Object)(b2 = (cyou.rimuru.shootingstardemo.mc1211.magic.b)item))) < 0) continue;
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.i[n2] = true;
        }
    }

    private static int c(LocalPlayer localPlayer) {
        for (ItemStack itemStack : new ItemStack[]{localPlayer.getMainHandItem(), localPlayer.getOffhandItem()}) {
            cyou.rimuru.shootingstardemo.mc1211.magic.b b2;
            Item item = itemStack.getItem();
            if (!(item instanceof cyou.rimuru.shootingstardemo.mc1211.magic.b) || !g.contains((Object)(b2 = (cyou.rimuru.shootingstardemo.mc1211.magic.b)item))) continue;
            return g.indexOf((Object)b2);
        }
        for (int i2 = 0; i2 < g.size(); ++i2) {
            if (!i[i2]) continue;
            return i2;
        }
        return 0;
    }

    static void a(SoundEvent soundEvent, float f2, float f3) {
        Minecraft.getInstance().getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)soundEvent, (float)f2, (float)f3));
    }

    public static float[] d(String string) {
        Vector3f vector3f = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(string);
        if (vector3f == null) {
            return null;
        }
        float[] fArray = cyou.rimuru.shootingstardemo.mc1211.client.magic.l.a(vector3f);
        return new float[]{m + fArray[0], fArray[1]};
    }

    public static float[] b(String string, float f2) {
        Vector3f vector3f = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(string, f2);
        if (vector3f == null) {
            return null;
        }
        float[] fArray = cyou.rimuru.shootingstardemo.mc1211.client.magic.l.a(vector3f);
        return new float[]{m + fArray[0], fArray[1]};
    }

    public static String h() {
        return G;
    }

    public static String i() {
        return y;
    }

    public static e j() {
        return q >= 0 && q < g.size() ? cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(q) : null;
    }

    public static d k() {
        return v == null ? null : cyou.rimuru.shootingstardemo.mc1211.client.magic.h.v.a;
    }

    public static d l() {
        return x;
    }

    public static String m() {
        return j.name();
    }

    public static float n() {
        return L;
    }

    public static float o() {
        return I;
    }

    public static void d(float f2) {
        cyou.rimuru.shootingstardemo.mc1211.client.magic.k.o = f2;
    }

    static {
        q = -1;
        t = -1;
        P = -10;
        z = new HashMap<String, float[]>();
        B = -100;
        D = -100;
        Q = -1;
        F = -100;
        R = new float[2];
        H = -100;
        U = -10;
        W = -100;
    }

    static enum Phase {
        a,
        b,
        c,
        d,
        e
    }

    static final class a {
        final d a;
        final int b;
        final String c;
        final Vector3f d;
        final int e;
        final int f;
        final float g;
        final float h;
        final int i;
        int j = -1;

        a(d d2, int n2, String string, Vector3f vector3f, int n3, int n4, float f2, float f3, int n5) {
            this.a = d2;
            this.b = n2;
            this.c = string;
            this.d = vector3f;
            this.e = n3;
            this.f = n4;
            this.g = f2;
            this.h = f3;
            this.i = n5;
        }
    }
}

