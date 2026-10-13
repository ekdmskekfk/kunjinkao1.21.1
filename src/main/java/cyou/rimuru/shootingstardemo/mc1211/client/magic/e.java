/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.magic;

import cyou.rimuru.shootingstardemo.mc1211.client.magic.a;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.d;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.f;
import cyou.rimuru.shootingstardemo.mc1211.magic.b;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;

public final class e {
    private static final int a = 66;
    private static final int b = 34;

    private e() {
    }

    public static void a(GuiGraphics guiGraphics, float f2) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        b b2 = cyou.rimuru.shootingstardemo.mc1211.magic.b.a((Player)localPlayer);
        if (b2 == null || minecraft.options.hideGui || localPlayer.isSpectator()) {
            return;
        }
        cyou.rimuru.shootingstardemo.mc1211.magic.e e2 = b2.d;
        cyou.rimuru.shootingstardemo.mc1211.magic.d d2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.a.a(e2);
        Font font = minecraft.font;
        int n2 = e.a(guiGraphics, minecraft, (Player)localPlayer);
        int n3 = e.a(guiGraphics);
        int n4 = d2.d();
        float f3 = 1.0f - Mth.clamp((float)(((float)cyou.rimuru.shootingstardemo.mc1211.client.magic.a.a() + f2) / 10.0f), (float)0.0f, (float)1.0f);
        guiGraphics.fill(n2, n3, n2 + 66, n3 + 34, -15594728);
        d.a(guiGraphics, e2, d2, n2 + 1, n3 + 1);
        d.a(guiGraphics, font, d2, n2 + 1, n3 + 1);
        if (f3 > 0.0f) {
            guiGraphics.fill(n2 + 1, n3 + 1, n2 + 66 - 1, n3 + 34 - 1, d.a((int)(160.0f * f3), 0xFFFFFF));
        }
        guiGraphics.renderOutline(n2, n3, 66, 34, d.a(255, e2.b));
        guiGraphics.renderOutline(n2 - 1, n3 - 1, 68, 36, d.a((int)(80.0f + 175.0f * f3), n4));
        d.a(guiGraphics, n2, n3, n4);
        String string = f.b(d2);
        if (!string.isEmpty()) {
            d.a(guiGraphics, font, string, n2 + 66 + 2, n3 + 34 + 2, e2.b);
        }
        int n5 = guiGraphics.guiWidth() - n2 - 4;
        guiGraphics.drawString(font, font.plainSubstrByWidth(d2.c(), n5), n2 + 1, n3 - 11, d.a(255, n4), true);
    }

    static int a(GuiGraphics guiGraphics, Minecraft minecraft, Player player) {
        int n2 = guiGraphics.guiWidth() / 2 + 91 + 8;
        if (minecraft.options.mainHand().get() == HumanoidArm.LEFT) {
            n2 += player.getOffhandItem().isEmpty() ? 0 : 29;
        } else if (minecraft.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
            n2 += 22;
        }
        return n2;
    }

    static int a(GuiGraphics guiGraphics) {
        return guiGraphics.guiHeight() - 34 - 1;
    }
}

