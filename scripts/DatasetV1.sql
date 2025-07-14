-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: BankAnalysis
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `approval_process`
--

DROP TABLE IF EXISTS `approval_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `approval_process` (
  `process_id` bigint NOT NULL AUTO_INCREMENT COMMENT '娴佺▼ID',
  `application_id` bigint NOT NULL COMMENT '鐢宠?ID',
  `step_number` int NOT NULL COMMENT '姝ラ?缂栧彿',
  `step_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '姝ラ?鍚嶇О',
  `approver_id` bigint NOT NULL COMMENT '瀹℃壒浜篒D',
  `approval_status` tinyint NOT NULL COMMENT '瀹℃壒鐘舵?锛?-寰呭?鎵癸紝2-閫氳繃锛?-鎷掔粷',
  `approval_time` datetime DEFAULT NULL COMMENT '瀹℃壒鏃堕棿',
  `approval_comment` text COLLATE utf8mb4_unicode_ci COMMENT '瀹℃壒鎰忚?',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`process_id`),
  KEY `application_id` (`application_id`),
  KEY `approver_id` (`approver_id`),
  CONSTRAINT `approval_process_ibfk_1` FOREIGN KEY (`application_id`) REFERENCES `credit_application` (`application_id`) ON DELETE CASCADE,
  CONSTRAINT `approval_process_ibfk_2` FOREIGN KEY (`approver_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='瀹℃牳娴佺▼琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `approval_process`
--

LOCK TABLES `approval_process` WRITE;
/*!40000 ALTER TABLE `approval_process` DISABLE KEYS */;
/*!40000 ALTER TABLE `approval_process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `certificate`
--

DROP TABLE IF EXISTS `certificate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `certificate` (
  `certificate_id` bigint NOT NULL AUTO_INCREMENT COMMENT '璇佷功ID',
  `user_id` bigint NOT NULL COMMENT '鐢ㄦ埛ID',
  `certificate_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '璇佷功绫诲瀷锛氳?绋嬭瘉涔︺?鍩硅?璇佷功銆佹妧鑳借瘉涔︾瓑',
  `certificate_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '璇佷功鍚嶇О',
  `issuing_organization` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '鍙戣瘉鏈烘瀯',
  `issue_date` date NOT NULL COMMENT '鍙戣瘉鏃ユ湡',
  `expiry_date` date DEFAULT NULL COMMENT '鏈夋晥鏈熻嚦',
  `certificate_number` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '璇佷功缂栧彿',
  `certificate_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '璇佷功鏂囦欢URL',
  `verification_status` tinyint DEFAULT '1' COMMENT '楠岃瘉鐘舵?锛?-鏈?獙璇侊紝1-宸查獙璇侊紝2-楠岃瘉澶辫触',
  `status` tinyint DEFAULT '1' COMMENT '鐘舵?锛?-鏃犳晥锛?-鏈夋晥',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  PRIMARY KEY (`certificate_id`),
  UNIQUE KEY `certificate_number` (`certificate_number`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `certificate_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='璇佷功琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certificate`
--

LOCK TABLES `certificate` WRITE;
/*!40000 ALTER TABLE `certificate` DISABLE KEYS */;
/*!40000 ALTER TABLE `certificate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `course_id` bigint NOT NULL AUTO_INCREMENT COMMENT '璇剧▼ID',
  `course_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '璇剧▼鍚嶇О',
  `course_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '璇剧▼缂栫爜',
  `category_id` bigint NOT NULL COMMENT '鍒嗙被ID',
  `instructor_id` bigint NOT NULL COMMENT '鎺堣?鏁欏笀ID',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '璇剧▼鎻忚堪',
  `syllabus` text COLLATE utf8mb4_unicode_ci COMMENT '璇剧▼澶х翰',
  `credit_hours` int NOT NULL COMMENT '瀛︽椂',
  `credit_value` decimal(5,2) NOT NULL COMMENT '瀛﹀垎鍊',
  `max_students` int DEFAULT NULL COMMENT '鏈?ぇ瀛︾敓鏁',
  `current_students` int DEFAULT '0' COMMENT '褰撳墠瀛︾敓鏁',
  `start_date` date DEFAULT NULL COMMENT '寮??鏃ユ湡',
  `end_date` date DEFAULT NULL COMMENT '缁撹?鏃ユ湡',
  `status` tinyint DEFAULT '1' COMMENT '鐘舵?锛?-鍏抽棴锛?-寮?斁锛?-婊″憳',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  PRIMARY KEY (`course_id`),
  UNIQUE KEY `course_code` (`course_code`),
  KEY `category_id` (`category_id`),
  KEY `instructor_id` (`instructor_id`),
  CONSTRAINT `course_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `resource_category` (`category_id`),
  CONSTRAINT `course_ibfk_2` FOREIGN KEY (`instructor_id`) REFERENCES `sys_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='璇剧▼琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,'test','CSCS',1,3,'','',36,2.00,100,1,'2025-07-15','2025-07-31',1,'2025-07-14 01:41:53','2025-07-14 01:46:23');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_enrollment`
--

DROP TABLE IF EXISTS `course_enrollment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_enrollment` (
  `enrollment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '閫夎?ID',
  `course_id` bigint NOT NULL COMMENT '璇剧▼ID',
  `user_id` bigint NOT NULL COMMENT '瀛︾敓ID',
  `enrollment_date` date NOT NULL COMMENT '閫夎?鏃ユ湡',
  `status` tinyint DEFAULT '1' COMMENT '鐘舵?锛?-宸查?璇撅紝1-姝ｅ父',
  `final_grade` decimal(5,2) DEFAULT NULL COMMENT '鏈?粓鎴愮哗',
  `credits_earned` decimal(5,2) DEFAULT NULL COMMENT '鑾峰緱瀛﹀垎',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`enrollment_id`),
  UNIQUE KEY `uk_course_user` (`course_id`,`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `course_enrollment_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE,
  CONSTRAINT `course_enrollment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='璇剧▼閫夎?琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_enrollment`
--

LOCK TABLES `course_enrollment` WRITE;
/*!40000 ALTER TABLE `course_enrollment` DISABLE KEYS */;
INSERT INTO `course_enrollment` VALUES (1,1,1,'2025-07-14',1,0.00,0.00,'2025-07-14 01:46:23');
/*!40000 ALTER TABLE `course_enrollment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credit_account`
--

DROP TABLE IF EXISTS `credit_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credit_account` (
  `account_id` bigint NOT NULL AUTO_INCREMENT COMMENT '璐︽埛ID',
  `user_id` bigint NOT NULL COMMENT '鐢ㄦ埛ID',
  `total_credits` decimal(10,2) DEFAULT '0.00' COMMENT '鎬诲?鍒',
  `available_credits` decimal(10,2) DEFAULT '0.00' COMMENT '鍙?敤瀛﹀垎',
  `frozen_credits` decimal(10,2) DEFAULT '0.00' COMMENT '鍐荤粨瀛﹀垎',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `credit_account_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='瀛﹀垎璐︽埛琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credit_account`
--

LOCK TABLES `credit_account` WRITE;
/*!40000 ALTER TABLE `credit_account` DISABLE KEYS */;
INSERT INTO `credit_account` VALUES (1,4,27.40,20.90,5.50,'2025-07-13 00:23:18','2025-07-14 00:28:04'),(2,5,18.00,15.00,3.00,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(3,1,0.00,0.00,0.00,'2025-07-13 11:26:07','2025-07-13 11:26:07'),(4,6,0.00,0.00,0.00,'2025-07-13 15:44:14','2025-07-13 15:44:14'),(5,7,0.00,0.00,0.00,'2025-07-13 15:45:27','2025-07-13 15:45:27');
/*!40000 ALTER TABLE `credit_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credit_application`
--

DROP TABLE IF EXISTS `credit_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credit_application` (
  `application_id` bigint NOT NULL AUTO_INCREMENT COMMENT '鐢宠?ID',
  `user_id` bigint NOT NULL COMMENT '鐢ㄦ埛ID',
  `application_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '鐢宠?绫诲瀷锛氭妧鑳借瘉涔︺?鑱屼笟鍩硅?銆佸?鍘嗘暀鑲茬瓑',
  `achievement_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '鎴愭灉鍚嶇О',
  `achievement_description` text COLLATE utf8mb4_unicode_ci COMMENT '鎴愭灉鎻忚堪',
  `applied_credits` decimal(10,2) NOT NULL COMMENT '鐢宠?瀛﹀垎',
  `evidence_files` json DEFAULT NULL COMMENT '璇佹嵁鏂囦欢鍒楄〃',
  `current_step` int DEFAULT '1' COMMENT '褰撳墠瀹℃牳姝ラ?',
  `status` tinyint DEFAULT '1' COMMENT '鐢宠?鐘舵?锛?-寰呭?鏍革紝2-瀹℃牳涓?紝3-閫氳繃锛?-鎷掔粷',
  `apply_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鐢宠?鏃堕棿',
  `approve_time` datetime DEFAULT NULL COMMENT '瀹℃牳鏃堕棿',
  `remark` text COLLATE utf8mb4_unicode_ci COMMENT '澶囨敞',
  PRIMARY KEY (`application_id`),
  KEY `idx_credit_application_user_id` (`user_id`),
  KEY `idx_credit_application_status` (`status`),
  CONSTRAINT `credit_application_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='瀛﹀垎鐢宠?琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credit_application`
--

LOCK TABLES `credit_application` WRITE;
/*!40000 ALTER TABLE `credit_application` DISABLE KEYS */;
INSERT INTO `credit_application` VALUES (1,4,'学历教育','高中','high school',1.00,NULL,1,3,'2025-07-13 23:09:34','2025-07-13 23:37:31','通过');
/*!40000 ALTER TABLE `credit_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credit_conversion_rule`
--

DROP TABLE IF EXISTS `credit_conversion_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credit_conversion_rule` (
  `rule_id` bigint NOT NULL AUTO_INCREMENT COMMENT '瑙勫垯ID',
  `source_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `target_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `conversion_rate` decimal(5,2) NOT NULL COMMENT '杞?崲姣斾緥',
  `min_credits` decimal(10,2) DEFAULT '0.00' COMMENT '鏈?皬杞?崲瀛﹀垎',
  `max_credits` decimal(10,2) DEFAULT NULL COMMENT '鏈?ぇ杞?崲瀛﹀垎',
  `status` tinyint DEFAULT '1' COMMENT '鐘舵?锛?-绂佺敤锛?-鍚?敤',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  PRIMARY KEY (`rule_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='瀛﹀垎杞?崲瑙勫垯琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credit_conversion_rule`
--

LOCK TABLES `credit_conversion_rule` WRITE;
/*!40000 ALTER TABLE `credit_conversion_rule` DISABLE KEYS */;
INSERT INTO `credit_conversion_rule` VALUES (6,'技能证书','职业培训',0.80,1.00,10.00,1,'2025-07-14 00:22:13','2025-07-14 00:22:13'),(7,'职业培训','技能证书',0.70,1.00,8.00,1,'2025-07-14 00:22:13','2025-07-14 00:22:13'),(8,'学历教育','职业培训',0.90,1.00,15.00,1,'2025-07-14 00:22:13','2025-07-14 00:22:13'),(9,'在线课程','职业培训',0.60,1.00,5.00,1,'2025-07-14 00:22:13','2025-07-14 00:22:13'),(10,'其他','技能证书',0.90,1.00,12.00,1,'2025-07-14 00:22:13','2025-07-14 00:22:13'),(11,'学历教育','技能证书',0.80,1.00,10.00,1,'2025-07-14 00:22:13','2025-07-14 00:22:13'),(12,'学历教育','在线课程',0.70,1.00,8.00,1,'2025-07-14 00:22:13','2025-07-14 00:22:13'),(13,'技能证书','学历教育',0.60,1.00,8.00,1,'2025-07-14 00:22:13','2025-07-14 00:22:13'),(14,'技能证书','在线课程',0.80,1.00,10.00,1,'2025-07-14 00:22:13','2025-07-14 00:22:13'),(15,'职业培训','学历教育',0.50,1.00,6.00,1,'2025-07-14 00:22:13','2025-07-14 00:22:13'),(16,'职业培训','在线课程',0.70,1.00,8.00,1,'2025-07-14 00:22:13','2025-07-14 00:22:13'),(17,'在线课程','学历教育',0.50,1.00,6.00,1,'2025-07-14 00:22:13','2025-07-14 00:22:13'),(18,'在线课程','技能证书',0.60,1.00,7.00,1,'2025-07-14 00:22:13','2025-07-14 00:22:13');
/*!40000 ALTER TABLE `credit_conversion_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credit_record`
--

DROP TABLE IF EXISTS `credit_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credit_record` (
  `record_id` bigint NOT NULL AUTO_INCREMENT COMMENT '璁板綍ID',
  `user_id` bigint NOT NULL COMMENT '鐢ㄦ埛ID',
  `credit_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '瀛﹀垎绫诲瀷锛氬?鍘嗘暀鑲层?鑱屼笟鍩硅?銆佹妧鑳借瘉涔︾瓑',
  `credit_source` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '瀛﹀垎鏉ユ簮',
  `credit_amount` decimal(10,2) NOT NULL COMMENT '瀛﹀垎鏁伴噺',
  `operation_type` tinyint NOT NULL COMMENT '鎿嶄綔绫诲瀷锛?-鑾峰緱锛?-娑堣垂锛?-杞?崲',
  `status` tinyint DEFAULT '1' COMMENT '鐘舵?锛?-鏃犳晥锛?-鏈夋晥锛?-寰呭?鏍',
  `remark` text COLLATE utf8mb4_unicode_ci COMMENT '澶囨敞',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`record_id`),
  KEY `idx_credit_record_user_id` (`user_id`),
  KEY `idx_credit_record_create_time` (`create_time`),
  CONSTRAINT `credit_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='瀛﹀垎璁板綍琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credit_record`
--

LOCK TABLES `credit_record` WRITE;
/*!40000 ALTER TABLE `credit_record` DISABLE KEYS */;
INSERT INTO `credit_record` VALUES (1,4,'Skill Certificate','Computer Level 2 Certificate',3.00,1,1,'Skill certification credit earned','2025-07-13 00:23:18'),(2,4,'Academic Education','CET-4 Certificate',2.50,1,1,'Language ability certification credit earned','2025-07-13 00:23:18'),(3,5,'Skill Certificate','Software Testing Engineer Certificate',4.00,1,1,'Professional skill certification credit earned','2025-07-13 00:23:18'),(4,5,'Online Course','Java Advanced Programming Course',3.00,1,1,'Online course credit earned','2025-07-13 00:23:18'),(5,4,'学历教育','高中',1.00,1,1,'学分申请审核通过，申请ID: 1','2025-07-13 23:37:31'),(6,4,'学分消费','系统扣减',1.00,2,1,'学分账户扣减','2025-07-14 00:28:04'),(7,4,'职业培训','学分转换',0.90,1,1,'从 学历教育 转换而来，原始学分：1','2025-07-14 00:28:04'),(8,4,'学分转换','学历教育 → 职业培训',0.90,3,1,'将 1 学历教育 转换为 0.90 职业培训','2025-07-14 00:28:04');
/*!40000 ALTER TABLE `credit_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learning_progress`
--

DROP TABLE IF EXISTS `learning_progress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `learning_progress` (
  `progress_id` bigint NOT NULL AUTO_INCREMENT COMMENT '杩涘害ID',
  `user_id` bigint NOT NULL COMMENT '鐢ㄦ埛ID',
  `course_id` bigint DEFAULT NULL COMMENT '璇剧▼ID',
  `resource_id` bigint DEFAULT NULL COMMENT '璧勬簮ID',
  `progress_percentage` decimal(5,2) DEFAULT '0.00' COMMENT '杩涘害鐧惧垎姣',
  `learning_time` int DEFAULT '0' COMMENT '瀛︿範鏃堕暱(鍒嗛挓)',
  `last_access_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鏈?悗璁块棶鏃堕棿',
  `completion_date` datetime DEFAULT NULL COMMENT '瀹屾垚鏃ユ湡',
  `status` tinyint DEFAULT '1' COMMENT '鐘舵?锛?-鏈?紑濮嬶紝1-瀛︿範涓?紝2-宸插畬鎴',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  PRIMARY KEY (`progress_id`),
  KEY `user_id` (`user_id`),
  KEY `course_id` (`course_id`),
  KEY `resource_id` (`resource_id`),
  CONSTRAINT `learning_progress_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `learning_progress_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE,
  CONSTRAINT `learning_progress_ibfk_3` FOREIGN KEY (`resource_id`) REFERENCES `learning_resource` (`resource_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='瀛︿範杩涘害琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learning_progress`
--

LOCK TABLES `learning_progress` WRITE;
/*!40000 ALTER TABLE `learning_progress` DISABLE KEYS */;
/*!40000 ALTER TABLE `learning_progress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `learning_resource`
--

DROP TABLE IF EXISTS `learning_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `learning_resource` (
  `resource_id` bigint NOT NULL AUTO_INCREMENT COMMENT '璧勬簮ID',
  `resource_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '璧勬簮鍚嶇О',
  `resource_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '璧勬簮绫诲瀷锛氳?绋嬨?鏁欐潗銆佽?浠躲?瀹炶?椤圭洰',
  `category_id` bigint NOT NULL COMMENT '鍒嗙被ID',
  `subject` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '瀛︾?棰嗗煙',
  `keywords` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '鍏抽敭璇嶏紝鐢ㄩ?鍙峰垎闅',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '璧勬簮鎻忚堪',
  `content_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '璧勬簮鍐呭?URL',
  `thumbnail_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '缂╃暐鍥綰RL',
  `file_size` bigint DEFAULT '0' COMMENT '鏂囦欢澶у皬(瀛楄妭)',
  `duration` int DEFAULT '0' COMMENT '鏃堕暱(鍒嗛挓)',
  `difficulty_level` tinyint DEFAULT '1' COMMENT '闅惧害绾у埆锛?-鍒濈骇锛?-涓?骇锛?-楂樼骇',
  `credit_value` decimal(5,2) DEFAULT '0.00' COMMENT '瀛﹀垎浠峰?',
  `view_count` int DEFAULT '0' COMMENT '娴忚?娆℃暟',
  `download_count` int DEFAULT '0' COMMENT '涓嬭浇娆℃暟',
  `favorite_count` int DEFAULT '0' COMMENT '鏀惰棌娆℃暟',
  `rating` decimal(3,2) DEFAULT '0.00' COMMENT '骞冲潎璇勫垎(0-5)',
  `rating_count` int DEFAULT '0' COMMENT '璇勫垎浜烘暟',
  `uploader_id` bigint NOT NULL COMMENT '涓婁紶鑰匢D',
  `tags` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '鏍囩?锛岀敤閫楀彿鍒嗛殧',
  `prerequisites` text COLLATE utf8mb4_unicode_ci COMMENT '鍓嶇疆瑕佹眰',
  `learning_objectives` text COLLATE utf8mb4_unicode_ci COMMENT '瀛︿範鐩?爣',
  `status` tinyint DEFAULT '2' COMMENT '鐘舵?锛?-涓嬫灦锛?-涓婃灦锛?-瀹℃牳涓',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  PRIMARY KEY (`resource_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_resource_type` (`resource_type`),
  KEY `idx_difficulty_level` (`difficulty_level`),
  KEY `idx_status` (`status`),
  KEY `idx_uploader_id` (`uploader_id`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `learning_resource_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `resource_category` (`category_id`),
  CONSTRAINT `learning_resource_ibfk_2` FOREIGN KEY (`uploader_id`) REFERENCES `sys_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='瀛︿範璧勬簮琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `learning_resource`
--

LOCK TABLES `learning_resource` WRITE;
/*!40000 ALTER TABLE `learning_resource` DISABLE KEYS */;
INSERT INTO `learning_resource` VALUES (1,'Java Basic Programming Tutorial','Video Course',3,'Computer Science','Java,Programming,Basic,OOP','Java programming language basic tutorial, includes syntax and OOP concepts','https://example.com/java-basic','https://example.com/java-basic-thumb.jpg',0,0,1,3.00,0,0,0,0.00,0,2,'Beginner Friendly,Video Tutorial','None','Master Java basic syntax and OOP concepts',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(2,'Python Data Analysis Practice','Practical Project',4,'Data Science','Python,Data Analysis,pandas,numpy','Python data analysis practical project using pandas and numpy','https://example.com/python-data-analysis','https://example.com/python-data-thumb.jpg',0,0,2,4.00,0,0,0,0.00,0,2,'Practical Project,Open Source','Master Python basic syntax','Ability to use Python for data analysis and visualization',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(3,'React Frontend Development Guide','eBook',7,'Frontend Development','React,Frontend,JavaScript,Components','Complete React frontend framework learning guide from basic to advanced','https://example.com/react-guide.pdf','https://example.com/react-guide-thumb.jpg',0,0,2,3.50,0,0,0,0.00,0,3,'Framework Learning,PDF Document','Master JavaScript and ES6 syntax','Ability to develop modern frontend applications with React',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(4,'Java核心编程基础','课程',3,'Java编程','Java,编程,基础','Java编程基础课程，适合初学者',NULL,NULL,0,0,1,2.00,0,0,0,0.00,0,1,'初学者友好,编程基础','无','掌握Java语言基础语法和编程思想',1,'2025-07-13 00:39:26','2025-07-13 00:39:26'),(5,'Spring Boot实战教程','教材',3,'Java框架','Spring Boot,Java,框架','Spring Boot框架学习教材，从入门到精通',NULL,NULL,0,0,2,3.50,0,0,0,0.00,0,1,'框架学习,企业级开发','Java基础','掌握Spring Boot框架开发企业级应用',1,'2025-07-13 00:39:26','2025-07-13 00:39:26'),(6,'Java设计模式详解','课程',3,'Java设计模式','设计模式,Java,面向对象','详细讲解Java中的23种设计模式及应用场景',NULL,NULL,0,0,3,4.00,0,0,0,0.00,0,1,'设计模式,面试必备','Java面向对象基础','灵活运用设计模式解决软件设计问题',1,'2025-07-13 00:39:26','2025-07-13 00:39:26'),(11,'CSS高级技巧与动画','课件',5,'前端开发','CSS,动画,前端','CSS高级特性与动画效果制作详解',NULL,NULL,0,0,2,2.00,0,0,0,0.00,0,3,'前端开发,UI设计','HTML,CSS基础','创建美观的网页动画和交互效果',1,'2025-07-13 00:39:26','2025-07-13 00:39:26'),(12,'MySQL性能优化实战','课程',6,'数据库','MySQL,性能优化,数据库','MySQL数据库性能调优与最佳实践',NULL,NULL,0,0,3,4.00,0,0,0,0.00,0,4,'数据库,性能优化','SQL基础,数据库原理','解决数据库性能瓶颈问题',1,'2025-07-13 00:39:26','2025-07-13 00:39:26'),(13,'MongoDB入门到精通','教材',6,'数据库','MongoDB,NoSQL,数据库','MongoDB文档数据库完全学习指南',NULL,NULL,0,0,2,3.00,0,0,0,0.00,0,4,'数据库,NoSQL','基本数据库概念','使用MongoDB存储和查询文档数据',1,'2025-07-13 00:39:26','2025-07-13 00:39:26'),(14,'机器学习','课程',14,NULL,NULL,'福建师范大学的机器学习课程','https://www.icourse163.org/course/FJNU-1472386164',NULL,NULL,NULL,1,2.00,0,0,0,NULL,NULL,1,'','','',1,'2025-07-13 08:42:54','2025-07-14 02:50:00');
/*!40000 ALTER TABLE `learning_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operation_log`
--

DROP TABLE IF EXISTS `operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operation_log` (
  `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '鏃ュ織ID',
  `user_id` bigint DEFAULT NULL COMMENT '鐢ㄦ埛ID',
  `module` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '妯″潡鍚嶇О',
  `operation` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '鎿嶄綔鍚嶇О',
  `method` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '璇锋眰鏂规硶',
  `request_url` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '璇锋眰URL',
  `request_params` text COLLATE utf8mb4_unicode_ci COMMENT '璇锋眰鍙傛暟',
  `response_time` int DEFAULT NULL COMMENT '鍝嶅簲鏃堕棿(姣??)',
  `ip_address` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'IP鍦板潃',
  `user_agent` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '鐢ㄦ埛浠ｇ悊',
  `status` tinyint DEFAULT '1' COMMENT '鐘舵?锛?-澶辫触锛?-鎴愬姛',
  `error_message` text COLLATE utf8mb4_unicode_ci COMMENT '閿欒?淇℃伅',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`log_id`),
  KEY `idx_operation_log_user_id` (`user_id`),
  KEY `idx_operation_log_create_time` (`create_time`),
  CONSTRAINT `operation_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='鎿嶄綔鏃ュ織琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operation_log`
--

LOCK TABLES `operation_log` WRITE;
/*!40000 ALTER TABLE `operation_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `operation_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource_category`
--

DROP TABLE IF EXISTS `resource_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resource_category` (
  `category_id` bigint NOT NULL AUTO_INCREMENT COMMENT '鍒嗙被ID',
  `category_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '鍒嗙被鍚嶇О',
  `parent_id` bigint DEFAULT '0' COMMENT '鐖剁骇鍒嗙被ID锛?琛ㄧず椤剁骇鍒嗙被',
  `category_path` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '鍒嗙被璺?緞锛岀敤閫楀彿鍒嗛殧',
  `level` int DEFAULT '1' COMMENT '鍒嗙被灞傜骇',
  `sort_order` int DEFAULT '0' COMMENT '鎺掑簭鏉冮噸',
  `icon` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '鍒嗙被鍥炬爣',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '鍒嗙被鎻忚堪',
  `status` tinyint DEFAULT '1' COMMENT '鐘舵?锛?-绂佺敤锛?-鍚?敤',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='璧勬簮鍒嗙被琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource_category`
--

LOCK TABLES `resource_category` WRITE;
/*!40000 ALTER TABLE `resource_category` DISABLE KEYS */;
INSERT INTO `resource_category` VALUES (1,'Computer Science',0,'1',1,1,NULL,'Computer Science Related Resources',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(2,'Programming Languages',1,'1,2',2,1,NULL,'Various Programming Language Learning Resources',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(3,'Java',2,'1,2,3',3,1,NULL,'Java Programming Language',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(4,'Python',2,'1,2,4',3,2,NULL,'Python Programming Language',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(5,'JavaScript',2,'1,2,5',3,3,NULL,'JavaScript Programming Language',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(6,'Frontend Technology',1,'1,6',2,2,NULL,'Frontend Development Technology',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(7,'React',6,'1,6,7',3,1,NULL,'React Frontend Framework',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(8,'Vue',6,'1,6,8',3,2,NULL,'Vue Frontend Framework',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(9,'Database',1,'1,9',2,3,NULL,'Database Technology',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(10,'MySQL',9,'1,9,10',3,1,NULL,'MySQL Database',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(11,'Software Engineering',0,'11',1,2,NULL,'Software Engineering Methods and Practices',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(12,'Project Management',11,'11,12',2,1,NULL,'Project Management Knowledge',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(13,'Testing Technology',11,'11,13',2,2,NULL,'Software Testing Technology',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(14,'Artificial Intelligence',0,'14',1,3,NULL,'AI Related Technology',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(15,'Machine Learning',14,'14,15',2,1,NULL,'Machine Learning Technology',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(16,'Deep Learning',14,'14,16',2,2,NULL,'Deep Learning Technology',1,'2025-07-13 00:23:18','2025-07-13 00:23:18');
/*!40000 ALTER TABLE `resource_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource_recommendation`
--

DROP TABLE IF EXISTS `resource_recommendation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resource_recommendation` (
  `recommendation_id` bigint NOT NULL AUTO_INCREMENT COMMENT '鎺ㄨ崘ID',
  `user_id` bigint NOT NULL COMMENT '鐢ㄦ埛ID',
  `resource_id` bigint NOT NULL COMMENT '璧勬簮ID',
  `recommendation_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '鎺ㄨ崘绫诲瀷锛歝ollaborative-鍗忓悓杩囨护锛宑ontent-鍐呭?鎺ㄨ崘锛宲opular-鐑?棬鎺ㄨ崘',
  `score` decimal(5,4) DEFAULT '0.0000' COMMENT '鎺ㄨ崘鍒嗘暟',
  `reason` text COLLATE utf8mb4_unicode_ci COMMENT '鎺ㄨ崘鐞嗙敱',
  `status` tinyint DEFAULT '1' COMMENT '鐘舵?锛?-澶辨晥锛?-鏈夋晥',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  PRIMARY KEY (`recommendation_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_resource_id` (`resource_id`),
  KEY `idx_recommendation_type` (`recommendation_type`),
  KEY `idx_score` (`score`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `resource_recommendation_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `resource_recommendation_ibfk_2` FOREIGN KEY (`resource_id`) REFERENCES `learning_resource` (`resource_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='璧勬簮鎺ㄨ崘琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource_recommendation`
--

LOCK TABLES `resource_recommendation` WRITE;
/*!40000 ALTER TABLE `resource_recommendation` DISABLE KEYS */;
/*!40000 ALTER TABLE `resource_recommendation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource_review`
--

DROP TABLE IF EXISTS `resource_review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resource_review` (
  `review_id` bigint NOT NULL AUTO_INCREMENT COMMENT '璇勪环ID',
  `resource_id` bigint NOT NULL COMMENT '璧勬簮ID',
  `user_id` bigint NOT NULL COMMENT '鐢ㄦ埛ID',
  `rating` tinyint NOT NULL COMMENT '璇勫垎(1-5)',
  `review_content` text COLLATE utf8mb4_unicode_ci COMMENT '璇勪环鍐呭?',
  `helpful_count` int DEFAULT '0' COMMENT '鏈夌敤鏁',
  `status` tinyint DEFAULT '1' COMMENT '鐘舵?锛?-闅愯棌锛?-鏄剧ず',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  PRIMARY KEY (`review_id`),
  UNIQUE KEY `uk_resource_user` (`resource_id`,`user_id`),
  KEY `idx_resource_id` (`resource_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `resource_review_ibfk_1` FOREIGN KEY (`resource_id`) REFERENCES `learning_resource` (`resource_id`) ON DELETE CASCADE,
  CONSTRAINT `resource_review_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='璧勬簮璇勪环琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource_review`
--

LOCK TABLES `resource_review` WRITE;
/*!40000 ALTER TABLE `resource_review` DISABLE KEYS */;
INSERT INTO `resource_review` VALUES (4,14,1,5,'太厉害了捏jrm',0,1,'2025-07-13 10:39:08','2025-07-13 10:49:43');
/*!40000 ALTER TABLE `resource_review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource_tag`
--

DROP TABLE IF EXISTS `resource_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resource_tag` (
  `tag_id` bigint NOT NULL AUTO_INCREMENT COMMENT '鏍囩?ID',
  `tag_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '鏍囩?鍚嶇О',
  `tag_color` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT '#409EFF' COMMENT '鏍囩?棰滆壊',
  `use_count` int DEFAULT '0' COMMENT '浣跨敤娆℃暟',
  `status` tinyint DEFAULT '1' COMMENT '鐘舵?锛?-绂佺敤锛?-鍚?敤',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `uk_tag_name` (`tag_name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='璧勬簮鏍囩?琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource_tag`
--

LOCK TABLES `resource_tag` WRITE;
/*!40000 ALTER TABLE `resource_tag` DISABLE KEYS */;
INSERT INTO `resource_tag` VALUES (1,'Beginner Friendly','#67C23A',0,1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(2,'Practical Project','#E6A23C',0,1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(3,'Interview Essential','#F56C6C',0,1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(4,'Framework Learning','#909399',0,1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(5,'Algorithm DataStructure','#409EFF',0,1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(6,'Enterprise Development','#606266',0,1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(7,'Open Source','#67C23A',0,1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(8,'Video Tutorial','#E6A23C',0,1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(9,'PDF Document','#F56C6C',0,1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(10,'Online Course','#909399',0,1,'2025-07-13 00:23:18','2025-07-13 00:23:18');
/*!40000 ALTER TABLE `resource_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource_tag_relation`
--

DROP TABLE IF EXISTS `resource_tag_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resource_tag_relation` (
  `relation_id` bigint NOT NULL AUTO_INCREMENT COMMENT '鍏宠仈ID',
  `resource_id` bigint NOT NULL COMMENT '璧勬簮ID',
  `tag_id` bigint NOT NULL COMMENT '鏍囩?ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`relation_id`),
  UNIQUE KEY `uk_resource_tag` (`resource_id`,`tag_id`),
  KEY `idx_resource_id` (`resource_id`),
  KEY `idx_tag_id` (`tag_id`),
  CONSTRAINT `resource_tag_relation_ibfk_1` FOREIGN KEY (`resource_id`) REFERENCES `learning_resource` (`resource_id`) ON DELETE CASCADE,
  CONSTRAINT `resource_tag_relation_ibfk_2` FOREIGN KEY (`tag_id`) REFERENCES `resource_tag` (`tag_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='璧勬簮鏍囩?鍏宠仈琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource_tag_relation`
--

LOCK TABLES `resource_tag_relation` WRITE;
/*!40000 ALTER TABLE `resource_tag_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `resource_tag_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_permission`
--

DROP TABLE IF EXISTS `sys_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_permission` (
  `permission_id` bigint NOT NULL AUTO_INCREMENT COMMENT '鏉冮檺ID',
  `permission_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '鏉冮檺鍚嶇О',
  `permission_key` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '鏉冮檺鏍囪瘑',
  `permission_type` tinyint NOT NULL COMMENT '鏉冮檺绫诲瀷锛?-鐩?綍锛?-鑿滃崟锛?-鎸夐挳',
  `parent_id` bigint DEFAULT '0' COMMENT '鐖舵潈闄怚D',
  `path` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '璺?敱鍦板潃',
  `component` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '缁勪欢璺?緞',
  `icon` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '鍥炬爣',
  `sort_order` int DEFAULT '0' COMMENT '鏄剧ず椤哄簭',
  `status` tinyint DEFAULT '1' COMMENT '鐘舵?锛?-绂佺敤锛?-鍚?敤',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  PRIMARY KEY (`permission_id`),
  UNIQUE KEY `permission_key` (`permission_key`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='鏉冮檺琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_permission`
--

LOCK TABLES `sys_permission` WRITE;
/*!40000 ALTER TABLE `sys_permission` DISABLE KEYS */;
INSERT INTO `sys_permission` VALUES (1,'System Management','system',1,0,'/system',NULL,'Setting',1,1,'2025-07-13 00:23:18','2025-07-14 02:19:34'),(2,'User Management','system:user',2,1,'/system/user','system/User','User',1,1,'2025-07-13 00:23:18','2025-07-14 02:19:34'),(3,'Role Management','system:role',2,1,'/system/role','system/Role','UserFilled',2,1,'2025-07-13 00:23:18','2025-07-14 02:19:34'),(4,'Permission Management','system:permission',2,1,'/system/permission','system/Permission','Key',3,1,'2025-07-13 00:23:18','2025-07-14 02:19:34'),(5,'Credit Management','credit',1,0,'/credit',NULL,'CreditCard',2,1,'2025-07-13 00:23:18','2025-07-14 02:19:35'),(6,'Credit Account','credit:account',2,5,'/credit/account','credit/Account','Wallet',1,1,'2025-07-13 00:23:18','2025-07-14 02:19:34'),(7,'Credit Record','credit:record',2,5,'/credit/record','credit/Record','Document',2,1,'2025-07-13 00:23:18','2025-07-14 02:19:34'),(8,'Credit Application','credit:application',2,5,'/credit/application','credit/Application','Edit',3,1,'2025-07-13 00:23:18','2025-07-14 02:19:34'),(9,'Resource Management','resource',1,0,'/resource',NULL,'Folder',3,1,'2025-07-13 00:23:18','2025-07-14 02:19:34'),(10,'Resource Library','resource:library',2,9,'/resource/library','resource/Library','Files',1,1,'2025-07-13 00:23:18','2025-07-14 02:19:34'),(11,'Resource Category','resource:category',2,9,'/resource/category','resource/Category','Menu',2,1,'2025-07-13 00:23:18','2025-07-14 02:19:35'),(12,'课程管理','course',1,0,'/course',NULL,'Files',4,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(13,'课程列表','course:list',2,12,'/course/list','course/CourseList','Document',1,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(14,'课程详情','course:detail',2,12,'/course/detail','course/CourseDetail','View',2,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(15,'学习进度','course:progress',2,12,'/course/progress','course/LearningProgress','Histogram',3,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(16,'课程查询','course:list:query',3,13,'','','',1,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(17,'课程创建','course:list:create',3,13,'','','',2,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(18,'课程修改','course:list:edit',3,13,'','','',3,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(19,'课程删除','course:list:delete',3,13,'','','',4,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(20,'课程审核','course:list:audit',3,13,'','','',5,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(21,'培训项目','training',1,0,'/training',NULL,'Connection',5,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(22,'培训计划','training:program',2,22,'/training/program','course/TrainingProgram','Calendar',1,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(23,'培训查询','training:program:query',3,23,'','','',1,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(24,'培训创建','training:program:create',3,23,'','','',2,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(25,'培训修改','training:program:edit',3,23,'','','',3,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(26,'培训删除','training:program:delete',3,23,'','','',4,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(27,'学分账户查询','credit:account:query',3,6,'','','',1,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(28,'学分账户修改','credit:account:edit',3,6,'','','',2,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(29,'学分记录查询','credit:record:query',3,7,'','','',1,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(30,'学分记录创建','credit:record:create',3,7,'','','',2,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(31,'学分申请查询','credit:application:query',3,8,'','','',1,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(32,'学分申请创建','credit:application:create',3,8,'','','',2,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(33,'学分申请审核','credit:application:audit',3,8,'','','',3,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(34,'资源查询','resource:library:query',3,10,'','','',1,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(35,'资源上传','resource:library:upload',3,10,'','','',2,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(36,'资源下载','resource:library:download',3,10,'','','',3,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(37,'资源修改','resource:library:edit',3,10,'','','',4,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(38,'资源删除','resource:library:delete',3,10,'','','',5,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(39,'资源分类查询','resource:category:query',3,11,'','','',1,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(40,'资源分类创建','resource:category:create',3,11,'','','',2,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(41,'资源分类修改','resource:category:edit',3,11,'','','',3,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(42,'资源分类删除','resource:category:delete',3,11,'','','',4,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(43,'用户查询','system:user:query',3,2,'','','',1,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(44,'用户创建','system:user:create',3,2,'','','',2,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(45,'用户修改','system:user:edit',3,2,'','','',3,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(46,'用户删除','system:user:delete',3,2,'','','',4,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(47,'角色查询','system:role:query',3,3,'','','',1,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(48,'角色创建','system:role:create',3,3,'','','',2,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(49,'角色修改','system:role:edit',3,3,'','','',3,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(50,'角色删除','system:role:delete',3,3,'','','',4,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(51,'权限查询','system:permission:query',3,4,'','','',1,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(52,'权限创建','system:permission:create',3,4,'','','',2,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(53,'权限修改','system:permission:edit',3,4,'','','',3,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(54,'权限删除','system:permission:delete',3,4,'','','',4,1,'2025-07-14 02:23:46','2025-07-14 02:23:46'),(58,'学分转换','credit:conversion',2,5,'/credit/conversion','credit/CreditConversion','RefreshRight',4,1,'2025-07-14 03:13:38','2025-07-14 03:13:38'),(59,'统计概览','credit:statistics',2,5,'/credit/statistics','credit/CreditStatistics','PieChart',5,1,'2025-07-14 03:13:38','2025-07-14 03:13:38'),(60,'培训项目','course:training',2,12,'/course/training','course/TrainingProgram','School',2,1,'2025-07-14 03:13:38','2025-07-14 03:13:38');
/*!40000 ALTER TABLE `sys_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '瑙掕壊ID',
  `role_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '瑙掕壊鍚嶇О',
  `role_key` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '瑙掕壊鏉冮檺瀛楃?涓',
  `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '瑙掕壊鎻忚堪',
  `status` tinyint DEFAULT '1' COMMENT '鐘舵?锛?-绂佺敤锛?-鍚?敤',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_name` (`role_name`),
  UNIQUE KEY `role_key` (`role_key`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='瑙掕壊琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'Super Admin','admin','System super administrator with all permissions.',1,'2025-07-13 00:23:18','2025-07-14 02:11:21'),(2,'Teacher','teacher','Teacher role, can manage courses and resources',1,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(3,'Student','student','Student role, can learn courses and resources',1,'2025-07-13 00:23:18','2025-07-13 00:23:18');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_permission`
--

DROP TABLE IF EXISTS `sys_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_permission` (
  `role_id` bigint NOT NULL COMMENT '瑙掕壊ID',
  `permission_id` bigint NOT NULL COMMENT '鏉冮檺ID',
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `permission_id` (`permission_id`),
  CONSTRAINT `sys_role_permission_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE CASCADE,
  CONSTRAINT `sys_role_permission_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `sys_permission` (`permission_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='瑙掕壊鏉冮檺鍏宠仈琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_permission`
--

LOCK TABLES `sys_role_permission` WRITE;
/*!40000 ALTER TABLE `sys_role_permission` DISABLE KEYS */;
INSERT INTO `sys_role_permission` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(2,5),(3,5),(1,6),(2,6),(3,6),(1,7),(2,7),(3,7),(1,8),(2,8),(3,8),(1,9),(2,9),(3,9),(1,10),(2,10),(3,10),(1,11),(2,11),(3,11),(1,12),(2,12),(3,12),(1,13),(2,13),(3,13),(1,14),(2,14),(3,14),(1,15),(2,15),(3,15),(1,16),(2,16),(3,16),(1,17),(2,17),(1,18),(2,18),(1,19),(2,19),(1,20),(2,20),(1,21),(2,21),(3,21),(1,22),(2,22),(3,22),(1,23),(2,23),(3,23),(1,24),(2,24),(1,25),(2,25),(1,26),(2,26),(1,27),(2,27),(3,27),(1,28),(1,29),(2,29),(3,29),(1,30),(1,31),(2,31),(3,31),(1,32),(3,32),(1,33),(2,33),(1,34),(2,34),(3,34),(1,35),(2,35),(1,36),(2,36),(3,36),(1,37),(2,37),(1,38),(2,38),(1,39),(2,39),(3,39),(1,40),(2,40),(1,41),(2,41),(1,42),(2,42),(1,43),(2,43),(1,44),(1,45),(1,46),(1,47),(1,48),(1,49),(1,50),(1,51),(1,52),(1,53),(1,54),(1,58),(2,58),(1,59),(2,59),(1,60),(2,60),(3,60);
/*!40000 ALTER TABLE `sys_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '鐢ㄦ埛ID',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '鐢ㄦ埛鍚',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '瀵嗙爜(MD5鍔犲瘑)',
  `real_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '鐪熷疄濮撳悕',
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '閭??',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '鎵嬫満鍙',
  `avatar` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '澶村儚URL',
  `gender` tinyint DEFAULT '0' COMMENT '鎬у埆锛?-鏈?煡锛?-鐢凤紝2-濂',
  `birth_date` date DEFAULT NULL COMMENT '鍑虹敓鏃ユ湡',
  `status` tinyint DEFAULT '1' COMMENT '鐘舵?锛?-绂佺敤锛?-鍚?敤',
  `last_login_time` datetime DEFAULT NULL COMMENT '鏈?悗鐧诲綍鏃堕棿',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_user_username` (`username`),
  KEY `idx_user_email` (`email`),
  KEY `idx_user_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='鐢ㄦ埛琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','0192023a7bbd73250516f069df18b500','Administrator','admin@bankanalysis.com','18888888888','/api/file/avatars/avatar_b94f0ffb-27dd-4766-b264-0e6532372738.jpg',1,NULL,1,'2025-07-14 10:33:29','2025-07-13 00:23:18','2025-07-14 10:33:29'),(2,'teacher1','e10adc3949ba59abbe56e057f20f883e','Prof. Zhang','teacher1@bankanalysis.com','18888888801',NULL,1,NULL,1,'2025-07-14 03:23:50','2025-07-13 00:23:18','2025-07-14 03:23:50'),(3,'teacher2','e10adc3949ba59abbe56e057f20f883e','Teacher Li','teacher2@bankanalysis.com','18888888802',NULL,2,NULL,1,NULL,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(4,'student1','e10adc3949ba59abbe56e057f20f883e','John Wang','student1@example.com','13800138001',NULL,1,'2000-01-15',1,'2025-07-14 03:22:42','2025-07-13 00:23:18','2025-07-14 03:22:42'),(5,'student2','e10adc3949ba59abbe56e057f20f883e','Mary Li','student2@example.com','13800138002',NULL,2,'2001-03-20',1,NULL,'2025-07-13 00:23:18','2025-07-13 00:23:18'),(6,'testuser2','e10adc3949ba59abbe56e057f20f883e','测试用户2','test2@example.com','13900139000',NULL,1,NULL,1,NULL,'2025-07-13 15:44:14','2025-07-13 15:44:14'),(7,'SAged','0192023a7bbd73250516f069df18b500','SA','18011185053@163.com','18011185053',NULL,0,NULL,1,NULL,'2025-07-13 15:45:26','2025-07-13 15:45:26');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '鐢ㄦ埛ID',
  `role_id` bigint NOT NULL COMMENT '瑙掕壊ID',
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `sys_user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `sys_user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='鐢ㄦ埛瑙掕壊鍏宠仈琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1),(2,2),(3,2),(4,3),(5,3);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_log`
--

DROP TABLE IF EXISTS `system_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_log` (
  `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '鏃ュ織ID',
  `log_level` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '鏃ュ織绾у埆锛欴EBUG銆両NFO銆乄ARN銆丒RROR',
  `log_message` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '鏃ュ織娑堟伅',
  `class_name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '绫诲悕',
  `method_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '鏂规硶鍚',
  `line_number` int DEFAULT NULL COMMENT '琛屽彿',
  `thread_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '绾跨▼鍚',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`log_id`),
  KEY `idx_system_log_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='绯荤粺鏃ュ織琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_log`
--

LOCK TABLES `system_log` WRITE;
/*!40000 ALTER TABLE `system_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `training_feedback`
--

DROP TABLE IF EXISTS `training_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `training_feedback` (
  `feedback_id` bigint NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
  `program_id` bigint NOT NULL COMMENT '培训项目ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `rating` int DEFAULT '0' COMMENT '评分(1-5)',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '反馈内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`feedback_id`),
  KEY `idx_program` (`program_id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='培训反馈表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `training_feedback`
--

LOCK TABLES `training_feedback` WRITE;
/*!40000 ALTER TABLE `training_feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `training_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `training_participant`
--

DROP TABLE IF EXISTS `training_participant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `training_participant` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '参与记录ID',
  `program_id` bigint NOT NULL COMMENT '培训项目ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `status` int DEFAULT '0' COMMENT '参与状态：0-已报名，1-已确认，2-已完成，3-已取消',
  `enroll_time` datetime DEFAULT NULL COMMENT '报名时间',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认时间',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_program_user` (`program_id`,`user_id`),
  KEY `idx_program` (`program_id`),
  KEY `idx_user` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='培训参与者表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `training_participant`
--

LOCK TABLES `training_participant` WRITE;
/*!40000 ALTER TABLE `training_participant` DISABLE KEYS */;
INSERT INTO `training_participant` VALUES (1,1,1,3,'2025-07-13 13:33:50',NULL,NULL,NULL,'2025-07-13 13:33:50','2025-07-13 13:48:40'),(2,1,2,1,'2025-07-12 13:33:50','2025-07-13 13:33:50',NULL,NULL,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(3,1,3,2,'2025-07-11 13:33:50','2025-07-12 13:33:50','2025-07-13 13:33:50','表现优秀','2025-07-13 13:33:50','2025-07-13 13:33:50'),(4,1,4,3,'2025-07-10 13:33:50',NULL,NULL,'个人原因取消','2025-07-13 13:33:50','2025-07-13 13:33:50'),(5,2,2,0,'2025-07-13 13:33:50',NULL,NULL,NULL,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(6,2,3,0,'2025-07-13 13:33:50',NULL,NULL,NULL,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(7,2,5,0,'2025-07-13 13:33:50',NULL,NULL,NULL,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(8,3,1,3,'2025-07-11 13:33:50','2025-07-13 13:33:50',NULL,NULL,'2025-07-13 13:33:50','2025-07-13 14:06:17'),(9,3,3,1,'2025-07-11 13:33:50','2025-07-13 13:33:50',NULL,NULL,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(10,3,5,1,'2025-07-11 13:33:50','2025-07-13 13:33:50',NULL,NULL,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(11,3,6,0,'2025-07-13 13:33:50',NULL,NULL,NULL,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(12,4,4,0,'2025-07-13 13:33:50',NULL,NULL,NULL,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(13,4,5,0,'2025-07-13 13:33:50',NULL,NULL,NULL,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(14,4,6,0,'2025-07-13 13:33:50',NULL,NULL,NULL,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(15,5,1,0,'2025-07-13 13:33:50',NULL,NULL,NULL,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(16,5,2,0,'2025-07-13 13:33:50',NULL,NULL,NULL,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(17,5,7,0,'2025-07-13 13:33:50',NULL,NULL,NULL,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(18,6,3,0,'2025-07-13 13:33:50',NULL,NULL,NULL,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(19,6,5,0,'2025-07-13 13:33:50',NULL,NULL,NULL,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(20,6,8,0,'2025-07-13 13:33:50',NULL,NULL,NULL,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(21,7,1,3,'2025-07-13 14:21:47',NULL,NULL,NULL,'2025-07-13 14:21:47','2025-07-13 14:21:50');
/*!40000 ALTER TABLE `training_participant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `training_program`
--

DROP TABLE IF EXISTS `training_program`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `training_program` (
  `program_id` bigint NOT NULL AUTO_INCREMENT COMMENT '培训项目ID',
  `program_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '培训项目名称',
  `program_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '培训项目编码',
  `program_type` int DEFAULT '1' COMMENT '项目类型：1-线上培训，2-线下培训，3-混合培训',
  `manager_id` bigint DEFAULT NULL COMMENT '培训负责人ID',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '培训项目描述',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '培训内容大纲',
  `credit_value` decimal(5,2) DEFAULT '0.00' COMMENT '学分值',
  `cost` decimal(10,2) DEFAULT '0.00' COMMENT '培训费用',
  `max_participants` int DEFAULT '0' COMMENT '最大参与人数',
  `current_participants` int DEFAULT '0' COMMENT '当前参与人数',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `location` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地点（线下或混合培训）',
  `status` int DEFAULT '0' COMMENT '状态：0-未开始，1-进行中，2-已结束，3-已取消',
  `enroll_deadline` datetime DEFAULT NULL COMMENT '报名截止时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`program_id`),
  KEY `idx_program_name` (`program_name`),
  KEY `idx_program_type` (`program_type`),
  KEY `idx_status` (`status`),
  KEY `idx_manager` (`manager_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='培训项目表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `training_program`
--

LOCK TABLES `training_program` WRITE;
/*!40000 ALTER TABLE `training_program` DISABLE KEYS */;
INSERT INTO `training_program` VALUES (1,'Spring Boot高级开发','SB2024001',1,1,'本课程将深入介绍Spring Boot框架的高级特性，帮助开发人员提升开发效率和系统性能。','1. Spring Boot核心原理\n2. 自动配置原理与定制\n3. 响应式编程\n4. WebFlux框架\n5. 安全与认证\n6. 性能优化\n7. 微服务架构整合',2.00,0.00,50,2,'2024-10-01 09:00:00','2024-10-05 17:00:00',NULL,2,'2024-09-25 23:59:59','2025-07-13 13:33:50','2025-07-13 13:48:40'),(2,'云原生架构实战','CN2024002',1,2,'本培训将介绍云原生架构的设计原则和最佳实践，包括容器化、微服务、DevOps等核心概念。','1. 云原生概念与架构\n2. Docker容器化技术\n3. Kubernetes编排平台\n4. 微服务设计模式\n5. CI/CD流水线构建\n6. 可观测性与监控\n7. 案例分析与实战',3.00,500.00,30,3,'2024-10-15 09:00:00','2024-10-20 17:00:00',NULL,0,'2024-10-10 23:59:59','2025-07-13 13:33:50','2025-07-13 13:33:50'),(3,'人工智能与大模型应用','AI2024003',2,3,'本次培训将探讨人工智能大模型的最新发展和企业实际应用场景，帮助技术团队了解如何有效利用AI技术。','1. AI大模型发展现状\n2. 大模型原理与架构\n3. 企业应用场景分析\n4. 开发框架与工具介绍\n5. 模型调优与部署\n6. 案例分享\n7. 实操演练',1.50,1000.00,20,3,'2024-09-20 09:00:00','2024-09-21 17:00:00','北京市海淀区科技园区12号楼3层多功能厅',0,'2024-09-15 23:59:59','2025-07-13 13:33:50','2025-07-13 14:06:17'),(4,'敏捷项目管理工作坊','PM2024004',2,4,'本工作坊将通过实际案例和互动练习，帮助项目经理和团队领导掌握敏捷项目管理的核心方法和技巧。','1. 敏捷宣言与原则\n2. Scrum框架详解\n3. 需求管理与用户故事\n4. 敏捷估算与规划\n5. 团队建设与协作\n6. 持续改进与回顾\n7. 敏捷工具应用',1.00,800.00,25,3,'2024-09-25 09:00:00','2024-09-26 17:00:00','上海市浦东新区张江高科技园区18号楼2层培训室',0,'2024-09-20 23:59:59','2025-07-13 13:33:50','2025-07-13 13:33:50'),(5,'全栈开发实战训练营','FS2024005',3,5,'本训练营结合线上课程和线下实操，全面提升学员的全栈开发能力，从前端到后端，从设计到部署全覆盖。','1. 现代前端技术栈(Vue/React)\n2. 后端开发与API设计\n3. 数据库设计与优化\n4. 云服务与部署\n5. 性能优化\n6. 安全与测试\n7. 项目实战',5.00,2000.00,30,3,'2024-11-01 09:00:00','2024-12-15 17:00:00','广州市天河区科技园B区5号楼(线下部分)',0,'2024-10-25 23:59:59','2025-07-13 13:33:50','2025-07-13 13:33:50'),(6,'数据分析与商业智能','DA2024006',3,6,'本课程将帮助学员掌握数据分析和商业智能的核心技能，结合线上理论学习和线下案例研讨，提升数据驱动决策能力。','1. 数据分析基础\n2. 统计学原理\n3. 数据可视化技巧\n4. 商业智能工具应用\n5. 预测分析模型\n6. 数据报告与决策\n7. 行业案例分析',2.50,1500.00,40,3,'2024-10-10 09:00:00','2024-11-20 17:00:00','深圳市南山区科技园C区3号楼(线下部分)',0,'2024-10-05 23:59:59','2025-07-13 13:33:50','2025-07-13 13:33:50'),(7,'JavaEE','CSCS',1,NULL,'test','',1.00,0.00,30,0,'2025-07-18 00:00:00','2025-07-26 00:00:00','',0,'2025-07-15 00:00:00','2025-07-13 14:21:41','2025-07-13 14:21:50');
/*!40000 ALTER TABLE `training_program` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `training_resource`
--

DROP TABLE IF EXISTS `training_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `training_resource` (
  `resource_id` bigint NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `program_id` bigint NOT NULL COMMENT '培训项目ID',
  `resource_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资源名称',
  `resource_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资源类型',
  `file_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件URL',
  `file_size` bigint DEFAULT '0' COMMENT '文件大小(KB)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`resource_id`),
  KEY `idx_program` (`program_id`),
  KEY `idx_resource_type` (`resource_type`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='培训资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `training_resource`
--

LOCK TABLES `training_resource` WRITE;
/*!40000 ALTER TABLE `training_resource` DISABLE KEYS */;
INSERT INTO `training_resource` VALUES (1,1,'Spring Boot高级开发课程大纲','pdf','/uploads/resources/sb2024001/outline.pdf',1024,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(2,1,'Spring Boot原理详解','pdf','/uploads/resources/sb2024001/principle.pdf',2048,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(3,1,'响应式编程示例代码','zip','/uploads/resources/sb2024001/reactive-demo.zip',5120,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(4,1,'课程视频第一讲','video','/uploads/resources/sb2024001/video-01.mp4',51200,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(5,2,'云原生架构概述','ppt','/uploads/resources/cn2024002/overview.pptx',3072,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(6,2,'Docker实践指南','pdf','/uploads/resources/cn2024002/docker-guide.pdf',4096,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(7,2,'Kubernetes入门到精通','pdf','/uploads/resources/cn2024002/k8s-mastery.pdf',8192,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(8,2,'微服务架构设计模式','pdf','/uploads/resources/cn2024002/microservice-patterns.pdf',6144,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(9,3,'AI大模型原理','ppt','/uploads/resources/ai2024003/llm-principles.pptx',5120,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(10,3,'OpenAI API使用指南','pdf','/uploads/resources/ai2024003/openai-guide.pdf',2048,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(11,3,'企业AI应用案例集','pdf','/uploads/resources/ai2024003/enterprise-cases.pdf',4096,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(12,4,'敏捷宣言与原则','ppt','/uploads/resources/pm2024004/agile-manifesto.pptx',1536,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(13,4,'Scrum指南2023版','pdf','/uploads/resources/pm2024004/scrum-guide-2023.pdf',1024,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(14,4,'用户故事编写模板','docx','/uploads/resources/pm2024004/user-story-template.docx',512,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(15,4,'敏捷估算工具包','zip','/uploads/resources/pm2024004/estimation-toolkit.zip',2048,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(16,5,'Vue3完全指南','pdf','/uploads/resources/fs2024005/vue3-guide.pdf',5120,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(17,5,'Node.js后端开发实践','pdf','/uploads/resources/fs2024005/nodejs-backend.pdf',4096,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(18,5,'数据库设计最佳实践','pdf','/uploads/resources/fs2024005/database-design.pdf',3072,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(19,5,'全栈项目源码','zip','/uploads/resources/fs2024005/fullstack-project.zip',10240,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(20,5,'课程视频合集','video','/uploads/resources/fs2024005/video-series.mp4',102400,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(21,6,'数据分析基础','ppt','/uploads/resources/da2024006/data-analysis-basics.pptx',2048,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(22,6,'Python数据分析实战','pdf','/uploads/resources/da2024006/python-data-analysis.pdf',4096,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(23,6,'数据可视化技巧','pdf','/uploads/resources/da2024006/data-visualization.pdf',3584,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(24,6,'案例数据集','csv','/uploads/resources/da2024006/case-dataset.csv',8192,'2025-07-13 13:33:50','2025-07-13 13:33:50'),(25,6,'Tableau使用指南','pdf','/uploads/resources/da2024006/tableau-guide.pdf',2560,'2025-07-13 13:33:50','2025-07-13 13:33:50');
/*!40000 ALTER TABLE `training_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_favorite`
--

DROP TABLE IF EXISTS `user_favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_favorite` (
  `favorite_id` bigint NOT NULL AUTO_INCREMENT COMMENT '鏀惰棌ID',
  `user_id` bigint NOT NULL COMMENT '鐢ㄦ埛ID',
  `resource_id` bigint NOT NULL COMMENT '璧勬簮ID',
  `favorite_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'resource' COMMENT '鏀惰棌绫诲瀷',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`favorite_id`),
  UNIQUE KEY `uk_user_resource` (`user_id`,`resource_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_resource_id` (`resource_id`),
  CONSTRAINT `user_favorite_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `user_favorite_ibfk_2` FOREIGN KEY (`resource_id`) REFERENCES `learning_resource` (`resource_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='鐢ㄦ埛鏀惰棌琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_favorite`
--

LOCK TABLES `user_favorite` WRITE;
/*!40000 ALTER TABLE `user_favorite` DISABLE KEYS */;
INSERT INTO `user_favorite` VALUES (4,1,4,'resource','2025-07-13 14:20:37');
/*!40000 ALTER TABLE `user_favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_learning_track`
--

DROP TABLE IF EXISTS `user_learning_track`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_learning_track` (
  `track_id` bigint NOT NULL AUTO_INCREMENT COMMENT '杞ㄨ抗ID',
  `user_id` bigint NOT NULL COMMENT '鐢ㄦ埛ID',
  `resource_id` bigint NOT NULL COMMENT '璧勬簮ID',
  `action_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '琛屼负绫诲瀷锛歷iew-娴忚?锛宒ownload-涓嬭浇锛宖avorite-鏀惰棌锛宑omplete-瀹屾垚',
  `progress` int DEFAULT '0' COMMENT '瀛︿範杩涘害鐧惧垎姣',
  `duration_minutes` int DEFAULT '0' COMMENT '瀛︿範鏃堕暱(鍒嗛挓)',
  `score` decimal(5,2) DEFAULT NULL COMMENT '瀛︿範寰楀垎',
  `status` tinyint DEFAULT '1' COMMENT '鐘舵?锛?-鍒犻櫎锛?-姝ｅ父',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  PRIMARY KEY (`track_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_resource_id` (`resource_id`),
  KEY `idx_action_type` (`action_type`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `user_learning_track_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `user_learning_track_ibfk_2` FOREIGN KEY (`resource_id`) REFERENCES `learning_resource` (`resource_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='鐢ㄦ埛瀛︿範杞ㄨ抗琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_learning_track`
--

LOCK TABLES `user_learning_track` WRITE;
/*!40000 ALTER TABLE `user_learning_track` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_learning_track` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-14 10:35:38
