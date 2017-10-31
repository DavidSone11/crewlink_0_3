package com.mathologic.projects.crewlink.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mathologic.projects.crewlink.models.Division;

@RepositoryRestResource
public interface DivisionRepository extends JpaRepository<Division, Long> {
	Page<Division> findByNameContains(@Param("name")String name, Pageable pageable);
	@Query("Select d from Division as d where "
			+ "( d.name LIKE %:name% or :name is '%%' or :name is null ) "
			)
	Page<Division> findByAllParams( @Param("name") String name, Pageable pageable);
}
