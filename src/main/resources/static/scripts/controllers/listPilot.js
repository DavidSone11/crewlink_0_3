'use strict';

/**
 * This RoundTrip controller is used to
 * 1. List all the Round Trips from DB
 * 2. List driving duties of particular Round Trip from DB
 * 3. Delete the Round Trip from DB   
 * @author 
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 16, 2016
 */

angular.module('crewLinkApp').controller(
		'ListPilotCtrl',
		function($scope,$position,$http,$resource,UserService,SpringDataRestAdapter,SpringDataRestApi,toaster,$q) {
			  $scope.Days=Days;
			  $scope.pilotTrips = [];
			  
			  $scope.updatePilotTripsUrl = function() {
					var uri =  "/api/custom/pilotTrips/list?userPlan="+UserService.getSelectedUserPlan().id;
				  return uri;
			  };

			  /*
			   * Server call to fetch all Pilots list
			   */
			  $scope.serverFetchPilotTrips = new ServerTableFetch2(
					  $scope.updatePilotTripsUrl.bind(this), // Url call
					  SpringDataRestApi,			// This is our Call Processing Service currently only SringDataRestApi is supported and used here. 
					  function(){					// Before processing this is called
						  $scope.isLoadingPilotTrips = true;
					  },
					  function(resultObj){			// After processing this is called
						  $scope.pilotTrips = resultObj;
							$scope.isLoadingPilotTrips = false;
					  },
					  function(response){
						  $scope.isLoadingPilotTrips = false;
					  },
					  true //(isWait) To make the results load after all fetching is done
			  );
		      
			/**
			 * This function is used to remove Pilots
			 */
		    $scope.removePilot = function(pilotId)
		    {    
		    	  SpringDataRestApi.deleteItem($scope.pilotTrips.selectionDetails.baseItemRestUri+pilotId).then(
		 	      function(response) 
		 				{	
		 	    	  		
		 	    	  		$scope.temp=[];
		 	    	  		var pilotIndex = 0;
		 	    	  		//Logic to remove deleted pilot trip from pilot trip list
		 	    	  		for(var i=0;i<$scope.pilotTrips.data.length;i++)
		 	    	  			{
		 	    	  					var count=0;
		 	    	  					if($scope.pilotTrips.data[i][0]==pilotId)
		 	    	  					{
		 	    	  						pilotIndex = i;
		 	    	  						break;
		 	    	  					}
		 	    	  			}
		 	    	  		$scope.pilotTrips.data.splice(pilotIndex,1);
		 	    	  		toaster.pop({type: 'success', title: 'Pilot Removed', body: 'Pilot Removed Successfully!!!'});
		 				}, 
		 				function(response) 
		 				{
		 					toaster.pop({type: 'error', title: 'Error', body: 'Unable To Remove Pilot. Please Try Again!!!'});
		 				});
		 	};
});
