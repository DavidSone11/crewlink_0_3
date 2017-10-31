-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.1.17-beta-community-nt-debug - MySQL Community Server (GPL)
-- Server OS:                    Win32
-- HeidiSQL Version:             9.1.0.4867
-- --------------------------------------------------------

SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT;
SET NAMES utf8;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO';

-- To Make driving_section's drivingDutyElement reference NULL
ALTER TABLE `driving_section`
	DROP FOREIGN KEY `FK_DRIVINGSECTION_DRIVINGDUTYELEMENT`;
ALTER TABLE `driving_section`
	ADD CONSTRAINT `FK_DRIVINGSECTION_DRIVINGDUTYELEMENT` FOREIGN KEY (`driving_duty_element`) REFERENCES `driving_duty_element` (`id`) ON DELETE SET NULL;
ALTER TABLE `driving_duty`
	DROP FOREIGN KEY `FK_DRIVINGDUTY_ROUNDTRIP`;
ALTER TABLE `driving_duty`
	ADD CONSTRAINT `FK_DRIVINGDUTY_ROUNDTRIP` FOREIGN KEY (`round_trip`) REFERENCES `round_trip` (`id`) ON DELETE SET NULL;
ALTER TABLE `round_trip`
	DROP FOREIGN KEY `FK_ROUNDTRIP_CREWLINK`;
ALTER TABLE `round_trip`
	ADD CONSTRAINT `FK_ROUNDTRIP_CREWLINK` FOREIGN KEY (`crew_link`) REFERENCES `crew_link` (`id`) ON DELETE SET NULL;
-- NULL Ref done

-- Dumping data for table crewlink_db.crew_link: ~0 rows (approximately)
DELETE FROM `crew_link`;
/*!40000 ALTER TABLE `crew_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `crew_link` ENABLE KEYS */;

-- Dumping data for table crewlink_db.crew_type: ~0 rows (approximately)
DELETE FROM `crew_type`;
/*!40000 ALTER TABLE `crew_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `crew_type` ENABLE KEYS */;

-- Dumping data for table crewlink_db.division: ~0 rows (approximately)
DELETE FROM `division`;
/*!40000 ALTER TABLE `division` DISABLE KEYS */;
/*!40000 ALTER TABLE `division` ENABLE KEYS */;

-- Dumping data for table crewlink_db.driving_duty: ~0 rows (approximately)
DELETE FROM `driving_duty`;
/*!40000 ALTER TABLE `driving_duty` DISABLE KEYS */;
/*!40000 ALTER TABLE `driving_duty` ENABLE KEYS */;

-- Dumping data for table crewlink_db.driving_duty_element: ~0 rows (approximately)
DELETE FROM `driving_duty_element`;
/*!40000 ALTER TABLE `driving_duty_element` DISABLE KEYS */;
/*!40000 ALTER TABLE `driving_duty_element` ENABLE KEYS */;

-- Dumping data for table crewlink_db.driving_section: ~0 rows (approximately)
DELETE FROM `driving_section`;
/*!40000 ALTER TABLE `driving_section` DISABLE KEYS */;
/*!40000 ALTER TABLE `driving_section` ENABLE KEYS */;

-- Dumping data for table crewlink_db.pilot_trip: ~0 rows (approximately)
DELETE FROM `pilot_trip`;
/*!40000 ALTER TABLE `pilot_trip` DISABLE KEYS */;
/*!40000 ALTER TABLE `pilot_trip` ENABLE KEYS */;

-- Dumping data for table crewlink_db.pilot_type: ~0 rows (approximately)
DELETE FROM `pilot_type`;
/*!40000 ALTER TABLE `pilot_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `pilot_type` ENABLE KEYS */;

-- Dumping data for table crewlink_db.role: ~2 rows (approximately)
DELETE FROM `role`;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`id`, `name`) VALUES
	(1, 'SUPER'),
	(2, 'ADMIN');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;

-- Dumping data for table crewlink_db.role_sub_roles: ~0 rows (approximately)
DELETE FROM `role_sub_roles`;
/*!40000 ALTER TABLE `role_sub_roles` DISABLE KEYS */;
/*!40000 ALTER TABLE `role_sub_roles` ENABLE KEYS */;

-- Dumping data for table crewlink_db.round_trip: ~0 rows (approximately)
DELETE FROM `round_trip`;
/*!40000 ALTER TABLE `round_trip` DISABLE KEYS */;
/*!40000 ALTER TABLE `round_trip` ENABLE KEYS */;

-- Dumping data for table crewlink_db.station: ~158 rows (approximately)
DELETE FROM `station`;
/*!40000 ALTER TABLE `station` DISABLE KEYS */;
INSERT INTO `station` (`id`, `code`, `head_station_sign_off_duration`, `head_station_sign_on_duration`, `name`, `number_of_beds`, `out_station_sign_off_duration`, `out_station_sign_on_duration`) VALUES
	(1, 'TS1', 20, 20, 'Test Station 1', 1, 20, 20),
	(2, 'TS2', 20, 20, 'Test Station 2', 1, 20, 20)
/*!40000 ALTER TABLE `station` ENABLE KEYS */;

-- Dumping data for table crewlink_db.station_crew_types: ~0 rows (approximately)
DELETE FROM `station_crew_types`;
/*!40000 ALTER TABLE `station_crew_types` DISABLE KEYS */;
/*!40000 ALTER TABLE `station_crew_types` ENABLE KEYS */;

-- Dumping data for table crewlink_db.station_divisions: ~0 rows (approximately)
DELETE FROM `station_divisions`;
/*!40000 ALTER TABLE `station_divisions` DISABLE KEYS */;
/*!40000 ALTER TABLE `station_divisions` ENABLE KEYS */;

-- Dumping data for table crewlink_db.sub_role: ~0 rows (approximately)
DELETE FROM `sub_role`;
/*!40000 ALTER TABLE `sub_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `sub_role` ENABLE KEYS */;

-- Dumping data for table crewlink_db.train: ~128 rows (approximately)
DELETE FROM `train`;
/*!40000 ALTER TABLE `train` DISABLE KEYS */;
/*!40000 ALTER TABLE `train` ENABLE KEYS */;

-- Dumping data for table crewlink_db.train_station: ~2,612 rows (approximately)
DELETE FROM `train_station`;
/*!40000 ALTER TABLE `train_station` DISABLE KEYS */;
/*!40000 ALTER TABLE `train_station` ENABLE KEYS */;

-- Dumping data for table crewlink_db.train_type: ~3 rows (approximately)
DELETE FROM `train_type`;
/*!40000 ALTER TABLE `train_type` DISABLE KEYS */;
INSERT INTO `train_type` (`id`, `name`) VALUES
	(2, 'Exp'),
	(1, 'SF');
/*!40000 ALTER TABLE `train_type` ENABLE KEYS */;

-- Dumping data for table crewlink_db.user: ~2 rows (approximately)
DELETE FROM `user`;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `activation_key`, `email`, `employee_no`, `extension`, `first_name`, `is_active`, `last_name`, `mobile_no`, `password`, `telephone_no`, `username`, `role`, `station`) VALUES
	(1, 'ddaf1346072f945c5a27e1ed3e557d59', 'test@sample', NULL, NULL, 'Test', b'1', NULL, NULL, 'test', NULL, 'test', 1, 1),
	(2, 'ddaf1346072f945c5a27e1ed3e557d59', 'test1@sample', NULL, NULL, 'Test1', b'1', NULL, NULL, 'test1', NULL, 'test1', 1, 1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

-- Dumping data for table crewlink_db.user_plan: ~0 rows (approximately)
DELETE FROM `user_plan`;
/*!40000 ALTER TABLE `user_plan` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_plan` ENABLE KEYS */;
SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '');
SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS);
SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT;
