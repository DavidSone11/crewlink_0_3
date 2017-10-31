angular.module('crewLinkApp')
.directive('mathoChart', ['$compile', function($compile) {
    return {
        restrict: 'E',
        scope: { 
        	chartType: '@',
        	reload: '=reload',
        	updateWait: '=',
        	serverLoad: '=',
        	serverLoadModel : '=',
        	serverPreLoad: '=',
        	serverPostLoad: '=',
        	serverDataType: '@',
        	serverDataFields: '@',
        	noDataProcessing: '@'
        },
        templateUrl: 'scripts/directives/mathoChart/mathoChart.tmpl.html',
        controller: function($scope,$timeout,$q) {
        	console.debug("In MathoChart Controller");
        	console.debug("Scope Values: ");
        	console.debug($scope);
        	var d = new Date();
        	var n = d.getTime();
        	$scope.chartId = "mathoChartId"+n;
        	var timeoutPromiseServerLoadModel;
        	var timeoutPromiseReload;
        	$scope.dataFieldsSeperator = ';';
        	$scope.dataTypes = {'array':0,'arrayFields':1,'arrayJson':2};
        	$scope.chartTypes = {'line':0,'bar':1,'multiLine':2,'multiBar':3};
        	if($scope.reload == null){
        		$scope.reload = true;
        	}
        	if(!$scope.serverDataType){
        		$scope.serverDataType = 'array';
        	}else if(!$scope.dataTypes[$scope.serverDataType]){
        		$scope.serverDataType = 'array';
        		throw new Error("Server Data Type not valid : "+ $scope.serverDataType + "\n Please choose from : "+JSON.stringify($scope.dataTypes));	
        	}
        	if($scope.serverDataFields){
        		$scope.dataFields = $scope.serverDataFields.split(',');
        	}
        	$scope.serverData = [];
        	
        	$scope.$watch("serverLoadModel", function(update){
        		$timeout.cancel(timeoutPromiseServerLoadModel);
        		timeoutPromiseServerLoadModel = $timeout(function(){
        			console.debug("Model Change detected");
        			$scope.callDataFetch(update);
        		},$scope.updateWait);
        	},true);
        	
        	$scope.$watch("reload", function(update){
        		console.debug("Reloading graph");
        		$timeout.cancel(timeoutPromiseServerLoadModel);
        		timeoutPromiseServerLoadModel = $timeout(function(){
        			console.debug("Model Change detected");
        			$scope.callDataFetch($scope.serverLoadModel);
        		},$scope.updateWait);	
        	},true);
        	
        	$scope.reDrawChart = function(chartType){
        		var selectedType = $scope.chartTypes[chartType];
        		if(!$scope.serverData || $scope.serverData.length == 0){
        			$scope.isData = false;
        			return;
        		}
        		else{
        			$scope.isData = true;
        		}
        		switch(selectedType){
        		case $scope.chartTypes.c3:
        			$scope.reDrawC3Chart();
        			break;
        		case $scope.chartTypes.line:
        			$scope.reDrawLineChart();
        			break;
        		case $scope.chartTypes.bar:
        			$scope.reDrawBarChart();
        			break;
        		case $scope.chartTypes.multiLine:
        			$scope.reDrawMultiLineChart();
        			break;
        		case $scope.chartTypes.multiBar:
        			$scope.reDrawMultiBarChart();
        			break;
        		default:
        			throw new Error("Server Data Type not valid : "+ chartType + "\n Please choose from : "+JSON.stringify($scope.chartTypes));
        			break;
        		}
        	};
        	
        	$scope.processServerResponse = function(response,dataType,noDataProcessing){
        		if(!noDataProcessing){
	        		var selectedType = $scope.dataTypes[dataType];
	        		switch(selectedType){
	        		case $scope.dataTypes.array:
	        			$scope.serverData = $scope.processArrayDataForChart(response,$scope.chartType);
	        			break;
	        		case $scope.dataTypes.arrayFields:
	        			$scope.serverData = $scope.processArrayFieldsDataForChart(response,$scope.chartType);
	        			break;
	        		case $scope.dataTypes.arrayJson:
	        			$scope.serverData = $scope.processArrayJsonDataForChart(response,$scope.chartType);
	        			break;
	        		default:
	        			throw new Error("Server Data Type not valid : "+ dataType + "\n Please choose from : "+JSON.stringify($scope.dataTypes));
	        			break;
	        		}
        		}else{
        			$scope.serverData = response;
        		}
        		$scope.reDrawChart($scope.chartType);
        	};
        	
        	$scope.processArrayDataForChart = function(res,chartType){
        		if(res.status != 200){
        			$scope.serverData = res;
        		}else{
        			$scope.serverData = res.data;
        		}
        		return $scope.convertArrayToSelectedData($scope.serverData);
        	};
        	
        	$scope.convertArrayToSelectedData = function(data){
        		var result = [];
        		if(!data || data.length == 0){
        			$scope.isData = false;
        			return result;
        		}
        		else{
        			$scope.isData = true;
        		}
        		var selectedFields = $scope.serverDataFields.split(',');
        		for(var row=0; row<data.length; row++){
        			var rowArray = {};
        			
	        		for(var i=0; i<selectedFields.length; i++){
	        			rowArray[i] = data[row][parseInt(selectedFields[i])];
	        		}
	        		result.push(rowArray);
        		}
        		return result;
        	};
        	
        	$scope.processArrayFieldsDataForChart = function(res,chartType){
        		if(res.status != 200){
        			$scope.serverData = res;
        		}else{
        			$scope.serverData = res.data;
        		}
        		return $scope.convertArrayFieldsToData($scope.serverData);
        	};
        	
        	$scope.convertArrayFieldsToData = function(data){
        		var result = [];
        		if(!data || !data.data || data.data.length == 0){
        			$scope.isData = false;
        			return result;
        		}
        		else{
        			$scope.isData = true;
        		}
        		var selectedFields = $scope.serverDataFields.split($scope.dataFieldsSeperator);
        		for(var row=0; row<data.data.length; row++){
        			var rowArray = {};
	        		for(var i=0; i<selectedFields.length; i++){
	        			rowArray[i] = data.data[row][data.fields[selectedFields[i]]];
	        			rowArray[selectedFields[i]] = rowArray[i];
	        		}
	        		result.push(rowArray);
        		}
        		return result;
        	};
        	
        	$scope.processArrayJsonDataForChart = function(res,chartType){
        		if(res.status != 200){
        			$scope.serverData = res;
        		}else{
        			$scope.serverData = res.data;
        		}
        		return $scope.convertArrayJsonToData($scope.serverData);
        	};
        	
        	$scope.convertArrayJsonToData = function(data){
        		var result = [];
        		if(!data || data.length == 0){
        			$scope.isData = false;
        			return result;
        		}
        		else{
        			$scope.isData = true;
        		}
        		var selectedFields = $scope.serverDataFields.split($scope.dataFieldsSeperator);
        		for(var row=0; row<data.length; row++){
        			var rowArray = {};
	        		for(var i=0; i<selectedFields.length; i++){
	        			rowArray[i] = data[row][selectedFields[i]];
	        			rowArray[selectedFields[i]] = rowArray[i];
	        		}
	        		result.push(rowArray);
        		}
        		return result;
        	};
        	
        	$scope.callDataFetch = function(data){
        		$scope.isLoading = true;
        		$scope.isError = false;
        		$scope.isData = false;
        		$scope.fetchData($scope.serverLoadModel).then(function(res){
    				console.debug("Data loaded Successfully");
    				console.debug(res);
    				$scope.processServerResponse(res,$scope.serverDataType,$scope.noDataProcessing);
    				
    				$scope.isLoading = false;
    				$scope.isError = false;
    			},function(err){
    				console.debug("Error in Fetching Data");
    				$scope.isError = true;
    				$scope.isLoading = false;
    			});
        	};
        	
        	$scope.fetchData = function(data){
        		var defered = $q.defer();
        		if($scope.serverPreLoad != null){
	        		var serverPreLoadPromise = $scope.serverPreLoad();
	        		if(serverPreLoadPromise.then != null){
	        			serverPreLoadPromise.then(function(res){
	        				$scope.fetchDataMain(res).then(function(res){
	        					defered.resolve(res);
	            			},function(err){
		        				console.debug("Error in Server Load");
		        				defered.reject(err);
		        			});
	        			},function(err){
	        				console.debug("Error in Server Pre Load");
	        				defered.reject(err);
	        			});
	        		} 
	        		else{
	        			data = $scope.serverPreLoad();
	        			$scope.fetchDataMain(data).then(function(res){
	        				defered.resolve(res);
	        			},function(err){
	        				console.debug("Error in Server Load");
	        				defered.reject(err);
	        			});
	        		}
        		}else{
        			$scope.fetchDataMain(data).then(function(res){
        				defered.resolve(res);
        			},function(err){
        				console.debug("Error in Server Load");
        				defered.reject(err);
        			});
        		}
        		return defered.promise;
        	};
        	
        	$scope.fetchDataMain = function(data){
        		var defered = $q.defer();
        		if($scope.serverLoad != null){
	        		var serverLoadPromise = $scope.serverLoad(data);
					if(serverLoadPromise.then != null){
						serverLoadPromise.then(function(res){
							$scope.fetchDataPostProcess(res).then(function(res){
								defered.resolve(res);
							},function(err){
								console.debug("Error in Server Post Load");
							});
						},function(err){
							console.debug("Error in Server Load");
							defered.reject(err);
						});
					}
					else{
						data = $scope.serverLoad(data);
						$scope.fetchDataPostProcess(data).then(function(res){
							defered.resolve(res);
						},function(err){
							console.debug("Error in Server Post Load");
						});
					}
        		}
        		else{
        			$scope.fetchDataPostProcess(data).then(function(res){
						defered.resolve(res);
					},function(err){
						console.debug("Error in Server Post Load");
					});
        		}
				return defered.promise;
        	};
        	
        	$scope.fetchDataPostProcess = function(data){
        		var defered = $q.defer();
        		if($scope.serverPostLoad != null){
        			var serverPostLoadPromise = $scope.serverPostLoad(data);
					if(serverPostLoadPromise.then != null){
						serverPostLoadPromise.then(function(res){
							defered.resolve(res);
						},function(err){
							console.debug("Error in Server Post Load");
							defered.reject(err);
						});
					}
					else{
						data = $scope.serverPostLoad(data);
						defered.resolve(data);
					}
        		}
        		else{
        			defered.resolve(data);
        		}
				return defered.promise;
        	}
        	
        	$scope.reDrawMultiBarChart = function(){
        		$scope.chart = c3.generate({
        		    data: {
        		    	x : 'x',
        		        columns: $scope.serverData,
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
        		        // or
        		        //width: 100 // this makes bar width 100px
        		    },
        		    size: {
        		    	  height: 800
        		    	},
    		        legend: {
    		            position: 'right'
    		        },
    		        zoom: {
    		        	enabled: true
        			},subchart: {
                	        show: true
            	    },
        		    bindto: "#"+$scope.chartId
        		});
        		setTimeout(function () {
        			$scope.chart.resize();
        		}, 10);
        	};
        	
        	$scope.reDrawC3Chart = function(){
        		$scope.serverData.bindto=  "#"+$scope.chartId;
        		$scope.serverData.zoom= {
        			  enabled: true
        			};
        		$scope.serverData.subchart= {
        	        show: true
        	    };
        		$scope.chart = c3.generate($scope.serverData);
        		setTimeout(function () {
        			$scope.chart.resize();
        		}, 10);
        		
        	};
        	
        	$scope.reDrawBarChart = function(){
        		var vis = d3.select('#'+$scope.chartId),
        	    WIDTH = $scope.width||2000,
        	    HEIGHT = $scope.height||500,
        	    MARGINS = $scope.margin||{
        	      top: ($scope.margin)?$scope.margin.top||20:20,
        	      right: ($scope.margin)?$scope.margin.right||20:20,
        	      bottom: ($scope.margin)?$scope.margin.bottom||70:70,
        	      left: ($scope.margin)?$scope.margin.left||50:50
        	    },
        	    xRange = d3.scale.ordinal().rangeRoundBands([MARGINS.left, WIDTH - MARGINS.right], 0.1).domain($scope.serverData.map(function (d) {
        	      return d[0];
        	    })),
        	    yRange = d3.scale.linear()
        	    	.range([HEIGHT - MARGINS.top, MARGINS.bottom])
        	    	.domain([0,d3.max($scope.serverData, function (d) {
        	    		return d[1];
        	    		})
    	    		]),
    	    		xAxis = d3.svg.axis()
    	    	      .scale(xRange)
    	    	      .tickSize(5)
    	    	      .tickSubdivide(false)
    	    	      .tickFormat(d3.time.format("(%a %H:%M)")),

    	    	    yAxis = d3.svg.axis()
    	    	      .scale(yRange)
    	    	      .tickSize(5)
    	    	      .orient("left")
    	    	      .tickSubdivide(true);


    	    	  vis.append('svg:g')
    	    	    .attr('class', 'x axis')
    	    	    .attr('transform', 'translate(0,' + (HEIGHT - MARGINS.bottom) + ')')
    	    	    .call(xAxis)
    	    	    .selectAll("text")  
						.style("text-anchor", "end")
						.attr("dx", "-.8em")
						.attr("dy", ".15em")
						.attr("transform", "rotate(-65)" );

    	    	  vis.append('svg:g')
    	    	    .attr('class', 'y axis')
    	    	    .attr('transform', 'translate(' + (MARGINS.left) + ',0)')
    	    	    .call(yAxis);

    	    	  vis.selectAll('rect')
    	    	    .data($scope.serverData)
    	    	    .enter()
    	    	    .append('rect')
    	    	    .attr('x', function (d) {
    	    	      return xRange(d[0]);
    	    	    })
    	    	    .attr('y', function (d) {
    	    	      return yRange(d[1]);
    	    	    })
    	    	    .attr('width', xRange.rangeBand())
    	    	    .attr('height', function (d) {
    	    	      return ((HEIGHT - MARGINS.bottom) - yRange(d[1]));
    	    	    })
    	    	    .attr('fill', 'grey');
        	}
        },
        linker : function(scope,element,attr){
        	("In MathoChart Linker");
        	console.debug("Scope Values: ");
        	console.debug(scope);
        }
    }
}]);