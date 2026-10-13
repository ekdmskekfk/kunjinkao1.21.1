/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.render;

public final class f {
    public float a;
    public float b;
    public float c;
    public float d;
    public float e;
    public float f;
    public float g;
    public float h;
    public float i;
    public float j;
    public float k;
    public float l;
    public float m;
    public float n;
    public float o;
    public float p;
    public float q = 0.5f;
    public float r = 0.5f;
    public float s;
    public float t;
    public float u;
    public float v;
    public float w;
    public float x;
    public float y;
    public float z;
    public float A = -1.0f;
    public float B;

    public void a() {
        this.d = 0.0f;
        this.c = 0.0f;
        this.b = 0.0f;
        this.a = 0.0f;
        this.h = 0.0f;
        this.g = 0.0f;
        this.f = 0.0f;
        this.e = 0.0f;
        this.l = 0.0f;
        this.k = 0.0f;
        this.j = 0.0f;
        this.i = 0.0f;
        this.p = 0.0f;
        this.o = 0.0f;
        this.n = 0.0f;
        this.m = 0.0f;
        this.r = 0.5f;
        this.q = 0.5f;
        this.y = 0.0f;
        this.x = 0.0f;
        this.w = 0.0f;
        this.v = 0.0f;
        this.u = 0.0f;
        this.t = 0.0f;
        this.s = 0.0f;
        this.B = 0.0f;
        this.z = 0.0f;
        this.A = -1.0f;
    }

    public boolean b() {
        return this.a < 1.0E-4f && this.b < 1.0E-4f && this.c < 1.0E-4f && this.d < 1.0E-4f && this.h < 1.0E-4f && this.l < 1.0E-4f && this.o < 1.0E-4f && this.s < 1.0E-4f && this.t < 1.0E-4f && this.u < 1.0E-4f && this.v < 1.0E-4f && this.y < 0.5f && this.z < 0.5f && this.A < 0.0f;
    }

    public void a(int n2, float f2) {
        if (f2 <= this.h) {
            return;
        }
        this.h = f2;
        this.e = (float)(n2 >> 16 & 0xFF) / 255.0f;
        this.f = (float)(n2 >> 8 & 0xFF) / 255.0f;
        this.g = (float)(n2 & 0xFF) / 255.0f;
    }

    public void a(float f2, int n2, float f3, float f4) {
        if (f2 <= this.o) {
            return;
        }
        this.o = f2;
        this.p = n2;
        this.q = f3;
        this.r = f4;
    }

    public void b(int n2, float f2) {
        if (f2 <= this.l) {
            return;
        }
        this.l = f2;
        this.i = (float)(n2 >> 16 & 0xFF) / 255.0f;
        this.j = (float)(n2 >> 8 & 0xFF) / 255.0f;
        this.k = (float)(n2 & 0xFF) / 255.0f;
    }
}

