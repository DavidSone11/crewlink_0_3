package com.mathologic.projects.crewlink.custom.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mathologic.projects.crewlink.custom.models.CircularList;
import com.mathologic.projects.crewlink.custom.models.OrderedRoundTrips;
import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.RoundTripSM;
import com.mathologic.projects.crewlink.custom.models.RoundTripVM;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.repositories.CrewLinkVMRepository;
import com.mathologic.projects.crewlink.custom.repositories.RoundTripVMRepository;
import com.mathologic.projects.crewlink.helpers.CrewlinkSolverV3;
import com.mathologic.projects.crewlink.models.Day;
import com.mathologic.projects.crewlink.utils.converters.ResultListToObject;

/**
 * This controller is used to list crew links and create crew links.
 * 
 * @author Vivek Yadav, Laxman
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */

@Controller
@RequestMapping("/api/custom/crewLinks")
public class CrewLinkVMController {
	@Autowired
	CrewLinkVMRepository crewLinkVMRepository;

	@Autowired
	RoundTripVMRepository roundTripVMRepository;

	/**
	 * This is used to list crew links
	 * 
	 * @param userPlan
	 *            ( eg. 1,2.. )
	 * @param linkName
	 *            ( eg. 11005 ; 11006 )
	 * @param locoPilots
	 *            ( eg. 1,2.. )
	 * @param station
	 *            ( eg. 'SBC' )
	 * @param minDuration
	 *            ( eg. 200 )
	 * @param maxDuration
	 *            ( eg. 1200 )
	 * @param minDistance
	 *            ( eg. 200 )
	 * @param maxDistance
	 *            ( eg. 2000 )
	 * @param departureDay
	 *            ( eg. 'MONDAY' )
	 * @param minDepartureTime
	 *            ( eg. 10:00 )
	 * @param maxDepartureTime
	 *            ( eg. 22:00 )
	 * @param arrivalDay
	 *            ( eg. 'TUESDAY')
	 * @param minArrivalTime
	 *            ( eg. 2:00 )
	 * @param maxArrivalTime
	 *            ( eg. 22:00 )
	 * @param sort
	 *            ( eg. linkName DESC )
	 * @param page
	 *            ( eg. 1,2.. )
	 * @param size
	 *            ( eg. 10,20 ...)
	 * @return SelectViewModel ( eg. { selectionDetails : { totalItems : 20 },
	 *         data : [], fields: { item1 : 0, item2 : 1 } } )
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel listCrewLinks(
			@RequestParam(value = "userPlan", required = true) String userPlan,
			@RequestParam(value = "linkName", required = false) String linkName,
			@RequestParam(value = "locoPilots", required = false) Integer locoPilots,
			@RequestParam(value = "station", required = false) String station,
			@RequestParam(value = "minDuration", required = false) Long minDuration,
			@RequestParam(value = "maxDuration", required = false) Long maxDuration,
			@RequestParam(value = "minDistance", required = false) Long minDistance,
			@RequestParam(value = "maxDistance", required = false) Long maxDistance,
			@RequestParam(value = "departureDay", required = false) String departureDay,
			@RequestParam(value = "minDepartureTime", required = false) String minDepartureTime,
			@RequestParam(value = "maxDepartureTime", required = false) String maxDepartureTime,
			@RequestParam(value = "arrivalDay", required = false) String arrivalDay,
			@RequestParam(value = "minArrivalTime", required = false) String minArrivalTime,
			@RequestParam(value = "maxArrivalTime", required = false) String maxArrivalTime,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "page", required = false, defaultValue = "0") Long page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Long size) {
		SelectViewModel result = null;
		Day departureDayI = null, arrivalDayI = null;
		if (departureDay != null) {
			try {
				// convert departure day name into departure day value
				departureDayI = Day.valueOf(departureDay);
			} catch (Exception ex) {
				System.out.println("Error : " + ex.getMessage());
			}
		}
		if (arrivalDay != null) {
			try {
				// convert arrival day name into arrival day value
				arrivalDayI = Day.valueOf(arrivalDay);
			} catch (Exception ex) {
				System.out.println("Error : " + ex.getMessage());
			}
		}
		// start = (start==null)?1:start;
		// offset= (offset==null)?10:offset;

		result = crewLinkVMRepository.listCrewLinks(userPlan, linkName,
				locoPilots, station, minDuration, maxDuration, minDistance,
				maxDistance, departureDayI, minDepartureTime, maxDepartureTime,
				arrivalDayI, minArrivalTime, maxArrivalTime, sort, page, size);
		return result;
	}

	/**
	 * This is used to fetch driving sections used for creating crew link
	 * 
	 * @param userPlan
	 *            ( eg. 1,2.. )
	 * @param crewLink
	 *            ( eg. 1,2.. )
	 * @param sort
	 *            ( eg. fromStation DESC )
	 * @param page
	 *            ( eg. 1,2.. )
	 * @param size
	 *            ( eg. 10,20.. )
	 * @return SelectViewModel ( eg. { selectionDetails : { totalItems : 20 },
	 *         data : [], fields: { item1 : 0, item2 : 1 } } )
	 */
	@RequestMapping(value = "/listDrivingSections", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel listDrivingSections(
			@RequestParam(value = "userPlan", required = true) String userPlan,
			@RequestParam(value = "crewLink", required = false) String crewLink,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "page", required = false, defaultValue = "0") Long page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Long size) {
		SelectViewModel result = null;
		result = crewLinkVMRepository.listDrivingSections(userPlan, crewLink,
				sort, page, size);
		return result;
	}
	
	@RequestMapping(value = "/stationSummary", method = RequestMethod.GET)
	public @ResponseBody SelectViewModel stationSummary(
			@RequestParam(value = "userPlan", required = true) Long userPlan) {
		SelectViewModel processResult = null;
		
		processResult = crewLinkVMRepository.stationSummary(userPlan);
		return processResult;

	}

	/**
	 * This is used to create crew links
	 * 
	 * @param roundTripIds
	 *            ( eg. 10, 20.. )
	 * @param orderNos
	 *            ( eg. 1,2.. )
	 * @param crewLinkName
	 *            ( eg. SBC ACME express)
	 * @param userPlan
	 *            ( eg. 1,2.. )
	 * @return ProcessResult ( result=true or false, outputValue=success value,
	 *         errorMessage="failure message.")
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public @ResponseBody ProcessResult createCrewLink(
			@RequestParam(value = "roundTripIds", required = true) String roundTripIds,
			@RequestParam(value = "orderNos", required = true) String orderNos,
			@RequestParam(value = "crewLinkName", required = true) String crewLinkName,
			@RequestParam(value = "userPlan", required = true) Long userPlan) {

		ProcessResult processResult = null;
		List<String> items = Arrays.asList(roundTripIds.split("\\s*,\\s*"));
		Integer uniqueCrewLinkId = (int)(Math.random() * ((999999 - 1) + 1)) + 1;
		for(int i=0; i<items.size(); i++){
			roundTripVMRepository.insertRoundTripForCrewLinkGeneration(Long.parseLong(items.get(i)), i+1, uniqueCrewLinkId);
		}
		processResult = crewLinkVMRepository.save(uniqueCrewLinkId,
				crewLinkName, userPlan);
		return processResult;
	}

	@RequestMapping(value = "/auto-gen", method = RequestMethod.GET)
	public @ResponseBody ProcessResult createCrewLink(
			@RequestParam(value = "baseStation", required = true) String baseStation,
			@RequestParam(value = "crewType", required = true) Long crewType,
			@RequestParam(value = "crewTypeName", required = true) String crewTypeName,
			@RequestParam(value = "minHQRestForLessThen8HrsDuty", required = false) Integer minHQRestForLessThen8HrsDuty,
			@RequestParam(value = "minHQRestForMoreThen8HrsDuty", required = false) Integer minHQRestForMoreThen8HrsDuty,
			@RequestParam(value = "minPR", required = false) Integer minPR,
			@RequestParam(value = "maxNoOfDaysForPR", required = false) Integer maxNoOfDaysForPR,
			@RequestParam(value = "minNoOfDaysForPR", required = false) Integer minNoOfDaysForPR,
			@RequestParam(value = "attemptLimit", required = false) Integer attemptLimit,
			@RequestParam(value = "userPlan", required = true) Long userPlan) {
		ProcessResult processResult = null;
		String crewLinkName = baseStation + "-" + crewTypeName + "-AUTO";
		List<RoundTripVM> mainList = fetchMainList(baseStation, crewType,
				userPlan);
		if (mainList == null || mainList.size() == 0) {
			processResult = new ProcessResult(false,
					"No round trips found for given station and crew type !!");
		} else {
			CircularList<RoundTripVM> circularMainList = new CircularList<RoundTripVM>(
					mainList);

			List<RoundTripVM> selectedList = new ArrayList<RoundTripVM>();

//			CrewlinkSolver solver = new CrewlinkSolver(circularMainList,
//					selectedList, minHQRestForLessThen8HrsDuty,
//					minHQRestForMoreThen8HrsDuty, minPR, maxNoOfDaysForPR,
//					minNoOfDaysForPR, attemptLimit);
//			CrewlinkSolverV2 solver = new CrewlinkSolverV2(circularMainList,
//					selectedList, minHQRestForLessThen8HrsDuty,
//					minHQRestForMoreThen8HrsDuty, minPR, maxNoOfDaysForPR,
//					minNoOfDaysForPR, attemptLimit);
			int totalRT = mainList.size();
			int prevRTCount = 0;
			int count = 1;
			OrderedRoundTrips result = new OrderedRoundTrips();
			result.roundTrips = new ArrayList<RoundTripSM>();
			while(result.roundTrips != null && result.roundTrips.size() + prevRTCount < totalRT) {
				CrewlinkSolverV3 solver = new CrewlinkSolverV3(mainList,minHQRestForLessThen8HrsDuty,minHQRestForMoreThen8HrsDuty,minPR,minNoOfDaysForPR,maxNoOfDaysForPR);
				result = solver.execute();
				if (result.roundTrips!=null && result.roundTrips.size()>0) {
					//Map<String, String> idLists = getIdList(solver.minSelectedList);
					Integer uniqueCrewLinkId = (int)(Math.random() * ((999999 - 1) + 1)) + 1;
					for(int i=0; i<result.roundTrips.size(); i++){
						roundTripVMRepository.insertRoundTripForCrewLinkGeneration(result.roundTrips.get(i).id, i+1, uniqueCrewLinkId);
					}
					String clName = crewLinkName+"-"+((count<10)?"0"+count:count);
					processResult = crewLinkVMRepository.save(uniqueCrewLinkId, clName, userPlan);
					
					if(result.roundTrips.size()+prevRTCount<totalRT) {
						mainFor: for(int x=0; x<mainList.size(); ) {
							for(RoundTripSM rtsm : result.roundTrips) {
								if(mainList.get(x).getId() == rtsm.id) {
									mainList.remove(x);
									continue mainFor;
								}
							}
							x++;
						}
					}
				} else if(count==1){
					processResult = new ProcessResult(false,
							"Failed to find Proper solutions");
				}
				count++;
			}
//			Boolean result = solver.solve();
//			if (result) {
//				//Map<String, String> idLists = getIdList(solver.minSelectedList);
//				Integer uniqueCrewLinkId = (int)(Math.random() * ((999999 - 1) + 1)) + 1;
//				for(int i=0; i<solver.minSelectedList.size(); i++){
//					roundTripVMRepository.insertRoundTripForCrewLinkGeneration(solver.minSelectedList.get(i).getId(), i+1, uniqueCrewLinkId);
//				}
//				processResult = crewLinkVMRepository.save(uniqueCrewLinkId, crewLinkName, userPlan);
//			} else {
//				processResult = new ProcessResult(result,
//						"Failed to find Proper solutions");
//			}
		}
		return processResult;
	}

	private List<RoundTripVM> fetchMainList(String baseStation, Long crewType,
			Long userPlan) {
		Integer startDay = 0;
		String startTime = "00:00:01";
		List<RoundTripVM> roundTrips = null;

		String sort = " CONCAT(departureDay,departureTime)<'" + startDay
				+ startTime + "',CONCAT(departureDay,departureTime)";
		SelectViewModel listOfRoundTrips = roundTripVMRepository
				.listRoundTrips(userPlan, null, baseStation, null, null, null,
						null, null, null, null, null, null, null, null, null,
						null, null, crewType, false, false, null, sort, 0L,
						1000L, null,null,null);
		if (listOfRoundTrips.getData() != null
				&& listOfRoundTrips.getData().size() > 0) {
			roundTrips = ResultListToObject.convertAll(
					listOfRoundTrips.getData(), RoundTripVM.class);
		}
		return roundTrips;
	}

	private Map<String, String> getIdList(List<RoundTripVM> selectedList) {
		Integer i = 1;
		String idList = "";
		String orderList = "";
		for (RoundTripVM item : selectedList) {
			idList += "," + item.getId();
			orderList += "," + i;
			i++;
		}
		idList = idList.substring(1);
		orderList = orderList.substring(1);
		Map<String, String> result = new HashMap<String, String>();
		result.put("idList", idList);
		result.put("orderList", orderList);
		return result;
	}

}
