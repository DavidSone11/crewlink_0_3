package com.mathologic.projects.crewlink.custom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mathologic.projects.crewlink.custom.models.CopyUserPlanPM;
import com.mathologic.projects.crewlink.custom.models.DrivingDutyPM;
import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.repositories.DrivingDutyVMRepository;
import com.mathologic.projects.crewlink.custom.repositories.UserPlanVMRepository;

/**
 * This controller is used to fetch details of trains in list form.
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date May 10, 2016
 */
@Controller
@RequestMapping("/api/custom/userPlan")
public class UserPlanVMController {
	@Autowired
	UserPlanVMRepository userPlanVMRepository;
	
	/**
	 * This is used to copy user plan
	 * @param data ( eg. data : { sourceUserPlanName,sourceUserPlanTimeStamp,newUserPlanName})
	 * @param userPlan ( eg. 1,2... )
	 * @return ProcessResult ( result=true or false, outputValue=success value, errorMessage="failure message.")
	 */
	
	@RequestMapping(value="/copy", method=RequestMethod.POST)
	public @ResponseBody ProcessResult copy(
			@RequestBody CopyUserPlanPM data ){
		
		ProcessResult processResult = null;
		
		processResult = userPlanVMRepository.copy(data);
		
		return processResult;
		
	}
}
