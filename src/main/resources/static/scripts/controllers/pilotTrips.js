'use strict';
/**
 * 1. PilotTrips controller is used to
 * 2. Fetch all the crewTypes from DB 
 * 3. Remove the seleced crewType 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */

angular.module('crewLinkApp')
  .controller('PilotTripsCtrl', function($scope,$position,$http,$resource,SpringDataRestAdapter,SpringDataRestApi,toaster) {
	  
	  $scope.pilotTrips = [];
	  /**
		* This is serverFetch table is Used List all the  pilotTrip along with their (sublinks) as From,to Station,pilotType,division from DB using SpringDataRestApi
		*  
	    */
	  $scope.serverFetch = new ServerTableFetch(
			  "/api/pilotTrips/search/findByAllParams",  // Url call that will be made all the time
			  ['fromStation','toStation','pilotType', 'division'], 	// These are the subLinks
			  SpringDataRestApi,			// This is our Call Processing Service currently only SringDataRestApi is supported and used here. 
			  function(){					// Before processing this is called
				  $scope.isLoading = true;
			  },
			  function(resultObj){			// After processing this is called
				  $scope.pilotTrips = resultObj; // assign the resultObject in PilotsTrips Array
				  $scope.isLoading = false;
			  }
	  );
	  /**
	   *  The SelectedPilotTrip function is Used to Selected the Current pilotTrip for Update/Modified
	   */
	  $scope.selectedPilotTrip = null;
      $scope.selectPilotTrip = function(pilotTrip){
    	  $scope.selectedPilotTrip = pilotTrip;
    	
      };
      
      /**
       *  removePilotTrip Function is Used to remove selected pilotTrips  
       */
      $scope.removePilotTrip = function(pilotTripobj) {
    	  /*
    	   *  SpringDataRestApi is a process to remove the PilotTrips
    	   */
			SpringDataRestApi.remove(pilotTripobj).then(
				function(response){
					$scope.pilotTrips.splice($scope.pilotTrips.indexOf(pilotTripobj),1); //The splice function used to delete the seleced Users from an Array based on Index
					$scope.selectedPilotTrip = null;
					toaster.pop({type: 'success', title: 'Pilot Trip Removed', body: 'Pilot Trip Removed Successfully!!!'}); // On Success PopUp the Toaster once PilotTrips removed from DB
				}, 
				function(response){
					toaster.pop({type: 'error', title: 'Error', body: 'Unable To Remove Pilot Trip. Please Try Again!!!'}); // On Failure  PopUp the Toaster once PilotTrips Unable to DB
					
				});
			};
});
