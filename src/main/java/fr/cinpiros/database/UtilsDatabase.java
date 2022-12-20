package fr.cinpiros.database;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.sql.*;

public class UtilsDatabase {

    public void configDatabase(Plugin plugin) throws SQLException {
        Connection conn = getConnection(plugin);
        Bukkit.getLogger().info("database connect successfully");
        try {
            ResultSet numTable = conn.prepareStatement("SELECT count(*) AS TOTALNUMBEROFTABLES" +
                    " FROM INFORMATION_SCHEMA.TABLES" +
                    " WHERE TABLE_SCHEMA = '"+plugin.getConfig().getString("database.database")+"';").executeQuery();
            numTable.next();
            if (numTable.getInt(1) != 0) {
                return;
            }
            Bukkit.getLogger().info("database not initialised");
            String prefix = plugin.getConfig().getString("database.prefix");
            PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task` (" +
                    "  `id` varchar(100) UNIQUE PRIMARY KEY NOT NULL," +
                    "  `name` varchar(255) NOT NULL," +
                    "  `on_panel` boolean NOT NULL," +
                    "  `complete_effect` boolean NOT NULL," +
                    "  `deposit_effect` boolean NOT NULL," +
                    "  `reward_money` int," +
                    "  `rarity` varchar(30) NOT NULL" +
                    ");");
            preRequest.execute();
            preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task_description` (" +
                    "  `FK_id` varchar(100) PRIMARY KEY NOT NULL," +
                    "  `lore` varchar(255) NOT NULL" +
                    ");");
            preRequest.execute();
            preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task_jobs_level` (" +
                    "  `FK_id` varchar(100) NOT NULL," +
                    "  `FK_jobs_id` varchar(100) NOT NULL," +
                    "  `level` int NOT NULL," +
                    "  PRIMARY KEY (`FK_id`, `FK_jobs_id`)" +
                    ");");
            preRequest.execute();
            preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"condition` (" +
                    "  `condition_id` varchar(100) UNIQUE PRIMARY KEY NOT NULL," +
                    "  `type` varchar(30) NOT NULL," +
                    "  `id` varchar(100)," +
                    "  `time` time," +
                    "  `level` tinyint," +
                    "  `quantity` int" +
                    ");");
            preRequest.execute();
            preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task_reward_jobs_exp` (" +
                    "  `FK_task_id` varchar(100) NOT NULL," +
                    "  `FK_jobs_id` varchar(100) NOT NULL," +
                    "  `exp` int NOT NULL," +
                    "  PRIMARY KEY (`FK_task_id`, `FK_jobs_id`)" +
                    ");");
            preRequest.execute();
            preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task_reward_item` (" +
                    "  `FK_task_id` varchar(100) PRIMARY KEY NOT NULL," +
                    "  `item` varchar(100) NOT NULL," +
                    "  `quantity` int NOT NULL" +
                    ");");
            preRequest.execute();
            preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task_reward_command` (" +
                    "  `FK_task_id` varchar(100) PRIMARY KEY NOT NULL," +
                    "  `command` varchar(255) NOT NULL" +
                    ");");
            preRequest.execute();
            preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"jobs` (" +
                    "  `id` varchar(100) UNIQUE PRIMARY KEY NOT NULL," +
                    "  `name` varchar(100) NOT NULL," +
                    "  `base_exp` int NOT NULL" +
                    ");");
            preRequest.execute();
            preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"player_jobs_exp` (" +
                    "  `uuid` varchar(36) UNIQUE NOT NULL," +
                    "  `FK_jobs_id` varchar(100) NOT NULL," +
                    "  `level` int NOT NULL DEFAULT 1," +
                    "  `exp` int NOT NULL DEFAULT 0," +
                    "  PRIMARY KEY (`uuid`, `FK_jobs_id`)" +
                    ");");
            preRequest.execute();
            preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task_instance` (" +
                    "  `id` int UNIQUE NOT NULL AUTO_INCREMENT," +
                    "  `uuid` varchar(36) UNIQUE NOT NULL," +
                    "  `FK_task_id` varchar(100) NOT NULL," +
                    "  `complete` boolean DEFAULT false," +
                    "  PRIMARY KEY (`id`, `uuid`)" +
                    ");");
            preRequest.execute();
            preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"condition_instance` (" +
                    "  `FK_task_instance_id` int NOT NULL," +
                    "  `FK_task_condition_id` varchar(100) NOT NULL," +
                    "  `quantity` int DEFAULT 0," +
                    "  `time` time DEFAULT 0," +
                    "  `complete` boolean DEFAULT false," +
                    "  PRIMARY KEY (`FK_task_instance_id`, `FK_task_condition_id`)" +
                    ");");
            preRequest.execute();
            preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_description` ADD FOREIGN KEY (`FK_id`) REFERENCES `"+prefix+"task` (`id`);");
            preRequest.execute();
            preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_jobs_level` ADD FOREIGN KEY (`FK_id`) REFERENCES `"+prefix+"task` (`id`);");
            preRequest.execute();
            preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_jobs_level` ADD FOREIGN KEY (`FK_jobs_id`) REFERENCES `"+prefix+"jobs` (`id`);");
            preRequest.execute();
            preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_reward_jobs_exp` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `"+prefix+"task` (`id`);");
            preRequest.execute();
            preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_reward_jobs_exp` ADD FOREIGN KEY (`FK_jobs_id`) REFERENCES `"+prefix+"jobs` (`id`);");
            preRequest.execute();
            preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_reward_item` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `"+prefix+"task` (`id`);");
            preRequest.execute();
            preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_reward_command` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `"+prefix+"task` (`id`);");
            preRequest.execute();
            preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"player_jobs_exp` ADD FOREIGN KEY (`FK_jobs_id`) REFERENCES `"+prefix+"jobs` (`id`);");
            preRequest.execute();
            preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_instance` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `"+prefix+"task` (`id`);");
            preRequest.execute();
            preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"condition_instance` ADD FOREIGN KEY (`FK_task_instance_id`) REFERENCES `"+prefix+"task_instance` (`id`);");
            preRequest.execute();
            preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"condition_instance` ADD FOREIGN KEY (`FK_task_condition_id`) REFERENCES `"+prefix+"condition` (`condition_id`);");
            preRequest.execute();
            Bukkit.getLogger().info("database initialize successfully");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
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
