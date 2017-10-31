'use strict';
/**
 * This TrainFrom Directive is Used for 
 * 1. Create a New Train Type 
 * @author Vivek Yadav,Santosh sahu
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */

angular.module('crewLinkApp')
		.directive('trainTypeForm',function() {
					return {
						templateUrl : 'scripts/directives/dashboard/trainTypeForm/trainTypeForm.html',
						restrict : 'E',
						replace : true,
						scope : {
							'traintypedetails' : '='
						},
						controller : function($scope, $location, $anchorScroll, $http, SpringDataRestApi, SpringDataRestAdapter, toaster) {
							$scope.blanktrainTypedetails = {};
							if (!$scope.trainTypedetails)
								$scope.trainTypedetails = {};
							$scope.submitClass='show-errors';
							
							/**
							 * Save Function is Used to Save the New Train Type
							 */
							$scope.saveTrainType = function(trainTypeobj, saveType) {
								SpringDataRestApi.save(trainTypeobj, 'trainTypes').then(
								function(response) 
								{
									toaster.pop({type: 'success', title: 'New Train Type' +saveType.capitalizeFirstLetter() +'d.', body: 'Train Type'+ saveType.capitalizeFirstLetter() +'d'+' Successfully!!!'});
									
									if (saveType == 'create') 
										$scope.$parent.trainTypes.push(trainTypeobj);
									$scope.reset();
								}, 
								function(response) 
								{
									(response);
									toaster.pop({type: 'error', title: 'Error', body: 'Unable To '+ saveType.capitalizeFirstLetter()+' Train Type. Please Try Again!!!'});
						
								});
							};

							/**
							 * Reset Function is Used to reset the Form
							 */
							$scope.reset = function() {
								$scope.traintypedetails = angular.copy($scope.blanktrainTypedetails);
								$scope.submitClass="hide-errors";
							};
							
							  $scope.$watch('traintypedetails', function(c) {
						    	  $location.hash('trainTypeFormDiv');
							      $anchorScroll();
					          });
						}
					}
				});
