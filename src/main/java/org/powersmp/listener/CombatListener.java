package org.powersmp.listener;

import org.powersmp.PowerSMP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class CombatListener implements Listener {

    private final PowerSMP plugin;

    public CombatListener(PowerSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPvPDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;

        Player attacker = null;
        if (event.getDamager() instanceof Player p) {
            attacker = p;
        }

        if (attacker != null && !attacker.equals(victim)) {
            plugin.getCombatManager().tagPlayer(victim);
            plugin.getCombatManager().tagPlayer(attacker);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null && !killer.equals(victim)) {
            plugin.getLivesManager().handleKill(killer, victim);
        } else {
            plugin.getLivesManager().removeLives(victim, 1);
            victim.sendMessage(plugin.getMessageManager().getMessage("lives.lost-death"));
        }

        plugin.getCombatManager().removeTag(victim);
    }
}