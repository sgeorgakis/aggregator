package com.pollfish.consumer.service.impl;

import com.pollfish.consumer.domain.LoggingEvent;
import com.pollfish.consumer.repository.LoggingEventRepository;
import com.pollfish.consumer.service.LoggingEventService;
import com.pollfish.consumer.service.dto.LevelType;
import com.pollfish.consumer.service.dto.LoggingEventDTO;
import com.pollfish.consumer.service.mapper.LoggingEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoggingEventServiceImpl implements LoggingEventService {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingEventServiceImpl.class);

    private final LoggingEventRepository repository;
    private final LoggingEventMapper mapper;

    public LoggingEventServiceImpl(LoggingEventRepository repository, LoggingEventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Save a {@link LoggingEventDTO} object
     *
     * @param eventDTO the {@link LoggingEventDTO} object
     * @return the persisted object
     */
    @Override
    public LoggingEventDTO save(LoggingEventDTO eventDTO) {
        LoggingEvent event = mapper.toEntity(eventDTO);
        return mapper.toDto(repository.save(mapper.toEntity(eventDTO)));
    }

    /**
     * Find all the {@link LoggingEventDTO} objects
     *
     * @return a list containing the {@link LoggingEventDTO} objects
     */
    @Override
    public List<LoggingEventDTO> findAll() {
        return mapper.toDto(repository.findAll());
    }

    /**
     * Find {@link LoggingEventDTO} objects according to date, app and level
     *
     * @param date the date in yyyyMMdd format
     * @param app the app generated the {@link LoggingEventDTO}
     * @param level the logging level of the event
     * @return a list containing the {@link LoggingEventDTO} objects
     */
    @Override
    public List<LoggingEventDTO> findByDayAndAppAndLevel(String date, int app, LevelType level) {
        return mapper.toDto(repository.findByDatePartitionAndAppAndLevel(date, app, level.name()));
    }
}
