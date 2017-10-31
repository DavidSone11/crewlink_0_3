package com.mathologic.projects.crewlink.custom.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.models.SelectionDetails;
import com.mathologic.projects.crewlink.custom.models.TrainByNumberVM;

@Repository
public class DrivingSectionChartVMImplRepository implements DrivingSectionChartVMRepository {

	
	@PersistenceContext
	private EntityManager entityManager;


	public DrivingSectionChartVMImplRepository() {
	}

	@Transactional(readOnly = true)
	@Override
	public SelectViewModel getlistOfTrains(int trainNo, Long page, Long size) {
		String FROM = " FROM train as t WHERE  (:trainNo IS NULL OR CONVERT(t.train_no,CHAR(10)) LIKE :trainNo)"
				+ " LEFT JOIN station as sf ON (t.from_station  = sf.id)"
				+ " LEFT JOIN station as st ON (t.to_station = st.id)"
				+ " LEFT JOIN train_type as tt ON (t.train_type = tt.trainType)";
		String query1 = "SELECT COUNT(t.id)" + FROM;
		Query query1f = entityManager.createNativeQuery(query1);
		query1f.setParameter("trainNo", trainNo);
		
		Long totalElements = ((java.math.BigInteger) query1f.getSingleResult()).longValue();
		Long startIndex = page * size;
		Long totalPages = totalElements / size;
		Long currentPage = page;
		String baseItemRestUri = "/api/trains/";
		SelectViewModel result = new SelectViewModel(TrainByNumberVM.class,
				new SelectionDetails(totalElements, startIndex, currentPage, totalPages, baseItemRestUri), null);
		if (totalElements>0){

			String query2 = "SELECT t.id as id t.name as name, sf.code as fromStation,"
							+ " st.code as toStation"
							+ " t.start_day as startDay, t.train_no as trainNo"+FROM + " LIMIT :start, :offset";
			Query query2f = entityManager.createNativeQuery(query2);
			query2f.setParameter("trainNo",trainNo);
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
