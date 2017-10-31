package com.mathologic.projects.crewlink.custom.models;

import com.mathologic.projects.crewlink.models.Day;

public class PilotTripVM {

	private Long id;
	private String pilotTripName;
	private String pilotType;
	private String fromStation;
	private String toStation;
	private Day departureDay;
	private Day arrivalDay;
	private String departureTime;
	private String arrivalTime;
	private Long distance;
	private Long duration;




	public PilotTripVM(Long id, String pilotTripName, String pilotType, String fromStation, String toStation,
			Day departureDay, Day arrivalDay, String departureTime, String arrivalTime, Long distance, Long duration) {
		super();
		this.id = id;
		this.pilotTripName = pilotTripName;
		this.pilotType = pilotType;
		this.fromStation = fromStation;
		this.toStation = toStation;
		this.departureDay = departureDay;
		this.arrivalDay = arrivalDay;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.distance = distance;
		this.duration = duration;
	}


	public Long getId() {
		return id;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	public String getPilotTripName() {
		return pilotTripName;
	}

	public void setPilotTripName(String pilotTripName) {
		this.pilotTripName = pilotTripName;
	}

	public String getPilotType() {
		return pilotType;
	}

	public void setPilotType(String pilotType) {
		this.pilotType = pilotType;
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

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public Long getDistance() {
		return distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Day getDepartureDay() {
		return departureDay;
	}

	public void setDepartureDay(Day departureDay) {
		this.departureDay = departureDay;
	}

	public Day getArrivalDay() {
		return arrivalDay;
	}

	public void setArrivalDay(Day arrivalDay) {
		this.arrivalDay = arrivalDay;
	}
	
	

}
