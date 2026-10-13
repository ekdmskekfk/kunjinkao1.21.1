/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.magic;

import cyou.rimuru.shootingstardemo.mc1211.magic.d;
import cyou.rimuru.shootingstardemo.mc1211.magic.f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public final class e {
    private static final List<e> d = new ArrayList<e>();
    private static final List<d> e = new ArrayList<d>();
    private static final Map<d, e> f = new IdentityHashMap<d, e>();
    private static final Map<d, Integer> g = new IdentityHashMap<d, Integer>();
    public final ResourceLocation a;
    public final int b;
    public final List<d> c;
    private final a<d> h;

    private e(ResourceLocation resourceLocation, int n2, List<d> list, a<d> a2) {
        this.a = resourceLocation;
        this.b = n2;
        this.c = list;
        this.h = a2;
    }

    public static <S extends d> e a(ResourceLocation resourceLocation, int n2, S[] SArray, a<S> a2) {
        a<d> a3 = (a<d>)(a<?>)a2;
        e e2 = new e(resourceLocation, n2, new ArrayList<d>(List.of(SArray)), a3);
        for (d d3 : e2.c) {
            if (f.putIfAbsent(d3, e2) != null) {
                throw new IllegalStateException("Skill " + d3.a() + " already belongs to a set");
            }
            g.put(d3, e.size());
            e.add(d3);
        }
        d.add(e2);
        return e2;
    }

    public static List<e> a() {
        return Collections.unmodifiableList(d);
    }

    public static int b() {
        return e.size();
    }

    public static d a(int n2) {
        return n2 >= 0 && n2 < e.size() ? e.get(n2) : null;
    }

    public static int a(d d2) {
        return g.get(d2);
    }

    public static e b(d d2) {
        return f.get(d2);
    }

    boolean a(ServerPlayer serverPlayer, d d2, f f2) {
        return this.h.cast(serverPlayer, d2, f2);
    }

    public ResourceLocation c(d d2) {
        return ResourceLocation.fromNamespaceAndPath((String)this.a.getNamespace(), (String)("skill/" + this.a.getPath() + "/" + d2.a()));
    }

    public String d(d d2) {
        return "key." + this.a.getNamespace() + "." + this.a.getPath() + "." + d2.a();
    }

    public Component e(d d2) {
        return Component.translatable((String)("skill." + this.a.getNamespace() + "." + this.a.getPath() + "." + d2.a() + ".desc"));
    }

    @FunctionalInterface
    public static interface a<S extends d> {
        public boolean cast(ServerPlayer var1, S var2, f var3);
    }
}

