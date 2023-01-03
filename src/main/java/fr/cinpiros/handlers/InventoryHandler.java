package fr.cinpiros.handlers;

import fr.cinpiros.SmcTask;
import fr.cinpiros.inventory.TaskPanel;
import fr.cinpiros.inventory.TaskInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;


public class InventoryHandler implements Listener {

    public InventoryHandler(SmcTask plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onTaskInventoryClick(InventoryClickEvent event) {
        //Player player = (Player) event.getWhoClicked();

        if (!event.getView().title().equals(TaskInventory.invName)) {
            return;
        }
        Inventory inv  = event.getClickedInventory();

        if (inv == null) {
            return;
        }
        ItemStack item = inv.getItem(event.getSlot());
        Component sqlrequesttest = Component.text("Task").color(NamedTextColor.AQUA);

        if (item == null || Objects.equals(item.getItemMeta().displayName(), sqlrequesttest)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onTaskPanelClick(InventoryClickEvent event) {
        if (!event.getView().title().equals(TaskPanel.invName)) {
            return;
        }

        event.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCloseMenu(InventoryCloseEvent event) {
        //Player player = (Player) event.getWhoClicked();
        if (!event.getView().title().equals(TaskInventory.invName)) {
            return;
        }

        Inventory inv = event.getInventory();
        ItemStack[] invContent = inv.getContents();

    }

}
