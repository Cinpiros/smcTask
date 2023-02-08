package fr.cinpiros.database;

import fr.cinpiros.SmcTask;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;

public class UtilsDatabase {
    public Connection getConnection() {
        Plugin plugin = SmcTask.getInstance();
        FileConfiguration conf = plugin.getConfig();
        String host = conf.getString("database.host");
        String user = conf.getString("database.user");
        String password = conf.getString("database.password");
        String database = conf.getString("database.database");

        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void closeConnection(Connection conn) {
        try {
            conn.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPrefix() {
        Plugin plugin = SmcTask.getInstance();
        return plugin.getConfig().getString("database.prefix");
    }

    public final String selectTask(final String prefix, final String task_id) {
        return "SELECT "+prefix+"task.name, "+prefix+"task.item, "+prefix+"task.color, "+prefix+"task.item_enchant_effect, " +
                prefix+"task.reward_money, "+prefix+"rarity.name, "+prefix+"rarity.color" +
                " FROM "+prefix+"task LEFT OUTER JOIN "+prefix+"rarity ON " +
                prefix+"task.FK_rarity_id = "+prefix+"rarity.id WHERE "+prefix+"task.id = '"+task_id+"' LIMIT 1;";
    }

    public final String insertTaskInstance(final String prefix, final String task_id) {
        return "INSERT INTO " + prefix + "task_instance (FK_task_id) VALUE ('" + task_id + "');";
    }

    public final String selectConditionDescription(final String prefix, final String task_id) {
        return "SELECT "+prefix + "condition.description, " + prefix + "task_condition.FK_condition_condition_id, " +
                prefix+"condition.complete_description FROM " +
                prefix + "task_condition INNER JOIN " + prefix + "condition ON " +
                prefix + "task_condition.FK_condition_condition_id = " + prefix + "condition.condition_id WHERE " +
                prefix + "task_condition.FK_task_id = '" + task_id + "';";
    }

    public final String insertConditionInstance(final String prefix) {
        return "INSERT INTO " + prefix + "condition_instance (FK_task_instance_id, FK_condition_condition_id, FK_task_id) VALUE (?, ?, ?);";
    }

    public final String selectTaskDescription(final String prefix, final String task_id) {
        return "SELECT lore FROM "+prefix+"task_description WHERE FK_task_id = '" + task_id + "' ORDER BY id;";
    }

    public final String selectTaskRewardItem(final String prefix, final String task_id) {
        return "SELECT item, quantity FROM " + prefix + "task_reward_item WHERE FK_task_id = '" + task_id + "' ORDER BY id;";
    }

    public final String selectTaskRewardCommandDescription(final String prefix, final String task_id) {
        return "SELECT description FROM " + prefix + "task_reward_command WHERE FK_task_id = '" + task_id + "' ORDER BY id;";
    }

    public final String selectTaskRewardJobsExp(final String prefix, final String task_id) {
        return "SELECT " + prefix + "jobs.name, " + prefix + "jobs.color, " + prefix + "task_reward_jobs_exp.exp FROM " +
                prefix + "task_reward_jobs_exp INNER JOIN " + prefix + "jobs ON " +
                prefix + "task_reward_jobs_exp.FK_jobs_id = " + prefix + "jobs.id WHERE " +
                prefix + "task_reward_jobs_exp.FK_task_id = '" + task_id + "';";
    }

    public final String selectTaskId(final String prefix, final Integer task_instance_id) {
        return "SELECT id from "+prefix+"task WHERE id = (SELECT FK_task_id FROM " +
                prefix+"task_instance WHERE id = "+task_instance_id+");";
    }

    public final String selectConditionInstance(final String prefix, final Integer task_instance_id){
        return "SELECT quantity, complete FROM "+prefix+"condition_instance WHERE FK_task_instance_id = " +
                task_instance_id+" AND FK_condition_condition_id = ?;";
    }

    public final String selectPlayerTaskInventory(final String prefix, final String uuid) {
        return "SELECT FK_task_instance_id, slot FROM "+prefix+"player_task_inventory WHERE uuid = '"+uuid+"' ORDER BY slot;";
    }


    public final String deletePlayerTaskInventoryTaskInstance(final String prefix, final String uuid, final Integer slot) {
        return "DELETE FROM "+prefix+"player_task_inventory WHERE uuid = '"+uuid+"' AND slot = "+slot+";";
    }

    public final String insertPlayerTaskInventoryTaskInstance(final String prefix, final Integer task_instance_id, final String uuid, final Integer slot) {
        return "INSERT INTO "+prefix+"player_task_inventory (FK_task_instance_id, uuid, slot)" +
                " VALUE ("+task_instance_id+", '"+uuid+"', "+slot+");";
    }

    public final String updatePlayerTaskInventoryTaskInstance(final String prefix, final Integer task_instance_id, final String uuid, final Integer slot) {
        return "UPDATE "+prefix+"player_task_inventory SET" +
                " FK_task_instance_id = "+task_instance_id+" WHERE uuid = '"+uuid+"' AND slot = "+slot+";";
    }

    public final String deletePlayerTaskInventory(final String prefix, final String uuid) {
        return "DELETE FROM "+prefix+"player_task_inventory WHERE uuid = '"+uuid+"';";
    }

    public final String insertPlayerTaskInventory(final String prefix) {
        return "INSERT INTO "+prefix+"player_task_inventory (FK_task_instance_id, uuid, slot) VALUE (?, ?, ?);";
    }
}
