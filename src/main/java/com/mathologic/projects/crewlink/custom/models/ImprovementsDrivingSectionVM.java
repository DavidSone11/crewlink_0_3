package com.mathologic.projects.crewlink.custom.models;


public class ImprovementsDrivingSectionVM{
	private Integer trainNo;
	private String ds1From;
	private String ds1To;
	private String ds1StartTime;
	private String ds1EndTime;
	private Long ds1Duration;
	private String ds1DDNames;
	private String ds1RTNames;
	private String ds1CLNames;
	private String ds2From;
	private String ds2To;
	private String ds2StartTime;
	private String ds2EndTime;
	private Long ds2Duration;
	private String ds2DDNames;
	private String ds2RTNames;
	private String ds2CLNames;
	private Integer counts;
	private Long newDuration;
	
	public ImprovementsDrivingSectionVM() {
		super();
	}
	public ImprovementsDrivingSectionVM(Integer trainNo, String ds1From,
			String ds1To, String ds1StartTime, String ds1EndTime,
			Long ds1Duration, String ds1ddNames, String ds1rtNames,
			String ds1clNames, String ds2From, String ds2To,
			String ds2StartTime, String ds2EndTime, Long ds2Duration,
			String ds2ddNames, String ds2rtNames, String ds2clNames,
			Integer counts, Long newDuration) {
		super();
		this.trainNo = trainNo;
		this.ds1From = ds1From;
		this.ds1To = ds1To;
		this.ds1StartTime = ds1StartTime;
		this.ds1EndTime = ds1EndTime;
		this.ds1Duration = ds1Duration;
		ds1DDNames = ds1ddNames;
		ds1RTNames = ds1rtNames;
		ds1CLNames = ds1clNames;
		this.ds2From = ds2From;
		this.ds2To = ds2To;
		this.ds2StartTime = ds2StartTime;
		this.ds2EndTime = ds2EndTime;
		this.ds2Duration = ds2Duration;
		ds2DDNames = ds2ddNames;
		ds2RTNames = ds2rtNames;
		ds2CLNames = ds2clNames;
		this.counts = counts;
		this.newDuration = newDuration;
	}
	public Integer getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(Integer trainNo) {
		this.trainNo = trainNo;
	}
	public String getDs1From() {
		return ds1From;
	}
	public void setDs1From(String ds1From) {
		this.ds1From = ds1From;
	}
	public String getDs1To() {
		return ds1To;
	}
	public void setDs1To(String ds1To) {
		this.ds1To = ds1To;
	}
	public String getDs1StartTime() {
		return ds1StartTime;
	}
	public void setDs1StartTime(String ds1StartTime) {
		this.ds1StartTime = ds1StartTime;
	}
	public String getDs1EndTime() {
		return ds1EndTime;
	}
	public void setDs1EndTime(String ds1EndTime) {
		this.ds1EndTime = ds1EndTime;
	}
	public Long getDs1Duration() {
		return ds1Duration;
	}
	public void setDs1Duration(Long ds1Duration) {
		this.ds1Duration = ds1Duration;
	}
	public String getDs1DDNames() {
		return ds1DDNames;
	}
	public void setDs1DDNames(String ds1ddNames) {
		ds1DDNames = ds1ddNames;
	}
	public String getDs1RTNames() {
		return ds1RTNames;
	}
	public void setDs1RTNames(String ds1rtNames) {
		ds1RTNames = ds1rtNames;
	}
	public String getDs1CLNames() {
		return ds1CLNames;
	}
	public void setDs1CLNames(String ds1clNames) {
		ds1CLNames = ds1clNames;
	}
	public String getDs2From() {
		return ds2From;
	}
	public void setDs2From(String ds2From) {
		this.ds2From = ds2From;
	}
	public String getDs2To() {
		return ds2To;
	}
	public void setDs2To(String ds2To) {
		this.ds2To = ds2To;
	}
	public String getDs2StartTime() {
		return ds2StartTime;
	}
	public void setDs2StartTime(String ds2StartTime) {
		this.ds2StartTime = ds2StartTime;
	}
	public String getDs2EndTime() {
		return ds2EndTime;
	}
	public void setDs2EndTime(String ds2EndTime) {
		this.ds2EndTime = ds2EndTime;
	}
	public Long getDs2Duration() {
		return ds2Duration;
	}
	public void setDs2Duration(Long ds2Duration) {
		this.ds2Duration = ds2Duration;
	}
	public String getDs2DDNames() {
		return ds2DDNames;
	}
	public void setDs2DDNames(String ds2ddNames) {
		ds2DDNames = ds2ddNames;
	}
	public String getDs2RTNames() {
		return ds2RTNames;
	}
	public void setDs2RTNames(String ds2rtNames) {
		ds2RTNames = ds2rtNames;
	}
	public String getDs2CLNames() {
		return ds2CLNames;
	}
	public void setDs2CLNames(String ds2clNames) {
		ds2CLNames = ds2clNames;
	}
	public Integer getCounts() {
		return counts;
	}
	public void setCounts(Integer counts) {
		this.counts = counts;
	}
	public Long getNewDuration() {
		return newDuration;
	}
	public void setNewDuration(Long newDuration) {
		this.newDuration = newDuration;
	}
	
}
