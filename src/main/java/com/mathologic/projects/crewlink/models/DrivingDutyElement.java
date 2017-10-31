package com.mathologic.projects.crewlink.models;

import java.io.Serializable;
import java.time.LocalTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mathologic.projects.crewlink.utils.converters.LocalTimeDeSerializer;
import com.mathologic.projects.crewlink.utils.converters.LocalTimeSerializer;

@Entity
@Table(name = "driving_duty_element")
public class DrivingDutyElement implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	/*
	 * @NotNull
	 * 
	 * @OneToMany(mappedBy="drivingDutyElement",fetch = FetchType.LAZY,cascade =
	 * CascadeType.REMOVE) private List<DrivingSection> drivingSections = new
	 * LinkedList<DrivingSection>();
	 */

	@OneToOne(mappedBy = "drivingDutyElement", fetch = FetchType.LAZY)
	private DrivingSection drivingSection;
	// @PreRemove
	// private void removeDrivingSection() {
	// drivingSection.setDrivingDutyElement(null);
	// }
	
	@NotNull
	private String ddeName;

	@ManyToOne
	private PilotTrip startPilotTrip;

	@ManyToOne
	private PilotTrip endPilotTrip;

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

	@NotNull
	@ManyToOne
	private UserPlan userPlan;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JoinColumn(foreignKey = @ForeignKey(name="FK_DRIVINGDUTYELEMENT_DRIVINGDUTY"))
	private DrivingDuty drivingDuty;

	private Integer drivingDutyOrderNo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DrivingSection getDrivingSection() {
		return drivingSection;
	}

	public void setDrivingSection(DrivingSection drivingSection) {
		this.drivingSection = drivingSection;
	}

	public String getDdeName() {
		return ddeName;
	}

	public void setDdeName(String ddeName) {
		this.ddeName = ddeName;
	}

	public PilotTrip getStartPilotTrip() {
		return startPilotTrip;
	}

	public void setStartPilotTrip(PilotTrip startPilotTrip) {
		this.startPilotTrip = startPilotTrip;
	}

	public PilotTrip getEndPilotTrip() {
		return endPilotTrip;
	}

	public void setEndPilotTrip(PilotTrip endPilotTrip) {
		this.endPilotTrip = endPilotTrip;
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

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
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

	public DrivingDuty getDrivingDuty() {
		return drivingDuty;
	}

	public void setDrivingDuty(DrivingDuty drivingDuty) {
		this.drivingDuty = drivingDuty;
	}

	public Integer getDrivingDutyOrderNo() {
		return drivingDutyOrderNo;
	}

	public void setDrivingDutyOrderNo(Integer drivingDutyOrderNo) {
		this.drivingDutyOrderNo = drivingDutyOrderNo;
	}

	public UserPlan getUserPlan() {
		return userPlan;
	}

	public void setUserPlan(UserPlan userPlan) {
		this.userPlan = userPlan;
	}
}
