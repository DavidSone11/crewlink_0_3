package com.mathologic.projects.crewlink.custom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.repositories.DrivingSectionVMRepository;
import com.mathologic.projects.crewlink.models.Day;
/**
 * This controller is used to
 * 1. save driving sections of single train for one day
 * 2. save driving sections of single train for all days
 * 3. save driving sections of single train for all days with duty 
 * 4. list all driving sections in the list form.
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
@Controller
@RequestMapping("/api/custom/drivingSections")
public class DrivingSectionVMController {
	@Autowired
	DrivingSectionVMRepository drivingSectionVMRepository;
	
	/**
	 * This saves the driving sections of single train of single day.
	 * @param trainNo ( eg. 11005)
	 * @param startDay ( eg. 'MONDAY')
	 * @param stopNumbers ( eg. 1,2,3...)
	 * @param userPlan ( eg. 1,2..)
	 * @return ProcessResult ( result=true or false, outputValue=success value, errorMessage="failure message.")
	 */
	@RequestMapping(value="/saveSingle", method=RequestMethod.GET)
	public @ResponseBody ProcessResult createSingle(
			@RequestParam(value = "trainNo", required = false) Integer trainNo,
			@RequestParam(value = "startDay", required = false) String startDay,
			@RequestParam(value = "stopNumbers", required = false) String stopNumbers,
			@RequestParam(value = "userPlan", required = false) Long userPlan){
		Day startDayI = null;
		if(startDay!=null){
			try{
				//converts the start day name into start day value 
			startDayI = Day.valueOf(startDay);
			}catch(Exception ex){
				System.out.println("Error : "+ex.getMessage());
			}
		}
		//Integer trainNoI = Integer.parseInt(trainNo);
		//Long userPlanI = Long.parseLong(userPlan);
		
		ProcessResult processResult = null;
		
		processResult = drivingSectionVMRepository.saveDrivingSectionsForSingleTrain(trainNo, startDayI, stopNumbers, userPlan);
		
		//returns whether successfully saved or not.
		return processResult;
		
	}
	/**
	 * This saves the driving sections of single train of all days.
	 * @param trainNo ( eg. 11005)
	 * @param startDay ( eg. 'MONDAY')
	 * @param stopNumbers ( eg. 1,2,3...)
	 * @param userPlan ( eg. 1,2..)
	 * @return ProcessResult ( result=true or false, outputValue=success value, errorMessage="failure message.")
	 */
	@RequestMapping(value="/saveForAllDays", method=RequestMethod.GET)
	public @ResponseBody ProcessResult saveDrivingSectionsForTrainAllDays(
			@RequestParam(value = "trainNo", required = false) Integer trainNo,
			@RequestParam(value = "stopNumbers", required = false) String stopNumbers,
			@RequestParam(value = "userPlan", required = false) Long userPlan){
		Day startDayI = null;
		
		//Integer trainNoI = Integer.parseInt(trainNo);
		//Long userPlanI = Long.parseLong(userPlan);
		
		ProcessResult processResult = null;
		
		processResult = drivingSectionVMRepository.saveDrivingSectionsForTrainAllDays(trainNo, stopNumbers, userPlan);
		
		return processResult;
		
	}
	/**
	 * This saves the driving sections of single train of all days with driving duties.
	 * @param trainNo ( eg. 11005)
	 * @param stopNumbers ( eg. 1,2,3...)
	 * @param userPlan ( eg. 1,2..)
	 * @return ProcessResult ( result=true or false, outputValue=success value, errorMessage="failure message.")
	 */
	@RequestMapping(value="/saveForAllDaysWithDrivingDuties", method=RequestMethod.GET)
	public @ResponseBody ProcessResult saveDrivingSectionsAndDrivingDutiesForTrainAllDays(
			@RequestParam(value = "trainNo", required = false) Integer trainNo,
			@RequestParam(value = "stopNumbers", required = false) String stopNumbers,
			@RequestParam(value = "userPlan", required = false) Long userPlan){
		Day startDayI = null;
		
		//Integer trainNoI = Integer.parseInt(trainNo);
		//Long userPlanI = Long.parseLong(userPlan);
		
		ProcessResult processResult = null;
		
		processResult = drivingSectionVMRepository.saveDrivingSectionsAndDrivingDutiesForTrainAllDays(trainNo, stopNumbers, userPlan);
		
		return processResult;
		
	}
	/**
	 * This list the driving section in the list form.
	 * @param trainNo ( eg. 11005 )
	 * @param originDay ( eg. 'MONDAY' )
	 * @param fromStation ( eg. 'DR' )
	 * @param toStation ( eg. 'PDY' )
	 * @param departureDay ( eg. 'TUESDAY' )
	 * @param minDepartureTime ( eg. 16:10 )
	 * @param maxDepartureTime ( eg. 20:00 ) 
	 * @param arrivalDay ( eg. 'WEDNESDAY' )
	 * @param minArrivalTime ( eg. 4:00 )
	 * @param maxArrivalTime ( eg. 23:00 )
	 * @param minDuration ( eg. 200 )
	 * @param maxDuration ( eg. 400 )
	 * @param minDistance ( eg. 100 )
	 * @param maxDistance ( eg. 500 )
	 * @param isDrivingDuty ( eg. TRUE or FALSE )
	 * @param userPlan ( eg. 1,2.. )
	 * @param sort ( eg. trainNo DESC )
	 * @param page ( eg. 1,2... )
	 * @param size ( eg. 10,20.... )
	 * @return SelectViewModel ( eg. { selectionDetails : { totalItems : 20 },
	 *         data : [], fields: { item1 : 0, item2 : 1 } } )
	 */
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel listDrivingSections(
			@RequestParam(value = "trainNo", required = false) Integer trainNo,
			@RequestParam(value = "originDay", required = false) String originDay,
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
			@RequestParam(value = "isDrivingDuty", required = false) Boolean isDrivingDuty,
			@RequestParam(value = "isIgnore", required = false) Boolean isIgnore,
			@RequestParam(value = "userPlan", required = true) Long userPlan,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "page", required = false, defaultValue = "0") Long page, 
			@RequestParam(value = "size", required = false, defaultValue = "10") Long size) {
		SelectViewModel result = null;
		Day originDayI = null;
		if(originDay!=null){
			try{
				//converts origin day name into origin day value
				originDayI = Day.valueOf(originDay);
			}catch(Exception ex){
				System.out.println("Error : "+ex.getMessage());
			}
		}
		Day departureDayI = null;
		if(departureDay!=null){
			try{
				//converts departure day name into departure day value
				departureDayI = Day.valueOf(departureDay);
			}catch(Exception ex){
				System.out.println("Error : "+ex.getMessage());
			}
		}
		Day arrivalDayI = null;
		if(arrivalDay!=null){
			try{
				//converts arrival day name into arrival day value
				arrivalDayI = Day.valueOf(arrivalDay);
			}catch(Exception ex){
				System.out.println("Error : "+ex.getMessage());
			}
		}
		if(fromStation!=null)
			if(fromStation.indexOf(",")!= -1)
				fromStation = fromStation.substring(0,fromStation.indexOf(",")-1);
		if(toStation!=null)
			if(toStation.indexOf(",")!= -1)
				toStation = toStation.substring(0,toStation.indexOf(",")-1);
//		start = (start==null)?1:start;
//		offset= (offset==null)?10:offset;
		
		
		result = drivingSectionVMRepository.listDrivingSections(trainNo, originDayI, fromStation, toStation, 
				departureDayI, minDepartureTime, maxDepartureTime,arrivalDayI,minArrivalTime, maxArrivalTime,
				minDuration, maxDuration, minDistance, 
				maxDistance, isDrivingDuty,isIgnore, userPlan,sort, page, size);
		return result;
	}
	
	
	
	     @RequestMapping(value = "/updateDrivingSectionIsIgnore", method = RequestMethod.GET)
	     public @ResponseBody  ProcessResult drivingSectionsIsIgnored(
	    	@RequestParam(value = "drivingSectionId", required = true) Long drivingSectionId,
	    	@RequestParam(value="isIgnore",required = true)Boolean isIgnore,
	    	@RequestParam(value = "userPlan", required = true) Long userPlan){
	    	 
	    	 ProcessResult processResult = null;
	    	 processResult = drivingSectionVMRepository.updateDrivingSectionInUserList(drivingSectionId, isIgnore, userPlan);
	    	  return processResult;
	    	 
	     }
	
	
}
