/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.render;

import net.minecraft.world.phys.Vec3;

public interface j {
    public void b(String var1, float var2);

    public void a(String var1, float var2, float var3);

    public void a(String var1, float var2, float var3, float var4);

    public void a(String var1, float var2, float var3, float var4, float var5);

    public void a(String var1, float[] var2);

    public void a(String var1, String var2);

    default public void a(String string, int n2) {
    }

    default public void a(String string, float f2) {
    }

    default public void a(String string, Vec3 vec3) {
        this.a(string, (float)vec3.x, (float)vec3.y, (float)vec3.z);
    }
}

