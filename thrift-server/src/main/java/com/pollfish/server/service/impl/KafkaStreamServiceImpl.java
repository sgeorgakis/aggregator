package com.pollfish.server.service.impl;

import com.pollfish.core.LoggingEvent;
import com.pollfish.server.config.ApplicationProperties;
import com.pollfish.server.service.StreamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class KafkaStreamServiceImpl implements StreamService {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamServiceImpl.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private ApplicationProperties applicationProperties;

    public KafkaStreamServiceImpl(KafkaTemplate<String, String> kafkaTemplate, ApplicationProperties applicationProperties) {
        this.kafkaTemplate = kafkaTemplate;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void forwardLoggingEvent(LoggingEvent event) {
        ListenableFuture<SendResult<String, String>> result = kafkaTemplate.send(applicationProperties.getKafka().getTopic(), event.getM());
    }
}
