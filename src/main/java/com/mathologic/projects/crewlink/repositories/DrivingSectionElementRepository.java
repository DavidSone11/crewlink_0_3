//package com.mathologic.projects.crewlink.repositories;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//
//import com.mathologic.projects.crewlink.models.Day;
//import com.mathologic.projects.crewlink.models.DrivingSectionElement;
//import com.mathologic.projects.crewlink.models.TrainStation;
//
//@RepositoryRestResource
//public interface DrivingSectionElementRepository extends JpaRepository<DrivingSectionElement, Long> {
//	
//
//	@Query("Select dse from DrivingSectionElement as dse where "
//			+ "( dse.trainStation.train.trainNo = :trainNo ) AND "
//			+ "( dse.trainStation.train.startDay = :startDay ) AND "
//			+ "( dse.userPlan.id = :userPlanId ) ")
//	Page<DrivingSectionElement> findByAllParams(@Param("trainNo")Integer trainNo, @Param("startDay") Day startDay,
//			@Param("userPlanId")Long userPlanId, Pageable pageable);
//	
//	
//}
