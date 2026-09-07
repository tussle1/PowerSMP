package org.powersmp.manager;

import org.powersmp.PowerSMP;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TeleportManager {

    private final PowerSMP plugin;
    private final Map<UUID, UUID> tpaRequests = new ConcurrentHashMap<>();

    public TeleportManager(PowerSMP plugin) {
        this.plugin = plugin;
    }

    public void sendTpaRequest(Player sender, Player target) {
        tpaRequests.put(target.getUniqueId(), sender.getUniqueId());

        sender.sendMessage(plugin.getMessageManager().getMessage("teleport.tpa-sent").replace("%target%", target.getName()));
        target.sendMessage(plugin.getMessageManager().getMessage("teleport.tpa-received").replace("%sender%", sender.getName()));
    }

    public void acceptTpa(Player target) {
        UUID senderUuid = tpaRequests.remove(target.getUniqueId());
        if (senderUuid == null) {
            target.sendMessage(plugin.getMessageManager().getMessage("teleport.no-pending-requests"));
            return;
        }

        Player sender = plugin.getServer().getPlayer(senderUuid);
        if (sender == null || !sender.isOnline()) {
            target.sendMessage(plugin.getMessageManager().getMessage("teleport.player-offline"));
            return;
        }

        teleportWithWarmup(sender, target.getLocation());
    }

    public void teleportWithWarmup(Player player, Location destination) {
        int warmupSeconds = plugin.getConfigManager().getConfig().getInt("teleport.warmup-seconds", 3);

        if (warmupSeconds <= 0) {
            player.teleport(destination);
            return;
        }

        player.sendMessage(plugin.getMessageManager().getMessage("teleport.warmup-start").replace("%seconds%", String.valueOf(warmupSeconds)));
        Location startLoc = player.getLocation().clone();

        new BukkitRunnable() {
            int time = warmupSeconds;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                if (startLoc.distanceSquared(player.getLocation()) > 0.1) {
                    player.sendMessage(plugin.getMessageManager().getMessage("teleport.cancelled-moved"));
                    cancel();
                    return;
                }

                if (time <= 0) {
                    player.teleport(destination);
                    player.sendMessage(plugin.getMessageManager().getMessage("teleport.success"));
                    cancel();
                    return;
                }
                time--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}