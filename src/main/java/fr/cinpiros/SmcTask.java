package fr.cinpiros;

import fr.cinpiros.database.UtilsDatabase;
import fr.cinpiros.handlers.InventoryHandler;
import fr.cinpiros.handlers.PlayerHandler;
import fr.cinpiros.task.TaskConfig;
import fr.cinpiros.utils.CommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SmcTask extends JavaPlugin {
    @Override
    public void onEnable() {

        UtilsDatabase database = new UtilsDatabase();
        if (!database.doDatabaseHaveTable(this)){
            database.configDatabase(this);
        }

        getCommand("task").setExecutor(new CommandHandler());

        new PlayerHandler(this);
        new InventoryHandler(this);


        saveDefaultConfig();
        this.saveResource("condition/example.yml", false);
        this.saveResource("task/example.yml", false);


        new TaskConfig(this).loadConfig();

        Bukkit.getLogger().info("smcTask Enable zefsdfqsd");
    }
    @Override
    public void onDisable() {
        Bukkit.getLogger().info("smcTask Disable");
    }
}