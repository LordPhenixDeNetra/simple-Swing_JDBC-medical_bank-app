-- --------------------------------------------------------
-- Hôte:                         127.0.0.1
-- Version du serveur:           8.0.30 - MySQL Community Server - GPL
-- SE du serveur:                Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Listage de la structure de la base pour compte_db
CREATE DATABASE IF NOT EXISTS `compte_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `compte_db`;

-- Listage de la structure de table compte_db. client
CREATE TABLE IF NOT EXISTS `client` (
  `numeroCompte` int NOT NULL,
  `solde` double DEFAULT NULL,
  `prenom` varchar(50) DEFAULT NULL,
  `nom` varchar(50) DEFAULT NULL,
  `decouvert` double DEFAULT NULL,
  PRIMARY KEY (`numeroCompte`),
  UNIQUE KEY `numeroCompte` (`numeroCompte`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Listage des données de la table compte_db.client : ~0 rows (environ)
INSERT INTO `client` (`numeroCompte`, `solde`, `prenom`, `nom`, `decouvert`) VALUES
	(1, 2000, 'Moussa', 'THIOR', 2000),
	(2, 8200, 'Abdou Fall', 'DRAME', 6000),
	(3, 9000, 'Mamadou', 'THIONGANE', 2000),
	(4, 7000, 'Fallou', 'DIENG', 800),
	(5, 4000, 'Souna', 'NIANG', 800);

-- Listage de la structure de table compte_db. historique
CREATE TABLE IF NOT EXISTS `historique` (
  `idOperation` int NOT NULL AUTO_INCREMENT,
  `typeOperation` enum('DEPOT','RETRAIT') DEFAULT NULL,
  `numeroCompte` int DEFAULT NULL,
  `montant` double DEFAULT NULL,
  PRIMARY KEY (`idOperation`),
  KEY `FK_Client_Historique` (`numeroCompte`),
  CONSTRAINT `FK_Client_Historique` FOREIGN KEY (`numeroCompte`) REFERENCES `client` (`numeroCompte`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Listage des données de la table compte_db.historique : ~0 rows (environ)
INSERT INTO `historique` (`idOperation`, `typeOperation`, `numeroCompte`, `montant`) VALUES
	(1, 'DEPOT', 1, 2000),
	(2, 'DEPOT', 2, 5000),
	(3, 'DEPOT', 3, 9000),
	(4, 'DEPOT', 4, 7000),
	(5, 'DEPOT', 5, 4000),
	(6, 'DEPOT', 1, 2000),
	(7, 'RETRAIT', 1, 3000);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
