package com.mathologic.projects.crewlink.custom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.repositories.DrivingDutyElementVMRepository;
import com.mathologic.projects.crewlink.models.Day;
/**
 * This controller is used to create driving duty elements 
 * 
 * @author santosh
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */

@Controller
@RequestMapping("/api/custom/drivingDutyElements")
public class DrivingDutyElementVMController {
	@Autowired
	DrivingDutyElementVMRepository drivingdutyElementVMRepository;
	
	/**
	 * This is used to list driving duty elements 
	 * @param id ( eg. 1,2..)
	 * @param drivingduty ( eg. 1,2.. )
	 * @param userPlan ( eg. 1,2..)
	 * @param sort ( eg. drivingDuty DESC )
	 * @param page ( eg. 1,2... )
	 * @param size ( eg. 10,20... )
	 * @return SelectViewModel ( eg. { selectionDetails : { totalItems : 20 },
	 *         data : [], fields: { item1 : 0, item2 : 1 } } )
	 */

	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel listDrivingDuties(
			
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "drivingduty", required = false) Long drivingduty,
			@RequestParam(value = "userPlan", required = true) Long userPlan,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "page", required = false, defaultValue = "0") Long page, 
			@RequestParam(value = "size", required = false, defaultValue = "10") Long size) {
			SelectViewModel result = null;
	
	
		
		result = drivingdutyElementVMRepository.listDrivingElements(id,drivingduty,userPlan,sort, page, size);
		return result;

		
	}
	
	/**
	 * This is used to save driving duty elements
	 * @param drivingSectionId ( eg. 1,2... )
	 * @param startPilotId ( eg. 1,2.. )
	 * @param endPilotId ( eg. 1,2.. )
	 * @param departureDay ( eg. 'MONDAY' )
	 * @param departureTime ( eg. 22:00 )
	 * @param userPlan ( eg. 1,2.. )
	 * @return ProcessResult ( result=true or false, outputValue=success value, errorMessage="failure message.")
	 */
	@RequestMapping(value="/save", method=RequestMethod.GET)
	public @ResponseBody ProcessResult create(
			@RequestParam(value = "drivingSectionId", required = false) Long drivingSectionId,
			@RequestParam(value = "startPilotId", required = false) Long startPilotId,
			@RequestParam(value = "endPilotId", required = false) Long endPilotId,
			@RequestParam(value = "userPlan", required = true) Long userPlan){
		
		ProcessResult processResult = null;
		
		processResult = drivingdutyElementVMRepository.save(drivingSectionId, startPilotId, endPilotId, userPlan);
		
		//returns the result whether driving duty element saved or not.
		return processResult;
		
	}
}
