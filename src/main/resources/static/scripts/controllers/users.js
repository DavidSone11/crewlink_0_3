'use strict';

/**
 * This User controller is used to
 * 1. List all the Users from DB
 * 2. Select the Users for Update in DB
 * 3. Delete the User from DB   
 * @author Vivek Yadav,Santosh sahu
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */

angular.module('crewLinkApp')
  .controller('UsersCtrl', function($scope,$position,$http,$resource,SpringDataRestAdapter,SpringDataRestApi,toaster,UserService) {
	    /**
		 * This is serverFetch table is Used List all the  Users from DB using SpringDataRestApi
		 */
	  $scope.users = [];
	  $scope.hasAdmin=false;
	  $scope.refreshUser = false;
	  $scope.serverFetch = new ServerTableFetch(
			  "/api/users/search/findByAllParams",  // Url call that will be made all the time
			  ['role'], 	// These are the subLinks
			  SpringDataRestApi,			// This is our Call Processing Service currently only SringDataRestApi is supported and used here. 
			  function(){					// Before processing this is called
				  $scope.isLoading = true;
			  },
			  function(resultObj){			// After processing this is called
				 /* An Empty Array is Used to store all the Users from Database */
				  $scope.users = resultObj;
				 
				  /*  Here we are fetching all the users based on their role to provide Authentication From FrontEnd 
				  *  If the Role is Admin then he is granted the Premission Update/Modify or Delete  
				  * */ 
				  SpringDataRestApi.get(UserService.getSelectedUserPlan()._links.user.href).then(
						  function(userResponse){
							 SpringDataRestApi.get(userResponse._links.role.href).then(
									  function(roleRespons){
										  if(roleRespons.name=="ADMIN")
											  $scope.hasAdmin=true;
										  else
											  $scope.hasAdmin=false;
									   }
							  );
										
						  }
				  );
				  $scope.isLoading = false;
		   }
	  );
      
	 
	  /**   
	   * The selectUser Function is Used to Removed the selected User  
	   */ 
	  $scope.selectedUser = null;
      $scope.selectUser = function(user){
    	  $scope.selectedUser = user;
      };
      
      
      /**   
	   * The RemoveUser Function is Used to Removed the selected User  
	   */ 
      $scope.removeUser = function(userobj) {
			SpringDataRestApi.remove(userobj).then(
				function(response){ // After delete the response will be Success  
					toaster.pop({type: 'success', title: 'User Removed', body: 'User Removed Successfully!!!'});
					$scope.users.splice($scope.users.indexOf(userobj),1); // The splice function used to delete the seleced Users from an Array based on Index 
					$scope.selectedUser = null;
				}, 
				function(response){ // If Error Occurred on deleting the response will be failure
					toaster.pop({type: 'error', title: 'Error', body: 'Unable To Remove User. Please Try Again!!!'});
				});
			};
  		});
