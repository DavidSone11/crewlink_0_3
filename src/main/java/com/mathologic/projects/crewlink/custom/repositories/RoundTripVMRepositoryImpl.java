package com.mathologic.projects.crewlink.custom.repositories;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

import com.mathologic.projects.crewlink.custom.models.DrivingDutyVM;
import com.mathologic.projects.crewlink.custom.models.ProcessResult;
import com.mathologic.projects.crewlink.custom.models.RoundTripManyDutiesPM;
import com.mathologic.projects.crewlink.custom.models.RoundTripPM;
import com.mathologic.projects.crewlink.custom.models.RoundTripVM;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.models.SelectionDetails;
import com.mathologic.projects.crewlink.models.Day;
import com.mathologic.projects.crewlink.utils.converters.ResultListToObject;

/**
 * This repository contains implementations of list round trips, save round
 * trips
 * 
 * @author Jagdish
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
@Repository
public class RoundTripVMRepositoryImpl implements RoundTripVMRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private DrivingDutyVMRepository drivingDutyVMRepository;

	/**
	 * This is used to list round trips
	 */
	@Override
	@Transactional(readOnly = true)
	public SelectViewModel listRoundTrips(Long userPlan, String rtName,
			String station, Long minDuration, Long maxDuration,
			Long minDistance, Long maxDistance, Day departureDay,
			String minDepartureTime, String maxDepartureTime,
			String minArrivalTime, String maxArrivalTime, Day arrivalDay,
			Long lastDrivingDutyDuration, String minAvailableTime,
			String maxAvailableTime, Day availableDay, Long crewType,
			Boolean isCrewLink, Boolean isIgnore, Long totalOutStationRestTime,
			String sort, Long page, Long size, Long crewLinkId,String crewLinkName,String os) {

		String FROM = " FROM  round_trip AS rt LEFT JOIN station AS st ON (rt.station = st.id)"
				+ " LEFT JOIN crew_type as ct ON (ct.id = rt.crew_type)"
				+ " LEFT JOIN crew_link as cl ON (cl.id = rt.crew_link)"
				+ " LEFT JOIN ("
				+ "		SELECT rt1.id as id, GROUP_CONCAT(ddfs.code) as os" 
				+ "		FROM round_trip as rt1 LEFT JOIN driving_duty as dd ON (dd.round_trip = rt1.id AND  dd.round_trip_order_no != 1)"  
				+ "		LEFT JOIN station as ddfs ON (dd.from_station = ddfs.id)" 
				+ "		GROUP BY rt1.id" 
				+ " ) as ost ON (ost.id = rt.id)"
				+ " WHERE rt.user_plan = :userPlan"
				+ " AND ( :rtName IS NULL OR rt.rt_name LIKE :rtName)"
				+ " AND ( :station IS NULL OR st.code = :station)"
				+ " AND ( :departureDay IS NULL OR rt.start_day = :departureDay) "
				+ " AND ( :minDepartureTime IS NULL OR CAST(IFNULL(:minDepartureTime,'0:00') as TIME) <= rt.start_time) "
				+ " AND ( :maxDepartureTime IS NULL OR CAST(IFNULL(:maxDepartureTime,'0:00') as TIME) >= rt.start_time) "
				+ " AND ( :minArrivalTime IS NULL OR CAST(IFNULL(:minArrivalTime,'0:00') as TIME) <= rt.end_time) "
				+ " AND ( :maxArrivalTime IS NULL OR CAST(IFNULL(:maxArrivalTime,'0:00') as TIME) >= rt.end_time) "
				+ " AND ( :arrivalDay IS NULL OR rt.end_day = :arrivalDay) "
				+ " AND ( :minAvailableTime IS NULL OR CAST(IFNULL(:minAvailableTime,'0:00') as TIME) <= rt.next_available_time) "
				+ " AND ( :maxAvailableTime IS NULL OR CAST(IFNULL(:maxDepartureTime,'0:00') as TIME) >= rt.next_available_time) "
				+ " AND ( :availableDay IS NULL OR rt.next_available_day = :availableDay) "
				+ " AND ( :minDuration IS NULL OR :minDuration <= rt.duration) "
				+ " AND ( :maxDuration IS NULL OR :maxDuration >= rt.duration) "
				+ " AND ( :minDistance IS NULL OR :minDistance <= rt.distance) "
				+ " AND ( :maxDistance IS NULL OR :maxDistance >= rt.distance) "
				+ " AND ( :totalOutStationRestTime IS NULL OR rt.total_Out_Station_Rest_Time =:totalOutStationRestTime)"
				+ " AND ( :lastDrivingDutyDuration IS NULL OR rt.last_driving_duty_duration = :lastDrivingDutyDuration) "
				+ " AND ( :crewType IS NULL OR rt.crew_type = :crewType) "
				+ " AND ( :isCrewLink IS NULL OR ((rt.crew_link IS NOT NULL) = :isCrewLink) )"
				+ " AND ( :isIgnore IS NULL OR rt.is_ignore = :isIgnore )"
				+ " AND ( :crewLinkId IS NULL OR rt.crew_link = :crewLinkId) "
				+ " AND ( :crewLinkName IS NULL OR cl.link_name LIKE :crewLinkName)"
				+ " AND ( :os IS NULL OR ost.os LIKE :os )";
				

		String query1 = "SELECT COUNT(DISTINCT rt.id)" + FROM;

		Query query1f = entityManager.createNativeQuery(query1);
		query1f.setParameter("userPlan", userPlan);
		query1f.setParameter("rtName",
				(rtName != null) ? (rtName.isEmpty() ? null : "%" + rtName
						+ "%") : null);
		query1f.setParameter("station",
				((station != null) ? ((station.isEmpty()) ? null : station)
						: null));
		query1f.setParameter("departureDay",
				(departureDay != null) ? departureDay.ordinal() : null);
		query1f.setParameter("minDepartureTime", minDepartureTime);
		query1f.setParameter("maxDepartureTime", maxDepartureTime);
		query1f.setParameter("minArrivalTime", minArrivalTime);
		query1f.setParameter("maxArrivalTime", maxArrivalTime);
		query1f.setParameter("arrivalDay",
				(arrivalDay != null) ? arrivalDay.ordinal() : null);
		query1f.setParameter("minAvailableTime", minAvailableTime);
		query1f.setParameter("maxAvailableTime", maxAvailableTime);
		query1f.setParameter("availableDay",
				(availableDay != null) ? availableDay.ordinal() : null);
		query1f.setParameter("lastDrivingDutyDuration", lastDrivingDutyDuration);
		query1f.setParameter("crewType",(crewType!=null)?crewType:null);
		query1f.setParameter("minDuration", minDuration);
		query1f.setParameter("maxDuration", maxDuration);
		query1f.setParameter("minDistance", minDistance);
		query1f.setParameter("maxDistance", maxDistance);
		query1f.setParameter("totalOutStationRestTime", totalOutStationRestTime);
		query1f.setParameter("isCrewLink", isCrewLink);
		query1f.setParameter("isIgnore", isIgnore);
		query1f.setParameter("crewLinkId", crewLinkId);
		query1f.setParameter("crewLinkName",
				(crewLinkName != null) ? (crewLinkName.isEmpty() ? null : "%" + crewLinkName
						+ "%") : null);
		query1f.setParameter("os",
				(os != null) ? (os.isEmpty() ? null : "%" + os
						+ "%") : null);

		Long totalElements = ((java.math.BigInteger) query1f.getSingleResult())
				.longValue();
		Long startIndex = page * size;
		Long totalPages = totalElements / size;
		Long currentPage = page;
		String baseItemRestUri = "/api/roundTrips/";
		SelectViewModel result = new SelectViewModel(RoundTripVM.class,
				new SelectionDetails(totalElements, startIndex, currentPage,
						totalPages, baseItemRestUri), null);
		if (totalElements > 0) {

			if (sort != null && sort.isEmpty()) {
				sort = null;
			}

			String query2 = "SELECT rt.id as id,"
					+ " rt.rt_name as rtName ,"
					+ " rt.start_time as departureTime,"
					+ " rt.start_day as departureDay," + " st.code as station,"
					+ " rt.end_time as arrivalTime, "
					+ " rt.end_day as arrivalDay,"
					+ " rt.next_available_time as availableTime, "
					+ " rt.next_available_day as availableDay, "
					+ " rt.duration as duration," + " rt.distance as distance,"
					+ " rt.total_Out_Station_Rest_Time as totalOutStationRestTime, "
					+ " rt.last_driving_duty_duration as lastDutyDuration,"
					+ " rt.crew_type as crewType,"
					+ " IF(rt.crew_link is NULL,FALSE,TRUE) as isCrewLink,"
					+ " rt.is_ignore as isIgnore,"
					+ " rt.crew_link_order_no as crewLinkOrderNo,"
					+ " ct.name as crewTypeName,"
					+ " cl.link_name as crewLinkName,"
					+ " ost.os as os" + FROM

					+ " ORDER BY " + sort + " LIMIT :start, :offset";
			Query query2f = entityManager.createNativeQuery(query2);
			query2f.setParameter("userPlan", userPlan);
			query2f.setParameter("rtName",
					(rtName != null) ? (rtName.isEmpty() ? null : "%" + rtName
							+ "%") : null);
			query2f.setParameter("station",
					((station != null) ? ((station.isEmpty()) ? null : station)
							: null));
			query2f.setParameter("departureDay",
					(departureDay != null) ? departureDay.ordinal() : null);
			query2f.setParameter("minDepartureTime", minDepartureTime);
			query2f.setParameter("maxDepartureTime", maxDepartureTime);
			query2f.setParameter("minArrivalTime", minArrivalTime);
			query2f.setParameter("maxArrivalTime", maxArrivalTime);
			query2f.setParameter("arrivalDay",
					(arrivalDay != null) ? arrivalDay.ordinal() : null);
			query2f.setParameter("minAvailableTime", minAvailableTime);
			query2f.setParameter("maxAvailableTime", maxAvailableTime);
			query2f.setParameter("availableDay",
					(availableDay != null) ? availableDay.ordinal() : null);
			query2f.setParameter("lastDrivingDutyDuration",
					lastDrivingDutyDuration);
			query2f.setParameter("crewType",(crewType!=null)?crewType:null);
			query2f.setParameter("isCrewLink", isCrewLink);
			query2f.setParameter("isIgnore", isIgnore);
			query2f.setParameter("minDuration", minDuration);
			query2f.setParameter("maxDuration", maxDuration);
			query2f.setParameter("minDistance", minDistance);
			query2f.setParameter("maxDistance", maxDistance);
			query2f.setParameter("totalOutStationRestTime",
					totalOutStationRestTime);
			query2f.setParameter("crewLinkId", crewLinkId);
			query2f.setParameter("crewLinkName",
					(crewLinkName != null) ? (crewLinkName.isEmpty() ? null : "%" + crewLinkName
							+ "%") : null);
			query2f.setParameter("os",
					(os != null) ? (os.isEmpty() ? null : "%" + os
							+ "%") : null);
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
	 * This is used to save round trips
	 */
	@Transactional(readOnly = false)
	@Override
	public ProcessResult saveSingleRoundTrip(String drivingDutyIds,
			String orderNos, Long crewType, Long userPlan) {

		ProcessResult resultO = null;

		// Create stored procedure query for save round trips
		// Register the parameters required for this stored procedure
		StoredProcedureQuery storedProcedure = entityManager
				.createStoredProcedureQuery("CreateRoundTrip");
		storedProcedure.registerStoredProcedureParameter("drivingDutyIds",
				String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("orderNos",
				String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("crewType",
				Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("userPlan",
				Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("createdRoundTrip",
				Long.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("result",
				Boolean.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("errorMessage",
				String.class, ParameterMode.OUT);
		// set the parameter value
		storedProcedure.setParameter("drivingDutyIds", drivingDutyIds);
		storedProcedure.setParameter("orderNos", orderNos);
		storedProcedure.setParameter("crewType", crewType);
		storedProcedure.setParameter("userPlan", userPlan);
		// call the stored procedure
		storedProcedure.execute();
		// Fetch the result and errorMessage output parameter
		Boolean result = (Boolean) storedProcedure
				.getOutputParameterValue("result");
		String errorMessage = storedProcedure.getOutputParameterValue(
				"errorMessage").toString();
		if (result) {
			Long outputValue = (Long) storedProcedure
					.getOutputParameterValue("createdRoundTrip");
			resultO = new ProcessResult(result, errorMessage,
					outputValue.toString());
		} else {
			resultO = new ProcessResult(result, errorMessage);
		}
		return resultO;

	}

	@Transactional(readOnly = false)
	@Override
	public ProcessResult updateRoundTripInUserList(Long id, Boolean isIgnore,
			Long userPlan) {

		ProcessResult result = null;
		String query1 = "UPDATE round_trip SET is_ignore = :isIgnore WHERE id = :id AND user_plan = :userPlan";

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
	
	@Override
	public ProcessResult saveManyList(List<RoundTripManyDutiesPM> data, Long userPlan) {
		ProcessResult result = new ProcessResult(false,"");
		
		for(RoundTripManyDutiesPM roundTrip : data){
			String dutyIdList = "";
			String orderNos = "";
			int index = 1;
			for(DrivingDutyVM duty : roundTrip.getDrivingDuties()){
				dutyIdList += duty.getId()+",";
				orderNos += index+",";
				index++;
			}
			dutyIdList = dutyIdList.substring(0, dutyIdList.length()-1);
			orderNos = orderNos.substring(0,orderNos.length()-1);
			ProcessResult partResult = saveSingleRoundTrip(
					dutyIdList, orderNos, roundTrip.getCrewType(), userPlan);
			if (partResult.getResult()) {
				result.setOutputValue(result.getOutputValue()+partResult.getOutputValue() + " Saved , ");
			} else {
				result.setErrorMessage(result.getErrorMessage()+partResult.getErrorMessage() +" , ");
			}
		}
		if(result.getErrorMessage().length() == 0){
			result.setResult(true);
		}
	
		return result;
	}

	@Override
	public ProcessResult saveManyMatrix(List<RoundTripPM> data, Long userPlan) {
		DateTimeFormatter formatter = DateTimeFormatter
				.ofPattern("dd/MM/yyyy HH:mm:ss");
		DateTimeFormatter formatter2 = DateTimeFormatter
				.ofPattern("dd/MM/yyyy H:mm");
		for (RoundTripPM roundTrip : data) {
			Long crewType = roundTrip.getCrewType();
			SelectViewModel fromListObj = drivingDutyVMRepository
					.listDrivingDuties(userPlan, roundTrip.getFrom()
							.getDdName(), roundTrip.getFrom().getFromStation(),
							roundTrip.getFrom().getToStation(), null, null,
							null, null, null, null, null, null, null, null,
							null, null, null, null, 0L, 7L);

			List<DrivingDutyVM> fromDuties = ResultListToObject.convertAll(
					fromListObj.getData(), DrivingDutyVM.class);
			Map<Long, Boolean> usedToDuties = new HashMap<Long, Boolean>();/*
																			 * For
																			 * checking
																			 * this
																			 * duty
																			 * was
																			 * not
																			 * used
																			 * by
																			 * earlier
																			 * roundtrip
																			 * .
																			 */
			for (DrivingDutyVM fromDuty : fromDuties) {

				LocalDateTime fromDateTime = LocalDateTime.parse("01/01/2011 "
						+ fromDuty.getArrivalTime(), formatter);
				int availableDay = fromDuty.getArrivalDay().ordinal();
				String availableTime = roundTrip.getFrom().getAvailableTime();
				LocalDateTime fromDateTimeAvailable = LocalDateTime.parse(
						"01/01/2011 " + availableTime, formatter2);
				long minutesBetArrNAvail = ChronoUnit.MINUTES.between(
						fromDateTime, fromDateTimeAvailable);
				if (minutesBetArrNAvail < 0) {
					availableDay++;
				}
				String sort = " CONCAT(departureDay,departureTime)<'"
						+ availableDay + roundTrip.getFrom().getAvailableTime()
						+ "',CONCAT(departureDay,departureTime)";
				SelectViewModel toListObj = drivingDutyVMRepository
						.listDrivingDuties(userPlan, roundTrip.getTo()
								.getDdName(), roundTrip.getTo()
								.getFromStation(), roundTrip.getTo()
								.getToStation(), null, null, null, null, null,
								null, null, null, null, null, null, null, null,
								sort, 0L, 7L);
				List<DrivingDutyVM> toDuties = ResultListToObject.convertAll(
						toListObj.getData(), DrivingDutyVM.class);
				if (toDuties.size() > 0) {
					Boolean isUsed = usedToDuties.get(toDuties.get(0).getId());
					if (isUsed == null || isUsed == false) {
						// Write logic to check if wait time is not more than 24
						// hrs.
						Integer diff = toDuties.get(0).getDepartureDay()
								.ordinal()
								- availableDay;
						if (diff < 0) {
							diff = 7 + diff;
						}
						LocalDateTime toDateTime = LocalDateTime
								.parse("0" + (1 + diff) + "/01/2011 "
										+ toDuties.get(0).getDepartureTime(),
										formatter);

						long hours = ChronoUnit.HOURS.between(
								fromDateTimeAvailable, toDateTime);
						if (hours < 24L) {
							/*
							 * only save round trip if difference is less than
							 * 24 hrs
							 */
							String drivingDutyIds = fromDuty.getId() + ","
									+ toDuties.get(0).getId();
							ProcessResult partResult = saveSingleRoundTrip(
									drivingDutyIds, "1,2", crewType, userPlan);
							if (partResult.getResult()) {
								usedToDuties.put(toDuties.get(0).getId(), true);
							} else {
								return partResult;
							}
						}
					}
				}

			}

		}
		return new ProcessResult(true, "SUCCESS");
	}

	@Transactional(readOnly=false)
	@Override
	public ProcessResult insertRoundTripForCrewLinkGeneration(Long roundTrip,
			Integer orderNo, Integer crewLinkId) {
		ProcessResult result = null;
		String query1 = "INSERT INTO roundtripsforcrewlinkgeneration (crew_link_id,order_no,round_trip)"
				+ " VALUES(:crewLinkId,:orderNo,:roundTrip)";

		Query query1f = entityManager.createNativeQuery(query1);
		query1f.setParameter("roundTrip", roundTrip);
		query1f.setParameter("orderNo", orderNo);
		query1f.setParameter("crewLinkId", crewLinkId);

		try {
			query1f.executeUpdate();
			result = new ProcessResult(true, "SUCCESS");
		} catch (Exception ex) {
			result = new ProcessResult(false, "Error in Insertion of Round Trip in Crew Link Generation Table."
					+ ex.getMessage());
			System.out.println("ERROR in QUERY: " + ex.getMessage());
		}

		return result;
	}

}
