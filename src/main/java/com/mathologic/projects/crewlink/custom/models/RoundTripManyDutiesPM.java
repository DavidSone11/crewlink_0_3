package com.mathologic.projects.crewlink.custom.models;

import java.io.Serializable;
import java.util.List;

public class RoundTripManyDutiesPM  implements Serializable{
	private List<DrivingDutyVM> drivingDuties;
	private Long crewType;
	public List<DrivingDutyVM> getDrivingDuties() {
		return drivingDuties;
	}
	public void setDrivingDuties(List<DrivingDutyVM> drivingDuties) {
		this.drivingDuties = drivingDuties;
	}
	public Long getCrewType() {
		return crewType;
	}
	public void setCrewType(Long crewType) {
		this.crewType = crewType;
	}
	
	public RoundTripManyDutiesPM() {
		super();
	}
	public RoundTripManyDutiesPM(List<DrivingDutyVM> drivingDuties,
			Long crewType) {
		super();
		this.drivingDuties = drivingDuties;
		this.crewType = crewType;
	}	
}
