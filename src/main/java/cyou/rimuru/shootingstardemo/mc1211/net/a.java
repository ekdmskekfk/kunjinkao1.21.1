/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.net;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public final class a
implements CustomPacketPayload {
    private final int c;
    private final float d;
    private final float e;
    private final float f;
    public static final CustomPacketPayload.Type<a> a = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"cast"));
    public static final StreamCodec<RegistryFriendlyByteBuf, a> b = CustomPacketPayload.codec(cyou.rimuru.shootingstardemo.mc1211.net.a::a, cyou.rimuru.shootingstardemo.mc1211.net.a::new);

    private a(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this(registryFriendlyByteBuf.readVarInt(), registryFriendlyByteBuf.readFloat(), registryFriendlyByteBuf.readFloat(), registryFriendlyByteBuf.readFloat());
    }

    public a(int skill, float power, float damage, float speed) {
        this.c = skill;
        this.d = power;
        this.e = damage;
        this.f = speed;
    }

    private void a(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        registryFriendlyByteBuf.writeVarInt(this.c);
        registryFriendlyByteBuf.writeFloat(this.d);
        registryFriendlyByteBuf.writeFloat(this.e);
        registryFriendlyByteBuf.writeFloat(this.f);
    }

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return a;
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
        return this.c;
    }

    public float b() {
        return this.d;
    }

    public float c() {
        return this.e;
    }

    public float d() {
        return this.f;
    }
}

