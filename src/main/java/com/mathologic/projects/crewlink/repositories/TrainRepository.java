package com.mathologic.projects.crewlink.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mathologic.projects.crewlink.models.Day;
import com.mathologic.projects.crewlink.models.Train;

@RepositoryRestResource
public interface TrainRepository extends JpaRepository<Train, Long> {
	Train findByTrainNoAndStartDay(@Param("trainNo") Integer trainNo, @Param("startDay") Day startDay);

	@Query("Select t from Train as t where ( t.trainNo = :trainNo ) ")
	List<Train> findByTrainNumber(@Param("trainNo")Integer trainNo);
	Page<Train> findByTrainNo(@Param("trainNo") Integer trainNo, Pageable pageable);

	Page<Train> findByNameContainsAndFromStation_CodeContainsAndToStation_CodeContains(@Param("name") String name,
			@Param("fromStation") String fromStationCode, @Param("toStation") String toStationCode,
			Pageable pagable);
	


	@Query("Select t from Train as t where ( t.trainNo = :trainNo or :trainNo = null ) AND "
	+ "( t.startDay = :startDay or :startDay = null ) AND"
	+ "( t.trainType.name = :field or :field = null) AND" 
	+ "( t.name LIKE %:name% or :name is '%%' or :name is null ) AND " 
	+ "( t.fromStation.code LIKE %:fromStation%  or :fromStation is '%%' or :fromStation is null ) AND "
	+ "( t.toStation.code LIKE %:toStation% or :toStation is '%%' or :toStation is null  )")
	Page<Train> findByAllParams(@Param("trainNo")Integer trainNo, @Param("name") String name,@Param("fromStation") String fromStationCode, @Param("toStation") String toStationCode, @Param("startDay")Day startDay,@Param("field")String field, Pageable pageable);

    @Query("SELECT COUNT(DISTINCT t.id) FROM Train as t WHERE t.id NOT IN ( SELECT DISTINCT ds.train.id FROM t.drivingSections as ds WHERE "
			+ "( ds.userPlan.id = :userPlanId ))")
	Long unusedTrains(@Param("userPlanId")Long userPlanId);
			 
	
			 
}
