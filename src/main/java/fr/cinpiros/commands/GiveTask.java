package fr.cinpiros.commands;

import fr.cinpiros.database.UtilsDatabase;
import fr.cinpiros.exeption.TaskCreateException;
import fr.cinpiros.task.TaskCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class GiveTask extends UtilsDatabase {
    private final Player player;
    private final CommandSender sender;

    public GiveTask(Player player, CommandSender sender) {
        this.player = player;
        this.sender = sender;
    }

    public boolean giveTask(final String task_id) {
        Inventory inv = this.player.getInventory();
        ItemStack item = null;
        try {
            item = new TaskCreator().createTask(task_id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (TaskCreateException e) {
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cError: unable to give task with this id"));
            } else {
                Bukkit.getLogger().warning(e.getMessage());
            }
        }

        if (item != null) {
            inv.addItem(item);
        } else {
            return false;
        }
        return true;
    }
}
