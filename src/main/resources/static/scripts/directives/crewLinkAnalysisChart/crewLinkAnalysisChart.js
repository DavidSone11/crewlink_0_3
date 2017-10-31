angular.module('crewLinkApp')
    .directive('crewLinkAnalysisChart', ['$compile', function($compile) {
        return {
            restrict: 'E',
            scope: { crewLink: '=',chartId : '='},
            templateUrl: 'scripts/directives/crewLinkAnalysisChart/crewLinkAnalysisChart.tmpl.html',
            controller: function($scope, $state,$window, $location,$timeout,ChartService) {
                
                
            	var timeoutP;
                var d = new Date();
            	var n = d.getTime();
            	$scope.chartId = "crewLinkAnalysisChartId"+n;
            	$scope.updateWait = 1000;
                
                $scope.$watch("crewLink", function(update){
            		$timeout.cancel(timeoutP);
            		timeoutPromiseServerLoadModel = $timeout(function(){
            			console.debug("Model Change detected");
            			$scope.init();
            		}.bind(this),$scope.updateWait);
            	},true);
                
                /**
                 * This function is used to remove train details from section object
                 */
                $scope.removeTrainDetails = function(newSection){
                	newSection[$scope.fields.dsTrain] = null;
            		newSection[$scope.fields.dsFrom] = null;
            		newSection[$scope.fields.dsTo] = null;
            		newSection[$scope.fields.dsArrivalDay] = null;
            		newSection[$scope.fields.dsArrivalTime] = null;
            		newSection[$scope.fields.dsDepartureDay] = null;
            		newSection[$scope.fields.dsDepartureTime] = null;
            		newSection[$scope.fields.dsDistance] = null;
            		return newSection;
                }
                $scope.init = function(){
                	
                	$scope.sections = $scope.crewLink.data;
                    $scope.fields = $scope.crewLink.fields;
                    $scope.fields.depWeek = $scope.sections[0].length;
                    $scope.fields.arrWeek = $scope.sections[0].length+1;
                    
	                //Split start pilots ,and end pilots
	                for(var s=0; s<$scope.sections.length;s++){
	                	//finding start&end pilots and update
	                	var newSection = {};
	                	if($scope.sections[s][$scope.fields.startPilot] && 
	                			$scope.sections[s][$scope.fields.dsTrain]){
	                		newSection = JSON.parse(JSON.stringify($scope.sections[s]));
	                		newSection = $scope.removeTrainDetails(newSection);
	                		$scope.sections.splice(s,0,newSection);s++;
	                		
	                		$scope.sections[s][$scope.fields.startPilot] = null;
	                		$scope.sections[s][$scope.fields.spDistance] = null;
	                		$scope.sections[s][$scope.fields.spArrivalDay] = null;
	                		$scope.sections[s][$scope.fields.spArrivalTime] = null;
	                		
	                	}else if($scope.sections[s][$scope.fields.endPilot] && 
	                			$scope.sections[s][$scope.fields.dsTrain]){
	                		newSection = JSON.parse(JSON.stringify($scope.sections[s]));
	                		newSection = $scope.removeTrainDetails(newSection);
	                		if(s+1 < $scope.sections.length)
	                			$scope.sections.splice(s+1,0,newSection);
	                		else
	                			$scope.sections.push(newSection);
	                		
	                		$scope.sections[s][$scope.fields.endPilot] = null;
	                		$scope.sections[s][$scope.fields.epDistance] = null;
	                		$scope.sections[s][$scope.fields.epDepartureDay] = null;
	                		$scope.sections[s][$scope.fields.epDepartureTime] = null;
	                		s++;
	                	}
	                }
	                
	                var week = 0;
	                for(var s=0; s<$scope.sections.length; s++){
	                	//finding dep week and arr week
	                	if(s==0){
	                		$scope.sections[0].push(week);
	                        if($scope.sections[0][$scope.fields.dsDepartureDay] > 
	        	            	$scope.sections[0][$scope.fields.dsArrivalDay] || 
	        	            	($scope.sections[0][$scope.fields.ddeStartDay] > 
	        	            	$scope.sections[0][$scope.fields.spArrivalDay] && $scope.sections[0][$scope.fields.dsTrain]) == null){
	        	                $scope.sections[0].push(++week);
	        	            }else{
	        	            	$scope.sections[0].push(week);
	        	            }
	                	}else{
	                		if(
	                			(($scope.sections[s-1][$scope.fields.dsArrivalDay] > $scope.sections[s][$scope.fields.ddeStartDay]	||
	                			(($scope.sections[s-1][$scope.fields.spArrivalDay] > $scope.sections[s][$scope.fields.ddeStartDay] ||
	                			$scope.sections[s-1][$scope.fields.ddeEndDay] > $scope.sections[s][$scope.fields.ddeStartDay]) && $scope.sections[s-1][$scope.fields.dsTrain] == null)) && $scope.sections[s][$scope.fields.dsTrain] == null && $scope.sections[s][$scope.fields.startPilot] != null) ||
	                			
	                			(($scope.sections[s-1][$scope.fields.dsArrivalDay] > $scope.sections[s][$scope.fields.dsDepartureDay] ||
	                			(($scope.sections[s-1][$scope.fields.spArrivalDay] > $scope.sections[s][$scope.fields.dsDepartureDay] ||
	                			$scope.sections[s-1][$scope.fields.ddeEndDay] > $scope.sections[s][$scope.fields.dsDepartureDay]) && $scope.sections[s-1][$scope.fields.dsTrain] == null)) && $scope.sections[s][$scope.fields.dsTrain] != null) ||
	                            
	                			(($scope.sections[s-1][$scope.fields.dsArrivalDay] > $scope.sections[s][$scope.fields.epDepartureDay]	||
	                			(($scope.sections[s-1][$scope.fields.spArrivalDay] > $scope.sections[s][$scope.fields.epDepartureDay] ||
	                			$scope.sections[s-1][$scope.fields.ddeEndDay] > $scope.sections[s][$scope.fields.epDepartureDay]) && $scope.sections[s-1][$scope.fields.dsTrain] == null)) && $scope.sections[s][$scope.fields.dsTrain] == null && $scope.sections[s][$scope.fields.endPilot] != null)
	                            ){
	                			$scope.sections[s].push(++week);
	                		}else
		                		$scope.sections[s].push(week);
		                	
	                		if(
	                			((($scope.sections[s][$scope.fields.ddeStartDay] > $scope.sections[s][$scope.fields.spArrivalDay]) ||
	                			($scope.sections[s][$scope.fields.epDepartureDay] > $scope.sections[s-1][$scope.fields.ddeEndDay])) && $scope.sections[s][$scope.fields.dsTrain] == null) ||
	                			($scope.sections[s][$scope.fields.dsDepartureDay] >	$scope.sections[s][$scope.fields.dsArrivalDay])
	                			){
	                			$scope.sections[s].push(++week);
	                		}else
	    		            	$scope.sections[s].push(week);
	                	}
	                }
	                $scope.crewLink.noOfWeeks = week+1;
	                
	                $scope.divWidth = document.getElementById($scope.chartId).offsetWidth;
                    $scope.generateLocoLinkChart();
                };
                
                
                /**
                 * This function is used to call generateLocoLinkChart function 
                 * after 500sec because div id is generating dynamically
                 */
                
                    
                

                $scope.initializeValues = function(){
                    $scope.margin={top:50,left:10,bottom:40,right:10};
                    $scope.width=$scope.divWidth-$scope.margin.left-$scope.margin.right;
                    $scope.finalWidth=$scope.width+$scope.margin.left+$scope.margin.right;
                    $scope.height=($scope.crewLink.noOfWeeks * 1000)-$scope.margin.top-$scope.margin.bottom;
                    $scope.finalHeight=$scope.height+$scope.margin.top+$scope.margin.bottom+100;
                    
                    $scope.tooltipId = "crewLinkAnalysisToolTip";
                    ChartService.removeElementById($scope.tooltipId);
                    var div = d3.select("body").append("div")	
    			    .attr("class", "tooltip")
    			    .attr("id", $scope.tooltipId)	
    			    .style("opacity", 0);
                    
                    $scope.newLineStroke = d3.svg.line().x(function(d,i){return d.x}).y(function(d){return d.y});
                    //Day and place on y axis to add days text
                    $scope.days=Days;
                    $scope.tickCircleRadius = 5;
                    $scope.tickLineWidth = 2;
                    document.getElementById($scope.chartId).innerHTML = "";
                    $scope.ticksPerWeek = 42;
                    $scope.noOfTicks = $scope.crewLink.noOfWeeks * $scope.ticksPerWeek;
                    $scope.noOfHrsPerTick = 4;
                    $scope.dimensionsYAxis=d3.scale.linear().range([0,$scope.height]).domain([0,$scope.noOfTicks]);
                    
                    $scope.stationDetails = {};
                    $scope.stationsList = [];
                    var station;
                    for(var section=0; section<$scope.sections.length; section++){
                    	station = (($scope.sections[section][$scope.fields.dsFrom])?$scope.sections[section][$scope.fields.dsFrom]:
		                    		(($scope.sections[section][$scope.fields.spStation])?$scope.sections[section][$scope.fields.spStation]:
		                    			$scope.sections[section][$scope.fields.epFromStation]));
                        if($scope.stationsList.indexOf(station) == -1){
                            $scope.stationDetails[station] = {};
                            $scope.stationsList.push(station);
                        }
                        
                        station = (($scope.sections[section][$scope.fields.dsTo])?$scope.sections[section][$scope.fields.dsTo]:
                    		(($scope.sections[section][$scope.fields.spToStation])?$scope.sections[section][$scope.fields.spToStation]:
                    			$scope.sections[section][$scope.fields.epStation]));
		                if($scope.stationsList.indexOf(station) == -1){
		                    $scope.stationDetails[station] = {};
		                    $scope.stationsList.push(station);
		                }
                    }
                    var stationsWidth = 1/($scope.stationsList.length+1);
                    var initialWidth = stationsWidth;
                    for(var i=0; i<$scope.stationsList.length; i++){
                        $scope.stationDetails[$scope.stationsList[i]].xPosition = $scope.divWidth*stationsWidth;
                        stationsWidth += initialWidth;
                    }
                }


                /**
                 * Function to load chart in div
                 * @param station
                 */
                 $scope.generateLocoLinkChart = function(){
                    
                    $scope.initializeValues();
                    $scope.graph=d3.select("#"+$scope.chartId).append("svg").attr("width",$scope.finalWidth)
                        .attr("height",$scope.finalHeight).append("g")
                        .attr("transform", "translate(" + $scope.margin.left + "," + $scope.margin.top + ")");
                    
                    //graph d3 object,text to display,x axis position,y axis position,class for text
                    ChartService.addYAxisHeading($scope.graph,$scope.crewLink.linkName+"- Crew",
                    		$scope.divWidth*0.45,function(d){return $scope.dimensionsYAxis(0)-35},"chartHeading");
                    
                    var distanceXValue = ($scope.stationDetails[$scope.stationsList[1]].xPosition-
                                                $scope.stationDetails[$scope.stationsList[0]].xPosition)/2;
                    for(var station in $scope.stationDetails){
                        //graph object to draw,id of axis,clas of axis,x position on x axis,side of ticks,
                        //height of axis,ticks on axis,click,mouseOver,mouseOut,dblClick function,no of days
                        ChartService.drawYAxis($scope.graph,"","y-axis",
                            $scope.stationDetails[station].xPosition,
                            ((station != $scope.stationsList[$scope.stationsList.length-1])?'left':'right'),$scope.height,
                            ((station == $scope.stationsList[$scope.stationsList.length-1] || 
                                station == $scope.stationsList[0])?$scope.noOfTicks:0),"","","","",$scope.crewLink.noOfWeeks*7);
                        //graph d3 object,text to display,x axis position,y axis position,class for text
                        ChartService.addYAxisHeading($scope.graph,station,
                            $scope.stationDetails[station].xPosition-20,function(d){return $scope.dimensionsYAxis(0)-20},"headingtext");
                    }
                    
                    
                    //graph d3 object,data array,divClass class name,lineClass class name,id line Id,counter id counter,
                    //dimensions line dimensions,stroke color,strokeWidth,click function,dblClick function,
                    //mouseOver function,mouseOut function
                    ChartService.generateLines($scope.graph,$scope.sections,"tickLines","tickLine","tickLine",
                        function(d){return $scope.getTickLineDimensions(d)},
                        function(d){return (((d[$scope.fields.startPilot] || d[$scope.fields.endPilot]))?
                        						"blue" :"green")},$scope.tickLineWidth,"","",
                        function(d){$scope.mouseOverTick(d)},function(d){$scope.mouseOutTick(d)});
                    
                    //graph d3 object,data array,divClass class name,id text id,textClass class name,x axis position,
	                //y axis position,text to display,click function,
	                //mouseOver function,mouseOut function
	                ChartService.addContentOnYAxis($scope.graph,$scope.sections,'ticksArrDetails',
	                	'tickArrDetails','tickArrDetails',
	                	function(d){return $scope.getXPositionToAddCircleOnTick(d,false)+
	                						((d[$scope.fields.dsFrom] == $scope.stationsList[$scope.stationsList.length-1] || 
	                								d[$scope.fields.epFromStation] == $scope.stationsList[$scope.stationsList.length-1] || 
	                								d[$scope.fields.spStation] == $scope.stationsList[$scope.stationsList.length-1])?(-200):7);},
	                    function(d){return $scope.getYPositionToAddCircleOnTick(d,false)+
	                							((d[$scope.fields.dsTo] == $scope.stationsList[0] ||
	                									d[$scope.fields.spToStation] == $scope.stationsList[0] ||
	                									d[$scope.fields.epStation] == $scope.stationsList[0])?10:(-5));},
	                    function(d){
	                    	return "W"+(d[$scope.fields.depWeek]+1)+","+
	                    			((d[$scope.fields.dsTrain])?d[$scope.fields.dsTrain]+","+d[$scope.fields.dsFrom]+"-"+d[$scope.fields.dsTo] :
	                    				((d[$scope.fields.startPilot])?d[$scope.fields.startPilot]+","+d[$scope.fields.spStation]+"-"+d[$scope.fields.spToStation]:
	                    					d[$scope.fields.endPilot]+","+d[$scope.fields.epFromStation]+"-"+d[$scope.fields.epStation]))+
	                    			","+((d[$scope.fields.dsDepartureTime])?d[$scope.fields.dsDepartureTime].substring(0,5):
	                    					((d[$scope.fields.epDepartureTime])?d[$scope.fields.epDepartureTime].substring(0,5):d[$scope.fields.ddeStartTime].substring(0,5)))+
	                    			"("+((d[$scope.fields.dsTrain])?d[$scope.fields.dsDistance]:
	                    				((d[$scope.fields.startPilot])?d[$scope.fields.spDistance]:d[$scope.fields.epDistance]))+"Km)";
	                    },"",function(d){$scope.mouseOverTick(d)},function(d){$scope.mouseOutTick()});
	                ChartService.addContentOnYAxis($scope.graph,$scope.sections,'ticksDepDetails',
	                	'tickDepDetails','tickDepDetails',
	                	function(d){return $scope.getXPositionToAddCircleOnTick(d,true)+
	                						((d[$scope.fields.dsTo] == $scope.stationsList[$scope.stationsList.length-1] ||
	                								d[$scope.fields.spToStation] == $scope.stationsList[$scope.stationsList.length-1] ||
	                								d[$scope.fields.epStation] == $scope.stationsList[$scope.stationsList.length-1])?(-200):7);},
	                    function(d){return $scope.getYPositionToAddCircleOnTick(d,true)+
	                							((d[$scope.fields.dsTo] == $scope.stationsList[0] || 
	                									d[$scope.fields.spToStation] == $scope.stationsList[0] || 
	                									d[$scope.fields.epStation] == $scope.stationsList[0])?15:(-5));},
	                    function(d){
	                    	return "W"+(d[$scope.fields.arrWeek]+1)+","+
	                    	((d[$scope.fields.dsTrain])?d[$scope.fields.dsTrain]+","+d[$scope.fields.dsFrom]+"-"+d[$scope.fields.dsTo] :
                				((d[$scope.fields.startPilot])?d[$scope.fields.startPilot]+","+d[$scope.fields.spStation]+"-"+d[$scope.fields.spToStation]:
                					d[$scope.fields.endPilot]+","+d[$scope.fields.epFromStation]+"-"+d[$scope.fields.epStation]))+
	                    	","+((d[$scope.fields.dsArrivalTime])?d[$scope.fields.dsArrivalTime].substring(0,5):
	                    		((d[$scope.fields.spArrivalTime])?d[$scope.fields.spArrivalTime].substring(0,5):d[$scope.fields.ddeEndTime].substring(0,5)))+
	                    	"("+((d[$scope.fields.dsTrain])?d[$scope.fields.dsDistance]:
                				((d[$scope.fields.startPilot])?d[$scope.fields.spDistance]:d[$scope.fields.epDistance]))+"Km)";
	                    },"",function(d){$scope.mouseOverTick(d)},function(d){$scope.mouseOutTick()});
                    
                    
                    //graph d3 object,data array,divClass class name,circleClass class name,id circle id,x axis position,
                    //yAxis position,r circle radius,color circle color
                    //dblClick function,,mouseOver function,mouseOut function
                    ChartService.generateCircles($scope.graph,$scope.sections,"tickCircles","tickCircle","tickCircle",
                        function(d){return $scope.getXPositionToAddCircleOnTick(d,false);},
                        function(d){return $scope.getYPositionToAddCircleOnTick(d,false);},
                        $scope.tickCircleRadius,"red","",
                        function(d){$scope.mouseOverTick(d)},function(d){$scope.mouseOutTick(d)});
                    ChartService.generateCircles($scope.graph,$scope.sections,"tickCircles","tickCircle","tickCircle",
                        function(d){return $scope.getXPositionToAddCircleOnTick(d,true);},
                        function(d){return $scope.getYPositionToAddCircleOnTick(d,true);},
                        $scope.tickCircleRadius,"red","",
                        function(d){$scope.mouseOverTick(d)},function(d){$scope.mouseOutTick(d)});
                }
                
                /**
                 * This function is used to show on mouse over line and hide other lines 
                 * @param d
                 */
                $scope.mouseOverTick = function(d){
                    ChartService.showAndGetToolTip($scope.tooltipId).innerHTML =
                    	"RT Order No : "+d[$scope.fields.rtOrder]+"<br>"+
                    	"Week : "+(d[$scope.fields.arrWeek]+1)+
                		((d[$scope.fields.arrWeek] != d[$scope.fields.depWeek])?" & "+(d[$scope.fields.depWeek]+1):"")+"<br>"+
                		
                		((d[$scope.fields.dsTrain])?"Train No : "+d[$scope.fields.dsTrain]+"<br>"+
                    	"From : "+d[$scope.fields.dsFrom]+"<br>"+
                    	"To : "+d[$scope.fields.dsTo]+"<br>"+
                    	"Departure : "+d[$scope.fields.dsDepartureTime]+"<br>"+
                        "Departure Day : "+$scope.days[d[$scope.fields.dsDepartureDay]]+"<br>"+
                        "Arrival : "+d[$scope.fields.dsArrivalTime]+"<br>"+
                        "Arrival Day : "+$scope.days[d[$scope.fields.dsArrivalDay]]+"<br>"+
                    	"Distance : "+d[$scope.fields.dsDistance]+"Km<br>":"")+
                    	
                    	((d[$scope.fields.startPilot])?"Start Pilot : "+d[$scope.fields.startPilot]+"<br>"+
                    	"Start Pilot From Station : "+d[$scope.fields.spStation]+"<br>"+
                    	"Start Pilot To Station : "+d[$scope.fields.spToStation]+"<br>"+
                    	"Start Pilot Distance : "+d[$scope.fields.spDistance]+"Km<br>":"")+
                    	
                        "SignOn : "+d[$scope.fields.ddSignOn]+"<br>"+
                        "SignOn Day : "+$scope.days[d[$scope.fields.ddSignOnDay]]+"<br>"+
                        "SignOff : "+d[$scope.fields.ddSignOff]+"<br>"+
                        "SignOff Day : "+$scope.days[d[$scope.fields.ddSignOffDay]]+"<br>"+
                    	((d[$scope.fields.endPilot])?"End Pilot : "+d[$scope.fields.endPilot]+"<br>"+
                    	"End Pilot From Station : "+d[$scope.fields.epFromStation]+"<br>"+
                    	"End Pilot To Station : "+d[$scope.fields.epStation]+"<br>"+
                    	"End Pilot Distance : "+d[$scope.fields.epDistance]+"Km<br>":"")+
                    	"Round Trip SignOn : "+d[$scope.fields.rtSignOn]+"<br>"+
                    	"Round Trip SignOn Day : "+$scope.days[d[$scope.fields.rtSignOnDay]]+"<br>"+
                    	"Round Trip SignOff : "+d[$scope.fields.rtSignOff]+"<br>"+
                    	"Round Trip SignOff Day : "+$scope.days[d[$scope.fields.rtSignOffDay]]+"<br>"+
                    	"Round Trip OS Rest : "+minutesToTimeFormat(d[$scope.fields.rtOSRest])+"<br>";
                }

                /**
                 * This function is used to undo hidden lines on mouse out
                 * @param d
                 */
                $scope.mouseOutTick = function(d){
                    ChartService.hideToolTip($scope.tooltipId);
                }
                
                /**
                 *  Function to get dimensions to draw line
                 * @param data
                 * @returns tick line dimentions
                 */
                $scope.getTickLineDimensions = function(data){
                    var y1,y2,x1,x2,dimensions,station;
                    station = ((data[$scope.fields.dsFrom])?data[$scope.fields.dsFrom]:
								((data[$scope.fields.spStation])?data[$scope.fields.spStation]:data[$scope.fields.epFromStation]));
                    x1 = (station)?$scope.stationDetails[station].xPosition:null;
                    station = ((data[$scope.fields.dsTo])?data[$scope.fields.dsTo]:
        						((data[$scope.fields.spToStation])?data[$scope.fields.spToStation]:data[$scope.fields.epStation]));
                    x2 = (station)?$scope.stationDetails[station].xPosition:null;
                    
                    y1 = $scope.getDepartureTickValue(data);
                    y2 = $scope.getArrivalTickValue(data);
                    
                    dimensions=[{x:x1,y:$scope.dimensionsYAxis(y1)},{x:x2,y:$scope.dimensionsYAxis(y2)}];
                    
                    
                    return (x2 && x1 && y2 && y1)?$scope.newLineStroke(dimensions):null;
                }
                
                /**
                 * This function is used to find and return departure tick value
                 */
                $scope.getDepartureTickValue = function(data){
                	var time,day,tick;
                	if(data[$scope.fields.dsDepartureTime]){
                		time = data[$scope.fields.dsDepartureTime];
                		day = data[$scope.fields.dsDepartureDay];
                	}else if(data[$scope.fields.epDepartureTime]){
                		time = data[$scope.fields.epDepartureTime];
                		day = data[$scope.fields.epDepartureDay];
                	}else if(data[$scope.fields.startPilot]){
                		time = data[$scope.fields.ddeStartTime];
                		day = data[$scope.fields.ddeStartDay];
                	}
                	tick = (ChartService.getTickValue(time,day)/$scope.noOfHrsPerTick) + 
            				(data[$scope.fields.depWeek]*$scope.ticksPerWeek);
                	return tick;
                }
                
                /**
                 * This function is used to find and return arrival tick value
                 */
                $scope.getArrivalTickValue = function(data){
                	var time,day,tick;
                	if(data[$scope.fields.dsArrivalTime]){
                		time = data[$scope.fields.dsArrivalTime];
                		day = data[$scope.fields.dsArrivalDay];
                	}else if(data[$scope.fields.spArrivalTime]){
                		time = data[$scope.fields.spArrivalTime];
                		day = data[$scope.fields.spArrivalDay];
                	}else if(data[$scope.fields.endPilot]){
                		time = data[$scope.fields.ddeEndTime];
                		day = data[$scope.fields.ddeEndDay];
                	}
                	tick = (ChartService.getTickValue(time,day)/$scope.noOfHrsPerTick) + 
            				(data[$scope.fields.arrWeek]*$scope.ticksPerWeek);
                	return tick;
                }
                
                /**
                 * Function to get position of X to add circle
                 * @param data
                 * @returns {Number} x position
                 */
                $scope.getXPositionToAddCircleOnTick = function(data,isArrival){
                    var tickVal,station;
                    if(isArrival){
                    	station = ((data[$scope.fields.dsTo])?data[$scope.fields.dsTo]:
                    				((data[$scope.fields.spToStation])?data[$scope.fields.spToStation]:data[$scope.fields.epStation]));
                    	tickVal = (station)?$scope.stationDetails[station].xPosition-3:null;
                    }else{
                    	station = ((data[$scope.fields.dsFrom])?data[$scope.fields.dsFrom]:
            						((data[$scope.fields.spStation])?data[$scope.fields.spStation]:data[$scope.fields.epFromStation]));
                    	tickVal = (station)?$scope.stationDetails[station].xPosition-3:null;
                    }
                    return tickVal;
                }

                /**
                 * Function to get position of Y to add circle on tick
                 */
                $scope.getYPositionToAddCircleOnTick = function(data,isArrival){
                    var tickVal;
                    if(isArrival){
                    	tickVal = $scope.getArrivalTickValue(data);
                    }else{
                    	tickVal = $scope.getDepartureTickValue(data);
                    }
                    return (tickVal != null)?$scope.dimensionsYAxis(tickVal):null;
                }
                
                /**
                 * Function to save chart as png
                 */
                $scope.saveCrewLinkAnalysisChart = function(){
              	  var html = d3.select("svg")
              	        .attr("version", 1.1)
              	        .attr("xmlns", "http://www.w3.org/2000/svg")
              	        .node().parentNode.innerHTML;
              	  var imgsrc = 'data:image/svg+xml;base64,'+ btoa(html);
              	  var image = new Image;
              	  image.src = imgsrc;
              	  image.onload = function() {
          			  window.open(imgsrc);
          			  var a = document.createElement("a");
            		  a.download = $scope.crewLink.linkName+".svg";
            		  a.href = imgsrc;
            		  a.click();
              	  };
              };

                
                
            }
        };
  }]);