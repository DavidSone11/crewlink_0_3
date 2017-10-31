
'use strict';
var app = angular.module('crewLinkApp');

app.controller('DrivingSectionChartCtrl',function($scope,ChartService ,$q, $state, $http, $resource, $filter,$timeout) {
					
			
	var d = new Date();
    var time = d.getTime();
    $scope.chartId = "createDrivingSectionChart" + time;
    
    
    
    $scope.initializeValues = function(){
    	$scope.margin = {top:50,left:10,bottom:40,right:10};
    	$scope.width = $scope.divWidth - $scope.margin.left - $scope.margin.right;
    	$scope.height = (700 - 1) * 50 - $scope.margin.top - $scope.margin.bottom;
        $scope.finalWidth = $scope.width + $scope.margin.left + $scope.margin.right;
        $scope.finalHeight = $scope.height + $scope.margin.top + $scope.margin.bottom + 100;
        $scope.dimensionsYAxis = d3.scale.linear().range([0, $scope.height]).domain([0, 700 - 1]);
        $scope.legendXValue = $scope.divWidth * 0.75;
        $scope.legendYValue = $scope.dimensionsYAxis(2);
        $scope.legendCircleRadious = 7;
        
    }
    
    
    $scope.reloadChart = function () {
        $timeout(function () {
                $scope.divWidth = document.getElementById($scope.chartId).offsetWidth;
                $scope.drivingSectionChart();
            
        }, ($scope.divWidth) ? 0 : 500);
    };
    $scope.reloadChart();
    
    $scope.drivingSectionChart = function () {
    	
    	
    	
    	$scope.initializeValues();
    	$scope.graph = d3.select("#" + $scope.chartId).append("svg").attr("width", $scope.finalWidth)
        .attr("height", $scope.finalHeight).append("g")
        .attr("transform", "translate(" + $scope.margin.left + "," + $scope.margin.top + ")");
    	
    	ChartService.drawYAxis($scope.graph, "", "y-axis", $scope.yAxisXValue, 'left', $scope.height, 0,
                function () {$scope.createCircleForNewStation(this)},
                function (d) {$scope.mouseOverYAxis(d) }, function (d) { $scope.mouseOutTick(d) });


    	
    }
    
    $scope.mouseOverYAxis = function (d) {
        ChartService.showAndGetToolTip($scope.tooltipId).innerHTML =
            (($scope.newCircleCounter == 0) ? "click me to<br>add new Change Station" :
                "Deselect previous new Change Station to<br>add another new Change Station");
    }
	
    $scope.mouseOutTick = function (d) {
        ChartService.hideToolTip($scope.tooltipId);
    }
    
    $scope.getTrainNo = function(query, timeout){
    
    	console.log(query);
    	var url = "/api/custom/drivingsectionChart/getTrainNo?trainNo="+query+"&size="+10+"&page="+1+"&sort="+"";
    	$http.get().then(function (response){
    		console.log(response);
    	});
    	
    	
    }
    $scope.selectedTrainNo = function(){
    	
    	
    	
    	
    }
								
				
				
				});