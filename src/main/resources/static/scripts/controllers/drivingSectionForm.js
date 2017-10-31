'use strict';

/**
 * This DrivingSectionForm controller is used to 1. List all the Users from DB
 * 2. Select the Users for Update in DB 3. Delete the User from DB
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */

angular
		.module('crewLinkApp')
		.controller(
				'DrivingSectionFormCtrl',
				/*
				 * $timeOut - $scope - $q - $resouce - $confirm
				 */
				function($timeout, $scope, $q, $state, $http, $resource,
						SpringDataRestAdapter, SpringDataRestApi, UserService,
						toaster, $confirm) {
					$scope.trainNo = ($state.params.trainNo) ? $state.params.trainNo: 0;
					$scope.startDay = ($state.params.startDay) ? $state.params.startDay : '';
					$scope.trainStations = {};
					$scope.selectedTrainStations = {};
					$scope.trains = [];
					$scope.trainDetails = [];
					$scope.selectedTrain = {
						trainNo : $scope.trainNo,
						startDay : $scope.startDay
					};
					$scope.selectedSections = {};
					$scope.isDrivingSection = false;

					$scope.existingDrivingSections = {};
					$scope.selectedCssClass = 'selected-train-section';
					$scope.savingInProcess = false;
					$scope.refreshTrainList = false;

					// added on 18-3-2016 according to Manish sir requirements
					$scope.trainTypes = [];
					$scope.passingStation1 = '';
					$scope.passingStation2 = '';
					$scope.Days = Days;
					$scope.itemsPerPageTrainsList = 10;
					$scope.itemsPerPageTrainTimeTable = 200;
					$scope.itemsPerPage = 200;

					if ($scope.selectedTrain.trainNo != 0
							&& $scope.selectedTrain.startDay != '') {
						SpringDataRestApi.get(
								'/api/trains/search/findByTrainNoAndStartDay?trainNo='
										+ $scope.selectedTrain.trainNo
										+ "&startDay="
										+ $scope.selectedTrain.startDay,
								[ 'fromStation', 'toStation' ]).then(
								function(response) {
									$scope.selectedTrain = response;
								}, function(response) {
									(response);
								});
					}

					$scope.updateTrainStationsUrl = function() {
						if ($scope.selectedTrain.trainNo == 0
								|| $scope.selectedTrain.startDay == '') {
							return '';
						}
						return "/api/custom/trainStations/listWithDrivingSections?userPlan="
								+ UserService.getSelectedUserPlan().id
								+ "&sort=stopNumber asc&trainNo="
								+ $scope.selectedTrain.trainNo
								+ "&startDay="
								+ $scope.selectedTrain.startDay
								+ "&size="
								+ $scope.itemsPerPage
								+ "&limit="
								+ $scope.itemsPerPage;
					};

					$scope.serverFetch = new ServerTableFetch2(
							$scope.updateTrainStationsUrl.bind(this),
							SpringDataRestApi, // This is our Call Processing
							// Service currently only
							// SringDataRestApi is supported
							// and used here.
							function() { // Before processing this is called
								$scope.isLoading = true;
								$scope.selectedTrainStations = {};
								$scope.isDrivingSection = false;

							},
							function(resultObj) { // After processing this is
								// called
								$scope.trainStations = resultObj;
								$scope.isLoading = false;
								var i = 0;
								if ($scope.trainStations.data == null) {
									return;
								}
								// The Following logic is Used for Deleting the
								// drivingSection if already Created and User
								// trying to save the same the DrivingSections
								var ds_id = $scope.trainStations.data[0][$scope.trainStations.fields.drivingSection];
								for (i = 1; i < $scope.trainStations.data.length; i++) {
									if ($scope.trainStations.data[i][$scope.trainStations.fields.id] == $scope.trainStations.data[i-1][$scope.trainStations.fields.id]) {
										$scope
												.addTrainStationSelectedList($scope.trainStations.data[i][$scope.trainStations.fields.stopNumber] );
										$scope.trainStations.data.splice(i-1,1);
										ds_id = $scope.trainStations.data[i][$scope.trainStations.fields.drivingSection];
										$scope.isDrivingSection = true;
									}
								}
								$scope.addTrainStationSelectedList(1);
								$scope
										.addTrainStationSelectedList($scope.trainStations.data[$scope.trainStations.data.length - 1][$scope.trainStations.fields.stopNumber]);

							}, function(response) {
								$scope.isLoading = false;
							}, true // Wait for all promises to resolve (run
					// success function only after all fetch are
					// done
					);

					$scope.addTrainStationSelectedList = function(stopNumber) {
						$scope.selectedTrainStations[stopNumber] = {
							data : $scope.trainStations.data[stopNumber],
							cssClass : $scope.selectedCssClass
						};
					};
					$scope.removeTrainStationSelectedList = function(stopNumber) {
						delete $scope.selectedTrainStations[stopNumber];
					};
					/**
					 * The Following Function used When a User select Section to
					 * and the Particular section turns to Green backgound
					 */
					$scope.rowClicked = function(stopNumber) {
						/*
						 * if the StopNumber is 1 AND 'lastitem' then we can't
						 * deselect
						 */
						if (stopNumber == 1
								|| stopNumber == $scope.trainStations.data[$scope.trainStations.data.length - 1][$scope.trainStations.fields.stopNumber]) {
							toaster
									.pop({
										type : 'error',
										title : 'Error',
										body : 'You cannot unselect first and last stations.'
									});
							return;
						}
						/*
						 * if the StopNumber is 1 AND 'lastitem' then we can't
						 * deselect
						 */
						if ($scope.selectedTrainStations[stopNumber]
								&& $scope.selectedTrainStations[stopNumber].cssClass) {
							$scope.removeTrainStationSelectedList(stopNumber);
						} else {
							$scope.addTrainStationSelectedList(stopNumber);
						}

					}

					/**
					 * The function is Used to search the train based on the
					 * given criteria
					 */
					$scope.searchTrain = function(term) {
						/*
						 * No search term: return initial items
						 */
						if (!term) {
							return $scope.trains;
						}
						var deferred = $q.defer();
						SpringDataRestApi.get(
								'/api/trains/search/findByTrainNo?trainNo='
										+ term, [ 'fromStation', 'toStation' ])
								.then(function(response) {
									$scope.trains = response._embedded.trains;
									deferred.resolve($scope.trains);
								});
						return deferred.promise;
					};

					$scope.getStopNumbersInString = function() {
						var stopNumbers = [];
						for ( var item in $scope.selectedTrainStations) {
							stopNumbers.push(item);
						}
						stopNumbers = stopNumbers.sort(function(a, b) {
							return a - b;
						});
						var stopNumbersStr = "";
						for (var i = 0; i < stopNumbers.length; i++) {
							stopNumbersStr += ',' + stopNumbers[i];
						}
						stopNumbersStr = stopNumbersStr.substring(1,
								stopNumbersStr.length);
						return stopNumbersStr;
					};

					/**
					 * This function is called inside saveSections
					 */
					$scope.callSaveApi = function(type) {
						var successMessage = "";
						var stopNumbers = $scope.getStopNumbersInString();
						var apiLink = "";
						switch (type) {
						/*
						 * If the Type is single pass all this parameter to
						 * create drivingSections 1. UserPlan (parameter) 2.
						 * trainNo (parameter) 3. startDay (parameter) 4.
						 * stopNumber (parameter)
						 */
						case 'single':
							apiLink = "/api/custom/drivingSections/saveSingle?userPlan="
									+ UserService.getSelectedUserPlan().id
									+ "&trainNo="
									+ $scope.selectedTrain.trainNo
									+ "&startDay="
									+ $scope.selectedTrain.startDay
									+ "&stopNumbers=" + stopNumbers;
							successMessage = "Driving Sections created Successfully.";
							break;
						/*
						 * If the Type is 'ALL' pass all this parameter to
						 * create drivingSections 1. UserPlan (parameter) 2.
						 * trainNo (parameter) 3. stopNumber (parameter)
						 * 
						 */
						case 'all':
							apiLink = "/api/custom/drivingSections/saveForAllDays?userPlan="
									+ UserService.getSelectedUserPlan().id
									+ "&trainNo="
									+ $scope.selectedTrain.trainNo
									+ "&stopNumbers=" + stopNumbers;
							successMessage = "All Driving Sections for all days created Successfully.";
							break;
						/*
						 * If the Type is 'allWithDrivingDuty' pass all this
						 * parameter to create drivingSections 1. UserPlan
						 * (parameter) 2. trainNo (parameter) 3. stopNumber
						 * (parameter)
						 * 
						 */
						case 'allWithDrivingDuty':
							apiLink = "/api/custom/drivingSections/saveForAllDaysWithDrivingDuties?userPlan="
									+ UserService.getSelectedUserPlan().id
									+ "&trainNo="
									+ $scope.selectedTrain.trainNo
									+ "&stopNumbers=" + stopNumbers;
							successMessage = "All Driving Sections and Driving Duties created Successfully.";
							break;
						}
						$scope.savingInProcess = true;

						$http
								.get(apiLink)
								.then(
										function(response) { // On Success
											// response the
											// PopUp
											// Messages
											// displayed
											if (response.data.result) {
												toaster
														.pop({
															type : 'success',
															title : 'Driving Section Saved',
															body : successMessage
														});
												$scope.isDrivingSection = true;
												$scope.refreshTrainList != $scope.refreshTrainList;
											} else {
												toaster
														// On Failure response
														// the PopUp Messages
														// displayed
														.pop({
															type : 'error',
															title : 'Error',
															body : 'Unable Save Driving Section. Please Try Again!!! '
																	+ response.data.errorMessage
														});
											}
											$scope.savingInProcess = false;
										});
					};
					/**
					 * This is used to save driving sections based upon type of
					 * save(single,all,allwithdrivingduty)
					 * 
					 */
					$scope.saveSections = function(type) {
						if ($scope.isDrivingSection) {
							/*
							 * If the driving section is already created and
							 * User clicked on the Save(button) then the PopUp
							 * messages will display to delete the already
							 * created Section
							 * 
							 */
							$confirm({ // Confirm PopUp to Remove fields from
								// DB
								text : 'Are you sure you want to delete?',
								headerClass : 'confirm-header-danger',
								okButtonClass : "btn-danger"
							}).then(function() {
								$http.get('/api/custom/dashboards/listDependencies?searchItem=train&searchValue='+$scope.selectedTrain.trainNo+':'+$scope.selectedTrain.startDay
										+'&userPlan='+UserService.getSelectedUserPlan().id).then(function(res){
									var duties = {};
									var roundTrips = {};
									var links = {};
									for(var i=0; i<res.data.data.length; i++){
										if(res.data.data[i][res.data.fields.ddName]!= null){
											duties[res.data.data[i][res.data.fields.ddName]] = res.data.data[i][res.data.fields.ddName];
										}
										if(res.data.data[i][res.data.fields.rtName] != null){
											roundTrips[res.data.data[i][res.data.fields.rtName]] = res.data.data[i][res.data.fields.rtName];
										}
										if(res.data.data[i][res.data.fields.clName] != null){
											links[res.data.data[i][res.data.fields.clName]] = res.data.data[i][res.data.fields.clName];
										}
									}
									var strDuties = "";
									var strRoundTrips = "";
									var strLinks = "";
									for(var item in duties){
										strDuties += " | "+item;
									}
									for(var item in roundTrips){
										strRoundTrips += " | "+item;
									}
									for(var item in links){
										strLinks += " | "+item;
									}
									$confirm({ // Confirm PopUp to Remove fields from
										// DB
										text : "These Items might get effected ? :    Duties: ["+strDuties+"]   Round Trips : ["+strRoundTrips+"]   Links: ["+strLinks+"]",
										headerClass : 'confirm-header-danger',
										okButtonClass : "btn-danger"
									}).then(function() {
										$scope.callSaveApi(type);
										
									});
								},function(res){
									$scope.callSaveApi(type);
								});
								
							});
						} else {
							/*
							 * Invoke this callSaveApi Method for creating
							 * Sections based on Type as
							 * (single,all,allwithdrivingduty)
							 */
							$scope.callSaveApi(type);
						}

					};

					// added on 18-3-2016 according to Manish sir requirements

					$scope.serverFetchTrainsList = new ServerTableFetch2(
							"/api/custom/trains/list?userPlan="
									+ UserService.getSelectedUserPlan().id
									+ "&size=" + $scope.itemsPerPageTrainsList
									+ "&limit=" + $scope.itemsPerPageTrainsList, // Url
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
					SpringDataRestApi.list('trainTypes').then(
							function(response) {
								$scope.trainTypes = response;

							});

					$scope.getTrainTimeTable = function(trainItem) {
						if ($scope.selectedTrain.trainNo != trainItem[$scope.trains.fields.trainNo]
								|| $scope.selectedTrain.startDay != Days[trainItem[$scope.trains.fields.startDay]]) {
							$scope.selectedTrain = {
								trainNo : trainItem[$scope.trains.fields.trainNo],
								startDay : Days[trainItem[$scope.trains.fields.startDay]],
								cssClass : $scope.selectedCssClass
							};
						}

					};
					$scope.getSelectedTrainCss = function(trainItem) {
						if ($scope.selectedTrain.trainNo == trainItem[$scope.trains.fields.trainNo]
								&& $scope.selectedTrain.startDay == Days[trainItem[$scope.trains.fields.startDay]]) {
							return $scope.selectedTrain.cssClass;
						}
						return "";
					};

				});
