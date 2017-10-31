USE `crewlink_db`;
ALTER TABLE `user_plan`
  ADD COLUMN `copy_of` BIGINT(20) NULL AFTER `user`,
  ADD INDEX `K_copy_of_user_plan` (`copy_of`);
ALTER TABLE `driving_section`
  ADD COLUMN `copy_of` BIGINT NULL AFTER `is_ignore`,
  ADD INDEX `K_copy_of_driving_section` (`copy_of`);
ALTER TABLE `driving_duty_element`
  ADD COLUMN `copy_of` BIGINT NULL AFTER `dde_name`,
  ADD INDEX `K_copy_of_driving_duty_element` (`copy_of`);
ALTER TABLE `driving_duty`
  ADD COLUMN `copy_of` BIGINT NULL DEFAULT '0' AFTER `is_ignore`,
  ADD INDEX `K_copy_of_driving_duty` (`copy_of`);
ALTER TABLE `round_trip`
  ADD COLUMN `copy_of` BIGINT NULL DEFAULT '0' AFTER `is_ignore`,
  ADD INDEX `K_copy_of_round_trip` (`copy_of`);
ALTER TABLE `crew_link`
  ADD COLUMN `copy_of` BIGINT(20) NULL AFTER `crew_type`,
  ADD INDEX `K_copy_of_crew_link` (`copy_of`);