package org.powersmp.manager;

import org.powersmp.PowerSMP;
import org.powersmp.database.SQLiteStorage;
import org.powersmp.database.StorageEngine;
import org.powersmp.database.YamlStorage;
import org.powersmp.model.UserData;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {

    private final PowerSMP plugin;
    private final StorageEngine storageEngine;
    private final Map<UUID, UserData> userCache;

    public DataManager(PowerSMP plugin) {
        this.plugin = plugin;
        this.userCache = new ConcurrentHashMap<>();

        String type = plugin.getConfigManager().getConfig().getString("settings.storage-type", "SQLITE");
        if (type.equalsIgnoreCase("YAML")) {
            this.storageEngine = new YamlStorage(plugin);
        } else {
            this.storageEngine = new SQLiteStorage(plugin);
        }
        this.storageEngine.init();
    }

    public CompletableFuture<UserData> loadPlayer(Player player) {
        return storageEngine.loadUser(player.getUniqueId(), player.getName()).thenApply(data -> {
            userCache.put(player.getUniqueId(), data);
            return data;
        });
    }

    public UserData getUserData(UUID uuid) {
        return userCache.get(uuid);
    }

    public UserData getUserData(Player player) {
        return getUserData(player.getUniqueId());
    }

    public void unloadPlayer(UUID uuid) {
        UserData data = userCache.remove(uuid);
        if (data != null) {
            storageEngine.saveUser(data);
        }
    }

    public void saveAll() {
        for (UserData data : userCache.values()) {
            storageEngine.saveUser(data);
        }
    }

    public void shutdown() {
        saveAll();
        storageEngine.shutdown();
    }
}