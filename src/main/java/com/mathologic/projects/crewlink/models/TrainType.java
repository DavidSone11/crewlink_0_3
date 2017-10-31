package com.mathologic.projects.crewlink.models;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "train_type")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TrainType implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique=true)
	private String name;
	
	@OneToMany(mappedBy="trainType", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
	private List<Train> trains = new LinkedList<Train>();
	
//	@ManyToMany
//	private List<CrewType> crewTypes = new LinkedList<CrewType>();

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

	public List<Train> getTrains() {
		return trains;
	}

	public void setTrains(List<Train> trains) {
		this.trains = trains;
	}

//	public List<CrewType> getCrewTypes() {
//		return crewTypes;
//	}
//
//	public void setCrewTypes(List<CrewType> crewTypes) {
//		this.crewTypes = crewTypes;
//	}
	
}