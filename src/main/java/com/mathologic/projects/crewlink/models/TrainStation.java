package com.mathologic.projects.crewlink.models;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "train_station")
public class TrainStation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne
    private Train train;

    @NotNull
    private Integer stopNumber;

    @NotNull
    @ManyToOne
    private Station station;
    
    @NotNull
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeSerializer.class)
    private LocalTime arrival;

    @NotNull
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeSerializer.class)
    private LocalTime departure;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
	private Day day;
    
    @NotNull
    private Integer dayOfJourney;

    @NotNull
    private Long distance;

    @NotNull
    private Long journeyDuration;
    
    @ManyToMany(mappedBy="trainStations",cascade=CascadeType.REMOVE,fetch=FetchType.LAZY)
    private List<DrivingSection> drivingSections = new LinkedList<DrivingSection>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}

	public Integer getStopNumber() {
		return stopNumber;
	}

	public void setStopNumber(Integer stopNumber) {
		this.stopNumber = stopNumber;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public LocalTime getArrival() {
		return arrival;
	}

	public void setArrival(LocalTime arrival) {
		this.arrival = arrival;
	}

	public LocalTime getDeparture() {
		return departure;
	}

	public void setDeparture(LocalTime departure) {
		this.departure = departure;
	}

	public Integer getDayOfJourney() {
		return dayOfJourney;
	}

	public void setDayOfJourney(Integer dayOfJourney) {
		this.dayOfJourney = dayOfJourney;
	}

	public Long getDistance() {
		return distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}

	public Long getJourneyDuration() {
		return journeyDuration;
	}

	public void setJourneyDuration(Long journeyDuration) {
		this.journeyDuration = journeyDuration;
	}

	public Day getDay() {
		return day;
	}

	public void setDay(Day day) {
		this.day = day;
	}

	public List<DrivingSection> getDrivingSections() {
		return drivingSections;
	}

	public void setDrivingSections(List<DrivingSection> drivingSections) {
		this.drivingSections = drivingSections;
	}
}
