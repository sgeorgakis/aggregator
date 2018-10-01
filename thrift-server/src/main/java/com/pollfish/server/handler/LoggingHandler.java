package com.pollfish.server.handler;

import com.pollfish.core.LoggingEvent;
import com.pollfish.core.LoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingHandler implements LoggingService.Iface {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingHandler.class);

    @Override
    public void pushLoggingEvent(LoggingEvent event) {
        LOG.info("{}", event);
    }
}
