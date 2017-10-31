'use strict';
/**
 * This DrivingSection controller is used to
 * 1. Retrive all the DrivingSection based on the Plan 
 * 2. Remove Driving sections    
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 11, 2016
 */

angular.module('crewLinkApp')
  .controller('DrivingSectionCtrl', function($scope,$q,$state,$http,$resource,SpringDataRestAdapter,SpringDataRestApi,UserService,toaster) {
	  $scope.Days = Days;
	//  $scope.drivingSections = [];
	 
	  $scope.selectedValue = 'false';
	  $scope.drivingSections = {
				data : [],
				fields : {}
			};
	  
	  $scope.serverFetch = new ServerTableFetch2(
			  "/api/custom/drivingSections/list?userPlan="+UserService.getSelectedUserPlan().id,  // Url call that will be made all the time
			  SpringDataRestApi,			// This is our Call Processing Service currently only SringDataRestApi is supported and used here. 
			  function(){					// Before processing this is called
				  $scope.isLoading = true;  // Used for Spinner on the Table which is Set to True while on First call
			  },
			  function(resultObj){			// After processing this is called
				  $scope.drivingSections = resultObj; // The Success Result stored on DrivingSection array
				  $scope.isLoading = false;
			  },
			  function(response){
				  $scope.isLoading = false; // Used for Spinner on the Table which is Set to false When any Error Occured
			  },
			  true //(isWait) To make the results load after all fetching is done
	  );
	 
	
      
		/**
		 *   removeDrivingSection function is Used to delete Driving Sections 
		 */
	  $scope.removeDrivingSection = function(link) {
		  	SpringDataRestApi.deleteItem(link).then(function(response){ // The SpringDataRestApi is a services which call the Function deleteItem and take link as a parameter
					$scope.drivingSections.data.splice($scope.drivingSections.indexOf(drivingSection),1);  // Splice() Method adds/removes items to/from an array
					toaster.pop({type: 'success', title: 'Driving Section Removed', body: 'Train Removed Successfully!!!'}); // Toaster to show the PopUp message after remove 
				}, 
				function(response){ 
					toaster.pop({type: 'error', title: 'Error', body: 'Unable To Remove Train. Please Try Again!!!'}); // On Failure Toaster to show the PopUp messages
					
			});
	  };
	  
	  
	  $scope.updateNotIgnore = function(){
		  angular.forEach($scope.drivingSections.data, function(drivingSection){
		                 if(drivingSection[$scope.drivingSections.fields.isIgnore]==true){
		                	 drivingSection[$scope.drivingSections.fields.isIgnore] = false;
		                	 $scope.updateDrivingSectionIsIgnore(drivingSection);
			               }
		  		});
	  };
	  
	  $scope.updateIgnoreAll = function(){
		  angular.forEach($scope.drivingSections.data, function(drivingSection){
              if(drivingSection[$scope.drivingSections.fields.isIgnore]==false){
             	 drivingSection[$scope.drivingSections.fields.isIgnore] = true;
             	 $scope.updateDrivingSectionIsIgnore(drivingSection);
	                }
		  	});
	  }
	  

	  
		$scope.updateDrivingSectionIsIgnore= function(drivingSection) {
			
			
			
			var uri = "/api/custom/drivingSections/updateDrivingSectionIsIgnore?userPlan="
					+ UserService.getSelectedUserPlan().id
					+ "&drivingSectionId="+drivingSection[$scope.drivingSections.fields.id]
					+ "&isIgnore="+ drivingSection[$scope.drivingSections.fields.isIgnore];
			$http
					.get(uri)
					.then(
							function(response) {
								if (response.data.result) {
									toaster
											.pop({
												type : 'success',
												title : 'DrivingSection update to user list',
												body : 'DrivingSection update to user list is successfully !!!'
											});
								} else {
									toaster
											.pop({
												type : 'error',
												title : 'Error',
												body : 'Unable to update DrivingSection to user list. Please Try Again!!! '
														+ response.data.errorMessage
											});
								}
							},
							function(response) {
								toaster
										.pop({
											type : 'error',
											title : 'Error',
											body : 'Unable to update DrivingSection to user list. Please Try Again!!! '
										});

							});
						};

	 	  
	  
});
