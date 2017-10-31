package com.mathologic.projects.crewlink.custom.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mathologic.projects.crewlink.custom.models.DrivingDutyManyPM;
import com.mathologic.projects.crewlink.custom.models.DrivingDutyPM;
import com.mathologic.projects.crewlink.custom.models.DrivingDutyVM;
import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.models.SelectionDetails;
import com.mathologic.projects.crewlink.models.Day;

/**
 * This repository contains implementations of list driving duties, saving driving duty,
 * delete driving duties .
 * 
 * @author Santosh
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
@Repository
public class DrivingDutyVMRepositoryImpl implements DrivingDutyVMRepository {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private DrivingDutyElementVMRepository drivingDutyElementVMRepository;

	/**
	 * This is used to list driving duties.
	 */
	@Override
	@Transactional(readOnly = true)
	public SelectViewModel listDrivingDuties(Long userPlan,String ddName, String fromStation, String toStation,
			Long minDuration, Long maxDuration, Long minDistance, Long maxDistance,Day arrivalDay,String minArrivalTime,String maxArrivalTime,Day departureDay,
			String minDepartureTime, String maxDepartureTime,Long roundTrip,Boolean isRoundTrip,Boolean isIgnore, String sort, Long page, Long size) {

		
		String FROM = " FROM  driving_duty AS dd, station AS fs,station AS ts"
				+ " WHERE dd.user_plan = :userPlan"
				+ " AND dd.to_station = ts.id"
				+ " AND dd.from_station = fs.id"
				+ " AND ( :roundTrip IS NULL OR dd.round_trip = :roundTrip)"
				+ " AND ( :ddName IS NULL OR dd.dd_name LIKE :ddName)"
				+ " AND ( :fromStation IS NULL OR fs.code = :fromStation)"
				+ " AND ( :toStation IS NULL OR ts.code = :toStation)"
				+ " AND ( :arrivalDay IS NULL OR dd.end_day = :arrivalDay) "
				+ " AND ( :minArrivalTime IS NULL OR (CAST(IFNULL(:minArrivalTime,'0:00')as TIME) <= dd.end_time))"
				+ " AND ( :maxArrivalTime IS NULL OR (CAST(IFNULL(:maxArrivalTime,'0:00')as TIME) >= dd.end_time))"
				+ " AND ( :departureDay IS NULL OR dd.start_day = :departureDay) "
				+ " AND ( :minDepartureTime IS NULL OR (CAST(IFNULL(:minDepartureTime,'0:00')as TIME) <= dd.start_time))"
				+ " AND ( :maxDepartureTime IS NULL OR (CAST(IFNULL(:maxDepartureTime,'0:00')as TIME) >= dd.start_time))"
				+ " AND ( :minDuration IS NULL OR :minDuration <= dd.duration) "
				+ " AND ( :maxDuration IS NULL OR :maxDuration >= dd.duration) "
				+ " AND ( :minDistance IS NULL OR :minDistance <= dd.distance) "
				+ " AND ( :maxDistance IS NULL OR :maxDistance >= dd.distance)"
				+ " AND ( :isRoundTrip IS NULL OR ((dd.round_trip IS NOT NULL) = :isRoundTrip) )"
				+ " AND ( :isIgnore IS NULL OR dd.is_ignore = :isIgnore)";
		
		
		String query1 = "SELECT COUNT(dd.id)"
		+ FROM;
		
		
		Query query1f = entityManager.createNativeQuery(query1);
		query1f.setParameter("userPlan", userPlan);
		query1f.setParameter("ddName", (ddName!=null)?"%"+ddName+"%":null);
		query1f.setParameter("fromStation", ((fromStation!=null)?((fromStation.isEmpty())?null:fromStation):null));
		query1f.setParameter("toStation", ((toStation!=null)?((toStation.isEmpty())?null:toStation):null));
		query1f.setParameter("arrivalDay", (arrivalDay!=null)?arrivalDay.ordinal():null);
		query1f.setParameter("minArrivalTime", minArrivalTime);
		query1f.setParameter("maxArrivalTime", maxArrivalTime);
		query1f.setParameter("departureDay", (departureDay!=null)?departureDay.ordinal():null);
		query1f.setParameter("minDepartureTime", minDepartureTime);
		query1f.setParameter("maxDepartureTime", maxDepartureTime);
		query1f.setParameter("minDuration", minDuration);
		query1f.setParameter("maxDuration", maxDuration);
		query1f.setParameter("minDistance", minDistance);
		query1f.setParameter("maxDistance", maxDistance);
		query1f.setParameter("roundTrip", roundTrip);
		query1f.setParameter("isRoundTrip", isRoundTrip);
		query1f.setParameter("isIgnore", isIgnore);
		
		
		
		Long totalElements = ((java.math.BigInteger) query1f.getSingleResult()).longValue();
		Long startIndex = page*size;
		Long totalPages = totalElements / size;
		Long currentPage = page;
		String baseItemRestUri = "/api/drivingDuties/";
		SelectViewModel result = new SelectViewModel(DrivingDutyVM.class,
				new SelectionDetails(totalElements, startIndex, currentPage,
						totalPages, baseItemRestUri), null);
		if (totalElements > 0) {
			
			if(sort!=null && sort.isEmpty()){
				sort = null;
			}
			
			String query2 = 
					  "SELECT dd.id as id,"
					  + " dd.dd_name as drivingdutyName,"
					  + " fs.code as fromStation,"
					  + " ts.code as toStation,"
					  + " dd.start_day as departureDay,"
					  + " dd.start_time as departureTime,"
					  + " dd.end_day as arrivalDay,"
					  + " dd.end_time as arrivalTime,"
					  + " dd.duration as duration,"
					  + " dd.distance as distance,"
					  + " dd.sign_on_duration as signOnDuration,"
					  + " dd.sign_off_duration as signOffDuration,"
					  + " dd.round_trip as roundTrip,"
					  + " IF(dd.round_trip is NULL,FALSE,TRUE) as isRoundTrip,"
					  + " dd.is_ignore as isIgnore,"
					  + " dd.round_trip_order_no as roundTripOrderNo"
					  + FROM
					  + " ORDER BY "+sort
					  + " LIMIT :start, :offset";
			Query query2f = entityManager.createNativeQuery(query2);
			query2f.setParameter("userPlan", userPlan);
			query2f.setParameter("ddName", (ddName!=null)?"%"+ddName+"%":null);
			query2f.setParameter("fromStation", ((fromStation!=null)?((fromStation.isEmpty())?null:fromStation):null));
			query2f.setParameter("toStation", ((toStation!=null)?((toStation.isEmpty())?null:toStation):null));
			query2f.setParameter("arrivalDay", (arrivalDay!=null)?arrivalDay.ordinal():null);
			query2f.setParameter("minArrivalTime", minArrivalTime);
			query2f.setParameter("maxArrivalTime", maxArrivalTime);
			query2f.setParameter("departureDay", (departureDay!=null)?departureDay.ordinal():null);
			query2f.setParameter("minDepartureTime", minDepartureTime);
			query2f.setParameter("maxDepartureTime", maxDepartureTime);
			query2f.setParameter("minDuration", minDuration);
			query2f.setParameter("maxDuration", maxDuration);
			query2f.setParameter("minDistance", minDistance);
			query2f.setParameter("maxDistance", maxDistance);
			query2f.setParameter("isRoundTrip", isRoundTrip);
			query2f.setParameter("isIgnore", isIgnore);
			
			
			query2f.setParameter("roundTrip",roundTrip);
			query2f.setParameter("start", page*size);
			
			query2f.setParameter("offset", size);
			try {
				result.setData((List<Object[]>)query2f.getResultList());
				for(int i=0; i<result.getData().size();i++){
					if(result.getData().get(i)[7].toString().equals("00:00:00")){
						result.getData().get(i)[7] = "24:00:00";
					}
				}
			} catch (Exception ex) {
				System.out.println("ERROR in QUERY: " + ex.getMessage());
			}
			
			
		}
		
		return result;
	}

	/**
	 * This is used to save the driving duty 
	 */
	@Override
	@Transactional(readOnly = false)
	public ProcessResult save(DrivingDutyPM data,Long userPlan) {
		ProcessResult result = null;
		List<ProcessResult> partResults = new ArrayList<ProcessResult>();
		List<Map<String,String>> drivingDutyElements = data.getDrivingDutyElements();
		for(Map<String,String>item : drivingDutyElements){
			// gets the driving section id, start pilot id, end pilot id, start day, start time
			// checks whether they are null or not
			Long drivingSectionId = item.get("drivingSectionId")==null?(-1):item.get("drivingSectionId").isEmpty()?-1:Long.parseLong(item.get("drivingSectionId"));
			String spId = item.get("startPilotId");
			Long startPilotId = spId==null?(-1):spId.isEmpty()?-1:Long.parseLong(spId);
			Long endPilotId = item.get("endPilotId")==null?(-1):item.get("endPilotId").isEmpty()?-1:Long.parseLong(item.get("endPilotId"));
			//Day startDay = item.get("startDay")==null?(Day.SUNDAY):item.get("startDay").isEmpty()?Day.SUNDAY:Day.valueOf(item.get("startDay"));
			//String startTime = item.get("startTime")==null?"":item.get("startTime");
			partResults.add(drivingDutyElementVMRepository.save(drivingSectionId, startPilotId, endPilotId, userPlan));
		}
		String drivingDutyElementIds = "";
		String orderNos = "";
		//checks the drivingDutyElements size and partResults size should be same.
		//Otherwise throw error.
		if(drivingDutyElements.size()!=partResults.size()){
			return new ProcessResult(false,"Failed to save all the driving duty elements.");
		}
		boolean isDDE = false;
		// Select the driving duty element ids and represent in string format by comma separated
		for(int i=0;i<partResults.size();i++){
			if(partResults.get(i).getOutputValue()!=null && !partResults.get(i).getOutputValue().equals("null")){
				drivingDutyElementIds += ","+ partResults.get(i).getOutputValue();
				orderNos += ","+(i+1);
				isDDE = true;
			}
		}
		if(isDDE){
			drivingDutyElementIds = drivingDutyElementIds.substring(1);
			orderNos = orderNos.substring(1);
			
			// Call the stored procedure.
			// Set the parameter type
			StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("CreateDrivingDuty");
	        storedProcedure.registerStoredProcedureParameter("drivingDutyElementIds", String.class, ParameterMode.IN);
	        storedProcedure.registerStoredProcedureParameter("orderNos", String.class, ParameterMode.IN);
	        storedProcedure.registerStoredProcedureParameter("signOnDuration", Long.class, ParameterMode.IN);
	        storedProcedure.registerStoredProcedureParameter("signOffDuration", Long.class, ParameterMode.IN);
	        storedProcedure.registerStoredProcedureParameter("userPlan", Long.class, ParameterMode.IN);
	        storedProcedure.registerStoredProcedureParameter("createdDrivingDuty", Long.class, ParameterMode.OUT);
	        storedProcedure.registerStoredProcedureParameter("result", Boolean.class, ParameterMode.OUT);
	        storedProcedure.registerStoredProcedureParameter("errorMessage", String.class, ParameterMode.OUT);
	        
	        
	        // Set parameter to the stored procedure
	        storedProcedure.setParameter("drivingDutyElementIds", drivingDutyElementIds);
	        storedProcedure.setParameter("orderNos", orderNos);
	        storedProcedure.setParameter("signOnDuration", data.getSignOnDuration()==null?0L:data.getSignOnDuration());
	        storedProcedure.setParameter("signOffDuration", data.getSignOffDuration()==null?0L:data.getSignOffDuration());
	        storedProcedure.setParameter("userPlan", userPlan);
	        //This calls the stored procedure
	        storedProcedure.execute();
	         
	        //Fetch the output parameter called result from the stored procedure. 
	        Boolean resultB=(Boolean)storedProcedure.getOutputParameterValue("result");
	        //Fetch the output parameter called errorMessage from the stored procedure.
	        String errorMessage=storedProcedure.getOutputParameterValue("errorMessage").toString();
	        //if resultB is false means procedure not executed properly, true means executed properly
	        // if it is executed properly means get the created id.
	        if(resultB){
	        	Long outputValue = (Long)storedProcedure.getOutputParameterValue("createdDrivingDuty");
	        	result = new ProcessResult(resultB, errorMessage,outputValue.toString());
	        }else{
	        	result= new ProcessResult(resultB, errorMessage);
	        }
		}
		else{
			result= new ProcessResult(false, "Failed to save Driving Duty as No Driving Duty Element was saved");
		}
		return result;
	}
	
	@Override
	@Transactional(readOnly = false)
	public ProcessResult saveMany(DrivingDutyManyPM data,Long userPlan) {
		ProcessResult result = null;
		
		for(String drivingSectionId : data.getDrivingSections()){
			DrivingDutyPM duty = new DrivingDutyPM();
			List<Map<String,String>> drivingDutyElements = new ArrayList<Map<String,String>>();
			Map<String,String> drivingSection = new HashMap<String,String>();
			drivingSection.put("drivingSectionId",drivingSectionId);
			drivingDutyElements.add(drivingSection);
			duty.setDrivingDutyElements(drivingDutyElements);
			duty.setSignOffDuration(data.getSignOffDuration());
			duty.setSignOnDuration(data.getSignOnDuration());
			result = save(duty,userPlan);
			if(!result.getResult())
				break;
		}
		
		return result;
	}
	

	/**
	 * This is used to delete driving duty
	 */
	@Override
	@Transactional(readOnly = false)
	public ProcessResult deleteDrivingDuty(Long userPlan, Long id) {
		ProcessResult result=null;
		String query2 = 
				  "DELETE FROM driving_duty"
				  + " WHERE id = :id"
				  + " AND user_plan = :userPlan" ;
		Query query2f = entityManager.createNativeQuery(query2);
		query2f.setParameter("userPlan", userPlan);
		query2f.setParameter("id", id);
		
		try {
			 ProcessResult result2=drivingDutyElementVMRepository.deleteDrivingDutyElementByDrivingDuty(userPlan, id);
			 Integer res = query2f.executeUpdate();
			
			 if(res!=0 && result2.getResult()){
		        	result = new ProcessResult(true,"SUCCESS");
		        }else{
		        	result= new ProcessResult(true, "Failed to delete from driving duty!");
		        }
			 
			 
		} catch (Exception ex) {
			System.out.println("ERROR in QUERY: " + ex.getMessage());
		}
		return result;
	
		
		
	}

	/**
	 * This method is used to list driving duties with Round trip details
	 */
	@Override
	public SelectViewModel listDrivingDutiesWithRT(Long userPlan,String fromStation, String toStation,Boolean isIgnore, String sort, Long page, Long size) {
		String FROM = " FROM  driving_duty AS dd LEFT JOIN round_trip as rt ON (dd.round_trip = rt.id) LEFT JOIN station as bs  ON (rt.station=bs.id),"
				+ " station AS fs,station AS ts"
				+ " WHERE dd.user_plan = :userPlan"
				+ " AND dd.to_station = ts.id"
				+ " AND dd.from_station = fs.id"
				+ " AND ( :fromStation IS NULL OR fs.code = :fromStation)"
				+ " AND ( :toStation IS NULL OR ts.code = :toStation)"
				+ " AND ( :isIgnore IS NULL OR dd.is_ignore = :isIgnore)";
		
		
		String query1 = "SELECT COUNT(dd.id)"
		+ FROM;
		
		
		Query query1f = entityManager.createNativeQuery(query1);
		query1f.setParameter("userPlan", userPlan);
		query1f.setParameter("fromStation", fromStation);
		query1f.setParameter("toStation", toStation);
		query1f.setParameter("isIgnore", isIgnore);
		
		Long totalElements = ((java.math.BigInteger) query1f.getSingleResult()).longValue();
		Long startIndex = page*size;
		Long totalPages = totalElements / size;
		Long currentPage = page;
		String baseItemRestUri = "/api/drivingDuties/";
		SelectViewModel result = new SelectViewModel(DrivingDutyVM.class,
				new SelectionDetails(totalElements, startIndex, currentPage,
						totalPages, baseItemRestUri), null);
		if (totalElements > 0) {
			
			if(sort!=null && sort.isEmpty()){
				sort = null;
			}
			
			String query2 = 
					  "SELECT dd.id as id,"
					  + " dd.dd_name as drivingdutyName,"
					  + " fs.code as fromStation,"
					  + " ts.code as toStation,"
					  + " dd.start_day as departureDay,"
					  + " dd.start_time as departureTime,"
					  + " dd.end_day as arrivalDay,"
					  + " dd.end_time as arrivalTime,"
					  + " dd.duration as duration,"
					  + " dd.distance as distance,"
					  + " dd.sign_on_duration as signOnDuration,"
					  + " dd.sign_off_duration as signOffDuration,"
					  + " dd.round_trip as roundTrip,"
					  + " IF(dd.round_trip is NULL,FALSE,TRUE) as isRoundTrip,"
					  + " dd.is_ignore as isIgnore,"
					  + " dd.round_trip_order_no as roundTripOrderNo,"
					  + " rt.rt_name as roundTripName,"
					  + " bs.code as roundTripBaseStation"
					  + FROM
					  + " ORDER BY "+sort
					  + " LIMIT :start, :offset";
			Query query2f = entityManager.createNativeQuery(query2);
			query2f.setParameter("userPlan", userPlan);
			query2f.setParameter("fromStation", fromStation);
			query2f.setParameter("toStation", toStation);
			query2f.setParameter("isIgnore", isIgnore);
			query2f.setParameter("start", page*size);
			query2f.setParameter("offset", size);
			try {
				result.setData(query2f.getResultList());
				for(int i=0; i<result.getData().size();i++){
					if(result.getData().get(i)[7].toString().equals("00:00:00")){
						result.getData().get(i)[7] = "24:00:00";
					}
				}
			} catch (Exception ex) {
				System.out.println("ERROR in QUERY: " + ex.getMessage());
			}
			
			
		}
		
		return result;
	}

	@Transactional(readOnly = false)
	@Override
	public ProcessResult updateDrivingDutyInUserList(Long id, Boolean isIgnore, Long userPlan) {

		ProcessResult result = null;
		String query1 = "UPDATE driving_duty SET is_ignore = :isIgnore WHERE id = :id AND user_plan = :userPlan";

		Query query1f = entityManager.createNativeQuery(query1);
		query1f.setParameter("isIgnore", isIgnore);
		query1f.setParameter("id", id);
		query1f.setParameter("userPlan", userPlan);

		try {
			query1f.executeUpdate();
			result = new ProcessResult(true, "SUCCESS");
		} catch (Exception ex) {
			result = new ProcessResult(false, "Error in updating isIgnore"
					+ ex.getMessage());
			System.out.println("ERROR in QUERY: " + ex.getMessage());
		}

		return result;
	}
	
	
	
}