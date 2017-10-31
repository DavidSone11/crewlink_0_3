package com.mathologic.projects.crewlink.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mathologic.projects.crewlink.utils.converters.LocalTimeDeSerializer;
import com.mathologic.projects.crewlink.utils.converters.LocalTimeSerializer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vivek on 3/10/15.
 */
@Entity
@Table(name = "round_trip")
public class RoundTrip implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@ManyToOne
	private Station station;

	@ManyToOne
	private CrewType crewType;

	@OneToMany(mappedBy = "roundTrip", fetch = FetchType.EAGER)
	private List<DrivingDuty> drivingDuties = new LinkedList<DrivingDuty>();

	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private Day startDay;

	@NotNull
	@JsonSerialize(using = LocalTimeSerializer.class)
	@JsonDeserialize(using = LocalTimeDeSerializer.class)
	private LocalTime startTime;

	@NotNull
    @Enumerated(EnumType.ORDINAL)
	private Day endDay;
    
    @NotNull
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeSerializer.class)
    private LocalTime endTime;
    
	@NotNull
	private Long duration;

	@NotNull
	private Long distance;

	@NotNull
	@ManyToOne
	private UserPlan userPlan;

	@NotNull
	private Long totalOutStationRestTime;

	@NotNull
	private Long lastDrivingDutyDuration;
	
	@NotNull
    @Enumerated(EnumType.ORDINAL)
	private Day nextAvailableDay;
    
    @NotNull
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeSerializer.class)
    private LocalTime nextAvailableTime;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_ROUNDTRIP_CREWLINK"))
	private CrewLink crewLink;

	private Integer crewLinkOrderNo;
    private String rtName;
	private boolean isIgnore;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public List<DrivingDuty> getDrivingDuties() {
		return drivingDuties;
	}

	public void setDrivingDuties(List<DrivingDuty> drivingDuties) {
		this.drivingDuties = drivingDuties;
	}

	public Day getStartDay() {
		return startDay;
	}

	public void setStartDay(Day startDay) {
		this.startDay = startDay;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public Day getEndDay() {
		return endDay;
	}

	public void setEndDay(Day endDay) {
		this.endDay = endDay;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
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

	public UserPlan getUserPlan() {
		return userPlan;
	}

	public void setUserPlan(UserPlan userPlan) {
		this.userPlan = userPlan;
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

	public Day getNextAvailableDay() {
		return nextAvailableDay;
	}

	public void setNextAvailableDay(Day nextAvailableDay) {
		this.nextAvailableDay = nextAvailableDay;
	}

	public LocalTime getNextAvailableTime() {
		return nextAvailableTime;
	}

	public void setNextAvailableTime(LocalTime nextAvailableTime) {
		this.nextAvailableTime = nextAvailableTime;
	}

	public CrewLink getCrewLink() {
		return crewLink;
	}

	public void setCrewLink(CrewLink crewLink) {
		this.crewLink = crewLink;
	}

	public CrewType getCrewType() {
		return crewType;
	}

	public void setCrewType(CrewType crewType) {
		this.crewType = crewType;
	}

	public Integer getCrewLinkOrderNo() {
		return crewLinkOrderNo;
	}

	public void setCrewLinkOrderNo(Integer crewLinkOrderNo) {
		this.crewLinkOrderNo = crewLinkOrderNo;
	}

	public String getRtName() {
		return rtName;
	}

	public void setRtName(String rtName) {
		this.rtName = rtName;
	}

	public boolean isIgnore() {
		return isIgnore;
	}

	public void setIgnore(boolean isIgnore) {
		this.isIgnore = isIgnore;
	}
}
