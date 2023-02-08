package fr.cinpiros.handlers;

import fr.cinpiros.SmcTask;
import fr.cinpiros.commands.GiveTaskClasseur;
import fr.cinpiros.inventory.OpenInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class PlayerHandler implements Listener {
    public PlayerHandler(SmcTask plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerUse(PlayerInteractEvent event){
        try {
            if (!Objects.equals(Objects.requireNonNull(event.getItem()).getItemMeta().displayName(), GiveTaskClasseur.itemName)){
                return;
            }
        } catch (NullPointerException e) {
            return;
        }
        if (!event.getPlayer().hasPermission("smcTask.useclasseur")) {
            return;
        }
        event.setCancelled(true);
        Player player = event.getPlayer();
        if (event.getAction().isRightClick()) {
            new OpenInventory(player).taskMenu();
        }
        if (event.getAction().isLeftClick()) {
            new OpenInventory(player).questMenu();
        }
    }
}
