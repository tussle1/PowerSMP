package org.powersmp.listener;

import org.powersmp.PowerSMP;
import org.powersmp.model.UserData;
import org.powersmp.power.Ability;
import org.powersmp.power.Power;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class AbilityTriggerListener implements Listener {

    private final PowerSMP plugin;

    public AbilityTriggerListener(PowerSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        if (!player.isSneaking()) return;

        UserData userData = plugin.getDataManager().getUserData(player);
        if (userData == null || !userData.hasPower()) return;

        Power power = plugin.getPowerManager().getPower(userData.getPowerId());
        if (power == null) return;

        Ability ability = power.getActiveAbility();
        if (ability == null) return;

        String cooldownKey = "ability_" + ability.getId();
        if (plugin.getCooldownManager().hasCooldown(player.getUniqueId(), cooldownKey)) {
            long remaining = plugin.getCooldownManager().getRemainingSeconds(player.getUniqueId(), cooldownKey);
            player.sendMessage(plugin.getMessageManager().getMessage("ability.cooldown").replace("%seconds%", String.valueOf(remaining)));
            return;
        }

        boolean success = ability.execute(player);
        if (success) {
            plugin.getCooldownManager().setCooldown(player.getUniqueId(), cooldownKey, ability.getCooldown());
            player.sendMessage(plugin.getMessageManager().getMessage("ability.used").replace("%ability%", ability.getName()));
        }
    }
}