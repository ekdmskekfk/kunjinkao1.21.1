/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.render;

import cyou.rimuru.shootingstardemo.mc1211.client.render.j;
import java.util.function.Consumer;

public final class e
{
    private final String a;
    private final Consumer<j> b;

    public e(String program, Consumer<j> setup) {
        this.a = program;
        this.b = setup;
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

    public String a() {
        return this.a;
    }

    public Consumer<j> b() {
        return this.b;
    }
}

