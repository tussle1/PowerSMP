package org.powersmp.database;

import org.powersmp.PowerSMP;
import org.powersmp.model.Home;
import org.powersmp.model.UserData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class YamlStorage implements StorageEngine {

    private final PowerSMP plugin;
    private File playersDir;

    public YamlStorage(PowerSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        playersDir = new File(plugin.getDataFolder(), "players");
        if (!playersDir.exists()) {
            playersDir.mkdirs();
        }
    }

    @Override
    public CompletableFuture<UserData> loadUser(UUID uuid, String username) {
        return CompletableFuture.supplyAsync(() -> {
            int defaultLives = plugin.getConfigManager().getConfig().getInt("lives.default-lives", 3);
            double defaultHealth = plugin.getConfigManager().getConfig().getDouble("health.starting-health", 20.0);
            BigDecimal defaultBal = BigDecimal.valueOf(plugin.getConfigManager().getConfig().getDouble("economy.starting-balance", 1000.0));

            UserData data = new UserData(uuid, username, defaultLives, defaultHealth, defaultBal);
            File userFile = new File(playersDir, uuid.toString() + ".yml");

            if (!userFile.exists()) {
                return data;
            }

            YamlConfiguration config = YamlConfiguration.loadConfiguration(userFile);
            data.setUsername(config.getString("username", username));
            data.setPowerId(config.getString("power", null));
            data.setLives(config.getInt("lives", defaultLives));
            data.setMaxHealth(config.getDouble("max-health", defaultHealth));
            data.setBalance(BigDecimal.valueOf(config.getDouble("balance", defaultBal.doubleValue())));

            ConfigurationSection homesSec = config.getConfigurationSection("homes");
            if (homesSec != null) {
                for (String key : homesSec.getKeys(false)) {
                    String world = homesSec.getString(key + ".world");
                    double x = homesSec.getDouble(key + ".x");
                    double y = homesSec.getDouble(key + ".y");
                    double z = homesSec.getDouble(key + ".z");
                    float yaw = (float) homesSec.getDouble(key + ".yaw");
                    float pitch = (float) homesSec.getDouble(key + ".pitch");
                    data.addHome(new Home(key, world, x, y, z, yaw, pitch));
                }
            }
            return data;
        });
    }

    @Override
    public CompletableFuture<Void> saveUser(UserData userData) {
        return CompletableFuture.runAsync(() -> {
            File userFile = new File(playersDir, userData.getUuid().toString() + ".yml");
            YamlConfiguration config = new YamlConfiguration();

            config.set("username", userData.getUsername());
            config.set("power", userData.getPowerId());
            config.set("lives", userData.getLives());
            config.set("max-health", userData.getMaxHealth());
            config.set("balance", userData.getBalance().doubleValue());

            for (Home home : userData.getHomes().values()) {
                String path = "homes." + home.getName() + ".";
                config.set(path + "world", home.getWorldName());
                config.set(path + "x", home.getX());
                config.set(path + "y", home.getY());
                config.set(path + "z", home.getZ());
                config.set(path + "yaw", home.getYaw());
                config.set(path + "pitch", home.getPitch());
            }

            try {
                config.save(userFile);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save YAML data for " + userData.getUsername(), e);
            }
        });
    }

    @Override
    public void shutdown() {}
}