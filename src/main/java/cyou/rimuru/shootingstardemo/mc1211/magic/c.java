/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.magic;


public final class c
{
    private final float a;
    private final float b;
    private final float c;
    private final boolean d;
    private final float e;
    private static final float f = 10.0f;

    public c(float min, float max, float initial, boolean factor, float blocks) {
        this.a = min;
        this.b = max;
        this.c = initial;
        this.d = factor;
        this.e = blocks;
    }

    public float a(float f2) {
        return Float.isNaN(f2) ? this.c : Math.max(this.a, Math.min(this.b, f2));
    }

    public float b(float f2) {
        float f3 = this.a(f2);
        if (this.d) {
            return (float)(Math.log(f3 / this.a) / Math.log(this.b / this.a));
        }
        return (f3 - this.a) / (this.b - this.a);
    }

    public float c(float f2) {
        float f3 = Math.max(0.0f, Math.min(1.0f, f2));
        float f4 = this.d ? (float)((double)this.a * Math.pow(this.b / this.a, f3)) : this.a + (this.b - this.a) * f3;
        return this.d(f4);
    }

    public float d(float f2) {
        float f3 = !this.d ? (float)Math.round(f2 / 10.0f) * 10.0f : (f2 < 1.0f ? (float)Math.round(f2 * 20.0f) / 20.0f : (f2 < 10.0f ? (float)Math.round(f2 * 10.0f) / 10.0f : (float)Math.round(f2 * 2.0f) / 2.0f));
        return this.a(f3);
    }

    public float a(float f2, int n2) {
        float f3 = this.a(f2);
        float f4 = this.b(f3);
        for (int i2 = 1; i2 <= 40; ++i2) {
            float f5 = this.c(f4 + (float)(n2 * i2) / 60.0f);
            if (f5 == f3) continue;
            return f5;
        }
        return f3;
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

    public float a() {
        return this.a;
    }

    public float b() {
        return this.b;
    }

    public float c() {
        return this.c;
    }

    public boolean d() {
        return this.d;
    }

    public float e() {
        return this.e;
    }
}

