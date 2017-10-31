package com.mathologic.projects.crewlink.custom.repositories;

import com.mathologic.projects.crewlink.custom.models.CopyUserPlanPM;
import com.mathologic.projects.crewlink.custom.models.ProcessResult;


/**
 * This repository contains declarations of list trains and save train
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date May 10, 2016
 */
public interface UserPlanVMRepository {

	ProcessResult copy(CopyUserPlanPM data);

}
