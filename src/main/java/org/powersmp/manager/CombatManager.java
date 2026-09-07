package org.powersmp.manager;

import org.powersmp.PowerSMP;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CombatManager {

    private final PowerSMP plugin;
    private final Map<UUID, Long> combatTags = new ConcurrentHashMap<>();

    public CombatManager(PowerSMP plugin) {
        this.plugin = plugin;
    }

    public void tagPlayer(Player player) {
        int duration = plugin.getConfigManager().getConfig().getInt("combat.tag-duration", 15);
        boolean wasInCombat = isInCombat(player);

        combatTags.put(player.getUniqueId(), System.currentTimeMillis() + (duration * 1000L));

        if (!wasInCombat) {
            player.sendMessage(plugin.getMessageManager().getMessage("combat.tagged"));
        }
    }

    public boolean isInCombat(Player player) {
        Long expire = combatTags.get(player.getUniqueId());
        if (expire == null) return false;

        if (System.currentTimeMillis() >= expire) {
            combatTags.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    public void removeTag(Player player) {
        if (combatTags.remove(player.getUniqueId()) != null) {
            player.sendMessage(plugin.getMessageManager().getMessage("combat.untagged"));
        }
    }

    public long getRemainingCombatTime(Player player) {
        Long expire = combatTags.get(player.getUniqueId());
        if (expire == null) return 0;

        long remaining = (expire - System.currentTimeMillis()) / 1000;
        return Math.max(0, remaining);
    }
}