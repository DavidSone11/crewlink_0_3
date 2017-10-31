'use strict';
/**
 * This Station directive is used to
 * 1. Save the saveStation 
 * 2. Reset the textField 
 * 3. clear the Input
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 14, 2016
 */

angular.module('crewLinkApp').directive('stationForm', function() {
	return {
		templateUrl : 'scripts/directives/dashboard/stationForm/stationForm.html',
		restrict : 'E',
		replace : true,
		scope : {
			'stationdetails' : '='
		},
		controller : function($scope, $location, $anchorScroll, $http, SpringDataRestApi, SpringDataRestAdapter, toaster) {
			
		      
		      $scope.blankstationdetails = {};
				if (!$scope.stationdetails)
					$scope.stationdetails = {};
				$scope.submitClass='show-errors';
				/**
				 *  This Function is Used to Save the New Station
				 * 
				 */
				$scope.saveStation = function(stationobj, saveType) {
					SpringDataRestApi.save(stationobj, 'stations').then(
					function(response){
						toaster.pop({type: 'success', title: 'New Station ' +saveType.capitalizeFirstLetter() +'d.', body: 'Station '+ saveType.capitalizeFirstLetter() +'d'+' Successfully!!!'});
						if (saveType == 'create') 
							$scope.$parent.stations.push(stationobj);
						$scope.reset();
					
					}, 
					function(response){
						toaster.pop({type: 'error', title: 'Error', body: 'Unable To '+ saveType.capitalizeFirstLetter()+' Station. Please Try Again!!!'});
					});
				};
			
				/**
				 *  This function is Used to reset the TextFields 
				 * 
				 */
				$scope.reset = function() {
					$scope.stationdetails = angular.copy($scope.blankstationdetails);
					$scope.clearInput();
					$scope.submitClass="hide-errors";
				};
				/**
				 *  This function is Used to clearInput once Saved 
				 * 
				 */
				$scope.clearInput = function(id) {
					if (id) {$scope.$broadcast('angucomplete-alt:clearInput', id);} 
					else {$scope.$broadcast('angucomplete-alt:clearInput');}
				}
				/** 
				 *   To watch the StationDetails variables
				 * 
				 */
				 $scope.$watch('stationdetails', function(c) {
			    	  $location.hash('stationFormDiv');
				      $anchorScroll();
		          });
			}
		}
	});
