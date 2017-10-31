package com.mathologic.projects.crewlink.utils.converters;

import java.sql.Time;
import java.time.LocalTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by vivek on 3/10/15.
 */
@Converter(autoApply = true)
public class LocalTimeConverter implements AttributeConverter<LocalTime, Time> {


    @Override
    public Time convertToDatabaseColumn(LocalTime localTime) {
    	if(localTime == null){
    		return null;
    	}
        Time sqlTime = Time.valueOf(localTime);
        return sqlTime;
    }

    @Override
    public LocalTime convertToEntityAttribute(Time time) {
    	if(time == null){
    		return null;
    	}
    	
        LocalTime localTime = time.toLocalTime();
        return localTime;
    }
}