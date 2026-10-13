/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.magic;

import cyou.rimuru.shootingstardemo.mc1211.magic.d;
import cyou.rimuru.shootingstardemo.mc1211.magic.e;
import cyou.rimuru.shootingstardemo.mc1211.registry.f;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public enum b
implements d {
    a("meridian", "SS-02 \u00b7 Meridian", 16757575, 40000, "the_shooting_star"),
    b("orrery", "SS-06 \u00b7 Orrery", 16761963, 50000, "tesseract");

    private final String c;
    private final String d;
    private final int e;
    private final int f;
    private final String g;

    b(String string2, String string3, int n3, int n4, String string4) {
        this.c = string2;
        this.d = string3;
        this.e = n3;
        this.f = n4;
        this.g = string4;
    }

    @Override
    public String a() {
        return this.c;
    }

    @Override
    public String b() {
        return "The Shooting Star";
    }

    @Override
    public String c() {
        return this.d;
    }

    @Override
    public int d() {
        return this.e;
    }

    @Override
    public int e() {
        return 0;
    }

    @Override
    public int f() {
        return 0;
    }

    @Override
    public int g() {
        return 0;
    }

    static List<d> a(e e2) {
        if (e2 != cyou.rimuru.shootingstardemo.mc1211.registry.f.a) {
            return e2.c;
        }
        ArrayList<d> arrayList = new ArrayList<d>(e2.c);
        for (b b2 : cyou.rimuru.shootingstardemo.mc1211.client.magic.b.values()) {
            int n2 = 0;
            for (int i2 = 0; i2 < arrayList.size(); ++i2) {
                if (!((d)arrayList.get(i2)).a().equals(b2.g)) continue;
                n2 = i2 + 1;
            }
            arrayList.add(n2, b2);
        }
        return arrayList;
    }

    static b a(String string) {
        for (b b2 : cyou.rimuru.shootingstardemo.mc1211.client.magic.b.values()) {
            if (!b2.c.equals(string)) continue;
            return b2;
        }
        return null;
    }

    String h() {
        return String.format(Locale.ROOT, "%,d", this.f);
    }

}

