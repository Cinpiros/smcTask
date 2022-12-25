package fr.cinpiros.task;

import fr.cinpiros.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class TaskConfig {
    private final ArrayList<FileConfiguration> taskConfig;
    ArrayList<ConfigurationSection> taskList;

    public TaskConfig (Plugin plugin) {
        taskConfig = new ConfigUtil(plugin, "task").getConfig();
    }

    public void loadConfig(){
        for (FileConfiguration config : this.taskConfig) {
            for (String rootObject : config.getKeys(false)) {
                this.taskList.add(config.getConfigurationSection(rootObject));
                Bukkit.getLogger().info(rootObject);
            }
        }
    }
}










