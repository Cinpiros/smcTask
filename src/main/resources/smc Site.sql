CREATE TABLE `task` (
  `id` varchar(100) UNIQUE PRIMARY KEY NOT NULL,
  `name` varchar(255) NOT NULL,
  `item` varchar(100) NOT NULL,
  `color` varchar(7) NOT NULL,
  `on_panel` boolean NOT NULL,
  `complete_effect` boolean NOT NULL,
  `deposit_effect` boolean NOT NULL,
  `item_enchant_effect` boolean NOT NULL,
  `reward_on_complete` boolean NOT NULL,
  `reward_money` int NOT NULL,
  `FK_rarity_id` varchar(100) NOT NULL
);

CREATE TABLE `rarity` (
  `id` varchar(100) UNIQUE PRIMARY KEY NOT NULL,
  `name` varchar(255) NOT NULL,
  `color` varchar(7) NOT NULL,
  `rarity` int NOT NULL,
  `complete_effect_color` varchar(7) NOT NULL,
  `complete_effect_sound` varchar(100) NOT NULL,
  `deposit_effect_color` varchar(7) NOT NULL,
  `deposit_effect_sound` varchar(100) NOT NULL
);

CREATE TABLE `task_description` (
  `id` int UNIQUE PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `FK_task_id` varchar(100) NOT NULL,
  `lore` varchar(255) NOT NULL
);

CREATE TABLE `task_jobs_level` (
  `FK_task_id` varchar(100) NOT NULL,
  `FK_jobs_id` varchar(100) NOT NULL,
  `level` int NOT NULL,
  PRIMARY KEY (`FK_task_id`, `FK_jobs_id`)
);

CREATE TABLE `condition` (
  `condition_id` varchar(100) UNIQUE PRIMARY KEY NOT NULL,
  `type` varchar(30) NOT NULL,
  `description` varchar(250) NOT NULL,
  `complete_description` varchar(250) NOT NULL,
  `id` varchar(100),
  `level` tinyint,
  `quantity` int
);

CREATE TABLE `task_condition` (
  `FK_task_id` varchar(100) NOT NULL,
  `FK_condition_condition_id` varchar(100) NOT NULL,
  PRIMARY KEY (`FK_task_id`, `FK_condition_condition_id`)
);

CREATE TABLE `task_reward_jobs_exp` (
  `FK_task_id` varchar(100) NOT NULL,
  `FK_jobs_id` varchar(100) NOT NULL,
  `exp` int NOT NULL,
  PRIMARY KEY (`FK_task_id`, `FK_jobs_id`)
);

CREATE TABLE `task_reward_item` (
  `id` int UNIQUE PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `FK_task_id` varchar(100) NOT NULL,
  `item` varchar(100) NOT NULL,
  `quantity` int NOT NULL
);

CREATE TABLE `task_reward_command` (
  `id` int UNIQUE PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `FK_task_id` varchar(100) NOT NULL,
  `command` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL
);

CREATE TABLE `jobs` (
  `id` varchar(100) UNIQUE PRIMARY KEY NOT NULL,
  `name` varchar(100) NOT NULL,
  `color` varchar(7) NOT NULL,
  `scale` int NOT NULL,
  `max_level` int NOT NULL
);

CREATE TABLE `player_jobs_exp` (
  `uuid` varchar(36) NOT NULL,
  `FK_jobs_id` varchar(100) NOT NULL,
  `level` int NOT NULL DEFAULT 1,
  `exp` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`uuid`, `FK_jobs_id`)
);

CREATE TABLE `task_instance` (
  `id` int UNIQUE PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `FK_task_id` varchar(100) NOT NULL,
  `complete` boolean NOT NULL DEFAULT false
);

CREATE TABLE `condition_instance` (
  `FK_task_instance_id` int NOT NULL,
  `FK_condition_condition_id` varchar(100) NOT NULL,
  `FK_task_id` varchar(100) NOT NULL,
  `quantity` int NOT NULL DEFAULT 0,
  `complete` boolean NOT NULL DEFAULT false,
  PRIMARY KEY (`FK_task_instance_id`, `FK_condition_condition_id`)
);

CREATE TABLE `player_task_inventory` (
  `FK_task_instance_id` int UNIQUE PRIMARY KEY NOT NULL,
  `uuid` varchar(36) NOT NULL,
  `slot` tinyint NOT NULL
);

CREATE TABLE `player_quest_inventory` (
  `FK_task_instance_id` int UNIQUE PRIMARY KEY NOT NULL,
  `uuid` varchar(36) NOT NULL,
  `slot` tinyint NOT NULL
);

ALTER TABLE `task` ADD FOREIGN KEY (`FK_rarity_id`) REFERENCES `rarity` (`id`);

ALTER TABLE `task_description` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `task` (`id`);

ALTER TABLE `task_jobs_level` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `task` (`id`);

ALTER TABLE `task_jobs_level` ADD FOREIGN KEY (`FK_jobs_id`) REFERENCES `jobs` (`id`);

ALTER TABLE `task_condition` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `task` (`id`);

ALTER TABLE `task_condition` ADD FOREIGN KEY (`FK_condition_condition_id`) REFERENCES `condition` (`condition_id`);

ALTER TABLE `task_reward_jobs_exp` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `task` (`id`);

ALTER TABLE `task_reward_jobs_exp` ADD FOREIGN KEY (`FK_jobs_id`) REFERENCES `jobs` (`id`);

ALTER TABLE `task_reward_item` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `task` (`id`);

ALTER TABLE `task_reward_command` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `task` (`id`);

ALTER TABLE `player_jobs_exp` ADD FOREIGN KEY (`FK_jobs_id`) REFERENCES `jobs` (`id`);

ALTER TABLE `task_instance` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `task` (`id`);

ALTER TABLE `condition_instance` ADD FOREIGN KEY (`FK_task_instance_id`) REFERENCES `task_instance` (`id`);

ALTER TABLE `condition_instance` ADD FOREIGN KEY (`FK_condition_condition_id`) REFERENCES `condition` (`condition_id`);

ALTER TABLE `condition_instance` ADD FOREIGN KEY (`FK_task_id`) REFERENCES `task` (`id`);

ALTER TABLE `player_task_inventory` ADD FOREIGN KEY (`FK_task_instance_id`) REFERENCES `task_instance` (`id`);

ALTER TABLE `player_quest_inventory` ADD FOREIGN KEY (`FK_task_instance_id`) REFERENCES `task_instance` (`id`);
