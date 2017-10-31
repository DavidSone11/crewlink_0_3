'use strict';
/**
 * This divisionForm directive is used to
 * 1. save new divison
 * 2. Reset division Form    
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 15, 2016
 */

angular.module('crewLinkApp')
		.directive('divisionForm',function() {
					return {
						templateUrl : 'scripts/directives/dashboard/divisionForm/divisionForm.html', // load divisionForm from the following location
						restrict : 'E',   // Restrict E: only matches Element name
						replace : true,  //  Replace : When replace: true, the template or templateUrl must be required.
						scope : {		//   Scope   : scope of the Directive
							'divisiondetails' : '='
						},
						controller : function($scope, $location, $anchorScroll, $http, SpringDataRestApi, SpringDataRestAdapter, toaster) {
							$scope.blankdivisiondetails = {};
							if (!$scope.divisiondetails)
								$scope.divisiondetails = {};
							$scope.submitClass='show-errors';
							/**
							 *  This saveDivision function is Used to create a new Division 
							 */
							$scope.saveDivision = function(divisionobj, saveType) {
								SpringDataRestApi.save(divisionobj, 'divisions').then(
								function(response){ // On Success Response: save the Division
									toaster.pop({type: 'success', title: 'New Division ' +saveType.capitalizeFirstLetter() +'d.', body: 'Division '+ saveType.capitalizeFirstLetter() +'d'+' Successfully!!!'});
									if (saveType == 'create') // If the Type is 'create' then Push the Object to division Array 
										$scope.$parent.divisions.push(divisionobj);
										$scope.reset(); // Once Push reset the Windows
								},   
								function(response){ //OnError Response: PopUp the Toaster
									toaster.pop({type: 'error', title: 'Error', body: 'Unable To '+ saveType.capitalizeFirstLetter()+' Division. Please Try Again!!!'});
						
								});
							};

							/**
							 *  This Rest function is Used to Reset the Form 
							 */
							$scope.reset = function() { 
								$scope.divisiondetails = angular.copy($scope.blankdivisiondetails);
								$scope.submitClass="hide-errors";
							};
							/**
							 *  This Angular Watch function is Used to watch the divisiondetails variable 
							 */
							  $scope.$watch('divisiondetails', function(c) {
						    	  $location.hash('divisionFormDiv');
							      $anchorScroll();
					          });
						}
					}
				});
