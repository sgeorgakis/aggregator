package com.pollfish.consumer.service;

import com.pollfish.core.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "logging", groupId = "logging-event")
    public void listen(LoggingEvent message) {
        LOG.info("Received Messasge: {}", message);
    }
}
