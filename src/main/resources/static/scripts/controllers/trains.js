'use strict';
/**
 * The Following Controller is Used to 1. List all trainTpes 2. Fetch all Trains
 * 3. Select the Train for Updated 4. Remove the Trains
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */

angular
		.module('crewLinkApp')
		.controller(
				'TrainsCtrl',
				function($scope, $position, $http, $resource,$confirm,
						SpringDataRestAdapter, SpringDataRestApi, toaster,
						UserService) {
					$scope.Days = Days;
					$scope.trains = {
						data : [],
						fields : {}
					};
					$scope.trainTypes = [];
					$scope.hasAdmin = false;
					$scope.passingStation1 = '';
					$scope.passingStation2 = '';
					var defaulteoption = {
						name : "select"
					};
					
					$scope.selectedValue = '';
					/*
					 * Fetch the Train Types from DB using SpringDataRestApi
					 */
					SpringDataRestApi
							.list('trainTypes')
							.then(
									function(response) {
										$scope.trainTypes = response;
										SpringDataRestApi
												.get(
														UserService
																.getSelectedUserPlan()._links.user.href)
												.then(
														function(userResponse) {
															SpringDataRestApi
																	.get(
																			userResponse._links.role.href)
																	.then(
																			// Find
																			// the
																			// Roles
																			// using
																			// following
																			// URL
																			function(
																					roleRespons) { // On
																				// Success
																				// Response
																				if (roleRespons.name == "ADMIN") // Check
																					// for
																					// Role
																					// whether
																					// its
																					// is
																					// Admin
																					// or
																					// Super
																					// User
																					$scope.hasAdmin = true;
																				else
																					$scope.hasAdmin = false;

																			});

														});

									});
				
					
					  
					 /* $scope.updateIgnoreAll = function(){
						  angular.forEach($scope.drivingSections.data, function(drivingSection){
				              if(drivingSection[$scope.drivingSections.fields.isIgnore]==false){
				             	 drivingSection[$scope.drivingSections.fields.isIgnore] = true;
				             	 $scope.updateDrivingSectionIsIgnore(drivingSection);
					                }
						  	});
					  }*/
					  
					
					  $scope.updateSelectAll = function(){
						  angular.forEach($scope.trains.data,function(train){
						                 if(train[$scope.trains.fields.isUserSelected]==false){
						                	 train[$scope.trains.fields.isUserSelected] =true;
						                	 $scope.updateTrainInUserList(train);
							               }
						  		});
					  };
					  
					  $scope.updateDeselectAll = function(){
						  angular.forEach($scope.trains.data,function(train){
						                 if(train[$scope.trains.fields.isUserSelected]==true){
						                	 train[$scope.trains.fields.isUserSelected] =false;
						                	 $scope.updateTrainInUserList(train);
							               }
						  		});
					  }; 
				
					$scope.updateTrainInUserList = function(trainItem) {
						var uri = "/api/custom/trains/updateTrainInUserList?userPlan="
								+ UserService.getSelectedUserPlan().id
								+ "&trainNo="+ trainItem[$scope.trains.fields.trainNo]
								+ "&isUserSelected="+ trainItem[$scope.trains.fields.isUserSelected];
						$http
								.get(uri)
								.then(
										function(response) {
											if (response.data.result) {
												toaster
														.pop({
															type : 'success',
															title : 'Train update to user list',
															body : 'Train update to user list is successfully !!!'
														});
											} else {
												toaster
														.pop({
															type : 'error',
															title : 'Error',
															body : 'Unable to update train to user list. Please Try Again!!! '
																	+ response.data.errorMessage
														});
											}
										},
										function(response) {
											toaster
													.pop({
														type : 'error',
														title : 'Error',
														body : 'Unable to update train to user list. Please Try Again!!! '
													});

										});
					};

					$scope.changeTrainFromUser = function(trainItem) {
						if (trainItem[$scope.trains.fields.hasDrivingSection]) {
							/*
							 * If the driving section is already created and
							 * User clicked on the Save(button) then the PopUp
							 * messages will display to delete the already
							 * created Section
							 * 
							 */
							$confirm(
									{ // Confirm PopUp to Remove fields from
										// DB
										text : 'Are you sure you want to delete all item of this train?',
										headerClass : 'confirm-header-danger',
										okButtonClass : "btn-danger"
									}).then(function() {
								$scope.updateTrainInUserList(trainItem);
							},function(){
								trainItem[$scope.trains.fields.isUserSelected] = !trainItem[$scope.trains.fields.isUserSelected];
							});
						} else {
							/*
							 * Invoke this callSaveApi Method for creating
							 * Sections based on Type as
							 * (single,all,allwithdrivingduty)
							 */
							$scope.updateTrainInUserList(trainItem);
						}

					};

					/**
					 * ServerFetch function used to Fetch the List of Trains
					 */
					$scope.serverFetch = new ServerTableFetch2(
							"/api/custom/trains/listByNumbers?userPlan="
									+ UserService.getSelectedUserPlan().id, // Url
							// call
							// that
							// will
							// be
							// made
							// all
							// the
							// time
							SpringDataRestApi, // This is our Call Processing
							// Service currently only
							// SringDataRestApi is supported
							// and used here.
							function() { // Before processing this is called
								$scope.isLoading = true;
							}, function(resultObj) { // After processing this
								// is called
								$scope.trains = resultObj;
								$scope.isLoading = false;
							});

					$scope.selectedTrain = null;

					/**
					 * The selectTrain is Used for Select the Train
					 */
					$scope.selectTrain = function(trainNo) {
						SpringDataRestApi.get(
								$scope.trains.selectionDetails.baseItemRestUri
										+ trainNo, null, this.isWait).then(
								function(response) {
									$scope.selectedTrain = response;
								}.bind(this), function(response) {

								}.bind(this));

					};

					/**
					 * The removeTrain Function is Used to Removed the selected
					 * Train
					 */
					$scope.removeTrain = function(trainId) {
						SpringDataRestApi.deleteItem($scope.trains.selectionDetails.baseItemRestUri+trainId).then(function(response){ // The SpringDataRestApi is a services which call the Function deleteItem and take link as a parameter
							$scope.temp=[];
		 	    	  		var trainIndex = 0;
		 	    	  		//Logic to remove deleted round trip from round trip list
		 	    	  		for(var i=0;i<$scope.trains.data.length;i++)
		 	    	  			{
		 	    	  					
		 	    	  					var count=0;
		 	    	  					if($scope.trains.data[i][0]==trainId)
		 	    	  					{
		 	    	  						trainIndex = i;
		 	    	  						break;
		 	    	  					}
		 	    	  			}
		 	    	  		$scope.trains.data.splice(trainIndex,1);
							toaster.pop({type: 'success', title: 'Train', body: 'Train Removed Successfully!!!'}); // Toaster to show the PopUp message after remove 
						}, 
						function(response){ 
							toaster.pop({type: 'error', title: 'Error', body: 'Unable To Remove Train. Please Try Again!!!'}); // On Failure Toaster to show the PopUp messages
							
						});
					};
				});
