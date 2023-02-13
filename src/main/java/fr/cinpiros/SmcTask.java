package fr.cinpiros;

import fr.cinpiros.database.ConfigDatabase;
import fr.cinpiros.database.ConfigSync;
import fr.cinpiros.handlers.InventoryHandler;
import fr.cinpiros.handlers.PlayerHandler;
import fr.cinpiros.utils.CommandHandler;
import fr.cinpiros.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Objects;

public class SmcTask extends JavaPlugin {
    private static SmcTask instance = null;
    @Override
    public void onEnable() {


        instance = this;
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
            new ConfigDatabase().configDatabase();
            if (this.getConfig().getBoolean("database.main_config")) {
                ConfigSync sync = new ConfigSync();
                sync.syncCondition(conditionConfig);
                sync.syncJobs(jobsConfig);
                sync.syncRarity(rarityConfig);
                sync.syncTask(taskConfig);
            }
        }



        Bukkit.getLogger().info("smcTask Enable");
    }
    @Override
    public void onDisable() {
        Bukkit.getLogger().info("smcTask Disable");
    }


    public static Plugin getInstance() {
        return instance;
    }
}