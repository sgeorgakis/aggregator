package com.pollfish.server;

import com.pollfish.server.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
@EnableConfigurationProperties({ApplicationProperties.class})
public class LoggingServerApp {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingServerApp.class);

    public static void main(String[] args) {
        LOG.info("Starting LoggingClient");
        SpringApplication.run(LoggingServerApp.class, args);
    }
}

