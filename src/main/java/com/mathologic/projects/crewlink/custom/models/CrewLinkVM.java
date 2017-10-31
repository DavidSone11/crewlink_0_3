package com.mathologic.projects.crewlink.custom.models;

import com.mathologic.projects.crewlink.models.Day;

public class CrewLinkVM {
	private Long id;
	private String linkName;
	private Integer locoPilots;
	private Long duration;
	private Long hqRest;
	private Long osRest;
	private String station;
	private String departureTime;
	private Day departureDay;
	private String arrivalTime;
	private Day arrivalDay;
	private Long distance;
	private String crewType;
	private Integer noOfRoundTrips;
	
	
	public CrewLinkVM(Long id, String linkName, Integer locoPilots,
			Long duration, Long hqRest, Long osRest, String station,
			String departureTime, Day departureDay, String arrivalTime,
			Day arrivalDay, Long distance, String crewType,
			Integer noOfRoundTrips) {
		super();
		this.id = id;
		this.linkName = linkName;
		this.locoPilots = locoPilots;
		this.duration = duration;
		this.hqRest = hqRest;
		this.osRest = osRest;
		this.station = station;
		this.departureTime = departureTime;
		this.departureDay = departureDay;
		this.arrivalTime = arrivalTime;
		this.arrivalDay = arrivalDay;
		this.distance = distance;
		this.crewType = crewType;
		this.noOfRoundTrips = noOfRoundTrips;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public Integer getLocoPilots() {
		return locoPilots;
	}
	public void setLocoPilots(Integer locoPilots) {
		this.locoPilots = locoPilots;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public Long getHqRest() {
		return hqRest;
	}
	public void setHqRest(Long hqRest) {
		this.hqRest = hqRest;
	}
	public Long getOsRest() {
		return osRest;
	}
	public void setOsRest(Long osRest) {
		this.osRest = osRest;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
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
	public Long getDistance() {
		return distance;
	}
	public void setDistance(Long distance) {
		this.distance = distance;
	}
	public String getCrewType() {
		return crewType;
	}
	public void setCrewType(String crewType) {
		this.crewType = crewType;
	}
	public Integer getNoOfRoundTrips() {
		return noOfRoundTrips;
	}
	public void setNoOfRoundTrips(Integer noOfRoundTrips) {
		this.noOfRoundTrips = noOfRoundTrips;
	}
	
}
