package com.mathologic.projects.crewlink.custom.repositories;

import com.mathologic.projects.crewlink.custom.models.SelectViewModel;

/**
 * This repository interface contains declarations of list driving duties,
 * delete driving duties .
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
public interface ImprovementSuggestionsRepository {

	SelectViewModel drivingSection(Long userPlan, Long maxDuration);

	SelectViewModel longDrivingDuty(Long userPlan, Long maxDuration);

	SelectViewModel drivingDutyWithDailyPilots(Long userPlan, Integer minRunDays);

	SelectViewModel roundTripWithExcessOSR(Long userPlan, Long maxOSR);
}