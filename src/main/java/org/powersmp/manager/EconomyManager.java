package org.powersmp.manager;

import org.powersmp.PowerSMP;
import org.powersmp.model.UserData;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class EconomyManager {

    private final PowerSMP plugin;

    public EconomyManager(PowerSMP plugin) {
        this.plugin = plugin;
    }

    public BigDecimal getBalance(Player player) {
        UserData data = plugin.getDataManager().getUserData(player);
        return data != null ? data.getBalance() : BigDecimal.ZERO;
    }

    public boolean has(Player player, BigDecimal amount) {
        return getBalance(player).compareTo(amount) >= 0;
    }

    public void deposit(Player player, BigDecimal amount) {
        UserData data = plugin.getDataManager().getUserData(player);
        if (data != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            data.setBalance(data.getBalance().add(amount));
        }
    }

    public boolean withdraw(Player player, BigDecimal amount) {
        if (!has(player, amount)) return false;

        UserData data = plugin.getDataManager().getUserData(player);
        if (data != null) {
            data.setBalance(data.getBalance().subtract(amount));
            return true;
        }
        return false;
    }
}