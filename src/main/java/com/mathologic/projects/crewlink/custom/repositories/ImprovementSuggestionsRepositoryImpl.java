package com.mathologic.projects.crewlink.custom.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.mathologic.projects.crewlink.custom.models.ImprovementsDrivingDutyWithDailyPilotVM;
import com.mathologic.projects.crewlink.custom.models.ImprovementsDrivingSectionVM;
import com.mathologic.projects.crewlink.custom.models.ImprovementsLongDrivingDutyVM;
import com.mathologic.projects.crewlink.custom.models.ImprovementsRoundTripWithExcessOSRVM;
import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.models.SelectionDetails;

/**
 * This repository contains implementations of list driving duties, saving driving duty,
 * delete driving duties .
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
@Repository
public class ImprovementSuggestionsRepositoryImpl implements ImprovementSuggestionsRepository {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public SelectViewModel drivingSection(Long userPlan, Long maxDuration) {
		SelectViewModel result;
		String query2 = "SELECT t.train_no as trainNo, ds1fs.code as ds1From, ds1ts.code as ds1To, ds1.start_time as ds1StartTime, ds1.end_time as ds1EndTime, ds1.duration as ds1Duration,  " + 
				"		GROUP_CONCAT(DISTINCT ds1dd.dd_name  " + 
				"        ORDER BY ds1dd.dd_name  " + 
				"        SEPARATOR '|') as ds1DDNames,  " + 
				"		GROUP_CONCAT(DISTINCT ds1rt.rt_name  " + 
				"        ORDER BY ds1rt.rt_name  " + 
				"        SEPARATOR '|') as ds1RTNames,  " + 
				"		GROUP_CONCAT(DISTINCT ds1cl.link_name  " + 
				"        ORDER BY ds1cl.link_name  " + 
				"        SEPARATOR '|') as ds1CLNames,  " + 
				"		ds2fs.code as ds2From, ds2ts.code as ds2To, ds2.start_time as ds2StartTime, ds2.end_time as ds2EndTime,ds2.duration as ds2Duration,   " + 
				"		GROUP_CONCAT(DISTINCT ds2dd.dd_name  " + 
				"        ORDER BY ds2dd.dd_name  " + 
				"        SEPARATOR '|') as ds2DDNames,  " + 
				"		GROUP_CONCAT(DISTINCT ds2rt.rt_name  " + 
				"        ORDER BY ds2rt.rt_name  " + 
				"        SEPARATOR '|') as ds2RTNames,  " + 
				"		GROUP_CONCAT(DISTINCT ds2cl.link_name  " + 
				"        ORDER BY ds2cl.link_name  " + 
				"        SEPARATOR '|') as ds2CLNames,  COUNT(*) as counts," + 
				"		CalTimeDiffWeekly(ds1.start_day, ds1.start_time, ds2.end_day, ds2.end_time) as newDuration  " + 
				"FROM  " + 
				"driving_section as ds1   " + 
				"LEFT JOIN driving_section as ds2 ON (ds1.train = ds2.train AND ds1.to_station = ds2.from_station AND ds1.user_plan = ds2.user_plan)  " + 
				"LEFT JOIN train as t ON (t.id = ds1.train)  " + 
				"LEFT JOIN station as ds1fs ON (ds1fs.id = ds1.from_station)  " + 
				"LEFT JOIN station as ds1ts ON (ds1ts.id = ds1.to_station)  " + 
				"LEFT JOIN station as ds2fs ON (ds2fs.id = ds2.from_station)  " + 
				"LEFT JOIN station as ds2ts ON (ds2ts.id = ds2.to_station)  " + 
				"LEFT JOIN driving_duty_element as ds1dde ON (ds1.driving_duty_element = ds1dde.id)  " + 
				"LEFT JOIN driving_duty_element as ds2dde ON (ds2.driving_duty_element = ds2dde.id)  " + 
				"LEFT JOIN driving_duty as ds1dd ON (ds1dd.id = ds1dde.driving_duty)  " + 
				"LEFT JOIN driving_duty as ds2dd ON (ds2dd.id = ds2dde.driving_duty)  " + 
				"LEFT JOIN round_trip as ds1rt ON (ds1rt.id = ds1dd.round_trip)  " + 
				"LEFT JOIN round_trip as ds2rt ON (ds2rt.id = ds2dd.round_trip)  " + 
				"LEFT JOIN crew_link as ds1cl ON (ds1cl.id = ds1rt.crew_link)  " + 
				"LEFT JOIN crew_link as ds2cl ON (ds2cl.id = ds2rt.crew_link)  " + 
				"WHERE :maxDur > CalTimeDiffWeekly(ds1.start_day, ds1.start_time, ds2.end_day, ds2.end_time)  " + 
				"AND ds1.user_plan = :userPlan  " + 
				"AND ds2.user_plan = :userPlan  " + 
				"GROUP BY t.train_no,ds1.from_station, ds1.to_station,ds1.driving_section_order_no  " +
				"ORDER BY trainNo";
		
		Query query2f = entityManager.createNativeQuery(query2);
		query2f.setParameter("userPlan", userPlan);
		query2f.setParameter("maxDur", maxDuration);
		result = new SelectViewModel(ImprovementsDrivingSectionVM.class,
				new SelectionDetails(0L, 0L, 0L,0L, ""), null);
		try {
			result.setData(query2f.getResultList());
		} catch (Exception ex) {
			System.out.println("ERROR in QUERY: " + ex.getMessage());
		}
	
		return result;		
	
	}
	
	@Override
	public SelectViewModel longDrivingDuty(Long userPlan, Long maxDuration) {
		SelectViewModel result;
		String query2 = "SELECT dd.dd_name as ddName, ddfs.code as fromStation, ddts.code as toStation, dd.start_time as startTime, dd.end_time as endTime, dd.duration as duration, (dd.duration - (:maxDur)) as excessDuration,  " + 
				"		COUNT(*) counts,  " + 
				"		GROUP_CONCAT(DISTINCT rt.rt_name  " + 
				"        ORDER BY rt.rt_name  " + 
				"        SEPARATOR '|') as rtNames,  " + 
				"      GROUP_CONCAT(DISTINCT cl.link_name  " + 
				"        ORDER BY cl.link_name  " + 
				"        SEPARATOR '|') as clNames  " + 
				"FROM driving_duty as dd  " + 
				"LEFT JOIN driving_duty_element as dde ON (dde.driving_duty = dd.id)  " + 
				"LEFT JOIN driving_section as ds ON (ds.driving_duty_element = dde.id)  " + 
				"LEFT JOIN station ddfs ON (ddfs.id = dd.from_station)  " + 
				"LEFT JOIN station ddts ON (ddts.id = dd.to_station)  " + 
				"LEFT JOIN round_trip as rt ON (dd.round_trip = rt.id)  " + 
				"LEFT JOIN crew_link as cl ON (rt.crew_link = cl.id)  " + 
				"WHERE dd.user_plan = :userPlan  " + 
				"AND ds.id IS NOT NULL  " + 
				"AND dd.is_ignore = false  " + 
				"AND dd.duration > (:maxDur)  " + 
				"GROUP BY dd.dd_name,dd.from_station,dd.to_station,dd.duration  " +
				"ORDER BY ddName";
		
		Query query2f = entityManager.createNativeQuery(query2);
		query2f.setParameter("userPlan", userPlan);
		query2f.setParameter("maxDur", maxDuration);
		result = new SelectViewModel(ImprovementsLongDrivingDutyVM.class,
				new SelectionDetails(0L, 0L, 0L,0L, ""), null);
		try {
			result.setData(query2f.getResultList());
		} catch (Exception ex) {
			System.out.println("ERROR in QUERY: " + ex.getMessage());
		}
	
		return result;		
	
	}

	@Override
	public SelectViewModel drivingDutyWithDailyPilots(Long userPlan,
			Integer minRunDays) {
		SelectViewModel result;
		String query2 = "SELECT dd.dd_name as ddName, ddfs.code as fromStation, ddts.code as toStation,dd.start_time as startTime, dd.end_time as endTime, dd.duration as duration, ddd.counts as counts, GROUP_CONCAT(DISTINCT rt.rt_name  " + 
				"        ORDER BY rt.rt_name  " + 
				"        SEPARATOR '|') as rtNames,  " + 
				"        GROUP_CONCAT(DISTINCT cl.link_name  " + 
				"        ORDER BY cl.link_name  " + 
				"        SEPARATOR '|') as clNames  " + 
				"FROM driving_duty as dd  " + 
				"LEFT JOIN station as ddfs ON (ddfs.id = dd.from_station)  " + 
				"LEFT JOIN station as ddts ON (ddts.id = dd.to_station)  " + 
				"LEFT JOIN round_trip as rt ON (dd.round_trip = rt.id)  " + 
				"LEFT JOIN crew_link as cl ON (rt.crew_link = cl.id)  " + 
				"LEFT JOIN (  " + 
				"	SELECT dd1.dd_name as ddName, dd1.from_station as frSt,dd1.to_station as toSt, COUNT(*) as counts  " + 
				"		FROM driving_duty as dd1  " + 
				"		WHERE dd1.user_plan = :userPlan  " + 
				"		AND dd1.is_ignore = false  " + 
				"		GROUP BY dd1.dd_name, dd1.from_station, dd1.to_station  " + 
				") as ddd ON (ddd.ddName = dd.dd_name AND ddd.frSt = dd.from_station AND ddd.toSt = dd.to_station)  " + 
				"WHERE dd.dd_name LIKE '%P%'  " + 
				"AND ddd.counts >= :minRunDays  " + 
				"AND dd.user_plan = :userPlan  " + 
				"AND dd.is_ignore = false  " + 
				"GROUP BY dd.dd_name, dd.from_station, dd.to_station";
		
		Query query2f = entityManager.createNativeQuery(query2);
		query2f.setParameter("userPlan", userPlan);
		query2f.setParameter("minRunDays", minRunDays);
		result = new SelectViewModel(ImprovementsDrivingDutyWithDailyPilotVM.class,
				new SelectionDetails(0L, 0L, 0L,0L, ""), null);
		try {
			result.setData(query2f.getResultList());
		} catch (Exception ex) {
			System.out.println("ERROR in QUERY: " + ex.getMessage());
		}
	
		return result;	
	}	
	
	@Override
	public SelectViewModel roundTripWithExcessOSR(Long userPlan,
			Long maxOSR) {
		SelectViewModel result;
		String query2 = "SELECT rt.rt_name as rtName, rtst.code as baseStation, rtd.OSR as OSR, (rtd.OSR - :maxOSR) as excessOSR, rtd.NIB as NIB, count(*) as counts, GROUP_CONCAT(DISTINCT cl.link_name  " + 
				"        ORDER BY cl.link_name  " + 
				"        SEPARATOR '|') as clNames  " + 
				"FROM   " + 
				"(  " + 
				"	SELECT dd1.round_trip as rtId, MAX(IF(dd2.id IS NOT NULL,((TIME_TO_SEC(TIMEDIFF(  CAST(dd2.start_time as TIME), CAST(dd1.end_time as TIME)))/60) +  " + 
				"	                      (IF(dd2.start_day - dd1.end_day < 0 , 7 - (dd1.end_day - dd2.start_day), (dd2.start_day - dd1.end_day))*1440)),0)) as OSR,  " +
				"                         MAX(IF(CAST('22:00' as TIME) - CAST(dd1.end_time as TIME)<0,false,IF(CAST(dd2.start_time as TIME) - CAST('06:00' as TIME)<0,false,true))) as NIB  " + 
				"	FROM driving_duty as dd1 LEFT JOIN driving_duty dd2 ON (dd1.round_trip = dd2.round_trip AND dd1.round_trip_order_no +1 = dd2.round_trip_order_no)  " + 
				"	WHERE dd1.user_plan = :userPlan  " + 
				"	AND dd2.user_plan = :userPlan  " + 
				"	AND dd1.is_ignore = false  " + 
				"	AND dd2.is_ignore = false  " + 
				"	GROUP BY dd1.round_trip  " + 
				") as rtd   " + 
				"LEFT JOIN round_trip as rt ON (rtd.rtId = rt.id)  " + 
				"LEFT JOIN station as rtst ON (rtst.id = rt.station)  " + 
				"LEFT JOIN crew_link as cl ON (rt.crew_link = cl.id)  " + 
				"WHERE rtd.OSR > (:maxOSR )  " + 
				"AND rt.user_plan = :userPlan  " + 
				"AND rt.is_ignore = false  " + 
				"GROUP BY rtName, baseStation, OSR " + 
				"ORDER BY baseStation,OSR";
		
		Query query2f = entityManager.createNativeQuery(query2);
		query2f.setParameter("userPlan", userPlan);
		query2f.setParameter("maxOSR", maxOSR);
		result = new SelectViewModel(ImprovementsRoundTripWithExcessOSRVM.class,
				new SelectionDetails(0L, 0L, 0L,0L, ""), null);
		try {
			result.setData(query2f.getResultList());
		} catch (Exception ex) {
			System.out.println("ERROR in QUERY: " + ex.getMessage());
		}
	
		return result;	
	}	
}