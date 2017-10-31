
-- To Make driving_section's drivingDutyElement reference NULL
ALTER TABLE `driving_section`
	DROP FOREIGN KEY `FK_DRIVINGSECTION_DRIVINGDUTYELEMENT`;
ALTER TABLE `driving_section`
	ADD CONSTRAINT `FK_DRIVINGSECTION_DRIVINGDUTYELEMENT` FOREIGN KEY (`driving_duty_element`) REFERENCES `driving_duty_element` (`id`) ON DELETE SET NULL;
ALTER TABLE `driving_duty_element`
	DROP FOREIGN KEY `FK_DRIVINGDUTYELEMENT_DRIVINGDUTY`;
ALTER TABLE `driving_duty_element`
	ADD CONSTRAINT `FK_DRIVINGDUTYELEMENT_DRIVINGDUTY` FOREIGN KEY (`driving_duty`) REFERENCES `driving_duty` (`id`) ON DELETE CASCADE;
ALTER TABLE `driving_duty`
	DROP FOREIGN KEY `FK_DRIVINGDUTY_ROUNDTRIP`;
ALTER TABLE `driving_duty`
	ADD CONSTRAINT `FK_DRIVINGDUTY_ROUNDTRIP` FOREIGN KEY (`round_trip`) REFERENCES `round_trip` (`id`) ON DELETE SET NULL;
ALTER TABLE `round_trip`
	DROP FOREIGN KEY `FK_ROUNDTRIP_CREWLINK`;
ALTER TABLE `round_trip`
	ADD CONSTRAINT `FK_ROUNDTRIP_CREWLINK` FOREIGN KEY (`crew_link`) REFERENCES `crew_link` (`id`) ON DELETE SET NULL;
	




INSERT INTO `crewlink_db`.`role` (`name`) VALUES ('SUPER');
INSERT INTO `crewlink_db`.`role` (`name`) VALUES ('ADMIN');

INSERT INTO `crewlink_db`.`station` (`code`, `name`) 
	VALUES 
		('TS1', 'Test Station 1');
INSERT INTO `crewlink_db`.`station` (`code`, `name`) 
	VALUES 
		('TS2', 'Test Station 2');
		
INSERT INTO `crewlink_db`.`train` (`name`, `start_day`, `train_no`, `from_station`, `to_station`)
	VALUES ('Train 1', '0', '111', '1', '1');

INSERT INTO `crewlink_db`.`train` (`name`, `start_day`, `train_no`, `from_station`, `to_station`)
	VALUES ('Train 2', '1', '122', '1', '1');

		
INSERT INTO `crewlink_db`.`user` (`email`, `first_name`, `password`, `username`,`role`,`activation_key`,`is_active`) 
	VALUES 
		('test@sample', 'Test', 'test', 'test',1,MD5(CONCAT('test-activate-',NOW())),b'1');

INSERT INTO `crewlink_db`.`user` (`email`, `first_name`, `password`, `username`, `role`,`activation_key`,`is_active`) VALUES ('iiitb@sample', 'IITB', 'IIITB', 'IIITB', 1,MD5(CONCAT('IITB-activate-',NOW())),b'1');
		

		
DROP PROCEDURE IF EXISTS `SplitStringToTempTable`;
CREATE DEFINER=`crewlink_db`@`%` PROCEDURE `SplitStringToTempTable`(IN `fullString1` VARCHAR(255), IN `fullString2` VARCHAR(255), IN `fullString3` VARCHAR(255), IN `seperator` VARCHAR(1))
	LANGUAGE SQL
	NOT DETERMINISTIC
	CONTAINS SQL
	SQL SECURITY DEFINER
	COMMENT 'This splits string into table  with a seperator'
BEGIN
SET @Occurrences1 = NULL;
SET @Occurrences2 = NULL;
SET @Occurrences3 = NULL;

	IF(fullString1 IS NOT NULL AND  fullString2 IS NULL AND fullString3 IS NULL) THEN
	   DROP TEMPORARY TABLE IF EXISTS splitStringTempTable;
	   CREATE TEMPORARY TABLE splitStringTempTable ( value VARCHAR(255));


	   SET @Occurrences1 = LENGTH(fullString1) - LENGTH(REPLACE(fullString1, seperator, ''));
    IF( @Occurrences1 IS NULL) THEN
         INSERT INTO splitStringTempTable values (fullString1);
     ELSE
          myloop: WHILE (@Occurrences1 > 0)
	        DO
	            SET @myValue1 = SUBSTRING_INDEX(fullString1, seperator, 1);
	            IF (@myValue1 != '') THEN
	                INSERT into splitStringTempTable values (@myValue1);
	            ELSE
	                LEAVE myloop;
	            END IF;
	            SET @Occurrences1 = LENGTH(fullString1) - LENGTH(REPLACE(fullString1, seperator, ''));
	            IF (@occurrences1 = 0) THEN
	                LEAVE myloop;
	            END IF;
	            SET fullString1 = SUBSTRING(fullString1,LENGTH(SUBSTRING_INDEX(fullString1, seperator, 1))+2);
	        END WHILE;
     END IF;
	ELSEIF (fullString1 IS NOT NULL AND fullString2 IS NOT NULL AND fullString3 IS NULL) THEN
		DROP TEMPORARY TABLE IF EXISTS splitStringTempTable;
	   CREATE TEMPORARY TABLE splitStringTempTable (keyVal VARCHAR(255), value VARCHAR(255));


	   SET @Occurrences1 = LENGTH(fullString1) - LENGTH(REPLACE(fullString1, seperator, ''));
	   SET @Occurrences2 = LENGTH(fullString2) - LENGTH(REPLACE(fullString2, seperator, ''));
     IF( @Occurrences1 = 0 OR @Occurrences2 = 0 ) THEN
         INSERT INTO splitStringTempTable values (fullString1,fullString2);
     ELSE
	        myloop: WHILE (@Occurrences1 > 0 OR @Occurrences2 > 0 )
	        DO
	            SET @myValue1 = SUBSTRING_INDEX(fullString1, seperator, 1);
	            SET @myValue2 = SUBSTRING_INDEX(fullString2, seperator, 1);
	            IF (@myValue1 != '' OR @myValue2 != '') THEN
	                INSERT into splitStringTempTable values (@myValue1,@myValue2);
	            ELSE
	                LEAVE myloop;
	            END IF;
	            SET @Occurrences1 = LENGTH(fullString1) - LENGTH(REPLACE(fullString1, seperator, ''));
	            SET @Occurrences2 = LENGTH(fullString2) - LENGTH(REPLACE(fullString2, seperator, ''));
	            IF (@occurrences1 = 0 OR @occurrences2 = 0) THEN
	                LEAVE myloop;
	            END IF;
	            SET fullString1 = SUBSTRING(fullString1,LENGTH(SUBSTRING_INDEX(fullString1, seperator, 1))+2);
	            SET fullString2 = SUBSTRING(fullString2,LENGTH(SUBSTRING_INDEX(fullString2, seperator, 1))+2);
	        END WHILE;
     END IF;
	ELSEIF (fullString1 IS NOT NULL AND fullString2 IS NOT NULL AND fullString3 IS NOT NULL) THEN
		DROP TEMPORARY TABLE IF EXISTS splitStringTempTable;
	   CREATE TEMPORARY TABLE splitStringTempTable (keyVal VARCHAR(255), value1 VARCHAR(255), value2 VARCHAR(255));


	   SET @Occurrences1 = LENGTH(fullString1) - LENGTH(REPLACE(fullString1, seperator, ''));
	   SET @Occurrences2 = LENGTH(fullString2) - LENGTH(REPLACE(fullString2, seperator, ''));
	   SET @Occurrences3 = LENGTH(fullString3) - LENGTH(REPLACE(fullString3, seperator, ''));
     IF( @Occurrences1 = 0 OR @Occurrences2 = 0 OR @Occurrences3 = 0 ) THEN
         INSERT INTO splitStringTempTable values (fullString1,fullString2);
     ELSE
	        myloop: WHILE (@Occurrences1 > 0 OR @Occurrences2 > 0 OR @Occurrences3 > 0 )
	        DO
	            SET @myValue1 = SUBSTRING_INDEX(fullString1, seperator, 1);
	            SET @myValue2 = SUBSTRING_INDEX(fullString2, seperator, 1);
              SET @myValue3 = SUBSTRING_INDEX(fullString3, seperator, 1);
              IF (@myValue1 != '' OR @myValue2 != '' OR @myValue3 != '') THEN
	                INSERT into splitStringTempTable values (@myValue1,@myValue2, @myValue3);
	            ELSE
	                LEAVE myloop;
	            END IF;
	            SET @Occurrences1 = LENGTH(fullString1) - LENGTH(REPLACE(fullString1, seperator, ''));
	            SET @Occurrences2 = LENGTH(fullString2) - LENGTH(REPLACE(fullString2, seperator, ''));
              SET @Occurrences3 = LENGTH(fullString3) - LENGTH(REPLACE(fullString3, seperator, ''));
	            IF (@occurrences1 = 0 OR @occurrences2 = 0 OR @occurrences3 = 0) THEN
	                LEAVE myloop;
	            END IF;
	            SET fullString1 = SUBSTRING(fullString1,LENGTH(SUBSTRING_INDEX(fullString1, seperator, 1))+2);
	            SET fullString2 = SUBSTRING(fullString2,LENGTH(SUBSTRING_INDEX(fullString2, seperator, 1))+2);
              SET fullString3 = SUBSTRING(fullString3,LENGTH(SUBSTRING_INDEX(fullString3, seperator, 1))+2);
	        END WHILE;
     END IF;
	END IF;
END


		
		
DROP PROCEDURE IF EXISTS `CreateCrewLink`;
CREATE DEFINER=`crewlink_db`@`%` PROCEDURE `CreateCrewLink`(IN `roundTripIds` VARCHAR(255), IN `orderNos` VARCHAR(255), IN `crewLinkName` VARCHAR(255), IN `userPlan` BIGINT, OUT `createdCrewLink` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
	LANGUAGE SQL
	NOT DETERMINISTIC
	CONTAINS SQL
	SQL SECURITY DEFINER
	COMMENT 'This will create crew link with list of round trips and order no'
BEGIN
DECLARE tDistance BIGINT DEFAULT 0;
DECLARE tDuration BIGINT DEFAULT 0;
DECLARE tHomeRest BIGINT DEFAULT 0;
DECLARE tOutRest BIGINT DEFAULT 0;
DECLARE rt_id VARCHAR(255);
DECLARE cl_order_no VARCHAR(255);
DECLARE done BOOLEAN DEFAULT FALSE;
DECLARE curs CURSOR FOR
        SELECT  keyVal, value FROM splitStringTempTable ORDER BY value ASC;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

 
DECLARE exit handler for sqlexception
  BEGIN
    -- ERROR
    SET result = 0;
    SET errorMessage = 'Error occured in Creating Round Trip';
  ROLLBACK;
END;

DECLARE exit handler for sqlwarning
 BEGIN
    -- WARNING
    SET result = 0;
    SET errorMessage = 'WARNING occured in Creating Round Trip';
 ROLLBACK;
END;

SET result = 1;

START TRANSACTION;

CALL SplitStringToTempTable(roundTripIds, orderNos,NULL, ',');

SET @rt_id_first = NULL;
SET @rt_id_last = NULL;
SET @last_rt = NULL;
SET @duration = 0;
SET @homeRest = 0;
SET @outRest = 0;
SET @lastRoundTripDuration = NULL;
SET @lastToStation = NULL;
SET @noOfLocoPilots = 0;

SELECT keyVal INTO @rt_id_first  FROM splitStringTempTable ORDER BY value ASC LIMIT 0,1;
SELECT keyVal INTO @rt_id_last  FROM splitStringTempTable ORDER BY value DESC LIMIT 0,1;

-- Finding total distance for driving duty
SELECT SUM(distance) INTO tDistance
  FROM round_trip as rt
    WHERE FIND_IN_SET(rt.id, roundTripIds);

-- Finding total duration for
    OPEN curs;
        SET done = FALSE;
        curs_loop: LOOP
        FETCH curs INTO rt_id,cl_order_no;
        IF done THEN LEAVE curs_loop; END IF;
           SET @existingCL = NULL;
           SELECT crew_link  INTO @existingCL
               FROM round_trip as rt
                 WHERE rt.id = rt_id;
           IF ( @existingCL IS NOT NULL ) THEN
             UPDATE round_trip SET crew_link = NULL, crew_link_order_no = NULL
               WHERE  round_trip.id = rt_id;

             DELETE FROM crew_link WHERE crew_link.id = @existingCL;
           END IF;

           IF( @last_rt IS NOT NULL ) THEN
             SET @duration = NULL;
             SELECT ((TIME_TO_SEC(TIMEDIFF(  CAST(rt2.start_time as TIME), CAST(rt1.start_time as TIME)))/60) +
                      (IF(rt2.start_day - rt1.start_day < 0 , 7 - (rt1.start_day - rt2.start_day), (rt2.start_day - rt1.start_day))*1440)),
                    ((TIME_TO_SEC(TIMEDIFF(  CAST(rt2.start_time as TIME), CAST(rt1.end_time as TIME)))/60) +
                      (IF(rt2.start_day - rt1.end_day < 0 , 7 - (rt1.end_day - rt2.start_day), (rt2.start_day - rt1.end_day))*1440)),
                      rt1.total_out_station_rest_time
               INTO @duration, @homeRest, @outRest
               FROM round_trip as rt1, round_trip as rt2
                 WHERE rt2.id = rt_id
                       AND rt1.id = @last_dd
                       AND rt1.station = rt2.station;
             IF(@duration IS NULL) THEN
               SET result = 0;
               SET errorMessage = 'Given Round Trips are not in proper combination or order.';
             END IF;

             SET tDuration = tDuration + @duration;
             SET tHomeRest = tHomeRest + @homeRest;
             SET tOutRest = tOutRest + @outRest;
           END IF;
           SET  @last_rt = rt_id;
        END LOOP;
    CLOSE curs;
    IF ( result = 1 ) THEN
      IF ( @last_rt IS NOT NULL ) THEN
        SELECT rt.duration, rt.total_out_station_rest INTO @duration, @outRest
        FROM round_trip as rt
        WHERE rt.id = @last_rt;
        SET tDuration = tDuration + @duration;

        SET @lastRoundTripDuration = @duration;
        SET tOutRest = tOutRest + @outRest;

        SELECT  ((TIME_TO_SEC(TIMEDIFF(  CAST(rt2.start_time as TIME), CAST(rt1.start_time as TIME)))/60) +
                (IF(rt2.start_day - rt1.start_day < 0 , 7 - (rt1.start_day - rt2.start_day), (rt2.start_day - rt1.start_day))*1440)) INTO @duration
        FROM round_trip as rt1, round_trip as rt2
        WHERE rt1.id = @last_rt AND rt2 = @rt_id_first;
        SET tDuration = tDuration + @duration;

        SET @noOfLocoPilots = CEIL((tDuration/60)/168);
      END IF;

      INSERT INTO crew_link (no_of_lp, link_name, distance, duration, start_day, start_time, end_day, end_time, total_home_station_rest, total_out_station_rest_time, station, user_plan)
      SELECT @noOfLocoPilots, crewLinkName, tDistance, tDuration, rt1.start_day, rt1.start_time, rt2.end_day, rt2.end_time, tHomeRest, tOutRest, rt1.station, userPlan
        FROM round_trip as rt1, round_trip as rt2
        WHERE rt1.id = @rt_id_first AND rt2.id = @rt_id_last AND rt1.station = rt2.station;

      SET @newCL = LAST_INSERT_ID();
      SET createdCrewLink = @newCL;

      OPEN curs;
        SET done = FALSE;
        curs_loop: LOOP
        FETCH curs INTO rt_id,cl_order_no;
        IF done THEN LEAVE curs_loop; END IF;
           UPDATE round_trip SET crew_link = createdRoundTrip, crew_link_order_no = cl_order_no
           WHERE round_trip.id = rt_id;
        END LOOP;
      CLOSE curs;
    END IF;

IF ( result = 1 ) THEN
  SET result = 1;
	SET errorMessage = 'SUCCESS';
	COMMIT;
ELSE
	ROLLBACK;
END IF;

END



DROP PROCEDURE IF EXISTS `CreateDrivingDuty`;
CREATE DEFINER=`crewlink_db`@`%` PROCEDURE `CreateDrivingDuty`(IN `drivingDutyElementIds` VARCHAR(255), IN `orderNos` VARCHAR(255), IN `signOnDuration` BIGINT, IN `signOffDuration` BIGINT, IN `userPlan` BIGINT, OUT `createdDrivingDuty` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
	LANGUAGE SQL
	NOT DETERMINISTIC
	CONTAINS SQL
	SQL SECURITY DEFINER
	COMMENT 'This will create driving duty with list of driving duty elements'
BEGIN
DECLARE tDistance BIGINT DEFAULT 0;
DECLARE tDuration BIGINT DEFAULT 0;
DECLARE dde_id VARCHAR(255);
DECLARE dd_order_no VARCHAR(255);
DECLARE done BOOLEAN DEFAULT FALSE;
DECLARE curs CURSOR FOR
        SELECT  keyVal, value FROM splitStringTempTable ORDER BY value ASC;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

DECLARE exit handler for sqlexception
  BEGIN
    -- ERROR
    SET result = 0;
    SET errorMessage = 'Error occured in Creating Driving Duty';
  ROLLBACK;
END;

DECLARE exit handler for sqlwarning
 BEGIN
    -- WARNING
    SET result = 0;
    SET errorMessage = 'WARNING occured in Creating Driving Duty';
 ROLLBACK;
END;

SET result = 1;

START TRANSACTION;

CALL SplitStringToTempTable(drivingDutyElementIds, orderNos,NULL, ',');

SET @dde_id_first = NULL;
SET @dde_id_last = NULL;
SET @last_dde = NULL;
SET @duration = NULL;

SELECT keyVal INTO @dde_id_first  FROM splitStringTempTable ORDER BY value ASC LIMIT 0,1;
SELECT keyVal INTO @dde_id_last  FROM splitStringTempTable ORDER BY value DESC LIMIT 0,1;
SET @trains = NULL;
    SELECT GROUP_CONCAT( DISTINCT t.train_no SEPARATOR ' + ')  INTO @trains
    FROM driving_duty_element as dde, driving_section as ds, train as t
    WHERE ds.train = t.id AND ds.driving_duty_element = dde.id
          AND FIND_IN_SET(drivingDutyElementIds,dde.id);

-- Finding total distance for driving duty
SELECT SUM(distance) INTO tDistance
  FROM driving_duty_element as dde
    WHERE FIND_IN_SET(dde.id, drivingDutyElementIds);

-- Finding total duration for
    OPEN curs;
        curs_loop: LOOP
        FETCH curs INTO dde_id,dd_order_no;
        IF done THEN LEAVE curs_loop; END IF;
           SET @existingDD = NULL;
           SELECT driving_duty  INTO @existingDD
               FROM driving_duty_element as dde
                 WHERE dde.id = dde_id;
           IF ( @existingDD IS NOT NULL ) THEN
             UPDATE driving_duty_element SET driving_duty = NULL, driving_duty_order_no = NULL
               WHERE  driving_duty_element.id = dde_id;

             DELETE FROM driving_duty WHERE driving_duty.id = @existingDD;
           END IF;

           IF( @last_dde IS NOT NULL ) THEN

             SELECT ((TIME_TO_SEC(TIMEDIFF(  CAST(dde2.start_time as TIME), CAST(dde1.start_time as TIME)))/60) +
                      (IF(dde2.start_day - dde1.start_day < 0 , 7 - (dde1.start_day - dde2.start_day), (dde2.start_day - dde1.start_day))*1440)) INTO @duration
               FROM driving_duty_element as dde1, driving_duty_element as dde2
                 WHERE dde2.id = dde_id
                       AND dde1.id = @last_dde;
             SET tDuration = tDuration + @duration;
           END IF;

           SET  @last_dde = dde_id;
        END LOOP;
    CLOSE curs;
    IF ( @last_dde IS NOT NULL ) THEN
      SELECT dde.duration INTO @duration
      FROM driving_duty_element as dde
        WHERE dde.id = @last_dde;
      SET tDuration = tDuration + @duration;
    END IF;

    IF ( signOnDuration IS NULL ) THEN
      SELECT out_station_sign_on_duration INTO signOnDuration FROM station WHERE id IN (
        SELECT dde1.from_station
        FROM driving_duty_element as dde1
        WHERE dde1.id = @dde_id_first  );
    END IF;
    IF ( signOffDuration IS NULL ) THEN
      SELECT out_station_sign_off_duration INTO signOffDuration FROM station WHERE id IN (
        SELECT dde2.to_station
        FROM driving_duty_element as dde2
        WHERE dde2.id = @dde_id_last  );
    END IF;

    INSERT INTO driving_duty (dd_name, distance, duration, start_day, start_time, end_day, end_time, sign_on_duration, sign_off_duration, from_station, to_station, user_plan)
    SELECT @trains, tDistance, tDuration + signOnDuration + signOffDuration,
           IF(
               (TIME_TO_SEC(CAST(dde1.start_time as TIME))/60 - signOnDuration)<0,
                 IF(dde1.start_day = 0,6,dde1.start_day-1),
                 dde1.start_day
               ),

             SEC_TO_TIME(
						 	IF(
							 	 (TIME_TO_SEC(CAST(dde1.start_time as TIME))/60 - signOnDuration)<0,
								 	(TIME_TO_SEC(CAST(dde1.start_time as TIME))/60 - signOnDuration +1440),
									 (TIME_TO_SEC(CAST(dde1.start_time as TIME))/60 - signOnDuration)
               )*60
             ),
             IF( (TIME_TO_SEC(CAST(dde2.end_time as TIME))/60 + signOffDuration)> 1440,
                           (dde2.end_day+1) % 7,
                           dde2.end_day
                      ),
             SEC_TO_TIME(
                           IF ((TIME_TO_SEC(CAST(dde2.end_time as TIME))/60 + signOffDuration)>1440,
									 	          (TIME_TO_SEC(CAST(dde2.end_time as TIME))/60 + signOffDuration) % 1440,
										           (TIME_TO_SEC(CAST(dde2.end_time as TIME))/60 + signOffDuration)
                           ) * 60
                        ),
           signOnDuration, signOffDuration,
           dde1.from_station, dde2.to_station, userPlan
        FROM driving_duty_element as dde1, driving_duty_element as dde2
        WHERE dde1.id = @dde_id_first AND dde2.id = @dde_id_last;

    SET @newDD = LAST_INSERT_ID();
    SET createdDrivingDuty = @newDD;

    OPEN curs;
        SET done = FALSE;
        curs_loop: LOOP
        FETCH curs INTO dde_id,dd_order_no;
        IF done THEN LEAVE curs_loop; END IF;
           UPDATE driving_duty_element SET driving_duty = @newDD, driving_duty_order_no = dd_order_no
           WHERE driving_duty_element.id = dde_id;
        END LOOP;
    CLOSE curs;


IF ( result = 1 ) THEN
  SET result = 1;
	SET errorMessage = 'SUCCESS';
	COMMIT;
ELSE
	ROLLBACK;
END IF;

END




DROP PROCEDURE IF EXISTS `CreateDrivingDutyElement`;
CREATE DEFINER=`crewlink_db`@`%` PROCEDURE `CreateDrivingDutyElement`(IN `drivingSectionId` BIGINT, IN `startPilotId` BIGINT, IN `endPilotId` BIGINT, IN `userPlan` BIGINT, IN `startDay` INT, IN `startTime` VARCHAR(10), OUT `createdDrivingDutyElement` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
	LANGUAGE SQL
	NOT DETERMINISTIC
	CONTAINS SQL
	SQL SECURITY DEFINER
	COMMENT 'Create driving duty element with driving section and pilots ( he'
BEGIN
DECLARE done INT;

DECLARE exit handler for sqlexception
  BEGIN
    -- ERROR
    SET result = 0;
    SET errorMessage = 'Error occured in Creating Driving Duty Element';
  ROLLBACK;
END;

DECLARE exit handler for sqlwarning
 BEGIN
    -- WARNING
    SET result = 0;
    SET errorMessage = 'WARNING occured in Creating Driving Duty Element';
 ROLLBACK;
END;

SET result = 1;

START TRANSACTION;
    SET @dist = NULL;
    SET @duration = NULL;
    SET @sDay = NULL;
    SET @sTime = NULL;
    SET @eDay = NULL;
    SET @eTime = NULL;
    
    -- Delete all the driving duty elements which exist without any driving duty
    -- This might delete other user's pending driving_duty_elements
    -- BUT it is good for clean up.
    DELETE FROM driving_duty_element WHERE driving_duty_element.driving_duty IS NULL;

		-- Case 1 : only driving section id
		IF ( startPilotId = -1 AND drivingSectionId != -1 AND endPilotId = -1 ) THEN
			INSERT INTO driving_duty_element ( distance, duration, start_day, start_time, end_day, end_time, from_station, to_station, user_plan )
				SELECT ds.distance, ds.duration, ds.start_day, ds.start_time, ds.end_day, ds.end_time ,
               ds.from_station, ds.to_station, userPLan
					FROM driving_section as ds
						WHERE ds.id = drivingSectionId;

		-- Case 2 : Start Pilot and Driving Section Id
		ELSEIF ( startPilotId != -1 AND drivingSectionId != -1 AND endPilotId = -1 ) THEN
      INSERT INTO driving_duty_element ( distance, duration, start_day, start_time, end_day, end_time, from_station, to_station, user_plan, start_pilot_trip )
				SELECT sp.distance + ds.distance,
						 sp.duration + ds.duration,
             IF(
               (TIME_TO_SEC(CAST(ds.start_time as TIME))/60 - sp.duration)<0,
                 IF(ds.start_day = 0,6,ds.start_day-1),
                 ds.start_day
               ),

             SEC_TO_TIME(
						 	IF(
							 	 (TIME_TO_SEC(CAST(ds.start_time as TIME))/60 - sp.duration)<0,
								 	(TIME_TO_SEC(CAST(ds.start_time as TIME))/60- sp.duration+1440),
									 (TIME_TO_SEC(CAST(ds.start_time as TIME))/60-sp.duration)
               )
             ),
             ds.end_day, ds.end_time,
						 ds.from_station, ds.to_station, userPlan, sp.id
					FROM driving_section as ds, pilot_trip as sp
						WHERE ds.id = drivingSectionId
							AND sp.id = startPilotId;

		-- Case 3 : Driving Section Id and End Pilot
		ELSEIF ( startPilotId = -1 AND drivingSectionId != -1 AND endPilotId != -1 ) THEN
      INSERT INTO driving_duty_element ( distance, duration, start_day, start_time, from_station, to_station, user_plan, end_pilot_trip )
				SELECT ds.distance + ep.distance ,
						 ds.duration + ep.duration ,
						 ds.start_day,
             ds.start_time,
             IF( (TIME_TO_SEC(CAST(ds.end_time as TIME))/60 + ep.duration)> 1440,
                           (ds.end_day+1) % 7,
                           ds.end_day
                      ),
             SEC_TO_TIME(
                           IF ((TIME_TO_SEC(CAST(ds.end_time as TIME))/60 + ep.duration)>1440,
									 	          (TIME_TO_SEC(CAST(ds.end_time as TIME))/60 + ep.duration) % 1440,
										           (TIME_TO_SEC(CAST(ds.end_time as TIME))/60 + ep.duration)
                           ) * 60
                        ),
						 ds.from_station, ds.to_station, userPlan, sp.id, ep.id
					FROM driving_section as ds, pilot_trip as ep
						WHERE ds.id = drivingSectionId
							AND ep.id = endPilotId;

		-- Case 4 : Start Pilot And Driving Section Id and End Pilot
		ELSEIF ( startPilotId != -1 AND drivingSectionId != -1 AND endPilotId != -1 ) THEN
			INSERT INTO driving_duty_element ( distance, duration, start_day, start_time, end_day, end_time, from_station, to_station, user_plan, start_pilot_trip, end_pilot_trip )
				SELECT sp.distance + ds.distance + ep.distance ,
						 sp.duration + ds.duration + ep.duration ,
             IF(
               (TIME_TO_SEC(CAST(ds.start_time as TIME))/60 - sp.duration)<0,
                 IF(ds.start_day = 0,6,ds.start_day-1),
                 ds.start_day
               ),

             SEC_TO_TIME(
						 	IF(
							 	 (TIME_TO_SEC(CAST(ds.start_time as TIME))/60 - sp.duration)<0,
								 	(TIME_TO_SEC(CAST(ds.start_time as TIME))/60- sp.duration+1440),
									 (TIME_TO_SEC(CAST(ds.start_time as TIME))/60-sp.duration)
               )*60
             ),
             IF( (TIME_TO_SEC(CAST(ds.end_time as TIME))/60 + ep.duration)> 1440,
                           (ds.end_day+1) % 7,
                           ds.end_day
                      ),
             SEC_TO_TIME(
                           IF ((TIME_TO_SEC(CAST(ds.end_time as TIME))/60 + ep.duration)>1440,
									 	          (TIME_TO_SEC(CAST(ds.end_time as TIME))/60 + ep.duration) % 1440,
										           (TIME_TO_SEC(CAST(ds.end_time as TIME))/60 + ep.duration)
                           ) * 60
                        ),
						 ds.from_station, ds.to_station, userPlan, sp.id, ep.id
					FROM driving_section as ds, pilot_trip as sp, pilot_trip as ep
						WHERE ds.id = drivingSectionId
							AND sp.id = startPilotId
							AND ep.id = endPilotId;
		-- Case 4 : Start Pilot Exists and Driving Section and End Pilot are NULL
		ELSEIF ( startPilotId != -1 AND drivingSectionId = -1 AND endPilotId = -1 ) THEN
			IF ( startDay = -1 OR startTime = '' ) THEN
				SET result = FALSE;
				SET errorMessage = 'Error as Start Day or Start Time was not set.';
			ELSE
				INSERT INTO driving_duty_element ( distance, duration, start_day, start_time, end_day, end_time, from_station, to_station, user_plan, start_pilot_trip )
					SELECT sp.distance,
							 sp.duration,
	             startDay,
					 startTime,
	             IF( (TIME_TO_SEC(CAST(startTime as TIME))/60 + sp.duration)> 1440,
	                           (startDay+1) % 7,
	                           startDay
	                      ),
	             SEC_TO_TIME(
	                           IF ((TIME_TO_SEC(CAST(startTime as TIME))/60 + sp.duration)>1440,
										 	          (TIME_TO_SEC(CAST(startTime as TIME))/60 + sp.duration) % 1440,
											           (TIME_TO_SEC(CAST(startTime as TIME))/60 + sp.duration)
	                           ) * 60
	                        ),
							 sp.from_station, sp.to_station, userPlan, sp.id
						FROM pilot_trip as sp
							WHERE sp.id = startPilotId;
			END IF;
	
		-- Else ERROR
		ELSE
			SET result = 0;
			SET errorMessage = 'NOT Valid way to save driving duty element';
		END IF;

    IF( result = 1 ) THEN
      SET createdDrivingDutyElement = LAST_INSERT_ID();
      UPDATE driving_section SET driving_duty_element = createdDrivingDutyElement
      WHERE id = drivingSectionId;
    END IF;
IF (result = 1) THEN
	SET result = 1;
	SET errorMessage = 'SUCCESS';
	COMMIT;
ELSE
  SET createdDrivingDutyElement = NULL;
	ROLLBACK;
END IF;



END


DROP PROCEDURE IF EXISTS `CreateDrivingSectionAndDrivingDutyForTrainAllDays`;
CREATE DEFINER=`crewlink_db`@`%` PROCEDURE `CreateDrivingSectionAndDrivingDutyForTrainAllDays`(IN `trainNo` INT, IN `stopNumbers` VARCHAR(255), IN `userPlan` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
	LANGUAGE SQL
	NOT DETERMINISTIC
	CONTAINS SQL
	SQL SECURITY DEFINER
	COMMENT ''
BEGIN
DECLARE ds_id BIGINT;
DECLARE t_start_day INT;
DECLARE done INT;
DECLARE i INT;

DECLARE curs CURSOR FOR
	SELECT driving_section_id
		FROM newDSListForAllTrains;

DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;


DECLARE exit handler for sqlexception
  BEGIN
    -- ERROR
    SET result = 0;
    SET errorMessage = 'Error occured in Create Driving Section And Driving Duty For Train All Days';
  ROLLBACK;
END;

DECLARE exit handler for sqlwarning
 BEGIN
    -- WARNING
    SET result = 0;
    SET errorMessage = 'WARNING occured in Create Driving Section And Driving Duty For Train All Days';
 ROLLBACK;
END;

SET result = 1;

START TRANSACTION;
		CALL CreateDrivingSectionForTrainAllDays(trainNo,stopNumbers,userPlan,result,errorMessage);

		OPEN curs;
			curs_loop : LOOP
				FETCH curs INTO ds_id; -- Loop through the list of newly create driving sections

				IF done = 1 THEN
					LEAVE curs_loop;
				END IF;

				-- Create Driving Duty Element
        SET @newCreatedDDE = NULL;
        CALL CreateDrivingDutyElement(ds_id, -1, -1, userPlan,-1,'', @newCreatedDDE, result, errorMessage);

        IF( @newCreatedDDE IS NULL) THEN
          LEAVE curs_loop;
        END IF;

        SET @newCreatedDD = NULL;
        CALL CreateDrivingDuty(@newCreatedDDE,'1',NULL,NULL, userPlan, @newCreatedDD, result, errorMessage);

        IF( @newCreatedDDE IS NULL) THEN
          LEAVE curs_loop;
        END IF;

				SET i = i + 1;

			END LOOP;
		CLOSE curs;

	IF (result = 1) THEN
		SET result = 1;
		SET errorMessage = 'SUCCESS';
		COMMIT;
	ELSE
		ROLLBACK;
	END IF;



END



DROP PROCEDURE IF EXISTS `CreateDrivingSectionForSingleTrain`;
CREATE DEFINER=`crewlink_db`@`%` PROCEDURE `CreateDrivingSectionForSingleTrain`(IN `trainNo` INT, IN `startDay` INT, IN `stopNumbers` VARCHAR(255), IN `userPlan` BIGINT, OUT `result` BIT, OUT `errorMessage` VARCHAR(255))
	LANGUAGE SQL
	NOT DETERMINISTIC
	CONTAINS SQL
	SQL SECURITY DEFINER
	COMMENT 'Single train is a train running on one day'
BEGIN
DECLARE ts_id BIGINT;
DECLARE done INT;
DECLARE ts_id_last BIGINT;
DECLARE i INT;
DECLARE days INT;
DECLARE last_ds BIGINT;

DECLARE curs CURSOR FOR
	SELECT ts.id
		FROM train_station as ts, train as t
	   WHERE ts.train = t.id
        AND t.train_no = trainNo
        AND t.start_day = startDay
        AND FIND_IN_SET(ts.stop_number, stopNumbers);

DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;



DECLARE exit handler for sqlexception
  BEGIN
    -- ERROR
    SET result = 0;
    SET errorMessage = 'Error occured in Creating Driving Section for Single Train';
  ROLLBACK;
END;

DECLARE exit handler for sqlwarning
 BEGIN
    -- WARNING
    SET result = 0;
    SET errorMessage = 'WARNING occured in Creating Driving Section for Single Train ';
 ROLLBACK;
END;
DROP TEMPORARY TABLE IF EXISTS `newDSListFor1Train`;
CREATE TEMPORARY TABLE `newDSListFor1Train` ( driving_section_id BIGINT NOT NULL);

START TRANSACTION;
		OPEN curs;



      DELETE FROM driving_section_train_stations
        WHERE driving_sections IN ( SELECT driving_section.id
                                      FROM driving_section
                                        WHERE driving_section.train IN ( SELECT train.id
                                                                            FROM train WHERE train.train_no = trainNo AND train.start_day = startDay) );

      DELETE FROM crew_link
        WHERE id IN ( SELECT crew_link
                      FROM round_trip
                      WHERE id IN ( SELECT round_trip
                                    FROM driving_duty
                                    WHERE id IN ( SELECT driving_duty
                                                  FROM driving_duty_element
                                                  WHERE id IN ( SELECT driving_duty_element
                                                                FROM driving_section
                                                                WHERE driving_section.train IN ( SELECT train.id
                                                                                                 FROM train WHERE train.train_no = trainNo AND train.start_day = startDay) ) ) ) );


      DELETE FROM round_trip
        WHERE id IN ( SELECT round_trip
                    FROM driving_duty
                    WHERE id IN (  SELECT driving_duty
                        FROM driving_duty_element
                          WHERE id IN ( SELECT driving_duty_element
                                        FROM driving_section
                                        WHERE driving_section.train IN ( SELECT train.id
                                                                         FROM train WHERE train.train_no = trainNo AND train.start_day = startDay) ) ) );


      DELETE FROM driving_duty
        WHERE id IN (  SELECT driving_duty
                        FROM driving_duty_element
                          WHERE id IN ( SELECT driving_duty_element
                                        FROM driving_section
                                        WHERE driving_section.train IN ( SELECT train.id
                                                                         FROM train WHERE train.train_no = trainNo AND train.start_day = startDay) ) );

      -- Delete the DDE driving duty element
      DELETE FROM driving_duty_element
        WHERE id IN ( SELECT driving_duty_element
                        FROM driving_section
                          WHERE driving_section.train IN ( SELECT train.id
                          FROM train WHERE train.train_no = trainNo AND train.start_day = startDay) );


      DELETE FROM driving_section
        WHERE driving_section.train IN ( SELECT train.id
								                          FROM train WHERE train.train_no = trainNo AND train.start_day = startDay);

			SET i = 0;
			curs_loop : LOOP
				FETCH curs INTO ts_id;

				IF done = 1 THEN
					LEAVE curs_loop;
				END IF;
        /*
        IF ( TIME_TO_SEC(TIMEDIFF(  CAST(ts2.arrival as TIME), CAST(ts1.departure as TIME))) < 0 ,
											(TIME_TO_SEC(TIMEDIFF(  CAST(ts2.arrival as TIME), CAST('00:00:00' as TIME)))
												+ TIME_TO_SEC(TIMEDIFF(  CAST('24:00:00' as TIME), CAST(ts1.departure as TIME))))/60 ,
									   	(TIME_TO_SEC(TIMEDIFF(  CAST(ts2.arrival as TIME), CAST(ts1.departure as TIME))))/60
									 )
        */
				IF ts_id_last IS NOT NULL THEN
          SET @dist = NULL;
          SET @duration = NULL;
          SET @sDay = NULL;
          SET @sTime = NULL;
          SET @eDay = NULL;
          SET @eTime = NULL;
          SELECT (ts2.distance - ts1.distance),
                  ((TIME_TO_SEC(TIMEDIFF(  CAST(ts2.arrival as TIME), CAST(ts1.departure as TIME)))/60) +
                    (IF(ts2.day - ts1.day < 0 , 7 - (ts1.day - ts2.day), (ts2.day - ts1.day))*1440)),
                    ts1.day, ts1.departure
                 INTO @dist, @duration, @sDay, @sTime
							FROM train_station as ts1, train_station as ts2
							WHERE ts1.id = ts_id_last
								AND ts2.id = ts_id;
          SET @eDay = IF( (TIME_TO_SEC(CAST(@sTime as TIME))/60 + @duration)> 1440,
                           (@sDay+1) % 7,
                           @sDay
                      );
          SET @eTime =  SEC_TO_TIME(
                           IF ((TIME_TO_SEC(CAST(@sTime as TIME))/60 + @duration)>1440,
									 	          (TIME_TO_SEC(CAST(@sTime as TIME))/60 + @duration) % 1440,
										           (TIME_TO_SEC(CAST(@sTime as TIME))/60 + @duration)
                           ) * 60
                        );

					INSERT INTO driving_section ( distance, driving_section_order_no, duration, start_day, start_time,end_day, end_time, from_station, to_station, train, user_plan )
						SELECT @dist,
									i,
                  @duration,
									ts1.day,
									ts1.departure,
                  @eDay,
                  @eTime,
									ts1.station,
									ts2.station,
									ts1.train,
									userPlan
							FROM train_station as ts1, train_station as ts2
							WHERE ts1.id = ts_id_last
								AND ts2.id = ts_id;

					SET last_ds =  LAST_INSERT_ID();
          INSERT INTO newDSListFor1Train ( driving_section_id ) VALUES (last_ds);

					INSERT INTO driving_section_train_stations (driving_sections, train_stations )
						SELECT	last_ds, ts.id
							FROM train_station as ts, train as t
							WHERE 	ts.train = t.id
									AND ts.stop_number <= (SELECT ts2.stop_number
																FROM train_station as ts2
																WHERE ts2.id = ts_id)
									AND ts.stop_number >= (SELECT ts1.stop_number
																FROM train_station as ts1
																WHERE ts1.id = ts_id_last)
									AND t.train_no = trainNo
									AND t.start_day = startDay;


				END IF;
				SET ts_id_last = ts_id;
				SET i = i + 1;

			END LOOP;
		CLOSE curs;


	SET result = 1;
	SET errorMessage = 'SUCCESS';
COMMIT;
END



DROP PROCEDURE IF EXISTS `CreateDrivingSectionForTrainAllDays`;
CREATE DEFINER=`crewlink_db`@`%` PROCEDURE `CreateDrivingSectionForTrainAllDays`(IN `trainNo` INT, IN `stopNumbers` VARCHAR(255), IN `userPlan` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
	LANGUAGE SQL
	NOT DETERMINISTIC
	CONTAINS SQL
	SQL SECURITY DEFINER
	COMMENT 'This is used to create Driving Sections for all the trains runni'
BEGIN
DECLARE t_id BIGINT;
DECLARE t_start_day INT;
DECLARE done INT;
DECLARE i INT;

DECLARE curs CURSOR FOR
	SELECT t.id, t.start_day
		FROM train as t
	   WHERE t.train_no = trainNo;

DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;


DECLARE exit handler for sqlexception
  BEGIN
    -- ERROR
    SET result = 0;
    SET errorMessage = 'Error occured in Creating Driving Section for Train All Days';
  ROLLBACK;
END;

DECLARE exit handler for sqlwarning
 BEGIN
    -- WARNING
    SET result = 0;
    SET errorMessage = 'WARNING occured in Creating Driving Section for Train All Days';
 ROLLBACK;
END;
      DROP TEMPORARY TABLE IF EXISTS  `newDSListForAllTrains`;
      CREATE TEMPORARY TABLE `newDSListForAllTrains` ( driving_section_id BIGINT NOT NULL);

START TRANSACTION;
		OPEN curs;


			SET result = 1;
			SET i = 0;
			curs_loop : LOOP
				FETCH curs INTO t_id, t_start_day;

				IF done = 1 THEN
					LEAVE curs_loop;
				END IF;

				-- DELETE FROM driving_section_train_stations
		    --    WHERE driving_sections IN ( SELECT driving_section.id
		    --                                  FROM driving_section
		    --                                    WHERE driving_section.train = t_id);
		    --   DELETE FROM driving_section
		    --    WHERE driving_section.train = t_id;


		      CALL CreateDrivingSectionForSingleTrain(trainNo,t_start_day,stopNumbers,userPlan,result,errorMessage);
          INSERT INTO `newDSListForAllTrains` ( driving_section_id )
          SELECT driving_section_id FROM `newDSListFor1Train`;

				IF(result = 0 ) THEN
					LEAVE curs_loop;
				END IF;

				SET i = i + 1;

			END LOOP;
		CLOSE curs;

	IF (result = 1) THEN
		SET result = 1;
		SET errorMessage = 'SUCCESS';
		COMMIT;
	ELSE
		ROLLBACK;
	END IF;



END




DROP PROCEDURE IF EXISTS `CreateRoundTrip`;
CREATE DEFINER=`crewlink_db`@`%` PROCEDURE `CreateRoundTrip`(IN `drivingDutyIds` VARCHAR(255), IN `orderNos` VARCHAR(255), IN `crewType` BIGINT, IN `userPlan` BIGINT, OUT `createdRoundTrip` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
	LANGUAGE SQL
	NOT DETERMINISTIC
	CONTAINS SQL
	SQL SECURITY DEFINER
	COMMENT 'This creates round trips with list of driving duties and order n'
BEGIN
DECLARE tDistance BIGINT DEFAULT 0;
DECLARE tDuration BIGINT DEFAULT 0;
DECLARE tOutRest BIGINT DEFAULT 0;
DECLARE dd_id VARCHAR(255);
DECLARE rt_order_no VARCHAR(255);
DECLARE done BOOLEAN DEFAULT FALSE;

DECLARE curs CURSOR FOR
        SELECT  keyVal, value FROM splitStringTempTable ORDER BY value ASC;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;


DECLARE exit handler for sqlexception
  BEGIN
    -- ERROR
    SET result = 0;
    SET errorMessage = 'Error occured in Creating Round Trip';
  ROLLBACK;
END;

DECLARE exit handler for sqlwarning
 BEGIN
    -- WARNING
    SET result = 0;
    SET errorMessage = 'WARNING occured in Creating Round Trip';
 ROLLBACK;
END;

SET result = 1;

START TRANSACTION;

CALL SplitStringToTempTable(drivingDutyIds, orderNos,NULL, ',');

SET @dd_id_first = NULL;
SET @dd_id_last = NULL;
SET @last_dd = NULL;
SET @duration = 0;
SET @outRest = 0;
SET @lastDrivingDutyDuration = NULL;
SET @lastToStation = NULL;
SET @aTime = NULL;
SET @aDay = NULL;
SET @aDuration = 12*60;

SELECT keyVal INTO @dd_id_first  FROM splitStringTempTable ORDER BY value ASC LIMIT 0,1;
SELECT keyVal INTO @dd_id_last  FROM splitStringTempTable ORDER BY value DESC LIMIT 0,1;
SET @trains = NULL;
    SELECT GROUP_CONCAT( DISTINCT dd_name SEPARATOR ' ; ')  INTO @trains
    FROM driving_duty
    WHERE FIND_IN_SET(id, drivingDutyIds);

-- Finding total distance for driving duty
SELECT SUM(distance) INTO tDistance
  FROM driving_duty as dd
    WHERE FIND_IN_SET(dd.id, drivingDutyIds);

-- Finding total duration for
    OPEN curs;
        SET done = FALSE;
        curs_loop: LOOP
        FETCH curs INTO dd_id,rt_order_no;
        IF done THEN LEAVE curs_loop; END IF;
           SET @existingRT = NULL;
           SELECT round_trip  INTO @existingRT
               FROM driving_duty as dd
                 WHERE dd.id = dd_id;
           IF ( @existingRT IS NOT NULL ) THEN
             UPDATE driving_duty SET round_trip = NULL, round_trip_order_no = NULL
               WHERE  driving_duty.id = dd_id;

             DELETE FROM round_trip WHERE round_trip.id = @existingRT;
           END IF;

           IF( @last_dd IS NOT NULL ) THEN
             SET @duration = NULL;
             SELECT ((TIME_TO_SEC(TIMEDIFF(  CAST(dd2.start_time as TIME), CAST(dd1.start_time as TIME)))/60) +
                      (IF(dd2.start_day - dd1.start_day < 0 , 7 - (dd1.start_day - dd2.start_day), (dd2.start_day - dd1.start_day))*1440)),
                    ((TIME_TO_SEC(TIMEDIFF(  CAST(dd2.start_time as TIME), CAST(dd1.end_time as TIME)))/60) +
                      (IF(dd2.start_day - dd1.end_day < 0 , 7 - (dd1.end_day - dd2.start_day), (dd2.start_day - dd1.end_day))*1440))
               INTO @duration, @outRest
               FROM driving_duty as dd1, driving_duty as dd2
                 WHERE dd2.id = dd_id
                       AND dd1.id = @last_dd
                       AND dd1.to_station = dd2.from_station;
             IF(@duration IS NULL) THEN
               SET result = 0;
               SET errorMessage = 'Given Driving Duties are not in proper combination or order.';
             END IF;

             SET tDuration = tDuration + @duration;
             SET tOutRest = tOutRest + @outRest;

           END IF;

           SET  @last_dd = dd_id;
        END LOOP;
    CLOSE curs;
    IF ( result = 1 ) THEN
      IF ( @last_dd IS NOT NULL ) THEN
        SELECT dd.duration, dd.end_day, dd.end_time INTO @duration ,@eDay, @eTime
        FROM driving_duty as dd
        WHERE dd.id = @last_dd;
        SET tDuration = tDuration + @duration;
        SET @lastDrivingDutyDuration = @duration;
        IF( @lastDrivingDutyDuration/60 > 8 ) THEN
            SET @aDuration = 16*60;    -- HOME Station Rest   defalut 12*60
        END IF;
        -- Add HOME Rest duration to get NEXT available day and time
        SET @aDay = IF( (TIME_TO_SEC(CAST(@eTime as TIME))/60 + @aDuration)> 1440,
                           (@eDay+1) % 7,
                           @eDay
                      );
        SET @aTime = SEC_TO_TIME(((TIME_TO_SEC(CAST(@eTime as TIME))/60 + @aDuration) % 1440)*60) ;

      END IF;
      INSERT INTO round_trip (rt_name, distance, duration, start_day, start_time, end_day, end_time, total_out_station_rest_time, last_driving_duty_duration, next_available_day, next_available_time, station, crew_type, user_plan)
      SELECT @trains, tDistance, tDuration, dd1.start_day, dd1.start_time, dd2.end_day, dd2.end_time, tOutRest, @lastDrivingDutyDuration,
             @aDay, @aTime, dd1.from_station, crewType, userPlan
        FROM driving_duty as dd1, driving_duty as dd2
        WHERE dd1.id = @dd_id_first AND dd2.id = @dd_id_last AND dd1.from_station = dd2.to_station;

      SET @newRT = LAST_INSERT_ID();
      SET createdRoundTrip = @newRT;

      OPEN curs;
        SET done = FALSE;
        curs_loop: LOOP
        FETCH curs INTO dd_id,rt_order_no;
        IF done THEN LEAVE curs_loop; END IF;
           UPDATE driving_duty SET round_trip = createdRoundTrip, round_trip_order_no = rt_order_no
           WHERE driving_duty.id = dd_id;
        END LOOP;
      CLOSE curs;
    END IF;

IF ( result = 1 ) THEN
  SET result = 1;
	SET errorMessage = 'SUCCESS';
	COMMIT;
ELSE
	ROLLBACK;
END IF;

END