package com.mathologic.projects.crewlink.custom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.repositories.TrainVMRepository;
import com.mathologic.projects.crewlink.models.Day;

/**
 * This controller is used to fetch details of trains in list form.
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
@Controller
@RequestMapping("/api/custom/trains")
public class TrainVMController {
	@Autowired
	TrainVMRepository trainVMRepository;

	/**
	 * This is used to list the trains based on any of the below parameters.
	 * 
	 * @param trainNo
	 *            ( eg. 11005 )
	 * @param startDay
	 *            ( eg. 'MONDAY' )
	 * @param name
	 *            ( eg. 'Puducherry Express' )
	 * @param fromStation
	 *            ( eg. 'DR' )
	 * @param toStation
	 *            ( eg. 'PDY' )
	 * @param trainType
	 *            ( eg. MEX )
	 * @param passingStation1
	 *            ( eg. 'YPR' )
	 * @param passingStation2
	 *            ( eg. 'KPD' )
	 * @param sort
	 *            ( eg. trainNo DESC )
	 * @param page
	 *            ( eg. 2,3,4.. )
	 * @param size
	 *            ( eg. 10,100,1000.. )
	 * @return SelectViewModel ( eg. { selectionDetails : { totalItems : 20 },
	 *         data : [], fields: { item1 : 0, item2 : 1 } } )
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel listTrains(@RequestParam(value = "trainNo", required = false) Integer trainNo,
			@RequestParam(value = "startDay", required = false) String startDay,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "fromStation", required = false) String fromStation,
			@RequestParam(value = "toStation", required = false) String toStation,
			@RequestParam(value = "trainType", required = false) String trainType,
			@RequestParam(value = "passingStation1", required = false) String passingStation1,
			@RequestParam(value = "passingStation2", required = false) String passingStation2,
			@RequestParam(value = "hasDrivingSection", required = false) Boolean hasDrivingSection,
			@RequestParam(value = "userPlan", required = true) Long userPlan,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "page", required = false, defaultValue = "0") Long page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Long size) {
		SelectViewModel result = null;
		Day startDayI = null;
		if (startDay != null) {
			try {
				startDayI = Day.valueOf(startDay); // This changes the Day name
													// to Enum value
			} catch (Exception ex) {
				System.out.println("Error : " + ex.getMessage());
			}
		}
		// Check passing stations is null or not
		passingStation1 = (passingStation1 != null && passingStation1.isEmpty()) ? null : passingStation1; 
		passingStation2 = (passingStation2 != null && passingStation2.isEmpty()) ? null : passingStation2;
		result = trainVMRepository.listTrains(trainNo, startDayI, name, fromStation, toStation, trainType,
				passingStation1, passingStation2, hasDrivingSection, userPlan, sort, page, size);
		return result;
	}
	
	@RequestMapping(value = "/listByNumbers", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel listTrainsByNumber(@RequestParam(value = "trainNo", required = false) Integer trainNo,
			@RequestParam(value = "startDay", required = false) String startDay,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "fromStation", required = false) String fromStation,
			@RequestParam(value = "toStation", required = false) String toStation,
			@RequestParam(value = "trainType", required = false) String trainType,
			@RequestParam(value = "passingStation1", required = false) String passingStation1,
			@RequestParam(value = "passingStation2", required = false) String passingStation2,
			@RequestParam(value = "hasDrivingSection", required = false) Boolean hasDrivingSection,
			@RequestParam(value = "isUserSelected", required = false) Boolean isUserSelected,
			@RequestParam(value = "userPlan", required = true) Long userPlan,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "page", required = false, defaultValue = "0") Long page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Long size) {
		SelectViewModel result = null;
		Day startDayI = null;
		if (startDay != null) {
			try {
				startDayI = Day.valueOf(startDay); // This changes the Day name
													// to Enum value
			} catch (Exception ex) {
				System.out.println("Error : " + ex.getMessage());
			}
		}
		// Check passing stations is null or not
		passingStation1 = (passingStation1 != null && passingStation1.isEmpty()) ? null : passingStation1; 
		passingStation2 = (passingStation2 != null && passingStation2.isEmpty()) ? null : passingStation2;
		result = trainVMRepository.listTrainsByNumber(trainNo, startDayI, name, fromStation, toStation, trainType,
				passingStation1, passingStation2, hasDrivingSection, isUserSelected, userPlan, sort, page, size);
		return result;
	}
	
	@RequestMapping(value="/updateTrainInUserList", method=RequestMethod.GET)
	public @ResponseBody ProcessResult updateTrainInUserList(
			@RequestParam(value = "trainNo", required = true) Integer trainNo,
			@RequestParam(value = "isUserSelected", required = true) Boolean isUserSelected,
			@RequestParam(value = "userPlan", required = false) Long userPlan){
		
		ProcessResult processResult = null;
		
		processResult = trainVMRepository.updateTrainInUserList(trainNo, isUserSelected, userPlan);
		
		//returns whether successfully saved or not.
		return processResult;
		
	}
	
	@RequestMapping(value = "/listByPilots", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel listTrainsForPilots(
			@RequestParam(value = "departureDay", required = false) String departureDay,
			@RequestParam(value = "arrivalDay", required = false) String arrivalDay,
			@RequestParam(value = "fromStation", required = false) String fromStation,
			@RequestParam(value = "toStation", required = false) String toStation,
			@RequestParam(value = "departureTime", required = false) String departureTime,
			@RequestParam(value = "arrivalTime", required = false) String arrivalTime,
			
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "page", required = false, defaultValue = "0") Long page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Long size) {
		SelectViewModel result = null;
		Day departureDayI = null;
		if (departureDay != null) {
			try {
				departureDayI = Day.valueOf(departureDay); // This changes the Day name
													// to Enum value
			} catch (Exception ex) {
				System.out.println("Error : " + ex.getMessage());
			}
		}
		Day arrivalDayI = null;
		if (arrivalDay != null) {
			try {
				arrivalDayI = Day.valueOf(arrivalDay); // This changes the Day name
													// to Enum value
			} catch (Exception ex) {
				System.out.println("Error : " + ex.getMessage());
			}
		}
		
		// Check passing stations is null or not
		
		result = trainVMRepository.listTrainsByPilots(fromStation, toStation, departureDayI,arrivalDayI,departureTime,
				arrivalTime,sort, page, size);
		return result;
	}

	
	
	
	
}
