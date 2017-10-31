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
import com.mathologic.projects.crewlink.models.RoundTrip;

@RepositoryRestResource
public interface RoundTripRepository extends JpaRepository<RoundTrip, Long> {
	
	@Query("Select rr from RoundTrip as rr WHERE "
			+ "("
			+ "		( rr.crewLink.id >= 0 AND :crewLinkShowNotNull = 'true') "
			+ "								OR 														"
			+ "		( rr.crewLink IS NULL AND :crewLinkShowNull = 'true') "
			+ "								OR 														"
			+ "		( :crewLinkShowNotNull IS NULL AND :crewLinkShowNull IS NULL )) AND "
			+ "( rr.userPlan.id = :userPlanId ) AND "
			+ "( rr.startDay LIKE %:startDay% OR :startDay is null ) AND "
			+ "( rr.station.code LIKE %:station% OR :station is '%%' OR :station is null) AND"
			+ "( rr.startTime >= :minStartTime OR :minStartTime is null ) AND "
			+ "( rr.startTime <= :maxStartTime OR :maxStartTime is null ) AND"
			+ "( rr.duration >= :minDuration OR :minDuration is null ) AND "
			+ "( rr.duration <= :maxDuration OR :maxDuration is null ) AND "
			+ "( rr.distance >= :minDistance OR :minDistance is null ) AND "
			+ "( rr.distance <= :maxDistance OR :maxDistance is null ) AND"
			+ "( rr.crewType.name LIKE  %:crewType% OR :crewType is '%%' OR :crewType is null)")
			
			
			Page<RoundTrip> findByAllParams(
		 	@Param("userPlanId")Long userPlanId,
			@Param("startDay")Day startDay,
			@Param("station")String station,
			@Param("minStartTime")@DateTimeFormat(pattern="HH:mm")LocalTime minStartTime,
			@Param("maxStartTime")@DateTimeFormat(pattern="HH:mm")LocalTime maxStartTime,
			@Param("minDuration")Long minDuration,
			@Param("maxDuration")Long maxDuration, @Param("minDistance")Long minDistance,
			@Param("maxDistance")Long maxDistance,
			@Param("crewLinkShowNull")String crewLinkShowNull,
			@Param("crewLinkShowNotNull")String crewLinkShowNotNull,
			@Param("crewType")String crewType,
			Pageable pageable);
	
	
			@Query("SELECT COUNT(rr.id) FROM RoundTrip as rr WHERE rr.crewLink IS NULL AND  "
			+ "( rr.userPlan.id = :userPlanId )")
			Long unusedRoundTrip(@Param("userPlanId")Long userPlanId);
	 
	
}


		
