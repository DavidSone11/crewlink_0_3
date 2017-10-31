package com.mathologic.projects.crewlink.models;

/**
 * Created by vivek on 3/10/15.
 */

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "crew_type")
public class CrewType implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;
    
    @ManyToMany(mappedBy="crewTypes",fetch = FetchType.LAZY)
    private List<Station> station = new LinkedList<Station>();
    
//    @ManyToMany(mappedBy="crewType_TrainType",fetch = FetchType.LAZY)
//    private List<TrainType> trainTypes = new LinkedList<TrainType>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Station> getStation() {
		return station;
	}

	public void setStation(List<Station> station) {
		this.station = station;
	}


//	public List<TrainType> getTrainTypes() {
//		return trainTypes;
//	}
//
//	public void setTrainTypes(List<TrainType> trainTypes) {
//		this.trainTypes = trainTypes;
//	}
}
