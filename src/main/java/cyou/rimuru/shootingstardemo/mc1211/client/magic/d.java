/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.magic;

import com.mojang.blaze3d.systems.RenderSystem;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.a;
import cyou.rimuru.shootingstardemo.mc1211.magic.e;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

final class d {
    static final int a = 64;
    static final int b = 32;
    static final int c = 0xFF5A5A;

    private d() {
    }

    static int a(int n2, int n3) {
        return Math.max(0, Math.min(255, n2)) << 24 | n3 & 0xFFFFFF;
    }

    static void a(GuiGraphics guiGraphics, e e2, cyou.rimuru.shootingstardemo.mc1211.magic.d d2, int n2, int n3) {
        boolean bl = cyou.rimuru.shootingstardemo.mc1211.client.magic.a.b(d2) <= 0;
        ResourceLocation resourceLocation = e2.c(d2);
        ResourceLocation resourceLocation2 = ResourceLocation.fromNamespaceAndPath((String)resourceLocation.getNamespace(), (String)("textures/gui/sprites/" + resourceLocation.getPath() + ".png"));
        if (!bl) {
            guiGraphics.setColor(0.47843137f, 0.45490196f, 0.5019608f, 1.0f);
        }
        guiGraphics.blit(resourceLocation2, n2, n3, 0.0f, 0.0f, 64, 32, 64, 32);
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    static void a(GuiGraphics guiGraphics, e e2, cyou.rimuru.shootingstardemo.mc1211.magic.d d2, int n2, int n3, int n4, int n5, float f2) {
        ResourceLocation resourceLocation = e2.c(d2);
        ResourceLocation resourceLocation2 = ResourceLocation.fromNamespaceAndPath((String)resourceLocation.getNamespace(), (String)("textures/gui/sprites/" + resourceLocation.getPath() + ".png"));
        RenderSystem.enableBlend();
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, f2);
        guiGraphics.blit(resourceLocation2, n2, n3, n4, n5, 0.0f, 0.0f, 64, 32, 64, 32);
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    static void a(GuiGraphics guiGraphics, Font font, cyou.rimuru.shootingstardemo.mc1211.magic.d d2, int n2, int n3) {
        int n4 = cyou.rimuru.shootingstardemo.mc1211.client.magic.a.b(d2);
        if (n4 <= 0) {
            return;
        }
        int n5 = Math.max(1, Math.round(32.0f * cyou.rimuru.shootingstardemo.mc1211.client.magic.a.c(d2)));
        guiGraphics.fill(n2, n3 + 32 - n5, n2 + 64, n3 + 32, -1710947830);
        guiGraphics.fill(n2, n3 + 32 - n5, n2 + 64, n3 + 32 - n5 + 1, d.a(192, d2.d()));
        String string = (n4 + 19) / 20 + "s";
        guiGraphics.drawString(font, string, n2 + (64 - font.width(string)) / 2 + 1, n3 + 12 + 1, -1, true);
    }

    static void a(GuiGraphics guiGraphics, Font font, String string, int n2, int n3, int n4) {
        int n5 = font.width(string) + 5;
        int n6 = n2 - n5;
        int n7 = n3 - 11;
        guiGraphics.fill(n6, n7, n2, n3, -267253224);
        guiGraphics.renderOutline(n6, n7, n5, 11, d.a(224, n4));
        guiGraphics.drawString(font, string, n6 + 3, n7 + 2, d.a(255, n4), false);
    }

    static void a(GuiGraphics guiGraphics, int n2, int n3, int n4) {
        guiGraphics.fill(n2 - 2, n3, n2 + 3, n3 + 1, d.a(255, n4));
        guiGraphics.fill(n2 - 1, n3 - 1, n2 + 2, n3 + 2, d.a(255, n4));
        guiGraphics.fill(n2, n3 - 2, n2 + 1, n3 + 3, d.a(255, n4));
        guiGraphics.fill(n2, n3, n2 + 1, n3 + 1, -2344);
    }

    static void a(GuiGraphics guiGraphics, int n2, int n3, int n4, int n5, int n6) {
        for (int i2 = 0; i2 < 6; ++i2) {
            int n7 = n4 * i2 / 12;
            guiGraphics.fill(n2 + n7, n3, n2 + n4 - n7, n3 + 1, d.a(n6 / 6 + n6 * i2 / 12, n5));
        }
    }

    static void b(GuiGraphics guiGraphics, int n2, int n3, int n4, int n5, int n6) {
        for (int i2 = 0; i2 < 6; ++i2) {
            int n7 = n4 * i2 / 12;
            guiGraphics.fill(n2, n3 + n7, n2 + 1, n3 + n4 - n7, d.a(n6 / 6 + n6 * i2 / 12, n5));
        }
    }
}

