/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.cinematic;

import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.i;

public final class g
{
    private final int a;
    private final int b;
    private final int c;
    private final a d;

    public g(int start, int end, int blendIn, a path) {
        this.a = start;
        this.b = end;
        this.c = blendIn;
        this.d = path;
    }

    public static g a(int n2, int n3, a a2) {
        return new g(n2, n3, 0, a2);
    }

    public static g a(int n2, int n3, int n4, a a2) {
        return new g(n2, n3, n4, a2);
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

    public int a() {
        return this.a;
    }

    public int b() {
        return this.b;
    }

    public int c() {
        return this.c;
    }

    public a d() {
        return this.d;
    }

    @FunctionalInterface
    public static interface a {
        public cyou.rimuru.shootingstardemo.mc1211.client.cinematic.a pose(i var1, float var2, float var3);
    }
}

