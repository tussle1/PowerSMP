package org.powersmp;

import org.powersmp.config.ConfigManager;
import org.powersmp.config.MessageManager;
import org.powersmp.listener.AbilityTriggerListener;
import org.powersmp.listener.CombatCommandListener;
import org.powersmp.listener.CombatListener;
import org.powersmp.listener.PlayerConnectionListener;
import org.powersmp.manager.CombatManager;
import org.powersmp.manager.CooldownManager;
import org.powersmp.manager.DataManager;
import org.powersmp.manager.EconomyManager;
import org.powersmp.manager.LivesManager;
import org.powersmp.manager.PowerManager;
import org.powersmp.manager.TeleportManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PowerSMP extends JavaPlugin {

    private static PowerSMP instance;

    private ConfigManager configManager;
    private MessageManager messageManager;
    private DataManager dataManager;
    private CooldownManager cooldownManager;
    private PowerManager powerManager;
    private LivesManager livesManager;
    private CombatManager combatManager;
    private EconomyManager economyManager;
    private TeleportManager teleportManager;

    @Override
    public void onEnable() {
        instance = this;

        this.configManager = new ConfigManager(this);
        this.messageManager = new MessageManager(this);
        this.dataManager = new DataManager(this);
        this.cooldownManager = new CooldownManager();
        this.powerManager = new PowerManager(this);
        this.livesManager = new LivesManager(this);
        this.combatManager = new CombatManager(this);
        this.economyManager = new EconomyManager(this);
        this.teleportManager = new TeleportManager(this);

        registerListeners();

        getLogger().info("PowerSMP initialized successfully!");
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerConnectionListener(this), this);
        pm.registerEvents(new AbilityTriggerListener(this), this);
        pm.registerEvents(new CombatListener(this), this);
        pm.registerEvents(new CombatCommandListener(this), this);
    }

    @Override
    public void onDisable() {
        if (dataManager != null) {
            dataManager.shutdown();
        }
        getLogger().info("PowerSMP shut down successfully.");
        instance = null;
    }

    public static PowerSMP getInstance() { return instance; }
    public ConfigManager getConfigManager() { return configManager; }
    public MessageManager getMessageManager() { return messageManager; }
    public DataManager getDataManager() { return dataManager; }
    public CooldownManager getCooldownManager() { return cooldownManager; }
    public PowerManager getPowerManager() { return powerManager; }
    public LivesManager getLivesManager() { return livesManager; }
    public CombatManager getCombatManager() { return combatManager; }
    public EconomyManager getEconomyManager() { return economyManager; }
    public TeleportManager getTeleportManager() { return teleportManager; }
}