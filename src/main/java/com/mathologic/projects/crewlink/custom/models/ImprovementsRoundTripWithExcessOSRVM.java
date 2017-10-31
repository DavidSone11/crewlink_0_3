package com.mathologic.projects.crewlink.custom.models;


public class ImprovementsRoundTripWithExcessOSRVM{
	private String rtName;
	private String baseStation;
	private Long OSR;
	private Long excessOSR;
	private Boolean NIB;
	private Integer counts;
	private String clNames;
	public ImprovementsRoundTripWithExcessOSRVM(String rtName,
			String baseStation, Long oSR, Long excessOSR, Boolean nIB,
			Integer counts, String clNames) {
		super();
		this.rtName = rtName;
		this.baseStation = baseStation;
		OSR = oSR;
		this.excessOSR = excessOSR;
		NIB = nIB;
		this.counts = counts;
		this.clNames = clNames;
	}
	public ImprovementsRoundTripWithExcessOSRVM() {
		super();
	}
	public String getRtName() {
		return rtName;
	}
	public void setRtName(String rtName) {
		this.rtName = rtName;
	}
	public String getBaseStation() {
		return baseStation;
	}
	public void setBaseStation(String baseStation) {
		this.baseStation = baseStation;
	}
	public Long getOSR() {
		return OSR;
	}
	public void setOSR(Long oSR) {
		OSR = oSR;
	}
	public Long getExcessOSR() {
		return excessOSR;
	}
	public void setExcessOSR(Long excessOSR) {
		this.excessOSR = excessOSR;
	}
	public Boolean getNIB() {
		return NIB;
	}
	public void setNIB(Boolean nIB) {
		NIB = nIB;
	}
	public Integer getCounts() {
		return counts;
	}
	public void setCounts(Integer counts) {
		this.counts = counts;
	}
	public String getClNames() {
		return clNames;
	}
	public void setClNames(String clNames) {
		this.clNames = clNames;
	}
		
}
