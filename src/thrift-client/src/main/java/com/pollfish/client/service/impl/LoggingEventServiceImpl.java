package com.pollfish.client.service.impl;

import com.pollfish.client.service.LoggingEventGenerationService;
import com.pollfish.client.service.LoggingEventService;
import org.springframework.stereotype.Service;

@Service
public class LoggingEventServiceImpl implements LoggingEventService {

    private final LoggingEventGenerationService loggingEventGenerationService;

    public LoggingEventServiceImpl(LoggingEventGenerationService loggingEventGenerationService) {
        this.loggingEventGenerationService = loggingEventGenerationService;
        startSendingLoggingEvents();
    }

    @Override
    public void startSendingLoggingEvents() {
        loggingEventGenerationService.sendEvents();
    }
}
