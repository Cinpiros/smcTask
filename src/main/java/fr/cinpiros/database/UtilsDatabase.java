package fr.cinpiros.database;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UtilsDatabase {
    public void configDatabase(Plugin plugin) {
        Connection conn = getConnection(plugin);
        try {
            String prefix = getPrefix(plugin);
            PreparedStatement requestListTable = conn.prepareStatement("SHOW TABLES;");
            ResultSet rsListTables = requestListTable.executeQuery();

            ArrayList<String> listTables = new ArrayList<>();
            while (rsListTables.next()) {
                listTables.add(rsListTables.getString(1));
            }

            if (!listTables.contains(prefix+"rarity")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"rarity` (" +
                        "  `id` varchar(100) UNIQUE PRIMARY KEY NOT NULL," +
                        "  `name` varchar(255) NOT NULL," +
                        "  `color` varchar(7) NOT NULL," +
                        "  `rarity` int NOT NULL," +
                        "  `complete_effect_color` varchar(7) NOT NULL," +
                        "  `complete_effect_sound` varchar(100) NOT NULL," +
                        "  `deposit_effect_color` varchar(7) NOT NULL," +
                        "  `deposit_effect_sound` varchar(100) NOT NULL" +
                        ");");
                preRequest.execute();
                Bukkit.getLogger().info("Table rarity created");
            }

            if (!listTables.contains(prefix+"task")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task` (" +
                        "  `id` varchar(100) UNIQUE PRIMARY KEY NOT NULL," +
                        "  `name` varchar(255) NOT NULL," +
                        "  `item` varchar(100) NOT NULL," +
                        "  `color` varchar(7) NOT NULL," +
                        "  `on_panel` boolean NOT NULL," +
                        "  `complete_effect` boolean NOT NULL," +
                        "  `deposit_effect` boolean NOT NULL," +
                        "  `item_enchant_effect` boolean NOT NULL," +
                        "  `reward_on_complete` boolean NOT NULL," +
                        "  `reward_money` int NOT NULL," +
                        "  `FK_rarity_id` varchar(100) NOT NULL" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task` ADD FOREIGN KEY (`FK_rarity_id`) REFERENCES `"+prefix+"rarity` (`id`);");
                preRequest.execute();
                Bukkit.getLogger().info("Table task created");
            }

            if (!listTables.contains(prefix+"task_description")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task_description` (" +
                        "  `id` int UNIQUE PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                        "  `FK_task_id` varchar(100) NOT NULL," +
                        "  `lore` varchar(255) NOT NULL" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_description` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `"+prefix+"task` (`id`);");
                preRequest.execute();
                Bukkit.getLogger().info("Table task_description created");
            }

            if (!listTables.contains(prefix+"jobs")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"jobs` (" +
                        "  `id` varchar(100) UNIQUE PRIMARY KEY NOT NULL," +
                        "  `name` varchar(100) NOT NULL," +
                        "  `color` varchar(7) NOT NULL," +
                        "  `scale` int NOT NULL," +
                        "  `max_level` int NOT NULL" +
                        ");");
                preRequest.execute();
                Bukkit.getLogger().info("Table jobs created");
            }

            if (!listTables.contains(prefix+"task_jobs_level")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task_jobs_level` (" +
                        "  `id` int UNIQUE PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                        "  `FK_task_id` varchar(100) NOT NULL," +
                        "  `FK_jobs_id` varchar(100) NOT NULL," +
                        "  `level` int NOT NULL" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_jobs_level` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `"+prefix+"task` (`id`);");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_jobs_level` ADD FOREIGN KEY (`FK_jobs_id`) REFERENCES `"+prefix+"jobs` (`id`);");
                preRequest.execute();
                Bukkit.getLogger().info("Table task_jobs_level created");
            }

            if (!listTables.contains(prefix+"condition")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"condition` (" +
                        "  `condition_id` varchar(100) UNIQUE PRIMARY KEY NOT NULL," +
                        "  `type` varchar(30) NOT NULL," +
                        "  `description` varchar(250) NOT NULL," +
                        "  `complete_description` varchar(250) NOT NULL," +
                        "  `id` varchar(100)," +
                        "  `level` tinyint," +
                        "  `quantity` int" +
                        ");");
                preRequest.execute();
                Bukkit.getLogger().info("Table condition created");
            }

            if (!listTables.contains(prefix+"task_condition")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task_Condition` (" +
                        "  `FK_task_id` varchar(100) NOT NULL," +
                        "  `FK_condition_condition_id` varchar(100) NOT NULL," +
                        "  PRIMARY KEY (`FK_task_id`, `FK_condition_condition_id`)" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_condition` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `"+prefix+"task` (`id`);");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_condition` ADD FOREIGN KEY (`FK_condition_condition_id`) REFERENCES `"+prefix+"condition` (`condition_id`);");
                preRequest.execute();
                Bukkit.getLogger().info("Table task_condition created");
            }

            if (!listTables.contains(prefix+"task_reward_jobs_exp")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task_reward_jobs_exp` (" +
                        "  `id` int UNIQUE PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                        "  `FK_task_id` varchar(100) NOT NULL," +
                        "  `FK_jobs_id` varchar(100) NOT NULL," +
                        "  `exp` int NOT NULL" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_reward_jobs_exp` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `"+prefix+"task` (`id`);");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_reward_jobs_exp` ADD FOREIGN KEY (`FK_jobs_id`) REFERENCES `"+prefix+"jobs` (`id`);");
                preRequest.execute();
                Bukkit.getLogger().info("Table task_reward_jobs_exp created");
            }

            if (!listTables.contains(prefix+"task_reward_item")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task_reward_item` (" +
                        "  `id` int UNIQUE PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                        "  `FK_task_id` varchar(100) NOT NULL," +
                        "  `item` varchar(100) NOT NULL," +
                        "  `quantity` int NOT NULL" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_reward_item` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `"+prefix+"task` (`id`);");
                preRequest.execute();
                Bukkit.getLogger().info("Table task_reward_item created");
            }

            if (!listTables.contains(prefix+"task_reward_command")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task_reward_command` (" +
                        "  `id` int UNIQUE PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                        "  `FK_task_id` varchar(100) NOT NULL," +
                        "  `command` varchar(255) NOT NULL" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_reward_command` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `"+prefix+"task` (`id`);");
                preRequest.execute();
                Bukkit.getLogger().info("Table task_reward_command created");
            }

            if (!listTables.contains(prefix+"player_jobs_exp")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"player_jobs_exp` (" +
                        "  `uuid` varchar(36) UNIQUE PRIMARY KEY NOT NULL," +
                        "  `FK_jobs_id` varchar(100) NOT NULL," +
                        "  `level` int NOT NULL DEFAULT 1," +
                        "  `exp` int NOT NULL DEFAULT 0" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"player_jobs_exp` ADD FOREIGN KEY (`FK_jobs_id`) REFERENCES `"+prefix+"jobs` (`id`);");
                preRequest.execute();
                Bukkit.getLogger().info("Table player_jobs_exp created");
            }

            if (!listTables.contains(prefix+"task_instance")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task_instance` (" +
                        "  `id` int UNIQUE PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                        "  `FK_task_id` varchar(100) NOT NULL," +
                        "  `complete` boolean NOT NULL DEFAULT false" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_instance` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `"+prefix+"task` (`id`);");
                preRequest.execute();
                Bukkit.getLogger().info("Table task_instance created");
            }

            if (!listTables.contains(prefix+"condition_instance")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"condition_instance` (" +
                        "  `FK_task_instance_id` int NOT NULL," +
                        "  `FK_condition_condition_id` varchar(100) NOT NULL," +
                        "  `quantity` int NOT NULL DEFAULT 0," +
                        "  `complete` boolean NOT NULL DEFAULT false," +
                        "  PRIMARY KEY (`FK_task_instance_id`, `FK_condition_condition_id`)" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"condition_instance` ADD FOREIGN KEY (`FK_task_instance_id`) REFERENCES `"+prefix+"task_instance` (`id`);");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"condition_instance` ADD FOREIGN KEY (`FK_condition_condition_id`) REFERENCES `"+prefix+"condition` (`condition_id`);");
                preRequest.execute();
                Bukkit.getLogger().info("Table condition_instance created");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
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

    public void closeConnection(Connection conn) {
        try {
            conn.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPrefix(Plugin plugin) {
        String prefix = plugin.getConfig().getString("database.prefix");
        return prefix;
    }

}
