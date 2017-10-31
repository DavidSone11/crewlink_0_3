//package com.mathologic.projects.crewlink.repositories;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.query.Param;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//
//import com.mathologic.projects.crewlink.models.HeadStation;
//
//@RepositoryRestResource
//public interface HeadStationRepository extends JpaRepository<HeadStation, Long> {
//	Page<HeadStation> findByStation_CodeContains(@Param("code")String code, Pageable pagable);
//}