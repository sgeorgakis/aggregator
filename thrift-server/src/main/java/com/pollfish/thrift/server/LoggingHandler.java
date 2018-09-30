package com.pollfish.thrift.server;

import com.pollfish.core.LoggingEvent;
import com.pollfish.core.LoggingService;

public class LoggingHandler implements LoggingService.Iface {

    @Override
    public void pushLoggingEvent(LoggingEvent event) {
        System.out.println(event);
    }
}
