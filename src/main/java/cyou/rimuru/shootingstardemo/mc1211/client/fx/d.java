/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

public final class d
{
    private final Minecraft a;
    private final float b;
    private final Vec3 c;

    public d(Minecraft minecraft, float partial, Vec3 camera) {
        this.a = minecraft;
        this.b = partial;
        this.c = camera;
    }

    public Vec3 a(Vec3 vec3) {
        return vec3.subtract(this.c);
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

    public Minecraft a() {
        return this.a;
    }

    public float b() {
        return this.b;
    }

    public Vec3 c() {
        return this.c;
    }
}

