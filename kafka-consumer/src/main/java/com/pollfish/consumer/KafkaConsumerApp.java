package com.pollfish.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class KafkaConsumerApp {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerApp.class);

    public static void main(String[] args) {
        LOG.info("Starting Kafka Consumer");
        SpringApplication.run(KafkaConsumerApp.class, args);
    }
}
