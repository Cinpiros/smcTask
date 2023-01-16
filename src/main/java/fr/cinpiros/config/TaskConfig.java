package fr.cinpiros.config;

import fr.cinpiros.utils.ConfigUtil;
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
                this.taskList.add(config.getConfigurationSection(rootObject));
            }
        }
        for (ConfigurationSection taskSection : this.taskList) {

        }
    }
}










