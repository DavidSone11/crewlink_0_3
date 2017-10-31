/**
 * 
 */
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author avitash
 *
 */
@Entity
@Table(name = "division")
public class Division implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    private Long id;
	
	@NotNull
	@Column(unique=true)
	private String name;
	
	@ManyToMany(mappedBy="divisions",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
	private List<Station> stations = new LinkedList<Station>();

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

	public List<Station> getStations() {
		return stations;
	}

	public void setStations(List<Station> stations) {
		this.stations = stations;
	}
}
