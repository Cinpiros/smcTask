package fr.cinpiros.task;

import fr.cinpiros.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class TaskConfig {
    private final ArrayList<YamlConfiguration> taskConfig;
    private final ArrayList<ConfigurationSection> taskList = new ArrayList<>();

    public TaskConfig (Plugin plugin) {
        this.taskConfig = new ConfigUtil(plugin, "task/").getConfig();
    }

    public void loadTaskConfig(Plugin plugin){

        for (YamlConfiguration config : this.taskConfig) {
            for (String rootObject : config.getKeys(false)) {
                Bukkit.getLogger().info("yousk2"+rootObject);
                this.taskList.add(config.getConfigurationSection(rootObject));
            }
        }
        for (ConfigurationSection taskSection : this.taskList) {

        }
    }
}










