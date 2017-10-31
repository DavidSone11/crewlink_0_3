package com.mathologic.projects.crewlink.custom.repositories;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mathologic.projects.crewlink.custom.models.CrewLinkStationSummaryVM;
import com.mathologic.projects.crewlink.custom.models.CrewLinkVM;
import com.mathologic.projects.crewlink.custom.models.ImprovementsRoundTripWithExcessOSRVM;
import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.models.SelectionDetails;
import com.mathologic.projects.crewlink.custom.models.CrewLinkDrivingSectionsVM;
import com.mathologic.projects.crewlink.models.Day;
/**
 * This repository contains implementations of list crew links, 
 * list driving sections, and save crew links .
 * 
 * @author Vivek Yadav,Laxman
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
@Repository
public class CrewLinkVMRepositoryImpl implements CrewLinkVMRepository {
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * This is used to list crew links. By setting the parameters to the query.
	 */
	@Transactional(readOnly = true)
	@Override
	public SelectViewModel listCrewLinks(String userPlan, String linkName,
			Integer locoPilots, String station, Long minDuration,
			Long maxDuration, Long minDistance, Long maxDistance,
			Day departureDay, String minDepartureTime, String maxDepartureTime,
			Day arrivalDay, String minArrivalTime, String maxArrivalTime,
			String sort, Long page, Long size) {
		String FROM = " FROM crew_link AS cl LEFT JOIN station AS s ON (cl.station = s.id) "
				+ " LEFT JOIN crew_type as ct ON ( cl.crew_type = ct.id)"
				+ " WHERE "
				+ " cl.user_plan= :userPlan "
				+ " AND ( :linkName IS NULL OR cl.link_name LIKE :linkName ) "
				+ " AND ( :locoPilots IS NULL OR cl.no_of_lp = :locoPilots ) "
				+ " AND ( :station IS NULL OR s.code = :station ) "
				+ " AND ( :departureDay IS NULL OR cl.start_day = :departureDay ) "
				+ " AND ( :arrivalDay IS NULL OR cl.end_day = :arrivalDay ) "
				+ " AND ( :minDepartureTime IS NULL OR CAST(IFNULL(:minDepartureTime,'0:00') as TIME) <= cl.start_time ) "
				+ " AND ( :maxDepartureTime IS NULL OR CAST(IFNULL(:maxDepartureTime,'0:00') as TIME) >= cl.start_time ) "
				+ " AND ( :minArrivalTime IS NULL OR CAST(IFNULL(:minArrivalTime,'0:00') as TIME) <= cl.end_time ) "
				+ " AND ( :maxArrivalTime IS NULL OR CAST(IFNULL(:maxArrivalTime,'0:00') as TIME) >= cl.end_time ) "
				+ " AND ( :minDuration IS NULL OR :minDuration <= cl.duration ) "
				+ " AND ( :maxDuration IS NULL OR :maxDuration >= cl.duration ) "
				+ " AND ( :minDistance IS NULL OR :minDistance <= cl.distance ) "
				+ " AND ( :maxDistance IS NULL OR :maxDistance >= cl.distance )";
		String query1 = "SELECT COUNT(cl.id) "
				+ FROM;

		Query query1f = entityManager.createNativeQuery(query1);
		query1f.setParameter("userPlan", userPlan);
		query1f.setParameter("linkName", (linkName != null) ? "%" + linkName
				+ "%" : null);
		query1f.setParameter("locoPilots", locoPilots);
		query1f.setParameter("station", ((station!=null)?((station.isEmpty())?null:station):null));
		query1f.setParameter("departureDay",
				(departureDay != null) ? departureDay.ordinal() : null);
		query1f.setParameter("arrivalDay",
				(arrivalDay != null) ? arrivalDay.ordinal() : null);
		query1f.setParameter("minDepartureTime", minDepartureTime);
		query1f.setParameter("maxDepartureTime", maxDepartureTime);
		query1f.setParameter("minArrivalTime", minArrivalTime);
		query1f.setParameter("maxArrivalTime", maxArrivalTime);
		query1f.setParameter("minDuration", minDuration);
		query1f.setParameter("maxDuration", maxDuration);
		query1f.setParameter("minDistance", minDistance);
		query1f.setParameter("maxDistance", maxDistance);

		//find out the total elements and displays only those records according to page size.
		Long totalElements = ((java.math.BigInteger) query1f.getSingleResult())
				.longValue();
		Long startIndex = page * size;
		Long totalPages = totalElements / size;
		Long currentPage = page;
		String baseItemRestUri = "/api/crewLinks/";
		// Selection details contains the data of the class and fields with the positions.
		SelectViewModel result = new SelectViewModel(CrewLinkVM.class,
				new SelectionDetails(totalElements, startIndex, currentPage,
						totalPages, baseItemRestUri), null);
		// fetch the required fields from the table
		if (totalElements > 0) {

			if (sort != null && sort.isEmpty()) {
				sort = null;
			}

			String query2 = "SELECT cl.id,cl.link_name AS linkName,cl.no_of_lp AS locoPilots, cl.duration,"
					+ " cl.total_head_station_rest_time AS hqRest,cl.total_out_station_rest_time AS osRest,"
					+ " s.code AS station, cl.start_time AS departureTime,cl.start_day AS departureDay,"
					+ " cl.end_time AS arrivalTime,cl.end_day AS arrivalDay, cl.distance, ct.name,"
					+ " (SELECT COUNT(id) FROM round_trip WHERE crew_link = cl.id) as noOfRoundTrips"
					+ FROM 

					+ " ORDER BY " + sort + " LIMIT :start, :offset";
			Query query2f = entityManager.createNativeQuery(query2);
			query2f.setParameter("userPlan", userPlan);
			query2f.setParameter("linkName", (linkName != null) ? "%"
					+ linkName + "%" : null);
			query2f.setParameter("locoPilots", locoPilots);
			query2f.setParameter("station", ((station!=null)?((station.isEmpty())?null:station):null));
			query2f.setParameter("departureDay",
					(departureDay != null) ? departureDay.ordinal() : null);
			query2f.setParameter("arrivalDay",
					(arrivalDay != null) ? arrivalDay.ordinal() : null);
			query2f.setParameter("minDepartureTime", minDepartureTime);
			query2f.setParameter("maxDepartureTime", maxDepartureTime);
			query2f.setParameter("minArrivalTime", minArrivalTime);
			query2f.setParameter("maxArrivalTime", maxArrivalTime);
			query2f.setParameter("minDuration", minDuration);
			query2f.setParameter("maxDuration", maxDuration);
			query2f.setParameter("minDistance", minDistance);
			query2f.setParameter("maxDistance", maxDistance);

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
	 * This is used to list driving sections of the crew link
	 */
	@Transactional(readOnly = true)
	@Override
	public SelectViewModel listDrivingSections(String userPlan, String crewLink,String sort, Long page, Long size) {
		String FROM = " FROM round_trip as rt LEFT JOIN driving_duty as dd ON (dd.round_trip = rt.id)"
				+ " LEFT JOIN driving_duty_element as dde ON (dde.driving_duty = dd.id)"
				+ " LEFT JOIN driving_section as ds ON (ds.driving_duty_element = dde.id)"
				+ " LEFT JOIN pilot_trip as sp ON (sp.id = dde.start_pilot_trip)"
				+ " LEFT JOIN pilot_trip as ep ON (ep.id = dde.end_pilot_trip)"
				+ " LEFT JOIN station as fssp ON (fssp.id = sp.from_station)"
				+ " LEFT JOIN station as tssp ON (tssp.id = sp.to_station)"
				+ " LEFT JOIN station as fsep ON (fsep.id = ep.from_station)"
				+ " LEFT JOIN station as tsep ON (tsep.id = ep.to_station)"
				+ " LEFT JOIN train as t ON (t.id = ds.train)"
				+ " LEFT JOIN station as fsds ON (fsds.id = ds.from_station)"
				+ " LEFT JOIN station as tsds ON (tsds.id = ds.to_station)"
				+ " WHERE"
				+ " rt.crew_link = :crewLink "
				+ " AND rt.user_plan= :userPlan "
				+ " ORDER BY rt.crew_link_order_no ASC, dd.round_trip_order_no ASC, dde.driving_duty_order_no ASC ";
		
		String query1 = "SELECT COUNT(ds.id) "
				+ FROM;

		Query query1f = entityManager.createNativeQuery(query1);
		query1f.setParameter("userPlan", userPlan);
		query1f.setParameter("crewLink", crewLink);

		Long totalElements = ((java.math.BigInteger) query1f.getSingleResult())
				.longValue();
		Long startIndex = page * size;
		Long totalPages = totalElements / size;
		Long currentPage = page;
		String baseItemRestUri = "/api/crewLinks/";
		SelectViewModel result = new SelectViewModel(CrewLinkDrivingSectionsVM.class,
				new SelectionDetails(totalElements, startIndex, currentPage,
						totalPages, baseItemRestUri), null);
		if (totalElements > 0) {

			if (sort != null && sort.isEmpty()) {
				sort = null;
			}

			String query2 = "SELECT rt.crew_link_order_no as rtOrder,"
					+ " rt.start_time as rtSignOn,"
					+ " rt.start_day as rtSignOnDay,"
					+ " dd.round_trip_order_no as ddOrder,"
					+ " dd.start_time as ddSignOn,"
					+ " dd.start_day as ddSignOnDay,"
					+ " dd.duration as ddDuration,"
					+ " dde.driving_duty_order_no as ddeOrder,"
					+ " dde.start_time as ddeStartTime,"
					+ " dde.start_day as ddeStartDay,"
					+ " sp.name as startPilot,"
					+ " fssp.code as spStation,"
					+ " (sp.distance/2) as spDistance,"
					+ " tssp.code as spToStation,"
					+ " sp.end_day as spArrivalDay,"
					+ " sp.end_time as spArrivalTime,"
					+ " fsds.code as dsFrom,"
					+ " t.train_no as dsTrain,"
					+ " t.start_day as dsTrainOriginDay,"
					+ " ds.distance as dsDistance,"
					+ " tsds.code as dsTo,"
					+ " ds.start_day as dsDepartureDay,"
					+ " ds.start_time as dsDepartureTime,"
					+ " ds.end_day as dsArrivalDay,"
					+ " ds.end_time as dsArrivalTime,"
					+ " ep.name as endPilot,"
					+ " tsep.code as epStation,"
					+ " (ep.distance/2) as epDistance,"
					+ " fsep.code as epFromStation,"
					+ " ep.start_day as epDepartureDay,"
					+ " ep.start_time as epDepartureTime,"
					+ " dde.end_time as ddeEndTime,"
					+ " dde.end_day as ddeEndDay,"
					+ " dd.end_time as ddSignOff,"
					+ " dd.end_day as ddSignOffDay,"
					+ " rt.end_time as rtSignOff,"
					+ " rt.end_day as rtSignOffDay,"
					+ " rt.total_out_station_rest_time as rtOTRest"
					
					+ FROM
					+ " LIMIT 1000";
			Query query2f = entityManager.createNativeQuery(query2);
			query2f.setParameter("userPlan", userPlan);
			query2f.setParameter("crewLink", crewLink);
			try {
				result.setData(query2f.getResultList());
			} catch (Exception ex) {
				System.out.println("ERROR in QUERY: " + ex.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * This is used to save the crewlink 
	 */

	@Transactional(readOnly = false)
	@Override
	public ProcessResult save(Integer crewLinkId,
			String crewLinkName, Long userPlan) {

		ProcessResult resultO = null;
		//Create stored procedure query for creating crew links
		// register the parameter types to match the db stored procedure
		StoredProcedureQuery storedProcedure = entityManager
				.createStoredProcedureQuery("CreateCrewLink");
		storedProcedure.registerStoredProcedureParameter("crewLinkId",
				Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("crewLinkName",
				String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("userPlan",
				Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("createdCrewLink",
				Long.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("result",
				Boolean.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("errorMessage",
				String.class, ParameterMode.OUT);

		//set the parameter values
		storedProcedure.setParameter("crewLinkId", crewLinkId);
		storedProcedure.setParameter("crewLinkName", crewLinkName);
		storedProcedure.setParameter("userPlan", userPlan);

		// call the stored procedure
		storedProcedure.execute();
		
		Boolean result = (Boolean) storedProcedure
				.getOutputParameterValue("result");
		String errorMessage = storedProcedure.getOutputParameterValue(
				"errorMessage").toString();
		if (result) {
			Long outputValue = (Long) storedProcedure
					.getOutputParameterValue("createdCrewLink");
			resultO = new ProcessResult(result, errorMessage,
					outputValue.toString());
		} else {
			resultO = new ProcessResult(result, errorMessage);
		}

		return resultO;

	}


	@Override
	public SelectViewModel stationSummary(Long userPlan) {
		SelectViewModel result;
		String query2 = "SELECT s.code as station, SUM(cl.no_of_lp) as noOfLP,SUM(rtd.noRT) as noOfRT, SUM(cl.distance) as totalDistance, SUM(rtd.pilotKM) as pilotDistance, (SUM(cl.distance)-SUM(rtd.pilotKM)) as trainDistance, SUM(cl.duration) as dutyTime, ((SUM(cl.duration)/SUM(cl.no_of_lp))*2) as dutyTimePer14Days, SUM(cl.total_out_station_rest_time) as OSR,((SUM(cl.total_out_station_rest_time)/SUM(cl.no_of_lp))*2) as OSRPer14Days, SUM(cl.total_head_station_rest_time) as HQR, ((SUM(cl.total_head_station_rest_time)/SUM(cl.no_of_lp))*2) as HQRPer14Days, (SUM(cl.total_head_station_rest_time)/SUM(cl.no_of_lp)) as avgHQR  " + 
				"FROM crew_link as cl  " + 
				"LEFT JOIN station as s ON (s.id = cl.station)  " + 
				"LEFT JOIN (  " + 
				"	SELECT rt1.crew_link as clID,COUNT(*) noRT, SUM(IFNULL(sp.distance,0) + IFNULL(ep.distance,0)) as pilotKM  " + 
				"	FROM round_trip as rt1  " + 
				"	LEFT JOIN driving_duty as dd ON ( dd.round_trip = rt1.id)  " + 
				"	LEFT JOIN driving_duty_element as dde ON ( dde.driving_duty = dd.id )  " + 
				"	LEFT JOIN pilot_trip as sp ON (sp.id = dde.start_pilot_trip)  " + 
				"	LEFT JOIN pilot_trip as ep ON (ep.id = dde.end_pilot_trip )  " + 
				"	LEFT JOIN driving_section as ds ON (ds.driving_duty_element = dd.id)  " + 
				"	GROUP BY rt1.crew_link  " + 
				") as rtd ON (rtd.clID = cl.id)  " + 
				"WHERE cl.user_plan = :userPlan  " + 
				"GROUP BY s.code  " + 
				"ORDER BY s.code";
		
		Query query2f = entityManager.createNativeQuery(query2);
		query2f.setParameter("userPlan", userPlan);
		result = new SelectViewModel(CrewLinkStationSummaryVM.class,
				new SelectionDetails(0L, 0L, 0L,0L, ""), null);
		try {
			result.setData(query2f.getResultList());
		} catch (Exception ex) {
			System.out.println("ERROR in QUERY: " + ex.getMessage());
		}
	
		return result;	
	}
}
