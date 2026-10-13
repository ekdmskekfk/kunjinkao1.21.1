/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.cinematic;

import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.a;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.b;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.g;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.i;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.Mth;

public final class c {
    static final int a = 6;
    int b = 6;
    static final int c = 14;
    final i d;
    final List<g> e;
    final String f;
    final String g;
    final int h;
    final int i;
    final int j;
    int k = 1;
    boolean l;
    int m;
    float n = 1.0f;
    float o;
    boolean p;
    int q;
    b r;

    public c(i i2, String string, String string2, int n2, int n3, List<g> list) {
        this.d = i2;
        this.g = string;
        this.f = string2;
        this.h = n2;
        this.i = n3;
        this.e = list;
        this.j = list.get(list.size() - 1).b() + 14;
    }

    public c a(List<g> list) {
        ArrayList<g> arrayList = new ArrayList<g>(this.e);
        arrayList.addAll(list);
        c c2 = new c(this.d, this.g, this.f, this.h, this.i, arrayList);
        c2.k = this.k;
        c2.b = this.b;
        c2.l = this.l;
        c2.n = this.n;
        c2.r = this.r;
        return c2;
    }

    public c a(b b2) {
        this.r = b2;
        return this;
    }

    public c a() {
        this.b = 0;
        return this;
    }

    public int b() {
        return this.e.get(this.e.size() - 1).b();
    }

    public c c() {
        this.l = true;
        return this;
    }

    public c a(int n2) {
        this.k = n2;
        return this;
    }

    public c a(float f2) {
        this.n = f2;
        return this;
    }

    void d() {
        ++this.m;
        this.o += this.n;
    }

    float b(float f2) {
        return this.o + f2 * this.n - (float)this.i;
    }

    boolean e() {
        return this.o >= (float)this.i;
    }

    boolean f() {
        return this.b(0.0f) > (float)this.j || this.p && this.m - this.q > 14;
    }

    float c(float f2) {
        float f3 = this.b(f2);
        if (f3 < 0.0f) {
            return 0.0f;
        }
        float f4 = this.b <= 0 ? 1.0f : cyou.rimuru.shootingstardemo.mc1211.client.cinematic.c.f(f3 / (float)this.b);
        float f5 = 1.0f - cyou.rimuru.shootingstardemo.mc1211.client.cinematic.c.f((f3 - (float)(this.j - 14)) / 14.0f);
        if (this.p) {
            f5 = Math.min(f5, 1.0f - cyou.rimuru.shootingstardemo.mc1211.client.cinematic.c.f(((float)this.m + f2 - (float)this.q) / 8.0f));
        }
        return Math.max(0.0f, Math.min(f4, f5));
    }

    int d(float f2) {
        int n2 = 0;
        for (int i2 = 0; i2 < this.e.size(); ++i2) {
            if (!(f2 >= (float)this.e.get(i2).a())) continue;
            n2 = i2;
        }
        return n2;
    }

    a e(float f2) {
        float f3 = this.b(f2);
        int n2 = this.d(f3);
        g g2 = this.e.get(n2);
        a a2 = this.a(g2, f3, f2);
        if (n2 > 0 && g2.c() > 0 && f3 - (float)g2.a() < (float)g2.c()) {
            g g3 = this.e.get(n2 - 1);
            a a3 = this.a(g3, g3.b(), f2);
            a2 = a3.a(a2, cyou.rimuru.shootingstardemo.mc1211.client.cinematic.c.f((f3 - (float)g2.a()) / (float)g2.c()));
        }
        return a2;
    }

    private a a(g g2, float f2, float f3) {
        float f4 = Mth.clamp((float)((f2 - (float)g2.a()) / Math.max(1.0f, (float)(g2.b() - g2.a()))), (float)0.0f, (float)1.0f);
        return g2.d().pose(this.d, f3, cyou.rimuru.shootingstardemo.mc1211.client.cinematic.c.g(f4));
    }

    static float f(float f2) {
        f2 = Mth.clamp((float)f2, (float)0.0f, (float)1.0f);
        return f2 * f2 * (3.0f - 2.0f * f2);
    }

    static float g(float f2) {
        return f2 < 0.5f ? 4.0f * f2 * f2 * f2 : 1.0f - (float)Math.pow(-2.0f * f2 + 2.0f, 3.0) / 2.0f;
    }
}

