package org.powersmp.power.impl;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.powersmp.PowerSMP;
import org.powersmp.power.Power;
import org.powersmp.power.PowerType;

public class TankPower extends Power {

    public TankPower(PowerSMP plugin) {
        super(plugin, PowerType.TANK, "Tank", 45);
    }

    @Override
    public void applyPassiveEffects(Player player) {
        // Continuous Resistance I passive
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.DAMAGE_RESISTANCE,
                Integer.MAX_VALUE,
                0,
                false,
                false,
                true
        ));
    }

    @Override
    public void removePassiveEffects(Player player) {
        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
    }

    @Override
    public boolean executeAbility(Player player) {
        Location loc = player.getLocation();

        // High resistance surge ability
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.DAMAGE_RESISTANCE,
                200, // 10 seconds (200 ticks)
                2,   // Resistance III
                false,
                true,
                true
        ));

        // Absorption hearts boost
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.ABSORPTION,
                300, // 15 seconds
                1,   // Absorption II
                false,
                true,
                true
        ));

        // Visual and sound effects
        if (loc.getWorld() != null) {
            loc.getWorld().spawnParticle(Particle.EXPLOSION, loc, 1);
            loc.getWorld().playSound(loc, Sound.ENTITY_IRON_GOLEM_ATTACK, 1.0f, 0.5f);
        }

        player.sendMessage("§8[§bPowerSMP§8] §aYou activated §eFortify§a! (Resistance III & Absorption)");
        return true;
    }
}