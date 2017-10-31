package com.mathologic.projects.crewlink.custom.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * This controller is used to fetch available distance and 
 * used(Round Trip) distance 
 * @author Laxman
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 28, 2016
 */

@Repository
public class BaseStationAnalysisVMRepositoryImpl implements BaseStationAnalysisVMRepository{

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = false)
	public List getTotalUsedDistance(Long userPlan, String station) {
		List result = null;
		String query2 = 
				  	"SELECT ct.name as crewType,sum(rt.distance) as totalDistance, count(rt.id) as noOfRoundTrips "
					+ " FROM round_trip as rt " 
					+ " LEFT JOIN crew_type as ct ON(rt.crew_type = ct.id) "
					+ " LEFT JOIN station as s ON(rt.station = s.id)"
					+ " WHERE rt.user_plan = :userPlan"
					+ " AND rt.is_ignore = 0"
					+ " AND s.code = :station" 
					+ " GROUP BY ct.name;";
		Query query2f = entityManager.createNativeQuery(query2);
		query2f.setParameter("userPlan", userPlan);
		query2f.setParameter("station", station);
		try {
			result = query2f.getResultList();
		} catch (Exception ex) {
			System.out.println("ERROR in QUERY: " + ex.getMessage());
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = false)
	public List getTotalAvailableDistanceFrom(Long userPlan, String station) {
		List result = null;
		String query2 = 
			  	"SELECT sum(dd.distance) as totalDistanceAvailableFrom, count(dd.id) as noOfDuties"
				+ " FROM driving_duty as dd "
				+ " JOIN station as fs ON(dd.from_station = fs.id)"
				+ " JOIN station as ts ON(dd.to_station = ts.id)"
				+ " WHERE dd.user_plan = :userPlan"
				+ " AND dd.is_ignore = 0"
				+ " AND fs.code = :station "
				+ " AND ts.code != :station"
				+ " AND dd.round_trip IS NULL" ;
		Query query2f = entityManager.createNativeQuery(query2);
		query2f.setParameter("userPlan", userPlan);
		query2f.setParameter("station", station);
		try {
			result = query2f.getResultList();
		} catch (Exception ex) {
			System.out.println("ERROR in QUERY: " + ex.getMessage());
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = false)
	public List getTotalAvailableDistanceTo(Long userPlan, String station) {
		List result = null;
		String query2 = 
			  	"SELECT sum(dd.distance) as totalDistanceAvailableTo, count(dd.id) as noOfDuties"
				+ " FROM driving_duty as dd "
				+ " JOIN station as fs ON(dd.from_station = fs.id)"
				+ " JOIN station as ts ON(dd.to_station = ts.id)"
				+ " WHERE dd.user_plan = :userPlan"
				+ " AND dd.is_ignore = 0"
				+ " AND fs.code != :station "
				+ " AND ts.code = :station"
				+ " AND dd.round_trip IS NULL" ;
		Query query2f = entityManager.createNativeQuery(query2);
		query2f.setParameter("userPlan", userPlan);
		query2f.setParameter("station", station);
		try {
			result = query2f.getResultList();
		} catch (Exception ex) {
			System.out.println("ERROR in QUERY: " + ex.getMessage());
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = false)
	public List getTotalAvailableDistanceBoth(Long userPlan, String station) {
		List result = null;
		String query2 = 
			  	"SELECT sum(dd.distance) as totalDistanceAvailableBoth, count(dd.id) as noOfDuties"
				+ " FROM driving_duty as dd "
				+ " JOIN station as fs ON(dd.from_station = fs.id)"
				+ " JOIN station as ts ON(dd.to_station = ts.id)"
				+ " WHERE dd.user_plan = :userPlan"
				+ " AND dd.is_ignore = 0"
				+ " AND fs.code = :station "
				+ " AND ts.code = :station"
				+ " AND dd.round_trip IS NULL" ;
		Query query2f = entityManager.createNativeQuery(query2);
		query2f.setParameter("userPlan", userPlan);
		query2f.setParameter("station", station);
		try {
			result = query2f.getResultList();
		} catch (Exception ex) {
			System.out.println("ERROR in QUERY: " + ex.getMessage());
		}
		return result;
	}

}
