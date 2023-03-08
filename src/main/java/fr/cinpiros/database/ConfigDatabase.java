package fr.cinpiros.database;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static fr.cinpiros.SmcTask.getSmcTaskInstance;

public class ConfigDatabase extends UtilsDatabase {
    public void configDatabase() {
        try (Connection conn = getConnection()){
            String prefix = super.prefix;
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
            }

            if (!listTables.contains(prefix+"task_jobs_level")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task_jobs_level` (" +
                        "  `FK_task_id` varchar(100) NOT NULL," +
                        "  `FK_jobs_id` varchar(100) NOT NULL," +
                        "  `level` int NOT NULL," +
                        "  PRIMARY KEY (`FK_task_id`, `FK_jobs_id`)" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_jobs_level` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `"+prefix+"task` (`id`);");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_jobs_level` ADD FOREIGN KEY (`FK_jobs_id`) REFERENCES `"+prefix+"jobs` (`id`);");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_jobs_level` ADD UNIQUE (`FK_task_id`, `FK_jobs_id`);");
                preRequest.execute();
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
            }

            if (!listTables.contains(prefix+"task_reward_jobs_exp")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task_reward_jobs_exp` (" +
                        "  `FK_task_id` varchar(100) NOT NULL," +
                        "  `FK_jobs_id` varchar(100) NOT NULL," +
                        "  `exp` int NOT NULL," +
                        "  PRIMARY KEY (`FK_task_id`, `FK_jobs_id`)" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_reward_jobs_exp` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `"+prefix+"task` (`id`);");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_reward_jobs_exp` ADD FOREIGN KEY (`FK_jobs_id`) REFERENCES `"+prefix+"jobs` (`id`);");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_reward_jobs_exp` ADD UNIQUE (`FK_task_id`, `FK_jobs_id`);");
                preRequest.execute();
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
            }

            if (!listTables.contains(prefix+"task_reward_command")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"task_reward_command` (" +
                        "  `id` int UNIQUE PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                        "  `FK_task_id` varchar(100) NOT NULL," +
                        "  `command` varchar(255) NOT NULL," +
                        "  `description` varchar(255) NOT NULL" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"task_reward_command` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `"+prefix+"task` (`id`);");
                preRequest.execute();
            }

            if (!listTables.contains(prefix+"player_jobs_exp")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"player_jobs_exp` (" +
                        "  `uuid` varchar(36) NOT NULL," +
                        "  `FK_jobs_id` varchar(100) NOT NULL," +
                        "  `level` int NOT NULL DEFAULT 1," +
                        "  `exp` int NOT NULL DEFAULT 0," +
                        "  PRIMARY KEY (`uuid`, `FK_jobs_id`)" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"player_jobs_exp` ADD FOREIGN KEY (`FK_jobs_id`) REFERENCES `"+prefix+"jobs` (`id`);");
                preRequest.execute();
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
            }

            if (!listTables.contains(prefix+"condition_instance")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"condition_instance` (" +
                        "  `FK_task_instance_id` int NOT NULL," +
                        "  `FK_condition_condition_id` varchar(100) NOT NULL," +
                        "  `FK_task_id` varchar(100) NOT NULL," +
                        "  `quantity` int NOT NULL DEFAULT 0," +
                        "  `complete` boolean NOT NULL DEFAULT false," +
                        "  PRIMARY KEY (`FK_task_instance_id`, `FK_condition_condition_id`)" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"condition_instance` ADD FOREIGN KEY (`FK_task_instance_id`) REFERENCES `"+prefix+"task_instance` (`id`);");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"condition_instance` ADD FOREIGN KEY (`FK_condition_condition_id`) REFERENCES `"+prefix+"condition` (`condition_id`);");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"condition_instance` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `"+prefix+"task` (`id`);");
                preRequest.execute();
            }


            if (!listTables.contains(prefix+"player_task_inventory")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"player_task_inventory` (" +
                        "  `FK_task_instance_id` int UNIQUE PRIMARY KEY NOT NULL," +
                        "  `uuid` varchar(36) NOT NULL," +
                        "  `slot` tinyint NOT NULL" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"player_task_inventory` ADD FOREIGN KEY (`FK_task_instance_id`) REFERENCES `"+prefix+"task_instance` (`id`);");
                preRequest.execute();
            }

            if (!listTables.contains(prefix+"player_quest_inventory")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"player_quest_inventory` (" +
                        "  `FK_task_instance_id` int UNIQUE PRIMARY KEY NOT NULL," +
                        "  `uuid` varchar(36) NOT NULL," +
                        "  `slot` tinyint NOT NULL" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"player_quest_inventory` ADD FOREIGN KEY (`FK_task_instance_id`) REFERENCES `"+prefix+"task_instance` (`id`);");
                preRequest.execute();
            }

            if (!listTables.contains(prefix+"player_daily_task")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"player_daily_task` (" +
                        "  `uuid` varchar(36) UNIQUE PRIMARY KEY NOT NULL," +
                        "  `daily_pick_up_task` int NOT NULL," +
                        "  `max_daily_pick_up_task` int NOT NULL," +
                        "  `today` date NOT NULL," +
                        "  `number_panel_task` tinyint NOT NULL" +
                        ");");
                preRequest.execute();
            }

            if (!listTables.contains(prefix+"player_daily_task_list")) {
                PreparedStatement preRequest = conn.prepareStatement("CREATE TABLE `"+prefix+"player_daily_task_list` (" +
                        "  `FK_uuid` varchar(36) NOT NULL," +
                        "  `FK_task_id` varchar(100) NOT NULL," +
                        "  `slot` tinyint NOT NULL," +
                        "  PRIMARY KEY (`FK_uuid`, `slot`)" +
                        ");");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"player_daily_task_list` ADD FOREIGN KEY (`FK_uuid`) REFERENCES `"+prefix+"player_daily_task` (`uuid`);");
                preRequest.execute();
                preRequest = conn.prepareStatement("ALTER TABLE `"+prefix+"player_daily_task_list` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `"+prefix+"task` (`id`);");
                preRequest.execute();
            }
            Bukkit.getLogger().info("[SmcTask] Database initialised with success");

        } catch (Exception e) {
            Bukkit.getLogger().warning("[SmcTask] Database initialisation error");
            Bukkit.getPluginManager().disablePlugin(getSmcTaskInstance());
            e.printStackTrace();
        }
    }
}
