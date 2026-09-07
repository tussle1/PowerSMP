package org.powersmp.power;

import org.bukkit.entity.Player;

public interface Ability {

    String getId();

    String getName();

    String getDescription();

    int getCooldown();

    boolean execute(Player player);
}