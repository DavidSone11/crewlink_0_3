package com.mathologic.projects.crewlink.utils.converters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import com.mathologic.projects.crewlink.custom.models.DateTimeModel;

public class TimeUtils {
	public DateTimeFormatter formatter = DateTimeFormatter
			.ofPattern("d/MM/yyyy H:m:s");
	
	public int convertDateTimeModelToNumber(DateTimeModel dateTimeObj,String target) {
	    if (target == null) {
	        target = "";
	    }
	    target = target.toLowerCase();

	    String[] timePartsStr = dateTimeObj.time.split(":");
	    int[] timeParts = new int[timePartsStr.length];
	    timeParts[0] = Integer.parseInt(timePartsStr[0]);
	    timeParts[1] = Integer.parseInt(timePartsStr[1]);
	    if((timeParts[0]>23 || timeParts[0]<0)&&(timeParts[1]>59 || timeParts[1]<0)){
	        throw new RuntimeException("Not valid dateTimeObj.time passed to convertDateTimeObj()");
	    }

	    int mins = (dateTimeObj.day * 1440)
	        + (timeParts[0] * 60)
	        + (timeParts[1]);

	    int result = 0;
	    switch (target) {
	        case "mins": case "min": case "minutes": case "minute":
	            result = mins;
	            break;
	        case "hrs": case "hr": case "hours": case "hour":
	            result = (mins / 60);
	            break;
	        case "days": case "day":
	            result = (mins / 1440);
	            break;
	        default:
	            result = mins;
	            break;
	    }
	    return result;
	} 
	
	public DateTimeModel convertNumberToDateTimeModel(int number, String type) {
	    if (type == null) {
	        type = "";
	    }
	    type = type.toLowerCase();

	    
	    int day = -1;
	    int hrs = -1;
	    int mins = -1;
	    switch (type) {
	        case "mins": case "min": case "minutes": case "minute":
	            day = (int) Math.floor(number / 1440);
	            number = number % 1440;
	            hrs = (int) Math.floor(number / 60);
	            number = number % 60;
	            mins = (int) Math.floor(number);
	            if (mins > 60) {
	                throw new Error("Number not in correct type given");
	            }
	            break;
	        case "hrs": case "hr": case "hours": case "hour":
	            day = (int) Math.floor(number / 1440);
	            number = number % 1440;
	            hrs = (int) Math.floor(number / 60);
	            if (hrs > 24) {
	                throw new Error("Number not in correct type given");
	            }
	            break;
	        case "days": case "day":
	            day = (int) Math.floor(number / 1440);
	            if (day > 24) {
	                throw new Error("Number not in correct type given");
	            }
	            break;
	        default:
	            day = (int) Math.floor(number / 1440);
	            number = number % 1440;
	            hrs = (int) Math.floor(number / 60);
	            number = number % 60;
	            mins = (int) Math.floor(number);
	            if (mins > 60) {
	                throw new Error("Number not in correct type given");
	            }
	            break;
	    }
	    DateTimeModel result = new DateTimeModel();
	    if (day != -1) {
	        result.days = day;
	        result.day = day % 7;
	    }
	    if (hrs != -1 && mins == -1) {
	        result.time = (hrs < 10) ? "0" : "" + hrs + ":00";
	    }
	    else if (hrs != -1 && mins != -1) {
	        result.time = "";
	        result.time += ((hrs < 10) ? "0" : "") + hrs;
	        result.time += ":" + ((mins < 10) ? "0" : "") + mins;
	    }
	    return result;
	}
	
	public Long findDiff(Integer fromDay, String fromTime, Integer toDay, String toTime,ChronoUnit unit){
		Long result = findDiffWeekly(fromDay, fromTime, toDay, toTime, unit);
		if(result<0){
			switch(unit){
			case MINUTES:
				result = (7*24*60) + result;
				break;
				
			case HOURS:
				result = (7*24)+result;
				break;
				
			case SECONDS:
				result = (7*24*60*60) + result;
				break;
			}
		}
		return result;
	}
	
	public Long findDiffWeekly(Integer fromDay, String fromTime, Integer toDay, String toTime,ChronoUnit unit){
		LocalDateTime fromDateTime = LocalDateTime.parse("1/01/2012 "+ fromTime, formatter);
		
		Integer diff = toDay - fromDay;
		if (diff < 0) {
			diff = 7 + diff;
		}
		
		LocalDateTime toDateTime = LocalDateTime.parse((1 + diff) + "/01/2012 "+ toTime,formatter);
		return unit.between(fromDateTime, toDateTime);
	}
	
	public Map<String,String> calculateNextDayTime(Integer day, String time, Long value,ChronoUnit unit, String operation){
		Map<String, String> result = new HashMap<String, String>();
		LocalDateTime fromDateTime = LocalDateTime.parse((day+1)+"/01/2012 "+ time, formatter);
		LocalDateTime toDateTime = null;
		switch(operation){
		case "plus": case "+":
			toDateTime = fromDateTime.plus(value, unit);
			break;
		case "minus": case "-":
			toDateTime = fromDateTime.minus(value, unit);
			break;
		}
		result.put("time", toDateTime.getHour()+":"+toDateTime.getMinute()+":"+toDateTime.getSecond());
		result.put("day", (toDateTime.getDayOfWeek().getValue()%7)+"" );
		return result;
	}
}
