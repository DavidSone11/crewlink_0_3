package com.mathologic.projects.crewlink.models;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by vivek on 25/9/15.
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@Column(unique = true)
	private String username;

	private String password;

	@NotNull
	private String firstName;
	private String lastName;

	@NotNull
	@Column(unique = true)
	private String email;

	private String mobileNo;
	private String telephoneNo;
	private String extension;
	private String employeeNo;
/*
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Station station;*/

	@NotNull
	@ManyToOne	
	private Role role;

	@OneToMany(mappedBy="user",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<UserPlan> userPlans = new LinkedList<UserPlan>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Station> Stations = new LinkedList<Station>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(name="FK_USER_TRAIN"))
	private List<Train> trains = new LinkedList<Train>();
	

	@Column(columnDefinition="BIT(1) DEFAULT b'0'")
	private Boolean isActive;
	
	private String activationKey; 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getTelephoneNo() {
		return telephoneNo;
	}

	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	/*public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}
*/
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<UserPlan> getUserPlans() {
		return userPlans;
	}

	public void setUserPlans(List<UserPlan> userPlans) {
		this.userPlans = userPlans;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getActivationKey() {
		return activationKey;
	}

	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

	public List<Station> getStations() {
		return Stations;
	}

	public void setStations(List<Station> stations) {
		Stations = stations;
	}

	public List<Train> getTrains() {
		return trains;
	}

	public void setTrains(List<Train> trains) {
		this.trains = trains;
	}
	
	

}