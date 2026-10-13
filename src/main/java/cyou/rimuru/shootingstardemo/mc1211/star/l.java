/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.star;

import cyou.rimuru.shootingstardemo.mc1211.magic.d;
import cyou.rimuru.shootingstardemo.mc1211.star.c;
import cyou.rimuru.shootingstardemo.mc1211.star.m;

public enum l
implements d {
    a("the_shooting_star", "The Shooting Star", "SS-01 \u00b7 Railgun", 16724808, 526, 1200, 75, new cyou.rimuru.shootingstardemo.mc1211.magic.c(10.0f, 1500.0f, 200.0f, false, 1.0f)),
    b("gungnir", "The Shooting Star", "SS-03 \u00b7 Gungnir", 16734751, 480, 1200, 73, new cyou.rimuru.shootingstardemo.mc1211.magic.c(0.1f, 20.0f, 4.0f, true, 100.0f)),
    c("seven_stars", "The Shooting Star", "SS-04 \u00b7 Seven Stars", 12101887, 560, 1200, 85, new cyou.rimuru.shootingstardemo.mc1211.magic.c(0.1f, 10.0f, 4.0f, true, (float)cyou.rimuru.shootingstardemo.mc1211.star.c.a(1.0))),
    d("tesseract", "The Shooting Star", "SS-05 \u00b7 Tesseract", 0x7FF0FF, 682, 1200, 89, new cyou.rimuru.shootingstardemo.mc1211.magic.c(0.05f, 10.0f, 1.0f, true, cyou.rimuru.shootingstardemo.mc1211.star.m.b(cyou.rimuru.shootingstardemo.mc1211.star.m.a(1.0f))));

    public final String e;
    public final String f;
    public final String g;
    public final int h;
    public final int i;
    public final int j;
    private final int k;
    private final cyou.rimuru.shootingstardemo.mc1211.magic.c l;

    l(String string2, String string3, String string4, int n3, int n4, int n5, int n6, cyou.rimuru.shootingstardemo.mc1211.magic.c c2) {
        this.e = string2;
        this.f = string3;
        this.g = string4;
        this.h = n3;
        this.i = n4;
        this.j = n5;
        this.k = n6;
        this.l = c2;
    }

    @Override
    public String a() {
        return this.e;
    }

    @Override
    public String b() {
        return this.f;
    }

    @Override
    public String c() {
        return this.g;
    }

    @Override
    public int d() {
        return this.h;
    }

    @Override
    public int e() {
        return this.j;
    }

    @Override
    public int f() {
        return this.i;
    }

    @Override
    public int g() {
        return this.k;
    }

    @Override
    public cyou.rimuru.shootingstardemo.mc1211.magic.c j() {
        return this.l;
    }

    @Override
    public String k() {
        return this == d ? "size" : "power";
    }

    @Override
    public float l() {
        return switch (this.ordinal()) {
            default -> throw new IncompatibleClassChangeError();
            case 0 -> 300.0f;
            case 1 -> 8.0f;
            case 2 -> 2.0f;
            case 3 -> 1.0f;
        };
    }

}

