package com.mathologic.projects.crewlink.custom.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mathologic.projects.crewlink.custom.models.BaseStationAnalysisVM;
import com.mathologic.projects.crewlink.custom.repositories.BaseStationAnalysisVMRepository;

/**
 * This controller is used to fetch available distance and 
 * used(Round Trip) distance 
 * @author Laxman
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 28, 2016
 */

@Controller
@RequestMapping("/api/custom/baseStationAnalysis")
public class BaseStationAnalysisVMController {
	
	@Autowired
	BaseStationAnalysisVMRepository baseStationAnalysisVMRepository;
	
	@RequestMapping(value="/totalDistance", method=RequestMethod.GET)
	public @ResponseBody BaseStationAnalysisVM getTotalDistance(
			@RequestParam(value = "userPlan", required = true) Long userPlan,
			@RequestParam(value = "station", required = true) String station){
		Map<String, Map<String,Long>> totalUsedDetails = new HashMap<>();
		@SuppressWarnings("rawtypes")
		List tempList= baseStationAnalysisVMRepository.getTotalUsedDistance(userPlan,station);
		if(tempList != null && tempList.size()>0){
			Object[] row = null;
			for(int i=0; i<tempList.size(); i++){
				if(tempList.get(i) != null){
					row = (Object[]) tempList.get(i);
					totalUsedDetails.put(row[0].toString(), getDistanceAndNoDetailsInMapFromRow(row,true));
				}
			}
		}
		Map<String, Map<String,Long>> totalAvailableDetails = new HashMap<>();
		tempList = baseStationAnalysisVMRepository.getTotalAvailableDistanceFrom(userPlan,station);
		totalAvailableDetails.put("from", getDistanceAndNoDetailsInMapFromList(tempList));
		
		tempList = baseStationAnalysisVMRepository.getTotalAvailableDistanceTo(userPlan,station);
		totalAvailableDetails.put("to", getDistanceAndNoDetailsInMapFromList(tempList));
		
		tempList = baseStationAnalysisVMRepository.getTotalAvailableDistanceBoth(userPlan,station);
		totalAvailableDetails.put("both", getDistanceAndNoDetailsInMapFromList(tempList));
		
		return new BaseStationAnalysisVM(totalUsedDetails,totalAvailableDetails);
	}
	
	/**
	 * This method is used to put result set in map from List
	 * @param tempList
	 * @return
	 */
	private Map<String,Long> getDistanceAndNoDetailsInMapFromList(@SuppressWarnings("rawtypes") List tempList){
		Map<String,Long> totalUsedDistanceAndNo = new HashMap<>();
		if(tempList != null && tempList.size()>0 && tempList.get(0) != null){
			totalUsedDistanceAndNo = getDistanceAndNoDetailsInMapFromRow((Object[]) tempList.get(0),false);
		}
		return totalUsedDistanceAndNo;
	}
	
	/**
	 * This method is used to put result set in map from row
	 * @param row
	 * @return
	 */
	private Map<String,Long> getDistanceAndNoDetailsInMapFromRow(Object[] row,boolean isRT){
		Map<String,Long> totalUsedDistanceAndNo = new HashMap<>();
		if(isRT){
			if(row[1] != null && row[2] != null){
				totalUsedDistanceAndNo.put("distance", Long.parseLong(row[1].toString()));
				totalUsedDistanceAndNo.put("noOfRoundTrips", Long.parseLong(row[2].toString()));
			}
		}else{
			if(row[0] != null && row[1] != null){
				totalUsedDistanceAndNo.put("distance", Long.parseLong(row[0].toString()));
				totalUsedDistanceAndNo.put("noOfDuties", Long.parseLong(row[1].toString()));
			}
		}
		return totalUsedDistanceAndNo;
	}
}
