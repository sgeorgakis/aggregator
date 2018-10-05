package com.pollfish.client.service.impl;

import com.pollfish.client.config.ApplicationProperties;
import com.pollfish.client.service.LoggingEventGenerationService;
import com.pollfish.client.service.LoggingEventHandlerService;
import com.pollfish.client.util.RandomEventUtil;
import com.pollfish.core.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class RandomEventGenerationServiceImpl extends Thread implements LoggingEventGenerationService {

    private static final Logger LOG = LoggerFactory.getLogger(RandomEventGenerationServiceImpl.class);

    private final ApplicationProperties applicationProperties;
    private final LoggingEventHandlerService loggingEventHandlerService;

    private boolean shouldRun;

    public RandomEventGenerationServiceImpl(ApplicationProperties applicationProperties, LoggingEventHandlerService loggingEventHandlerService) {
        this.applicationProperties = applicationProperties;
        this.loggingEventHandlerService = loggingEventHandlerService;
        this.shouldRun = true;
    }

    /**
     * Send events to the server
     */
    @Override
    public void sendEvents() {
        this.start();
    }

    /**
     * Generates random events and sends them to the server.
     * Runs until the shouldRun attribute is set to false.
     */
    public void run() {
        while (shouldRun) {
            try {
                LoggingEvent event = RandomEventUtil.generateRandomEvent();
                boolean wasSent = loggingEventHandlerService.sendLoggingEvent(event);
                if (wasSent) {
                    LOG.info("Sent event to server: {}", event);
                }
                Thread.sleep(applicationProperties.getInterval());
            } catch (InterruptedException e) {
                LOG.error("Thread interrupted");
                LOG.error(e.getMessage());
            }
        }
    }

    public void setShouldRun(boolean shouldRun) {
        this.shouldRun = shouldRun;
    }

    public boolean shouldRun() {
        return shouldRun;
    }

    @PreDestroy
    public void onDestroy() {
        setShouldRun(false);
    }
}
