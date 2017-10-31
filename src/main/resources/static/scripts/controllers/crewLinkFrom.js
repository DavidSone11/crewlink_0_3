'use strict';

/**
 * This CrewLinkForm controller is used to 1. List all the Round Trips from DB
 * 2. Creating crew link in DB 3. View round trips and crew links in graph from
 * DB
 * 
 * @authors Vivek Yadav, Laxman
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 16, 2016
 */

angular
		.module('crewLinkApp')
		.controller(
				'CrewLinkFormCtrl',
				function($scope, $q, $state, $http, $resource, $filter,
						SpringDataRestAdapter, SpringDataRestApi, UserService,
						toaster, $timeout,TimeCalService) {

					$scope.Days = Days;
					$scope.selectedRoundTripsToCreateCrewLink = [];
					$scope.roundTripsList = {
						data : [],
						fields : {}
					};
					$scope.crewUsageAnalysisSearchData = {};
					$scope.isCrewLinkForCrewUsageAnalysis = "";
					$scope.crewLinkToSave = {};
					$scope.stationRT = "";
					$scope.stationDisabled = false;
					$scope.startDay = "";
					$scope.startTime = "";
					$scope.minHQRestForLessThen8HrsDuty = 12;
					$scope.minHQRestForMoreThen8HrsDuty = 16;

					$scope.minPR = 30;
					$scope.minNoOfDaysForPR = 6;
					$scope.maxNoOfDaysForPR = 10;
					$scope.attemptLimit = 100;

					$scope.barData = [{
					    'x': 1,
					    'y': 5
					  }, {
					    'x': 20,
					    'y': 20
					  }, {
					    'x': 40,
					    'y': 10
					  }, {
					    'x': 60,
					    'y': 40
					  }, {
					    'x': 80,
					    'y': 5
					  }, {
					    'x': 100,
					    'y': 60
					  }];
					
					$scope.range = function(a,b) {
						if(b == null){
							b = a;
							a = 1;
						}
						var array = [];
						for (var i = a; i <= b; i++) {
							array.push(i);
						}
						return array;
					};
					$scope.updateCrewUsageAnalysisSearchData = function(){
						$scope.crewUsageAnalysisSearchData.stationCode = $scope.stationForCrewUsageAnalysis;
						$scope.crewUsageAnalysisSearchData.isCrewLink = $scope.isCrewLinkForCrewUsageAnalysis;
					}
					$scope.loadCrewUsageAnalysis = function(data){
						var uri = "/api/custom/roundTrips/list?userPlan="
							+ UserService.getSelectedUserPlan().id
							+ "&isCrewLink="+data.isCrewLink + "&isIgnore=false"
							+ "&sort=CONCAT(departureDay,departureTime)"
							+ "&station="+data.stationCode
							+ "&size=2000";
						return $http.get(uri);
					};
					$scope.postLoading = function(res){
						var result = [];
						if(!res.data.data){
							return result;
						}
						var data = res.data.data;
						var fields = res.data.fields;
						var timeValueMap = {};
						var timeArray = [];
						
						for(var i=0; i<data.length; i++){
							var depObj = { day : data[i][fields.departureDay], time: data[i][fields.departureTime] };
							var arrObj = { day : data[i][fields.availableDay], time : data[i][fields.availableTime] };
							var dep = TimeCalService.convertDateTimeObjToNumber(depObj);
							var arr = TimeCalService.convertDateTimeObjToNumber(arrObj);
							
							if(timeValueMap[dep]!= null){
								timeValueMap[dep] += 1;
							}
							else{
								timeArray.push(dep);
								timeValueMap[dep] = +1;
							}
							if(timeValueMap[arr]!= null){
								timeValueMap[arr] -= 1;
							}
							else{
								timeArray.push(arr);
								timeValueMap[arr] = -1;
							}
						}
						
						timeArray.sort(function(a, b){return a-b});
						
						/* Finding running sum to get no of crew at each time interval of arrival and departure */
						var runningSum = 0;
						var minCrew = 9999999;
						var maxCrew = -1;
						for(var t=0; t<timeArray.length; t++){
							var currentVal = runningSum + timeValueMap[timeArray[t]];
							result.push({time:timeArray[t], crew: currentVal});
							if(currentVal<minCrew){
								minCrew = currentVal;
							}
							if(currentVal>maxCrew){
								maxCrew = currentVal;
							}
							runningSum = currentVal;
						}
						
						var resultObj = {
								data: {
								        x: 'x',
								        xFormat: '%a %b %d %Y %H:%M', // 'xFormat' can be used as custom format of 'x'
								        columns: [
								            ['x'],
								//            ['x', '20130101', '20130102', '20130103', '20130104', '20130105', '20130106'],
								            ['No of Crew Used']
								        ],
								        type: 'area-step'
								    },
								    axis: {
								        x: {
								            type: 'timeseries',
								            tick: {
								                format: '%a %H:%M'
								            }
								        }
								    },
								    grid: {
								    	  y: {
								    	    lines: []
								    	  }
								    	}
							};
						var d = new Date("01/01/2012");
						/* normalization of the no. of crew at each time interval as in previous setup it might have been negative */
						for(var i=0; i<result.length; i++){
							result[i].crew = result[i].crew - minCrew;
							result[i].time = new Date(d.getTime()+(result[i].time*60000));
							var date = result[i].time.toDateString()+" "+result[i].time.getHours()+":"+result[i].time.getMinutes();
							var time = new Date(d.getTime()+(result[i].time*60000));
							resultObj.data.columns[0].push(date);
							resultObj.data.columns[1].push(result[i].crew);
						}
						
						maxCrew = maxCrew - minCrew;
						for(var j = 5; j<maxCrew; j=j+5){
							resultObj.grid.y.lines.push({value: j, text: j+' crews'});
						}
						resultObj.grid.y.lines.push({value: maxCrew, text: 'Max ('+maxCrew+') crews'});
						
						return resultObj;
					}
					$scope.loadCrewUsageAnalysisChart = function(){
						$scope.crewUsageAnalysisReload != ($scope.crewUsageAnalysisReload)?$scope.crewUsageAnalysisReload:true;
						$scope.isLoadCrewUsageAnalysis = true;
					}

					/*
					 * Server call to fetch crew types list
					 */
					SpringDataRestApi
							.get(
									"/api/crewTypes/search/findByNameContains?"
											+ "page=0&size=1000&limit=1000",
									[], false)
							.then(
									function(response) {
										$scope.crewTypes = response._embedded.crewTypes;
										// Logic to add crewTypeId to the
										// crewType from crewType self url
										angular
												.forEach(
														$scope.crewTypes,
														function(crewType) {
															crewType.id = crewType._links.self.href
																	.substring(crewType._links.self.href
																			.lastIndexOf("/") + 1);
														});
									});

					$scope.getCrewLinksList = function() {
						/*
						 * Server call to fetch crew links list
						 */
						$http.get(
								"/api/custom/crewLinks/list?userPlan="
										+ UserService.getSelectedUserPlan().id+ "page=0&size=1000&limit=1000")
								.then(function(response) {
									$scope.crewLinks = response.data;
								});
					}

					$scope.getCrewLinksList();
					/**
					 * 
					 */
					$scope.updateRoundTripUrl = function(tableState) {
						if (!tableState.search.predicateObject) {
							tableState.search.predicateObject = {};
						}
						if (tableState.search.predicateObject) {
							if (tableState.search.predicateObject["crewType"] != $scope.crewType)
								tableState.search.predicateObject["crewType"] = $scope.crewType;
							if (tableState.search.predicateObject["station"] != $scope.stationRT)
								tableState.search.predicateObject["station"] = $scope.stationRT;
						}
						var uri = "/api/custom/roundTrips/list?userPlan="
								+ UserService.getSelectedUserPlan().id
								+ "&isCrewLink=false" + "&isIgnore=false";

						if ($scope.startDay != null && $scope.startTime != null) {
							uri += "&sort=CONCAT(departureDay,departureTime)<'"
									+ $scope.startDay + $scope.startTime
									+ "',CONCAT(departureDay,departureTime)";
						}
						return uri;
					};

					/**
					 * Server call to fetch all round trips list
					 */
					$scope.serverFetchRoundTrips = new ServerTableFetch2(
							$scope.updateRoundTripUrl.bind(this), // Url call
							// that will
							// be made
							// all the
							// time
							SpringDataRestApi,
							function() {
								$scope.isroundTripsLoading = true;
							},
							function(resultObj) { // After processing this is
								// called
								$scope.roundTripsList = resultObj;
								$scope.isroundTripsLoading = false;
								if (!$scope.roundTripsList.fields.estimatedHQRest) {
									$scope.roundTripsList.fields.estimatedHQRest = $scope.roundTripsList.data[0].length;
									for (var i = 0; i < $scope.roundTripsList.data.length; i++) {
										$scope.roundTripsList.data[i]
												.push("---");
									}
								}
								angular
										.forEach(
												$scope.roundTripsList.data,
												function(roundTrip) {
													if ($scope.selectedRoundTripsToCreateCrewLink.length > 0) {
														var diff = diffTimeObj(
																{
																	day : $scope.selectedRoundTripsToCreateCrewLink[$scope.selectedRoundTripsToCreateCrewLink.length - 1][$scope.roundTripsList.fields.arrivalDay],
																	time : $scope.selectedRoundTripsToCreateCrewLink[$scope.selectedRoundTripsToCreateCrewLink.length - 1][$scope.roundTripsList.fields.arrivalTime]
																},
																{
																	day : roundTrip[$scope.roundTripsList.fields.departureDay],
																	time : roundTrip[$scope.roundTripsList.fields.departureTime]
																}, "min");
														var hours = Math
																.floor(diff / 60);
														var minutes = diff % 60;
														roundTrip[$scope.roundTripsList.fields.estimatedHQRest] = hours
																+ 'h '
																+ minutes + 'm';
													}

													angular
															.forEach(
																	$scope.crewTypes,
																	function(
																			crewType) {
																		if (roundTrip[$scope.roundTripsList.fields.crewType] == crewType.id) {
																			roundTrip[$scope.roundTripsList.fields.crewType] = crewType.name;
																		}
																	});
												});
								$scope.removeSelectedRoundTrips();
							}, function(error) {
							}, true);

					/**
					 * This function is used to remove selected round
					 * trips(round trips which are there in selected round trips
					 * to make crew link table ) from round trips list(Round
					 * trips which will be displayed in round trips table)
					 */
					$scope.removeSelectedRoundTrips = function() {
						var temp = [];
						var count;
						if (!$scope.roundTripsList.data) {
							return;
						}
						// logic to remove selected round trips
						for (var i = 0; i < $scope.roundTripsList.data.length; i++) {
							count = 0;
							for (var j = 0; j < $scope.selectedRoundTripsToCreateCrewLink.length; j++) {
								if ($scope.roundTripsList.data[i][$scope.roundTripsList.fields.id] == $scope.selectedRoundTripsToCreateCrewLink[j][$scope.roundTripsList.fields.id])
									count++;
							}
							if (count == 0) {
								temp.push($scope.roundTripsList.data[i]);
							}
						}
						$scope.roundTripsList.data = temp;
						if ($scope.selectedRoundTripsToCreateCrewLink.length > 0) {
							$scope.stationDisabled = true;
							$scope.stationRT = $scope.selectedRoundTripsToCreateCrewLink[$scope.selectedRoundTripsToCreateCrewLink.length - 1][$scope.roundTripsList.fields.station];
							$scope.startTime = $scope.selectedRoundTripsToCreateCrewLink[$scope.selectedRoundTripsToCreateCrewLink.length - 1][$scope.roundTripsList.fields.availableTime];
							$scope.startDay = $scope.selectedRoundTripsToCreateCrewLink[$scope.selectedRoundTripsToCreateCrewLink.length - 1][$scope.roundTripsList.fields.availableDay];
						} else {
							$scope.stationDisabled = false;
							$scope.stationRT = "";
							$scope.startTime = null;
							$scope.startDay = null;
						}

					}

					/**
					 * This function will be called when user clicked on a
					 * particular round trip from round trips table and used to
					 * add that round trip in selected round trips list, to add
					 * round trip to drag and drop table(Selected round trips to
					 * make crew link table), to calculated HQRest and to
					 * generated crew link
					 */
					$scope.roundTripClicked = function(roundTrip) {
						$scope.crewTypeDisable = true;
						angular
								.forEach(
										$scope.crewTypes,
										function(crewType) {
											if (roundTrip[$scope.roundTripsList.fields.crewType] == crewType.name) {
												$scope.crewType = crewType.id;
											}
										});

						$scope.selectedRoundTripsToCreateCrewLink
								.push(roundTrip);
						if (roundTrip) {
							$scope.stationDisabled = true;
							$scope.stationRT = $scope.selectedRoundTripsToCreateCrewLink[$scope.selectedRoundTripsToCreateCrewLink.length - 1][$scope.roundTripsList.fields.station];
							$scope.startTime = $scope.selectedRoundTripsToCreateCrewLink[$scope.selectedRoundTripsToCreateCrewLink.length - 1][$scope.roundTripsList.fields.availableTime];
							$scope.startDay = $scope.selectedRoundTripsToCreateCrewLink[$scope.selectedRoundTripsToCreateCrewLink.length - 1][$scope.roundTripsList.fields.availableDay];
							$scope.addToDragAndDropTable(roundTrip);
							$scope.calculateHQRest();
							$scope.generateCrewLink();
						}
						// Logic to refresh round trips table with filters
						$scope.refreshRoundTrips = !$scope.refreshRoundTrips;
					}

					/**
					 * This function is used to add round trip to drag and drop
					 * table(Selected round trips to make crew link table)
					 */
					$scope.addToDragAndDropTable = function(roundTrip) {
						var roundTripToAddDragAndDropTable = {}, temp;
						roundTripToAddDragAndDropTable.rtName = roundTrip[$scope.roundTripsList.fields.rtName];
						roundTripToAddDragAndDropTable.depTime = roundTrip[$scope.roundTripsList.fields.departureTime];
						roundTripToAddDragAndDropTable.depDay = $scope.Days[roundTrip[$scope.roundTripsList.fields.departureDay]];
						roundTripToAddDragAndDropTable.baseStation = roundTrip[$scope.roundTripsList.fields.station];
						roundTripToAddDragAndDropTable.arrTime = roundTrip[$scope.roundTripsList.fields.arrivalTime];
						roundTripToAddDragAndDropTable.arrDay = $scope.Days[roundTrip[$scope.roundTripsList.fields.arrivalDay]];
						roundTripToAddDragAndDropTable.durationMins = roundTrip[$scope.roundTripsList.fields.duration];
						temp = (roundTrip[$scope.roundTripsList.fields.duration]) ? roundTrip[$scope.roundTripsList.fields.duration] / 60
								: 0;
						roundTripToAddDragAndDropTable.duration = Math
								.floor(temp)
								+ "h " + Math.floor((temp % 1) * 60) + "m";
						roundTripToAddDragAndDropTable.km = roundTrip[$scope.roundTripsList.fields.distance];
						roundTripToAddDragAndDropTable.totalOutStationRestTimeMins = roundTrip[$scope.roundTripsList.fields.totalOutStationRestTime];
						temp = (roundTrip[$scope.roundTripsList.fields.totalOutStationRestTime]) ? roundTrip[$scope.roundTripsList.fields.totalOutStationRestTime] / 60
								: 0;
						roundTripToAddDragAndDropTable.totalOutStationRestTime = Math
								.floor(temp)
								+ "h " + Math.floor((temp % 1) * 60) + "m";
						roundTripToAddDragAndDropTable.id = roundTrip[$scope.roundTripsList.fields.id];
						roundTripToAddDragAndDropTable.availableDay = $scope.Days[roundTrip[$scope.roundTripsList.fields.availableDay]];
						roundTripToAddDragAndDropTable.availableTime = roundTrip[$scope.roundTripsList.fields.availableTime];
						$scope.selectedRoundTripsTable.selectedRoundTripsForDragAndDropTable
								.push(roundTripToAddDragAndDropTable);
					}

					/**
					 * This function is used to calculate HQRest for all round
					 * trips in selected round trips list(Selected round trips
					 * to make crew link list)
					 */
					$scope.calculateHQRest = function() {
						var selectedRoundTripsList = $scope.selectedRoundTripsTable.selectedRoundTripsForDragAndDropTable;
						if (selectedRoundTripsList
								&& selectedRoundTripsList.length > 0
								&& selectedRoundTripsList.length < 2) {
							selectedRoundTripsList[0].HQRest = "---";
							selectedRoundTripsList[0].HQRestMins = null;
							return;
						}
						if (!selectedRoundTripsList
								|| selectedRoundTripsList.length < 1) {
							return;
						}
						for (var i = 0; i < selectedRoundTripsList.length; i++) {
							selectedRoundTripsList[i].HQRestMins = diffTimeObj(
									{
										day : selectedRoundTripsList[i].arrDay,
										time : selectedRoundTripsList[i].arrTime
									},
									{
										day : selectedRoundTripsList[(i + 1)
												% selectedRoundTripsList.length].depDay,
										time : selectedRoundTripsList[(i + 1)
												% selectedRoundTripsList.length].depTime
									}, "min");
							var hours = Math
									.floor(selectedRoundTripsList[i].HQRestMins / 60);
							var minutes = selectedRoundTripsList[i].HQRestMins % 60;
							selectedRoundTripsList[i].HQRest = hours + 'h '
									+ minutes + 'm';

						}
					};

					/**
					 * This function is used to remove selected round trip from
					 * selectedRoundTripsForDragAndDropTable List
					 */
					$scope.unSelectRoundTrip = function(index) {
						var temp = $scope.selectedRoundTripsToCreateCrewLink[index];
						$scope.selectedRoundTripsTable.selectedRoundTripsForDragAndDropTable
								.splice(index, 1);
						$scope.selectedRoundTripsToCreateCrewLink.splice(index,
								1);
						if ($scope.selectedRoundTripsToCreateCrewLink.length < 1) {
							$scope.crewTypeDisable = false;
							$scope.stationDisabled = false;
						} else {
							$scope.stationDisabled = true;
							$scope.stationRT = $scope.selectedRoundTripsToCreateCrewLink[$scope.selectedRoundTripsToCreateCrewLink.length - 1][$scope.roundTripsList.fields.station];
							$scope.startTime = $scope.selectedRoundTripsToCreateCrewLink[$scope.selectedRoundTripsToCreateCrewLink.length - 1][$scope.roundTripsList.fields.availableTime];
							$scope.startDay = $scope.selectedRoundTripsToCreateCrewLink[$scope.selectedRoundTripsToCreateCrewLink.length - 1][$scope.roundTripsList.fields.availableDay];

						}

						// Logic to refresh the round trips table
						$scope.refreshRoundTrips = !$scope.refreshRoundTrips;
						$scope.calculateHQRest();
						$scope.generateCrewLink();

					}

					/**
					 * This function is used to generate crew link with selected
					 * round trips
					 */
					$scope.generateCrewLink = function() {
						if ($scope.selectedRoundTripsTable.selectedRoundTripsForDragAndDropTable.length == 0) {
							$scope.crewLinkToSave = {};
						} else {
							var temp = $scope.selectedRoundTripsTable.selectedRoundTripsForDragAndDropTable;
							$scope.crewLinkToSave.depTime = temp[0].depTime;
							$scope.crewLinkToSave.baseStation = temp[0].baseStation;
							$scope.crewLinkToSave.depDay = temp[0].depDay;

							$scope.crewLinkToSave.duration = 0;
							$scope.crewLinkToSave.distance = 0;
							$scope.crewLinkToSave.dutyHrs = 0;
							$scope.crewLinkToSave.totalHQRest = 0;
							$scope.crewLinkToSave.totalOSRest = 0;
							$scope.crewLinkToSave.totalLocoPilots = 0;
							var temp = $scope.selectedRoundTripsTable.selectedRoundTripsForDragAndDropTable;

							for (var i = 0; i < temp.length; i++) {
								$scope.crewLinkToSave.totalHQRest += temp[i].HQRestMins;
								$scope.crewLinkToSave.duration += temp[i].durationMins;
								$scope.crewLinkToSave.dutyHrs += temp[i].durationMins
										- temp[i].totalOutStationRestTimeMins;
								$scope.crewLinkToSave.distance += temp[i].km;
								$scope.crewLinkToSave.totalOSRest += temp[i].totalOutStationRestTimeMins;
								$scope.crewLinkToSave.userPlan = UserService
										.getSelectedUserPlan();
							}
							if (temp && temp.length > 0 && temp.length < 2) {
								$scope.crewLinkToSave.totalHQRest = null;

							}
							if (temp.length > 1) {
								$scope.crewLinkToSave.totalLocoPilots = Math
										.ceil(($scope.crewLinkToSave.duration + $scope.crewLinkToSave.totalHQRest)
												/ (168 * 60));
								
								$scope.crewLinkToSave.dutyHrsPer14Days = ($scope.crewLinkToSave.dutyHrs / ($scope.crewLinkToSave.duration + $scope.crewLinkToSave.totalHQRest)) 
										* (14 * 24 * 60);
								
								
								$scope.crewLinkToSave.PercentDutyHrs = Math
										.round(($scope.crewLinkToSave.dutyHrs / ($scope.crewLinkToSave.duration + $scope.crewLinkToSave.totalHQRest)) * (100) * 10) / 10;
								$scope.crewLinkToSave.PercentOSR = Math
										.round(($scope.crewLinkToSave.totalOSRest / ($scope.crewLinkToSave.duration + $scope.crewLinkToSave.totalHQRest)) * (100) * 10) / 10;

								$scope.crewLinkToSave.PercentHQR = Math
										.round(($scope.crewLinkToSave.totalHQRest / ($scope.crewLinkToSave.duration + $scope.crewLinkToSave.totalHQRest)) * (100) * 10) / 10;

							}

						}
					}

					/**
					 * This function is used to save crew link
					 */
					$scope.saveCrewLink = function() {
						if ($scope.linkName != null
								&& $scope.crewLinkToSave.depTime) {
							$scope.crewLinkToSave.linkName = $scope.linkName;
							var roundTripIds = "";
							var orderNos = "";
							for (var i = 0; i < $scope.selectedRoundTripsTable.selectedRoundTripsForDragAndDropTable.length; i++) {
								roundTripIds += ","
										+ $scope.selectedRoundTripsTable.selectedRoundTripsForDragAndDropTable[i].id;
								orderNos += "," + (i + 1);
							}
							roundTripIds = roundTripIds.substring(1);
							orderNos = orderNos.substring(1);
							var uri = "/api/custom/crewLinks/save?userPlan="
									+ UserService.getSelectedUserPlan().id
									+ "&roundTripIds=" + roundTripIds
									+ "&orderNos=" + orderNos
									+ "&crewLinkName=" + $scope.linkName;
							$http
									.get(uri)
									.then(
											function(response) {
												if (response.data.result) {
													toaster
															.pop({
																type : 'success',
																title : 'Crew Link Section Saved',
																body : 'Crew Link is saved successfully !!!'
															});
													$scope.getCrewLinksList();
													$scope.crewLinkToSave = {};
													$scope.selectedRoundTripsToCreateCrewLink = [];
													$scope.crewLinkToSave = [];
													$scope.selectedRoundTripsTable.selectedRoundTripsForDragAndDropTable = [];
													$scope
															.removeSelectedRoundTrips();
													$scope.startDay = null;
													$scope.startTime = null;
													$scope.refreshRoundTrips = !$scope.refreshRoundTrips;
												} else {
													toaster
															.pop({
																type : 'error',
																title : 'Error',
																body : 'Unable to save Crew Link. Please Try Again!!! '
																		+ response.data.errorMessage
															});
													$scope.crewLinkToSave = {};
													$scope.selectedRoundTripsToCreateCrewLink = [];
													$scope.crewLinkToSave = [];
													$scope.selectedRoundTripsTable.selectedRoundTripsForDragAndDropTable = [];
													$scope
															.removeSelectedRoundTrips();
													$scope.startDay = null;
													$scope.startTime = null;
													$scope.refreshRoundTrips = !$scope.refreshRoundTrips;
												}
												$scope.savingInProcess = false;
											},
											function(response) {
												toaster
														.pop({
															type : 'error',
															title : 'Error',
															body : 'Unable to save Crew Link. Please Try Again!!! '
														});
												$scope.crewLinkToSave = {};
												$scope.selectedRoundTripsToCreateCrewLink = [];
												$scope.selectedRoundTripsTable.selectedRoundTripsForDragAndDropTable = [];
												$scope
														.removeSelectedRoundTrips();
												$scope.startDay = null;
												$scope.startTime = null;
												$scope.refreshRoundTrips = !$scope.refreshRoundTrips;
											});
						} else {
							toaster
									.pop({
										type : 'error',
										title : 'Error',
										body : 'Please Enter Valid Crew Link Details(Ex: Link name)'
									});
						}
					}

					$scope.autoGenCrewlink = function() {
						if (!$scope.crewTypeModel || !$scope.stationRT) {
							toaster
									.pop({
										type : 'error',
										title : 'Error',
										body : 'Please enter Base Station and Crew Type'
									});
							return;
						}
						var crewType = $scope.crewTypeModel;

						var uri = "/api/custom/crewLinks/auto-gen?userPlan="
								+ UserService.getSelectedUserPlan().id
								+ "&crewType=" + crewType.id + "&crewTypeName="
								+ crewType.name + "&baseStation="
								+ $scope.stationRT
								+ "&minHQRestForLessThen8HrsDuty="
								+ $scope.minHQRestForLessThen8HrsDuty
								+ "&minHQRestForMoreThen8HrsDuty="
								+ $scope.minHQRestForMoreThen8HrsDuty
								+ "&minPR=" + $scope.minPR
								+ "&minNoOfDaysForPR="
								+ $scope.minNoOfDaysForPR
								+ "&maxNoOfDaysForPR="
								+ $scope.maxNoOfDaysForPR + "&attemptLimit="
								+ $scope.attemptLimit;
						$http
								.get(uri)
								.then(
										function(response) {
											if (response.data.result) {
												toaster
														.pop({
															type : 'success',
															title : 'Crew Link Generated and Saved',
															body : 'Crew Link Generated is saved successfully !!!'
														});
											} else {
												toaster
														.pop({
															type : 'error',
															title : 'Error',
															body : 'Unable to Auto Generate Crew Link. Please Try Again!!! '
														});
											}
										},
										function(response) {
											toaster
													.pop({
														type : 'error',
														title : 'Error',
														body : 'Unable to Auto Generate Crew Link. Please Try Again!!! '
													});
										})
					}

					// Selected round trips table object used to create drag and
					// drop table
					$scope.selectedRoundTripsTable = {
						heads : [ 'rtName', 'depTime', 'depDay', 'baseStation',
								'arrTime', 'arrDay', 'availableTime',
								'availableDay', 'duration', 'km',
								'totalOutStationRestTime', 'HQRest' ],
						selectedRoundTripsForDragAndDropTable : []
					};

					$scope.noOfTrainsLimit = 1000;
					$scope.selectedCrewLink = "";
					/**
					 * This function is used to load chart based on the selected
					 * option (1. All unused round trips) (2. Selected crew link
					 * and unused round trips of selected crew link crew type
					 * round trips)
					 */
					$scope.loadRoundTripsChart = function() {
						angular.element(
								document.querySelector('#roundTripsChart'))
								.empty();
						$scope.crewType = "";
						$scope.station = "";
						if (($scope.selectedCrewLink != "")
								&& $scope.selectedCrewLink != "unusedRoundTrips") {
							var selectedCrewLink = angular
									.fromJson($scope.selectedCrewLink);
							// Server call to fetch round trips list of selected
							// crew link
							$http
									.get(
											"/api/custom/roundTrips/list?userPlan="
													+ UserService
															.getSelectedUserPlan().id
													+ "&crewLinkId="
													+ selectedCrewLink[$scope.crewLinks.fields.id]
													+ "&page=0&size="
													+ $scope.noOfTrainsLimit)
									.then(
											function(response) {
												$scope.roundTripsWithCrewLink = response.data;
											});
							$scope.station = "&station="+selectedCrewLink[$scope.crewLinks.fields.station]
							angular
									.forEach(
											$scope.crewTypes,
											function(crewType) {
												if (crewType.name == selectedCrewLink[$scope.crewLinks.fields.crewType]) {
													$scope.crewType = "&crewType="
															+ crewType.id;
												}
											})
						}
						$scope.isChartLoading = true;
						$scope.isRoundTripsFound = true;
						// call to fetch round trips without crew link
						$timeout(
								function() {
									$http
											.get(
													"/api/custom/roundTrips/list?userPlan="
															+ UserService
																	.getSelectedUserPlan().id
															+ "&isCrewLink=false"
															+ $scope.crewType
															+ $scope.station
															+ "&page=0&size="
															+ $scope.noOfTrainsLimit
															+ "&isIgnore=false")
											.then(
													function(response) {
														$scope.roundTrips = response.data;
														if ($scope.roundTrips.data == null
																&& $scope.roundTripsWithCrewLink.data == null)
															$scope.isRoundTripsFound = false;
														else {
															$scope.toggleRoundTrips = true;
															$scope.toggleCrewLinks = true;
															if ($scope.roundTripsWithCrewLink != null) {
																$scope.roundTripsWithCrewLink.data = quickSort(
																		$scope.roundTripsWithCrewLink.data,
																		"",
																		"",
																		$scope.roundTrips.fields.crewLinkOrderNo);
																var newCrewRoundTrip;
																$scope.roundTrips.fields.crewLinkLine = $scope.roundTripsWithCrewLink.data[0].length;
																$scope.roundTripsWithCrewLink.fields.crewLinkLine = $scope.roundTripsWithCrewLink.data[0].length;
																// Logic to add
																// crew link
																// line
																if($scope.roundTrips.data == null)
																	$scope.roundTrips.data = [];
																angular
																		.forEach(
																				$scope.roundTripsWithCrewLink.data,
																				function(
																						roundTrip,
																						key) {
																					$scope.roundTrips.data
																							.push(roundTrip);
																					if (key > 0) {
																						newCrewRoundTrip = [];
																						for (var i = 0; i <= $scope.roundTripsWithCrewLink.data[0].length; i++) {
																							newCrewRoundTrip
																									.push("");
																						}
																						newCrewRoundTrip[$scope.roundTripsWithCrewLink.fields.departureTime] = roundTrip[$scope.roundTripsWithCrewLink.fields.departureTime];
																						newCrewRoundTrip[$scope.roundTripsWithCrewLink.fields.departureDay] = roundTrip[$scope.roundTripsWithCrewLink.fields.departureDay];
																						newCrewRoundTrip[$scope.roundTripsWithCrewLink.fields.arrivalTime] = $scope.roundTripsWithCrewLink.data[key - 1][$scope.roundTripsWithCrewLink.fields.arrivalTime];
																						newCrewRoundTrip[$scope.roundTripsWithCrewLink.fields.arrivalDay] = $scope.roundTripsWithCrewLink.data[key - 1][$scope.roundTripsWithCrewLink.fields.arrivalDay];
																						newCrewRoundTrip[$scope.roundTripsWithCrewLink.fields.crewLinkLine] = true;
																						$scope.roundTrips.data
																								.push(newCrewRoundTrip);
																					}
																					if (key == $scope.roundTripsWithCrewLink.data.length - 1) {
																						newCrewRoundTrip = [];
																						for (var i = 0; i <= $scope.roundTripsWithCrewLink.data[0].length; i++) {
																							newCrewRoundTrip
																									.push("");
																						}
																						newCrewRoundTrip[$scope.roundTripsWithCrewLink.fields.departureTime] = $scope.roundTripsWithCrewLink.data[0][$scope.roundTripsWithCrewLink.fields.departureTime];
																						newCrewRoundTrip[$scope.roundTripsWithCrewLink.fields.departureDay] = $scope.roundTripsWithCrewLink.data[0][$scope.roundTripsWithCrewLink.fields.departureDay];
																						newCrewRoundTrip[$scope.roundTripsWithCrewLink.fields.arrivalTime] = roundTrip[$scope.roundTripsWithCrewLink.fields.arrivalTime];
																						newCrewRoundTrip[$scope.roundTripsWithCrewLink.fields.arrivalDay] = roundTrip[$scope.roundTripsWithCrewLink.fields.arrivalDay];
																						newCrewRoundTrip[$scope.roundTripsWithCrewLink.fields.crewLinkLine] = true;
																						$scope.roundTrips.data
																								.push(newCrewRoundTrip);
																					}
																				});
																$scope
																		.changeDayToString($scope.roundTrips.data);
																$scope
																		.generateChart(
																				$scope.toggleRoundTrips,
																				$scope.toggleCrewLinks);
															} else {
																$scope
																		.changeDayToString($scope.roundTrips.data);
																$scope
																		.generateChart(
																				$scope.toggleRoundTrips,
																				$scope.toggleCrewLinks);
															}
														}
														$scope.isChartLoading = false;
														if (!$scope.isRoundTripsFound)
															toaster
																	.pop({
																		type : 'error',
																		title : 'Error',
																		body : 'No Round Trips found!!!'
																	});
													});
								}, 100);
					}
					/**
					 * Function to change day of round trip from number to
					 * string
					 */
					$scope.changeDayToString = function(roundTrips) {
						angular
								.forEach(
										roundTrips,
										function(roundTrip) {
											roundTrip[$scope.roundTrips.fields.arrivalDay] = Days[roundTrip[$scope.roundTrips.fields.arrivalDay]];
											roundTrip[$scope.roundTrips.fields.departureDay] = Days[roundTrip[$scope.roundTrips.fields.departureDay]];
										});
					}

					/**
					 * Function to generate chart using roundtrips list
					 */
					$scope.generateChart = function(isRoundTrips, isCrewLink) {
						angular.element(
								document.querySelector('#roundTripsChart'))
								.empty();
						var roundTrips = $scope.roundTrips.data;
						if (!isRoundTrips && !isCrewLink) {// If both legends
							// are unselected
							roundTrips = [];
						} else if (!isRoundTrips) {// If round trips legend
							// unselected
							var temp = [];
							// Logic to filter round trips with crew link
							angular
									.forEach(
											$scope.roundTrips.data,
											function(roundTrip) {
												if (roundTrip[$scope.roundTrips.fields.isCrewLink] == 1
														|| roundTrip[$scope.roundTrips.fields.crewLinkLine])
													temp.push(roundTrip);
											});
							roundTrips = temp;
						} else if (!isCrewLink) {// If crew links legend
							// unselected
							var temp = [];
							// Logic to filter round trips without crew link
							angular
									.forEach(
											$scope.roundTrips.data,
											function(roundTrip) {
												if (roundTrip[$scope.roundTrips.fields.isCrewLink] != 1
														&& !roundTrip[$scope.roundTrips.fields.crewLinkLine])
													temp.push(roundTrip);
											});
							roundTrips = temp;
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
						var p = d3.select("#roundTripsChart").append("svg")
								.attr("width", finalWidth).attr("height",
										finalHeight).append("g").attr(
										"transform",
										"translate(" + marg.left + ","
												+ marg.top + ")");
						var plotdays = [ [ "Sunday", 24 ], [ "Monday", 48 ],
								[ "Tuesday", 72 ], [ "Wednesday", 96 ],
								[ "Thursday", 120 ], [ "Friday", 144 ],
								[ "Saturday", 168 ] ];
						var dim_plot = [ {
							name : "ARR",
							scale : d3.scale.linear().range([ 0, height ]),
							type : "Time"
						}, {
							name : "DEP",
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
						color = d3.scale.category10().domain(sortedItems);
						weekly_plotting(roundTrips);

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

							linechart
									.append("path")
									.attr("d", draw)
									.attr(
											"stroke",
											function(d) {
												return (d[$scope.roundTrips.fields.isCrewLink] == 1) ? "green"
														: (d[$scope.roundTrips.fields.crewLinkLine]) ? "yellow"
																: "#d62728";
											})
									.attr(
											"stroke-width",
											function(d) {
												if (d[$scope.roundTrips.fields.isCrewLink] == 1)
													return 1;
												else
													return 1.5;
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
							p
									.selectAll(".text")
									.data(mydata)
									.enter()
									.append("g")
									.attr("class", "foreground")
									.append("text")
									.attr("x", -200)
									.attr(
											"y",
											function(d) {
												myday = d[$scope.roundTrips.fields.arrivalDay];
												mytime = d[$scope.roundTrips.fields.arrivalTime];
												var val = null;
												if (mytime) {
													myhr = parseFloat(mytime
															.slice(0, 2));
													mymin = parseInt(mytime
															.slice(-2));
													val = theswitch(mytime,
															myday);
												}
												return other_yaxis(val) - 3;
											})
									.text(
											function(d) {
												var myvalues = [
														d[$scope.roundTrips.fields.station],
														d[$scope.roundTrips.fields.rtName],
														d[$scope.roundTrips.fields.arrivalTime],
														"("
																+ d[$scope.roundTrips.fields.duration]
																+ "m)" ];
												return (!d[$scope.roundTrips.fields.crewLinkLine]) ? myvalues
														: "";
											});

							// add details on DEP axis
							p
									.selectAll(".text")
									.data(mydata)
									.enter()
									.append("g")
									.attr("class", "foreground")
									.append("text")
									.attr("x", 360)
									.attr(
											"y",
											function(d) {
												myday = d[$scope.roundTrips.fields.departureDay];
												mytime = d[$scope.roundTrips.fields.departureTime];
												var val = null;
												if (mytime) {
													myhr = parseFloat(mytime
															.slice(0, 2));
													mymin = parseInt(mytime
															.slice(-2));
													val = theswitch(mytime,
															myday);
												}
												return other_yaxis(val) - 3;
											})
									.text(
											function(d) {
												var myvalues = [
														"("
																+ d[$scope.roundTrips.fields.duration]
																+ "m)",
														d[$scope.roundTrips.fields.departureTime],
														d[$scope.roundTrips.fields.rtName],
														d[$scope.roundTrips.fields.station] ];
												return (!d[$scope.roundTrips.fields.crewLinkLine]) ? myvalues
														: "";
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
												return (d[$scope.roundTrips.fields.isCrewLink] != 1 && !d[$scope.roundTrips.fields.crewLinkLine]) ? "#d62728"
														: "green";
											})
									.attr(
											"stroke-width",
											function(d) {
												if (d[$scope.roundTrips.fields.isCrewLink] != 1)
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
												return (d[$scope.roundTrips.fields.isCrewLink] != 1 && !d[$scope.roundTrips.fields.crewLinkLine]) ? "#d62728"
														: "green";
											})
									.attr(
											"stroke-width",
											function(d) {
												if (d[$scope.roundTrips.fields.isCrewLink] != 1)
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
										if (p.name == "ARR") {
											myday = d[$scope.roundTrips.fields.arrivalDay];
											mytime = d[$scope.roundTrips.fields.arrivalTime];
										} else {
											myday = d[$scope.roundTrips.fields.departureDay];
											mytime = d[$scope.roundTrips.fields.departureTime];
										}
										val = theswitch(mytime, myday);
										return [ x_axis(p.name), p.scale(val) ];
									}));
						}

						function arrdraw(d) {
							mytime = d[$scope.roundTrips.fields.arrivalTime];
							myday = d[$scope.roundTrips.fields.arrivalDay];
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
							mytime = d[$scope.roundTrips.fields.departureTime];
							myday = d[$scope.roundTrips.fields.departureDay];
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
								mymin = parseInt(currenttime.slice(3, 5));
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
																		+ (($scope.toggleRoundTrips) ? "activeLegend"
																				: ""))
														.on("click",
																loadRoundTripsOnChart)
														.style("stroke",
																"#d62728");
												g
														.append("rect")
														.attr("x",
																legend_w + 10)
														.attr("y",
																legend_h + 335)
														.attr("width", 50)
														.attr(
																"height",
																(($scope.selectedCrewLink) ? 1
																		: 0))
														.style("fill", "green")
														.attr(
																"class",
																"d3legend "
																		+ (($scope.toggleCrewLinks) ? "activeLegend"
																				: ""))
														.on("click",
																loadCrewLinksOnChart)
														.style(
																(($scope.selectedCrewLink) ? "stroke"
																		: ""),
																"green");
												g
														.append("rect")
														.attr("x",
																legend_w + 10)
														.attr("y",
																legend_h + 370)
														.attr("width", 50)
														.attr(
																"height",
																(($scope.selectedCrewLink) ? 1
																		: 0))
														.style("fill", "yellow")
														.attr(
																"class",
																"d3legend "
																		+ (($scope.toggleCrewLinks) ? "activeLegend"
																				: ""))
														.on("click",
																loadCrewLinksOnChart)
														.style(
																(($scope.selectedCrewLink) ? "stroke"
																		: ""),
																"yellow");
												g
														.append("text")
														.attr("x",
																legend_w + 65)
														.attr("y",
																legend_h + 305)
														.style(
																"fill",
																(($scope.toggleRoundTrips) ? "#d62728"
																		: "black"))
														.attr(
																"class",
																"d3legend "
																		+ (($scope.toggleRoundTrips) ? "activeLegend"
																				: ""))
														.on("click",
																loadRoundTripsOnChart)
														.text("R-Trip");
												g
														.append("text")
														.attr("x",
																legend_w + 65)
														.attr("y",
																legend_h + 340)
														.style(
																"fill",
																(($scope.toggleCrewLinks) ? "green"
																		: "black"))
														.attr(
																"class",
																"d3legend "
																		+ (($scope.toggleCrewLinks) ? "activeLegend"
																				: ""))
														.on("click",
																loadCrewLinksOnChart)
														.text(
																(($scope.selectedCrewLink) ? "C-Link"
																		: ""));
												g
														.append("text")
														.attr("x",
																legend_w + 65)
														.attr("y",
																legend_h + 375)
														.style(
																"fill",
																(($scope.toggleCrewLinks) ? "yellow"
																		: "black"))
														.attr(
																"class",
																"d3legend "
																		+ (($scope.toggleCrewLinks) ? "activeLegend"
																				: ""))
														.on("click",
																loadCrewLinksOnChart)
														.text(
																(($scope.selectedCrewLink) ? "C-Line"
																		: ""));
											});
						}
						function loadRoundTripsOnChart() {
							$scope.toggleRoundTrips = !$scope.toggleRoundTrips;
							$scope.generateChart($scope.toggleRoundTrips,
									$scope.toggleCrewLinks)
						}
						function loadCrewLinksOnChart() {
							$scope.toggleCrewLinks = !$scope.toggleCrewLinks;
							$scope.generateChart($scope.toggleRoundTrips,
									$scope.toggleCrewLinks)
						}

						function mouseover(d) {
							div.transition().duration(200).style("opacity", 1);
							div
									.html(
											((d[$scope.roundTrips.fields.isCrewLink] == 1 || d[$scope.roundTrips.fields.crewLinkLine]) ? "CREW LINK<br/>"
													: "ROUND TRIP<br/>")
													+ ((d[$scope.roundTrips.fields.rtName]) ? "Train No - "
															+ d[$scope.roundTrips.fields.rtName]
															+ "<br/>"
															: "")
													+ ((d[$scope.roundTrips.fields.crewLinkLine]) ? ((d[$scope.roundTrips.fields.arrivalTime]) ? "Arr Time - "
															+ d[$scope.roundTrips.fields.arrivalTime]
															+ "<br/>"
															: "")
															+ ((d[$scope.roundTrips.fields.arrivalDay]) ? "Arr Day - "
																	+ d[$scope.roundTrips.fields.arrivalDay]
																	+ "<br/>"
																	: "")
															+ ((d[$scope.roundTrips.fields.departureTime]) ? "Dep Time - "
																	+ d[$scope.roundTrips.fields.departureTime]
																	+ "<br/>"
																	: "")
															+ ((d[$scope.roundTrips.fields.departureDay]) ? "Dep Day - "
																	+ d[$scope.roundTrips.fields.departureDay]
																	+ "<br/>"
																	: "")
															: ((d[$scope.roundTrips.fields.station]) ? "From - "
																	+ d[$scope.roundTrips.fields.station]
																	+ "<br/>"
																	: "")
																	+ ((d[$scope.roundTrips.fields.departureTime]) ? "Dep Time - "
																			+ d[$scope.roundTrips.fields.departureTime]
																			+ "<br/>"
																			: "")
																	+ ((d[$scope.roundTrips.fields.departureDay]) ? "Dep Day - "
																			+ d[$scope.roundTrips.fields.departureDay]
																			+ "<br/>"
																			: "")
																	+ ((d[$scope.roundTrips.fields.station]) ? "To - "
																			+ d[$scope.roundTrips.fields.station]
																			+ "<br/>"
																			: "")
																	+ ((d[$scope.roundTrips.fields.arrivalTime]) ? "Arr Time - "
																			+ d[$scope.roundTrips.fields.arrivalTime]
																			+ "<br/>"
																			: "")
																	+ ((d[$scope.roundTrips.fields.arrivalDay]) ? "Arr Day - "
																			+ d[$scope.roundTrips.fields.arrivalDay]
																			+ "<br/>"
																			: ""))
													+ ((d[$scope.roundTrips.fields.duration]) ? "Duration - "
															+ d[$scope.roundTrips.fields.duration]
															+ "mins<br/>"
															: "")).style(
											"left",
											(d3.event.pageX - 11) + "px")
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

/**
 * Directives for drag and drop table rows
 */
angular.module('crewLinkApp').directive(
		'droppable',
		[
				'$parse',
				function($parse) {
					return {
						link : function(scope, element, attr) {
							function onDragOver(e) {
								if (e.preventDefault) {
									e.preventDefault();
								}
								if (e.stopPropagation) {
									e.stopPropagation();
								}
								e.dataTransfer.dropEffect = 'move';
								return false;
							}
							function onDrop(e) {
								if (e.preventDefault) {
									e.preventDefault();
								}
								if (e.stopPropagation) {
									e.stopPropagation();
								}
								var data = e.dataTransfer.getData("rowData");
								data = angular.fromJson(data);
								var dropfn = attr.drop;
								var fn = $parse(attr.drop);
								var rowDroppedAt = jQuery(e.target).parents(
										"tr")[0].sectionRowIndex;
								scope.$apply(function() {

									scope[dropfn](data, rowDroppedAt);
								});
							}
							element.bind("dragover", onDragOver);
							element.bind("drop", onDrop);
						}
					};
				} ]);

/**
 * Directives for drag and drop table rows
 */
angular.module('crewLinkApp').directive(
		'draggable',
		function() {
			return {
				link : function(scope, elem, attr) {
					if (window.jQuery
							&& !window.jQuery.event.props.dataTransfer) {
						window.jQuery.event.props.push('dataTransfer');
					}
					elem.attr("draggable", true);
					var dragDataVal = '';
					var draggedGhostImgElemId = '';
					attr.$observe('dragdata', function(newVal) {
						dragDataVal = newVal;
					});
					attr.$observe('dragimage', function(newVal) {
						draggedGhostImgElemId = newVal;
					});
					elem.bind("dragstart", function dragstart(e) {
						var sendData = angular.toJson(dragDataVal);
						e.dataTransfer.setData("rowData", sendData);
						if (attr.dragimage !== 'undefined') {
							e.dataTransfer.setDragImage(document
									.getElementById(draggedGhostImgElemId), 0,
									0);
						}
						var dragFn = attr.drag;
						if (dragFn !== 'undefined') {
							scope.$apply(function() {
								scope[dragFn](sendData);
							})
						}
					});
				}
			};
		});

/**
 * Directive for drag and drop table
 */
angular
		.module('crewLinkApp')
		.directive(
				'angTable',
				[
						'$compile',
						function($compile) {
							return {
								restrict : 'E',
								templateUrl : 'scripts/directives/draggableTable/draggabletabletemplate.html',
								replace : true,
								scope : {
									conf : "="
								},
								controller : function($scope, $attrs) {
									var parentScope = angular
											.element(
													document
															.getElementById("selectedRoundTripsTableId"))
											.scope();
									$scope.dragIndex = 0;
									$scope.dragImageId = "dragtable";
									$scope.handleDrop = function(draggedData,
											targetElem) {
										function swapArrayElements(
												array_object, index_a, index_b) {
											if (Object
													.keys($scope.selectedRoundTripsToDrag).length > 0) {
												// Logic to drop selected round
												// trips at dropped location if
												// round trips are selected and
												// gragged
												var count = -1;
												// Removing selected round trips
												// to drag and drop from dragged
												// position
												angular
														.forEach(
																$scope.selectedRoundTripsToDrag,
																function(
																		selectedRoundTrip) {
																	selectedRoundTrip.cssClass = null;
																	var index = $scope.conf.selectedRoundTripsForDragAndDropTable
																			.indexOf(selectedRoundTrip);
																	$scope.conf.selectedRoundTripsForDragAndDropTable
																			.splice(
																					index,
																					1);
																	if (index_b > index)
																		count++;
																});
												if (count == -1) {
													count = 0;
												}
												index_b = index_b - count;
												// Adding selected round trips
												// to drag and drop at dropped
												// position
												angular
														.forEach(
																$scope.selectedRoundTripsToDrag,
																function(
																		selectedRoundTrip) {
																	$scope.conf.selectedRoundTripsForDragAndDropTable
																			.splice(
																					index_b,
																					0,
																					selectedRoundTrip);
																	index_b++;
																});
												$scope.selectedRoundTripsToDrag = {};
											} else {
												// Logic to drop selected round
												// trip at dropped location if
												// round trip is dragged with
												// put selecting
												var temp = array_object[index_a];
												var index = array_object
														.indexOf(array_object[index_a]);
												// Remove round trip from
												// dragged position
												array_object.splice(index, 1);
												// Add round trip at dropped
												// position
												array_object.splice(index_b, 0,
														temp);
											}
											// Calculate HQrest after drag and
											// drop
											parentScope.calculateHQRest();
										}
										$scope.selectedRoundTripsToDragLength = 0;
										swapArrayElements(
												$scope.conf.selectedRoundTripsForDragAndDropTable,
												draggedData, targetElem);
									};
									$scope.handleDrag = function(rowIndex) {
										if (rowIndex !== null) {
											$scope.dragIndex = rowIndex
													.replace(/["']/g, "");
											;
										}
									};
									$scope.selectedRoundTripsToDrag = {};
									$scope.selectedRoundTripsToDragLength = 0;
									// Logic to add/remove green back ground
									// color for round trip row on clicking of
									// it
									$scope.selectOrDeselectRoundTripToDrag = function(
											index) {
										if ($scope.conf.selectedRoundTripsForDragAndDropTable[index].cssClass) {
											$scope.conf.selectedRoundTripsForDragAndDropTable[index].cssClass = null;
											delete $scope.selectedRoundTripsForDragAndDropTableToDrag[index];
										} else {
											$scope.selectedRoundTripsToDrag[index] = $scope.conf.selectedRoundTripsForDragAndDropTable[index];
											$scope.conf.selectedRoundTripsForDragAndDropTable[index].cssClass = "selected-train-section";
										}
										$scope.selectedRoundTripsToDragLength = Object
												.keys($scope.selectedRoundTripsToDrag).length;
									}
									$scope.unSelectRoundTrip = function(index) {
										parentScope.unSelectRoundTrip(index);
									}
								},
								compile : function(elem) {
									return function(ielem, $scope) {
										$compile(ielem)($scope);
									};
								}
							};
						} ]);
