package com.mathologic.projects.crewlink.custom.repositories;

import java.util.List;

/**
 * This Repository is used to fetch available distance and 
 * used(Round Trip) distance 
 * @author Laxman
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 28, 2016
 */

public interface BaseStationAnalysisVMRepository {

	@SuppressWarnings("rawtypes")
	List getTotalUsedDistance(Long userPlan, String station);

	@SuppressWarnings("rawtypes")
	List getTotalAvailableDistanceFrom(Long userPlan, String station);

	@SuppressWarnings("rawtypes")
	List getTotalAvailableDistanceTo(Long userPlan, String station);

	@SuppressWarnings("rawtypes")
	List getTotalAvailableDistanceBoth(Long userPlan, String station);

}
