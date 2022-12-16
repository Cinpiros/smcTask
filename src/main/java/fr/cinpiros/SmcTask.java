package fr.cinpiros;

import fr.cinpiros.handlers.InventoryHandler;
import fr.cinpiros.handlers.PlayerHandler;
import fr.cinpiros.task.TaskConfig;
import fr.cinpiros.utils.CommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SmcTask extends JavaPlugin {
    @Override
    public void onEnable() {

        getCommand("task").setExecutor(new CommandHandler());

        new PlayerHandler(this);
        new InventoryHandler(this);

        getConfig().options().copyDefaults();
        saveConfig();

        new TaskConfig(this).loadConfig();

        Bukkit.getLogger().info("smcTask Enable");
    }
    @Override
    public void onDisable() {
        Bukkit.getLogger().info("smcTask Disable");
    }

}