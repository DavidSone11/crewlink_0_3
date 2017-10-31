package com.mathologic.projects.crewlink.custom.repositories;


import com.mathologic.projects.crewlink.custom.models.SelectViewModel;

/**
 * This repository contains declarations of list stations and find by code
 * 
 * @author Jagdish
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
public interface StationVMRepository 
{
	public SelectViewModel listStations(Long id,String code,String name,int noOfBeds,Long headStationSignOffDuration,
			Long headStationSignOnDuration,Long outStationSignOffDuration, Long outStationSignOnDuration,String sort, Long page, Long size);
	
	public SelectViewModel findByCode(String code,String sort, Long page, Long size);
	
	/*public ProcessResult insertStation(String code, String name,int noOfBeds,Long headStationSignOffDuration,
			Long headStationSignOnDuration,Long outStationSignOffDuration, Long outStationSignOnDuration);*/
}
