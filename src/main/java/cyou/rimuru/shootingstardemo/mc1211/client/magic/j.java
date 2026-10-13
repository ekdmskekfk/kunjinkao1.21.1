/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.magic;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.c;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.lwjgl.opengl.GL11;

final class j
extends AbstractTexture {
    static final ResourceLocation a = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a("white_space/mirror");
    private static j b;
    private int c;
    private int d;

    private j() {
    }

    public void load(ResourceManager resourceManager) {
    }

    static void a() {
        Minecraft minecraft = Minecraft.getInstance();
        RenderTarget renderTarget = minecraft.getMainRenderTarget();
        if (b == null) {
            b = new j();
            minecraft.getTextureManager().register(a, (AbstractTexture)b);
        }
        int n2 = renderTarget.width;
        int n3 = renderTarget.height;
        int n4 = b.getId();
        if (n2 != j.b.c || n3 != j.b.d) {
            TextureUtil.prepareImage((int)n4, (int)n2, (int)n3);
            j.b.c = n2;
            j.b.d = n3;
        }
        GlStateManager._bindTexture((int)n4);
        GlStateManager._texParameter((int)3553, (int)10241, (int)9729);
        GlStateManager._texParameter((int)3553, (int)10240, (int)9729);
        GlStateManager._glBindFramebuffer((int)36008, (int)renderTarget.frameBufferId);
        GL11.glCopyTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)0, (int)0, (int)n2, (int)n3);
        GlStateManager._glBindFramebuffer((int)36160, (int)renderTarget.frameBufferId);
    }
}

