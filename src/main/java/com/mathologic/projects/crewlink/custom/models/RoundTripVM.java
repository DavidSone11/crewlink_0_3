package com.mathologic.projects.crewlink.custom.models;

import com.mathologic.projects.crewlink.models.Day;
import com.mathologic.projects.crewlink.models.Station;

public class RoundTripVM {
	private Long id;
	private String rtName;
	private String departureTime;
	private Day departureDay;
	private String station;
	private String arrivalTime;
	private Day arrivalDay;
	private String availableTime;
	private Day availableDay;
	private Long duration;
	private Long distance;
	private Long totalOutStationRestTime;
	private Long lastDrivingDutyDuration;
	private Integer crewType;
	private Boolean isCrewLink;
	private Boolean isIgnore;
	private Integer crewLinkOrderNo;
	private String crewTypeName;
	private String crewLinkName;
	private String os;
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoundTripVM other = (RoundTripVM) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public RoundTripVM() {
		super();
	}
	
	public RoundTripVM(Long id, String rtName, String departureTime,
			Day departureDay, String station, String arrivalTime,
			Day arrivalDay, String availableTime, Day availableDay,
			Long duration, Long distance, Long totalOutStationRestTime,
			Long lastDrivingDutyDuration, Integer crewType, Boolean isCrewLink,
			Boolean isIgnore, Integer crewLinkOrderNo, String crewTypeName,
			String crewLinkName, String os) {
		super();
		this.id = id;
		this.rtName = rtName;
		this.departureTime = departureTime;
		this.departureDay = departureDay;
		this.station = station;
		this.arrivalTime = arrivalTime;
		this.arrivalDay = arrivalDay;
		this.availableTime = availableTime;
		this.availableDay = availableDay;
		this.duration = duration;
		this.distance = distance;
		this.totalOutStationRestTime = totalOutStationRestTime;
		this.lastDrivingDutyDuration = lastDrivingDutyDuration;
		this.crewType = crewType;
		this.isCrewLink = isCrewLink;
		this.isIgnore = isIgnore;
		this.crewLinkOrderNo = crewLinkOrderNo;
		this.crewTypeName = crewTypeName;
		this.crewLinkName = crewLinkName;
		this.os = os;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRtName() {
		return rtName;
	}
	public void setRtName(String rtName) {
		this.rtName = rtName;
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
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
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
	public String getAvailableTime() {
		return availableTime;
	}
	public void setAvailableTime(String availableTime) {
		this.availableTime = availableTime;
	}
	public Day getAvailableDay() {
		return availableDay;
	}
	public void setAvailableDay(Day availableDay) {
		this.availableDay = availableDay;
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
	public Long getTotalOutStationRestTime() {
		return totalOutStationRestTime;
	}
	public void setTotalOutStationRestTime(Long totalOutStationRestTime) {
		this.totalOutStationRestTime = totalOutStationRestTime;
	}
	public Long getLastDrivingDutyDuration() {
		return lastDrivingDutyDuration;
	}
	public void setLastDrivingDutyDuration(Long lastDrivingDutyDuration) {
		this.lastDrivingDutyDuration = lastDrivingDutyDuration;
	}
	public Integer getCrewType() {
		return crewType;
	}
	public void setCrewType(Integer crewType) {
		this.crewType = crewType;
	}
	public Boolean getIsCrewLink() {
		return isCrewLink;
	}
	public void setIsCrewLink(Boolean isCrewLink) {
		this.isCrewLink = isCrewLink;
	}
	public Boolean getIsIgnore() {
		return isIgnore;
	}
	public void setIsIgnore(Boolean isIgnore) {
		this.isIgnore = isIgnore;
	}
	public Integer getCrewLinkOrderNo() {
		return crewLinkOrderNo;
	}
	public void setCrewLinkOrderNo(Integer crewLinkOrderNo) {
		this.crewLinkOrderNo = crewLinkOrderNo;
	}
	public String getCrewTypeName() {
		return crewTypeName;
	}
	public void setCrewTypeName(String crewTypeName) {
		this.crewTypeName = crewTypeName;
	}
	public String getCrewLinkName() {
		return crewLinkName;
	}
	public void setCrewLinkName(String crewLinkName) {
		this.crewLinkName = crewLinkName;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	
}
