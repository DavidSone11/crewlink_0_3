package com.mathologic.projects.crewlink.helpers;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mathologic.projects.crewlink.custom.models.CircularList;
import com.mathologic.projects.crewlink.custom.models.RoundTripVM;
import com.mathologic.projects.crewlink.utils.converters.TimeUtils;

public class CrewlinkSolverV2 {
	TimeUtils timeUtils = new TimeUtils();

	CircularList<RoundTripVM> circularMainList;
	Map<Long, RoundTripVM> mainMap;
	ArrayList<Long> fromList;
	Map<Long,Integer> fromMap;
	ArrayList<Long> toList;
	Map<Long, Integer> toMap;
	List<RoundTripVM> selectedList;
	Map<Long,RoundTripVM> selectedFromMap;
	Map<Long,RoundTripVM> selectedToMap;
	
	HungarianAlgorithm hungarianAlgo;
	
	Integer minHQRestForLessThen8HrsDuty = 12;
	Integer minHQRestForMoreThen8HrsDuty = 16;
	Integer minPR = 30;
	Integer maxNoOfDaysForPR = 10;
	Integer minNoOfDaysForPR = 4;
	double[][] PRMatrix;
	double[][] HQRMatrix;
	double[][] temp;
	
	Integer attemptLimit = 10;
	Integer attemptNo = 0;

	Long minTotalTime = Long.MAX_VALUE;

	public CrewlinkSolverV2(CircularList<RoundTripVM> circularMainList,
			List<RoundTripVM> selectedList) {
		super();
		this.circularMainList = circularMainList;
		this.selectedList = selectedList;
	}

	public CrewlinkSolverV2(CircularList<RoundTripVM> circularMainList,
			List<RoundTripVM> selectedList,
			Integer minHQRestForLessThen8HrsDuty,
			Integer minHQRestForMoreThen8HrsDuty, Integer minPR,
			Integer maxNoOfDaysForPR,
			Integer minNoOfDaysForPR,
			Integer attemptLimit) {
		super();
		this.circularMainList = circularMainList;
		this.selectedList = selectedList;
		this.minHQRestForLessThen8HrsDuty = (minHQRestForLessThen8HrsDuty != null) ? minHQRestForLessThen8HrsDuty
				: this.minHQRestForLessThen8HrsDuty;
		this.minHQRestForMoreThen8HrsDuty = (minHQRestForMoreThen8HrsDuty != null) ? minHQRestForMoreThen8HrsDuty
				: this.minHQRestForMoreThen8HrsDuty;
		this.minPR = (minPR != null) ? minPR : this.minPR;
		this.minNoOfDaysForPR = (minNoOfDaysForPR != null)?minNoOfDaysForPR:this.minNoOfDaysForPR;
		this.maxNoOfDaysForPR = (maxNoOfDaysForPR != null)?maxNoOfDaysForPR:this.maxNoOfDaysForPR;
		this.attemptLimit = (attemptLimit != null)?attemptLimit:this.attemptLimit;
		
		selectedFromMap = new HashMap<Long,RoundTripVM>();
		for(RoundTripVM item : selectedList) {
			selectedFromMap.put(item.getId(), item);
		}
		selectedToMap = new HashMap<Long,RoundTripVM>();
		for(RoundTripVM item : selectedList) {
			selectedToMap.put(item.getId(), item);
		}
		mainMap = new HashMap<Long, RoundTripVM>();
		fromList = new ArrayList<Long>();
		fromMap = new HashMap<Long,Integer>();
		toList = new ArrayList<Long>();
		toMap = new HashMap<Long,Integer>();
		int lenFrom = 0;
		int lenTo = 0;
		for(RoundTripVM item : circularMainList) {
			mainMap.put(item.getId(), item);
			if(!selectedFromMap.containsKey(item.getId())) {
				fromList.add(item.getId());
				fromMap.put(item.getId(), lenFrom);
				lenFrom++;
			}
			if(!selectedToMap.containsKey(item.getId())) {
				toList.add(item.getId());
				toMap.put(item.getId(), lenTo);
				lenTo++;
			}
		}
//		HQRMatrix = new double[fromList.size()][toList.size()];
//		PRMatrix = new double[fromList.size()][toList.size()];
//		for(int i=0; i<fromList.size(); i++) {
//			for(int j=0; j<toList.size(); j++) {
//				Integer startDay = mainMap.get(fromList.get(i)).getDepartureDay().ordinal();
//				String startTime = mainMap.get(fromList.get(i)).getDepartureTime();
//				Integer fromDay = mainMap.get(fromList.get(i)).getArrivalDay().ordinal();
//				Integer toDay = mainMap.get(fromList.get(j)).getDepartureDay().ordinal();
//				String fromTime = mainMap.get(fromList.get(i)).getArrivalTime();
//				String toTime = mainMap.get(fromList.get(j)).getDepartureTime();
//				
//				Long lastDutyTime = timeUtils.findDiff(startDay, startTime, fromDay, fromTime, ChronoUnit.MINUTES);
//				Long restTime = new Long(this.minHQRestForMoreThen8HrsDuty*60);
//				if(lastDutyTime < 8*60) {
//					restTime = new Long(this.minHQRestForLessThen8HrsDuty*60);
//				}
//				Map<String,String> availableTime = timeUtils.calculateNextDayTime(fromDay, fromTime, restTime, ChronoUnit.MINUTES, "+");
//				HQRMatrix[i][j] =  timeUtils.findDiff(Integer.parseInt(availableTime.get("day")), availableTime.get("time"), toDay, toTime, ChronoUnit.MINUTES);
//				
//				Long prTime = new Long(this.minPR*60);
//				Map<String,String> availablePRTime = timeUtils.calculateNextDayTime(fromDay, fromTime, prTime, ChronoUnit.MINUTES, "+");
//				HQRMatrix[i][j] =  timeUtils.findDiff(Integer.parseInt(availablePRTime.get("day")), availablePRTime.get("time"), toDay, toTime, ChronoUnit.MINUTES);
//			}
//		}
	}
	
	public Boolean solve() {
		generateMatrix();
		
		hungarianAlgo = new HungarianAlgorithm(HQRMatrix);
		int[] HQRMatrixResult = hungarianAlgo.execute();
		
//		hungarianAlgo = new HungarianAlgorithm(PRMatrix);
//		int[] PRMatrixResult = hungarianAlgo.execute();
		
		Long timeAfterLastPR = 0L;
		Long from = fromList.get(0);
		Long to = -1L;
		boolean reRun = false;
		for(int i=1; i>=1 && from!=to; i=HQRMatrixResult[i],from = to) {
			if(!selectedFromMap.containsKey(from)) {
				selectedFromMap.put(from, mainMap.get(from));
				boolean found = false;
				for(RoundTripVM item : selectedList) {
					if(item.getId()==from) {
						found = true;
					}
				}
				if(!found) {
					selectedList.add(mainMap.get(from));
				}
			}
			to = toList.get(HQRMatrixResult[i]);
			Integer startDay = mainMap.get(from).getDepartureDay().ordinal();
			String startTime = mainMap.get(from).getDepartureTime();
			Integer fromDay = mainMap.get(from).getArrivalDay().ordinal();
			String fromTime = mainMap.get(from).getArrivalTime();
			Integer toDay = mainMap.get(to).getDepartureDay().ordinal();
			String toTime = mainMap.get(to).getDepartureTime();
			Integer endDay = mainMap.get(to).getArrivalDay().ordinal();
			String endTime = mainMap.get(to).getArrivalTime();
			
			Long workHrsWithOutPR = timeUtils.findDiff(startDay,startTime, endDay, endTime, ChronoUnit.MINUTES);
			workHrsWithOutPR += timeAfterLastPR;
			
//			if(workHrsWithOutPR > maxNoOfDaysForPR*60*24) {
//				int col = getMinFromPRMatrix(i);
//				if(col!=-1) {
//					to = toList.get(col);
//					selectedToMap.put(to, mainMap.get(to));
//					boolean found = false;
//					for(RoundTripVM item : selectedList) {
//						if(item.getId()==to) {
//							found = true;
//						}
//					}
//					if(!found) {
//						selectedList.add(mainMap.get(to));
//					}
//					timeAfterLastPR = 0L;
//					reRun = true;
//					break;
//				}
//			}
			Long nextHrs = timeUtils.findDiff(startDay,startTime, toDay, toTime, ChronoUnit.MINUTES);
			timeAfterLastPR += nextHrs;
			
			selectedToMap.put(to, mainMap.get(to));
			boolean found = false;
			for(RoundTripVM item : selectedList) {
				if(item.getId()==to) {
					found = true;
				}
			}
			if(!found) {
				selectedList.add(mainMap.get(to));
			}
		}
		
		if(!reRun) {
			if(selectedList.size()<circularMainList.size()) {
				solve();
				return true;
			}else {
				return true;
			}
			
		}else {
			solve();
		}
		
		return true;
	}
	
	public void generateMatrix() {
		fromList = new ArrayList<Long>();
		fromMap = new HashMap<Long,Integer>();
		toList = new ArrayList<Long>();
		toMap = new HashMap<Long,Integer>();
		int lenFrom = 0;
		int lenTo = 0;
		for(RoundTripVM item : circularMainList) {
			if(!selectedFromMap.containsKey(item.getId())) {
				fromList.add(item.getId());
				fromMap.put(item.getId(), lenFrom);
				lenFrom++;
			}
			if(!selectedToMap.containsKey(item.getId())) {
				toList.add(item.getId());
				toMap.put(item.getId(), lenTo);
				lenTo++;
			}
		}
		
		HQRMatrix = new double[fromList.size()][toList.size()];
		PRMatrix = new double[fromList.size()][toList.size()];
		for(int i=0; i<fromList.size(); i++) {
			for(int j=0; j<toList.size(); j++) {
				Integer startDay = mainMap.get(fromList.get(i)).getDepartureDay().ordinal();
				String startTime = mainMap.get(fromList.get(i)).getDepartureTime();
				Integer fromDay = mainMap.get(fromList.get(i)).getArrivalDay().ordinal();
				Integer toDay = mainMap.get(fromList.get(j)).getDepartureDay().ordinal();
				String fromTime = mainMap.get(fromList.get(i)).getArrivalTime();
				String toTime = mainMap.get(fromList.get(j)).getDepartureTime();
				
				Long lastDutyTime = timeUtils.findDiff(startDay, startTime, fromDay, fromTime, ChronoUnit.MINUTES);
				Long restTime = new Long(this.minHQRestForMoreThen8HrsDuty*60);
				if(lastDutyTime < 8*60) {
					restTime = new Long(this.minHQRestForLessThen8HrsDuty*60);
				}
				Map<String,String> availableTime = timeUtils.calculateNextDayTime(fromDay, fromTime, restTime, ChronoUnit.MINUTES, "+");
				HQRMatrix[i][j] =  timeUtils.findDiff(Integer.parseInt(availableTime.get("day")), availableTime.get("time"), toDay, toTime, ChronoUnit.MINUTES);
				
				Long prTime = new Long(this.minPR*60);
				Map<String,String> availablePRTime = timeUtils.calculateNextDayTime(fromDay, fromTime, prTime, ChronoUnit.MINUTES, "+");
				HQRMatrix[i][j] =  timeUtils.findDiff(Integer.parseInt(availablePRTime.get("day")), availablePRTime.get("time"), toDay, toTime, ChronoUnit.MINUTES);
			}
		}
		
	}
	
	public int getMinFromPRMatrix(int row) {
		int minCol = -1;
		double min = Double.MAX_VALUE;
		for(int j=1;j<PRMatrix[row].length;j++) {
			if(!selectedToMap.containsKey(toList.get(j))) {
				if(min>PRMatrix[row][j]) {
					min = PRMatrix[row][j];
					minCol = j;
				}
			}
		}
		if(minCol==-1) {
			if(!selectedToMap.containsKey(toList.get(0))){
				return 0;
			}
		}
		return minCol;
	}
	
}