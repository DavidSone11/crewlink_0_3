package com.mathologic.projects.crewlink.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mathologic.projects.crewlink.utils.converters.LocalDateTimeDeSerializer;
import com.mathologic.projects.crewlink.utils.converters.LocalDateTimeSerializer;

@Entity
@Table(name = "user_plan")
public class UserPlan implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique=true,columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeSerializer.class)
	private LocalDateTime timeStamp = LocalDateTime.now();
	
	@NotNull
	@Column(unique=true)
	private String name;
	
	@ManyToOne
	private User user;
	
	@OneToMany(mappedBy="userPlan",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
	private List<DrivingSection> drivingSections = new LinkedList<DrivingSection>();

	@OneToMany(mappedBy="userPlan",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
	private List<DrivingDutyElement> drivingDutyElements = new LinkedList<DrivingDutyElement>();
	
	@OneToMany(mappedBy="userPlan",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
	private List<DrivingDuty> drivingDuties = new LinkedList<DrivingDuty>();
	
	@OneToMany(mappedBy="userPlan",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
	private List<RoundTrip> roundTrips = new LinkedList<RoundTrip>();
	
	@OneToMany(mappedBy="userPlan",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
	private List<CrewLink> crewLinks = new LinkedList<CrewLink>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<DrivingSection> getDrivingSections() {
		return drivingSections;
	}

	public List<DrivingDutyElement> getDrivingDutyElements() {
		return drivingDutyElements;
	}

	public void setDrivingDutyElements(List<DrivingDutyElement> drivingDutyElements) {
		this.drivingDutyElements = drivingDutyElements;
	}

	public void setDrivingSections(List<DrivingSection> drivingSections) {
		this.drivingSections = drivingSections;
	}

	public List<DrivingDuty> getDrivingDuties() {
		return drivingDuties;
	}

	public void setDrivingDuties(List<DrivingDuty> drivingDuties) {
		this.drivingDuties = drivingDuties;
	}

	public List<RoundTrip> getRoundTrips() {
		return roundTrips;
	}

	public void setRoundTrips(List<RoundTrip> roundTrips) {
		this.roundTrips = roundTrips;
	}

	public List<CrewLink> getCrewLinks() {
		return crewLinks;
	}

	public void setCrewLinks(List<CrewLink> crewLinks) {
		this.crewLinks = crewLinks;
	}
	
}
