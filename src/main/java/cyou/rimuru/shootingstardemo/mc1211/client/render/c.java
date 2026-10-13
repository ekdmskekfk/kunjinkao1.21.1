/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.render;

import cyou.rimuru.shootingstardemo.mc1211.client.render.b;
import java.util.Objects;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

public final class c
extends Screen {
    private static final int a = -52408;
    private final b.a b;

    public c(b.a a2) {
        super((Component)Component.translatable((String)"gui.shooting_star_demo.gpu.title"));
        this.b = a2;
    }

    protected void init() {
        this.addRenderableWidget(Button.builder((Component)Component.translatable((String)"gui.shooting_star_demo.gpu.exit"), button -> this.minecraft.stop()).bounds(this.width / 2 - 75, this.height - 44, 150, 20).build());
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    public boolean isPauseScreen() {
        return false;
    }

    private void a(GuiGraphics guiGraphics) {
        guiGraphics.fill(0, 0, this.width, this.height, -16316914);
    }

    public void render(GuiGraphics guiGraphics, int n2, int n3, float f2) {
        this.a(guiGraphics);
        super.render(guiGraphics, n2, n3, f2);
        int n4 = this.width / 2;
        int n5 = Math.min(360, this.width - 40);
        int n6 = Math.max(28, this.height / 2 - 90);
        guiGraphics.drawCenteredString(this.font, this.title, n4, n6, -52408);
        n6 += 18;
        n6 = this.a(guiGraphics, (Component)Component.translatable((String)"gui.shooting_star_demo.gpu.body"), n4, n6, n5, -1513744) + 8;
        for (Component component : this.b.e()) {
            n6 = this.a(guiGraphics, (Component)Component.literal((String)"\u2022 ").append(component), n4, n6, n5, -20296) + 2;
        }
        n6 += 8;
        n6 = this.a(guiGraphics, (Component)Component.translatable((String)"gui.shooting_star_demo.gpu.card", (Object[])new Object[]{this.b.b(), this.b.d()}), n4, n6, n5, -7567192) + 10;
        this.a(guiGraphics, (Component)Component.translatable((String)"gui.shooting_star_demo.gpu.hint"), n4, n6, n5, -4672308);
    }

    private int a(GuiGraphics guiGraphics, Component component, int n2, int n3, int n4, int n5) {
        for (FormattedCharSequence formattedCharSequence : this.font.split((FormattedText)component, n4)) {
            guiGraphics.drawCenteredString(this.font, formattedCharSequence, n2, n3, n5);
            Objects.requireNonNull(this.font);
            n3 += 9 + 1;
        }
        return n3;
    }
}

