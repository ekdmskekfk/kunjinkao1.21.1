/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.magic;

import cyou.rimuru.shootingstardemo.mc1211.ShootingStarDemo;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.c;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.d;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.f;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.g;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.h;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.l;
import cyou.rimuru.shootingstardemo.mc1211.magic.e;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

final class k {
    static final int a = 0;
    static final int b = 1;
    static final int c = 2;
    static final int d = 3;
    static final int e = 4;
    static final int f = 5;
    static final int g = 6;
    static final int h = 7;
    static final int i = 8;
    static final int j = 9;
    static final int k = 10;
    static final int l = 11;
    static final int m = 12;
    static final int n = 13;
    private static final int p = 15026253;
    private static final int q = 30;
    private static final int r = 14;
    private static final int s = 22;
    private static final int t = -17;
    private static final int u = 26;
    private static final int v = 26;
    private static final int w = -21;
    private static final int x = 16724808;
    private static final int y = 4;
    private static final int z = 156;
    private static final int A = 22;
    private static final int B = 26;
    private static final int C = 64;
    private static final int D = 167;
    private static final int E = 4;
    private static final int F = 16;
    private static final int G = 12;
    private static final int H = 148;
    private static final int I = 11;
    private static final int[] J = new int[]{18, 30, 42};
    private static final String[] K = new String[]{"power:", "damage:", "speed:"};
    private static final int L = 37;
    private static final int M = 40;
    private static final float N = 12.0f;
    private static final int O = 44;
    private static final int P = 68;
    private static final int Q = -99;
    private static final int R = 120;
    private static final int S = 16;
    private static final int T = 6;
    private static final int U = 200;
    private static final float V = 1.62f;
    private static final float W = 0.1f;
    private static final float X = 0.04f;
    private static final float Y = 0.05f;
    private static final float Z = 0.1f;
    private static final int aa = 58;
    private static final int ab = 16;
    private static final int ac = 104;
    private static final int ad = 180;
    private static final int ae = 9;
    private static final int af = 6;
    private static List<l.a> ag = List.of();
    private static a[] ah;
    private static float ai;
    private static float aj;
    static float o;

    private k() {
    }

    static void a() {
        ag = List.of();
        ah = null;
    }

    static void a(GuiGraphics guiGraphics, float f2) {
        int n2 = guiGraphics.guiWidth();
        int n3 = guiGraphics.guiHeight();
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics);
        float f3 = (float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.k + f2;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a((float)Minecraft.getInstance().getWindow().getHeight() * 0.5f * (float)(Math.tan(Math.toRadians(0.17f)) / Math.tan(Math.toRadians(35.0))));
        float[] fArray = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.c(f2);
        l l2 = new l(fArray[0], fArray[1], n2, n3);
        List<l.a> list = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(l2, f3, f2);
        ag = list;
        if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a()) {
            if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.G != null) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(list, cyou.rimuru.shootingstardemo.mc1211.client.magic.h.G, (float)n2, (float)n3));
            }
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.y = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.G != null ? cyou.rimuru.shootingstardemo.mc1211.client.magic.h.G : cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(list, (float)n2, (float)n3);
        }
        cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(guiGraphics, n2, n3, f3, f2);
        Font font = Minecraft.getInstance().font;
        ArrayList<l.a> arrayList = new ArrayList<l.a>(list);
        arrayList.sort(Comparator.comparingDouble((l.a a2) -> a2.v).reversed());
        for (l.a a3 : arrayList) {
            if (a3.t == null || a3.i <= 0.004f) continue;
            cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().mulPose(cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(a3.t));
            switch (a3.g) {
                case 0: {
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(guiGraphics, font, a3);
                    break;
                }
                case 8: {
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(guiGraphics, a3, f3);
                    break;
                }
                case 9: {
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(guiGraphics, font, a3);
                    break;
                }
                case 1: {
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(guiGraphics, a3, f3, f2);
                    break;
                }
                case 2: {
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(guiGraphics, font, a3);
                    break;
                }
                case 3: {
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.k.d(guiGraphics, font, a3);
                    break;
                }
                case 4: {
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(guiGraphics, a3);
                    break;
                }
                case 5: {
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(guiGraphics, font, a3, f3, f2);
                    break;
                }
                case 6: {
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(guiGraphics, font, a3, f3, f2);
                    break;
                }
                case 7: {
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(guiGraphics, font, a3, f2);
                    break;
                }
                case 10: {
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(guiGraphics, font, a3, f2);
                    break;
                }
                case 11: {
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(guiGraphics, font, a3, f2);
                    break;
                }
                case 13: {
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.k.e(guiGraphics, font, a3);
                    break;
                }
                case 12: {
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(guiGraphics, a3);
                    break;
                }
            }
            guiGraphics.pose().popPose();
        }
        cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(guiGraphics, n2, n3, f3, f2);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(guiGraphics, font, n2, n3, f3);
    }

    static String a(int n2, int n3, float f2) {
        float[] fArray = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.c(f2);
        l l2 = new l(fArray[0], fArray[1], n2, n3);
        return cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(l2, (float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.k + f2, f2), (float)n2, (float)n3);
    }

    static l.a a(String string) {
        for (l.a a2 : ag) {
            if (!a2.a.equals(string)) continue;
            return a2;
        }
        return null;
    }

    static float a(String string, int n2, int n3, float f2) {
        float[] fArray = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.c(f2);
        l l2 = new l(fArray[0], fArray[1], n2, n3);
        return cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(l2, (float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.k + f2, f2), string, (float)n2, (float)n3);
    }

    private static float a(List<l.a> list, String string, float f2, float f3) {
        for (l.a a2 : list) {
            if (!a2.a.equals(string)) continue;
            Vector2f vector2f = a2.b(f2 * 0.5f, f3 * 0.5f);
            return vector2f == null ? Float.NaN : (vector2f.x - 44.0f) / 68.0f;
        }
        return Float.NaN;
    }

    static float b(String string) {
        cyou.rimuru.shootingstardemo.mc1211.magic.d d2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(string);
        return d2 == null || d2.j() == null ? 0.0f : cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(d2, cyou.rimuru.shootingstardemo.mc1211.client.magic.k.d(string));
    }

    private static int d(String string) {
        return string.contains("power:") ? 0 : (string.contains("speed:") ? 2 : 1);
    }

    private static float a(cyou.rimuru.shootingstardemo.mc1211.magic.d d2, int n2) {
        return switch (n2) {
            case 0 -> d2.j().b(cyou.rimuru.shootingstardemo.mc1211.client.magic.g.b(d2));
            case 2 -> cyou.rimuru.shootingstardemo.mc1211.magic.f.c.b(cyou.rimuru.shootingstardemo.mc1211.client.magic.g.e(d2));
            default -> (float)cyou.rimuru.shootingstardemo.mc1211.magic.f.a(cyou.rimuru.shootingstardemo.mc1211.client.magic.g.d(d2)) / (float)(cyou.rimuru.shootingstardemo.mc1211.magic.f.b.length - 1);
        };
    }

    static Vector3f a(String string, float f2) {
        l l2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b();
        for (l.a a2 : cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(l2, (float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.k + 1.0f, 1.0f)) {
            if (!a2.a.equals(string)) continue;
            return a2.a(44.0f + f2 * 68.0f, (float)a2.f * 0.5f);
        }
        return null;
    }

    private static l b() {
        Minecraft minecraft = Minecraft.getInstance();
        return new l(0.0f, 0.0f, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight());
    }

    static Vector3f c(String string) {
        l l2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b();
        List<l.a> list = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(l2, (float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.k + 1.0f, 1.0f);
        for (l.a a2 : list) {
            if (!a2.a.equals(string)) continue;
            return new Vector3f((Vector3fc)a2.b);
        }
        return null;
    }

    private static List<l.a> a(l l2, float f2, float f3) {
        float f4;
        int n2;
        Object object;
        int n3;
        Font font = Minecraft.getInstance().font;
        ArrayList<l.a> arrayList = new ArrayList<l.a>();
        h.Phase b2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.j;
        float f5 = f2 - (float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.l;
        float f6 = b2 == cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.d ? cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(f5 / 4.0f) : 0.0f;
        float f7 = b2 == cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.e ? cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(f5 / 2.0f) : 0.0f;
        float f8 = 1.0f - f7;
        float f9 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.i(f2);
        float f10 = 1.0f - 0.45f * f9;
        float f11 = 0.1f * f9;
        float f12 = 1.88f;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(l2);
        float f13 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.h(f3);
        b b3 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q >= 0 ? new b(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q, f13) : null;
        float[] fArray = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(font, b3);
        float f14 = fArray[0] + 22.0f;
        String string = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.e(Component.translatable((String)"gui.shooting_star_demo.void.title").getString());
        float f15 = (float)(-font.width((FormattedText)cyou.rimuru.shootingstardemo.mc1211.client.magic.c.d(string))) * 0.5f;
        for (int i2 = 0; i2 < string.length(); i2 += Character.charCount(n3)) {
            n3 = string.codePointAt(i2);
            MutableComponent mutableComponent = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.d(new String(Character.toChars(n3)));
            int n4 = font.width((FormattedText)mutableComponent);
            if (n3 != 32) {
                object = new l.a("title:" + i2, cyou.rimuru.shootingstardemo.mc1211.client.magic.l.a(f15 + (float)n4 * 0.5f, f14, f12), n4 + 2, 30);
                ((l.a)object).g = 0;
                ((l.a)object).s = mutableComponent;
                ((l.a)object).i = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - 7.0f - 0.35f * (float)i2) / 3.0f) * f8 * f10 * (1.0f - f6);
                ((l.a)object).a(-f11);
                arrayList.add((l.a)object);
            }
            f15 += (float)n4;
        }
        float f16 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - 6.0f) / 8.0f) * 14.0f;
        for (n3 = 0; n3 < 14 && f16 > (float)n3; ++n3) {
            float f17 = ((float)n3 + 0.5f - 7.0f) * 22.0f;
            l.a a2 = new l.a("rule:" + n3, cyou.rimuru.shootingstardemo.mc1211.client.magic.l.a(f17, f14 - 9.0f, f12), 22, 6);
            a2.g = 8;
            a2.q = 1.0f;
            a2.h = n3;
            a2.j = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(f16 - (float)n3);
            a2.i = 0.85f * f8 * f10 * (1.0f - f6);
            a2.a(-f11);
            arrayList.add(a2);
        }
        n3 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.g.size();
        for (n2 = 0; n2 < n3; ++n2) {
            float f18;
            float f19 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - 8.0f - 0.6f * (float)n2) / 5.0f);
            if (f19 <= 0.0f) continue;
            object = "item:" + cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b((int)n2).a.getPath();
            float f20 = b2 == cyou.rimuru.shootingstardemo.mc1211.client.magic.h.Phase.d ? cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f5 - (float)(n3 - 1 - n2) * 0.25f) / 2.0f) : 0.0f;
            f4 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a((String)object, f3);
            l.a a3 = new l.a((String)object, cyou.rimuru.shootingstardemo.mc1211.client.magic.l.a(-17.0f, cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c() - (float)(26 * n2), f12), 26, 26);
            a3.g = 1;
            a3.h = n2;
            a3.r = true;
            a3.p = f19 > 0.6f;
            a3.k = f18 = Mth.lerp((float)cyou.rimuru.shootingstardemo.mc1211.client.magic.k.g(f19), (float)0.4f, (float)1.0f) * (1.0f - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.e(f20));
            a3.l = f18;
            a3.i = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(f19 * 3.0f) * f8 * f10 * (1.0f - f20);
            a3.a(0.1f * f4 + 0.05f * cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(n2, f2) - 0.25f * (1.0f - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.d(f19)) - f11 - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b((String)object, f2));
            arrayList.add(a3);
            if (!(f4 > 0.02f) || n2 == cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q) continue;
            MutableComponent mutableComponent = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.h.get(n2).getHoverName());
            int n5 = font.width((FormattedText)mutableComponent) + 14;
            l.a a4 = new l.a("label", cyou.rimuru.shootingstardemo.mc1211.client.magic.l.a(-36.0f - (float)n5 * 0.5f, cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c() - (float)(26 * n2), f12), n5, 14);
            a4.g = 2;
            a4.s = mutableComponent;
            a4.i = f4 * f8;
            a4.a(0.1f * f4 + 0.06f);
            arrayList.add(a4);
        }
        n2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q;
        int n6 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.t;
        if (n6 >= 0 && n6 != n2 && f2 - cyou.rimuru.shootingstardemo.mc1211.client.magic.h.u < 4.0f) {
            float f21 = 1.0f - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - cyou.rimuru.shootingstardemo.mc1211.client.magic.h.u) / 4.0f);
            cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(arrayList, font, n6, f21, f21, f8 * f10, f11, f6, "panel:prev");
            cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(arrayList, new b(n6, 0.0f), null, f21, f8 * f10, f11, f6, f2, f3);
        }
        if (n2 >= 0) {
            float f22 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - cyou.rimuru.shootingstardemo.mc1211.client.magic.h.s - 1.0f) / 6.0f);
            cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(arrayList, font, n2, f22, cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - cyou.rimuru.shootingstardemo.mc1211.client.magic.h.s - 4.0f) / 3.0f), f8 * f10, f11, f6, "panel");
            cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(arrayList, font, n2, f2, f8 * f10, f11, f6, f3);
            l.a a5 = new l.a("notch", cyou.rimuru.shootingstardemo.mc1211.client.magic.l.a(0.0f, cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c() - (float)(26 * n2), f12), 6, 10);
            a5.g = 4;
            a5.i = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - cyou.rimuru.shootingstardemo.mc1211.client.magic.h.s - 1.0f) / 3.0f) * f8 * f10 * (1.0f - f6);
            a5.a(-f11);
            arrayList.add(a5);
            cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(arrayList, b3, "skill:", -1.0f, f8 * f10, f11, f6, f2, f3);
            if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.L > 0.0f) {
                f4 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - cyou.rimuru.shootingstardemo.mc1211.client.magic.h.s - 2.0f) / 5.0f);
                cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(arrayList, b3, cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(f4 * 2.5f) * f8 * f10 * (1.0f - f6));
            }
        }
        Component component = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.e();
        l.a a6 = new l.a("credit", cyou.rimuru.shootingstardemo.mc1211.client.magic.l.a(0.0f, fArray[1] - 18.0f, f12), font.width((FormattedText)component) + 16, 14);
        a6.g = 9;
        a6.s = component;
        a6.i = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - 13.0f - 6.0f) / 6.0f) * f8 * f10 * (1.0f - f6);
        a6.a(-f11);
        arrayList.add(a6);
        if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.w != null) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(arrayList, font, cyou.rimuru.shootingstardemo.mc1211.client.magic.h.w, f2, f8, f6, f3);
        }
        if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.v != null) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(arrayList, font, cyou.rimuru.shootingstardemo.mc1211.client.magic.h.v, f2, f8, f6, f3);
        }
        for (l.a a3 : arrayList) {
            l2.a(a3);
        }
        return arrayList;
    }

    private static float c() {
        return -21.0f + 13.0f * (float)(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.g.size() - 1);
    }

    private static float[] a(Font font, b b2) {
        int n2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.g.size();
        float f2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c() + 13.0f;
        float f3 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c() - (float)(26 * (n2 - 1)) - 13.0f;
        int n3 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q;
        if (n3 >= 0) {
            int n4 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(null, font, n3, 1.0f, 1.0f);
            float f4 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c() - (float)(26 * n3), n4);
            f2 = Math.max(f2, b2.g);
            f3 = Math.min(f3, b2.g - b2.h);
            f2 = Math.max(f2, f4 + (float)n4 * 0.5f);
            f3 = Math.min(f3, f4 - (float)n4 * 0.5f - 6.0f - 16.0f);
        }
        return new float[]{f2, f3};
    }

    private static float b(float f2, int n2) {
        return Mth.clamp((float)f2, (float)(-175.0f + (float)n2 * 0.5f + 6.0f + 16.0f), (float)(133.0f - (float)n2 * 0.5f));
    }

    private static void a(List<l.a> list, Font font, int n2, float f2, float f3, float f4, float f5, float f6) {
        float f7 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - cyou.rimuru.shootingstardemo.mc1211.client.magic.h.s - 6.0f) / 4.0f);
        if (f7 <= 0.0f) {
            return;
        }
        int n3 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(null, font, n2, 1.0f, 1.0f);
        float f8 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c() - (float)(26 * n2), n3);
        String string = "switch:cutscenes";
        l.a a2 = new l.a(string, cyou.rimuru.shootingstardemo.mc1211.client.magic.l.a(-99.0f, f8 - (float)n3 * 0.5f - 6.0f - 8.0f, 1.88f), 120, 16);
        a2.g = 11;
        a2.n = 0.0f;
        a2.l = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.d(f7) * (1.0f - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.e(f5));
        a2.i = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(f7 * 2.5f) * f3 * (1.0f - f5);
        a2.p = f7 > 0.6f;
        float f9 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(string, f6);
        a2.a(0.05f * f9 - f4 - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(string, f2));
        list.add(a2);
    }

    private static void a(GuiGraphics guiGraphics, Font font, l.a a2, float f2) {
        float f3 = a2.i;
        float f4 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(a2.a, f2);
        boolean bl = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b();
        float f5 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(f2);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(guiGraphics, 0, 2, a2.e, a2.f, 1712688, 0.12f * f3, 1, false);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0, 0, a2.e, a2.f, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(0xFBFBFC, 0xFFFFFF, f4), f3, 1, true);
        int n2 = a2.e - 30;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, n2, 3, 22, 10, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(15659251, 16037439, f5), f3, 3, true);
        int n3 = n2 + 1 + Math.round(12.0f * f5);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, n3, 4, 8, 0xFBFBFC, f3, 4);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, n3, 4, 8, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(13225686, 16037439, f4), f3, 1);
        if (f3 > 0.02f) {
            MutableComponent mutableComponent = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)Component.translatable((String)"gui.shooting_star_demo.void.cutscenes"));
            guiGraphics.drawString(font, (Component)mutableComponent, 8, 5, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f3, 3883080), false);
            MutableComponent mutableComponent2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)Component.translatable((String)(bl ? "gui.shooting_star_demo.void.on" : "gui.shooting_star_demo.void.off")));
            guiGraphics.drawString(font, (Component)mutableComponent2, n2 - 4 - font.width((FormattedText)mutableComponent2), 5, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f3, bl ? 3883080 : 9080985), false);
        }
    }

    private static int a(cyou.rimuru.shootingstardemo.mc1211.magic.d d2) {
        return d2.j() != null ? 64 : 26;
    }

    private static float b(cyou.rimuru.shootingstardemo.mc1211.magic.d d2) {
        return d2.j() != null ? 37.0f : 0.0f;
    }

    private static float d() {
        return ai - aj;
    }

    private static void a(l l2) {
        float f2 = 205.88235f;
        ai = f2 - 41.0f;
        float f3 = 16.0f / (l2.k * (float)Math.tan(Math.toRadians(0.17f)));
        aj = -f2 + f3 + 28.0f;
        if (!Float.isNaN(o)) {
            float f4 = Math.max(0.0f, (ai - aj - o) * 0.5f);
            ai -= f4;
            aj += f4;
        }
    }

    private static float h(float f2) {
        float f3;
        b b2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q >= 0 ? new b(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q, 0.0f) : null;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.L = f3 = b2 == null ? 0.0f : b2.a();
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.I = Mth.clamp((float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.I, (float)0.0f, (float)f3);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.J = Mth.clamp((float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.J, (float)0.0f, (float)f3);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.K = Mth.clamp((float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.K, (float)0.0f, (float)f3);
        if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.M && f3 > 0.0f) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.M = false;
            cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(b2);
        }
        return Mth.lerp((float)f2, (float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.J, (float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.I);
    }

    private static void a(b b2) {
        int n2;
        int n3;
        e e2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(b2.a);
        cyou.rimuru.shootingstardemo.mc1211.magic.d d2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.i[b2.a] ? cyou.rimuru.shootingstardemo.mc1211.client.magic.a.a(e2) : null;
        int n4 = n3 = d2 == null ? -1 : b2.b.indexOf(d2);
        if (n3 < 0) {
            return;
        }
        for (n2 = 0; n2 < n3 && b2.d[n3] - b2.c[n2] > cyou.rimuru.shootingstardemo.mc1211.client.magic.k.d(); ++n2) {
        }
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.J = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.K = b2.c[n2];
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.I = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.K;
    }

    static float a(float f2, int n2) {
        if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q < 0) {
            return 0.0f;
        }
        b b2 = new b(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q, f2);
        return b2.c[Mth.clamp((int)(b2.a(f2) + n2), (int)0, (int)b2.e)];
    }

    static int a(float f2) {
        if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q < 0) {
            return 1;
        }
        b b2 = new b(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q, f2);
        int n2 = b2.a(f2);
        int n3 = 1;
        while (n2 + n3 < b2.d.length && b2.d[n2 + n3] - b2.c[n2] <= cyou.rimuru.shootingstardemo.mc1211.client.magic.k.d()) {
            ++n3;
        }
        return n3;
    }

    private static Component e() {
        String string = ShootingStarDemo.DEMO_NOTICE;
        String string2 = ShootingStarDemo.DEMO_HANDLE;
        int n2 = string.indexOf(string2);
        if (n2 < 0) {
            return cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c(string);
        }
        return cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)Component.literal((String)string.substring(0, n2)).append((Component)Component.literal((String)string2).withColor(16724808)).append((Component)Component.literal((String)string.substring(n2 + string2.length()))));
    }

    private static void a(GuiGraphics guiGraphics, Font font, l.a a2) {
        Component component = (Component)a2.s;
        if (a2.i > 0.02f) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 8, 0, a2.e - 16, 13225686, 1.0f, a2.i, 0);
            guiGraphics.drawString(font, component, (a2.e - font.width((FormattedText)component)) / 2, 4, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(a2.i, 9080985), false);
        }
    }

    private static void a(List<l.a> list, Font font, int n2, float f2, float f3, float f4, float f5, float f6, String string) {
        if (f2 <= 0.0f) {
            return;
        }
        int n3 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(null, font, n2, 1.0f, 1.0f);
        float f7 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c() - (float)(26 * n2);
        float f8 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(f7, n3);
        l.a a2 = new l.a(string, cyou.rimuru.shootingstardemo.mc1211.client.magic.l.a(-99.0f, f8, 1.88f), 120, n3);
        a2.g = 3;
        a2.h = n2;
        a2.n = 0.0f;
        a2.l = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.d(f2) * (1.0f - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.e(f6));
        a2.i = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(f2 * 3.0f) * f4 * (1.0f - f6);
        a2.j = f3 * (1.0f - f6);
        a2.a(-f5);
        list.add(a2);
    }

    private static void a(List<l.a> list, b b2, String string, float f2, float f3, float f4, float f5, float f6, float f7) {
        for (int i2 = 0; i2 < b2.b.size(); ++i2) {
            float f8;
            float f9;
            cyou.rimuru.shootingstardemo.mc1211.magic.d d2 = b2.b.get(i2);
            float f10 = b2.a(i2) - 11.0f;
            float f11 = b2.b(i2);
            cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(list, b2.a, d2, i2, f10, string == null, f2, f3 * f11, f4, f5, f6, f7);
            float f12 = f9 = string == null ? f2 : cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f6 - cyou.rimuru.shootingstardemo.mc1211.client.magic.h.s - 2.0f - 0.8f * (float)i2) / 5.0f);
            if (f9 <= 0.0f) continue;
            String string2 = (string == null ? "skill.prev:" : string) + d2.a();
            l.a a2 = new l.a(string2, cyou.rimuru.shootingstardemo.mc1211.client.magic.l.a(82.0f, f10, 1.88f), 156, 22);
            a2.g = 5;
            a2.h = b2.a;
            a2.s = d2;
            a2.m = 0.0f;
            a2.k = (0.3f + 0.7f * cyou.rimuru.shootingstardemo.mc1211.client.magic.k.d(f9)) * (1.0f - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.e(f5));
            a2.i = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(f9 * 2.5f) * f3 * (1.0f - f5) * f11;
            a2.p = string != null && f9 > 0.6f;
            float f13 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(string2, f7);
            a2.a(0.1f * f13 - f4 - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(string2, f6));
            if (string2.equals(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.E) && (f8 = f6 - (float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.F) < 6.0f) {
                a2.o = Mth.sin((float)(f8 * 2.6f)) * 3.0f * (1.0f - f8 / 6.0f);
            }
            list.add(a2);
        }
    }

    private static void a(List<l.a> list, int n2, cyou.rimuru.shootingstardemo.mc1211.magic.d d2, int n3, float f2, boolean bl, float f3, float f4, float f5, float f6, float f7, float f8) {
        if (d2.j() == null) {
            return;
        }
        for (int i2 = 0; i2 < K.length; ++i2) {
            float f9;
            float f10;
            float f11 = f10 = bl ? f3 : cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f7 - cyou.rimuru.shootingstardemo.mc1211.client.magic.h.s - 3.0f - 0.8f * (float)n3 - 0.6f * (float)i2) / 5.0f);
            if (f10 <= 0.0f) continue;
            String string = (bl ? "slider.prev:" : "") + K[i2] + d2.a();
            l.a a2 = new l.a(string, cyou.rimuru.shootingstardemo.mc1211.client.magic.l.a(86.0f, f2 - (float)J[i2], 1.88f), 148, 11);
            a2.g = 10;
            a2.h = n2;
            a2.s = d2;
            a2.m = 0.0f;
            a2.k = (0.3f + 0.7f * cyou.rimuru.shootingstardemo.mc1211.client.magic.k.d(f10)) * (1.0f - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.e(f6));
            a2.i = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(f10 * 2.5f) * f4 * (1.0f - f6);
            a2.p = !bl && f10 > 0.6f;
            float f12 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(string, f8);
            a2.a(0.05f * f12 - f5 - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(string, f7));
            list.add(a2);
            if (i2 != 0 || bl || !cyou.rimuru.shootingstardemo.mc1211.client.magic.g.c(d2) || !((f9 = Math.max(f12, string.equals(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.G) ? 1.0f : 0.0f)) > 0.01f)) continue;
            l.a a3 = new l.a("warncard:" + d2.a(), cyou.rimuru.shootingstardemo.mc1211.client.magic.l.a(86.0f, f2 - (float)J[0] - 5.5f - 3.0f - 20.0f, 1.88f), 148, 40);
            a3.g = 13;
            a3.s = d2;
            a3.l = 0.6f + 0.4f * cyou.rimuru.shootingstardemo.mc1211.client.magic.k.d(f9);
            a3.i = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(f9 * 1.5f) * a2.i;
            a3.a(0.15f - f5);
            list.add(a3);
        }
    }

    private static void a(List<l.a> list, b b2, float f2) {
        float f3 = Math.max(16.0f, b2.h * b2.h / b2.b());
        float f4 = b2.g - (b2.h - f3) * b2.f / b2.a();
        float f5 = f4 - f3;
        float f6 = b2.g - b2.h;
        int n2 = (int)Math.ceil(b2.h / 16.0f);
        for (int i2 = 0; i2 < n2; ++i2) {
            float f7 = b2.g - (float)(16 * i2);
            int n3 = Math.max(1, Math.round(Math.min(16.0f, f7 - f6)));
            l.a a2 = new l.a("scroll:" + i2, cyou.rimuru.shootingstardemo.mc1211.client.magic.l.a(169.0f, f7 - (float)n3 * 0.5f, 1.88f), 4, n3);
            a2.g = 12;
            a2.h = b2.a;
            a2.q = 1.0f;
            a2.s = new float[]{f7, f7 - (float)n3, f4, f5};
            a2.i = f2;
            list.add(a2);
        }
    }

    private static void a(List<l.a> list, Font font, h.a a2, float f2, float f3, float f4, float f5) {
        boolean bl = a2 == cyou.rimuru.shootingstardemo.mc1211.client.magic.h.v;
        float f6 = bl ? cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - (float)a2.i) / 8.0f) : 1.0f - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - (float)a2.j) / 5.0f);
        int n2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a((GuiGraphics)null, font, a2, 200, 1.0f, 0.0f, 0.0f);
        Vector3f vector3f = cyou.rimuru.shootingstardemo.mc1211.client.magic.l.a(a2.g, a2.h, 1.62f);
        Vector3f vector3f2 = new Vector3f((Vector3fc)a2.d).lerp((Vector3fc)vector3f, bl ? cyou.rimuru.shootingstardemo.mc1211.client.magic.k.g(f6) : cyou.rimuru.shootingstardemo.mc1211.client.magic.k.d(f6 *= 1.0f - f4));
        l.a a3 = new l.a(bl ? "info" : "info:closing", vector3f2, 200, n2);
        a3.g = 6;
        a3.s = a2;
        float f7 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.d(f6);
        a3.k = Mth.lerp((float)f7, (float)((float)a2.e / 200.0f), (float)1.0f);
        a3.l = Mth.lerp((float)f7, (float)((float)a2.f / (float)n2), (float)1.0f);
        a3.i = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(f6 * 3.0f) * f3;
        a3.j = bl ? cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - (float)a2.i - 5.0f) / 3.0f) : cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(1.0f - (f2 - (float)a2.j) / 2.0f);
        a3.j *= 1.0f - f4;
        a3.p = bl && f6 > 0.9f;
        list.add(a3);
        for (String string : cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(a2)) {
            int[] nArray = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(a2, string, n2);
            float f8 = bl ? cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(string, f5) : 0.0f;
            l.a a4 = new l.a((String)(bl ? string : string + ":closing"), a3.a((float)nArray[0] + 29.0f, (float)nArray[1] + 8.0f), 58, 16);
            a4.g = 7;
            a4.s = a2;
            a4.i = a3.i * a3.j;
            a4.p = a3.p;
            float f9 = 0.006f + 0.02f * f8 - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(string, f2);
            a4.a(f9);
            a4.w = a3.b.distance((Vector3fc)cyou.rimuru.shootingstardemo.mc1211.client.magic.l.h) - 0.005f - f9;
            list.add(a4);
        }
    }

    private static float i(float f2) {
        if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.v != null) {
            return cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - (float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.v.i) / 8.0f);
        }
        if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.w != null) {
            return 1.0f - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - (float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.w.j) / 5.0f);
        }
        return 0.0f;
    }

    private static float a(int n2, float f2) {
        if (n2 == cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q) {
            return cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - cyou.rimuru.shootingstardemo.mc1211.client.magic.h.r) / 3.0f);
        }
        if (n2 == cyou.rimuru.shootingstardemo.mc1211.client.magic.h.t) {
            return 1.0f - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - cyou.rimuru.shootingstardemo.mc1211.client.magic.h.u) / 3.0f);
        }
        return 0.0f;
    }

    private static float b(String string, float f2) {
        if (!string.equals(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.A)) {
            return 0.0f;
        }
        return 0.04f * Mth.sin((float)((float)Math.PI * cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - (float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.B) / 4.0f)));
    }

    private static String a(List<l.a> list, float f2, float f3) {
        if (!cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a()) {
            return null;
        }
        ArrayList<l.a> arrayList = new ArrayList<l.a>(list);
        arrayList.sort(Comparator.comparingDouble(a2 -> a2.v));
        boolean bl = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.v != null;
        for (l.a a3 : arrayList) {
            Vector2f vector2f;
            if (a3.t == null || !a3.p || a3.i < 0.5f || bl && a3.g != 6 && a3.g != 7 || !a3.a(vector2f = a3.b(f2 * 0.5f, f3 * 0.5f))) continue;
            return a3.g == 6 ? "info" : a3.a;
        }
        return null;
    }

    private static boolean a(h.a a2) {
        return a2.a != null && !(a2.a instanceof cyou.rimuru.shootingstardemo.mc1211.client.magic.b);
    }

    private static String[] b(h.a a2) {
        String[] stringArray;
        if (cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(a2)) {
            String[] stringArray2 = new String[3];
            stringArray2[0] = "info:setkey";
            stringArray2[1] = "info:select";
            stringArray = stringArray2;
            stringArray2[2] = "info:close";
        } else {
            String[] stringArray3 = new String[1];
            stringArray = stringArray3;
            stringArray3[0] = "info:close";
        }
        return stringArray;
    }

    private static int[] a(h.a a2, String string, int n2) {
        int[] nArray;
        int n3 = n2 - 16 - 10;
        if (!cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(a2)) {
            int[] nArray2;
            if (string.equals("info:close")) {
                int[] nArray3 = new int[2];
                nArray3[0] = 71;
                nArray2 = nArray3;
                nArray3[1] = n3;
            } else {
                nArray2 = null;
            }
            return nArray2;
        }
        switch (string) {
            case "info:setkey": {
                int[] nArray4 = new int[2];
                nArray4[0] = 9;
                nArray = nArray4;
                nArray4[1] = n3;
                break;
            }
            case "info:select": {
                int[] nArray5 = new int[2];
                nArray5[0] = 71;
                nArray = nArray5;
                nArray5[1] = n3;
                break;
            }
            case "info:close": {
                int[] nArray6 = new int[2];
                nArray6[0] = 133;
                nArray = nArray6;
                nArray6[1] = n3;
                break;
            }
            default: {
                nArray = null;
            }
        }
        return nArray;
    }

    private static void a(GuiGraphics guiGraphics, int n2, int n3, float f2, float f3) {
        float f4 = f2 - (float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.l;
        switch (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.j) {
            case b: {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, n2, n3, cyou.rimuru.shootingstardemo.mc1211.client.magic.k.f(cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(f4 / 10.0f)), 1.0f);
                break;
            }
            case c: {
                guiGraphics.fill(0, 0, n2, n3, -1);
                break;
            }
            case d: {
                if (f4 < 3.0f) {
                    guiGraphics.fill(0, 0, n2, n3, -1);
                    break;
                }
                cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, n2, n3, 1.0f - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.f(cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f4 - 3.0f) / 5.0f)), 1.0f);
                break;
            }
            case e: {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(guiGraphics, n2, n3, f4);
                break;
            }
        }
    }

    private static void a(GuiGraphics guiGraphics, int n2, int n3, float f2) {
        if (ah == null) {
            ah = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(n2, n3);
        }
        boolean bl = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.A != null;
        float f3 = 1.0f - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - 8.0f) / 6.0f);
        float f4 = Math.max(0.0f, f2 - 1.0f);
        float f5 = (float)n3 * 0.0025f;
        float f6 = 1.0f - 0.3f * cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(f4 / 13.0f);
        int n4 = f2 < 2.0f ? 1 : 0;
        float[] fArray = new float[3];
        float[] fArray2 = new float[3];
        for (a a2 : ah) {
            float f7 = a2.c + a2.e * f4;
            float f8 = a2.d + a2.f * f4 + 0.5f * f5 * f4 * f4;
            float f9 = (float)Math.toRadians(a2.g * f4);
            float f10 = Mth.cos((float)f9);
            float f11 = Mth.sin((float)f9);
            for (int i2 = 0; i2 < 3; ++i2) {
                float f12 = (a2.a[i2] - a2.c) * f6;
                float f13 = (a2.b[i2] - a2.d) * f6;
                fArray[i2] = f7 + f12 * f10 - f13 * f11;
                fArray2[i2] = f8 + f12 * f11 + f13 * f10;
            }
            guiGraphics.pose().pushPose();
            guiGraphics.pose().mulPose(cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(new Matrix3x2f((fArray[1] - fArray[0]) / 64.0f, (fArray2[1] - fArray2[0]) / 64.0f, (fArray[2] - fArray[0]) / 64.0f, (fArray2[2] - fArray2[0]) / 64.0f, fArray[0], fArray2[0])));
            if (bl) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, f7 / (float)n2, f8 / (float)n3, a2.h + f9 * 0.8f, a2.i, f3, f2 < 2.0f);
            } else {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0xF2F8FF, f3, n4);
            }
            guiGraphics.pose().popPose();
        }
        if (f2 < 2.0f) {
            guiGraphics.fill(0, 0, n2, n3, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(0.75f * (1.0f - f2 / 2.0f), 0xFFFFFF));
        }
    }

    private static a[] a(int n2, int n3) {
        int n4;
        float f2 = (float)n2 / 9.0f;
        float f3 = (float)n3 / 6.0f;
        float[][] fArray = new float[10][7];
        float[][] fArray2 = new float[10][7];
        for (int i2 = 0; i2 <= 9; ++i2) {
            for (int i3 = 0; i3 <= 6; ++i3) {
                boolean bl = i2 == 0 || i2 == 9;
                n4 = i3 == 0 || i3 == 6 ? 1 : 0;
                fArray[i2][i3] = (float)i2 * f2 + (bl ? 0.0f : (cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(i2, i3, 1) - 0.5f) * 0.7f * f2);
                fArray2[i2][i3] = (float)i3 * f3 + (n4 != 0 ? 0.0f : (cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(i2, i3, 2) - 0.5f) * 0.7f * f3);
            }
        }
        float f4 = (float)n2 * (0.5f + cyou.rimuru.shootingstardemo.mc1211.client.magic.h.N * 0.5f);
        float f5 = (float)n3 * 0.5f;
        ArrayList<a> arrayList = new ArrayList<a>();
        for (n4 = 0; n4 < 9; ++n4) {
            for (int i4 = 0; i4 < 6; ++i4) {
                int[][][] nArrayArray;
                int[][] nArrayArray2 = new int[][]{{n4, i4}, {n4 + 1, i4}, {n4 + 1, i4 + 1}, {n4, i4 + 1}};
                if (cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(n4, i4, 3) < 0.5f) {
                    int[][][] nArrayArray3 = new int[2][][];
                    nArrayArray3[0] = new int[][]{nArrayArray2[0], nArrayArray2[1], nArrayArray2[2]};
                    nArrayArray = nArrayArray3;
                    nArrayArray3[1] = new int[][]{nArrayArray2[0], nArrayArray2[2], nArrayArray2[3]};
                } else {
                    int[][][] nArrayArray4 = new int[2][][];
                    nArrayArray4[0] = new int[][]{nArrayArray2[0], nArrayArray2[1], nArrayArray2[3]};
                    nArrayArray = nArrayArray4;
                    nArrayArray4[1] = new int[][]{nArrayArray2[1], nArrayArray2[2], nArrayArray2[3]};
                }
                int[][][] nArrayArray5 = nArrayArray;
                for (int i5 = 0; i5 < 2; ++i5) {
                    a a2 = new a();
                    for (int i6 = 0; i6 < 3; ++i6) {
                        a2.a[i6] = fArray[nArrayArray5[i5][i6][0]][nArrayArray5[i5][i6][1]];
                        a2.b[i6] = fArray2[nArrayArray5[i5][i6][0]][nArrayArray5[i5][i6][1]];
                    }
                    a2.c = (a2.a[0] + a2.a[1] + a2.a[2]) / 3.0f;
                    a2.d = (a2.b[0] + a2.b[1] + a2.b[2]) / 3.0f;
                    float f6 = a2.c - f4;
                    float f7 = a2.d - f5;
                    float f8 = Math.max(1.0f, Mth.sqrt((float)(f6 * f6 + f7 * f7)));
                    float f9 = (float)n3 * (0.012f + 0.025f * cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(n4, i4, 4 + i5));
                    a2.e = f6 / f8 * f9;
                    a2.f = f7 / f8 * f9 - (float)n3 * 0.004f;
                    a2.g = (4.0f + 14.0f * cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(n4, i4, 6 + i5)) * (cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(n4, i4, 8 + i5) < 0.5f ? -1.0f : 1.0f);
                    a2.h = (cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(n4, i4, 10 + i5) - 0.5f) * 1.75f + (cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(n4, i4, 14 + i5) < 0.34f ? (float)Math.PI : 0.0f);
                    a2.i = 0.82f + 0.45f * cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(n4, i4, 12 + i5);
                    arrayList.add(a2);
                }
            }
        }
        return arrayList.toArray(new a[0]);
    }

    private static float a(int n2, int n3, int n4) {
        int n5 = n2 * 374761393 + n3 * 668265263 + n4 * 1274126177;
        n5 = (n5 ^ n5 >>> 13) * 1274126177;
        n5 ^= n5 >>> 16;
        return (float)(n5 & 0xFFFFFF) / 1.6777216E7f;
    }

    private static void b(GuiGraphics guiGraphics, Font font, l.a a2) {
        guiGraphics.drawString(font, (Component)a2.s, 1, 9, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(a2.i, 3883080), false);
    }

    private static void a(GuiGraphics guiGraphics, l.a a2, float f2) {
        int n2 = a2.h == 0 ? 1 : (a2.h == 13 ? 2 : 4);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0, 3, a2.e, 9080985, a2.j, a2.i, n2);
    }

    private static void a(GuiGraphics guiGraphics, l.a a2, float f2, float f3) {
        int n2 = a2.h;
        e e2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(n2);
        float f4 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(a2.a, f3);
        float f5 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(n2, f2);
        float f6 = a2.i;
        int n3 = e2.b;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, -4, -4, 34, n3, 0.5f * f4 * f6, 2);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(guiGraphics, 2, 4, 22, 22, 1712688, 0.18f * f6, 3, false);
        int n4 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(0xFBFBFC, 16037439, f5);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 2, 2, 22, n4, f6, 4);
        int n5 = f5 > 0.5f ? 0xFFFFFF : cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(13225686, n3, f4);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 2, 2, 22, n5, f6, f5 > 0.5f ? 3 : 1);
        if (f6 > 0.5f) {
            guiGraphics.renderItem(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.h.get(n2), 5, 5);
            if (!cyou.rimuru.shootingstardemo.mc1211.client.magic.h.i[n2]) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 3, 3, 20, n4, 0.55f * f6, 0);
            }
        }
    }

    private static void c(GuiGraphics guiGraphics, Font font, l.a a2) {
        float f2 = a2.i;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(guiGraphics, 0, 2, a2.e, a2.f, 1712688, 0.12f * f2, 3, false);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0, 0, a2.e, a2.f, 0xFBFBFC, f2, 3, true);
        Component component = (Component)a2.s;
        if (f2 > 0.05f) {
            guiGraphics.drawString(font, component, (a2.e - font.width((FormattedText)component)) / 2, 4, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f2, 3883080), false);
        }
    }

    private static void d(GuiGraphics guiGraphics, Font font, l.a a2) {
        float f2 = a2.i;
        e e2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(a2.h);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(guiGraphics, 0, 4, a2.e, a2.f, 1712688, 0.16f * f2, 1, true);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0, 0, a2.e, a2.f, 0xFBFBFC, f2, 1, true);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 3, 0, a2.e - 6, 2, e2.b, f2, 0, false);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(guiGraphics, font, a2.h, f2, a2.j);
    }

    private static int a(GuiGraphics guiGraphics, Font font, int n2, float f2, float f3) {
        cyou.rimuru.shootingstardemo.mc1211.magic.b b2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.g.get(n2);
        e e2 = b2.d;
        boolean bl = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.i[n2];
        float f4 = f2 * f3;
        boolean bl2 = guiGraphics != null && f4 > 0.02f;
        int n3 = 9;
        String string = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.h.get(n2).getHoverName().getString();
        List<FormattedCharSequence> list = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(font, string, 104);
        if (list.size() > 3) {
            list = font.split((FormattedText)cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(string.toUpperCase(Locale.ROOT)), 104);
        }
        for (FormattedCharSequence formattedCharSequence : list) {
            if (bl2) {
                guiGraphics.drawString(font, formattedCharSequence, (120 - font.width(formattedCharSequence)) / 2, n3, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f4, 3883080), false);
            }
            n3 += 10;
        }
        n3 += 3;
        if (guiGraphics != null) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(guiGraphics, 12, n3, 96, 13225686, 1.0f, f4);
        }
        n3 += 6;
        if (guiGraphics != null) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(guiGraphics, 40, n3 + 2, 40, 40, 1712688, 0.12f * f2, 3, false);
            cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 40, n3, 40, 0xFBFBFC, f2, 4);
            cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 40, n3, 40, 13225686, f2, 1);
            if (f3 > 0.5f) {
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(44.0f, (float)n3 + 4.0f, 0.0f);
                guiGraphics.pose().scale(2.0f, 2.0f, 1.0f);
                guiGraphics.renderItem(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.h.get(n2), 0, 0);
                guiGraphics.pose().popPose();
            }
        }
        n3 += 47;
        List list2 = font.split((FormattedText)cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)Component.translatable((String)(b2.getDescriptionId() + ".lore"))), 104);
        for (int i2 = 0; i2 < Math.min(6, list2.size()); ++i2) {
            if (bl2) {
                guiGraphics.drawString(font, (FormattedCharSequence)list2.get(i2), (120 - font.width((FormattedCharSequence)list2.get(i2))) / 2, n3, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f4, 9080985), false);
            }
            n3 += 8;
        }
        n3 += 5;
        MutableComponent mutableComponent = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)Component.translatable((String)(bl ? "gui.shooting_star_demo.void.carried" : "gui.shooting_star_demo.void.not_carried")));
        int n4 = font.width((FormattedText)mutableComponent) + 12;
        if (guiGraphics != null) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, (120 - n4) / 2, n3, n4, 12, bl ? 15135979 : 15659251, f2, 3, true);
            if (bl2) {
                guiGraphics.drawString(font, (Component)mutableComponent, (120 - font.width((FormattedText)mutableComponent)) / 2, n3 + 3, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f4, bl ? 3055195 : 9080985), false);
            }
        }
        n3 += 16;
        int n5 = e2.c.size();
        MutableComponent mutableComponent2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)(n5 == 1 ? Component.translatable((String)"gui.shooting_star_demo.void.skills_one") : Component.translatable((String)"gui.shooting_star_demo.void.skills", (Object[])new Object[]{n5})));
        if (bl2) {
            guiGraphics.drawString(font, (Component)mutableComponent2, (120 - font.width((FormattedText)mutableComponent2)) / 2, n3, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f4, 9080985), false);
        }
        return (n3 += 9) + 7;
    }

    private static List<FormattedCharSequence> a(Font font, String string, int n2) {
        ArrayList<FormattedCharSequence> arrayList = new ArrayList<FormattedCharSequence>();
        String string2 = "";
        for (String string3 : string.trim().split(" +")) {
            String string4;
            String string5 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.e(string3);
            String string6 = string4 = string2.isEmpty() ? string5 : string2 + "   " + string5;
            if (!string2.isEmpty() && font.width((FormattedText)cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(string4)) > n2) {
                arrayList.add(cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(string2).getVisualOrderText());
                string2 = string5;
                continue;
            }
            string2 = string4;
        }
        if (!string2.isEmpty()) {
            arrayList.add(cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(string2).getVisualOrderText());
        }
        return arrayList;
    }

    private static void a(GuiGraphics guiGraphics, l.a a2) {
        float[] fArray = (float[])a2.s;
        float f2 = a2.i;
        guiGraphics.fill(1, 0, 3, a2.f, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(0.35f * f2, 13225686));
        float f3 = Math.min(fArray[0], fArray[2]);
        float f4 = Math.max(fArray[1], fArray[3]);
        if (f3 > f4) {
            guiGraphics.fill(0, Math.round(fArray[0] - f3), 4, Math.round(fArray[0] - f4), cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f2, cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b((int)a2.h).b));
        }
    }

    private static void b(GuiGraphics guiGraphics, l.a a2) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().mulPose(cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(new Matrix3x2f(-0.09375f, 0.078125f, 0.0f, 0.15625f, 6.0f, 0.0f)));
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 16037439, a2.i, 2);
        guiGraphics.pose().popPose();
    }

    private static void a(GuiGraphics guiGraphics, Font font, l.a a2, cyou.rimuru.shootingstardemo.mc1211.client.magic.b b2, float f2) {
        e e2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(a2.h);
        float f3 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(a2.a, f2);
        float f4 = a2.i;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(guiGraphics, 0, 2, a2.e, a2.f, 1712688, 0.12f * f4, 1, false);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0, 0, a2.e, a2.f, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(0xFBFBFC, 0xFFFFFF, f3), f4, 1, true);
        if (f3 > 0.01f) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0, 3, 3, a2.f - 6, e2.b, f4 * f3, 0, false);
        }
        cyou.rimuru.shootingstardemo.mc1211.client.magic.d.a(guiGraphics, e2, b2, 6, 3, 32, 16, f4 * 0.55f);
        if (f4 > 0.02f) {
            MutableComponent mutableComponent = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)Component.translatable((String)"gui.shooting_star_demo.void.locked"));
            int n2 = a2.e - 12;
            int n3 = n2 - 4 - font.width((FormattedText)mutableComponent);
            guiGraphics.drawString(font, (Component)mutableComponent, n3, 8, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f4, 9080985), false);
            cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(guiGraphics, n2, 11, f4);
            int n4 = n3 - 4;
            int n5 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f4, 11909826);
            guiGraphics.drawString(font, cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(font, b2.c(), n4 - 44, false), 44, 4, n5, false);
            guiGraphics.drawString(font, cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(font, b2.b(), n4 - 44, true), 44, 12, n5, false);
        }
    }

    private static void b(GuiGraphics guiGraphics, int n2, int n3, float f2) {
        int n4 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f2, 9080985);
        guiGraphics.fill(n2, n3, n2 + 7, n3 + 5, n4);
        guiGraphics.fill(n2 + 1, n3 - 3, n2 + 6, n3 - 2, n4);
        guiGraphics.fill(n2 + 1, n3 - 2, n2 + 2, n3, n4);
        guiGraphics.fill(n2 + 5, n3 - 2, n2 + 6, n3, n4);
    }

    private static void a(GuiGraphics guiGraphics, Font font, l.a a2, float f2, float f3) {
        Object object = a2.s;
        if (object instanceof cyou.rimuru.shootingstardemo.mc1211.client.magic.b) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.b b2 = (cyou.rimuru.shootingstardemo.mc1211.client.magic.b)object;
            cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(guiGraphics, font, a2, b2, f3);
            return;
        }
        cyou.rimuru.shootingstardemo.mc1211.magic.d d2 = (cyou.rimuru.shootingstardemo.mc1211.magic.d)a2.s;
        object = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(a2.h);
        boolean bl = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.i[a2.h];
        float f4 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(a2.a, f3);
        float f5 = a2.i;
        boolean bl2 = bl && cyou.rimuru.shootingstardemo.mc1211.client.magic.a.a((e)object) == d2;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(guiGraphics, 0, 2, a2.e, a2.f, 1712688, 0.12f * f5, 1, false);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0, 0, a2.e, a2.f, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(0xFBFBFC, 0xFFFFFF, f4), f5, 1, true);
        if (bl2) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0, 3, 3, a2.f - 6, 16037439, f5, 0, false);
        } else if (f4 > 0.01f) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0, 3, 3, a2.f - 6, ((e)object).b, f5 * f4, 0, false);
        }
        int n2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.a.b(d2);
        int n3 = bl && n2 > 0 ? 0x9A9AA4 : 0xFFFFFF;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(6.0f, 3.0f, 0.0f);
        guiGraphics.pose().scale(0.5f, 0.5f, 1.0f);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.d.a(guiGraphics, (e)object, d2, 0, 0);
        guiGraphics.pose().popPose();
        if (!bl || n3 != 0xFFFFFF) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 6, 3, 32, 16, bl ? 9079444 : 0xFBFBFC, (bl ? 0.35f : 0.6f) * f5, 0, false);
        }
        if (f5 > 0.02f) {
            String string = cyou.rimuru.shootingstardemo.mc1211.client.magic.f.b(d2);
            int n4 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(font, string);
            int n5 = a2.e - 5;
            if (n4 > 0) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(guiGraphics, font, string, n5, 6, !cyou.rimuru.shootingstardemo.mc1211.client.magic.f.c(d2).isEmpty(), f5);
                n5 -= n4 + 4;
            }
            if (bl && n2 > 0) {
                MutableComponent mutableComponent = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c((n2 + 19) / 20 + "s");
                guiGraphics.drawString(font, (Component)mutableComponent, n5 -= font.width((FormattedText)mutableComponent), 8, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5, 9080985), false);
                n5 -= 4;
                float f6 = 1.0f - cyou.rimuru.shootingstardemo.mc1211.client.magic.a.c(d2);
                guiGraphics.fill(6, a2.f - 2, a2.e - 6, a2.f - 1, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(0.12f * f5, 3883080));
                guiGraphics.fill(6, a2.f - 2, 6 + Math.round((float)(a2.e - 12) * f6), a2.f - 1, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5, d2.d()));
            }
            guiGraphics.drawString(font, cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(font, d2.c(), n5 - 44, false), 44, 4, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5, bl ? 3883080 : 11909826), false);
            guiGraphics.drawString(font, cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(font, d2.b(), n5 - 44, true), 44, 12, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5, bl ? 9080985 : 11909826), false);
        }
        if (a2.a.equals(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.C)) {
            float f7 = 1.0f - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - (float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.D) / 6.0f);
            cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0, 0, a2.e, a2.f, 16037439, 0.55f * f7 * f5, 1, false);
        }
    }

    private static void b(GuiGraphics guiGraphics, Font font, l.a a2, float f2) {
        cyou.rimuru.shootingstardemo.mc1211.magic.d d2 = (cyou.rimuru.shootingstardemo.mc1211.magic.d)a2.s;
        int n2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.d(a2.a);
        float f3 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(a2.a, f2);
        boolean bl = a2.a.equals(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.G);
        float f4 = a2.i;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(guiGraphics, 0, 1, a2.e, a2.f, 1712688, 0.08f * f4, 0, false);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0, 0, a2.e, a2.f, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(0xFBFBFC, 0xFFFFFF, f3), f4, 0, true);
        float f5 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(d2, n2);
        int n3 = 44 + Math.round(68.0f * f5);
        float f6 = n2 == 0 ? d2.j().b(d2.j().c()) : (n2 == 2 ? cyou.rimuru.shootingstardemo.mc1211.magic.f.c.b(cyou.rimuru.shootingstardemo.mc1211.magic.f.c.c()) : 1.0f);
        int n4 = 44 + Math.round(68.0f * f6);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 44, 4, 68, 3, 15659251, f4, 3, true);
        if (n3 - 44 >= 2) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 44, 4, n3 - 44, 3, d2.d(), f4, 3, false);
        }
        guiGraphics.fill(n4, 1, n4 + 1, 3, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(0.7f * f4, 9080985));
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, n3 - 4, 1, 9, 0xFBFBFC, f4, 4);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, n3 - 4, 1, 9, bl ? 16037439 : cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(13225686, d2.d(), f3), f4, bl ? 3 : 1);
        if (f4 > 0.02f) {
            String string = n2 == 1 ? "damage" : (n2 == 2 ? "speed" : (d2.j().d() ? "power" : "radius"));
            MutableComponent mutableComponent = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)Component.translatable((String)("gui.shooting_star_demo.void." + string)));
            guiGraphics.drawString(font, (Component)mutableComponent, 6, 2, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f4, 9080985), false);
            if (n2 == 1 && cyou.rimuru.shootingstardemo.mc1211.client.magic.g.d(d2) == Float.POSITIVE_INFINITY) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, a2.e - 17, 3, 6, 3883080, f4, 1);
                cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, a2.e - 12, 3, 6, 3883080, f4, 1);
            } else {
                MutableComponent mutableComponent2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c(n2 == 0 ? cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(d2.j(), cyou.rimuru.shootingstardemo.mc1211.client.magic.g.b(d2)) : (n2 == 2 ? cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(cyou.rimuru.shootingstardemo.mc1211.magic.f.c, cyou.rimuru.shootingstardemo.mc1211.client.magic.g.e(d2)) : cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(cyou.rimuru.shootingstardemo.mc1211.client.magic.g.d(d2))));
                guiGraphics.drawString(font, (Component)mutableComponent2, a2.e - 6 - font.width((FormattedText)mutableComponent2), 2, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f4, 3883080), false);
            }
        }
    }

    private static void e(GuiGraphics guiGraphics, Font font, l.a a2) {
        cyou.rimuru.shootingstardemo.mc1211.magic.d d2 = (cyou.rimuru.shootingstardemo.mc1211.magic.d)a2.s;
        float f2 = a2.i;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(guiGraphics, 0, 3, a2.e, a2.f, 1712688, 0.18f * f2, 1, false);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0, 0, a2.e, a2.f, 0xFFF3F3, f2, 1, true);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0, 3, 3, a2.f - 6, 15026253, f2, 0, false);
        if (f2 < 0.02f) {
            return;
        }
        cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(guiGraphics, 12, 4, f2);
        MutableComponent mutableComponent = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)Component.translatable((String)"gui.shooting_star_demo.void.warn_title"));
        guiGraphics.drawString(font, (Component)mutableComponent, 20, 5, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f2, 15026253), false);
        MutableComponent mutableComponent2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)Component.translatable((String)"gui.shooting_star_demo.void.warn", (Object[])new Object[]{cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(d2.j(), d2.l())}));
        int n2 = 16;
        for (FormattedCharSequence formattedCharSequence : font.split((FormattedText)mutableComponent2, a2.e - 16)) {
            if (n2 > a2.f - 8) break;
            guiGraphics.drawString(font, formattedCharSequence, 8, n2, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f2, 9080985), false);
            n2 += 8;
        }
    }

    private static void c(GuiGraphics guiGraphics, int n2, int n3, float f2) {
        for (int i2 = 0; i2 < 9; ++i2) {
            int n4 = (i2 + 1) / 2;
            guiGraphics.fill(n2 - n4, n3 + i2, n2 + 1 + n4, n3 + i2 + 1, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f2, 15026253));
        }
        guiGraphics.fill(n2, n3 + 3, n2 + 1, n3 + 6, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f2, 0xFFFFFF));
        guiGraphics.fill(n2, n3 + 7, n2 + 1, n3 + 8, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f2, 0xFFFFFF));
    }

    static String a(cyou.rimuru.shootingstardemo.mc1211.magic.c c2, float f2) {
        String string;
        if (!c2.d()) {
            return Integer.toString(Math.round(f2));
        }
        string = f2 < 1.0f ? ((string = String.format(Locale.ROOT, "%.2f", Float.valueOf(f2))).endsWith("0") ? string.substring(0, string.length() - 1) : string) : (f2 < 10.0f ? String.format(Locale.ROOT, "%.1f", Float.valueOf(f2)) : (f2 == (float)Math.round(f2) ? Integer.toString(Math.round(f2)) : String.format(Locale.ROOT, "%.1f", Float.valueOf(f2))));
        return string + "\u00d7";
    }

    static String b(float f2) {
        return f2 == Float.POSITIVE_INFINITY ? "\u221e" : Integer.toString(Math.round(f2));
    }

    private static Component e(String string) {
        MutableComponent mutableComponent;
        cyou.rimuru.shootingstardemo.mc1211.magic.d d2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(string);
        if (d2 == null || d2.j() == null) {
            return Component.empty();
        }
        if (string.startsWith("speed:")) {
            mutableComponent = Component.translatable((String)"gui.shooting_star_demo.tune.speed", (Object[])new Object[]{cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(cyou.rimuru.shootingstardemo.mc1211.magic.f.c, cyou.rimuru.shootingstardemo.mc1211.client.magic.g.e(d2))});
        } else if (string.startsWith("power:")) {
            cyou.rimuru.shootingstardemo.mc1211.magic.c c2 = d2.j();
            float f2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.g.b(d2);
            mutableComponent = Component.translatable((String)("gui.shooting_star_demo.tune." + d2.a()), (Object[])new Object[]{cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(c2, f2), String.format(Locale.ROOT, "%,d", Math.round(f2 * c2.e()))});
        } else {
            float f3 = cyou.rimuru.shootingstardemo.mc1211.client.magic.g.d(d2);
            float f4 = f3 / 2.0f;
            mutableComponent = f3 == Float.POSITIVE_INFINITY ? Component.translatable((String)"gui.shooting_star_demo.tune.damage_infinite") : Component.translatable((String)"gui.shooting_star_demo.tune.damage", (Object[])new Object[]{cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(f3), f4 == (float)Math.round(f4) ? Integer.toString(Math.round(f4)) : String.format(Locale.ROOT, "%.1f", Float.valueOf(f4))});
        }
        return Component.literal((String)(d2.c() + " \u00b7 ")).append((Component)mutableComponent).append(" \u00b7 ").append((Component)Component.translatable((String)"gui.shooting_star_demo.tune.how"));
    }

    private static int a(Font font, String string) {
        return string.isEmpty() ? 0 : font.width((FormattedText)cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c(string)) + 7;
    }

    private static void a(GuiGraphics guiGraphics, Font font, String string, int n2, int n3, boolean bl, float f2) {
        int n4 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(font, string);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, n2 - n4, n3, n4, 10, bl ? 16507875 : 0xFBFBFC, f2, 0, true);
        guiGraphics.drawString(font, (Component)cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c(string), n2 - n4 + 4, n3 + 2, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f2, bl ? 14239816 : 9080985), false);
    }

    private static Component a(Font font, String string, int n2, boolean bl) {
        MutableComponent mutableComponent;
        MutableComponent mutableComponent2 = mutableComponent = bl ? cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c(string) : cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(string);
        if (font.width((FormattedText)mutableComponent) <= n2) {
            return mutableComponent;
        }
        String string2 = string;
        while (string2.length() > 1) {
            string2 = string2.substring(0, string2.length() - 1);
            mutableComponent = bl ? cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c(string2.strip() + "\u2026") : cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(string2.strip() + "\u2026");
            if (font.width((FormattedText)mutableComponent) > n2) continue;
            return mutableComponent;
        }
        return mutableComponent;
    }

    private static void b(GuiGraphics guiGraphics, Font font, l.a a2, float f2, float f3) {
        h.a a3 = (h.a)a2.s;
        float f4 = a2.i;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(guiGraphics, 0, 6, a2.e, a2.f, 1712688, 0.22f * f4, 2, true);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0, 0, a2.e, a2.f, 0xFBFBFC, f4, 2, true);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 1, 1, a2.e - 2, 22, 15659251, f4, 2, false);
        guiGraphics.fill(1, 23, a2.e - 1, 24, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f4, 13225686));
        if (a2.j > 0.01f) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(guiGraphics, font, a3, a2.e, f4, a2.j, f3);
        }
    }

    private static void a(GuiGraphics guiGraphics, Font font, String string, int n2, int n3, float f2, boolean bl) {
        MutableComponent mutableComponent = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c(cyou.rimuru.shootingstardemo.mc1211.client.magic.c.e(string));
        if (font.width((FormattedText)mutableComponent) > 180) {
            mutableComponent = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c(string.toUpperCase(Locale.ROOT));
        }
        if (bl) {
            guiGraphics.drawString(font, (Component)mutableComponent, (n2 - font.width((FormattedText)mutableComponent)) / 2, n3, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f2, 9080985), false);
        }
    }

    private static int a(GuiGraphics guiGraphics, Font font, h.a a2, int n2, float f2, float f3, float f4) {
        MutableComponent text;
        float f5 = f2 * f3;
        boolean bl = guiGraphics != null && f5 > 0.02f;
        cyou.rimuru.shootingstardemo.mc1211.magic.d d2 = a2.a;
        int n3 = a2.b;
        e e2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(n3);
        boolean bl2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.i[n3];
        String string = d2 != null ? d2.c() : cyou.rimuru.shootingstardemo.mc1211.client.magic.h.h.get(n3).getHoverName().getString();
        MutableComponent mutableComponent = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(cyou.rimuru.shootingstardemo.mc1211.client.magic.c.e(string));
        if (font.width((FormattedText)mutableComponent) > n2 - 16) {
            mutableComponent = (MutableComponent)cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(font, string.toUpperCase(Locale.ROOT), n2 - 16, false);
        }
        if (bl) {
            guiGraphics.drawString(font, (Component)mutableComponent, (n2 - font.width((FormattedText)mutableComponent)) / 2, 8, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5, 3883080), false);
        }
        int n4 = 30;
        if (d2 instanceof cyou.rimuru.shootingstardemo.mc1211.client.magic.b) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.b b2 = (cyou.rimuru.shootingstardemo.mc1211.client.magic.b)d2;
            if (guiGraphics != null) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(guiGraphics, 36, n4 + 2, 128, 64, 1712688, 0.16f * f2, 0, false);
                if (f3 > 0.5f) {
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().translate(36.0f, (float)n4, 0.0f);
                    guiGraphics.pose().scale(2.0f, 2.0f, 1.0f);
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.d.a(guiGraphics, e2, b2, 0, 0, 64, 32, f2);
                    guiGraphics.pose().popPose();
                }
                guiGraphics.renderOutline(35, n4 - 1, 130, 66, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f2, 13225686));
            }
            cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(guiGraphics, font, b2.b(), n2, n4 += 71, f5, bl);
            n4 += 12;
            MutableComponent mutableComponent2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)Component.translatable((String)"gui.shooting_star_demo.void.locked"));
            if (bl) {
                guiGraphics.drawString(font, (Component)mutableComponent2, (n2 - font.width((FormattedText)mutableComponent2)) / 2, n4, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5, 9080985), false);
            }
            n4 += 12;
            MutableComponent mutableComponent3 = Component.literal((String)ShootingStarDemo.DEMO_HANDLE).withStyle(style -> style.withColor(16724808));
            text = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a((Component)Component.translatable((String)"gui.shooting_star_demo.void.unlocks_at", (Object[])new Object[]{b2.h(), mutableComponent3}));
            for (net.minecraft.util.FormattedCharSequence object : font.split((FormattedText)text, 180)) {
                if (bl) {
                    guiGraphics.drawString(font, object, (n2 - font.width(object)) / 2, n4, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5, 3883080), false);
                }
                n4 += 10;
            }
        } else if (d2 != null) {
            if (guiGraphics != null) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(guiGraphics, 36, n4 + 2, 128, 64, 1712688, 0.16f * f2, 0, false);
                if (f3 > 0.5f) {
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().translate(36.0f, (float)n4, 0.0f);
                    guiGraphics.pose().scale(2.0f, 2.0f, 1.0f);
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.d.a(guiGraphics, e2, d2, 0, 0);
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.d.a(guiGraphics, font, d2, 0, 0);
                    guiGraphics.pose().popPose();
                }
                guiGraphics.renderOutline(35, n4 - 1, 130, 66, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f2, d2.d()));
            }
            cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(guiGraphics, font, d2.b(), n2, n4 += 71, f5, bl);
            n4 += 12;
            for (FormattedCharSequence formattedCharSequence : font.split((FormattedText)cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(e2.e(d2)), 180)) {
                if (bl) {
                    guiGraphics.drawString(font, formattedCharSequence, 10, n4, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5, 3883080), false);
                }
                n4 += 10;
            }
            n4 += 4;
            if (guiGraphics != null) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(guiGraphics, 10, n4, 180, 13225686, 1.0f, f5);
            }
            n4 += 6;
            int n5 = cyou.rimuru.shootingstardemo.mc1211.client.magic.a.b(d2);
            if (bl) {
                MutableComponent formattedCharSequence;
                formattedCharSequence = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)Component.translatable((String)"gui.shooting_star_demo.skill.recharge", (Object[])new Object[]{(cyou.rimuru.shootingstardemo.mc1211.client.magic.a.d(d2) + 19) / 20}));
                guiGraphics.drawString(font, (Component)formattedCharSequence, 10, n4, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5, 9080985), false);
                text = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)(n5 > 0 ? Component.translatable((String)"gui.shooting_star_demo.skill.recharging", (Object[])new Object[]{(n5 + 19) / 20}) : Component.translatable((String)"gui.shooting_star_demo.skill.ready")));
                guiGraphics.drawString(font, (Component)text, 190 - font.width((FormattedText)text), n4, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5, n5 > 0 ? 3883080 : 3055195), false);
            }
            n4 += 9;
            if (guiGraphics != null && n5 > 0) {
                int n6 = Math.round(180.0f * (1.0f - cyou.rimuru.shootingstardemo.mc1211.client.magic.a.c(d2)));
                guiGraphics.fill(10, n4, 190, n4 + 1, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(0.12f * f5, 3883080));
                guiGraphics.fill(10, n4, 10 + n6, n4 + 1, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5, d2.d()));
            }
            n4 += 4;
            if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.x == d2) {
                boolean bl3 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.k / 4 % 2 == 0;
                for (net.minecraft.util.FormattedCharSequence object : font.split((FormattedText)cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)Component.translatable((String)"gui.shooting_star_demo.menu.binding", (Object[])new Object[]{d2.c()})), 180)) {
                    if (bl) {
                        guiGraphics.drawString(font, (FormattedCharSequence)object, 10, n4, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5 * (bl3 ? 1.0f : 0.55f), 13208082), false);
                    }
                    n4 += 9;
                }
            } else {
                KeyMapping keyMapping = cyou.rimuru.shootingstardemo.mc1211.client.magic.f.a(d2);
                text = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)(keyMapping.isUnbound() ? Component.translatable((String)"gui.shooting_star_demo.void.no_key") : Component.translatable((String)"gui.shooting_star_demo.skill.key", (Object[])new Object[]{cyou.rimuru.shootingstardemo.mc1211.client.magic.f.b(keyMapping)})));
                if (bl) {
                    guiGraphics.drawString(font, (Component)text, 10, n4, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5, 9080985), false);
                }
                n4 += 9;
                List<KeyMapping> conflicts = cyou.rimuru.shootingstardemo.mc1211.client.magic.f.c(d2);
                if (!conflicts.isEmpty()) {
                    String clashText;
                    clashText = Component.translatable((String)"gui.shooting_star_demo.skill.clash", (Object[])new Object[]{Component.translatable((String)conflicts.get(0).getName())}).getString();
                    if (bl) {
                        guiGraphics.drawString(font, cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(font, clashText, 180, true), 10, n4, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5, 14239816), false);
                    }
                    n4 += 9;
                }
            }
            if (!bl2) {
                if (bl) {
                    guiGraphics.drawString(font, (Component)cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)Component.translatable((String)"gui.shooting_star_demo.void.not_carried_hint")), 10, n4, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5, 9080985), false);
                }
                n4 += 9;
            }
        } else {
            if (guiGraphics != null) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(guiGraphics, (n2 - 48) / 2, n4 + 2, 48, 48, 1712688, 0.12f * f2, 3, false);
                cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, (n2 - 48) / 2, n4, 48, 0xFBFBFC, f2, 4);
                cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, (n2 - 48) / 2, n4, 48, e2.b, f2, 1);
                if (f3 > 0.5f) {
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().translate((float)(n2 - 32) / 2.0f, (float)n4 + 8.0f, 0.0f);
                    guiGraphics.pose().scale(2.0f, 2.0f, 1.0f);
                    guiGraphics.renderItem(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.h.get(n3), 0, 0);
                    guiGraphics.pose().popPose();
                }
            }
            n4 += 56;
            cyou.rimuru.shootingstardemo.mc1211.magic.b b3 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.g.get(n3);
            for (net.minecraft.util.FormattedCharSequence object : font.split((FormattedText)cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a((Component)Component.translatable((String)(b3.getDescriptionId() + ".lore"))), 180)) {
                if (bl) {
                    guiGraphics.drawString(font, (FormattedCharSequence)object, (n2 - font.width((FormattedCharSequence)object)) / 2, n4, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5, 3883080), false);
                }
                n4 += 10;
            }
            n4 += 4;
            if (guiGraphics != null) {
                cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(guiGraphics, 10, n4, 180, 13225686, 1.0f, f5);
            }
            n4 += 6;
            int n7 = e2.c.size();
            text = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)Component.translatable((String)(bl2 ? "gui.shooting_star_demo.void.carried" : "gui.shooting_star_demo.void.not_carried")).append(" \u00b7 ").append((Component)(n7 == 1 ? Component.translatable((String)"gui.shooting_star_demo.void.skills_one") : Component.translatable((String)"gui.shooting_star_demo.void.skills", (Object[])new Object[]{n7}))));
            if (bl) {
                guiGraphics.drawString(font, (Component)text, (n2 - font.width((FormattedText)text)) / 2, n4, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5, bl2 ? 3055195 : 9080985), false);
            }
            n4 += 9;
            if (!bl2) {
                text = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)Component.translatable((String)"gui.shooting_star_demo.void.not_carried_hint"));
                if (bl) {
                    guiGraphics.drawString(font, (Component)text, (n2 - font.width((FormattedText)text)) / 2, n4, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f5, 9080985), false);
                }
                n4 += 9;
            }
        }
        return (n4 += 10) + 16 + 10;
    }

    private static void c(GuiGraphics guiGraphics, Font font, l.a a2, float f2) {
        int n2;
        int n3;
        MutableComponent mutableComponent;
        h.a a3 = (h.a)a2.s;
        String string = a2.a.endsWith(":closing") ? a2.a.substring(0, a2.a.length() - 8) : a2.a;
        float f3 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(a2.a, f2);
        cyou.rimuru.shootingstardemo.mc1211.magic.d d2 = a3.a;
        if (string.equals("info:setkey")) {
            mutableComponent = Component.translatable((String)"gui.shooting_star_demo.void.set_key");
            n3 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.x != null && cyou.rimuru.shootingstardemo.mc1211.client.magic.h.x == d2 ? 2 : 0;
        } else if (string.equals("info:select")) {
            e e2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(a3.b);
            boolean bl2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.i[a3.b];
            boolean bl = bl2 && cyou.rimuru.shootingstardemo.mc1211.client.magic.a.a(e2) == d2;
            mutableComponent = Component.translatable((String)(bl ? "gui.shooting_star_demo.void.selected" : "gui.shooting_star_demo.void.select"));
            n3 = !bl2 ? 1 : (bl ? 0 : 2);
        } else {
            mutableComponent = Component.translatable((String)"gui.shooting_star_demo.void.close");
            n3 = 0;
        }
        float f4 = a2.i;
        n2 = n3 == 2 ? cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(16037439, 16765562, f3) : (n3 == 1 ? 15659251 : cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(0xFBFBFC, 16774364, f3));
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(guiGraphics, 0, 1 + Math.round(2.0f * f3), a2.e, a2.f, 1712688, (0.1f + 0.12f * f3) * f4, 3, false);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0, 0, a2.e, a2.f, n2, f4, 3, true);
        MutableComponent mutableComponent2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)mutableComponent);
        if (f4 > 0.02f) {
            guiGraphics.drawString(font, (Component)mutableComponent2, (a2.e - font.width((FormattedText)mutableComponent2)) / 2, 5, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f4, n3 == 1 ? 11909826 : 3883080), false);
        }
    }

    private static void b(GuiGraphics guiGraphics, int n2, int n3, float f2, float f3) {
        float f4;
        switch (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.j) {
            case b: {
                f4 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - (float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.l - 4.0f) / 4.0f);
                break;
            }
            case c: {
                f4 = 1.0f;
                break;
            }
            default: {
                f4 = 0.0f;
            }
        }
        if (f4 <= 0.0f) {
            return;
        }
        String string = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.y;
        float f6 = string == null ? 0.0f : cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(string, f3);
        int n4 = 16037439;
        if (string != null && string.startsWith("item:")) {
            int n5 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.c(string.substring(5));
            n4 = n5 >= 0 ? cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b((int)n5).b : n4;
        } else if (string != null && string.startsWith("skill:") && cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q >= 0) {
            n4 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b((int)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.q).b;
        } else if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(string) && cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(string) != null) {
            n4 = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(string).d();
        }
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float)n2 * 0.5f, (float)n3 * 0.5f, 0.0f);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0, 0, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(3883080, n4, f6), f6, f4);
        float f7 = (f2 - (float)cyou.rimuru.shootingstardemo.mc1211.client.magic.h.B) / 5.0f;
        if (f7 >= 0.0f && f7 < 1.0f) {
            int n6 = Math.round(10.0f + 18.0f * cyou.rimuru.shootingstardemo.mc1211.client.magic.k.d(f7));
            cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, -n6 / 2, -n6 / 2, n6, n4, (1.0f - f7) * f4, 1);
        }
        guiGraphics.pose().popPose();
    }

    private static void a(GuiGraphics guiGraphics, Font font, int n2, int n3, float f2) {
        float f3;
        Component component;
        if (!cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a()) {
            return;
        }
        boolean bl = cyou.rimuru.shootingstardemo.mc1211.client.magic.h.v == null && cyou.rimuru.shootingstardemo.mc1211.client.magic.h.y != null && cyou.rimuru.shootingstardemo.mc1211.client.magic.h.y.startsWith("skill:");
        cyou.rimuru.shootingstardemo.mc1211.client.magic.b b2 = bl ? cyou.rimuru.shootingstardemo.mc1211.client.magic.b.a(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.y.substring(6)) : null;
        int n4 = 9080985;
        if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.x != null) {
            component = Component.translatable((String)"gui.shooting_star_demo.menu.binding", (Object[])new Object[]{cyou.rimuru.shootingstardemo.mc1211.client.magic.h.x.c()});
            f3 = 1.0f;
            n4 = 13208082;
        } else if (cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.y) && cyou.rimuru.shootingstardemo.mc1211.client.magic.h.v == null) {
            component = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.e(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.y);
            f3 = 1.0f;
            n4 = 3883080;
        } else if ("switch:cutscenes".equals(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.y) && cyou.rimuru.shootingstardemo.mc1211.client.magic.h.v == null) {
            component = Component.translatable((String)"gui.shooting_star_demo.void.cutscenes_hint", (Object[])new Object[]{Component.translatable((String)(cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b() ? "gui.shooting_star_demo.void.on" : "gui.shooting_star_demo.void.off")), cyou.rimuru.shootingstardemo.mc1211.client.magic.f.b(cyou.rimuru.shootingstardemo.mc1211.client.magic.f.b)});
            f3 = 1.0f;
            n4 = 3883080;
        } else if (b2 != null) {
            component = Component.translatable((String)"gui.shooting_star_demo.void.locked_hint", (Object[])new Object[]{b2.c()});
            f3 = 1.0f;
            n4 = 3883080;
        } else {
            component = Component.translatable((String)"gui.shooting_star_demo.void.hint", (Object[])new Object[]{cyou.rimuru.shootingstardemo.mc1211.client.magic.f.b(cyou.rimuru.shootingstardemo.mc1211.client.magic.f.a)});
            f3 = cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - 14.0f) / 6.0f) * (1.0f - cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c((f2 - 120.0f) / 20.0f));
        }
        if (f3 <= 0.02f) {
            return;
        }
        MutableComponent mutableComponent = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(component, (float)Minecraft.getInstance().getWindow().getGuiScale());
        guiGraphics.drawString(font, (Component)mutableComponent, (n2 - font.width((FormattedText)mutableComponent)) / 2, n3 - 16, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(f3, n4), false);
    }

    static float c(float f2) {
        return Mth.clamp((float)f2, (float)0.0f, (float)1.0f);
    }

    static float d(float f2) {
        float f3 = 1.0f - f2;
        return 1.0f - f3 * f3 * f3;
    }

    static float e(float f2) {
        return f2 * f2 * f2;
    }

    static float f(float f2) {
        return f2 < 0.5f ? 4.0f * f2 * f2 * f2 : 1.0f - (float)Math.pow(-2.0f * f2 + 2.0f, 3.0) / 2.0f;
    }

    static float g(float f2) {
        float f3 = 1.70158f;
        float f4 = f3 + 1.0f;
        float f5 = f2 - 1.0f;
        return 1.0f + f4 * f5 * f5 * f5 + f3 * f5 * f5;
    }

    static {
        o = Float.NaN;
    }

    private static final class a {
        final float[] a = new float[3];
        final float[] b = new float[3];
        float c;
        float d;
        float e;
        float f;
        float g;
        float h;
        float i;

        private a() {
        }
    }

    private static final class b {
        final int a;
        final List<cyou.rimuru.shootingstardemo.mc1211.magic.d> b;
        final float[] c;
        final float[] d;
        final int e;
        final float f;
        final float g;
        final float h;

        b(int n2, float f2) {
            int n3;
            this.a = n2;
            this.b = cyou.rimuru.shootingstardemo.mc1211.client.magic.b.a(cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(n2));
            int n4 = this.b.size();
            this.c = new float[n4];
            this.d = new float[n4];
            for (n3 = 0; n3 < n4; ++n3) {
                this.c[n3] = n3 == 0 ? 0.0f : this.c[n3 - 1] + (float)cyou.rimuru.shootingstardemo.mc1211.client.magic.k.a(this.b.get(n3 - 1));
                this.d[n3] = this.c[n3] + 22.0f + cyou.rimuru.shootingstardemo.mc1211.client.magic.k.b(this.b.get(n3));
            }
            for (n3 = 0; n3 < n4 - 1 && this.d[n4 - 1] - this.c[n3] > cyou.rimuru.shootingstardemo.mc1211.client.magic.k.d(); ++n3) {
            }
            this.e = n3;
            this.f = Mth.clamp((float)f2, (float)0.0f, (float)this.c[this.e]);
            int n5 = this.a(this.f);
            this.h = n5 == this.e ? this.c(n5) : Mth.lerp((float)((this.f - this.c[n5]) / (this.c[n5 + 1] - this.c[n5])), (float)this.c(n5), (float)this.c(n5 + 1));
            this.g = Mth.clamp((float)(cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c() - (float)(26 * n2) + this.h * 0.5f), (float)(aj + this.h), (float)ai);
        }

        float a() {
            return this.c[this.e];
        }

        float b() {
            return this.d[this.d.length - 1];
        }

        float a(int n2) {
            return this.g - (this.c[n2] - this.f);
        }

        float b(int n2) {
            float f2 = this.a(n2);
            float f3 = f2 - (this.d[n2] - this.c[n2]);
            float f4 = Math.max(0.0f, f2 - this.g) + Math.max(0.0f, this.g - this.h - f3);
            return cyou.rimuru.shootingstardemo.mc1211.client.magic.k.c(1.0f - f4 / 12.0f);
        }

        private float c(int n2) {
            float f2 = this.d[n2] - this.c[n2];
            for (int i2 = n2 + 1; i2 < this.d.length && this.d[i2] - this.c[n2] <= cyou.rimuru.shootingstardemo.mc1211.client.magic.k.d(); ++i2) {
                f2 = this.d[i2] - this.c[n2];
            }
            return f2;
        }

        private int a(float f2) {
            int n2;
            for (n2 = 0; n2 < this.e && this.c[n2 + 1] <= f2; ++n2) {
            }
            return n2;
        }
    }
}

