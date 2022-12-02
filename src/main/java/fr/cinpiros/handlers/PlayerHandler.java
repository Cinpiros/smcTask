package fr.cinpiros.handlers;

import fr.cinpiros.commands.GiveTaskClasseur;
import fr.cinpiros.taskInventory.QuestInventoryMenu;
import fr.cinpiros.taskInventory.TaskInventoryMenu;
import fr.cinpiros.utils.DelayedTask;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import fr.cinpiros.SmcTask;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class PlayerHandler implements Listener {
    public PlayerHandler(SmcTask plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerUse(PlayerInteractEvent event){
        try {
            if (!event.getItem().displayName().equals(Component.text(GiveTaskClasseur.itemName))){
                return;
            }
        } catch (NullPointerException e) {
            return;
        }
        if (!event.getPlayer().hasPermission("smcTask.useclasseur")) {
            return;
        }

        Player player = event.getPlayer();
        if (event.getAction().isRightClick()) {
            new TaskInventoryMenu(player).openMenu();
        }
        if (event.getAction().isLeftClick()) {
            new QuestInventoryMenu(player).openMenu();
        }
    }
}
