package fr.cinpiros.handlers;

import fr.cinpiros.commands.GiveTaskClasseur;
import fr.cinpiros.inventory.QuestInventory;
import fr.cinpiros.inventory.TaskInventory;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import fr.cinpiros.SmcTask;

public class PlayerHandler implements Listener {
    public PlayerHandler(SmcTask plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerUse(PlayerInteractEvent event){
        try {
            if (!event.getItem().getItemMeta().displayName().equals(GiveTaskClasseur.itemName)){
                return;
            }
        } catch (NullPointerException e) {
            return;
        }
        //if (!event.getPlayer().hasPermission("smcTask.useclasseur")) {
        //    return;
        //}

        Player player = event.getPlayer();
        if (event.getAction().isRightClick()) {
            new TaskInventory(player).openMenu();
            return;
        }
        if (event.getAction().isLeftClick()) {
            new QuestInventory(player).openMenu();
        }
    }
}
