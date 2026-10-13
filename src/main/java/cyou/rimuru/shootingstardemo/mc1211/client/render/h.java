/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.render;

import cyou.rimuru.shootingstardemo.mc1211.ShootingStarDemo;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.e;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public final class h {
    private static final String a = "shaders/fx";
    private static final String[] b = new String[]{"composite", "bloom_down", "bloom_up"};
    private static List<String> c;
    private static int d;
    private static int e;
    private static final List<String> f;
    private static long g;
    private static long h;
    private static boolean i;
    private static boolean j;

    private h() {
    }

    public static boolean a() {
        if (i) {
            return false;
        }
        i = true;
        return !cyou.rimuru.shootingstardemo.mc1211.client.render.h.b().isEmpty();
    }

    static List<String> b() {
        if (c == null) {
            ArrayList arrayList = new ArrayList();
            Minecraft.getInstance().getResourceManager().listResources(a, resourceLocation -> resourceLocation.getNamespace().equals("shooting_star_demo") && resourceLocation.getPath().endsWith(".fsh")).keySet().stream().map(ResourceLocation::getPath).map(string -> string.substring(a.length() + 1, string.length() - ".fsh".length())).filter(string -> !string.contains("/")).sorted().forEach(arrayList::add);
            ArrayList<String> arrayList2 = new ArrayList<String>();
            for (String string2 : b) {
                if (!arrayList.remove(string2)) continue;
                arrayList2.add(string2);
            }
            arrayList2.addAll(arrayList);
            c = arrayList2;
        }
        return c;
    }

    static void a(long l2) {
        List<String> list = cyou.rimuru.shootingstardemo.mc1211.client.render.h.b();
        if (j || d >= list.size()) {
            cyou.rimuru.shootingstardemo.mc1211.client.render.h.g();
            return;
        }
        if (g == 0L) {
            g = System.nanoTime();
        }
        long l3 = System.nanoTime();
        do {
            long l4 = System.nanoTime();
            if (!cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(list.get(d))) {
                ++e;
                f.add(list.get(d));
            }
            h += System.nanoTime() - l4;
        } while (++d < list.size() && System.nanoTime() - l3 < l2);
        if (d >= list.size()) {
            cyou.rimuru.shootingstardemo.mc1211.client.render.h.g();
        }
    }

    private static void g() {
        if (!j) {
            j = true;
            ShootingStarDemo.LOGGER.a("Warmed {} spell shaders in {} ms ({} ms compiling{})", c == null ? 0 : c.size(), g == 0L ? 0L : (System.nanoTime() - g) / 1000000L, h / 1000000L, e > 0 ? ", " + e + " broken" : "");
        }
    }

    static List<String> c() {
        return f;
    }

    public static boolean d() {
        return j;
    }

    static int e() {
        return d;
    }

    static String f() {
        List<String> list = cyou.rimuru.shootingstardemo.mc1211.client.render.h.b();
        return d < list.size() ? list.get(d) : "";
    }

    static {
        f = new ArrayList<String>();
    }
}

