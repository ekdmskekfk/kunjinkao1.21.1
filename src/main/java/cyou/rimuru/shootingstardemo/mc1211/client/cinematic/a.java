/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.cinematic;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class a
{
    private final Vec3 a;
    private final Vec3 b;
    private final float c;
    private final float d;

    public a(Vec3 position, Vec3 look, float fov, float roll) {
        this.a = position;
        this.b = look;
        this.c = fov;
        this.d = roll;
    }

    public float a() {
        Vec3 vec3 = this.b.subtract(this.a);
        return (float)Math.toDegrees(Math.atan2(-vec3.x, vec3.z));
    }

    public float b() {
        Vec3 vec3 = this.b.subtract(this.a);
        double d2 = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z);
        return (float)(-Math.toDegrees(Math.atan2(vec3.y, d2)));
    }

    public a a(a a2, float f2) {
        return new a(this.a.lerp(a2.a, (double)f2), this.b.lerp(a2.b, (double)f2), Mth.lerp((float)f2, (float)this.c, (float)a2.c), Mth.lerp((float)f2, (float)this.d, (float)a2.d));
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

    public Vec3 c() {
        return this.a;
    }

    public Vec3 d() {
        return this.b;
    }

    public float e() {
        return this.c;
    }

    public float f() {
        return this.d;
    }
}

