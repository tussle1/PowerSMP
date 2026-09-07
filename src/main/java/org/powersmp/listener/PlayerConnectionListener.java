package org.powersmp.listener;

import org.powersmp.PowerSMP;
import org.powersmp.model.UserData;
import org.powersmp.power.Power;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final PowerSMP plugin;

    public PlayerConnectionListener(PowerSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.getDataManager().loadPlayer(player).thenAccept(data -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                if (!player.isOnline()) return;

                if (data.hasPower()) {
                    Power power = plugin.getPowerManager().getPower(data.getPowerId());
                    if (power != null) {
                        power.applyPassive(player);
                    }
                }
            });
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (plugin.getCombatManager().isInCombat(player)) {
            player.setHealth(0.0);
            plugin.getServer().broadcastMessage(
                    plugin.getMessageManager().getMessage("combat.logged-out").replace("%player%", player.getName())
            );
        }

        plugin.getDataManager().unloadPlayer(player.getUniqueId());
    }
}