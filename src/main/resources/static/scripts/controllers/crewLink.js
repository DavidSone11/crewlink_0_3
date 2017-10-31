'use strict';

/**
 * This CrewLink controller is used to 1. List all the Crew Links from DB 2.
 * List all the Driving Sections of selected Crew Link from DB 3. Remove crew
 * link from DB 4. Creating crew link plan with all the driving sections of
 * selected crew link
 * 
 * @authors Vivek Yadav, Laxman,Santosh
 * @company Mathologic Technologies Pvt. Ltd.
 * @date March 16, 2016
 */

angular
		.module('crewLinkApp')
		.controller(
				'CrewLinkCtrl',
				function($state, $scope, $position, $http, $resource,
						UserService, SpringDataRestAdapter, SpringDataRestApi,
						toaster, $q, $timeout, CSV, $compile,$window) {
					$scope.Days = Days;
					$scope.drivingDutyElementList = [];
					$scope.crewLinkPlan = {};
					$scope.selectedCrewLink = [];
					$scope.crewLink = [];
					$scope.drivingSectionList = {};
					$scope.drivingDutyList = [];
					$scope.crewLinkCSVRecords = [];
					$scope.crewLinksListCSVRecords = [];
					$scope.crewLinks = {
						data : [],
						fields : {}
					};
					$scope.endDayArray = [];
					$scope.arrivalTimes = [];
					$scope.roundTripList = [];
					$scope.drivingDutyObj = {
						_links : {},
						_ref : {}
					};
					$scope.drivingDutyList = [];
					var osRest = 0, hqRest = 0;
					
					$scope.itemsPerPage = 100;
					/*
					 * Server call to fetch list of Crew Links
					 */
					$scope.serverFetch = new ServerTableFetch2(
							"/api/custom/crewLinks/list?userPlan="
									+ UserService.getSelectedUserPlan().id
									+ "&sort=linkName", // Url
							// call
							// that
							// will
							// be
							// made
							// all
							// the
							// time",
							// [
							SpringDataRestApi, function() {// Before processing
								// request
								$scope.isLoading = true;

							}, function(resultObj) {// After processing request
								$scope.crewLinks = resultObj;
								if($scope.crewLinks.data == null){
									$scope.isLoading = false;
									return;
								}
								for(var i=0;i<$scope.crewLinks.data.length; i++){
									$scope.crewLinks.fields.NoOfRTPer30Days = $scope.crewLinks.data[i].length; 
									$scope.crewLinks.data[i].push( $scope.calNoOfRTPer30Days($scope.crewLinks.data[i]) );
									
									$scope.crewLinks.fields.Km30Days = $scope.crewLinks.data[i].length; 
									$scope.crewLinks.data[i].push( $scope.calKm30Days($scope.crewLinks.data[i]) );
									
									$scope.crewLinks.fields.KMPerLPPerDay = $scope.crewLinks.data[i].length; 
									$scope.crewLinks.data[i].push( $scope.calKMPerLPPerDay($scope.crewLinks.data[i]) );
									
									$scope.crewLinks.fields.DutyHrs14Days = $scope.crewLinks.data[i].length; 
									$scope.crewLinks.data[i].push( $scope.calDutyHrs14Days($scope.crewLinks.data[i]) );
									
									$scope.crewLinks.fields.PercentDutyHrs = $scope.crewLinks.data[i].length; 
									$scope.crewLinks.data[i].push( $scope.calPercentDutyHrs($scope.crewLinks.data[i]) );
									
									$scope.crewLinks.fields.PercentOSR = $scope.crewLinks.data[i].length; 
									$scope.crewLinks.data[i].push( $scope.calPercentOSR($scope.crewLinks.data[i]) );
									
									$scope.crewLinks.fields.PercentHQR = $scope.crewLinks.data[i].length; 
									$scope.crewLinks.data[i].push( $scope.calPercentHQR($scope.crewLinks.data[i]) );
								}
								$scope.isLoading = false;
								$scope.loadCrewLinkCompChartSortList();
							});

					/**
					 * This function will be called when user views particular
					 * crew link And used to fetch list of driving sections for
					 * selected crew link
					 */
					$scope.loadCompleteCrewLink = function(link) {
						$scope.isLoadingCrewLink = true;
						$scope.crewLinkMetaData = link;
						$http
								.get(
										"/api/custom/crewLinks/listDrivingSections?userPlan="
												+ UserService
														.getSelectedUserPlan().id
												+ "&crewLink=" + link[0])
								.then(
										function(response) {
											$scope.crewLink = response.data;
											$scope.crewLink.linkName = $scope.crewLinkMetaData[$scope.crewLinks.fields.linkName];
											$scope
													.createCrewLinkPlan($scope.crewLink.data);
											$scope.isLoadingCrewLink = false;
											$scope.selectedCrewLink = link;
											$scope.AverageCalculation(link,
													$scope.crewLinkPlan.length);
											$scope
													.loadcrewlinkChart($scope.crewLink);
											
										});
					}
					$scope.isHeading = function(orderNo) {
						return (orderNo > 0) ? false : true;
					};

					$scope.calNoOfRTPer30Days = function(link) {
						var dutyHrs = link[$scope.crewLinks.fields.duration];
						var osRest = link[$scope.crewLinks.fields.osRest];
						var hqRest = link[$scope.crewLinks.fields.hqRest];
						var totalTime = dutyHrs + osRest + hqRest;
						var days = totalTime / (60 * 24);
						var noOfRT = link[$scope.crewLinks.fields.noOfRoundTrips];
						var noOfRTPerDay = noOfRT / days;
						var noOfRTPer30Days = noOfRTPerDay * 30;
						var rounded = Math.round(noOfRTPer30Days * 10) / 10;
						return rounded;
					}
					$scope.calPercentDutyHrs = function(link) {
						var dutyHrs = link[$scope.crewLinks.fields.duration];
						var osRest = link[$scope.crewLinks.fields.osRest];
						var hqRest = link[$scope.crewLinks.fields.hqRest];
						var totalTime = dutyHrs + osRest + hqRest;
						var dutyPercent = (dutyHrs / totalTime) * 100;
						var rounded = Math.round(dutyPercent * 10) / 10;
						return rounded ;
					}

					$scope.calKMPerLPPerDay = function(link) {
						var dutyHrs = link[$scope.crewLinks.fields.duration];
						var osRest = link[$scope.crewLinks.fields.osRest];
						var hqRest = link[$scope.crewLinks.fields.hqRest];
						var totalKm = link[$scope.crewLinks.fields.distance];
						var LP = link[$scope.crewLinks.fields.locoPilots];

						var kmPerLP = totalKm / LP;
						var kmPerLPPerDay = kmPerLP / 7;
						var rounded = Math.round(kmPerLPPerDay * 10) / 10;
						return rounded;
					}
					$scope.calPercentOSR = function(link) {
						var dutyHrs = link[$scope.crewLinks.fields.duration];
						var osRest = link[$scope.crewLinks.fields.osRest];
						var hqRest = link[$scope.crewLinks.fields.hqRest];
						var totalTime = dutyHrs + osRest + hqRest;
						var osrPercent = (osRest / totalTime) * 100;
						var rounded = Math.round(osrPercent * 10) / 10;
						return rounded ;
					}

					$scope.calPercentHQR = function(link) {
						var dutyHrs = link[$scope.crewLinks.fields.duration];
						var osRest = link[$scope.crewLinks.fields.osRest];
						var hqRest = link[$scope.crewLinks.fields.hqRest];
						var totalTime = dutyHrs + osRest + hqRest;
						var hqrPercent = (hqRest / totalTime) * 100;
						var rounded = Math.round(hqrPercent * 10) / 10;
						return rounded;
					}

					/**
					 * This function is used to calculate duty hours for 14 days
					 * of passed crew link
					 */
					$scope.calDutyHrs14Days = function(link) {
						var dutyHrs = link[$scope.crewLinks.fields.duration];
						var osRest = link[$scope.crewLinks.fields.osRest];
						var hqRest = link[$scope.crewLinks.fields.hqRest];
						var hr = Math
								.floor(((dutyHrs / (dutyHrs + osRest + hqRest)) * (14 * 24 * 60)) / 60);
						var min = Math
								.round(((dutyHrs / (dutyHrs + osRest + hqRest)) * (14 * 24 * 60)) % 60);
						return hr + '.' + min;
					};
					/**
					 * This function is used to calculate KM for 30 days of
					 * passed crew link
					 */
					$scope.calKm30Days = function(link) {
						var dist = link[$scope.crewLinks.fields.distance];
						var dutyHrs = link[$scope.crewLinks.fields.duration];
						var osRest = link[$scope.crewLinks.fields.osRest];
						var hqRest = link[$scope.crewLinks.fields.hqRest];
						var km = Math
								.round((dist / ((dutyHrs + osRest + hqRest) / 60))
										* (30 * 24));
						return km;
					};

					$scope.createCrewLinkPlan = function(crewLink) {
						$scope.crewLinkPlan = [];
						for (var i = 0; i < crewLink.length; i++) {
							var rtOrder = crewLink[i][$scope.crewLink.fields.rtOrder] - 1;
							if ($scope.crewLinkPlan[rtOrder] == null) {
								$scope.crewLinkPlan
										.push({
											rtOrder : rtOrder,
											rtSignOn : crewLink[i][$scope.crewLink.fields.rtSignOn],
											rtSignOnDay : crewLink[i][$scope.crewLink.fields.rtSignOnDay],
											rtSignOff : crewLink[i][$scope.crewLink.fields.rtSignOff],
											rtSignOffDay : crewLink[i][$scope.crewLink.fields.rtSignOffDay],
											rtOSRest : crewLink[i][$scope.crewLink.fields.rtOSRest],
											dd : []
										});
							}
							var ddo = crewLink[i][$scope.crewLink.fields.ddOrder];
							var ddOrder = ddo - 1;
							if ($scope.crewLinkPlan[rtOrder].dd[ddOrder] == null) {
								$scope.crewLinkPlan[rtOrder].dd
										.push({
											ddOrder : ddOrder,
											ddSignOn : crewLink[i][$scope.crewLink.fields.ddSignOn],
											ddSignOnDay : crewLink[i][$scope.crewLink.fields.ddSignOnDay],
											ddDuration : crewLink[i][$scope.crewLink.fields.ddDuration],
											ddSignOff : crewLink[i][$scope.crewLink.fields.ddSignOff],
											ddSignOffDay : crewLink[i][$scope.crewLink.fields.ddSignOffDay],
											dde : []
										});
							}
							var ddeo = crewLink[i][$scope.crewLink.fields.ddeOrder];
							var ddeOrder = ddeo - 1;
							if ($scope.crewLinkPlan[rtOrder].dd[ddOrder].dde[ddeOrder] == null) {
								$scope.crewLinkPlan[rtOrder].dd[ddOrder].dde
										.push({
											ddeOrder : ddeOrder,
											ddeStartTime : crewLink[i][$scope.crewLink.fields.ddeStartTime],
											ddeStartDay : crewLink[i][$scope.crewLink.fields.ddeStartDay],
											ddeEndTime : crewLink[i][$scope.crewLink.fields.ddeEndTime],
											ddeEndDay : crewLink[i][$scope.crewLink.fields.ddeEndDay],
											sp : {
												startPilot : crewLink[i][$scope.crewLink.fields.startPilot],
												spStation : crewLink[i][$scope.crewLink.fields.spStation],
												spToStation : crewLink[i][$scope.crewLink.fields.spToStation],
												spDistance : crewLink[i][$scope.crewLink.fields.spDistance]
											},
											ds : {
												dsFrom : crewLink[i][$scope.crewLink.fields.dsFrom],
												dsTrain : crewLink[i][$scope.crewLink.fields.dsTrain],
												dsTrainOriginDay : crewLink[i][$scope.crewLink.fields.dsTrainOriginDay],
												dsDistance : crewLink[i][$scope.crewLink.fields.dsDistance],
												dsTo : crewLink[i][$scope.crewLink.fields.dsTo]
											},
											ep : {
												endPilot : crewLink[i][$scope.crewLink.fields.endPilot],
												epFromStation : crewLink[i][$scope.crewLink.fields.epFromStation],
												epStation : crewLink[i][$scope.crewLink.fields.epStation],
												epDistance : crewLink[i][$scope.crewLink.fields.epDistance]
											}
										});
							}
						}
						for (var i = 0; i < $scope.crewLinkPlan.length; i++) {
							var from = {
								day : $scope.crewLinkPlan[i].rtSignOffDay,
								time : $scope.crewLinkPlan[i].rtSignOff
							};
							var to = {
								day : $scope.crewLinkPlan[(i + 1)
										% $scope.crewLinkPlan.length].rtSignOnDay,
								time : $scope.crewLinkPlan[(i + 1)
										% $scope.crewLinkPlan.length].rtSignOn
							};

							var hqRest = diffTimeObj(from, to, "min");
							$scope.crewLinkPlan[i].hqRest = hqRest;
						}
						return $scope.crewLinkPlan;
					};
					$scope.crewlinkDetails = {};
					$scope.AverageCalculation = function(linkobj,
							roundTripsCount) {

						$scope.Totalcalculation = [];
						$scope.crewlinkDetails.linkname = linkobj[1];
						$scope.crewlinkDetails.NoOfLpp = linkobj[2];
						$scope.crewlinkDetails.NoOfLPPer30Days = $scope
								.calNoOfRTPer30Days(linkobj);
						$scope.crewlinkDetails.KMPerLPPerDay = $scope
								.calKMPerLPPerDay(linkobj);
						$scope.crewlinkDetails.KMPer30Days = $scope
								.calKm30Days(linkobj);
						$scope.crewlinkDetails.DutyHrs14Days = $scope
								.calDutyHrs14Days(linkobj);
						$scope.crewlinkDetails.PercentDutyHrs = $scope
								.calPercentDutyHrs(linkobj);
						$scope.crewlinkDetails.PercentOSR = $scope
								.calPercentOSR(linkobj);
						$scope.crewlinkDetails.PercentHQR = $scope
								.calPercentHQR(linkobj);

						$scope.crewlinkDetails.averageworkinghoursPerWeek = $scope
								.averageworkinghoursPerWeek(linkobj).toFixed(2);
						$scope.crewlinkDetails.AverageKmsPermonth = $scope
								.AverageKmsPermonth(linkobj).toFixed(2);
						$scope.crewlinkDetails.averageOSRest = (linkobj[$scope.crewLinks.fields.osRest] / roundTripsCount)
								.toFixed(2);
						$scope.crewlinkDetails.averageHQRest = (linkobj[$scope.crewLinks.fields.hqRest] / roundTripsCount)
								.toFixed(2);
						$scope.Totalcalculation.push($scope.crewlinkDetails);
						return $scope.crewlinkDetails;

					}
					$scope.averageworkinghoursPerWeek = function(linkobj) {
						var totalduration = linkobj[3] / 60;
						var noOfWeeks = 4;
						var noofLPP = linkobj[2];
						var averageWorkinghour = totalduration * noOfWeeks;
						var totalaverageworkinghour = totalduration / noofLPP;
						return totalaverageworkinghour;
					}

					$scope.AverageKmsPermonth = function(linkobj) {
						// ("dad",linkobj);
						var totaldistance = linkobj[11];
						var totalAverageKm = totaldistance * parseFloat(4.3)
								/ linkobj[2];
						return totalAverageKm;
					}

					/**
					 * This function is used to remove crew link
					 */
					$scope.removeCrewLink = function(crewLinkId) {
						SpringDataRestApi
								.deleteItem(
										$scope.crewLinks.selectionDetails.baseItemRestUri
												+ crewLinkId)
								.then(
										function(response) {
											for (var i = 0; i < $scope.crewLinks.data.length; i++) {
												if ($scope.crewLinks.data[i][$scope.crewLinks.fields.id] == crewLinkId)
													$scope.crewLinks.data
															.splice(i, 1);// Remove
												// deleted
												// crew
												// link
												// from
												// crewlinks
												// list
											}
											toaster
													.pop({
														type : 'success',
														title : 'Crew Link Removed',
														body : 'Crew Link Removed Successfully!!!'
													});
										},
										function(response) {
											toaster
													.pop({
														type : 'error',
														title : 'Error',
														body : 'Unable To Remove Crew Link . Please Try Again!!!'
													});

										});
					};
					$scope.loadCrewLinksComp = function(){
						$scope.isLoadCrewLinksComp = true;
						$scope.reloadCrewLinksComp = true;
					}
					
					$scope.postLoadingCrewLinksComp = function(res){
						var result = [
						              	['x'],
						              	['No Of RT / 30Days'],
						              	['KM/30Days'],
						              	['KM/LP/Day'],
						              	['Duty Hrs/14Days'],
						              	['% Duty Hrs'],
						              	['% OSR'],
						              	['% HQR']
						             ];
						var hideFields = [];
						if($scope.crewLinkCompChartSortBy){
							for(var i=0 ;i <result.length; i++){
								if($scope.crewLinkCompChartSortBy.name.indexOf(result[i][0]) == -1){
									hideFields.push(result[i][0]);
								}
							}
						}
						var resultObj = {
								data: {
								        x: 'x',
								        columns: result,
								        hide : hideFields,
								        type: 'bar'
								    },
								    axis: {
				        		        x: {
				        		            type: 'category' // this needed to load string x value
				        		        }
				        		    },
				        		    bar: {
				        		        width: {
				        		            ratio: 0.8 // this makes bar width 50% of length between ticks
				        		        }
				        		    },
				        		    size: {
				        		    	  height: $window.height-100
				        		    	},
				    		        legend: {
				    		            position: 'right'
				    		        }
							};
						
						for(var i=0; i<res.data.length; i++){
							result[0].push(res.data[i][res.fields.linkName]);
							result[1].push(parseInt(res.data[i][res.fields.NoOfRTPer30Days]));
							result[2].push(parseInt(res.data[i][res.fields.Km30Days]));
							result[3].push(parseInt(res.data[i][res.fields.KMPerLPPerDay]));
							result[4].push(parseInt(res.data[i][res.fields.DutyHrs14Days]));
							result[5].push(parseInt(res.data[i][res.fields.PercentDutyHrs]));
							result[6].push(parseInt(res.data[i][res.fields.PercentOSR]));
							result[7].push(parseInt(res.data[i][res.fields.PercentHQR]));
						}
						
						return resultObj;
					}
					
					/**
					 * This function is used to initialize crew link analysis sort list
					 */
					$scope.loadCrewLinkCompChartSortList = function(){
						$scope.crewLinkCompChartSortBy = "";
						$scope.crewLinkCompChartSortList = [
						                                   {id:$scope.crewLinks.fields.NoOfRTPer30Days , name : 'No Of RT / 30Days'},
						                                   {id:$scope.crewLinks.fields.Km30Days , name : 'KM/30Days'},
						                                   {id:$scope.crewLinks.fields.KMPerLPPerDay , name : 'KM/LP/Day'},
						                                   {id:$scope.crewLinks.fields.DutyHrs14Days , name : 'Duty Hrs/14Days'},
						                                   {id:$scope.crewLinks.fields.PercentDutyHrs , name : '% Duty Hrs'},
						                                   {id:$scope.crewLinks.fields.PercentOSR , name : '% OSR'},
						                                   {id:$scope.crewLinks.fields.PercentHQR , name : '% HQR'},
						                                   ];
						$scope.crewLinkCompChartSortOrder = true;
					}
					
					/**
					 * This function is used to sort crew link Complysis chart
					 */
					$scope.sortCrewLinkCompChart = function(){
						$scope.crewLinks.data = quickSort($scope.crewLinks.data,"","",$scope.crewLinkCompChartSortBy.id,$scope.crewLinkCompChartSortOrder);
					}
					
					$scope.toggleCrewLinkCompChartSortOrder = function(ele){
						$scope.crewLinkCompChartSortOrder = !$scope.crewLinkCompChartSortOrder;
						if($scope.crewLinkCompChartSortOrder){
							ele.target.className = "fa fa-sort-amount-desc";
						}else{
							ele.target.className = "fa fa-sort-amount-asc"
						}
						$scope.sortCrewLinkCompChart();
					}

					/***********************************************************
					 * PDF generator
					 */

					$scope.getPdf = function(plan, sectionsCount, link) {
						// getImageFromUrl('/assets/img/logo.png',createPdf);
						// var tableValues = generateTableForPdfMake();
						var values = $scope.getCrewLinkCSVRecords(plan,
								sectionsCount, link, false);
						values.unshift($scope.getCrewLinkCSVHeader());
						var widths = genrateWidths(values);
						var summary = getSummaryValues(plan, link);

						var docDefinition = {
							// a string or { width: number, height: number }
							pageSize : 'A2',

							// by default we use portrait, you can
							// change it to landscape if you wish
							pageOrientation : 'landscape',

							// [left, top, right, bottom] or
							// [horizontal, vertical] or just a number
							// for equal margins
							pageMargins : [ 5, 5, 5, 5 ],
							content : [ {
								table : {
									headerRows : 1,
									widths : [ '*' ],

									body : summary
								}
							}, {
								table : {
									headerRows : 1,
									widths : widths,

									body : values
								}
							} ]
						};
						// open the PDF in a new window
						pdfMake.createPdf(docDefinition).open();

					}
					var getSummaryValues = function(plan, link) {
						var crewlinkDetails = $scope.AverageCalculation(link,
								plan.length);
						var summary = [];
						summary.push([ "CREW LINK :  "
								+ crewlinkDetails.linkname + "  ("
								+ crewlinkDetails.NoOfLpp + " MEN)" ]);
						summary.push([ "No.of loco pilot in the link : "
								+ crewlinkDetails.NoOfLpp + " Men" ]);
						summary.push([ "No. of RT per 30 Days : "
								+ crewlinkDetails.NoOfLPPer30Days ]);
						summary.push([ "KM per LP per Day : "
								+ crewlinkDetails.KMPerLPPerDay.toString() ]);
						summary.push([ "KM per 30 Days : "
								+ crewlinkDetails.KMPer30Days ]);
						summary.push([ "Duty Hrs per 14 Days : "
								+ crewlinkDetails.DutyHrs14Days ]);
						summary.push([ "Percent Duty Hrs : "
								+ crewlinkDetails.PercentDutyHrs ]);
						summary.push([ "Percent OSR : "
								+ crewlinkDetails.PercentOSR ]);
						summary.push([ "Percent HQR : "
								+ crewlinkDetails.PercentHQR ]);
						summary
								.push([ "Average working hours per LP per week : "
										+ crewlinkDetails.averageworkinghoursPerWeek
										+ " Hours" ]);
						summary
								.push([ "Average Kms.perLP per month : "
										+ crewlinkDetails.AverageKmsPermonth
										+ " Kms" ]);
						summary
								.push([ "Average OS Rest : "
										+ minutesToHours(crewlinkDetails.averageOSRest) ]);
						summary
								.push([ "Average HQ Rest : "
										+ minutesToHours(crewlinkDetails.averageHQRest) ]);
						return summary;
					}

					var genrateWidths = function(values) {
						var result = [];
						if (values.length > 0) {
							for (var row = 0; row < values[0].length; row++) {
								result.push('auto');
							}
						}
						return result;
					};

					/***********************************************************
					 * END of pdf generator
					 */

					// The following function is used to load the Crewlink
					// spiral Chart
					$scope.loadcrewlinkChart = function() {
						$scope.crewlink = [];
						for (var i = 0; i < $scope.crewLink.data.length; i++) {
							var rtOrder = $scope.crewLink.data[i][$scope.crewLink.fields.rtOrder] - 1;
							if ($scope.crewlink[rtOrder] == null) {
								$scope.crewlink
										.push({
											rtOrderNo : rtOrder,
											rtSignOn : $scope.crewLink.data[i][$scope.crewLink.fields.rtSignOn],
											rtSignOnDay : $scope.crewLink.data[i][$scope.crewLink.fields.rtSignOnDay],
											rtSignOff : $scope.crewLink.data[i][$scope.crewLink.fields.rtSignOff],
											rtSignOffDay : $scope.crewLink.data[i][$scope.crewLink.fields.rtSignOffDay],
											rtOSRest : $scope.crewLink.data[i][$scope.crewLink.fields.rtOSRest],
											dd : []
										});
							}
							var ddOrder = $scope.crewLink.data[i][$scope.crewLink.fields.ddOrder] - 1;
							if ($scope.crewlink[rtOrder].dd[ddOrder] == null) {
								$scope.crewlink[rtOrder].dd
										.push({
											ddOrder : ddOrder,
											ddSignOn : $scope.crewLink.data[i][$scope.crewLink.fields.ddSignOn],
											ddSignOnDay : $scope.crewLink.data[i][$scope.crewLink.fields.ddSignOnDay],
											ddDuration : $scope.crewLink.data[i][$scope.crewLink.fields.ddDuration],
											ddSignOff : $scope.crewLink.data[i][$scope.crewLink.fields.ddSignOff],
											ddSignOffDay : $scope.crewLink.data[i][$scope.crewLink.fields.ddSignOffDay],
											dde : []
										});
							}
						}

						var mar = {
							top : 20,
							left : 20,
							bottom : 20,
							right : 20
						};

						var width = 500 - mar.left - mar.right;
						var height = 530 - mar.top - mar.bottom;

						var num_axes = 8;
						var tick_axis = 1;
						var start = 0;
						var end = 2;

						var div = d3.select("body").append("div").attr("class",
								"tooltip").style("opacity", 0);

						var theta = function(r) {
							return -2 * Math.PI * r;
						};

						var arc = d3.svg.arc().startAngle(0).endAngle(
								2 * Math.PI);

						var radius = d3.scale
								.linear()
								.domain([ start, end ])
								.range(
										[ 0, d3.min([ width, height ]) / 2 - 20 ]);

						var angle = d3.scale.linear().domain([ 0, num_axes ])
								.range([ 0, 360 ])

						var svg = d3.select("#spiralCrewlinkChart").append(
								"svg").attr("width", width).attr("height",
								height).append("g").attr(
								"transform",
								"translate(" + width / 2 + ","
										+ (height / 2 + 8) + ")");

						var pieces = d3.range(start, end + 0.001,
								(end - start) / 1000);

						var spiral = d3.svg.line.radial().interpolate(
								"cardinal").angle(theta).radius(radius);

						clearall();
						svg.append("text").text("Chart for CrewLink").attr(
								"class", "title").attr("x", 0).attr("y",
								-height / 2 + 16).attr("text-anchor", "middle")

						svg.selectAll("circle.tick").data(
								d3.range(end, start, (start - end) / 4))
								.enter().append("circle").attr("class", "tick")
								.attr("cx", 0).attr("cy", 0).attr("r",
										function(d) {
											return radius(d);
										})

						svg.selectAll("circle.tick").on("mouseover", mouseover)
								.on("mouseout", mouseout);

						svg.selectAll(".axis").data(d3.range(num_axes)).enter()
								.append("g").attr("class", "axis").attr(
										"transform", function(d) {
											return "rotate(" + -angle(d) + ")";
										}).call(radial_tick).append("text")
								.attr("y", radius(end) + 13).text(function(d) {
									return angle(d) + "°";
								}).attr("text-anchor", "middle").attr(
										"transform", function(d) {
											return "rotate(" + -90 + ")"
										})

						svg.selectAll(".spiral").data([ pieces ]).enter()
								.append("path").attr("class", "spiral").attr(
										"d", spiral).attr("transform",
										function(d) {
											return "rotate(" + 90 + ")"
										});

						function radial_tick(selection) {
							selection.each(function(axis_num) {
								d3.svg.axis().scale(radius).ticks(5)
										.tickValues(
												axis_num == tick_axis ? null
														: []).orient("bottom")(
												d3.select(this))

								d3.select(this).selectAll("text").attr(
										"text-anchor", "bottom").attr(
										"transform",
										"rotate(" + angle(axis_num) + ")")
							});
						}

						function mouseout(d) {
							div.transition().duration(200).style("opacity", 0);

							// p.classed("active", false);
							// projection.classed("inactive", false);
							// ordinal_labels.classed("inactive", false);
						}

						function mouseover(d) {
							var Title = "DrivingSection";
							var dep = "12:00:00";
							var arr = "17:00:00";
							var dis = "350";
							var trainNo = "11014";
							div.transition().duration(200).style("opacity", 1);
							div.html(

									"<h4>Driving Sections</h4>"
											+ "<h5>Train No</h5>" + "<b>"
											+ trainNo + "</b>"
											+ "<h5>Distance</h5>" + "<b>" + dis
											+ "</b>" + "<h5>Departure</h5>"
											+ "<b>" + dep + "</b>"
											+ "<h5>Arrival</h5>" + "<b>" + arr
											+ "</b>"

							).style("left", (d3.event.pageX - 11) + "px")
									.style("top", (d3.event.pageY - 74) + "px");
							;
							// p.classed("active", false);
							// projection.classed("inactive", false);
							// ordinal_labels.classed("inactive", false);
						}

						// clear the SVG during reload
						function clearall() {
							svg.selectAll(".circle.tick").remove();
							svg.selectAll(".axis").remove();
							svg.selectAll(".spiral").remove();
						}

					}; // end of function

					/**
					 * This function is used to return header for csv file
					 */
					$scope.getCrewLinkCSVHeader = function() {
						return [ "RT S.No", "RT Sign On Time",
								"RT Sign On Day", "DD S.No", "DD Sign On Time",
								"DD Sign On Day", "DD Duration", "DDE S.No",
								"DDE Dep Time", "DDE Dep Day", "Start Pilot",
								"SP From Station","SP To Station", "SP Dist(Half)", 
								"DS Train","DS From","DS To", "DS TRain Origin Day", "DS Dist",
								 "End Pilot", "EP From Station","EP To Station",
								"EP Dist(Half)", "DDE Arr Time", "DDE Arr Day",
								"DD Sign Off Time", "DD Sign Off Day",
								"RT Sign Off Time", "RT Sign Off Day",
								"RT OS Rest", "HQ Rest" ];
					}

					/**
					 * This function is used to return records for csv file
					 */
					$scope.getCrewLinkCSVRecords = function(crewLinkPlan,
							sectionsCount, crewLink, isSummary) {
						var crewlinkDetails = $scope.AverageCalculation(
								crewLink, crewLinkPlan.length);
						$scope.crewLinkCSVRecords = [];
						$scope.crewLinkCSVFields = {
							RTSNo : 0,
							RTSignOnTime : 1,
							RTSignOnDay : 2,
							DDSNo : 3,
							DDSignOnTime : 4,
							DDSignOnDay : 5,
							DDDuration : 6,
							DDESNo : 7,
							DDEDepTime : 8,
							DDEDepDay : 9,
							StartPilot : 10,
							SPFromStation : 11,
							SPToStation : 12,
							SPDistHalf : 13,
							DSTrain : 14,
							DSFrom : 15,
							DSTo : 16,
							DSTRainOriginDay : 17,
							DSDist : 18,
							EndPilot : 19,
							EPFromStation : 20,
							EPToStation : 21,
							EPDistHalf : 22,
							DDEArrTime : 23,
							DDEArrDay : 24,
							DDSignOffTime : 25,
							DDSignOffDay : 26,
							RTSignOffTime : 27,
							RTSignOffDay : 28,
							RTOSRest : 29,
							HQRest : 30
						}
						var limit = (isSummary) ? sectionsCount + 15
								: sectionsCount;
						for (var i = 0; i < limit; i++) {
							$scope.crewLinkCSVRecords.push([ "", "", "", "",
									"", "", "", "", "", "", "", "", "", "", "",
									"", "", "", "", "", "", "", "", "", "", "",
									"", "", "", "", "" ]);
						}
						var roundTrip, duty, dutyEle, section, endPilot, startPilot;
						var index = 0;
						for (var rt = 0; rt < crewLinkPlan.length; rt++) {
							roundTrip = crewLinkPlan[rt];
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.RTSNo] = (rt + 1)
									+ "";
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.RTSignOnTime] = roundTrip.rtSignOn;
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.RTSignOnDay] = $scope.Days[roundTrip.rtSignOnDay];
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.RTSignOffTime] = roundTrip.rtSignOff;
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.RTSignOffDay] = $scope.Days[roundTrip.rtSignOffDay];
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.RTOSRest] = minutesToHours(roundTrip.rtOSRest);
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.HQRest] = minutesToHours(roundTrip.hqRest);

							for (var dd = 0; dd < roundTrip.dd.length; dd++) {
								duty = roundTrip.dd[dd];
								$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSNo] = (dd + 1)
										+ "";
								$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnTime] = duty.ddSignOn;
								$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnDay] = $scope.Days[duty.ddSignOnDay];
								$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDDuration] = minutesToHours(duty.ddDuration);
								$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOffTime] = duty.ddSignOff;
								$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOffDay] = $scope.Days[duty.ddSignOffDay];

								for (var dde = 0; dde < duty.dde.length; dde++) {
									dutyEle = duty.dde[dde];
									$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDESNo] = (dde + 1)
											+ "";
									$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDEDepTime] = dutyEle.ddeStartTime;
									$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDEDepDay] = $scope.Days[dutyEle.ddeStartDay];
									$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDEArrTime] = dutyEle.ddeEndTime;
									$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDEArrDay] = $scope.Days[dutyEle.ddeEndDay];

									startPilot = dutyEle.sp;
									if (startPilot.spDistance) {

										$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.SPDistHalf] = ""+startPilot.spDistance;
										$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.SPFromStation] = startPilot.spStation;
										$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.SPToStation] = startPilot.spToStation;
										$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.StartPilot] = startPilot.startPilot;
									}
									section = dutyEle.ds;
									if (section.dsFrom) {
										$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DSFrom] = section.dsFrom;
										$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DSTrain] = section.dsTrain
												+ "";
										$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DSTRainOriginDay] = $scope.Days[section.dsTrainOriginDay];
										$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DSDist] = section.dsDistance
												+ "";
										$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DSTo] = section.dsTo;
									}

									endPilot = dutyEle.ep;
									if (endPilot.endPilot) {
										$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.EndPilot] = endPilot.endPilot;
										$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.EPFromStation] = endPilot.epFromStation;
										$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.EPToStation] = endPilot.epStation;
										$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.EPDistHalf] = ""+endPilot.epDistance;
									}
									index++;
								}
							}

						}
						if (isSummary) {
							index += 2;
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnTime] = "PROPOSED "
									+ crewlinkDetails.linkname;
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnDay] = "("
									+ crewlinkDetails.NoOfLpp + " MEN)";
							index++;

							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnTime] = "No. of RT per 30 Days : ";
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnDay] = ""+crewlinkDetails.NoOfLPPer30Days;
							index++;

							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnTime] = "KM per LP per Day : ";
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnDay] = ""+crewlinkDetails.KMPerLPPerDay;
							index++;

							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnTime] = "KM per 30 Days : ";
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnDay] = ""+crewlinkDetails.KMPer30Days;
							index++;

							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnTime] = "Duty Hrs per 14 Days : ";
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnDay] = ""+crewlinkDetails.DutyHrs14Days;
							index++;

							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnTime] = "Percent Duty Hrs : ";
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnDay] = ""+crewlinkDetails.PercentDutyHrs;
							index++;

							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnTime] = "Percent OSR : ";
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnDay] = crewlinkDetails.PercentOSR;
							index++;

							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnTime] = "Percent HQR : ";
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnDay] = crewlinkDetails.PercentHQR;
							index++

							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnTime] = "No.of loco pilot in the link";
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnDay] = crewlinkDetails.NoOfLpp
									+ " Men";
							index++;
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnTime] = "Average working hours per LP per week";
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnDay] = crewlinkDetails.averageworkinghoursPerWeek
									+ " Hours";
							index++;
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnTime] = "Average Kms.perLP per month";
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnDay] = crewlinkDetails.AverageKmsPermonth
									+ " Kms";
							index++;
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnTime] = "Average OS Rest";
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnDay] = minutesToHours(crewlinkDetails.averageOSRest);
							index++;
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnTime] = "Average HQ Rest";
							$scope.crewLinkCSVRecords[index][$scope.crewLinkCSVFields.DDSignOnDay] = minutesToHours(crewlinkDetails.averageHQRest);
						}
						return $scope.crewLinkCSVRecords;
					}

					/**
					 * This function is used to return header for list of
					 * crewlinks csv
					 */
					$scope.getCrewLinksListCSVHeader = function() {
						return [ "S.No", "Link Name", "Loco Pilots",
								"Base Station", "No Of RT/30D", "Total KM",
								"KM/LP/D", "KM/30D", "Duty Hrs/14Days",
								"%Duty Hrs", "%OS Rest", "%HQ Rest" ]
					}

					/**
					 * This function is used to return list of crew link records
					 * for list of crewlinks csv
					 */
					$scope.getCrewLinksListCSVRecords = function() {
						$scope.crewLinksListCSVFields = {
							SNo : 0,
							LinkName : 1,
							LocoPilots : 2,
							BaseStation : 3,
							NoOfRTPer30Days : 4,
							TotalKM : 5,
							KMPerLPPerDay : 6,
							KMPer30Days : 7,
							DutyHrs14Days : 8,
							PercentDutyHrs : 9,
							PercentOSR : 10,
							PercentHQR : 11
						};
						for (var i = 0; i < $scope.crewLinks.data.length; i++) {
							$scope.crewLinksListCSVRecords.push([ "", "", "",
									"", "", "", "", "", "", "", "", "" ]);
						}
						var crewLink;
						for (var cl = 0; cl < $scope.crewLinks.data.length; cl++) {
							crewLink = $scope.crewLinks.data[cl];
							$scope.crewLinksListCSVRecords[cl][$scope.crewLinksListCSVFields.SNo] = cl + 1;
							$scope.crewLinksListCSVRecords[cl][$scope.crewLinksListCSVFields.LinkName] = crewLink[$scope.crewLinks.fields.linkName];
							$scope.crewLinksListCSVRecords[cl][$scope.crewLinksListCSVFields.LocoPilots] = crewLink[$scope.crewLinks.fields.locoPilots];
							$scope.crewLinksListCSVRecords[cl][$scope.crewLinksListCSVFields.BaseStation] = crewLink[$scope.crewLinks.fields.station];
							$scope.crewLinksListCSVRecords[cl][$scope.crewLinksListCSVFields.NoOfRTPer30Days] = $scope.calNoOfRTPer30Days(crewLink);
							$scope.crewLinksListCSVRecords[cl][$scope.crewLinksListCSVFields.TotalKM] = crewLink[$scope.crewLinks.fields.distance];
							$scope.crewLinksListCSVRecords[cl][$scope.crewLinksListCSVFields.KMPerLPPerDay] = $scope.calKMPerLPPerDay(crewLink);
							$scope.crewLinksListCSVRecords[cl][$scope.crewLinksListCSVFields.KMPer30Days] = $scope
							.calKm30Days(crewLink);
							$scope.crewLinksListCSVRecords[cl][$scope.crewLinksListCSVFields.DutyHrs14Days] = $scope
									.calDutyHrs14Days(crewLink);
							$scope.crewLinksListCSVRecords[cl][$scope.crewLinksListCSVFields.KM30Days] = parseInt($scope
									.calKm30Days(crewLink));
							$scope.crewLinksListCSVRecords[cl][$scope.crewLinksListCSVFields.PercentDutyHrs] = $scope.calPercentDutyHrs(crewLink);
							$scope.crewLinksListCSVRecords[cl][$scope.crewLinksListCSVFields.PercentOSR] = $scope.calPercentOSR(crewLink);
							$scope.crewLinksListCSVRecords[cl][$scope.crewLinksListCSVFields.PercentHQR] = $scope.calPercentHQR(crewLink);
						}
						return $scope.crewLinksListCSVRecords;
					}

					/**
					 * This function is used to export all crew links to csv
					 */
					$scope.exportAllLinksToCSV = function() {
						var link, records, header, fileName, options = {}, plan;
						angular
								.forEach(
										$scope.crewLinks.data,
										function(link, index) {
											$http
													.get(
															"/api/custom/crewLinks/listDrivingSections?userPlan="
																	+ UserService
																			.getSelectedUserPlan().id
																	+ "&crewLink="
																	+ link[0])
													.then(
															function(response) {
																$scope.crewLink = response.data;
																plan = $scope
																		.createCrewLinkPlan($scope.crewLink.data);
																records = $scope
																		.getCrewLinkCSVRecords(
																				plan,
																				$scope.crewLink.data.length,
																				link,
																				true);
																options.header = $scope
																		.getCrewLinkCSVHeader();
																fileName = (index + 1)
																		+ $scope.crewlinkDetails.linkname
																		+ "-Summary.csv";
																CSV
																		.stringify(
																				records,
																				options)
																		.then(
																				function(
																						result) {
																					console
																							.log(result);
																					var encodedUri = encodeURI("data:text/csv;charset=utf-8,"
																							+ result);
																					var link = document
																							.createElement("a");
																					link
																							.setAttribute(
																									"href",
																									encodedUri);
																					link
																							.setAttribute(
																									"download",
																									fileName);
																					link
																							.click();
																				});
															});
										});
						/*
						 * //inject CSV service in your controller
						 * $scope.getArray = [{a: 1, b:2}, {a:3, b:4}];
						 * $scope.options = {}; $scope.options.header = ['Field
						 * A', 'Field B'];
						 * 
						 * $scope.send_email = function() {
						 * CSV.stringify($scope.getArray,
						 * $scope.options).then(function(result){
						 * (result); }); }
						 */
					}

					$scope.loadCrewLinkAnalysisChart = function(link) {
						$scope.isCrewLinkAnalysisChartLoading = true;
						if(link)
							$scope.loadCompleteCrewLink(link);
						var time = ($scope.crewLinkMetaData[$scope.crewLinks.fields.departureDay]*1440);
						$scope.crewLink.noOfWeeks = Math
								.ceil((
										time+
										$scope.crewLinkMetaData[$scope.crewLinks.fields.duration] +
										$scope.crewLinkMetaData[$scope.crewLinks.fields.osRest] + 
										$scope.crewLinkMetaData[$scope.crewLinks.fields.hqRest]
										) / 10080);
						
						$scope.isCrewLinkAnalysisChartLoading = false;
					}
				});
