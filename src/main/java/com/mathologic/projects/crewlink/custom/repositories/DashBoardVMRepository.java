package com.mathologic.projects.crewlink.custom.repositories;


import com.mathologic.projects.crewlink.custom.models.DashBoardVM;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;

/**
 * This repository interface contains declarations of list driving duties, 
 * delete driving duties .
 * 
 * @author Santosh
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
public interface DashBoardVMRepository {

	DashBoardVM listDashboardItems(Long userPlan, String station);
	
	SelectViewModel listDependencies(String searchItem, String searchValue,
			Long userPlan);
	
}