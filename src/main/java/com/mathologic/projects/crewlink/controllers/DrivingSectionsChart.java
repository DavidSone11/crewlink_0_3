/**
 * 
 */
package com.mathologic.projects.crewlink.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mathologic.projects.crewlink.custom.repositories.DrivingSectionChartVMRepository;
import com.mathologic.projects.crewlink.models.Train;

/**
 * @author SANTOSH
 *
 */

@RestController
@RequestMapping("/api/custom/drivingsectionChart")
public class DrivingSectionsChart {

	/**
	 * 
	 */
	
	
	
	
	@Autowired
	DrivingSectionChartVMRepository drivingSectionChartVMRepository;
	
	public DrivingSectionsChart() {
		// TODO Auto-generated constructor stub
	}
	@RequestMapping(value="/getTrainNo", method=RequestMethod.GET)
	public Page<Train> getAllTrain(
			@RequestParam(value = "trainNo", required = true) int trainNo,
			@RequestParam(value = "size", required = true) Long size,
			@RequestParam(value = "page", required = true) Long page
			){
		
		
		drivingSectionChartVMRepository.getlistOfTrains(trainNo, page, size);
		return null;
	}
	
	
}
