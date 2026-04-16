-- MySQL dump 10.13  Distrib 9.5.0, for macos15 (arm64)
--
-- Host: classdb2.csc.ncsu.edu    Database: akhanda2
-- ------------------------------------------------------
-- Server version	5.5.5-10.5.22-MariaDB

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
-- Table structure for table `ARTICLE`
--

DROP TABLE IF EXISTS `ARTICLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ARTICLE` (
  `issue_id` int(11) NOT NULL,
  `article_number` int(11) NOT NULL,
  `title` varchar(150) NOT NULL,
  `person_id` int(11) NOT NULL,
  `topic` varchar(100) DEFAULT NULL,
  `date_written` date DEFAULT NULL,
  `full_text` text DEFAULT NULL,
  PRIMARY KEY (`issue_id`,`article_number`),
  KEY `person_id` (`person_id`),
  CONSTRAINT `article_ibfk_1` FOREIGN KEY (`issue_id`) REFERENCES `ISSUE` (`issue_id`) ON DELETE CASCADE,
  CONSTRAINT `article_ibfk_2` FOREIGN KEY (`person_id`) REFERENCES `PERSON` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ARTICLE`
--

LOCK TABLES `ARTICLE` WRITE;
/*!40000 ALTER TABLE `ARTICLE` DISABLE KEYS */;
INSERT INTO `ARTICLE` VALUES (101,1,'AI in 2026',105,'Artificial Intelligence','2026-01-20','Artificial intelligence systems in 2026 operate at unprecedented scale, integrating multimodal inputs and real-time decision making. Rapid advances in model efficiency have made AI deployment feasible across industries.'),(102,1,'Quantum Computing Basics',106,'Quantum Computing','2026-01-25','Quantum computing departs fundamentally from classical computation by leveraging superposition and entanglement. Even small quantum systems demonstrate behaviors that have no classical equivalent.'),(102,2,'Data Privacy in Practice',107,'Cybersecurity','2026-01-20','Organizations increasingly face the challenge of protecting user data while maintaining usability and performance. Regulatory frameworks now require concrete safeguards and transparent data-handling practices.'),(103,1,'Edge AI Applications',108,'Artificial Intelligence','2026-02-01','Deploying AI models on edge devices reduces latency and enhances privacy by processing data locally. Advances in hardware acceleration have made on-device inference more practical than ever before.'),(104,1,'Election Trends Worldwide',109,'International Politics','2025-12-15','Recent elections across multiple regions reveal shifting voter priorities and evolving campaign strategies. Digital platforms now play a decisive role in shaping public discourse.'),(105,1,'Energy Policy Updates',110,'Public Policy','2026-01-05','Global energy markets continue to respond to geopolitical pressures and climate commitments. Policymakers are balancing economic growth with long-term sustainability goals.');
/*!40000 ALTER TABLE `ARTICLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ASSIGNED_TO`
--

DROP TABLE IF EXISTS `ASSIGNED_TO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ASSIGNED_TO` (
  `publication_id` int(11) NOT NULL,
  `person_id` int(11) NOT NULL,
  PRIMARY KEY (`publication_id`,`person_id`),
  KEY `person_id` (`person_id`),
  CONSTRAINT `assigned_to_ibfk_1` FOREIGN KEY (`publication_id`) REFERENCES `PUBLICATION` (`publication_id`) ON DELETE CASCADE,
  CONSTRAINT `assigned_to_ibfk_2` FOREIGN KEY (`person_id`) REFERENCES `PERSON` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ASSIGNED_TO`
--

LOCK TABLES `ASSIGNED_TO` WRITE;
/*!40000 ALTER TABLE `ASSIGNED_TO` DISABLE KEYS */;
INSERT INTO `ASSIGNED_TO` VALUES (101,101),(102,101),(102,102),(103,103);
/*!40000 ALTER TABLE `ASSIGNED_TO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BOOKS`
--

DROP TABLE IF EXISTS `BOOKS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BOOKS` (
  `BOOK_TITLE` varchar(32) DEFAULT NULL,
  `ID` int(11) DEFAULT NULL,
  `PRICE` float DEFAULT NULL,
  `AVAILABLE` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BOOKS`
--

LOCK TABLES `BOOKS` WRITE;
/*!40000 ALTER TABLE `BOOKS` DISABLE KEYS */;
INSERT INTO `BOOKS` VALUES ('All about DBMS',17,13.49,5),('Jack the Ripper',13,9.99,1),('Queen Lucia',72,5.99,0),('A Calendar of Sonnets',101,3.49,15),('Napoleon and Blucher',5,9.99,0);
/*!40000 ALTER TABLE `BOOKS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BOOK_EDITION`
--

DROP TABLE IF EXISTS `BOOK_EDITION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BOOK_EDITION` (
  `edition_id` int(11) NOT NULL,
  `publication_id` int(11) NOT NULL,
  `edition_number` int(11) NOT NULL,
  `ISBN` varchar(20) DEFAULT NULL,
  `publication_date` date DEFAULT NULL,
  PRIMARY KEY (`edition_id`),
  UNIQUE KEY `publication_id` (`publication_id`,`edition_number`),
  UNIQUE KEY `ISBN` (`ISBN`),
  CONSTRAINT `book_edition_ibfk_1` FOREIGN KEY (`publication_id`) REFERENCES `PUBLICATION` (`publication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BOOK_EDITION`
--

LOCK TABLES `BOOK_EDITION` WRITE;
/*!40000 ALTER TABLE `BOOK_EDITION` DISABLE KEYS */;
INSERT INTO `BOOK_EDITION` VALUES (101,101,1,'9781458300001','2025-03-15'),(102,101,2,'9781458300002','2026-02-01');
/*!40000 ALTER TABLE `BOOK_EDITION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CATS`
--

DROP TABLE IF EXISTS `CATS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CATS` (
  `CNAME` varchar(20) DEFAULT NULL,
  `TYPE` varchar(30) DEFAULT NULL,
  `AGE` int(11) DEFAULT NULL,
  `WEIGHT` float DEFAULT NULL,
  `SEX` char(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CATS`
--

LOCK TABLES `CATS` WRITE;
/*!40000 ALTER TABLE `CATS` DISABLE KEYS */;
INSERT INTO `CATS` VALUES ('Oscar','Egyptian Mau',3,23.4,'F'),('Max','Turkish Van Cats',2,21.8,'M'),('Tiger','Russian Blue',1,13.3,'M'),('Sam','Persian Cats',5,24.3,'M'),('Simba','Americal Bobtail',3,19.8,'F'),('Lucy','Turkish Angora Cats',2,22.4,'F');
/*!40000 ALTER TABLE `CATS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CHAPTER`
--

DROP TABLE IF EXISTS `CHAPTER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CHAPTER` (
  `edition_id` int(11) NOT NULL,
  `chapter_number` int(11) NOT NULL,
  `title` varchar(150) NOT NULL,
  `person_id` int(11) NOT NULL,
  `topic` varchar(100) DEFAULT NULL,
  `date_written` date DEFAULT NULL,
  `full_text` text DEFAULT NULL,
  PRIMARY KEY (`edition_id`,`chapter_number`),
  KEY `person_id` (`person_id`),
  CONSTRAINT `chapter_ibfk_1` FOREIGN KEY (`edition_id`) REFERENCES `BOOK_EDITION` (`edition_id`) ON DELETE CASCADE,
  CONSTRAINT `chapter_ibfk_2` FOREIGN KEY (`person_id`) REFERENCES `PERSON` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CHAPTER`
--

LOCK TABLES `CHAPTER` WRITE;
/*!40000 ALTER TABLE `CHAPTER` DISABLE KEYS */;
INSERT INTO `CHAPTER` VALUES (101,1,'Ch1 Distributed Transactions',104,'Distributed Systems','2025-03-15','Modern distributed database systems coordinate data across multiple sites, often separated by large geographic distances. Ensuring that a transaction commits consistently at every participating node is one of the central challenges of distributed computing.'),(102,1,'Ch1 Distributed Transactions (Edition 2)',104,'Distributed Systems','2026-02-01','As distributed infrastructures scale to cloud environments, transaction coordination becomes increasingly complex. Newer consensus-based approaches build upon traditional commit protocols to improve fault tolerance and availability.'),(102,2,'Ch2 Data Replication',104,'Data Management','2026-02-01','Data replication allows distributed systems to improve reliability and performance by maintaining multiple copies of data. Different replication strategies, however, introduce trade-offs between consistency, latency, and availability.');
/*!40000 ALTER TABLE `CHAPTER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DISTRIBUTOR`
--

DROP TABLE IF EXISTS `DISTRIBUTOR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DISTRIBUTOR` (
  `distributor_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `category` varchar(50) NOT NULL,
  `street` varchar(150) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `zip` varchar(20) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `contact_person` varchar(100) DEFAULT NULL,
  `opening_balance` decimal(10,2) DEFAULT 0.00,
  PRIMARY KEY (`distributor_id`),
  CONSTRAINT `distributor_chk_1` CHECK (`category` in (_utf8mb4'Bookstore',_utf8mb4'Wholesale',_utf8mb4'Library'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DISTRIBUTOR`
--

LOCK TABLES `DISTRIBUTOR` WRITE;
/*!40000 ALTER TABLE `DISTRIBUTOR` DISABLE KEYS */;
INSERT INTO `DISTRIBUTOR` VALUES (101,'Triangle Books','Bookstore','142 Hillsborough Street','Raleigh','NC','27603','USA','9195552101','Laura Thompson',4850.00),(102,'Eastern Academic Wholesale','Wholesale','850 Logistics Parkway','Charlotte','NC','28208','USA','7045557782','Mark Reynolds',12300.00),(103,'Wake County Public Library','Library','336 Fayetteville Street','Raleigh','NC','27601','USA','9195554433','Emily Carter',0.00),(104,'Capitol City Books','Bookstore','91 Market Square','Durham','NC','27701','USA','9195556610','Daniel Wright',2175.50);
/*!40000 ALTER TABLE `DISTRIBUTOR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DISTRIBUTOR_PAYMENT`
--

DROP TABLE IF EXISTS `DISTRIBUTOR_PAYMENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DISTRIBUTOR_PAYMENT` (
  `payment_id` int(11) NOT NULL,
  `distributor_id` int(11) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `payment_date` date NOT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `distributor_id` (`distributor_id`),
  CONSTRAINT `distributor_payment_ibfk_1` FOREIGN KEY (`distributor_id`) REFERENCES `DISTRIBUTOR` (`distributor_id`),
  CONSTRAINT `distributor_payment_chk_1` CHECK (`amount` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DISTRIBUTOR_PAYMENT`
--

LOCK TABLES `DISTRIBUTOR_PAYMENT` WRITE;
/*!40000 ALTER TABLE `DISTRIBUTOR_PAYMENT` DISABLE KEYS */;
INSERT INTO `DISTRIBUTOR_PAYMENT` VALUES (101,101,15000.00,'2026-02-20'),(102,102,26000.00,'2026-02-15'),(103,102,5100.00,'2026-02-20'),(104,103,3520.00,'2026-02-18'),(105,104,3600.00,'2026-01-20'),(106,104,1299.50,'2026-02-10');
/*!40000 ALTER TABLE `DISTRIBUTOR_PAYMENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DIST_ORDER`
--

DROP TABLE IF EXISTS `DIST_ORDER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DIST_ORDER` (
  `order_id` int(11) NOT NULL,
  `distributor_id` int(11) NOT NULL,
  `edition_id` int(11) DEFAULT NULL,
  `issue_id` int(11) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `shipping_cost` decimal(10,2) NOT NULL,
  `required_date` date NOT NULL,
  `billed_amount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `distributor_id` (`distributor_id`),
  KEY `edition_id` (`edition_id`),
  KEY `issue_id` (`issue_id`),
  CONSTRAINT `dist_order_ibfk_1` FOREIGN KEY (`distributor_id`) REFERENCES `DISTRIBUTOR` (`distributor_id`),
  CONSTRAINT `dist_order_ibfk_2` FOREIGN KEY (`edition_id`) REFERENCES `BOOK_EDITION` (`edition_id`),
  CONSTRAINT `dist_order_ibfk_3` FOREIGN KEY (`issue_id`) REFERENCES `ISSUE` (`issue_id`),
  CONSTRAINT `dist_order_chk_1` CHECK (`quantity` > 0),
  CONSTRAINT `dist_order_chk_2` CHECK (`price` >= 0 and `shipping_cost` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DIST_ORDER`
--

LOCK TABLES `DIST_ORDER` WRITE;
/*!40000 ALTER TABLE `DIST_ORDER` DISABLE KEYS */;
INSERT INTO `DIST_ORDER` VALUES (101,104,NULL,104,90,15.00,160.00,'2026-01-03',1510.00),(102,104,NULL,105,75,15.00,140.00,'2026-02-03',1265.00),(103,102,NULL,101,650,12.00,750.00,'2026-02-03',8550.00),(104,102,NULL,102,650,12.00,750.00,'2026-02-11',8550.00),(105,102,NULL,103,500,12.00,600.00,'2026-02-17',6600.00),(106,101,102,NULL,120,85.00,350.00,'2026-02-10',10550.00),(107,103,102,NULL,40,85.00,120.00,'2026-02-12',3520.00);
/*!40000 ALTER TABLE `DIST_ORDER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ISSUE`
--

DROP TABLE IF EXISTS `ISSUE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ISSUE` (
  `issue_id` int(11) NOT NULL,
  `publication_id` int(11) NOT NULL,
  `issue_title` varchar(150) NOT NULL,
  `publication_date` date DEFAULT NULL,
  PRIMARY KEY (`issue_id`),
  KEY `publication_id` (`publication_id`),
  CONSTRAINT `issue_ibfk_1` FOREIGN KEY (`publication_id`) REFERENCES `PUBLICATION` (`publication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ISSUE`
--

LOCK TABLES `ISSUE` WRITE;
/*!40000 ALTER TABLE `ISSUE` DISABLE KEYS */;
INSERT INTO `ISSUE` VALUES (101,102,'Tech Weekly - February 1, 2026','2026-02-01'),(102,102,'Tech Weekly - February 8, 2026','2026-02-08'),(103,102,'Tech Weekly - February 15, 2026','2026-02-15'),(104,103,'Global Affairs Review - January 2026','2026-01-01'),(105,103,'Global Affairs Review - February 2026','2026-02-01');
/*!40000 ALTER TABLE `ISSUE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PAYMENT_ALLOCATION`
--

DROP TABLE IF EXISTS `PAYMENT_ALLOCATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PAYMENT_ALLOCATION` (
  `payment_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `allocated_amount` decimal(10,2) NOT NULL,
  PRIMARY KEY (`payment_id`,`order_id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `payment_allocation_ibfk_1` FOREIGN KEY (`payment_id`) REFERENCES `DISTRIBUTOR_PAYMENT` (`payment_id`) ON DELETE CASCADE,
  CONSTRAINT `payment_allocation_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `DIST_ORDER` (`order_id`),
  CONSTRAINT `payment_allocation_chk_1` CHECK (`allocated_amount` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PAYMENT_ALLOCATION`
--

LOCK TABLES `PAYMENT_ALLOCATION` WRITE;
/*!40000 ALTER TABLE `PAYMENT_ALLOCATION` DISABLE KEYS */;
/*!40000 ALTER TABLE `PAYMENT_ALLOCATION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PERSON`
--

DROP TABLE IF EXISTS `PERSON`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PERSON` (
  `person_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PERSON`
--

LOCK TABLES `PERSON` WRITE;
/*!40000 ALTER TABLE `PERSON` DISABLE KEYS */;
INSERT INTO `PERSON` VALUES (101,'Olivia Bennett'),(102,'Helena Strauss'),(103,'Daniel Whitmore'),(104,'Alice Morgan'),(105,'Sarah Lee'),(106,'Michael Brown'),(107,'Emily Zhang'),(108,'Daniel Kim'),(109,'Laura Martinez'),(110,'Robert Singh');
/*!40000 ALTER TABLE `PERSON` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PUBLICATION`
--

DROP TABLE IF EXISTS `PUBLICATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PUBLICATION` (
  `publication_id` int(11) NOT NULL,
  `title` varchar(150) NOT NULL,
  `type` varchar(50) NOT NULL,
  `periodicity` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`publication_id`),
  CONSTRAINT `publication_chk_1` CHECK (`type` in (_utf8mb4'Book',_utf8mb4'Magazine',_utf8mb4'Journal')),
  CONSTRAINT `publication_chk_2` CHECK (`periodicity` is null or `type` in (_utf8mb4'Magazine',_utf8mb4'Journal'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PUBLICATION`
--

LOCK TABLES `PUBLICATION` WRITE;
/*!40000 ALTER TABLE `PUBLICATION` DISABLE KEYS */;
INSERT INTO `PUBLICATION` VALUES (101,'Foundations of Distributed Databases','Book',NULL),(102,'Tech Weekly','Magazine','Weekly'),(103,'Global Affairs Review','Journal','Monthly');
/*!40000 ALTER TABLE `PUBLICATION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `STAFF_PAYMENT`
--

DROP TABLE IF EXISTS `STAFF_PAYMENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STAFF_PAYMENT` (
  `payment_id` int(11) NOT NULL,
  `person_id` int(11) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `issued_date` date NOT NULL,
  `claimed_date` date DEFAULT NULL,
  `contribution_reference` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `person_id` (`person_id`),
  CONSTRAINT `staff_payment_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `PERSON` (`person_id`),
  CONSTRAINT `staff_payment_chk_1` CHECK (`amount` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `STAFF_PAYMENT`
--

LOCK TABLES `STAFF_PAYMENT` WRITE;
/*!40000 ALTER TABLE `STAFF_PAYMENT` DISABLE KEYS */;
INSERT INTO `STAFF_PAYMENT` VALUES (101,104,2500.00,'2025-03-20','2025-04-05','Ch1 Distributed Transactions'),(102,104,1000.00,'2026-02-05',NULL,'Ch1 Distributed Transactions (Edition 2)'),(103,104,2000.00,'2026-02-10',NULL,'Ch2 Data Replication'),(104,105,1200.00,'2026-01-28','2026-02-03','AI in 2026'),(105,106,1100.00,'2026-01-30',NULL,'Quantum Computing Basics'),(106,107,1150.00,'2026-01-25','2026-02-02','Data Privacy in Practice'),(107,108,1300.00,'2026-02-05',NULL,'Edge AI Applications'),(108,109,1250.00,'2025-12-20','2026-01-05','Election Trends Worldwide'),(109,110,1180.00,'2026-01-10',NULL,'Energy Policy Updates'),(110,101,4000.00,'2025-03-15','2025-04-10','Foundations of Distributed Databases - Edition 1'),(111,101,4000.00,'2026-02-01','2026-02-10','Foundations of Distributed Databases - Edition 2'),(112,101,2500.00,'2026-02-28',NULL,'Tech Weekly - Feb issues'),(113,102,2000.00,'2026-02-28','2026-03-05','Tech Weekly - Feb issues'),(114,103,3000.00,'2026-01-31','2026-02-07','Global Affairs Review - Jan'),(115,103,3000.00,'2026-02-28',NULL,'Global Affairs Review - Feb');
/*!40000 ALTER TABLE `STAFF_PAYMENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Schools`
--

DROP TABLE IF EXISTS `Schools`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Schools` (
  `Name` varchar(10) DEFAULT NULL,
  `Location` varchar(30) DEFAULT NULL,
  `TuitionFees` int(11) DEFAULT NULL,
  `LivingExpenses` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Schools`
--

LOCK TABLES `Schools` WRITE;
/*!40000 ALTER TABLE `Schools` DISABLE KEYS */;
INSERT INTO `Schools` VALUES ('NC State','North Carolina',24000,20000),('Stanford','California',44000,35000),('UNC','North Carolina',34000,20000),('Harvard','Massachusetts',50000,38000),('UCLA','California',36000,30000),('NYU','New York',15000,41000);
/*!40000 ALTER TABLE `Schools` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Students`
--

DROP TABLE IF EXISTS `Students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Students` (
  `Name` varchar(20) DEFAULT NULL,
  `School` varchar(10) DEFAULT NULL,
  `Age` int(11) DEFAULT NULL,
  `FundingReceived` int(11) DEFAULT NULL,
  `Income` int(11) DEFAULT NULL,
  `Sex` char(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Students`
--

LOCK TABLES `Students` WRITE;
/*!40000 ALTER TABLE `Students` DISABLE KEYS */;
INSERT INTO `Students` VALUES ('Todd','NC State',18,16000,30000,'M'),('Max','Stanford',21,20000,70000,'M'),('Alex','UNC',19,8000,40000,'M'),('Natasha','Harvard',22,15000,75000,'F'),('Kelly','UCLA',23,2000,50000,'F'),('Angela','NYU',18,8000,55000,'F');
/*!40000 ALTER TABLE `Students` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-15 14:14:28
