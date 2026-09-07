package org.powersmp.model;

import org.bukkit.ChatColor;

public enum Rarity {
    COMMON("Common", ChatColor.GRAY),
    RARE("Rare", ChatColor.BLUE),
    EPIC("Epic", ChatColor.DARK_PURPLE),
    LEGENDARY("Legendary", ChatColor.GOLD),
    MYTHIC("Mythic", ChatColor.RED);

    private final String displayName;
    private final ChatColor color;

    Rarity(String displayName, ChatColor color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() { return displayName; }
    public ChatColor getColor() { return color; }

    public String getFormattedName() {
        return color + "" + ChatColor.BOLD + displayName;
    }
}