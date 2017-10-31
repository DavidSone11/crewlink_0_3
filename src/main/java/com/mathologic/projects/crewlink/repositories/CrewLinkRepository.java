package com.mathologic.projects.crewlink.repositories;

import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;

import com.mathologic.projects.crewlink.models.CrewLink;
import com.mathologic.projects.crewlink.models.Day;
import com.mathologic.projects.crewlink.models.RoundTrip;

@RepositoryRestResource
public interface CrewLinkRepository extends JpaRepository<CrewLink, Long> {
	
	
	@Query("Select cl from CrewLink as cl WHERE "
			+ "( cl.userPlan.id = :userPlanId ) AND "
			+ "( cl.startDay = :startDay OR :startDay is null ) AND "
			+ "( cl.linkName = :linkName OR :linkName is null ) AND "
			+ "( cl.station.code LIKE %:station% OR :station is '%%' OR :station is null) AND"
			+ "( cl.startTime >= :minStartTime OR :minStartTime is null ) AND "
			+ "( cl.startTime <= :maxStartTime OR :maxStartTime is null ) AND"
			+ "( cl.duration >= :minDuration OR :minDuration is null ) AND "
			+ "( cl.duration <= :maxDuration OR :maxDuration is null ) AND "
			+ "( cl.distance >= :minDistance OR :minDistance is null ) AND "
			+ "( cl.distance <= :maxDistance OR :maxDistance is null )")

			Page<RoundTrip> findByAllParams(
		 	@Param("userPlanId")Long userPlanId,
			@Param("startDay")Day startDay,
			@Param("station")String station,
			@Param("linkName")String linkName,
			@Param("minStartTime")@DateTimeFormat(pattern="HH:mm")LocalTime minStartTime,
			@Param("maxStartTime")@DateTimeFormat(pattern="HH:mm")LocalTime maxStartTime,
			@Param("minDuration")Long minDuration,@Param("maxDuration")Long maxDuration,
			@Param("minDistance")Long minDistance,@Param("maxDistance")Long maxDistance,
			Pageable pageable);
	
			@Query("SELECT COUNT(cc.id) FROM CrewLink as  cc WHERE "
			+ "( cc.userPlan.id = :userPlanId )")
			Long unusedCrewLink(@Param("userPlanId")Long userPlanId);
	
	
	
}
