package com.mathologic.projects.crewlink.custom.repositories;

import java.util.List;

import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.RoundTripManyDutiesPM;
import com.mathologic.projects.crewlink.custom.models.RoundTripPM;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.models.Day;

/**
 * This repository contains declarations of list round trips, save
 * round trips
 * 
 * @author Jagdish
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
public interface RoundTripVMRepository {
	SelectViewModel listRoundTrips(Long userPlan,String rtName, String station, Long minDuration,
								   Long maxDuration, Long minDistance, Long maxDistance, Day departureDayI, 
								   String minDepartureTime,String maxDepartureTime,String minArrivalTime,String maxArrivalTime,
								   Day arrivalDay,Long lastDrivingDutyDuration,String minAvailableTime,String maxAvailableTime,
								   Day availableDay,Long crewType,Boolean isCrewLink,Boolean isIgnore, Long totalOutStationRestTime, String sort, Long page, 
								   Long size, Long crewLinkId,String crewLinkName,String os);
	ProcessResult saveSingleRoundTrip(String drivingDutyIds, String orderNos,Long crewType, Long userPlan);
	
	ProcessResult updateRoundTripInUserList(Long roundTrip, Boolean isIgnore, Long userPlan);
	ProcessResult saveManyMatrix(List<RoundTripPM> data, Long userPlan);
	ProcessResult saveManyList(List<RoundTripManyDutiesPM> data, Long userPlan);
	
	ProcessResult insertRoundTripForCrewLinkGeneration(Long roundTrip, Integer orderNo, Integer crewLinkId);
}
