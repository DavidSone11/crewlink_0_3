package com.mathologic.projects.crewlink.models;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mathologic.projects.crewlink.utils.converters.LocalTimeDeSerializer;
import com.mathologic.projects.crewlink.utils.converters.LocalTimeSerializer;

/**
 * Created by vivek on 3/10/15.
 */
@Entity
@Table(name = "crew_link")
public class CrewLink implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@ManyToOne
	private Station station;

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
	private Long totalHeadStationRestTime;
	
	@NotNull
	private Long totalOutStationRestTime;

	@NotNull
	private String linkName;
	
	@NotNull
	private Integer NoOfLp;
	
	@NotNull 
	private Long crewType;
	

	@OneToMany(mappedBy = "crewLink", fetch = FetchType.EAGER)
	private List<RoundTrip> roundTrips = new LinkedList<RoundTrip>();

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

	public Long getTotalHeadStationRestTime() {
		return totalHeadStationRestTime;
	}

	public void setTotalHeadStationRestTime(Long totalHeadStationRestTime) {
		this.totalHeadStationRestTime = totalHeadStationRestTime;
	}

	public Long getTotalOutStationRestTime() {
		return totalOutStationRestTime;
	}

	public void setTotalOutStationRestTime(Long totalOutStationRestTime) {
		this.totalOutStationRestTime = totalOutStationRestTime;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public Integer getNoOfLp() {
		return NoOfLp;
	}

	public void setNoOfLp(Integer noOfLp) {
		NoOfLp = noOfLp;
	}
	
	public Long getCrewType() {
		return crewType;
	}

	public void setCrewType(Long crewType) {
		this.crewType = crewType;
	}

	public List<RoundTrip> getRoundTrips() {
		return roundTrips;
	}

	public void setRoundTrips(List<RoundTrip> roundTrips) {
		this.roundTrips = roundTrips;
	}

	
}
