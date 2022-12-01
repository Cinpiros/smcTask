package fr.cinpiros.handlers;

import fr.cinpiros.commands.GiveTaskClasseur;
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

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("smcTask.playerjoin.info")) {
            return;
        }

        Player Player = event.getPlayer();

        Bukkit.getLogger().info(Player.getName()+" a rejoin le server");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player && event.getCause()== EntityDamageEvent.DamageCause.FALL)) {
            return;
        }

        Player player = (Player) event.getEntity();
        DelayedTask task = new DelayedTask(() -> {
            player.getInventory().addItem(new ItemStack(Material.FEATHER));
        }, 20 * 5);

        Bukkit.getScheduler().cancelTask(task.getId());
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerUse(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (event.getItem() == null) {
            player.sendMessage("pas d'item");
            return;
        }
        if (!event.getItem().displayName().equals(Component.text(GiveTaskClasseur.itemName))){
            player.sendMessage("pas un classeur");
            return;
        }

        if (event.getAction().isRightClick()) {
            new TaskInventoryMenu(player).openMenu();
        }
        if (event.getAction().isLeftClick()) {

        }
    }
}
