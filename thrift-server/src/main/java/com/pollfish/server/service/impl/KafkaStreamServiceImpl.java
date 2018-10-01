package com.pollfish.server.service.impl;

import com.pollfish.core.LoggingEvent;
import com.pollfish.server.config.ApplicationProperties;
import com.pollfish.server.service.StreamService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaStreamServiceImpl implements StreamService {

    private final KafkaTemplate<String, LoggingEvent> kafkaTemplate;
    private ApplicationProperties applicationProperties;

    public KafkaStreamServiceImpl(KafkaTemplate<String, LoggingEvent> kafkaTemplate, ApplicationProperties applicationProperties) {
        this.kafkaTemplate = kafkaTemplate;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void forwardLoggingEvent(LoggingEvent event) {
        kafkaTemplate.send(applicationProperties.getKafka().getTopic(), event);
    }
}
