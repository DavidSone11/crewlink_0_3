package com.mathologic.projects.crewlink.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mathologic.projects.crewlink.models.SubRole;

@RepositoryRestResource
public interface SubRoleRepository extends JpaRepository<SubRole, Long> {
	
}
