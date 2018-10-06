package com.pollfish.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class LoggingConsumerApp {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingConsumerApp.class);

    public static void main(String[] args) {
        LOG.info("Starting Logging Consumer");
        SpringApplication.run(LoggingConsumerApp.class, args);
    }
}
