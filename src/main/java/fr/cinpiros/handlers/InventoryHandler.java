package fr.cinpiros.handlers;

import fr.cinpiros.SmcTask;
import fr.cinpiros.inventory.OpenInventory;
import fr.cinpiros.inventory.SaveInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;


public class InventoryHandler implements Listener {

    public InventoryHandler(SmcTask plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    /*@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onTaskInventoryClick(InventoryClickEvent event) {
        //Player player = (Player) event.getWhoClicked();

        if (!event.getView().title().equals(OpenInventory.invTaskName)) {
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
    }*/

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTaskPanelClick(InventoryClickEvent event) {
        if (!event.getView().title().equals(OpenInventory.invPanelName)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCloseTaskInventory(InventoryCloseEvent event) {
        if (!event.getView().title().equals(OpenInventory.invTaskName)) {
            return;
        }
        Player player = (Player) event.getPlayer();
        Inventory inv = event.getInventory();

        new SaveInventory().saveTaskInventory(inv, player);

        /*try {
            new SaveInventory().saveTaskInventory(inv, player);
        } catch (SaveInventoryException e){
            Bukkit.getLogger().warning(e.getMessage());
            player.sendMessage("Erreur lors de la syncronisation de l'inventaire de task merci de contacter un administrateur");
        }*/



    }

}
