-- phpMyAdmin SQL Dump
-- version 4.1.6
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Oct 17, 2015 at 08:29 AM
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

--
-- Dumping data for table `division`
--

INSERT INTO `division` (`id`, `name`) VALUES
(1, 'testDivision');

--
-- Dumping data for table `division_head_quarter_list`
--

INSERT INTO `division_head_quarter_list` (`division`, `head_quarter_list`) VALUES
(1, 1);

--
-- Dumping data for table `head_quarter`
--

INSERT INTO `head_quarter` (`id`, `rest_duration`, `sign_off_duration`, `sign_on_duration`, `station`) VALUES
(1, '11:38:52', '11:38:53', '11:38:53', 1);

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`id`, `name`) VALUES
(1, 'SUPERUSER');

--
-- Dumping data for table `station`
--

INSERT INTO `station` (`id`, `code`, `is_crew_base`, `is_out_station`, `name`) VALUES
(1, 'test', b'1', b'1', 'test');

--
-- Dumping data for table `user`
--

INSERT INTO `user` ( `email`, `employee_no`, `extension`, `first_name`, `last_name`, `mobile_no`, `password`, `status`, `telephone_no`, `username`, `version`, `base_division`, `role`) VALUES
( 'avitash@gmail.com', NULL, NULL, 'avitash', NULL, '123456789', 'avitash', NULL, NULL, 'avitash', NULL, 1, 1),
( 'test@gmail.com', NULL, NULL, 'test', NULL, '11111', 'test', NULL, NULL, 'test', NULL, 1, 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
