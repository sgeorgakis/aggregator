package com.pollfish.client.domain;

import com.pollfish.client.util.RandomEventUtil;
import com.pollfish.core.LoggingService;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventGenerator extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(EventGenerator.class);
    private final LoggingService.Client client;
    private int interval;

    private boolean shouldRun;

    public EventGenerator(LoggingService.Client client, int interval) {
        this.client = client;
        this.interval = interval;
        this.shouldRun = true;
    }

    public void run() {
        try {
            while (shouldRun) {
                client.pushLoggingEvent(RandomEventUtil.generateRandomEventUtil());
                Thread.sleep(interval);
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
}
