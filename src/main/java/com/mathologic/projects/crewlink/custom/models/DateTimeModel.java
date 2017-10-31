package com.mathologic.projects.crewlink.custom.models;

import com.mathologic.projects.crewlink.models.Day;

public class DateTimeModel {
	public int day;
	public String time;
	public int days;
	public int number;
	public String numberType;
	public DateTimeModel(int day, String time) {
		super();
		this.day = day;
		this.time = time;
		this.number = convertDateTimeModelToNumber(this,"mins");
		this.numberType = "mins";
	}
	public DateTimeModel() {
		super();
	}
	public DateTimeModel(int number) {
		super();
		DateTimeModel dm = convertNumberToDateTimeModel(number,"mins");
		this.day = dm.day;
		this.days = dm.days;
		this.time = dm.time;
		this.number = dm.number;
		this.numberType = dm.numberType;
	}
	
	@Override
	public String toString() {
		return " DateTimeModel [day=" + Day.values()[day] + ", time=" + time + ", days="
				+ days + ", number=" + number + ", numberType=" + numberType
				+ "] ";
	}
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
	    DateTimeModel result = new DateTimeModel();
	    result.number = number;
	    result.numberType = type;
	    
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
	
}
