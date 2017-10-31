package com.mathologic.projects.crewlink.custom.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderedRoundTrips implements Serializable {
	public List<RoundTripSM> roundTrips;
	public Long duration;
	@Override
	protected OrderedRoundTrips clone() {
		OrderedRoundTrips newObj = new OrderedRoundTrips();
		newObj.roundTrips = new ArrayList<RoundTripSM>();
		for(RoundTripSM i : this.roundTrips) {
			newObj.roundTrips.add(i);
		}
		newObj.duration = this.duration;
		return newObj;
	}
	@Override
	public String toString() {
		return "OrderedRoundTrips [roundTrips=" + roundTrips + ", duration="
				+ duration + "]\n";
	}
}
