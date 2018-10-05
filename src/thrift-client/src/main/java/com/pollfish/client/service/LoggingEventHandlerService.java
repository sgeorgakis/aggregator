package com.pollfish.client.service;

import com.pollfish.core.LoggingEvent;

public interface LoggingEventHandlerService {

    boolean sendLoggingEvent(LoggingEvent event);
}
