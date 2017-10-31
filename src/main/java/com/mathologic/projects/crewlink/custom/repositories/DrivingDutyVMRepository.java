package com.mathologic.projects.crewlink.custom.repositories;


import com.mathologic.projects.crewlink.custom.models.DrivingDutyManyPM;
import com.mathologic.projects.crewlink.custom.models.DrivingDutyPM;
import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.models.Day;

/**
 * This repository interface contains declarations of list driving duties, 
 * delete driving duties .
 * 
 * @author Santosh
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
public interface DrivingDutyVMRepository {

	SelectViewModel listDrivingDuties(Long userPlan,String ddName, String fromStation, String toStation, Long minDuration,
			Long maxDuration, Long minDistance, Long maxDistance,Day arrivalDay,String minArrivalTime,String maxArrivalTime,Day departureDay, String minDepartureTime,
			String maxDepartureTime,Long RoundTrip,Boolean isRoundTrip,Boolean isIgnore, String sort, Long page, Long size);

	ProcessResult save(DrivingDutyPM data, Long userPlan);
	
	ProcessResult saveMany(DrivingDutyManyPM data, Long userPlan);
	
	ProcessResult deleteDrivingDuty(Long userPlan,Long id);

	SelectViewModel listDrivingDutiesWithRT(Long userPlan, String fromStation, String toStation,Boolean isIgnore, String sort, Long page, Long size);

	ProcessResult updateDrivingDutyInUserList(Long id, Boolean isIgnore, Long userPlan);
}