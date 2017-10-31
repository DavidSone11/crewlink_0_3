package com.mathologic.projects.crewlink.models;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mathologic.projects.crewlink.utils.converters.LocalTimeDeSerializer;
import com.mathologic.projects.crewlink.utils.converters.LocalTimeSerializer;

/**
 * Created by vivek on 3/10/15.
 */
@Entity
@Table(name = "pilot_trip")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PilotTrip implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @ManyToOne
    private PilotType pilotType;

    @NotNull
    @ManyToOne
    private Station fromStation;

    @NotNull
    @ManyToOne
    private Station toStation;
    
    @NotNull
    private Long duration;

    @NotNull
    private Long distance;
    
    @OneToMany(mappedBy="startPilotTrip",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<DrivingDutyElement> startDrivingDutyElement = new LinkedList<DrivingDutyElement>();
    
    @OneToMany(mappedBy="endPilotTrip",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<DrivingDutyElement> endDrivingDutyElement = new LinkedList<DrivingDutyElement>();
    
    @ManyToOne
    private Division division;
    
    @NotNull
	@Enumerated(EnumType.ORDINAL)
	private Day startDay;
    
    @NotNull
   	@Enumerated(EnumType.ORDINAL)
   	private Day endDay;
    
	@NotNull
	@JsonSerialize(using = LocalTimeSerializer.class)
	@JsonDeserialize(using = LocalTimeDeSerializer.class)
	private LocalTime startTime;
	
	
	@NotNull
	@JsonSerialize(using = LocalTimeSerializer.class)
	@JsonDeserialize(using = LocalTimeDeSerializer.class)
	private LocalTime endTime;
	
	@NotNull
	@ManyToOne
	private User user;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PilotType getPilotType() {
		return pilotType;
	}

	public void setPilotType(PilotType pilotType) {
		this.pilotType = pilotType;
	}

	public Station getFromStation() {
		return fromStation;
	}

	public void setFromStation(Station fromStation) {
		this.fromStation = fromStation;
	}

	public Station getToStation() {
		return toStation;
	}

	public void setToStation(Station toStation) {
		this.toStation = toStation;
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

	public List<DrivingDutyElement> getStartDrivingDutyElement() {
		return startDrivingDutyElement;
	}

	public void setStartDrivingDutyElement(
			List<DrivingDutyElement> startDrivingDutyElement) {
		this.startDrivingDutyElement = startDrivingDutyElement;
	}

	public List<DrivingDutyElement> getEndDrivingDutyElement() {
		return endDrivingDutyElement;
	}

	public void setEndDrivingDutyElement(
			List<DrivingDutyElement> endDrivingDutyElement) {
		this.endDrivingDutyElement = endDrivingDutyElement;
	}

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public Day getStartDay() {
		return startDay;
	}

	public void setStartDay(Day startDay) {
		this.startDay = startDay;
	}

	public Day getEndDay() {
		return endDay;
	}

	public void setEndDay(Day endDay) {
		this.endDay = endDay;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
