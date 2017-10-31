package com.mathologic.projects.crewlink.custom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mathologic.projects.crewlink.custom.models.DrivingDutyManyPM;
import com.mathologic.projects.crewlink.custom.models.DrivingDutyPM;
import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.repositories.DrivingDutyVMRepository;
import com.mathologic.projects.crewlink.models.Day;

/**
 * This controller is used to create driving duty 
 * 
 * @author Vivek Yadav, Jagdish
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
@Controller
@RequestMapping("/api/custom/drivingDuties")
public class DrivingDutyVMController {
	@Autowired
	DrivingDutyVMRepository drivingDutyVMRepository;
	
	/**
	 * This is used to save the driving duty
	 * @param data ( eg. data : { signOnDuration,signOffDuration, drivingDutyElements })
	 * @param userPlan ( eg. 1,2... )
	 * @return ProcessResult ( result=true or false, outputValue=success value, errorMessage="failure message.")
	 */
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public @ResponseBody ProcessResult create(
			@RequestBody DrivingDutyPM data,
			@RequestParam(value = "userPlan", required = true) Long userPlan){
		
		ProcessResult processResult = null;
		
		processResult = drivingDutyVMRepository.save(data,userPlan);
		
		return processResult;
		
	}
	
	@RequestMapping(value="/saveMany", method=RequestMethod.POST)
	public @ResponseBody ProcessResult createMany(
			@RequestBody DrivingDutyManyPM data,
			@RequestParam(value = "userPlan", required = true) Long userPlan){
		
		ProcessResult processResult = null;
		
		processResult = drivingDutyVMRepository.saveMany(data,userPlan);
		
		return processResult;
		
	}
	
	/**
	 * This is used to list the driving duty 
	 * @param userPlan ( eg. 1,2... )
	 * @param fromStation ( eg. 'SBC' )
	 * @param toStation ( eg. 'MAS' )
	 * @param departureDay ( eg. 'MONDAY' )
	 * @param arrivalDay ( eg.'TUESDAY' )
	 * @param minArrivalTime ( eg. 12:00 ) 
	 * @param maxArrivalTime ( eg. 23:00 )
	 * @param minDepartureTime ( eg. 04:00 )
	 * @param maxDepartureTime ( eg. 23:55 )
	 * @param minDuration ( eg. 100 )
	 * @param maxDuration ( eg. 1000 )
	 * @param minDistance ( eg. 200 )
	 * @param maxDistance ( eg. 1000 )
	 * @param roundTrip ( eg. 1,2.. )
	 * @param isRoundTrip ( eg. TRUE or FALSE )
	 * @param sort ( eg. fromStation ASC or DESC )
	 * @param page ( eg. 1,2... )
	 * @param size ( eg. 10,20... )
	 * @return SelectViewModel ( eg. { selectionDetails : { totalItems : 20 },
	 *         data : [], fields: { item1 : 0, item2 : 1 } } )
	 */

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel listDrivingDuties(

			@RequestParam(value = "userPlan", required = false) Long userPlan,
			@RequestParam(value = "ddName", required = false) String ddName,
			@RequestParam(value = "fromStation", required = false) String fromStation,
			@RequestParam(value = "toStation", required = false) String toStation,
			@RequestParam(value = "departureDay", required = false) String departureDay,
			@RequestParam(value = "arrivalDay", required = false) String arrivalDay,
			@RequestParam(value = "minArrivalTime", required = false) String minArrivalTime,
			@RequestParam(value = "maxArrivalTime", required = false) String maxArrivalTime,
			@RequestParam(value = "minDepartureTime", required = false) String minDepartureTime,
			@RequestParam(value = "maxDepartureTime", required = false) String maxDepartureTime,
			@RequestParam(value = "minDuration", required = false) Long minDuration,
			@RequestParam(value = "maxDuration", required = false) Long maxDuration,
			@RequestParam(value = "minDistance", required = false) Long minDistance,
			@RequestParam(value = "maxDistance", required = false) Long maxDistance,
			@RequestParam(value = "roundTrip", required = false) Long roundTrip,
			@RequestParam(value = "isRoundTrip", required = false) Boolean isRoundTrip,
			@RequestParam(value = "isIgnore", required = false) Boolean isIgnore,
			
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "page", required = false, defaultValue = "0") Long page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Long size) {
		SelectViewModel result = null;
		Day departureDayI = null;
		if (departureDay != null) {
			try {
				//convert departure day name into departure day value 
				departureDayI = Day.valueOf(departureDay);
			} catch (Exception ex) {
				System.out.println("Error : " + ex.getMessage());
			}
		}
		
		Day arrivalDayI = null;
		if(arrivalDay!=null){
			try{
				//convert arrival day name into arrival day value 
				arrivalDayI = Day.valueOf(arrivalDay);
				
			}catch(Exception e){
				System.out.println("Error"+e.getMessage());
			}
		}
		if(fromStation!=null)
		if(fromStation.indexOf(",")!= -1)
			fromStation = fromStation.substring(0,fromStation.indexOf(","));
		
		
		result = drivingDutyVMRepository.listDrivingDuties(userPlan,ddName, fromStation, toStation,minDuration, maxDuration, 
				minDistance,maxDistance,arrivalDayI,minArrivalTime,maxArrivalTime,departureDayI,minDepartureTime, maxDepartureTime, 
				roundTrip,isRoundTrip,isIgnore,sort, page, size);
		
		//returns the result containing the driving duties and the fields with positions.
		return result;

	}
	
	/**
	 * This is used to list the driving duty 
	 * @param userPlan ( eg. 1,2... )
	 * @param fromStation ( eg. 'SBC' )
	 * @param toStation ( eg. 'MAS' )
	 * @param departureDay ( eg. 'MONDAY' )
	 * @param arrivalDay ( eg.'TUESDAY' )
	 * @param minArrivalTime ( eg. 12:00 ) 
	 * @param maxArrivalTime ( eg. 23:00 )
	 * @param minDepartureTime ( eg. 04:00 )
	 * @param maxDepartureTime ( eg. 23:55 )
	 * @param minDuration ( eg. 100 )
	 * @param maxDuration ( eg. 1000 )
	 * @param minDistance ( eg. 200 )
	 * @param maxDistance ( eg. 1000 )
	 * @param roundTrip ( eg. 1,2.. )
	 * @param isRoundTrip ( eg. TRUE or FALSE )
	 * @param sort ( eg. fromStation ASC or DESC )
	 * @param page ( eg. 1,2... )
	 * @param size ( eg. 10,20... )
	 * @return SelectViewModel ( eg. { selectionDetails : { totalItems : 20 },
	 *         data : [], fields: { item1 : 0, item2 : 1 } } )
	 */

	@RequestMapping(value = "/listWithRT", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel listDrivingDutiesWithRT(

			@RequestParam(value = "userPlan", required = false) Long userPlan,
			@RequestParam(value = "fromStation", required = false) String fromStation,
			@RequestParam(value = "toStation", required = false) String toStation,
			@RequestParam(value = "isIgnore", required = false) Boolean isIgnore,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "page", required = false, defaultValue = "0") Long page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Long size) {
		SelectViewModel result = null;
		if(fromStation!=null)
			if(fromStation.indexOf(",")!= -1)
				fromStation = fromStation.substring(0,fromStation.indexOf(",")-1);
		result = drivingDutyVMRepository.listDrivingDutiesWithRT(userPlan,fromStation, toStation, isIgnore, sort, page, size);
		
		//returns the result containing the driving duties and the fields with positions.
		return result;

	}
	/**
	 * This is used to delete driving duty
	 * @param userPlan ( eg. 1,2... )
	 * @param id ( eg. 1,2... )
	 * @return ProcessResult ( result=true or false, outputValue=success value, errorMessage="failure message.")
	 */
	@RequestMapping(value = "/deleteDrivingDuty", method = RequestMethod.GET)
	public  @ResponseBody ProcessResult deleteDrivingDuty(
			@RequestParam(value = "userPlan", required = false) Long userPlan,
			@RequestParam(value = "id", required = false) Long id){
		ProcessResult result = null;
		result = drivingDutyVMRepository.deleteDrivingDuty(userPlan, id);
		return result;
	}
	
	     @RequestMapping(value = "/updateDrivingDutyIsIgnore", method = RequestMethod.GET)
	     public @ResponseBody  ProcessResult drivingdutyIsIgnored(
	    	@RequestParam(value ="drivingduty", required = true) Long drivingduty,
	    	@RequestParam(value ="isIgnore",required = true)Boolean isIgnore,
	    	@RequestParam(value ="userPlan", required = true) Long userPlan){
	    	 
	    	 ProcessResult processResult = null;
	    	 processResult = drivingDutyVMRepository.updateDrivingDutyInUserList(drivingduty, isIgnore, userPlan);
	    	 return processResult;
	    	  
	    	  
	    	 
	     }
	
}
