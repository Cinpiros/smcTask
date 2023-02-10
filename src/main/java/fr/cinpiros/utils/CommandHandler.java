package fr.cinpiros.utils;

import fr.cinpiros.commands.GiveTask;
import fr.cinpiros.commands.GiveTaskClasseur;
import fr.cinpiros.inventory.OpenInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String [] args) {
        if (args.length == 0){
            sender.sendMessage("/task <commande>");
            return true;
        }

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
        }
        boolean commandReturn = true;
        switch (args[0]) {
            case "presentoire", "menutask" -> commandReturn = new OpenInventory(player).panelMenu();

            case "taskbundle", "bundle" -> commandReturn = new GiveTaskClasseur(player).giveClasseur();

            case "give" -> {
                if (args[2] != null) {
                    commandReturn = new GiveTask(player, sender).giveTask(args[2]);
                } else {
                    sender.sendMessage("/task "+args[0]+" "+player+" <task id>");
                }
            }

            default -> Bukkit.getLogger().info("argument not found /task help");
        }


        return commandReturn;
    }
}
