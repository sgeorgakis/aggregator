package com.pollfish.client.service.impl;

import com.pollfish.client.config.ApplicationProperties;
import com.pollfish.client.service.LoggingEventGenerationService;
import com.pollfish.client.util.RandomEventUtil;
import com.pollfish.core.LoggingEvent;
import com.pollfish.core.LoggingService;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class RandomEventGenerationServiceImpl extends Thread implements LoggingEventGenerationService {

    private static final Logger LOG = LoggerFactory.getLogger(RandomEventGenerationServiceImpl.class);

    private final LoggingService.Client client;
    private final ApplicationProperties applicationProperties;

    private boolean shouldRun;

    public RandomEventGenerationServiceImpl(LoggingService.Client client, ApplicationProperties applicationProperties) {
        this.client = client;
        this.applicationProperties = applicationProperties;
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
        try {
            while (shouldRun) {
                LoggingEvent event = RandomEventUtil.generateRandomEvent();
                client.pushLoggingEvent(event);
                LOG.info("Sent event to server: {}", event);
                Thread.sleep(applicationProperties.getInterval());
            }
        } catch (InterruptedException e) {
            LOG.error("Thread interrupted");
            LOG.error(e.getMessage());
            Thread.currentThread().interrupt();
        } catch (TException e) {
            LOG.error("Thread interrupted by client exception");
            LOG.error(e.getMessage());
            Thread.currentThread().interrupt();
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
