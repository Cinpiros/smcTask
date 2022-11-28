package fr.cinpiros.handlers;

import fr.cinpiros.SmcTask;
import fr.cinpiros.commands.MenuTask;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


public class MenuInventoryHandler implements Listener {

    public MenuInventoryHandler(SmcTask plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().title().equals(MenuTask.invName)) {
            return;
        }

        event.setCancelled(true);
    }
}
