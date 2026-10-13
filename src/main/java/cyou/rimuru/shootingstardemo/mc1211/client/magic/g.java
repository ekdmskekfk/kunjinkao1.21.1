/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.magic;

import cyou.rimuru.shootingstardemo.mc1211.ShootingStarDemo;
import cyou.rimuru.shootingstardemo.mc1211.magic.c;
import cyou.rimuru.shootingstardemo.mc1211.magic.d;
import cyou.rimuru.shootingstardemo.mc1211.magic.e;
import cyou.rimuru.shootingstardemo.mc1211.magic.f;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public final class g {
    private static final Path a = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a.resolveSibling("shooting_star_demo_skills.properties");
    private static final Map<d, float[]> b = new HashMap<d, float[]>();
    private static boolean c;
    private static boolean d;

    private g() {
    }

    public static f a(d d2) {
        float[] fArray = g.g(d2);
        return new f(fArray[0], fArray[1], fArray[2]);
    }

    public static float b(d d2) {
        return g.g(d2)[0];
    }

    public static boolean c(d d2) {
        return d2.j() != null && g.b(d2) > d2.l() + 1.0E-4f;
    }

    public static float d(d d2) {
        return g.g(d2)[1];
    }

    public static float e(d d2) {
        return g.g(d2)[2];
    }

    public static boolean a(d d2, float f2) {
        float[] fArray;
        float f3 = f.c.d(f2);
        if (f3 == (fArray = g.g(d2))[2]) {
            return false;
        }
        fArray[2] = f3;
        d = true;
        return true;
    }

    public static boolean b(d d2, float f2) {
        float[] fArray;
        c c2 = d2.j();
        if (c2 == null) {
            return false;
        }
        float f3 = c2.d(f2);
        if (f3 == (fArray = g.g(d2))[0]) {
            return false;
        }
        fArray[0] = f3;
        d = true;
        return true;
    }

    public static boolean a(d d2, int n2) {
        float[] fArray;
        float f2 = f.b[Math.max(0, Math.min(f.b.length - 1, n2))];
        if (f2 == (fArray = g.g(d2))[1]) {
            return false;
        }
        fArray[1] = f2;
        d = true;
        return true;
    }

    public static void f(d d2) {
        f f2 = f.a(d2);
        float[] fArray = g.g(d2);
        fArray[0] = f2.b();
        fArray[1] = f2.c();
        fArray[2] = f2.d();
        d = true;
        g.a();
    }

    public static void a() {
        if (d) {
            d = false;
            g.c();
        }
    }

    private static float[] g(d d3) {
        g.b();
        return b.computeIfAbsent(d3, d2 -> {
            f f2 = f.a(d2);
            return new float[]{f2.b(), f2.c(), f2.d()};
        });
    }

    private static String h(d d2) {
        return e.b((d)d2).a.getPath() + "." + d2.a();
    }

    private static void b() {
        if (c) {
            return;
        }
        c = true;
        if (!Files.exists(a, new LinkOption[0])) {
            return;
        }
        Properties properties = new Properties();
        try (InputStream inputStream = Files.newInputStream(a, new OpenOption[0]);){
            properties.load(inputStream);
        }
        catch (IOException iOException) {
            ShootingStarDemo.LOGGER.b("Could not read {}", a, iOException);
            return;
        }
        for (e e2 : e.a()) {
            for (d d2 : e2.c) {
                c c2 = d2.j();
                if (c2 == null) continue;
                f f2 = f.a(d2);
                float f3 = g.a(properties.getProperty(g.h(d2) + "." + d2.k()), f2.b());
                float f4 = g.a(properties.getProperty(g.h(d2) + ".damage"), f2.c());
                float f5 = g.a(properties.getProperty(g.h(d2) + ".speed"), f2.d());
                b.put(d2, new float[]{c2.d(f3), f.b[f.a(f4)], f.c.d(f5)});
            }
        }
    }

    private static float a(String string, float f2) {
        if (string == null) {
            return f2;
        }
        if (string.trim().equalsIgnoreCase("infinite")) {
            return Float.POSITIVE_INFINITY;
        }
        try {
            float f3 = Float.parseFloat(string.trim());
            return Float.isNaN(f3) ? f2 : f3;
        }
        catch (NumberFormatException numberFormatException) {
            return f2;
        }
    }

    private static void c() {
        Properties properties = new Properties();
        for (Map.Entry<d, float[]> entry : b.entrySet()) {
            properties.setProperty(g.h(entry.getKey()) + "." + entry.getKey().k(), String.format(Locale.ROOT, "%s", Float.valueOf(entry.getValue()[0])));
            float f2 = entry.getValue()[1];
            properties.setProperty(g.h(entry.getKey()) + ".damage", f2 == Float.POSITIVE_INFINITY ? "infinite" : String.format(Locale.ROOT, "%s", Float.valueOf(f2)));
            properties.setProperty(g.h(entry.getKey()) + ".speed", String.format(Locale.ROOT, "%s", Float.valueOf(entry.getValue()[2])));
        }
        try (OutputStream outputStream = Files.newOutputStream(a, new OpenOption[0]);){
            properties.store(outputStream, "The Shooting Star (Demo): each skill's power and damage, as set in White Space");
        }
        catch (IOException iOException) {
            ShootingStarDemo.LOGGER.b("Could not write {}", a, iOException);
        }
    }
}

