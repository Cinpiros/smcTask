package fr.cinpiros.task;

import fr.cinpiros.utils.ConfigUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class TaskConfig {
    private ArrayList<FileConfiguration> taskConfig;

    public TaskConfig (Plugin plugin) {
        taskConfig = new ConfigUtil(plugin, "task/").getConfig();
    }
}
