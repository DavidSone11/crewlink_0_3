angular.module('crewLinkApp')
    .directive('createRoundTripChart', ['$compile', function($compile) {
        return {
            restrict: 'E',
            scope: { duties: '='},
            templateUrl: 'scripts/directives/createRoundTrip/createRoundTripChart.tmpl.html',
            controller: function($scope, $state,$window, $location,$timeout,ChartService) {
            	$scope.Days = Days;
            	$scope.id = $scope.duties.id;
                $scope.dutiesData = $scope.duties.data;
                $scope.dutiesFields = $scope.duties.fields;
                $scope.baseStation = $scope.duties.baseStation;
                $scope.chartId  = "createRoundTripChart"+$scope.id;
                $scope.tooltipId = "createRoundTripsToolTip"+$scope.id;
                /**
                 * This function is used to call generateDrivingDutiesChart function 
                 * after 500sec because div id is generating dynamically
                 */
                $timeout(function(){
                    $scope.divWidth = 850;
                    $scope.generateDrivingDutiesChart();
                },100);

                $scope.initializeValues = function(){
                    $scope.margin={top:50,left:10,bottom:40,right:10};
                    $scope.width=$scope.divWidth-$scope.margin.left-$scope.margin.right;
                    $scope.finalWidth=$scope.width+$scope.margin.left+$scope.margin.right;
                    $scope.noOfTicks = 168;
                    $scope.height=5040-$scope.margin.top-$scope.margin.bottom;
                    $scope.finalHeight=$scope.height+$scope.margin.top+$scope.margin.bottom+100;
                    $scope.newLineStroke = d3.svg.line().x(function(d,i){return d.x}).y(function(d){return d.y});
                    $scope.toggleDuties = $scope.duties.legends.isDuties;
                    $scope.toggleRT = $scope.duties.legends.isRTs;
                    $scope.arrAxisXValue = $scope.divWidth*0.30;
                    $scope.depAxisXValue = $scope.divWidth*0.70;
                    $scope.depTickCircleXValue = $scope.depAxisXValue-3;
                    $scope.arrTickCircleXValue = $scope.arrAxisXValue+3;
                    $scope.tickLineArrAxisXValue = $scope.arrAxisXValue;
                    $scope.tickLineDepAxisXValue = $scope.depAxisXValue;
                    $scope.detailsArrAxisXValue = $scope.arrAxisXValue-170;
                    $scope.detailsDepAxisXValue = $scope.depAxisXValue+10;
                    $scope.fromStationsXValue = $scope.detailsArrAxisXValue-65;
                    $scope.toStationsXValue = $scope.detailsDepAxisXValue+190;
                    $scope.fromStationsRectXValue = $scope.detailsArrAxisXValue-15;
                    $scope.toStationsRectXValue = $scope.detailsDepAxisXValue+175;
                    $scope.rectWidth = 10;
                    $scope.rectHeight = 10;
                    $scope.tickCircleId = "tickCircle";
                    $scope.tickCircleRadious = 5;
                    $scope.toggleAllFromStations = $scope.duties.stations.toggleAllFromStations;
                    $scope.toggleAllToStations = $scope.duties.stations.toggleAllToStations;
                    $scope.toStations = [];
                    $scope.fromStations = [];
                    $scope.firstTick = {};
                    $scope.secondTick = {};
                    $scope.tickLineWidth = 2;
                    $scope.dimensionsYAxis=d3.scale.linear().range([0,$scope.height]).domain([0,$scope.noOfTicks]);
                    $scope.legendXValue = $scope.divWidth*0.47;
                    $scope.legendYValue = $scope.dimensionsYAxis(0)-20;
                    $scope.legendCircleRadious = 7;
                    ChartService.removeElementById($scope.tooltipId);
                    var div = d3.select("body").append("div")	
    			    .attr("class", "tooltip")
    			    .attr("id", $scope.tooltipId)	
    			    .style("opacity", 0);
                    $scope.selectedFromStations = [];
                	$scope.selectedToStations = [];
                    if($scope.duties.stations.fromStations.length == 0){
                    	$scope.setFromAndToStations(true);
                    	$scope.setFromAndToStations(false);
                    }else{
                    	$scope.selectedFromStations = $scope.duties.stations.fromStations;
                    	$scope.selectedToStations = $scope.duties.stations.toStations;
                    }
                }
                
                /**
                 * This function is used to get all from and to stations
                 */
                $scope.setFromAndToStations = function(isFrom){
                	
                	var fromAndToStations = ChartService.separateFromAndToStations($scope.duties.data,$scope.baseStation,
                			$scope.dutiesFields.toStation,$scope.dutiesFields.fromStation);
                	if(isFrom){
                		if($scope.toggleAllFromStations)$scope.duties.stations.fromStations = fromAndToStations.fromStations;
                		else $scope.duties.stations.fromStations = [];
                		$scope.selectedFromStations = $scope.duties.stations.fromStations;
                	}else{
                		if($scope.toggleAllToStations)$scope.duties.stations.toStations = fromAndToStations.toStations
                    	else $scope.duties.stations.toStations = [];
                		$scope.selectedToStations = $scope.duties.stations.toStations;
                	}
                }


                /**
                 * Function to load chart in div
                 * @param station
                 */
                 $scope.generateDrivingDutiesChart = function(){
                    
                    $scope.initializeValues();
                    $scope.applySelectedStationsFilter();
                    $scope.applyLegendFilters();
                	$scope.loadChart();
                }
                 
	            /**
	             * This function is used to apply selected stations filter
	             */
                $scope.applySelectedStationsFilter = function(){
                	var temp = [];
                	for(var d=0; d<$scope.duties.data.length; d++){
                		if($scope.duties.data[d][$scope.dutiesFields.fromStation] == $scope.baseStation &&
                				($scope.selectedToStations.indexOf($scope.duties.data[d][$scope.dutiesFields.toStation]) != -1)){
                			temp.push($scope.duties.data[d]);
                		}else if($scope.duties.data[d][$scope.dutiesFields.toStation] == $scope.baseStation &&
                				$scope.selectedFromStations.indexOf($scope.duties.data[d][$scope.dutiesFields.fromStation]) != -1){
                			temp.push($scope.duties.data[d]);
                		}
                			
                	}
                	$scope.dutiesData = temp;
                }
                 
                /**
                 * This function is used to apply legend filters
                 */
                $scope.applyLegendFilters = function(){
                 	var temp = [];
                 	if($scope.toggleDuties && $scope.toggleRT)
             			temp = $scope.dutiesData;
             		else{
 	                	for(var d=0; d<$scope.dutiesData.length; d++){
 	                		if($scope.toggleDuties && $scope.dutiesData[d][$scope.dutiesFields.isRoundTrip] != 1)
 	                			temp.push($scope.dutiesData[d]);
 	                		else if($scope.toggleRT && $scope.dutiesData[d][$scope.dutiesFields.isRoundTrip] == 1)
 	                			temp.push($scope.dutiesData[d]);
 	                	}
             		}
                 	$scope.dutiesData = temp;
                 }
                 
                $scope.loadChart = function(){
                	document.getElementById($scope.chartId).innerHTML = "";
                    $scope.graph=d3.select("#"+$scope.chartId).append("svg").attr("width",$scope.finalWidth)
                    .attr("height",$scope.finalHeight).append("g")
                    .attr("transform", "translate(" + $scope.margin.left + "," + $scope.margin.top + ")");
                
	                //graph object to draw,id of axis,clas of axis,x position on x axis,side of ticks,
	                //height of axis,ticks on axis,click,mouseOver,museOut,dblclick function,no of days
	                ChartService.drawYAxis($scope.graph,"","y-axis",$scope.arrAxisXValue,'right',$scope.height,$scope.noOfTicks,"","","","",7);
	                ChartService.drawYAxis($scope.graph,"","y-axis",$scope.depAxisXValue,'left',$scope.height,$scope.noOfTicks,"","","","",7);
	                
	                //graph d3 object,text to display,x axis position,y axis position,class for text
	                ChartService.addYAxisHeading($scope.graph,"ARR-"+$scope.baseStation,$scope.arrAxisXValue-20,
	                    function(d){return $scope.dimensionsYAxis(0)-20},"headingtext");
	                ChartService.addYAxisHeading($scope.graph,"DEP-"+$scope.baseStation,$scope.depAxisXValue-20,
	                    function(d){return $scope.dimensionsYAxis(0)-20},"headingtext");
	                
	                //duties,baseStation,toStation index,fromStation index
	                var fromAndToStations = ChartService.separateFromAndToStations($scope.duties.data,$scope.baseStation,$scope.dutiesFields.toStation,
	                    $scope.dutiesFields.fromStation);
	                $scope.fromStations = fromAndToStations.fromStations;
	                $scope.toStations = fromAndToStations.toStations;
	                
	                $scope.fromStations = ChartService.generateStationsTickValues($scope.noOfTicks,$scope.fromStations);
	                $scope.toStations = ChartService.generateStationsTickValues($scope.noOfTicks,$scope.toStations);
	                
	                //graph d3 object,data array,divClass class name,id text id,textClass class name,x axis position,
	                //y axis position,text to display,click function,
	                //mouseOver function,mouseOut function
	                ChartService.addContentOnYAxis($scope.graph,$scope.fromStations,'fromStations',
	                	function(d){return 'fromStation'+$scope.id+d["id"];},'fromStation active',
	                    $scope.fromStationsXValue,function(d){return $scope.dimensionsYAxis(d['tickVal'])},
	                    function(d){return "<<"+d['station']},function(d){$scope.$parent.loadCreateRoundTripChart(d["station"],false,$scope.duties)},
	                    function(d){$scope.mouseOverStation(d)},function(d){$scope.mouseOutTick()});
	                ChartService.addContentOnYAxis($scope.graph,$scope.toStations,'toStations',
	                	function(d){return 'toStation'+$scope.id+d["id"];},'toStation active',
	                    $scope.toStationsXValue,function(d){return $scope.dimensionsYAxis(d['tickVal'])},
	                    function(d){return d['station']+">>"},function(d){$scope.$parent.loadCreateRoundTripChart(d["station"],true,$scope.duties)},
	                    function(d){$scope.mouseOverStation(d)},function(d){$scope.mouseOutTick()});
	                //graph d3 object,rectClass class name,rectId circle id,x axis position,y Axis position
	                //width,height,fillColor rect color,strokeWidth,strokeColor,click function,mouseOver function,mouseOut function,
	                //dblCclick function
	                ChartService.generateRectangle($scope.graph,"allFromStationsRect","allFromStationsRect"+$scope.id
	                		,$scope.fromStationsRectXValue,$scope.dimensionsYAxis(0)-30,$scope.rectWidth,$scope.rectHeight,
                            function(){return (($scope.toggleAllFromStations)?"blue":"white")},"black",2,
                            function(d){$scope.toggleAllSelectedStations(this,true)},function(d){$scope.mouseOverAllStationsRect(d)},
                    		function(d){$scope.mouseOutTick()},"")
                    ChartService.generateRectangle($scope.graph,"allToStationsRect","allToStationsRect"+$scope.id
	                		,$scope.toStationsRectXValue,$scope.dimensionsYAxis(0)-30,$scope.rectWidth,$scope.rectHeight,
                            function(){return (($scope.toggleAllToStations)?"blue":"white")},"black",2,
                            function(d){$scope.toggleAllSelectedStations(this,false)},function(d){$scope.mouseOverAllStationsRect(d)},
                    		function(d){$scope.mouseOutTick()},"")
	                //graph d3 object,data array,divClass class name,rectClass class name,rectId circle id,x axis position,y Axis position
	                //width,height,fillColor rect color,strokeWidth,strokeColor,click function,mouseOver function,mouseOut function,
	                //dblCclick function
	                ChartService.generateRectangles($scope.graph,$scope.fromStations,'fromStationRects','fromStationRect',
                		function(d){return 'fromStationRect'+$scope.id+d[$scope.dutiesFields.id];},$scope.fromStationsRectXValue,
                		function(d){return $scope.dimensionsYAxis(d['tickVal'])-10},$scope.rectWidth,$scope.rectHeight,
                		function(d){return (($scope.selectedFromStations.indexOf(d["station"]) == -1)?"white":"blue")},"black",2,
                		function(d){$scope.toggleSelectedStations(d,this,true)},function(d){$scope.mouseOverStationRect(d)},
                		function(d){$scope.mouseOutTick()},"");
	                ChartService.generateRectangles($scope.graph,$scope.toStations,'toStationRects','toStationRect',
                		function(d){return 'toStationRect'+$scope.id+d[$scope.dutiesFields.id];},$scope.toStationsRectXValue,
                		function(d){return $scope.dimensionsYAxis(d['tickVal'])-10},$scope.rectWidth,$scope.rectHeight,
                		function(d){return (($scope.selectedToStations.indexOf(d["station"]) == -1)?"white":"blue")},"black",2,
                		function(d){$scope.toggleSelectedStations(d,this,false)},function(d){$scope.mouseOverStationRect(d)},
                		function(d){$scope.mouseOutTick()},"");
	                    
	                //graph d3 object,data array,divClass class name,circleClass class name,id circle id,x axis position,
	                //yAxis object,tickKey to find out tick,r circle radious,color circle color
	                //dblClick function,,mouseOver function,mouseOut function
	                ChartService.generateCircles($scope.graph,$scope.dutiesData,"tickCircles","tickCircle"+$scope.id,
	                	function(d) {return $scope.tickCircleId+$scope.id+d[$scope.dutiesFields.id];},
	                    function(d){return $scope.getXPositionToAddCircleAndDetailsOnTick(d,true)},
	                    function(d){return $scope.getYPositionToAddCircleAndDetailsOnTick(d);},
	                    $scope.tickCircleRadious,function(d){return (d[$scope.dutiesFields.isRoundTrip] == 1)?"green":"red";},
	                    function(d){$scope.circleClick(d,this);},
	                    function(d){$scope.mouseOverTick(d)},function(d){$scope.mouseOutTick(d)});
	                $scope.generateConnectedLinks($scope.dutiesData);
	                
	                //graph d3 object,data array,divClass class name,id text id,textClass class name,x axis position,
	                //y axis position,text to display,click function,
	                //mouseOver function,mouseOut function
	                ChartService.addContentOnYAxis($scope.graph,$scope.dutiesData,'tickDetailsDiv',
	                	function(d){return 'tickDetails'+$scope.id+d[$scope.dutiesFields.id];},
	                    'tickDetails',function(d){return $scope.getXPositionToAddCircleAndDetailsOnTick(d,false);},
	                    function(d){return $scope.getYPositionToAddCircleAndDetailsOnTick(d);},
	                    function(d){
	                        var details; 
	                        if(d[$scope.dutiesFields.fromStation] == $scope.baseStation){
	                            details = ["("+d[$scope.dutiesFields.duration]+"m)",d[$scope.dutiesFields.ddName],
	                                        d[$scope.dutiesFields.departureTime],d[$scope.dutiesFields.toStation]];
	                        }else if(d[$scope.dutiesFields.toStation] == $scope.baseStation){
	                            details = [d[$scope.dutiesFields.fromStation],d[$scope.dutiesFields.ddName],
	                                        d[$scope.dutiesFields.arrivalTime],"("+d[$scope.dutiesFields.duration]+"m)"];
	                        }
	                        return details;
	                    },
	                    function(d){$scope.circleClick(d,document.getElementById($scope.tickCircleId+$scope.id+d[$scope.dutiesFields.id]));},
	                    function(d){$scope.mouseOverTick(d)},function(d){$scope.mouseOutTick(d)},"",
	                    function(d){return (($scope.duties.duties.previousDuty[$scope.dutiesFields.id] == d[$scope.dutiesFields.id])?"red":"")});
	                $scope.addLegends();
	            }
                
                
                /**
                 * Function to get position of X to add circle and details on tick
                 * @param data
                 * @param isCircle decides for circle or details
                 * @returns {Number} x position
                 */
                $scope.getXPositionToAddCircleAndDetailsOnTick = function(data,isCircle){
                    var x = null;
                    if(data[$scope.dutiesFields.fromStation] == $scope.baseStation){
                        if(isCircle)
                            x = $scope.depTickCircleXValue;
                        else
                            x = $scope.detailsDepAxisXValue;
                    }else if(data[$scope.dutiesFields.toStation] == $scope.baseStation){
                        if(isCircle)
                            x = $scope.arrTickCircleXValue;
                        else
                            x = $scope.detailsArrAxisXValue;
                    }
                    return x;
                }

                /**
                 * Function to get position of Y to add circle on tick
                 */
                $scope.getYPositionToAddCircleAndDetailsOnTick = function(data){
                    var time,day,tickVal = null;
                    if(data[$scope.dutiesFields.fromStation] == $scope.baseStation){
                        time = data[$scope.dutiesFields.departureTime];
                        day = data[$scope.dutiesFields.departureDay];
                    }else if(data[$scope.dutiesFields.toStation] == $scope.baseStation){
                        time = data[$scope.dutiesFields.arrivalTime];
                        day = data[$scope.dutiesFields.arrivalDay];
                    }
                    tickVal = ChartService.getTickValue(time,day,$scope.isDailyTrains);
                    return (tickVal != null)?$scope.dimensionsYAxis(tickVal):null;
                }

                /**
                 * This function is used to draw lines between connected links
                 * @param data
                 */
                $scope.generateConnectedLinks = function(data){
                    
                    var count = 0,time,day,tickVal1,tickVal2,x1,x2 = null,dimensions,lineId,circleClass;
                    for(var duty1 = 0; duty1<data.length; duty1++){
                        for(var duty = duty1; duty<data.length; duty++){
                            if(((data[duty1][$scope.dutiesFields.isRoundTrip] == 1 && 
                                data[duty][$scope.dutiesFields.isRoundTrip] == 1 &&
                                data[duty1][$scope.dutiesFields.roundTrip] == data[duty][$scope.dutiesFields.roundTrip]) ||
                                (data[duty1][$scope.dutiesFields.isPartialLink] && data[duty][$scope.dutiesFields.isPartialLink] &&
                                data[duty1][$scope.dutiesFields.partialLinkId] == data[duty][$scope.dutiesFields.partialLinkId])) &&
                                duty1 != duty &&
                                data[duty1][$scope.dutiesFields.fromStation] != data[duty][$scope.dutiesFields.fromStation] && 
                                data[duty1][$scope.dutiesFields.toStation] != data[duty][$scope.dutiesFields.toStation]){
                                if(data[duty][$scope.dutiesFields.fromStation] == $scope.baseStation){
                                    time = data[duty][$scope.dutiesFields.departureTime];
                                    day = data[duty][$scope.dutiesFields.departureDay];
                                    tickVal2= ChartService.getTickValue(time,day,false);
                                }else if(data[duty][$scope.dutiesFields.toStation] == $scope.baseStation){
                                    time = data[duty][$scope.dutiesFields.arrivalTime];
                                    day = data[duty][$scope.dutiesFields.arrivalDay];
                                    tickVal1= ChartService.getTickValue(time,day,false);
                                }
                                if(data[duty1][$scope.dutiesFields.fromStation] == $scope.baseStation){
                                    time = data[duty1][$scope.dutiesFields.departureTime];
                                    day = data[duty1][$scope.dutiesFields.departureDay];
                                    tickVal2= ChartService.getTickValue(time,day,false);
                                }else if(data[duty1][$scope.dutiesFields.toStation] == $scope.baseStation){
                                    time = data[duty1][$scope.dutiesFields.arrivalTime];
                                    day = data[duty1][$scope.dutiesFields.arrivalDay];
                                    tickVal1= ChartService.getTickValue(time,day,false);
                                }
                                if(tickVal1 && tickVal2)
                                    dimensions=[{x:$scope.arrAxisXValue,y:$scope.dimensionsYAxis(tickVal1)+3},
                                                {x:$scope.depAxisXValue,y:$scope.dimensionsYAxis(tickVal2)-3}];
                                lineId = "existingConnectedLines"+$scope.id+data[duty1][$scope.dutiesFields.id];
                                circleClass = 'existingConnectedCircle'+$scope.id+data[duty1][$scope.dutiesFields.id];
                                var color = (data[duty1][$scope.dutiesFields.isPartialLink])?"blue":"green";
                                var strokeWidth = (data[duty1][$scope.dutiesFields.isNewRT])?3:$scope.tickLineWidth;
	                            $scope.generateLines($scope.graph,[data[duty]],"existingConnectedLines"+$scope.id,circleClass,lineId,
	                                $scope.newLineStroke(dimensions),color,strokeWidth,"","",function(d){$scope.mouseOverTick(d)},
	                                function(d){$scope.mouseOutTick(d)},data[duty],data[duty1]);
                                
                                $scope.generateCircle($scope.graph,circleClass,circleClass,$scope.arrTickCircleXValue,
                                    $scope.dimensionsYAxis(tickVal1),color,$scope.tickCircleRadious,"",
                                    function(d){$scope.mouseOverExtraCircle(d)},function(d){$scope.mouseOutTick(d)},"",
                                    data[duty],data[duty1]);
                                $scope.generateCircle($scope.graph,circleClass,circleClass,$scope.depTickCircleXValue,
                                    $scope.dimensionsYAxis(tickVal2),color,$scope.tickCircleRadious,"",
                                    function(d){$scope.mouseOverExtraCircle(d)},function(d){$scope.mouseOutTick(d)},"",
                                    data[duty],data[duty1]);
                            }
                        }
                    }
                    
                }
                $scope.generateCircle = function(graph,id,clas,x,y,color,tickCircleRadious,dblclick,mouseOver,mouseOut,click,duty1,duty2){
                    //graph d3 object,id circle id,clas circle class,x position,y position,color to fill circle,
                    //tickCircleRadious circle radious,click on dblclick function,mouseOver on mouseOver function,
                    //mouseOut on mouseOut function
                    ChartService.generateCircle(graph,id,clas,x,y,color,tickCircleRadious,
                                    function(){$scope.deleteConnectedLine(clas,duty1,duty2)},
                                    mouseOver,mouseOut);
                }


                $scope.generateLines = function(graph,data,divClass,lineClass,lineId,dimensions,stroke,strokeWidth,
                            click,dblClick,mouseOver,mouseOut,duty1,duty2){
                        //graph d3 object,data array,divClass class name,lineClass class name,id line Id,counter id counter,
                        //dimensions line dimensions,stroke color,strokeWidth,dblClick function,click function,
                        //mouseOver function,mouseOut function
                        ChartService.generateLines(graph,data,divClass,lineClass,lineId,dimensions,stroke,strokeWidth,"",
                                    function(){$scope.deleteConnectedLine(lineClass,duty1,duty2);},mouseOver,mouseOut);   
                }
                /**
                 * Function to generate new connections with user selected ticks
                 * @param d
                 * @param i
                 */
                $scope.circleClick = function(d,ele) {
                    if ($scope.firstTick && $scope.secondTick) {
                        $scope.firstTick = '';
                        $scope.secondTick = '';
                        }
                    if ((!$scope.firstTick || $scope.firstTick === '') && ele.attributes.fill.nodeValue != "blue") {
                            $scope.firstTick = {id:ele.id,dimensions:[ele.attributes.cx.nodeValue,ele.attributes.cy.nodeValue],data : d};
                            ele.setAttribute("fill", "blue");
                    } 
                    else if (($scope.secondTick != 'undefined' || $scope.secondTick === '')) {
                        if($scope.firstTick.id == ele.id || 
                                (((d[$scope.dutiesFields.fromStation] == $scope.baseStation && 
                                    $scope.firstTick.data[$scope.dutiesFields.fromStation] == $scope.baseStation) ||
                                    (d[$scope.dutiesFields.toStation] == $scope.baseStation && 
                                    $scope.firstTick.data[$scope.dutiesFields.toStation] == $scope.baseStation))) ||
                                    $scope.firstTick.data[$scope.dutiesFields.toStation] != d[$scope.dutiesFields.fromStation]){
                            document.getElementById($scope.firstTick.id).setAttribute("fill", "red");
                            $scope.firstTick = "";
                        }
                        else{
                            $scope.secondTick = {id:ele.id,dimensions:[ele.attributes.cx.nodeValue,ele.attributes.cy.nodeValue], data :d}
                            ele.setAttribute("fill", "blue");
                        }
                    }
                    if ($scope.firstTick && $scope.secondTick) {
                                $scope.firstTick.data[$scope.dutiesFields.isPartialLink] = true;
                                $scope.secondTick.data[$scope.dutiesFields.isPartialLink] = true;
                                $scope.firstTick.data[$scope.dutiesFields.partialLinkId] = $scope.firstTick.data[$scope.dutiesFields.id];
                                $scope.secondTick.data[$scope.dutiesFields.partialLinkId] = $scope.firstTick.data[$scope.dutiesFields.id]
                                var dimensions=[{x:$scope.firstTick.dimensions[0],y:$scope.firstTick.dimensions[1]},
                                                {x:$scope.secondTick.dimensions[0],y:$scope.secondTick.dimensions[1]}];
                                var lineId ="newConnectedLine"+$scope.id+$scope.firstTick.data[$scope.dutiesFields.id];
                                var crossMarkId ="newConnectedCircle"+$scope.id+$scope.firstTick.data[$scope.dutiesFields.id];
                                var strokeWidth = ($scope.firstTick.data[$scope.dutiesFields.fromStation] == 
                                		$scope.secondTick.data[$scope.dutiesFields.toStation])?3:$scope.tickLineWidth;
                                var firstTick1 = $scope.firstTick;
                                var secondTick1 = $scope.secondTick;
                                
                                //graph d3 object,data array,divClass class name,lineClass class name,id line Id,
                                //dimensions line dimensions,stroke color,strokeWidth,dblClick function,click function,
                                //mouseOver function,mouseOut function
                                ChartService.generateLines($scope.graph,[$scope.firstTick.data],"newConnectedLines"+$scope.id,crossMarkId,lineId,
                                    $scope.newLineStroke(dimensions),"blue",strokeWidth,"",
                                    function(){$scope.deleteConnectedLine(crossMarkId,firstTick1.data,secondTick1.data);},
                                    function(d){$scope.mouseOverTick(d)},function(d){$scope.mouseOutTick(d)});
                                
                                //graph d3 object,id circle id,clas circle class,x position,y position,color to fill circle,
                                //tickCircleRadious circle radious,click on dblclick function,mouseOver on mouseOver function,
                                //mouseOut on mouseOut function
                                ChartService.generateCircle($scope.graph,crossMarkId,crossMarkId,$scope.secondTick.dimensions[0],
                                    $scope.secondTick.dimensions[1],"blue",$scope.tickCircleRadious,
                                    function(){$scope.deleteConnectedLine(crossMarkId,firstTick1.data,secondTick1.data);},
                                    function(d){$scope.mouseOverExtraCircle(d)},function(d){$scope.mouseOutTick(d)});
                                ChartService.generateCircle($scope.graph,crossMarkId,crossMarkId,$scope.firstTick.dimensions[0],
                                    $scope.firstTick.dimensions[1],"blue",$scope.tickCircleRadious,
                                    function(){$scope.deleteConnectedLine(crossMarkId,firstTick1.data,secondTick1.data);},
                                    function(d){$scope.mouseOverExtraCircle(d)},function(d){$scope.mouseOutTick(d)});
                                
                                $scope.duties.duties.nextDuty = $scope.secondTick.data;
                                $scope.$parent.addDutiesToNewRTFromChart($scope.duties,[$scope.firstTick.data,$scope.secondTick.data])
                        }
                    };
                    
                
                /**
                 * Function to delete dynamically generated line and connected line
                 * @param lineId
                 * @param circleId
                 * @param firstTick
                 * @param secondTick
                 */
                $scope.deleteConnectedLine = function(circleId,duty1,duty2){
                    $scope.mouseOutTick();
                    var isRT = false;;
                    ChartService.removeElementByClassName(circleId)
                    if(document.getElementById($scope.tickCircleId+$scope.id+duty1[$scope.dutiesFields.id]).attributes.fill.nodeValue == "green")
                    	isRT = true; 
                    document.getElementById($scope.tickCircleId+$scope.id+duty1[$scope.dutiesFields.id]).attributes.fill.nodeValue = "red";
                    document.getElementById($scope.tickCircleId+$scope.id+duty2[$scope.dutiesFields.id]).attributes.fill.nodeValue = "red";
                    $scope.removeLink(duty1);
                    $scope.removeLink(duty2);
                    $scope.$parent.deleteDutiesAndRTFromChart(duty1,isRT);
                }
                
                /**
                 * This function is used to remove link from data object
                 */
                $scope.removeLink = function(duty){
                	duty[$scope.dutiesFields.isRoundTrip] = 0;
                	duty[$scope.dutiesFields.isPartialLink] = false;
                	duty[$scope.dutiesFields.partialLinkId] = "";
                }
                
                /**
                 * This function is used to add legends
                 */
                $scope.addLegends = function(){
                    //graph d3 object,data,id circle id,x position,y position,color to fill circle,
                    //tickCircleRadious circle radious,click on dblclick function,mouseOver on mouseOver function,
                    //mouseOut on mouseOut function
                    ChartService.addLegend($scope.graph,"Duty","dutyLegend"+$scope.id,$scope.legendXValue,$scope.legendYValue,
                        (($scope.toggleDuties)?"red":"black"),$scope.legendCircleRadious,function(d){$scope.toggleDuty()},
                        function(d){$scope.mouseOverLegend(d)},function(d){$scope.mouseOutTick(d)});
                    ChartService.addLegend($scope.graph,"RT","rtLegend"+$scope.id,$scope.legendXValue,$scope.legendYValue+25,
                    	(($scope.toggleRT)?"green":"black"),$scope.legendCircleRadious,function(d){$scope.toggleRoundTrips()},
                        function(d){$scope.mouseOverLegend(d)},function(d){$scope.mouseOutTick(d)});
                }
                
                /**
                 * This function is used to toggle Link trains
                 */
                $scope.toggleDuty = function(){
                	$scope.toggleDuties = !$scope.toggleDuties;
                    $scope.toggleLegend("dutyLegend"+$scope.id+"Text","dutyLegend"+$scope.id+"Circle","red",$scope.toggleDuties);
                    $scope.applySelectedStationsFilter();
                    $scope.applyLegendFilters();
                    $scope.loadChart();
                    $scope.duties.legends.isDuties = $scope.toggleDuties;
                }
                
                /**
                 * This function is used to toggle Link trains
                 */
                $scope.toggleRoundTrips = function(){
                	$scope.toggleRT = !$scope.toggleRT;
                    $scope.toggleLegend("rtLegend"+$scope.id+"Text","rtLegend"+$scope.id+"Circle","green",$scope.toggleRT);
                    $scope.applySelectedStationsFilter();
                    $scope.applyLegendFilters();
                    $scope.loadChart();
                    $scope.duties.legends.isRTs = $scope.toggleRT;
                }
                
                /**
                 * This function is used to toggle legend colors
                 */
                $scope.toggleLegend = function(textId,circleId,color,isLegend){
                    var ele1 = document.getElementById(textId);
                    var ele2 = document.getElementById(circleId);
                    if(ele1.classList.contains("active") && !isLegend){
                        ele1.classList.remove("active");
                        ele2.attributes.fill.nodeValue = "black";
                    }else if(!ele1.classList.contains("active") && isLegend){
                        ele1.classList.add("active");
                        ele2.attributes.fill.nodeValue = color;
                    }
                }
                
                /**
                 * This function is used to toggle selected stations
                 */
                $scope.toggleSelectedStations = function(d,ele,isFromStations){
                	var index;
                	if(isFromStations){
                		index = $scope.duties.stations.fromStations.indexOf(d["station"]);
                		if(index == -1){
                			$scope.duties.stations.fromStations.push(d["station"]);
                			ele.setAttribute("fill", "blue");
                		}else{
                			$scope.duties.stations.fromStations.splice(index,1);
                			ele.setAttribute("fill", "white");
                		}
                	}else{
                		index = $scope.duties.stations.toStations.indexOf(d["station"]);
                		if(index == -1){
                			$scope.duties.stations.toStations.push(d["station"]);
                			ele.setAttribute("fill", "blue");
                		}else{
                			$scope.duties.stations.toStations.splice(index,1);
                			ele.setAttribute("fill", "white");
                		}
                	}
                	$scope.applySelectedStationsFilter();
                    $scope.applyLegendFilters();
                	$scope.loadChart();
                }
                
                /**
                 * This function is used to toggle all selected stations
                 */
                $scope.toggleAllSelectedStations = function(ele,isFromStations){
                	var index;
                	if(isFromStations){
                		$scope.toggleAllFromStations = !$scope.toggleAllFromStations;
                		$scope.duties.stations.toggleAllFromStations = $scope.toggleAllFromStations
                		$scope.setFromAndToStations(true);
                		if($scope.toggleAllFromStations)ele.setAttribute("fill", "blue")
                		else ele.setAttribute("fill", "white");
                	}else{
                		$scope.toggleAllToStations = !$scope.toggleAllToStations;
                		$scope.duties.stations.toggleAllToStations = $scope.toggleAllToStations
                		$scope.setFromAndToStations(false);
                		if($scope.toggleAllToStations)ele.setAttribute("fill", "blue")
                		else ele.setAttribute("fill", "white");
                	}
                	$scope.applySelectedStationsFilter();
                    $scope.applyLegendFilters();
                	$scope.loadChart();
                }
                
                /**
                 * This function is used to show on mouse over line and hide other lines 
                 * @param d
                 */
                $scope.mouseOverTick = function(d){
                    ChartService.showAndGetToolTip($scope.tooltipId).innerHTML =
                            ((d[$scope.dutiesFields.isPartialLink])?"PARTIAL LINK":((d[$scope.dutiesFields.isRoundTrip] == 1)?"ROUND TRIP":"DRIVING DUTY"))+"<br>"+
                            "DD Name : "+d[$scope.dutiesFields.ddName]+"<br>"+
                            ((d[$scope.dutiesFields.isRoundTrip] == 1 && !d[$scope.dutiesFields.isPartialLink])?"Round Trip Name : "+d[$scope.dutiesFields.roundTripName]+"<br>":"")+
                            ((d[$scope.dutiesFields.isRoundTrip] == 1 && !d[$scope.dutiesFields.isPartialLink])?"Round Trip Base : "+d[$scope.dutiesFields.roundTripBaseStation]+"<br>":"")+
                            "From : "+d[$scope.dutiesFields.fromStation]+"<br>"+
                            "To : "+d[$scope.dutiesFields.toStation]+"<br>"+
                            "Departure Time : "+d[$scope.dutiesFields.departureTime]+"<br>"+
                            "Departure Day : "+$scope.Days[d[$scope.dutiesFields.departureDay]]+"<br>"+
                            "Arrival Time : "+d[$scope.dutiesFields.arrivalTime]+"<br>"+
                            "Arrival Day : "+$scope.Days[d[$scope.dutiesFields.arrivalDay]]+"<br>"+
                            "Duration : "+d[$scope.dutiesFields.duration]+"m<br>"+
                            "Distance : "+d[$scope.dutiesFields.distance]+"<br>"+
                            "Sign On Duration : "+d[$scope.dutiesFields.signOnDuration]+"m<br>"+
                            "Sign Off Duration : "+d[$scope.dutiesFields.signOffDuration]+"m<br>"+
                            ((d[$scope.dutiesFields.isRoundTrip] == 1 && !d[$scope.dutiesFields.isPartialLink])?"Round Trip Order No : "+d[$scope.dutiesFields.roundTripOrderNo]+"<br>":"")
                            ;
                    
                    
                    
                    var tickLines = $scope.graph.selectAll(".tickLines"+$scope.id+" path");
                    var existingConnectedLines = $scope.graph.selectAll(".existingConnectedLines"+$scope.id+" path");
                    var newConnectedLines = $scope.graph.selectAll(".newConnectedLines"+$scope.id+" path");
                    
                    tickLines.classed("inactive",
                            function(x) {
                                return (x !== d && 
                                        ((x[$scope.dutiesFields.isRoundTrip] == 1 && d[$scope.dutiesFields.isRoundTrip] == 1 && 
                                        x[$scope.dutiesFields.roundTrip] != d[$scope.dutiesFields.roundTrip]) || 
                                        (x[$scope.dutiesFields.isRoundTrip] == 0 || d[$scope.dutiesFields.isRoundTrip] == 0)) &&
                                        ((x[$scope.dutiesFields.isPartialLink] && d[$scope.dutiesFields.isPartialLink] && 
                                        x[$scope.dutiesFields.partialLinkId] != d[$scope.dutiesFields.partialLinkId]) || 
                                        (!x[$scope.dutiesFields.isPartialLink] || !d[$scope.dutiesFields.isPartialLink])));
                            });
                    existingConnectedLines.classed("inactive",function(x){return x[$scope.dutiesFields.roundTrip] !== d[$scope.dutiesFields.roundTrip];});
                    newConnectedLines.classed("inactive",function(x){return x[$scope.dutiesFields.partialLinkId] !== d[$scope.dutiesFields.partialLinkId];});
                }

                /**
                 * This function is used to undo hidden lines on mouse out
                 * @param d
                 */
                $scope.mouseOutTick = function(d){
                	var tickLines = $scope.graph.selectAll(".tickLines"+$scope.id+" path");
                    var existingConnectedLines = $scope.graph.selectAll(".existingConnectedLines"+$scope.id+" path");
                    var newConnectedLines = $scope.graph.selectAll(".newConnectedLines"+$scope.id+" path");
                    
                    ChartService.hideToolTip($scope.tooltipId);
                    tickLines.classed("inactive", false);
                    existingConnectedLines.classed("inactive", false);
                    newConnectedLines.classed("inactive", false);
                }
                
                /**
                 * This function is used show tooltip
                 * @param d
                 */
                $scope.mouseOverLegend = function(d){
                    ChartService.showAndGetToolTip($scope.tooltipId).innerHTML =
                            "Click me to<br>Show or Hide "+((d)?d:"this")+" Trinas"
                }
                
                /**
                 * This function is used show tooltip
                 * @param d
                 */
                $scope.mouseOverStation = function(d){
                    ChartService.showAndGetToolTip($scope.tooltipId).innerHTML =
                            "Click me to<br>load "+d.station
                }
                
                /**
                 * This function is used show tooltip
                 * @param d
                 */
                $scope.mouseOverStationRect = function(d){
                    ChartService.showAndGetToolTip($scope.tooltipId).innerHTML =
                            "Click me to<br>show/hide "+d.station+" <br>station duties"
                }
                
                /**
                 * This function is used show tooltip
                 * @param d
                 */
                $scope.mouseOverAllStationsRect = function(){
                    ChartService.showAndGetToolTip($scope.tooltipId).innerHTML =
                            "Click me to<br>show/hide all <br>stations duties <br>of this side"
                }
                
                /**
                 * This function is used show tooltip
                 * @param d
                 */
                $scope.mouseOverExtraCircle = function(d){
                    ChartService.showAndGetToolTip($scope.tooltipId).innerHTML =
                            "Click me to<br>remove this link ";
                }
            }
        };
  }]);