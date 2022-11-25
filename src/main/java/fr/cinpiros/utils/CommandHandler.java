package fr.cinpiros.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

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
            sender.sendMessage("/task <action>");
            return true;
        }


        if (!isPlayer || Bukkit.getServer().getPlayer(args[1]) == null) {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage("/task "+args[0]+" <player>");
            } else {
                Bukkit.getLogger().info("Error <player is not online>² by non player entity or anorther plugin");
            }
            return true;
        }


        return true;
    }
}
