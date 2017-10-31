package com.mathologic.projects.crewlink.utils.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by vivek on 3/10/15.
 */
public class LocalTimeDeSerializer extends JsonDeserializer<LocalTime> {

    @Override
    public LocalTime deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;
        return LocalTime.parse(jp.getText(),formatter);
    }
}