'use strict';
/**
 * This Train directive is used to
 * Save the new Train 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */

angular.module('crewLinkApp').directive('trainForm', function() {
	return {
		templateUrl : 'scripts/directives/dashboard/trainForm/trainForm.html',
		restrict : 'E',
		replace : true,
		scope : {
			'traindetails' : '='
		},
		controller : function($scope, $location, $anchorScroll, $http, SpringDataRestApi, SpringDataRestAdapter, toaster) {
			
		      $scope.data = {
		    		    Days : [ 'SUNDAY','MONDAY','TUESDAY','WEDNESDAY', 'THURSDAY', 'FRIDAY','SATURDAY']
		      }
		     
		      $scope.blanktraindetails = {};
				if (!$scope.traindetails)
					$scope.traindetails = {};
				$scope.submitClass='show-errors';
				SpringDataRestApi.list('trainTypes').then(function(response) {
					$scope.trainTypes = response;
					//($scope.trainTypes);
				});
				
				$scope.$watch('traindetails',function() {
					if ($scope.traindetails) 
						if ($scope.traindetails._links) 
							$scope.traindetails._ref.trainType = $scope.trainTypes.findWithHref($scope.traindetails._ref.trainType);
				}, true);
				  /**   
				   * The save Function is Used to save the New Train
				   */
				$scope.saveTrain = function(trainobj, saveType) {
					SpringDataRestApi.save(trainobj, 'trains').then(
					function(response) 
					{
						toaster.pop({type: 'success', title: 'New Train ' +saveType.capitalizeFirstLetter() +'d.', body: 'Train '+ saveType.capitalizeFirstLetter() +'d'+' Successfully!!!'});
						
						if (saveType == 'create') 
							$scope.$parent.trains.push(trainobj);
						$scope.reset();
					}, 
					function(response) 
					{
						(response);
						toaster.pop({type: 'error', title: 'Error', body: 'Unable To '+ saveType.capitalizeFirstLetter()+' Train. Please Try Again!!!'});
			
					});
				};
				
				
				/**   
				 * To get the From & to station based on Query    
				*/
				$scope.getFromStation = function(query, timeout) {return SpringDataRestAdapter.process($http.get('/api/stations/search/findByCodeContains?code='+ query),[], true);
				
				}
				/**   
				 * The toStationSelected  function is used to select the Fromstation from DB   
				*/

				$scope.fromStationSelected = function(selected) {
					if (selected) 
					{
						if (!$scope.traindetails._ref)
						{
							$scope.traindetails._ref = {};
						}
							$scope.traindetails._ref.fromStation = selected.originalObject;
					}
				}
				
				/**   
				 * The toStationSelected  function is used to select the tostation from DB   
				*/
				$scope.toStationSelected = function(selected) {
					if (selected) 
					{
						if (!$scope.traindetails._ref)
						{
							$scope.traindetails._ref = {};
						}
							$scope.traindetails._ref.toStation = selected.originalObject;
					}
				}
				
				/**   
				 * The reset function is used to Reset the train   
				*/
				$scope.reset = function() {
					$scope.traindetails = angular.copy($scope.blanktraindetails);
					$scope.clearInput();
					$scope.submitClass="hide-errors";
					
				};
				/**   
				* The clearInput function is used to clear all the textFields    
				*/

				$scope.clearInput = function(id) {
					if (id) {$scope.$broadcast('angucomplete-alt:clearInput', id);} 
					else {$scope.$broadcast('angucomplete-alt:clearInput');}
				}
				
				  $scope.$watch('traindetails', function(c) {
			    	  $location.hash('trainFormDiv');
				      $anchorScroll();
		          });
			}
		}
	});
