package com.mathologic.projects.crewlink.custom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.repositories.ImprovementSuggestionsRepository;

/**
 * This controller is used to fetch details of trains in list form.
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
@Controller
@RequestMapping("/api/custom/improvementSuggestions")
public class ImprovementSuggestions {
	@Autowired
	ImprovementSuggestionsRepository improvementSuggestionsRepository;

	@RequestMapping(value = "/drivingSection", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel listDrivingSections(
			@RequestParam(value = "userPlan", required = true) Long userPlan,
			@RequestParam(value = "maxDuration", required = true) String maxDuration) {
		SelectViewModel processResult = null;
		Long hr, min, maxDur;
		try {
			if (maxDuration.contains(":")) {
				String[] dur = maxDuration.split(":");
				if (dur.length == 2) {
					hr = Long.parseLong(dur[0]);
					min = Long.parseLong(dur[1]);
					maxDur = hr * 60 + min;
				} else {
					return processResult;
				}

			} else {

				hr = Long.parseLong(maxDuration);
				maxDur = hr * 60;

			}
		} catch (Exception ex) {
			return processResult;
		}
		processResult = improvementSuggestionsRepository.drivingSection(
				userPlan, maxDur);
		return processResult;

	}

	@RequestMapping(value = "/longDrivingDuty", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel listLongDrivingDuties(
			@RequestParam(value = "userPlan", required = true) Long userPlan,
			@RequestParam(value = "maxDuration", required = true) String maxDuration) {
		SelectViewModel processResult = null;
		Long hr, min, maxDur;
		try {
			if (maxDuration.contains(":")) {
				String[] dur = maxDuration.split(":");
				if (dur.length == 2) {
					hr = Long.parseLong(dur[0]);
					min = Long.parseLong(dur[1]);
					maxDur = hr * 60 + min;
				} else {
					return processResult;
				}

			} else {

				hr = Long.parseLong(maxDuration);
				maxDur = hr * 60;

			}
		} catch (Exception ex) {
			return processResult;
		}
		processResult = improvementSuggestionsRepository.longDrivingDuty(
				userPlan, maxDur);
		return processResult;

	}

	@RequestMapping(value = "/drivingDutyWithDailyPilots", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel listDrivingDutiesWithDailyPilots(
			@RequestParam(value = "userPlan", required = true) Long userPlan,
			@RequestParam(value = "minRunDays", required = true) Integer minRunDays) {
		SelectViewModel processResult = null;

		processResult = improvementSuggestionsRepository
				.drivingDutyWithDailyPilots(userPlan, minRunDays);
		return processResult;

	}

	@RequestMapping(value = "/roundTripWithExcessOSR", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel listRoundTripsWithExcessOSR(
			@RequestParam(value = "userPlan", required = true) Long userPlan,
			@RequestParam(value = "maxOSR", required = true) String maxOSR) {
		SelectViewModel processResult = null;
		Long hr, min, maxDur;
		try {
			if (maxOSR.contains(":")) {
				String[] dur = maxOSR.split(":");
				if (dur.length == 2) {
					hr = Long.parseLong(dur[0]);
					min = Long.parseLong(dur[1]);
					maxDur = hr * 60 + min;
				} else {
					return processResult;
				}

			} else {

				hr = Long.parseLong(maxOSR);
				maxDur = hr * 60;

			}
		} catch (Exception ex) {
			return processResult;
		}
		processResult = improvementSuggestionsRepository
				.roundTripWithExcessOSR(userPlan, maxDur);
		return processResult;

	}

}
