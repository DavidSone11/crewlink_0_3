package com.mathologic.projects.crewlink.models;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by vivek on 3/10/15.
 */
@Entity
@Table(name = "train")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Train implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private Integer trainNo;
	
	// Monday, Tuesday, etc.
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private Day startDay;

	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	private Station fromStation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Station toStation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private TrainType trainType;
	
	@OneToMany(mappedBy="train", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
	private List<TrainStation> trainStations = new LinkedList<TrainStation>();
	
	@OneToMany(mappedBy="train", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
	private List<DrivingSection> drivingSections = new LinkedList<DrivingSection>();

	@ManyToMany(mappedBy="trains", fetch = FetchType.LAZY)
	private List<User> users = new LinkedList<User>();
	
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

	public Day getStartDay() {
		return startDay;
	}

	public void setStartDay(Day startDay) {
		this.startDay = startDay;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public TrainType getTrainType() {
		return trainType;
	}

	public void setTrainType(TrainType trainType) {
		this.trainType = trainType;
	}

	public List<TrainStation> getTrainStations() {
		return trainStations;
	}

	public void setTrainStations(List<TrainStation> trainStations) {
		this.trainStations = trainStations;
	}

	public List<DrivingSection> getDrivingSections() {
		return drivingSections;
	}

	public void setDrivingSections(List<DrivingSection> drivingSections) {
		this.drivingSections = drivingSections;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
}
