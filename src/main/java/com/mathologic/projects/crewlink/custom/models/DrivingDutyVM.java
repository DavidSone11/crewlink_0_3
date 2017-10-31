package com.mathologic.projects.crewlink.custom.models;

import java.io.Serializable;

import com.mathologic.projects.crewlink.models.Day;

public class DrivingDutyVM  implements Serializable{
	private Long id;
	private String ddName;
	private String fromStation;
	private String toStation;
	private Day departureDay;
	private String departureTime;
	private Day arrivalDay;
	private String arrivalTime;
	private Long duration;
	private Long distance;
	private Integer signOnDuration;
	private Integer signOffDuration;
	private Long roundTrip;
	private Boolean isRoundTrip;
	private Boolean isIgnore;
	private Integer roundTripOrderNo;
	private String roundTripName;
	private String roundTripBaseStation; 
	private String availableTime;
	
	public DrivingDutyVM(){};
	
	public DrivingDutyVM(Long id, String ddName, String fromStation,
			String toStation, Day departureDay, String departureTime,
			Day arrivalDay, String arrivalTime, Long duration, Long distance,
			Integer signOnDuration, Integer signOffDuration, Long roundTrip,
			Boolean isRoundTrip, Boolean isIgnore, Integer roundTripOrderNo) {
		super();
		this.id = id;
		this.ddName = ddName;
		this.fromStation = fromStation;
		this.toStation = toStation;
		this.departureDay = departureDay;
		this.departureTime = departureTime;
		this.arrivalDay = arrivalDay;
		this.arrivalTime = arrivalTime;
		this.duration = duration;
		this.distance = distance;
		this.signOnDuration = signOnDuration;
		this.signOffDuration = signOffDuration;
		this.roundTrip = roundTrip;
		this.isRoundTrip = isRoundTrip;
		this.isIgnore = isIgnore;
		this.roundTripOrderNo = roundTripOrderNo;
	}

	public DrivingDutyVM(Long id, String ddName, String fromStation, String toStation, Day departureDay,
			String departureTime, Day arrivalDay, String arrivalTime, Long duration, Long distance,
			Integer signOnDuration, Integer signOffDuration, Long roundTrip, Boolean isRoundTrip, Boolean isIgnore,
			Integer roundTripOrderNo, String roundTripName, String roundTripBaseStation) {
		super();
		this.id = id;
		this.ddName = ddName;
		this.fromStation = fromStation;
		this.toStation = toStation;
		this.departureDay = departureDay;
		this.departureTime = departureTime;
		this.arrivalDay = arrivalDay;
		this.arrivalTime = arrivalTime;
		this.duration = duration;
		this.distance = distance;
		this.signOnDuration = signOnDuration;
		this.signOffDuration = signOffDuration;
		this.roundTrip = roundTrip;
		this.isRoundTrip = isRoundTrip;
		this.isIgnore = isIgnore;
		this.roundTripOrderNo = roundTripOrderNo;
		this.roundTripName = roundTripName;
		this.roundTripBaseStation = roundTripBaseStation;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDrivingdutyName() {
		return ddName;
	}
	public void setDrivingdutyName(String drivingdutyName) {
		this.ddName = drivingdutyName;
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

	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
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
	public Boolean getIsRoundTrip() {
		return isRoundTrip;
	}
	public void setIsRoundTrip(Boolean isRoundTrip) {
		this.isRoundTrip = isRoundTrip;
	}

	public Integer getSignOnDuration() {
		return signOnDuration;
	}

	public void setSignOnDuration(Integer signOnDuration) {
		this.signOnDuration = signOnDuration;
	}

	public Integer getSignOffDuration() {
		return signOffDuration;
	}

	public void setSignOffDuration(Integer signOffDuration) {
		this.signOffDuration = signOffDuration;
	}

	public Long getRoundTrip() {
		return roundTrip;
	}

	public void setRoundTrip(Long roundTrip) {
		this.roundTrip = roundTrip;
	}
	public Integer getRoundTripOrderNo() {
		return roundTripOrderNo;
	}
	public void setRoundTripOrderNo(Integer roundTripOrderNo) {
		this.roundTripOrderNo = roundTripOrderNo;
	}
	public String getDdName() {
		return ddName;
	}
	public void setDdName(String ddName) {
		this.ddName = ddName;
	}
	public Boolean getIsIgnore() {
		return isIgnore;
	}
	public void setIsIgnore(Boolean isIgnore) {
		this.isIgnore = isIgnore;
	}
	public String getRoundTripName() {
		return roundTripName;
	}
	public void setRoundTripName(String roundTripName) {
		this.roundTripName = roundTripName;
	}

	public String getRoundTripBaseStation() {
		return roundTripBaseStation;
	}

	public void setRoundTripBaseStation(String roundTripBaseStation) {
		this.roundTripBaseStation = roundTripBaseStation;
	}

	public String getAvailableTime() {
		return availableTime;
	}

	public void setAvailableTime(String availableTime) {
		this.availableTime = availableTime;
	}
	
	
}
