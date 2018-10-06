package com.pollfish.client;

import com.pollfish.client.config.ApplicationProperties;
import com.pollfish.client.service.LoggingEventService;
import com.pollfish.core.LoggingService;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LoggingClientApp.class)
@TestPropertySource("classpath:config/application.yml")
public class LoggingClientIntTest {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingClientIntTest.class);

    private Thread serverThread;

    @Autowired
    LoggingEventService loggingEventService;

    @Autowired
    private ApplicationProperties applicationProperties;

    private MockHandler handler;

    @Before
    public void init() {
        handler = new MockHandler();
        LoggingService.Processor processor = new LoggingService.Processor(handler);
        Runnable simple = () -> simple(processor);
        serverThread = new Thread(simple);
    }

    @Test
    public void successfullySendLoggingEventTest() {
        // Start the server
        serverThread.start();

        // Send event from client
        loggingEventService.startSendingLoggingEvents();

        // Verify that the server received it
        assertThat(handler.getEvent()).isNotNull();
        LOG.info("Event received.");
    }

    @Test
    public void tryToSendWhileServerIsDownTest() {

        // Send event from client
        // If the exceptions are handled correctly, the test will finish without errors
        loggingEventService.startSendingLoggingEvents();
    }

    private void simple(LoggingService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(applicationProperties.getServer().getPort());
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));
            server.serve();
        } catch (TTransportException e) {
            LOG.error(e.getMessage());
        }
    }


}
