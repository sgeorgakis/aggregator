package com.pollfish.server.service;

import com.pollfish.core.LoggingEvent;

public interface StreamService {

    void forwardLoggingEvent(LoggingEvent event);
}
