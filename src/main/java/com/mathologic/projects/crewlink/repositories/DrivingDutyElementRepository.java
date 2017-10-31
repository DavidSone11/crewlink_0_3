package com.mathologic.projects.crewlink.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mathologic.projects.crewlink.models.DrivingDutyElement;

@RepositoryRestResource
public interface DrivingDutyElementRepository extends JpaRepository<DrivingDutyElement, Long> {
	
}