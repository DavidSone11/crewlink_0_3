package com.mathologic.projects.crewlink.custom.repositories;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.models.TrainTypeVM;

/**
 * This repository contains implementations of find train type by name
 * 
 * @author Jagdish
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
@Repository
public class TrainTypeVMRespositoryImpl implements TrainTypeVMRepository{
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * This is used to Fetch train type by name 
	 */
	@Transactional(readOnly = true)
	@Override
	public SelectViewModel findByName(String name) {
		String query1 = 
				"SELECT COUNT(tt.id) "
				+ " FROM train_type AS tt"
				+ " WHERE :name IS NULL OR tt.name = :name";
		
		Query query1f = entityManager.createNativeQuery(query1);
		query1f.setParameter("name", name);
		
		Long totalElements = ((java.math.BigInteger) query1f.getSingleResult())
				.longValue();
		SelectViewModel result = new SelectViewModel(TrainTypeVM.class,null, null);
		if (totalElements > 0) {
			
			String query2 = 
					  "SELECT tt.id as Id,tt.name "
					+ " FROM train_type AS tt"
					+ " WHERE :name IS NULL OR tt.name= :name ";
			Query query2f = entityManager.createNativeQuery(query2);
			query2f.setParameter("name", name);
			
			try {
				result.setData(query2f.getResultList());
			} catch (Exception ex) {
				System.out.println("ERROR in QUERY: " + ex.getMessage());
			}
		}
		return result;
	}

}
