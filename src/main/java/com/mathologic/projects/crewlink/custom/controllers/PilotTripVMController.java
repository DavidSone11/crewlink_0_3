package com.mathologic.projects.crewlink.custom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mathologic.projects.crewlink.custom.models.DrivingDutyPM;
import com.mathologic.projects.crewlink.custom.models.PilotTripPM;
import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.repositories.PilotTripVMRepository;
import com.mathologic.projects.crewlink.models.Day;

@Controller
@RequestMapping("/api/custom/pilotTrips")
public class PilotTripVMController {
	@Autowired
	PilotTripVMRepository pilotTripVMRepository;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel listPilotTrips(
						@RequestParam(value = "pilotTripName", required = false) String pilotTripName,
						@RequestParam(value = "pilotType", required = false) String pilotType,
						@RequestParam(value = "fromStation", required = false) String fromStation,
						@RequestParam(value = "toStation", required = false) String toStation,
						@RequestParam(value = "departureDay", required = false) String departureDay,
						@RequestParam(value = "minDepartureTime", required = false) String minDepartureTime,
						@RequestParam(value = "maxDepartureTime", required = false) String maxDepartureTime,
						@RequestParam(value = "arrivalDay", required = false) String arrivalDay,
						@RequestParam(value = "minArrivalTime", required = false) String minArrivalTime,
						@RequestParam(value = "maxArrivalTime", required = false) String maxArrivalTime,
						@RequestParam(value = "minDuration", required = false) Long minDuration,
						@RequestParam(value = "maxDuration", required = false) Long maxDuration,
						@RequestParam(value = "minDistance", required = false) Long minDistance,
						@RequestParam(value = "maxDistance", required = false) Long maxDistance,
						@RequestParam(value = "userPlan", required = true) Long userPlan,
						@RequestParam(value = "sort", required = false) String sort,
						@RequestParam(value = "page", required = false, defaultValue = "0") Long page,
						@RequestParam(value = "size", required = false, defaultValue = "10") Long size) {
		SelectViewModel result = null;
		
		
		Day arrivalDayI = null;
		if(arrivalDay!=null){
			try{
				arrivalDayI = Day.valueOf(arrivalDay);
			}catch(Exception ex){
				System.out.println("Error : "+ex.getMessage());
			}
		}
		
		Day departureDayI = null;
		if(departureDay!=null){
			try{
				departureDayI = Day.valueOf(departureDay);
			}catch(Exception ex){
				System.out.println("Error : "+ex.getMessage());
			}
		}

		
		result =pilotTripVMRepository.listPilotTrip(pilotTripName, pilotType, fromStation, toStation, departureDayI, arrivalDayI, minDepartureTime, maxDepartureTime, minArrivalTime, maxArrivalTime,minDistance, maxDistance,minDuration, maxDuration, userPlan, sort, page, size);
		return result;
	}
	
	@RequestMapping(value="/savePilotTrain", method=RequestMethod.POST)
	public @ResponseBody ProcessResult createPilotTrain(
			@RequestBody PilotTripPM data,
			@RequestParam(value = "userPlan", required = true) Long userPlan){
		
		ProcessResult processResult = null;
		processResult = pilotTripVMRepository.savePilotTrain(data,userPlan);
		return processResult;
		
	}
	@RequestMapping(value="/savePilot", method=RequestMethod.POST)
	public @ResponseBody ProcessResult createPilot(
			@RequestBody PilotTripPM data,
			@RequestParam(value = "userPlan", required = true) Long userPlan){
		
		ProcessResult processResult = null;
		
		processResult = pilotTripVMRepository.savePilot(data,userPlan);
		
		return processResult;
		
	}
	
}
