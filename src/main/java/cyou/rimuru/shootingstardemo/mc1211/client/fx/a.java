/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import com.mojang.blaze3d.vertex.PoseStack;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.i;
import cyou.rimuru.shootingstardemo.mc1211.magic.d;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class a {
    private static final int a = 70;
    private static final List<Entry> b = new ArrayList<Entry>();

    private a() {
    }

    public static void a(d d2, boolean bl, Vec3 vec3) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.player.position().distanceTo(vec3) > 160.0) {
            return;
        }
        b.removeIf(a2 -> a2.a == d2);
        b.add(new Entry(d2, bl, new int[1]));
        while (b.size() > 2) {
            b.remove(0);
        }
    }

    static void a() {
        b.removeIf(a2 -> {
            a2.c[0] = a2.c[0] + 1;
            return a2.c[0] > 70;
        });
    }

    public static void a(GuiGraphics guiGraphics, float f2) {
        i.a(guiGraphics);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.a.b(guiGraphics, f2);
        i.b(guiGraphics);
    }

    private static void b(GuiGraphics guiGraphics, float f2) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        int n2 = i.c(guiGraphics) / 2;
        int n3 = i.d(guiGraphics) / 4 + 6;
        for (Entry a2 : b) {
            int n4;
            int n5;
            float f3;
            float f4 = (float)a2.c[0] + f2;
            float f5 = Mth.clamp((float)(f4 / 7.0f), (float)0.0f, (float)1.0f);
            float f6 = f5 * (f3 = 1.0f - Mth.clamp((float)((f4 - 54.0f) / 16.0f), (float)0.0f, (float)1.0f));
            if (f6 < 0.03f) continue;
            int n6 = (int)(f6 * 255.0f);
            String string = a2.a.b();
            String string2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.a.a(string.toUpperCase(), (int)(1.0f + (1.0f - f5) * 3.0f));
            guiGraphics.drawCenteredString(font, string2, n2, n3, n6 << 24 | 0xE8C66A);
            String string3 = a2.a.c();
            float f7 = (float)i.c(guiGraphics) * 0.55f / (float)Math.max(1, font.width(string3));
            float f8 = Math.min(2.6f, f7) * (1.0f + (1.0f - f5) * 0.45f);
            int n7 = a2.a.d();
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate((float)n2, (float)(n3 + 14), 0.0f);
            poseStack.scale(f8, f8, 1.0f);
            int n8 = font.width(string3);
            int n9 = (int)((float)n6 * 0.28f);
            for (n5 = -1; n5 <= 1; ++n5) {
                for (n4 = -1; n4 <= 1; ++n4) {
                    if (n5 == 0 && n4 == 0) continue;
                    guiGraphics.drawString(font, string3, -n8 / 2 + n5, n4, n9 << 24 | n7 & 0xFFFFFF, false);
                }
            }
            guiGraphics.drawString(font, string3, -n8 / 2, 0, n6 << 24 | 0xFFFFFF, false);
            guiGraphics.drawString(font, string3, -n8 / 2, 0, (int)((float)n6 * 0.55f) << 24 | n7 & 0xFFFFFF, false);
            poseStack.popPose();
            n5 = (int)((float)n8 * f8 * 0.55f * Mth.clamp((float)(f4 / 14.0f), (float)0.0f, (float)1.0f));
            n4 = n3 + 14 + (int)(10.0f * f8) + 3;
            for (int i2 = 0; i2 < 6; ++i2) {
                int n10 = n5 * (6 - i2) / 6;
                int n11 = (int)((float)n6 * (0.25f + 0.15f * (float)i2));
                guiGraphics.fill(n2 - n10, n4, n2 + n10, n4 + 1, n11 << 24 | 0xE8C66A);
            }
            n3 += (int)(10.0f * f8) + 26;
        }
    }

    private static String a(String string, int n2) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < string.length(); ++i2) {
            stringBuilder.append(string.charAt(i2));
            if (i2 >= string.length() - 1) continue;
            stringBuilder.append(" ".repeat(n2));
        }
        return stringBuilder.toString();
    }

    private static final class Entry
{
        private final d a;
        private final boolean b;
        private final int[] c;

        private Entry(d skill, boolean empowered, int[] age) {
            this.a = skill;
            this.b = empowered;
            this.c = age;
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

        public d a() {
            return this.a;
        }

        public boolean b() {
            return this.b;
        }

        public int[] c() {
            return this.c;
        }
    }
}

