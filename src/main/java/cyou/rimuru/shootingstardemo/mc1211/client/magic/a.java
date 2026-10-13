/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.magic;

import cyou.rimuru.shootingstardemo.mc1211.magic.d;
import cyou.rimuru.shootingstardemo.mc1211.magic.e;
import cyou.rimuru.shootingstardemo.mc1211.net.b;
import cyou.rimuru.shootingstardemo.mc1211.registry.g;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;

public final class a {
    private static final Map<e, d> a = new IdentityHashMap<e, d>();
    private static int[] b = new int[0];
    private static int[] c = new int[0];
    private static int d = 0x3FFFFFFF;

    private a() {
    }

    public static d a(e e2) {
        return a.getOrDefault(e2, e2.c.get(0));
    }

    public static void a(d d2) {
        e e2 = e.b(d2);
        a.put(e2, d2);
        d = 0;
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            localPlayer.level().playLocalSound(localPlayer.getX(), localPlayer.getY(), localPlayer.getZ(), g.a, SoundSource.PLAYERS, 0.8f, 0.9f + (float)e2.c.indexOf(d2) * 0.05f, false);
            localPlayer.displayClientMessage((Component)Component.literal((String)("\u2726 " + d2.c())).withStyle(style -> style.withColor(d2.d())), true);
        }
    }

    public static d b(e e2) {
        return e2.c.get((e2.c.indexOf(cyou.rimuru.shootingstardemo.mc1211.client.magic.a.a(e2)) + 1) % e2.c.size());
    }

    public static int b(d d2) {
        int n2 = e.a(d2);
        return n2 < b.length ? b[n2] : 0;
    }

    public static float c(d d2) {
        int n2 = e.a(d2);
        return n2 < b.length ? Math.min(1.0f, (float)b[n2] / (float)c[n2]) : 0.0f;
    }

    public static int d(d d2) {
        int n2 = e.a(d2);
        return n2 < c.length ? c[n2] : d2.e();
    }

    public static int a() {
        return d;
    }

    public static void a(b b2) {
        b = (int[])b2.a().clone();
        c = new int[b.length];
        for (int i2 = 0; i2 < c.length; ++i2) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.a.c[i2] = Math.max(1, i2 < b2.b().length ? b2.b()[i2] : 1);
        }
    }

    public static void b() {
        for (int i2 = 0; i2 < b.length; ++i2) {
            if (b[i2] <= 0) continue;
            int n2 = i2;
            b[n2] = b[n2] - 1;
        }
        if (d < 0x3FFFFFFF) {
            ++d;
        }
    }
}

