'use strict';
/**
 * This CrewType directive is used to
 * 1. save new CrewType
 * 3. Reset all CrewForm text fields   
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */
angular.module('crewLinkApp')
		.directive('crewTypeForm',function() {
					return {
						templateUrl : 'scripts/directives/dashboard/crewTypeForm/crewTypeForm.html',  // load CrewTypeForm from the following location
						restrict : 'E',   // Restrict E: only matches Element name
						replace : true,  //  Replace : When replace: true, the template or templateUrl must be required. 
						scope : {       //   Scope   : scope of the Directive 
							'crewtypedetails' : '='   
						},
						controller : function($scope, $location, $anchorScroll, $http, SpringDataRestApi, SpringDataRestAdapter, toaster) {
							$scope.blankcrewTypedetails = {};
							if (!$scope.crewTypedetails)
								$scope.crewTypedetails = {};
							$scope.submitClass='show-errors';
							
							/**
							 *saveCrewType Function is Used to Save the New crewType 
							 */
							$scope.saveCrewType = function(crewTypeobj, saveType) {
								SpringDataRestApi.save(crewTypeobj, 'crewTypes').then(  //springDataRestApi is used to save a new CrewType
								function(response){
									toaster.pop({type: 'success', title: 'New Crew Type ' +saveType.capitalizeFirstLetter() 
										+'d.', body: 'Crew Type '+ saveType.capitalizeFirstLetter() +'d'+' Successfully!!!'});
									
									if (saveType == 'create') 
										$scope.$parent.crewTypes.push(crewTypeobj); // Push the crewTypeobj Object to crewTypes Array, which is Present in crewtype.js (Crewtype Controller)    
									    $scope.reset(); // call the 'Reset function' to reset all the values
								}, 
								function(response){
									(response);
									toaster.pop({type: 'error', title: 'Error', body: 'Unable To '+ saveType.capitalizeFirstLetter()+' Crew Type. Please Try Again!!!'});
						
								});
							};
							
							
							/**
							 *   reset Function is Used to Reset all the Fields 
							 */
							$scope.reset = function() {
								$scope.crewtypedetails = angular.copy($scope.blankcrewTypedetails); // copy an Empty Array to crewtypedetails array
								$scope.submitClass="hide-errors"; //   Hide Error is a class
							};
							
							
							/**
							 *   Angular $watch Function to monitor crewtypedetails variables   
							 */
							  $scope.$watch('crewtypedetails', function(c){
						    	  $location.hash('crewTypeFormDiv');  //It scrolls to the element related to the specified hash
							      $anchorScroll();  
					          });
						}
					}
				});
