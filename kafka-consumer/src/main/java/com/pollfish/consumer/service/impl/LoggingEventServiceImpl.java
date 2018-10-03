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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.pollfish.consumer.config.Constants.DATE_PARTITION_FORMAT;

@Service
public class LoggingEventServiceImpl implements LoggingEventService {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingEventServiceImpl.class);

    private final LoggingEventRepository repository;
    private final LoggingEventMapper mapper;

    private DateFormat dateFormat;

    public LoggingEventServiceImpl(LoggingEventRepository repository, LoggingEventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.dateFormat = new SimpleDateFormat(DATE_PARTITION_FORMAT);
    }

    @Override
    public LoggingEventDTO save(LoggingEventDTO eventDTO) {
        LoggingEvent event = mapper.toEntity(eventDTO);
        LOG.info("{}", event);
        repository.save(mapper.toEntity(eventDTO));
        return eventDTO;
    }

    @Override
    public List<LoggingEventDTO> findAll() {
        return mapper.toDto(repository.findAll());
    }

    @Override
    public List<LoggingEventDTO> findByDateAndAppAndLevel(Date date, int app, LevelType level) {
        String datePartition = dateFormat.format(date);
        return mapper.toDto(repository.findByDatePartitionAndAppAndLevel(datePartition, app, level.name()));
    }
}
