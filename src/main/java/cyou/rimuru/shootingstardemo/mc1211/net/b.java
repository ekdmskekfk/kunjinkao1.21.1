/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.net;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public final class b
implements CustomPacketPayload {
    private final int[] c;
    private final int[] d;
    public static final CustomPacketPayload.Type<b> a = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"cooldowns"));
    public static final StreamCodec<RegistryFriendlyByteBuf, b> b = CustomPacketPayload.codec(cyou.rimuru.shootingstardemo.mc1211.net.b::b, cyou.rimuru.shootingstardemo.mc1211.net.b::a);

    public b(int[] remaining, int[] total) {
        this.c = remaining;
        this.d = total;
    }

    private static b a(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        int n2 = Math.min(registryFriendlyByteBuf.readVarInt(), 4096);
        int[] nArray = new int[n2];
        int[] nArray2 = new int[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray[i2] = registryFriendlyByteBuf.readVarInt();
            nArray2[i2] = registryFriendlyByteBuf.readVarInt();
        }
        return new b(nArray, nArray2);
    }

    private void b(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        registryFriendlyByteBuf.writeVarInt(this.c.length);
        for (int i2 = 0; i2 < this.c.length; ++i2) {
            registryFriendlyByteBuf.writeVarInt(this.c[i2]);
            registryFriendlyByteBuf.writeVarInt(this.d[i2]);
        }
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

    public int[] a() {
        return this.c;
    }

    public int[] b() {
        return this.d;
    }
}

