package fr.cinpiros.utils;

import fr.cinpiros.commands.GiveTaskBundle;
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
            if (!listCommand.contains(args[0])) {
                if (sender instanceof ConsoleCommandSender) {
                    sender.sendMessage("/task "+args[0]+" <player>");
                } else {
                    Bukkit.getLogger().info("Error player not found by non player entity or a plugin");
                }
                return true;
            }
        }
        Boolean commandReturn = true;
        switch (args[0]) {
            case "help", "aide" -> {
                player.sendMessage("help");
                commandReturn = true;
            }
            case "version" -> {
                player.sendMessage("verssion");
                commandReturn = true;
            }
            case "presentoire", "menutask" -> {
                commandReturn = new MenuTask(player).openMenu();
            }
            case "give", "taskbundle", "bundle" -> {
                commandReturn = new GiveTaskBundle(player).giveBundle();

            }
            default -> {
                sender.sendMessage("action non reconnue taper /task help pour de l'aide");
                commandReturn = true;
            }
        }


        return commandReturn;
    }
}
