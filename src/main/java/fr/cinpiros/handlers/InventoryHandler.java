package fr.cinpiros.handlers;

import fr.cinpiros.SmcTask;
import fr.cinpiros.commands.MenuTask;
import fr.cinpiros.taskInventory.TaskInventoryMenu;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class InventoryHandler implements Listener {

    public InventoryHandler(SmcTask plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        //Player player = (Player) event.getWhoClicked();

        if (!event.getView().title().equals(MenuTask.invName)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCloseMenu(InventoryCloseEvent event) {
        //Player player = (Player) event.getWhoClicked();
        if (!event.getView().title().equals(TaskInventoryMenu.invName)) {
            return;
        }

        Inventory inv = event.getInventory();
        ItemStack[] invContent = inv.getContents();

    }

}
