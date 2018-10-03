package com.pollfish.consumer.service;

import com.pollfish.consumer.service.dto.LevelType;
import com.pollfish.consumer.service.dto.LoggingEventDTO;

import java.util.Date;
import java.util.List;

public interface LoggingEventService {

    LoggingEventDTO save(LoggingEventDTO eventDTO);

    List<LoggingEventDTO> findAll();

    List<LoggingEventDTO> findByDateAndAppAndLevel(Date date, int app, LevelType level);
}
