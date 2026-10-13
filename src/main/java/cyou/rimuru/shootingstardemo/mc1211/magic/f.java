/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.magic;

import cyou.rimuru.shootingstardemo.mc1211.magic.c;
import cyou.rimuru.shootingstardemo.mc1211.magic.d;

public final class f
{
    private final float d;
    private final float e;
    private final float f;
    public static final float a = Float.POSITIVE_INFINITY;
    public static final float[] b = new float[]{2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 8.0f, 10.0f, 12.0f, 15.0f, 20.0f, 25.0f, 30.0f, 40.0f, 50.0f, 60.0f, 80.0f, 100.0f, 150.0f, 200.0f, 300.0f, 500.0f, 1000.0f, Float.POSITIVE_INFINITY};
    public static final c c = new c(0.3f, 3.0f, 1.0f, true, 0.0f);

    public f(float power, float damage, float speed) {
        this.d = power;
        this.e = damage;
        this.f = speed;
    }

    public static f a(d d2) {
        c c2 = d2.j();
        return new f(c2 == null ? 1.0f : c2.c(), Float.POSITIVE_INFINITY, c.c());
    }

    public f b(d d2) {
        c c2 = d2.j();
        float f2 = c2 == null ? 1.0f : c2.a(this.d);
        float f3 = this.e >= b[0] ? this.e : b[0];
        return new f(f2, f3, c.a(this.f));
    }

    public boolean a() {
        return this.e == Float.POSITIVE_INFINITY;
    }

    public static int a(float f2) {
        int n2 = 0;
        for (int i2 = 1; i2 < b.length; ++i2) {
            if (!(Math.abs(Math.log(b[i2] / f2)) < Math.abs(Math.log(b[n2] / f2))) && (f2 != Float.POSITIVE_INFINITY || b[i2] != Float.POSITIVE_INFINITY)) continue;
            n2 = i2;
        }
        return n2;
    }

    @Override
    public final String toString() {
        return cyou.rimuru.shootingstardemo.mc1211.RecordBridge.toString(this);
    }

    @Override
    public final int hashCode() {
        return (int)cyou.rimuru.shootingstardemo.mc1211.RecordBridge.hashCode(this);
    }

    @Override
    public final boolean equals(Object object) {
        return (boolean)cyou.rimuru.shootingstardemo.mc1211.RecordBridge.equals(this, object);
    }

    public float b() {
        return this.d;
    }

    public float c() {
        return this.e;
    }

    public float d() {
        return this.f;
    }
}

