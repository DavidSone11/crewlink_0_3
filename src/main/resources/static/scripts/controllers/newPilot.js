'use strict';

/**
 * This CrewLink controller is used to 1. List all the Crew Links from DB 2.
 * List all the Driving Sections of selected Crew Link from DB 3. Remove crew
 * link from DB 4. Creating crew link plan with all the driving sections of
 * selected crew link
 * 
 * @authors Vivek Yadav, Laxman
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 16, 2016
 */

angular.module('crewLinkApp')
		.controller(
				'NewPilotCtrl',
				function($state, $scope, $position, $http, $resource,
						UserService, SpringDataRestAdapter, SpringDataRestApi,
						toaster, $q, $timeout) {
					$scope.Days = Days;
					$scope.timePattern = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
					$scope.pilotTypeTrain = null;
					$scope.stations = [];
					$scope.name = null;
					$scope.fromStation = null;
					$scope.toStation = null;
					$scope.departureTime = "";
					$scope.arrivalTime = "";
					$scope.departureDay = "";
					$scope.arrivalDay = "";
					$scope.duration = null;
					$scope.distance = null;
					$scope.itemsPerPage = 10;
					$scope.refreshPilotTrains = false;
					$scope.pilotTypes = [];
					$scope.selectedPilotType = {name:""};
					$scope.selectedCssClass = 'selected-train-section';
					$scope.signOnDuration = 15;
					$scope.signOffDuration = 15;
					
					$scope.isPilotTypeTrain = function(){
						if(!$scope.selectedPilotType)
							return false;
						return $scope.selectedPilotType.name.search(/Train/i)!= -1
					};
					//

					$scope.trains = {
						data : [],
						fields : {}
					};
					$scope.selectedTrain = null;
					$scope.selectTrain = function(trainItem){
						$scope.selectedTrain = trainItem;
					}

					$scope.validateTime = function(time) {
						var patt = new RegExp($scope.timePattern);
						return patt.test(time)||time.isEmpty();
					}
					$scope.trainSelected = function() {
						for(var i=0; i<$scope.pilotTypes.length; i++){
							if($scope.pilotTypes[i].name.search(/Train/i)!= -1){
								$scope.selectedPilotType = $scope.pilotTypes[i];
							}
						}

						$scope.fromStation = null;
						$scope.toStation = null;
						$scope.departureTime = "";
						$scope.arrivalTime = "";
						$scope.departureDay = "";
						$scope.arrivalDay = "";
					}
					$scope.othersSelected = function() {
						$scope.selectedPilotType = {name:""};

						$scope.fromStation = null;
						$scope.toStation = null;
						$scope.departureTime = "";
						$scope.arrivalTime = "";
						$scope.departureDay = "";
						$scope.arrivalDay = "";
					}
					
					

					// Fetch the list based on search Term
					$scope.searchStation = function(term) {
						// No search term: return initial items
						$scope.stations = [];
						if (!term) {
							return $scope.stations;
						}
						var deferred = $q.defer();
						SpringDataRestApi.get(
								'/api/stations/search/findByCodeContains?code='
										+ term).then(function(response) {
							$scope.stations = response._embedded.stations;
							deferred.resolve($scope.stations);
						});
						return deferred.promise;
					};

					SpringDataRestApi.list('pilotTypes').then(
							function(response) {
								$scope.pilotTypes = response;
								$scope.trainSelected();
							});

					$scope.setPilotDepartureDay = function(day) {
						$scope.selectedDepDay = day;
					}
					$scope.setPilotArrivalDay = function(day) {
						$scope.selectedArrDay = day;
					}
					$scope.searchTrainForPilot = function() {
						$scope.refreshPilotTrains = !$scope.refreshPilotTrains;
					}
					$scope.searchTrainForPilotUrl = function() {
						if(!$scope.fromStation || !$scope.toStation){
							
							return "";
						}
						var uri = "/api/custom/trains/listByPilots?";
						uri += ($scope.fromStation) ? "&fromStation="
								+ $scope.fromStation: "";
						uri += ($scope.toStation) ? "&toStation="
								+ $scope.toStation: "";
						uri += ($scope.departureTime) ? "&departureTime="
								+ $scope.departureTime : "";
						uri += ($scope.arrivalTime) ? "&arrivalTime="
								+ $scope.arrivalTime : "";
						uri += ($scope.departureDay) ? "&departureDay="
								+ $scope.departureDay : "";
						uri += ($scope.arrivalDay) ? "&arrivalDay="
								+ $scope.arrivalDay : "";
						
						var sort = "";
						if($scope.departureTime!=""){
							if($scope.departureDay!="")
								sort += " CONCAT(departureDay,departureTime)<'"
									+ Days.indexOf($scope.departureDay) + $scope.departureTime
									+ "',CONCAT(departureDay,departureTime)";
							else
								sort += " departureTime <'"
									+ $scope.departureTime
									+ "',departureTime";
						}
						if($scope.arrivalTime!=""){
							if(!sort.isEmpty()){
								sort+= ",";
							}
							if($scope.arrivalDay!="")
								sort += " CONCAT(arrivalDay,arrivalTime) >'"
									+ Days.indexOf($scope.arrivalDay) + $scope.arrivalTime
									+ "',CONCAT(arrivalDay,arrivalTime) DESC";
							else
								sort += " arrivalTime >'"
									+ $scope.arrivalTime
									+ "',arrivalTime DESC";
						}
						
						if(!sort.isEmpty()){
							uri += "&sort="+sort;
						}
						return uri;
					}
					/*
					 * Server call to fetch list of Pilot Trains
					 */
					$scope.serverFetchTrains = new ServerTableFetch2(
							$scope.searchTrainForPilotUrl.bind(this),
							SpringDataRestApi, function() {
								$scope.isLoading = true;

							}, function(resultObj) {
								$scope.trains = resultObj;
								$scope.isLoading = false;

							}, function(error) {
								$scope.isLoading = false;
							});
					
					$scope.validateForSave = function(){
						if($scope.isPilotTypeTrain()){
							if($scope.selectedTrain != null){
								return true;
							}	
							else{
								toaster.pop({
									type : 'error',
									title : 'Error',
									body : 'You forgot to select the train.'
								});
								return false;
							}
								
						}else{
							if(!$scope.fromStation ||  !$scope.toStation || $scope.departureTime=='' || $scope.arrivalTime==''
									|| $scope.departureDay == '' || $scope.arrivalDay == ''
									|| $scope.selectedPilotType.name == '' || !$scope.name || !$scope.distance)
							{
								
								toaster.pop({
									type : 'error',
									title : 'Error',
									body : 'You forgot to enter all the values.'
								});
								return false; // add all checks
							}
							else{
								$scope.duration = diffTimeObj(
										{
											day : $scope.departureDay,
											time : $scope.departureTime
										},{
											day : $scope.arrivalDay,
											time : $scope.arrivalTime
										},
										 "min");
								return true;
							}
						}
						return false;
					};
					$scope.generateModel = function(){
						var data = {};
						if($scope.isPilotTypeTrain()){
							
							data.name = "P".concat($scope.selectedTrain[$scope.trains.fields.trainNo]);
							data.fromStation = $scope.selectedTrain[$scope.trains.fields.fromStation];
							data.toStation = $scope.selectedTrain[$scope.trains.fields.toStation];
							data.departureDay = $scope.selectedTrain[$scope.trains.fields.departureDay];
							data.departureTime = $scope.selectedTrain[$scope.trains.fields.departureTime];
							data.arrivalDay = $scope.selectedTrain[$scope.trains.fields.arrivalDay];
							data.arrivalTime = $scope.selectedTrain[$scope.trains.fields.arrivalTime];
							data.duration =  diffTimeObj(
									{
										day : data.departureDay,
										time : data.departureTime
									},{
										day : data.arrivalDay,
										time : data.arrivalTime
									},
									 "min");
							data.distance = $scope.selectedTrain[$scope.trains.fields.distance];
							data.pilotType = $scope.selectedPilotType._links.self.href.substring($scope.selectedPilotType._links.self.href.lastIndexOf('/')+1,$scope.selectedPilotType._links.self.href.length);
							
							("duratin " +$scope.duration);
							
						}else{
							data.name = $scope.name;
							data.fromStation = $scope.fromStation;
							data.toStation = $scope.toStation;
							data.departureDay = $scope.departureDay;
							data.departureTime = $scope.departureTime;
							data.arrivalDay = $scope.arrivalDay;
							data.arrivalTime = $scope.arrivalTime;
							data.duration = $scope.duration;
							data.distance = $scope.distance;
							data.pilotType = $scope.selectedPilotType._links.self.href.substring($scope.selectedPilotType._links.self.href.lastIndexOf('/')+1,$scope.selectedPilotType._links.self.href.length);
							("duratin " +$scope.duration);
							
						}
						return data;
					};
					
					$scope.save = function(withDuty){
						if($scope.validateForSave()){
							("saving: ");
							var data = $scope.generateModel();
						//	(data);
							if($scope.isPilotTypeTrain()){
								
							$http.post("/api/custom/pilotTrips/savePilotTrain?userPlan="+UserService.getSelectedUserPlan().id,JSON.stringify(data))
								.then(
									function(response){
										if(response.data.result){
											$scope.selectedTrain = null;
											if(withDuty){
												var data = {
														signOnDuration : $scope.signOnDuration,
														signOffDuration : $scope.signOffDuration,
														drivingDutyElements : []
													};
												var dde = {};
												dde.drivingSectionId = "";
												dde.startPilotId = (response.data.outputValue)?response.data.outputValue:"";
												dde.endPilotId = "";
												data.drivingDutyElements.push(dde);
												$http
												.post(
														'/api/custom/drivingDuties/save?userPlan='
																+ UserService
																		.getSelectedUserPlan().id,
														JSON.stringify(data))
												.then(
														function(response) {
															if (response.data.result) {
																toaster
																		.pop({
																			type : 'success',
																			title : 'Driving Duty Saved',
																			body : 'Driving Duty Saved successfully!!'
																		});
															} else {
																toaster
																		.pop({
																			type : 'error',
																			title : 'Failed to Save Driving Duty',
																			body : 'Failed to save Driving Duty !!'
																					+ response.data.errorMessage
																		});
															}
														},
														function(error) {
															toaster
																	.pop({
																		type : 'error',
																		title : 'Failed to Save Driving Duty',
																		body : 'Failed to save Driving Duty !!'
																	});
														});
												
											}
											toaster.pop({
												type : 'success',
												title : 'Successfully saved',
												body : 'Pilot trip saved successfully.'
											});
										}else{
											toaster.pop({
												type : 'error',
												title : 'Error',
												body : 'Failed to save pilot trip, please try with different value!!!'
											});
										}
									},function(response){
										toaster.pop({
											type : 'error',
											title : 'Error',
											body : 'Failed to save due to server error!!!'
										});
									}
								);
							}
							else
								{
									$http.post("/api/custom/pilotTrips/savePilot?userPlan="+UserService.getSelectedUserPlan().id,JSON.stringify(data))
									.then(
										function(response){
											if(response.data.result){
												toaster.pop({
													type : 'success',
													title : 'Successfully saved',
													body : 'Pilot trip saved successfully.'
												});
											}else{
												toaster.pop({
													type : 'error',
													title : 'Error',
													body : 'Failed to save pilot trip, please try with different value!!!'
												});
											}
										},function(response){
											toaster.pop({
												type : 'error',
												title : 'Error',
												body : 'Failed to save due to server error!!!'
											});
										}
									);	
								}
						}
					};
				
				});
