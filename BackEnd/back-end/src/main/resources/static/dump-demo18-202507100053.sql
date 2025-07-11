-- MySQL dump 10.13  Distrib 5.7.31, for Win64 (x86_64)
--
-- Host: localhost    Database: demo18
-- ------------------------------------------------------
-- Server version	5.7.31-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `hd_log`
--

DROP TABLE IF EXISTS `hd_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hd_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_user_id` int(11) DEFAULT NULL COMMENT '访问人userId',
  `create_date` datetime DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL COMMENT '访问人ip地址',
  `url` varchar(512) DEFAULT NULL COMMENT '访问url',
  `stay_time` bigint(20) DEFAULT NULL COMMENT '执行时长（毫秒）',
  `method` varchar(255) DEFAULT NULL COMMENT '接口方法',
  `param_val` longtext COMMENT '请求参数',
  `return_val` longtext COMMENT '方法返回值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hd_log`
--

LOCK TABLES `hd_log` WRITE;
/*!40000 ALTER TABLE `hd_log` DISABLE KEYS */;
INSERT INTO `hd_log` VALUES (1,20,'2022-09-15 15:58:04','127.0.0.1',NULL,318,'cn.sopuzi.ssm.controller.LoginController.login','[UserEntity(userId=0, name=null, phone=null, createDate=null, accountName=555, password=555), org.apache.catalina.session.StandardSessionFacade@300722e]','class cn.sopuzi.ssm.utils.ResultMsg'),(2,20,'2022-09-15 16:04:00','127.0.0.1','http://localhost:8080/login',269,'cn.sopuzi.ssm.controller.LoginController.login','[UserEntity(userId=0, name=null, phone=null, createDate=null, accountName=555, password=555), org.apache.catalina.session.StandardSessionFacade@b0e1a74]','class cn.sopuzi.ssm.utils.ResultMsg'),(3,0,'2022-09-15 16:05:21','127.0.0.1','http://localhost:8080/',34,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(4,0,'2022-09-15 16:05:22','127.0.0.1','http://localhost:8080/',1,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(5,20,'2022-09-15 16:05:38','127.0.0.1','http://localhost:8080/login',31,'cn.sopuzi.ssm.controller.LoginController.login','[UserEntity(userId=0, name=null, phone=null, createDate=null, accountName=555, password=555), org.apache.catalina.session.StandardSessionFacade@39be4407]','class cn.sopuzi.ssm.utils.ResultMsg'),(6,20,'2022-09-15 16:05:39','127.0.0.1','http://localhost:8080/user/findUserlist',1,'cn.sopuzi.ssm.controller.UserController.findUserlist','[, 0, 10]','class cn.sopuzi.ssm.utils.ResultMsg'),(7,20,'2022-09-15 16:06:39','127.0.0.1','http://localhost:8080/user',9,'cn.sopuzi.ssm.controller.UserController.user','[]','class java.lang.String'),(8,20,'2022-09-15 16:06:39','127.0.0.1','http://localhost:8080/user/findUserlist',2,'cn.sopuzi.ssm.controller.UserController.findUserlist','[, 0, 10]','class java.lang.String'),(9,20,'2022-09-15 16:06:53','127.0.0.1','http://localhost:8080/user',1,'cn.sopuzi.ssm.controller.UserController.user','[]','class java.lang.String'),(10,20,'2022-09-15 16:06:53','127.0.0.1','http://localhost:8080/user/findUserlist',1,'cn.sopuzi.ssm.controller.UserController.findUserlist','[, 0, 10]','class java.lang.String'),(11,0,'2022-09-15 16:14:48','127.0.0.1','http://localhost:8080/',23,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(12,0,'2022-09-15 16:14:48','127.0.0.1','http://localhost:8080/',1,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(13,20,'2022-09-15 16:14:59','127.0.0.1','http://localhost:8080/login',39,'cn.sopuzi.ssm.controller.LoginController.login','[UserEntity(userId=0, name=null, phone=null, createDate=null, accountName=555, password=555), org.apache.catalina.session.StandardSessionFacade@684af8dd]','class cn.sopuzi.ssm.utils.ResultMsg'),(14,20,'2022-09-15 16:15:00','127.0.0.1','http://localhost:8080/user/findUserlist',232,'cn.sopuzi.ssm.controller.UserController.findUserlist','[, 0, 10]','class cn.sopuzi.ssm.utils.ResultMsg'),(15,0,'2022-09-15 16:19:02','127.0.0.1','http://localhost:8080/',33982,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(16,0,'2022-09-15 16:19:02','127.0.0.1','http://localhost:8080/',33982,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(17,0,'2022-09-15 16:19:03','127.0.0.1','http://localhost:8080/',157,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(18,0,'2022-09-15 16:19:03','127.0.0.1','http://localhost:8080/',179,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(19,20,'2022-09-15 16:19:14','127.0.0.1','http://localhost:8080/login',32,'cn.sopuzi.ssm.controller.LoginController.login','[UserEntity(userId=0, name=null, phone=null, createDate=null, accountName=555, password=555), org.apache.catalina.session.StandardSessionFacade@1da1048e]','class cn.sopuzi.ssm.utils.ResultMsg'),(20,20,'2022-09-15 16:19:15','127.0.0.1','http://localhost:8080/user/findUserlist',200,'cn.sopuzi.ssm.controller.UserController.findUserlist','[, 0, 10]','class cn.sopuzi.ssm.utils.ResultMsg'),(21,0,'2022-09-15 16:23:56','127.0.0.1','http://localhost:8080/',21,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(22,0,'2022-09-15 16:23:56','127.0.0.1','http://localhost:8080/',1,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(23,20,'2022-09-15 16:24:02','127.0.0.1','http://localhost:8080/login',44,'cn.sopuzi.ssm.controller.LoginController.login','[UserEntity(userId=0, name=null, phone=null, createDate=null, accountName=555, password=555), org.apache.catalina.session.StandardSessionFacade@27263a95]','class cn.sopuzi.ssm.utils.ResultMsg'),(24,20,'2022-09-15 16:24:03','127.0.0.1','http://localhost:8080/user/findUserlist',190,'cn.sopuzi.ssm.controller.UserController.findUserlist','[, 0, 10]','class cn.sopuzi.ssm.utils.ResultMsg'),(25,0,'2022-09-15 16:27:24','127.0.0.1','http://localhost:8080/',18,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(26,0,'2022-09-15 16:27:24','127.0.0.1','http://localhost:8080/',0,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(27,20,'2022-09-15 16:27:35','127.0.0.1','http://localhost:8080/login',30,'cn.sopuzi.ssm.controller.LoginController.login','[UserEntity(userId=0, name=null, phone=null, createDate=null, accountName=555, password=555), org.apache.catalina.session.StandardSessionFacade@75197c72]','class cn.sopuzi.ssm.utils.ResultMsg'),(28,20,'2022-09-15 16:27:35','127.0.0.1','http://localhost:8080/user/findUserlist',181,'cn.sopuzi.ssm.controller.UserController.findUserlist','[, 0, 10]','class cn.sopuzi.ssm.utils.ResultMsg'),(29,0,'2022-09-15 16:28:52','127.0.0.1','http://localhost:8080/',34,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(30,0,'2022-09-15 16:28:53','127.0.0.1','http://localhost:8080/',0,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(31,20,'2022-09-15 16:28:58','127.0.0.1','http://localhost:8080/login',38,'cn.sopuzi.ssm.controller.LoginController.login','[UserEntity(userId=0, name=null, phone=null, createDate=null, accountName=555, password=555), org.apache.catalina.session.StandardSessionFacade@28a79f73]','class cn.sopuzi.ssm.utils.ResultMsg'),(32,20,'2022-09-15 16:28:58','127.0.0.1','http://localhost:8080/user/findUserlist',211,'cn.sopuzi.ssm.controller.UserController.findUserlist','[, 0, 10]','class cn.sopuzi.ssm.utils.ResultMsg'),(33,0,'2022-09-15 16:31:03','127.0.0.1','http://localhost:8080/',21,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(34,0,'2022-09-15 16:31:03','127.0.0.1','http://localhost:8080/',1,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(35,0,'2022-09-15 16:33:28','127.0.0.1','http://localhost:8080/',0,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(36,20,'2022-09-15 16:33:35','127.0.0.1','http://localhost:8080/login',42,'cn.sopuzi.ssm.controller.LoginController.login','[UserEntity(userId=0, name=null, phone=null, createDate=null, accountName=555, password=555), org.apache.catalina.session.StandardSessionFacade@35e9156f]','class cn.sopuzi.ssm.utils.ResultMsg'),(37,20,'2022-09-15 16:33:36','127.0.0.1','http://localhost:8080/user/findUserlist',203,'cn.sopuzi.ssm.controller.UserController.findUserlist','[, 0, 10]','class cn.sopuzi.ssm.utils.ResultMsg'),(38,0,'2022-09-15 16:35:27','127.0.0.1','http://localhost:8080/',22,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(39,0,'2022-09-15 16:35:27','127.0.0.1','http://localhost:8080/',1,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(40,0,'2022-09-15 16:35:50','127.0.0.1','http://localhost:8080/',0,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(41,20,'2022-09-15 16:35:55','127.0.0.1','http://localhost:8080/login',47,'cn.sopuzi.ssm.controller.LoginController.login','[UserEntity(userId=0, name=null, phone=null, createDate=null, accountName=555, password=555), org.apache.catalina.session.StandardSessionFacade@3f2e458c]','class cn.sopuzi.ssm.utils.ResultMsg'),(42,20,'2022-09-15 16:35:56','127.0.0.1','http://localhost:8080/user/findUserlist',230,'cn.sopuzi.ssm.controller.UserController.findUserlist','[, 0, 10]','class cn.sopuzi.ssm.utils.ResultMsg'),(43,0,'2022-09-15 16:36:41','127.0.0.1','http://localhost:8080/',22,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(44,0,'2022-09-15 16:36:41','127.0.0.1','http://localhost:8080/',0,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(45,20,'2022-09-15 16:36:47','127.0.0.1','http://localhost:8080/login',47,'cn.sopuzi.ssm.controller.LoginController.login','[UserEntity(userId=0, name=null, phone=null, createDate=null, accountName=555, password=555), org.apache.catalina.session.StandardSessionFacade@1d94a945]','class cn.sopuzi.ssm.utils.ResultMsg'),(46,20,'2022-09-15 16:36:47','127.0.0.1','http://localhost:8080/user/findUserlist',238,'cn.sopuzi.ssm.controller.UserController.findUserlist','[, 0, 10]','class cn.sopuzi.ssm.utils.ResultMsg'),(47,0,'2022-09-15 16:40:50','127.0.0.1','http://localhost:8080/',30,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(48,0,'2022-09-15 16:40:51','127.0.0.1','http://localhost:8080/',1,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(49,0,'2022-09-15 16:41:33','127.0.0.1','http://localhost:8080/',23,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(50,0,'2022-09-15 16:41:33','127.0.0.1','http://localhost:8080/',1,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(51,0,'2022-09-15 16:42:38','127.0.0.1','http://localhost:8080/',33,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String'),(52,0,'2022-09-15 16:42:38','127.0.0.1','http://localhost:8080/',1,'cn.sopuzi.ssm.controller.IndexController.index','[]','class java.lang.String');
/*!40000 ALTER TABLE `hd_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hd_menu`
--

DROP TABLE IF EXISTS `hd_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hd_menu` (
  `menu_id` int(11) NOT NULL,
  `cname` varchar(70) DEFAULT NULL,
  `ename` varchar(70) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `component` varchar(255) DEFAULT NULL,
  `icon` varchar(70) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  `sort` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hd_menu`
--

LOCK TABLES `hd_menu` WRITE;
/*!40000 ALTER TABLE `hd_menu` DISABLE KEYS */;
INSERT INTO `hd_menu` VALUES (1,'首页','homeContent','/','/home/homeContent.vue','el-icon-s-home',0,'1','2022-06-26 10:39:44',NULL),(2,'用户管理','user','/user','/user/index.vue','el-icon-s-custom',0,'2','2022-06-26 10:39:44',NULL),(3,'角色管理','role','/role','/role/index.vue','el-icon-s-release',0,'3','2022-06-26 10:39:44',NULL),(4,'菜单管理','menu','/menu','/menu/index.vue','el-icon-menu',0,'4','2022-06-26 10:39:44',NULL),(5,'商城管理','mall',NULL,NULL,'el-icon-shopping-cart-2',0,'5','2022-06-26 10:39:44',NULL),(6,'订单管理','menuOrder','/mall/order','/mall/order.vue','el-icon-s-operation',5,'6','2022-06-26 10:39:44',NULL),(7,'其他管理','menuTest','/menu/test','/mall/test.vue','el-icon-s-operation',5,'7','2022-06-26 10:39:44',NULL);
/*!40000 ALTER TABLE `hd_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hd_role`
--

DROP TABLE IF EXISTS `hd_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hd_role` (
  `role_id` int(255) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hd_role`
--

LOCK TABLES `hd_role` WRITE;
/*!40000 ALTER TABLE `hd_role` DISABLE KEYS */;
INSERT INTO `hd_role` VALUES (1,'系统管理员',NULL,NULL,NULL),(2,'商城管理员',NULL,NULL,NULL),(3,'普通用户',NULL,NULL,NULL);
/*!40000 ALTER TABLE `hd_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hd_role_menu`
--

DROP TABLE IF EXISTS `hd_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hd_role_menu` (
  `role_id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hd_role_menu`
--

LOCK TABLES `hd_role_menu` WRITE;
/*!40000 ALTER TABLE `hd_role_menu` DISABLE KEYS */;
INSERT INTO `hd_role_menu` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(2,1),(2,5),(2,6),(2,7);
/*!40000 ALTER TABLE `hd_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hd_user`
--

DROP TABLE IF EXISTS `hd_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hd_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `account` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hd_user`
--

LOCK TABLES `hd_user` WRITE;
/*!40000 ALTER TABLE `hd_user` DISABLE KEYS */;
INSERT INTO `hd_user` VALUES (20,'我是超级管理员','13300000000','admin','2022-06-22 10:48:00','842b0839718d6767cbf9bebde8628f5a'),(24,'张三','18888888888','zhangsan','2025-06-12 16:02:47','842b0839718d6767cbf9bebde8628f5a'),(26,'低调','17738875171','didiao','2025-06-16 22:27:34','842b0839718d6767cbf9bebde8628f5a'),(34,'hepeng','17738875171','hepeng','2025-06-17 16:34:28','842b0839718d6767cbf9bebde8628f5a'),(38,'hepen','17738875171','hepen','2025-06-18 11:39:35','842b0839718d6767cbf9bebde8628f5a');
/*!40000 ALTER TABLE `hd_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hd_user_role`
--

DROP TABLE IF EXISTS `hd_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hd_user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hd_user_role`
--

LOCK TABLES `hd_user_role` WRITE;
/*!40000 ALTER TABLE `hd_user_role` DISABLE KEYS */;
INSERT INTO `hd_user_role` VALUES (20,1),(20,2),(21,2);
/*!40000 ALTER TABLE `hd_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'demo18'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-10  0:53:22
