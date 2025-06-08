-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: booking_system_db
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `id` binary(16) NOT NULL,
  `booked_at` datetime(6) DEFAULT NULL,
  `canceled` bit(1) DEFAULT NULL,
  `checked_in` bit(1) DEFAULT NULL,
  `refunded_credit` bit(1) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `class_schedule_id` binary(16) DEFAULT NULL,
  `user_id` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhanufdlsbky5e967jssrm2xr7` (`class_schedule_id`),
  KEY `FKeyog2oic85xg7hsu2je2lx3s6` (`user_id`),
  CONSTRAINT `FKeyog2oic85xg7hsu2je2lx3s6` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKhanufdlsbky5e967jssrm2xr7` FOREIGN KEY (`class_schedule_id`) REFERENCES `class_schedules` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` VALUES (_binary '™´?aMÕ…Î­\É\"\×H','2025-06-08 04:03:18.234894',_binary '',_binary '',_binary '\0','CHECKED_IN',_binary '1\ÚýÉƒFü¡™\Z|ž_F®',_binary ' \ÖIÁfKkD€N\nž>');
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `class_schedules`
--

DROP TABLE IF EXISTS `class_schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `class_schedules` (
  `id` binary(16) NOT NULL,
  `class_name` varchar(255) NOT NULL,
  `completed` bit(1) NOT NULL,
  `country_code` varchar(255) NOT NULL,
  `end_time` datetime(6) NOT NULL,
  `max_slots` int NOT NULL,
  `required_credits` int NOT NULL,
  `start_time` datetime(6) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `class_schedules`
--

LOCK TABLES `class_schedules` WRITE;
/*!40000 ALTER TABLE `class_schedules` DISABLE KEYS */;
INSERT INTO `class_schedules` VALUES (_binary '1\ÚýÉƒFü¡™\Z|ž_F®','Morning Yoga Flow',_binary '\0','SG','2025-06-10 09:00:00.000000',5,2,'2025-06-07 08:00:00.000000',_binary '\0'),(_binary 'ž\0NŸŸ‡›f™¤','Evening Meditation',_binary '\0','SG','2025-06-10 19:00:00.000000',10,1,'2025-06-10 18:00:00.000000',_binary '\0'),(_binary '—8¥-\\lF¡¡4\È\èŒÅ¯','Power Yoga',_binary '\0','MM','2025-06-11 08:00:00.000000',5,3,'2025-06-11 07:00:00.000000',_binary '\0'),(_binary '›A\ã/¦H>†ª\é\ð\ã©','Stretch & Breathe',_binary '\0','MM','2025-06-12 11:00:00.000000',12,1,'2025-06-12 10:00:00.000000',_binary '\0');
/*!40000 ALTER TABLE `class_schedules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `packages`
--

DROP TABLE IF EXISTS `packages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `packages` (
  `id` binary(16) NOT NULL,
  `active` bit(1) NOT NULL,
  `country_code` varchar(2) NOT NULL,
  `credits` int NOT NULL,
  `expiration_date` datetime(6) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `price` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `packages`
--

LOCK TABLES `packages` WRITE;
/*!40000 ALTER TABLE `packages` DISABLE KEYS */;
INSERT INTO `packages` VALUES (_binary '(‘lX¯@¥‰\n‹Fn—K',_binary '','MM',20,'2025-06-10 23:59:59.000000','Wellness Monthly',18.99),(_binary '-\î$1l\ØKÀ‡‰¯¸r5À\ë',_binary '','SG',10,'2025-06-10 23:59:59.000000','Fitness Boost',9.99),(_binary 'p˜\ñ\îiDg‡¼\æ\É\ÒPV',_binary '','SG',5,'2025-06-10 23:59:59.000000','Starter Pack',5),(_binary '†¼¨±ÀEB¬ú‹\ö.s',_binary '','MM',999,'2025-06-11 23:59:59.000000','Unlimited Trial',1);
/*!40000 ALTER TABLE `packages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` binary(16) NOT NULL,
  `description` varchar(512) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_8sewwnpamngi6b1dwaa88askk` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (_binary '\ß?S\ë\"\nD/•,!\ß=ht','User Role','ROLE_USER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_packages`
--

DROP TABLE IF EXISTS `user_packages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_packages` (
  `id` binary(16) NOT NULL,
  `country_code` varchar(255) NOT NULL,
  `deleted` bit(1) DEFAULT NULL,
  `expiration_date` datetime(6) DEFAULT NULL,
  `expired` bit(1) DEFAULT NULL,
  `remaining_credits` int NOT NULL,
  `package_id` binary(16) DEFAULT NULL,
  `user_id` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKamf4ragc34vb3il9wxrwjxw7b` (`package_id`),
  KEY `FKlhj6dofmpikc4fp9nf3ciksdq` (`user_id`),
  CONSTRAINT `FKamf4ragc34vb3il9wxrwjxw7b` FOREIGN KEY (`package_id`) REFERENCES `packages` (`id`),
  CONSTRAINT `FKlhj6dofmpikc4fp9nf3ciksdq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_packages`
--

LOCK TABLES `user_packages` WRITE;
/*!40000 ALTER TABLE `user_packages` DISABLE KEYS */;
INSERT INTO `user_packages` VALUES (_binary '2\ëþ\ËJ¹‚†¯\Çy*','MM',_binary '\0','2025-06-11 23:59:59.000000',_binary '\0',999,_binary '†¼¨±ÀEB¬ú‹\ö.s',_binary '\Û\Ï-\ð_†DÂ½³X0>\ÒDú'),(_binary 'Prˆ¸,E-™\ö\ä·\é‡z4','SG',_binary '\0','2025-06-10 23:59:59.000000',_binary '\0',5,_binary 'p˜\ñ\îiDg‡¼\æ\É\ÒPV',_binary ' \ÖIÁfKkD€N\nž>'),(_binary 'ž<Ù(\ÕB™GÇ²¬PŒ´','MM',_binary '\0','2025-06-10 23:59:59.000000',_binary '\0',20,_binary '(‘lX¯@¥‰\n‹Fn—K',_binary '\Û\Ï-\ð_†DÂ½³X0>\ÒDú'),(_binary '\Í7Õ¸\òYIŒ”\ïº:^µ~±','SG',_binary '\0','2025-06-10 23:59:59.000000',_binary '\0',8,_binary '-\î$1l\ØKÀ‡‰¯¸r5À\ë',_binary ' \ÖIÁfKkD€N\nž>');
/*!40000 ALTER TABLE `user_packages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` binary(16) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `country_code` varchar(2) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `email_verified` bit(1) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `verification_code` varchar(255) DEFAULT NULL,
  `role_id` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`),
  KEY `FK4qu1gr772nnf6ve5af002rwya` (`role_id`),
  CONSTRAINT `FK4qu1gr772nnf6ve5af002rwya` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (_binary ' \ÖIÁfKkD€N\nž>',_binary '','SG','2025-06-08 03:10:15.189625',NULL,'user@gmail.com',_binary '','$2a$10$EtOXRkeQRRb7LKbmGLWDh.MSFGvs.U4K6o0.8i.uHhXAVEYlTGC8C','2025-06-08 03:11:04.844370','User',NULL,_binary '\ß?S\ë\"\nD/•,!\ß=ht'),(_binary '\Û\Ï-\ð_†DÂ½³X0>\ÒDú',_binary '','MM','2025-06-08 03:14:13.075141',NULL,'user1@gmail.com',_binary '','$2a$10$4GFShGZosBNYh/BuuMziiO6B8MgUsxWLIOFMttcwXLMRjBgO8pybG','2025-06-08 03:14:37.040467','User1',NULL,_binary '\ß?S\ë\"\nD/•,!\ß=ht');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `waitlists`
--

DROP TABLE IF EXISTS `waitlists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `waitlists` (
  `id` binary(16) NOT NULL,
  `added_at` datetime(6) DEFAULT NULL,
  `refunded` bit(1) NOT NULL,
  `class_schedule_id` binary(16) DEFAULT NULL,
  `user_id` binary(16) DEFAULT NULL,
  `user_package_id` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1ob68kh9jcx72a2rwtamawo1p` (`class_schedule_id`),
  KEY `FKapgrrvgbi28civjdxv78e7mea` (`user_id`),
  KEY `FKkdwrlbr3hhl7klip9of9nm7os` (`user_package_id`),
  CONSTRAINT `FK1ob68kh9jcx72a2rwtamawo1p` FOREIGN KEY (`class_schedule_id`) REFERENCES `class_schedules` (`id`),
  CONSTRAINT `FKapgrrvgbi28civjdxv78e7mea` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKkdwrlbr3hhl7klip9of9nm7os` FOREIGN KEY (`user_package_id`) REFERENCES `user_packages` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `waitlists`
--

LOCK TABLES `waitlists` WRITE;
/*!40000 ALTER TABLE `waitlists` DISABLE KEYS */;
/*!40000 ALTER TABLE `waitlists` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-08  8:48:43
