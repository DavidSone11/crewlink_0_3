'use strict';
/**
 * This pilotTypeForm directive is used to
 * 1. save new savePilotTrip
 * 2. clearInput   
 * 3. watch the Angular variable using $watch Function
 * 4. Retrive the station based on Query  
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 15, 2016
 */

angular.module('crewLinkApp')
		.directive('pilotTypeForm',function() {
					return {
						templateUrl : 'scripts/directives/dashboard/pilotTypeForm/pilotTypeForm.html', // load pilotTripForm from the following location
						restrict : 'E',   // Restrict E: only matches Element name
						replace : true,  //  Replace : When replace: true, the template or templateUrl must be required.
						scope : {		//   Scope   : scope of the Directive
							'pilottypedetails' : '='
						},
						controller : function($scope, $location, $anchorScroll, $http, SpringDataRestApi, SpringDataRestAdapter, toaster) {
							$scope.blankpilotTypedetails = {};
							if (!$scope.pilotTypedetails)
								$scope.pilotTypedetails = {};
							$scope.submitClass='show-errors';
							/**
							 *  This savePilotType function is Used to save the newly created PilotType 
							 */
							$scope.savePilotType = function(pilotTypeobj, saveType){
								SpringDataRestApi.save(pilotTypeobj, 'pilotTypes').then(
								function(response){
									toaster.pop({type: 'success', title: 'New Pilot Type ' +saveType.capitalizeFirstLetter() +'d.', body: 'Pilot Type '+ saveType.capitalizeFirstLetter() +'d'+' Successfully!!!'});
									if (saveType == 'create') 
										$scope.$parent.pilotTypes.push(pilotTypeobj);
									$scope.reset();
								}, 
								function(response){
									toaster.pop({type: 'error', title: 'Error', body: 'Unable To '+ saveType.capitalizeFirstLetter()+' Pilot Type. Please Try Again!!!'});
						
								});
							};
							
							/**
							 *  This reset function is Used to reset the Form 
							 */
							$scope.reset = function() {
								$scope.pilottypedetails = angular.copy($scope.blankpilotTypedetails);
								$scope.submitClass="hide-errors";
							};
							/**
							 *  Angular watch function is Used to watch the pilottypedetails
							 */
							  $scope.$watch('pilottypedetails', function(c) {
						    	  $location.hash('pilotTypeFormDiv');
							      $anchorScroll();
					          });
						}
					}
				});
