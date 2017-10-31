package com.mathologic.projects.crewlink.custom.repositories;

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;

import com.mathologic.projects.crewlink.custom.models.CopyUserPlanPM;
import com.mathologic.projects.crewlink.custom.models.ProcessResult;

/**
 * This repository contains implementations of list trains and save train
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date May 10, 2016
 */
@Repository
public class UserPlanVMRepositoryImpl implements UserPlanVMRepository {
	@PersistenceContext
	private EntityManager entityManager;

	
	@Override
	public ProcessResult copy( CopyUserPlanPM data) {
		ProcessResult result = null;

		// Create stored procedure query for create train
		// Register the parameters required for this stored procedure

		StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("CopyPlan");
		storedProcedure.registerStoredProcedureParameter("sourcePlanName", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("sourcePlanTimeStamp", Timestamp.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("newPlanName", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("user", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("newUserPlanId", Long.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("result", Boolean.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("errorMessage", String.class, ParameterMode.OUT);
		// Set the parameter value
		storedProcedure.setParameter("sourcePlanName", data.getSourceUserPlanName());
		storedProcedure.setParameter("sourcePlanTimeStamp", Timestamp.valueOf(data.getSourceUserPlanTimeStamp()));
		storedProcedure.setParameter("newPlanName", data.getNewUserPlanName());
		storedProcedure.setParameter("user", data.getUsername());
		// call the stored procedure
		storedProcedure.execute();
		// Fetch the result and errorMessage output parameter of the stored
		// procedure
		Boolean resultB = (Boolean) storedProcedure.getOutputParameterValue("result");
		String errorMessage = storedProcedure.getOutputParameterValue("errorMessage").toString();
		if (resultB) {
			Long outputValue = (Long) storedProcedure.getOutputParameterValue("newUserPlanId");
			result = new ProcessResult(resultB, errorMessage, outputValue.toString());
		} else {
			result = new ProcessResult(resultB, errorMessage);
		}

		return result;
	}

}