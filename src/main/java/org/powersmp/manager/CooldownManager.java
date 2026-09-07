package org.powersmp.manager;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownManager {

    private final Map<UUID, Map<String, Long>> cooldowns = new ConcurrentHashMap<>();

    public void setCooldown(UUID player, String key, int seconds) {
        cooldowns.computeIfAbsent(player, k -> new ConcurrentHashMap<>())
                .put(key, System.currentTimeMillis() + (seconds * 1000L));
    }

    public boolean hasCooldown(UUID player, String key) {
        return getRemainingSeconds(player, key) > 0;
    }

    public long getRemainingSeconds(UUID player, String key) {
        Map<String, Long> playerMap = cooldowns.get(player);
        if (playerMap == null) return 0;

        Long expire = playerMap.get(key);
        if (expire == null) return 0;

        long remaining = (expire - System.currentTimeMillis()) / 1000;
        if (remaining <= 0) {
            playerMap.remove(key);
            return 0;
        }
        return remaining;
    }

    public void removeCooldown(UUID player, String key) {
        Map<String, Long> playerMap = cooldowns.get(player);
        if (playerMap != null) {
            playerMap.remove(key);
        }
    }
}