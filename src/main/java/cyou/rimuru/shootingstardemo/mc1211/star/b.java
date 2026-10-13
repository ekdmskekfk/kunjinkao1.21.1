/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.star;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public final class b {
    private static final Map<ResourceKey<Level>, Long2IntOpenHashMap> a = new HashMap<ResourceKey<Level>, Long2IntOpenHashMap>();
    private static final Map<ResourceKey<Level>, Long2IntOpenHashMap> b = new HashMap<ResourceKey<Level>, Long2IntOpenHashMap>();

    private b() {
    }

    public static int a(ServerLevel serverLevel, long l2) {
        b.computeIfAbsent((ResourceKey<Level>)serverLevel.dimension(), resourceKey -> new Long2IntOpenHashMap()).addTo(l2, 1);
        return cyou.rimuru.shootingstardemo.mc1211.star.b.c(serverLevel, l2);
    }

    public static void b(ServerLevel serverLevel, long l2) {
        Long2IntOpenHashMap long2IntOpenHashMap = b.get(serverLevel.dimension());
        if (long2IntOpenHashMap == null || !long2IntOpenHashMap.containsKey(l2)) {
            return;
        }
        if (long2IntOpenHashMap.addTo(l2, -1) <= 1) {
            long2IntOpenHashMap.remove(l2);
            Long2IntOpenHashMap long2IntOpenHashMap2 = a.get(serverLevel.dimension());
            if (long2IntOpenHashMap2 != null) {
                long2IntOpenHashMap2.remove(l2);
            }
        }
    }

    public static int c(ServerLevel serverLevel, long l2) {
        Long2IntOpenHashMap long2IntOpenHashMap = a.get(serverLevel.dimension());
        return long2IntOpenHashMap == null ? 0 : long2IntOpenHashMap.get(l2);
    }

    public static void d(ServerLevel serverLevel, long l2) {
        Long2IntOpenHashMap long2IntOpenHashMap = b.get(serverLevel.dimension());
        if (long2IntOpenHashMap != null && long2IntOpenHashMap.containsKey(l2)) {
            a.computeIfAbsent((ResourceKey<Level>)serverLevel.dimension(), resourceKey -> new Long2IntOpenHashMap()).addTo(l2, 1);
        }
    }

    public static void a() {
        a.clear();
        b.clear();
    }
}

