package com.mathologic.projects.crewlink.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mathologic.projects.crewlink.models.PilotType;
import com.mathologic.projects.crewlink.models.*;

@RepositoryRestResource
public interface PilotTypeRepository extends JpaRepository<PilotType, Long> {
	
	@Query("Select pt from PilotType as pt where "
			+ "( pt.name LIKE %:name% or :name is '%%' or :name is null ) "
			)
	Page<PilotType> findByAllParams( @Param("name") String name, Pageable pageable);
	
	
}
