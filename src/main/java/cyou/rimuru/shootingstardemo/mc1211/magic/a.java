/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.magic;

import cyou.rimuru.shootingstardemo.mc1211.magic.b;
import cyou.rimuru.shootingstardemo.mc1211.magic.d;
import cyou.rimuru.shootingstardemo.mc1211.magic.e;
import cyou.rimuru.shootingstardemo.mc1211.magic.f;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class a {
    private static final Map<UUID, long[]> a = new HashMap<UUID, long[]>();
    private static final int b = 20;

    private a() {
    }

    public static void a(ServerPlayer serverPlayer, int n2, float f2, float f3, float f4) {
        d d2 = e.a(n2);
        if (d2 != null) {
            cyou.rimuru.shootingstardemo.mc1211.magic.a.a(serverPlayer, d2, new f(f2, f3, f4).b(d2));
        }
    }

    public static void a(ServerPlayer serverPlayer, d d2) {
        cyou.rimuru.shootingstardemo.mc1211.magic.a.a(serverPlayer, d2, f.a(d2));
    }

    public static void a(ServerPlayer serverPlayer, d d2, f f2) {
        long[] lArray;
        e e2 = e.b(d2);
        if (e2 == null || !serverPlayer.isAlive() || serverPlayer.isSpectator() || !cyou.rimuru.shootingstardemo.mc1211.magic.b.a((Player)serverPlayer, e2)) {
            return;
        }
        int n2 = e.a(d2);
        long l2 = serverPlayer.level().getGameTime();
        if (l2 < (lArray = cyou.rimuru.shootingstardemo.mc1211.magic.a.b(serverPlayer))[n2]) {
            cyou.rimuru.shootingstardemo.mc1211.magic.a.a(serverPlayer, d2.c() + " is recharging \u2014 " + (lArray[n2] - l2 + 19L) / 20L + "s");
            return;
        }
        if (!e2.a(serverPlayer, d2, f2)) {
            return;
        }
        lArray[n2] = l2 + (long)(serverPlayer.isCreative() ? 20 : d2.e());
        cyou.rimuru.shootingstardemo.mc1211.magic.a.a(serverPlayer);
    }

    public static int b(ServerPlayer serverPlayer, d d2) {
        return (int)Math.max(0L, cyou.rimuru.shootingstardemo.mc1211.magic.a.b(serverPlayer)[e.a(d2)] - serverPlayer.level().getGameTime());
    }

    public static void a(ServerPlayer serverPlayer, String string) {
        serverPlayer.displayClientMessage((Component)Component.literal((String)string).withStyle(ChatFormatting.DARK_PURPLE), true);
    }

    public static void a(ServerPlayer serverPlayer) {
        long l2 = serverPlayer.level().getGameTime();
        long[] lArray = cyou.rimuru.shootingstardemo.mc1211.magic.a.b(serverPlayer);
        int[] nArray = new int[lArray.length];
        int[] nArray2 = new int[lArray.length];
        for (int i2 = 0; i2 < lArray.length; ++i2) {
            nArray[i2] = (int)Math.max(0L, lArray[i2] - l2);
            nArray2[i2] = serverPlayer.isCreative() ? 20 : e.a(i2).e();
        }
        cyou.rimuru.shootingstardemo.mc1211.net.d.a(serverPlayer, new cyou.rimuru.shootingstardemo.mc1211.net.b(nArray, nArray2));
    }

    public static void a() {
        a.clear();
    }

    private static long[] b(ServerPlayer serverPlayer) {
        return a.computeIfAbsent(serverPlayer.getUUID(), uUID -> new long[e.b()]);
    }
}

