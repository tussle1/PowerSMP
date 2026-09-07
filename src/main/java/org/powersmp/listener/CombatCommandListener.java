package org.powersmp.listener;

import org.powersmp.PowerSMP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CombatCommandListener implements Listener {

    private final PowerSMP plugin;

    public CombatCommandListener(PowerSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getCombatManager().isInCombat(player)) return;
        if (player.hasPermission("powersmp.admin.bypasscombat")) return;

        String command = event.getMessage().substring(1).split(" ")[0].toLowerCase();
        List<String> blockedCommands = plugin.getConfigManager().getConfig().getStringList("combat.blocked-commands");

        if (blockedCommands.contains(command)) {
            event.setCancelled(true);
            long remaining = plugin.getCombatManager().getRemainingCombatTime(player);
            player.sendMessage(plugin.getMessageManager().getMessage("combat.command-blocked").replace("%seconds%", String.valueOf(remaining)));
        }
    }
}