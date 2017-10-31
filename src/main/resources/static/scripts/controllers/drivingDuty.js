'use strict';

/**
 * This Driving Duty controller is used to
 * 1. List all the Driving Duties from DB
 * 2. List driving sections of particular Driving Duty from DB
 * 3. Delete the Driving Duty from DB   
 * @author 
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 16, 2016
 */


angular.module('crewLinkApp')
  .controller('DrivingDutyCtrl', function($scope,$position,$http,$confirm,$resource,SpringDataRestAdapter,SpringDataRestApi,toaster,UserService,$timeout) {
	  
	  $scope.drivingDuties = [];
	  $scope.drivingDutiesList = [];
	  $scope.Days = Days;
	  $scope.selectedValue = 'false';
	  
	  /*
	   * Server call to fetch all driving duties list
	   */
	  $scope.serverFetch = new ServerTableFetch2(
			  "/api/custom/drivingDuties/list?userPlan="+UserService.getSelectedUserPlan().id,
			  SpringDataRestApi,			 
			  function(){//Before request processing					
				  $scope.isLoading = true;
			  },
			  function(resultObj){			// After request processing
				  $scope.drivingDutiesList = resultObj;
				  $scope.isLoading = false;
			  },
			  function(error){},
			  true
	  );
	  
	  /**
	   * This function is used to fetch driving sections of passed driving duty  
	   */
	  $scope.viewdrivingDutyElement = function(drivingDuty){
		   $scope.viewDrivingSectionDetails = [];
		   $http.get("api/custom/drivingDutyElements/list?userPlan="+UserService.getSelectedUserPlan().id+"&drivingduty="+drivingDuty).then(function(response) {
			   $scope.drivingDutyElements = response.data; 
		   });
	   }

	  /**
	   * This function is used to remove selected driving duty  
	   */
	  $scope.removeDrivingDuty = function(drivingduty)
	  {
		  
				$http.get('/api/custom/dashboards/listDependencies?searchItem=drivingDuty&searchValue='+drivingduty
						+'&userPlan='+UserService.getSelectedUserPlan().id).then(function(res){
					var duties = {};
					var roundTrips = {};
					var links = {};
					for(var i=0; i<res.data.data.length; i++){
						if(res.data.data[i][res.data.fields.ddName]!= null){
							duties[res.data.data[i][res.data.fields.ddName]] = res.data.data[i][res.data.fields.ddName];
						}
						if(res.data.data[i][res.data.fields.rtName] != null){
							roundTrips[res.data.data[i][res.data.fields.rtName]] = res.data.data[i][res.data.fields.rtName];
						}
						if(res.data.data[i][res.data.fields.clName] != null){
							links[res.data.data[i][res.data.fields.clName]] = res.data.data[i][res.data.fields.clName];
						}
						
					}
					var strDuties = "";
					var strRoundTrips = "";
					var strLinks = "";
					for(var item in duties){
						strDuties += " | "+item;
					}
					for(var item in roundTrips){
						strRoundTrips += " | "+item;
					}
					for(var item in links){
						strLinks += " | "+item;
					}
					$confirm({ // Confirm PopUp to Remove fields from
						// DB
						text : "Are you sure you want to delete these items too ? :   Round Trips : ["+strRoundTrips+"]   Links: ["+strLinks+"]",
						headerClass : 'confirm-header-danger',
						okButtonClass : "btn-danger"
					}).then(function() {
						SpringDataRestApi
						.deleteItem(
								$scope.drivingDutiesList.selectionDetails.baseItemRestUri
										+ drivingduty)
						.then(
								function(response) {
									$scope.temp=[];
				 	    	  		var drivingDutyIndex = 0;
				 	    	  		//Logic to remove deleted driving duty from list
									for (var i = 0; i < $scope.drivingDutiesList.data.length; i++) {
										var count = 0;
										if ($scope.drivingDutiesList.data[i][0] == drivingduty) {
											drivingDutyIndex = i;
											break;
										}
									}
				 	    	  		$scope.drivingDutiesList.data.splice(drivingDutyIndex,1);
				 					toaster.pop({type: 'success', title: 'DrivingDuty Removed', body: 'DrivingDuty Removed Successfully!!!'});
								}.bind(this),
								function(response) {
									toaster.pop({type: 'error', title: 'Error', body: 'Unable To Remove DrivingDuty. Please Try Again!!!'});

								}.bind(this));
						
					}.bind(this));
				}.bind(this),function(res){
					
				});
				
		  
	 	};
	 	
	 	
	 	$scope.updateNotIgnore = function(){
	 		 angular.forEach($scope.drivingDutiesList.data, function(drivingduty){
	 			  if(drivingduty[$scope.drivingDutiesList.fields.isIgnore]==true){
	 				 drivingduty[$scope.drivingDutiesList.fields.isIgnore]=false;
	 				 $scope.updateDrivingDutyIsIgnore(drivingduty);
	 			  }
	 			});
	 		
	 	}
	 	
	 	  $scope.updateIgnoreAll = function(){
	 		 angular.forEach($scope.drivingDutiesList.data, function(drivingduty){
	 			  if(drivingduty[$scope.drivingDutiesList.fields.isIgnore]==false){
	 				 drivingduty[$scope.drivingDutiesList.fields.isIgnore]=true;
	 				 $scope.updateDrivingDutyIsIgnore(drivingduty);
	 			  }
	 			});
		     }
		  
	 	
	 	
	 	$scope.updateDrivingDutyIsIgnore= function(drivingduty) {
	 		var uri = "/api/custom/drivingDuties/updateDrivingDutyIsIgnore?userPlan="
					+ UserService.getSelectedUserPlan().id
					+ "&drivingduty="+drivingduty[$scope.drivingDutiesList.fields.id]
					+ "&isIgnore="+ drivingduty[$scope.drivingDutiesList.fields.isIgnore];
			$http
					.get(uri)
					.then(
							function(response) {
								if (response.data.result) {
									toaster
											.pop({
												type : 'success',
												title : 'DrivingDuty update to user list',
												body :  'DrivingDuty update to user list is successfully !!!'
											});
								} else {
									toaster
											.pop({
												type : 'error',
												title : 'Error',
												body : 'Unable to update DrivingDuty to user list. Please Try Again!!! '
														+ response.data.errorMessage
											});
								}
							},
							function(response) {
								toaster
										.pop({
											type : 'error',
											title : 'Error',
											body : 'Unable to update DrivingDuty to user list. Please Try Again!!! '
										});

							});
	 					};
	 	
	 	
	 	
	 	
	 	
	 	
	 	
     
});
