package com.mathologic.projects.crewlink.custom.models;

import com.mathologic.projects.crewlink.models.Day;

public class DrivingDutyElementVM {
	
	// driving duty Element
	private Long id;
	private String fromStation;
	private String toStation;
	private String departureTime;
	private Day departureDay;
	private String arrivalTime;
	private Day arrivalDay;
	private Long duration;
	private Long distance;
	
	
	// Start pilot
	
	private String startPilotTypeName;
	private String startPilotTripName;
	private Day startPilotDepartureDay;
	private String startPilotDepartureTime;
	private Day startPilotArrivalDay;
	private String startPilotArrivalTime;
	private String startPilotFromStation;
	private String startPilotToStation;
	private Long startPilotduration;
	private Long startPilotdistance;
	
	
	
	
	// end Pilot 
	private String endPilotTypeName;
	private String endPilotTripName;
	private Day endPilotDepartureDay;
	private String endPilotDepartureTime;
	private Day endPilotArrivalDay;
	private String endPilotArrivalTime;
	private String endPilotFromStation;
	private String endPilotToStation;
	private Long endPilotduration;
	private Long endPilotdistance;
	
	
	
	// drivingSection
	private String 	drivingSectionFromStation;
	private String 	drivingSectionToStation;
	private Day 	drivingSectionDepartureDay;
	private String 	drivingSectionDepartureTime;
	private Day 	drivingSectionArrivalDay;
	private String 	drivingSectionArrivalTime;
	private Long 	drivingSectionDuration;
	private Long 	drivingSectionDistance;
	
	

	public DrivingDutyElementVM(Long id, String fromStation, String toStation, String departureTime, Day departureDay,
			String arrivalTime, Day arrivalDay, Long duration, Long distance, String startPilotTypeName,
			String startPilotTripName, String startPilotFromStation, String startPilotToStation,
			Long startPilotduration, Long startPilotdistance, Day startPilotDepartureDay,
			String startPilotDepartureTime, Day startPilotArrivalDay, String startPilotArrivalTime,
			String endPilotTypeName, String endPilotTripName, String endPilotFromStation, String endPilotToStation,
			Long endPilotduration, Long endPilotdistance, Day endPilotDepartureDay, String endPilotDepartureTime,
			Day endPilotArrivalDay, String endPilotArrivalTime, String drivingSectionFromStation,
			String drivingSectionToStation, Day drivingSectionDepartureDay, String drivingSectionDepartureTime,
			Day drivingSectionArrivalDay, String drivingSectionArrivalTime, Long drivingSectionDuration,
			Long drivingSectionDistance) {
		super();
		this.id = id;
		this.fromStation = fromStation;
		this.toStation = toStation;
		this.departureTime = departureTime;
		this.departureDay = departureDay;
		this.arrivalTime = arrivalTime;
		this.arrivalDay = arrivalDay;
		this.duration = duration;
		this.distance = distance;
		this.startPilotTypeName = startPilotTypeName;
		this.startPilotTripName = startPilotTripName;
		this.startPilotFromStation = startPilotFromStation;
		this.startPilotToStation = startPilotToStation;
		this.startPilotduration = startPilotduration;
		this.startPilotdistance = startPilotdistance;
		this.startPilotDepartureDay = startPilotDepartureDay;
		this.startPilotDepartureTime = startPilotDepartureTime;
		this.startPilotArrivalDay = startPilotArrivalDay;
		this.startPilotArrivalTime = startPilotArrivalTime;
		this.endPilotTypeName = endPilotTypeName;
		this.endPilotTripName = endPilotTripName;
		this.endPilotFromStation = endPilotFromStation;
		this.endPilotToStation = endPilotToStation;
		this.endPilotduration = endPilotduration;
		this.endPilotdistance = endPilotdistance;
		this.endPilotDepartureDay = endPilotDepartureDay;
		this.endPilotDepartureTime = endPilotDepartureTime;
		this.endPilotArrivalDay = endPilotArrivalDay;
		this.endPilotArrivalTime = endPilotArrivalTime;
		this.drivingSectionFromStation = drivingSectionFromStation;
		this.drivingSectionToStation = drivingSectionToStation;
		this.drivingSectionDepartureDay = drivingSectionDepartureDay;
		this.drivingSectionDepartureTime = drivingSectionDepartureTime;
		this.drivingSectionArrivalDay = drivingSectionArrivalDay;
		this.drivingSectionArrivalTime = drivingSectionArrivalTime;
		this.drivingSectionDuration = drivingSectionDuration;
		this.drivingSectionDistance = drivingSectionDistance;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFromStation() {
		return fromStation;
	}
	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}
	public String getToStation() {
		return toStation;
	}
	public void setToStation(String toStation) {
		this.toStation = toStation;
	}
	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	public Day getDepartureDay() {
		return departureDay;
	}
	public void setDepartureDay(Day departureDay) {
		this.departureDay = departureDay;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public Day getArrivalDay() {
		return arrivalDay;
	}
	public void setArrivalDay(Day arrivalDay) {
		this.arrivalDay = arrivalDay;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public Long getDistance() {
		return distance;
	}
	public void setDistance(Long distance) {
		this.distance = distance;
	}
	
	// setter and Getter 
	public String getStartPilotTypeName() {
		return startPilotTypeName;
	}
	public void setStartPilotTypeName(String startPilotTypeName) {
		this.startPilotTypeName = startPilotTypeName;
	}
	public String getStartPilotTripName() {
		return startPilotTripName;
	}
	public void setStartPilotTripName(String startPilotTripName) {
		this.startPilotTripName = startPilotTripName;
	}
	public String getStartPilotFromStation() {
		return startPilotFromStation;
	}
	public void setStartPilotFromStation(String startPilotFromStation) {
		this.startPilotFromStation = startPilotFromStation;
	}
	public String getStartPilotToStation() {
		return startPilotToStation;
	}
	public void setStartPilotToStation(String startPilotToStation) {
		this.startPilotToStation = startPilotToStation;
	}
	public Long getStartPilotduration() {
		return startPilotduration;
	}
	public void setStartPilotduration(Long startPilotduration) {
		this.startPilotduration = startPilotduration;
	}
	public Long getStartPilotdistance() {
		return startPilotdistance;
	}
	public void setStartPilotdistance(Long startPilotdistance) {
		this.startPilotdistance = startPilotdistance;
	}
	public String getEndPilotTypeName() {
		return endPilotTypeName;
	}
	public void setEndPilotTypeName(String endPilotTypeName) {
		this.endPilotTypeName = endPilotTypeName;
	}
	public String getEndPilotTripName() {
		return endPilotTripName;
	}
	public void setEndPilotTripName(String endPilotTripName) {
		this.endPilotTripName = endPilotTripName;
	}
	public String getEndPilotFromStation() {
		return endPilotFromStation;
	}
	public void setEndPilotFromStation(String endPilotFromStation) {
		this.endPilotFromStation = endPilotFromStation;
	}
	public String getEndPilotToStation() {
		return endPilotToStation;
	}
	public void setEndPilotToStation(String endPilotToStation) {
		this.endPilotToStation = endPilotToStation;
	}
	public Long getEndPilotduration() {
		return endPilotduration;
	}
	public void setEndPilotduration(Long endPilotduration) {
		this.endPilotduration = endPilotduration;
	}
	public Long getEndPilotdistance() {
		return endPilotdistance;
	}
	public void setEndPilotdistance(Long endPilotdistance) {
		this.endPilotdistance = endPilotdistance;
	}
	public String getDrivingSectionFromStation() {
		return drivingSectionFromStation;
	}
	public void setDrivingSectionFromStation(String drivingSectionFromStation) {
		this.drivingSectionFromStation = drivingSectionFromStation;
	}
	public String getDrivingSectionToStation() {
		return drivingSectionToStation;
	}
	public void setDrivingSectionToStation(String drivingSectionToStation) {
		this.drivingSectionToStation = drivingSectionToStation;
	}
	public Day getDrivingSectionDepartureDay() {
		return drivingSectionDepartureDay;
	}
	public void setDrivingSectionDepartureDay(Day drivingSectionDepartureDay) {
		this.drivingSectionDepartureDay = drivingSectionDepartureDay;
	}
	public String getDrivingSectionDepartureTime() {
		return drivingSectionDepartureTime;
	}
	public void setDrivingSectionDepartureTime(String drivingSectionDepartureTime) {
		this.drivingSectionDepartureTime = drivingSectionDepartureTime;
	}
	public Day getDrivingSectionArrivalDay() {
		return drivingSectionArrivalDay;
	}
	public void setDrivingSectionArrivalDay(Day drivingSectionArrivalDay) {
		this.drivingSectionArrivalDay = drivingSectionArrivalDay;
	}
	public String getDrivingSectionArrivalTime() {
		return drivingSectionArrivalTime;
	}
	public void setDrivingSectionArrivalTime(String drivingSectionArrivalTime) {
		this.drivingSectionArrivalTime = drivingSectionArrivalTime;
	}
	public Long getDrivingSectionDuration() {
		return drivingSectionDuration;
	}
	public void setDrivingSectionDuration(Long drivingSectionDuration) {
		this.drivingSectionDuration = drivingSectionDuration;
	}
	public Long getDrivingSectionDistance() {
		return drivingSectionDistance;
	}
	public void setDrivingSectionDistance(Long drivingSectionDistance) {
		this.drivingSectionDistance = drivingSectionDistance;
	}
	public Day getStartPilotDepartureDay() {
		return startPilotDepartureDay;
	}
	public void setStartPilotDepartureDay(Day startPilotDepartureDay) {
		this.startPilotDepartureDay = startPilotDepartureDay;
	}
	public String getStartPilotDepartureTime() {
		return startPilotDepartureTime;
	}
	public void setStartPilotDepartureTime(String startPilotDepartureTime) {
		this.startPilotDepartureTime = startPilotDepartureTime;
	}
	public Day getStartPilotArrivalDay() {
		return startPilotArrivalDay;
	}
	public void setStartPilotArrivalDay(Day startPilotArrivalDay) {
		this.startPilotArrivalDay = startPilotArrivalDay;
	}
	public String getStartPilotArrivalTime() {
		return startPilotArrivalTime;
	}
	public void setStartPilotArrivalTime(String startPilotArrivalTime) {
		this.startPilotArrivalTime = startPilotArrivalTime;
	}
	public Day getEndPilotDepartureDay() {
		return endPilotDepartureDay;
	}
	public void setEndPilotDepartureDay(Day endPilotDepartureDay) {
		this.endPilotDepartureDay = endPilotDepartureDay;
	}
	public String getEndPilotDepartureTime() {
		return endPilotDepartureTime;
	}
	public void setEndPilotDepartureTime(String endPilotDepartureTime) {
		this.endPilotDepartureTime = endPilotDepartureTime;
	}
	public Day getEndPilotArrivalDay() {
		return endPilotArrivalDay;
	}
	public void setEndPilotArrivalDay(Day endPilotArrivalDay) {
		this.endPilotArrivalDay = endPilotArrivalDay;
	}
	public String getEndPilotArrivalTime() {
		return endPilotArrivalTime;
	}
	public void setEndPilotArrivalTime(String endPilotArrivalTime) {
		this.endPilotArrivalTime = endPilotArrivalTime;
	}
	
	
	
	

	
}
