-- phpMyAdmin SQL Dump
-- version 4.1.6
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Oct 17, 2015 at 08:27 AM
-- Server version: 5.6.16
-- PHP Version: 5.5.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `crewlink_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `crew_link`
--

CREATE TABLE IF NOT EXISTS `crew_link` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `base_station_rest_duration` time DEFAULT NULL,
  `is_end` bit(1) NOT NULL,
  `is_periodicrest` bit(1) NOT NULL,
  `is_start` bit(1) NOT NULL,
  `crew_base_station` bigint(20) DEFAULT NULL,
  `crew_type` bigint(20) DEFAULT NULL,
  `next_crew_link` bigint(20) DEFAULT NULL,
  `prev_crew_link` bigint(20) DEFAULT NULL,
  `round_trip` bigint(20) NOT NULL,
  `user` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_h8ihm9f6w26d5xj6eaoby7dyu` (`crew_base_station`),
  KEY `FK_hrmjpnt090ek1ugsdnt65qxic` (`crew_type`),
  KEY `FK_m4vio53snb28dm3th5n5u2u3j` (`next_crew_link`),
  KEY `FK_fp8t3ew8andtyf6m8r04iufgs` (`prev_crew_link`),
  KEY `FK_6x6j5356rj75si5ukbidsr72g` (`round_trip`),
  KEY `FK_67tpoo53xangg4glbrltqplra` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `crew_type`
--

CREATE TABLE IF NOT EXISTS `crew_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `division`
--

CREATE TABLE IF NOT EXISTS `division` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_bw4owjjddobaevrmh4o67vaho` (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `division`
--

INSERT INTO `division` (`id`, `name`) VALUES
(1, 'testDivision');

-- --------------------------------------------------------

--
-- Table structure for table `division_head_quarter_list`
--

CREATE TABLE IF NOT EXISTS `division_head_quarter_list` (
  `division` bigint(20) NOT NULL,
  `head_quarter_list` bigint(20) NOT NULL,
  UNIQUE KEY `UK_iupwm3ncnu6ivdyrd16vqmlgc` (`head_quarter_list`),
  KEY `FK_humngkmpire4aj2mhdyq1cbtj` (`division`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `division_head_quarter_list`
--

INSERT INTO `division_head_quarter_list` (`division`, `head_quarter_list`) VALUES
(1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `driving_duty`
--

CREATE TABLE IF NOT EXISTS `driving_duty` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_end` bit(1) NOT NULL,
  `is_start` bit(1) NOT NULL,
  `driving_section` bigint(20) DEFAULT NULL,
  `end_pilot` bigint(20) DEFAULT NULL,
  `next_driving_duty` bigint(20) DEFAULT NULL,
  `prev_driving_duty` bigint(20) DEFAULT NULL,
  `start_pilot` bigint(20) DEFAULT NULL,
  `user` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_qjajaue7278t0lv3se26xm2bf` (`driving_section`),
  KEY `FK_h4vyxoo1esvjhvoxhhlmjdhb0` (`end_pilot`),
  KEY `FK_61k7ruj9kognm10q202p1ckpq` (`next_driving_duty`),
  KEY `FK_mjkgeg3ahvrpwdltuegf9yb83` (`prev_driving_duty`),
  KEY `FK_g1xvifuu4tc408aqyunyy2toi` (`start_pilot`),
  KEY `FK_5boelfv9sx5624dkovv4ac8lk` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `driving_section`
--

CREATE TABLE IF NOT EXISTS `driving_section` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_end` bit(1) NOT NULL,
  `is_start` bit(1) NOT NULL,
  `train` tinyblob NOT NULL,
  `crew_base_station` bigint(20) DEFAULT NULL,
  `crew_type` bigint(20) DEFAULT NULL,
  `end_time_table_entry` bigint(20) NOT NULL,
  `next_driving_section` bigint(20) DEFAULT NULL,
  `prev_driving_section` bigint(20) DEFAULT NULL,
  `start_time_table_entry` bigint(20) DEFAULT NULL,
  `user` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_2aswvh374vsi3dildbiewqsrs` (`crew_base_station`),
  KEY `FK_ib3ka39t6qhk9ch69eycnyi9n` (`crew_type`),
  KEY `FK_7b5ee81iykhrj8jq5ki36j4sx` (`end_time_table_entry`),
  KEY `FK_2r933dnvryy6ssj0j8hqtnpna` (`next_driving_section`),
  KEY `FK_qo7xx1jpnthfy81xdxxf15e74` (`prev_driving_section`),
  KEY `FK_ab4dgumlqybp898swkdy9og6j` (`start_time_table_entry`),
  KEY `FK_r9akbyu096cqklbfa19dilbkj` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `head_quarter`
--

CREATE TABLE IF NOT EXISTS `head_quarter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rest_duration` time NOT NULL,
  `sign_off_duration` time NOT NULL,
  `sign_on_duration` time NOT NULL,
  `station` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_35inlls20vqsb2qn7rpuqoyug` (`station`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `head_quarter`
--

INSERT INTO `head_quarter` (`id`, `rest_duration`, `sign_off_duration`, `sign_on_duration`, `station`) VALUES
(1, '11:38:52', '11:38:53', '11:38:53', 1);

-- --------------------------------------------------------

--
-- Table structure for table `head_quarter_crew_types`
--

CREATE TABLE IF NOT EXISTS `head_quarter_crew_types` (
  `head_quarter` bigint(20) NOT NULL,
  `crew_types` bigint(20) NOT NULL,
  KEY `FK_kjigloxuiucbm889u17eb2hcy` (`crew_types`),
  KEY `FK_15sesi3ewq7hgaxrtqfnsrcft` (`head_quarter`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `out_station`
--

CREATE TABLE IF NOT EXISTS `out_station` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `number_of_beds` int(11) NOT NULL,
  `sign_off_duration` time NOT NULL,
  `sign_on_duration` time NOT NULL,
  `station` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ndj61oemqhxgs74nwwcbhxcgf` (`station`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `pilot`
--

CREATE TABLE IF NOT EXISTS `pilot` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `distance` int(11) DEFAULT NULL,
  `duration` time DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `destination_station` bigint(20) NOT NULL,
  `division` bigint(20) DEFAULT NULL,
  `origin_station` bigint(20) NOT NULL,
  `type` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9lci5j4gqe92q05wdqsk9cogq` (`destination_station`),
  KEY `FK_rnx8s3mvmy77c4qjyu52hlc3b` (`division`),
  KEY `FK_41wug0hxft2vcb17xxdhljnem` (`origin_station`),
  KEY `FK_88f2jhfg7pbkoavnyc4md86ds` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `pilot_type`
--

CREATE TABLE IF NOT EXISTS `pilot_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_39q4819jt431ndxff9g93hfa6` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`id`, `name`) VALUES
(1, 'SUPERUSER');

-- --------------------------------------------------------

--
-- Table structure for table `role_sub_roles`
--

CREATE TABLE IF NOT EXISTS `role_sub_roles` (
  `role` bigint(20) NOT NULL,
  `sub_roles` bigint(20) NOT NULL,
  KEY `FK_swmogi75b25j281jywp4nwy56` (`sub_roles`),
  KEY `FK_ncayxpeohgcbn9sugc6gthy3l` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `round_trip`
--

CREATE TABLE IF NOT EXISTS `round_trip` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_end` bit(1) NOT NULL,
  `is_start` bit(1) NOT NULL,
  `out_station_rest_duration` time DEFAULT NULL,
  `driving_duty` bigint(20) NOT NULL,
  `next_round_trip` bigint(20) DEFAULT NULL,
  `out_station` bigint(20) DEFAULT NULL,
  `prev_round_trip` bigint(20) DEFAULT NULL,
  `user` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_1ijn4fcad9mio86vd5anyw85c` (`driving_duty`),
  KEY `FK_ifyi793wwsac0klfuvsh2877r` (`next_round_trip`),
  KEY `FK_9muqo42kpa6xpjthf5w7nulye` (`out_station`),
  KEY `FK_h2g4dtegxbodh9fgrdqlsj03q` (`prev_round_trip`),
  KEY `FK_4fwvc5uuw6owjpo5lg7gbt3dr` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `station`
--

CREATE TABLE IF NOT EXISTS `station` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `is_crew_base` bit(1) NOT NULL,
  `is_out_station` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1mq0bkovmdvo63nfgcmtxrgd4` (`code`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `station`
--

INSERT INTO `station` (`id`, `code`, `is_crew_base`, `is_out_station`, `name`) VALUES
(1, 'test', b'1', b'1', 'test');

-- --------------------------------------------------------

--
-- Table structure for table `sub_role`
--

CREATE TABLE IF NOT EXISTS `sub_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `train`
--

CREATE TABLE IF NOT EXISTS `train` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_daily` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `start_day` int(11) DEFAULT NULL,
  `train_number` int(11) NOT NULL,
  `destination_station` bigint(20) NOT NULL,
  `origin_station` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_5mk8elq5bgy1fnyhfx1v4espy` (`train_number`),
  KEY `FK_c3rn0cqr0cxamrty03wefu0k` (`destination_station`),
  KEY `FK_ktrq0d3lgsasogiqpv3kihe99` (`origin_station`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `train_time_table`
--

CREATE TABLE IF NOT EXISTS `train_time_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `arrival` time NOT NULL,
  `day_of_journey` int(11) NOT NULL,
  `departure` time NOT NULL,
  `distance` int(11) NOT NULL,
  `is_loco_change` bit(1) NOT NULL,
  `journey_duration` time NOT NULL,
  `loco_change` varchar(255) DEFAULT NULL,
  `station` bigint(20) NOT NULL,
  `train` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_69b9jhjnfn25otdjw46gag6h0` (`station`),
  KEY `FK_9yb3y34ch4n61un3ass2wpbo1` (`train`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `employee_no` varchar(255) DEFAULT NULL,
  `extension` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `mobile_no` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `telephone_no` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `base_division` bigint(20) DEFAULT NULL,
  `role` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`),
  KEY `FK_i94233ffqgxe25f5l27aljxqi` (`base_division`),
  KEY `FK_dl7g53f7lpmorjc24kx74apx8` (`role`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `email`, `employee_no`, `extension`, `first_name`, `last_name`, `mobile_no`, `password`, `status`, `telephone_no`, `username`, `version`, `base_division`, `role`) VALUES
(1, 'avitash@gmail.com', NULL, NULL, 'avitash', NULL, '123456789', 'avitash', NULL, NULL, 'avitash', NULL, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `user_train_time_table`
--

CREATE TABLE IF NOT EXISTS `user_train_time_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_crew_change` bit(1) NOT NULL,
  `train_time_table` bigint(20) NOT NULL,
  `user` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ti42xle4twkvxfv7suxlp9vwb` (`train_time_table`),
  KEY `FK_8i7c2reymyi4wk8ljaakkgpx0` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `crew_link`
--
ALTER TABLE `crew_link`
  ADD CONSTRAINT `FK_67tpoo53xangg4glbrltqplra` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FK_6x6j5356rj75si5ukbidsr72g` FOREIGN KEY (`round_trip`) REFERENCES `round_trip` (`id`),
  ADD CONSTRAINT `FK_fp8t3ew8andtyf6m8r04iufgs` FOREIGN KEY (`prev_crew_link`) REFERENCES `crew_link` (`id`),
  ADD CONSTRAINT `FK_h8ihm9f6w26d5xj6eaoby7dyu` FOREIGN KEY (`crew_base_station`) REFERENCES `station` (`id`),
  ADD CONSTRAINT `FK_hrmjpnt090ek1ugsdnt65qxic` FOREIGN KEY (`crew_type`) REFERENCES `crew_type` (`id`),
  ADD CONSTRAINT `FK_m4vio53snb28dm3th5n5u2u3j` FOREIGN KEY (`next_crew_link`) REFERENCES `crew_link` (`id`);

--
-- Constraints for table `division_head_quarter_list`
--
ALTER TABLE `division_head_quarter_list`
  ADD CONSTRAINT `FK_humngkmpire4aj2mhdyq1cbtj` FOREIGN KEY (`division`) REFERENCES `division` (`id`),
  ADD CONSTRAINT `FK_iupwm3ncnu6ivdyrd16vqmlgc` FOREIGN KEY (`head_quarter_list`) REFERENCES `head_quarter` (`id`);

--
-- Constraints for table `driving_duty`
--
ALTER TABLE `driving_duty`
  ADD CONSTRAINT `FK_5boelfv9sx5624dkovv4ac8lk` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FK_61k7ruj9kognm10q202p1ckpq` FOREIGN KEY (`next_driving_duty`) REFERENCES `driving_duty` (`id`),
  ADD CONSTRAINT `FK_g1xvifuu4tc408aqyunyy2toi` FOREIGN KEY (`start_pilot`) REFERENCES `pilot` (`id`),
  ADD CONSTRAINT `FK_h4vyxoo1esvjhvoxhhlmjdhb0` FOREIGN KEY (`end_pilot`) REFERENCES `pilot` (`id`),
  ADD CONSTRAINT `FK_mjkgeg3ahvrpwdltuegf9yb83` FOREIGN KEY (`prev_driving_duty`) REFERENCES `driving_duty` (`id`),
  ADD CONSTRAINT `FK_qjajaue7278t0lv3se26xm2bf` FOREIGN KEY (`driving_section`) REFERENCES `driving_section` (`id`);

--
-- Constraints for table `driving_section`
--
ALTER TABLE `driving_section`
  ADD CONSTRAINT `FK_r9akbyu096cqklbfa19dilbkj` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FK_2aswvh374vsi3dildbiewqsrs` FOREIGN KEY (`crew_base_station`) REFERENCES `station` (`id`),
  ADD CONSTRAINT `FK_2r933dnvryy6ssj0j8hqtnpna` FOREIGN KEY (`next_driving_section`) REFERENCES `driving_section` (`id`),
  ADD CONSTRAINT `FK_7b5ee81iykhrj8jq5ki36j4sx` FOREIGN KEY (`end_time_table_entry`) REFERENCES `train_time_table` (`id`),
  ADD CONSTRAINT `FK_ab4dgumlqybp898swkdy9og6j` FOREIGN KEY (`start_time_table_entry`) REFERENCES `train_time_table` (`id`),
  ADD CONSTRAINT `FK_ib3ka39t6qhk9ch69eycnyi9n` FOREIGN KEY (`crew_type`) REFERENCES `crew_type` (`id`),
  ADD CONSTRAINT `FK_qo7xx1jpnthfy81xdxxf15e74` FOREIGN KEY (`prev_driving_section`) REFERENCES `driving_section` (`id`);

--
-- Constraints for table `head_quarter`
--
ALTER TABLE `head_quarter`
  ADD CONSTRAINT `FK_35inlls20vqsb2qn7rpuqoyug` FOREIGN KEY (`station`) REFERENCES `station` (`id`);

--
-- Constraints for table `head_quarter_crew_types`
--
ALTER TABLE `head_quarter_crew_types`
  ADD CONSTRAINT `FK_15sesi3ewq7hgaxrtqfnsrcft` FOREIGN KEY (`head_quarter`) REFERENCES `head_quarter` (`id`),
  ADD CONSTRAINT `FK_kjigloxuiucbm889u17eb2hcy` FOREIGN KEY (`crew_types`) REFERENCES `crew_type` (`id`);

--
-- Constraints for table `out_station`
--
ALTER TABLE `out_station`
  ADD CONSTRAINT `FK_ndj61oemqhxgs74nwwcbhxcgf` FOREIGN KEY (`station`) REFERENCES `station` (`id`);

--
-- Constraints for table `pilot`
--
ALTER TABLE `pilot`
  ADD CONSTRAINT `FK_88f2jhfg7pbkoavnyc4md86ds` FOREIGN KEY (`type`) REFERENCES `pilot_type` (`id`),
  ADD CONSTRAINT `FK_41wug0hxft2vcb17xxdhljnem` FOREIGN KEY (`origin_station`) REFERENCES `station` (`id`),
  ADD CONSTRAINT `FK_9lci5j4gqe92q05wdqsk9cogq` FOREIGN KEY (`destination_station`) REFERENCES `station` (`id`),
  ADD CONSTRAINT `FK_rnx8s3mvmy77c4qjyu52hlc3b` FOREIGN KEY (`division`) REFERENCES `division` (`id`);

--
-- Constraints for table `role_sub_roles`
--
ALTER TABLE `role_sub_roles`
  ADD CONSTRAINT `FK_ncayxpeohgcbn9sugc6gthy3l` FOREIGN KEY (`role`) REFERENCES `role` (`id`),
  ADD CONSTRAINT `FK_swmogi75b25j281jywp4nwy56` FOREIGN KEY (`sub_roles`) REFERENCES `sub_role` (`id`);

--
-- Constraints for table `round_trip`
--
ALTER TABLE `round_trip`
  ADD CONSTRAINT `FK_4fwvc5uuw6owjpo5lg7gbt3dr` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FK_1ijn4fcad9mio86vd5anyw85c` FOREIGN KEY (`driving_duty`) REFERENCES `driving_duty` (`id`),
  ADD CONSTRAINT `FK_9muqo42kpa6xpjthf5w7nulye` FOREIGN KEY (`out_station`) REFERENCES `out_station` (`id`),
  ADD CONSTRAINT `FK_h2g4dtegxbodh9fgrdqlsj03q` FOREIGN KEY (`prev_round_trip`) REFERENCES `round_trip` (`id`),
  ADD CONSTRAINT `FK_ifyi793wwsac0klfuvsh2877r` FOREIGN KEY (`next_round_trip`) REFERENCES `round_trip` (`id`);

--
-- Constraints for table `train`
--
ALTER TABLE `train`
  ADD CONSTRAINT `FK_ktrq0d3lgsasogiqpv3kihe99` FOREIGN KEY (`origin_station`) REFERENCES `station` (`id`),
  ADD CONSTRAINT `FK_c3rn0cqr0cxamrty03wefu0k` FOREIGN KEY (`destination_station`) REFERENCES `station` (`id`);

--
-- Constraints for table `train_time_table`
--
ALTER TABLE `train_time_table`
  ADD CONSTRAINT `FK_9yb3y34ch4n61un3ass2wpbo1` FOREIGN KEY (`train`) REFERENCES `train` (`id`),
  ADD CONSTRAINT `FK_69b9jhjnfn25otdjw46gag6h0` FOREIGN KEY (`station`) REFERENCES `station` (`id`);

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `FK_dl7g53f7lpmorjc24kx74apx8` FOREIGN KEY (`role`) REFERENCES `role` (`id`),
  ADD CONSTRAINT `FK_i94233ffqgxe25f5l27aljxqi` FOREIGN KEY (`base_division`) REFERENCES `division` (`id`);

--
-- Constraints for table `user_train_time_table`
--
ALTER TABLE `user_train_time_table`
  ADD CONSTRAINT `FK_8i7c2reymyi4wk8ljaakkgpx0` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FK_ti42xle4twkvxfv7suxlp9vwb` FOREIGN KEY (`train_time_table`) REFERENCES `train_time_table` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
