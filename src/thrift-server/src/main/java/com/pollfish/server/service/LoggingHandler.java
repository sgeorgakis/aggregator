package com.pollfish.server.service;

import com.pollfish.core.LoggingEvent;
import com.pollfish.core.LoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingHandler implements LoggingService.Iface {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingHandler.class);

    private final ProducerService producerService;

    public LoggingHandler(ProducerService producerService) {
        this.producerService = producerService;
    }

    /**
     * Receives a {@link LoggingEvent} from a client
     *
     * @param event
     */
    @Override
    public void pushLoggingEvent(LoggingEvent event) {
        LOG.info("Received event: {}. Forwarding to broker.", event);
        producerService.forwardLoggingEvent(event);
    }
}
