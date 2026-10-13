/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;


public final class b
{
    private final float b;
    private final float c;
    private final float d;
    private final float e;
    private final float f;
    private final float g;
    private final float h;
    public static final float a = Float.NaN;

    public b(float rightX, float rightY, float rightZ, float leftX, float leftY, float leftZ, float weight) {
        this.b = rightX;
        this.c = rightY;
        this.d = rightZ;
        this.e = leftX;
        this.f = leftY;
        this.g = leftZ;
        this.h = weight;
    }

    public b a(float f2) {
        return new b(this.b, this.c, this.d, this.e, this.f, this.g, f2);
    }

    public static b a(float f2, float f3, float f4) {
        return new b(f2, f3, f4, Float.NaN, Float.NaN, Float.NaN, 1.0f);
    }

    public static b b(float f2, float f3, float f4) {
        return new b(f2, f3, f4, f2, -f3, -f4, 1.0f);
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
        return this.b;
    }

    public float b() {
        return this.c;
    }

    public float c() {
        return this.d;
    }

    public float d() {
        return this.e;
    }

    public float e() {
        return this.f;
    }

    public float f() {
        return this.g;
    }

    public float g() {
        return this.h;
    }
}

