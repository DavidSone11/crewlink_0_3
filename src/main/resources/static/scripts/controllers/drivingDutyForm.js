'use strict';

/**
 * This Driving Duty Form controller is used to
 * 1. List all the Driving Sections from DB
 * 2. List all the Pilot trips from DB
 * 3. Create Driving Duty in DB  
 * 4. View all driving section and driving duties in graph from DB
 * @authors Laxman,Santosh
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 16, 2016
 */

angular
		.module('crewLinkApp')
		.controller(
				'DrivingDutyFormCtrl',
				function($scope, $q, $state, $http, $resource,
						SpringDataRestAdapter, SpringDataRestApi, UserService,
						toaster) {
					$scope.Days = Days;
					$scope.drivingSections = {};
					$scope.drivingSectionsForMany = {};
					$scope.drivingDutyElements = [];
					$scope.refreshDrivingSection = true;
					$scope.fromStationDrivingSection = "";
					$scope.toStationDrivingSection = "";
					$scope.Days = Days;
					$scope.disableFromStation = false;
					$scope.disableToStation = false;
					$scope.disableFromPilotTrip = false;
					$scope.disableToPilotTrip = false;
					UserService.config.sidebarTrigger = false;
					$scope.refreshSingleDrivingduty = true;
					$scope.signOnDuration = 0;
					$scope.signOffDuration = 0;
					$scope.startDay = null;
					$scope.startTime = null;
					$scope.multiSelect = [];

					$scope.singledrivingduty = [];
					
					$scope.updateDrivingSectionsUrl = function(tableState) {
						if(!tableState.search.predicateObject){
							tableState.search.predicateObject = {};
						}
						if (tableState.search.predicateObject) {
							if (tableState.search.predicateObject["fromStation"] != $scope.fromStationDrivingSection)
								tableState.search.predicateObject["fromStation"] = $scope.fromStationDrivingSection;
							if (tableState.search.predicateObject["toStation"] != $scope.toStationDrivingSection)
								tableState.search.predicateObject["toStation"] = $scope.toStationDrivingSection;
						}
						var uri = "/api/custom/drivingSections/list?userPlan="
								+ UserService.getSelectedUserPlan().id +
								// "&fromStation="+$scope.fromStationDrivingSection+"&toStation="+$scope.toStationDrivingSection+
								"&isDrivingDuty=false"
								+"&isIgnore=false";
						if($scope.startDay!=null && $scope.startTime!=null){
							  uri += "&sort=CONCAT(departureDay,departureTime)<'"+$scope.startDay+$scope.startTime+"',CONCAT(departureDay,departureTime)";  
						  }
						return uri;
					};
					
					/*
					 * Fetching all driving sections list
					 */
					$scope.serverFetchDrivingSections = new ServerTableFetch2(
							$scope.updateDrivingSectionsUrl.bind(this),
							SpringDataRestApi, // This is our Call Processing
							// Service currently only
							// SringDataRestApi is supported
							// and used here.
							function() { // Before processing this is called
								$scope.isLoadingDrivingSections = true;
							}, function(resultObj) { // After processing this
								// is called
								$scope.drivingSections = resultObj;
								$scope.isLoadingDrivingSections = false;
							}, function(response) {
								$scope.isLoadingDrivingSections = false;
							}, true // (isWait) To make the results load after
					// all fetching is done
					);
					$scope.updateDrivingSectionsUrlForMany = function(
							tableState) {

						var uri = "/api/custom/drivingSections/list?userPlan="
								+ UserService.getSelectedUserPlan().id +
								// "&fromStation="+$scope.fromStationDrivingSection+"&toStation="+$scope.toStationDrivingSection+
								"&isDrivingDuty=false"
								+"&isIgnore=false";
						if ($scope.startDay && $scope.startTime) {
							uri += "&sort=CONCAT(departureDay,departureTime)<'"
									+ $scope.startDay + $scope.startTime
									+ "',CONCAT(departureDay,departureTime)";

						}
						return uri;
					};

					$scope.serverFetchDrivingSectionsForMany = new ServerTableFetch2(
							$scope.updateDrivingSectionsUrlForMany.bind(this),
							SpringDataRestApi, // This is our Call Processing
							// Service currently only
							// SringDataRestApi is supported
							// and used here.
							function() { // Before processing this is called
								$scope.isLoadingDrivingSectionsForMany = true;
							}, function(resultObj) { // After processing this
								// is called
								$scope.drivingSectionsForMany = resultObj;
								$scope.isLoadingDrivingSectionsForMany = false;
							}, function(response) {
								$scope.isLoadingDrivingSectionsForMany = false;
							}, true // (isWait) To make the results load after
					// all fetching is done
					);

					$scope.fromStationPilotTrip = "";
					$scope.toStationPilotTrip = "";
					$scope.refreshPilotTrip = true;

					$scope.pilotTrips = [];

					$scope.updatePilotTripsUrl = function(tableState) {
						if(!tableState.search.predicateObject){
							tableState.search.predicateObject = {};
						}
						if (tableState.search.predicateObject) {
							if (tableState.search.predicateObject["fromStation"] != $scope.fromStationPilotTrip)
								tableState.search.predicateObject["fromStation"] = $scope.fromStationPilotTrip;
							if (tableState.search.predicateObject["toStation"] != $scope.toStationPilotTrip)
								tableState.search.predicateObject["toStation"] = $scope.toStationPilotTrip;
						}
						var uri =  "/api/custom/pilotTrips/list?userPlan="+UserService.getSelectedUserPlan().id
								+"&sort=id DESC";
						
						if($scope.selectedDrivingDutyElement.drivingSection){
							$scope.startDay = $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.departureDay];
							$scope.startTime = $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.departureTime];	
						}
						
						
						
						 if($scope.startDay!=null && $scope.startTime!=null){
							  uri += "&sort=CONCAT(arrivalDay,arrivalTime)>'"+$scope.startDay+$scope.startTime+"',CONCAT(arrivalDay,arrivalTime) DESC";
  
						  }
						  return uri;
						
					};
					  
					  $scope.serverFetchPilotTrips = new ServerTableFetch2(
							  $scope.updatePilotTripsUrl.bind(this), // Url call
							  SpringDataRestApi,			// This is our Call Processing Service currently only SringDataRestApi is supported and used here. 
							  function(){					// Before processing this is called
								  $scope.isLoadingPilotTrips = true;
							  },
							  function(resultObj){			// After processing this is called
								  $scope.pilotTrips = resultObj;
									$scope.isLoadingPilotTrips = false;
							  },
							  function(response){
								  $scope.isLoadingPilotTrips = false;
							  },
							  true //(isWait) To make the results load after all fetching is done
					  );
				      

					$scope.selectedDrivingDutyElement = {
						startPilotTrip : null,
						drivingSection : null,
						endPilotTrip : null
					};
					$scope.selectedDrivingDuty = {
						_ref : {
							startPilotTrip : null,
							drivingSection : null,
							endPilotTrip : null
						}
					};

					$scope.addDrivingSectionOfMany = function(drivingSection) {

					}

					$scope.addDrivingSection = function(drivingSection) {
						if ((($scope.selectedDrivingDutyElement.drivingSection != null) && ($scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.id] != drivingSection[$scope.drivingSections.fields.id]))
								|| ($scope.selectedDrivingDutyElement.drivingSection == null)) {
							$scope.selectedDrivingDutyElement.drivingSection = drivingSection;
							$scope.toStationPilotTrip = drivingSection[$scope.drivingSections.fields.fromStation];
							$scope.fromStationPilotTrip = drivingSection[$scope.drivingSections.fields.toStation];
							$scope.refreshPilotTrip = ($scope.refreshPilotTrip) ? false
									: true;

							// clear Start Pilot
							if ($scope.selectedDrivingDutyElement.startPilotTrip) {
								if ($scope.selectedDrivingDutyElement.startPilotTrip[$scope.pilotTrips.fields.toStation]!= $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.fromStation]) {
									$scope.selectedDrivingDutyElement.startPilotTrip = null;
								}
							}

							// clear end pilot
							if ($scope.selectedDrivingDutyElement.endPilotTrip) {
								if ($scope.selectedDrivingDutyElement.endPilotTrip[$scope.pilotTrips.fields.fromStation]!= $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.toStation]) {
									$scope.selectedDrivingDutyElement.endPilotTrip = null;
								}
							}
						}
					};

					$scope.addStartPilotTrip = function(pilotTrip) {
						$scope.selectedDrivingDutyElement.startPilotTrip = pilotTrip;
						$scope.fromStationDrivingSection = pilotTrip[$scope.pilotTrips.fields.toStation];
						$scope.refreshDrivingSection = ($scope.refreshDrivingSection) ? false
								: true;
					};
					$scope.addEndPilotTrip = function(pilotTrip) {
						$scope.selectedDrivingDutyElement.endPilotTrip = pilotTrip;
						$scope.toStationDrivingSection = pilotTrip[$scope.pilotTrips.fields.fromStation];
						$scope.refreshDrivingSection = ($scope.refreshDrivingSection) ? false
								: true;
					};

					$scope.removeDrivingSection = function() {
						$scope.selectedDrivingDutyElement.drivingSection = null;
						$scope.toStationPilotTrip = "";
						$scope.fromStationPilotTrip = "";
						$scope.refreshPilotTrip = ($scope.refreshPilotTrip) ? false
								: true;

						// $scope.disableFromStation=false;
						// $scope.disableToStation=false;
					};

					$scope.removeStartPilotTrip = function() {
						$scope.selectedDrivingDutyElement.startPilotTrip = null;
						$scope.fromStationDrivingSection = "";
						$scope.refreshDrivingSection = ($scope.refreshDrivingSection) ? false
								: true;

						// $scope.disableFromPilotTrip=false;
						// $scope.disableToPilotTrip=false;
					};

					$scope.removeEndPilotTrip = function() {
						$scope.selectedDrivingDutyElement.endPilotTrip = null;
						$scope.toStationDrivingSection = "";
						$scope.refreshDrivingSection = ($scope.refreshDrivingSection) ? false
								: true;

						// $scope.disableFromPilotTrip=false;
						// $scope.disableToPilotTrip=false;
					};

					$scope.changeStartTime = function(time) {
						$scope.selectedDrivingDutyElement.startTime = ((time
								.getHours() < 10) ? '0' : '')
								+ time.getHours()
								+ ":"
								+ ((time.getMinutes() < 10) ? '0' : '')
								+ time.getMinutes();
					};

					$scope.addDrivingDutyElement = function() {
						if (!$scope.selectedDrivingDutyElement.startPilotTrip
								&& !$scope.selectedDrivingDutyElement.drivingSection
								&& !$scope.selectedDrivingDutyElement.endPilotTrip) {
							toaster.pop({
								type : 'error',
								title : 'Error',
								body : 'Please choose a Driving Section !!!'
							});
							return;
						}
						if ($scope.selectedDrivingDutyElement.startPilotTrip
								&& !$scope.selectedDrivingDutyElement.drivingSection
								&& $scope.selectedDrivingDutyElement.endPilotTrip) {
							$scope.disableFromStation = false;
							$scope.disableToStation = false;
							$scope.disableFromPilotTrip = true;
							$scope.disableToPilotTrip = true;
							toaster.pop({
								type : 'error',
								title : 'Error',
								body : 'Please choose a Driving Section !!!'
							});
							return;
						}
						// ONLY START PILOT TRIP
						if ($scope.selectedDrivingDutyElement.startPilotTrip
								&& !$scope.selectedDrivingDutyElement.drivingSection
								&& !$scope.selectedDrivingDutyElement.endPilotTrip) {
							
							$scope.selectedDrivingDutyElement.fromStation 		= $scope.selectedDrivingDutyElement.startPilotTrip[$scope.pilotTrips.fields.fromStation];
							$scope.selectedDrivingDutyElement.toStation 		= $scope.selectedDrivingDutyElement.startPilotTrip[$scope.pilotTrips.fields.toStation];
							$scope.selectedDrivingDutyElement.duration 			= $scope.selectedDrivingDutyElement.startPilotTrip[$scope.pilotTrips.fields.duration];
							$scope.selectedDrivingDutyElement.distance 			= $scope.selectedDrivingDutyElement.startPilotTrip[$scope.pilotTrips.fields.distance];
							$scope.selectedDrivingDutyElement.departureDay 		= $scope.selectedDrivingDutyElement.startPilotTrip[$scope.pilotTrips.fields.departureDay];
							$scope.selectedDrivingDutyElement.departureTime 	= $scope.selectedDrivingDutyElement.startPilotTrip[$scope.pilotTrips.fields.departureTime];
							$scope.selectedDrivingDutyElement.arrivalDay 		= $scope.selectedDrivingDutyElement.startPilotTrip[$scope.pilotTrips.fields.arrivalDay];
							$scope.selectedDrivingDutyElement.arrivalTime 		= $scope.selectedDrivingDutyElement.startPilotTrip[$scope.pilotTrips.fields.arrivalTime];

						}
						// ONLY END PILOT TRIP
						else if (!$scope.selectedDrivingDutyElement.startPilotTrip
								&& !$scope.selectedDrivingDutyElement.drivingSection
								&& $scope.selectedDrivingDutyElement.endPilotTrip) {
							toaster.pop({
								type : 'error',
								title : 'Error',
								body : 'Please choose a Driving Section !!!'
							});
							return;
						}
						/* ONLY DRIVING SECTION */
						else if (!$scope.selectedDrivingDutyElement.startPilotTrip
								&& $scope.selectedDrivingDutyElement.drivingSection
								&& !$scope.selectedDrivingDutyElement.endPilotTrip) {
							$scope.selectedDrivingDutyElement.fromStation 		= $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.fromStation];
							$scope.selectedDrivingDutyElement.toStation 		= $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.toStation];
							$scope.selectedDrivingDutyElement.duration 			= $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.duration];
							$scope.selectedDrivingDutyElement.distance 			= $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.distance];
							$scope.selectedDrivingDutyElement.departureDay 		= $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.departureDay];
							$scope.selectedDrivingDutyElement.departureTime 	= $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.departureTime];
							$scope.selectedDrivingDutyElement.arrivalDay 		= $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.arrivalDay];
							$scope.selectedDrivingDutyElement.arrivalTime 		= $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.arrivalTime];

						}
						/* START PILOT AND DRIVING SECTION */
						else if ($scope.selectedDrivingDutyElement.startPilotTrip
								&& $scope.selectedDrivingDutyElement.drivingSection
								&& !$scope.selectedDrivingDutyElement.endPilotTrip) {
							$scope.selectedDrivingDutyElement.fromStation 		= $scope.selectedDrivingDutyElement.startPilotTrip[$scope.pilotTrips.fields.fromStation];
							$scope.selectedDrivingDutyElement.toStation 		= $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.toStation];
							
							$scope.selectedDrivingDutyElement.distance 			= $scope.selectedDrivingDutyElement.startPilotTrip[$scope.pilotTrips.fields.distance]+ $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.distance];
					
							$scope.selectedDrivingDutyElement.departureDay 		= $scope.selectedDrivingDutyElement.startPilotTrip[$scope.pilotTrips.fields.departureDay];
							$scope.selectedDrivingDutyElement.departureTime 	= $scope.selectedDrivingDutyElement.startPilotTrip[$scope.pilotTrips.fields.departureTime];
							$scope.selectedDrivingDutyElement.arrivalDay 		= $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.arrivalDay];
							$scope.selectedDrivingDutyElement.arrivalTime 		= $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.arrivalTime];
							

						}
						/* DRIVING SECTION AND END PILOT */
						else if (!$scope.selectedDrivingDutyElement.startPilotTrip
								&& $scope.selectedDrivingDutyElement.drivingSection
								&& $scope.selectedDrivingDutyElement.endPilotTrip) {
							$scope.selectedDrivingDutyElement.fromStation 		= $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.fromStation];
							$scope.selectedDrivingDutyElement.toStation 		= $scope.selectedDrivingDutyElement.endPilotTrip[$scope.pilotTrips.fields.toStation];
							$scope.selectedDrivingDutyElement.distance 			= $scope.selectedDrivingDutyElement.endPilotTrip[$scope.pilotTrips.fields.distance]+ $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.distance];
							$scope.selectedDrivingDutyElement.departureDay 		= $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.departureDay];
							$scope.selectedDrivingDutyElement.departureTime 	= $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.departureTime];
							$scope.selectedDrivingDutyElement.arrivalDay 		= $scope.selectedDrivingDutyElement.endPilotTrip[$scope.pilotTrips.fields.arrivalDay];
							$scope.selectedDrivingDutyElement.arrivalTime 		= $scope.selectedDrivingDutyElement.endPilotTrip[$scope.pilotTrips.fields.arrivalTime];
							
						}
						/* WHEN ALL Are There */
						else if ($scope.selectedDrivingDutyElement.startPilotTrip
								&& $scope.selectedDrivingDutyElement.drivingSection
								&& $scope.selectedDrivingDutyElement.endPilotTrip) {
							$scope.selectedDrivingDutyElement.fromStation 		= $scope.selectedDrivingDutyElement.startPilotTrip[$scope.pilotTrips.fields.fromStation];
							$scope.selectedDrivingDutyElement.toStation 		= $scope.selectedDrivingDutyElement.endPilotTrip[$scope.pilotTrips.fields.toStation];
							$scope.selectedDrivingDutyElement.distance 			= $scope.selectedDrivingDutyElement.startPilotTrip[$scope.pilotTrips.fields.distance]+ $scope.selectedDrivingDutyElement.endPilotTrip[$scope.pilotTrips.fields.distance]
																					+ $scope.selectedDrivingDutyElement.drivingSection[$scope.drivingSections.fields.distance];
							$scope.selectedDrivingDutyElement.departureDay 		= $scope.selectedDrivingDutyElement.startPilotTrip[$scope.pilotTrips.fields.departureDay];
							$scope.selectedDrivingDutyElement.departureTime		= $scope.selectedDrivingDutyElement.startPilotTrip[$scope.pilotTrips.fields.departureTime];
							$scope.selectedDrivingDutyElement.arrivalDay 		= $scope.selectedDrivingDutyElement.endPilotTrip[$scope.pilotTrips.fields.arrivalDay];
							$scope.selectedDrivingDutyElement.arrivalTime 		= $scope.selectedDrivingDutyElement.endPilotTrip[$scope.pilotTrips.fields.arrivalTime];

						}
						$scope.selectedDrivingDutyElement.duration 			= diffTimeObj(
								{
									day : $scope.selectedDrivingDutyElement.departureDay,
									time : $scope.selectedDrivingDutyElement.departureTime
								},{
									day : $scope.selectedDrivingDutyElement.arrivalDay,
									time : $scope.selectedDrivingDutyElement.arrivalTime
								},
								 "min");
						$scope.startDay = $scope.selectedDrivingDutyElement.arrivalDay;
						$scope.startTime = $scope.selectedDrivingDutyElement.arrivalTime;
						$scope.disableFromStation = true;
						$scope.disableToStation = false;
						$scope.disableFromPilotTrip = true;
						$scope.disableToPilotTrip = false;

						$scope.drivingDutyElements.push(angular
								.copy($scope.selectedDrivingDutyElement));

						// Remove Driving Duty Element & Filter Records
						// According To Just Created Duty Element
						$scope.selectedDrivingDutyElement = {
							startPilotTrip : null,
							drivingSection : null,
							endPilotTrip : null
						};

						$scope.toStationPilotTrip = "";
						$scope.fromStationPilotTrip = $scope.drivingDutyElements[$scope.drivingDutyElements.length - 1].toStation;
						$scope.refreshPilotTrip = ($scope.refreshPilotTrip) ? false
								: true;

						$scope.fromStationDrivingSection = $scope.drivingDutyElements[$scope.drivingDutyElements.length - 1].toStation;
						$scope.toStationDrivingSection = "";
						$scope.refreshDrivingSection = ($scope.refreshDrivingSection) ? false
								: true;

					}
					$scope.calDiffTwoTimes = function(availableTime, signOnTime) {
						var avlTime = availableTime.split(":");
						var avlTotalMins = parseInt(avlTime[0]) * 60
								+ parseInt(avlTime[1]);
						var soTime = signOnTime.split(":");
						var soTotalMins = parseInt(soTime[0]) * 60
								+ parseInt(soTime[1]);
						if (avlTotalMins > soTotalMins) {
							soTotalMins += 1440 - avlTotalMins;
						} else if (avlTotalMins < soTotalMins) {
							soTotalMins -= avlTotalMins;
						} else {
							soTotalMins = 0;
						}
						var mins = Math
								.round(parseInt((((soTotalMins / 60) % 1)
										.toFixed(2).substring(2))) * 0.6);
						mins = (mins < 10) ? "0" + mins : mins;
						var hrs = (Math.floor(soTotalMins / 60) >= 24) ? Math
								.floor(soTotalMins / 60) - 24 : Math
								.floor(soTotalMins / 60);
						soTotalMins = hrs + ":" + mins;
						return soTotalMins;
					}
					$scope.saveDrivingDuty = function() {
						var data = {
							signOnDuration : $scope.signOnDuration,
							signOffDuration : $scope.signOffDuration,
							drivingDutyElements : []
						};
						for (var i = 0; i < $scope.drivingDutyElements.length; i++) {
							var dde = {};
							if ($scope.drivingDutyElements[i].drivingSection) {
								dde.drivingSectionId = $scope.drivingDutyElements[i].drivingSection[$scope.drivingSections.fields.id];
							} else {
								dde.drivingSectionId = "";
							}
							if ($scope.drivingDutyElements[i].startPilotTrip) {
								var id = $scope.drivingDutyElements[i].startPilotTrip[$scope.pilotTrips.fields.id];
								dde.startPilotId = id;

							} else {
								dde.startPilotId = "";
							}
							if ($scope.drivingDutyElements[i].endPilotTrip) {
								var id = $scope.drivingDutyElements[i].endPilotTrip[$scope.pilotTrips.fields.id];
								dde.endPilotId = id;
							} else {
								dde.endPilotId = "";
							}
//							if ($scope.drivingDutyElements[i].startDay
//									&& $scope.drivingDutyElements[i].startTime) {
//								dde.startDay = $scope.drivingDutyElements[i].startDay;
//								dde.startTime = $scope.drivingDutyElements[i].startTime;
//							} else {
//								dde.startDay = "";
//								dde.startTime = "";
//							}

							data.drivingDutyElements.push(dde);
						}

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
											$scope.drivingDutyElements = [];
											$scope.disableFromStation = false;
											$scope.disableToStation = false;
											$scope.disableFromPilotTrip = false;
											$scope.disableToPilotTrip = false;
											$scope.fromStationDrivingSection = "";
											$scope.toStationDrivingSection = "";
											$scope.fromStationPilotTrip = "";
											$scope.toStationPilotTrip = "";
											$scope.refreshDrivingSection = ($scope.refreshDrivingSection) ? false
													: true;
											$scope.refreshPilotTrip = ($scope.refreshPilotTrip) ? false
													: true;

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
					$scope.isDrivingSectionManySaved = function(drivingSection){
						if($scope.drivingSections.data[0].length == drivingSection.length){
							drivingSection.push(false);
							drivingSection.push(false);
							return false;
						}else if($scope.drivingSections.data[0].length+1 == drivingSection.length){
							drivingSection.push(false);
							return false;
						}
						else{
							return drivingSection[drivingSection.length-1];
						}
					};
					
					$scope.saveMultipleDrivingDuty = function() {
						$scope.drivingDutyElements = [];
						var data = {
								signOnDuration : $scope.signOnDuration,
								signOffDuration : $scope.signOffDuration,
								drivingSections : []
							};
						for(var i = 0; i<$scope.drivingSectionsForMany.data.length;i++){
							if ($scope.drivingSectionsForMany.data[i][$scope.drivingSectionsForMany.data[i].length - 2]) { // check if the selection field (ie. second last is true or false
								data.drivingSections.push($scope.drivingSectionsForMany.data[i][$scope.drivingSections.fields.id]);
							}
						}
						$http
						.post(
								'/api/custom/drivingDuties/saveMany?userPlan='
										+ UserService
												.getSelectedUserPlan().id,
								JSON
										.stringify(data))
						.then(
								function(
										response) {
									if (response.data.result) {
										toaster
												.pop({
													type : 'success',
													title : 'Many Driving Duty Saved',
													body : 'Many Driving Duty Saved successfully!!'
												});
										$scope.refreshDrivingSection = !$scope.refreshDrivingSection;
									} else {
										toaster
												.pop({
													type : 'error',
													title : 'Failed to Save Driving Duty',
													body : 'Failed to save Driving Duty !!'
															+ response.data.errorMessage
												});
										$scope.refreshDrivingSection = !$scope.refreshDrivingSection;
									}
									

								}.bind(this),
								function(error) {
									toaster
											.pop({
												type : 'error',
												title : 'Failed to Save Driving Duty',
												body : 'Failed to save Driving Duty !!'
											});
								}.bind(this));
									
							
					}

					$scope.removeLastDrivingDutyElement = function() {
						if ($scope.drivingDutyElements.length == 1) {
							$scope.drivingDutyElements.splice(
									$scope.drivingDutyElements.length - 1, 1);
							$scope.disableFromStation = false;
							$scope.disableToStation = false;
							$scope.disableFromPilotTrip = false;
							$scope.disableToPilotTrip = false;
							$scope.toStationPilotTrip = "";
							$scope.fromStationDrivingSection = "";
							$scope.fromStationPilotTrip = "";
							$scope.refreshPilotTrip = ($scope.refreshPilotTrip) ? false
									: true;
							$scope.refreshDrivingSection = ($scope.refreshDrivingSection) ? false
									: true;
						} else {

							if ($scope.drivingDutyElements.length > 1) {
								$scope.drivingDutyElements.splice(
										$scope.drivingDutyElements.length - 1,
										1);
								$scope.toStationPilotTrip = "";
								//$scope.fromStationPilotTrip = $scope.drivingDutyElements[$scope.drivingDutyElements.length - 1]._ref.toStation.code;
								
								$scope.fromStationPilotTrip = $scope.drivingDutyElements[$scope.drivingDutyElements.length - 1].drivingSection[$scope.drivingSections.fields.toStation];
								//$scope.fromStationDrivingSection = $scope.drivingDutyElements[$scope.drivingDutyElements.length - 1]._ref.toStation.code;
								$scope.fromStationDrivingSection = $scope.drivingDutyElements[$scope.drivingDutyElements.length - 1].drivingSection[$scope.drivingSections.fields.toStation];
								
								$scope.toStationDrivingSection = "";

								$scope.refreshPilotTrip = ($scope.refreshPilotTrip) ? false
										: true;
								$scope.refreshDrivingSection = ($scope.refreshDrivingSection) ? false
										: true;
							} else {
								$scope.toStationPilotTrip = $scope.fromStationPilotTrip = $scope.fromStationDrivingSection = $scope.toStationDrivingSection = "";

								$scope.refreshPilotTrip = ($scope.refreshPilotTrip) ? false
										: true;
								$scope.refreshDrivingSection = ($scope.refreshDrivingSection) ? false
										: true;
							}
						}

					}
					$scope.resetDrivingDutyForm = function() {
						$scope.drivingDutyElements = {
							data : [],
							fields : {}
						};
						$scope.toStationPilotTrip = "";
						$scope.fromStationPilotTrip = "";

						$scope.fromStationDrivingSection = "";
						$scope.toStationDrivingSection = "";

						$scope.refreshPilotTrip = ($scope.refreshPilotTrip) ? false
								: true;
						$scope.refreshDrivingSection = ($scope.refreshDrivingSection) ? false
								: true;
					}
					$scope.setDrivingDutyElementStartDay = function(selectedDay) {
						$scope.selectedDrivingDutyElement.startDay = selectedDay;
					}

					$scope.isDrivingSectionUnused = function(drivingSection) {

						for (var i = 0; i < $scope.drivingDutyElements.length; i++) {
							if ($scope.drivingDutyElements[i].drivingSection)
								if ($scope.drivingDutyElements[i].drivingSection[$scope.drivingSections.fields.id] == drivingSection[$scope.drivingSections.fields.id])
									return false;
						}
						return true;
					}

					$scope.toggleAll = function() {
						var toggleStatus = !$scope.isAllSelected;
						angular.forEach($scope.drivingSectionsForMany.data,
								function(itm) {
									itm[itm.length-2] = toggleStatus;
								});

					}

					$scope.saveSingleDrivingDuty = function() {
						$scope.drivingSectionSelected = [];
						var counter = 0;
						angular
								.forEach(
										$scope.drivingSections,
										function(drivingSectionsResponse) {
											if (!!drivingSectionsResponse.selected) {
												$scope.drivingSectionSelected
														.push(drivingSectionsResponse);

												$scope.drivingDuty = {
													_ref : {}
												};
												$scope.drivingDuty.ddName = drivingSectionsResponse._ref.train.trainNo;
												$scope.drivingDuty.distance = drivingSectionsResponse.distance;
												$scope.drivingDuty.duration = drivingSectionsResponse.duration;
												$scope.drivingDuty._ref.fromStation = drivingSectionsResponse._ref.fromStation;
												$scope.drivingDuty._ref.toStation = drivingSectionsResponse._ref.toStation;
												$scope.drivingDuty.startDay = drivingSectionsResponse.startDay;
												$scope.drivingDuty.startTime = drivingSectionsResponse.startTime;
												$scope.drivingDuty._ref.userPlan = UserService
														.getSelectedUserPlan();

												SpringDataRestApi
														.save(
																$scope.drivingDuty,
																'drivingDuties')
														.then(
																function(
																		drivingDutySaveSuccessResponse) {
																	toaster
																			.pop({
																				type : 'success',
																				title : 'Driving Duty Saved',
																				body : 'Driving Duty Saved successfully!!'
																			});
																	console
																			.log(drivingDutySaveSuccessResponse.data);
																	$scope.drivingDutyElement = {
																		_ref : {}
																	};
																	$scope.drivingDutyElement.distance = drivingSectionsResponse.distance;
																	$scope.drivingDutyElement.duration = drivingSectionsResponse.duration;
																	$scope.drivingDutyElement._ref.fromStation = drivingSectionsResponse._ref.fromStation;
																	$scope.drivingDutyElement._ref.toStation = drivingSectionsResponse._ref.toStation;
																	$scope.drivingDutyElement.startDay = drivingSectionsResponse.startDay;
																	$scope.drivingDutyElement.startTime = drivingSectionsResponse.startTime;
																	$scope.drivingDutyElement._ref.userPlan = UserService
																			.getSelectedUserPlan();
																	$scope.drivingDutyElement._ref.drivingDuty = drivingDutySaveSuccessResponse.data;
																	$scope.drivingDutyElement.drivingDutyOrderNo = 1;
																	$scope.drivingSectionObj = {
																		_ref : {}
																	};

																	/*
																	 * SpringDataRestApi.save($scope.drivingDutyElement,
																	 * 'drivingDutyElements').then(function(drivingDutyElementSaveSuccessResponse) {
																	 * 
																	 * (drivingDutyElementSaveSuccessResponse);
																	 * });
																	 */

																	SpringDataRestApi
																			.save(
																					$scope.drivingDutyElement,
																					'drivingDutyElements')
																			.then(
																					function(
																							drivingDutyElementSaveSuccessResponse) {
																						drivingSectionsResponse._ref.drivingDutyElement = drivingDutyElementSaveSuccessResponse.data;
																						SpringDataRestApi
																								.save(
																										drivingSectionsResponse,
																										'drivingSections')
																								.then(
																										function(
																												drivingSectionPutSuccessResponse) {
																											counter++;
																											console
																													.log("counter : "
																															+ counter);
																											if (counter == $scope.drivingSections.length) {
																												$scope.isAllSelected = false;
																											}
																											$scope.refreshSingleDrivingduty = ($scope.refreshSingleDrivingduty) ? false
																													: true;
																										});

																					})
																			.then(
																					function(
																							ErrorResponse) {
																						// (ErrorResponse)
																					});

																})
														.then(
																function(
																		ErrorResponse) {
																	console
																			.log(ErrorResponse)

																});
											}
										});

					};

					$scope.noOfTrainsLimit = 1000;
					/**
					 * This function is used to load driving sections and driving duties on chart
					 */
					$scope.loadDrivingSectionsChart = function() {
						angular
						.element(
								document
										.querySelector('#drivingSectionsForSingleStation'))
						.empty();
						$scope.isChartLoading = true;
						$scope.isDrivingSectionsFound = true;
						if($scope.chartStationCode == undefined || $scope.chartStationCode == "" || $scope.chartStationCode == null){
							$scope.isChartLoading = false;
							$scope.isDrivingSectionsFound = false;
							toaster
							.pop({
								type : 'error',
								title : 'Error',
								body : 'Enter Staton Code!!!'
							});
						}
						else
						{
							// server call to fetch all driving sections which are going from selected station
							$http
									.get(
											"/api/custom/drivingSections/list?userPlan="
											+ UserService.getSelectedUserPlan().id
													+ "&fromStation="
													+ $scope.chartStationCode
													+ "&page=0&size="
													+ $scope.noOfTrainsLimit
													+"&isIgnore=false")
									.then(
											function(drivingSectionsFrom) {
												// call to get all driving sections
												// which are coming to selected
												// station
												$http
														.get(
																"/api/custom/drivingSections/list?userPlan="
																		+ UserService
																				.getSelectedUserPlan().id
																		+ "&toStation="
																		+ $scope.chartStationCode
																		+ "&page=0&size="
																		+ $scope.noOfTrainsLimit
																		+"&isIgnore=false")
														.then(
																function(
																		drivingSectionsTo) {
																	$scope.drivingSectionsFrom = drivingSectionsFrom.data;
																	$scope.drivingSectionsTo = drivingSectionsTo.data;
																	($scope.drivingSectionsFrom);
																	($scope.drivingSectionsTo);
																	if ($scope.drivingSectionsFrom.data == null
																			&& $scope.drivingSectionsTo.data == null)
																		$scope.isDrivingSectionsFound = false;
																	else {
																		$scope.chartStationCode = $scope.drivingSectionsTo.data[0][$scope.drivingSectionsTo.fields.toStation];
																		//Logic for arrival driving sections List
																		angular
																				.forEach(
																						$scope.drivingSectionsTo.data,
																						function(
																								drivingSection) {
																							drivingSection[$scope.drivingSectionsFrom.fields.departureTime] = "";
																							drivingSection[$scope.drivingSectionsFrom.fields.departureDay] = "";
																							drivingSection[$scope.drivingSectionsFrom.fields.arrivalDay] = 
																								Days[drivingSection[$scope.drivingSectionsFrom.fields.arrivalDay]];
																							
																						});
																		//Logic for departure driving sections List
																		angular
																				.forEach(
																						$scope.drivingSectionsFrom.data,
																						function(
																								drivingSection) {
																							drivingSection[$scope.drivingSectionsFrom.fields.arrivalTime] = "";
																							drivingSection[$scope.drivingSectionsFrom.fields.arrivalDay] = "";
																							drivingSection[$scope.drivingSectionsFrom.fields.departureDay] = 
																								Days[drivingSection[$scope.drivingSectionsFrom.fields.departureDay]];
																						});
																		var dataLength = $scope.drivingSectionsTo.data[0].length;
																		$scope.drivingSectionsTo.fields.toTrainNo = dataLength;
																		$scope.drivingSectionsTo.fields.toDuration = dataLength+1;
																		$scope.drivingSectionsTo.fields.duty = dataLength+2;
																		//Logic for driving duties List
																		angular
																				.forEach(
																						$scope.drivingSectionsTo.data,
																						function(
																								drivingSectionTo,
																								index) {
																							angular
																									.forEach(
																											$scope.drivingSectionsFrom.data,
																											function(
																													drivingSectionFrom,
																													key) {
																												if (drivingSectionTo[$scope.drivingSectionsTo.fields.isDrivingDuty] == 1
																														&& drivingSectionFrom[$scope.drivingSectionsFrom.fields.isDrivingDuty] == 1)
																													if (drivingSectionTo[$scope.drivingSectionsTo.fields.drivingDutyId] == 
																															drivingSectionFrom[$scope.drivingSectionsFrom.fields.drivingDutyId]) {
																														drivingSectionTo[$scope.drivingSectionsTo.fields.departureTime] = drivingSectionFrom[$scope.drivingSectionsFrom.fields.departureTime];
																														drivingSectionTo[$scope.drivingSectionsTo.fields.departureDay] = drivingSectionFrom[$scope.drivingSectionsFrom.fields.departureDay];
																														drivingSectionTo.push(drivingSectionFrom[$scope.drivingSectionsFrom.fields.trainNo]);
																														drivingSectionTo[$scope.drivingSectionsTo.fields.toStation] = drivingSectionFrom[$scope.drivingSectionsFrom.fields.fromStation];
																														drivingSectionTo.push(drivingSectionFrom[$scope.drivingSectionsFrom.fields.duration]);
																														drivingSectionTo.push(true);
																														drivingSectionFrom[$scope.drivingSectionsFrom.fields.departureTime] = "";
																														drivingSectionFrom[$scope.drivingSectionsFrom.fields.departureDay] = "";
																													}
																											});
																						});
																		$scope.toggleSections = true;
																		$scope.toggleDuties = true;
																		$scope
																				.generateChart(
																						$scope.toggleSections,
																						$scope.toggleDuties);
																	}
																	$scope.isChartLoading = false;
																	if (!$scope.isDrivingSectionsFound)
																		toaster
																				.pop({
																					type : 'error',
																					title : 'Error',
																					body : 'No Driving Sections found from and to '
																							+ $scope.chartStationCode
																							+ ' station!!!'
																				});
																});
											});
						}
					}
					
					/**
					 * This function is used to generate graph with filtered Driving sections list 
					 */
					$scope.generateChart = function(isDrivingSection,
							isDrivingDuty) {
						angular
								.element(
										document
												.querySelector('#drivingSectionsForSingleStation'))
								.empty();
						var drivingSectionsTo = $scope.drivingSectionsTo.data, drivingSectionsFrom = $scope.drivingSectionsFrom.data;
						//If both legends are unselected
						if (!isDrivingSection && !isDrivingDuty) {
							drivingSectionsTo = [];
							drivingSectionsFrom = [];
						} else if (!isDrivingSection) {//If sections legend is unselected
							var temp = [];
							//Logic to filter all driving sections with driving duties
							angular.forEach($scope.drivingSectionsTo.data, function(
									drivingSection) {
								if (drivingSection[$scope.drivingSectionsTo.fields.isDrivingDuty] == 1)
									temp.push(drivingSection);
							});
							angular
									.forEach(
											$scope.drivingSectionsFrom.data,
											function(drivingSection) {
												if (drivingSection[$scope.drivingSectionsFrom.fields.isDrivingDuty] == 1)
													temp.push(drivingSection);
											});
							drivingSectionsTo = temp;
							drivingSectionsFrom = [];
						} else if (!isDrivingDuty) {//If duties legend is unselected
							var temp = [];
							//Logic to filter all driving sections without driving duties
							angular.forEach($scope.drivingSectionsTo.data, function(
									drivingSection) {
								if (drivingSection[$scope.drivingSectionsTo.fields.isDrivingDuty] != 1)
									temp.push(drivingSection);
							});
							angular
									.forEach(
											$scope.drivingSectionsFrom.data,
											function(drivingSection) {
												if (drivingSection[$scope.drivingSectionsTo.fields.isDrivingDuty] != 1)
													temp.push(drivingSection);
											});
							drivingSectionsTo = temp;
							drivingSectionsFrom = [];
						}
						var linechart, mydata;
						var color, background, foreground, ordinal_labels, projection;
						var mytime, myhr, mymin, myday, val, ch, i, j, k;
						var sortedItems;
						var parseTime = d3.time.format("%H:%M").parse;
						// Define the div for the tooltip
						var div = d3.select("body").append("div").attr("class",
								"tooltip").style("opacity", 0);
						var marg = {
							top : 50,
							left : 250,
							bottom : 40,
							right : 40
						};
						var width = 650 - marg.left - marg.right, height = 3520
								- marg.top - marg.bottom, finalWidth = width
								+ marg.left + marg.right + 300, finalHeight = height
								+ marg.top + marg.bottom + 100;
						var p = d3.select("#drivingSectionsForSingleStation")
								.append("svg").attr("width", finalWidth).attr(
										"height", finalHeight).append("g")
								.attr(
										"transform",
										"translate(" + marg.left + ","
												+ marg.top + ")");
						var plotdays = [ [ "Sunday", 24 ], [ "Monday", 48 ],
								[ "Tuesday", 72 ], [ "Wednesday", 96 ],
								[ "Thursday", 120 ], [ "Friday", 144 ],
								[ "Saturday", 168 ] ];
						var dim_plot = [ {
							name : $scope.chartStationCode + "-ARR",
							scale : d3.scale.linear().range([ 0, height ]),
							type : "Time"
						}, {
							name : $scope.chartStationCode + "-DEP",
							scale : d3.scale.linear().range([ 0, height ]),
							type : "Time"
						}, ];

						var x_axis = d3.scale.ordinal().rangePoints(
								[ 0, width ]).domain(dim_plot.map(function(d) {
							return d.name;
						}));
						var y_Axis = d3.svg.axis().orient("left").ticks(168);
						var other_yaxis = d3.scale.linear()
								.range([ 0, height ]).domain([ 0, 168 ]);
						var fun_yaxis = d3.scale.linear().range([ 0, height ])
								.domain([ 0, 24 ]);
						var lineGen = d3.svg.line().defined(function(d) {
							return !isNaN(d[1]);
						});
						var newline = d3.svg.line().x(function(d, i) {
							return d.x
						}).y(function(d) {
							return d.y
						});
						var dimension = p.selectAll(".dimension")
								.data(dim_plot).enter().append("g").attr(
										"class", "dimension").attr(
										"transform",
										function(d) {
											return "translate("
													+ x_axis(d.name) + ")";
										});

						clearall();
						sortedItems = [ "Duties" ];
						// #0966EA #d62728
						color = d3.scale.category10().domain(sortedItems);
						weekly_plotting(drivingSectionsFrom);
						weekly_plotting(drivingSectionsTo);

						function weekly_plotting(mydata) {
							dim_plot.forEach(function(p) {
								p.scale.domain([ 0, 168 ]);
							});

							p.append("g").attr("class", "background")
									.selectAll("path").data(mydata).enter()
									.append("path").attr("d", draw);

							linechart = p.append("g").attr("class",
									"foreground").selectAll("path")
									.data(mydata).enter();

							linechart.append("path").attr("d", draw).attr(
									"stroke", function(d) {
										return "green";
									}).attr("stroke-width", function(d) {
								if (d[$scope.drivingSectionsTo.fields.duty])
									return 1;
								else
									return 0;
							});

							dimension.append("g").attr("class", "axis").each(
									function(d) {
										d3.select(this).call(
												y_Axis.scale(d.scale));
									}).append("text").attr("text-anchor",
									"middle").attr("y", -9).text(function(d) {
								return d.name;
							});

							// to add legend
							mylegend();

							// add details on ARR axis
							p.selectAll(".text").data(mydata).enter().append(
									"g").attr("class", "foreground").append(
									"text").attr("x", -200).attr(
									"y",
									function(d) {
										myday = d[$scope.drivingSectionsTo.fields.arrivalDay];
										mytime = d[$scope.drivingSectionsTo.fields.arrivalTime];
										var val = null;
										if (mytime) {
											myhr = parseFloat(mytime
													.slice(0, 2));
											mymin = parseInt(mytime.slice(-2));
											val = theswitch(mytime, myday);
										}
										return other_yaxis(val) - 3;
									}).text(
									function(d) {
										var myvalues = [
												d[$scope.drivingSectionsTo.fields.fromStation],
												d[$scope.drivingSectionsTo.fields.trainNo],
												d[$scope.drivingSectionsTo.fields.arrivalTime],
												"(" + d[$scope.drivingSectionsTo.fields.duration] + "m)" ];
										return (d[$scope.drivingSectionsTo.fields.arrivalTime]) ? myvalues : "";
									});

							// add details on DEP axis
							p.selectAll(".text").data(mydata).enter().append(
									"g").attr("class", "foreground").append(
									"text").attr("x", 360).attr(
									"y",
									function(d) {
										myday = d[$scope.drivingSectionsTo.fields.departureDay];
										mytime = d[$scope.drivingSectionsTo.fields.departureTime];
										var val = null;
										if (mytime) {
											myhr = parseFloat(mytime
													.slice(0, 2));
											mymin = parseInt(mytime.slice(-2));
											val = theswitch(mytime, myday);
										}
										return other_yaxis(val) - 3;
									}).text(
									function(d) {
										var myvalues = [
												"(" + d[$scope.drivingSectionsTo.fields.duration] + "m)",
												d[$scope.drivingSectionsTo.fields.departureTime],
												d[$scope.drivingSectionsTo.fields.trainNo],
												d[$scope.drivingSectionsTo.fields.toStation] ];
										if (d[$scope.drivingSectionsTo.fields.arrivalTime] && d[$scope.drivingSectionsTo.fields.departureTime]) {
											myvalues = [
													"(" + d[$scope.drivingSectionsTo.fields.toDuration] + "m)",
													d[$scope.drivingSectionsTo.fields.departureTime], d[$scope.drivingSectionsTo.fields.toTrainNo],
													d[$scope.drivingSectionsTo.fields.toStation] ]
										}
										return (d[$scope.drivingSectionsTo.fields.departureTime]) ? myvalues : "";
									});

							// adding days text on arrival line
							p.selectAll(".text").data(plotdays).enter().append(
									"g").attr("class", "daystext").append(
									"text").attr("x", -80).attr("y",
									function(d) {
										return other_yaxis(d[1])
									}).text(function(d) {
								return d[0];
							});

							// adding days text on departure line
							p.selectAll(".text").data(plotdays).enter().append(
									"g").attr("class", "daystext").append(
									"text").attr("x", 470).attr("y",
									function(d) {
										return other_yaxis(d[1])
									}).text(function(d) {
								return d[0];
							});

							// drawing additional lines
							p
									.append("g")
									.attr("class", "foreground")
									.selectAll("path")
									.data(mydata)
									.attr("class", "extra_lines_fore")
									.enter()
									.append("path")
									.attr("d", arrdraw)
									.attr(
											"stroke",
											function(d) {
												return (!d[$scope.drivingSectionsTo.fields.isDrivingDuty]) ? "#d62728"
														: "green";
											}).attr("stroke-width",
											function(d) {
												if (!d[$scope.drivingSectionsTo.fields.isDrivingDuty])
													return 2;
												else
													return 1;
											});

							p
									.append("g")
									.attr("class", "foreground")
									.selectAll("path")
									.data(mydata)
									.enter()
									.append("path")
									.attr("class", "extra_lines_fore")
									.attr("d", depdraw)
									.attr(
											"stroke",
											function(d) {
												return (!d[$scope.drivingSectionsTo.fields.isDrivingDuty]) ? "#d62728"
														: "green";
											}).attr("stroke-width",
											function(d) {
												if (!d[$scope.drivingSectionsTo.fields.isDrivingDuty])
													return 2;
												else
													return 1;
											});

							p.append("g").attr("class", "background")
									.selectAll("path").data(mydata).enter()
									.append("path").attr("class",
											"extra_lines_fore").attr("d",
											arrdraw);

							p.append("g").attr("class", "background")
									.selectAll("path").data(mydata).enter()
									.append("path").attr("class",
											"extra_lines_fore").attr("d",
											depdraw);

							ordinal_labels = p.selectAll(".foreground text")
									.on("mouseover", mouseover).on("mouseout",
											mouseout);
							projection = p.selectAll(
									".background path,.foreground path").on(
									"mouseover", mouseover).on("mouseout",
									mouseout);
						}

						function draw(d) {
							return lineGen(dim_plot
									.map(function(p) {
										if (p.name == $scope.chartStationCode
												+ "-ARR") {
											myday = d[$scope.drivingSectionsTo.fields.arrivalDay];
											mytime = d[$scope.drivingSectionsTo.fields.arrivalTime];
										} else {
											myday = d[$scope.drivingSectionsTo.fields.departureDay];
											mytime = d[$scope.drivingSectionsTo.fields.departureTime];
										}
										val = theswitch(mytime, myday);
										return [ x_axis(p.name), p.scale(val) ];
									}));
						}

						function arrdraw(d) {
							mytime = d[$scope.drivingSectionsTo.fields.arrivalTime];
							myday = d[$scope.drivingSectionsTo.fields.arrivalDay];
							var val = null;
							if (myday) {
								myhr = parseFloat(mytime.slice(0, 2));
								mymin = parseInt(mytime.slice(-2));
								val = theswitch(mytime, myday);
							}
							var myarray = [ {
								x : 0,
								y : other_yaxis(val)
							}, {
								x : -200,
								y : other_yaxis(val)
							} ];
							return (mytime) ? newline(myarray) : null;
						}

						function depdraw(d) {
							mytime = d[$scope.drivingSectionsTo.fields.departureTime];
							myday = d[$scope.drivingSectionsTo.fields.departureDay];
							var val = null;
							if (myday) {
								myhr = parseFloat(mytime.slice(0, 2));
								mymin = parseInt(mytime.slice(-2));
								val = theswitch(mytime, myday);
							}
							var myarray = [ {
								x : 360,
								y : other_yaxis(val)
							}, {
								x : 550,
								y : other_yaxis(val)
							} ];
							return (mytime) ? newline(myarray) : null;
						}

						function theswitch(currenttime, currentday) {
							var val = null
							if (currenttime && currentday) {
								myhr = parseFloat(currenttime.slice(0, 2));
								mymin = parseInt(currenttime.slice(3,5));
								switch (currentday) {
								case "SUNDAY":
									val = myhr + parseFloat(mymin / 60);
									break;
								case "MONDAY":
									val = 24.0 + myhr + parseFloat(mymin / 60);
									break;
								case "TUESDAY":
									val = 48.0 + myhr + parseFloat(mymin / 60);
									break;
								case "WEDNESDAY":
									val = 72.0 + myhr + parseFloat(mymin / 60);
									break;
								case "THURSDAY":
									val = 96.0 + myhr + parseFloat(mymin / 60);
									break;
								case "FRIDAY":
									val = 120.0 + myhr + parseFloat(mymin / 60);
									break;
								case "SATURDAY":
									val = 144.0 + myhr + parseFloat(mymin / 60);
									break;
								default:
									val = null;
									break;
								}
							}
							return val;
						}

						function mylegend() {
							// (data);
							color.domain(sortedItems);
							var legend_w = width + 220;
							var legend_h = -60;
							var legend = p
									.selectAll(".legend")
									.data(sortedItems)
									.enter()
									.append("g")
									.attr("class", "legend")
									.each(
											function(d) {
												var g = d3.select(this);
												g
														.append("rect")
														.attr("x",
																legend_w + 10)
														.attr("y",
																legend_h + 300)
														.attr("width", 50)
														.attr("height", 2)
														.style("fill",
																"#d62728")
														.attr(
																"class",
																"d3legend "
																		+ (($scope.toggleSections) ? "activeLegend"
																				: ""))
														.on("click",
																loadSectionsOnChart)
														.style("stroke",
																"#d62728");
												g
														.append("rect")
														.attr("x",
																legend_w + 10)
														.attr("y",
																legend_h + 335)
														.attr("width", 50)
														.attr("height", 1)
														.style("fill", "green")
														.attr(
																"class",
																"d3legend "
																		+ (($scope.toggleDuties) ? "activeLegend"
																				: ""))
														.on("click",
																loadDutiesOnChart)
														.style("stroke",
																"green");
												g
														.append("text")
														.attr("x",
																legend_w + 65)
														.attr("y",
																legend_h + 305)
														.style("fill", (($scope.toggleSections) ? "#d62728": "black"))
														.attr(
																"class",
																"d3legend "
																		+ (($scope.toggleSections) ? "activeLegend"
																				: ""))
														.on("click",
																loadSectionsOnChart)
														.text("Sections");
												g
														.append("text")
														.attr("x",
																legend_w + 65)
														.attr("y",
																legend_h + 340)
														.style("fill", (($scope.toggleDuties) ? "green": "black"))
														.attr(
																"class",
																"d3legend "
																		+ (($scope.toggleDuties) ? "activeLegend"
																				: ""))
														.on("click",
																loadDutiesOnChart)
														.text("Duties");
											});
						}
						function loadSectionsOnChart() {
							$scope.toggleSections = !$scope.toggleSections;
							$scope.generateChart($scope.toggleSections,
									$scope.toggleDuties)
						}
						function loadDutiesOnChart() {
							$scope.toggleDuties = !$scope.toggleDuties;
							$scope.generateChart($scope.toggleSections,
									$scope.toggleDuties)
						}

						function mouseover(d) {
							div.transition().duration(200).style("opacity", 1);
							div
									.html(
											((d[$scope.drivingSectionsTo.fields.isDrivingDuty] == 1) ? "DRIVING DUTY<br/>"
													: "DRIVING SECTION<br/>")
													+ ((d[$scope.drivingSectionsTo.fields.trainNo]) ? "Train No -"
															+ ((d[$scope.drivingSectionsTo.fields.toTrainNo]) ? d[$scope.drivingSectionsTo.fields.toTrainNo]
																	+ " - "
																	: "")
															+ d[$scope.drivingSectionsTo.fields.trainNo]
															+ "<br/>"
															: "")
													+

													((d[$scope.drivingSectionsTo.fields.fromStation]) ? "From -"
															+ ((d[$scope.drivingSectionsTo.fields.duty]) ? d[$scope.drivingSectionsTo.fields.toStation]
																	: d[$scope.drivingSectionsTo.fields.fromStation])
															+ "<br/>"
															: "")
													+ ((d[$scope.drivingSectionsTo.fields.departureTime]) ? "Dep Time - "
															+ d[$scope.drivingSectionsTo.fields.departureTime]
															+ "<br/>"
															: "")
													+ ((d[$scope.drivingSectionsTo.fields.departureDay]) ? "Dep Day - "
															+ d[$scope.drivingSectionsTo.fields.departureDay]
															+ "<br/>"
															: "")
													+ ((d[$scope.drivingSectionsTo.fields.toStation]) ? "To -"
															+ d[$scope.drivingSectionsTo.fields.toStation]
															+ "<br/>"
															: "")
													+ ((d[$scope.drivingSectionsTo.fields.arrivalTime]) ? "Arr Time - "
															+ d[$scope.drivingSectionsTo.fields.arrivalTime]
															+ "<br/>"
															: "")
													+ ((d[$scope.drivingSectionsTo.fields.arrivalDay]) ? "Arr Day - "
															+ d[$scope.drivingSectionsTo.fields.arrivalDay]
															+ "<br/>"
															: "")
													+ ((d[$scope.drivingSectionsTo.fields.duty]) ? "Duration - "
															+ ((d[$scope.drivingSectionsTo.fields.duration]) + 
																	((d[$scope.drivingSectionsTo.fields.toDuration]) ? (d[$scope.drivingSectionsTo.fields.toDuration])
																	: 0))
															+ "mins"
															: ((d[$scope.drivingSectionsTo.fields.duration]) ? "Duration - "
																	+ d[$scope.drivingSectionsTo.fields.duration]
																	+ "mins"
																	: "")))
									.style("left", (d3.event.pageX - 11) + "px")
									.style("top", (d3.event.pageY - 74) + "px");
							p.classed("active", true);
							if (typeof d === "string") {
								projection.classed("inactive", function(x) {
									return x.name !== d;
								});
								projection.filter(function(x) {
									return x.name === d;
								}).each(moveToFront);
								ordinal_labels.classed("inactive", function(x) {
									return x !== d;
								});
								ordinal_labels.filter(function(x) {
									return x === d;
								}).each(moveToFront);
							} else {
								projection.classed("inactive", function(x) {
									return x !== d;
								});
								projection.filter(function(x) {
									return x === d;
								}).each(moveToFront);
								ordinal_labels.classed("inactive", function(x) {
									return x !== d.name;
								});
								ordinal_labels.filter(function(x) {
									return x === d.name;
								}).each(moveToFront);
							}
						}

						function mouseout(d) {
							div.transition().duration(200).style("opacity", 0);
							p.classed("active", false);
							projection.classed("inactive", false);
							ordinal_labels.classed("inactive", false);
						}

						function moveToFront() {
							this.parentNode.appendChild(this);
						}

						function clearall() {
							p.selectAll(".foreground text").remove();
							p.selectAll(".daystext").remove();
							p.selectAll(".legend").remove();
							p.selectAll("path").remove();
							p.selectAll(".axis").remove();
							p.selectAll(".background path").remove();
							p.selectAll(".foreground path").remove();
						}
					}
				});
