package org.powersmp.config;

import org.powersmp.PowerSMP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private final PowerSMP plugin;
    private FileConfiguration config;
    private FileConfiguration guiConfig;
    private FileConfiguration powersConfig;

    public ConfigManager(PowerSMP plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();

        this.guiConfig = loadYaml("gui.yml");
        this.powersConfig = loadYaml("powers.yml");
    }

    private FileConfiguration loadYaml(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() { return config; }
    public FileConfiguration getGuiConfig() { return guiConfig; }
    public FileConfiguration getPowersConfig() { return powersConfig; }
}