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

import java.util.List;

public class ShadowPower implements Power {

    private final Ability ability = new ShadowStepAbility();

    @Override
    public String getId() { return "shadow"; }

    @Override
    public String getName() { return "&8&lShadow"; }

    @Override
    public List<String> getDescription() {
        return List.of("&7Assassinate from darkness.", "&7Passive: Permanent Night Vision.");
    }

    @Override
    public Material getIcon() { return Material.OBSIDIAN; }

    @Override
    public Rarity getRarity() { return Rarity.LEGENDARY; }

    @Override
    public Ability getActiveAbility() { return ability; }

    @Override
    public void applyPassive(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, PotionEffect.INFINITE_DURATION, 0, false, false));
    }

    @Override
    public void removePassive(Player player) {
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    private static class ShadowStepAbility implements Ability {
        @Override
        public String getId() { return "shadow_step"; }

        @Override
        public String getName() { return "Shadow Step"; }

        @Override
        public String getDescription() { return "Grants temporary invisibility and agility."; }

        @Override
        public int getCooldown() { return 30; }

        @Override
        public boolean execute(Player player) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2));
            player.getWorld().spawnParticle(Particle.SQUID_INK, player.getLocation(), 50, 0.5, 1, 0.5, 0.1);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);
            return true;
        }
    }
}