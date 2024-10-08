package fr.cinpiros.utils;

import fr.cinpiros.commands.GiveTask;
import fr.cinpiros.commands.GiveTaskClasseur;
import fr.cinpiros.inventory.OpenInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String [] args) {
        if (args.length == 0){
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/task <commande>"));
            } else {
                Bukkit.getLogger().warning("[SmcTask] Commend send with no argument");
            }
            return true;
        }
        /*
        Player player = null;
        if (args.length >= 2) {
            player = Bukkit.getPlayer(args[1]);
        }

        if (player == null) {
            List<String> listCommandSelfOnly = new ArrayList<>();
            listCommandSelfOnly.add("config");
            if (!listCommandSelfOnly.contains(args[0])) {
                List<String> listCommandConsoleOnly = new ArrayList<>();
                listCommandConsoleOnly.add("sync");
                if (listCommandConsoleOnly.contains(args[0])) {
                    if (!(sender instanceof ConsoleCommandSender)) {
                        return true;
                    }
                } else {
                    if (sender instanceof Player) {
                        sender.sendMessage("/task "+args[0]+" <player>");
                    } else if (sender instanceof ConsoleCommandSender) {
                        Bukkit.getLogger().info("/task "+args[0]+" <player>");
                    }
                    return true;
                }
            } else {
                if (!(sender instanceof Player)) {
                    return true;
                }
            }
        }*/

        boolean commandReturn = true;
        switch (args[0]) {
            case "config" -> {
                commandReturn = new Test().test();
            }

            case "panel" -> {
                if (args.length < 2) {
                    if (sender instanceof Player) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/task "+args[0]+" <player>"));
                    } else {
                        Bukkit.getLogger().info("[SmcTask] Wrong usage of command: task "+args[0]+" <player>");
                    }
                    break;
                }

                final Player player = Bukkit.getPlayer(args[1]);

                if (player == null) {
                    if (sender instanceof Player) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis player is not connected"));
                    } else {
                        Bukkit.getLogger().info("[SmcTask] Command send non-existent player");
                    }
                    break;
                }

                commandReturn = new OpenInventory(player).panelMenu();
            }

            case "workbook" -> {
                if (args.length < 2) {
                    if (sender instanceof Player) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/task "+args[0]+" <player>"));
                    } else {
                        Bukkit.getLogger().warning("[SmcTask] Wrong usage of command: task "+args[0]+" <player>");
                    }
                    break;
                }

                final Player player = Bukkit.getPlayer(args[1]);

                if (player == null) {
                    if (sender instanceof Player) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis player is not connected"));
                    } else {
                        Bukkit.getLogger().warning("[SmcTask] Command send non-existent player");
                    }
                    break;
                }

                commandReturn = new GiveTaskClasseur(player).giveClasseur();
            }

            case "give" -> {
                if (args.length < 2) {
                    if (sender instanceof Player) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/task "+args[0]+" <player>"));
                    } else {
                        Bukkit.getLogger().warning("[SmcTask] Wrong usage of command: task "+args[0]+" <player>");
                    }
                    break;
                }

                final Player player = Bukkit.getPlayer(args[1]);

                if (player == null) {
                    if (sender instanceof Player) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis player is not connected"));
                    } else {
                        Bukkit.getLogger().warning("[SmcTask] Command send non-existent player");
                    }
                    break;
                }


                if (args[2] != null) {
                    commandReturn = new GiveTask(player, sender).giveTask(args[2]);
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/task "+args[0]+" "+player+" <task id>"));
                }
            }

            default -> {
                if (sender instanceof Player) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cArgument not found /task help"));
                } else {
                    Bukkit.getLogger().warning("[SmcTask] Command send with wrong arguments");
                }
            }
        }
        return commandReturn;
    }
}
