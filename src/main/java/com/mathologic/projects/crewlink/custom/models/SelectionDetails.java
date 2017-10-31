package com.mathologic.projects.crewlink.custom.models;

public class SelectionDetails {
	private Long totalElements;
	private Long startIndex;
	private Long currentPage;
	private Long totalPages;
	private String baseItemRestUri;
	
	public SelectionDetails(Long totalElements, Long startIndex,
			Long currentPage, Long totalPages, String baseItemRestUri) {
		super();
		this.totalElements = totalElements;
		this.startIndex = startIndex;
		this.currentPage = currentPage;
		this.totalPages = totalPages;
		this.baseItemRestUri = baseItemRestUri;
	}
	
	public Long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}
	public Long getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(Long startIndex) {
		this.startIndex = startIndex;
	}
	public Long getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Long currentPage) {
		this.currentPage = currentPage;
	}
	public Long getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Long totalPages) {
		this.totalPages = totalPages;
	}
	public String getBaseItemRestUri() {
		return baseItemRestUri;
	}
	public void setBaseItemRestUri(String baseItemRestUri) {
		this.baseItemRestUri = baseItemRestUri;
	}
	
}
