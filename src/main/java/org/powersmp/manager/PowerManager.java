package org.powersmp.manager;

import org.powersmp.PowerSMP;
import org.powersmp.power.Power;
import org.powersmp.power.impl.*;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PowerManager {

    private final PowerSMP plugin;
    private final Map<String, Power> registeredPowers = new ConcurrentHashMap<>();

    public PowerManager(PowerSMP plugin) {
        this.plugin = plugin;
        registerDefaultPowers();
    }

    private void registerDefaultPowers() {
        registerPower(new FlamePower());
        registerPower(new SpeedPower());
        registerPower(new TankPower());
        registerPower(new ShadowPower());
        registerPower(new LightningPower());
    }

    public void registerPower(Power power) {
        registeredPowers.put(power.getId().toLowerCase(), power);
    }

    public Power getPower(String id) {
        if (id == null) return null;
        return registeredPowers.get(id.toLowerCase());
    }

    public Collection<Power> getRegisteredPowers() {
        return registeredPowers.values();
    }

    public void setPlayerPower(Player player, Power power) {
        var userData = plugin.getDataManager().getUserData(player);
        if (userData == null) return;

        if (userData.hasPower()) {
            Power oldPower = getPower(userData.getPowerId());
            if (oldPower != null) {
                oldPower.removePassive(player);
            }
        }

        if (power != null) {
            userData.setPowerId(power.getId());
            power.applyPassive(player);
        } else {
            userData.setPowerId(null);
        }
    }
}