package com.mathologic.projects.crewlink.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mathologic.projects.crewlink.models.TrainType;

@RepositoryRestResource
public interface TrainTypeRepository extends JpaRepository<TrainType, Long> {
	TrainType findByName(@Param("Name")String name);
	@Query("Select tt from TrainType as tt where "
			+ "( tt.name LIKE %:name% or :name is '%%' or :name is null ) "
			)
	Page<TrainType> findByNameContains( @Param("name") String name, Pageable pageable);
}
