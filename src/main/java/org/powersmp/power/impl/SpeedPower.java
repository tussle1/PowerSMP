package org.powersmp.power.impl;

import org.powersmp.model.Rarity;
import org.powersmp.power.Ability;
import org.powersmp.power.Power;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;

public class SpeedPower implements Power {

    private final Ability ability = new DashAbility();

    @Override
    public String getId() { return "speed"; }

    @Override
    public String getName() { return "&b&lSpeed"; }

    @Override
    public List<String> getDescription() {
        return List.of("&7Lightning speed on the battlefield.", "&7Passive: Permanent Speed II.");
    }

    @Override
    public Material getIcon() { return Material.SUGAR; }

    @Override
    public Rarity getRarity() { return Rarity.COMMON; }

    @Override
    public Ability getActiveAbility() { return ability; }

    @Override
    public void applyPassive(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 1, false, false));
    }

    @Override
    public void removePassive(Player player) {
        player.removePotionEffect(PotionEffectType.SPEED);
    }

    private static class DashAbility implements Ability {
        @Override
        public String getId() { return "dash"; }

        @Override
        public String getName() { return "Dash"; }

        @Override
        public String getDescription() { return "Launches you forward instantly."; }

        @Override
        public int getCooldown() { return 10; }

        @Override
        public boolean execute(Player player) {
            Vector dir = player.getLocation().getDirection().multiply(2.2).setY(0.4);
            player.setVelocity(dir);
            player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 30, 0.5, 0.2, 0.5, 0.05);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.2f);
            return true;
        }
    }
}