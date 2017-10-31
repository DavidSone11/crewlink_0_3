package com.mathologic.projects.crewlink.models;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by vivek on 3/10/15.
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "station")
public class Station implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@Column(unique = true)
	private String code;

	private String name;

	@Column(columnDefinition="BIGINT DEFAULT 30")
	private Long headStationSignOnDuration = 30L;

	@Column(columnDefinition="BIGINT DEFAULT 30")
	private Long headStationSignOffDuration= 30L;
	
	@Column(columnDefinition="INT DEFAULT 1")
	private Integer numberOfBeds= 1;

	@Column(columnDefinition="BIGINT DEFAULT 30")
	private Long outStationSignOnDuration= 30L;

	@Column(columnDefinition="BIGINT DEFAULT 30")
	private Long outStationSignOffDuration= 30L;
	
	
	@ManyToMany(fetch = FetchType.LAZY)
	private List<CrewType> crewTypes = new LinkedList<CrewType>();
	
	@ManyToMany(mappedBy="Stations", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<User> users = new LinkedList<User>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Division> divisions = new LinkedList<Division>();

	@OneToMany(mappedBy = "station", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
	private List<TrainStation> trainStations = new LinkedList<TrainStation>();

	@OneToMany(mappedBy = "fromStation", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
	private List<Train> fromTrains = new LinkedList<Train>();

	@OneToMany(mappedBy = "toStation", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
	private List<Train> toTrains = new LinkedList<Train>();

	@OneToMany(mappedBy = "fromStation", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
	private List<DrivingSection> fromDrivingSections = new LinkedList<DrivingSection>();

	@OneToMany(mappedBy = "toStation", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
	private List<DrivingSection> toDrivingSections = new LinkedList<DrivingSection>();
	
	@OneToMany(mappedBy = "fromStation", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
	private List<DrivingDutyElement> fromDrivingDutyElements = new LinkedList<DrivingDutyElement>();
	
	@OneToMany(mappedBy = "toStation", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
	private List<DrivingDutyElement> toDrivingDutyElements = new LinkedList<DrivingDutyElement>();

	@OneToMany(mappedBy = "fromStation", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
	private List<DrivingDuty> fromDrivingDuties = new LinkedList<DrivingDuty>();

	@OneToMany(mappedBy = "toStation", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
	private List<DrivingDuty> toDrivingDuties = new LinkedList<DrivingDuty>();

	@OneToMany(mappedBy = "station", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
	private List<RoundTrip> roundTrips = new LinkedList<RoundTrip>();
	
	@OneToMany(mappedBy = "station", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
	private List<CrewLink> crewLinks = new LinkedList<CrewLink>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getHeadStationSignOnDuration() {
		return headStationSignOnDuration;
	}

	public void setHeadStationSignOnDuration(Long headStationSignOnDuration) {
		this.headStationSignOnDuration = headStationSignOnDuration;
	}

	public Long getHeadStationSignOffDuration() {
		return headStationSignOffDuration;
	}

	public void setHeadStationSignOffDuration(Long headStationSignOffDuration) {
		this.headStationSignOffDuration = headStationSignOffDuration;
	}

	public Integer getNumberOfBeds() {
		return numberOfBeds;
	}

	public void setNumberOfBeds(Integer numberOfBeds) {
		this.numberOfBeds = numberOfBeds;
	}

	public Long getOutStationSignOnDuration() {
		return outStationSignOnDuration;
	}

	public void setOutStationSignOnDuration(Long outStationSignOnDuration) {
		this.outStationSignOnDuration = outStationSignOnDuration;
	}

	public Long getOutStationSignOffDuration() {
		return outStationSignOffDuration;
	}

	public void setOutStationSignOffDuration(Long outStationSignOffDuration) {
		this.outStationSignOffDuration = outStationSignOffDuration;
	}

	public List<CrewType> getCrewTypes() {
		return crewTypes;
	}

	public void setCrewTypes(List<CrewType> crewTypes) {
		this.crewTypes = crewTypes;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Division> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<Division> divisions) {
		this.divisions = divisions;
	}

	public List<TrainStation> getTrainStations() {
		return trainStations;
	}

	public void setTrainStations(List<TrainStation> trainStations) {
		this.trainStations = trainStations;
	}

	public List<Train> getFromTrains() {
		return fromTrains;
	}

	public void setFromTrains(List<Train> fromTrains) {
		this.fromTrains = fromTrains;
	}

	public List<Train> getToTrains() {
		return toTrains;
	}

	public void setToTrains(List<Train> toTrains) {
		this.toTrains = toTrains;
	}

	public List<DrivingSection> getFromDrivingSections() {
		return fromDrivingSections;
	}

	public void setFromDrivingSections(List<DrivingSection> fromDrivingSections) {
		this.fromDrivingSections = fromDrivingSections;
	}

	public List<DrivingSection> getToDrivingSections() {
		return toDrivingSections;
	}

	public void setToDrivingSections(List<DrivingSection> toDrivingSections) {
		this.toDrivingSections = toDrivingSections;
	}

	public List<DrivingDutyElement> getFromDrivingDutyElements() {
		return fromDrivingDutyElements;
	}

	public void setFromDrivingDutyElements(
			List<DrivingDutyElement> fromDrivingDutyElements) {
		this.fromDrivingDutyElements = fromDrivingDutyElements;
	}

	public List<DrivingDutyElement> getToDrivingDutyElements() {
		return toDrivingDutyElements;
	}

	public void setToDrivingDutyElements(
			List<DrivingDutyElement> toDrivingDutyElements) {
		this.toDrivingDutyElements = toDrivingDutyElements;
	}

	public List<DrivingDuty> getFromDrivingDuties() {
		return fromDrivingDuties;
	}

	public void setFromDrivingDuties(List<DrivingDuty> fromDrivingDuties) {
		this.fromDrivingDuties = fromDrivingDuties;
	}

	public List<DrivingDuty> getToDrivingDuties() {
		return toDrivingDuties;
	}

	public void setToDrivingDuties(List<DrivingDuty> toDrivingDuties) {
		this.toDrivingDuties = toDrivingDuties;
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
