//package com.mathologic.projects.crewlink.models;
//
//import java.io.Serializable;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.OneToOne;
//import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//
///**
// * Created by vivek on 3/10/15.
// */
//@Entity
//@Table(name = "out_station")
//public class OutStation implements Serializable {
//	
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@GeneratedValue
//	private Long id;
//
//	@NotNull
//	@OneToOne
//	private Station station;
//
//	private Integer numberOfBeds;
//
//	private Long signOnDuration;
//
//	private Long signOffDuration;
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
//	public Integer getNumberOfBeds() {
//		return numberOfBeds;
//	}
//
//	public void setNumberOfBeds(Integer numberOfBeds) {
//		this.numberOfBeds = numberOfBeds;
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
//}
