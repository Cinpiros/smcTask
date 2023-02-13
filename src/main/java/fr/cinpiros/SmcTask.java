package fr.cinpiros;

import fr.cinpiros.database.ConfigDatabase;
import fr.cinpiros.database.ConfigSync;
import fr.cinpiros.handlers.InventoryHandler;
import fr.cinpiros.handlers.PlayerHandler;
import fr.cinpiros.utils.CommandHandler;
import fr.cinpiros.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public class SmcTask extends JavaPlugin {
    private static SmcTask instance = null;
    @Override
    public void onEnable() {


        instance = this;
        PluginCommand command = getCommand("task");
        if (command != null) {
            command.setExecutor(new CommandHandler());
        } else {
            Bukkit.getLogger().warning("[smcTask] can't get command");
            Bukkit.getPluginManager().disablePlugin(getSmcTaskInstance());
        }


        new PlayerHandler(this);
        new InventoryHandler(this);


        saveDefaultConfig();

        if (!new File(this.getDataFolder().getAbsolutePath()+"/condition/example.yml").exists()) {
            this.saveResource("condition/example.yml", false);
        }
        if (!new File(this.getDataFolder().getAbsolutePath()+"/task/example.yml").exists()) {
            this.saveResource("task/example.yml", false);
        }
        if (!new File(this.getDataFolder().getAbsolutePath()+"/jobs/example.yml").exists()) {
            this.saveResource("jobs/example.yml", false);
        }
        if (!new File(this.getDataFolder().getAbsolutePath()+"/rarity/example.yml").exists()) {
            this.saveResource("rarity/example.yml", false);
        }


        ArrayList<ConfigurationSection> taskConfig = new ConfigUtil(this, "task/").loadConfigSection();
        ArrayList<ConfigurationSection> conditionConfig = new ConfigUtil(this, "condition/").loadConfigSection();
        ArrayList<ConfigurationSection> jobsConfig = new ConfigUtil(this, "jobs/").loadConfigSection();
        ArrayList<ConfigurationSection> rarityConfig = new ConfigUtil(this, "rarity/").loadConfigSection();

        if (this.getConfig().getBoolean("database.enable")) {
            new ConfigDatabase().configDatabase();
            if (this.getConfig().getBoolean("database.main_config")) {
                ConfigSync sync = new ConfigSync();
                int sync_condition_num = sync.syncCondition(conditionConfig);
                int sync_jobs_num = sync.syncJobs(jobsConfig);
                int sync_rarity_num = sync.syncRarity(rarityConfig);
                int sync_task_num = sync.syncTask(taskConfig);
                Bukkit.getLogger().info("[SmcTask] "+sync_task_num+" Task synchronised");
                Bukkit.getLogger().info("[SmcTask] "+sync_condition_num+" Condition synchronised");
                Bukkit.getLogger().info("[SmcTask] "+sync_rarity_num+" Rarity synchronised");
                Bukkit.getLogger().info("[SmcTask] "+sync_jobs_num+" Jobs synchronised");
            }
        }

        Bukkit.getLogger().info("[SmcTask] Enable correctly");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[SmcTask] Disable correctly");
    }


    public static Plugin getSmcTaskInstance() {
        return instance;
    }
}