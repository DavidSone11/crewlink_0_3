package com.mathologic.projects.crewlink.custom.models;

import java.io.Serializable;
import java.util.Map;
/**
 * 
 * @author Laxman
 *
 */
public class BaseStationAnalysisVM implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Map<String,Long>> baseStationUsedDistance;
	private Map<String, Map<String,Long>> baseStationAvailableDistance;
	
	public BaseStationAnalysisVM(){
		
	}

	public BaseStationAnalysisVM(Map<String, Map<String, Long>> baseStationUsedDistance,
			Map<String, Map<String, Long>> baseStationAvailableDistance) {
		super();
		this.baseStationUsedDistance = baseStationUsedDistance;
		this.baseStationAvailableDistance = baseStationAvailableDistance;
	}

	public Map<String, Map<String, Long>> getBaseStationUsedDistance() {
		return baseStationUsedDistance;
	}

	public void setBaseStationUsedDistance(Map<String, Map<String, Long>> baseStationUsedDistance) {
		this.baseStationUsedDistance = baseStationUsedDistance;
	}

	public Map<String, Map<String, Long>> getBaseStationAvailableDistance() {
		return baseStationAvailableDistance;
	}

	public void setBaseStationAvailableDistance(Map<String, Map<String, Long>> baseStationAvailableDistance) {
		this.baseStationAvailableDistance = baseStationAvailableDistance;
	}
	
}
