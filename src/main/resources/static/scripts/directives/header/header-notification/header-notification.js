'use strict';

/**
 * @ngdoc directive
 * @name izzyposWebApp.directive:adminPosHeader
 * @description
 * # adminPosHeader
 */
angular.module('crewLinkApp')
	.directive('headerNotification',function(){
		return {
	        templateUrl:'scripts/directives/header/header-notification/header-notification.html',
	        restrict: 'E',
	        replace: true,
	        controller:function($scope,$http,$window,UserService,$cookies,SpringDataRestApi){
             $scope.userName = "";
             $scope.roleName = "";
	        	
	        	 SpringDataRestApi.get('/api/custom/user/myDetails').then(function(user){
	        		 $scope.userName  = user.username;
	        		 $scope.roleName  = user.role.name;
	        	 });
	        	
	        
	        	$scope.logout = function(){
	        		UserService.selectedUserPlan = null
	        		$cookies.selectedUserPlan = null
	        		$http.get('/logout').then(function(response){
	        			$window.location.href="/login/index.html";
	        		});
	        	}
	        }
    	}
	});


