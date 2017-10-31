package com.mathologic.projects.crewlink.custom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.repositories.TrainStationVMRepository;
import com.mathologic.projects.crewlink.models.Day;

/**
 * This controller is used to fetch details of train running details in list
 * form.
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
@Controller
@RequestMapping("/api/custom/trainStations")
public class TrainStationVMController {
	@Autowired
	TrainStationVMRepository trainStationVMRepository;

	/**
	 * This is used to list the train running details of particular train.
	 * 
	 * @param trainNo(
	 *            eg. 11005 )
	 * @param startDay
	 *            ( eg. 'MONDAY' )
	 * @param stationCode
	 *            ( eg. 'DR' )
	 * @param sort
	 *            ( eg. trainNo DESC )
	 * @param page
	 *            ( eg. 1,2....)
	 * @param size
	 *            ( eg. 10,20... )
	 * @return SelectViewModel ( eg. { selectionDetails : { totalItems : 20 },
	 *         data : [], fields: { item1 : 0, item2 : 1 } } )
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel listTrainStations(
			@RequestParam(value = "trainNo", required = false) Integer trainNo,
			@RequestParam(value = "startDay", required = false) String startDay,
			@RequestParam(value = "stationCode", required = false) String stationCode,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "page", required = false, defaultValue = "0") Long page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Long size) {
		SelectViewModel result = null;
		Day startDayI = null;
		if (startDay != null) {
			try {
				startDayI = Day.valueOf(startDay);
			} catch (Exception ex) {
				System.out.println("Error : " + ex.getMessage());
			}
		}
		

		result = trainStationVMRepository.listTrainStations(trainNo, startDayI, stationCode, sort, page, size);
		return result;
	}

	/**
	 * @param trainNo(
	 *            eg. 11005 )
	 * @param startDay
	 *            ( eg. 'MONDAY' )
	 * @param stationCode
	 *            ( eg. 'DR' )
	 * @param userPlan 
	 * 			  ( eg. 1,2..)
	 * @param sort
	 *            ( eg. trainNo DESC )
	 * @param page
	 *            ( eg. 1,2....)
	 * @param size
	 *            ( eg. 10,20... )
	 * @return SelectViewModel ( eg. { selectionDetails : { totalItems : 20 },
	 *         data : [], fields: { item1 : 0, item2 : 1 } } )
	 */
	@RequestMapping(value = "/listWithDrivingSections", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel listTrainStationsWithDrivingSections(
			@RequestParam(value = "trainNo", required = false) Integer trainNo,
			@RequestParam(value = "startDay", required = false) String startDay,
			@RequestParam(value = "stationCode", required = false) String stationCode,
			@RequestParam(value = "userPlan", required = false) Long userPlan,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "page", required = false, defaultValue = "0") Long page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Long size) {
		SelectViewModel result = null;
		Day startDayI = null;
		if (startDay != null) {
			try {
				startDayI = Day.valueOf(startDay);
			} catch (Exception ex) {
				System.out.println("Error : " + ex.getMessage());
			}
		}

		result = trainStationVMRepository.listTrainStationsWithDrivingSection(trainNo, startDayI, stationCode, userPlan,
				sort, page, size);
		return result;
	}
}
