package org.powersmp.power.impl;

import org.powersmp.model.Rarity;
import org.powersmp.power.Ability;
import org.powersmp.power.Power;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;

public class TankPower implements Power {

    private final Ability ability = new GroundSlamAbility();

    @Override
    public String getId() { return "tank"; }

    @Override
    public String getName() { return "&a&lTank"; }

    @Override
    public List<String> getDescription() {
        return List.of("&7Immovable fortress.", "&7Passive: Permanent Resistance I.");
    }

    @Override
    public Material getIcon() { return Material.ANVIL; }

    @Override
    public Rarity getRarity() { return Rarity.EPIC; }

    @Override
    public Ability getActiveAbility() { return ability; }

    @Override
    public void applyPassive(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, PotionEffect.INFINITE_DURATION, 0, false, false));
    }

    @Override
    public void removePassive(Player player) {
        player.removePotionEffect(PotionEffectType.RESISTANCE);
    }

    private static class GroundSlamAbility implements Ability {
        @Override
        public String getId() { return "ground_slam"; }

        @Override
        public String getName() { return "Ground Slam"; }

        @Override
        public String getDescription() { return "Knocks back and damages nearby enemies."; }

        @Override
        public int getCooldown() { return 25; }

        @Override
        public boolean execute(Player player) {
            player.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, player.getLocation(), 1);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.8f);

            for (Entity entity : player.getNearbyEntities(6, 4, 6)) {
                if (entity instanceof LivingEntity target && !entity.equals(player)) {
                    target.damage(6.0, player);
                    Vector kb = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(1.5).setY(0.6);
                    target.setVelocity(kb);
                }
            }
            return true;
        }
    }
}