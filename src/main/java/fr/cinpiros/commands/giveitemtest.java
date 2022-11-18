package fr.cinpiros.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class giveitemtest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String [] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("seulement un joueur peux utiliser cette commande");
            return true;
        }

        Player player = (Player) sender;

        ItemStack item = new ItemStack(Material.BOOK, 1);
        Inventory inv = player.getInventory();

        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Task"));
        item.setItemMeta(meta);

        inv.addItem(item);



        return true;
    }
}
