package com.mathologic.projects.crewlink.utils.converters;

import org.springframework.stereotype.Service;

/**
 * Created by vivek on 31/10/15.
 */
@Service
public interface CsvToDatabase {

    boolean processRecord(String[] columns);
}
