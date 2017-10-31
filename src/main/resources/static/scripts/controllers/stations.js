'use strict';
/**
 * The Following Controller is Used to 
 * 1. List all stations
 * 2. Check for User whether is Admin or Super
 * 3. Select the stations for Updated
 * 4. Remove the Station 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 14, 2016
 */

angular.module('crewLinkApp')
  .controller('StationsCtrl', function($scope,$position,$http,$resource,SpringDataRestAdapter,SpringDataRestApi,toaster,UserService) {
	  
	  $scope.stations = [];
	  $scope.hasAdmin=false;
	  $scope.serverFetch = new ServerTableFetch(
			  "/api/stations/search/findByAllParams",  // Url call that will be made all the time
			  [], 	// These are the subLinks
			  SpringDataRestApi,			// This is our Call Processing Service currently only SringDataRestApi is supported and used here. 
			  function(beforeProcessingResponse){
				  // Before processing this is called
				  (beforeProcessingResponse);
				  $scope.isLoading = true;
			  },
			  function(resultObj){			// After processing this is called
				  $scope.stations = resultObj;
				  SpringDataRestApi.get(UserService.getSelectedUserPlan()._links.user.href).then(
						  function(userResponse)
						  {
							  SpringDataRestApi.get(userResponse._links.role.href).then(
									  function(roleRespons){
										  //(roleRespons.name);
										  if(roleRespons.name=="ADMIN")
											  $scope.hasAdmin=true;
										  else
											  $scope.hasAdmin=false;
											  
										  
									  }
							  );
										
						  }
				  );
				  $scope.isLoading = false;
			  },function(AfterProcessingErrorResponse){					// Call Back After Error
				 concole.log(""+AfterProcessingErrorResponse);
			  }
	  );
	  /**
	   *  The selectedStation Function is Used for  select the Station for Update  
	   */
	  $scope.selectedStation = null;
      $scope.selectStation = function(station){
    	  $scope.selectedStation = station;
    	
      };
      
      /**
	   *  The removeStation Function is Used for  removed the Station  
	   */
      $scope.removeStation = function(stationobj) {
			SpringDataRestApi.remove(stationobj).then(
				function(response){
					$scope.stations.splice($scope.stations.indexOf(stationobj),1);
					$scope.selectedStation = null;
					toaster.pop({type: 'success', title: 'Station Removed', body: 'Station Removed Successfully!!!'});
				}, 
				function(response){
					toaster.pop({type: 'error', title: 'Error', body: 'Unable To Remove Station. Please Try Again!!!'});
					
				});
			};
});
