package com.mathologic.projects.crewlink.custom.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.RoundTripManyDutiesPM;
import com.mathologic.projects.crewlink.custom.models.RoundTripPM;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.repositories.RoundTripVMRepository;
import com.mathologic.projects.crewlink.models.Day;

/**
 * This controller is used to create round trips and list the round trips
 * 
 * @author Jagdish
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
@Controller
@RequestMapping("/api/custom/roundTrips")
public class RoundTripVMController {

	@Autowired
	RoundTripVMRepository roundTripVMRepository;

	/**
	 * This is used to list the round trips
	 * 
	 * @param userPlan
	 *            ( eg. 1,2.. )
	 * @param rtName
	 *            ( eg. 11005 ; 11006 )
	 * @param station
	 *            ( eg. 'SBC')
	 * @param departureDay
	 *            ( eg. 'MONDAY' )
	 * @param minDepartureTime
	 *            ( eg. 10:00 )
	 * @param maxDepartureTime
	 *            ( eg. 22:00 )
	 * @param minArrivalTime
	 *            ( eg. 2:00 )
	 * @param maxArrivalTime
	 *            ( eg. 23:00 )
	 * @param arrivalDay
	 *            ( eg. 'TUESDAY')
	 * @param lastDrivingDutyDuration
	 *            ( eg. 290 )
	 * @param minAvailableTime
	 *            ( eg. 3:00 )
	 * @param maxAvailableTime
	 *            ( eg. 23:00 )
	 * @param availableDay
	 *            ( eg. 'WEDNESDAY')
	 * @param crewType
	 *            ( eg. 'ACME')
	 * @param minDuration
	 *            ( eg. 200 )
	 * @param maxDuration
	 *            ( eg. 1000 )
	 * @param minDistance
	 *            ( eg. 200 )
	 * @param maxDistance
	 *            ( eg. 1200 )
	 * @param isCrewLink
	 *            ( eg. TRUE or FALSE )
	 * @param totalOutStationRestTime
	 *            ( eg. 1200 )
	 * @param sort
	 *            ( eg. rtName DESC )
	 * @param page
	 *            ( eg. 1,2... )
	 * @param size
	 *            ( eg. 10,20... )
	 * @return SelectViewModel ( eg. { selectionDetails : { totalItems : 20 },
	 *         data : [], fields: { item1 : 0, item2 : 1 } } )
	 */

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel listRoundTrips(

			@RequestParam(value = "userPlan", required = false) Long userPlan,
			@RequestParam(value = "rtName", required = false) String rtName,
			@RequestParam(value = "station", required = false) String station,
			@RequestParam(value = "departureDay", required = false) String departureDay,
			@RequestParam(value = "minDepartureTime", required = false) String minDepartureTime,
			@RequestParam(value = "maxDepartureTime", required = false) String maxDepartureTime,
			@RequestParam(value = "minArrivalTime", required = false) String minArrivalTime,
			@RequestParam(value = "maxArrivalTime", required = false) String maxArrivalTime,
			@RequestParam(value = "arrivalDay", required = false) String arrivalDay,
			@RequestParam(value = "lastDrivingDutyDuration", required = false) Long lastDrivingDutyDuration,
			@RequestParam(value = "minAvailableTime", required = false) String minAvailableTime,
			@RequestParam(value = "maxAvailableTime", required = false) String maxAvailableTime,
			@RequestParam(value = "availableDay", required = false) String availableDay,
			@RequestParam(value = "crewType", required = false) Long crewType,
			@RequestParam(value = "minDuration", required = false) Long minDuration,
			@RequestParam(value = "maxDuration", required = false) Long maxDuration,
			@RequestParam(value = "minDistance", required = false) Long minDistance,
			@RequestParam(value = "maxDistance", required = false) Long maxDistance,
			@RequestParam(value = "isCrewLink", required = false) Boolean isCrewLink,
			@RequestParam(value = "isIgnore", required = false) Boolean isIgnore,
			@RequestParam(value = "crewLinkId", required = false) Long crewLinkId,
			@RequestParam(value = "crewLinkName", required = false) String crewLinkName,
			@RequestParam(value = "os", required = false) String os,
			@RequestParam(value = "totalOutStationRestTime", required = false) Long totalOutStationRestTime,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "page", required = false, defaultValue = "0") Long page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Long size) {
		SelectViewModel result = null;
		Day departureDayI = null;
		if (departureDay != null) {
			try {
				// convert the departure day name into departure day value
				departureDayI = Day.valueOf(departureDay);
			} catch (Exception ex) {
				System.out.println("Error : " + ex.getMessage());
			}
		}

		Day arrivalDayI = null;
		if (arrivalDay != null) {
			try {
				// convert the arrival day name into arrival day value
				arrivalDayI = Day.valueOf(arrivalDay);
			} catch (Exception ex) {
				System.out.println("Error : " + ex.getMessage());
			}
		}
		Day availableDayI = null;
		if (availableDay != null) {
			try {
				// convert available day name into available day value
				availableDayI = Day.valueOf(availableDay);
			} catch (Exception ex) {
				System.out.println("Error : " + ex.getMessage());
			}
		}
		result = roundTripVMRepository.listRoundTrips(userPlan, rtName,
				station, minDuration, maxDuration, minDistance, maxDistance,
				departureDayI, minDepartureTime, maxDepartureTime,
				minArrivalTime, maxArrivalTime, arrivalDayI,
				lastDrivingDutyDuration, minAvailableTime, maxAvailableTime,
				availableDayI, crewType, isCrewLink, isIgnore,
				totalOutStationRestTime, sort, page, size, crewLinkId,crewLinkName,os);
		return result;
	}

	/**
	 * This is used to create round trips by taking driving duty ids and order
	 * numbers
	 * 
	 * @param drivingDutyIds
	 *            ( eg. 4,12.. )
	 * @param orderNos
	 *            ( eg. 1,2... )
	 * @param crewType
	 *            ( eg. 'ACME')
	 * @param userPlan
	 *            ( eg. 1,2... )
	 * @return ProcessResult ( result=true or false, outputValue=success value,
	 *         errorMessage="failure message.")
	 */
	@RequestMapping(value = "/saveSingleRoundTrip", method = RequestMethod.GET)
	public @ResponseBody ProcessResult createSingle(
			@RequestParam(value = "drivingDutyIds", required = false) String drivingDutyIds,
			@RequestParam(value = "orderNos", required = false) String orderNos,
			@RequestParam(value = "crewType", required = false) Long crewType,
			@RequestParam(value = "userPlan", required = false) Long userPlan) {

		ProcessResult processResult = null;

		processResult = roundTripVMRepository.saveSingleRoundTrip(
				drivingDutyIds, orderNos, crewType, userPlan);

		return processResult;
	}

	@RequestMapping(value = "/updateRoundTripIsIgnore", method = RequestMethod.GET)
	public @ResponseBody ProcessResult roundTripIsIgnored(
			@RequestParam(value = "roundTrip", required = true) Long roundTrip,
			@RequestParam(value = "isIgnore", required = true) Boolean isIgnore,
			@RequestParam(value = "userPlan", required = true) Long userPlan) {

		ProcessResult processResult = null;
		processResult = roundTripVMRepository.updateRoundTripInUserList(
				roundTrip, isIgnore, userPlan);
		return processResult;

	}
	
	@RequestMapping(value="/saveManyMatrix", method=RequestMethod.POST)
	public @ResponseBody ProcessResult createManyMatrix(
			@RequestBody List<RoundTripPM> data,
			@RequestParam(value = "userPlan", required = true) Long userPlan){
		
		ProcessResult processResult = null;
		
		processResult = roundTripVMRepository.saveManyMatrix(data,userPlan);
		
		return processResult;
		
	}
	
	@RequestMapping(value="/saveManyList", method=RequestMethod.POST)
	public @ResponseBody ProcessResult createManyList(
			@RequestBody List<RoundTripManyDutiesPM> data,
			@RequestParam(value = "userPlan", required = true) Long userPlan){
		
		ProcessResult processResult = null;
		
		processResult = roundTripVMRepository.saveManyList(data,userPlan);
		
		return processResult;
		
	}

}
