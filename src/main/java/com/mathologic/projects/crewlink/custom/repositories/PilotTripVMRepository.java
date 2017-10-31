package com.mathologic.projects.crewlink.custom.repositories;

import com.mathologic.projects.crewlink.custom.models.PilotTripPM;
import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.models.Day;

public interface PilotTripVMRepository {

	SelectViewModel listPilotTrip(String pilotTripName, String pilotType, String fromStation, String toStation,
			Day departureDay, Day arrivalDay, String minDepartureTime, String maxDepartureTime, String minArrivalTime,
			String maxArrivalTime, Long minDistance, Long maxDistance, Long minDuration, Long maxDuration,
			Long userPlan, String sort, Long page, Long size);

	ProcessResult savePilotTrain(PilotTripPM data, Long userPlan);

	ProcessResult savePilot(PilotTripPM data, Long userPlan);

}
