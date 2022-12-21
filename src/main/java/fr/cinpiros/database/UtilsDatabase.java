package fr.cinpiros.database;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.sql.*;

public class UtilsDatabase {

    public boolean doDatabaseHaveTable(Plugin plugin) {
        Connection conn = getConnection(plugin);
        boolean haveTable = false;
        try {
            ResultSet numTable = conn.prepareStatement("SELECT count(*) AS TOTALNUMBEROFTABLES" +
                    " FROM INFORMATION_SCHEMA.TABLES" +
                    " WHERE TABLE_SCHEMA = '" + plugin.getConfig().getString("database.database") + "';").executeQuery();
            numTable.next();
            if (numTable.getInt(1) != 0) {
                haveTable =  true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        return haveTable;
    }

    public void configDatabase(Plugin plugin) {
        Connection conn = getConnection(plugin);
        try {
            String prefix = plugin.getConfig().getString("database.prefix");


            PreparedStatement requestCreateTable = conn.prepareStatement("CREATE TABLE "+prefix+"? (?);");

            requestCreateTable.setString(1, "task");
            requestCreateTable.setString(2, "id varchar(100) UNIQUE PRIMARY KEY NOT NULL," +
                    " name varchar(255) NOT NULL," +
                    " on_panel boolean NOT NULL," +
                    " complete_effect boolean NOT NULL," +
                    " deposit_effect boolean NOT NULL," +
                    " reward_money int," +
                    " rarity varchar(30) NOT NULL");
            requestCreateTable.addBatch();

            requestCreateTable.clearParameters();
            requestCreateTable.setString(1, "task_description");
            requestCreateTable.setString(2, "FK_id varchar(100) PRIMARY KEY NOT NULL," +
                    " lore varchar(255) NOT NULL");
            requestCreateTable.addBatch();

            requestCreateTable.clearParameters();
            requestCreateTable.setString(1, "task_jobs_level");
            requestCreateTable.setString(2, "FK_id varchar(100) NOT NULL," +
                    " FK_jobs_id varchar(100) NOT NULL," +
                    " level int NOT NULL," +
                    " PRIMARY KEY (FK_id, FK_jobs_id)");
            requestCreateTable.addBatch();

            requestCreateTable.clearParameters();
            requestCreateTable.setString(1, "condition");
            requestCreateTable.setString(2, "condition_id varchar(100) UNIQUE PRIMARY KEY NOT NULL," +
                    " type varchar(30) NOT NULL," +
                    " id varchar(100)," +
                    " time time," +
                    " level tinyint," +
                    " quantity int");
            requestCreateTable.addBatch();

            requestCreateTable.clearParameters();
            requestCreateTable.setString(1, "task_reward_jobs_exp");
            requestCreateTable.setString(2, "FK_task_id varchar(100) NOT NULL," +
                    " FK_jobs_id varchar(100) NOT NULL," +
                    " exp int NOT NULL," +
                    " PRIMARY KEY (FK_task_id, FK_jobs_id)");
            requestCreateTable.addBatch();

            requestCreateTable.clearParameters();
            requestCreateTable.setString(1, "task_reward_item");
            requestCreateTable.setString(2, "FK_task_id varchar(100) PRIMARY KEY NOT NULL," +
                    " item varchar(100) NOT NULL," +
                    " quantity int NOT NULL");
            requestCreateTable.addBatch();

            requestCreateTable.clearParameters();
            requestCreateTable.setString(1, "task_reward_command");
            requestCreateTable.setString(2, "FK_task_id varchar(100) PRIMARY KEY NOT NULL," +
                    " command varchar(255) NOT NULL");
            requestCreateTable.addBatch();

            requestCreateTable.clearParameters();
            requestCreateTable.setString(1, "jobs");
            requestCreateTable.setString(2, "id varchar(100) UNIQUE PRIMARY KEY NOT NULL," +
                    " name varchar(100) NOT NULL," +
                    " base_exp int NOT NULL");
            requestCreateTable.addBatch();

            requestCreateTable.clearParameters();
            requestCreateTable.setString(1, "player_jobs_exp");
            requestCreateTable.setString(2, "uuid varchar(36) UNIQUE NOT NULL," +
                    " FK_jobs_id varchar(100) NOT NULL," +
                    " level int NOT NULL DEFAULT 1," +
                    " exp int NOT NULL DEFAULT 0," +
                    " PRIMARY KEY (uuid, FK_jobs_id)");
            requestCreateTable.addBatch();

            requestCreateTable.clearParameters();
            requestCreateTable.setString(1, "task_instance");
            requestCreateTable.setString(2, "id int UNIQUE NOT NULL AUTO_INCREMENT," +
                    " uuid varchar(36) UNIQUE NOT NULL," +
                    " FK_task_id varchar(100) NOT NULL," +
                    " complete boolean DEFAULT false," +
                    " PRIMARY KEY (id, uuid)");
            requestCreateTable.addBatch();

            requestCreateTable.clearParameters();
            requestCreateTable.setString(1, "condition_instance");
            requestCreateTable.setString(2, "FK_task_instance_id int NOT NULL," +
                    " FK_task_condition_id varchar(100) NOT NULL," +
                    " quantity int DEFAULT 0," +
                    " time time DEFAULT 0," +
                    " complete boolean DEFAULT false," +
                    " PRIMARY KEY (FK_task_instance_id, FK_task_condition_id)");
            requestCreateTable.addBatch();

            requestCreateTable.executeBatch();

            PreparedStatement requestAlterTable = conn.prepareStatement("ALTER TABLE "+prefix+"? ADD FOREIGN KEY (?) REFERENCES "+prefix+"? (?);");
            requestAlterTable.setString(1, "task_description");
            requestAlterTable.setString(2, "FK_id");
            requestAlterTable.setString(3, "task");
            requestAlterTable.setString(4, "id");
            requestAlterTable.addBatch();

            requestAlterTable.clearParameters();
            requestAlterTable.setString(1, "task_jobs_level");
            requestAlterTable.setString(2, "FK_id");
            requestAlterTable.setString(3, "task");
            requestAlterTable.setString(4, "id");
            requestAlterTable.addBatch();

            requestAlterTable.clearParameters();
            requestAlterTable.setString(1, "task_jobs_level");
            requestAlterTable.setString(2, "FK_jobs_id");
            requestAlterTable.setString(3, "jobs");
            requestAlterTable.setString(4, "id");
            requestAlterTable.addBatch();

            requestAlterTable.clearParameters();
            requestAlterTable.setString(1, "task_reward_jobs_exp");
            requestAlterTable.setString(2, "FK_task_id");
            requestAlterTable.setString(3, "task");
            requestAlterTable.setString(4, "id");
            requestAlterTable.addBatch();

            requestAlterTable.clearParameters();
            requestAlterTable.setString(1, "task_reward_jobs_exp");
            requestAlterTable.setString(2, "FK_jobs_id");
            requestAlterTable.setString(3, "jobs");
            requestAlterTable.setString(4, "id");
            requestAlterTable.addBatch();

            requestAlterTable.clearParameters();
            requestAlterTable.setString(1, "task_reward_item");
            requestAlterTable.setString(2, "FK_task_id");
            requestAlterTable.setString(3, "task");
            requestAlterTable.setString(4, "id");
            requestAlterTable.addBatch();

            requestAlterTable.clearParameters();
            requestAlterTable.setString(1, "task_reward_command");
            requestAlterTable.setString(2, "FK_task_id");
            requestAlterTable.setString(3, "task");
            requestAlterTable.setString(4, "id");
            requestAlterTable.addBatch();

            requestAlterTable.clearParameters();
            requestAlterTable.setString(1, "player_jobs_exp");
            requestAlterTable.setString(2, "FK_jobs_id");
            requestAlterTable.setString(3, "jobs");
            requestAlterTable.setString(4, "id");
            requestAlterTable.addBatch();

            requestAlterTable.clearParameters();
            requestAlterTable.setString(1, "task_instance");
            requestAlterTable.setString(2, "FK_task_id");
            requestAlterTable.setString(3, "task");
            requestAlterTable.setString(4, "id");
            requestAlterTable.addBatch();

            requestAlterTable.clearParameters();
            requestAlterTable.setString(1, "condition_instance");
            requestAlterTable.setString(2, "FK_task_instance_id");
            requestAlterTable.setString(3, "task_instance");
            requestAlterTable.setString(4, "id");
            requestAlterTable.addBatch();

            requestAlterTable.clearParameters();
            requestAlterTable.setString(1, "condition_instance");
            requestAlterTable.setString(2, "FK_task_condition_id");
            requestAlterTable.setString(3, "condition");
            requestAlterTable.setString(4, "condition_id");
            requestAlterTable.addBatch();

            requestAlterTable.executeBatch();

            Bukkit.getLogger().info("database initialize successfully");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public Connection getConnection(Plugin plugin) {
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

}
