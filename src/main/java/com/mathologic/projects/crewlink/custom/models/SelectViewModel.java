package com.mathologic.projects.crewlink.custom.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectViewModel {
	private SelectionDetails selectionDetails;
	private List<Object[]> data;
	private Map<String,Integer> fields;
	private Class<?> clazz;
	
	private void fillFields(){
		fields = new HashMap<String,Integer>();
		for(int i=0; i<clazz.getDeclaredFields().length; i++){
			fields.put(clazz.getDeclaredFields()[i].getName(), i);
		}
	}
	public SelectViewModel(Class<?> clazz, SelectionDetails selectionDetails,
			List data) {
		super();
		this.clazz = clazz;
		this.selectionDetails = selectionDetails;
		this.data = data;
		fillFields();
	}
	
	public Map<String,Integer> getFields() {
		return fields;
	}
	public SelectionDetails getSelectionDetails() {
		return selectionDetails;
	}
	public void setSelectionDetails(SelectionDetails selectionDetails) {
		this.selectionDetails = selectionDetails;
	}
	public List<Object[]> getData() {
		return data;
	}
	public void setData(List<Object[]> data) {
		this.data = data;
	}
	
}
