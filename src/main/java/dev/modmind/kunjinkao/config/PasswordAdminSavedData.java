package dev.modmind.kunjinkao.config;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/** 当前服务器存档的密码管理员 UUID 集合。 */
public final class PasswordAdminSavedData extends SavedData {
    private static final String DATA_NAME = "kunjinkao_password_admins";
    private static final String UUIDS_KEY = "AuthorizedUuids";
    private final Set<UUID> authorizedUuids = new HashSet<>();

    public static PasswordAdminSavedData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(PasswordAdminSavedData::new, PasswordAdminSavedData::load), DATA_NAME);
    }

    private static PasswordAdminSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        PasswordAdminSavedData data = new PasswordAdminSavedData();
        for (Tag entry : tag.getList(UUIDS_KEY, Tag.TAG_STRING)) {
            try {
                data.authorizedUuids.add(UUID.fromString(entry.getAsString()));
            } catch (IllegalArgumentException ignored) {
                // 忽略损坏条目，不能借此取得管理员权限。
            }
        }
        return data;
    }

    public boolean isAuthorized(UUID playerUuid) {
        return authorizedUuids.contains(playerUuid);
    }

    public void authorize(UUID playerUuid) {
        if (authorizedUuids.add(playerUuid)) {
            setDirty();
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag entries = new ListTag();
        for (UUID uuid : authorizedUuids) {
            entries.add(StringTag.valueOf(uuid.toString()));
        }
        tag.put(UUIDS_KEY, entries);
        return tag;
    }
}