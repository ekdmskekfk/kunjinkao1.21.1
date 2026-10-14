/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.net;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public final class e
implements CustomPacketPayload {
    private final int g;
    private final int h;
    private final double i;
    private final double j;
    private final double k;
    private final double l;
    private final double m;
    private final double n;
    private final float o;
    private final long p;
    private final int[] q;
    private final int r;
    private final float s;
    private final float t;
    public static final int a = 1;
    public static final int b = 2;
    public static final int c = 4;
    public static final int d = 2048;
    public static final CustomPacketPayload.Type<e> e = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"spell_fx"));
    public static final StreamCodec<RegistryFriendlyByteBuf, e> f = CustomPacketPayload.codec(cyou.rimuru.shootingstardemo.mc1211.net.e::b, cyou.rimuru.shootingstardemo.mc1211.net.e::new);

    private e(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this(registryFriendlyByteBuf.readVarInt(), registryFriendlyByteBuf.readVarInt(), registryFriendlyByteBuf.readDouble(), registryFriendlyByteBuf.readDouble(), registryFriendlyByteBuf.readDouble(), registryFriendlyByteBuf.readDouble(), registryFriendlyByteBuf.readDouble(), registryFriendlyByteBuf.readDouble(), registryFriendlyByteBuf.readFloat(), registryFriendlyByteBuf.readLong(), cyou.rimuru.shootingstardemo.mc1211.net.e.a(registryFriendlyByteBuf), registryFriendlyByteBuf.readVarInt(), registryFriendlyByteBuf.readFloat(), registryFriendlyByteBuf.readFloat());
    }

    public e(int spell, int casterId, double ox, double oy, double oz, double tx, double ty, double tz, float yaw, long seed, int[] targets, int flags, float power, float speed) {
        this.g = spell;
        this.h = casterId;
        this.i = ox;
        this.j = oy;
        this.k = oz;
        this.l = tx;
        this.m = ty;
        this.n = tz;
        this.o = yaw;
        this.p = seed;
        this.q = targets;
        this.r = flags;
        this.s = power;
        this.t = speed;
    }

    private static int[] a(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        int[] nArray = new int[Math.min(registryFriendlyByteBuf.readVarInt(), 2048)];
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            nArray[i2] = registryFriendlyByteBuf.readVarInt();
        }
        return nArray;
    }

    public int a() {
        return this.q.length > 0 ? this.q[0] : -1;
    }

    private void b(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        registryFriendlyByteBuf.writeVarInt(this.g);
        registryFriendlyByteBuf.writeVarInt(this.h);
        registryFriendlyByteBuf.writeDouble(this.i);
        registryFriendlyByteBuf.writeDouble(this.j);
        registryFriendlyByteBuf.writeDouble(this.k);
        registryFriendlyByteBuf.writeDouble(this.l);
        registryFriendlyByteBuf.writeDouble(this.m);
        registryFriendlyByteBuf.writeDouble(this.n);
        registryFriendlyByteBuf.writeFloat(this.o);
        registryFriendlyByteBuf.writeLong(this.p);
        int n2 = Math.min(this.q.length, 2048);
        registryFriendlyByteBuf.writeVarInt(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            registryFriendlyByteBuf.writeVarInt(this.q[i2]);
        }
        registryFriendlyByteBuf.writeVarInt(this.r);
        registryFriendlyByteBuf.writeFloat(this.s);
        registryFriendlyByteBuf.writeFloat(this.t);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return e;
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

    public int b() {
        return this.g;
    }

    public int c() {
        return this.h;
    }

    public double d() {
        return this.i;
    }

    public double e() {
        return this.j;
    }

    public double f() {
        return this.k;
    }

    public double g() {
        return this.l;
    }

    public double h() {
        return this.m;
    }

    public double i() {
        return this.n;
    }

    public float j() {
        return this.o;
    }

    public long k() {
        return this.p;
    }

    public int[] l() {
        return this.q;
    }

    public int m() {
        return this.r;
    }

    public float n() {
        return this.s;
    }

    public float o() {
        return this.t;
    }
}

