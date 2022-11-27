package fr.cinpiros.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServer;

public class CommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String [] args) {
        final boolean isPlayer;
        if (sender instanceof Player) {
            isPlayer = true;
        } else {
            isPlayer = false;
        }

        if (args.length == 0){
            sender.sendMessage("/"+command+" <action>");
            return true;
        }
        Player player = Bukkit.getPlayer(args[1]);

        if (!isPlayer && Bukkit.getServer().getPlayer(args[1]) == null) {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage("/"+command+" "+args[0]+" <player>");
            } else {
                Bukkit.getLogger().info("Error player not found by non player entity or a plugin");
            }
            return true;
        }
        switch (args[0]) {
            case "help", "aide" -> {
                break;
            }
            case "version" -> {

                break;
            }
            case "presentoire" -> {
                break;
            }
            default -> {
                sender.sendMessage("action non reconue taper /task help pour de l'aide");
            }
        }


        return true;
    }
}
