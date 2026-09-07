package org.powersmp.power.impl;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.powersmp.model.Rarity;
import org.powersmp.power.Ability;
import org.powersmp.power.Power;

import java.util.Arrays;
import java.util.List;

public class TankPower implements Power {

    @Override
    public String getId() {
        return "tank";
    }

    @Override
    public String getName() {
        return "Tank";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
            "§7Passive: Permanent Resistance I.",
            "§7Active: Gain Resistance III and Absorption II for 10s."
        );
    }

    @Override
    public Material getIcon() {
        return Material.NETHERITE_CHESTPLATE;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public Ability getActiveAbility() {
        return new Ability() {
            @Override
            public String getId() {
                return "fortify";
            }

            @Override
            public String getName() {
                return "Fortify";
            }

            @Override
            public String getDescription() {
                return "Gives Resistance III and Absorption II.";
            }

            @Override
            public int getCooldown() {
                return 45;
            }

            @Override
            public boolean execute(Player player) {
                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.DAMAGE_RESISTANCE,
                        200, // 10 seconds
                        2,   // Resistance III
                        false,
                        true,
                        true
                ));

                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.ABSORPTION,
                        300, // 15 seconds
                        1,   // Absorption II
                        false,
                        true,
                        true
                ));

                if (player.getLocation().getWorld() != null) {
                    player.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, player.getLocation(), 10);
                    player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_ATTACK, 1.0f, 0.5f);
                }

                player.sendMessage("§8[§bPowerSMP§8] §aYou activated §eFortify§a!");
                return true;
            }
        };
    }

    @Override
    public void applyPassive(Player player) {
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
    public void removePassive(Player player) {
        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
    }
}