package com.mathologic.projects.crewlink.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.mathologic.projects.crewlink.models.PilotTrip;



@RepositoryRestResource
public interface PilotTripRepository extends JpaRepository<PilotTrip, Long> {

	
	
	@Query("Select pt from PilotTrip as pt where "
			+ "( pt.name LIKE %:name% or :name is '%%' or :name is null ) AND " 
			+ "( pt.distance = :distance or :distance = null ) AND "
			+ "( pt.duration = :duration or :duration = null ) AND "
			+ "( pt.division.name LIKE %:division% or :division is '%%' or :division is null ) AND " 
			+ "( pt.fromStation.code LIKE %:fromStation%  or :fromStation is '%%' or :fromStation is null ) AND "
			+ "( pt.toStation.code LIKE %:toStation%  or :toStation is '%%' or :toStation is null ) AND "
			+ "( pt.pilotType.name LIKE %:pilotType% or :pilotType is '%%' or :pilotType is null  )"
			)
	
			Page<PilotTrip> findByAllParams(@Param("name") String name , @Param("distance")Long distance, @Param("duration")Long duration, @Param("division") String divisionName, @Param("fromStation") String fromStationCode, @Param("toStation") String toStationCode, @Param("pilotType") String pilotTypeName, Pageable pageable);
	
	@Query("Select pt from PilotTrip as pt where "
			+ "( pt.name LIKE %:name% or :name is '%%' or :name is null ) AND " 
			+ "( pt.distance = :distance or :distance = null ) AND "
			+ "( pt.duration = :duration or :duration = null ) AND "
			+ "( pt.division.name LIKE %:division% or :division is '%%' or :division is null ) AND " 
			+ "( ( pt.fromStation.code LIKE %:fromStation%  or :fromStation is '%%' or :fromStation is null ) OR "
			+ "( pt.toStation.code LIKE %:toStation%  or :toStation is '%%' or :toStation is null ) ) AND "
			+ "( pt.pilotType.name LIKE %:pilotType% or :pilotType is '%%' or :pilotType is null  )"
			)
	
			Page<PilotTrip> findByAllParamsOr(@Param("name") String name , @Param("distance")Long distance, @Param("duration")Long duration, @Param("division") String divisionName, @Param("fromStation") String fromStationCode, @Param("toStation") String toStationCode, @Param("pilotType") String pilotTypeName, Pageable pageable);
	
}
