'use strict';
/**
 * CrewType controller is used to
 * 1. Fetch all the crewTypes from DB 
 * 2. Remove the seleced crewType 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */

angular.module('crewLinkApp')
  .controller('CrewTypesCtrl', function($scope,$position,$http,$resource,SpringDataRestAdapter,SpringDataRestApi,toaster,UserService) {
	 
	  $scope.crewTypes = [];
	  $scope.hasAdmin=false;
	  /**
		* This is serverFetch table is Used List all the  Users from DB using SpringDataRestApi
		*  
	    */
	  $scope.serverFetch = new ServerTableFetch(
			  "/api/crewTypes/search/findByNameContains",  // Url call that will be made all the time
			  [], 	// These are the subLinks
			  SpringDataRestApi,			// This is our Call Processing Service currently only SringDataRestApi is supported and used here. 
			  function(){					// Before processing this is called
				  $scope.isLoading = true;
			  },
			  function(resultObj){			// After processing this is called
				  $scope.crewTypes = resultObj;
				  if(UserService.getSelectedUserPlan().name!=null){
				  SpringDataRestApi.get(UserService.getSelectedUserPlan()._links.user.href).then( // Fetch the Current UserPlan
						  function(userResponse){
							  SpringDataRestApi.get(userResponse._links.role.href).then( // Fetch the Role of the User for Authentication 
									  function(roleRespons){
										  if(roleRespons.name=="ADMIN")
											  $scope.hasAdmin=true;
										  else
											  $scope.hasAdmin=false;
										  
									  }
							  );
										
						  }
				  );}
				  else{toaster.pop({type: 'error', title: 'Error', body: 'Please select user plan!!!'}); 
					  }
				  $scope.isLoading = false;
			  }
	  );
      
	  /**
       *  This Function is Used to select the crewType for Update/Modifiy  
       */
      $scope.selectedCrewType = null;
      $scope.selectCrewType = function(crewType){
    	  $scope.selectedCrewType = crewType; 
      };
      
      
      /**
       *  removeCrewType Function is Used to remove selected CrewType  
       */
       $scope.removeCrewType = function(crewTypeobj) {
			SpringDataRestApi.remove(crewTypeobj).then(
				function(response){
					$scope.crewTypes.splice($scope.crewTypes.indexOf(crewTypeobj),1); //The splice function used to delete the seleced Users from an Array based on Index  
					$scope.selectedCrewType = null;  //Set the Current Type to Null 
					toaster.pop({type: 'success', title: 'Crew Type Removed', body: 'Crew Type Removed Successfully!!!'}); // On Success PopUp the Toaster once crewType removed from DB
				}, 
				function(response){
					toaster.pop({type: 'error', title: 'Error', body: 'Unable To Remove Crew Type. Please Try Again!!!'}); // On Failure  PopUp the Toaster once crewType Unable to DB
				});
			};
      
      
	});
