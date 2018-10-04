package com.pollfish.consumer.service;

import com.pollfish.consumer.service.dto.LevelType;
import com.pollfish.consumer.service.dto.LoggingEventDTO;

import java.util.List;

public interface LoggingEventService {

    /**
     * Save a {@link LoggingEventDTO} object
     *
     * @param eventDTO the {@link LoggingEventDTO} object
     * @return the persisted object
     */
    LoggingEventDTO save(LoggingEventDTO eventDTO);

    /**
     * Find all the {@link LoggingEventDTO} objects
     *
     * @return a list containing the {@link LoggingEventDTO} objects
     */
    List<LoggingEventDTO> findAll();

    /**
     * Find {@link LoggingEventDTO} objects according to date, app and level
     *
     * @param date the date (yyyMMdd format)
     * @param app the app generated the {@link LoggingEventDTO}
     * @param level the logging level of the event
     * @return a list containing the {@link LoggingEventDTO} objects
     */
    List<LoggingEventDTO> findByDayAndAppAndLevel(String date, int app, LevelType level);
}
