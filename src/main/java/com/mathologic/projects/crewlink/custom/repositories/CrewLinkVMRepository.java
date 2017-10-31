package com.mathologic.projects.crewlink.custom.repositories;

import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.models.Day;


/**
 * This repository interface contains declarations for list crew links, 
 * list driving sections, and save crew links .
 * 
 * @author Vivek Yadav,Laxman
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
public interface CrewLinkVMRepository {
	SelectViewModel listCrewLinks(String userPlan, String linkName, Integer locoPilots, String station, Long minDuration,
			Long maxDuration, Long minDistance, Long maxDistance, Day departureDay, String minDepartureTime,
			String maxDepartureTime, Day arrivalDayI, String minArrivalTime, String maxArrivalTime, String sort, Long page, Long size);

	SelectViewModel listDrivingSections(String userPlan, String linkId,String sort, Long page, Long size);

	ProcessResult save(Integer crewLinkId, String crewLinkName,
			Long userPlan);

	SelectViewModel stationSummary(Long userPlan);
}