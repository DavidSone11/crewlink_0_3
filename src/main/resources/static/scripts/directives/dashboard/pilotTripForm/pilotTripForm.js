'use strict';
/**
 * This PilotTripForm directive is used to
 * 1. save new savePilotTrip
 * 2. clearInput   
 * 3. watch the Angular variable using $watch Function
 * 4. Retrive the station based on Query  
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 15, 2016
 */

angular.module('crewLinkApp').directive('pilotTripForm', function() {
	return {
		templateUrl : 'scripts/directives/dashboard/pilotTripForm/pilotTripForm.html', // load pilotTripForm from the following location
		restrict : 'E',   // Restrict E: only matches Element name
		replace : true,  //  Replace : When replace: true, the template or templateUrl must be required.
		scope : {		//   Scope   : scope of the Directive
			'pilottripdetails' : '='
		},
		controller : function($scope, $location, $anchorScroll, $http, SpringDataRestApi, SpringDataRestAdapter, toaster) {
			
		      $scope.blankpilottripdetails = {};
				if (!$scope.pilottripdetails)
					$scope.pilottripdetails = {};
				$scope.submitClass='show-errors';
				/**
				 *  This SpringDataRestApi.list api is Used to list all pilot Types  
				 */
				SpringDataRestApi.list('pilotTypes').then(function(response) {
					$scope.pilotTypes = response;
				});
				
				/**
				 *  Angular watch function is Used to watch the pilottripdetails
				 */
				$scope.$watch('pilottripdetails',function() {
					if ($scope.pilottripdetails) 
						if ($scope.pilottripdetails._links) 
							$scope.pilottripdetails._ref.pilotType = $scope.pilotTypes.findWithHref($scope.pilottripdetails._ref.pilotType);
				}, true);
				
				/**
				 *  This SavePilotTrip function is Used to save the newly created Pilot Trip 
				 */
							$scope.savePilotTrip = function(pilotTripobj,
									saveType) {
								(pilotTripobj);
								SpringDataRestApi
										.save(pilotTripobj, 'pilotTrips')
										.then(
												function(response) {
													toaster
															.pop({
																type : 'success',
																title : 'New Pilot Trip '
																		+ saveType
																				.capitalizeFirstLetter()
																		+ 'd.',
																body : 'Pilot Trip '
																		+ saveType
																				.capitalizeFirstLetter()
																		+ 'd'
																		+ ' Successfully!!!'
															});

													if (saveType == 'create')
														$scope.$parent.pilotTrips
																.push(pilotTripobj);
													$scope.reset();
												},
												function(response) {
													(response);
													toaster
															.pop({
																type : 'error',
																title : 'Error',
																body : 'Unable To '
																		+ saveType
																				.capitalizeFirstLetter()
																		+ ' Pilot Trip. Please Try Again!!!'
															});

												});
							};
				
				
							/**
							 *  This getStation function is Used to fetch the desire station based on Query 
							 */
				$scope.getStation = function(query, timeout) {return SpringDataRestAdapter.process($http.get('/api/stations/search/findByCodeContains?code='+ query),[], true);
				
				}
				
				/**
				 *  This fromStationSelected function is Used to select the FromStationFrom from DB 
				 */
				$scope.fromStationSelected = function(selected) {
								if (selected) {
									if (!$scope.pilottripdetails._ref) {
										$scope.pilottripdetails._ref = {};
									}
									$scope.pilottripdetails._ref.fromStation = selected.originalObject;
								}
							}
				
				/**
				 *  This toStationSelected function is Used to select the toStationFrom from DB 
				 */	

				$scope.toStationSelected = function(selected) {
				if (selected) {
					if (!$scope.pilottripdetails._ref){
						$scope.pilottripdetails._ref = {};
						}
					$scope.pilottripdetails._ref.toStation = selected.originalObject;
				}
			 }
				/**
				 *  This getDivision function is Used to fetch the division 
				 */	
				$scope.getDivision = function(query, timeout) {return SpringDataRestAdapter.process($http.get('/api/divisions/search/findByNameContains?name='+ query),[], true);
				
				}
				
				/**
				 *  This divisionSelected function is Used to select the division 
				 */		
				$scope.divisionSelected = function(selected) {
								if (selected) {
									if (!$scope.pilottripdetails._ref) {
										$scope.pilottripdetails._ref = {};
									}
									$scope.pilottripdetails._ref.division = selected.originalObject;
								}
				}
				
				/**
				 *  This reset function is Used to reset the Form 
				 */
				$scope.reset = function() {
					$scope.pilottripdetails = angular.copy($scope.blankpilottripdetails);
					$scope.clearInput();
					$scope.submitClass="hide-errors";
					
				};
				/**
				 *  This clearInput function is Used to clear all the TextField once Saved 
				 */
				$scope.clearInput = function(id) {
					if (id) {$scope.$broadcast('angucomplete-alt:clearInput', id);} 
					else {$scope.$broadcast('angucomplete-alt:clearInput');}
				}
				/**
				 *  Angular watch function is Used to watch the Pilottripdetails
				 */
				  $scope.$watch('pilottripdetails', function(c) {
			    	  $location.hash('pilotTripFormDiv'); //It scrolls to the element related to the specified hash
				      $anchorScroll();
		          });
			}
		}
	});
