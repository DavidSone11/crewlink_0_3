package com.mathologic.projects.crewlink.custom.repositories;


import com.mathologic.projects.crewlink.custom.models.SelectViewModel;

/**
 * This repository contains declarations of find by name of train type
 * 
 * @author Jagdish
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
public interface TrainTypeVMRepository 
{
	public SelectViewModel findByName(String name);
	
	
}
