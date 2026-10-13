/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;

public final class i {
    static final float a = 710.0f;
    private static float b = 1.0f;
    private static int c;

    private i() {
    }

    public static void a(GuiGraphics guiGraphics) {
        if (c++ == 0) {
            b = (float)guiGraphics.guiHeight() / 710.0f;
        }
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.scale(b, b, 1.0f);
    }

    public static void b(GuiGraphics guiGraphics) {
        guiGraphics.pose().popPose();
        if (--c == 0) {
            b = 1.0f;
        }
    }

    public static int c(GuiGraphics guiGraphics) {
        return (int)Math.ceil((float)guiGraphics.guiWidth() / b);
    }

    public static int d(GuiGraphics guiGraphics) {
        return (int)Math.ceil((float)guiGraphics.guiHeight() / b);
    }
}

