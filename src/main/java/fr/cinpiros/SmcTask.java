package fr.cinpiros;

import fr.cinpiros.database.ConfigSync;
import fr.cinpiros.database.UtilsDatabase;
import fr.cinpiros.handlers.InventoryHandler;
import fr.cinpiros.handlers.PlayerHandler;
import fr.cinpiros.utils.CommandHandler;
import fr.cinpiros.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Objects;

public class SmcTask extends JavaPlugin {
    @Override
    public void onEnable() {

        Objects.requireNonNull(getCommand("task")).setExecutor(new CommandHandler());

        new PlayerHandler(this);
        new InventoryHandler(this);


        saveDefaultConfig();
        this.saveResource("condition/example.yml", false);
        this.saveResource("task/example.yml", false);
        this.saveResource("jobs/example.yml", false);
        this.saveResource("rarity/example.yml", false);


        ArrayList<ConfigurationSection> taskConfig = new ConfigUtil(this, "task/").loadConfigSection();
        ArrayList<ConfigurationSection> conditionConfig = new ConfigUtil(this, "condition/").loadConfigSection();
        ArrayList<ConfigurationSection> jobsConfig = new ConfigUtil(this, "jobs/").loadConfigSection();
        ArrayList<ConfigurationSection> rarityConfig = new ConfigUtil(this, "rarity/").loadConfigSection();

        if (this.getConfig().getBoolean("database.enable")) {
            UtilsDatabase database = new UtilsDatabase();
            database.configDatabase(this);
            if (this.getConfig().getBoolean("database.main_config")) {
                ConfigSync sync = new ConfigSync();
                sync.syncCondition(this, database, conditionConfig);
                sync.syncJobs(this, database, jobsConfig);
                sync.syncRarity(this, database, rarityConfig);
                sync.syncTask(this, database, taskConfig);
            }
        }



        Bukkit.getLogger().info("smcTask Enable");
    }
    @Override
    public void onDisable() {
        Bukkit.getLogger().info("smcTask Disable");
    }
}