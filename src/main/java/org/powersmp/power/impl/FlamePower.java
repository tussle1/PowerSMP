package org.powersmp.power.impl;

import org.powersmp.model.Rarity;
import org.powersmp.power.Ability;
import org.powersmp.power.Power;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class FlamePower implements Power {

    private final Ability ability = new FlameBurstAbility();

    @Override
    public String getId() { return "flame"; }

    @Override
    public String getName() { return "&c&lFlame"; }

    @Override
    public List<String> getDescription() {
        return List.of("&7Master of elemental fire.", "&7Passive: Permanent Fire Resistance.");
    }

    @Override
    public Material getIcon() { return Material.FIRE_CHARGE; }

    @Override
    public Rarity getRarity() { return Rarity.RARE; }

    @Override
    public Ability getActiveAbility() { return ability; }

    @Override
    public void applyPassive(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, PotionEffect.INFINITE_DURATION, 0, false, false));
    }

    @Override
    public void removePassive(Player player) {
        player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
    }

    private static class FlameBurstAbility implements Ability {
        @Override
        public String getId() { return "fire_burst"; }

        @Override
        public String getName() { return "Fire Burst"; }

        @Override
        public String getDescription() { return "Creates a flame explosion igniting nearby targets."; }

        @Override
        public int getCooldown() { return 20; }

        @Override
        public boolean execute(Player player) {
            Location loc = player.getLocation();
            player.getWorld().spawnParticle(Particle.FLAME, loc, 100, 3, 1, 3, 0.1);
            player.getWorld().playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0f, 1.0f);

            for (Entity entity : player.getNearbyEntities(5, 5, 5)) {
                if (entity instanceof LivingEntity target && !entity.equals(player)) {
                    target.damage(4.0, player);
                    target.setFireTicks(100);
                }
            }
            return true;
        }
    }
}