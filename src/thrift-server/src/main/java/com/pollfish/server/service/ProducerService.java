package com.pollfish.server.service;

import com.pollfish.core.LoggingEvent;

public interface ProducerService {

    /**
     * Forwards a {@link LoggingEvent} object to a broker
     *
     * @param event the event to forward
     */
    void forwardLoggingEvent(LoggingEvent event);
}
