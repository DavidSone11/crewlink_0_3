package com.mathologic.projects.crewlink.custom.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mathologic.projects.crewlink.models.Day;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TrainByNumberVM  implements Serializable {
	private Long id;
	private String name;
	private Day startDay;
	private Integer trainNo;
	private String fromStationCode;
	private String toStationCode;
	private String trainType;
	private Integer noOfDaysTrainRuns;
	private String days;
	private Boolean hasDrivingSection;
	private Boolean isUserSelected;
	
	
	
	public TrainByNumberVM(Long id, String name, Day startDay, Integer trainNo,
			String fromStationCode, String toStationCode, String trainType,
			Integer noOfDaysTrainRuns, String days, Boolean hasDrivingSection, Boolean isUserSelected) {
		super();
		this.id = id;
		this.name = name;
		this.startDay = startDay;
		this.trainNo = trainNo;
		this.fromStationCode = fromStationCode;
		this.toStationCode = toStationCode;
		this.trainType = trainType;
		this.noOfDaysTrainRuns = noOfDaysTrainRuns;
		this.days = days;
		this.hasDrivingSection = hasDrivingSection;
		this.isUserSelected = isUserSelected;
	}
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
	public Day getStartDay() {
		return startDay;
	}
	public void setStartDay(Day startDay) {
		this.startDay = startDay;
	}
	public Integer getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(Integer trainNo) {
		this.trainNo = trainNo;
	}
	public String getFromStationCode() {
		return fromStationCode;
	}
	public void setFromStationCode(String fromStationCode) {
		this.fromStationCode = fromStationCode;
	}
	public String getToStationCode() {
		return toStationCode;
	}
	public void setToStationCode(String toStationCode) {
		this.toStationCode = toStationCode;
	}
	public String getTrainType() {
		return trainType;
	}
	public void setTrainType(String trainType) {
		this.trainType = trainType;
	}
	public Integer getNoOfDaysTrainRuns() {
		return noOfDaysTrainRuns;
	}
	public void setNoOfDaysTrainRuns(Integer noOfDaysTrainRuns) {
		this.noOfDaysTrainRuns = noOfDaysTrainRuns;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public Boolean getHasDrivingSection() {
		return hasDrivingSection;
	}
	public void setHasDrivingSection(Boolean hasDrivingSection) {
		this.hasDrivingSection = hasDrivingSection;
	}
	public Boolean getIsUserSelected() {
		return isUserSelected;
	}
	public void setIsUserSelected(Boolean isUserSelected) {
		this.isUserSelected = isUserSelected;
	}
	
	
}
