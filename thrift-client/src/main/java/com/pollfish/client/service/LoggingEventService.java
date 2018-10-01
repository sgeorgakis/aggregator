package com.pollfish.client.service;

import com.pollfish.client.config.ApplicationProperties;
import com.pollfish.client.domain.EventGenerator;
import com.pollfish.core.LoggingService;
import org.springframework.stereotype.Service;

@Service
public class LoggingEventService {

    private final LoggingService.Client client;
    private final ApplicationProperties applicationProperties;
    private EventGenerator eventGenerator;

    public LoggingEventService(LoggingService.Client client, ApplicationProperties applicationProperties) {
        this.client = client;
        this.applicationProperties = applicationProperties;
        eventGenerator = new EventGenerator(client, applicationProperties.getInterval());
        eventGenerator.run();
    }
}
