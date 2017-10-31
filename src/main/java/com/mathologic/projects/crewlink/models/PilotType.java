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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by vivek on 3/10/15.
 */
@Entity
@Table(name = "pilot_type")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PilotType implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;
    
    @OneToMany(mappedBy="pilotType",fetch=FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<PilotTrip> pilotTrips = new LinkedList<PilotTrip>();
    
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

	public List<PilotTrip> getPilotTrips() {
		return pilotTrips;
	}

	public void setPilotTrips(List<PilotTrip> pilotTrips) {
		this.pilotTrips = pilotTrips;
	}
}
