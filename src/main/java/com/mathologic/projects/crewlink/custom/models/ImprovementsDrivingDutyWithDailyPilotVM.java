package com.mathologic.projects.crewlink.custom.models;


public class ImprovementsDrivingDutyWithDailyPilotVM {
	private String ddName;
	private String fromStation;
	private String toStation;
	private String startTime;
	private String endTime;
	private Long duration;
	private Integer counts;
	private String rtNames;
	private String clNames;
	public ImprovementsDrivingDutyWithDailyPilotVM(String ddName,
			String fromStation, String toStation, String startTime,
			String endTime, Long duration, Integer counts, String rtNames,
			String clNames) {
		super();
		this.ddName = ddName;
		this.fromStation = fromStation;
		this.toStation = toStation;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
		this.counts = counts;
		this.rtNames = rtNames;
		this.clNames = clNames;
	}
	public ImprovementsDrivingDutyWithDailyPilotVM() {
		super();
	}
	public String getDdName() {
		return ddName;
	}
	public void setDdName(String ddName) {
		this.ddName = ddName;
	}
	public String getFromStation() {
		return fromStation;
	}
	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}
	public String getToStation() {
		return toStation;
	}
	public void setToStation(String toStation) {
		this.toStation = toStation;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public Integer getCounts() {
		return counts;
	}
	public void setCounts(Integer counts) {
		this.counts = counts;
	}
	public String getRtNames() {
		return rtNames;
	}
	public void setRtNames(String rtNames) {
		this.rtNames = rtNames;
	}
	public String getClNames() {
		return clNames;
	}
	public void setClNames(String clNames) {
		this.clNames = clNames;
	}
		
}
