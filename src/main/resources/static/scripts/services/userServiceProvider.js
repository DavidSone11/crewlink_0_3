(function() {
	/**
	 * @author Santosh
	 */

	'use strict';
	var userService = angular.module('crewLinkApp');
	
	userService
		.provider("UserService",
			function() {
				var provider = {};
				var config = {};
				config.sidebarTrigger = true;
				provider.$get = function($http,$q,$timeout,SpringDataRestApi,$cookies) {
					var service = {};
					service.config = {};
					service.config.sidebarTrigger = config.sidebarTrigger;
				
					service.selectedUserPlan = null;
					service.user = null;
					service.getUser = function() {
						var deferred = $q.defer();
						
						if(config.user == null){
							SpringDataRestApi.get('/api/custom/user/myDetails').then(function(user){
								SpringDataRestApi.get('/api/users/'+user.id,['role']).then(function(userModel){
									config.user = userModel;
									service.user = config.user;
									deferred.resolve(config.user);
								});
							},function(response){
								deferred.reject(response);
							});
						}else{
							deferred.resolve(config.user);
						}
						return deferred.promise;
					};
					
					service.getCurrentUser = function(){
						return service.user;
					};
					
					service.setSelectedUserPlan = function(userPlan){
						config.selectedUserPlan = (userPlan)?userPlan:selectedUserPlan;
						var id = config.selectedUserPlan._links.self.href.match(/.*\/(.*?)$/)[1];
						config.selectedUserPlan.id = id;
						$cookies.selectedUserPlan = JSON.stringify(config.selectedUserPlan);
						service.selectedUserPlan = config.selectedUserPlan;
					};
					service.clearSelectedUserPlan = function(){
						config.selectedUserPlan = {};
						$cookies.selectedUserPlan = null;
						delete $cookies['selectedUserPlan'];
					/*	$cookies.remove('selectedUserPlan');*/
						service.selectedUserPlan = config.selectedUserPlan;
					};
					service.getSelectedUserPlan = function(){
						return (service.selectedUserPlan)?service.selectedUserPlan:($cookies.selectedUserPlan)?JSON.parse($cookies.selectedUserPlan):{};
					};
					
					return service;
				}
				
				return provider;
			});
})();