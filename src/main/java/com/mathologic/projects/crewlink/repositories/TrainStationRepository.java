package com.mathologic.projects.crewlink.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mathologic.projects.crewlink.models.Day;
import com.mathologic.projects.crewlink.models.TrainStation;

@RepositoryRestResource
public interface TrainStationRepository extends JpaRepository<TrainStation, Long> {
	TrainStation findByTrain_TrainNoAndTrain_StartDayAndStopNumber(@Param("TrainNo")Integer trainNo, @Param("StartDay")Day startDay, @Param("StopNumber")Integer stopNumber);
	
	TrainStation findTopByTrain_TrainNoAndTrain_StartDayOrderByStopNumberDesc(@Param("TrainNo")Integer trainNo, @Param("StartDay")Day startDay);
	
	@Query("Select ts from TrainStation as ts where "
			+ "( ts.train.trainNo = :trainNo ) AND "
			+ "( ts.train.startDay = :startDay ) AND "
			+ "( ts.station.code LIKE %:station% or :station is '%%' or :station is null ) AND "
			+ "( ts.stopNumber = :stopNumber or :stopNumber is null ) ")
	Page<TrainStation> findByAllParams(@Param("trainNo")Integer trainNo, @Param("startDay") Day startDay,
			@Param("station")String station,@Param("stopNumber")Integer stopNumber, Pageable pageable);
	
	
	   @Query("Select ts from TrainStation as ts where (ts.train.trainNo = :trainNo) AND (ts.day = :startDay )")
	   List<TrainStation> findByTrain_TrainNoAndTrain_StartDay(@Param("trainNo")Integer trainNo, @Param("startDay")Day startDay,Pageable pageable);
	    
	    
	
}
