package com.mathologic.projects.crewlink.utils.converters;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mathologic.projects.crewlink.custom.models.CircularList;
import com.mathologic.projects.crewlink.custom.models.RoundTripVM;

public class CrewlinkSolver {
	TimeUtils timeUtils = new TimeUtils();

	CircularList<RoundTripVM> circularMainList;
	List<RoundTripVM> selectedList;
	Integer minHQRestForLessThen8HrsDuty = 12;
	Integer minHQRestForMoreThen8HrsDuty = 16;
	Integer minPR = 30;
	Integer maxNoOfDaysForPR = 10;
	Integer minNoOfDaysForPR = 4;
	
	Integer attemptLimit = 10;
	Integer attemptNo = 0;

	List<SolverItemDetails> selectedListDetails = new ArrayList<SolverItemDetails>();
	public List<RoundTripVM> minSelectedList;
	public List<SolverItemDetails> minSelectedListDetails;
	Long minTotalTime = Long.MAX_VALUE;

	public CrewlinkSolver(CircularList<RoundTripVM> circularMainList,
			List<RoundTripVM> selectedList) {
		super();
		this.circularMainList = circularMainList;
		this.selectedList = selectedList;
	}

	public CrewlinkSolver(CircularList<RoundTripVM> circularMainList,
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
	}
	
	public Boolean solve(){
		/* we can put a for loop to get it best result in all possible starting points */
		System.out.println("==== Start Cycles ====");
		Long totalTime = mainSolver(circularMainList.get(0), new SolverItemDetails());
		if(minTotalTime<Long.MAX_VALUE && minTotalTime == totalTime){
 			return true;
		}else{
			return false;
		}
	}
	
	public Long mainSolver(RoundTripVM RT, SolverItemDetails RT_D){
		selectedList.add(RT);
		Integer restHrs = calculateRestHrs(RT, RT_D);
		if(restHrs == -1){
			return Long.MAX_VALUE;
		}
		
		Integer index = getStartIndexOfPossibleSolutionsAfter(RT, restHrs);
		if(index == -1){
			System.out.println("|\t"+RT.getRtName()+"\t|\t"+RT.getDepartureDay()+"\t|\t"+RT.getDepartureTime()+"\t|\t");
			System.out.println("==== Cycle Complete ====");
			attemptNo++;
			return RT_D.mins;
		}
		
		Integer i = index;
		do{
			RoundTripVM nextRT = circularMainList.get(i);
			if(selectedList.indexOf(nextRT) == -1){
				Long result = Long.MAX_VALUE;
				if(attemptNo<attemptLimit){
					calculateDetailsNSave(RT,RT_D,nextRT,restHrs);
					System.out.println("|\t"+RT.getRtName()+"\t|\t"+RT.getDepartureDay()+"\t|\t"+RT.getDepartureTime()+"\t|\t"+(RT_D.lastHQRest/60)+" hrs\t|");
					result = mainSolver(nextRT,RT_D.getCopy());
				}
				else{
					return minTotalTime;
				}
				if(result < minTotalTime && selectedList.size()==circularMainList.size()){
					System.out.println("Min time = "+minTotalTime+" : "+(int)(minTotalTime/60)+"hrs");
					minSelectedList = new ArrayList<RoundTripVM>(selectedList);
					minSelectedListDetails = new ArrayList<SolverItemDetails>(selectedListDetails);
					minTotalTime = result;
				}
				selectedList.remove(selectedList.size()-1);
				selectedListDetails.remove(selectedListDetails.size()-1);
			}
			i = (i+1)%circularMainList.size();
		}while(i != index);
		
		return minTotalTime;
	}
	
	private void calculateDetailsNSave(RoundTripVM RT, SolverItemDetails RT_D,
			RoundTripVM nextRT, Integer restHrs) {
		Integer prDue = amountOfPR(RT);
		prDue *= 60;
		Map<String,String> availableTime = timeUtils.calculateNextDayTime(RT.getArrivalDay().ordinal(), RT.getArrivalTime(), 
											new Long(restHrs), ChronoUnit.HOURS, "+");
		Long availRest = timeUtils.findDiff( RT.getArrivalDay().ordinal(), RT.getArrivalTime(),
				Integer.parseInt(availableTime.get("day")), availableTime.get("time"), ChronoUnit.MINUTES);
		
		Long extraRest = timeUtils.findDiff(Integer.parseInt(availableTime.get("day")), availableTime.get("time"), 
				nextRT.getDepartureDay().ordinal(), nextRT.getDepartureTime(), ChronoUnit.MINUTES);
		
		Long restGiven = extraRest + availRest;
		if(prDue <= restGiven){
			RT_D.minsFromLastPR = 0L;
		}else{
			RT_D.minsFromLastPR += RT.getDuration()+restGiven;
		}
		RT_D.totalRest += restGiven;
		RT_D.lastHQRest = restGiven;
		RT_D.mins += RT.getDuration() + restGiven;
		RT_D.lastIndex = circularMainList.indexOf(RT);
		selectedListDetails.add(RT_D);
	}
	
	
	private Integer calculateRestHrs(RoundTripVM RT, SolverItemDetails RT_D) {
		if (RT_D.minsFromLastPR > (minNoOfDaysForPR * 24 * 60)
				|| (RT.getDuration() + RT_D.minsFromLastPR) > (maxNoOfDaysForPR * 24 * 60)) {
			return amountOfPR(RT);
		}
		/* if last duty > 8hrs */
		else if (RT.getLastDrivingDutyDuration() >= 8 * 60) {
			return minHQRestForMoreThen8HrsDuty;
		}
		/* if last duty < 8hrs */
		else if (RT.getLastDrivingDutyDuration() < 8 * 60) {
			return minHQRestForMoreThen8HrsDuty;
		}
		return -1;
	}
	private Integer amountOfPR(RoundTripVM RT){
		Long diff = timeUtils.findDiffWeekly(RT.getArrivalDay().ordinal(), RT.getArrivalTime(), 
				RT.getArrivalDay().ordinal(), "22:00:00", ChronoUnit.MINUTES);
		
		Long next22 = 0L;
		if(diff<0){
			next22 = timeUtils.findDiff(RT.getArrivalDay().ordinal(), RT.getArrivalTime(), 
					RT.getArrivalDay().ordinal()+1, "22:00:00", ChronoUnit.MINUTES);
			next22 = next22 + diff;
		}else{
			next22 = timeUtils.findDiff(RT.getArrivalDay().ordinal(), RT.getArrivalTime(), 
					RT.getArrivalDay().ordinal(), "22:00:00", ChronoUnit.MINUTES);
		}
		Long totalMins = next22 + (8*60);
		
		if(totalMins < minPR*60){
			totalMins = new Long(minPR*60);
		}
		return (int)(totalMins/60);
	}


	

	private Integer getStartIndexOfPossibleSolutionsAfter(
			RoundTripVM selectedRoundTrip, Integer hrs) {
		Integer i = 0;
		Integer currentRTindex = circularMainList.indexOf(selectedRoundTrip);
		Integer minIndex = -1;
		Long minDiffInMins = Long.MAX_VALUE;
		for (i = currentRTindex + 1;  i != currentRTindex; i = (i + 1) % circularMainList.size()) {
			if (!isSelected(circularMainList.get(i))) {
				Long diff = timeUtils.findDiff(selectedRoundTrip
						.getArrivalDay().ordinal(), selectedRoundTrip
						.getArrivalTime(), circularMainList.get(i)
						.getDepartureDay().ordinal(),
						circularMainList.get(i).getDepartureTime(),
						ChronoUnit.MINUTES);
				if (diff >= hrs*60) {
					if (diff < minDiffInMins) {
						minDiffInMins = diff;
						minIndex = i;
					}
				}
			}
		}
		return minIndex;
	}

	private Boolean isSelected(RoundTripVM roundTrip) {
		if(selectedList.indexOf(roundTrip) == -1)
			return false;
		return true;
	}

}

class SolverItemDetails {
	public Long mins = 0L;
	public Long minsWasted = 0L;
	public Long totalRest = 0L;
	public Long minsFromLastPR = 0L;
	public Integer lastDay = 0;
	public String lastTime = "00:00:00";
	public Integer lastIndex = -1;
	public Long lastHQRest = 0L;

	public SolverItemDetails getCopy() {
		SolverItemDetails itemDetails = new SolverItemDetails();
		itemDetails.lastDay = this.lastDay;
		itemDetails.lastTime = this.lastTime;
		itemDetails.totalRest = this.totalRest;
		itemDetails.mins = this.mins;
		itemDetails.minsFromLastPR = this.minsFromLastPR;
		itemDetails.minsWasted = this.minsWasted;
		itemDetails.lastHQRest = this.lastHQRest;
		itemDetails.lastIndex = this.lastIndex;
		itemDetails.lastDay = this.lastDay;
		itemDetails.lastTime = this.lastTime;
		return itemDetails;
	}
}