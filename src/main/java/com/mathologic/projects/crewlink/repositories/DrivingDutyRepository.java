package com.mathologic.projects.crewlink.repositories;

import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;

import com.mathologic.projects.crewlink.models.Day;
import com.mathologic.projects.crewlink.models.DrivingDuty;

@RepositoryRestResource
public interface DrivingDutyRepository extends JpaRepository<DrivingDuty, Long> {
	
	@Query("Select dd from DrivingDuty as dd WHERE "
			+ "("
			+ "		( dd.roundTrip.id >= 0 AND :roundTripShowNotNull = 'true') "
			+ "								OR 														"
			+ "		( dd.roundTrip IS NULL AND :roundTripShowNull = 'true') "
			+ "								OR 														"
			+ "		( :roundTripShowNotNull IS NULL AND :roundTripShowNull IS NULL )) AND "
			+ "( dd.fromStation.code LIKE %:fromStation% OR :fromStation is '%%' OR :fromStation is null ) AND "
			+ "( dd.toStation.code LIKE %:toStation% OR :toStation is '%%' OR :toStation is null ) AND "
			+ "( dd.startDay = :startDay OR :startDay is null ) AND "
			+ "( dd.startTime >= :minStartTime OR :minStartTime is null ) AND "
			+ "( dd.startTime <= :maxStartTime OR :maxStartTime is null ) AND "
			+ "( dd.duration >= :minDuration OR :minDuration is null ) AND "
			+ "( dd.duration <= :maxDuration OR :maxDuration is null ) AND "
			+ "( dd.distance >= :minDistance OR :minDistance is null ) AND "
			+ "( dd.distance <= :maxDistance OR :maxDistance is null ) AND "
			+ "( dd.userPlan.id = :userPlanId )")
	Page<DrivingDuty> findByAllParams(@Param("fromStation")String fromStation,
			@Param("toStation")String toStation, @Param("startDay")Day startDay,
			@Param("minStartTime")@DateTimeFormat(pattern="HH:mm")LocalTime minStartTime,
			@Param("maxStartTime")@DateTimeFormat(pattern="HH:mm")LocalTime maxStartTime, 
			@Param("minDuration")Long minDuration,@Param("maxDuration")Long maxDuration,
			@Param("minDistance")Long minDistance, 
			@Param("maxDistance")Long maxDistance,@Param("userPlanId")Long userPlanId, 
			@Param("roundTripShowNull")String roundTripShowNull,
			@Param("roundTripShowNotNull")String roundTripShowNotNull,
			Pageable pageable);
	
	
    @Query("SELECT COUNT(dd.id) FROM DrivingDuty as dd WHERE dd.roundTrip IS NULL AND  "
			+ "( dd.userPlan.id = :userPlanId )")
   	Long unusedDrivingDuty(@Param("userPlanId")Long userPlanId);
}
