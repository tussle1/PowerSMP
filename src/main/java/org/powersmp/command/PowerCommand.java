package org.powersmp.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.powersmp.PowerSMP;
import org.powersmp.power.Power;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PowerCommand implements CommandExecutor, TabCompleter {

    private final PowerSMP plugin;

    public PowerCommand(PowerSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "list":
                handleList(sender);
                break;
            case "info":
                handleInfo(sender, args);
                break;
            case "set":
                handleSet(sender, args);
                break;
            case "clear":
                handleClear(sender, args);
                break;
            case "reload":
                handleReload(sender);
                break;
            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m----------------------------------"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lPowerSMP &7- Commands"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/power list &7- List all available powers"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/power info [power] &7- View power details"));
        if (sender.hasPermission("powersmp.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/power set <player> <power> &7- Assign a power"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/power clear <player> &7- Remove a player's power"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3/power reload &7- Reload plugin configs"));
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m----------------------------------"));
    }

    private void handleList(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lAvailable Powers:"));
        for (Power power : plugin.getPowerManager().getRegisteredPowers()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                "&7- &e" + power.getName() + " &8(&7ID: " + power.getId() + "&8)"));
        }
    }

    private void handleInfo(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /power info <power>");
            return;
        }

        Power power = plugin.getPowerManager().getPower(args[1]);
        if (power == null) {
            sender.sendMessage(ChatColor.RED + "Power not found: " + args[1]);
            return;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lPower Info: &e" + power.getName()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Rarity: " + power.getRarity().getDisplayName()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Description:"));
        for (String line : power.getDescription()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &7" + line));
        }
    }

    private void handleSet(CommandSender sender, String[] args) {
        if (!sender.hasPermission("powersmp.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /power set <player> <power>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found or offline.");
            return;
        }

        Power power = plugin.getPowerManager().getPower(args[2]);
        if (power == null) {
            sender.sendMessage(ChatColor.RED + "Power not found: " + args[2]);
            return;
        }

        plugin.getPowerManager().setPlayerPower(target, power);
        sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s power to " + power.getName() + ".");
        target.sendMessage(ChatColor.GREEN + "Your power has been set to " + power.getName() + "!");
    }

    private void handleClear(CommandSender sender, String[] args) {
        if (!sender.hasPermission("powersmp.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /power clear <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found or offline.");
            return;
        }

        plugin.getPowerManager().setPlayerPower(target, null);
        sender.sendMessage(ChatColor.YELLOW + "Cleared " + target.getName() + "'s power.");
        target.sendMessage(ChatColor.YELLOW + "Your power has been cleared.");
    }

    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("powersmp.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return;
        }

        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "PowerSMP configuration reloaded!");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> subs = new ArrayList<>(Arrays.asList("list", "info"));
            if (sender.hasPermission("powersmp.admin")) {
                subs.addAll(Arrays.asList("set", "clear", "reload"));
            }
            return filter(subs, args[0]);
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info") || (args[0].equalsIgnoreCase("set") && sender.hasPermission("powersmp.admin"))) {
                List<String> powerIds = plugin.getPowerManager().getRegisteredPowers().stream()
                        .map(Power::getId)
                        .collect(Collectors.toList());
                return filter(powerIds, args[1]);
            }
            if (args[0].equalsIgnoreCase("clear") && sender.hasPermission("powersmp.admin")) {
                return null;
            }
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("set") && sender.hasPermission("powersmp.admin")) {
            List<String> powerIds = plugin.getPowerManager().getRegisteredPowers().stream()
                    .map(Power::getId)
                    .collect(Collectors.toList());
            return filter(powerIds, args[2]);
        }

        return new ArrayList<>();
    }

    private List<String> filter(List<String> list, String prefix) {
        return list.stream()
                .filter(s -> s.toLowerCase().startsWith(prefix.toLowerCase()))
                .collect(Collectors.toList());
    }
}