package com.mathologic.projects.crewlink.custom.repositories;

import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.models.Day;

/**
 * This repository interface contains declarations of list driving sections saves 
 * driving section for single day, for all days, for all days plus driving duty .
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
public interface DrivingSectionVMRepository {
	ProcessResult saveDrivingSectionsForSingleTrain(Integer trainNo,
			Day startDay, String stopNumbers, Long userPlan);

	ProcessResult saveDrivingSectionsForTrainAllDays(Integer trainNo,
			String stopNumbers, Long userPlan);

	ProcessResult saveDrivingSectionsAndDrivingDutiesForTrainAllDays(
			Integer trainNo, String stopNumbers, Long userPlan);
	
	SelectViewModel listDrivingSections(Integer trainNo, Day originDay, String fromStation, String toStation, 
			Day departureDay, String minDepartureTime, String maxDepartureTime, Day arrivalDay, String minArrivalTime,
			String maxArrivalTime, Long minDuration, 
			Long maxDuration, Long minDistance, Long maxDistance, Boolean isDrivingDuty,Boolean isIgnore, Long userPlan,
			String sort, Long page, Long size);
	
	ProcessResult updateDrivingSectionInUserList(Long id, Boolean isIgnore,
			Long userPlan);
		
	
}