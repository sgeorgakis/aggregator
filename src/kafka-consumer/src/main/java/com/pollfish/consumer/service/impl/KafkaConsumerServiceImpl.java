package com.pollfish.consumer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollfish.consumer.service.ConsumerService;
import com.pollfish.consumer.service.LoggingEventService;
import com.pollfish.consumer.service.dto.LoggingEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KafkaConsumerServiceImpl implements ConsumerService {

    private static final String TOPIC = "logging";
    private static final String GROUP_ID = "logging-event";
    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerServiceImpl.class);

    private final LoggingEventService loggingEventService;

    private ObjectMapper mapper;

    public KafkaConsumerServiceImpl(LoggingEventService loggingEventService) {
        this.loggingEventService = loggingEventService;
        this.mapper = new ObjectMapper();
    }

    /**
     * Listen for a message to consume
     * @param message the message
     */
    @Override
    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void listen(String message) {
        try {
            LoggingEventDTO eventDTO = mapper.readValue(message, LoggingEventDTO.class);
            LOG.info("Received Message: {}", eventDTO);
            loggingEventService.save(eventDTO);
        } catch (IOException e) {
            LOG.error("Could not convert message {}", message);
            LOG.error(e.getMessage());
        }
    }
}
