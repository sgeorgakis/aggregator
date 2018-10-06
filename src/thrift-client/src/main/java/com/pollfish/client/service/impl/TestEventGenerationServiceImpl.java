package com.pollfish.client.service.impl;

import com.pollfish.client.config.Constants;
import com.pollfish.client.service.LoggingEventGenerationService;
import com.pollfish.client.service.LoggingEventHandlerService;
import com.pollfish.client.util.RandomEventUtil;
import com.pollfish.core.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(Constants.TEST_PROFILE)
public class TestEventGenerationServiceImpl implements LoggingEventGenerationService {

    private static final Logger LOG = LoggerFactory.getLogger(RandomEventGenerationServiceImpl.class);

    private final LoggingEventHandlerService loggingEventHandlerService;

    public TestEventGenerationServiceImpl(LoggingEventHandlerService loggingEventHandlerService) {
        this.loggingEventHandlerService = loggingEventHandlerService;
    }

    /**
     * Send events to the server
     */
    @Override
    public void sendEvents() {
        LoggingEvent event = RandomEventUtil.generateRandomEvent();
        boolean wasSent = loggingEventHandlerService.sendLoggingEvent(event);
        if (wasSent) {
            LOG.info("Sent event to server: {}", event);
        }
    }
}
