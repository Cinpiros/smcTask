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
        //File file = new File(plugin.getDataFolder().getAbsolutePath()+"/task/example.yml");
        //YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
        //if (!conf.contains("tasks")) {
        //    Bukkit.getLogger().info("WTF");
        //}
        for (YamlConfiguration config : this.taskConfig) {
            //getDefaultSection()
            //for (String rootObject : config.getConfigurationSection("tasks").getKeys(false)) {
            for (String rootObject : config.getKeys(false)) {
                Bukkit.getLogger().info("yousk2"+rootObject);
                this.taskList.add(config.getConfigurationSection(rootObject));
            }
        }
    }
}










