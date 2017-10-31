package com.mathologic.projects.crewlink.custom.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DrivingDutyPM implements Serializable{
	private Long signOnDuration;
	private Long signOffDuration;
	private List<Map<String,String>> drivingDutyElements = new ArrayList<Map<String,String>>();
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
	public List<Map<String, String>> getDrivingDutyElements() {
		return drivingDutyElements;
	}
	public void setDrivingDutyElements(List<Map<String, String>> drivingDutyElements) {
		this.drivingDutyElements = drivingDutyElements;
	}
	
}
