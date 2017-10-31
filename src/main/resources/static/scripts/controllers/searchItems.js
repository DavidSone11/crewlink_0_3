'use strict';

/**
 * This allows user to search for Any Relevant Item and check to which things it
 * links to.
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 16, 2016
 */

angular.module('crewLinkApp').controller(
		'SearchItemsCtrl',
		function($scope, $position, $http, $confirm, $resource,
				SpringDataRestAdapter, SpringDataRestApi, toaster, UserService,
				$timeout) {
			$scope.Days = Days;
			$scope.searchItems = {"trainNo":"Train No",
								  "train" : "Train No and Start Day",
								  "fromStationDS" : "From Station Driving Section",
								  "toStationDS" : "To Station Driving Section",
								  "ddName" : "Driving Duty Name",
								  "rtName" : "Round Trip Name",
								  "clName" : "Crew Link Name"};
			
			
			$scope.callDataFetch = function(searchModel){
				if(!$scope.searchModel){
					return;
				}
				if($scope.searchModel.item == 'train'){
					$scope.searchModel.value3 = $scope.searchModel.value + ":"+$scope.searchModel.value2;
				}else{
					$scope.searchModel.value3 = $scope.searchModel.value;
				}
				
				$http.get('/api/custom/dashboards/listDependencies?searchItem='+$scope.searchModel.item+'&searchValue='+$scope.searchModel.value3
						+'&userPlan='+UserService.getSelectedUserPlan().id).then(function(res){
					$scope.searchResult = res.data;
					
				}.bind(this),function(res){
					toaster.pop({type: 'error', title: 'Error', body: 'Unable To Load Data. Please Try Again!!!'});
				});
			}
			

		});
