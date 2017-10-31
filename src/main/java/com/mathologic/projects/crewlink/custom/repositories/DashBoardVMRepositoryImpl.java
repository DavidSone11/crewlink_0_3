package com.mathologic.projects.crewlink.custom.repositories;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mathologic.projects.crewlink.custom.models.DashBoardVM;
import com.mathologic.projects.crewlink.custom.models.ItemSearchResult;
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
public class DashBoardVMRepositoryImpl implements DashBoardVMRepository {
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional(readOnly = false)
	@Override
	public DashBoardVM listDashboardItems(Long userPlan,String station) {
		
		
		DashBoardVM dbVm;
		
		StoredProcedureQuery storedProcedure = entityManager
				.createStoredProcedureQuery("dashBoardItems");
		
		storedProcedure.registerStoredProcedureParameter("userPlan",Long.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("station",String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("pTrains",Integer.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("pDrivingSections",Integer.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("pDrivingDuties",Integer.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("pRoundTrips",Integer.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("cCrewlinks",Integer.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("noOfLocoPilots",Integer.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("totalKM",Double.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("pilotKM",Double.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("trainKM",Double.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("avgDutyHrsPer14Days",Double.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("noOfRTPerMonthPerCrew",Double.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("pAvgDutyHrs",Double.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("pAvgOSR",Double.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("pAvgHQR",Double.class, ParameterMode.OUT);

		
	
				storedProcedure.setParameter("userPlan", userPlan);
				storedProcedure.setParameter("station", (station!=null)?((station.isEmpty())?"":station):"");
				storedProcedure.execute();
				Integer pendingTrain  				=  (Integer) storedProcedure.getOutputParameterValue("pTrains");
				Integer pendingDrivingSections  	=  (Integer) storedProcedure.getOutputParameterValue("pDrivingSections");
				Integer pendingDrivingDuties  		=  (Integer) storedProcedure.getOutputParameterValue("pDrivingDuties");
				Integer pendingRoundTrips  			=  (Integer) storedProcedure.getOutputParameterValue("pRoundTrips");
				Integer completedCrewlink  			=  (Integer) storedProcedure.getOutputParameterValue("cCrewlinks");
				Integer noOfLocoPilots  			=  (Integer) storedProcedure.getOutputParameterValue("noOfLocoPilots");
				Double totalKM  					=  (Double) storedProcedure.getOutputParameterValue("totalKM");
				Double pilotKM  					=  (Double) storedProcedure.getOutputParameterValue("pilotKM");
				Double trainKM  					=  (Double) storedProcedure.getOutputParameterValue("trainKM");
				Double avgDutyHrsPer14Days  		=  (Double) storedProcedure.getOutputParameterValue("avgDutyHrsPer14Days");
				Double noOfRTPerMonthPerCrew  		=  (Double) storedProcedure.getOutputParameterValue("noOfRTPerMonthPerCrew");
				Double pAvgDutyHrs  				=  (Double) storedProcedure.getOutputParameterValue("pAvgDutyHrs");
				Double pAvgOSR  					=  (Double) storedProcedure.getOutputParameterValue("pAvgOSR");
				Double pAvgHQR  					=  (Double) storedProcedure.getOutputParameterValue("pAvgHQR");
				
				
				dbVm = new DashBoardVM(pendingTrain, pendingDrivingSections,pendingDrivingDuties,pendingRoundTrips,completedCrewlink
						,noOfLocoPilots,totalKM,pilotKM,trainKM,avgDutyHrsPer14Days,noOfRTPerMonthPerCrew,pAvgDutyHrs,pAvgOSR,pAvgHQR);
		
				return dbVm;
		
	
	}
	
	@Override
	public SelectViewModel listDependencies(String searchItem, String searchValue,Long userPlan) {
		String FROM = 
				" FROM train as t LEFT JOIN train_station as ts ON (t.id = ts.train) " + 
				" LEFT JOIN driving_section_train_stations as dsts ON (dsts.train_stations = ts.id)" + 
				" LEFT JOIN driving_section as ds ON (ds.id = dsts.driving_sections)" + 
				" LEFT JOIN station as fsds ON (fsds.id = ds.from_station)" + 
				" LEFT JOIN station as tsds ON (tsds.id = ds.to_station)" + 
				" LEFT JOIN driving_duty_element as dde ON (dde.id = ds.driving_duty_element)" + 
				" LEFT JOIN driving_duty as dd ON (dd.id = dde.driving_duty)" + 
				" LEFT JOIN round_trip as rt ON (rt.id = dd.round_trip)" + 
				" LEFT JOIN crew_link as cl ON (cl.id = rt.crew_link)" + 
				" WHERE " +
				" (ds.user_plan = :userPlan  || ds.user_plan IS NULL ) AND " +
				" (dde.user_plan = :userPlan  || dde.user_plan IS NULL ) AND " +
				" (dd.user_plan = :userPlan  || dd.user_plan IS NULL ) AND " +
				" (rt.user_plan = :userPlan  || rt.user_plan IS NULL ) AND " +
				" (cl.user_plan = :userPlan  || cl.user_plan IS NULL ) AND ";
		switch(searchItem) {		
			case "trainNo":
				FROM +=" t.train_no = "+searchValue;
				break;
			case "train":
				if(searchValue.contains(":")) {
					String[] vals = searchValue.split(":",2);
					if(vals.length==2) {
						FROM += " t.train_no = "+vals[0];
						if(!vals[1].equals("undefined")) {
							FROM += " AND t.start_day = "+Day.valueOf(vals[1]).ordinal();
						}
					}
				}else {
					FROM += " ts.train = "+searchValue;
				}
				break;
			case "drivingSection":
				FROM += " ds.id = "+searchValue;
				break;
			case "drivingDuty":
				FROM += " dd.id = "+searchValue;
				break;
			case "ddName":
				FROM += " dd.dd_name LIKE '%"+searchValue+"%'";
				break;
			case "roundTrip":
				FROM += " rt.id = "+searchValue;
				break;
			case "rtName":
				FROM += " rt.rt_name LIKE '%"+searchValue+"%'";
				break;
			case "crewLink":
				FROM += " cl.id = "+searchValue;
				break;
			case "clName":
				FROM += " cl.link_name LIKE '%"+searchValue+"%'";
				break;
			default:
				return null;
		}		
				
		FROM += " GROUP BY trainNo,trainStartDay,fromStationDS,toStationDS,ddName,rtName,clName";

		String query2 = "SELECT t.train_no as trainNo,t.start_day as trainStartDay, fsds.code as fromStationDS, tsds.code as toStationDS," + 
				"	dd.dd_name as ddName, rt.rt_name as rtName, cl.link_name as clName" + FROM;
		
		Query query2f = entityManager.createNativeQuery(query2);
		query2f.setParameter("userPlan", userPlan);
		SelectViewModel result = new SelectViewModel(ItemSearchResult.class,
				new SelectionDetails(0L, 0L, 0L,0L, ""), null);
		try {
			result.setData(query2f.getResultList());
		} catch (Exception ex) {
			System.out.println("ERROR in QUERY: " + ex.getMessage());
		}

		return result;
	}


	
	
	
}