package com.mathologic.projects.crewlink.custom.repositories;

import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.models.Day;

public interface PilotVMRepository {
	
	ProcessResult searchTrainForPilot(String fromStation, String toStation,Day departureDay,Day arrivalDay, String arrivalTime, String DepartureTime);

}
