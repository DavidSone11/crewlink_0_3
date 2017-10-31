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
@Table(name = "driving_duty")
public class DrivingDuty implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@OneToMany(mappedBy = "drivingDuty", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<DrivingDutyElement> drivingDutyElements = new LinkedList<DrivingDutyElement>();

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
	private Long signOnDuration;
    
    @NotNull
	private Long signOffDuration;
    
	@NotNull
	private Long duration;

	@NotNull
	private Long distance;

	@NotNull
	@ManyToOne
	private UserPlan userPlan;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JoinColumn(foreignKey = @ForeignKey(name="FK_DRIVINGDUTY_ROUNDTRIP"))
	private RoundTrip roundTrip;

	private Integer roundTripOrderNo;
	
	
	private String ddName;
	
	private boolean isIgnore;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<DrivingDutyElement> getDrivingDutyElements() {
		return drivingDutyElements;
	}

	public void setDrivingDutyElements(
			List<DrivingDutyElement> drivingDutyElements) {
		this.drivingDutyElements = drivingDutyElements;
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

	public Long getSignOnDuration() {
		return signOnDuration;
	}

	public void setSignOnDuration(Long signOnDuration) {
		this.signOnDuration = signOnDuration;
	}

	public Long getSignOffDuration() {
		return signOffDuration;
	}

	public void setSignOffDuration(Long signOffDuration) {
		this.signOffDuration = signOffDuration;
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

	public RoundTrip getRoundTrip() {
		return roundTrip;
	}

	public void setRoundTrip(RoundTrip roundTrip) {
		this.roundTrip = roundTrip;
	}

	public Integer getRoundTripOrderNo() {
		return roundTripOrderNo;
	}

	public void setRoundTripOrderNo(Integer roundTripOrderNo) {
		this.roundTripOrderNo = roundTripOrderNo;
	}

	public String getDdName() {
		return ddName;
	}

	public void setDdName(String ddName) {
		this.ddName = ddName;
	}

	public boolean isIgnore() {
		return isIgnore;
	}

	public void setIgnore(boolean isIgnore) {
		this.isIgnore = isIgnore;
	}
}
