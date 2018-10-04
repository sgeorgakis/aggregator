package com.pollfish.server.service;

import com.pollfish.core.LoggingEvent;
import com.pollfish.core.LoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingHandler implements LoggingService.Iface {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingHandler.class);

    private final StreamService streamService;

    public LoggingHandler(StreamService streamService) {
        this.streamService = streamService;
    }

    /**
     * Receives a {@link LoggingEvent} from a client
     *
     * @param event
     */
    @Override
    public void pushLoggingEvent(LoggingEvent event) {
        streamService.forwardLoggingEvent(event);
    }
}
