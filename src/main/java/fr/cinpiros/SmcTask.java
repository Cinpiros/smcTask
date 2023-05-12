package fr.cinpiros;

import fr.cinpiros.database.ConfigDatabase;
import fr.cinpiros.database.ConfigSync;
import fr.cinpiros.database.ConnectionPoolMysql;
import fr.cinpiros.database.ConnectionPoolRedis;
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
import java.sql.SQLException;
import java.util.ArrayList;

public class SmcTask extends JavaPlugin {
    private static SmcTask instance;
    private static final ConnectionPoolMysql poolMysql = ConnectionPoolMysql.create();
    private static final ConnectionPoolRedis poolRedis = ConnectionPoolRedis.create();

    @Override
    public void onEnable() {
        instance = this;

        PluginCommand command = getCommand("task");
        if (command != null) {
            command.setExecutor(new CommandHandler());
        } else {
            disablePlugin("can't get task command");
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

        new ConfigDatabase().configDatabase();
        if (this.getConfig().getBoolean("database.main_config")) {
            ConfigSync sync = new ConfigSync();
            int sync_condition_num = sync.syncCondition(conditionConfig);
            int sync_jobs_num = sync.syncJobs(jobsConfig);
            int sync_rarity_num = sync.syncRarity(rarityConfig);
            int sync_task_num = sync.syncTask(taskConfig);
            logInfo(sync_task_num+" Task synchronised");
            logInfo(sync_condition_num+" Condition synchronised");
            logInfo(sync_rarity_num+" Rarity synchronised");
            logInfo(sync_jobs_num+" Jobs synchronised");
        }

        logInfo("Enable correctly");
    }

    @Override
    public void onDisable() {
        try {
            poolMysql.shutdown();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        poolRedis.shutdown();
        logInfo("Disable correctly");
    }

    public static void disablePlugin(String msg) {
        logWarn("Disable Cause: "+msg);
        Bukkit.getPluginManager().disablePlugin(instance);
    }
    public static void disablePlugin() {
        Bukkit.getPluginManager().disablePlugin(instance);
    }


    public static Plugin getSmcTaskInstance() {
        return instance;
    }

    public static ConnectionPoolMysql getConnectionPoolMysql() {return poolMysql;}
    public static ConnectionPoolRedis getConnectionPoolRedis() {return poolRedis;}

    public static void logInfo(String msg) {
        Bukkit.getLogger().info("[SmcTask] "+msg);
    }

    public static void logWarn(String msg) {
        Bukkit.getLogger().warning("[SmcTask] "+msg);
    }
}