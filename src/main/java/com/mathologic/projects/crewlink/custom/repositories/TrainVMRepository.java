package com.mathologic.projects.crewlink.custom.repositories;

import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.models.Day;

/**
 * This repository contains declarations of list trains and save train
 * 
 * @author Jagdish
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
public interface TrainVMRepository {

	SelectViewModel listTrains(Integer trainNo, Day startDay, String name,
			String fromStation, String toStation, String trainType,
			String passingStation1, String passingStation2, Boolean hasDrivingSection,Long user, String sort,
			Long page, Long size);

	ProcessResult saveTrain(Integer trainNo,  String name,
			String fromStation, String toStation,String startDay,
			 String trainType);

	SelectViewModel listTrainsByNumber(Integer trainNo, Day startDay, String name,
			String fromStation, String toStation, String trainType,
			String passingStation1, String passingStation2,
			Boolean hasDrivingSection, Boolean isUserSelected, Long userPlan, String sort, Long page,
			Long size);

	SelectViewModel listTrainsWithOutUserPlan(Integer trainNo, Day startDay,
			String name, String fromStation, String toStation,
			String trainType, String passingStation1, String passingStation2,
			String sort, Long page, Long size);

	ProcessResult updateTrainInUserList(Integer trainNo, Boolean isUserSelected,
			Long userPlan);

	SelectViewModel listTrainsByPilots(String fromStation, String toStation, Day departureDayI, Day arrivalDayI,
			String departureTime, String arrivalTime, String sort, Long page, Long size);

}
