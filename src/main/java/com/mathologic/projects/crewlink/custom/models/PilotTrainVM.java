package com.mathologic.projects.crewlink.custom.models;

import java.io.Serializable;

import com.mathologic.projects.crewlink.models.Day;

public class PilotTrainVM implements Serializable{
	
	private Long id;
	private Integer trainNo;
	private String days;
	private String name;
	private Day startDay;
	private String originatingStation;
	private String destinationStation;
	private String trainType;
	private String fromStation;
	private Day departureDay;
	private String departureTime;
	private String toStation;
	private Day arrivalDay;
	private String arrivalTime;
	private Long distance;
	private Long duration;
	
	public PilotTrainVM(){};
	
	public PilotTrainVM(Long id, Integer trainNo, String days, String name, Day startDay, String originatingStation,
			String destinationStation, String trainType, String fromStation, Day departureDay, String departureTime,
			String toStation, Day arrivalDay, String arrivalTime, Long distance, Long duration) {
		super();
		this.id = id;
		this.trainNo = trainNo;
		this.days = days;
		this.name = name;
		this.startDay = startDay;
		this.originatingStation = originatingStation;
		this.destinationStation = destinationStation;
		this.trainType = trainType;
		this.fromStation = fromStation;
		this.departureDay = departureDay;
		this.departureTime = departureTime;
		this.toStation = toStation;
		this.arrivalDay = arrivalDay;
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
	public Integer getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(Integer trainNo) {
		this.trainNo = trainNo;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Day getStartDay() {
		return startDay;
	}
	public void setStartDay(Day startDay) {
		this.startDay = startDay;
	}
	public String getOriginatingStation() {
		return originatingStation;
	}
	public void setOriginatingStation(String originatingStation) {
		this.originatingStation = originatingStation;
	}
	public String getDestinationStation() {
		return destinationStation;
	}
	public void setDestinationStation(String destinationStation) {
		this.destinationStation = destinationStation;
	}
	public String getTrainType() {
		return trainType;
	}
	public void setTrainType(String trainType) {
		this.trainType = trainType;
	}
	public String getFromStation() {
		return fromStation;
	}
	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
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
	public String getToStation() {
		return toStation;
	}
	public void setToStation(String toStation) {
		this.toStation = toStation;
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
