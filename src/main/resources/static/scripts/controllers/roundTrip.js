'use strict';

/**
 * This RoundTrip controller is used to 1. List all the Round Trips from DB 2.
 * List driving duties of particular Round Trip from DB 3. Delete the Round Trip
 * from DB
 * 
 * @author
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 16, 2016
 */

angular
		.module('crewLinkApp')
		.controller(
				'RoundTripCtrl',
				function($scope, $position, $http, $resource, $confirm, UserService,
						SpringDataRestAdapter, SpringDataRestApi, toaster, $q) {
					$scope.Days = Days;
					$scope.roundTripList = [];
					$scope.endDayArray = [];
					$scope.arrivalTimes = [];
					$scope.selectedValue = 'false';
					$scope.crewTypes = [];

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
					$scope.getCrewTypeName = function(crewTypeId) {
						for ( var item in crewTypes) {
							if (item.id == crewTypeId) {
								return item.name;
							}
						}
						return "";
					};

					$scope.updateRoundTripUrl = function(tableState) {
						if (!tableState.search.predicateObject) {
							tableState.search.predicateObject = {};
						}
						if (tableState.search.predicateObject) {
							if (tableState.search.predicateObject["crewType"] != $scope.crewType)
								tableState.search.predicateObject["crewType"] = $scope.crewType;
						}
						var uri = "/api/custom/roundTrips/list?userPlan="
								+ UserService.getSelectedUserPlan().id;

						return uri;
					};

					/*
					 * Server call to fetch all round trips list
					 */
					$scope.serverFetch = new ServerTableFetch2(
							$scope.updateRoundTripUrl.bind(this),
							SpringDataRestApi, function() {
								$scope.isLoading = true;
							}, function(resultObj) { // After processing this
														// is called
								$scope.roundTripList = resultObj;
								$scope.isLoading = false;
								
							}, function(error) {
							}, true);

					$scope.drivingDutyList = [];
					/**
					 * This function is used to fetch all driving duties of
					 * passed round trip
					 */
					$scope.viewDrivingDuty = function(roundTrip) {
						$http.get(
								"api/custom/drivingDuties/list?userPlan="
										+ UserService.getSelectedUserPlan().id
										+ "&roundTrip=" + roundTrip[0]
										+ "&sort=roundTripOrderNo ASC").then(
								function(response) {
									$scope.drivingDutyList = response.data;
								});
					};

					/**
					 * This function is used to remove round trip
					 */
					$scope.removeRoundTrip = function(roundTripId) {
						$http.get('/api/custom/dashboards/listDependencies?searchItem=roundTrip&searchValue='+roundTripId
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
								text : "Are you sure you want to delete these items too ? :   Links: ["+strLinks+"]",
								headerClass : 'confirm-header-danger',
								okButtonClass : "btn-danger"
							}).then(function() {
								SpringDataRestApi
								.deleteItem(
										$scope.roundTripList.selectionDetails.baseItemRestUri
												+ roundTripId)
								.then(
										function(response) {
											$scope.temp = [];
											var roundTripIndex = 0;
											// Logic to remove deleted round
											// trip from round trip list
											for (var i = 0; i < $scope.roundTripList.data.length; i++) {
												console
														.log($scope.roundTripList.data[i][0]);
												var count = 0;
												if ($scope.roundTripList.data[i][0] == roundTripId) {
													roundTripIndex = i;
													break;
												}
											}
											$scope.roundTripList.data.splice(
													roundTripIndex, 1);
											toaster
													.pop({
														type : 'success',
														title : 'RoundTrip Removed',
														body : 'RoundTrip Removed Successfully!!!'
													});
										}.bind(this),
										function(response) {
											toaster
													.pop({
														type : 'error',
														title : 'Error',
														body : 'Unable To Remove RoundTrip. Please Try Again!!!'
													});

										}.bind(this));
								
							}.bind(this));
						}.bind(this),function(res){
							
						});
						
						
					};

					$scope.updateNotIgnore = function() {
						angular
								.forEach(
										$scope.roundTripList.data,
										function(roundTrip) {
											if (roundTrip[$scope.roundTripList.fields.isIgnore] == true) {
												roundTrip[$scope.roundTripList.fields.isIgnore] = false;
												$scope
														.updateRoundTripIsIgnore(roundTrip);
											}
										});
					};

					$scope.updateIgnoreAll = function() {
						angular
								.forEach(
										$scope.roundTripList.data,
										function(roundTrip) {
											if (roundTrip[$scope.roundTripList.fields.isIgnore] == false) {
												roundTrip[$scope.roundTripList.fields.isIgnore] = true;
												$scope
														.updateRoundTripIsIgnore(roundTrip);
											}
										});
					}

					$scope.updateRoundTripIsIgnore = function(round) {
						var uri = "/api/custom/roundTrips/updateRoundTripIsIgnore?userPlan="
								+ UserService.getSelectedUserPlan().id
								+ "&roundTrip="
								+ round[$scope.roundTripList.fields.id]
								+ "&isIgnore="
								+ round[$scope.roundTripList.fields.isIgnore];
						$http
								.get(uri)
								.then(
										function(response) {
											if (response.data.result) {
												toaster
														.pop({
															type : 'success',
															title : 'RoundTrip update to user list',
															body : 'RoundTrip update to user list is successfully !!!'
														});
											} else {
												toaster
														.pop({
															type : 'error',
															title : 'Error',
															body : 'Unable to update RoundTrip to user list. Please Try Again!!! '
																	+ response.data.errorMessage
														});
											}
										},
										function(response) {
											toaster
													.pop({
														type : 'error',
														title : 'Error',
														body : 'Unable to update DrivingDuty to user list. Please Try Again!!! '
													});

										});

					}

				});
