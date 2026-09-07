package org.powersmp.config;

import org.powersmp.PowerSMP;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageManager {

    private final PowerSMP plugin;
    private FileConfiguration messagesConfig;

    public MessageManager(PowerSMP plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        this.messagesConfig = YamlConfiguration.loadConfiguration(file);
    }

    public String getMessage(String path) {
        String msg = messagesConfig.getString(path);
        if (msg == null) {
            return ChatColor.RED + "Missing message: " + path;
        }
        String prefix = messagesConfig.getString("prefix", "&8[&bPowerSMP&8] ");
        return ChatColor.translateAlternateColorCodes('&', prefix + msg);
    }

    public String getRaw(String path) {
        String msg = messagesConfig.getString(path, "");
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}