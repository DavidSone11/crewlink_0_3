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
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name = "driving_section")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DrivingSection implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Train train;
    
    private Integer drivingSectionOrderNo;
    
    @NotNull
    @ManyToOne
    private Station fromStation;
    
    @NotNull
    @ManyToOne
    private Station toStation;
    
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
    
    @ManyToMany(fetch=FetchType.LAZY)
    private List<TrainStation> trainStations = new LinkedList<TrainStation>();
   // @JoinColumn(name = "driving_duty_element", nullable = true, foreignKey = @ForeignKey(name="FK_DRIVING_SECTION__DRIVING_DUTY_ELEMENT", foreignKeyDefinition="foreign key (driving_duty_element) references driving_duty_element ON DELETE NO ACTION"))
    // @JoinColumn(name = "driving_duty_element", nullable = true, foreignKey = @ForeignKey(													foreignKeyDefinition="FOREIGN KEY ( driving_duty_element ) REFERENCES driving_duty_element ( id ) ON DELETE SET NULL"))
   
    @OneToOne(cascade=CascadeType.REMOVE)
    @JoinColumn(foreignKey = @ForeignKey(name="FK_DRIVINGSECTION_DRIVINGDUTYELEMENT"))
    private DrivingDutyElement drivingDutyElement;
    
    @NotNull
    @ManyToOne
    private UserPlan userPlan;
    
    private boolean isIgnore;

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

	public Integer getDrivingSectionOrderNo() {
		return drivingSectionOrderNo;
	}

	public void setDrivingSectionOrderNo(Integer drivingSectionOrderNo) {
		this.drivingSectionOrderNo = drivingSectionOrderNo;
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
	
	public List<TrainStation> getTrainStations() {
		return trainStations;
	}

	public void setTrainStations(List<TrainStation> trainStations) {
		this.trainStations = trainStations;
	}

	public DrivingDutyElement getDrivingDutyElement() {
		return drivingDutyElement;
	}

	public void setDrivingDutyElement(DrivingDutyElement drivingDutyElement) {
		this.drivingDutyElement = drivingDutyElement;
	}

	public UserPlan getUserPlan() {
		return userPlan;
	}

	public void setUserPlan(UserPlan userPlan) {
		this.userPlan = userPlan;
	}

	public boolean isIgnore() {
		return isIgnore;
	}

	public void setIgnore(boolean isIgnore) {
		this.isIgnore = isIgnore;
	}
}
