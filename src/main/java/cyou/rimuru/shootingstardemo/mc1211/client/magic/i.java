/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.magic;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;

public final class i {
    private static final Set<AbstractTexture> a = Collections.newSetFromMap(new WeakHashMap());

    private i() {
    }

    public static void a(ResourceLocation resourceLocation, AbstractTexture abstractTexture) {
        if (resourceLocation.getNamespace().equals("shooting_star_demo") && (resourceLocation.getPath().startsWith("white_space_") || resourceLocation.getPath().startsWith("tess_"))) {
            a.add(abstractTexture);
        }
    }

    public static boolean a(AbstractTexture abstractTexture) {
        return a.contains(abstractTexture);
    }
}

