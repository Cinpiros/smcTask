package fr.cinpiros;

import fr.cinpiros.handlers.InventoryHandler;
import fr.cinpiros.handlers.PlayerHandler;
import fr.cinpiros.task.TaskConfig;
import fr.cinpiros.utils.CommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public class SmcTask extends JavaPlugin {
    @Override
    public void onEnable() {

        getCommand("task").setExecutor(new CommandHandler());

        new PlayerHandler(this);
        new InventoryHandler(this);


        saveDefaultConfig();
        this.saveResource("condition/example.yml", false);
        this.saveResource("task/example.yml", false);


        new TaskConfig(this).loadConfig();

        Bukkit.getLogger().info("smcTask Enable");
    }
    @Override
    public void onDisable() {
        Bukkit.getLogger().info("smcTask Disable");
    }

}