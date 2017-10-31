package com.mathologic.projects.crewlink.custom.repositories;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mathologic.projects.crewlink.custom.models.DrivingSectionVM;
import com.mathologic.projects.crewlink.custom.models.PilotTripPM;
import com.mathologic.projects.crewlink.custom.models.PilotTripVM;
import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.models.SelectionDetails;
import com.mathologic.projects.crewlink.models.Day;

@Repository
public class PilotTripVMRepositoryImpl implements PilotTripVMRepository {

	@PersistenceContext
	private EntityManager entityManager;



	@Transactional(readOnly = true)
	@Override
	public SelectViewModel listPilotTrip(String pilotTripName, String pilotType, String fromStation, String toStation,
			Day departureDay, Day arrivalDay, String minDepartureTime, String maxDepartureTime,
			String minArrivalTime, String maxArrivalTime, Long minDistance,
			Long maxDistance,Long minDuration,
			Long maxDuration,Long userPlan, String sort, Long page, Long size) {
		
		
		
		String FROM =" FROM pilot_trip as pt "
				+ " LEFT JOIN station as sf ON (pt.from_station  = sf.id)"
				+ " LEFT JOIN station as st ON (pt.to_station = st.id)"
				+ " LEFT JOIN pilot_type as ptt ON (ptt.id = pt.pilot_type)"
				+ " WHERE pt.user IN (SELECT user FROM user_plan WHERE id = :userPlan)"
				+ " AND ( :pilotTripName IS NULL OR pt.name = :pilotTripName)"
				+ " AND ( :fromStation IS NULL OR sf.code = :fromStation)"
				+ " AND ( :toStation IS NULL OR st.code = :toStation)"
				+ " AND ( :departureDay IS NULL OR pt.start_day = :departureDay)"
				+ " AND ( :minDepartureTime IS NULL OR (CAST(IFNULL(:minDepartureTime,'0:00') as TIME) <= pt.start_time))"
				+ " AND ( :maxDepartureTime IS NULL OR (CAST(IFNULL(:maxDepartureTime,'0:00') as TIME) >= pt.start_time))"

				+ " AND ( :arrivalDay IS NULL OR pt.end_day = :arrivalDay)"
				+ " AND ( :minArrivalTime IS NULL OR (CAST(IFNULL(:minArrivalTime,'0:00') as TIME) <= pt.end_time))"
				+ " AND ( :maxArrivalTime IS NULL OR (CAST(IFNULL(:maxArrivalTime,'0:00') as TIME) >= pt.end_time))"
				+ " AND ( :pilotType IS NULL OR ptt.name  = :pilotType)"
				+ " AND ( :minDistance IS NULL OR :minDistance <= pt.distance ) "
				+ " AND ( :maxDistance IS NULL OR :maxDistance >= pt.distance )"
				+ " AND ( :minDuration IS NULL OR :minDuration <= pt.duration )"
				+ " AND ( :maxDuration IS NULL OR :maxDuration >= pt.duration )"; 
				
		
				String query1 = "SELECT COUNT(pt.id) " + FROM;
				Query query1f = entityManager.createNativeQuery(query1);
				
				query1f.setParameter("pilotTripName", ((pilotTripName!=null)?((pilotTripName.isEmpty())?null:pilotTripName):null));
				query1f.setParameter("pilotType", ((pilotType!=null)?((pilotType.isEmpty())?null:pilotType):null));
				
				query1f.setParameter("fromStation", ((fromStation!=null)?((fromStation.isEmpty())?null:fromStation):null));
				query1f.setParameter("toStation",((toStation!=null)?((toStation.isEmpty())?null:toStation):null));
				query1f.setParameter("departureDay",(departureDay != null) ? departureDay.ordinal() : null);
				query1f.setParameter("minDepartureTime", minDepartureTime);
				query1f.setParameter("maxDepartureTime", maxDepartureTime);
				
				query1f.setParameter("arrivalDay",(arrivalDay != null) ? arrivalDay.ordinal() : null);
				query1f.setParameter("minArrivalTime", minArrivalTime);
				query1f.setParameter("maxArrivalTime", maxArrivalTime);
				query1f.setParameter("minDuration", minDuration);
				query1f.setParameter("maxDuration", maxDuration);
				query1f.setParameter("minDistance", minDistance);
				query1f.setParameter("maxDistance", maxDistance);
				query1f.setParameter("userPlan", userPlan);
				
				Long totalElements = ((java.math.BigInteger) query1f.getSingleResult()).longValue();
				Long startIndex = page * size;
				Long totalPages = totalElements / size;
				Long currentPage = page;
				
				String baseItemRestUri = "/api/pilotTrips/";
				SelectViewModel result = new SelectViewModel(PilotTripVM.class,
						new SelectionDetails(totalElements, startIndex, currentPage,
								totalPages, baseItemRestUri), null);
				if (totalElements > 0) {

					if (sort != null && sort.isEmpty()) {
						sort = null;
					}
					
					String query2 = "SELECT"
							+ " pt.id as id,"
							+ " pt.name as pilotTripName,"
							+ " ptt.name as pilotType,"
							+ " sf.code as fromStation,"
							+ " st.code as toStation,"
							+ " pt.start_day as departureDay,"
							+ " pt.end_day as arrivalDay,"
							+ " pt.start_time as departureTime,"
							+ " pt.end_time as arrivalTime,"
							+ " pt.distance as distance,"
							+ " pt.duration as duration" + FROM
							+ " ORDER BY " + sort + " LIMIT :start, :offset";
							
							Query query2f = entityManager.createNativeQuery(query2);
							
							query2f.setParameter("pilotTripName", ((pilotTripName!=null)?((pilotTripName.isEmpty())?null:pilotTripName):null));
							query2f.setParameter("pilotType", ((pilotType!=null)?((pilotType.isEmpty())?null:pilotType):null));
							
							query2f.setParameter("fromStation", ((fromStation!=null)?((fromStation.isEmpty())?null:fromStation):null));
							query2f.setParameter("toStation",((toStation!=null)?((toStation.isEmpty())?null:toStation):null));
							query2f.setParameter("departureDay",(departureDay != null) ? departureDay.ordinal() : null);
							query2f.setParameter("minDepartureTime", minDepartureTime);
							query2f.setParameter("maxDepartureTime", maxDepartureTime);
							query2f.setParameter("arrivalDay",(arrivalDay != null) ? arrivalDay.ordinal() : null);
							query2f.setParameter("minArrivalTime", minArrivalTime);
							query2f.setParameter("maxArrivalTime", maxArrivalTime);
							query2f.setParameter("minDuration", minDuration);
							query2f.setParameter("maxDuration", maxDuration);
							query2f.setParameter("minDistance", minDistance);
							query2f.setParameter("maxDistance", maxDistance);
							query2f.setParameter("userPlan", userPlan);
							query2f.setParameter("start", page * size);
							query2f.setParameter("offset", size);
							
							
					try {
						result.setData(query2f.getResultList());
					} catch (Exception ex) {
						System.out.println("ERROR in QUERY: " + ex.getMessage());
					}
				}

		return result;
	}


	@Transactional(readOnly = false)
	@Override
	public ProcessResult savePilotTrain(PilotTripPM data, Long userPlan) {
		
		ProcessResult result = null;

		// Create stored procedure query for create train
		// Register the parameters required for this stored procedure

		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("CreatePilotTrip");
		storedProcedure.registerStoredProcedureParameter("name", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("fromStation", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("toStation", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("duration", Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("distance", Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("pilotType", Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("startDay", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("startTime", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("endDay", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("endTime", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("userPlan", Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("createdPilotTripId", Long.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("result", Boolean.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("errorMessage", String.class, ParameterMode.OUT);
		// Set the parameter value
		storedProcedure.setParameter("name", data.getName());
		storedProcedure.setParameter("fromStation", data.getFromStation());
		storedProcedure.setParameter("toStation", data.getToStation());
		storedProcedure.setParameter("duration", data.getDuration());
		storedProcedure.setParameter("distance", data.getDistance());
		storedProcedure.setParameter("pilotType", data.getPilotType());
		storedProcedure.setParameter("startDay", data.getDepartureDay().ordinal());
		storedProcedure.setParameter("startTime", data.getDepartureTime());
		storedProcedure.setParameter("endDay", data.getArrivalDay().ordinal());
		storedProcedure.setParameter("endTime", data.getArrivalTime());
		storedProcedure.setParameter("userPlan", userPlan);
		// call the stored procedure
		storedProcedure.execute();
		// Fetch the result and errorMessage output parameter of the stored
		// procedure
		Boolean resultB = (Boolean) storedProcedure.getOutputParameterValue("result");
		String errorMessage = storedProcedure.getOutputParameterValue("errorMessage").toString();
		if (resultB) {
			Long outputValue = (Long) storedProcedure.getOutputParameterValue("createdPilotTripId");
			result = new ProcessResult(resultB, errorMessage, outputValue.toString());
		} else {
			result = new ProcessResult(resultB, errorMessage);
		}

		return result;
	}



	@Transactional(readOnly = false)
	@Override
	public ProcessResult savePilot(PilotTripPM data, Long userPlan) {
		ProcessResult result = null;

		// Create stored procedure query for create train
		// Register the parameters required for this stored procedure

		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("CreatePilotTrip");
		storedProcedure.registerStoredProcedureParameter("name", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("fromStation", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("toStation", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("duration", Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("distance", Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("pilotType", Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("startDay", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("startTime", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("endDay", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("endTime", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("userPlan", Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("createdPilotTripId", Long.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("result", Boolean.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("errorMessage", String.class, ParameterMode.OUT);
		// Set the parameter value
		storedProcedure.setParameter("name", data.getName());
		storedProcedure.setParameter("fromStation", data.getFromStation());
		storedProcedure.setParameter("toStation", data.getToStation());
		storedProcedure.setParameter("duration", data.getDuration());
		storedProcedure.setParameter("distance", data.getDistance());
		storedProcedure.setParameter("pilotType", data.getPilotType());
		storedProcedure.setParameter("startDay", data.getDepartureDay().ordinal());
		storedProcedure.setParameter("startTime", data.getDepartureTime());
		storedProcedure.setParameter("endDay", data.getArrivalDay().ordinal());
		storedProcedure.setParameter("endTime", data.getArrivalTime());
		storedProcedure.setParameter("userPlan", userPlan);
		// call the stored procedure
		storedProcedure.execute();
		// Fetch the result and errorMessage output parameter of the stored
		// procedure
		Boolean resultB = (Boolean) storedProcedure.getOutputParameterValue("result");
		String errorMessage = storedProcedure.getOutputParameterValue("errorMessage").toString();
		if (resultB) {
			Long outputValue = (Long) storedProcedure.getOutputParameterValue("createdPilotTripId");
			result = new ProcessResult(resultB, errorMessage, outputValue.toString());
		} else {
			result = new ProcessResult(resultB, errorMessage);
		}

		return result;
	}
}
