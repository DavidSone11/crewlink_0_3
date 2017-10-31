package com.mathologic.projects.crewlink.custom.models;

import java.io.Serializable;

import com.mathologic.projects.crewlink.models.Day;

public class TrainStationVM implements Serializable{
	private Long id;
	private Integer stopNumber;
	private String stationCode;
	private String stationName;
	private String arrival;
	private String departure;
	private Day day;
	private Integer dayOfJourney;
	private Long distance;
	private Long duration;
	
	
	
	public TrainStationVM(Long id, Integer stopNumber, String stationCode, String stationName, String arrival,
			String departure, Day day, Integer dayOfJourney, Long distance, Long duration) {
		super();
		this.id = id;
		this.stopNumber = stopNumber;
		this.stationCode = stationCode;
		this.stationName = stationName;
		this.arrival = arrival;
		this.departure = departure;
		this.day = day;
		this.dayOfJourney = dayOfJourney;
		this.distance = distance;
		this.duration = duration;
	}
	public Integer getStopNumber() {
		return stopNumber;
	}
	public void setStopNumber(Integer stopNumber) {
		this.stopNumber = stopNumber;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getArrival() {
		return arrival;
	}
	public void setArrival(String arrival) {
		this.arrival = arrival;
	}
	public String getDeparture() {
		return departure;
	}
	public void setDeparture(String departure) {
		this.departure = departure;
	}
	public Day getDay() {
		return day;
	}
	public void setDay(Day day) {
		this.day = day;
	}
	public Integer getDayOfJourney() {
		return dayOfJourney;
	}
	public void setDayOfJourney(Integer dayOfJourney) {
		this.dayOfJourney = dayOfJourney;
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
	
}
