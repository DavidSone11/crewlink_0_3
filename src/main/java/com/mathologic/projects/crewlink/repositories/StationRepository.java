package com.mathologic.projects.crewlink.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mathologic.projects.crewlink.models.Station;
import com.mathologic.projects.crewlink.models.Train;

@RepositoryRestResource
public interface StationRepository extends JpaRepository<Station, Long> {
	Station findByCode(@Param("code")String code);
	Page<Station> findByCodeContains(@Param("code")String code, Pageable pageable);
	
	@Query("Select s from Station as s where "
			+ "( s.name LIKE %:name% or :name is '%%' or :name is null ) AND " 
			+ "( s.code LIKE %:code% or :code is '%%' or :code is null  ) "
			)
	Page<Station> findByAllParams(@Param("code")String code, @Param("name") String name, Pageable pageable);
	
}