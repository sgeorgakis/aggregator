package com.pollfish.server.config;

import com.pollfish.core.LoggingService;
import com.pollfish.server.service.LoggingHandler;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SimpleServerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleServerConfig.class);

    private final ApplicationProperties applicationProperties;


    public SimpleServerConfig(ApplicationProperties applicationProperties, LoggingHandler handler) {
        this.applicationProperties = applicationProperties;
        createProcessor(handler);
    }

    private void createProcessor(LoggingHandler handler) {
        LoggingService.Processor processor = new LoggingService.Processor(handler);
        Runnable simple = () -> simple(processor);
        new Thread(simple).start();
    }

    private void simple(LoggingService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(applicationProperties.getServer().getPort());
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));
            LOG.info("Starting the simple server at port {}", applicationProperties.getServer().getPort());
            server.serve();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}
