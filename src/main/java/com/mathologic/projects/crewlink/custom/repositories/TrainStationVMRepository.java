package com.mathologic.projects.crewlink.custom.repositories;

import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.models.Day;

/**
 * This repository contains declarations of list train stations with driving section,
 * list train stations, create train stations, and delete train stations.
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
public interface TrainStationVMRepository {
	SelectViewModel listTrainStationsWithDrivingSection(Integer trainNo, Day startDay, String stationCode,Long userPlan, String sort, Long page, Long size);
	SelectViewModel listTrainStations(Integer trainNo, Day startDay, String stationCode, String sort, Long page, Long size);
	ProcessResult createTrainStations(int trainNo,int stopNumber, String stationCode,int dayOfJourney, String arrivalTime, String departureTime, long distance);
    
	ProcessResult deleteTrainStations(Long id);
	
}
