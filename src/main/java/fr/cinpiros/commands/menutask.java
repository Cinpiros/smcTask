package fr.cinpiros.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class menutask implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String [] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("seulement un joueur peux utiliser cette commande");
            return true;
        }

        Player player = (Player) sender;

        return true;
    }
}
