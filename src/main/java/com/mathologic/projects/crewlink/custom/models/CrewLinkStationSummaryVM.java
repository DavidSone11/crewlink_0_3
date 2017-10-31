package com.mathologic.projects.crewlink.custom.models;

public class CrewLinkStationSummaryVM {
	private String station;
	private Integer noOfLP;
	private Integer noOfRT;
	private Long totalDistance;
	private Long pilotDistance;
	private Long trainDistance;
	private Long dutyTime;
	private Long dutyTimePer14Days;
	private Long OSR;
	private Long OSRPer14Days;
	private Long HQR;
	private Long HQRPer14Days;
	private Long avgHQR;
	public CrewLinkStationSummaryVM(String station, Integer noOfLP,
			Integer noOfRT, Long totalDistance, Long pilotDistance,
			Long trainDistance, Long dutyTime, Long dutyTimePer14Days,
			Long oSR, Long oSRPer14Days, Long hQR, Long hQRPer14Days,
			Long avgHQR) {
		super();
		this.station = station;
		this.noOfLP = noOfLP;
		this.noOfRT = noOfRT;
		this.totalDistance = totalDistance;
		this.pilotDistance = pilotDistance;
		this.trainDistance = trainDistance;
		this.dutyTime = dutyTime;
		this.dutyTimePer14Days = dutyTimePer14Days;
		OSR = oSR;
		OSRPer14Days = oSRPer14Days;
		HQR = hQR;
		HQRPer14Days = hQRPer14Days;
		this.avgHQR = avgHQR;
	}
	public CrewLinkStationSummaryVM() {
		super();
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public Integer getNoOfLP() {
		return noOfLP;
	}
	public void setNoOfLP(Integer noOfLP) {
		this.noOfLP = noOfLP;
	}
	public Integer getNoOfRT() {
		return noOfRT;
	}
	public void setNoOfRT(Integer noOfRT) {
		this.noOfRT = noOfRT;
	}
	public Long getTotalDistance() {
		return totalDistance;
	}
	public void setTotalDistance(Long totalDistance) {
		this.totalDistance = totalDistance;
	}
	public Long getPilotDistance() {
		return pilotDistance;
	}
	public void setPilotDistance(Long pilotDistance) {
		this.pilotDistance = pilotDistance;
	}
	public Long getTrainDistance() {
		return trainDistance;
	}
	public void setTrainDistance(Long trainDistance) {
		this.trainDistance = trainDistance;
	}
	public Long getDutyTime() {
		return dutyTime;
	}
	public void setDutyTime(Long dutyTime) {
		this.dutyTime = dutyTime;
	}
	public Long getDutyTimePer14Days() {
		return dutyTimePer14Days;
	}
	public void setDutyTimePer14Days(Long dutyTimePer14Days) {
		this.dutyTimePer14Days = dutyTimePer14Days;
	}
	public Long getOSR() {
		return OSR;
	}
	public void setOSR(Long oSR) {
		OSR = oSR;
	}
	public Long getOSRPer14Days() {
		return OSRPer14Days;
	}
	public void setOSRPer14Days(Long oSRPer14Days) {
		OSRPer14Days = oSRPer14Days;
	}
	public Long getHQR() {
		return HQR;
	}
	public void setHQR(Long hQR) {
		HQR = hQR;
	}
	public Long getHQRPer14Days() {
		return HQRPer14Days;
	}
	public void setHQRPer14Days(Long hQRPer14Days) {
		HQRPer14Days = hQRPer14Days;
	}
	public Long getAvgHQR() {
		return avgHQR;
	}
	public void setAvgHQR(Long avgHQR) {
		this.avgHQR = avgHQR;
	}
		
}
