//package com.mathologic.projects.crewlink.models;
//
//import java.io.Serializable;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//
///**
// * Created by vivek on 3/10/15.
// */
//@Entity
//@Table(name = "driving_section_element")
//public class DrivingSectionElement implements Serializable{
//	
//	private static final long serialVersionUID = 1L;
//
//	@Id
//    @GeneratedValue
//    private Long id;
//
//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY)
//    private TrainStation trainStation;
//    
//    @NotNull
//    @ManyToOne
//    private UserPlan userPlan;
//
//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private DrivingSection drivingSection;
//    
//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private DrivingSection nextDrivingSection;
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public TrainStation getTrainStation() {
//		return trainStation;
//	}
//
//	public void setTrainStation(TrainStation trainStation) {
//		this.trainStation = trainStation;
//	}
//
//	public UserPlan getUserPlan() {
//		return userPlan;
//	}
//
//	public void setUserPlan(UserPlan userPlan) {
//		this.userPlan = userPlan;
//	}
//
//	public DrivingSection getDrivingSection() {
//		return drivingSection;
//	}
//
//	public void setDrivingSection(DrivingSection drivingSection) {
//		this.drivingSection = drivingSection;
//	}
//
//	public DrivingSection getNextDrivingSection() {
//		return nextDrivingSection;
//	}
//
//	public void setNextDrivingSection(DrivingSection nextDrivingSection) {
//		this.nextDrivingSection = nextDrivingSection;
//	}
//}
