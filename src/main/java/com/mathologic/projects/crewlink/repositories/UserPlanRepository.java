package com.mathologic.projects.crewlink.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mathologic.projects.crewlink.models.UserPlan;

@RepositoryRestResource
public interface UserPlanRepository extends JpaRepository<UserPlan, Long> {
	Page<UserPlan> findByUser_Username(@Param("username")String username,Pageable pageable);
	
	@Query("Select up from UserPlan as up where ( up.name LIKE %:name% or :name is '%%' or :name is null ) AND "
			+ "( up.user.username = :username )")
	Page<UserPlan> findByAllParams(@Param("name")String name, @Param("username") String username, Pageable pageable);

}
