package org.powersmp.manager;

import org.powersmp.PowerSMP;
import org.powersmp.model.UserData;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class LivesManager {

    private final PowerSMP plugin;

    public LivesManager(PowerSMP plugin) {
        this.plugin = plugin;
    }

    public int getLives(Player player) {
        UserData data = plugin.getDataManager().getUserData(player);
        return data != null ? data.getLives() : 0;
    }

    public void addLives(Player player, int amount) {
        UserData data = plugin.getDataManager().getUserData(player);
        if (data == null) return;

        data.setLives(data.getLives() + amount);
        if (player.getGameMode() == GameMode.SPECTATOR && data.getLives() > 0) {
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    public void removeLives(Player player, int amount) {
        UserData data = plugin.getDataManager().getUserData(player);
        if (data == null) return;

        int newLives = data.getLives() - amount;
        data.setLives(newLives);

        if (newLives <= 0) {
            handleElimination(player);
        }
    }

    public void handleKill(Player killer, Player victim) {
        removeLives(victim, 1);
        addLives(killer, 1);
        killer.sendMessage(plugin.getMessageManager().getMessage("lives.stolen-killer").replace("%target%", victim.getName()));
        victim.sendMessage(plugin.getMessageManager().getMessage("lives.stolen-victim").replace("%killer%", killer.getName()));
    }

    private void handleElimination(Player player) {
        boolean banOnElimination = plugin.getConfigManager().getConfig().getBoolean("lives.ban-on-zero-lives", false);

        if (banOnElimination) {
            Bukkit.getBanList(BanList.Type.NAME).addBan(
                    player.getName(),
                    plugin.getMessageManager().getRaw("lives.ban-reason"),
                    null,
                    "PowerSMP"
            );
            player.kickPlayer(plugin.getMessageManager().getRaw("lives.ban-reason"));
        } else {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(plugin.getMessageManager().getMessage("lives.eliminated-spectator"));
        }
    }
}