'use strict';
/**
 * @ngdoc function
 * @name crewLinkApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the crewLinkApp
 */
angular.module('crewLinkApp')
  .controller('FormCtrl', function($scope) {
	  
	  $scope.companydetailsList  = [];
	  
	  $http({
          method:'GET',
          url:'api/v1/company/getAllCompany?companyName',
      }).then(function successCallback(response) {
    	  
    	  $scope.companydetailsList  = response.data.content;
          
      }, function errorCallback(response) {
          
          $window.location.href='/';
      });
    
});