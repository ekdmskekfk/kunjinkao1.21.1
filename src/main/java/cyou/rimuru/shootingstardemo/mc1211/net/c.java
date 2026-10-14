/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.net;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public final class c
implements CustomPacketPayload {
    private final int[] c;
    public static final CustomPacketPayload.Type<c> a = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"erase"));
    public static final StreamCodec<RegistryFriendlyByteBuf, c> b = CustomPacketPayload.codec(cyou.rimuru.shootingstardemo.mc1211.net.c::b, cyou.rimuru.shootingstardemo.mc1211.net.c::a);

    public c(int[] ids) {
        this.c = ids;
    }

    private static c a(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        int n2 = Math.min(registryFriendlyByteBuf.readVarInt(), 4096);
        int[] nArray = new int[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray[i2] = registryFriendlyByteBuf.readVarInt();
        }
        return new c(nArray);
    }

    private void b(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        registryFriendlyByteBuf.writeVarInt(this.c.length);
        for (int n2 : this.c) {
            registryFriendlyByteBuf.writeVarInt(n2);
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
}

