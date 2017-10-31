package com.mathologic.projects.crewlink.custom.models;

import java.io.Serializable;

import com.mathologic.projects.crewlink.models.Day;

public class PilotTripPM implements Serializable{
	// Train type
	private Integer trainNo;
//	private Day startDay;
	private String fromStation;
	private String toStation;
	
	// Other type
	private Day departureDay;
	private String departureTime;
	private Day arrivalDay;
	private String arrivalTime;
	private Long duration;
	private Long distance;
	private Long pilotType;
	private String name;
	
	public PilotTripPM(){};
	

	public PilotTripPM(Integer trainNo, String fromStation, String toStation, Day departureDay, String departureTime,
			Day arrivalDay, String arrivalTime, Long duration, Long distance, Long pilotType, String name) {
		super();
		this.trainNo = trainNo;
		this.fromStation = fromStation;
		this.toStation = toStation;
		this.departureDay = departureDay;
		this.departureTime = departureTime;
		this.arrivalDay = arrivalDay;
		this.arrivalTime = arrivalTime;
		this.duration = duration;
		this.distance = distance;
		this.pilotType = pilotType;
		this.name = name;
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
	public Day getDepartureDay() {
		return departureDay;
	}
	public void setDepartureDay(Day departureDay) {
		this.departureDay = departureDay;
	}
	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	public Day getArrivalDay() {
		return arrivalDay;
	}
	public void setArrivalDay(Day arrivalDay) {
		this.arrivalDay = arrivalDay;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
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
	public Long getPilotType() {
		return pilotType;
	}
	public void setPilotType(Long pilotType) {
		this.pilotType = pilotType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
