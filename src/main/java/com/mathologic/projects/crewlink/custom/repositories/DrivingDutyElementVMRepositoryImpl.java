package com.mathologic.projects.crewlink.custom.repositories;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;

import com.mathologic.projects.crewlink.custom.models.DrivingDutyElementVM;
import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.models.SelectionDetails;
import com.mathologic.projects.crewlink.models.Day;

import org.springframework.transaction.annotation.Transactional;

/**
 * This repository contains implementations of list driving duty elements, 
 * delete driving duty elements .
 * 
 * @author Santosh
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
@Repository
public class DrivingDutyElementVMRepositoryImpl implements DrivingDutyElementVMRepository {
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * This is used to list driving duty elements
	 */
	@Transactional(readOnly = true)
	@Override
	public SelectViewModel listDrivingElements(Long id,Long drivingduty,Long userPlan, String sort, Long page, Long size) {
		
		String FROM = "	FROM driving_duty_element as dde" 
			    + "	LEFT JOIN station AS fs ON (dde.from_station = fs.id)"
			    + "	LEFT JOIN station AS ts ON (dde.to_station = ts.id)" 
			    + " LEFT JOIN driving_section as ds ON (ds.driving_duty_element = dde.id ) "
			    + " LEFT JOIN station as dsfs ON (dsfs.id = ds.from_station) "
			    + " LEFT JOIN station as dsts ON (dsts.id = ds.to_station)"
			    + "	LEFT JOIN pilot_trip as spt ON (dde.start_pilot_trip = spt.id)" 
			    + "	LEFT JOIN pilot_trip as ept ON (dde.end_pilot_trip = ept.id)"
			    + "	LEFT JOIN station as fsp ON (spt.from_station = fsp.id)"
			    + "	LEFT JOIN station as tsp ON (spt.to_station = tsp.id)"
			    + "	LEFT JOIN station as fep ON (ept.from_station = fep.id)"
			    + "	LEFT JOIN station as tep ON (ept.to_station = tep.id)"
			    + "	LEFT JOIN pilot_type as spty ON ( spt.pilot_type = spty.id)"
			    + "	LEFT JOIN pilot_type as epty ON ( ept.pilot_type = epty.id)"
			    + "	WHERE (:drivingduty IS NULL OR dde.driving_duty = :drivingduty)"	
			    + " AND (:id IS NULL OR dde.id = :id)"
			    + "	AND dde.user_plan = :userPlan";
		String query1 = "SELECT COUNT(dde.id)"
				+ FROM;
								
				Query query1f = entityManager.createNativeQuery(query1);
				query1f.setParameter("userPlan", userPlan);
				query1f.setParameter("drivingduty", drivingduty);
				query1f.setParameter("id", id);
				Long totalElements = ((java.math.BigInteger) query1f.getSingleResult()).longValue();
				Long startIndex = page*size;
				Long totalPages = totalElements / size;
				Long currentPage = page;
				String baseItemRestUri = "/api/drivingDutyElements/";
				SelectViewModel result = new SelectViewModel(DrivingDutyElementVM.class,
						new SelectionDetails(totalElements, startIndex, currentPage,
								totalPages, baseItemRestUri), null);
				if (totalElements > 0) {
					
					if(sort!=null && sort.isEmpty()){
						sort = null;
					}
					
					String query2 = "SELECT dde.id,"
							+ " fs.code as fromStation,"
							+ " ts.code as toStation,"
							+ " dde.start_time as departureTime,"
							+ " dde.start_day as departureDay,"
							+ " dde.end_time as arrivalTime,"
							+ " dde.end_day as arrivalDay,"
							+ " dde.duration as duration,"
							+ " dde.distance as distance,"
							+ " spty.name as startPilotTypeName,"
							+ "	spt.name  as startPilotTripName,"
							+ " spt.start_day as startPilotDepartureDay,"
							+ " spt.start_time as startPilotDepartureTime,"
							+ " spt.end_day as startPilotArrivalDay,"
							+ " spt.end_time as startPilotArrivalTime,"
							+ " fsp.code as startPilotFromStation,"
							+ " tsp.code as startPilotToStation,"
							+ " spt.duration as startPilotduration,"
							+ " spt.distance as startPilotdistance,"
							+ " epty.name as endPilotTypeName,"
							+ " ept.name  as endPilotTripName,"
							+ " ept.start_day as endPilotDepartureDay,"
							+ " ept.start_time as endPilotDepartureTime,"
							+ " ept.end_day as endPilotArrivalDay,"
							+ " ept.end_time as endPilotArrivalTime,"
							+ " fep.code as endPilotFromStation, "
							+ " tep.code as endPilotToStation,ept.duration as endPilotduration,"
							+ " ept.distance as endPilotdistance,"
							+ "	dsfs.code as drivingSectionFromStation,dsts.code as drivingSectionToStation,ds.start_day as drivingSectionDepartureDay,"
							+ "	ds.start_time as drivingSectionDepartureTime,ds.end_day as drivingSectionArrivalDay, ds.end_time as drivingSectionArrivalTime,"
							+ "	ds.duration as drivingSectionDuration,ds.distance as drivingSectionDistance" 
							+ FROM
							+ " ORDER BY "+sort
							+ " LIMIT :start, :offset";
					Query query2f = entityManager.createNativeQuery(query2);
					query2f.setParameter("userPlan", userPlan);
					query2f.setParameter("drivingduty", drivingduty);
					query2f.setParameter("id", id);
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
	 * This is used to save the driving duty elements
	 */
	@Transactional(readOnly = false)
	@Override
	public ProcessResult save(Long drivingSectionId, Long startPilotId, Long endPilotId ,Long userPlan){
		ProcessResult resultO = null;
		
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("CreateDrivingDutyElement");
		        storedProcedure.registerStoredProcedureParameter("drivingSectionId", Long.class, ParameterMode.IN);
		        storedProcedure.registerStoredProcedureParameter("startPilotId", Long.class, ParameterMode.IN);
		        storedProcedure.registerStoredProcedureParameter("endPilotId", Long.class, ParameterMode.IN);
		        storedProcedure.registerStoredProcedureParameter("userPlan", Long.class, ParameterMode.IN);
		        storedProcedure.registerStoredProcedureParameter("createdDrivingDutyElement", Long.class, ParameterMode.OUT);
		        storedProcedure.registerStoredProcedureParameter("result", Boolean.class, ParameterMode.OUT);
		        storedProcedure.registerStoredProcedureParameter("errorMessage", String.class, ParameterMode.OUT);
		        
		        storedProcedure.setParameter("drivingSectionId", (drivingSectionId!=null)?drivingSectionId:-1);
		        storedProcedure.setParameter("startPilotId", (startPilotId!=null)?startPilotId:-1);
		        storedProcedure.setParameter("endPilotId", (endPilotId!=null)?endPilotId:-1);
		        storedProcedure.setParameter("userPlan", (userPlan!=null)?userPlan:-1);		        
		        
		        storedProcedure.execute();
		         
		        Boolean result=(Boolean)storedProcedure.getOutputParameterValue("result");
		        String errorMessage=storedProcedure.getOutputParameterValue("errorMessage").toString();
		        if(result){
		        	Long outputValue = (Long)storedProcedure.getOutputParameterValue("createdDrivingDutyElement");
		        	resultO = new ProcessResult(result, errorMessage,outputValue.toString());
		        }else{
		        	resultO= new ProcessResult(result, errorMessage);
		        }

		
		return resultO;
	}

	/**
	 * This is used to delete driving duty element 
	 */
	
	@Transactional(readOnly = false)
	@Override
	public ProcessResult deleteDrivingDutyElementByDrivingDuty(Long userPlan,Long id)
	{
		ProcessResult result=null;
		String query2 = "DELETE FROM driving_duty_element"
				+ " WHERE"
				+ " driving_duty = :id"
				+ " AND user_plan = :userPlan";
		Query query2f = entityManager.createNativeQuery(query2);
		query2f.setParameter("userPlan", userPlan);
		query2f.setParameter("id", id);
		try {
			 Integer res = query2f.executeUpdate();
			 if(res!=0){
		        	result = new ProcessResult(true,"SUCCESS");
		        }else{
		        	result= new ProcessResult(true, "Failed to delete from driving duty!");
		        }
		} catch (Exception ex) {
			System.out.println("ERROR in QUERY: " + ex.getMessage());
		}

			return result;
	}

	
}
