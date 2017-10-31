package com.mathologic.projects.crewlink.custom.models;



public class DashBoardVM {
	private Integer pTrains;
	private Integer pDrivingSections;
	private Integer pDrivingDuties;
	private Integer pRoundTrips;
	private Integer cCrewlink;
	private Integer noOfLocoPilots;
	private Double totalKM;
	private Double pilotKM;
	private Double trainKM;
	private Double avgDutyHrsPer14Days;
	private Double noOfRTPerMonthPerCrew;
	private Double pAvgDutyHrs;
	private Double pAvgOSR;
	private Double pAvgHQR;
	public DashBoardVM(Integer pTrains, Integer pDrivingSections,
			Integer pDrivingDuties, Integer pRoundTrips, Integer cCrewlink,
			Integer noOfLocoPilots, Double totalKM, Double pilotKM,
			Double trainKM, Double avgDutyHrsPer14Days,
			Double noOfRTPerMonthPerCrew, Double pAvgDutyHrs, Double pAvgOSR,
			Double pAvgHQR) {
		super();
		this.pTrains = pTrains;
		this.pDrivingSections = pDrivingSections;
		this.pDrivingDuties = pDrivingDuties;
		this.pRoundTrips = pRoundTrips;
		this.cCrewlink = cCrewlink;
		this.noOfLocoPilots = noOfLocoPilots;
		this.totalKM = totalKM;
		this.pilotKM = pilotKM;
		this.trainKM = trainKM;
		this.avgDutyHrsPer14Days = avgDutyHrsPer14Days;
		this.noOfRTPerMonthPerCrew = noOfRTPerMonthPerCrew;
		this.pAvgDutyHrs = pAvgDutyHrs;
		this.pAvgOSR = pAvgOSR;
		this.pAvgHQR = pAvgHQR;
	}
	public Integer getpTrains() {
		return pTrains;
	}
	public void setpTrains(Integer pTrains) {
		this.pTrains = pTrains;
	}
	public Integer getpDrivingSections() {
		return pDrivingSections;
	}
	public void setpDrivingSections(Integer pDrivingSections) {
		this.pDrivingSections = pDrivingSections;
	}
	public Integer getpDrivingDuties() {
		return pDrivingDuties;
	}
	public void setpDrivingDuties(Integer pDrivingDuties) {
		this.pDrivingDuties = pDrivingDuties;
	}
	public Integer getpRoundTrips() {
		return pRoundTrips;
	}
	public void setpRoundTrips(Integer pRoundTrips) {
		this.pRoundTrips = pRoundTrips;
	}
	public Integer getcCrewlink() {
		return cCrewlink;
	}
	public void setcCrewlink(Integer cCrewlink) {
		this.cCrewlink = cCrewlink;
	}
	public Integer getNoOfLocoPilots() {
		return noOfLocoPilots;
	}
	public void setNoOfLocoPilots(Integer noOfLocoPilots) {
		this.noOfLocoPilots = noOfLocoPilots;
	}
	public Double getTotalKM() {
		return totalKM;
	}
	public void setTotalKM(Double totalKM) {
		this.totalKM = totalKM;
	}
	public Double getPilotKM() {
		return pilotKM;
	}
	public void setPilotKM(Double pilotKM) {
		this.pilotKM = pilotKM;
	}
	public Double getTrainKM() {
		return trainKM;
	}
	public void setTrainKM(Double trainKM) {
		this.trainKM = trainKM;
	}
	public Double getAvgDutyHrsPer14Days() {
		return avgDutyHrsPer14Days;
	}
	public void setAvgDutyHrsPer14Days(Double avgDutyHrsPer14Days) {
		this.avgDutyHrsPer14Days = avgDutyHrsPer14Days;
	}
	public Double getNoOfRTPerMonthPerCrew() {
		return noOfRTPerMonthPerCrew;
	}
	public void setNoOfRTPerMonthPerCrew(Double noOfRTPerMonthPerCrew) {
		this.noOfRTPerMonthPerCrew = noOfRTPerMonthPerCrew;
	}
	public Double getpAvgDutyHrs() {
		return pAvgDutyHrs;
	}
	public void setpAvgDutyHrs(Double pAvgDutyHrs) {
		this.pAvgDutyHrs = pAvgDutyHrs;
	}
	public Double getpAvgOSR() {
		return pAvgOSR;
	}
	public void setpAvgOSR(Double pAvgOSR) {
		this.pAvgOSR = pAvgOSR;
	}
	public Double getpAvgHQR() {
		return pAvgHQR;
	}
	public void setpAvgHQR(Double pAvgHQR) {
		this.pAvgHQR = pAvgHQR;
	}
}
