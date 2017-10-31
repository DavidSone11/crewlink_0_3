package com.mathologic.projects.crewlink.utils.converters;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by vivek on 3/10/15.
 */
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        return timestamp;
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
    	LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return localDateTime;
    }
}