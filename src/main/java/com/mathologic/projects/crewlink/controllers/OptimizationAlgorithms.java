package com.mathologic.projects.crewlink.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mathologic.projects.crewlink.helpers.HungarianAlgorithm;

@Controller
@RequestMapping("/api/custom/optimization")
public class OptimizationAlgorithms {
	@RequestMapping(value="/minimizeMatrix", method=RequestMethod.POST)
	public @ResponseBody int[] minimizeMatrix(
			@RequestBody double[][] data){
		int[] result = null;
		HungarianAlgorithm algo = new HungarianAlgorithm(data);
		result = algo.execute();
		
		
		return result;
		
	}
}
