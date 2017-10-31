'use strict';

/**
 * This allows user to search for Any Relevant Item and check to which things it
 * links to.
 * 
 * @author Vivek Yadav
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 16, 2016
 */

angular.module('crewLinkApp').controller(
		'ImproveRoundTripWithExcessOSRCtrl',
		function($scope, $position, $http, $confirm, $resource,
				SpringDataRestAdapter, SpringDataRestApi, toaster, UserService,
				$timeout,CSV) {
			$scope.Days = Days;
			$scope.maxOSR = "16";
			$scope.fieldsArray = [];
			$scope.generateArray = function(obj){
				var result = [];
				for(var i =0; i<Object.keys(obj).length ;i++){
					for(var item in obj){
						if(obj[item] == i){
							var itemObj = {key:item,val:i};
							result.push(itemObj);
							break;
						}
					}
				}
				return result;
			}
			$scope.callDataFetch = function(duration){
				
				$http.get('/api/custom/improvementSuggestions/roundTripWithExcessOSR?maxOSR='+duration
						+'&userPlan='+UserService.getSelectedUserPlan().id).then(function(res){
					$scope.searchResult = res.data;
				}.bind(this),function(res){
					toaster.pop({type: 'error', title: 'Error', body: 'Unable To Load Data. Please Try Again!!!'});
				});
			}
			
			$scope.getCSVHeaders = function(resultObj){
				var arr = $scope.generateArray(resultObj.fields);
				var result = [];
				for(var i=0; i<arr.length; i++){
					result.push(arr[i].key);
				}
				return result;
			};
			
			$scope.getCSVDataRows = function(resultObj){
				var cols = $scope.generateArray(resultObj.fields);
				var rowLen=resultObj.data.length, colLen=cols.length;
				var resultArr = [];
				for(var i =0; i<rowLen;i++){
					var rowData = [];
					for(var j=0;j<colLen; j++){
						if(resultObj.data[i][j]!=null){
							rowData.push( resultObj.data[i][j] +"");
						}
						else{
							rowData.push("");
						}
					}
					resultArr.push(rowData);
				}
				return resultArr;
			};
			

		});
