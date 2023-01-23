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
        String prefix = database.getPrefix(plugin);
        try {
            PreparedStatement conditionInsert = conn.prepareStatement("INSERT INTO "+prefix+"condition VALUES (?, ?, ?, ?, ?, ?, ?);");
            for (ConfigurationSection conditionSection : conditionConfig) {
                String id = conditionSection.getCurrentPath();
                PreparedStatement tryexist = conn.prepareStatement("SELECT condition_id FROM "+prefix+"condition WHERE condition_id = '"+id+"';");
                ResultSet rsDoesKeyExist = tryexist.executeQuery();
                if (!rsDoesKeyExist.next()) {
                    conditionInsert.setString(1, id);
                    conditionInsert.setString(2, conditionSection.getString("type"));
                    conditionInsert.setString(3, conditionSection.getString("description"));
                    conditionInsert.setString(4, conditionSection.getString("complete_description"));
                    if (conditionSection.contains("id")) {
                        conditionInsert.setString(5, conditionSection.getString("id"));
                    } else {
                        conditionInsert.setString(5, "null");
                    }
                    if (conditionSection.contains("level")) {
                        conditionInsert.setInt(6, conditionSection.getInt("level"));
                    } else {
                        conditionInsert.setInt(6, 0);
                    }
                    if (conditionSection.contains("quantity")) {
                        conditionInsert.setInt(7, conditionSection.getInt("quantity"));
                    } else {
                        conditionInsert.setInt(7, 0);
                    }
                    conditionInsert.addBatch();
                    conditionInsert.clearParameters();
                } /*else {
                    Bukkit.getLogger().info("rarity '"+id+"' already exist");
                }*/
            }
            conditionInsert.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.closeConnection(conn);
        }
    }

    public void syncJobs(Plugin plugin, UtilsDatabase database, ArrayList<ConfigurationSection> jobsConfig) {
        Connection conn = database.getConnection(plugin);
        String prefix = database.getPrefix(plugin);
        try {
            PreparedStatement jobsInsert = conn.prepareStatement("INSERT INTO "+prefix+"jobs VALUES (?, ?, ?, ?, ?);");
            for (ConfigurationSection jobsSection : jobsConfig) {
                String id = jobsSection.getCurrentPath();
                PreparedStatement tryexist = conn.prepareStatement("SELECT id FROM "+prefix+"jobs WHERE id = '"+id+"';");
                ResultSet rsDoesKeyExist = tryexist.executeQuery();
                if (!rsDoesKeyExist.next()) {
                    jobsInsert.setString(1, id);
                    jobsInsert.setString(2, jobsSection.getString("name"));
                    jobsInsert.setString(3, jobsSection.getString("color"));
                    jobsInsert.setInt(4, jobsSection.getInt("scale"));
                    jobsInsert.setInt(5, jobsSection.getInt("max_level"));
                    jobsInsert.addBatch();
                    jobsInsert.clearParameters();
                } /*else {
                    Bukkit.getLogger().info("jobs '"+id+"' already exist");
                }*/
            }
            jobsInsert.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.closeConnection(conn);
        }
    }

    public void syncRarity(Plugin plugin, UtilsDatabase database, ArrayList<ConfigurationSection> rarityConfig) {
        Connection conn = database.getConnection(plugin);
        String prefix = database.getPrefix(plugin);
        try {
            PreparedStatement rarityInsert = conn.prepareStatement("INSERT INTO "+prefix+"rarity VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
            for (ConfigurationSection raritySection : rarityConfig) {
                String id = raritySection.getCurrentPath();
                PreparedStatement tryexist = conn.prepareStatement("SELECT id FROM "+prefix+"rarity WHERE id = '"+id+"';");
                ResultSet rsDoesKeyExist = tryexist.executeQuery();
                if (!rsDoesKeyExist.next()) {
                    rarityInsert.setString(1, id);
                    rarityInsert.setString(2, raritySection.getString("name"));
                    rarityInsert.setString(3, raritySection.getString("color"));
                    rarityInsert.setInt(4, raritySection.getInt("rarity"));
                    rarityInsert.setString(5, raritySection.getString("complete_effect_color"));
                    rarityInsert.setString(6, raritySection.getString("complete_effect_sound"));
                    rarityInsert.setString(7, raritySection.getString("deposit_effect_color"));
                    rarityInsert.setString(8, raritySection.getString("deposit_effect_sound"));
                    rarityInsert.addBatch();
                    rarityInsert.clearParameters();
                } /*else {
                    Bukkit.getLogger().info("rarity '"+id+"' already exist");
                }*/
            }
            rarityInsert.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.closeConnection(conn);
        }
    }
}
