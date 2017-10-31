'use strict';
/**
 * Division controller is used to
 * 1. Fetch all the division from DB 
 * 2. select division for Update
 * 3. Remove the division 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 15, 2016
 */



angular.module('crewLinkApp')
  .controller('DivisionsCtrl', function($scope,$position,$http,$resource,SpringDataRestAdapter,SpringDataRestApi,toaster,UserService) {
	 
	  $scope.divisions = [];
	  $scope.hasAdmin=false;
	  /**
		* This is serverFetch table is Used List all the  Users from DB using SpringDataRestApi
		*  
	    */
	  $scope.serverFetch = new ServerTableFetch(
			  "/api/divisions/search/findByAllParams",  // Url call that will be made all the time
			  [], 	// These are the subLinks
			  SpringDataRestApi,			// This is our Call Processing Service currently only SringDataRestApi is supported and used here. 
			  function(){					// Before processing this is called
				  $scope.isLoading = true;
			  },
			  function(resultObj){			// After processing this is called
				  $scope.divisions = resultObj;
				  /**
				   *   Check for User whether its is Admin or Super User
				   * 
				   */
				  SpringDataRestApi.get(UserService.getSelectedUserPlan()._links.user.href).then(
						  function(userResponse){  
							  SpringDataRestApi.get(userResponse._links.role.href).then(
									  function(roleRespons){
										  if(roleRespons.name=="ADMIN") // Check for Admin from DB
											  $scope.hasAdmin=true;    // set to True if Admin Exists
										  else
											  $scope.hasAdmin=false;  // set to False if user is not an Admin
											  
										  
									  }  
							  );
										
						  }
				  );
				  $scope.isLoading = false;
			  }
	  );
      
	  /**
       *   selectedDivision Function is Used to select the Existing division for Updated in DB
       */
      $scope.selectedDivision = null;
      $scope.selectDivision = function(division){
    	  $scope.selectedDivision = division;
      };
      /**
       *   RemoveDivision Function is Used to Removed the Division from DB
       */
      $scope.removeDivision = function(divisionobj) {
			SpringDataRestApi.remove(divisionobj).then(
				function(response){
					$scope.divisions.splice($scope.divisions.indexOf(divisionobj),1); // Splice the division Array and remove object from Array
					$scope.selectedDivision = null;  // Set selectedDivision to Null
					toaster.pop({type: 'success', title: 'Division Removed', body: 'Division Removed Successfully!!!'}); // PopUp the toaster Once division has removed 
				}, 
				function(response){
					toaster.pop({type: 'error', title: 'Error', body: 'Unable To Remove Division. Please Try Again!!!'}); // On Error Response PopUp the toaster if division Not removed from DB
				});
			};
      
      
	});
