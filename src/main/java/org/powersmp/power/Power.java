package org.powersmp.power;

import org.powersmp.model.Rarity;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public interface Power {

    String getId();

    String getName();

    List<String> getDescription();

    Material getIcon();

    Rarity getRarity();

    Ability getActiveAbility();

    void applyPassive(Player player);

    void removePassive(Player player);
}