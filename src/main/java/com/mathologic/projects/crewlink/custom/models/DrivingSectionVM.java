package com.mathologic.projects.crewlink.custom.models;

import com.mathologic.projects.crewlink.models.Day;

public class DrivingSectionVM {
	private Long id;
	private Integer trainNo;
	private Day originDay;
	private Integer drivingSectionOrderNo;
	private String fromStation;
	private String toStation;
	private Day departureDay;
	private String departureTime;
	private Day arrivalDay;
	private String arrivalTime;
	private Long duration;
	private Long distance;
	private Boolean isDrivingDuty;
	private Long drivingDutyId;
	private Boolean isIgnore;
	public DrivingSectionVM(Long id, Integer trainNo, Day originDay,
			Integer drivingSectionOrderNo, String fromStation,
			String toStation, Day departureDay, String departureTime,
			Long duration, Long distance, Boolean isDrivingDuty, Long drivingDutyId,Boolean isIgnore) {
		super();
		this.id = id;
		this.trainNo = trainNo;
		this.originDay = originDay;
		this.drivingSectionOrderNo = drivingSectionOrderNo;
		this.fromStation = fromStation;
		this.toStation = toStation;
		this.departureDay = departureDay;
		this.departureTime = departureTime;
		this.duration = duration;
		this.distance = distance;
		this.isDrivingDuty = isDrivingDuty;
		this.drivingDutyId = drivingDutyId;
		this.isIgnore = isIgnore;
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
	public Day getOriginDay() {
		return originDay;
	}
	public void setOriginDay(Day originDay) {
		this.originDay = originDay;
	}
	public Integer getDrivingSectionOrderNo() {
		return drivingSectionOrderNo;
	}
	public void setDrivingSectionOrderNo(Integer drivingSectionOrderNo) {
		this.drivingSectionOrderNo = drivingSectionOrderNo;
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
	public Boolean getIsDrivingDuty() {
		return isDrivingDuty;
	}
	public void setIsDrivingDuty(Boolean isDrivingDuty) {
		this.isDrivingDuty = isDrivingDuty;
	}
	public Long getDrivingDutyId() {
		return drivingDutyId;
	}
	public void setDrivingDutyId(Long drivingDutyId) {
		this.drivingDutyId = drivingDutyId;
	}
	public Boolean getIsIgnore() {
		return isIgnore;
	}
	public void setIsIgnore(Boolean isIgnore) {
		this.isIgnore = isIgnore;
	}	
}
