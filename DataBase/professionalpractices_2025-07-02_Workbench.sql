-- MySQL dump 10.13  Distrib 9.0.1, for macos14.7 (arm64)
--
-- Host: localhost    Database: professionalpractices
-- ------------------------------------------------------
-- Server version	9.0.1

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
-- Table structure for table `academic`
--

DROP TABLE IF EXISTS `academic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `academic` (
  `academicID` int NOT NULL AUTO_INCREMENT,
  `personalNumber` varchar(5) NOT NULL,
  `name` varchar(100) NOT NULL,
  `firstSurname` varchar(100) NOT NULL,
  `secondSurname` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` varchar(20) NOT NULL,
  PRIMARY KEY (`academicID`)
) ENGINE=InnoDB AUTO_INCREMENT=347 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `academic`
--

LOCK TABLES `academic` WRITE;
/*!40000 ALTER TABLE `academic` DISABLE KEYS */;
INSERT INTO `academic` VALUES (13,'54321','Ángel Juan','Sánchez','García','angel@uv.mx','angel','f54aa1f214ad2c5296299e91481ea104097ff37581f791f65d69635c6e121677','COORDINADOR'),(16,'120','Juan José Guadalupe','Ladrón de Guevara','Del Perpetuo Socorro','juaperez@uv.mx','juaperez','03c80a3d8cc3b8374812200ef19dafcf7b153ef25157e9060cd3f7fcf3153dec','ACADÉMICO'),(30,'98765','Jerry','Merry','','jerry@gmail.com','jerry2','005e793f4b1bd62252c2a20eecfe8594f58623b000bc640c90c09b395f2fa954','ACADEMICO'),(53,'00001','Albus Percival','Dumbledore','','albus@gmail.com','albus','0bf22086198e82c3c912e8c00c06ecf91eb4ea4c47b25a55ae18d4f5861e4266','COORDINADOR'),(54,'00002','Minerva','McGonagall','','minmcgonagall@gmail.com','minmcgonagall','2fea01dd88ff9d8b2ccda2751c1e8ba04fde294bd0bfa6b30677dfb995c7b57a','ACADEMICO'),(55,'00003','Remus','Lupin','','lunatico@gmail.com','lunatico','2cd585e41aaad6e87a9cdc7734c259c5dc6fe7a5c1a916993dc351072915aa39','ACADEMICO'),(62,'78945','Dolores','Umbridge','','dolores@gmail.com','dolores','e7a6c0ab717a09fac8c25fdf232140f69d93e1d8bcf06293f01948f71f8e7c04','EVALUADOR'),(69,'26140','Juan Carlos','Pérez','Arriaga','elrevo@uv.mx','elrevo','d0b409481e1864edce6df4cd41a3afbfc1454476979372dd1a0bec95aaf458e8','COORDINADOR'),(70,'26140','Juan Carlos','Pérez','Arriaga','elrevo@uv.mx','relevo','d0b409481e1864edce6df4cd41a3afbfc1454476979372dd1a0bec95aaf458e8','EVALUADOR');
/*!40000 ALTER TABLE `academic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AcademicSection`
--

DROP TABLE IF EXISTS `AcademicSection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AcademicSection` (
  `academicID` int NOT NULL,
  `sectionID` int NOT NULL,
  PRIMARY KEY (`academicID`,`sectionID`),
  KEY `sectionID` (`sectionID`),
  CONSTRAINT `academicsection_ibfk_1` FOREIGN KEY (`academicID`) REFERENCES `Academic` (`academicID`) ON DELETE CASCADE,
  CONSTRAINT `academicsection_ibfk_2` FOREIGN KEY (`sectionID`) REFERENCES `Section` (`sectionID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AcademicSection`
--

LOCK TABLES `AcademicSection` WRITE;
/*!40000 ALTER TABLE `AcademicSection` DISABLE KEYS */;
INSERT INTO `AcademicSection` VALUES (13,1),(16,1),(30,1),(53,1),(54,1),(55,1),(62,1),(69,1),(70,1);
/*!40000 ALTER TABLE `AcademicSection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Evaluation`
--

DROP TABLE IF EXISTS `Evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Evaluation` (
  `evaluationID` int NOT NULL AUTO_INCREMENT,
  `evaluationPath` varchar(800) DEFAULT NULL,
  `academicId` int DEFAULT NULL,
  `averageGrade` double NOT NULL DEFAULT '0',
  `presentationId` int DEFAULT NULL,
  PRIMARY KEY (`evaluationID`),
  KEY `academicId` (`academicId`),
  KEY `presentationId` (`presentationId`),
  CONSTRAINT `evaluation_ibfk_1` FOREIGN KEY (`academicId`) REFERENCES `Academic` (`academicID`) ON UPDATE CASCADE,
  CONSTRAINT `evaluation_ibfk_2` FOREIGN KEY (`presentationId`) REFERENCES `Presentation` (`presentationID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=189 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Evaluation`
--

LOCK TABLES `Evaluation` WRITE;
/*!40000 ALTER TABLE `Evaluation` DISABLE KEYS */;
INSERT INTO `Evaluation` VALUES (1,'/Users/gabosaurio/Downloads/para diseno de sw/expediente PP/Payon Aguilar/EvaluacionOV_PayonAguilar.pdf',62,100,3),(7,'/Users/gabosaurio/Downloads/EV_Hermione.pdf',70,0,3),(8,'/Users/gabosaurio/Downloads/EV_Hermione.pdf',70,0,3),(10,'/Users/gabosaurio/Downloads/boleto_vuelo.jpg',70,0,4),(11,'/Users/gabosaurio/Downloads/boleto_vuelo.jpg',70,0,4),(12,'/Users/gabosaurio/Downloads/boleto_vuelo.jpg',70,80,4),(13,'/Users/gabosaurio/Downloads/boleto_vuelo.jpg',70,90,3),(14,'/Users/gabosaurio/Downloads/Comunicacion.png',70,1,4),(42,'/Users/gabosaurio/Downloads/boleto_vuelo.jpg',62,100,4);
/*!40000 ALTER TABLE `Evaluation` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_insert_Evaluation` AFTER INSERT ON `evaluation` FOR EACH ROW BEGIN
DECLARE evaluationsCounter INT;
DECLARE totalSum DOUBLE;

SELECT COUNT(*) INTO evaluationsCounter 
FROM Evaluation 
WHERE presentationId = NEW.presentationId;

SELECT SUM(averageGrade) INTO totalSum
FROM Evaluation
WHERE presentationId = NEW.presentationId;

IF evaluationsCounter > 0 THEN

UPDATE Presentation 
SET presentationGrade = totalSum / evaluationsCounter
WHERE presentationID = NEW.presentationId;

END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `LinkedOrganization`
--

DROP TABLE IF EXISTS `LinkedOrganization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LinkedOrganization` (
  `linkedOrganizationID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `description` varchar(500) NOT NULL,
  `address` varchar(200) NOT NULL,
  `email` varchar(100) NOT NULL,
  `alterContact` varchar(100) NOT NULL,
  PRIMARY KEY (`linkedOrganizationID`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LinkedOrganization`
--

LOCK TABLES `LinkedOrganization` WRITE;
/*!40000 ALTER TABLE `LinkedOrganization` DISABLE KEYS */;
INSERT INTO `LinkedOrganization` VALUES (5,'Facultad de Estadística e Informática','Departamento de laboratiorio de innovación de Software','Av. Xalapa casi esquina con Av. Ávila Camacho','juanperez@uv.mx','2288421700 ext:14554'),(6,'Facultad de Estadística e Informática','Departamento: Unidad de accesibilidad tecnológica','Av. Xalapa casi esquina on Av. Ávila Camacho','juanperez@uv.mx','2288421700'),(9,'La Cotorrisa','Podcast y creación de contenido','CDMX Polanco, 9','lacoto@gmail.com','2265437187'),(13,'Hogwarts','Colegio de Magia y Hechicería','Gran Bretaña','hogwarts@gmail.com','Lechuza 26 blanca');
/*!40000 ALTER TABLE `LinkedOrganization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LinkedOrganizationSection`
--

DROP TABLE IF EXISTS `LinkedOrganizationSection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LinkedOrganizationSection` (
  `linkedOrganizationID` int NOT NULL,
  `sectionID` int NOT NULL,
  PRIMARY KEY (`linkedOrganizationID`,`sectionID`),
  KEY `sectionID` (`sectionID`),
  CONSTRAINT `linkedorganizationsection_ibfk_1` FOREIGN KEY (`linkedOrganizationID`) REFERENCES `LinkedOrganization` (`linkedOrganizationID`) ON DELETE CASCADE,
  CONSTRAINT `linkedorganizationsection_ibfk_2` FOREIGN KEY (`sectionID`) REFERENCES `Section` (`sectionID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LinkedOrganizationSection`
--

LOCK TABLES `LinkedOrganizationSection` WRITE;
/*!40000 ALTER TABLE `LinkedOrganizationSection` DISABLE KEYS */;
INSERT INTO `LinkedOrganizationSection` VALUES (5,1),(6,1),(9,1),(13,1);
/*!40000 ALTER TABLE `LinkedOrganizationSection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Presentation`
--

DROP TABLE IF EXISTS `Presentation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Presentation` (
  `presentationID` int NOT NULL AUTO_INCREMENT,
  `presentationGrade` double DEFAULT '0',
  `presentationPath` varchar(800) DEFAULT NULL,
  `date` date NOT NULL,
  `studentId` int NOT NULL,
  PRIMARY KEY (`presentationID`),
  KEY `studentId` (`studentId`),
  CONSTRAINT `presentation_ibfk_1` FOREIGN KEY (`studentId`) REFERENCES `Student` (`studentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Presentation`
--

LOCK TABLES `Presentation` WRITE;
/*!40000 ALTER TABLE `Presentation` DISABLE KEYS */;
INSERT INTO `Presentation` VALUES (3,NULL,'','2025-06-25',117),(4,36.2,'','2025-06-30',119);
/*!40000 ALTER TABLE `Presentation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Project`
--

DROP TABLE IF EXISTS `Project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Project` (
  `projectID` int NOT NULL AUTO_INCREMENT,
  `title` varchar(150) NOT NULL,
  `category` varchar(100) NOT NULL,
  `beginningDate` date NOT NULL,
  `endingDate` date NOT NULL,
  `status` varchar(100) NOT NULL,
  `openSpots` int NOT NULL DEFAULT '0',
  `linkedOrganizationId` int NOT NULL,
  `projectResponsibleId` int NOT NULL,
  `documentInfoPath` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`projectID`),
  KEY `linkedOrganizationId` (`linkedOrganizationId`),
  KEY `projectResponsibleId` (`projectResponsibleId`),
  CONSTRAINT `project_ibfk_1` FOREIGN KEY (`linkedOrganizationId`) REFERENCES `LinkedOrganization` (`linkedOrganizationID`) ON UPDATE CASCADE,
  CONSTRAINT `project_ibfk_2` FOREIGN KEY (`projectResponsibleId`) REFERENCES `ProjectResponsible` (`projectResponsibleID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Project`
--

LOCK TABLES `Project` WRITE;
/*!40000 ALTER TABLE `Project` DISABLE KEYS */;
INSERT INTO `Project` VALUES (6,'Mantenimiento de la plataforma de Varamiento de mamíferos marinos en costas mexicanas','Mantenimiento','2025-08-01','2026-01-31','En proceso',8,5,2,NULL),(7,'Plataforma de asistencia para aprendizaje de personas con discapacidad auditiva','Desarrollo de Software','2025-08-01','2026-01-31','En proceso',0,6,9,NULL),(9,'Sitio web cotorro','Desarrollo web','2025-06-30','2026-01-31','En planeación',2,9,1,'/Users/gabosaurio/Documents/Dieta Tt.pdf'),(10,'Sistema inventario EcoModa','Desarrollo de escritorio','2025-06-20','2026-06-20','En proceso',2,6,1,''),(13,'Inventario de Herbología','Desarrollo de aplicaciones de escritorio','2025-07-01','2026-07-01','En proceso',0,13,10,'/Users/gabosaurio/Downloads/InventarioDeHerboogía.pdf'),(14,'Sistema de alimentación de bestias y animales fantásticos','Automatización de procesos','2025-10-10','2026-05-15','En planeación',0,13,11,'/Users/gabosaurio/Downloads/SistemaDeAlimentación.pdf');
/*!40000 ALTER TABLE `Project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectasignation`
--

DROP TABLE IF EXISTS `projectasignation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `projectasignation` (
  `assignationID` int NOT NULL AUTO_INCREMENT,
  `assignationDocumentPath` varchar(500) NOT NULL,
  `studentId` int NOT NULL,
  `coordinatorId` int NOT NULL,
  `projectId` int NOT NULL,
  PRIMARY KEY (`assignationID`),
  KEY `coordinatorId` (`coordinatorId`),
  KEY `projectId` (`projectId`),
  CONSTRAINT `projectasignation_ibfk_1` FOREIGN KEY (`coordinatorId`) REFERENCES `Academic` (`academicID`) ON UPDATE CASCADE,
  CONSTRAINT `projectasignation_ibfk_2` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectasignation`
--

LOCK TABLES `projectasignation` WRITE;
/*!40000 ALTER TABLE `projectasignation` DISABLE KEYS */;
INSERT INTO `projectasignation` VALUES (2,'src/main/java/resources/AssignationDocument/AsignacionMantenimientodelaplataformadeVaramientodemamíferosmarinosencostasmexicanasHarry.pdf',48,13,6),(3,'src/main/java/resources/AssignationDocument/AsignacionMantenimientodelaplataformadeVaramientodemamíferosmarinosencostasmexicanasAngelina.pdf',49,13,6),(5,'src/main/java/resources/AssignationDocument/AsignacionPlataformadeasistenciaparaaprendizajedepersonascondiscapacidadauditivaRon.pdf',51,13,7),(6,'src/main/java/resources/AssignationDocument/AsignacionMantenimientodelaplataformadeVaramientodemamíferosmarinosencostasmexicanasHarry.pdf',55,13,6),(7,'src/main/java/resources/AssignationDocument/AsignacionMantenimientodelaplataformadeVaramientodemamíferosmarinosencostasmexicanasHarry.pdf',56,13,6),(8,'src/main/java/resources/AssignationDocument/AsignacionMantenimientodelaplataformadeVaramientodemamíferosmarinosencostasmexicanasHarry.pdf',57,13,6),(9,'src/main/java/resources/AssignationDocument/AsignacionMantenimientodelaplataformadeVaramientodemamíferosmarinosencostasmexicanasHarry.pdf',58,13,6),(19,'src/main/java/resources/AssignationDocument/ReAsignacionSistemainventarioEcoModaHermione.pdf',50,13,10),(20,'src/main/java/resources/AssignationDocument/ReAsignacionSistemainventarioEcoModaHarry.pdf',66,13,10),(23,'src/main/java/resources/AssignationDocument/ReAsignacionMantenimientodelaplataformadeVaramientodemamíferosmarinosencostasmexicanasNeville.pdf',68,13,6),(24,'src/main/java/resources/AssignationDocument/OficioAsignacionPedro.pdf',69,13,9),(25,'src/main/java/resources/AssignationDocument/OficioAsignacionSamuel.pdf',70,13,9),(26,'src/main/java/resources/AssignationDocument/OficioAsignacionRenata.pdf',71,13,9),(27,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/java/resources/AssignationDocument/OficioAsignacionLex.pdf',72,13,9),(28,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/java/resources/AssignationDocuments/OficioAsignacionLex.pdf',73,13,9),(29,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionLex.pdf',74,13,9),(30,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionLex.pdf',75,13,9),(31,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionLex.pdf',76,13,9),(32,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionLex.pdf',77,13,9),(33,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionLex.pdf',78,13,9),(34,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionLex.pdf',79,13,9),(35,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionLex.pdf',80,13,9),(36,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionLex.pdf',82,13,9),(37,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionLex.pdf',83,13,9),(38,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionLex.pdf',84,13,9),(39,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionLex.docx',85,13,9),(40,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionLex.docx',86,13,9),(41,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionLex.docx',87,13,9),(42,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionLex.docx',88,13,9),(43,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionClark.docx',89,13,6),(49,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioReAsignacionClark.docx',90,13,9),(50,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionHermione.docx',95,13,6),(51,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionHermioneJean.docx',117,13,13),(52,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionNeville.docx',120,13,13),(53,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionRon.docx',118,13,14),(54,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/src/main/resources/AssignationDocuments/OficioAsignacionHarryJames.docx',119,13,14),(55,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/OficioAsignacionDraco.docx',155,55,13),(56,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/OficioAsignacionDraco.docx',156,13,13),(57,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/OficiosAsignacion/OficioAsignacionCollin.docx',157,13,10),(58,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/OficiosAsignacion/OficioAsignacionCollin.docx',158,13,9),(78,'/Users/gabosaurio/Documents/UV/4° Semestre/PCS/ProyectoFinal/PCS-PracticasPro/PCPracticasProfesionales/OficiosAsignacion/RE-OficioAsignacionSeamous.docx',210,13,6);
/*!40000 ALTER TABLE `projectasignation` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_insert_projectAsignation` AFTER INSERT ON `projectasignation` FOR EACH ROW BEGIN
UPDATE Project SET openSpots = openSpots - 1 WHERE projectID = new.projectID;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_insert_projectAsignationUpdateStudent` AFTER INSERT ON `projectasignation` FOR EACH ROW BEGIN
UPDATE Student SET projectID = new.projectID 
WHERE studentID = new.studentID;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `ProjectRequest`
--

DROP TABLE IF EXISTS `ProjectRequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ProjectRequest` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `studentId` int NOT NULL,
  `documentPath` varchar(500) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `studentId` (`studentId`),
  CONSTRAINT `projectrequest_ibfk_1` FOREIGN KEY (`studentId`) REFERENCES `Student` (`studentID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ProjectRequest`
--

LOCK TABLES `ProjectRequest` WRITE;
/*!40000 ALTER TABLE `ProjectRequest` DISABLE KEYS */;
/*!40000 ALTER TABLE `ProjectRequest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ProjectResponsible`
--

DROP TABLE IF EXISTS `ProjectResponsible`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ProjectResponsible` (
  `projectResponsibleID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  `email` varchar(100) NOT NULL,
  `alterContact` varchar(100) NOT NULL,
  `linkedOrganizationId` int DEFAULT NULL,
  `period` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`projectResponsibleID`),
  KEY `projectresponsible_ibfk_1` (`linkedOrganizationId`),
  CONSTRAINT `projectresponsible_ibfk_1` FOREIGN KEY (`linkedOrganizationId`) REFERENCES `linkedorganization` (`linkedOrganizationID`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ProjectResponsible`
--

LOCK TABLES `ProjectResponsible` WRITE;
/*!40000 ALTER TABLE `ProjectResponsible` DISABLE KEYS */;
INSERT INTO `ProjectResponsible` VALUES (1,'Susana Zabaleta','sussy@gmail.com','2287761909',9,'202551'),(2,'Ricardo Peralta','richie@gmail.com','2276543672',9,'202551'),(9,'Slobotsky','slobo@gmail.com','2265471648',9,'202551'),(10,'Pomona Sprout','sprout@gmail.com','Lechuza 10 Negra',13,'202551'),(11,'Rubeus Hagrid','hagrid@gmail.com','Buckbeak (Hipogrifo)',13,'202551');
/*!40000 ALTER TABLE `ProjectResponsible` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ProjectSection`
--

DROP TABLE IF EXISTS `ProjectSection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ProjectSection` (
  `projectID` int NOT NULL,
  `sectionID` int NOT NULL,
  PRIMARY KEY (`projectID`,`sectionID`),
  KEY `sectionID` (`sectionID`),
  CONSTRAINT `projectsection_ibfk_1` FOREIGN KEY (`projectID`) REFERENCES `Project` (`projectID`) ON DELETE CASCADE,
  CONSTRAINT `projectsection_ibfk_2` FOREIGN KEY (`sectionID`) REFERENCES `Section` (`sectionID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ProjectSection`
--

LOCK TABLES `ProjectSection` WRITE;
/*!40000 ALTER TABLE `ProjectSection` DISABLE KEYS */;
INSERT INTO `ProjectSection` VALUES (6,1),(7,1),(9,1),(10,1),(13,1),(14,1);
/*!40000 ALTER TABLE `ProjectSection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Report`
--

DROP TABLE IF EXISTS `Report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Report` (
  `reportID` int NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `reportType` varchar(100) NOT NULL,
  `hours` int NOT NULL,
  `studentId` int NOT NULL,
  `reportPath` varchar(300) NOT NULL,
  `period` varchar(8) NOT NULL,
  PRIMARY KEY (`reportID`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Report`
--

LOCK TABLES `Report` WRITE;
/*!40000 ALTER TABLE `Report` DISABLE KEYS */;
INSERT INTO `Report` VALUES (5,'2025-07-01','Mensual',80,117,'/Users/gabosaurio/Downloads/para diseno de sw/expediente PP/Payon Aguilar/reportes/ReporteMayo_PayonAguilar.pdf','202551'),(9,'2025-06-23','Mensual',80,70,'/Users/gabosaurio/Downloads/booking-b503-nyyexny.pdf','202551');
/*!40000 ALTER TABLE `Report` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_insert_into_report` AFTER INSERT ON `report` FOR EACH ROW BEGIN
DECLARE t_tuition VARCHAR(10);
SELECT tuition INTO t_tuition FROM Student 
WHERE studentID = NEW.studentId;
UPDATE StudentProgress SET hoursValidated = hoursValidated + NEW.hours, 
remainingHours = remainingHours - NEW.hours 
WHERE tuition = t_tuition;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_delete_into_report` AFTER DELETE ON `report` FOR EACH ROW BEGIN
DECLARE t_tuition VARCHAR(10);
SELECT tuition INTO t_tuition FROM Student 
WHERE studentID = OLD.studentId;
UPDATE StudentProgress SET hoursValidated = hoursValidated - OLD.hours, 
remainingHours = remainingHours + OLD.hours 
WHERE tuition = t_tuition;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `ReportTypeCatalog`
--

DROP TABLE IF EXISTS `ReportTypeCatalog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ReportTypeCatalog` (
  `reportTypeID` int NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL,
  PRIMARY KEY (`reportTypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ReportTypeCatalog`
--

LOCK TABLES `ReportTypeCatalog` WRITE;
/*!40000 ALTER TABLE `ReportTypeCatalog` DISABLE KEYS */;
INSERT INTO `ReportTypeCatalog` VALUES (1,'Mensual'),(2,'210 horas'),(3,'480 horas');
/*!40000 ALTER TABLE `ReportTypeCatalog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rolescatalog`
--

DROP TABLE IF EXISTS `rolescatalog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rolescatalog` (
  `roleID` int NOT NULL AUTO_INCREMENT,
  `role` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`roleID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rolescatalog`
--

LOCK TABLES `rolescatalog` WRITE;
/*!40000 ALTER TABLE `rolescatalog` DISABLE KEYS */;
INSERT INTO `rolescatalog` VALUES (1,'COORDINADOR'),(2,'ACADEMICO'),(3,'EVALUADOR');
/*!40000 ALTER TABLE `rolescatalog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Section`
--

DROP TABLE IF EXISTS `Section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Section` (
  `sectionID` int NOT NULL AUTO_INCREMENT,
  `nrc` varchar(15) NOT NULL,
  `period` varchar(10) NOT NULL,
  PRIMARY KEY (`sectionID`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Section`
--

LOCK TABLES `Section` WRITE;
/*!40000 ALTER TABLE `Section` DISABLE KEYS */;
INSERT INTO `Section` VALUES (1,'12345','202551');
/*!40000 ALTER TABLE `Section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Student`
--

DROP TABLE IF EXISTS `Student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Student` (
  `studentID` int NOT NULL AUTO_INCREMENT,
  `tuition` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `paternalSurname` varchar(100) NOT NULL,
  `maternalSurname` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `creditAdvance` int NOT NULL,
  `grade` float NOT NULL,
  `academicId` int NOT NULL,
  `projectId` int DEFAULT NULL,
  `autoevaluationGrade` double DEFAULT '0',
  PRIMARY KEY (`studentID`),
  UNIQUE KEY `tuition` (`tuition`),
  UNIQUE KEY `username` (`username`),
  KEY `academicId` (`academicId`),
  KEY `projectId` (`projectId`),
  CONSTRAINT `fk_student_project` FOREIGN KEY (`projectId`) REFERENCES `Project` (`projectID`) ON DELETE SET DEFAULT ON UPDATE CASCADE,
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`academicId`) REFERENCES `Academic` (`academicID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=479 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Student`
--

LOCK TABLES `Student` WRITE;
/*!40000 ALTER TABLE `Student` DISABLE KEYS */;
INSERT INTO `Student` VALUES (117,'S25000001','Hermione Jean','Granger','','zS25000001@estudiantes.uv.mx','zS25000001','39e31415923c462ff00a8c41e9f203f4c849d12227739f03f4e980241e7a12f1',345,10,54,13,50),(119,'S25000003','Harry James','Potter','','zS25000003@estudiantes.uv.mx','zS25000003','8718983454fd5e81fd6c22f93d57669f184242a61c74eb46acd0b60a9fbc6f0e',300,10,55,14,10),(120,'S25000004','Neville','Longbottom','','zS25000004@estudiantes.uv.mx','zS25000004','5da3ec5e1110c42d36e307daa6c71a0fd8c3240b054bc648a2d295570fcc7b7f',295,10,55,13,10),(156,'S23000004','Draco','Malfoy','','zS23000004@estudiantes.uv.mx','zS23000004','3cae79a4c232d261fe9f9f13eacecc54b0c57387942972536cccc1eedc5978a0',300,10,55,13,10),(158,'S26000001','Collin','Crewey','','zS26000001@estudiantes.uv.mx','zS26000001','a3bbf7c99b7d20f97114edb426e802f8259c749038e16c3ea1b90c608bd89c5a',292,8,55,9,10),(210,'S25000010','Seamous','Finnegan','','zS25000010@estudiantes.uv.mx','zS25000010','3bf7095be9397d32c3f51b9eb663fe58129116437a40594419deef053733cb55',300,10,55,6,10);
/*!40000 ALTER TABLE `Student` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_insert_Student` AFTER INSERT ON `student` FOR EACH ROW BEGIN
INSERT INTO StudentProgress (hoursValidated, remainingHours, tuition)
VALUES (0, 420, new.tuition);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `StudentProgress`
--

DROP TABLE IF EXISTS `StudentProgress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `StudentProgress` (
  `progressID` int NOT NULL AUTO_INCREMENT,
  `hoursValidated` int DEFAULT '0',
  `remainingHours` int DEFAULT '480',
  `tuition` varchar(10) NOT NULL,
  PRIMARY KEY (`progressID`),
  KEY `tuition` (`tuition`),
  CONSTRAINT `studentprogress_ibfk_1` FOREIGN KEY (`tuition`) REFERENCES `Student` (`tuition`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `StudentProgress`
--

LOCK TABLES `StudentProgress` WRITE;
/*!40000 ALTER TABLE `StudentProgress` DISABLE KEYS */;
INSERT INTO `StudentProgress` VALUES (17,80,400,'S25000001'),(19,0,480,'S25000003'),(20,0,480,'S25000004'),(47,0,480,'S23000004'),(49,0,480,'S26000001'),(81,0,420,'S25000010');
/*!40000 ALTER TABLE `StudentProgress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `StudentSection`
--

DROP TABLE IF EXISTS `StudentSection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `StudentSection` (
  `studentID` int NOT NULL,
  `sectionID` int NOT NULL,
  PRIMARY KEY (`studentID`,`sectionID`),
  KEY `sectionID` (`sectionID`),
  CONSTRAINT `studentsection_ibfk_1` FOREIGN KEY (`studentID`) REFERENCES `Student` (`studentID`) ON DELETE CASCADE,
  CONSTRAINT `studentsection_ibfk_2` FOREIGN KEY (`sectionID`) REFERENCES `Section` (`sectionID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `StudentSection`
--

LOCK TABLES `StudentSection` WRITE;
/*!40000 ALTER TABLE `StudentSection` DISABLE KEYS */;
INSERT INTO `StudentSection` VALUES (117,1),(119,1),(120,1),(156,1),(158,1),(210,1);
/*!40000 ALTER TABLE `StudentSection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SystemConfiguration`
--

DROP TABLE IF EXISTS `SystemConfiguration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SystemConfiguration` (
  `projectRegistrationStatus` int DEFAULT NULL,
  `currentSectionID` int DEFAULT NULL,
  KEY `currentSectionID` (`currentSectionID`),
  CONSTRAINT `systemconfiguration_ibfk_1` FOREIGN KEY (`currentSectionID`) REFERENCES `Section` (`sectionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SystemConfiguration`
--

LOCK TABLES `SystemConfiguration` WRITE;
/*!40000 ALTER TABLE `SystemConfiguration` DISABLE KEYS */;
INSERT INTO `SystemConfiguration` VALUES (0,1);
/*!40000 ALTER TABLE `SystemConfiguration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'professionalpractices'
--

--
-- Dumping routines for database 'professionalpractices'
--
/*!50003 DROP FUNCTION IF EXISTS `findUserRole` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `findUserRole`(
p_userName VARCHAR(100),
p_userPassword VARCHAR(100)) RETURNS varchar(20) CHARSET utf8mb4
    DETERMINISTIC
BEGIN
DECLARE userRole VARCHAR(100) DEFAULT NULL;
DECLARE v_studentID INT;

SELECT role
INTO userRole
FROM Academic
WHERE userName = p_userName AND password = p_userPassword;

IF userRole IS NULL THEN

SELECT studentID
INTO v_studentID
FROM Student
WHERE userName = p_userName AND password = p_userPassword;

IF v_studentID IS NOT NULL THEN
SET userRole = 'ESTUDIANTE';
END IF;
END IF;
RETURN userRole;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `isUserFromLogin` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `isUserFromLogin`(p_userName VARCHAR(100)) RETURNS int
    DETERMINISTIC
BEGIN
DECLARE p_userID INT;

SELECT academicID INTO p_userID
FROM Academic
WHERE userName = p_userName
LIMIt 1;

IF p_userID IS NULL THEN
SELECT studentID INTO p_userID
FROM Student
WHERE userName = p_userName
LIMIT 1;
END IF;

RETURN p_userID;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getUserFromLogin` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getUserFromLogin`(
IN p_userName VARCHAR(100),
IN p_password VARCHAR(100),
OUT p_userID INT,
OUT p_userRole VARCHAR(20)
)
BEGIN
DECLARE v_studentID INT DEFAULT NULL;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET p_userID = NULL;

SELECT academicID, role
INTO p_userID, p_userRole
FROM Academic
WHERE userName = p_userName AND password = p_password;

IF p_userID IS NULL THEN

SELECT studentID
INTO v_studentID
FROM Student
WHERE userName = p_userName AND password = p_password;

IF v_studentID IS NOT NULL THEN
SET p_userID = v_studentID;
SET p_userRole = 'ESTUDIANTE';
ELSE
SET p_userRole = NULL;
END IF;
END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02  3:52:46
