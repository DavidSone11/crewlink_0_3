package com.mathologic.projects.crewlink.custom.repositories;

import com.mathologic.projects.crewlink.custom.models.SelectViewModel;


/**
 * 
 * 
 * 
 * @author Santosh 
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 25, 2017
 */
public interface DrivingSectionChartVMRepository {
	
	SelectViewModel getlistOfTrains(int trainNo,Long page, Long size);

	
}