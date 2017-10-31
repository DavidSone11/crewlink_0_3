-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.1.13-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             9.1.0.4867
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
USE `crewlink_db`;


-- Dumping structure for function crewlink_db.CalTimeDiffWeekly
DROP FUNCTION IF EXISTS `CalTimeDiffWeekly`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` FUNCTION `CalTimeDiffWeekly`(`startDay` INT, `startTime` VARCHAR(50), `endDay` INT, `endTime` VARCHAR(50)) RETURNS bigint(20)
    NO SQL
    DETERMINISTIC
    COMMENT 'This will calculate time difference with week cycle'
BEGIN
DECLARE result BIGINT;

SET @diffTime = ((TIME_TO_SEC(CAST(endTime as TIME)) - TIME_TO_SEC(CAST(startTime as TIME)))/60);
SET @diffDay = (IF(endDay - startDay < 0 , 7 + (endDay - startDay), (endDay - startDay))*1440);

SET @result = null;

IF ( @diffTime < 0 AND @diffDay = 0) THEN
	SET result = (7*1440) + @diffTime;
ELSE
	SET result = @diffTime + @diffDay;
END IF;

 RETURN result;
END//
DELIMITER ;


-- Dumping structure for procedure crewlink_db.CopyPlan
DROP PROCEDURE IF EXISTS `CopyPlan`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` PROCEDURE `CopyPlan`(IN `sourcePlanName` VARCHAR(255), IN `sourcePlanTimeStamp` TIMESTAMP, IN `newPlanName` VARCHAR(255), IN `userName` VARCHAR(255), OUT `newUserPlanId` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
    COMMENT 'This copies One Plan to another'
BEGIN
DECLARE sourcePlanId BIGINT DEFAULT NULL;
DECLARE user BIGINT DEFAULT NULL;
DECLARE exit handler for sqlexception
  BEGIN
    ROLLBACK;
    SET result = 0;
    SET errorMessage = 'Error occured in Coping User Plan';
END;
SET result = 1;
START TRANSACTION;



SELECT user_plan.id INTO sourcePlanId FROM user_plan WHERE name = sourcePlanName AND time_stamp = sourcePlanTimeStamp;
SELECT user.id INTO user FROM user WHERE user.username = userName;

if( sourcePlanId IS NULL OR user IS NULL ) THEN
  SET result = 0;
  SET errorMessage = 'Error : NOT valid UserName or Source Plan Name or Timestamp';
ELSE


INSERT INTO user_plan(name,user,copy_of) VALUES(newPlanName,user,sourcePlanId);


SET newUserPlanId = LAST_INSERT_ID();



INSERT INTO crew_link
(no_of_lp, distance, duration, link_name, start_day, start_time, end_day, end_time, total_head_station_rest_time, total_out_station_rest_time, station, user_plan, crew_type, copy_of)
  SELECT cl.no_of_lp, cl.distance, cl.duration, cl.link_name, cl.start_day, cl.start_time, cl.end_day, cl.end_time, cl.total_head_station_rest_time,
  cl.total_out_station_rest_time, cl.station, newUserPlanId, cl.crew_type, cl.id
  FROM crew_link as cl WHERE cl.user_plan = sourcePlanId;




INSERT INTO round_trip
(crew_link_order_no, distance, duration , rt_name, start_day, start_time, end_day, end_time, total_out_station_rest_time, last_driving_duty_duration, 
next_available_day, next_available_time, crew_type, station, user_plan, is_ignore, crew_link, copy_of)
  SELECT rt.crew_link_order_no, rt.distance, rt.duration, rt.rt_name, rt.start_day, rt.start_time, rt.end_day, rt.end_time, rt.total_out_station_rest_time, rt.last_driving_duty_duration,
  rt.next_available_day, rt.next_available_time, rt.crew_type, rt.station, newUserPlanId, rt.is_ignore,rt.crew_link, rt.id
    FROM round_trip as rt WHERE rt.user_plan = sourcePlanId;




INSERT INTO driving_duty
(dd_name, distance, duration , round_trip_order_no, start_day, start_time, end_day, end_time, sign_on_duration, sign_off_duration,
from_station, to_station, user_plan, is_ignore, round_trip, copy_of)
  SELECT dd.dd_name, dd.distance, dd.duration, dd.round_trip_order_no, dd.start_day, dd.start_time, dd.end_day, dd.end_time, dd.sign_on_duration, dd.sign_off_duration,
  dd.from_station, dd.to_station, newUserPlanId, dd.is_ignore,dd.round_trip, dd.id
    FROM driving_duty as dd WHERE dd.user_plan = sourcePlanId;




INSERT INTO driving_duty_element
(distance, driving_duty_order_no, duration, start_day, start_time, end_day, end_time, 
  start_pilot_trip, end_pilot_trip, from_station, to_station, user_plan, dde_name, driving_duty, copy_of)
  SELECT dde.distance, dde.driving_duty_order_no, dde.duration, dde.start_day, dde.start_time, dde.end_day, dde.end_time,
  dde.start_pilot_trip, dde.end_pilot_trip, dde.from_station, dde.to_station, newUserPlanId, dde.dde_name, dde.driving_duty, dde.id
    FROM driving_duty_element as dde WHERE dde.user_plan = sourcePlanId;




INSERT INTO driving_section 
(distance,driving_section_order_no,duration,start_day,start_time,end_day,end_time,
from_station,to_station,train,user_plan,is_ignore, driving_duty_element, copy_of)
  SELECT ds.distance,ds.driving_section_order_no,ds.duration,ds.start_day,ds.start_time,ds.end_day,ds.end_time,
  ds.from_station, ds.to_station, ds.train, newUserPlanId, ds.is_ignore, ds.driving_duty_element, ds.id
    FROM driving_section as ds where ds.user_plan = sourcePlanId;




INSERT INTO driving_section_train_stations (driving_sections,train_stations)
  SELECT ds.id, ts.id
  FROM driving_section as ds, train_station as ts
  WHERE ds.user_plan = newUserPlanId
  AND ds.train = ts.train
  AND ts.id IN (SELECT ts1.id FROM train_station as ts1 
            WHERE ts1.train = ds.train 
            AND ts1.stop_number >= (SELECT ts2.stop_number as stop_number 
                            FROM train_station as ts2
                            WHERE ts2.train = ds.train AND ts2.station = ds.from_station
                            )
            AND ts1.stop_number <=(SELECT ts2.stop_number as stop_number 
                            FROM train_station as ts2
                            WHERE ts2.train = ds.train AND ts2.station = ds.to_station
                            )
          );




UPDATE driving_section as nds LEFT JOIN ( 
SELECT ds.id as dsId, sdde.id as sddeId, dde.id as ddeId
FROM driving_section as ds LEFT JOIN driving_duty_element as sdde ON (ds.driving_duty_element = sdde.id),  driving_duty_element as dde
WHERE ds.user_plan = newUserPlanId
AND dde.user_plan = newUserPlanId
AND dde.copy_of = sdde.id ) as ds_dde ON (nds.id = ds_dde.dsId)
SET
  nds.driving_duty_element = ds_dde.ddeId
WHERE nds.user_plan = newUserPlanId;




UPDATE driving_duty_element as ndde LEFT JOIN ( 
SELECT dde.id as ddeId, sdd.id as sddId, dd.id as ddId
FROM driving_duty_element as dde LEFT JOIN driving_duty as sdd ON (dde.driving_duty = sdd.id),  driving_duty as dd
WHERE dde.user_plan = newUserPlanId
AND dd.user_plan = newUserPlanId
AND dd.copy_of = sdd.id ) as dde_dd ON (ndde.id = dde_dd.ddeId)
SET
ndde.driving_duty = dde_dd.ddId
WHERE ndde.user_plan = newUserPlanId;




UPDATE driving_duty as ndd LEFT JOIN ( 
SELECT dd.id as ddId, srt.id as srtId, rt.id as rtId
FROM driving_duty as dd LEFT JOIN round_trip as srt ON (dd.round_trip = srt.id),  round_trip as rt
WHERE dd.user_plan = newUserPlanId
AND rt.user_plan = newUserPlanId
AND rt.copy_of = srt.id ) as dd_rt ON (ndd.id = dd_rt.ddId)
SET
ndd.round_trip = dd_rt.rtId
WHERE ndd.user_plan = newUserPlanId;




UPDATE round_trip as nrt LEFT JOIN ( 
SELECT rt.id as rtId, scl.id as sclId, cl.id as clId
FROM round_trip as rt LEFT JOIN crew_link as scl ON (rt.crew_link = scl.id),  crew_link as cl
WHERE rt.user_plan = newUserPlanId
AND cl.user_plan = newUserPlanId
AND cl.copy_of = scl.id ) as rt_cl ON (nrt.id = rt_cl.rtId)
SET
nrt.crew_link = rt_cl.clId
WHERE nrt.user_plan = newUserPlanId;



END IF;

IF ( result = 1 ) THEN
  SET result = 1;
  SET errorMessage = 'SUCCESS';
  COMMIT;
  
ELSE
  ROLLBACK;
END IF;
END//
DELIMITER ;


-- Dumping structure for procedure crewlink_db.CreateCrewLink
DROP PROCEDURE IF EXISTS `CreateCrewLink`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` PROCEDURE `CreateCrewLink`(IN `crewLinkId` INT, IN `crewLinkName` VARCHAR(255), IN `userPlan` BIGINT, OUT `createdCrewLink` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
    COMMENT 'This will create crew link with list of round trips and order no'
BEGIN
DECLARE tDistance BIGINT DEFAULT 0;
DECLARE tDuration BIGINT DEFAULT 0;
DECLARE tHomeRest BIGINT DEFAULT 0;
DECLARE tOutRest BIGINT DEFAULT 0;
DECLARE tDutyHrs bigint DEFAULT 0;
DECLARE rt_id VARCHAR(255);
DECLARE cl_order_no VARCHAR(255);
DECLARE done BOOLEAN DEFAULT FALSE;
DECLARE curs CURSOR FOR
        SELECT  round_trip, order_no FROM roundtripsforcrewlinkgeneration WHERE crew_link_id = crewLinkId ORDER BY order_no ASC;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

 
DECLARE exit handler for sqlexception
  BEGIN
		ROLLBACK;
		DELETE FROM roundtripsforcrewlinkgeneration WHERE crew_link_id = crewLinkId;
		SET result = 0;
		SET errorMessage = 'Error occured in Creating Round Trip';
END;


SET result = 1;

START TRANSACTION;



SET @rt_id_first = NULL;
SET @rt_id_last = NULL;
SET @last_rt = NULL;
SET @duration = 0;
SET @homeRest = 0;
SET @outRest = 0;
SET @dutyHrs = 0;
SET @lastRoundTripDuration = NULL;
SET @lastToStation = NULL;
SET @noOfLocoPilots = 0;
SET @crewType = NULL;

SELECT round_trip INTO @rt_id_first FROM roundtripsforcrewlinkgeneration WHERE crew_link_id = crewLinkId ORDER BY order_no ASC LIMIT 0,1;
SELECT round_trip INTO @rt_id_last FROM roundtripsforcrewlinkgeneration WHERE crew_link_id = crewLinkId ORDER BY order_no DESC LIMIT 0,1;


SELECT SUM(rt.distance),SUM(rt.duration) - SUM(rt.total_out_station_rest_time), SUM(rt.total_out_station_rest_time) INTO  tDistance, tDutyHrs, tOutRest
  FROM round_trip as rt INNER JOIN roundtripsforcrewlinkgeneration as rtclg ON (rtclg.round_trip = rt.id)
  WHERE rtclg.crew_link_id = crewLinkId;


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
             SELECT	CalTimeDIffWeekly(rt1.end_day, rt1.end_time, rt2.start_day, rt2.start_time)
               INTO @homeRest
               FROM round_trip as rt1, round_trip as rt2
                 WHERE rt2.id = rt_id
                       AND rt1.id = @last_rt
                       AND rt1.station = rt2.station
							  AND rt2.crew_type = rt1.crew_type;
             IF(@homeRest IS NULL) THEN
               SET result = 0;
               SET errorMessage = 'Given Round Trips are not in proper combination or order.';
             END IF;
             SET tHomeRest = tHomeRest + @homeRest;
           END IF;
           SET  @last_rt = rt_id;
        END LOOP;
    CLOSE curs;
    IF ( result = 1 ) THEN
      IF ( @last_rt IS NOT NULL ) THEN

        SELECT  CalTimeDIffWeekly( rt1.end_day, rt1.end_time, rt2.start_day, rt2.start_time),
                rt1.duration, rt1.crew_type
					 INTO @homeRest, @lastRoundTripDuration, @crewType
        FROM round_trip as rt1, round_trip as rt2
        WHERE rt1.id = @rt_id_last AND rt2.id = @rt_id_first;
        
        SET tHomeRest = tHomeRest + @homeRest;

        SET @noOfLocoPilots = CEIL((tDutyHrs + tHomeRest + tOutRest)/(60 *168));
      END IF;

      INSERT INTO crew_link (no_of_lp, link_name, crew_type, distance, duration, start_day, start_time, end_day, end_time, total_head_station_rest_time, total_out_station_rest_time, station, user_plan)
      SELECT @noOfLocoPilots, crewLinkName, @crewType, tDistance, tDutyHrs, rt1.start_day, rt1.start_time, rt2.end_day, rt2.end_time, tHomeRest, tOutRest, rt1.station, userPlan
        FROM round_trip as rt1, round_trip as rt2
        WHERE rt1.id = @rt_id_first AND rt2.id = @rt_id_last AND rt1.station = rt2.station;

      SET @newCL = LAST_INSERT_ID();
      SET createdCrewLink = @newCL;

      OPEN curs;
        SET done = FALSE;
        curs_loop: LOOP
        FETCH curs INTO rt_id,cl_order_no;
        IF done THEN LEAVE curs_loop; END IF;
           UPDATE round_trip SET crew_link = createdCrewLink, crew_link_order_no = cl_order_no
           WHERE round_trip.id = rt_id;
        END LOOP;
      CLOSE curs;
    END IF;

IF ( result = 1 ) THEN
	DELETE FROM roundtripsforcrewlinkgeneration WHERE crew_link_id = crewLinkId;
	SET result = 1;
	SET errorMessage = 'SUCCESS';
	COMMIT;
ELSE
	ROLLBACK;
	DELETE FROM roundtripsforcrewlinkgeneration WHERE crew_link_id = crewLinkId;
END IF;

END//
DELIMITER ;


-- Dumping structure for procedure crewlink_db.CreateDrivingDuty
DROP PROCEDURE IF EXISTS `CreateDrivingDuty`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` PROCEDURE `CreateDrivingDuty`(IN `drivingDutyElementIds` VARCHAR(2048), IN `orderNos` VARCHAR(2048), IN `signOnDuration` BIGINT, IN `signOffDuration` BIGINT, IN `userPlan` BIGINT, OUT `createdDrivingDuty` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
    COMMENT 'This will create driving duty with list of driving duty elements'
BEGIN
DECLARE tDistance BIGINT DEFAULT 0;
DECLARE tDuration BIGINT DEFAULT 0;
DECLARE dde_id VARCHAR(255);
DECLARE dd_order_no VARCHAR(255);
DECLARE done BOOLEAN DEFAULT FALSE;
DECLARE curs CURSOR FOR
        SELECT  keyVal, value FROM splitStringTempTable ORDER BY value *1 ASC;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

DECLARE exit handler for sqlexception
  BEGIN

    SET result = 0;
    SET errorMessage = 'Error occured in Creating Driving Duty';
  ROLLBACK;
END;

SET result = 1;

START TRANSACTION;

CALL SplitStringToTempTable(drivingDutyElementIds, orderNos,NULL, ',');

SET @dde_id_first = NULL;
SET @dde_id_last = NULL;
SET @last_dde = NULL;
SET @duration = NULL;

SELECT keyVal INTO @dde_id_first  FROM splitStringTempTable ORDER BY value *1 ASC LIMIT 0,1;
SELECT keyVal INTO @dde_id_last  FROM splitStringTempTable ORDER BY value *1 DESC LIMIT 0,1;
SET @trains = NULL;
	SELECT GROUP_CONCAT( DISTINCT IFNULL(dde.dde_name,'-') SEPARATOR ' + ')  INTO @trains
    FROM driving_duty_element as dde JOIN splitStringTempTable as ss ON (ss.keyVal = dde.id)
    ORDER BY ss.value *1 ASC;
   

SELECT SUM(distance) INTO tDistance
  FROM driving_duty_element as dde
    WHERE FIND_IN_SET(dde.id, drivingDutyElementIds);


    OPEN curs;
        curs_loop: LOOP
        FETCH curs INTO dde_id,dd_order_no;
        IF done THEN LEAVE curs_loop; END IF;
           SET @existingDD = NULL;
           SELECT driving_duty  INTO @existingDD
               FROM driving_duty_element as dde
                 WHERE dde.id = dde_id;
           IF ( @existingDD IS NOT NULL ) THEN
					DELETE FROM crew_link
					  WHERE id IN ( SELECT crew_link
					                FROM round_trip
					                WHERE id IN ( SELECT round_trip
					                              FROM driving_duty
					                              WHERE id IN ( SELECT driving_duty
					                                            FROM driving_duty_element
					                                            WHERE id = @existingDD ) ) );
					
					
					DELETE FROM round_trip
					  WHERE id IN ( SELECT round_trip
					              FROM driving_duty
					              WHERE id = @existingDD  );
					                                  
					UPDATE driving_duty_element SET driving_duty = NULL, driving_duty_order_no = NULL
						WHERE  driving_duty_element.id = dde_id;
					
					DELETE FROM driving_duty WHERE driving_duty.id = @existingDD;
           END IF;
        END LOOP;
    CLOSE curs;

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
    SELECT @trains, tDistance, IF(dde2.end_day - dde1.start_day = 0, ((TIME_TO_SEC(dde2.end_time)/60)) - (TIME_TO_SEC(dde1.start_time)/60),
              1440 * IF(dde2.end_day - dde1.start_day > 0, dde2.end_day - dde1.start_day, 7 
                - (dde2.end_day + dde1.start_day)) + ((TIME_TO_SEC(dde2.end_time)/60)) - (TIME_TO_SEC(dde1.start_time)/60)
              ) + signOnDuration + signOffDuration,
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
END//
DELIMITER ;


-- Dumping structure for procedure crewlink_db.CreateDrivingDutyElement
DROP PROCEDURE IF EXISTS `CreateDrivingDutyElement`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` PROCEDURE `CreateDrivingDutyElement`(IN `drivingSectionId` BIGINT, IN `startPilotId` BIGINT, IN `endPilotId` BIGINT, IN `userPlan` BIGINT, OUT `createdDrivingDutyElement` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
    COMMENT 'Create driving duty element with driving section and pilots ( he'
BEGIN
	DECLARE done INT;
	
	DECLARE exit handler for sqlexception
	  BEGIN
	    
	    SET result = 0;
	    SET errorMessage = 'Error occured in Creating Driving Duty Element';
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
	    
	    DELETE FROM crew_link
	        WHERE id IN ( SELECT crew_link
	                      FROM round_trip
	                      WHERE id IN ( SELECT round_trip
	                                    FROM driving_duty
	                                    WHERE id IN ( SELECT driving_duty
	                                                  FROM driving_duty_element
	                                                  WHERE id IN ( SELECT driving_duty_element
	                                                                FROM driving_section
	                                                                WHERE id = drivingSectionId ) ) ) );
	
	
	      DELETE FROM round_trip
	        WHERE id IN ( SELECT round_trip
	                    FROM driving_duty
	                    WHERE id IN (  SELECT driving_duty
	                        FROM driving_duty_element
	                          WHERE id IN ( SELECT driving_duty_element
	                                        FROM driving_section
	                                        WHERE id = drivingSectionId ) ) );
	
	
	      DELETE FROM driving_duty
	        WHERE id IN (  SELECT driving_duty
	                        FROM driving_duty_element
	                          WHERE id IN ( SELECT driving_duty_element
	                                        FROM driving_section
	                                        WHERE id = drivingSectionId) );
	
	    
			DELETE FROM driving_duty_element 
				WHERE id IN (SELECT ds.driving_duty_element 
									FROM driving_section as ds 
										WHERE ds.id = drivingSectionId );
	    
	    	
			IF ( startPilotId = -1 AND drivingSectionId != -1 AND endPilotId = -1 ) THEN
				INSERT INTO driving_duty_element (dde_name, distance, duration, start_day, start_time, end_day, end_time, from_station, to_station, user_plan )
					SELECT t.train_no, ds.distance, ds.duration, ds.start_day, ds.start_time, ds.end_day, ds.end_time ,
	               ds.from_station, ds.to_station, userPLan
						FROM driving_section as ds LEFT JOIN train as t on (t.id = ds.train)
							WHERE ds.id = drivingSectionId;
	
			
			ELSEIF ( startPilotId != -1 AND drivingSectionId != -1 AND endPilotId = -1 ) THEN
	      INSERT INTO driving_duty_element (dde_name, distance, duration, start_day, start_time, end_day, end_time, from_station, to_station, user_plan, start_pilot_trip )
					SELECT CONCAT(IFNULL(sp.name,''),':',t.train_no), sp.distance + ds.distance,
							 IF(ds.end_day - sp.start_day = 0, ((TIME_TO_SEC(ds.end_time)/60)) - (TIME_TO_SEC(sp.start_time)/60),
							 	1440 * IF(ds.end_day - sp.start_day > 0, ds.end_day - sp.start_day, 7 
							 		- (ds.end_day + sp.start_day)) + ((TIME_TO_SEC(ds.end_time)/60)) - (TIME_TO_SEC(sp.start_time)/60)
							 	),
	             sp.start_day,sp.start_time,
	             ds.end_day, ds.end_time,
					sp.from_station, ds.to_station, userPlan, sp.id
						FROM driving_section as ds LEFT JOIN train as t on (t.id = ds.train) , pilot_trip as sp
							WHERE ds.id = drivingSectionId
								AND sp.id = startPilotId;
	
			
			ELSEIF ( startPilotId = -1 AND drivingSectionId != -1 AND endPilotId != -1 ) THEN
	      INSERT INTO driving_duty_element ( dde_name, distance, duration, start_day, start_time, end_day, end_time, from_station, to_station, user_plan, end_pilot_trip )
					SELECT CONCAT(t.train_no,':',IFNULL(ep.name,'')), ds.distance + ep.distance ,
							 IF(ep.end_day - ds.start_day = 0, ((TIME_TO_SEC(ep.end_time)/60)) - (TIME_TO_SEC(ds.start_time)/60),
							 	1440 * IF(ep.end_day - ds.start_day > 0, ep.end_day - ds.start_day, 7 
							 		- (ep.end_day + ds.start_day)) + ((TIME_TO_SEC(ep.end_time)/60)) - (TIME_TO_SEC(ds.start_time)/60)
							 	),
							 ds.start_day,
	             ds.start_time,
	             ep.end_day,
	             ep.end_time,
							 ds.from_station, ep.to_station, userPlan,  ep.id
						FROM driving_section as ds LEFT JOIN train as t on (t.id = ds.train), pilot_trip as ep
							WHERE ds.id = drivingSectionId
								AND ep.id = endPilotId;
	
			
			ELSEIF ( startPilotId != -1 AND drivingSectionId != -1 AND endPilotId != -1 ) THEN
				INSERT INTO driving_duty_element (dde_name, distance, duration, start_day, start_time, end_day, end_time, from_station, to_station, user_plan, start_pilot_trip, end_pilot_trip )
					SELECT CONCAT(IFNULL(sp.name,''),':',t.train_no,':',IFNULL(ep.name,'')), sp.distance + ds.distance + ep.distance ,
							 IF(ep.end_day - sp.start_day = 0, ((TIME_TO_SEC(ep.end_time)/60)) - (TIME_TO_SEC(sp.start_time)/60),
							 	1440 * IF(ep.end_day - sp.start_day > 0, ep.end_day - sp.start_day, 7 
							 		- (ep.end_day + sp.start_day)) + ((TIME_TO_SEC(ep.end_time)/60)) - (TIME_TO_SEC(sp.start_time)/60)
							 	), 
	             			sp.start_day,sp.start_time,
	             			ep.end_day,ep.end_time,
							 sp.from_station, ep.to_station, userPlan, sp.id, ep.id
						FROM driving_section as ds LEFT JOIN train as t on (t.id = ds.train) , pilot_trip as sp, pilot_trip as ep
							WHERE ds.id = drivingSectionId
								AND sp.id = startPilotId
								AND ep.id = endPilotId;
			
			ELSEIF ( startPilotId != -1 AND drivingSectionId = -1 AND endPilotId = -1 ) THEN
					INSERT INTO driving_duty_element ( dde_name, distance, duration, start_day, start_time, end_day, end_time, from_station, to_station, user_plan, start_pilot_trip )
						SELECT IFNULL(sp.name,''), sp.distance,
								 sp.duration,sp.start_day,sp.start_time,
		             			 sp.end_day,sp.end_time,
								 sp.from_station, sp.to_station, userPlan, sp.id
							FROM pilot_trip as sp
								WHERE sp.id = startPilotId;
		
			
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
	
	
	
	END//
DELIMITER ;


-- Dumping structure for procedure crewlink_db.CreateDrivingDutyWithListOfDrivingSections
DROP PROCEDURE IF EXISTS `CreateDrivingDutyWithListOfDrivingSections`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` PROCEDURE `CreateDrivingDutyWithListOfDrivingSections`(IN `drivingSectionIds` VARCHAR(2048), IN `userPlan` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
    COMMENT 'Create driving duty with list of driving sections'
BEGIN
DECLARE ds_id BIGINT;
DECLARE i INT DEFAULT 0;
DECLARE done INT;
DECLARE curs CURSOR FOR
        SELECT  keyVal FROM splitStringTempTable;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

DECLARE exit handler for sqlexception
  BEGIN
    
    SET result = 0;
    SET errorMessage = 'Error occured in Creating Driving Duty with List of driving sections';
  ROLLBACK;
END;


SET result = 1;

CALL SplitStringToTempTable(drivingSectionIds, NULL, NULL, ',');

START TRANSACTION;
		OPEN curs;
			curs_loop : LOOP
				FETCH curs INTO ds_id; 

				IF done = 1 THEN
					LEAVE curs_loop;
				END IF;

				
        SET @newCreatedDDE = NULL;
        CALL CreateDrivingDutyElement(ds_id, -1, -1, userPlan, @newCreatedDDE, result, errorMessage);

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



END//
DELIMITER ;


-- Dumping structure for procedure crewlink_db.CreateDrivingSectionAndDrivingDutyForTrainAllDays
DROP PROCEDURE IF EXISTS `CreateDrivingSectionAndDrivingDutyForTrainAllDays`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` PROCEDURE `CreateDrivingSectionAndDrivingDutyForTrainAllDays`(IN `trainNo` INT, IN `stopNumbers` VARCHAR(2048), IN `userPlan` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
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
    
    SET result = 0;
    SET errorMessage = 'Error occured in Create Driving Section And Driving Duty For Train All Days';
  ROLLBACK;
END;


SET result = 1;

START TRANSACTION;
		CALL CreateDrivingSectionForTrainAllDays(trainNo,stopNumbers,userPlan,result,errorMessage);

		OPEN curs;
			curs_loop : LOOP
				FETCH curs INTO ds_id; 

				IF done = 1 THEN
					LEAVE curs_loop;
				END IF;

				
        SET @newCreatedDDE = NULL;
        CALL CreateDrivingDutyElement(ds_id, -1, -1, userPlan,@newCreatedDDE, result, errorMessage);

        IF( @newCreatedDDE IS NULL) THEN
          LEAVE curs_loop;
        END IF;

        SET @newCreatedDD = NULL;
        CALL CreateDrivingDuty(@newCreatedDDE,'1', null, null, userPlan, @newCreatedDD, result, errorMessage);

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



END//
DELIMITER ;


-- Dumping structure for procedure crewlink_db.CreateDrivingSectionForSingleTrain
DROP PROCEDURE IF EXISTS `CreateDrivingSectionForSingleTrain`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` PROCEDURE `CreateDrivingSectionForSingleTrain`(IN `trainNo` INT, IN `startDay` INT, IN `stopNumbers` VARCHAR(2048), IN `userPlan` BIGINT, OUT `result` BIT, OUT `errorMessage` VARCHAR(255))
    COMMENT 'Single train is a train running on one day'
BEGIN
DECLARE ts_id BIGINT;
DECLARE trainId BIGINT;
DECLARE done INT;
DECLARE ts_id_last BIGINT;
DECLARE st_id BIGINT;
DECLARE st_id_last BIGINT;
DECLARE stop_no INT;
DECLARE stop_no_last INT;
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
    
    SET result = 0;
    SET errorMessage = 'Error occured in Creating Driving Section for Single Train';
  ROLLBACK;
END;

DROP TEMPORARY TABLE IF EXISTS `newDSListFor1Train`;
CREATE TEMPORARY TABLE `newDSListFor1Train` ( driving_section_id BIGINT NOT NULL);

								                          
START TRANSACTION;
		SELECT id INTO trainId FROM train WHERE train_no = trainNo AND start_day = startDay;
		
		OPEN curs;
			SET i = 0;
			SET @lastExistDS = NULL;
			SET @lastExistDSOrderNo = NULL;
			curs_loop : LOOP
				FETCH curs INTO ts_id;

				IF done = 1 THEN
					LEAVE curs_loop;
				END IF;
				
				SELECT ts.station,ts.stop_number INTO st_id,stop_no FROM train_station as ts WHERE ts.id = ts_id;
				
				SET @cExistDS = 0;
				IF ts_id_last IS NOT NULL THEN
					SELECT COUNT(ds.id) INTO @cExistDS FROM driving_section as ds
					WHERE ds.from_station = st_id_last AND ds.to_station = st_id
					AND ds.train = trainId
					AND ds.user_plan = userPlan;
					
					SELECT stop_no_last,stop_no,@cExistDS;
		          
					IF(@cExistDS = 0) THEN
		          	SET @cExistDS1 = 0;
		          	SET @cExistDS2 = 0;
		          	SET @existDS1 = NULL;
		          	SET @existDS2 = NULL;
		          	
						SELECT COUNT(ds.id) INTO @cExistDS1 FROM driving_section as ds
						WHERE ds.from_station = st_id_last
						AND ds.train = trainId
						AND ds.user_plan = userPlan;
						
						SELECT COUNT(ds.id) INTO @cExistDS2 FROM driving_section as ds
						WHERE ds.to_station = st_id
						AND ds.train = trainId
						AND ds.user_plan = userPlan;
		          	
		          	

						IF ( @cExistDS1 > 0  ) THEN
							SELECT ds.id as DELETE_DS1  FROM driving_section as ds
							WHERE ds.from_station = st_id_last
							AND ds.train = trainId
							AND ds.user_plan = userPlan;
							
							SELECT ds.id as DELETE_DS1, ds.driving_section_order_no as orderNo  INTO @lastExistDS,@lastExistDSOrderNo 
							FROM driving_section as ds
							WHERE ds.from_station = st_id_last
							AND ds.train = trainId
							AND ds.user_plan = userPlan;
							
							
							
							
								SET @delDS = @lastExistDS;
								SET @delOrderNo = @lastExistDSOrderNo;
								
								DELETE FROM driving_section_train_stations
						        WHERE driving_sections IN ( SELECT driving_section.id
						                                      FROM driving_section
						                                        WHERE driving_section.user_plan = userPlan AND driving_section.id = @delDS);
						      DELETE FROM crew_link
						        WHERE id IN ( SELECT crew_link
						                      FROM round_trip
						                      WHERE id IN ( SELECT round_trip
						                                    FROM driving_duty
						                                    WHERE id IN ( SELECT driving_duty
						                                                  FROM driving_duty_element
						                                                  WHERE id IN ( SELECT driving_duty_element
						                                                                FROM driving_section
						                                                                WHERE driving_section.user_plan = userPlan AND driving_section.id = @delDS ) ) ) );
			
			
						      DELETE FROM round_trip
						        WHERE id IN ( SELECT round_trip
						                    FROM driving_duty
						                    WHERE id IN (  SELECT driving_duty
						                        FROM driving_duty_element
						                          WHERE id IN ( SELECT driving_duty_element
						                                        FROM driving_section
						                                        WHERE driving_section.user_plan = userPlan AND driving_section.id = @delDS ) ) );
			
			
						      DELETE FROM driving_duty
						        WHERE id IN (  SELECT driving_duty
						                        FROM driving_duty_element
						                          WHERE id IN ( SELECT driving_duty_element
						                                        FROM driving_section
						                                        WHERE driving_section.user_plan = userPlan AND driving_section.id = @delDS ) );
			
			      
						      DELETE FROM driving_duty_element
						        WHERE id IN ( SELECT driving_duty_element
						                        FROM driving_section
						                          WHERE driving_section.user_plan = userPlan 
														  	AND driving_section.id = @delDS );
			
			
						      DELETE FROM driving_section
						        WHERE driving_section.user_plan = userPlan AND driving_section.id = @delDS;
						        
						      
								
								
								
								
								
							
							
							
						END IF;
						IF ( @cExistDS2 > 0 ) THEN
							SET @ds2OrderNo = 0;
							SELECT DISTINCT ds.id as DELETE_DS2  FROM driving_section as ds
							WHERE ds.to_station = st_id
							AND ds.train = trainId
							AND ds.user_plan = userPlan;
							
							SELECT ds.id as DELETE_DS2,ds.driving_section_order_no as OrderNo INTO @existDS2,@ds2OrderNo FROM driving_section as ds
							WHERE ds.to_station = st_id
							AND ds.train = trainId
							AND ds.user_plan = userPlan;
							
							
							
							IF ( @lastExistDS IS NOT NULL ) THEN
								SET @existDS1 = @lastExistDS;
								SET @ds1OrderNo = @lastExistDSOrderNo;
								SET @cDelDS = 0;
								
								
								
								SELECT COUNT(ds.id) INTO @cDelDS FROM driving_section as ds
								WHERE ds.driving_section_order_no > @ds1OrderNo AND ds.driving_section_order_no < @ds2OrderNo
								AND ds.train = trainId
								AND ds.user_plan = userPlan;
								
								
								
								IF ( @cDelDS > 0 ) THEN
									
									
									
									SELECT DISTINCT ds.id as DELETE_DS_LIST FROM driving_section as ds
																			WHERE ds.driving_section_order_no > @ds1OrderNo AND ds.driving_section_order_no < @ds2OrderNo
																			AND ds.train = trainId
																			AND ds.user_plan = userPlan
																			AND ds.id NOT IN (SELECT driving_section_id FROM newDSListFor1Train);
									
									DELETE FROM driving_section_train_stations
							        WHERE driving_sections IN ( SELECT driving_section.id
							                                      FROM driving_section
							                                        WHERE driving_section.user_plan = userPlan 
																				 AND driving_section.id IN (
																				 		SELECT DISTINCT ds.id  FROM driving_section as ds
																							WHERE ds.driving_section_order_no > @ds1OrderNo AND ds.driving_section_order_no < @ds2OrderNo
																							AND ds.train = trainId
																							AND ds.user_plan = userPlan
																							AND ds.id NOT IN (SELECT driving_section_id FROM newDSListFor1Train)));
																				
							      DELETE FROM crew_link
							        WHERE id IN ( SELECT crew_link
							                      FROM round_trip
							                      WHERE id IN ( SELECT round_trip
							                                    FROM driving_duty
							                                    WHERE id IN ( SELECT driving_duty
							                                                  FROM driving_duty_element
							                                                  WHERE id IN ( SELECT driving_duty_element
							                                                                FROM driving_section
							                                                                WHERE driving_section.user_plan = userPlan 
																												 AND driving_section.id IN (SELECT DISTINCT ds.id  FROM driving_section as ds
																																						WHERE ds.driving_section_order_no > @ds1OrderNo AND ds.driving_section_order_no < @ds2OrderNo
																																						AND ds.train = trainId
																																						AND ds.user_plan = userPlan
																																						AND ds.id NOT IN (SELECT driving_section_id FROM newDSListFor1Train)) ) ) ) );
									
				
							      DELETE FROM round_trip
							        WHERE id IN ( SELECT round_trip
							                    FROM driving_duty
							                    WHERE id IN (  SELECT driving_duty
							                        FROM driving_duty_element
							                          WHERE id IN ( SELECT driving_duty_element
							                                        FROM driving_section
							                                        WHERE driving_section.user_plan = userPlan 
																				 AND driving_section.id IN (SELECT DISTINCT ds.id  FROM driving_section as ds
																														WHERE ds.driving_section_order_no > @ds1OrderNo AND ds.driving_section_order_no < @ds2OrderNo
																														AND ds.train = trainId
																														AND ds.user_plan = userPlan
																														AND ds.id NOT IN (SELECT driving_section_id FROM newDSListFor1Train) )) ) );
									
				
							      DELETE FROM driving_duty
							        WHERE id IN (  SELECT driving_duty
							                        FROM driving_duty_element
							                          WHERE id IN ( SELECT driving_duty_element
							                                        FROM driving_section
							                                        WHERE driving_section.user_plan = userPlan 
																				 AND driving_section.id IN (SELECT DISTINCT ds.id  FROM driving_section as ds
																														WHERE ds.driving_section_order_no > @ds1OrderNo AND ds.driving_section_order_no < @ds2OrderNo
																														AND ds.train = trainId
																														AND ds.user_plan = userPlan
																														AND ds.id NOT IN (SELECT driving_section_id FROM newDSListFor1Train) ) ) );
									
				      
							      DELETE FROM driving_duty_element
							        WHERE id IN ( SELECT driving_duty_element
							                        FROM driving_section
							                          WHERE driving_section.user_plan = userPlan 
															  	AND driving_section.id IN (SELECT DISTINCT ds.id  FROM driving_section as ds
																									WHERE ds.driving_section_order_no > @ds1OrderNo AND ds.driving_section_order_no < @ds2OrderNo
																									AND ds.train = trainId
																									AND ds.user_plan = userPlan
																									AND ds.id NOT IN (SELECT driving_section_id FROM newDSListFor1Train) ) );
				
									
							      DELETE FROM driving_section
							        WHERE driving_section.user_plan = userPlan 
									  AND driving_section.id IN (SELECT list.dsId FROM (SELECT DISTINCT ds.id as dsId  FROM driving_section as ds
																			WHERE ds.driving_section_order_no > @ds1OrderNo AND ds.driving_section_order_no < @ds2OrderNo
																			AND ds.train = trainId
																			AND ds.user_plan = userPlan
																			AND ds.id NOT IN (SELECT driving_section_id FROM newDSListFor1Train) ) as list);
																			
									
								END IF;	
								SET @lastExistDS = NULL;
								SET @lastExistDSOrderNo = 0;
								
								
							END IF;
							
							
								SET @delDS = @existDS2;
								SET @delOrderNo = @ds2OrderNo;
								
								DELETE FROM driving_section_train_stations
						        WHERE driving_sections IN ( SELECT driving_section.id
						                                      FROM driving_section
						                                        WHERE driving_section.user_plan = userPlan AND driving_section.id = @delDS);
						      DELETE FROM crew_link
						        WHERE id IN ( SELECT crew_link
						                      FROM round_trip
						                      WHERE id IN ( SELECT round_trip
						                                    FROM driving_duty
						                                    WHERE id IN ( SELECT driving_duty
						                                                  FROM driving_duty_element
						                                                  WHERE id IN ( SELECT driving_duty_element
						                                                                FROM driving_section
						                                                                WHERE driving_section.user_plan = userPlan AND driving_section.id = @delDS ) ) ) );
			
			
						      DELETE FROM round_trip
						        WHERE id IN ( SELECT round_trip
						                    FROM driving_duty
						                    WHERE id IN (  SELECT driving_duty
						                        FROM driving_duty_element
						                          WHERE id IN ( SELECT driving_duty_element
						                                        FROM driving_section
						                                        WHERE driving_section.user_plan = userPlan AND driving_section.id = @delDS ) ) );
			
			
						      DELETE FROM driving_duty
						        WHERE id IN (  SELECT driving_duty
						                        FROM driving_duty_element
						                          WHERE id IN ( SELECT driving_duty_element
						                                        FROM driving_section
						                                        WHERE driving_section.user_plan = userPlan AND driving_section.id = @delDS ) );
			
			      
						      DELETE FROM driving_duty_element
						        WHERE id IN ( SELECT driving_duty_element
						                        FROM driving_section
						                          WHERE driving_section.user_plan = userPlan 
														  	AND driving_section.id = @delDS );
			
			
						      DELETE FROM driving_section
						        WHERE driving_section.user_plan = userPlan AND driving_section.id = @delDS;
						        
						      
								
								
								
							
								
							
						END IF;
						
						
							SET @dist = NULL;
							SET @duration = NULL;
							SET @sDay = NULL;
							SET @sTime = NULL;
							SET @eDay = NULL;
							SET @eTime = NULL;
							
							SET @ts1Dep = NULL;
							SET @ts1Arr = NULL;
							SET @ts1Day = NULL;
							
							
							
							SELECT ts1.day, ts1.departure, ts1.arrival INTO @ts1Day, @ts1Dep, @ts1Arr
							FROM train_station as ts1 WHERE ts1.id = ts_id_last;
							SET @ts1DDay = @ts1Day;
							
							IF( TIMEDIFF(CAST(@ts1Dep as TIME), CAST(@ts1Arr as TIME)) <0 ) THEN
							SET @ts1Day = (@ts1Day +1) % 7;
							SET @ts1DDay = @ts1Day;
							END IF;
							
							SELECT fs.code, ts1.stop_number, ts.code, ts2.stop_number from train_station as ts1, train_station as ts2, station as fs, station as ts
							WHERE ts1.id = ts_id_last AND ts2.id = ts_id AND fs.id = ts1.station AND ts.id = ts2.station;
							
							SELECT (ts2.distance - ts1.distance),
								((TIME_TO_SEC(TIMEDIFF(  CAST(ts2.arrival as TIME), CAST(ts1.departure as TIME)))/60) +
								(IF(ts2.day - @ts1Day < 0 , 7 - (@ts1Day - ts2.day), (ts2.day - @ts1Day))*1440)),
								@ts1Day, ts1.departure
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
								SELECT 	@dist,
											i,
											@duration,
											@ts1Day,
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
								AND ts.stop_number <= (	SELECT ts2.stop_number
																FROM train_station as ts2
																WHERE ts2.id = ts_id)
								AND ts.stop_number >= (	SELECT ts1.stop_number
																FROM train_station as ts1
																WHERE ts1.id = ts_id_last)
								AND t.train_no = trainNo
								AND t.start_day = startDay;
							
						
					END IF;
					
					SELECT ds.id INTO @newOrExistDS FROM driving_section as ds
					WHERE ds.from_station = st_id_last AND ds.to_station = st_id
					AND ds.train = trainId
					AND ds.user_plan = userPlan;
					
					UPDATE driving_section SET driving_section_order_no = i
					WHERE id = @newOrExistDS;

					
				END IF;
        
			
				SET ts_id_last = ts_id;
				SET st_id_last = st_id;
				SET stop_no_last = stop_no;
				SET i = i + 1;

			END LOOP;
		CLOSE curs;


	SET result = 1;
	SET errorMessage = 'SUCCESS';
COMMIT;
END//
DELIMITER ;


-- Dumping structure for procedure crewlink_db.CreateDrivingSectionForTrainAllDays
DROP PROCEDURE IF EXISTS `CreateDrivingSectionForTrainAllDays`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` PROCEDURE `CreateDrivingSectionForTrainAllDays`(IN `trainNo` INT, IN `stopNumbers` VARCHAR(2048), IN `userPlan` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
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
    
    SET result = 0;
    SET errorMessage = 'Error occured in Creating Driving Section for Train All Days';
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



END//
DELIMITER ;


-- Dumping structure for procedure crewlink_db.CreatePilotTrip
DROP PROCEDURE IF EXISTS `CreatePilotTrip`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` PROCEDURE `CreatePilotTrip`(IN `name` VARCHAR(255), IN `fromStation` VARCHAR(255), IN `toStation` VARCHAR(255), IN `duration` BIGINT, IN `distance` BIGINT, IN `pilotType` BIGINT, IN `startDay` INT, IN `startTime` VARCHAR(255), IN `endDay` INT, IN `endTime` VARCHAR(255), IN `userPlan` BIGINT, OUT `createdPilotTripId` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
BEGIN

START TRANSACTION;

SET @user = null;
SELECT user into @user FROM user_plan WHERE id = userPlan;
SET @fromStation = null;
SELECT id into @fromStation FROM station where code = fromStation;
SET @toStation = null;
SELECT id into @toStation FROM station where code = toStation;

 INSERT INTO pilot_trip(name,from_station,to_station,duration,distance,start_day,start_time,end_day,end_time,pilot_type,user) 
					  values(name,@fromStation,@toStation,duration,distance,startDay,startTime,endDay,endTime,pilotType,@user);

SET createdPilotTripId = LAST_INSERT_ID();
	                        
SET result = 1;
SET errorMessage = 'SUCCESS';

COMMIT;

END//
DELIMITER ;


-- Dumping structure for procedure crewlink_db.CreatePilotTripWithTrain
DROP PROCEDURE IF EXISTS `CreatePilotTripWithTrain`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` PROCEDURE `CreatePilotTripWithTrain`(IN `name` VARCHAR(50), IN `fromStation` VARCHAR(50), IN `toStation` VARCHAR(50), IN `duration` BIGINT, IN `distance` BIGINT, IN `startDay` INT, IN `startTime` TIME, IN `endDay` INT, IN `endTime` TIME, IN `pilotType` VARCHAR(50), IN `user` BIGINT, OUT `createdPilotTripId` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(50))
BEGIN

START TRANSACTION;
    SET @fs = fromStation;
	 SET @ts = toStation;
    SET @pt= pilotType;
   
    SET @fsid = 0;
    SET @tsid = 0; 
    SET @ptid = 0;

	 IF @fs IS NOT NULL THEN
	 SELECT s.id into @fsid FROM 
							  station AS s
							  WHERE s.code = @fs;
    END IF;

	 IF @ts IS NOT NULL THEN
	 SELECT s.id into @tsid FROM 
							  station AS s
							  WHERE s.code = @fs;
    END IF;
    
    IF @pt IS NOT NULL THEN
	 SELECT pt.id into @ptid FROM 
							  pilot_type AS pt
							  WHERE pt.name = @pt;
    END IF;
     
	    
    CALL CreatePilotTrip(name,@fsid,@tsid,duration,distance,@ptid,startDay,startTime,endDay,endTime,user,
	                              createdPilotTripId,result,errorMessage);
   
    
COMMIT;




END//
DELIMITER ;


-- Dumping structure for procedure crewlink_db.CreateRoundTrip
DROP PROCEDURE IF EXISTS `CreateRoundTrip`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` PROCEDURE `CreateRoundTrip`(IN `drivingDutyIds` VARCHAR(2048), IN `orderNos` VARCHAR(2048), IN `crewType` BIGINT, IN `userPlan` BIGINT, OUT `createdRoundTrip` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
    COMMENT 'This creates round trips with list of driving duties and order n'
BEGIN
DECLARE tDistance BIGINT DEFAULT 0;
DECLARE tDuration BIGINT DEFAULT 0;
DECLARE tOutRest BIGINT DEFAULT 0;
DECLARE dd_id VARCHAR(255);
DECLARE rt_order_no VARCHAR(255);
DECLARE done BOOLEAN DEFAULT FALSE;

DECLARE curs CURSOR FOR
        SELECT  keyVal, value FROM splitStringTempTable ORDER BY value *1 ASC;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;


DECLARE exit handler for sqlexception
  BEGIN
    
    SET result = 0;
    SET errorMessage = 'Error occured in Creating Round Trip';
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
SET @fs1 = 0;
SET @ts2 = 0;

SELECT keyVal INTO @dd_id_first  FROM splitStringTempTable ORDER BY value *1 ASC LIMIT 0,1;
SELECT keyVal INTO @dd_id_last  FROM splitStringTempTable ORDER BY value *1 DESC LIMIT 0,1;

SELECT COUNT(dd1.id) INTO @fs1
FROM driving_duty as dd1 INNER JOIN driving_duty as dd2 ON (dd1.to_station = dd2.from_station)
WHERE dd2.id = @dd_id_first
     AND dd1.id = @dd_id_last;

IF (@fs1 > 0) THEN

	SET @trains = NULL;
	    SELECT GROUP_CONCAT( DISTINCT dd.dd_name SEPARATOR ' ; ')  INTO @trains
	    FROM driving_duty AS dd JOIN splitStringTempTable as ss ON (ss.keyVal = dd.id)
    ORDER BY ss.value *1 ASC;
	
	
	SELECT SUM(distance) INTO tDistance
	  FROM driving_duty as dd
	    WHERE FIND_IN_SET(dd.id, drivingDutyIds);
	
	
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
						DELETE FROM crew_link
						  WHERE id IN ( SELECT crew_link
						                FROM round_trip
						                WHERE id = @existingRT );
					
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
	               FROM driving_duty as dd1 INNER JOIN driving_duty as dd2 ON (dd1.to_station = dd2.from_station)
	                 WHERE dd2.id = dd_id
	                       AND dd1.id = @last_dd;
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
	            SET @aDuration = 16*60;    
	        END IF;
	        
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
ELSE
	SET result = false;
	SET errorMessage = 'ERROR : Start station and End station did not match';
END IF;

IF ( result = 1 ) THEN
  SET result = 1;
	SET errorMessage = 'SUCCESS';
	COMMIT;
ELSE
	ROLLBACK;
END IF;

END//
DELIMITER ;


-- Dumping structure for procedure crewlink_db.CreateTrain
DROP PROCEDURE IF EXISTS `CreateTrain`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` PROCEDURE `CreateTrain`(IN `trainNo` INT, IN `name` VARCHAR(255), IN `fromStation` VARCHAR(255), IN `toStation` VARCHAR(255), IN `startDay` VARCHAR(255), IN `trainType` VARCHAR(255), OUT `createdTrainId` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
BEGIN
DECLARE fsid BIGINT DEFAULT 0;
DECLARE tsid BIGINT DEFAULT 0;
DECLARE trainId BIGINT DEFAULT 0;
DECLARE ttid BIGINT DEFAULT 0;
DECLARE startDayVal BIGINT;
DECLARE trainIdExist BIGINT;
DECLARE done int;

DECLARE curs1 CURSOR FOR
        SELECT  value FROM splitStringTempTable;

DECLARE curs CURSOR FOR
SELECT t.id
		FROM train as t
	   WHERE t.train_no = trainNo
              AND NOT FIND_IN_SET(t.start_day, startDay);

DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

DECLARE exit handler for sqlexception
  BEGIN
    SET result = 0;
    SET errorMessage = 'Error occured in Creating Train';
  ROLLBACK;
END;

SET result = 1;
CALL SplitStringToTempTable(startDay, NULL, NULL, ',');

START TRANSACTION;
    SET @fs = fromStation;
	  SET @ts = toStation;
    SET @tt= trainType;
    SET @fsidcount = 0;
    SET @tsidcount = 0; 
    SET @ttcount = 0;
    SET @trainidcount=0;
    SET @trainNo=trainNo;
    SET @trainName=name;
    SET @startDay=startDay;

    SET @isTrainIdExist=0;
    SET @dbTrainStartDay=0;
    
    
  OPEN curs;
      curs_loop : LOOP
				FETCH curs INTO trainIdExist; 

        DELETE from train  WHERE id=trainIdExist;
				IF done = 1 THEN
					LEAVE curs_loop;
				END IF;
     END LOOP;
		CLOSE curs;

 SET done=0;

  OPEN curs1;

			curs_loop : LOOP
				FETCH curs1 INTO startDayVal; 

				IF done = 1 THEN
					LEAVE curs_loop;
				END IF;

       
   
		SELECT COUNT(s.id) into @fsidcount FROM 
							  station AS s
							  WHERE s.code = @fs;
		

		IF (@fsidcount != 1) THEN
				INSERT INTO station (code,head_station_sign_off_duration,head_station_sign_on_duration,name,number_of_beds,out_station_sign_off_duration,out_station_sign_on_duration)VALUES(@fs,30,30,'',1,30,30);
				SET fsid = LAST_INSERT_ID();
		ELSE
		        UPDATE station s SET s.head_station_sign_off_duration=30,
		                             s.head_station_sign_on_duration = 30,
		                             s.number_of_beds=1,
		                             s.out_station_sign_off_duration=30,
		                             s.out_station_sign_on_duration=30
		        WHERE s.code=@fs;
            SELECT s.id into fsid FROM 
							  station AS s
							  WHERE s.code = @fs;
		      
		END IF;
	
	
		SELECT COUNT(s.id) into @tsidcount FROM 
							  station AS s
							  WHERE s.code = @ts;
	
		IF (@tsidcount != 1) THEN
				INSERT INTO station (code,head_station_sign_off_duration,head_station_sign_on_duration,name,number_of_beds,out_station_sign_off_duration,out_station_sign_on_duration)VALUES(toStation,30,30,'',1,30,30);
				SET tsid = LAST_INSERT_ID();
		ELSE
		        UPDATE station s SET s.head_station_sign_off_duration=30,
		                             s.head_station_sign_on_duration = 30,
		                             s.number_of_beds=1,
		                             s.out_station_sign_off_duration=30,
		                             s.out_station_sign_on_duration=30
		        WHERE s.code=@ts;
		        
            SELECT s.id into tsid FROM 
							  station AS s
							  WHERE s.code = @ts;
		END IF;
		
		SELECT COUNT(tt.id) into @ttcount
		FROM train_type as tt
		WHERE tt.name = @tt;
		
		IF (@ttcount != 1) THEN
				INSERT INTO train_type(name) VALUES(trainType);
				SET ttid = LAST_INSERT_ID();
    ELSE
        SELECT tt.id into ttid
  		  FROM train_type as tt
  		  WHERE tt.name = @tt;
		
		END IF;
	
		
		SELECT COUNT(t.id) into @trainidcount 
		FROM train as t
		WHERE t.train_no=@trainNo and t.start_day=startDayVal;
			
		IF (@trainidcount != 1) THEN
					
					INSERT INTO train(name,start_day,train_no,from_station,to_station,train_type) VALUES(@trainName,startDayVal,@trainNo,fsid,tsid,ttid);
			SET createdTrainId = LAST_INSERT_ID();
		ELSE
				
				UPDATE train SET name=@trainName,
							        from_station=fsid,
							        to_station=tsid,
							        train_type=ttid
				WHERE train_no=@trainNo and start_day=startDayVal;

        SELECT t.id INTO createdTrainId FROM train AS t WHERE train_no=@trainNo and start_day=startDayVal;
	END IF;

    END LOOP;
		CLOSE curs1;

IF ( result = 1 ) THEN
	SET result = 1;
	SET errorMessage = 'SUCCESS';
	COMMIT;
ELSE
	ROLLBACK;
END IF;
END//
DELIMITER ;


-- Dumping structure for procedure crewlink_db.CreateTrainStations
DROP PROCEDURE IF EXISTS `CreateTrainStations`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` PROCEDURE `CreateTrainStations`(IN `trainNo` INT, IN `stopNumber` INT, IN `stationCode` VARCHAR(255), IN `dayOfJourney` INT, IN `arrivalTime` VARCHAR(255), IN `departureTime` VARCHAR(255), IN `distance` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
    COMMENT 'This will create or update Train Stations'
BEGIN
DECLARE t_id BIGINT;
DECLARE i int DEFAULT 0;
DECLARE startDay VARCHAR(255);

DECLARE done BOOLEAN DEFAULT FALSE;
DECLARE curs CURSOR FOR
        SELECT t.id,t.start_day FROM train as t WHERE t.train_no = trainNo;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

DECLARE exit handler for sqlexception
BEGIN
	SET result = 0;
	SET errorMessage = 'Error occured in Upload Train_running_details';
	ROLLBACK;
END;

SET @trainCount = 0;
SELECT COUNT(t.id) INTO @trainCount  FROM train as t WHERE t.train_no = trainNo;

OPEN curs;
        SET done = FALSE;
        curs_loop: LOOP
        FETCH curs INTO t_id,startDay;
        IF i = @trainCount THEN LEAVE curs_loop; END IF;
        

START TRANSACTION;
        SET @ts_id = NULL;
        SELECT ts.id into @ts_id FROM train_station as ts WHERE ts.train = t_id AND ts.stop_number = stopNumber;
        SET @station = NULL;
        SELECT s.id into @station FROM station as s WHERE s.code = stationCode;
        IF( @station IS NULL ) THEN
             
          INSERT INTO station (code,head_station_sign_off_duration,head_station_sign_on_duration,name,number_of_beds,out_station_sign_off_duration,out_station_sign_on_duration)VALUES(stationCode,30,30,'',1,30,30);

        END IF;
        IF @ts_id IS NOT NULL THEN
          SET @ts1Dep = NULL;
          SET @ts1Day = NULL;
          SET @ts1Arr = NULL;
          SET @ts1JD = NULL;
          SELECT ts1.departure,ts1.day,ts1.arrival, ts1.journey_duration INTO @ts1Dep,@ts1Day, @ts1Arr, @ts1JD FROM train_station as ts1 WHERE ts1.train = t_id AND ts1.stop_number = (stopNumber-1);
          IF(@ts1Dep IS NULL) THEN
            
            UPDATE train_station  SET arrival = arrivalTime,
                                       departure = departureTime,
                                       day_of_journey= dayOfJourney,
                                       distance = distance,
                                       station = @station,
                                       stop_number= stopNumber,
                                       train = t_id,
                                       journey_duration = 0,
                                       day = (startDay + dayOfJourney -1) % 7
                                       WHERE id = @ts_id;
          ELSE
          	SET @ts1ADay= @ts1Day;
          	SET @ts1DDay= @ts1Day;
            IF( TIMEDIFF(CAST(@ts1Dep as TIME), CAST(@ts1Arr as TIME)) <0 ) THEN
              SET @ts1Day = (@ts1Day +1) % 7;
              SET @ts1DDay = @ts1Day;
            END IF;
            SET @ts2Day = (startDay + dayOfJourney - 1) % 7;
            SET @ts2JD = ((TIME_TO_SEC(TIMEDIFF(CAST(arrivalTime as TIME), CAST(@ts1Dep as TIME)))/60) +(IF(@ts2Day - @ts1Day < 0 , 7 - (@ts1Day - @ts2Day), (@ts2Day - @ts1Day))*1440)) + @ts1JD;
            IF( @ts1Arr NOT LIKE '%0:00:00%' ) THEN
					SET @ts2JD = @ts2JD + ((TIME_TO_SEC(TIMEDIFF(CAST(@ts1Dep as TIME), CAST(@ts1Arr as TIME)))/60) +(IF(@ts1DDay - @ts1ADay < 0 , 7 - (@ts1ADay - @ts1DDay), (@ts1DDay - @ts1ADay))*1440));
				END IF;
				UPDATE train_station SET arrival = arrivalTime,
                                       departure = departureTime,
                                       day_of_journey= dayOfJourney,
                                       distance = distance,
                                       station = @station,
                                       stop_number= stopNumber,
                                       train = t_id,
                                       journey_duration = @ts2JD,
                                       day = @ts2Day
                                       WHERE id = @ts_id;

          END IF;
        ELSE
          
          SET @ts1Dep = NULL;
          SET @ts1Day = NULL;
          SET @ts1Arr = NULL;
          SET @ts1JD = NULL;
          SELECT ts1.departure,ts1.day,ts1.arrival, ts1.journey_duration INTO @ts1Dep,@ts1Day, @ts1Arr, @ts1JD FROM train_station as ts1 WHERE ts1.train = t_id AND ts1.stop_number = (stopNumber-1);
          IF(@ts1Dep IS NULL) THEN
            INSERT INTO train_station (arrival,
                                       day,
                                       day_of_journey,
                                       departure,
                                       distance,
                                       journey_duration,
                                       stop_number,
                                       station,
                                       train)VALUES(arrivalTime,
                                                    (startDay + dayOfJourney -1) % 7,
                                                    dayOfJourney,
                                                    departureTime,
                                                    distance,
                                                    0,
                                                    stopNumber,
                                                    @station,
                                                    t_id);
          ELSE
            SET @ts1ADay= @ts1Day;
          	SET @ts1DDay= @ts1Day;
            IF( TIMEDIFF(CAST(@ts1Dep as TIME), CAST(@ts1Arr as TIME)) <0 ) THEN
              SET @ts1Day = (@ts1Day +1) % 7;
              SET @ts1DDay = @ts1Day;
            END IF;
            SET @ts2Day = (startDay + dayOfJourney - 1) % 7;
            SET @ts2JD = ((TIME_TO_SEC(TIMEDIFF(  CAST(arrivalTime as TIME), CAST(@ts1Dep as TIME)))/60) +(IF(@ts2Day - @ts1Day < 0 , 7 - (@ts1Day - @ts2Day), (@ts2Day - @ts1Day))*1440)) + @ts1JD;
            IF( @ts1Arr NOT LIKE '%0:00:00%' ) THEN
            	SET @ts2JD = @ts2JD + ((TIME_TO_SEC(TIMEDIFF(CAST(@ts1Dep as TIME), CAST(@ts1Arr as TIME)))/60) +(IF(@ts1DDay - @ts1ADay < 0 , 7 - (@ts1ADay - @ts1DDay), (@ts1DDay - @ts1ADay))*1440));
				END IF;
				INSERT INTO train_station (arrival,
                                      day,
                                      day_of_journey,
                                      departure,
                                      distance,
                                      journey_duration,
                                      stop_number,
                                      station,
                                      train)VALUES(arrivalTime,
                                                   @ts2Day,
                                                   dayOfJourney,
                                                   departureTime,
                                                   distance,
                                                   @ts2JD,
                                                   stopNumber,
                                                   @station,
                                                   t_id);


          END IF;
        END IF;
       
    
    SET result = 1;
	  SET errorMessage = 'SUCCESS';
  COMMIT;
  SET i = i + 1;
  END LOOP;
CLOSE curs;
  
END//
DELIMITER ;


-- Dumping structure for procedure crewlink_db.dashBoardItems
DROP PROCEDURE IF EXISTS `dashBoardItems`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` PROCEDURE `dashBoardItems`(IN `userPlan` BIGINT, IN `station` VARCHAR(255), OUT `pTrains` INT, OUT `pDrivingSections` INT, OUT `pDrivingDuties` INT, OUT `pRoundTrips` INT, OUT `cCrewlinks` INT, OUT `noOfLocoPilots` INT, OUT `totalKM` DOUBLE, OUT `pilotKM` DOUBLE, OUT `trainKM` DOUBLE, OUT `avgDutyHrsPer14Days` DOUBLE, OUT `noOfRTPerMonthPerCrew` DOUBLE, OUT `pAvgDutyHrs` DOUBLE, OUT `pAvgOSR` DOUBLE, OUT `pAvgHQR` DOUBLE)
    COMMENT 'This stored Procedure is used for displaying pending items'
BEGIN

SELECT COUNT(id) INTO  pTrains
FROM train
WHERE id NOT IN (SELECT train FROM driving_section WHERE user_plan = userPlan)
AND id IN (SELECT trains FROM user_trains WHERE users IN (SELECT user FROM user_plan WHERE id = userPlan));

SELECT COUNT(id) INTO pDrivingSections
FROM driving_section
WHERE driving_duty_element IS NULL
AND user_plan = userPlan
AND is_ignore = false;

SELECT COUNT(id) INTO pDrivingDuties
FROM driving_duty
WHERE round_trip IS NULL
AND user_plan = userPlan
AND is_ignore = false;

SELECT COUNT(id) INTO pRoundTrips
FROM round_trip
WHERE crew_link IS NULL
AND user_plan = userPlan
AND is_ignore = false;

SELECT COUNT(id) INTO cCrewlinks
FROM crew_link
WHERE user_plan = userPlan;

SET @st = station;
IF (@st = '' OR @st IS NULL) THEN
	SET @totalDuration = null;
	
	SELECT SUM(cl.no_of_lp) , 
			SUM(cl.distance) ,
			SUM(cl.duration + cl.total_head_station_rest_time + cl.total_out_station_rest_time)
				INTO noOfLocoPilots, totalKM, @totalDuration
	FROM crew_link as cl 
	WHERE cl.user_plan = userPlan;
	
	SELECT SUM(IFNULL(sp.distance,0) + IFNULL(ep.distance,0)) INTO pilotKM 
	FROM crew_link as cl LEFT JOIN round_trip as rt ON ( rt.crew_link = cl.id )
	LEFT JOIN driving_duty as dd ON ( dd.round_trip = rt.id)
	LEFT JOIN driving_duty_element as dde ON ( dde.driving_duty = dd.id )
	LEFT JOIN pilot_trip as sp ON (sp.id = dde.start_pilot_trip)
	LEFT JOIN pilot_trip as ep ON (ep.id = dde.end_pilot_trip )
	LEFT JOIN driving_section as ds ON (ds.driving_duty_element = dd.id)
	WHERE cl.user_plan = userPlan;
	
	SET trainKM = totalKM - pilotKM;
	
	SELECT ((SUM((SELECT COUNT(id) FROM round_trip as rt WHERE rt.crew_link = cl.id))/ noOfLocoPilots)*30)/7,
	((SUM(cl.duration)/noOfLocoPilots)/60)*2,
	(((SUM(cl.duration)/168)/noOfLocoPilots)*100)/60,
	(((SUM(cl.total_out_station_rest_time)/168)/noOfLocoPilots)*100)/60,
	(((SUM(cl.total_head_station_rest_time)/168)/noOfLocoPilots)*100)/60
				INTO noOfRTPerMonthPerCrew,avgDutyHrsPer14Days,pAvgDutyHrs, pAvgOSR,  pAvgHQR
	FROM crew_link as cl 
	WHERE cl.user_plan = userPlan
	ORDER BY cl.link_name;

ELSE 
SET @totalDuration = null;
	
	SELECT SUM(cl.no_of_lp) , 
		SUM(cl.distance) ,
		SUM(cl.duration + cl.total_head_station_rest_time + cl.total_out_station_rest_time)
			INTO noOfLocoPilots, totalKM, @totalDuration
		FROM crew_link as cl 
		RIGHT JOIN (
		SELECT cl1.id as id, bs1.code as st
		FROM crew_link as cl1 
		INNER JOIN round_trip as rt1 ON (cl1.id = rt1.crew_link)
		LEFT JOIN station as bs1 ON (bs1.id = rt1.station)
		WHERE cl1.user_plan = userPlan
		GROUP BY cl1.id
		) as cld ON (cl.id = cld.id AND cld.st = @st)
		WHERE cl.user_plan = userPlan
		; 	
	
	SELECT SUM(IFNULL(sp.distance,0) + IFNULL(ep.distance,0)) INTO pilotKM 
	FROM crew_link as cl 
	RIGHT JOIN (
		SELECT cl1.id as id, bs1.code as st
		FROM crew_link as cl1 
		INNER JOIN round_trip as rt1 ON (cl1.id = rt1.crew_link)
		LEFT JOIN station as bs1 ON (bs1.id = rt1.station)
		WHERE cl1.user_plan = userPlan
		GROUP BY cl1.id
		) as cld ON (cl.id = cld.id AND cld.st = @st)
	LEFT JOIN round_trip as rt ON ( rt.crew_link = cl.id )
	LEFT JOIN driving_duty as dd ON ( dd.round_trip = rt.id)
	LEFT JOIN driving_duty_element as dde ON ( dde.driving_duty = dd.id )
	LEFT JOIN pilot_trip as sp ON (sp.id = dde.start_pilot_trip)
	LEFT JOIN pilot_trip as ep ON (ep.id = dde.end_pilot_trip )
	LEFT JOIN driving_section as ds ON (ds.driving_duty_element = dd.id)
	WHERE cl.user_plan = userPlan;
	
	SET trainKM = totalKM - pilotKM;
	
	SELECT ((SUM((SELECT COUNT(id) FROM round_trip as rt WHERE rt.crew_link = cl.id))/ noOfLocoPilots)*30)/7,
	((SUM(cl.duration)/noOfLocoPilots)/60)*2,
	(((SUM(cl.duration)/168)/noOfLocoPilots)*100)/60,
	(((SUM(cl.total_out_station_rest_time)/168)/noOfLocoPilots)*100)/60,
	(((SUM(cl.total_head_station_rest_time)/168)/noOfLocoPilots)*100)/60
				INTO noOfRTPerMonthPerCrew,avgDutyHrsPer14Days,pAvgDutyHrs, pAvgOSR,  pAvgHQR
	FROM crew_link as cl 
	RIGHT JOIN (
		SELECT cl1.id as id, bs1.code as st
		FROM crew_link as cl1 
		INNER JOIN round_trip as rt1 ON (cl1.id = rt1.crew_link)
		LEFT JOIN station as bs1 ON (bs1.id = rt1.station)
		WHERE cl1.user_plan = userPlan
		GROUP BY cl1.id
		) as cld ON (cl.id = cld.id AND cld.st = @st)
	WHERE cl.user_plan = userPlan
	ORDER BY cl.link_name;


END IF;

END//
DELIMITER ;


-- Dumping structure for function crewlink_db.getFirstOrLastDDStation
DROP FUNCTION IF EXISTS `getFirstOrLastDDStation`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` FUNCTION `getFirstOrLastDDStation`(`rt_id` BIGINT, `isLast` TINYINT) RETURNS bigint(20)
    READS SQL DATA
    DETERMINISTIC
BEGIN
SET @result = null;
IF(isLast = 0) THEN
	SELECT dd.from_station INTO @result FROM driving_duty as dd
			WHERE dd.round_trip = rt_id
         ORDER BY dd.round_trip_order_no ASC
			LIMIT 1;
ELSE
	SELECT dd.to_station INTO @result FROM driving_duty as dd
			WHERE dd.round_trip = rt_id
         ORDER BY dd.round_trip_order_no DESC
			LIMIT 1;
END IF;

RETURN @result;

END//
DELIMITER ;


-- Dumping structure for procedure crewlink_db.SplitStringToTempTable
DROP PROCEDURE IF EXISTS `SplitStringToTempTable`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` PROCEDURE `SplitStringToTempTable`(IN `fullString1` VARCHAR(2048), IN `fullString2` VARCHAR(2048), IN `fullString3` VARCHAR(2048), IN `seperator` VARCHAR(1))
    COMMENT 'This splits string into table  with a seperator'
BEGIN
SET @Occurrences1 = NULL;
SET @Occurrences2 = NULL;
SET @Occurrences3 = NULL;

	IF(fullString1 IS NOT NULL AND  fullString2 IS NULL AND fullString3 IS NULL) THEN
	   DROP TEMPORARY TABLE IF EXISTS splitStringTempTable;
	   CREATE TEMPORARY TABLE splitStringTempTable ( value VARCHAR(255));


	   SET @Occurrences1 = LENGTH(fullString1) - LENGTH(REPLACE(fullString1, seperator, ''));
    IF( @Occurrences1 = 0) THEN
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
END//
DELIMITER ;


-- Dumping structure for procedure crewlink_db.UpdateTrainInUserList
DROP PROCEDURE IF EXISTS `UpdateTrainInUserList`;
DELIMITER //
CREATE DEFINER=`crewlink_db`@`localhost` PROCEDURE `UpdateTrainInUserList`(IN `trainNo` INT, IN `isUserSelected` TINYINT, IN `userPlan` BIGINT, OUT `result` TINYINT, OUT `errorMessage` VARCHAR(255))
BEGIN


DECLARE exit handler for sqlexception
  BEGIN
    
    SET result = 0;
    SET errorMessage = 'Error occured in Updating Trains in User List';
  ROLLBACK;
END;
							                          
START TRANSACTION;
	SET @user = NULL;
	SELECT DISTINCT user INTO @user FROM user_plan WHERE id = userPlan LIMIT 0,1;

	IF( isUserSelected = true ) THEN
		INSERT INTO user_trains (users,trains) SELECT @user, t.id FROM train as t WHERE t.train_no = trainNo;
	ELSE
		DELETE FROM user_trains WHERE users = @user AND trains IN (SELECT id FROM train WHERE train_no = trainNo);
	END IF;


	SET result = 1;
	SET errorMessage = 'SUCCESS';
COMMIT;
      
          
      
      
END//
DELIMITER ;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
