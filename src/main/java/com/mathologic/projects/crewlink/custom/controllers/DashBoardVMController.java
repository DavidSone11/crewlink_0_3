package com.mathologic.projects.crewlink.custom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mathologic.projects.crewlink.custom.models.DashBoardVM;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.repositories.DashBoardVMRepository;

/**
 * This controller is used to fetch details of trains in list form.
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
@Controller
@RequestMapping("/api/custom/dashboards")
public class DashBoardVMController {
	@Autowired
	DashBoardVMRepository dashBoardVMRepository;

	
	@RequestMapping(value="/listdashboard", method=RequestMethod.GET)
	public @ResponseBody DashBoardVM listDashBoardItems(
			@RequestParam(value = "userPlan", required = true) Long userPlan,
			@RequestParam(value = "station", required = false) String station){
		DashBoardVM processResult = null;
		processResult = dashBoardVMRepository.listDashboardItems(userPlan, station);
		return processResult;
		
	}
	
	@RequestMapping(value="/listDependencies", method=RequestMethod.GET)
	public @ResponseBody SelectViewModel listDependencies(
			@RequestParam(value = "searchItem", required = true) String searchItem,
			@RequestParam(value = "searchValue", required = true) String searchValue,
			@RequestParam(value = "userPlan", required = true) Long userPlan){
		SelectViewModel processResult = null;
		processResult = dashBoardVMRepository.listDependencies(searchItem, searchValue, userPlan);
		return processResult;
		
	}
}
