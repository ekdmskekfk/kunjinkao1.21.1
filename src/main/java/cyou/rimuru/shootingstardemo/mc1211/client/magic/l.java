/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.magic;

import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

final class l {
    static final float a = 70.0f;
    static final float b = 0.12f;
    static final float c = 1.88f;
    static final float d = 0.17f;
    static final float e = (float)((double)1.88f * Math.tan(Math.toRadians(0.17f)));
    static final float f = 0.25f;
    static final float g = 0.35f;
    static final Vector3f h = new Vector3f(0.0f, 0.0f, 0.12f);
    final float i;
    final float j;
    final float k;
    final Vector3f l;
    final Vector3f m;
    final Vector3f n;
    final Vector3f o;

    l(float f2, float f3, float f4, float f5) {
        this.i = f4;
        this.j = f5;
        this.k = (float)((double)f5 * 0.5 / Math.tan(Math.toRadians(35.0)));
        double d2 = Math.toRadians(f2);
        double d3 = Math.toRadians(f3);
        float f6 = (float)Math.sin(d2);
        float f7 = (float)Math.cos(d2);
        float f8 = (float)Math.sin(d3);
        float f9 = (float)Math.cos(d3);
        this.l = new Vector3f(f6 * f9, -f8, f7 * f9);
        this.m = new Vector3f(f7, 0.0f, -f6);
        this.n = new Vector3f(f6 * f8, f9, f7 * f8);
        this.o = new Vector3f((Vector3fc)this.l).mul(0.12f);
    }

    static Vector3f a(float f2, float f3) {
        double d2 = Math.toRadians(f2 * 0.17f);
        double d3 = Math.toRadians(f3 * 0.17f);
        return new Vector3f((float)(Math.sin(d2) * Math.cos(d3)), (float)Math.sin(d3), (float)(Math.cos(d2) * Math.cos(d3)));
    }

    static Vector3f a(float f2, float f3, float f4) {
        return cyou.rimuru.shootingstardemo.mc1211.client.magic.l.a(f2, f3).mul(f4).add((Vector3fc)h);
    }

    boolean a(a a2) {
        float f2;
        Vector3f vector3f = new Vector3f((Vector3fc)a2.b).sub((Vector3fc)this.o);
        a2.v = Float.isNaN(a2.w) ? a2.b.distance((Vector3fc)h) : a2.w;
        float f3 = vector3f.dot((Vector3fc)this.m);
        float f4 = vector3f.dot((Vector3fc)this.n);
        a2.u = f2 = vector3f.dot((Vector3fc)this.l);
        if (f2 < 0.25f || Math.abs(f3) > f2 * 6.0f || Math.abs(f4) > f2 * 6.0f) {
            a2.t = null;
            return false;
        }
        float f5 = this.i * 0.5f + this.k * f3 / f2;
        float f6 = this.j * 0.5f - this.k * f4 / f2;
        float f7 = this.a(a2.c, f3, f2);
        float f8 = this.b(a2.c, f4, f2);
        float f9 = this.a(a2.d, f3, f2);
        float f10 = this.b(a2.d, f4, f2);
        float f11 = (float)Math.atan2(f8 - f9, f7 + f10);
        float f12 = (float)Math.cos(f11);
        float f13 = (float)Math.sin(f11);
        float f14 = f12 * f7 + f13 * f8;
        float f15 = -f13 * f7 + f12 * f8;
        float f16 = f12 * f9 + f13 * f10;
        float f17 = -f13 * f9 + f12 * f10;
        float f18 = this.k / vector3f.length() * (1.0f - a2.q);
        f14 = f18 + a2.q * f14;
        f15 = a2.q * f15;
        f16 = a2.q * f16;
        f17 = f18 + a2.q * f17;
        float f19 = e * (f12 * f14 - f13 * f15);
        float f20 = e * (f13 * f14 + f12 * f15);
        float f21 = e * (f12 * f16 - f13 * f17);
        float f22 = e * (f13 * f16 + f12 * f17);
        float f23 = f5 - f19 * (float)a2.e * 0.5f - f21 * (float)a2.f * 0.5f;
        float f24 = f6 - f20 * (float)a2.e * 0.5f - f22 * (float)a2.f * 0.5f;
        Matrix3x2f matrix3x2f = new Matrix3x2f(f19, f20, f21, f22, f23, f24);
        if (a2.k != 1.0f || a2.l != 1.0f || a2.o != 0.0f) {
            matrix3x2f.translate(a2.m + a2.o, a2.n).scale(a2.k, a2.l).translate(-a2.m, -a2.n);
        }
        a2.t = matrix3x2f;
        return true;
    }

    private float a(Vector3f vector3f, float f2, float f3) {
        return this.k * (vector3f.dot((Vector3fc)this.m) * f3 - f2 * vector3f.dot((Vector3fc)this.l)) / (f3 * f3);
    }

    private float b(Vector3f vector3f, float f2, float f3) {
        return -this.k * (vector3f.dot((Vector3fc)this.n) * f3 - f2 * vector3f.dot((Vector3fc)this.l)) / (f3 * f3);
    }

    static float[] a(Vector3f vector3f) {
        double d2 = Math.atan2(vector3f.x - cyou.rimuru.shootingstardemo.mc1211.client.magic.l.h.x, vector3f.z);
        double d3 = 0.0;
        for (int i2 = 0; i2 < 6; ++i2) {
            Vector3f vector3f2 = new Vector3f((float)(Math.sin(d2) * Math.cos(d3)), (float)(-Math.sin(d3)), (float)(Math.cos(d2) * Math.cos(d3))).mul(0.12f);
            Vector3f vector3f3 = new Vector3f((Vector3fc)vector3f).sub((Vector3fc)vector3f2);
            d2 = Math.atan2(vector3f3.x, vector3f3.z);
            d3 = -Math.atan2(vector3f3.y, Math.sqrt(vector3f3.x * vector3f3.x + vector3f3.z * vector3f3.z));
        }
        return new float[]{(float)Math.toDegrees(d2), (float)Math.toDegrees(d3)};
    }

    static final class a {
        final String a;
        final Vector3f b;
        final Vector3f c = new Vector3f();
        final Vector3f d = new Vector3f();
        final int e;
        final int f;
        int g;
        int h = -1;
        float i = 1.0f;
        float j = 1.0f;
        float k = 1.0f;
        float l = 1.0f;
        float m;
        float n;
        float o;
        boolean p;
        float q = 0.35f;
        boolean r;
        Object s;
        Matrix3x2f t;
        float u;
        float v;
        float w = Float.NaN;

        a(String string, Vector3f vector3f, int n2, int n3) {
            this.a = string;
            this.b = vector3f;
            this.e = n2;
            this.f = n3;
            this.m = (float)n2 * 0.5f;
            this.n = (float)n3 * 0.5f;
            Vector3f vector3f2 = new Vector3f((Vector3fc)vector3f).sub((Vector3fc)cyou.rimuru.shootingstardemo.mc1211.client.magic.l.h).normalize();
            this.c.set(vector3f2.z, 0.0f, -vector3f2.x).normalize();
            this.c.cross((Vector3fc)vector3f2, this.d);
        }

        a a(float f2) {
            Vector3f vector3f = new Vector3f((Vector3fc)this.b).sub((Vector3fc)cyou.rimuru.shootingstardemo.mc1211.client.magic.l.h).normalize();
            this.b.sub((Vector3fc)vector3f.mul(f2));
            return this;
        }

        Vector3f a(float f2, float f3) {
            return new Vector3f((Vector3fc)this.b).add((Vector3fc)new Vector3f((Vector3fc)this.c).mul((f2 - (float)this.e * 0.5f) * e)).add((Vector3fc)new Vector3f((Vector3fc)this.d).mul((f3 - (float)this.f * 0.5f) * e));
        }

        Vector2f b(float f2, float f3) {
            if (this.t == null || Math.abs(this.t.determinant()) < 1.0E-6f) {
                return null;
            }
            return new Matrix3x2f((Matrix3x2fc)this.t).invert().transformPosition(f2, f3, new Vector2f());
        }

        boolean a(Vector2f vector2f) {
            if (vector2f == null) {
                return false;
            }
            if (this.r) {
                return vector2f.distance((float)this.e * 0.5f, (float)this.f * 0.5f) <= (float)Math.min(this.e, this.f) * 0.5f;
            }
            return vector2f.x >= 0.0f && vector2f.y >= 0.0f && vector2f.x <= (float)this.e && vector2f.y <= (float)this.f;
        }
    }
}

