'use strict';
/**
 * @ngdoc function
 * @name crewLinkApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the crewLinkApp
 */
angular.module('crewLinkApp')
  .controller('MainCtrl', function($scope,$location,SpringDataRestApi,UserService,$http) {
	  SpringDataRestApi.init();
	  $scope.UserService = UserService;
	  if(!UserService.getSelectedUserPlan()){
		  $location.path('/dashboard/userPlans');
	  }
	
	  $scope.pendingTrains = "";
	  $scope.pendingSections = "";
	  $scope.pendingDrivingDuty = "";
	  $scope.pendingRoundTrip="";
	  $scope.pendingCrewLink = "";
	  $scope.goToUserPlans = function(){
		  $location.path('/dashboard/userPlans');
	  }
		 
	  $scope.station ="";
	  $scope.dashboardItems= "";
	  
	  $scope.selectedStation = "";
	  
	  $scope.fetchData = function(){
		  var url = "/api/custom/dashboards/listdashboard?userPlan="
				+ UserService.getSelectedUserPlan().id+"&station="+$scope.station;
		  $http({
			  method:'GET',
			  url: url
			  }).then(function(successresponse) {
				  $scope.dashboardItems = successresponse.data; 
				  $scope.selectedStation = $scope.station;
			  }, function (errorresponse) {
				  
			  });
	  } 
	  $scope.fetchData();
	  
	  

	  
	  
	  
	  /*
	    SpringDataRestApi.get('/api/trains/search/unusedTrains?userPlanId='+UserService.getSelectedUserPlan().id).then(
			  function(response){
				  $scope.pendingTrains = response;
			  }
		  );
		  SpringDataRestApi.get('/api/drivingSections/search/unusedSections?userPlanId='+UserService.getSelectedUserPlan().id).then(
				  function(response){
					  $scope.pendingSections = response;
				  }
			  );
		  SpringDataRestApi.get('/api/drivingDuties/search/unusedDrivingDuty?userPlanId='+UserService.getSelectedUserPlan().id).then(
				  function(response){
					  $scope.pendingDrivingDuty = response;
				  }
			  );
		  
		  
		  SpringDataRestApi.get('/api/roundTrips/search/unusedRoundTrip?userPlanId='+UserService.getSelectedUserPlan().id).then(
				  function(response){
					  $scope.pendingRoundTrip = response;
				  }
			  );
		  
	  
		  SpringDataRestApi.get('/api/crewLinks/search/unusedCrewLink?userPlanId='+UserService.getSelectedUserPlan().id).then(
				  function(response){
					  $scope.pendingCrewLink  = response;
				  }
			  );
			  
			  */
		  
		  
  });
