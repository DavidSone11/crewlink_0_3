package com.mathologic.projects.crewlink.custom.repositories;


import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.models.Day;


/**
 * This repository interface contains declarations of list driving duty elements, 
 * delete driving duty elements .
 * 
 * @author Santosh
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
public interface DrivingDutyElementVMRepository {

	SelectViewModel listDrivingElements(Long id,Long drivingduty,Long userPlan,String sort, Long page, Long size);
	ProcessResult save(Long drivingSectionId, Long startPilotId, Long endPilotId ,Long userPlan);
	
	public ProcessResult deleteDrivingDutyElementByDrivingDuty(Long userPlan,Long id);
	
}