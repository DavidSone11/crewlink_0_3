package com.mathologic.projects.crewlink.custom.repositories;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mathologic.projects.crewlink.custom.models.DrivingSectionVM;
import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.models.SelectionDetails;
import com.mathologic.projects.crewlink.models.Day;

/**
 * This repository contains implementations of list driving sections, save
 * driving section for single day, for all days, for all days plus driving duty
 * .
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
@Repository
public class DrivingSectionVMRepositoryImpl implements
		DrivingSectionVMRepository {
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * This is used to save driving section for single train for single day
	 */
	@Transactional(readOnly = false)
	@Override
	public ProcessResult saveDrivingSectionsForSingleTrain(Integer trainNo,
			Day startDay, String stopNumbers, Long userPlan) {
		ProcessResult resultO = null;

		// Create stored procedure query for creating driving section for single
		// train for single day
		// register the parameter types to match the db stored procedure
		StoredProcedureQuery storedProcedure = entityManager
				.createStoredProcedureQuery("CreateDrivingSectionForSingleTrain");
		storedProcedure.registerStoredProcedureParameter("trainNo",
				Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("startDay",
				Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("stopNumbers",
				String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("userPlan",
				Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("result",
				Boolean.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("errorMessage",
				String.class, ParameterMode.OUT);
		// set the parameter values
		storedProcedure.setParameter("trainNo", trainNo);
		storedProcedure.setParameter("startDay", startDay.ordinal());
		storedProcedure.setParameter("stopNumbers", stopNumbers);
		storedProcedure.setParameter("userPlan", userPlan);
		// call the stored procedure
		storedProcedure.execute();

		Boolean result = (Boolean) storedProcedure
				.getOutputParameterValue("result");
		String errorMessage = storedProcedure.getOutputParameterValue(
				"errorMessage").toString();

		resultO = new ProcessResult(result, errorMessage);

		return resultO;
	}

	/**
	 * This is used to save driving sections of single train for all days
	 */
	@Transactional(readOnly = false)
	@Override
	public ProcessResult saveDrivingSectionsForTrainAllDays(Integer trainNo,
			String stopNumbers, Long userPlan) {
		ProcessResult resultO = null;

		// Create stored procedure query for save driving sections for single
		// train for all days
		// Register the parameters required for this stored procedure
		StoredProcedureQuery storedProcedure = entityManager
				.createStoredProcedureQuery("CreateDrivingSectionForTrainAllDays");
		storedProcedure.registerStoredProcedureParameter("trainNo",
				Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("stopNumbers",
				String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("userPlan",
				Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("result",
				Boolean.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("errorMessage",
				String.class, ParameterMode.OUT);
		// set the parameter
		storedProcedure.setParameter("trainNo", trainNo);
		storedProcedure.setParameter("stopNumbers", stopNumbers);
		storedProcedure.setParameter("userPlan", userPlan);
		// call the stored procedure
		storedProcedure.execute();
		// Fetch the result output parameter and error Message
		Boolean result = (Boolean) storedProcedure
				.getOutputParameterValue("result");
		String errorMessage = storedProcedure.getOutputParameterValue(
				"errorMessage").toString();

		resultO = new ProcessResult(result, errorMessage);

		return resultO;
	}

	/**
	 * This is used to save driving sections for all days with driving duties
	 */
	@Transactional(readOnly = false)
	@Override
	public ProcessResult saveDrivingSectionsAndDrivingDutiesForTrainAllDays(
			Integer trainNo, String stopNumbers, Long userPlan) {
		ProcessResult resultO = null;
		// Create stored procedure query for save driving sections for single
		// train for all days with driving duty
		// Register the parameters required for this stored procedure
		StoredProcedureQuery storedProcedure = entityManager
				.createStoredProcedureQuery("CreateDrivingSectionAndDrivingDutyForTrainAllDays");
		storedProcedure.registerStoredProcedureParameter("trainNo",
				Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("stopNumbers",
				String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("userPlan",
				Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("result",
				Boolean.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("errorMessage",
				String.class, ParameterMode.OUT);
		// set the parameter
		storedProcedure.setParameter("trainNo", trainNo);
		storedProcedure.setParameter("stopNumbers", stopNumbers);
		storedProcedure.setParameter("userPlan", userPlan);
		// call the stored procedure
		storedProcedure.execute();
		// Fetch the result output parameter and error Message
		Boolean result = (Boolean) storedProcedure
				.getOutputParameterValue("result");
		String errorMessage = storedProcedure.getOutputParameterValue(
				"errorMessage").toString();

		resultO = new ProcessResult(result, errorMessage);

		return resultO;
	}

	/**
	 * This is used to list driving sections
	 */
	@Transactional(readOnly = true)
	@Override
	public SelectViewModel listDrivingSections(Integer trainNo, Day originDay,
			String fromStation, String toStation, Day departureDay,
			String minDepartureTime, String maxDepartureTime, Day arrivalDay,
			String minArrivalTime, String maxArrivalTime, Long minDuration,
			Long maxDuration, Long minDistance, Long maxDistance,
			Boolean isDrivingDuty, Boolean isIgnore, Long userPlan,
			String sort, Long page, Long size) {
		String FROM = " FROM driving_section as ds LEFT JOIN driving_duty_element as de ON (ds.driving_duty_element = de.id), train as t, station as sf, station as st "
				+ " WHERE ds.train = t.id "
				+ " AND ds.from_station = sf.id "
				+ " AND ds.to_station = st.id "

				+ " AND ( :trainNo IS NULL OR CONVERT(t.train_no,CHAR(8)) LIKE :trainNo)"
				+ " AND ( :originDay IS NULL OR t.start_day = :originDay ) "
				+ " AND ( :fromStation IS NULL OR sf.code = :fromStation ) "
				+ " AND ( :toStation IS NULL OR st.code = :toStation ) "
				+ " AND ( :departureDay IS NULL OR ds.start_day = :departureDay ) "
				+ " AND ( :minDepartureTime IS NULL OR (CAST(IFNULL(:minDepartureTime,'0:00') as TIME) <= ds.start_time) ) "
				+ " AND ( :maxDepartureTime IS NULL OR (CAST(IFNULL(:maxDepartureTime,'0:00') as TIME) >= ds.start_time) ) "
				+ " AND ( :arrivalDay IS NULL OR ds.end_day = :arrivalDay ) "
				+ " AND ( :minArrivalTime IS NULL OR (CAST(IFNULL(:minArrivalTime,'0:00') as TIME) <= ds.end_time) ) "
				+ " AND ( :maxArrivalTime IS NULL OR (CAST(IFNULL(:maxArrivalTime,'0:00') as TIME) >= ds.end_time) ) "
				+ " AND ( :minDuration IS NULL OR :minDuration <= ds.duration ) "
				+ " AND ( :maxDuration IS NULL OR :maxDuration >= ds.duration ) "
				+ " AND ( :minDistance IS NULL OR :minDistance <= ds.distance ) "
				+ " AND ( :maxDistance IS NULL OR :maxDistance >= ds.distance ) "
				+ " AND ( :isDrivingDuty IS NULL OR ((ds.driving_duty_element IS NOT NULL) = :isDrivingDuty) )"
				+ " AND ( :isIgnore IS NULL OR ds.is_ignore = :isIgnore )"
				+ " AND ( :userPlan = ds.user_plan )";
		String query1 = "SELECT COUNT(ds.id) " + FROM;

		Query query1f = entityManager.createNativeQuery(query1);
		query1f.setParameter("trainNo", trainNo);
		query1f.setParameter("originDay",
				(originDay != null) ? originDay.ordinal() : null);
		query1f.setParameter("fromStation", ((fromStation!=null)?((fromStation.isEmpty())?null:fromStation):null));
		query1f.setParameter("toStation",((toStation!=null)?((toStation.isEmpty())?null:toStation):null));
		query1f.setParameter("departureDay",
				(departureDay != null) ? departureDay.ordinal() : null);
		query1f.setParameter("minDepartureTime", minDepartureTime);
		query1f.setParameter("maxDepartureTime", maxDepartureTime);
		query1f.setParameter("arrivalDay",
				(arrivalDay != null) ? arrivalDay.ordinal() : null);
		query1f.setParameter("minArrivalTime", minArrivalTime);
		query1f.setParameter("maxArrivalTime", maxArrivalTime);
		query1f.setParameter("minDuration", minDuration);
		query1f.setParameter("maxDuration", maxDuration);
		query1f.setParameter("minDistance", minDistance);
		query1f.setParameter("maxDistance", maxDistance);
		query1f.setParameter("isDrivingDuty", isDrivingDuty);
		query1f.setParameter("isIgnore", isIgnore);
		query1f.setParameter("userPlan", userPlan);
		Long totalElements = ((java.math.BigInteger) query1f.getSingleResult())
				.longValue();
		Long startIndex = page * size;
		Long totalPages = totalElements / size;
		Long currentPage = page;
		String baseItemRestUri = "/api/drivingStations/";
		SelectViewModel result = new SelectViewModel(DrivingSectionVM.class,
				new SelectionDetails(totalElements, startIndex, currentPage,
						totalPages, baseItemRestUri), null);
		if (totalElements > 0) {

			if (sort != null && sort.isEmpty()) {
				sort = null;
			}

			String query2 = "SELECT ds.id as id, t.train_no as trainNo, t.start_day as originDay, ds.driving_section_order_no as drivingSectionOrderNo, sf.code as fromStation, "
					+ " st.code as toStation, ds.start_day as departureDay, ds.start_time as departureTime,"
					+ " ds.end_day as arrivalDay, ds.end_time as arrivalTime,"
					+ " ds.duration as duration, ds.distance as distance, IF(ds.driving_duty_element IS NOT NULL,TRUE,FALSE) as isDrivingDuty, de.driving_duty as dutyId,"
					+ " ds.is_ignore as isIgnore" + FROM

					+ " ORDER BY " + sort + " LIMIT :start, :offset";
			Query query2f = entityManager.createNativeQuery(query2);
			query2f.setParameter("trainNo", trainNo);
			query2f.setParameter("originDay",
					(originDay != null) ? originDay.ordinal() : null);
			query2f.setParameter("fromStation", ((fromStation!=null)?((fromStation.isEmpty())?null:fromStation):null));
			query2f.setParameter("toStation",((toStation!=null)?((toStation.isEmpty())?null:toStation):null));
			query2f.setParameter("departureDay",
					(departureDay != null) ? departureDay.ordinal() : null);
			query2f.setParameter("minDepartureTime", minDepartureTime);
			query2f.setParameter("maxDepartureTime", maxDepartureTime);
			query2f.setParameter("arrivalDay",
					(arrivalDay != null) ? arrivalDay.ordinal() : null);
			query2f.setParameter("minArrivalTime", minArrivalTime);
			query2f.setParameter("maxArrivalTime", maxArrivalTime);
			query2f.setParameter("minDuration", minDuration);
			query2f.setParameter("maxDuration", maxDuration);
			query2f.setParameter("minDistance", minDistance);
			query2f.setParameter("maxDistance", maxDistance);
			query2f.setParameter("isDrivingDuty", isDrivingDuty);
			query2f.setParameter("isIgnore", isIgnore);
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
	public ProcessResult updateDrivingSectionInUserList(Long id,
			Boolean isIgnore, Long userPlan) {
		ProcessResult result = null;
		String query1 = "UPDATE driving_section SET is_ignore = :isIgnore WHERE id = :id AND user_plan = :userPlan";

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
