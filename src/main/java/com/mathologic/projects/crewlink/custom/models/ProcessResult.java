package com.mathologic.projects.crewlink.custom.models;

import java.io.Serializable;

public class ProcessResult implements Serializable {
	private Boolean result;
	private String errorMessage;
	private String outputValue;

	public ProcessResult(Boolean result, String errorMessage, String outputValue) {
		super();
		this.errorMessage = errorMessage;
		this.result = result;
		this.outputValue = outputValue;
	}

	public ProcessResult(Boolean result, String errorMessage) {
		super();
		this.errorMessage = errorMessage;
		this.result = result;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getOutputValue() {
		return outputValue;
	}

	public void setOutputValue(String outputValue) {
		this.outputValue = outputValue;
	}

}
