package com.pollfish.client;

import com.pollfish.client.util.RandomEventUtil;
import com.pollfish.core.LoggingService;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class EventGenerator extends Thread implements KeyListener {

    private static final Logger LOG = LoggerFactory.getLogger(EventGenerator.class);
    private boolean shouldRun;
    private LoggingService.Client client;

    public EventGenerator(LoggingService.Client client) {
        this.client = client;
        this.shouldRun = true;
    }

    public void run() {
        try {
            while (shouldRun) {
                client.pushLoggingEvent(RandomEventUtil.generateRandomEventUtil());
                Thread.sleep(500L);
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


    @Override
    public void keyTyped(KeyEvent event) {
    }

    @Override
    public void keyPressed(KeyEvent event) {
        this.shouldRun = false;
    }

    @Override
    public void keyReleased(KeyEvent event) {
    }
}
