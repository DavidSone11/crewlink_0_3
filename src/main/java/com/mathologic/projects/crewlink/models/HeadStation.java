//package com.mathologic.projects.crewlink.models;
//
//import java.io.Serializable;
//import java.util.LinkedList;
//import java.util.List;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.ManyToMany;
//import javax.persistence.OneToMany;
//import javax.persistence.OneToOne;
//import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//
///**
// * Created by vivek on 3/10/15.
// */
//@Entity
//@Table(name = "head_station")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//public class HeadStation implements Serializable {
//	
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@GeneratedValue
//	private Long id;
//
//	@NotNull
//	@OneToOne(fetch = FetchType.LAZY)
//	private Station station;
//
//	private Long signOnDuration;
//
//	private Long signOffDuration;
//
//	@ManyToMany(fetch = FetchType.LAZY)
//	private List<CrewType> crewTypes = new LinkedList<CrewType>();
//	
//	@OneToMany(mappedBy="headStation",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
//	private List<User> users = new LinkedList<User>();
//	
//	@ManyToMany(fetch = FetchType.LAZY)
//	private List<Division> divisions = new LinkedList<Division>();
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public Station getStation() {
//		return station;
//	}
//
//	public void setStation(Station station) {
//		this.station = station;
//	}
//
//	public Long getSignOnDuration() {
//		return signOnDuration;
//	}
//
//	public void setSignOnDuration(Long signOnDuration) {
//		this.signOnDuration = signOnDuration;
//	}
//
//	public Long getSignOffDuration() {
//		return signOffDuration;
//	}
//
//	public void setSignOffDuration(Long signOffDuration) {
//		this.signOffDuration = signOffDuration;
//	}
//
//	public List<CrewType> getCrewTypes() {
//		return crewTypes;
//	}
//
//	public void setCrewTypes(List<CrewType> crewTypes) {
//		this.crewTypes = crewTypes;
//	}
//
//	public List<User> getUsers() {
//		return users;
//	}
//
//	public void setUsers(List<User> users) {
//		this.users = users;
//	}
//
//	public List<Division> getDivisions() {
//		return divisions;
//	}
//
//	public void setDivisions(List<Division> divisions) {
//		this.divisions = divisions;
//	}
//}
