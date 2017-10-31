package com.mathologic.projects.crewlink.custom.models;

import com.mathologic.projects.crewlink.models.Day;

public class CrewLinkDrivingSectionsVM {
	private Integer rtOrder;
	private String rtSignOn;
	private Day rtSignOnDay;
	private Integer ddOrder;
	private String ddSignOn;
	private Day ddSignOnDay;
	private Long ddDuration;
	private Integer ddeOrder;
	private String ddeStartTime;
	private Day ddeStartDay;
	private String startPilot;
	private String spStation;
	private Long spDistance;
	private String spToStation;
	private Day spArrivalDay;
	private String spArrivalTime;
	private String dsFrom;
	private Integer dsTrain;
	private Day dsTrainOriginDay;
	private Long dsDistance;
	private String dsTo;
	private Day dsDepartureDay;
	private String dsDepartureTime;
	private Day dsArrivalDay;
	private String dsArrivalTime;
	private String endPilot;
	private String epStation;
	private Long epDistance;
	private String epFromStation;
	private Day epDepartureDay;
	private String epDepartureTime;
	private String ddeEndTime;
	private Day ddeEndDay;
	private String ddSignOff;
	private Day ddSignOffDay;
	private String rtSignOff;
	private Day rtSignOffDay;
	private Integer rtOSRest;
	
	
	public CrewLinkDrivingSectionsVM(Integer rtOrder, String rtSignOn,
			Day rtSignOnDay, Integer ddOrder, String ddSignOn, Day ddSignOnDay,
			Long ddDuration, Integer ddeOrder, String ddeStartTime,
			Day ddeStartDay, String startPilot, String spStation,
			Long spDistance, String spToStation, Day spArrivalDay,
			String spArrivalTime, String dsFrom, Integer dsTrain,
			Day dsTrainOriginDay, Long dsDistance, String dsTo,
			Day dsDepartureDay, String dsDepartureTime, Day dsArrivalDay,
			String dsArrivalTime, String endPilot, String epStation,
			Long epDistance, String epFromStation, Day epDepartureDay,
			String epDepartureTime, String ddeEndTime, Day ddeEndDay,
			String ddSignOff, Day ddSignOffDay, String rtSignOff,
			Day rtSignOffDay, Integer rtOSRest) {
		super();
		this.rtOrder = rtOrder;
		this.rtSignOn = rtSignOn;
		this.rtSignOnDay = rtSignOnDay;
		this.ddOrder = ddOrder;
		this.ddSignOn = ddSignOn;
		this.ddSignOnDay = ddSignOnDay;
		this.ddDuration = ddDuration;
		this.ddeOrder = ddeOrder;
		this.ddeStartTime = ddeStartTime;
		this.ddeStartDay = ddeStartDay;
		this.startPilot = startPilot;
		this.spStation = spStation;
		this.spDistance = spDistance;
		this.spToStation = spToStation;
		this.spArrivalDay = spArrivalDay;
		this.spArrivalTime = spArrivalTime;
		this.dsFrom = dsFrom;
		this.dsTrain = dsTrain;
		this.dsTrainOriginDay = dsTrainOriginDay;
		this.dsDistance = dsDistance;
		this.dsTo = dsTo;
		this.dsDepartureDay = dsDepartureDay;
		this.dsDepartureTime = dsDepartureTime;
		this.dsArrivalDay = dsArrivalDay;
		this.dsArrivalTime = dsArrivalTime;
		this.endPilot = endPilot;
		this.epStation = epStation;
		this.epDistance = epDistance;
		this.epFromStation = epFromStation;
		this.epDepartureDay = epDepartureDay;
		this.epDepartureTime = epDepartureTime;
		this.ddeEndTime = ddeEndTime;
		this.ddeEndDay = ddeEndDay;
		this.ddSignOff = ddSignOff;
		this.ddSignOffDay = ddSignOffDay;
		this.rtSignOff = rtSignOff;
		this.rtSignOffDay = rtSignOffDay;
		this.rtOSRest = rtOSRest;
	}


	public Integer getRtOrder() {
		return rtOrder;
	}


	public void setRtOrder(Integer rtOrder) {
		this.rtOrder = rtOrder;
	}


	public String getRtSignOn() {
		return rtSignOn;
	}


	public void setRtSignOn(String rtSignOn) {
		this.rtSignOn = rtSignOn;
	}


	public Day getRtSignOnDay() {
		return rtSignOnDay;
	}


	public void setRtSignOnDay(Day rtSignOnDay) {
		this.rtSignOnDay = rtSignOnDay;
	}


	public Integer getDdOrder() {
		return ddOrder;
	}


	public void setDdOrder(Integer ddOrder) {
		this.ddOrder = ddOrder;
	}


	public String getDdSignOn() {
		return ddSignOn;
	}


	public void setDdSignOn(String ddSignOn) {
		this.ddSignOn = ddSignOn;
	}


	public Day getDdSignOnDay() {
		return ddSignOnDay;
	}


	public void setDdSignOnDay(Day ddSignOnDay) {
		this.ddSignOnDay = ddSignOnDay;
	}


	public Long getDdDuration() {
		return ddDuration;
	}


	public void setDdDuration(Long ddDuration) {
		this.ddDuration = ddDuration;
	}


	public Integer getDdeOrder() {
		return ddeOrder;
	}


	public void setDdeOrder(Integer ddeOrder) {
		this.ddeOrder = ddeOrder;
	}


	public String getDdeStartTime() {
		return ddeStartTime;
	}


	public void setDdeStartTime(String ddeStartTime) {
		this.ddeStartTime = ddeStartTime;
	}


	public Day getDdeStartDay() {
		return ddeStartDay;
	}


	public void setDdeStartDay(Day ddeStartDay) {
		this.ddeStartDay = ddeStartDay;
	}


	public String getStartPilot() {
		return startPilot;
	}


	public void setStartPilot(String startPilot) {
		this.startPilot = startPilot;
	}


	public String getSpStation() {
		return spStation;
	}


	public void setSpStation(String spStation) {
		this.spStation = spStation;
	}


	public Long getSpDistance() {
		return spDistance;
	}


	public void setSpDistance(Long spDistance) {
		this.spDistance = spDistance;
	}


	public String getSpToStation() {
		return spToStation;
	}


	public void setSpToStation(String spToStation) {
		this.spToStation = spToStation;
	}


	public Day getSpArrivalDay() {
		return spArrivalDay;
	}


	public void setSpArrivalDay(Day spArrivalDay) {
		this.spArrivalDay = spArrivalDay;
	}


	public String getSpArrivalTime() {
		return spArrivalTime;
	}


	public void setSpArrivalTime(String spArrivalTime) {
		this.spArrivalTime = spArrivalTime;
	}


	public String getDsFrom() {
		return dsFrom;
	}


	public void setDsFrom(String dsFrom) {
		this.dsFrom = dsFrom;
	}


	public Integer getDsTrain() {
		return dsTrain;
	}


	public void setDsTrain(Integer dsTrain) {
		this.dsTrain = dsTrain;
	}


	public Day getDsTrainOriginDay() {
		return dsTrainOriginDay;
	}


	public void setDsTrainOriginDay(Day dsTrainOriginDay) {
		this.dsTrainOriginDay = dsTrainOriginDay;
	}


	public Long getDsDistance() {
		return dsDistance;
	}


	public void setDsDistance(Long dsDistance) {
		this.dsDistance = dsDistance;
	}


	public String getDsTo() {
		return dsTo;
	}


	public void setDsTo(String dsTo) {
		this.dsTo = dsTo;
	}


	public Day getDsDepartureDay() {
		return dsDepartureDay;
	}


	public void setDsDepartureDay(Day dsDepartureDay) {
		this.dsDepartureDay = dsDepartureDay;
	}


	public String getDsDepartureTime() {
		return dsDepartureTime;
	}


	public void setDsDepartureTime(String dsDepartureTime) {
		this.dsDepartureTime = dsDepartureTime;
	}


	public Day getDsArrivalDay() {
		return dsArrivalDay;
	}


	public void setDsArrivalDay(Day dsArrivalDay) {
		this.dsArrivalDay = dsArrivalDay;
	}


	public String getDsArrivalTime() {
		return dsArrivalTime;
	}


	public void setDsArrivalTime(String dsArrivalTime) {
		this.dsArrivalTime = dsArrivalTime;
	}


	public String getEndPilot() {
		return endPilot;
	}


	public void setEndPilot(String endPilot) {
		this.endPilot = endPilot;
	}


	public String getEpStation() {
		return epStation;
	}


	public void setEpStation(String epStation) {
		this.epStation = epStation;
	}


	public Long getEpDistance() {
		return epDistance;
	}


	public void setEpDistance(Long epDistance) {
		this.epDistance = epDistance;
	}


	public String getEpFromStation() {
		return epFromStation;
	}


	public void setEpFromStation(String epFromStation) {
		this.epFromStation = epFromStation;
	}


	public Day getEpDepartureDay() {
		return epDepartureDay;
	}


	public void setEpDepartureDay(Day epDepartureDay) {
		this.epDepartureDay = epDepartureDay;
	}


	public String getEpDepartureTime() {
		return epDepartureTime;
	}


	public void setEpDepartureTime(String epDepartureTime) {
		this.epDepartureTime = epDepartureTime;
	}


	public String getDdeEndTime() {
		return ddeEndTime;
	}


	public void setDdeEndTime(String ddeEndTime) {
		this.ddeEndTime = ddeEndTime;
	}


	public Day getDdeEndDay() {
		return ddeEndDay;
	}


	public void setDdeEndDay(Day ddeEndDay) {
		this.ddeEndDay = ddeEndDay;
	}


	public String getDdSignOff() {
		return ddSignOff;
	}


	public void setDdSignOff(String ddSignOff) {
		this.ddSignOff = ddSignOff;
	}


	public Day getDdSignOffDay() {
		return ddSignOffDay;
	}


	public void setDdSignOffDay(Day ddSignOffDay) {
		this.ddSignOffDay = ddSignOffDay;
	}


	public String getRtSignOff() {
		return rtSignOff;
	}


	public void setRtSignOff(String rtSignOff) {
		this.rtSignOff = rtSignOff;
	}


	public Day getRtSignOffDay() {
		return rtSignOffDay;
	}


	public void setRtSignOffDay(Day rtSignOffDay) {
		this.rtSignOffDay = rtSignOffDay;
	}


	public Integer getRtOSRest() {
		return rtOSRest;
	}


	public void setRtOSRest(Integer rtOSRest) {
		this.rtOSRest = rtOSRest;
	}
	
}
