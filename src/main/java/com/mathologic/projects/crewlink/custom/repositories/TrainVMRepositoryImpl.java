package com.mathologic.projects.crewlink.custom.repositories;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mathologic.projects.crewlink.custom.models.PilotTrainVM;
import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.models.SelectionDetails;
import com.mathologic.projects.crewlink.custom.models.TrainByNumberVM;
import com.mathologic.projects.crewlink.custom.models.TrainVM;
import com.mathologic.projects.crewlink.models.Day;

/**
 * This repository contains implementations of list trains and save train
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
@Repository
public class TrainVMRepositoryImpl implements TrainVMRepository {
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * This is used to list the trains
	 */
	@Transactional(readOnly = true)
	@Override
	public SelectViewModel listTrainsByNumber(Integer trainNo, Day startDay, String name, String fromStation,
			String toStation, String trainType, String passingStation1, String passingStation2,
			Boolean hasDrivingSection, Boolean isUserSelected, Long userPlan, String sort, Long page, Long size) {
		String FROM = " FROM train as t LEFT JOIN station as sf ON (t.from_station = sf.id)"
				+ " LEFT JOIN station as st ON (t.to_station = st.id)"
				+ " LEFT JOIN train_type as tt ON (t.train_type = tt.id) "
				+ " WHERE "
				+ " (:trainNo IS NULL OR CONVERT(t.train_no,CHAR(8)) LIKE :trainNo)";
		if(passingStation1 !=null || passingStation2 != null){
			FROM += " AND t.id IN ( SELECT DISTINCT ts.train as train FROM train_station as ts LEFT JOIN station as s  ON (ts.station = s.id)"
					+ " WHERE  (:passingStation1 IS NULL OR s.code = :passingStation1 ) ) "
					+ " AND t.id IN ( SELECT DISTINCT ts.train as train FROM train_station as ts LEFT JOIN station as s  ON (ts.station = s.id)"
					+ " WHERE  (:passingStation2 IS NULL OR s.code = :passingStation2 ) ) ";
		}
		
			FROM += " AND (:startDay IS NULL OR t.start_day = :startDay)" 
				+ " AND (:name IS NULL OR t.name LIKE :name )"
				+ " AND (:fromStation IS NULL OR sf.code = :fromStation )"
				+ " AND (:toStation IS NULL OR st.code = :toStation )"
				+ " AND (:trainType IS NULL OR tt.name LIKE :trainType )"
				+ " AND (:hasDrivingSection IS NULL OR (SELECT IF(COUNT(ds.id) > 0, true, false) FROM driving_section as ds WHERE ds.user_plan = :userPlan AND ds.train = t.id) = :hasDrivingSection)"
				+ " AND (:isUserSelected IS NULL OR (SELECT IF(COUNT(trains)>0,true,false) from user_trains WHERE (t.id = trains AND users in (SELECT user FROM user_plan WHERE id = :userPlan))) = :isUserSelected)";

		String query1 = "SELECT COUNT(DISTINCT t.train_no)" + FROM;

		Query query1f = entityManager.createNativeQuery(query1);
		query1f.setParameter("trainNo", (trainNo != null) ? "%" + trainNo + "%" : null);
		query1f.setParameter("startDay", (startDay != null) ? startDay.ordinal() : null);
		query1f.setParameter("name", (name != null) ? "%" + name + "%" : null);
		query1f.setParameter("fromStation", fromStation);
		query1f.setParameter("toStation", toStation);
		query1f.setParameter("trainType", (trainType != null) ? "%" + trainType + "%" : null);
		if(passingStation1 !=null || passingStation2 != null){ 
			query1f.setParameter("passingStation1", passingStation1);
			query1f.setParameter("passingStation2", passingStation2);
		}
		query1f.setParameter("hasDrivingSection", hasDrivingSection);
		query1f.setParameter("isUserSelected", isUserSelected);
		query1f.setParameter("userPlan", userPlan);
		
		//query1f.setParameter("start", page * size);
		//query1f.setParameter("offset", size*10);
		
		Long totalElements = ((java.math.BigInteger) query1f.getSingleResult()).longValue();
		Long startIndex = page * size;
		Long totalPages = totalElements / size;
		Long currentPage = page;
		String baseItemRestUri = "/api/trains/";
		SelectViewModel result = new SelectViewModel(TrainByNumberVM.class,
				new SelectionDetails(totalElements, startIndex, currentPage, totalPages, baseItemRestUri), null);
		if (totalElements > 0) {

			if (sort != null && sort.isEmpty()) {
				sort = null;
			}

			String query2 = "SELECT t.id as id, t.name as name, t.start_day as startDay, t.train_no as trainNo,"
					+ " sf.code as fromStationCode, st.code as toStationCode, tt.name as trainType,"
					+ " COUNT(t.train_no) as noOfDaysTrainRuns,GROUP_CONCAT( CONVERT(t.start_day,CHAR(1)) SEPARATOR ';') as days,"
					+ " (SELECT IF(COUNT(ds.id) > 0, true, false) FROM driving_section as ds WHERE ds.user_plan = :userPlan AND ds.train = t.id) as hasDrivingSection,"
					+ " (SELECT IF(COUNT(trains)>0,true,false) from user_trains WHERE (t.id = trains AND users in (SELECT user FROM user_plan WHERE id = :userPlan))) as isUserSelected"
					+ FROM + " GROUP BY t.train_no " + " ORDER BY " + sort

			+ " LIMIT :start, :offset";
			Query query2f = entityManager.createNativeQuery(query2);
			query2f.setParameter("trainNo", (trainNo != null) ? "%" + trainNo + "%" : null);
			query2f.setParameter("startDay", (startDay != null) ? startDay.ordinal() : null);
			query2f.setParameter("name", (name != null) ? "%" + name + "%" : null);
			query2f.setParameter("fromStation", fromStation);
			query2f.setParameter("toStation", toStation);
			query2f.setParameter("trainType", (trainType != null) ? "%" + trainType + "%" : null);
			if(passingStation1 !=null || passingStation2 != null){
				query2f.setParameter("passingStation1", passingStation1);
				query2f.setParameter("passingStation2", passingStation2);
			}
			query2f.setParameter("hasDrivingSection", hasDrivingSection);
			query2f.setParameter("isUserSelected", isUserSelected);
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

	/**
	 * This is used to list the trains
	 */
	@Transactional(readOnly = true)
	@Override
	public SelectViewModel listTrains(Integer trainNo, Day startDay, String name, String fromStation, String toStation,
			String trainType, String passingStation1, String passingStation2, Boolean hasDrivingSection, Long userPlan,
			String sort, Long page, Long size) {
		String FROM = " FROM train as t LEFT JOIN station as sf ON (t.from_station = sf.id)"
				+ " LEFT JOIN station as st ON (t.to_station = st.id)"
				+ " LEFT JOIN train_type as tt ON (t.train_type = tt.id) "
				+ " INNER JOIN user_trains as ut on (t.id = ut.trains AND ut.users IN (SELECT user FROM user_plan WHERE id = :userPlan) )"
				+ " WHERE "
				+ " t.id IN ( SELECT DISTINCT ts.train FROM train_station as ts LEFT JOIN station as s  ON (ts.station = s.id)"
				+ "			   WHERE  (:passingStation1 IS NULL OR s.code = :passingStation1 ) " + " )" + " AND "
				+ " t.id IN ( SELECT DISTINCT ts.train FROM train_station as ts LEFT JOIN station as s ON (ts.station = s.id)"
				+ "			   WHERE (:passingStation2 IS NULL OR s.code = :passingStation2 ) " + " )"

		+ " AND (:trainNo IS NULL OR	CONVERT(t.train_no,CHAR(8)) LIKE :trainNo)"
				+ " AND (:startDay IS NULL OR t.start_day = :startDay) " + " AND (:name IS NULL OR t.name LIKE :name ) "
				+ " AND (:fromStation IS NULL OR sf.code = :fromStation ) "
				+ " AND (:toStation IS NULL OR st.code = :toStation )"
				+ " AND (:trainType IS NULL OR tt.name LIKE :trainType )"
				+ " AND (:hasDrivingSection IS NULL OR (SELECT IF(COUNT(ds.id) > 0, true, false) FROM driving_section as ds WHERE ds.user_plan = :userPlan AND ds.train = t.id) = :hasDrivingSection)";

		String query1 = "SELECT COUNT(t.id)" + FROM;

		Query query1f = entityManager.createNativeQuery(query1);
		query1f.setParameter("trainNo", trainNo);
		query1f.setParameter("startDay", (startDay != null) ? startDay.ordinal() : null);
		query1f.setParameter("name", (name != null) ? "%" + name + "%" : null);
		query1f.setParameter("fromStation", fromStation);
		query1f.setParameter("toStation", toStation);
		query1f.setParameter("trainType", (trainType != null) ? "%" + trainType + "%" : null);
		query1f.setParameter("passingStation1", passingStation1);
		query1f.setParameter("passingStation2", passingStation2);
		query1f.setParameter("hasDrivingSection", hasDrivingSection);
		query1f.setParameter("userPlan", userPlan);

		Long totalElements = ((java.math.BigInteger) query1f.getSingleResult()).longValue();
		Long startIndex = page * size;
		Long totalPages = totalElements / size;
		Long currentPage = page;
		String baseItemRestUri = "/api/trains/";
		SelectViewModel result = new SelectViewModel(TrainVM.class,
				new SelectionDetails(totalElements, startIndex, currentPage, totalPages, baseItemRestUri), null);
		if (totalElements > 0) {

			if (sort != null && sort.isEmpty()) {
				sort = null;
			}

			String query2 = "SELECT t.id as id, t.name as name, t.start_day as startDay, t.train_no as trainNo,"
					+ " sf.code as fromStationCode, st.code as toStationCode, tt.name as trainType,"
					+ " (SELECT IF(COUNT(ds.id) > 0, true, false) FROM driving_section as ds WHERE ds.user_plan = :userPlan AND ds.train = t.id) as hasDrivingSection"
					+ FROM + " ORDER BY " + sort

			+ " LIMIT :start, :offset";
			Query query2f = entityManager.createNativeQuery(query2);
			query2f.setParameter("trainNo", trainNo);
			query2f.setParameter("startDay", (startDay != null) ? startDay.ordinal() : null);
			query2f.setParameter("name", (name != null) ? "%" + name + "%" : null);
			query2f.setParameter("fromStation", fromStation);
			query2f.setParameter("toStation", toStation);
			query2f.setParameter("trainType", (trainType != null) ? "%" + trainType + "%" : null);
			query2f.setParameter("passingStation1", passingStation1);
			query2f.setParameter("passingStation2", passingStation2);
			query2f.setParameter("hasDrivingSection", hasDrivingSection);
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

	/**
	 * This is used to list the trains
	 */
	@Transactional(readOnly = true)
	@Override
	public SelectViewModel listTrainsWithOutUserPlan(Integer trainNo, Day startDay, String name, String fromStation,
			String toStation, String trainType, String passingStation1, String passingStation2, String sort, Long page,
			Long size) {
		String FROM = " FROM train as t LEFT JOIN station as sf ON (t.from_station = sf.id)"
				+ " LEFT JOIN station as st ON (t.to_station = st.id)"
				+ " LEFT JOIN train_type as tt ON (t.train_type = tt.id) " + " WHERE "
				+ " t.id IN ( SELECT DISTINCT ts.train FROM train_station as ts LEFT JOIN station as s  ON (ts.station = s.id)"
				+ "			   WHERE  (:passingStation1 IS NULL OR s.code = :passingStation1 ) " + " )" + " AND "
				+ " t.id IN ( SELECT DISTINCT ts.train FROM train_station as ts LEFT JOIN station as s ON (ts.station = s.id)"
				+ "			   WHERE (:passingStation2 IS NULL OR s.code = :passingStation2 ) " + " )"

		+ "AND (:trainNo IS NULL OR	CONVERT(t.train_no,CHAR(8)) LIKE :trainNo)"
				+ "AND (:startDay IS NULL OR t.start_day = :startDay) " + "AND (:name IS NULL OR t.name LIKE :name ) "
				+ "AND (:fromStation IS NULL OR sf.code = :fromStation ) "
				+ "AND (:toStation IS NULL OR st.code = :toStation )"
				+ "AND (:trainType IS NULL OR tt.name LIKE :trainType )";

		String query1 = "SELECT COUNT(t.id)" + FROM;

		Query query1f = entityManager.createNativeQuery(query1);
		query1f.setParameter("trainNo", trainNo);
		query1f.setParameter("startDay", (startDay != null) ? startDay.ordinal() : null);
		query1f.setParameter("name", (name != null) ? "%" + name + "%" : null);
		query1f.setParameter("fromStation", fromStation);
		query1f.setParameter("toStation", toStation);
		query1f.setParameter("trainType", (trainType != null) ? "%" + trainType + "%" : null);
		query1f.setParameter("passingStation1", passingStation1);
		query1f.setParameter("passingStation2", passingStation2);

		Long totalElements = ((java.math.BigInteger) query1f.getSingleResult()).longValue();
		Long startIndex = page * size;
		Long totalPages = totalElements / size;
		Long currentPage = page;
		String baseItemRestUri = "/api/trains/";
		SelectViewModel result = new SelectViewModel(TrainVM.class,
				new SelectionDetails(totalElements, startIndex, currentPage, totalPages, baseItemRestUri), null);
		if (totalElements > 0) {

			if (sort != null && sort.isEmpty()) {
				sort = null;
			}

			String query2 = "SELECT t.id as id, t.name as name, t.start_day as startDay, t.train_no as trainNo,"
					+ " sf.code as fromStationCode, st.code as toStationCode, tt.name as trainType" + FROM
					+ " ORDER BY " + sort

			+ " LIMIT :start, :offset";
			Query query2f = entityManager.createNativeQuery(query2);
			query2f.setParameter("trainNo", trainNo);
			query2f.setParameter("startDay", (startDay != null) ? startDay.ordinal() : null);
			query2f.setParameter("name", (name != null) ? "%" + name + "%" : null);
			query2f.setParameter("fromStation", fromStation);
			query2f.setParameter("toStation", toStation);
			query2f.setParameter("trainType", (trainType != null) ? "%" + trainType + "%" : null);
			query2f.setParameter("passingStation1", passingStation1);
			query2f.setParameter("passingStation2", passingStation2);

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

	/**
	 * This is used to save trains
	 */
	@Override
	public ProcessResult saveTrain(Integer trainNo,  String name, String fromStation,
			 String toStation, String startDay, String trainType) {
		ProcessResult result = null;

		// Create stored procedure query for create train
		// Register the parameters required for this stored procedure

		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("CreateTrain");
		storedProcedure.registerStoredProcedureParameter("trainNo", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("name", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("fromStation", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("toStation", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("startDay", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("trainType", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("createdTrainId", Long.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("result", Boolean.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("errorMessage", String.class, ParameterMode.OUT);
		// Set the parameter value
		storedProcedure.setParameter("trainNo", trainNo);
		storedProcedure.setParameter("name", name);
		storedProcedure.setParameter("fromStation", fromStation);
		storedProcedure.setParameter("toStation", toStation);
		storedProcedure.setParameter("startDay", startDay);
		storedProcedure.setParameter("trainType", trainType);
		// call the stored procedure
		storedProcedure.execute();
		// Fetch the result and errorMessage output parameter of the stored
		// procedure
		Boolean resultB = (Boolean) storedProcedure.getOutputParameterValue("result");
		String errorMessage = storedProcedure.getOutputParameterValue("errorMessage").toString();
		if (resultB) {
			Long outputValue = (Long) storedProcedure.getOutputParameterValue("createdTrainId");
			result = new ProcessResult(resultB, errorMessage, outputValue.toString());
		} else {
			result = new ProcessResult(resultB, errorMessage);
		}

		return result;
	}

	@Transactional(readOnly = false)
	@Override
	public ProcessResult updateTrainInUserList(Integer trainNo, Boolean isUserSelected, Long userPlan) {
		ProcessResult resultO = null;

		// Create stored procedure query for creating driving section for single
		// train for single day
		// register the parameter types to match the db stored procedure
		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("UpdateTrainInUserList");
		storedProcedure.registerStoredProcedureParameter("trainNo", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("isUserSelected", Boolean.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("userPlan", Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("result", Boolean.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("errorMessage", String.class, ParameterMode.OUT);
		// set the parameter values
		storedProcedure.setParameter("trainNo", trainNo);
		storedProcedure.setParameter("isUserSelected", isUserSelected);
		storedProcedure.setParameter("userPlan", userPlan);
		// call the stored procedure
		storedProcedure.execute();

		Boolean result = (Boolean) storedProcedure.getOutputParameterValue("result");
		String errorMessage = storedProcedure.getOutputParameterValue("errorMessage").toString();

		resultO = new ProcessResult(result, errorMessage);

		return resultO;
	}

	@Override
	public SelectViewModel listTrainsByPilots(String fromStation, String toStation, Day departureDayI, Day arrivalDayI,
			String departureTime, String arrivalTime, String sort, Long page, Long size) {

		String FROM = " FROM train as t LEFT JOIN station as sf ON (t.from_station = sf.id)"
				+ " LEFT JOIN station as st ON (t.to_station = st.id)"
				+ " LEFT JOIN train_type as tt ON (t.train_type = tt.id)"
				+ " LEFT JOIN train_station as fts ON (fts.train = t.id)"
				+ " LEFT JOIN train_station as tts ON (tts.train  =  t.id)"
				+ " LEFT JOIN station as sfts ON ( sfts.id = fts.station) "
				+ " LEFT JOIN station as stts ON ( stts.id = tts.station)" 
				+ " WHERE  fts.stop_number < tts.stop_number"
				+ " AND (:fromStation IS NULL OR fts.station IN (SELECT id FROM station WHERE code = :fromStation ) )"
				+ " AND (:toStation IS NULL OR tts.station IN (SELECT id FROM station WHERE code = :toStation ))"
//				+ " AND (:departureDay IS NULL OR fts.day = :departureDay)"
//				+ " AND (:departureTime IS NULL OR fts.departure > CAST(IFNULL(:departureTime,'0:00') as TIME) )"
//				+ " AND (:arrivalDay IS NULL OR tts.day = :arrivalDay)"
//				+ " AND (:arrivalTime IS NULL OR tts.arrival < CAST(IFNULL(:arrivalTime,'0:00') as TIME))"
				;

		String query1 = "SELECT COUNT(DISTINCT t.train_no)" + FROM;

		Query query1f = entityManager.createNativeQuery(query1);

		query1f.setParameter("fromStation", fromStation);
		query1f.setParameter("toStation", toStation);
		
//		query1f.setParameter("departureDay", (departureDayI!=null)?departureDayI.ordinal():null);
//		query1f.setParameter("departureTime", departureTime);
//		query1f.setParameter("arrivalDay", (arrivalDayI!=null)?arrivalDayI.ordinal():null);
//		query1f.setParameter("arrivalTime", arrivalTime);

		Long totalElements = ((java.math.BigInteger) query1f.getSingleResult()).longValue();
		Long startIndex = page * size;
		Long totalPages = totalElements / size;
		Long currentPage = page;
		String baseItemRestUri = "/api/pilotTrains/";
		SelectViewModel result = new SelectViewModel(PilotTrainVM.class,
				new SelectionDetails(totalElements, startIndex, currentPage, totalPages, baseItemRestUri), null);
		if (totalElements > 0) {

			if (sort != null && sort.isEmpty()) {
				sort = null;
			}

			String query2 = "SELECT t.id as id, "
					+ " t.train_no as trainNo,"
					+ " (SELECT GROUP_CONCAT( CONVERT(start_day,CHAR(1)) SEPARATOR ';') FROM train WHERE train_no=t.train_no) AS days," 
					+ " t.name as name, "
					+ " t.start_day as startDay,"
					+ " sf.code as originatingStation, "
					+ " st.code as destinationStation, "
					+ " tt.name as trainType,"
					+ " sfts.code as fromStation,"
					+ " fts.day as departureDay,"
					+ " fts.departure as departureTime," 
					+ " stts.code as toStation,"
					+ " tts.day as arrivalDay,"
					+ " tts.arrival as arrivalTime,"
					+ " (tts.distance - fts.distance) as distance,"
					+ " (tts.journey_duration - fts.journey_duration) as duration"
					+ FROM
					+ " ORDER BY " + sort

			+ " LIMIT :start, :offset";
			Query query2f = entityManager.createNativeQuery(query2);
			query2f.setParameter("fromStation", fromStation);
			query2f.setParameter("toStation", toStation);
		
//			query2f.setParameter("departureDay", (departureDayI!=null)?departureDayI.ordinal():null);
//			query2f.setParameter("departureTime", departureTime);
//			query2f.setParameter("arrivalDay", (arrivalDayI!=null)?arrivalDayI.ordinal():null);
//			query2f.setParameter("arrivalTime", arrivalTime);

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
}