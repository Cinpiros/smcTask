package fr.cinpiros.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class ConfigUtil {
    private ArrayList<File> files = new ArrayList<File>();
    private ArrayList<FileConfiguration>  configs = new ArrayList<FileConfiguration>();

    public ConfigUtil(Plugin plugin, String path) {
        this(plugin.getDataFolder().getAbsolutePath() + "/" + path);
    }
    public ConfigUtil(String paths) {
        FilenameFilter filter = (f, name) -> name.endsWith(".yml");
        String[] pathList = new File(paths).list(filter);

        if (pathList == null) {
            return;
        }

        for (String path : pathList) {
            this.files.add(new File(path));
        }

        for (File file : this.files) {
            this.configs.add(YamlConfiguration.loadConfiguration(file));
        }
    }
    public ArrayList<File> getFile() {
        return this.files;
    }

    public ArrayList<FileConfiguration> getConfig() {
        return this.configs;
    }


    public boolean save() {
        try {
            int id = 0;
            for (FileConfiguration config : this.configs){
                config.save(this.files.get(id));
                id++;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
