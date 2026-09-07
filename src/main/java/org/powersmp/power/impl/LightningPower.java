package org.powersmp.power.impl;

import org.powersmp.model.Rarity;
import org.powersmp.power.Ability;
import org.powersmp.power.Power;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public class LightningPower implements Power {

    private final Ability ability = new SmiteAbility();

    @Override
    public String getId() { return "lightning"; }

    @Override
    public String getName() { return "&e&lLightning"; }

    @Override
    public List<String> getDescription() {
        return List.of("&7Command the skies.", "&7Ability: Smite target block.");
    }

    @Override
    public Material getIcon() { return Material.LIGHTNING_ROD; }

    @Override
    public Rarity getRarity() { return Rarity.MYTHIC; }

    @Override
    public Ability getActiveAbility() { return ability; }

    @Override
    public void applyPassive(Player player) {}

    @Override
    public void removePassive(Player player) {}

    private static class SmiteAbility implements Ability {
        @Override
        public String getId() { return "smite"; }

        @Override
        public String getName() { return "Smite"; }

        @Override
        public String getDescription() { return "Calls down lightning at your target location."; }

        @Override
        public int getCooldown() { return 15; }

        @Override
        public boolean execute(Player player) {
            Block target = player.getTargetBlockExact(20);
            if (target == null) {
                return false;
            }
            player.getWorld().strikeLightning(target.getLocation());
            return true;
        }
    }
}