CREATE DATABASE  IF NOT EXISTS `smctask` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `smctask`;
-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: smctask
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `smctask_condition`
--

DROP TABLE IF EXISTS `smctask_condition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smctask_condition` (
  `condition_id` varchar(100) NOT NULL,
  `type` varchar(30) NOT NULL,
  `description` varchar(250) NOT NULL,
  `complete_description` varchar(250) NOT NULL,
  `id` varchar(100) DEFAULT NULL,
  `level` tinyint DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`condition_id`),
  UNIQUE KEY `condition_id` (`condition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smctask_condition`
--

LOCK TABLES `smctask_condition` WRITE;
/*!40000 ALTER TABLE `smctask_condition` DISABLE KEYS */;
INSERT INTO `smctask_condition` VALUES ('beekeeping_64','BEEKEEPING','&7Hive harvest: &f%q% &7/ 64','&8Hive harvest: 64 / 64','null',0,64),('break_stone_64','BREAK','&7Break stone: &f%q% &7/ 64','&8Break stone: 64 / 64','STONE',0,64),('breed_pig_8','BREED','&7Breed pig: &f%q% &7/ 8','&8Breed pig: 8 / 8','PIG',0,8),('brew_netherwart_16','BREW','&7Brew nether wart: &f%q% &7/ 16','&8Brew nether wart: 16 / 16','NETHER_WART',0,16),('completetask_test_1','COMPLETE_TASK','&7Complete task harvest 12 potatoes: &f%q% &7/ 1','&8Complete task harvest 12 potatoes: 1 / 1','test',0,1),('craft_compass_32','CRAFT','&7Craft compass: &f%q% &7/ 32','&8Craft compass: 32 / 32','COMPASS',0,32),('eat_cookedporkchop_16','EAT','&7Eat cooked porkchop: &f%q% &7/ 16','&8Eat cooked porkchop: 16 / 16','COOKED_PORKCHOP',0,16),('echant_durability_0_1','ECHANT','&7Enchant durability enchantment: &f%q% &7/ 1','&8Enchant durability enchantment: 1 / 1','DURABILITY',0,1),('fish_tropicalfish_64','FISHING','&7Fishing tropical fish: &f%q% &7/ 64','&8Fishing tropical fish: 64 / 64','TROPICAL_FISH',0,64),('harvest_potatoes_512','HARVEST','&7Harverst potatoes: &f%q% &7/ %qm%','&8Harverst potatoes: 512 / 512','POTATOES',0,512),('harvest_wheat_512','HARVEST','&7Harverst wheat: &f%q% &7/ 512','&8Harverst wheat: 512 / 512','WHEAT',0,512),('harvest_wheat_64','HARVEST','&7Harverst wheat: &f%q% &7/ 64','&8Harverst wheat: 64 / 64','WHEAT',0,64),('kill_zombie_64','KILL','&7Kill zombie: &f%q% &7/ 64','&8Kill zombie: 64 / 64','ZOMBIE',0,64),('pickup_stone_64','PICKUP','&7Pickup stone: &f%q% &7/ 64','&8Pickup stone: 64 / 64','STONE',0,64),('shear_16','SHEAR','&7Shear mutton: &f%q% &7/ 16','&8Shear mutton: 16 / 16','null',0,16),('smelt_ironingot_64','SMELT','&7Smelt iron ingot: &f%q% &7/ 64','&8Smelt iron ingot: 64 / 64','IRON_INGOT',0,64),('smithing_netheriteaxe_1','SMITHING','&7Smith netherite axe: &f%q% &7/ 1','&8Smith netherite axe: 1 / 1','NETHERITE_AXE',0,1),('tame_wolf_8','TAME','&7Tame wolf: &f%q% &7/ 64','&8Tame wolf: 64 / 64','WOLF',0,8),('time_7j','TIME','&7Play time: &f%t% &7/ 7J 00:00','&8Play time: 7J 00:00 / 7J 00:00','null',0,10080),('tomilk_1','TOMILK','&7Milk cow: &f%q% &7/ 8','&8Milk cow: 8 / 8','null',0,8),('travel_feet_1km','TRAVEL','&7Travel by feet: &f%q% &7/ 1000 blocks','&8Travel by feet: 1000 / 1000 blocks','FEET',0,1000),('visit_spawn','VISIT','&7Visit spawn','&8Visit spawn','spawn',0,0);
/*!40000 ALTER TABLE `smctask_condition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smctask_condition_instance`
--

DROP TABLE IF EXISTS `smctask_condition_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smctask_condition_instance` (
  `FK_task_instance_id` int NOT NULL,
  `FK_condition_condition_id` varchar(100) NOT NULL,
  `FK_task_id` varchar(100) NOT NULL,
  `quantity` int NOT NULL DEFAULT '0',
  `complete` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`FK_task_instance_id`,`FK_condition_condition_id`),
  KEY `FK_condition_condition_id` (`FK_condition_condition_id`),
  KEY `FK_task_id` (`FK_task_id`),
  CONSTRAINT `smctask_condition_instance_ibfk_1` FOREIGN KEY (`FK_task_instance_id`) REFERENCES `smctask_task_instance` (`id`),
  CONSTRAINT `smctask_condition_instance_ibfk_2` FOREIGN KEY (`FK_condition_condition_id`) REFERENCES `smctask_condition` (`condition_id`),
  CONSTRAINT `smctask_condition_instance_ibfk_3` FOREIGN KEY (`FK_task_id`) REFERENCES `smctask_task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smctask_condition_instance`
--

LOCK TABLES `smctask_condition_instance` WRITE;
/*!40000 ALTER TABLE `smctask_condition_instance` DISABLE KEYS */;
INSERT INTO `smctask_condition_instance` VALUES (1,'beekeeping_64','test',0,0),(1,'break_stone_64','test',0,0),(1,'breed_pig_8','test',0,0),(1,'brew_netherwart_16','test',0,0),(1,'completetask_test_1','test',0,0),(1,'craft_compass_32','test',0,0),(1,'eat_cookedporkchop_16','test',0,0),(1,'echant_durability_0_1','test',0,0),(1,'fish_tropicalfish_64','test',0,0),(1,'harvest_wheat_64','test',0,0),(1,'kill_zombie_64','test',0,0),(1,'pickup_stone_64','test',0,0),(1,'shear_16','test',0,0),(1,'smelt_ironingot_64','test',0,0),(1,'smithing_netheriteaxe_1','test',0,0),(1,'tame_wolf_8','test',0,0),(1,'time_7j','test',0,0),(1,'tomilk_1','test',0,0),(1,'travel_feet_1km','test',0,0),(1,'visit_spawn','test',0,0);
/*!40000 ALTER TABLE `smctask_condition_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smctask_jobs`
--

DROP TABLE IF EXISTS `smctask_jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smctask_jobs` (
  `id` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `color` varchar(7) NOT NULL,
  `scale` int NOT NULL,
  `max_level` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smctask_jobs`
--

LOCK TABLES `smctask_jobs` WRITE;
/*!40000 ALTER TABLE `smctask_jobs` DISABLE KEYS */;
INSERT INTO `smctask_jobs` VALUES ('alier_des_nain','Alier des nain','#37ff30',10,100),('ami_des_elfe','Ami des elfe','#37ff30',10,100),('test_jobs','test jobs','#37ff30',10,100);
/*!40000 ALTER TABLE `smctask_jobs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smctask_player_daily_task`
--

DROP TABLE IF EXISTS `smctask_player_daily_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smctask_player_daily_task` (
  `uuid` varchar(36) NOT NULL,
  `daily_pick_up_task` int NOT NULL,
  `max_daily_pick_up_task` int NOT NULL,
  `today` date NOT NULL,
  `number_panel_task` tinyint NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uuid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smctask_player_daily_task`
--

LOCK TABLES `smctask_player_daily_task` WRITE;
/*!40000 ALTER TABLE `smctask_player_daily_task` DISABLE KEYS */;
INSERT INTO `smctask_player_daily_task` VALUES ('db6ac855-8c51-48e9-8f16-6a53623045c9',5,5,'2023-02-15',21);
/*!40000 ALTER TABLE `smctask_player_daily_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smctask_player_daily_task_list`
--

DROP TABLE IF EXISTS `smctask_player_daily_task_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smctask_player_daily_task_list` (
  `id` int NOT NULL AUTO_INCREMENT,
  `FK_uuid` varchar(36) NOT NULL,
  `FK_task_id` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `FK_uuid` (`FK_uuid`),
  KEY `FK_task_id` (`FK_task_id`),
  CONSTRAINT `smctask_player_daily_task_list_ibfk_1` FOREIGN KEY (`FK_uuid`) REFERENCES `smctask_player_daily_task` (`uuid`),
  CONSTRAINT `smctask_player_daily_task_list_ibfk_2` FOREIGN KEY (`FK_task_id`) REFERENCES `smctask_task` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smctask_player_daily_task_list`
--

LOCK TABLES `smctask_player_daily_task_list` WRITE;
/*!40000 ALTER TABLE `smctask_player_daily_task_list` DISABLE KEYS */;
INSERT INTO `smctask_player_daily_task_list` VALUES (22,'db6ac855-8c51-48e9-8f16-6a53623045c9','test1'),(23,'db6ac855-8c51-48e9-8f16-6a53623045c9','test1'),(24,'db6ac855-8c51-48e9-8f16-6a53623045c9','test'),(25,'db6ac855-8c51-48e9-8f16-6a53623045c9','test'),(26,'db6ac855-8c51-48e9-8f16-6a53623045c9','test1'),(27,'db6ac855-8c51-48e9-8f16-6a53623045c9','test'),(28,'db6ac855-8c51-48e9-8f16-6a53623045c9','test1'),(29,'db6ac855-8c51-48e9-8f16-6a53623045c9','test1'),(30,'db6ac855-8c51-48e9-8f16-6a53623045c9','test'),(31,'db6ac855-8c51-48e9-8f16-6a53623045c9','test1'),(32,'db6ac855-8c51-48e9-8f16-6a53623045c9','recolte_wheat'),(33,'db6ac855-8c51-48e9-8f16-6a53623045c9','test1'),(34,'db6ac855-8c51-48e9-8f16-6a53623045c9','test'),(35,'db6ac855-8c51-48e9-8f16-6a53623045c9','test'),(36,'db6ac855-8c51-48e9-8f16-6a53623045c9','test1'),(37,'db6ac855-8c51-48e9-8f16-6a53623045c9','test1'),(38,'db6ac855-8c51-48e9-8f16-6a53623045c9','test1'),(39,'db6ac855-8c51-48e9-8f16-6a53623045c9','test'),(40,'db6ac855-8c51-48e9-8f16-6a53623045c9','test1'),(41,'db6ac855-8c51-48e9-8f16-6a53623045c9','recolte_wheat'),(42,'db6ac855-8c51-48e9-8f16-6a53623045c9','test');
/*!40000 ALTER TABLE `smctask_player_daily_task_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smctask_player_jobs_exp`
--

DROP TABLE IF EXISTS `smctask_player_jobs_exp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smctask_player_jobs_exp` (
  `uuid` varchar(36) NOT NULL,
  `FK_jobs_id` varchar(100) NOT NULL,
  `level` int NOT NULL DEFAULT '1',
  `exp` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`uuid`,`FK_jobs_id`),
  KEY `FK_jobs_id` (`FK_jobs_id`),
  CONSTRAINT `smctask_player_jobs_exp_ibfk_1` FOREIGN KEY (`FK_jobs_id`) REFERENCES `smctask_jobs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smctask_player_jobs_exp`
--

LOCK TABLES `smctask_player_jobs_exp` WRITE;
/*!40000 ALTER TABLE `smctask_player_jobs_exp` DISABLE KEYS */;
INSERT INTO `smctask_player_jobs_exp` VALUES ('db6ac855-8c51-48e9-8f16-6a53623045c9','alier_des_nain',50,0),('db6ac855-8c51-48e9-8f16-6a53623045c9','ami_des_elfe',60,0),('db6ac855-8c51-48e9-8f16-6a53623045c9','test_jobs',1,0);
/*!40000 ALTER TABLE `smctask_player_jobs_exp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smctask_player_quest_inventory`
--

DROP TABLE IF EXISTS `smctask_player_quest_inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smctask_player_quest_inventory` (
  `FK_task_instance_id` int NOT NULL,
  `uuid` varchar(36) NOT NULL,
  `slot` tinyint NOT NULL,
  PRIMARY KEY (`FK_task_instance_id`),
  UNIQUE KEY `FK_task_instance_id` (`FK_task_instance_id`),
  CONSTRAINT `smctask_player_quest_inventory_ibfk_1` FOREIGN KEY (`FK_task_instance_id`) REFERENCES `smctask_task_instance` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smctask_player_quest_inventory`
--

LOCK TABLES `smctask_player_quest_inventory` WRITE;
/*!40000 ALTER TABLE `smctask_player_quest_inventory` DISABLE KEYS */;
/*!40000 ALTER TABLE `smctask_player_quest_inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smctask_player_task_inventory`
--

DROP TABLE IF EXISTS `smctask_player_task_inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smctask_player_task_inventory` (
  `FK_task_instance_id` int NOT NULL,
  `uuid` varchar(36) NOT NULL,
  `slot` tinyint NOT NULL,
  PRIMARY KEY (`FK_task_instance_id`),
  UNIQUE KEY `FK_task_instance_id` (`FK_task_instance_id`),
  CONSTRAINT `smctask_player_task_inventory_ibfk_1` FOREIGN KEY (`FK_task_instance_id`) REFERENCES `smctask_task_instance` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smctask_player_task_inventory`
--

LOCK TABLES `smctask_player_task_inventory` WRITE;
/*!40000 ALTER TABLE `smctask_player_task_inventory` DISABLE KEYS */;
INSERT INTO `smctask_player_task_inventory` VALUES (1,'db6ac855-8c51-48e9-8f16-6a53623045c9',21);
/*!40000 ALTER TABLE `smctask_player_task_inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smctask_rarity`
--

DROP TABLE IF EXISTS `smctask_rarity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smctask_rarity` (
  `id` varchar(100) NOT NULL,
  `name` varchar(255) NOT NULL,
  `color` varchar(7) NOT NULL,
  `rarity` int NOT NULL,
  `complete_effect_color` varchar(7) NOT NULL,
  `complete_effect_sound` varchar(100) NOT NULL,
  `deposit_effect_color` varchar(7) NOT NULL,
  `deposit_effect_sound` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smctask_rarity`
--

LOCK TABLES `smctask_rarity` WRITE;
/*!40000 ALTER TABLE `smctask_rarity` DISABLE KEYS */;
INSERT INTO `smctask_rarity` VALUES ('commun','COMMUN','#7A8085',1000,'#7A8085','ENTITY_EXPERIENCE_ORB_PICKUP','#7A8085','ENTITY_FIREWORK_ROCKET_LAUNCH'),('divin','DIVIN','#FED060',0,'#FED060','ENTITY_EXPERIENCE_ORB_PICKUP','#FED060','ENTITY_FIREWORK_ROCKET_LAUNCH'),('epique','EPIQUE','#9641C1',20,'#9641C1','ENTITY_EXPERIENCE_ORB_PICKUP','#9641C1','ENTITY_FIREWORK_ROCKET_LAUNCH'),('legendaire','LEGENDAIRE','#DA7A39',5,'#DA7A39','ENTITY_EXPERIENCE_ORB_PICKUP','#DA7A39','ENTITY_FIREWORK_ROCKET_LAUNCH'),('rare','RARE','#1591B5',100,'#1591B5','ENTITY_EXPERIENCE_ORB_PICKUP','#1591B5','ENTITY_FIREWORK_ROCKET_LAUNCH');
/*!40000 ALTER TABLE `smctask_rarity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smctask_task`
--

DROP TABLE IF EXISTS `smctask_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smctask_task` (
  `id` varchar(100) NOT NULL,
  `name` varchar(255) NOT NULL,
  `item` varchar(100) NOT NULL,
  `color` varchar(7) NOT NULL,
  `on_panel` tinyint(1) NOT NULL,
  `complete_effect` tinyint(1) NOT NULL,
  `deposit_effect` tinyint(1) NOT NULL,
  `item_enchant_effect` tinyint(1) NOT NULL,
  `reward_on_complete` tinyint(1) NOT NULL,
  `reward_money` int NOT NULL,
  `FK_rarity_id` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `FK_rarity_id` (`FK_rarity_id`),
  CONSTRAINT `smctask_task_ibfk_1` FOREIGN KEY (`FK_rarity_id`) REFERENCES `smctask_rarity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smctask_task`
--

LOCK TABLES `smctask_task` WRITE;
/*!40000 ALTER TABLE `smctask_task` DISABLE KEYS */;
INSERT INTO `smctask_task` VALUES ('recolte_wheat','recolte ble simple','PAPER','#A316DC',1,1,1,1,0,10,'rare'),('recolte_wheat_potatoes','recolte','PAPER','#A316DC',1,1,1,1,0,150,'rare'),('test','test','PAPER','#A316DC',1,1,1,1,0,100,'commun'),('test1','a name','BOOK','#F79600',0,0,0,0,0,0,'commun'),('test2','test2','PAPER','#A316DC',1,1,1,1,0,0,'epique'),('test3','test2','PAPER','#A316DC',1,1,1,1,0,0,'epique');
/*!40000 ALTER TABLE `smctask_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smctask_task_condition`
--

DROP TABLE IF EXISTS `smctask_task_condition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smctask_task_condition` (
  `FK_task_id` varchar(100) NOT NULL,
  `FK_condition_condition_id` varchar(100) NOT NULL,
  PRIMARY KEY (`FK_task_id`,`FK_condition_condition_id`),
  KEY `FK_condition_condition_id` (`FK_condition_condition_id`),
  CONSTRAINT `smctask_task_condition_ibfk_1` FOREIGN KEY (`FK_task_id`) REFERENCES `smctask_task` (`id`),
  CONSTRAINT `smctask_task_condition_ibfk_2` FOREIGN KEY (`FK_condition_condition_id`) REFERENCES `smctask_condition` (`condition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smctask_task_condition`
--

LOCK TABLES `smctask_task_condition` WRITE;
/*!40000 ALTER TABLE `smctask_task_condition` DISABLE KEYS */;
INSERT INTO `smctask_task_condition` VALUES ('test','beekeeping_64'),('test','break_stone_64'),('test','breed_pig_8'),('test','brew_netherwart_16'),('test','completetask_test_1'),('test','craft_compass_32'),('test','eat_cookedporkchop_16'),('test','echant_durability_0_1'),('test','fish_tropicalfish_64'),('recolte_wheat_potatoes','harvest_potatoes_512'),('recolte_wheat','harvest_wheat_512'),('recolte_wheat_potatoes','harvest_wheat_512'),('test','harvest_wheat_64'),('test2','harvest_wheat_64'),('test3','harvest_wheat_64'),('test','kill_zombie_64'),('test','pickup_stone_64'),('test1','pickup_stone_64'),('test','shear_16'),('test','smelt_ironingot_64'),('test','smithing_netheriteaxe_1'),('test','tame_wolf_8'),('test','time_7j'),('test','tomilk_1'),('test','travel_feet_1km'),('test','visit_spawn');
/*!40000 ALTER TABLE `smctask_task_condition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smctask_task_description`
--

DROP TABLE IF EXISTS `smctask_task_description`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smctask_task_description` (
  `id` int NOT NULL AUTO_INCREMENT,
  `FK_task_id` varchar(100) NOT NULL,
  `lore` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `FK_task_id` (`FK_task_id`),
  CONSTRAINT `smctask_task_description_ibfk_1` FOREIGN KEY (`FK_task_id`) REFERENCES `smctask_task` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smctask_task_description`
--

LOCK TABLES `smctask_task_description` WRITE;
/*!40000 ALTER TABLE `smctask_task_description` DISABLE KEYS */;
INSERT INTO `smctask_task_description` VALUES (1,'test','lore line 1'),(2,'test','lore line 2'),(3,'test','ect ...'),(4,'test1','&9&lyes description'),(5,'test1','&canother line'),(6,'test2','lore line 1'),(7,'test3','lore line 1'),(8,'recolte_wheat_potatoes','lore line 123'),(9,'recolte_wheat_potatoes','lore line 2'),(10,'recolte_wheat_potatoes','&9sdfqsdfq qsdqsd 2'),(11,'recolte_wheat_potatoes','&cue qsdqsd &f2');
/*!40000 ALTER TABLE `smctask_task_description` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smctask_task_instance`
--

DROP TABLE IF EXISTS `smctask_task_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smctask_task_instance` (
  `id` int NOT NULL AUTO_INCREMENT,
  `FK_task_id` varchar(100) NOT NULL,
  `complete` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `FK_task_id` (`FK_task_id`),
  CONSTRAINT `smctask_task_instance_ibfk_1` FOREIGN KEY (`FK_task_id`) REFERENCES `smctask_task` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smctask_task_instance`
--

LOCK TABLES `smctask_task_instance` WRITE;
/*!40000 ALTER TABLE `smctask_task_instance` DISABLE KEYS */;
INSERT INTO `smctask_task_instance` VALUES (1,'test',0);
/*!40000 ALTER TABLE `smctask_task_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smctask_task_jobs_level`
--

DROP TABLE IF EXISTS `smctask_task_jobs_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smctask_task_jobs_level` (
  `FK_task_id` varchar(100) NOT NULL,
  `FK_jobs_id` varchar(100) NOT NULL,
  `level` int NOT NULL,
  PRIMARY KEY (`FK_task_id`,`FK_jobs_id`),
  UNIQUE KEY `FK_task_id` (`FK_task_id`,`FK_jobs_id`),
  KEY `FK_jobs_id` (`FK_jobs_id`),
  CONSTRAINT `smctask_task_jobs_level_ibfk_1` FOREIGN KEY (`FK_task_id`) REFERENCES `smctask_task` (`id`),
  CONSTRAINT `smctask_task_jobs_level_ibfk_2` FOREIGN KEY (`FK_jobs_id`) REFERENCES `smctask_jobs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smctask_task_jobs_level`
--

LOCK TABLES `smctask_task_jobs_level` WRITE;
/*!40000 ALTER TABLE `smctask_task_jobs_level` DISABLE KEYS */;
INSERT INTO `smctask_task_jobs_level` VALUES ('recolte_wheat_potatoes','alier_des_nain',15),('recolte_wheat_potatoes','ami_des_elfe',10),('test','ami_des_elfe',10),('test2','alier_des_nain',30),('test2','ami_des_elfe',5),('test2','test_jobs',15),('test3','alier_des_nain',5),('test3','ami_des_elfe',5);
/*!40000 ALTER TABLE `smctask_task_jobs_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smctask_task_reward_command`
--

DROP TABLE IF EXISTS `smctask_task_reward_command`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smctask_task_reward_command` (
  `id` int NOT NULL AUTO_INCREMENT,
  `FK_task_id` varchar(100) NOT NULL,
  `command` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `FK_task_id` (`FK_task_id`),
  CONSTRAINT `smctask_task_reward_command_ibfk_1` FOREIGN KEY (`FK_task_id`) REFERENCES `smctask_task` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smctask_task_reward_command`
--

LOCK TABLES `smctask_task_reward_command` WRITE;
/*!40000 ALTER TABLE `smctask_task_reward_command` DISABLE KEYS */;
INSERT INTO `smctask_task_reward_command` VALUES (1,'test','lp user <user> promote usertrack','&epromote to next grade'),(2,'test','give <user> minecraft:stone 1','&e1 Stone'),(3,'recolte_wheat_potatoes','give <user> minecraft:wheat 1','&e1 Blé');
/*!40000 ALTER TABLE `smctask_task_reward_command` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smctask_task_reward_item`
--

DROP TABLE IF EXISTS `smctask_task_reward_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smctask_task_reward_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `FK_task_id` varchar(100) NOT NULL,
  `item` varchar(100) NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `FK_task_id` (`FK_task_id`),
  CONSTRAINT `smctask_task_reward_item_ibfk_1` FOREIGN KEY (`FK_task_id`) REFERENCES `smctask_task` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smctask_task_reward_item`
--

LOCK TABLES `smctask_task_reward_item` WRITE;
/*!40000 ALTER TABLE `smctask_task_reward_item` DISABLE KEYS */;
INSERT INTO `smctask_task_reward_item` VALUES (1,'test','STONE',64),(2,'test','TROPICAL_FISH',1),(3,'recolte_wheat_potatoes','STONE',64),(4,'recolte_wheat_potatoes','TROPICAL_FISH',1);
/*!40000 ALTER TABLE `smctask_task_reward_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smctask_task_reward_jobs_exp`
--

DROP TABLE IF EXISTS `smctask_task_reward_jobs_exp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `smctask_task_reward_jobs_exp` (
  `FK_task_id` varchar(100) NOT NULL,
  `FK_jobs_id` varchar(100) NOT NULL,
  `exp` int NOT NULL,
  PRIMARY KEY (`FK_task_id`,`FK_jobs_id`),
  UNIQUE KEY `FK_task_id` (`FK_task_id`,`FK_jobs_id`),
  KEY `FK_jobs_id` (`FK_jobs_id`),
  CONSTRAINT `smctask_task_reward_jobs_exp_ibfk_1` FOREIGN KEY (`FK_task_id`) REFERENCES `smctask_task` (`id`),
  CONSTRAINT `smctask_task_reward_jobs_exp_ibfk_2` FOREIGN KEY (`FK_jobs_id`) REFERENCES `smctask_jobs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smctask_task_reward_jobs_exp`
--

LOCK TABLES `smctask_task_reward_jobs_exp` WRITE;
/*!40000 ALTER TABLE `smctask_task_reward_jobs_exp` DISABLE KEYS */;
INSERT INTO `smctask_task_reward_jobs_exp` VALUES ('recolte_wheat_potatoes','alier_des_nain',150),('recolte_wheat_potatoes','ami_des_elfe',100),('test','ami_des_elfe',100);
/*!40000 ALTER TABLE `smctask_task_reward_jobs_exp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'smctask'
--

--
-- Dumping routines for database 'smctask'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-16  9:21:37
