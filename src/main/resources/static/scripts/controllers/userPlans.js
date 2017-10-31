'use strict';
/**
 * This UserPlans controller is used to 1. Save the NewUserPlan in DB 2. Select
 * the UserPlan for Update in DB 3. Delete the UserPlan from DB
 * 
 * @authors Vivek Yadav, Laxman
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 14, 2016
 */

angular
		.module('crewLinkApp')
		.controller(
				'UserPlansCtrl',
				function($q, $scope, $location, $http, $resource,
						SpringDataRestAdapter, SpringDataRestApi, UserService,
						toaster) {

					// Init the list
					$scope.userPlans = [];

					$scope.setInitValues = function() {
						$scope.isSave = true;
						$scope.action = "Create New";
						$scope.userPlan = {};
						$scope.submitted = false;
					}

					/**
					 * Fetch the list First time
					 */
					$scope.setInitValues();

					/**
					 * The GetUser is Function is Used to fetch the Current
					 * UserPlan based on Logged User
					 */
					UserService
							.getUser()
							.then(
									function(user) {
										$scope.user = user;
										SpringDataRestApi
												.get(
														'/api/userPlans/search/findByAllParams?username='
																+ user.username
																+ '&name=')
												.then(
														function(response) {
															$scope.userPlans = response._embedded.userPlans;
														});
									});

					/**
					 * Fetch the list based on search Term
					 */
					$scope.searchUserPlan = function(term) {
						// No search term: return initial items
						if (!term) {
							return $scope.userPlans;
						}
						var deferred = $q.defer();
						UserService
								.getUser()
								.then(
										function(user) {
											SpringDataRestApi
													.get(
															'/api/userPlans/search/findByAllParams?username='
																	+ user.username
																	+ '&name='
																	+ term
																	+ '&limit=10')
													.then(
															function(response) {
																$scope.userPlans = response._embedded.userPlans;
																deferred
																		.resolve($scope.userPlans);
															});
										});
						return deferred.promise;
					}
					/**
					 * The selectUserPlan Function is Used to select the Current
					 * Userplan and redirected to Home
					 */
					$scope.selectUserPlan = function(userPlan) {
						UserService.setSelectedUserPlan(userPlan);
						$location.path('/dashboard/home');
					};

					/**
					 * The save Function is Used to save the Newly Created Plan
					 */
					$scope.save = function(userPlan, type) {
						if ($scope.userPlans.length > 9) { // A User can be
															// created Max 10
															// UserPlan
							toaster
									.pop({
										type : 'error',
										title : 'Error',
										body : 'You can add more than 10 User Plans . Please delete existing user plan to add new one'
									});
						} else {
							if (userPlan.name)
								UserService
										.getUser()
										.then(
												function(user) {
													if (!userPlan._ref) {
														userPlan._ref = {};
													}
													userPlan._ref.user = user;
													SpringDataRestApi
															.save(userPlan,
																	'userPlans')
															.then(
																	function(
																			response) { // On
																						// Success
																						// Create
																						// a
																						// new
																						// Play
																						// based
																						// on
																						// Type
																		if (type == "create") // The
																								// type
																								// is
																								// Used
																								// to
																								// Create
																								// New
																								// Plan
																			$scope.userPlans
																					.push(userPlan); // Push
																										// the
																										// New
																										// Plan
																										// to
																										// the
																										// Array
																		toaster
																				.pop({
																					type : 'success',
																					title : 'User Plan '
																							+ type
																							+ 'd',
																					body : 'User Plan '
																							+ type
																							+ 'd Successfully!!!'
																				});
																		$scope
																				.setInitValues(); // Set
																									// the
																									// InitialValues
																									// once
																									// Plan
																									// Created
																	},
																	function(
																			response) { // On
																						// Error
																						// :
																						// Dispplay
																						// popUp
																						// with
																						// Error
																						// Messages
																		toaster
																				.pop({
																					type : 'error',
																					title : 'Error',
																					body : 'Unable To '
																							+ type
																							+ ' User Plan . Please Try with different Plan name!!!'
																				});
																	});
												});
						}
					};
					/**
					 * The updateUserPlan Function is Used to Update the
					 * selected UserPlan
					 */
					$scope.updateUserPlan = function(userplan) {
						$scope.userPlan = userplan; // assign the userPlan for
													// Update
						$scope.copyUserPlan = {
							sourceUserPlanName : userplan.name,
							sourceUserPlanTimeStamp : userplan.timeStamp,
							newUserPlanName : 'Copy of - ' + userplan.name,
							username : $scope.user.username
						};
						$scope.isSave = false; // set the Issave boolean
												// variable to false
						$scope.action = "Update";
					}
					$scope.copyPlan = function() {

						if (!$scope.copyUserPlan
								|| !$scope.copyUserPlan.newUserPlanName
								|| $scope.copyUserPlan.newUserPlanName == '') {
							$scope.copyUserPlanError = true;
							return;
						}
						$scope.isCopying = true;
						$http
								.post('/api/custom/userPlan/copy',
										$scope.copyUserPlan)
								.then(
										function(res) {
											toaster
													.pop({
														type : 'success',
														title : 'User Plan Copied',
														body : 'User Plan Copied Successfully!!!'
													});
											$scope.isCopying = false;
											UserService
													.getUser()
													.then(
															function(user) {
																SpringDataRestApi
																		.get(
																				'/api/userPlans/search/findByAllParams?username='
																						+ user.username
																						+ '&name=')
																		.then(
																				function(
																						response) {
																					$scope.userPlans = response._embedded.userPlans;
																				});
															});
											$scope.setInitValues();
										},
										function(res) {
											$scope.isCopying = false;
											var msg = "";
											if (res.data.errorMessage) {
												msg += res.data.errorMessage
											}
											if (res.data.message) {
												msg += res.data.message
											}
											toaster
													.pop({
														type : 'error',
														title : 'Error',
														body : msg
																+ ' Unable To Copy User Plan . Please Try Again!!!'
													});
										});
					}

					/**   
					 * The DeleteUserPlan Function is Used to Removed the selected UserPlan 
					 */
					$scope.deleteUserPlan = function(userplan) {
						SpringDataRestApi
								.remove(userplan)
								.then(
										function(response) //On Success Remove the UserPlan from DB as well as Array and display the PopUp Message
										{
											$scope.userPlans.splice(
													$scope.userPlans
															.indexOf(userplan),
													1);
											toaster
													.pop({
														type : 'success',
														title : 'User Plan Removed',
														body : 'User Plan Removed Successfully!!!'
													});
											var id = userplan._links.self.href
													.match(/.*\/(.*?)$/)[1];
											var selectedUserPlanId = UserService
													.getSelectedUserPlan()._links.self.href
													.match(/.*\/(.*?)$/)[1];
											if (id == selectedUserPlanId) {
												UserService
														.clearSelectedUserPlan();
											}
										},
										function(response) //On Error PopUp Message should be displayed with the 'Unable to Remove'
										{
											toaster
													.pop({
														type : 'error',
														title : 'Error',
														body : 'Unable To Remove User Plan . Please Try Again!!!'
													});

										});

					}
				});
