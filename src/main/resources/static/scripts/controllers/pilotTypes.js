'use strict';
/**
 * 1. PilotTypes controller is used to
 * 2. Fetch all the PilotTypes from DB 
 * 3. Remove the seleced PilotType 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 15, 2016
 */


angular.module('crewLinkApp')
  .controller('PilotTypesCtrl', function($scope,$position,$http,$resource,SpringDataRestAdapter,SpringDataRestApi,toaster) {
	 
	  $scope.pilotTypes = [];
	  /**
		* This is serverFetch table is Used List all the  pilotTypes from DB using SpringDataRestApi
		*  
	    */
	  $scope.serverFetch = new ServerTableFetch(
			  "/api/pilotTypes/search/findByAllParams",  // Url call that will be made all the time
			  [], 	// These are the subLinks
			  SpringDataRestApi,			// This is our Call Processing Service currently only SringDataRestApi is supported and used here. 
			  function(){					// Before processing this is called
				  $scope.isLoading = true;
			  },
			  function(resultObj){			// After processing this is called
				  $scope.pilotTypes = resultObj;
				  $scope.isLoading = false;
			  }
	  );
	  /**
	   *  The selectedPilotType function is Used to Selected the Current pilotType for Update/Modified
	   */
      $scope.selectedPilotType = null;
      $scope.selectPilotType = function(pilotType){
    	  $scope.selectedPilotType = pilotType;
      };
      
      /**
       *  removePilotType Function is Used to remove PilotType  
       */
      $scope.removePilotType = function(pilotTypeobj) {
			SpringDataRestApi.remove(pilotTypeobj).then(function(response){ 
					$scope.pilotTypes.splice($scope.pilotTypes.indexOf(pilotTypeobj),1); //The splice function used to delete the seleced pilotTypes from an Array based on Index
					$scope.selectedPilotType = null;
					toaster.pop({type: 'success', title: 'Pilot Type Removed', body: 'Pilot Type Removed Successfully!!!'}); // On Success PopUp the Toaster once PilotType removed from DB
				}, 
				function(response){
					toaster.pop({type: 'error', title: 'Error', body: 'Unable To Remove Pilot Type. Please Try Again!!!'}); // On Error PopUp the Toaster if the PilotType Unable to remove from DB
					
				});
			};
      
      
	});
