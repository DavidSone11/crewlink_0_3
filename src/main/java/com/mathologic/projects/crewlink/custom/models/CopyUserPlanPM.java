package com.mathologic.projects.crewlink.custom.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mathologic.projects.crewlink.utils.converters.LocalDateTimeDeSerializer;
import com.mathologic.projects.crewlink.utils.converters.LocalDateTimeSerializer;

public class CopyUserPlanPM implements Serializable{
	private String sourceUserPlanName;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeSerializer.class)
	private LocalDateTime sourceUserPlanTimeStamp;
	
	private String newUserPlanName;
	
	private String  username;

	public CopyUserPlanPM() {
		super();
	}

	public CopyUserPlanPM(String sourceUserPlanName,
			LocalDateTime sourceUserPlanTimeStamp, String newUserPlanName,
			String username) {
		super();
		this.sourceUserPlanName = sourceUserPlanName;
		this.sourceUserPlanTimeStamp = sourceUserPlanTimeStamp;
		this.newUserPlanName = newUserPlanName;
		this.username = username;
	}

	public String getSourceUserPlanName() {
		return sourceUserPlanName;
	}

	public void setSourceUserPlanName(String sourceUserPlanName) {
		this.sourceUserPlanName = sourceUserPlanName;
	}

	public LocalDateTime getSourceUserPlanTimeStamp() {
		return sourceUserPlanTimeStamp;
	}

	public void setSourceUserPlanTimeStamp(LocalDateTime sourceUserPlanTimeStamp) {
		this.sourceUserPlanTimeStamp = sourceUserPlanTimeStamp;
	}

	public String getNewUserPlanName() {
		return newUserPlanName;
	}

	public void setNewUserPlanName(String newUserPlanName) {
		this.newUserPlanName = newUserPlanName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
}
