'use strict';

angular.module('crewLinkApp')
  .controller('TrainTimeTableCtrl', function($scope,$state,$http,$resource,SpringDataRestAdapter,SpringDataRestApi) {
	  $scope.Days = Days;
	  $scope.trainNo = ($state.params.trainNo)?$state.params.trainNo:'';
	  $scope.startDay = ($state.params.startDay)?$state.params.startDay:'';
	  $scope.trainStations = [];
//	  $scope.serverFetch = new ServerTableFetch(
//			  "/api/trainStations/search/findByAllParams?trainNo="+$scope.trainNo+"&startDay="+$scope.startDay,  // Url call that will be made all the time
//			  ['station'], 	// These are the subLinks
//			  SpringDataRestApi,			// This is our Call Processing Service currently only SringDataRestApi is supported and used here. 
//			  function(){					// Before processing this is called
//				  $scope.isLoading = true;
//			  },
//			  function(resultObj){			// After processing this is called
//				  $scope.trainStations = resultObj;
//				  $scope.isLoading = false;
//			  }
//	  );
	  $scope.serverFetch = new ServerTableFetch2(
			  "/api/custom/trainStations/list?trainNo="+$scope.trainNo+"&startDay="+$scope.startDay,  // Url call that will be made all the time
			  SpringDataRestApi,			// This is our Call Processing Service currently only SringDataRestApi is supported and used here. 
			  function(){					// Before processing this is called
				  $scope.isLoading = true;
			  },
			  function(resultObj){			// After processing this is called
				  $scope.trainStationsResult = resultObj;
				  $scope.isLoading = false;
			  }
	  );
});
