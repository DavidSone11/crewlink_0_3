package com.mathologic.projects.crewlink.custom.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DrivingDutyManyPM implements Serializable{
	private Long signOnDuration;
	private Long signOffDuration;
	private List<String> drivingSections = new ArrayList<String>();
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
	public List<String> getDrivingSections() {
		return drivingSections;
	}
	public void setDrivingSections(List<String> drivingSections) {
		this.drivingSections = drivingSections;
	}
	
}
