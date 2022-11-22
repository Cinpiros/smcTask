package fr.cinpiros.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import fr.cinpiros.SmcTask;

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
}
