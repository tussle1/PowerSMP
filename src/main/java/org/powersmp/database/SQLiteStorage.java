package org.powersmp.database;

import org.powersmp.PowerSMP;
import org.powersmp.model.Home;
import org.powersmp.model.UserData;

import java.io.File;
import java.math.BigDecimal;
import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class SQLiteStorage implements StorageEngine {

    private final PowerSMP plugin;
    private final File dbFile;
    private Connection connection;

    public SQLiteStorage(PowerSMP plugin) {
        this.plugin = plugin;
        this.dbFile = new File(plugin.getDataFolder(), "data.db");
    }

    @Override
    public void init() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());

            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                        "uuid VARCHAR(36) PRIMARY KEY, " +
                        "username VARCHAR(16), " +
                        "power VARCHAR(64), " +
                        "lives INT, " +
                        "max_health DOUBLE, " +
                        "balance DECIMAL(15,2)" +
                        ");");

                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS homes (" +
                        "uuid VARCHAR(36), " +
                        "name VARCHAR(32), " +
                        "world VARCHAR(64), " +
                        "x DOUBLE, y DOUBLE, z DOUBLE, " +
                        "yaw FLOAT, pitch FLOAT, " +
                        "PRIMARY KEY (uuid, name)" +
                        ");");
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize SQLite database", e);
        }
    }

    @Override
    public CompletableFuture<UserData> loadUser(UUID uuid, String username) {
        return CompletableFuture.supplyAsync(() -> {
            int defaultLives = plugin.getConfigManager().getConfig().getInt("lives.default-lives", 3);
            double defaultHealth = plugin.getConfigManager().getConfig().getDouble("health.starting-health", 20.0);
            BigDecimal defaultBal = BigDecimal.valueOf(plugin.getConfigManager().getConfig().getDouble("economy.starting-balance", 1000.0));

            UserData data = new UserData(uuid, username, defaultLives, defaultHealth, defaultBal);

            try {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE uuid = ?");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    data.setUsername(rs.getString("username"));
                    data.setPowerId(rs.getString("power"));
                    data.setLives(rs.getInt("lives"));
                    data.setMaxHealth(rs.getDouble("max_health"));
                    data.setBalance(rs.getBigDecimal("balance"));
                }
                rs.close();
                ps.close();

                PreparedStatement psHomes = connection.prepareStatement("SELECT * FROM homes WHERE uuid = ?");
                psHomes.setString(1, uuid.toString());
                ResultSet rsHomes = psHomes.executeQuery();

                while (rsHomes.next()) {
                    Home home = new Home(
                            rsHomes.getString("name"),
                            rsHomes.getString("world"),
                            rsHomes.getDouble("x"),
                            rsHomes.getDouble("y"),
                            rsHomes.getDouble("z"),
                            rsHomes.getFloat("yaw"),
                            rsHomes.getFloat("pitch")
                    );
                    data.addHome(home);
                }
                rsHomes.close();
                psHomes.close();

            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load user data for " + username, e);
            }
            return data;
        });
    }

    @Override
    public CompletableFuture<Void> saveUser(UserData userData) {
        return CompletableFuture.runAsync(() -> {
            try {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO users (uuid, username, power, lives, max_health, balance) " +
                                "VALUES (?, ?, ?, ?, ?, ?) " +
                                "ON CONFLICT(uuid) DO UPDATE SET " +
                                "username = excluded.username, " +
                                "power = excluded.power, " +
                                "lives = excluded.lives, " +
                                "max_health = excluded.max_health, " +
                                "balance = excluded.balance;"
                );

                ps.setString(1, userData.getUuid().toString());
                ps.setString(2, userData.getUsername());
                ps.setString(3, userData.getPowerId());
                ps.setInt(4, userData.getLives());
                ps.setDouble(5, userData.getMaxHealth());
                ps.setBigDecimal(6, userData.getBalance());
                ps.executeUpdate();
                ps.close();

                PreparedStatement delHomes = connection.prepareStatement("DELETE FROM homes WHERE uuid = ?");
                delHomes.setString(1, userData.getUuid().toString());
                delHomes.executeUpdate();
                delHomes.close();

                PreparedStatement insHomes = connection.prepareStatement(
                        "INSERT INTO homes (uuid, name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                );
                for (Home home : userData.getHomes().values()) {
                    insHomes.setString(1, userData.getUuid().toString());
                    insHomes.setString(2, home.getName());
                    insHomes.setString(3, home.getWorldName());
                    insHomes.setDouble(4, home.getX());
                    insHomes.setDouble(5, home.getY());
                    insHomes.setDouble(6, home.getZ());
                    insHomes.setFloat(7, home.getYaw());
                    insHomes.setFloat(8, home.getPitch());
                    insHomes.addBatch();
                }
                insHomes.executeBatch();
                insHomes.close();

            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save user data for " + userData.getUsername(), e);
            }
        });
    }

    @Override
    public void shutdown() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to close SQLite connection", e);
        }
    }
}