'use strict';

angular.module('crewLinkApp')
  .controller('TrainTypesCtrl', function($scope,$position,$http,$resource,SpringDataRestAdapter,SpringDataRestApi,toaster,UserService) {
	 
	  $scope.trainTypes = [];
	  $scope.hasAdmin=false;
	  $scope.serverFetch = new ServerTableFetch(
			  "/api/trainTypes/search/findByNameContains",  // Url call that will be made all the time
			  [], 	// These are the subLinks
			  SpringDataRestApi,			// This is our Call Processing Service currently only SringDataRestApi is supported and used here. 
			  function(){					// Before processing this is called
				  $scope.isLoading = true;
			  },
			  function(resultObj){			// After processing this is called
				  $scope.trainTypes = resultObj;
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
			  }
	  );
      
      $scope.selectedTrainType = null;
      $scope.selectTrainType = function(trainType){
    	  $scope.selectedTrainType = trainType;
      };
      
      $scope.removeTrainType = function(trainTypeobj) {
			SpringDataRestApi.remove(trainTypeobj).then(
				function(response) 
				{
					$scope.trainTypes.splice($scope.trainTypes.indexOf(trainTypeobj),1);
					$scope.selectedTrainType = null;
					toaster.pop({type: 'success', title: 'Train Type Removed', body: 'Train Type Removed Successfully!!!'});
				}, 
				function(response) 
				{
					toaster.pop({type: 'error', title: 'Error', body: 'Unable To Remove Train Type. Please Try Again!!!'});
					
				});
			};
      
      
	});
