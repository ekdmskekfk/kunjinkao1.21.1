/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.render;

import cyou.rimuru.shootingstardemo.mc1211.client.render.b;
import cyou.rimuru.shootingstardemo.mc1211.client.render.c;
import cyou.rimuru.shootingstardemo.mc1211.client.render.h;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class i
extends Screen {
    private static final long a = 45000000L;
    private static final int[] b = new int[]{0xFF222C, 16744464, 16769566, 2681940, 1869055, 5517567, 12205311};
    private final Screen c;
    private boolean d;

    public i(Screen screen) {
        super((Component)Component.translatable((String)"gui.shooting_star_demo.warmup.compiling"));
        this.c = screen;
    }

    public boolean isPauseScreen() {
        return false;
    }

    public void onClose() {
        this.a();
    }

    public void tick() {
        if (h.d()) {
            this.a();
        }
    }

    private void a() {
        if (!this.d) {
            this.d = true;
            boolean bl = h.d() && !h.c().isEmpty();
            this.minecraft.setScreen((Screen)(bl ? new c(cyou.rimuru.shootingstardemo.mc1211.client.render.b.a(h.c())) : this.c));
        }
    }

    private void a(GuiGraphics guiGraphics) {
        guiGraphics.fill(0, 0, this.width, this.height, -16316914);
    }

    public void render(GuiGraphics guiGraphics, int n2, int n3, float f2) {
        if (!h.d()) {
            h.a(45000000L);
        }
        this.a(guiGraphics);
        super.render(guiGraphics, n2, n3, f2);
        int n4 = Math.max(h.b().size(), 1);
        int n5 = Math.min(h.e(), n4);
        int n6 = this.width / 2;
        int n7 = this.height / 2;
        guiGraphics.drawCenteredString(this.font, (Component)Component.translatable((String)"gui.shooting_star_demo.warmup.title"), n6, n7 - 34, -1);
        guiGraphics.drawCenteredString(this.font, this.title, n6, n7 - 20, -4672308);
        int n8 = Math.min(260, this.width - 40);
        int n9 = n6 - n8 / 2;
        int n10 = n7 - 3;
        guiGraphics.fill(n9 - 1, n10 - 1, n9 + n8 + 1, n10 + 7, -14014408);
        guiGraphics.fill(n9, n10, n9 + n8, n10 + 6, -15856618);
        int n11 = n8 * n5 / n4;
        for (int i2 = 0; i2 < n11; i2 += 2) {
            guiGraphics.fill(n9 + i2, n10, n9 + Math.min(i2 + 2, n11), n10 + 6, 0xFF000000 | i.a((float)i2 / (float)n8));
        }
        guiGraphics.drawCenteredString(this.font, n5 + " / " + n4 + (String)(h.d() ? "" : "  \u00b7  " + h.f()), n6, n7 + 12, -7567192);
        guiGraphics.drawCenteredString(this.font, (Component)Component.translatable((String)"gui.shooting_star_demo.warmup.skip"), n6, this.height - 20, -10856850);
    }

    private static int a(float f2) {
        float f3 = Math.max(0.0f, Math.min(0.999f, f2)) * 6.0f;
        int n2 = (int)f3;
        float f4 = f3 - (float)n2;
        int n3 = b[n2];
        int n4 = b[Math.min(n2 + 1, 6)];
        int n5 = (int)((float)(n3 >> 16 & 0xFF) + (float)((n4 >> 16 & 0xFF) - (n3 >> 16 & 0xFF)) * f4);
        int n6 = (int)((float)(n3 >> 8 & 0xFF) + (float)((n4 >> 8 & 0xFF) - (n3 >> 8 & 0xFF)) * f4);
        int n7 = (int)((float)(n3 & 0xFF) + (float)((n4 & 0xFF) - (n3 & 0xFF)) * f4);
        return n5 << 16 | n6 << 8 | n7;
    }
}

