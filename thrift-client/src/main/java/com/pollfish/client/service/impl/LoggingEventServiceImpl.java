package com.pollfish.client.service.impl;

import com.pollfish.client.config.ApplicationProperties;
import com.pollfish.client.service.LoggingEventGenerationService;
import com.pollfish.client.service.LoggingEventService;
import org.springframework.stereotype.Service;

@Service
public class LoggingEventServiceImpl implements LoggingEventService {

    private final LoggingEventGenerationService loggingEventGenerationService;
    private final ApplicationProperties applicationProperties;

    public LoggingEventServiceImpl(LoggingEventGenerationService loggingEventGenerationService, ApplicationProperties applicationProperties) {
        this.loggingEventGenerationService = loggingEventGenerationService;
        this.applicationProperties = applicationProperties;
        loggingEventGenerationService.sendEvents();
    }
}
