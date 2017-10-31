package com.mathologic.projects.crewlink.custom.repositories;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mathologic.projects.crewlink.custom.models.CrewLinkVM;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.models.SelectionDetails;
import com.mathologic.projects.crewlink.custom.models.StationVM;

/**
 * This repository contains implementations of list stations and find by code
 * 
 * @author Jagdish
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */

@Repository
public class StationVMRespositoryImpl implements StationVMRepository{
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * This is used to list stations
	 */
	@Transactional(readOnly = true)
	@Override
	public SelectViewModel listStations(Long id,String code,String name,int noOfBeds,Long headStationSignOffDuration,
										Long headStationSignOnDuration,Long outStationSignOffDuration, Long outStationSignOnDuration,String sort, Long page, Long size) {
		
		String FROM = " FROM station AS s"
				+ " WHERE ( :code IS NULL OR s.code = :code ) "
				+ " AND ( :name IS NULL OR s.name LIKE :name ) "
				+ " AND ( :noOfBeds IS NULL OR s.number_of_beds = :noOfBeds ) "
				+ " AND ( :headStationSignOffDuration IS NULL OR s.head_station_sign_off_duration = :headStationSignOffDuration ) "
				+ " AND ( :headStationSignOnDuration IS NULL OR s.head_station_sign_on_duration = :headStationSignOnDuration ) "
				+ " AND ( :outStationSignOffDuration IS NULL OR s.out_station_sign_off_duration = :outStationSignOffDuration ) "
				+ " AND ( :outStationSignOnDuration IS NULL OR s.out_station_sign_on_duration = :outStationSignOnDuration ) ";
		
		String query1 = 
				"SELECT COUNT(s.id) "
				+ FROM;
		
		Query query1f = entityManager.createNativeQuery(query1);
		query1f.setParameter("code", code);
		query1f.setParameter("name", (name!=null)?"%"+ name +"%":null);
		query1f.setParameter("noOfBeds", noOfBeds);
		query1f.setParameter("headStationSignOffDuration", headStationSignOffDuration);
		query1f.setParameter("headStationSignOnDuration", headStationSignOnDuration);
		query1f.setParameter("outStationSignOffDuration", outStationSignOffDuration);
		query1f.setParameter("outStationSignOnDuration", outStationSignOnDuration);
		
		
		Long totalElements = ((java.math.BigInteger) query1f.getSingleResult())
				.longValue();
		Long startIndex = page*size;
		Long totalPages = totalElements / size;
		Long currentPage = page;
		String baseItemRestUri = "/api/crewLinks/";
		SelectViewModel result = new SelectViewModel(CrewLinkVM.class,
				new SelectionDetails(totalElements, startIndex, currentPage,
						totalPages, baseItemRestUri), null);
		if (totalElements > 0) {
			
			if(sort!=null && sort.isEmpty()){
				sort = null;
			}
			
			String query2 = 
					  "SELECT s.id as Id,s.name AS Name,s.number_of_beds as noOfBeds, s.head_station_sign_off_duration as headStationSignOffDuration,"
					+ "s.head_station_sign_on_duration as headStationSignOnDuration,s.out_station_sign_off_duration as outStationSignOffDuration,"
					+ "s.out_station_sign_on_duration as outStationSignOnDuration"
					+ FROM
					
					+ " ORDER BY "+sort
					+ " LIMIT :start, :offset";
			Query query2f = entityManager.createNativeQuery(query2);
			query2f.setParameter("code", code);
			query2f.setParameter("name", (name!=null)?"%"+ name +"%":null);
			query2f.setParameter("noOfBeds", noOfBeds);
			query2f.setParameter("headStationSignOffDuration", headStationSignOffDuration);
			query2f.setParameter("headStationSignOnDuration", headStationSignOnDuration);
			query2f.setParameter("outStationSignOffDuration", outStationSignOffDuration);
			query2f.setParameter("outStationSignOnDuration", outStationSignOnDuration);
			
			query2f.setParameter("start", page*size);
			query2f.setParameter("offset", size);
			try {
				result.setData(query2f.getResultList());
			} catch (Exception ex) {
				System.out.println("ERROR in QUERY: " + ex.getMessage());
			}
		}

		
		return result;
	}
	/**
	 * This is used to find the station record using station code
	 */

	@Override
	public SelectViewModel findByCode(String code,String sort, Long page, Long size) {


		String query1 = 
				"SELECT COUNT(s.id) "
				+ " FROM station AS s"
				+ " WHERE :code IS NULL OR s.code = :code";
		
		Query query1f = entityManager.createNativeQuery(query1);
		query1f.setParameter("code", code);
		
		Long totalElements = ((java.math.BigInteger) query1f.getSingleResult())
				.longValue();
		Long startIndex = page*size;
		Long totalPages = totalElements / size;
		Long currentPage = page;
		String baseItemRestUri = "/api/stations/";
		SelectViewModel result = new SelectViewModel(StationVM.class,
				new SelectionDetails(totalElements, startIndex, currentPage,
						totalPages, baseItemRestUri), null);
		if (totalElements > 0) {
			
			if(sort!=null && sort.isEmpty()){
				sort = null;
			}
			
			String query2 = 
					  "SELECT s.id as Id,s.code as Code,s.name AS Name,s.number_of_beds as noOfBeds, s.head_station_sign_off_duration as headStationSignOffDuration,"
					+ " s.head_station_sign_on_duration as headStationSignOnDuration,s.out_station_sign_off_duration as outStationSignOffDuration,"
					+ " s.out_station_sign_on_duration as outStationSignOnDuration"
					+ " FROM station AS s"
					+ " WHERE :code IS NULL OR s.code = :code ";
			Query query2f = entityManager.createNativeQuery(query2);
			query2f.setParameter("code", code);
			
			try {
				result.setData(query2f.getResultList());
			} catch (Exception ex) {
				System.out.println("ERROR in QUERY: " + ex.getMessage());
			}
		}
		
		
		return result;
	}
	
/*//	@Override
//	public ProcessResult insertStation(String code, String name, int noOfBeds, Long headStationSignOffDuration,
//			Long headStationSignOnDuration, Long outStationSignOffDuration, Long outStationSignOnDuration) {
//	
//		ProcessResult result=null;
//		String query2 = 
//				  "INSERT INTO "
//				  + " station(code,"
//				  + " name,"
//				  + " number_of_beds,"
//				  + " head_station_sign_off_duration,"
//				  + " head_station_sign_on_duration,"
//				  + " out_station_sign_off_duration,"
//				  + " out_station_sign_on_duration) VALUES"
//				  + " (?,?,?,?,?,?,?)";
//		
//		Query query2f = entityManager.createNativeQuery(query2);
//		query2f.setParameter(1, code);
//		query2f.setParameter(2, name);
//		query2f.setParameter(3, noOfBeds);
//		query2f.setParameter(4, headStationSignOffDuration);
//		query2f.setParameter(5, headStationSignOnDuration);
//		query2f.setParameter(6, outStationSignOffDuration);
//		query2f.setParameter(7, outStationSignOnDuration);
//		
//		try {
//			 Integer res = query2f.executeUpdate();
//			 if(res!=0){
//		        	result = new ProcessResult(true,"SUCCESS");
//		        }else{
//		        	result= new ProcessResult(true, "Failed to delete from driving duty!");
//		        }
//		} catch (Exception ex) {
//			System.out.println("ERROR in QUERY: " + ex.getMessage());
//		}
//		
//		return result;
//	}
*/
	
	

}
