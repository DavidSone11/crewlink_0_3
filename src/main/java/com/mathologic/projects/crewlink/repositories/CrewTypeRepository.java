package com.mathologic.projects.crewlink.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mathologic.projects.crewlink.models.CrewType;

@RepositoryRestResource
public interface CrewTypeRepository extends JpaRepository<CrewType, Long> {
	@Query("Select ct from CrewType as ct where "
			+ "( ct.name LIKE %:name% or :name is '%%' or :name is null ) "
			)
	Page<CrewType > findByNameContains( @Param("name") String name, Pageable pageable);
}
