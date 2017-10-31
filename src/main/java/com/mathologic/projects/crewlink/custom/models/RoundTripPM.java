package com.mathologic.projects.crewlink.custom.models;

import java.io.Serializable;
import java.util.List;

public class RoundTripPM  implements Serializable{
	private DrivingDutyVM from;
	private DrivingDutyVM to;
	private Long crewType;
	public DrivingDutyVM getFrom() {
		return from;
	}
	public void setFrom(DrivingDutyVM from) {
		this.from = from;
	}
	public DrivingDutyVM getTo() {
		return to;
	}
	public void setTo(DrivingDutyVM to) {
		this.to = to;
	}
	public Long getCrewType() {
		return crewType;
	}
	public void setCrewType(Long crewType) {
		this.crewType = crewType;
	}
	
	
}
