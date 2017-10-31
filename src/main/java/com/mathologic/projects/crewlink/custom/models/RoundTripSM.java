package com.mathologic.projects.crewlink.custom.models;

import java.io.Serializable;


public class RoundTripSM implements Serializable{
	public Long id;
	public int startMins;
	public int endMins;
	public Long lastDutyDuration;
	
	
	
	public RoundTripSM() {
		super();
	}


	public RoundTripSM(Long id, int startMins, int endMins, Long lastDutyDuration) {
		super();
		this.id = id;
		this.startMins = startMins;
		this.endMins = endMins;
		this.lastDutyDuration = lastDutyDuration;
	}


	@Override
	public RoundTripSM clone() {
		RoundTripSM newObj = new RoundTripSM();
		newObj.id = this.id;
		newObj.startMins = this.startMins;
		newObj.endMins = this.endMins;
		newObj.lastDutyDuration = this.lastDutyDuration;
		return newObj;
	}


	@Override
	public boolean equals(Object arg0) {
		return this.id == ((RoundTripSM)arg0).id;
	}


	@Override
	public String toString() {
		return "RoundTripSM [id=" + id + ", startMins=" + new DateTimeModel(startMins)
				+ ", endMins=" + new DateTimeModel(endMins) + ", lastDutyDuration="+lastDutyDuration+"]\n";
	}
	
}
