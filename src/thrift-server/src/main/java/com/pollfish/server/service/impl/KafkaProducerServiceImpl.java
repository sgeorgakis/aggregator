package com.pollfish.server.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollfish.core.LoggingEvent;
import com.pollfish.server.config.ApplicationProperties;
import com.pollfish.server.service.ProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaProducerServiceImpl implements ProducerService {

    private static final String TOPIC = "logging";
    private static final Logger LOG = LoggerFactory.getLogger(KafkaProducerServiceImpl.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ApplicationProperties applicationProperties;

    private ObjectMapper mapper;

    public KafkaProducerServiceImpl(KafkaTemplate<String, String> kafkaTemplate, ApplicationProperties applicationProperties) {
        this.kafkaTemplate = kafkaTemplate;
        this.applicationProperties = applicationProperties;
        this.mapper = new ObjectMapper();
    }

    /**
     * Forwards a {@link LoggingEvent} object to a broker
     *
     * @param event the event to forward
     */
    @Override
    public void forwardLoggingEvent(LoggingEvent event) {
        try {
            String message = mapper.writeValueAsString(event);
            ListenableFuture<SendResult<String, String>> result = kafkaTemplate.send(TOPIC, message);
            result.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onSuccess(SendResult<String, String> result) {
                    LOG.info("Message was sent");
                }

                @Override
                public void onFailure(Throwable ex) {
                    LOG.error(ex.getMessage());
                }
            });
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage());
        }
    }
}
