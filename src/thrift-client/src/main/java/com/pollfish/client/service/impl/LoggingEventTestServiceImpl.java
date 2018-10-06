package com.pollfish.client.service.impl;

import com.pollfish.client.config.Constants;
import com.pollfish.client.service.LoggingEventGenerationService;
import com.pollfish.client.service.LoggingEventService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(Constants.TEST_PROFILE)
public class LoggingEventTestServiceImpl implements LoggingEventService {

    private final LoggingEventGenerationService loggingEventGenerationService;

    public LoggingEventTestServiceImpl(LoggingEventGenerationService loggingEventGenerationService) {
        this.loggingEventGenerationService = loggingEventGenerationService;
    }

    @Override
    public void startSendingLoggingEvents() {
        loggingEventGenerationService.sendEvents();
    }
}
