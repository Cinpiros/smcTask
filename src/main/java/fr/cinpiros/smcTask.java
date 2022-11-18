package fr.cinpiros;

import fr.cinpiros.handlers.playerJoinHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class smcTask extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getLogger().info("smcTask Enable");
        new playerJoinHandler(this);
    }
    @Override
    public void onDisable() {
        Bukkit.getLogger().info("smcTask Disable");
    }

}