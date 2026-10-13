/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.cinematic;

import net.minecraft.world.phys.Vec3;

public interface i {
    public Vec3 a(float var1);

    public Vec3 a();

    public Vec3 b();

    default public Vec3 b(float f2) {
        return this.a(f2).add(0.0, 1.62, 0.0);
    }

    default public Vec3 c(float f2) {
        return this.a(f2);
    }

    default public Vec3 c() {
        Vec3 vec3 = this.b();
        return new Vec3(-vec3.z, 0.0, vec3.x);
    }

    default public Vec3 a(Vec3 vec3, double d2, double d3, double d4) {
        Vec3 vec32 = this.b();
        Vec3 vec33 = this.c();
        return vec3.add(vec33.x * d2 + vec32.x * d4, d3, vec33.z * d2 + vec32.z * d4);
    }

    default public Vec3 a(float f2, double d2, double d3, double d4) {
        Vec3 vec3 = this.a();
        Vec3 vec32 = vec3.subtract(this.a(f2));
        Vec3 vec33 = new Vec3(vec32.x, 0.0, vec32.z);
        vec33 = vec33.lengthSqr() < 1.0E-4 ? this.b() : vec33.normalize();
        Vec3 vec34 = new Vec3(-vec33.z, 0.0, vec33.x);
        return vec3.add(vec34.x * d2 - vec33.x * d4, d3, vec34.z * d2 - vec33.z * d4);
    }

    public static Vec3 a(Vec3 vec3, Vec3 vec32, double d2) {
        double d3 = Math.toRadians(d2);
        double d4 = vec3.x - vec32.x;
        double d5 = vec3.z - vec32.z;
        return new Vec3(vec32.x + d4 * Math.cos(d3) - d5 * Math.sin(d3), vec3.y, vec32.z + d4 * Math.sin(d3) + d5 * Math.cos(d3));
    }
}

