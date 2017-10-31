package com.mathologic.projects.crewlink.custom.repositories;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.models.SelectionDetails;
import com.mathologic.projects.crewlink.custom.models.TrainStationVM;
import com.mathologic.projects.crewlink.custom.models.TrainStationVMWithDS;
import com.mathologic.projects.crewlink.models.Day;

/**
 * This repository contains implementations of list train stations with driving section,
 * list train stations, create train stations, and delete train stations.
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
@Repository
public class TrainStationVMRepositoryImpl implements TrainStationVMRepository {
	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * This is used to list the train stations with the driving sections
	 */
	
	
	@Transactional(readOnly = true)
	@Override
	public SelectViewModel listTrainStationsWithDrivingSection(Integer trainNo, Day startDay, String stationCode,Long userPlan, String sort, Long page, Long size) {

		String FROM = "FROM train_station as ts "
				+ " LEFT JOIN train as t  ON (ts.train = t.id)"
				+ " LEFT JOIN station as s  ON (ts.station = s.id)"
				+ " LEFT JOIN ("
				+ " SELECT dsts.driving_sections as ds, dsts.train_stations as ts"
				+ " FROM driving_section_train_stations as dsts LEFT JOIN driving_section as ds"
				+ " ON (dsts.driving_sections = ds.id) WHERE ds.user_plan = :userPlan "
				+ " ) as dsp ON (dsp.ts = ts.id)"
				+ " WHERE  t.train_no = :trainNo AND  t.start_day = :startDay "
				+ " AND (:stationCode IS NULL OR s.code = :stationCode ) ";
		
		String query1 = "SELECT COUNT( ts.id)"
				+ FROM;
		
		Query query1f = entityManager.createNativeQuery(query1);
		query1f.setParameter("trainNo", trainNo);
		query1f.setParameter("startDay", (startDay!=null)?startDay.ordinal():null);
		query1f.setParameter("stationCode", stationCode);
		query1f.setParameter("userPlan", userPlan);
		
		Long totalElements = ((java.math.BigInteger) query1f.getSingleResult())
				.longValue();
		Long startIndex = page*size;
		Long totalPages = totalElements / size;
		Long currentPage = page;
		String baseItemRestUri = "/api/trainStations/";
		SelectViewModel result = new SelectViewModel(TrainStationVMWithDS.class,
				new SelectionDetails(totalElements, startIndex, currentPage,
						totalPages, baseItemRestUri), null);
		if (totalElements > 0) {
			
			if(sort!=null && sort.isEmpty()){
				sort = null;
			}
			
			String query2 = "SELECT ts.id as id,ts.stop_number as stopNumber, s.code as stationCode, s.name as stationName,"
					+ " ts.arrival as arrival, ts.departure as departure, ts.day as day, "
					+ " ts.day_of_journey as dayOfJourney, ts.distance as distance, ts.journey_duration as duration, "
					+ " IF(dsp.ds IS NULL,FALSE,TRUE) as isDrivingSection,"
					+ " dsp.ds as drivingSection "
					
					+ FROM 
					//+ " GROUP BY id "
					
					+ " ORDER BY "+sort
					
					+ " LIMIT :start, :offset";
			Query query2f = entityManager.createNativeQuery(query2);
			query2f.setParameter("trainNo", trainNo);
			query2f.setParameter("startDay", (startDay!=null)?startDay.ordinal():null);
			query2f.setParameter("stationCode", stationCode);
			query2f.setParameter("userPlan", userPlan);
			
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
	 * This is used to list train stations
	 */
	@Transactional(readOnly = true)
	@Override
	public SelectViewModel listTrainStations(Integer trainNo, Day startDay, String stationCode, String sort, Long page, Long size) {

		String FROM = " FROM train_station as ts LEFT JOIN train as t ON (ts.train = t.id) LEFT JOIN station as s ON (ts.station = s.id)"
				+ " WHERE "
				+ " t.train_no = :trainNo AND "  // Here Train No and Start Day is compulsory
				+ " t.start_day = :startDay AND"
				+ " (:stationCode IS NULL OR s.code = :stationCode )";
		String query1 = "SELECT COUNT(ts.id)"
				+ FROM;
		
		Query query1f = entityManager.createNativeQuery(query1);
		query1f.setParameter("trainNo", trainNo);
		query1f.setParameter("startDay", (startDay!=null)?startDay.ordinal():null);
		query1f.setParameter("stationCode", stationCode);
		
		Long totalElements = ((java.math.BigInteger) query1f.getSingleResult())
				.longValue();
		Long startIndex = page*size;
		Long totalPages = totalElements / size;
		Long currentPage = page;
		String baseItemRestUri = "/api/trainStations/";
		SelectViewModel result = new SelectViewModel(TrainStationVM.class,
				new SelectionDetails(totalElements, startIndex, currentPage,
						totalPages, baseItemRestUri), null);
		if (totalElements > 0) {
			
			if(sort!=null && sort.isEmpty()){
				sort = null;
			}
			
			String query2 = "SELECT ts.id, ts.stop_number as stopNumber, s.code as stationCode, s.name as stationName,"
					+ " ts.arrival as arrival, ts.departure as departure, ts.day as day, "
					+ " ts.day_of_journey as dayOfJourney, ts.distance as distance, ts.journey_duration as duration"
					+ FROM
					
					+ " ORDER BY "+sort
					
					+ " LIMIT :start, :offset";
			Query query2f = entityManager.createNativeQuery(query2);
			query2f.setParameter("trainNo", trainNo);
			query2f.setParameter("startDay", (startDay!=null)?startDay.ordinal():null);
			query2f.setParameter("stationCode", stationCode);
			
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
	 * This is used to create train stations
	 */
	@Transactional(readOnly = false)
	@Override
	public ProcessResult createTrainStations(int trainNo,int stopNumber, String stationCode,
			int dayOfJourney, String arrivalTime, String departureTime, long distance) {
		ProcessResult resultO = null;
		
		//Create stored procedure query for create train stations
		//Register the parameters required for this stored procedure
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("CreateTrainStations");
		        storedProcedure.registerStoredProcedureParameter("trainNo", Integer.class, ParameterMode.IN);
		        storedProcedure.registerStoredProcedureParameter("stopNumber", Integer.class, ParameterMode.IN);
		        storedProcedure.registerStoredProcedureParameter("stationCode", String.class, ParameterMode.IN);
		        storedProcedure.registerStoredProcedureParameter("dayOfJourney", Integer.class, ParameterMode.IN);
		        storedProcedure.registerStoredProcedureParameter("arrivalTime", String.class, ParameterMode.IN);
		        storedProcedure.registerStoredProcedureParameter("departureTime", String.class, ParameterMode.IN);
		        storedProcedure.registerStoredProcedureParameter("distance", Long.class, ParameterMode.IN);		        
		        storedProcedure.registerStoredProcedureParameter("result", Boolean.class, ParameterMode.OUT);
		        storedProcedure.registerStoredProcedureParameter("errorMessage", String.class, ParameterMode.OUT);
		        //set the parameter value
		        storedProcedure.setParameter("trainNo", trainNo);
		        storedProcedure.setParameter("stopNumber",stopNumber);
		        storedProcedure.setParameter("stationCode",stationCode);
		        storedProcedure.setParameter("dayOfJourney",dayOfJourney);
		        storedProcedure.setParameter("arrivalTime",arrivalTime);
		        storedProcedure.setParameter("departureTime",departureTime);
		        storedProcedure.setParameter("distance",distance);
		        // call the stored procedure
		        storedProcedure.execute();
		        // Fetch the result and errorMessage output parameter of stored procedure
		        Boolean result=(Boolean)storedProcedure.getOutputParameterValue("result");
		        String errorMessage=storedProcedure.getOutputParameterValue("errorMessage").toString();
		        resultO = new ProcessResult(result, errorMessage);
		  return resultO;
		
		
	}

	/**
	 * Delete the train station
	 */
	@Transactional(readOnly = false)
	@Override
	public ProcessResult deleteTrainStations(Long id) {
		ProcessResult result=null;
		String query2 = " DELETE FROM train_station as ts WHERE ts.id = :id";
		Query query2f = entityManager.createNativeQuery(query2);
		query2f.setParameter("id",id);
		try{
			 Integer res = query2f.executeUpdate();
			 if(res!=0){
				 result = new ProcessResult(true,"train station has been deleted Successfully !");
			 }else{
				 result= new ProcessResult(true, "Failed to delete train station!");	 
			 }
			
		}catch(Exception ex){
			System.out.println("ERROR in (DELETE FROM train_station) Query: " + ex.getMessage());
		}
		return result;
	
	}
}
