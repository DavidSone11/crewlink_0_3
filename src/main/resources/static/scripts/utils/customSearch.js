Array.prototype.findWithHref = function(inputValue) {
	if (inputValue) {
		for ( var value in this) {
			if (this[value]._links.self.href == inputValue._links.self.href) {
				return this[value];
			}
		}
	}
	return null;
};
String.prototype.isEmpty = function() {
    return (this.length === 0 || !this.trim());
};
var Days = ['SUNDAY','MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY'];
function calculateDurationBetweenTwoTimes(startTimeObj,endTimeObj)
{
	var endDayValue=getDayId(endTimeObj.endDay);
	var startDayValue=getDayId(startTimeObj.startDay);
	var counter=0;
	if(endDayValue<startDayValue)
		{
			counter = endDayValue-startDayValue;
		}
	else
		{
			for(var i=endDayValue;i<Days.length;i++)
				{
					++counter;
				}
			for(var i=0;i<startDayValue-1;i++)
				{
					++counter;
				}
		}
	return counter;
}


function calDiffBetweenTwoTimes(object)
{
		(object);
	  var departure;
	  var arrival;
	  var startingDayValue,startingTime;
	  var endDayValue,endTime;
	  var diffDayValue;
	  var totalDuration=0;
	 
	  
	  departure = object.startTime.split(':')
	  var depHour = $.trim(departure[0]);
	  var depMin = $.trim(departure[1]);
	  
	  
	  arrival = object.arrival.split(':')
	  var arrHour = $.trim(arrival[0]);
	  var arrMin = $.trim(arrival[1]);
	
	  var departureInMin = parseInt(depHour * 60) + parseInt(depMin);
	  var arrivalInMin = parseInt(arrHour * 60) + parseInt(arrMin);
	  
	  ("departureInMin " +departureInMin+"arrivalInMin " +arrivalInMin);
	  if(object.endDay==0)
		  diffDayValue = object.startDay-1;
	  else
		if(object.startDay==0)
			 diffDayValue = 7- object.startDay-1;
		else
			diffDayValue = object.startDay-object.endDay -1;
	  
	  
		("object.endDay " +object.endDay+"object.startDay "+object.startDay);
		
	  ("diffDayValue " +diffDayValue);
	  if(diffDayValue>=0)
	  {
		  totalDuration = diffDayValue * 24*60 + 24*60 - departureInMin + arrivalInMin;
	  }
	  else if(diffDayValue==-1)
	  {
		  totalDuration = (departureInMin-arrivalInMin);
	  }
	  else
	  {
		  diffDayValue = Days.length-1-object.startDay+object.endDay;
		  totalDuration = diffDayValue*24*60 + 24*60 - departureInMin+
		  arrivalInMin;
	  }
	
	  return totalDuration;
}
function calDiff(arrivalObject,departureObject)
{
	var startingDayValue, startingTime, startingTimeTest;
	var endDayValue, endTime;
	var diffDayValue;
	var outStationRest;
	var totalDistance = 0;
	var totalDuration = 0;
	var totalOSDuration = 0;

	startingTime = departureObject.departureTime;
	endTime = arrivalObject.arrivalTime;
	/*startingDayValue = getDayId(departureObject.startDay);
	endDayValue = getDayId(arrivalObject.startDay );
	*/
	/*var totalOsSignOnOff = parseInt(departureObject._ref.fromStation.outStationSignOnDuration)
			+ parseInt(arrivalObject._ref.toStation.outStationSignOffDuration);*/
	
	diffDayValue = departureObject.departureDay - arrivalObject.arrivalDay - 1;

	var departureTime = [];
	departureTime = departureObject.departureTime.split(':')
	var hourStarting = $.trim(departureTime[0]);
	var minStarting = $.trim(departureTime[1]);
	var departureInMin = parseInt(hourStarting) * 60
			+ parseInt(minStarting);

	var arrivalTime = [];
	arrivalTime = arrivalObject.arrivalTime.split(':')
	var hourEnd = $.trim(arrivalTime[0]);
	var minEnd = $.trim(arrivalTime[1]);
	var arrivalInMin = parseInt(hourEnd) * 60 + parseInt(minEnd);

	
	if (diffDayValue >= 0 || diffDayValue == -1) {
		
		if(diffDayValue == -1){
			outStationRest = arrivalInMin - departureInMin;
			/*if(outStationRest>=totalOsSignOnOff)
				outStationRest = outStationRest - totalOsSignOnOff;*/
			outStationRest=Math.abs(outStationRest);
			("1 " +outStationRest)
		}
		else
		{
			outStationRest = diffDayValue * 24 * 60 + 24 * 60
					- arrivalInMin + departureInMin;
			("2 " +outStationRest)
		}
	} else {
				if(departureObject.departureDay==0)
							diffDayValue= 7-arrivalObject.arrivalDay-1;
				else if(arrivalObject.arrivalDay==0)
					diffDayValue=departureObject.departureDay;
				else
					diffDayValue = 6- arrivalObject.arrivalDay + departureObject.departureDay;
		//diffDayValue = Days.length - 1 - departureObject.departureDay + arrivalObject.arrivalDay;
		outStationRest = diffDayValue * 24 * 60 + 24 * 60
				- arrivalInMin + departureInMin;
		("3 " +outStationRest)
	}

	return outStationRest;
		
}

function diffTimeObj(fromTimeObj,toTimeObj,units){
	if(fromTimeObj.day==null||fromTimeObj.time==null)
		return null;
	if(toTimeObj.day==null||toTimeObj.time==null)
		return null;
	
	var fromDayI = getDayId(fromTimeObj.day);
	//(fromDayI);
	if(fromDayI == -1){
		return null;
	}
	
	//fromDayI++;
	var toDayI = getDayId(toTimeObj.day);
	//(toDayI);
	if(toDayI == -1){
		return null;
	}
	//toDayI++;
	var diffDays = toDayI - fromDayI;
	if(diffDays <0){
		diffDays = 7 + diffDays;
	}else{
		diffDays = Math.abs(diffDays);
	}
//	if(fromTimeObj.time == "00:00:00"){
//		fromTimeObj.time = "24:00:00"
//	}
	var fromDate = new Date("1/"+ 1 +"/2012 " + fromTimeObj.time);
	var toDate = new Date("1/"+(1+diffDays)+"/2012 " + toTimeObj.time);
	//(fromDate);
	//(toDate);
	return diffDate(fromDate,toDate,units);
}

function diffDate(fromDate,toDate,units){
	// Copy date parts of the timestamps, discarding the time parts.
    //var one = new Date(fromDate.getFullYear(), fromDate.getMonth(), fromDate.getDate());
    //var two = new Date(toDate.getFullYear(), toDate.getMonth(), toDate.getDate());
	var one = fromDate;
	var two = toDate;
	
    // Do the math.
    var millisecondsPerDay = 1000 * 60 * 60 * 24;
    var millisecondsPerHour = 1000*60*60;
    var millisecondsPerMinute = 1000*60;
    var millisecondsPerSecond = 1000;
    var diffSec = two.getSeconds() - one.getSeconds();
    var millisBetween = two.getTime() - one.getTime();
    if(millisBetween<0){
    	millisBetween = (millisecondsPerDay*7) + millisBetween;
    }
    switch(units.toLowerCase()){
	    case 'days':case 'day': return Math.floor(millisBetween / millisecondsPerDay); break;
	    case 'hours': case 'hours': case 'hr': case 'hrs': return Math.floor(millisBetween / millisecondsPerHour); break;
	    case 'minutes': case 'minute': case 'min': case 'mins': return Math.floor(millisBetween / millisecondsPerMinute); break;
	    case 'seconds': case 'second': case 'sec': case 'secs': return Math.floor(millisBetween / millisecondsPerSecond); break;
	    default : return null;
    }
}

function getDayId(day){
	if(Number.isInteger(day))
		return day; // return the day if it was number
	
	var dayI = -1;
	for(var i=0;i<Days.length;i++){
		if(Days[i]==day){
			dayI = i;
			break;
		}
	}
	return dayI;
}
function calTime(day,time,duration,operation){
	var initDate = new Date("1/1/2012 " + time);
	var resultDate = null;
	var diffDays = null;
	switch(operation.toLowerCase()){
	case '+': case 'add': case 'addition': resultDate = dateAdd(initDate,'minute',duration);
	 		
		break;
	case '-': case 'sub': case 'substraction': resultDate = dateSub(initDate,'minute',duration);
		break;
	default : return null;
	}
	var dayI = getDayId(day);
	
	if(dayI == -1){
		return null;
	}
	diffDays = daysBetween(initDate,resultDate);
	//("diffDays " +diffDays);
	var resultObj = {
			day : Days[(dayI+diffDays)%7],
			time: ((resultDate.getHours()<10)?'0':'')+resultDate.getHours() + ":" +((resultDate.getMinutes()<10)?'0':'') + resultDate.getMinutes()
	}
	return resultObj;
	
}

function daysBetween(first, second) {

    // Copy date parts of the timestamps, discarding the time parts.
    var one = new Date(first.getFullYear(), first.getMonth(), first.getDate());
    var two = new Date(second.getFullYear(), second.getMonth(), second.getDate());

    // Do the math.
    var millisecondsPerDay = 1000 * 60 * 60 * 24;
    var millisBetween = two.getTime() - one.getTime();
    var days = millisBetween / millisecondsPerDay;

    // Round down.
    return Math.floor(days);
}


function dateAdd(date, interval, units) {
	  var ret = new Date(date); //don't change original date
	  switch(interval.toLowerCase()) {
	    case 'year'   :  ret.setFullYear(ret.getFullYear() + units);  break;
	    case 'quarter':  ret.setMonth(ret.getMonth() + 3*units);  break;
	    case 'month'  :  ret.setMonth(ret.getMonth() + units);  break;
	    case 'week'   :  ret.setDate(ret.getDate() + 7*units);  break;
	    case 'day'    :  ret.setDate(ret.getDate() + units);  break;
	    case 'hour'   :  ret.setTime(ret.getTime() + units*3600000);  break;
	    case 'minute' :  ret.setTime(ret.getTime() + units*60000);  break;
	    case 'second' :  ret.setTime(ret.getTime() + units*1000);  break;
	    default       :  ret = undefined;  break;
	  }
	  return ret;
	}
function dateSub(date, interval, units) {
	  var ret = new Date(date); //don't change original date
	  switch(interval.toLowerCase()) {
	    case 'year'   :  ret.setFullYear(ret.getFullYear() - units);  break;
	    case 'quarter':  ret.setMonth(ret.getMonth() - 3*units);  break;
	    case 'month'  :  ret.setMonth(ret.getMonth() - units);  break;
	    case 'week'   :  ret.setDate(ret.getDate() - 7*units);  break;
	    case 'day'    :  ret.setDate(ret.getDate() - units);  break;
	    case 'hour'   :  ret.setTime(ret.getTime() - units*3600000);  break;
	    case 'minute' :  ret.setTime(ret.getTime() - units*60000);  break;
	    case 'second' :  ret.setTime(ret.getTime() - units*1000);  break;
	    default       :  ret = undefined;  break;
	  }
	  return ret;
	}

//From https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Object/keys
if (!Object.keys) {
  Object.keys = (function() {
    'use strict';
    var hasOwnProperty = Object.prototype.hasOwnProperty,
        hasDontEnumBug = !({ toString: null }).propertyIsEnumerable('toString'),
        dontEnums = [
          'toString',
          'toLocaleString',
          'valueOf',
          'hasOwnProperty',
          'isPrototypeOf',
          'propertyIsEnumerable',
          'constructor'
        ],
        dontEnumsLength = dontEnums.length;

    return function(obj) {
      if (typeof obj !== 'object' && (typeof obj !== 'function' || obj === null)) {
        throw new TypeError('Object.keys called on non-object');
      }

      var result = [], prop, i;

      for (prop in obj) {
        if (hasOwnProperty.call(obj, prop)) {
          result.push(prop);
        }
      }

      if (hasDontEnumBug) {
        for (i = 0; i < dontEnumsLength; i++) {
          if (hasOwnProperty.call(obj, dontEnums[i])) {
            result.push(dontEnums[i]);
          }
        }
      }
      return result;
    };
  }());
}

String.prototype.capitalizeFirstLetter = function() {
    return this.charAt(0).toUpperCase() + this.slice(1);
}

function extractValue(obj, key) {
    var keys, result;
    if (key) {
      keys= key.split('.');
      result = obj;
      for (var i = 0; i < keys.length; i++) {
        result = result[keys[i]];
      }
    }
    else {
      result = obj;
    }
    return result;
  }

function getParameterByName(url,name) {
	name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
	var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex
			.exec(url);
	return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g,
			" "));
}
function calDiffCrewLink(previous,current)
{
	
	var startTime,hour,min,splittingStartingTime,totalDuration,noDaysPassed,startingDayValue;
	var endDayValue, endTime,diffDayValue,noOfCrew=0;

	splittingStartingTime = previous.startTime;
	for (var j = 0; j < Days.length; j++) {
		if (previous.startDay == Days[j]) 
		{
			startingDayValue = j;
			//("day value : " +startingDayValue);
		}
		if (current.startDay == Days[j]) {
			endDayValue = j;
			endTime = current.startTime;
		//	("end day value : "+startingDayValue);
		}
	}
//calculate arrival time in minutes
var departureTime = [];
departureTime = previous.startTime.split(':')
var hourStarting = $.trim(departureTime[0]);
var minStarting = $.trim(departureTime[1]);


//($scope.hourStarting + "\t\t "+ $scope.minStarting);

var hourAndMinuteStartingTime = parseInt(hourStarting)* 60+ parseInt(minStarting)+previous.duration;


//("starting time duration : " +$scope.hourAndMinuteStartingTime);
//if departure time plus duration exceeds next day or further
if(hourAndMinuteStartingTime>1440)
{
	var noOfDays = parseInt(hourAndMinuteStartingTime/1440);
	//("nmber of Days : " +noOfDays );
	startingDayValue = parseInt(startingDayValue)+parseInt(noOfDays);
	//("starting day value : " +startingDayValue);
	//("$scope.hourAndMinuteStartingTime day value is : " +$scope.hourAndMinuteStartingTime);
	hourAndMinuteStartingTime = hourAndMinuteStartingTime-(noOfDays*1440);
	diffDayValue = parseInt(endDayValue - startingDayValue-1);
	//("days diff : " +diffDayValue);
	//("$scope.hourAndMinuteStartingTime day value is : " +$scope.hourAndMinuteStartingTime);
}
else
{
		diffDayValue = endDayValue - startingDayValue - 1;
}

var arrEndTime = [];
arrEndTime = current.startTime.split(':')
hourEnd = $.trim(arrEndTime[0]);
minEnd = $.trim(arrEndTime[1]);
//($scope.hourEnd + "\t\t "+ $scope.minEnd);
var hourAndMinuteEndTime = parseInt(hourEnd)* 60 + parseInt(minEnd);
if (diffDayValue >= 0) {
//	("$scope.hourAndMinuteStartingTime day value is 2: " +(24 * 60 - $scope.hourAndMinuteStartingTime));
	//("$scope.hourAndMinuteStartingTime day value is 3: " +$scope.hourAndMinuteEndTime);
	//("diffDayValue day value is 4: " +diffDayValue);
	headStationRest = parseInt(diffDayValue * 24 * 60) + parseInt(24 * 60 - hourAndMinuteStartingTime)+parseInt(hourAndMinuteEndTime);
	//("head quarter rest1 : "+ headStationRest);
}
else if(diffDayValue==-1)
  {
	headStationRest = (hourAndMinuteEndTime-hourAndMinuteStartingTime);
	  //	("head quarter rest2 : " +headStationRest);
  }

else {
	//($scope.days.length + "\t\t "+ endDayValue + "\t\t"+ startingDayValue);
	diffDayValue = (Days.length - 1
			- startingDayValue) + endDayValue;
	//("diffDayValue : "+ diffDayValue);
	headStationRest = parseInt(diffDayValue * 24 * 60+ 24 * 60- hourAndMinuteStartingTime+ hourAndMinuteEndTime);
	//("hourAndMinuteStartingTime : "+ $scope.hourAndMinuteStartingTime);
	//("head quarter rest2 : "+ headStationRest);
}

return headStationRest;
}
var noOfCrew=0;
function calNumberOfCrew(Object,length)
{
	
	
	var currentDay;
	
	var startTime,hour,min,splittingStartingTime,totalDuration,noDaysPassed,nextDayValue=0;
	var endDayValue, endTime,diffDayValue,noOfCrew=0;
	
	if(length==1)
		{
				currentDay=getDayId(Object.startDay);
				//currentDay=Object.startDay;
				("currentDay value : " +currentDay);
		}
	if(currentDay!=null)
		{
				nextDayValue=getDayId(Object.startDay);
				if(currentDay==nextDayValue)
					{
						++noOfCrew;
						return noOfCrew;
					}
				
		}

/*	var diff = endDayValue - startingDayValue;
	if (diff > 0)
		noOfCrew = noOfCrew + diff;
	else {
		noOfCrew = noOfCrew
				+ Days.length - 1
				- startingDayValue + endDayValue
				+ 1;
	}
*//*("noOfCrew : "
		+ noOfCrew);
*/	

return noOfCrew;

}