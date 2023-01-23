package fr.cinpiros.database;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ConfigSync {
    public void syncTask(Plugin plugin, UtilsDatabase database, ArrayList<ConfigurationSection> taskConfig) {
        Connection conn = database.getConnection(plugin);

        database.closeConnection(conn);
    }

    public void syncCondition(Plugin plugin, UtilsDatabase database, ArrayList<ConfigurationSection> conditionConfig) {
        Connection conn = database.getConnection(plugin);

        database.closeConnection(conn);
    }

    public void syncJobs(Plugin plugin, UtilsDatabase database, ArrayList<ConfigurationSection> jobsConfig) {
        Connection conn = database.getConnection(plugin);

        database.closeConnection(conn);
    }

    public void syncRarity(Plugin plugin, UtilsDatabase database, ArrayList<ConfigurationSection> rarityConfig) {
        Connection conn = database.getConnection(plugin);
        String prefix = database.getPrefix(plugin);
        try {
            PreparedStatement rarityInsert = conn.prepareStatement("INSERT INTO "+prefix+"rarity VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
            for (ConfigurationSection raritySection : rarityConfig) {
                String key = raritySection.getKeys(false).toString();
                PreparedStatement tryexist = conn.prepareStatement("SELECT id FROM "+prefix+"rarity WHERE id = '"+key+"';");
                ResultSet rsDoesKeyExist = tryexist.executeQuery();
                if (!rsDoesKeyExist.next()) {
                    rarityInsert.setString(1, "'"+key+"'");
                    rarityInsert.setString(2, "'"+raritySection.getString(key+".name")+"'");
                    rarityInsert.setString(3, "'"+raritySection.getString(key+".color")+"'");
                    rarityInsert.setInt(4, raritySection.getInt(key+".rarity"));
                    rarityInsert.setString(5, "'"+raritySection.getString(key+".complete_effect_color")+"'");
                    rarityInsert.setString(6, "'"+raritySection.getString(key+".complete_effect_sound")+"'");
                    rarityInsert.setString(7, "'"+raritySection.getString(key+".deposit_effect_color")+"'");
                    rarityInsert.setString(8, "'"+raritySection.getString(key+".deposit_effect_sound")+"'");
                    rarityInsert.addBatch();
                    rarityInsert.clearParameters();
                }
            }
            rarityInsert.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.closeConnection(conn);
        }
    }
}
