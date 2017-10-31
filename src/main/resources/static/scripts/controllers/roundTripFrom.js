'use strict';

/**
 * This Round Trip Form controller is used to
 * 1. List all the Driving Duties from DB
 * 3. Create Round Trip in DB  
 * 4. View all driving duties and round trips in graph from DB
 * 5. View analysis table chart between 2 selected stations from DB
 * @authors Laxman
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 16, 2016
 */

angular.module('crewLinkApp')
  .controller('RoundTripFormCtrl', function($scope,$q,$state,$http,$resource,
		  SpringDataRestAdapter,SpringDataRestApi,UserService,toaster,$window,$timeout,$confirm,$compile) {
	  
	  $scope.isAnalysis = ($state.params.isAnalysis)?true:false;
      $scope.minutesToTimeFormat = minutesToTimeFormat;
      $scope.minutesToHours = minutesToHours;
	  $scope.Days=Days;
	  $scope.dutiesToCreateRoundTrips = [];
	  $scope.createRoundTripsChartList = [];
	  $scope.newRoundTripsFromChart = [];
	  $scope.deletedRoundTripsFromChart = [];
	  $scope.deletedRoundTripsIdFromChart = [];
	  $scope.drivingDuty = [];
	  $scope.drivingSections =[];
	  $scope.refreshDrivingDuty = true;
	  $scope.fromStationDD = "";
	  $scope.toStation = "";
	  $scope.roundTripsData = [];
	  $scope.crewTypes=[];
	  $scope.selectedCrewType=null;
	  var lengthOfDrivingDuty=0;
	  var isSort=false;
	  $scope.selectedDuties=[];
	  $scope.startDay = null;
	  $scope.startTime = null;
	  $scope.selectedCrewTypeForSaveMany = null;
	  
	  $scope.applyDualScroll = function(div1,div2){
		  document.getElementById(div1).onscroll= function() {
		      	document.getElementById(div2).scrollLeft= document.getElementById(div1).scrollLeft;
	      };
	      document.getElementById(div2).onscroll= function() {
				document.getElementById(div1).scrollLeft= document.getElementById(div2).scrollLeft;
	      };
	  }
	  $scope.applyDualScroll("matrixChartDiv","matrixChartDiv1");
	  $scope.applyDualScroll("createRoundTripChartDiv","createRoundTripChartDiv1");
	  
	  //new data
	  $scope.drivingDutiesList=[];
	  
	  $scope.updateDrivingDutyUrl = function(tableState){
		  	if(!tableState.search.predicateObject){
				tableState.search.predicateObject = {};
			}
			if (tableState.search.predicateObject) {
				if(tableState.search.predicateObject["fromStation"] != $scope.fromStationDD)
					tableState.search.predicateObject["fromStation"] = $scope.fromStationDD;
				if(tableState.search.predicateObject["toStation"] != $scope.toStation)
					tableState.search.predicateObject["toStation"] = $scope.toStation;
			}
		  var uri =  "/api/custom/drivingDuties/list?userPlan="+UserService.getSelectedUserPlan().id
		  			+ "&isRoundTrip=false&isIgnore=false";
		  if($scope.startDay!=null && $scope.startTime!=null){
			  uri += "&sort=CONCAT(departureDay,departureTime)<'"+$scope.startDay+$scope.startTime+"',CONCAT(departureDay,departureTime)";
			  //uri += "&sort=departureDay < '"+$scope.startDay+"', departureDay, departureTime < '"+$scope.startTime+"', departureTime";  
		  }
		  return uri;
	  };
	  
	  //This server call is used to fetch crew types list
	  SpringDataRestApi.get("/api/crewTypes").then(
				function(crewTypeResponse)
				{
						$scope.crewTypes=[];
						$scope.crewTypes=crewTypeResponse._embedded.crewTypes;
				}
		);
	  
	  // new implementation
	  $scope.serverFetch = new ServerTableFetch2(
			  $scope.updateDrivingDutyUrl.bind(this),  // Url call that will be made all the time
			    SpringDataRestApi,			 
			  function(){					
				  $scope.isLoadingDrivingDuties = true;
			  },
			  function(resultObj){			// After processing this is called
				  $scope.drivingDuties = resultObj;
				  $scope.isLoadingDrivingDuties = false;
				  
				  for(var j = 0; j<$scope.selectedDuties.length; j++){
					  for(var i = 0; i<$scope.drivingDuties.data.length; i++){
						  if($scope.selectedDuties[j][$scope.drivingDuties.fields.id]== $scope.drivingDuties.data[i][$scope.drivingDuties.fields.id])
							  $scope.drivingDuties.data.splice(i,1);
					  }
				  }
				  
			  },
			  function(error){
				  isLoadingDrivingDuties=false;
			  },
			  true
	  );
	  
	  
	  var departureObj={};
	  var arrivalObj={};
	  $scope.addDrivingDuty=function(drivingDutySelected)
	  {
		  //(drivingDutySelected);
		 
		  if($scope.selectedDuties.length>0)
			  {
				  arrivalObj.time=$scope.selectedDuties[$scope.selectedDuties.length-1][$scope.drivingDuties.fields.arrivalTime];
				  arrivalObj.day=$scope.selectedDuties[$scope.selectedDuties.length-1][$scope.drivingDuties.fields.arrivalDay];
				  departureObj.time=drivingDutySelected[$scope.drivingDuties.fields.departureTime];
				  departureObj.day=drivingDutySelected[$scope.drivingDuties.fields.departureDay];
				  drivingDutySelected.totalOutStationRestTime=diffTimeObj(arrivalObj,departureObj,"min");
			  }
		  $scope.selectedDuties.push(drivingDutySelected);
		  $scope.startDay = drivingDutySelected[$scope.drivingDuties.fields.arrivalDay];
		  $scope.startTime = drivingDutySelected[$scope.drivingDuties.fields.arrivalTime];
		  $scope.fromStationDD = drivingDutySelected[$scope.drivingDuties.fields.toStation];
		  $scope.toStation = "";
		  $scope.refreshDrivingDuty = ($scope.refreshDrivingDuty) ? false : true;
	  }
	 
	  $scope.saveRoundTrips=function(){
		  var orderN=[],numbers = [];
		  var successMessage = "";
		  var apiLink = "";
		  var count=0;
		  if($scope.selectedCrewType!=null)
			  $scope.roundTripSaveObject._ref.crewType=$scope.selectedCrewType;
		  else
			  {
			  	toaster.pop({type: 'error', title: 'Crew Type', body: 'Please Select Crew Type!!'});
			  	return;
			  }
		 
		for(var i=0;i<$scope.selectedDuties.length;i++)
		{
				numbers.push($scope.selectedDuties[i][0]);
				orderN.push(++count);
		}
		var drivingDutyIdsInString=$scope.getNumbersInString(numbers);
		var orderNos=$scope.getNumbersInString(orderN);
		var crewType=$scope.selectedCrewType;
		//($scope.selectedCrewType);
		var url = $scope.selectedCrewType._links.self.href; 
		
		url = url.toString(); 
		var crewTypeId = url.substring(url.lastIndexOf('/') + 1); 
		apiLink = "/api/custom/roundTrips/saveSingleRoundTrip?drivingDutyIds="
			+ drivingDutyIdsInString
			+ "&orderNos="
			+ orderNos
			+ "&crewType=" 
			+ crewTypeId
			+ "&userPlan=" 
			+ UserService.getSelectedUserPlan().id+"&isIgnore=false";
		successMessage = "Round Trips created Successfully.";
		
		$scope.savingInProcess = true;
		
		$http
		.get(apiLink)
		.then(
				function(response) {
					if (response.data.result) {
						toaster
								.pop({
									type : 'success',
									title : 'Round Trip Section Saved',
									body : successMessage
								});
						$scope.selectedDuties=[];
						 $scope.startDay = null;
						  $scope.startTime = null;
						  $scope.refreshDrivingDuty = ($scope.refreshDrivingDuty) ? false : true;
					} else {
						toaster
								.pop({
									type : 'error',
									title : 'Error',
									body : 'Unable Round Trip. Please Try Again!!! '
											+ response.data.errorMessage
								});
						$scope.selectedDuties=[];
						 $scope.startDay = null;
						  $scope.startTime = null;
						  $scope.refreshDrivingDuty = ($scope.refreshDrivingDuty) ? false : true;
					}
					$scope.savingInProcess = false;
				},function(response){
					toaster
					.pop({
						type : 'error',
						title : 'Error',
						body : 'Unable Round Trip. Please Try Again!!! '
					});
					$scope.selectedDuties=[];
					 $scope.startDay = null;
					  $scope.startTime = null;
					  $scope.refreshDrivingDuty = ($scope.refreshDrivingDuty) ? false : true;
					  $scope.savingInProcess = false;
				}
				);
		 

	  }
	  
	  $scope.getNumbersInString = function(numbers) {
			var numbersInStr = "";
			for (var i = 0; i < numbers.length; i++) {
				numbersInStr += ',' + numbers[i];
			}
			numbersInStr = numbersInStr.substring(1,
					numbersInStr.length);
			return numbersInStr;
		};

	  $scope.roundTripSaveObject={_ref:{},drivingDutyObj: [],drivingDutyLinks: ""};
	  var count=0;
	  
	  
	  $scope.removeLastDrivingDuty=function()
	  {
		  $scope.selectedDuties.splice($scope.selectedDuties.length-1,1);
		  
		  //("length : " ,$scope.selectedDuties.length);
		  if($scope.selectedDuties.length > 0)
		  {
			  $scope.startDay = $scope.selectedDuties[$scope.drivingDuties.fields.arrivalDay];
			  $scope.startTime = $scope.selectedDuties[$scope.drivingDuties.fields.arrivalTime];
			 // $scope.drivingDuties=[];
			  $scope.fromStation = $scope.selectedDuties[$scope.selectedDuties.length-1][$scope.drivingDuties.fields.toStation];
			  $scope.toStation = "";
			  $scope.refreshDrivingDuty = ($scope.refreshDrivingDuty)?false:true;
		  }
		  else
			  {
			  $scope.startDay = null;
			  $scope.startTime = null;
			//  $scope.drivingDuties=[];
			  $scope.fromStation = $scope.toStation = "";
			  $scope.refreshDrivingDuty = ($scope.refreshDrivingDuty)?false:true;	
			  }
		  
		  $scope.drivingDutyObjAll=[];
	  }

	  $scope.chartMetaData={};
	  
	  /**
	   * This function is used to generate table rows to display in table chart 
	   */
	  $scope.loadAllDutiesInMatrixChart = function(is6Day,isDaily,isAll){
		  $scope.is6Day = is6Day;
		  $scope.isDaily = isDaily;
		//Validation of two stations text fields
		  if(!$scope.chartMetaData.fromStation || !$scope.chartMetaData.toStation){
			  toaster.pop({type: 'error', title: 'Error', body: 'Enter valid station codes!!!'});
		  }else{
			  //To show loading spinner until data gets load
			  $scope.isLoadingMatrixChart = true;
			  $scope.isMatrixSearched = true;
			  $scope.isMatrixDutiesFound = true;
			  //Server call to fetch Driving duties between from and to stations
			  $http.get("/api/custom/drivingDuties/listWithRT?userPlan="+UserService.getSelectedUserPlan().id+
					  	"&fromStation="+$scope.chartMetaData.fromStation+"&toStation="+$scope.chartMetaData.toStation+
					  	"&sort=CONCAT(departureDay,departureTime)&isIgnore=false&page=0&size=1000").then(
						function(drivingDuties) 
						{
							drivingDuties = drivingDuties.data;
							if(!drivingDuties.data) $scope.isMatrixDutiesFound = false;
							else{
								$scope.sourceStationDuties = drivingDuties;
								//Logic to update meta data with Server fetched data 
								$scope.chartMetaData.fromStation = $scope.sourceStationDuties.data[0][$scope.sourceStationDuties.fields.fromStation];
								$scope.chartMetaData.toStation = $scope.sourceStationDuties.data[0][$scope.sourceStationDuties.fields.toStation];
								
								//Server call to fetch Driving duties between from and to stations
								$http.get("/api/custom/drivingDuties/listWithRT?userPlan="+UserService.getSelectedUserPlan().id+"&fromStation="+$scope.chartMetaData.toStation+
										  "&toStation="+$scope.chartMetaData.fromStation+"&sort=CONCAT(departureDay,departureTime)&isIgnore=false&page=0&size=1000").then(
											function(drivingDuties1)
											{
												drivingDuties1 = drivingDuties1.data
												if((!drivingDuties1.data)) 
													$scope.isMatrixDutiesFound = false;
												else{
													$scope.destinationStationDuties = drivingDuties1;
													$scope.ddFieldsForMatrixChart = $scope.destinationStationDuties.fields;
													if(is6Day || isDaily || isAll){
														$scope.sourceStationDuties.data = $scope.filterDuties(is6Day,isDaily,isAll,$scope.sourceStationDuties.data);
														$scope.destinationStationDuties.data = $scope.filterDuties(is6Day,isDaily,isAll,$scope.destinationStationDuties.data);
													}
													if(isAll){
														var dutiesForGraph = $scope.generateDutiesForGraph($scope.destinationStationDuties.data);
														$scope.newDutiesForGraph = $scope.sourceStationDuties.data.concat(dutiesForGraph);
														$scope.toggleFromStationRT = true;$scope.toggleFromRTDaily = true;$scope.toggleFromRT6Day = true;$scope.toggleFromRTWeekly = true;
														$scope.toggleToStationRT = true;$scope.toggleToRTDaily = true;$scope.toggleToRT6Day = true;$scope.toggleToRTWeekly = true;
														$scope.toggleOtherStationRT = true;$scope.toggleOtherRTDaily = true;$scope.toggleOtherRT6Day = true;$scope.toggleOtherRTWeekly = true;
														$scope.toggleNoRT = true;$scope.toggleNoRTDaily = true;$scope.toggleNoRT6Day = true;$scope.toggleNoRTWeekly = true;
														
														$scope.generateDutiesAndRoundTripsChartForTwoStations($scope.toggleFromRTDaily,$scope.toggleFromRT6Day,$scope.toggleFromRTWeekly,
																$scope.toggleToRTDaily,$scope.toggleToRT6Day,$scope.toggleToRTWeekly,
																$scope.toggleOtherRTDaily,$scope.toggleOtherRT6Day,$scope.toggleOtherRTWeekly,
																$scope.toggleNoRTDaily,$scope.toggleNoRT6Day,$scope.toggleNoRTWeekly);
													}else{
														$scope.sourceStationDuties = $scope.calculateAvailableTime($scope.sourceStationDuties.data);
														$scope.destinationStationDuties = $scope.calculateAvailableTime($scope.destinationStationDuties.data);
														$scope.loadWaitingTimes($scope.sourceStationDuties,$scope.destinationStationDuties,true,is6Day,isDaily);
													}
													$scope.isLoadingMatrixChart = false;
												}
											}
									);
								}
							if(!$scope.isMatrixDutiesFound){
								toaster.pop({type: 'error', title: 'No duties found', 
									body: 'No daily found between ('+
									$scope.chartMetaData.fromStation+'-'+$scope.chartMetaData.toStation+')/('+$scope.chartMetaData.toStation+'-'+$scope.chartMetaData.fromStation+')!!!'});
							}
						}
				);
		  }
	  }
	  
	  /**
	   * This function is used to filter Daily duties and six day duties
	   */
	  $scope.filterDuties = function(is6Day,isDaily,isAll,drivingDuties){
		  var filteredDuties = []
		  var count;
		  $scope.ddFieldsForMatrixChart.availableTime = drivingDuties[0].length;
		  $scope.ddFieldsForMatrixChart.availableDay = drivingDuties[0].length+1;
		  $scope.ddFieldsForMatrixChart.isForGraph = drivingDuties[0].length+2;
		  $scope.ddFieldsForMatrixChart.is6Day = drivingDuties[0].length+3;
		  $scope.ddFieldsForMatrixChart.isDaily = drivingDuties[0].length+4;
		  angular.forEach(drivingDuties,function(duty){
			  	duty.push("","","","","");
			  	duty[$scope.ddFieldsForMatrixChart.isForGraph] = false;
				count = 0;
				angular.forEach(drivingDuties,function(duty1){
					if(duty[$scope.ddFieldsForMatrixChart.ddName] == duty1[$scope.ddFieldsForMatrixChart.ddName]){
						count = count+1;
					}
				});	
				if(count == 7 || (is6Day && count == 6) || isAll){
					var isListed = false;
					if(!isAll)
						angular.forEach(filteredDuties,function(duty1){
							if(duty1[$scope.ddFieldsForMatrixChart.ddName] == duty[$scope.ddFieldsForMatrixChart.ddName]) 
								isListed = true;
						});
					if(!isListed || isAll) {
						if(count == 6){//If the count is 6 mark it as 6 day train
							duty[$scope.ddFieldsForMatrixChart.is6Day] = true;
							duty[$scope.ddFieldsForMatrixChart.isDaily] = false;
						}else if(count == 7){//If the count is 7 mark it as all day train
							duty[$scope.ddFieldsForMatrixChart.isDaily] = true;
							duty[$scope.ddFieldsForMatrixChart.is6Day] = false;
						}else {
							duty[$scope.ddFieldsForMatrixChart.is6Day] = false;
							duty[$scope.ddFieldsForMatrixChart.isDaily] = false;
						}
						filteredDuties.push(duty)
					};
				}
			});
		  return filteredDuties;
	  }
	  
	  /**
	   * This function is used to generate duties for graph
	   */
	  $scope.generateDutiesForGraph = function(duties){
		  var temp;
		  angular.forEach(duties,function(duty){
			  temp = duty[$scope.ddFieldsForMatrixChart.departureTime];
			  duty[$scope.ddFieldsForMatrixChart.departureTime] = duty[$scope.ddFieldsForMatrixChart.arrivalTime];
			  duty[$scope.ddFieldsForMatrixChart.arrivalTime] = temp;
			  
			  temp = duty[$scope.ddFieldsForMatrixChart.departureDay];
			  duty[$scope.ddFieldsForMatrixChart.departureDay] = duty[$scope.ddFieldsForMatrixChart.arrivalDay];
			  duty[$scope.ddFieldsForMatrixChart.arrivalDay] = temp;
			  
			  duty[$scope.ddFieldsForMatrixChart.isForGraph] = true;
		  })
		  return duties;
	  }
	  
	  /**
	   * This function is used to calculateWaitingTimes
	   */
	  $scope.calculateWaitingTimes = function(sourceStationDuties,destinationStationDuties,is6Day,isDaily){
		  $scope.waitingTimes = [[],[]];
		  var temp,temp1,waitingTime1,waitingTime2;
		  for(var d=0; d<sourceStationDuties.length; d++){
				temp = [];temp1 = []
				for(var d1=0; d1<destinationStationDuties.length; d1++){
					if(is6Day || isDaily){
						waitingTime1 = diffTwoTimesWithoutDay(minutesToTimeFormat(sourceStationDuties[d][$scope.ddFieldsForMatrixChart.availableTime]),
										destinationStationDuties[d1][$scope.ddFieldsForMatrixChart.departureTime]);
						waitingTime2 = diffTwoTimesWithoutDay(minutesToTimeFormat(destinationStationDuties[d1][$scope.ddFieldsForMatrixChart.availableTime]),
										sourceStationDuties[d][$scope.ddFieldsForMatrixChart.departureTime]);
					}else{
						waitingTime1 = diffBetweenTwoTimes(
								minutesToTimeFormat(sourceStationDuties[d][$scope.ddFieldsForMatrixChart.availableTime]),
								sourceStationDuties[d][$scope.ddFieldsForMatrixChart.availableDay],
								destinationStationDuties[d1][$scope.ddFieldsForMatrixChart.departureTime],
								destinationStationDuties[d1][$scope.ddFieldsForMatrixChart.departureDay]);
						waitingTime2 = diffBetweenTwoTimes(
								minutesToTimeFormat(destinationStationDuties[d1][$scope.ddFieldsForMatrixChart.availableTime]),
								destinationStationDuties[d1][$scope.ddFieldsForMatrixChart.availableDay],
								sourceStationDuties[d][$scope.ddFieldsForMatrixChart.departureTime],
								sourceStationDuties[d][$scope.ddFieldsForMatrixChart.departureDay]);
					}
					temp.push({clas:"",waitingTime:waitingTime1});
					temp1.push({clas:"",waitingTime:waitingTime2});
				}
				$scope.waitingTimes[0].push({clas:"",waitingTimes:temp});
				$scope.waitingTimes[1].push({clas:"",waitingTimes:temp1});
			}
	  }
	  
	  /**
	   * This function is used to calculate available time for driving duties
	   */
	  $scope.calculateAvailableTime = function(duties){
		  angular.forEach(duties,function(duty){
			  var totalMins = timeToMinuts(duty[$scope.ddFieldsForMatrixChart.arrivalTime]);
			  //Logic to calculate available time
			  if(duty[$scope.ddFieldsForMatrixChart.duration] <= 300){
				  totalMins += duty[$scope.ddFieldsForMatrixChart.duration]+60;
			  }else if(duty[$scope.ddFieldsForMatrixChart.duration] < 480){
				  totalMins += 360;
			  }else{
				  totalMins += 480;
			  }
			  duty[$scope.ddFieldsForMatrixChart.availableTime] = (((totalMins >= 1440)?totalMins-1440:totalMins));
			  duty[$scope.ddFieldsForMatrixChart.availableDay] = 
				  		(((totalMins >= 1440)?
						  	((duty[$scope.ddFieldsForMatrixChart.arrivalDay]+1 == 7)?0:
									  duty[$scope.ddFieldsForMatrixChart.arrivalDay]+1):
										  duty[$scope.ddFieldsForMatrixChart.arrivalDay]));
		  });
		  return duties;
	  }
	  
	  /**
	   * This function is used to load waiting times
	   */
	  $scope.loadWaitingTimes = function(sourceStationDuties,destinationStationDuties,isAutoSelect,is6Day,isDaily){
		  
		  $scope.sourceRest = 0;
		  $scope.destinationRest = 0;
		  $scope.dutiesToCreateRTsFromMatrixChart = [];
		  $scope.existingRTsMatrixChart = [];
		  $scope.existingRoundtrips = [];
		  $scope.waitingTimes = [[],[]];
		  $scope.calculateWaitingTimes(sourceStationDuties,destinationStationDuties,is6Day,isDaily);
		  if(isAutoSelect && !is6Day && !isDaily)
			  $scope.autoSelectExistingRoundTrips(sourceStationDuties,destinationStationDuties);
		  
		  $timeout(function(){
	        	var text="",scrollWidth = document.getElementById('matrixChartDiv').scrollWidth;
	        	for(var i=0;i<scrollWidth/7.5;i++){
	        		text += "a";
	        	}
	        	document.getElementById('matrixChartDiv1').innerHTML = text;
	        },500);
	  }
	  
	  /**
	   * This function is used to auto select existing round trips
	   */
	  $scope.autoSelectExistingRoundTrips = function(sourceStationDuties,destinationStationDuties){
		  var index;
		  for(var s=0; s<sourceStationDuties.length; s++){
			  if(sourceStationDuties[s][$scope.ddFieldsForMatrixChart.isRoundTrip] == 1){
				  for(var d=0; d<destinationStationDuties.length; d++){
					  if((sourceStationDuties[s][$scope.ddFieldsForMatrixChart.roundTrip] == 
								  destinationStationDuties[d][$scope.ddFieldsForMatrixChart.roundTrip])){
							  if(sourceStationDuties[s][$scope.ddFieldsForMatrixChart.roundTripBaseStation] == 
								  $scope.chartMetaData.fromStation)
								  	index = 0;
							  else
								  index = 1;
							  $scope.selectRTInMatrixChart(index,s,d,true);
					  }
				  }
			  }
		  }
		  $scope.existingRoundtrips = JSON.parse(JSON.stringify($scope.existingRTsMatrixChart));
	  }
	  
	  /**
	   * This function is used to auto select new round trips
	   */
	  $scope.findOptimizedRTsInMatrixChart = function(isAll){
		  $scope.isLoadingMatrixChart = true;
		  if(isAll)
			  $scope.loadWaitingTimes($scope.sourceStationDuties,$scope.destinationStationDuties,false,$scope.is6Day,$scope.isDaily);	
		  $scope.selectedWaitingTimes = $scope.getNotUsedWaitingTimes($scope.sourceStationDuties,$scope.destinationStationDuties,$scope.waitingTimes,isAll);
		  $scope.selectedTimesToOptimize = $scope.getMinimumWaitingTimes($scope.selectedWaitingTimes);
		  
		  $http.post("/api/custom/optimization/minimizeMatrix",JSON.stringify($scope.selectedTimesToOptimize))
		  .then(function(response){
			  if(response){
				  $scope.autoSelectNewRTsInMatrixChart(response.data);
				  $scope.isLoadingMatrixChart = false;
			  }else{
				  toaster.pop({type: 'error', title: 'Error Auto Select', body: 'Error in auto selecting round trips!!'});
				  $scope.isLoadingMatrixChart = false;
			  }
		  },function(response){
			  toaster.pop({type: 'error', title: 'Error Auto Select', body: 'Error in auto selecting round trips!!'});
			  $scope.isLoadingMatrixChart = false;
		  });
	  }
	  
	  /**
	   * This function is used to get not used times
	   */
	  $scope.getNotUsedWaitingTimes = function(sourceDuties,destinationDuties,waitingTimes,isAll){
		  var selectedWaitingTimesRows = [[],[]];
		  var selectedWaitingTimes = [[],[]];
		  $scope.selectedWaitingTimesRowIndexes = [];
		  $scope.selectedWaitingTimesColIndexes = [];
		  var firstTime = true;
		  for(var r=0; r<sourceDuties.length; r++){
			  if(sourceDuties[r][$scope.ddFieldsForMatrixChart.isRoundTrip] == 0 || isAll){
				  selectedWaitingTimesRows[0].push(waitingTimes[0][r].waitingTimes);
				  selectedWaitingTimesRows[1].push(waitingTimes[1][r].waitingTimes);
				  $scope.selectedWaitingTimesRowIndexes.push(r);
			  }
		  }
		  for(var r=0; r<selectedWaitingTimesRows[0].length; r++){
			  var temp = [],temp1 = [];
			  for(var c=0; c<selectedWaitingTimesRows[0][r].length; c++){
				  if(destinationDuties[c][$scope.ddFieldsForMatrixChart.isRoundTrip] == 0 || isAll){
					  temp.push(selectedWaitingTimesRows[0][r][c].waitingTime);
					  temp1.push(selectedWaitingTimesRows[1][r][c].waitingTime);
					  if(firstTime){
						  $scope.selectedWaitingTimesColIndexes.push(c);
					  }
				  }
			  }
			  firstTime = false;
			  selectedWaitingTimes[0].push(temp);
			  selectedWaitingTimes[1].push(temp1);
		  }
		  return selectedWaitingTimes;
	  }
	  
	  /**
	   * This function is used to select minimum waiting times
	   */
	  $scope.getMinimumWaitingTimes = function(waitingTimes,isAll){
		  $scope.selectedTimesIndexesToOptimize = [];
		  var times,indexes,selectedTimesToOptimize = []
		  for(var t=0; t<waitingTimes[0].length; t++){
			  times=[];indexes=[];
			  for(var d=0; d<waitingTimes[0][0].length; d++){
				  if(waitingTimes[0][t][d] <= waitingTimes[1][t][d]){
					  times.push(waitingTimes[0][t][d])
					  indexes.push(0);
				  }else{
					  times.push(waitingTimes[1][t][d])
					  indexes.push(1);
				  }
			  }
			  selectedTimesToOptimize.push(times);
			  $scope.selectedTimesIndexesToOptimize.push(indexes);
		  }
		  return selectedTimesToOptimize;
	  }
	  
	  /**
	   * This function is used to autoSelectNewRTsInMatrixChart
	   */
	  $scope.autoSelectNewRTsInMatrixChart = function(cols){
		  for(var c=0; c<cols.length; c++){
			  if(cols[c] > -1)
			  $scope.selectRTInMatrixChart($scope.selectedTimesIndexesToOptimize[c][cols[c]],
					  $scope.selectedWaitingTimesRowIndexes[c],$scope.selectedWaitingTimesColIndexes[cols[c]])
		  }
	  }
	  
	  /**
	   * Function to select/remove rt from matrix chart
	   */
	  $scope.selectRTInMatrixChart = function(base,row,col,isExistingRT){
		  var clas = ($scope.waitingTimes[base][row].clas)?"":"bg-yellow";
		  var duration,duties;
		  $scope.waitingTimes[base][row].clas=clas;
		  $scope.waitingTimes[((base == 0)?1:0)][row].clas=clas;
		  for(var t=0; t<$scope.waitingTimes[0].length; t++){
			  $scope.waitingTimes[0][t].waitingTimes[col].clas=clas;
			  $scope.waitingTimes[1][t].waitingTimes[col].clas=clas;
		  }
		  $scope.waitingTimes[base][row].waitingTimes[col].clas=(clas)?"bg-red":"";
		  if(clas){
			  duties = ((base == 0)?
					  [$scope.sourceStationDuties[row],$scope.destinationStationDuties[col]] :
						  [$scope.destinationStationDuties[col],$scope.sourceStationDuties[row]]);
			  if(isExistingRT)
				  $scope.existingRTsMatrixChart.push(duties);
			  else
				  $scope.dutiesToCreateRTsFromMatrixChart.push(duties);
			  duration = $scope.waitingTimes[base][row].waitingTimes[col].waitingTime;
		  }else{
			  duration = -$scope.waitingTimes[base][row].waitingTimes[col].waitingTime;
			  for(var d=0; d<$scope.dutiesToCreateRTsFromMatrixChart.length ||
			  		d<$scope.existingRTsMatrixChart.length; d++){
				  if(d<$scope.dutiesToCreateRTsFromMatrixChart.length)
					  if(($scope.dutiesToCreateRTsFromMatrixChart[d][0][$scope.ddFieldsForMatrixChart.id] == 
						  $scope.sourceStationDuties[row][$scope.ddFieldsForMatrixChart.id]) ||
						  ($scope.dutiesToCreateRTsFromMatrixChart[d][0][$scope.ddFieldsForMatrixChart.id] == 
							  $scope.destinationStationDuties[col][$scope.ddFieldsForMatrixChart.id])){
						  $scope.dutiesToCreateRTsFromMatrixChart.splice(d,1);break;
					  }
				  if(d<$scope.existingRTsMatrixChart.length)
					  if($scope.existingRTsMatrixChart[d][0][$scope.ddFieldsForMatrixChart.id] == 
						  $scope.sourceStationDuties[row][$scope.ddFieldsForMatrixChart.id] ||
						  $scope.existingRTsMatrixChart[d][0][$scope.ddFieldsForMatrixChart.id] == 
							  $scope.destinationStationDuties[col][$scope.ddFieldsForMatrixChart.id]){
						  $scope.existingRTsMatrixChart.splice(d,1);break;
					  }
			  }
		  }
		  if(base == 0){
			  $scope.sourceRest += duration;
		  }else{
			  $scope.destinationRest += duration;
		  }
	  }
	  
	  /**
	   * Function to create roundtrips from matrix chart
	   */
	  $scope.createRoundTripsFromMatrixChart = function(){
		  if($scope.dutiesToCreateRTsFromMatrixChart.length == 0){
			  toaster.pop({type: 'error', title: 'Error Round Trips', body: 'Select proper round trips!!'});
		  }else if($scope.matrixChartCrewType == "" || $scope.matrixChartCrewType == null || $scope.matrixChartCrewType == undefined){
			  toaster.pop({type: 'error', title: 'Error Crew Type', body: 'Select Crew Type!!'});
		  }else{
			  var url = ($scope.is6Day || $scope.isDaily)?"saveManyMatrix":"saveManyList";
			  var matrixChartRTsInFormat = $scope.arrangeMatrixChartRTsInFormat($scope.isDaily,$scope.is6Day);
			  $http.post("/api/custom/roundTrips/"+url+"?userPlan="+UserService.getSelectedUserPlan().id,
					  JSON.stringify(matrixChartRTsInFormat))
			  .then(function(response){
				  if(response.data.result){
					  toaster
						.pop({
							type : 'success',
							title : 'Saved successfully',
							body : 'Round Trips Saved successfully!!!'
						});
					  $scope.loadAllDutiesInMatrixChart($scope.is6Day,$scope.isDaily);
				  }else{
					  toaster.pop({type: 'error', title: 'Error Saving', body: 'Error in saving many round trips!!'});
				  }
			  },function(response){
				  toaster.pop({type: 'error', title: 'Error Saving', body: 'Error in saving many round trips!!'});
			  });
		  }
	  }
	  
	  /**
	   * This function is used to arrange selected duties in format to save
	   */
	  $scope.arrangeMatrixChartRTsInFormat = function(isDaily,is6Day){
		  var isContinue,roundTrips = [];
		  var crewType = $scope.matrixChartCrewType._links.self.href.substring(
				  $scope.matrixChartCrewType._links.self.href.lastIndexOf('/')+1,$scope.matrixChartCrewType._links.self.href.length);
		  for(var d=0; d<$scope.dutiesToCreateRTsFromMatrixChart.length; d++){
			  isContinue = true;
			  //Remove exisitng round trips
			  for(var d1=0 ;d1<$scope.existingRoundtrips.length; d1++){
				  if(($scope.dutiesToCreateRTsFromMatrixChart[d][0][$scope.ddFieldsForMatrixChart.id] == 
					  	$scope.existingRoundtrips[d1][0][$scope.ddFieldsForMatrixChart.id]) &&
						  $scope.dutiesToCreateRTsFromMatrixChart[d][1][$scope.ddFieldsForMatrixChart.id] == 
							  $scope.existingRoundtrips[d1][1][$scope.ddFieldsForMatrixChart.id]){
					  	$scope.dutiesToCreateRTsFromMatrixChart.splice(d,1);
				  		d--;
				  		isContinue = false;
				  		break;
				  }
			  }
			  if(isContinue){
				  if(isDaily || is6Day){
					  roundTrips.push({from:$scope.getDutyObject($scope.dutiesToCreateRTsFromMatrixChart[d][0],$scope.ddFieldsForMatrixChart),
			  				to:$scope.getDutyObject($scope.dutiesToCreateRTsFromMatrixChart[d][1],$scope.ddFieldsForMatrixChart),
			  				crewType:crewType});
				  }else{
					  roundTrips.push({drivingDuties : [{id:$scope.dutiesToCreateRTsFromMatrixChart[d][0][$scope.ddFieldsForMatrixChart.id]},
						                                   {id:$scope.dutiesToCreateRTsFromMatrixChart[d][1][$scope.ddFieldsForMatrixChart.id]}],
						                   crewType:crewType})
				  }
			  }
		  }
		  return roundTrips;
	  }
	  
	  /**
	   * This function is used to create duty object from duty array
	   */
	  $scope.getDutyObject = function(duty,fields){
		  return {
			  "arrivalDay" : duty[fields.arrivalDay],
			  "arrivalTime" : duty[fields.arrivalTime],
			  "ddName" : duty[fields.ddName],
			  "departureDay" : duty[fields.departureDay],
			  "departureTime" : duty[fields.departureTime],
			  "distance" : duty[fields.distance],
			  "duration" : duty[fields.duration],
			  "fromStation" : duty[fields.fromStation],
			  "id" : duty[fields.id],
			  "is6DayTrain" : duty[fields.is6DayTrain],
			  "isDailyTrain" : duty[fields.isDailyTrain],
			  "toStation" : duty[fields.toStation],
			  "isIgnore" : duty[fields.isIgnore],
			  "isRoundTrip" : duty[fields.isRoundTrip],
			  "roundTrip" : duty[fields.roundTrip],
			  "roundTripBaseStation" : duty[fields.roundTripBaseStation],
			  "roundTripName" : duty[fields.roundTripName],
			  "roundTripOrderNo" : duty[fields.roundTripOrderNo],
			  "signOffDuration" : duty[fields.signOffDuration],
			  "availableTime": minutesToTimeFormat(duty[fields.availableTime])
			}
	  }
	  
	  /**
	   * This function is used to generate duties and round trips chart between two stations 
	   */
	  $scope.generateDutiesAndRoundTripsChartForTwoStations = function(toggleFromRTDailyTrain,toggleFromRT6DayTrain,toggleFromRTWeeklyTrain,
				toggleToRTDailyTrain,toggleToRT6DayTrain,toggleToRTWeeklyTrain,
				toggleOtherRTDailyTrain,toggleOtherRT6DayTrain,toggleOtherRTWeeklyTrain,
				toggleNoRTDailyTrain,toggleNoRT6DayTrain,toggleNoRTWeeklyTrain){
		  	angular.element( document.querySelector( '#drivingDutiesForTwoStations' )).empty();
			$scope.strokeColors = {fromStationRoundTrip:"blue",toStationRoundTrip:"green",noStationRoundTrip:"yellow",noRoundTrip:"#d62728"};
			$scope.strokeWidth = {dailyTrain:3,sixDayTrain:1.5,weeklyTrain:0.7};
			var filteredDuties = [];
			if( toggleFromRTDailyTrain && toggleFromRT6DayTrain && toggleFromRTWeeklyTrain && 
					toggleToRTDailyTrain && toggleToRT6DayTrain && toggleToRTWeeklyTrain && 
					toggleOtherRTDailyTrain && toggleOtherRT6DayTrain && toggleOtherRTWeeklyTrain && 
					toggleNoRTDailyTrain && toggleNoRT6DayTrain && toggleNoRTWeeklyTrain){
				filteredDuties = $scope.newDutiesForGraph;
			}else{
				angular.forEach($scope.newDutiesForGraph,function(drivingDuty){
					if(toggleFromRTDailyTrain){
						if(drivingDuty[$scope.ddFieldsForMatrixChart.isRoundTrip] == 1 && 
								drivingDuty[$scope.ddFieldsForMatrixChart.isDaily] && 
								drivingDuty[$scope.ddFieldsForMatrixChart.roundTripBaseStation] == $scope.chartMetaData.fromStation)
						  filteredDuties.push(drivingDuty);
					}
					if(toggleFromRT6DayTrain){
						if(drivingDuty[$scope.ddFieldsForMatrixChart.isRoundTrip] == 1 && 
								drivingDuty[$scope.ddFieldsForMatrixChart.is6Day] && 
								drivingDuty[$scope.ddFieldsForMatrixChart.roundTripBaseStation] == $scope.chartMetaData.fromStation)
						  filteredDuties.push(drivingDuty);
					}
					if(toggleFromRT6DayTrain){
						if(drivingDuty[$scope.ddFieldsForMatrixChart.isRoundTrip] == 1 && 
								! drivingDuty[$scope.ddFieldsForMatrixChart.is6Day] && !drivingDuty[$scope.ddFieldsForMatrixChart.isDaily] &&
								drivingDuty[$scope.ddFieldsForMatrixChart.roundTripBaseStation] == $scope.chartMetaData.fromStation)
						  filteredDuties.push(drivingDuty);
					}
					if(toggleToRTDailyTrain){
						if(drivingDuty[$scope.ddFieldsForMatrixChart.isRoundTrip] == 1 && 
								drivingDuty[$scope.ddFieldsForMatrixChart.isDaily] && 
								drivingDuty[$scope.ddFieldsForMatrixChart.roundTripBaseStation] == $scope.chartMetaData.toStation)
						  filteredDuties.push(drivingDuty);
					}
					if(toggleToRT6DayTrain){
						if(drivingDuty[$scope.ddFieldsForMatrixChart.isRoundTrip] == 1 && 
								drivingDuty[$scope.ddFieldsForMatrixChart.is6Day] && 
								drivingDuty[$scope.ddFieldsForMatrixChart.roundTripBaseStation] == $scope.chartMetaData.toStation)
						  filteredDuties.push(drivingDuty);
					}
					if(toggleToRTWeeklyTrain){
						if(drivingDuty[$scope.ddFieldsForMatrixChart.isRoundTrip] == 1 && 
								! drivingDuty[$scope.ddFieldsForMatrixChart.is6Day] && !drivingDuty[$scope.ddFieldsForMatrixChart.isDaily] &&
								drivingDuty[$scope.ddFieldsForMatrixChart.roundTripBaseStation] == $scope.chartMetaData.toStation)
						  filteredDuties.push(drivingDuty);
					}
					
					if(toggleOtherRTDailyTrain){
						if(drivingDuty[$scope.ddFieldsForMatrixChart.isRoundTrip] == 1 && 
								drivingDuty[$scope.ddFieldsForMatrixChart.isDaily] && 
								drivingDuty[$scope.ddFieldsForMatrixChart.roundTripBaseStation] != $scope.chartMetaData.toStation &&
								drivingDuty[$scope.ddFieldsForMatrixChart.roundTripBaseStation] != $scope.chartMetaData.fromStation)
						  filteredDuties.push(drivingDuty);
					}
					if(toggleOtherRT6DayTrain){
						if(drivingDuty[$scope.ddFieldsForMatrixChart.isRoundTrip] == 1 && 
								drivingDuty[$scope.ddFieldsForMatrixChart.is6Day] && 
								drivingDuty[$scope.ddFieldsForMatrixChart.roundTripBaseStation] != $scope.chartMetaData.toStation &&
								drivingDuty[$scope.ddFieldsForMatrixChart.roundTripBaseStation] != $scope.chartMetaData.fromStation)
						  filteredDuties.push(drivingDuty);
					}
					if(toggleOtherRTWeeklyTrain){
						if(drivingDuty[$scope.ddFieldsForMatrixChart.isRoundTrip] == 1 && 
								!drivingDuty[$scope.ddFieldsForMatrixChart.is6Day] && !drivingDuty[$scope.ddFieldsForMatrixChart.isDaily] &&
								drivingDuty[$scope.ddFieldsForMatrixChart.roundTripBaseStation] != $scope.chartMetaData.toStation &&
								drivingDuty[$scope.ddFieldsForMatrixChart.roundTripBaseStation] != $scope.chartMetaData.fromStation)
						  filteredDuties.push(drivingDuty);
					}
					if(toggleNoRTDailyTrain){
						if(drivingDuty[$scope.ddFieldsForMatrixChart.isRoundTrip] != 1 && 
								drivingDuty[$scope.ddFieldsForMatrixChart.isDaily])
						  filteredDuties.push(drivingDuty);
					}
					if(toggleNoRT6DayTrain){
						if(drivingDuty[$scope.ddFieldsForMatrixChart.isRoundTrip] != 1 && 
								drivingDuty[$scope.ddFieldsForMatrixChart.is6Day])
						  filteredDuties.push(drivingDuty);
					}
					if(toggleNoRTWeeklyTrain){
						if(drivingDuty[$scope.ddFieldsForMatrixChart.isRoundTrip] != 1 && 
								! drivingDuty[$scope.ddFieldsForMatrixChart.is6Day] && !drivingDuty[$scope.ddFieldsForMatrixChart.isDaily])
						  filteredDuties.push(drivingDuty);
					}
				  });
			}
			
			var linechart, mydata;
			var color, background, foreground, ordinal_labels, projection;
			var mytime, myhr, mymin, myday, val, ch, i, j, k;
			var sortedItems;
			var parseTime=d3.time.format("%H:%M").parse;
			// Define the div for the tooltip
			var div = d3.select("body").append("div")	
			    .attr("class", "tooltip")				
			    .style("opacity", 0);
			var marg={top:50,left:250,bottom:40,right:40};
			var width=650-marg.left-marg.right,
				height=3520-marg.top-marg.bottom,
				finalWidth=width+marg.left+marg.right+300,
				finalHeight=height+marg.top+marg.bottom+100;
			var p=d3.select("#drivingDutiesForTwoStations").append("svg").attr("width",finalWidth).attr("height",finalHeight).append("g").attr("transform", "translate(" + marg.left + "," + marg.top + ")");
			var plotdays=[["Sunday",24],["Monday",48],["Tuesday",72],["Wednesday",96],["Thursday",120],["Friday",144],["Saturday",168]];
			var dim_plot = [
			{
			    name: $scope.chartMetaData.fromStation,
			    scale: d3.scale.linear().range([0,height]),
			    type: "Time"
			},
			{
			    name: $scope.chartMetaData.toStation,
			    scale: d3.scale.linear().range([0,height]),
				type: "Time"
			},];

			var x_axis = d3.scale.ordinal().rangePoints([0, width]).domain(dim_plot.map(function(d){return d.name; }));
			var y_Axis = d3.svg.axis().orient("left").ticks(168);
			var other_yaxis=d3.scale.linear().range([0,height]).domain([0,168]);
			var fun_yaxis=d3.scale.linear().range([0,height]).domain([0,24]);
			var lineGen = d3.svg.line().defined(function(d) { return !isNaN(d[1]); });
			var newline = d3.svg.line().x(function(d,i){return d.x}).y(function(d){return d.y});
			var dimension = p.selectAll(".dimension")
							 .data(dim_plot).enter()
							 .append("g").attr("class", "dimension")
							 .attr("transform", function(d) { return "translate(" + x_axis(d.name) + ")"; });

				clearall();
				sortedItems = ["Duties"];
				//#0966EA #d62728
				color = d3.scale.category10().domain(sortedItems);
				weekly_plotting(filteredDuties);

			function weekly_plotting(mydata)
			{
				dim_plot.forEach(function(p){ p.scale.domain([0,168]); });
				
				p.append("g").attr("class", "background").selectAll("path").data(mydata).enter().append("path").attr("d", draw);
				 
				linechart=p.append("g").attr("class", "foreground").selectAll("path").data(mydata).enter();
				 
				linechart.append("path").attr("d", draw).attr("stroke", 
						function(d){
							var color = $scope.strokeColors.noStationRoundTrip;
							if(d[$scope.ddFieldsForMatrixChart.isRoundTrip] != 1){
								color = $scope.strokeColors.noRoundTrip;
							}else if(d[$scope.ddFieldsForMatrixChart.roundTripBaseStation] == $scope.chartMetaData.fromStation){
								color = $scope.strokeColors.fromStationRoundTrip;
							}else if(d[$scope.ddFieldsForMatrixChart.roundTripBaseStation] == $scope.chartMetaData.toStation){
								color = $scope.strokeColors.toStationRoundTrip;
							}
							return color;
							}).attr("stroke-width", function(d){ 
									var width = $scope.strokeWidth.weeklyTrain;
									if(d[$scope.ddFieldsForMatrixChart.isDaily])
										width = $scope.strokeWidth.dailyTrain;
									else if(d[$scope.ddFieldsForMatrixChart.is6Day])
										width = $scope.strokeWidth.sixDayTrain;
									return width;
							})/*.style("stroke-dasharray", ("10,3"));*/
				 
				dimension.append("g").attr("class", "axis")
						 .each(function(d) {d3.select(this).call(y_Axis.scale(d.scale)); })
						 .append("text").attr("text-anchor", "middle")
						 .attr("y", -9).text(function(d) { return d.name; });
				

				//to add legend
				mylegend();
						 
				//add details on ARR axis
				p.selectAll(".text")
				 .data(mydata).enter()
				 .append("g").attr("class", "foreground")
				 .append("text").attr("x", -200)
				 .attr("y", function(d)
				 {	
					myday=d[$scope.ddFieldsForMatrixChart.departureDay];
					mytime=d[$scope.ddFieldsForMatrixChart.departureTime];
					var val = null;
					if(mytime){
						myhr=parseFloat(mytime.slice(0,2));
						mymin=parseInt(mytime.slice(-2));
						val=theswitch(mytime,myday);
					}
					return other_yaxis(val)-3;
				 })
				 .text(function(d){var myvalues=[d[$scope.ddFieldsForMatrixChart.ddName], d[$scope.ddFieldsForMatrixChart.departureTime],"("+d[$scope.ddFieldsForMatrixChart.duration]+"m)"]; return myvalues;});
				 
				//add details on DEP axis
				p.selectAll(".text")
				 .data(mydata).enter()
				 .append("g").attr("class", "foreground")
				 .append("text").attr("x", 360)
				 .attr("y", function(d)
				 {	
					myday=d[$scope.ddFieldsForMatrixChart.arrivalDay];
					mytime=d[$scope.ddFieldsForMatrixChart.arrivalTime];
					var val = null;
					if(mytime){
						myhr=parseFloat(mytime.slice(0,2));
						mymin=parseInt(mytime.slice(-2));
						val=theswitch(mytime,myday);
					}
					return other_yaxis(val)-3;
				 })
				 .text(function(d){
					 var myvalues=["("+d[$scope.ddFieldsForMatrixChart.duration]+"m)",d[$scope.ddFieldsForMatrixChart.arrivalTime], d[$scope.ddFieldsForMatrixChart.ddName]]; 
					 return myvalues; 
					});
				 
				//adding days text on arrival line
				p.selectAll(".text")
				 .data(plotdays).enter()
				 .append("g").attr("class", "daystext")
				 .append("text").attr("x", -80)
				 .attr("y", function(d){return other_yaxis(d[1])})
				 .text(function(d) { return d[0]; });
				 
				//adding days text on departure line
				p.selectAll(".text")
				 .data(plotdays).enter()
				 .append("g").attr("class", "daystext")
				 .append("text").attr("x", 470)
				 .attr("y", function(d){return other_yaxis(d[1])})
				 .text(function(d) { return d[0]; });
				 
				//drawing additional lines
				p.append("g")
				 .attr("class", "foreground")
				 .selectAll("path").data(mydata)
				 .attr("class", "extra_lines_fore")
				 .enter().append("path")
				 .attr("d", arrdraw)
				 .attr("stroke", function(d){
					 	var color = $scope.strokeColors.noStationRoundTrip;
						if(d[$scope.ddFieldsForMatrixChart.isRoundTrip] != 1){
							color = $scope.strokeColors.noRoundTrip;
						}else if(d[$scope.ddFieldsForMatrixChart.roundTripBaseStation] == $scope.chartMetaData.fromStation){
							color = $scope.strokeColors.fromStationRoundTrip;
						}else if(d[$scope.ddFieldsForMatrixChart.roundTripBaseStation] == $scope.chartMetaData.toStation){
							color = $scope.strokeColors.toStationRoundTrip;
						}
						return color;
					})
				 .attr("stroke-width", function(d){ 
					 	var width = $scope.strokeWidth.weeklyTrain;
						if(d[$scope.ddFieldsForMatrixChart.isDaily])
							width = $scope.strokeWidth.dailyTrain;
						else if(d[$scope.ddFieldsForMatrixChart.is6Day])
							width = $scope.strokeWidth.sixDayTrain;
						return width;
						});
				
				p.append("g")
				 .attr("class", "foreground")
				 .selectAll("path").data(mydata)
				 .enter().append("path")
				 .attr("class", "extra_lines_fore")
				 .attr("d", depdraw)
				 .attr("stroke", function(d){
					 	var color = $scope.strokeColors.noStationRoundTrip;
						if(d[$scope.ddFieldsForMatrixChart.isRoundTrip] != 1){
							color = $scope.strokeColors.noRoundTrip;
						}else if(d[$scope.ddFieldsForMatrixChart.roundTripBaseStation] == $scope.chartMetaData.fromStation){
							color = $scope.strokeColors.fromStationRoundTrip;
						}else if(d[$scope.ddFieldsForMatrixChart.roundTripBaseStation] == $scope.chartMetaData.toStation){
							color = $scope.strokeColors.toStationRoundTrip;
						}
						return color;
					})
				 .attr("stroke-width", function(d){ 
					 	var width = $scope.strokeWidth.weeklyTrain;
						if(d[$scope.ddFieldsForMatrixChart.isDaily])
							width = $scope.strokeWidth.dailyTrain;
						else if(d[$scope.ddFieldsForMatrixChart.is6Day])
							width = $scope.strokeWidth.sixDayTrain;
						return width;
						});
				 
				p.append("g").attr("class", "background")
				 .selectAll("path").data(mydata)
				 .enter().append("path")
				 .attr("class", "extra_lines_fore").attr("d", arrdraw);
				 
				p.append("g").attr("class", "background")
				 .selectAll("path").data(mydata)
				 .enter().append("path")
				 .attr("class", "extra_lines_fore").attr("d", depdraw);
				 
				ordinal_labels = p.selectAll(".foreground text").on("mouseover", mouseover).on("mouseout", mouseout);
				projection = p.selectAll(".background path,.foreground path").on("mouseover", mouseover).on("mouseout", mouseout);	
			}

			function draw(d){
				return lineGen(dim_plot.map(function(p){ 
					if(p.name!=$scope.chartMetaData.fromStation){
						myday=d[$scope.ddFieldsForMatrixChart.arrivalDay];
						mytime = d[$scope.ddFieldsForMatrixChart.arrivalTime];
					}else{
						myday=d[$scope.ddFieldsForMatrixChart.departureDay];
						mytime = d[$scope.ddFieldsForMatrixChart.departureTime];
					}
					val=theswitch(mytime,myday);
					return [x_axis(p.name), p.scale(val)];
				}));
			}

			function arrdraw(d){
				mytime=d[$scope.ddFieldsForMatrixChart.departureTime];
				myday=d[$scope.ddFieldsForMatrixChart.departureDay];
				var val = null;
				if(myday >= 0){
					myhr=parseFloat(mytime.slice(0,2));
					mymin=parseInt(mytime.slice(-2));
					val=theswitch(mytime,myday);
				}
				var myarray=[{x:0,y:other_yaxis(val)},{x:-200,y:other_yaxis(val)}];
				return (mytime)?newline(myarray):null;
			}

			function depdraw(d){
				mytime=d[$scope.ddFieldsForMatrixChart.arrivalTime];
				myday=d[$scope.ddFieldsForMatrixChart.arrivalDay];
				var val = null;
				if(myday >= 0){
					myhr=parseFloat(mytime.slice(0,2));
					mymin=parseInt(mytime.slice(-2));
					val=theswitch(mytime,myday);
				}
				var myarray=[{x:360,y:other_yaxis(val)},{x:550,y:other_yaxis(val)}];
				return (mytime)?newline(myarray):null;
			}

			function theswitch(currenttime,currentday)
			{
				var val = null 
				if(currenttime && currentday >= 0){
					myhr=parseFloat(currenttime.slice(0,2));
					mymin=parseInt(currenttime.slice(3,5));
					switch(currentday){
						case 0:
							val=myhr+parseFloat(mymin/60);
							break;
						case 1:
							val=24.0+myhr+parseFloat(mymin/60);
							break;
						case 2:
							val=48.0+myhr+parseFloat(mymin/60);
							break;
						case 3:
							val=72.0+myhr+parseFloat(mymin/60);
							break;
						case 4:
							val=96.0+myhr+parseFloat(mymin/60);
							break;
						case 5:
							val=120.0+myhr+parseFloat(mymin/60);
							break;
						case 6:
							val=144.0+myhr+parseFloat(mymin/60);
							break;
						default :
							val=null;
							break;
						} 
				}
				return val;
			}

			function mylegend()
			{
				//(data);
				color.domain(sortedItems);
				var legend_w=width+210;
				var legend_h=-60;							
				var legend=p.selectAll(".legend")
							.data(sortedItems).enter()
							.append("g").attr("class", "legend")
							.each(function(d){ 
								var g = d3.select(this);
								g.append("rect").attr("x", legend_w+10).attr("y", legend_h+300).attr("width", 50).attr("height", $scope.strokeWidth.dailyTrain).style("fill", $scope.strokeColors.fromStationRoundTrip)
									.attr("class", "d3legend "+(($scope.toggleFromStationRT)?"activeLegend":"")).on("click",toggleFromStationRT).style("stroke", $scope.strokeColors.fromStationRoundTrip);
								g.append("rect").attr("x", legend_w+10).attr("y", legend_h+330).attr("width", 50).attr("height", $scope.strokeWidth.dailyTrain).style("fill", $scope.strokeColors.fromStationRoundTrip)
								.attr("class", "d3legend "+(($scope.toggleFromStationRT)?"activeLegend":"")).on("click",toggleFromStationRT).style("stroke", $scope.strokeColors.fromStationRoundTrip);
								g.append("rect").attr("x", legend_w+10).attr("y", legend_h+360).attr("width", 50).attr("height", $scope.strokeWidth.sixDayTrain).style("fill", $scope.strokeColors.fromStationRoundTrip)
								.attr("class", "d3legend "+(($scope.toggleFromRT6Day)?"activeLegend":"")).on("click",toggleFromRT6Day).style("stroke", $scope.strokeColors.fromStationRoundTrip);
								g.append("rect").attr("x", legend_w+10).attr("y", legend_h+390).attr("width", 50).attr("height", $scope.strokeWidth.weeklyTrain).style("fill", $scope.strokeColors.fromStationRoundTrip)
								.attr("class", "d3legend "+(($scope.toggleFromRTWeekly)?"activeLegend":"")).on("click",toggleFromRTWeekly).style("stroke", $scope.strokeColors.fromStationRoundTrip);
								
								g.append("rect").attr("x", legend_w+10).attr("y", legend_h+440).attr("width", 50).attr("height", $scope.strokeWidth.dailyTrain).style("fill", $scope.strokeColors.toStationRoundTrip)
								.attr("class", "d3legend "+(($scope.toggleToStationRT)?"activeLegend":"")).on("click",toggleToStationRT).style("stroke", $scope.strokeColors.toStationRoundTrip);
								g.append("rect").attr("x", legend_w+10).attr("y", legend_h+470).attr("width", 50).attr("height", $scope.strokeWidth.dailyTrain).style("fill", $scope.strokeColors.toStationRoundTrip)
								.attr("class", "d3legend "+(($scope.toggleToRTDaily)?"activeLegend":"")).on("click",toggleToRTDaily).style("stroke", $scope.strokeColors.toStationRoundTrip);
								g.append("rect").attr("x", legend_w+10).attr("y", legend_h+500).attr("width", 50).attr("height", $scope.strokeWidth.sixDayTrain).style("fill", $scope.strokeColors.toStationRoundTrip)
								.attr("class", "d3legend "+(($scope.toggleToRT6Day)?"activeLegend":"")).on("click",toggleToRT6Day).style("stroke", $scope.strokeColors.toStationRoundTrip);
								g.append("rect").attr("x", legend_w+10).attr("y", legend_h+530).attr("width", 50).attr("height", $scope.strokeWidth.weeklyTrain).style("fill", $scope.strokeColors.toStationRoundTrip)
								.attr("class", "d3legend "+(($scope.toggleToRTWeekly)?"activeLegend":"")).on("click",toggleToRTWeekly).style("stroke", $scope.strokeColors.toStationRoundTrip);
								
								g.append("rect").attr("x", legend_w+10).attr("y", legend_h+580).attr("width", 50).attr("height", $scope.strokeWidth.dailyTrain).style("fill", $scope.strokeColors.noStationRoundTrip)
								.attr("class", "d3legend "+(($scope.toggleOtherStationRT)?"activeLegend":"")).on("click",toggleOtherStationRT).style("stroke", $scope.strokeColors.noStationRoundTrip);
								g.append("rect").attr("x", legend_w+10).attr("y", legend_h+610).attr("width", 50).attr("height", $scope.strokeWidth.dailyTrain).style("fill", $scope.strokeColors.noStationRoundTrip)
								.attr("class", "d3legend "+(($scope.toggleOtherRTDaily)?"activeLegend":"")).on("click",toggleOtherRTDaily).style("stroke", $scope.strokeColors.noStationRoundTrip);
								g.append("rect").attr("x", legend_w+10).attr("y", legend_h+640).attr("width", 50).attr("height", $scope.strokeWidth.sixDayTrain).style("fill", $scope.strokeColors.noStationRoundTrip)
								.attr("class", "d3legend "+(($scope.toggleOtherRT6Day)?"activeLegend":"")).on("click",toggleOtherRT6Day).style("stroke", $scope.strokeColors.noStationRoundTrip);
								g.append("rect").attr("x", legend_w+10).attr("y", legend_h+670).attr("width", 50).attr("height", $scope.strokeWidth.weeklyTrain).style("fill", $scope.strokeColors.noStationRoundTrip)
								.attr("class", "d3legend "+(($scope.toggleOtherRTWeekly)?"activeLegend":"")).on("click",toggleOtherRTWeekly).style("stroke", $scope.strokeColors.noStationRoundTrip);
						
								g.append("rect").attr("x", legend_w+10).attr("y", legend_h+720).attr("width", 50).attr("height", $scope.strokeWidth.dailyTrain).style("fill", $scope.strokeColors.noRoundTrip)
								.attr("class", "d3legend "+(($scope.toggleNoRT)?"activeLegend":"")).on("click",toggleNoRT).style("stroke", $scope.strokeColors.noRoundTrip);
								g.append("rect").attr("x", legend_w+10).attr("y", legend_h+750).attr("width", 50).attr("height", $scope.strokeWidth.dailyTrain).style("fill", $scope.strokeColors.noRoundTrip)
								.attr("class", "d3legend "+(($scope.toggleNoRTDaily)?"activeLegend":"")).on("click",toggleNoRTDaily).style("stroke", $scope.strokeColors.noRoundTrip);
								g.append("rect").attr("x", legend_w+10).attr("y", legend_h+780).attr("width", 50).attr("height", $scope.strokeWidth.sixDayTrain).style("fill", $scope.strokeColors.noRoundTrip)
								.attr("class", "d3legend "+(($scope.toggleNoRT6Day)?"activeLegend":"")).on("click",toggleNoRT6Day).style("stroke", $scope.strokeColors.noRoundTrip);
								g.append("rect").attr("x", legend_w+10).attr("y", legend_h+810).attr("width", 50).attr("height", $scope.strokeWidth.weeklyTrain).style("fill", $scope.strokeColors.noRoundTrip)
								.attr("class", "d3legend "+(($scope.toggleNoRTWeekly)?"activeLegend":"")).on("click",toggleNoRTWeekly).style("stroke", $scope.strokeColors.noRoundTrip);
								
								g.append("text").attr("x", legend_w+65).attr("y", legend_h+305).style("fill", (($scope.toggleFromStationRT)?$scope.strokeColors.fromStationRoundTrip:"black"))
									.attr("class", "d3legend "+(($scope.toggleFromStationRT)?"activeLegend":"")).on("click",toggleFromStationRT).text($scope.chartMetaData.fromStation+" RT");
								g.append("text").attr("x", legend_w+65).attr("y", legend_h+335).style("fill", (($scope.toggleFromRTDaily)?$scope.strokeColors.fromStationRoundTrip:"black"))
								.attr("class", "d3legend "+(($scope.toggleFromRTDaily)?"activeLegend":"")).on("click",toggleFromRTDaily).text("Daily");
								g.append("text").attr("x", legend_w+65).attr("y", legend_h+365).style("fill", (($scope.toggleFromRT6Day)?$scope.strokeColors.fromStationRoundTrip:"black"))
								.attr("class", "d3legend "+(($scope.toggleFromRT6Day)?"activeLegend":"")).on("click",toggleFromRT6Day).text("Six Day");
								g.append("text").attr("x", legend_w+65).attr("y", legend_h+395).style("fill", (($scope.toggleFromRTWeekly)?$scope.strokeColors.fromStationRoundTrip:"black"))
								.attr("class", "d3legend "+(($scope.toggleFromRTWeekly)?"activeLegend":"")).on("click",toggleFromRTWeekly).text("weekly");
							
								g.append("text").attr("x", legend_w+65).attr("y", legend_h+445).style("fill", (($scope.toggleToStationRT)?$scope.strokeColors.toStationRoundTrip:"black"))
								.attr("class", "d3legend "+(($scope.toggleToStationRT)?"activeLegend":"")).on("click",toggleToStationRT).text($scope.chartMetaData.toStation+" RT");
								g.append("text").attr("x", legend_w+65).attr("y", legend_h+475).style("fill", (($scope.toggleToRTDaily)?$scope.strokeColors.toStationRoundTrip:"black"))
								.attr("class", "d3legend "+(($scope.toggleToRTDaily)?"activeLegend":"")).on("click",toggleToRTDaily).text("Daily");
								g.append("text").attr("x", legend_w+65).attr("y", legend_h+505).style("fill", (($scope.toggleToRT6Day)?$scope.strokeColors.toStationRoundTrip:"black"))
								.attr("class", "d3legend "+(($scope.toggleToRT6Day)?"activeLegend":"")).on("click",toggleToRT6Day).text("Six Day");
								g.append("text").attr("x", legend_w+65).attr("y", legend_h+535).style("fill", (($scope.toggleToRTWeekly)?$scope.strokeColors.toStationRoundTrip:"black"))
								.attr("class", "d3legend "+(($scope.toggleToRTWeekly)?"activeLegend":"")).on("click",toggleToRTWeekly).text("weekly");
							
								g.append("text").attr("x", legend_w+65).attr("y", legend_h+585).style("fill", (($scope.toggleOtherStationRT)?$scope.strokeColors.noStationRoundTrip:"black"))
								.attr("class", "d3legend "+(($scope.toggleOtherStationRT)?"activeLegend":"")).on("click",toggleOtherStationRT).text("Other RT");
								g.append("text").attr("x", legend_w+65).attr("y", legend_h+615).style("fill", (($scope.toggleOtherRTDaily)?$scope.strokeColors.noStationRoundTrip:"black"))
								.attr("class", "d3legend "+(($scope.toggleOtherRTDaily)?"activeLegend":"")).on("click",toggleOtherRTDaily).text("Daily");
								g.append("text").attr("x", legend_w+65).attr("y", legend_h+645).style("fill", (($scope.toggleOtherRT6Day)?$scope.strokeColors.noStationRoundTrip:"black"))
								.attr("class", "d3legend "+(($scope.toggleOtherRT6Day)?"activeLegend":"")).on("click",toggleOtherRT6Day).text("Six Day");
								g.append("text").attr("x", legend_w+65).attr("y", legend_h+675).style("fill", (($scope.toggleOtherRTWeekly)?$scope.strokeColors.noStationRoundTrip:"black"))
								.attr("class", "d3legend "+(($scope.toggleOtherRTWeekly)?"activeLegend":"")).on("click",toggleOtherRTWeekly).text("weekly");
							
								g.append("text").attr("x", legend_w+65).attr("y", legend_h+725).style("fill", (($scope.toggleNoRT)?$scope.strokeColors.noRoundTrip:"black"))
								.attr("class", "d3legend "+(($scope.toggleNoRT)?"activeLegend":"")).on("click",toggleNoRT).text("No RT");
								g.append("text").attr("x", legend_w+65).attr("y", legend_h+755).style("fill", (($scope.toggleNoRTDaily)?$scope.strokeColors.noRoundTrip:"black"))
								.attr("class", "d3legend "+(($scope.toggleNoRTDaily)?"activeLegend":"")).on("click",toggleNoRTDaily).text("Daily");
								g.append("text").attr("x", legend_w+65).attr("y", legend_h+785).style("fill", (($scope.toggleNoRT6Day)?$scope.strokeColors.noRoundTrip:"black"))
								.attr("class", "d3legend "+(($scope.toggleNoRT6Day)?"activeLegend":"")).on("click",toggleNoRT6Day).text("Six Day");
								g.append("text").attr("x", legend_w+65).attr("y", legend_h+815).style("fill", (($scope.toggleNoRTWeekly)?$scope.strokeColors.noRoundTrip:"black"))
								.attr("class", "d3legend "+(($scope.toggleNoRTWeekly)?"activeLegend":"")).on("click",toggleNoRTWeekly).text("weekly");
								
							
							});
			}
			function generateChartMethod(){
				$scope.generateDutiesAndRoundTripsChartForTwoStations($scope.toggleFromRTDaily,$scope.toggleFromRT6Day,$scope.toggleFromRTWeekly,
						$scope.toggleToRTDaily,$scope.toggleToRT6Day,$scope.toggleToRTWeekly,
						$scope.toggleOtherRTDaily,$scope.toggleOtherRT6Day,$scope.toggleOtherRTWeekly,
						$scope.toggleNoRTDaily,$scope.toggleNoRT6Day,$scope.toggleNoRTWeekly);
			}
			function toggleFromStationRT(){
				$scope.toggleFromStationRT = !$scope.toggleFromStationRT;
				if($scope.toggleFromStationRT){
					$scope.toggleFromRTDaily = true;
					$scope.toggleFromRT6Day = true;
					$scope.toggleFromRTWeekly = true;
				}else{
					$scope.toggleFromRTDaily = false;
					$scope.toggleFromRT6Day = false;
					$scope.toggleFromRTWeekly = false;
				}
				generateChartMethod();
			}
			function toggleFromRTDaily(){
				$scope.toggleFromRTDaily = !$scope.toggleFromRTDaily;
				generateChartMethod();
			}
			function toggleFromRT6Day(){
				$scope.toggleFromRT6Day = !$scope.toggleFromRT6Day;
				generateChartMethod();
			}
			function toggleFromRTWeekly(){
				$scope.toggleFromRTWeekly = !$scope.toggleFromRTWeekly;
				generateChartMethod();
			}
			
			function toggleToStationRT(){
				$scope.toggleToStationRT = !$scope.toggleToStationRT;
				if($scope.toggleToStationRT){
					$scope.toggleToRTDaily = true;
					$scope.toggleToRT6Day = true;
					$scope.toggleToRTWeekly = true;
				}else{
					$scope.toggleToRTDaily = false;
					$scope.toggleToRT6Day = false;
					$scope.toggleToRTWeekly = false;
				}
				generateChartMethod();
			}
			function toggleToRTDaily(){
				$scope.toggleToRTDaily = !$scope.toggleToRTDaily;
				generateChartMethod();
			}
			function toggleToRT6Day(){
				$scope.toggleToRT6Day = !$scope.toggleToRT6Day;
				generateChartMethod();
			}
			function toggleToRTWeekly(){
				$scope.toggleToRTWeekly = !$scope.toggleToRTWeekly;
				generateChartMethod();
			}
			function toggleOtherStationRT(){
				$scope.toggleOtherStationRT = !$scope.toggleOtherStationRT;
				if($scope.toggleOtherStationRT){
					$scope.toggleOtherRTDaily = true;
					$scope.toggleOtherRT6Day = true;
					$scope.toggleOtherRTWeekly = true;
				}else{
					$scope.toggleOtherRTDaily = false;
					$scope.toggleOtherRT6Day = false;
					$scope.toggleOtherRTWeekly = false;
				}
				generateChartMethod();
			}
			function toggleOtherRTDaily(){
				$scope.toggleOtherRTDaily = !$scope.toggleOtherRTDaily;
				generateChartMethod();
			}
			function toggleOtherRT6Day(){
				$scope.toggleOtherRT6Day = !$scope.toggleOtherRT6Day;
				generateChartMethod();
			}
			function toggleOtherRTWeekly(){
				$scope.toggleOtherRTWeekly = !$scope.toggleOtherRTWeekly;
				generateChartMethod();
			}
			function toggleNoRT(){
				$scope.toggleNoRT = !$scope.toggleNoRT;
				if($scope.toggleNoRT){
					$scope.toggleNoRTDaily = true;
					$scope.toggleNoRT6Day = true;
					$scope.toggleNoRTWeekly = true;
				}else{
					$scope.toggleNoRTDaily = false;
					$scope.toggleNoRT6Day = false;
					$scope.toggleNoRTWeekly = false;
				}
				$scope.generateDutiesAndRoundTripsChartForTwoStations($scope.toggleFromRTDaily,$scope.toggleFromRT6Day,$scope.toggleFromRTWeekly,
										$scope.toggleToRTDaily,$scope.toggleToRT6Day,$scope.toggleToRTWeekly,
										$scope.toggleOtherRTDaily,$scope.toggleOtherRT6Day,$scope.toggleOtherRTWeekly,
										$scope.toggleNoRTDaily,$scope.toggleNoRT6Day,$scope.toggleNoRTWeekly);
			}
			function toggleNoRTDaily(){
				$scope.toggleNoRTDaily = !$scope.toggleNoRTDaily;
				$scope.generateDutiesAndRoundTripsChartForTwoStations($scope.toggleFromRTDaily,$scope.toggleFromRT6Day,$scope.toggleFromRTWeekly,
						$scope.toggleToRTDaily,$scope.toggleToRT6Day,$scope.toggleToRTWeekly,
						$scope.toggleOtherRTDaily,$scope.toggleOtherRT6Day,$scope.toggleOtherRTWeekly,
						$scope.toggleNoRTDaily,$scope.toggleNoRT6Day,$scope.toggleNoRTWeekly);
			}
			function toggleNoRT6Day(){
				$scope.toggleNoRT6Day = !$scope.toggleNoRT6Day;
				$scope.generateDutiesAndRoundTripsChartForTwoStations($scope.toggleFromRTDaily,$scope.toggleFromRT6Day,$scope.toggleFromRTWeekly,
						$scope.toggleToRTDaily,$scope.toggleToRT6Day,$scope.toggleToRTWeekly,
						$scope.toggleOtherRTDaily,$scope.toggleOtherRT6Day,$scope.toggleOtherRTWeekly,
						$scope.toggleNoRTDaily,$scope.toggleNoRT6Day,$scope.toggleNoRTWeekly);
			}
			function toggleNoRTWeekly(){
				$scope.toggleNoRTWeekly = !$scope.toggleNoRTWeekly;
				$scope.generateDutiesAndRoundTripsChartForTwoStations($scope.toggleFromRTDaily,$scope.toggleFromRT6Day,$scope.toggleFromRTWeekly,
						$scope.toggleToRTDaily,$scope.toggleToRT6Day,$scope.toggleToRTWeekly,
						$scope.toggleOtherRTDaily,$scope.toggleOtherRT6Day,$scope.toggleOtherRTWeekly,
						$scope.toggleNoRTDaily,$scope.toggleNoRT6Day,$scope.toggleNoRTWeekly);
			}

			function mouseover(d){
				div.transition()		
	                .duration(200)		
	                .style("opacity", 1);		
				div	.html(((d[$scope.ddFieldsForMatrixChart.isRoundTrip] == 1)?d[$scope.ddFieldsForMatrixChart.roundTripBaseStation]+"-ROUND TRIP<br/>"+
								"RT - "+d[$scope.ddFieldsForMatrixChart.roundTripName]+"<br/>---------------------------<br/>":"")+
						("Train No - "+d[$scope.ddFieldsForMatrixChart.ddName]+"<br/>")+
						"From -"+d[$scope.ddFieldsForMatrixChart.fromStation]+"<br/>"+ 
						"Dep Time -"+((d[$scope.ddFieldsForMatrixChart.isForGraph])?d[$scope.ddFieldsForMatrixChart.arrivalTime ]:d[$scope.ddFieldsForMatrixChart.departureTime])+"<br/>"+
						"Dep Day -"+((d[$scope.ddFieldsForMatrixChart.isForGraph])?d[$scope.ddFieldsForMatrixChart.arrivalDay ]:d[$scope.ddFieldsForMatrixChart.departureDay])+"<br/>"+
						"To -"+d[$scope.ddFieldsForMatrixChart.toStation]+"<br/>"+ 
						"Arr Time -"+((d[$scope.ddFieldsForMatrixChart.isForGraph])?d[$scope.ddFieldsForMatrixChart.departureTime ]:d[$scope.ddFieldsForMatrixChart.arrivalTime])+"<br/>"+
						"Arr Day -"+((d[$scope.ddFieldsForMatrixChart.isForGraph])?d[$scope.ddFieldsForMatrixChart.departureDay ]:d[$scope.ddFieldsForMatrixChart.arrivalDay])+"<br/>"+
						"Duration -"+d[$scope.ddFieldsForMatrixChart.duration]+" mins")
              .style("left", (d3.event.pageX-11) + "px")		
              .style("top", (d3.event.pageY - 74) + "px");
				p.classed("active", true);
			    if (typeof d === "string") {
					projection.classed("inactive", function(x) { return x.name !== d; });
					projection.filter(function(x) { return x.name === d; }).each(moveToFront);
					ordinal_labels.classed("inactive", function(x) { return x !== d; });
					ordinal_labels.filter(function(x) { return x === d; }).each(moveToFront);
			    } 
				else {
					projection.classed("inactive", function(x) { return x !== d; });
					projection.filter(function(x) { return x === d; }).each(moveToFront);
					ordinal_labels.classed("inactive", function(x) { return x !== d.name; });
					ordinal_labels.filter(function(x) { return x === d.name; }).each(moveToFront);
			    }				
			}

			function mouseout(d){
				div.transition()		
              .duration(200)		
              .style("opacity", 0);	
				p.classed("active", false);
				projection.classed("inactive", false);
				ordinal_labels.classed("inactive", false);
			}

			function moveToFront(){
				this.parentNode.appendChild(this);
			}

			function clearall()
			{
				p.selectAll(".foreground text").remove();
				p.selectAll(".daystext").remove();
				p.selectAll(".legend").remove();
				p.selectAll("path").remove();
				p.selectAll(".axis").remove();
				p.selectAll(".background path").remove();
				p.selectAll(".foreground path").remove();
			}
	  }
	  
	  $scope.noOfDutiesLimit = 1000;
		/**
		 * This function is used to load create round trip chart
		 */
		$scope.loadCreateRoundTripChart = function(newStation,isNext,data){
			var baseStation,id;
			if(!newStation){
				newStation = $scope.stationCodeForCreateRoundtrip;
				$scope.createRoundTripsChartList = [];
				$scope.newRoundTripsFromChart = [];
				$scope.deletedRoundTripsIdFromChart = [];
				$scope.deletedRoundTripsFromChart = [];
			}else{
				baseStation = data.baseStation;id=data.id;
			}
			var temp = $scope.createRoundTripsChartList,isContinue = true,index = temp.length;
			if(newStation != baseStation){
				if(temp.length>1){
					if(!isNext && temp[0].baseStation == baseStation && id == temp[0].id) index = 0;
					else
					for(var i=((isNext)?0:1);i<temp.length+((isNext)?-1:0);i++){
						if(temp[i].baseStation == baseStation && id == temp[i].id)
							if(isNext){ 
								if(temp[i+1].baseStation == newStation){
									isContinue = false;
								}else
									index = i+1;
							}
							else if(!isNext){
								if(temp[i-1].baseStation == newStation){
									isContinue = false;
								}else
									index = i;
							}
					}
				}else{
					index = (isNext)?1:0;
				}
			}else{
				isContinue = false;
			}
			var dutiesObj = {};
			if(isContinue){
				for(var i=0; i<temp.length; i++){
					if(temp[i].baseStation == newStation){
						dutiesObj.baseStation = temp[i].baseStation;
						dutiesObj.data = temp[i].data;
						dutiesObj.fields = temp[i].fields;
						dutiesObj.id = 100;
						dutiesObj.legends= {isDuties : true,isRTs : false};
						dutiesObj.stations= {fromStations : [],toStations : [],toggleAllFromStations : true,toggleAllToStations: true};
						dutiesObj.duties = {previousDuty:[""],nextDuty:[""]};
						
						$scope.addNewStationToDutiesList(index,temp,dutiesObj,isNext);
						$scope.updateCharts(isNext);
						isContinue = false;
						break;
					}
				}
			}
			if(newStation == undefined || newStation == "" || newStation == null || 
					$scope.noOfDutiesLimit == undefined || $scope.noOfDutiesLimit == "" || $scope.noOfDutiesLimit == null){
				toaster
				.pop({
					type : 'error',
					title : 'Error',
					body : 'Enter Staton Code && Limit!!!'
				});
			}else if(isContinue){
			//server call to fetch all driving duties which are going from selected station 
				$http.get("/api/custom/drivingDuties/listWithRT?userPlan="+UserService.getSelectedUserPlan().id+"&fromStation="+newStation
						  +"&page=0&size="+$scope.noOfDutiesLimit+"&isIgnore=false").then(
							function(drivingDutiesFrom)
							{
								//server call to fetch all driving duties which are going to selected station
								$http.get("/api/custom/drivingDuties/listWithRT?userPlan="+UserService.getSelectedUserPlan().id+"&toStation="+newStation
										  +"&page=0&size="+$scope.noOfDutiesLimit+"&isIgnore=false").then(
											function(drivingDutiesTo)
											{
												var drivingDutiesFromBase = drivingDutiesFrom.data;
												var drivingDutiesToBase = drivingDutiesTo.data;
												var duties = [];
												if(drivingDutiesFromBase.data && drivingDutiesToBase.data)
													duties = drivingDutiesFromBase.data.concat(drivingDutiesToBase.data);
												else if(drivingDutiesFromBase.data)
													duties = drivingDutiesFromBase.data;
												else if(drivingDutiesToBase.data)
													duties = drivingDutiesToBase.data;
												if(duties.length > 0){
													$scope.createRoundTripDutiesFields = drivingDutiesToBase.fields;
													$scope.createRoundTripDutiesFields.isPartialLink = duties[0].length+1;
													$scope.createRoundTripDutiesFields.partialLinkId = duties[0].length+2;
													$scope.createRoundTripDutiesFields.isNewRT = duties[0].length+3;
													
													dutiesObj.id = 100;
													dutiesObj.baseStation = newStation;
													dutiesObj.data = duties;
													dutiesObj.fields = $scope.createRoundTripDutiesFields;
													dutiesObj.legends= {isDuties : true,isRTs : false};
													dutiesObj.stations= {fromStations : [],toStations : [],toggleAllFromStations : true,toggleAllToStations: true};
													dutiesObj.duties = {previousDuty:[""],nextDuty:[""]};
													
													for(var d = 0; d<dutiesObj.data.length; d++){
									                	dutiesObj.data[d].push("");
									                	dutiesObj.data[d].push(false);
									                	dutiesObj.data[d].push("");
									                	dutiesObj.data[d].push(false);
									                }
													
													$scope.addNewStationToDutiesList(index,temp,dutiesObj,isNext);
													$scope.setRemovedRTAsDuty($scope.deletedRoundTripsIdFromChart,$scope.createRoundTripDutiesFields.roundTrip);
													$scope.updateCharts(isNext);
												}else{
													toaster
													.pop({
														type : 'error',
														title : 'Duties',
														body : 'No Duties Found!!!'
													});
												}
											});
							});
						}
		}
		
		/**
		 * This function is used to add new station duties to station duties list
		 */
		$scope.addNewStationToDutiesList = function(index,dutiesList,dutiesObj,isNext){
			if(index < dutiesList.length)
				$scope.createRoundTripsChartList.splice(index,0,dutiesObj);
			else
				$scope.createRoundTripsChartList.push(dutiesObj);
			if(isNext)
				$scope.createRoundTripsChartList.length = index+1;
			else
				$scope.createRoundTripsChartList.splice(0,index);
			for(var i=0; i<$scope.createRoundTripsChartList.length; i++){
				$scope.createRoundTripsChartList[i].id = i;
			}
			$scope.setPreviousAndNextDuties(isNext);
		}
		
		/**
		 * This function is used to set previous duty and next duty
		 */
		$scope.setPreviousAndNextDuties = function(isNext){
			var temp = $scope.createRoundTripsChartList;
			for(var i=0; i<temp.length; i++){
				if(isNext && i==temp.length-1 && temp.length>1){
					temp[i].duties.previousDuty = temp[i-1].duties.nextDuty;
				}else if(!isNext && i==0 && temp.length>1){
					temp[i].duties.previousDuty = temp[i+1].duties.nextDuty;
				}
			}
		}
		
		/**
		 * This function is used to delete existing graphs and re-draw them with updated data
		 */
		$scope.updateCharts = function(isNext){
			document.getElementById("createRoundTripChartDiv").setAttribute("style","width: 100%;overflow-x: scroll;height:5180px");
			document.getElementById('createRoundTripChartDiv').innerHTML = "";
			angular.element(document.getElementById('createRoundTripChartDiv'))
			.append($compile('<div ng-repeat = "duties in createRoundTripsChartList" style="display: table-cell;"><create-round-trip-chart duties="duties"></create-round-trip-chart></div>')($scope));
		
			$timeout(function(){
	        	var text="",scrollWidth = document.getElementById('createRoundTripChartDiv').scrollWidth;
	        	for(var i=0;i<scrollWidth/7.5;i++){
	        		text += "a";
	        	}
	        	document.getElementById('createRoundTripChartDiv1').innerHTML = text;
	        	if(isNext)
	        		document.getElementById('createRoundTripChartDiv1').scrollLeft = scrollWidth;
	        },500);
		}
		
		/**
		 * This function is used to update station duties data if same station is repeated in chartList
		 */
		$scope.updateStationsDuties = function(data){
			var temp = $scope.createRoundTripsChartList,count = 0;
			for(var i=0; i<temp.length; i++){
				if(temp[i].baseStation == data.baseStation){
					temp[i].data = data.data;
					count++;
				}
			}
			$scope.updateCharts();
		}
		/**
		 * This function is used to add duties to new round trip and it will be called from
		 * create-round-trips-chart directive
		 */
		$scope.addDutiesToNewRTFromChart = function(data,duties){
			var rt = $scope.newRoundTripsFromChart,isFound = false;
			for(var r=0; r<rt.length; r++){
				if(rt[r].drivingDuties[rt[r].drivingDuties.length-1][$scope.createRoundTripDutiesFields.id] == duties[0][$scope.createRoundTripDutiesFields.id]){
					rt[r].drivingDuties.push(duties[1]);
					if(rt[r].drivingDuties[0][$scope.createRoundTripDutiesFields.fromStation] == 
						rt[r].drivingDuties[rt[r].drivingDuties.length-1][$scope.createRoundTripDutiesFields.toStation]){
						rt[r].drivingDuties = $scope.setThisDutiesAsNewRT(rt[r].drivingDuties);
					}else{
						rt[r].drivingDuties = $scope.setThisDutiesAsNoNewRT(rt[r].drivingDuties);
					}
					isFound = true;
					break;
				}
				if(isFound)break;
			}
			if(!isFound){
				if(duties[0][$scope.createRoundTripDutiesFields.fromStation] == duties[1][$scope.createRoundTripDutiesFields.toStation]){
					duties = $scope.setThisDutiesAsNewRT(duties);
				}
				rt.push({drivingDuties:duties,crewType:$scope.crewTypeForGraph});
			}
			$scope.updateStationsDuties(data);
			$scope.$apply();
		}
		/**
		 * This function is used to set duties as new RT
		 */
		$scope.setThisDutiesAsNewRT = function(duties){
			for(var i=0; i<duties.length; i++){
				duties[i][$scope.createRoundTripDutiesFields.isNewRT] = true;
			}
			return duties;
		}
		/**
		 * This function is used to set duties as No RT
		 */
		$scope.setThisDutiesAsNoNewRT = function(duties){
			for(var i=0; i<duties.length; i++){
				duties[i][$scope.createRoundTripDutiesFields.isNewRT] = false;
			}
			return duties;
		}
		/**
		 * This function is used to delete duties from new round trip and it will be called from
		 * create-round-trips-chart directive
		 */
		$scope.deleteDutiesAndRTFromChart = function(dd,isRT){
			if(isRT){
				$scope.deletedRoundTripsIdFromChart.push(dd[$scope.createRoundTripDutiesFields.roundTrip]);
				$scope.setRemovedRTAsDuty($scope.deletedRoundTripsIdFromChart,$scope.createRoundTripDutiesFields.roundTrip);
			}else{
				var rt = $scope.newRoundTripsFromChart,isFound = false,dutyIdsToRemove=[],count;
				for(var r=0; r<rt.length; r++){
					for(var d=0; d<rt[r].drivingDuties.length; d++){
						if(rt[r].drivingDuties[d][$scope.createRoundTripDutiesFields.id] == dd[$scope.createRoundTripDutiesFields.id]){
							isFound = true;
							count = 0;
						}
						if((isFound && d==0) || (isFound && count>0)){
							dutyIdsToRemove.push(rt[r].drivingDuties[d][$scope.createRoundTripDutiesFields.id]);
							rt[r].drivingDuties.splice(d,1);
							d--;
						}
						count = (isFound)?count+1:count;
					}
					if(isFound){
						if(rt[r].drivingDuties.length == 0){
							rt.splice(r,1);r--;
						}else{
							rt[r].drivingDuties = $scope.setThisDutiesAsNoNewRT(rt[r].drivingDuties);
						}
						break;
					}
				}
				$scope.setRemovedRTAsDuty(dutyIdsToRemove,$scope.createRoundTripDutiesFields.id);
			}
			$scope.updateCharts();
			$scope.$apply();
		}
		
		/**
		 * This function is used to remove selected duties from chartList data
		 */
		$scope.setRemovedRTAsDuty = function(dutyIdsToRemove,id){
			var std = $scope.createRoundTripsChartList,duty;
			if(dutyIdsToRemove.length>0 || std.length>1){
				for(var s=0; s<std.length; s++){
					for(var d=0 ;d<std[s].data.length; d++){
						duty = std[s].data[d];
						if(dutyIdsToRemove.indexOf(duty[id]) != -1){
							duty[$scope.createRoundTripDutiesFields.isRoundTrip] = 0;
							duty[$scope.createRoundTripDutiesFields.isPartialLink] = false;
							duty[$scope.createRoundTripDutiesFields.partialLinkId] = "";
						}
					}
				}
			}
			$scope.getDeletedRoundTrips();
		}
		
		/**
		 * This function is used to get deleted roundtrips
		 */
		$scope.getDeletedRoundTrips = function(){
			var rtIds = $scope.deletedRoundTripsIdFromChart,isFound;
			var rts = $scope.deletedRoundTripsFromChart;
			for(var id=0; id<rtIds.length; id++){
				isFound = false;
				for(var rt=0; rt<rts.length; rt++){
					if(rtIds[id] == rts[rt][0][$scope.createRoundTripDutiesFields.roundTrip])
						isFound = true;
				}
				if(!isFound){
					$http.get("/api/custom/drivingDuties/list?userPlan="+UserService.getSelectedUserPlan().id+"&roundTrip="+rtIds[id]
							  +"&sort=roundTripOrderNo%20asc&page=0&size="+$scope.noOfDutiesLimit+"&isIgnore=false").then(
								function(drivingDuties){
									$scope.deletedRoundTripsFromChart.push(drivingDuties.data.data);
								});
								
				}
			}
		}
		
		/**
		 * This function is used to remove deleted rt from list
		 */
		$scope.removeRTFromDeletedList = function(rt){
			for(var r=0; r<$scope.deletedRoundTripsFromChart.length; r++){
				if($scope.deletedRoundTripsIdFromChart[r] == rt[0][$scope.createRoundTripDutiesFields.roundTrip])
					$scope.deletedRoundTripsIdFromChart.splice(r,1);
				if($scope.deletedRoundTripsFromChart[r][0][$scope.createRoundTripDutiesFields.roundTrip] == 
					rt[0][$scope.createRoundTripDutiesFields.roundTrip]){
					$scope.deletedRoundTripsFromChart.splice(r,1);break;
				}
			}
			var temp = $scope.createRoundTripsChartList;
			for(var r=0; r<temp.length; r++){
				for(var d=0; d<temp[r].data.length; d++){
					if(temp[r].data[d][$scope.createRoundTripDutiesFields.roundTrip] == 
						rt[0][$scope.createRoundTripDutiesFields.roundTrip]){
						temp[r].data[d][$scope.createRoundTripDutiesFields.isRoundTrip] = 1;
						temp[r].data[d][$scope.createRoundTripDutiesFields.isPartialLink] = false;
						temp[r].data[d][$scope.createRoundTripDutiesFields.partialLinkId] = "";
					}
				}
			}
			$scope.updateCharts();
		}
		
		/**
		 * This function is used to set chart crew type in individual roundtrip
		 */
		$scope.changeAllChartRTCrewTypes = function(){
			var rts = $scope.newRoundTripsFromChart;
			for(var rt=0 ;rt<rts.length; rt++){
				rts[rt].crewType = $scope.crewTypeForGraph;
			}
		}
		
		/**
		 * This function is used to show confirmation box to save many tound trips from chart
		 */
		$scope.saveRoundTripsFromGraph = function(){
			if($scope.deletedRoundTripsIdFromChart.length > 0){
				$confirm({ // Confirm PopUp to Remove fields from
					// DB
					text : 'Round Trips which are removed from chart will be deleted. Are you sure to delete them?',
					headerClass : 'confirm-header-danger',
					okButtonClass : "btn-danger"
				}).then(function() {
					
					$scope.saveManyRoundTripsFromGraph();
				});
			}else{
				$scope.saveManyRoundTripsFromGraph();
			}
		}
		
		/**
		 * This function is used to save many round trips from chart
		 */
		$scope.saveManyRoundTripsFromGraph = function(){
			var rts = $scope.newRoundTripsFromChart,isCrewLink = true,roundTrips= [],temp=[];
			for(var rt=0 ;rt<rts.length; rt++){
				temp = [];
				for(var d=0 ;d<rts[rt].drivingDuties.length; d++){
					temp.push({id : rts[rt].drivingDuties[d][$scope.createRoundTripDutiesFields.id]})
				}
				roundTrips.push({drivingDuties : temp,crewType : rts[rt].crewType});
			}
			rts = roundTrips;
			for(var rt=0 ;rt<rts.length; rt++){
				if(rts[rt].crewType == "" || rts[rt].crewType == undefined || rts[rt].crewType == null)
					isCrewLink = false;
				else
					rts[rt].crewType = rts[rt].crewType._links.self.href
										.substring(rts[rt].crewType._links.self.href.lastIndexOf('/')+1,
												rts[rt].crewType._links.self.href.length);
				if(rts[rt].drivingDuties[0][$scope.createRoundTripDutiesFields.fromStation] != 
					rts[rt].drivingDuties[rts[rt].drivingDuties.length-1][$scope.createRoundTripDutiesFields.toStation]){
					rts.splice(rt,1);
					rt--;
				}
			}
			if(rts.length == 0 || !isCrewLink){
				 toaster.pop({type: 'error', title: 'Error Round Trips', body: 'Select proper round trips!!'});
			}else{
				$scope.deleteRoundTripFromChart();
				$http.post("/api/custom/roundTrips/saveManyList?userPlan="+UserService.getSelectedUserPlan().id,JSON.stringify(roundTrips))
				  .then(function(response){
					  if(response.data.result){
						  toaster
							.pop({
								type : 'success',
								title : 'Saved successfully',
								body : 'Round Trips Saved successfully!!!'
							});
						  $scope.loadCreateRoundTripChart();
					  }else{
						  toaster.pop({type: 'error', title: 'Error Saving', body: 'Error in saving many round trips!!'});
					  }
				  },function(response){
					  toaster.pop({type: 'error', title: 'Error Saving', body: 'Error in saving many round trips!!'});
				  });
			}
		}
		
		/**
		 * This function is used to delete round trips
		 */
		$scope.deleteRoundTripFromChart = function(isDelete){
			if($scope.deletedRoundTripsIdFromChart.length > 0){
				var rtIds = $scope.deletedRoundTripsIdFromChart,count = 0;
				for(var id=0; id<rtIds.length; id++){
					SpringDataRestApi
					.deleteItem("/api/roundTrips/"+ rtIds[id])
					.then(
							function(response) {
								toaster
										.pop({
											type : 'success',
											title : 'RoundTrip Removed',
											body : 'RoundTrip Removed Successfully!!!'
										});
								count++;
							},
							function(response) {
								toaster
										.pop({
											type : 'error',
											title : 'Error',
											body : 'Unable To Remove RoundTrip. Please Try Again!!!'
										});

							});
				}
				if(isDelete){
					$timeout(function(){
						$scope.loadCreateRoundTripChart();
					},rtIds.length*500)
				}
			}
		}
		
});
