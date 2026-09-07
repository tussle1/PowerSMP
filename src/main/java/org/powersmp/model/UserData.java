package org.powersmp.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserData {

    private final UUID uuid;
    private String username;
    private String powerId;
    private int lives;
    private double maxHealth;
    private BigDecimal balance;
    private final Map<String, Home> homes = new ConcurrentHashMap<>();

    public UserData(UUID uuid, String username, int lives, double maxHealth, BigDecimal balance) {
        this.uuid = uuid;
        this.username = username;
        this.lives = lives;
        this.maxHealth = maxHealth;
        this.balance = balance;
    }

    public UUID getUuid() { return uuid; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPowerId() { return powerId; }
    public void setPowerId(String powerId) { this.powerId = powerId; }
    public boolean hasPower() { return powerId != null && !powerId.isEmpty(); }

    public int getLives() { return lives; }
    public void setLives(int lives) { this.lives = Math.max(0, lives); }

    public double getMaxHealth() { return maxHealth; }
    public void setMaxHealth(double maxHealth) { this.maxHealth = maxHealth; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public Map<String, Home> getHomes() { return Collections.unmodifiableMap(homes); }
    public void addHome(Home home) { homes.put(home.getName().toLowerCase(), home); }
    public void removeHome(String name) { homes.remove(name.toLowerCase()); }
    public Home getHome(String name) { return homes.get(name.toLowerCase()); }
}