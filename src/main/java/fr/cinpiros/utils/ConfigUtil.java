package fr.cinpiros.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class ConfigUtil {
    private final ArrayList<File> files = new ArrayList<>();
    private final ArrayList<YamlConfiguration>  configs = new ArrayList<>();

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
            this.files.add(new File(paths+path));
        }

        for (File file : this.files) {
            this.configs.add(YamlConfiguration.loadConfiguration(file));
        }
    }
    public ArrayList<File> getFile() {
        return this.files;
    }

    public ArrayList<YamlConfiguration> getConfig() {
        return this.configs;
    }


    public boolean save() {
        try {
            int id = 0;
            for (YamlConfiguration config : this.configs){
                config.save(this.files.get(id));
                id++;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<ConfigurationSection> loadConfigSection(){
        ArrayList<ConfigurationSection> sectionList = new ArrayList<>();

        for (YamlConfiguration config : this.configs) {
            for (String rootObject : config.getKeys(false)) {
                sectionList.add(config.getConfigurationSection(rootObject));
            }
        }
        return sectionList;
    }
}
