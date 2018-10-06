package com.pollfish.client;

import com.pollfish.core.LoggingEvent;
import com.pollfish.core.LoggingService;

public class MockHandler implements LoggingService.Iface {

    private LoggingEvent event;

    @Override
    public void pushLoggingEvent(LoggingEvent event) {
        this.event = event;
    }

    public LoggingEvent getEvent() {
        return event;
    }
}
