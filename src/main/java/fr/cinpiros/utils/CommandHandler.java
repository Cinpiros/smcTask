package fr.cinpiros.utils;

import fr.cinpiros.commands.GiveTask;
import fr.cinpiros.commands.GiveTaskClasseur;
import fr.cinpiros.inventory.TaskPanel;
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
        if (args.length == 0){
            sender.sendMessage("/task <commande>");
            return true;
        }

        Player player = null;
        if (args.length >= 2) {
            player = Bukkit.getPlayer(args[1]);
        }

        if (player == null) {
            List<String> listCommand = new ArrayList<>();
            listCommand.add("config");
            if (!listCommand.contains(args[0])) {
                if (sender instanceof Player) {
                    sender.sendMessage("/task "+args[0]+" <player>");
                } else if (sender instanceof ConsoleCommandSender) {
                    Bukkit.getLogger().info("/task "+args[0]+" <player>");
                }
                return true;
            }
        }
        boolean commandReturn = true;
        switch (args[0]) {
            case "help", "aide" -> {
                player.sendMessage("help");
            }
            case "version" -> {
                player.sendMessage("verssion");
            }
            case "presentoire", "menutask" -> {
                commandReturn = new TaskPanel(player).openMenu();
            }
            case "give", "taskbundle", "bundle" -> {
                commandReturn = new GiveTaskClasseur(player).giveClasseur();

            }
            case "task" -> {
                commandReturn = new GiveTask(player).giveTask(args[2]);

            }
            default -> {
                Bukkit.getLogger().info("action non reconnue taper /task help pour de l'aide");
            }
        }


        return commandReturn;
    }
}
