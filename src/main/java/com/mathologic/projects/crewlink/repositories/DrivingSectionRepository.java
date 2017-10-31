package com.mathologic.projects.crewlink.repositories;


import java.time.LocalTime;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;

import com.mathologic.projects.crewlink.models.Day;
import com.mathologic.projects.crewlink.models.DrivingSection;

@RepositoryRestResource
public interface DrivingSectionRepository extends JpaRepository<DrivingSection, Long> {
	
	@Query("Select ds from DrivingSection as ds WHERE "
			+ "("
			+ "		( ds.drivingDutyElement.id >= 0 AND :drivingDutyElementShowNotNull = 'true') "
			+ "								OR 														"
			+ "		( ds.drivingDutyElement IS NULL AND :drivingDutyElementShowNull = 'true') "
			+ "								OR 														"
			+ "		( :drivingDutyElementShowNotNull IS NULL AND :drivingDutyElementShowNull IS NULL )) AND "
			+ "( ds.userPlan.id = :userPlanId ) AND "
			+ "( ds.train.trainNo = :trainNo OR :trainNo is null ) AND "
			+ "( ds.train.startDay = :trainDay OR :trainDay is null ) AND "
			+ "( ds.fromStation.code LIKE %:fromStation% OR :fromStation is '%%' OR :fromStation is null ) AND "
			+ "( ds.toStation.code LIKE %:toStation% OR :toStation is '%%' OR :toStation is null ) AND "
			+ "( ds.startDay = :startDay OR :startDay is null ) AND "
			+ "( ds.startTime >= :minStartTime OR :minStartTime is null ) AND "
			+ "( ds.startTime <= :maxStartTime OR :maxStartTime is null ) AND "
			+ "( ds.duration >= :minDuration OR :minDuration is null ) AND "
			+ "( ds.duration <= :maxDuration OR :maxDuration is null ) AND "
			+ "( ds.distance >= :minDistance OR :minDistance is null ) AND "
			+ "( ds.distance <= :maxDistance OR :maxDistance is null )  "
			)
			//+ "( ds.drivingDutyElement is not :drivingDutyElementNull) ")
			/*+ "(CASE WHEN (:drivingDutyElementId = 1) THEN ds.drivingDutyElement.id is not null "
			+ " WHEN (:drivingDutyElementId = 2) THEN ds.drivingDutyElement.id is null "
			+ " ELSE 1=1 END )")*/
		    Page<DrivingSection> findByAllParams(
			@Param("trainNo")Integer trainNo,@Param("trainDay")Day trainDay,
			@Param("fromStation")String fromStation, @Param("toStation")String toStation,
			@Param("startDay")Day startDay, @Param("minStartTime")@DateTimeFormat(pattern="HH:mm")LocalTime minStartTime,
			@Param("maxStartTime")@DateTimeFormat(pattern="HH:mm")LocalTime maxStartTime, @Param("minDuration")Long minDuration,
			@Param("maxDuration")Long maxDuration, @Param("minDistance")Long minDistance,
			@Param("maxDistance")Long maxDistance, @Param("userPlanId")Long userPlanId,
			@Param("drivingDutyElementShowNull")String drivingDutyElementShowNull,
			@Param("drivingDutyElementShowNotNull")String drivingDutyElementShowNotNull,
			Pageable pageable);
	
	
	
	
	
	
	
/*	@Query("Select ds from DrivingSection as ds WHERE "
			+ "((( ds.drivingDutyElement.id >= :drivingDutyElementId OR :drivingDutyElementId is null ) AND (:drivingDutyElementShowAll is null)) OR "
			+ "(( ds.drivingDutyElement IS NULL AND :drivingDutyElementShowAllNull IS NULL) AND ( :drivingDutyElementShowAll is not null ))) AND "
			+ "( ds.userPlan.id = :userPlanId ) AND "
			+ "( ds.train.trainNo = :trainNo OR :trainNo is null ) AND "
			+ "( ds.train.startDay = :trainDay OR :trainDay is null ) AND "
			+ "( ds.fromStation.code LIKE %:fromStation% OR :fromStation is '%%' OR :fromStation is null ) AND "
			+ "( ds.toStation.code LIKE %:toStation% OR :toStation is '%%' OR :toStation is null ) AND "
			+ "( ds.startDay = :startDay OR :startDay is null ) AND "
			+ "( ds.startTime >= :minStartTime OR :minStartTime is null ) AND "
			+ "( ds.startTime <= :maxStartTime OR :maxStartTime is null ) AND "
			+ "( ds.duration >= :minDuration OR :minDuration is null ) AND "
			+ "( ds.duration <= :maxDuration OR :maxDuration is null ) AND "
			+ "( ds.distance >= :minDistance OR :minDistance is null ) AND "
			+ "( ds.distance <= :maxDistance OR :maxDistance is null )  "
			)
			//+ "( ds.drivingDutyElement is not :drivingDutyElementNull) ")
			+ "(CASE WHEN (:drivingDutyElementId = 1) THEN ds.drivingDutyElement.id is not null "
			+ " WHEN (:drivingDutyElementId = 2) THEN ds.drivingDutyElement.id is null "
			+ " ELSE 1=1 END )")
	Page<DrivingSection> findByAllParams(
			@Param("trainNo")Integer trainNo,@Param("trainDay")Day trainDay,
			@Param("fromStation")String fromStation, @Param("toStation")String toStation,
			@Param("startDay")Day startDay, @Param("minStartTime")@DateTimeFormat(pattern="HH:mm")LocalTime minStartTime,
			@Param("maxStartTime")@DateTimeFormat(pattern="HH:mm")LocalTime maxStartTime, @Param("minDuration")Long minDuration,
			@Param("maxDuration")Long maxDuration, @Param("minDistance")Long minDistance,
			@Param("maxDistance")Long maxDistance, @Param("userPlanId")Long userPlanId,
			@Param("drivingDutyElementId")Long drivingDutyElementId,
			@Param("drivingDutyElementShowAll")String drivingDutyElementShowAll,
			@Param("drivingDutyElementShowAllNull")String drivingDutyElementShowAllNull,
			Pageable pageable);
	*/
//    @Query("Select ds from driving_section as ds where t.trainNo = :trainNo AND t.startDay = :startDay ")
//    void deleteByTrainNoAndStartDay(@Param("trainNo")Integer trainNo, @Param("startDay")Day startDay);
	
    
    @Query("Select ds from DrivingSection as ds where ds.train.trainNo = :trainNo AND ds.startDay = :startDay ")
    Page<DrivingSection> findByTrainNoAndStartDay(
    		@Param("trainNo")Integer trainNo, @Param("startDay")Day startDay,Pageable pageable);
	
   /* @Modifying
    @Transactional
    @Query("delete from DrivingSection as ds where ds.train = (SELECT id from Train as t where t.trainNo = :trainNo AND t.startDay = :startDay)")
    void deleteInactiveUsers(@Param("trainNo")Integer trainNo, @Param("startDay")Day startDay);
    */
	
	//void deleteByTrain_TrainNo(@Param("trainNo")Integer trainNo);
    
    @Query("SELECT COUNT(ds.id) FROM DrivingSection as ds WHERE ds.drivingDutyElement IS NULL AND  "
			+ "( ds.userPlan.id = :userPlanId )")
   	Long unusedSections(@Param("userPlanId")Long userPlanId);
	
}
