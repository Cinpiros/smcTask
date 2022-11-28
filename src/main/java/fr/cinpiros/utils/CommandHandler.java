package fr.cinpiros.utils;

import fr.cinpiros.commands.MenuTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String [] args) {
        Player player = null;
        boolean needPlayer = true;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            if (args.length >= 2) {
                player = Bukkit.getPlayer(args[1]);
            }
        }

        if (args.length == 0){
            sender.sendMessage("/task <commande>");
            return true;
        }



        if (player == null) {
            List<String> listCommand = new ArrayList<>();
            listCommand.add("config");
            if (listCommand.contains(args[0])) {
                needPlayer = false;
            } else {
                if (sender instanceof ConsoleCommandSender) {
                    sender.sendMessage("/task "+args[0]+" <player>");
                } else {
                    Bukkit.getLogger().info("Error player not found by non player entity or a plugin");
                }
                return true;
            }
        }

        switch (args[0]) {
            case "help", "aide" -> {
                player.sendMessage("help");
                break;
            }
            case "version" -> {
                player.sendMessage("verssion");
                break;
            }
            case "presentoire", "menutask" -> {
                new MenuTask(player).openMenu();
                break;
            }
            default -> {
                sender.sendMessage("action non reconnue taper /task help pour de l'aide");
            }
        }


        return true;
    }
}
