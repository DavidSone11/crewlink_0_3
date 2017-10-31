package com.mathologic.projects.crewlink.custom.models;

public class StationVM 
{
	private Long id;
	private String code;
	private String name;
	private int noOfBeds;
	private Long headStationSignOffDuration;
	private Long headStationSignOnDuration;
	private Long outStationSignOffDuration;
	private Long outStationSignOnDuration;
	
	public StationVM()
	{
		
	}
	
	public StationVM(Long id,String code, String name, Long headStationSignOffDuration, Long headStationSignOnDuration,
			int noOfBeds, Long outStationSignOnDuration, Long outStationSignOffDuration) {
		super();
		this.id=id;
		this.code = code;
		this.name = name;
		this.headStationSignOffDuration = headStationSignOffDuration;
		this.headStationSignOnDuration = headStationSignOnDuration;
		this.noOfBeds = noOfBeds;
		this.outStationSignOnDuration = outStationSignOnDuration;
		this.outStationSignOffDuration = outStationSignOffDuration;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getHeadStationSignOffDuration() {
		return headStationSignOffDuration;
	}

	public void setHeadStationSignOffDuration(Long headStationSignOffDuration) {
		this.headStationSignOffDuration = headStationSignOffDuration;
	}

	public Long getHeadStationSignOnDuration() {
		return headStationSignOnDuration;
	}

	public void setHeadStationSignOnDuration(Long headStationSignOnDuration) {
		this.headStationSignOnDuration = headStationSignOnDuration;
	}

	public int getNoOfBeds() {
		return noOfBeds;
	}

	public void setNoOfBeds(int noOfBeds) {
		this.noOfBeds = noOfBeds;
	}

	public Long getOutStationSignOnDuration() {
		return outStationSignOnDuration;
	}

	public void setOutStationSignOnDuration(Long outStationSignOnDuration) {
		this.outStationSignOnDuration = outStationSignOnDuration;
	}

	public Long getOutStationSignOffDuration() {
		return outStationSignOffDuration;
	}

	public void setOutStationSignOffDuration(Long outStationSignOffDuration) {
		this.outStationSignOffDuration = outStationSignOffDuration;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
