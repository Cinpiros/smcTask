package fr.cinpiros.database;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ConfigSync extends UtilsDatabase {
    public void syncTask(ArrayList<ConfigurationSection> taskConfig) {
        String prefix = super.prefix;
        try (
                Connection conn = getConnection()
                ){
            PreparedStatement taskInsert = conn.prepareStatement("INSERT INTO "+prefix+"task (id, name, item, color, on_panel, complete_effect, deposit_effect, item_enchant_effect, reward_on_complete, reward_money, FK_rarity_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            for (ConfigurationSection taskSection : taskConfig) {
                String id = taskSection.getCurrentPath();
                PreparedStatement tryexist = conn.prepareStatement("SELECT id FROM "+prefix+"task WHERE id = '"+id+"';");
                ResultSet rsDoesKeyExist = tryexist.executeQuery();
                if (!rsDoesKeyExist.next()) {
                    taskInsert.setString(1, id);
                    taskInsert.setString(2, taskSection.getString("name"));
                    taskInsert.setString(3, taskSection.getString("item"));
                    taskInsert.setString(4, taskSection.getString("color"));
                    taskInsert.setBoolean(5, taskSection.getBoolean("config.on_panel"));
                    taskInsert.setBoolean(6, taskSection.getBoolean("config.complete_effect"));
                    taskInsert.setBoolean(7, taskSection.getBoolean("config.deposit_effect"));
                    taskInsert.setBoolean(8, taskSection.getBoolean("config.item_echant_effect"));
                    taskInsert.setBoolean(9, taskSection.getBoolean("config.reward_on_complete"));
                    taskInsert.setInt(10, taskSection.getInt("reward.money"));
                    taskInsert.setString(11, taskSection.getString("rarity"));
                    taskInsert.executeUpdate();

                    PreparedStatement taskConditionInsert = conn.prepareStatement("INSERT INTO "+prefix+"task_condition (FK_task_id, FK_condition_condition_id) VALUES ('"+id+"', ?);");
                    for (String condition : taskSection.getStringList("condition")) {
                        PreparedStatement testCondition = conn.prepareStatement("SELECT condition_id FROM "+prefix+"condition WHERE condition_id = '"+condition+"';");
                        ResultSet rsDoesConditionExist = testCondition.executeQuery();
                        if (rsDoesConditionExist.next()) {
                            taskConditionInsert.setString(1, condition);
                            taskConditionInsert.addBatch();
                        } else {
                            Bukkit.getLogger().warning("Condition: "+condition+" does not exist in database condition not added");
                        }
                    }
                    taskConditionInsert.executeBatch();

                    if (taskSection.contains("description")) {
                        PreparedStatement taskDescriptionInsert = conn.prepareStatement("INSERT INTO "+prefix+"task_description (FK_task_id, lore) VALUES ('"+id+"', ?);");
                        for (String description : taskSection.getStringList("description")) {
                            taskDescriptionInsert.setString(1, description);
                            taskDescriptionInsert.addBatch();
                        }
                        taskDescriptionInsert.executeBatch();
                    }

                    if (taskSection.contains("jobs_level")) {
                        PreparedStatement taskJobsLevelInsert = conn.prepareStatement("INSERT INTO "+prefix+"task_jobs_level (FK_task_id, FK_jobs_id, level) VALUES ('"+id+"', ?, ?);");
                        ConfigurationSection section = taskSection.getConfigurationSection("jobs_level");
                        if (section != null) {
                            for (String jobs_id : section.getKeys(false)) {
                                PreparedStatement testJobs = conn.prepareStatement("SELECT id FROM "+prefix+"jobs WHERE id = '"+jobs_id+"';");
                                ResultSet rsDoesJobsExist = testJobs.executeQuery();
                                if (rsDoesJobsExist.next()) {
                                    taskJobsLevelInsert.setString(1, jobs_id);
                                    taskJobsLevelInsert.setInt(2, taskSection.getInt("jobs_level."+jobs_id));
                                    taskJobsLevelInsert.addBatch();
                                } else {
                                    Bukkit.getLogger().warning("Jobs: "+jobs_id+" does not exist in database task min jobs level not added");
                                }
                            }
                            taskJobsLevelInsert.executeBatch();
                        }

                    }

                    if (taskSection.contains("reward.jobs_exp")) {
                        PreparedStatement taskJobsLevelInsert = conn.prepareStatement("INSERT INTO "+prefix+"task_reward_jobs_exp (FK_task_id, FK_jobs_id, exp) VALUES ('"+id+"', ?, ?);");

                        ConfigurationSection section = taskSection.getConfigurationSection("reward.jobs_exp");
                        if (section != null) {
                            for (String jobs_id : section.getKeys(false)) {
                                PreparedStatement testJobs = conn.prepareStatement("SELECT id FROM "+prefix+"jobs WHERE id = '"+jobs_id+"';");
                                ResultSet rsDoesJobsExist = testJobs.executeQuery();
                                if (rsDoesJobsExist.next()) {
                                    taskJobsLevelInsert.setString(1, jobs_id);
                                    taskJobsLevelInsert.setInt(2, taskSection.getInt("reward.jobs_exp."+jobs_id));
                                    taskJobsLevelInsert.addBatch();
                                } else {
                                    Bukkit.getLogger().warning("Jobs: "+jobs_id+" does not exist in database task reward exp not added");
                                }
                            }
                            taskJobsLevelInsert.executeBatch();
                        }
                    }

                    if (taskSection.contains("reward.item")) {
                        PreparedStatement taskJobsLevelInsert = conn.prepareStatement("INSERT INTO "+prefix+"task_reward_item (FK_task_id, item, quantity) VALUES ('"+id+"', ?, ?);");
                        ConfigurationSection section = taskSection.getConfigurationSection("reward.item");
                        if (section != null) {
                            for (String item : section.getKeys(false)) {
                                taskJobsLevelInsert.setString(1, item);
                                taskJobsLevelInsert.setInt(2, taskSection.getInt("reward.item."+item));
                                taskJobsLevelInsert.addBatch();
                            }
                            taskJobsLevelInsert.executeBatch();
                        }
                    }

                    if (taskSection.contains("reward.command")) {
                        PreparedStatement taskRewardCommandInsert = conn.prepareStatement("INSERT INTO "+prefix+"task_reward_command (FK_task_id, command, description) VALUES ('"+id+"', ?, ?);");
                        ConfigurationSection section = taskSection.getConfigurationSection("reward.command");
                        if (section != null) {
                            for (String command : section.getKeys(false)) {
                                taskRewardCommandInsert.setString(1, command);
                                taskRewardCommandInsert.setString(2, taskSection.getString("reward.command."+command));
                                taskRewardCommandInsert.addBatch();
                            }
                            taskRewardCommandInsert.executeBatch();
                        }
                    }
                } /*else {
                    Bukkit.getLogger().info("rarity '"+id+"' already exist");
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void syncCondition(ArrayList<ConfigurationSection> conditionConfig) {
        String prefix = super.prefix;
        try (
                Connection conn = getConnection()
        ){
            PreparedStatement conditionInsert = conn.prepareStatement("INSERT INTO "+prefix+"condition (condition_id, type, description, complete_description, id, level, quantity) VALUES (?, ?, ?, ?, ?, ?, ?);");
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
                } /*else {
                    Bukkit.getLogger().info("rarity '"+id+"' already exist");
                }*/
            }
            conditionInsert.executeBatch();

        } //catch (NullPointerException ignored) {}
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void syncJobs(ArrayList<ConfigurationSection> jobsConfig) {
        String prefix = super.prefix;
        try (
                Connection conn = getConnection()
                ) {
            PreparedStatement jobsInsert = conn.prepareStatement("INSERT INTO "+prefix+"jobs (id, name, color, scale, max_level) VALUES (?, ?, ?, ?, ?);");
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
                } /*else {
                    Bukkit.getLogger().info("jobs '"+id+"' already exist");
                }*/
            }
            jobsInsert.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void syncRarity(ArrayList<ConfigurationSection> rarityConfig) {
        String prefix = super.prefix;
        try (
                Connection conn = getConnection()
                ){
            PreparedStatement rarityInsert = conn.prepareStatement("INSERT INTO "+prefix+"rarity (id, name, color, rarity, complete_effect_color, complete_effect_sound, deposit_effect_color, deposit_effect_sound) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
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
                } /*else {
                    Bukkit.getLogger().info("rarity '"+id+"' already exist");
                }*/
            }
            rarityInsert.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
