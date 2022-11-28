package fr.cinpiros.handlers;

import fr.cinpiros.commands.MenuTask;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.awt.*;

public class menuInventoryHandler {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().title().equals(MenuTask.invName)) {
            return;
        }

        event.setCancelled(true);
    }
}
