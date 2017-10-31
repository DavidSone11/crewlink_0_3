package com.mathologic.projects.crewlink.custom.models;

public class ItemSearchResult {
	private String trainNo;
	private Integer trainStartDay;
	private String fromStationDS;
	private String toStationDS;
	private String ddName;
	private String rtName;
	private String clName;
	public ItemSearchResult(String trainNo, Integer trainStartDay,
			String fromStationDS, String toStationDS, String ddName,
			String rtName, String clName) {
		super();
		this.trainNo = trainNo;
		this.trainStartDay = trainStartDay;
		this.fromStationDS = fromStationDS;
		this.toStationDS = toStationDS;
		this.ddName = ddName;
		this.rtName = rtName;
		this.clName = clName;
	}
	public ItemSearchResult() {
		super();
	}
	public String getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	public Integer getTrainStartDay() {
		return trainStartDay;
	}
	public void setTrainStartDay(Integer trainStartDay) {
		this.trainStartDay = trainStartDay;
	}
	public String getFromStationDS() {
		return fromStationDS;
	}
	public void setFromStationDS(String fromStationDS) {
		this.fromStationDS = fromStationDS;
	}
	public String getToStationDS() {
		return toStationDS;
	}
	public void setToStationDS(String toStationDS) {
		this.toStationDS = toStationDS;
	}
	public String getDdName() {
		return ddName;
	}
	public void setDdName(String ddName) {
		this.ddName = ddName;
	}
	public String getRtName() {
		return rtName;
	}
	public void setRtName(String rtName) {
		this.rtName = rtName;
	}
	public String getClName() {
		return clName;
	}
	public void setClName(String clName) {
		this.clName = clName;
	}
	
	
}
