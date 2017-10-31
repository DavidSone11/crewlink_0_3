'use strict';
/**
 * This User directive is used to
 * 1. Save the User 
 * 2. Retrive the headStation(headQuarter) to Subscribe Stations for a Users
 * @author Vivek Yadav,Santosh sahu
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 10, 2016
 */

angular.module('crewLinkApp')
		.directive('userForm',function() {
					return {
						templateUrl : 'scripts/directives/dashboard/userForm/userForm.html',
						restrict : 'E',
						replace : true,
						scope : {
							'userdetails' : '='
						},
						controller : function($scope, $location, $anchorScroll, $http, SpringDataRestApi, SpringDataRestAdapter, toaster) {
							$scope.blankuserdetails = {};
							$scope.selectedHeadStation =[];
							if (!$scope.userdetails)
								$scope.userdetails = {};
							$scope.submitClass='show-errors';
							SpringDataRestApi.list('roles').then(function(response) {  // Fetch all the Roles 
										$scope.roles = response;
									});

							$scope.$watch('userdetails',function() {
									if ($scope.userdetails) 
										if ($scope.userdetails._links) 
											$scope.userdetails._ref.role = $scope.roles.findWithHref($scope.userdetails._ref.role);
								}, true);

							 /**
							 * The SaveUser function is Used to Save the new User
							 */
							$scope.saveUser = function(userobj, saveType) 
							{
								var userStation={_ref:{}};
								userStation.userStationslink="";
								angular.forEach($scope.selectedHeadStation,function(responseStations){  // adding the station reponse links to 
									userStation.userStationslink+=responseStations._links.self.href+"\n";
								});
								SpringDataRestApi.save(userobj, 'users').then(function(response){  //SpringDataRestApi is used for Save the user Using POST CALL
										 var req = {
												  method: 'POST',
										  url: userobj._links.Stations.href,
										  headers: {
										    'Content-Type': 'text/uri-list'
												  },
												  data: userStation.userStationslink
												 };
										  $http(req)
										  	.then(function(response){ 
										  		/*   On Success response 
										  		 *   a.) Display a Success Messages 
										  		 *   b.) Refresh the Parent table (userTable) 
										  		 *   c.) Selected HeadStation set to NULL
										  		 *   d.) call to 'RestFunction' to Rest all the Fields
										  		 */
										  		toaster.pop({type: 'success', title: 'User Saved', body: 'User Saved successfully!!'});
										  		$scope.$parent.refreshUser = !$scope.$parent.refreshUser;
										  		$scope.selectedHeadStation = null;
										  		$scope.reset(); 
										  	},function(response){
										  		/*   On Error response 
										  		 *   a. PopUp the Toaster with Error Messages (display a Error Messages for User seleced Stations)  
										  		 *   
										  		 */
											toaster.pop({type: 'error', title: 'Error', body: 'Unable To Save User Station. Please Try Again!!!'});
									});
								
							},function(response){
								/*   On Error response 
						  		 *   a. PopUp the Toaster with Error Messages (display a Error Messages)  
						  		 *   
						  		 */
									(response);
									toaster.pop({type: 'error', title: 'Error', body: 'Unable To '+ saveType.capitalizeFirstLetter()+' User. Please Try Again!!!'});
							});
					};
							
					
					/**
					 * Fetch all headStation based on Search Criteria
					 */
							$scope.getHeadStation = function(query, timeout)
							{return SpringDataRestAdapter.process($http.get('/api/stations/search/findByCodeContains?code='+ query),[], true);
								
							}
							
							/**
							 * The headStationseleced Function is used for retrive the station to Subscribe to make CrewLinks for (Base Station) 
							 */
							$scope.headStationSelected = function(selected) {
								if (selected){
									if (!$scope.userdetails._ref) 
										$scope.userdetails._ref = {};
									
									/*
									 *  On station selected 
									 *  1. Comparing whether its reaches the maximum station Upto 10 or Not 
									 *  2. its already Exists or Not  
									 * 
									 */
									  if($scope.selectedHeadStation.length > 9){  
							    		  toaster.pop({type: 'error', title: 'Error', body: 'You can add maximum 10 Station. Please delete existing station to add new one'});
							    	  }else{
							    		      var IsExists  = false;
							    			  for(var i=0;i<$scope.selectedHeadStation.length;i++){
								    			     if($scope.selectedHeadStation[i].code==selected.originalObject.code){
								    			    	 IsExists = true;
								    			    	 break;
								    			     }
							    			      }
							    		   if(IsExists){
							    			   toaster.pop({type: 'success', title: 'Station already Exists', body: 'Station already Exists!!!'});
							    		   }else{
							    			   $scope.selectedHeadStation.push(selected.originalObject);
									    		(""+$scope.selectedHeadStation);
							    			   }
							    		}
							    	//$scope.userdetails._ref.station = selected.originalObject;
								}
							};
							
							/**
							 * The removeheadStation Function is used for Remove 
							 * headStation from selecedHeadStation Array 
							 */
							$scope.removeheadStation  = function(headStationObj){
								if(headStationObj!=null){
									$scope.selectedHeadStation.splice($scope.selectedHeadStation.indexOf(headStationObj),1);
									toaster.pop({type: 'success', title: 'headStaion Type Removed', body: 'HeadStaion Removed Successfully!!!'});	
								}
								
							}
							
						
							/**
							 * The Reset Function is used to Reset all fields 
							 *  
							 */
							$scope.reset = function() {
								$scope.userdetails = angular.copy($scope.blankuserdetails);
								$scope.submitClass="hide-errors";
							};
							
							/**
							 * The clearInput Function is used to Reset AutoCompleteFields
							 *  
							 */
							$scope.clearInput = function(id) {
								if (id) {$scope.$broadcast('angucomplete-alt:clearInput', id);} 
								else {$scope.$broadcast('angucomplete-alt:clearInput');}
							}
							/**
							 * The Watch Function is used to watch the Userdetails 
							 *  
							 */
							 $scope.$watch('userdetails', function(c) {
						    	  $location.hash('userFormDiv');
							      $anchorScroll();
					          });
						}
					}
				});
