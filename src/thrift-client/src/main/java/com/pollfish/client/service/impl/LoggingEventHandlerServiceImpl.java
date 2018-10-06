package com.pollfish.client.service.impl;

import com.pollfish.client.config.ApplicationProperties;
import com.pollfish.client.service.LoggingEventHandlerService;
import com.pollfish.core.LoggingEvent;
import com.pollfish.core.LoggingService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class LoggingEventHandlerServiceImpl implements LoggingEventHandlerService {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingEventHandlerServiceImpl.class);

    private final ApplicationProperties applicationProperties;

    private LoggingService.Client client;

    public LoggingEventHandlerServiceImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public boolean sendLoggingEvent(LoggingEvent event) {
        boolean wasSent;
        connectClient();
        try {
            client.pushLoggingEvent(event);
            wasSent = true;
        } catch (TException e) {
            LOG.error("A server exception occurred");
            LOG.error(e.getMessage());
            wasSent = false;
        }
        closeTransport();
        return wasSent;
    }

    private void connectClient() {
        TTransport transport = null;
        try {
            String ip = applicationProperties.getServer().getIp();
            int port = applicationProperties.getServer().getPort();
            LOG.debug("Connecting to server: {}:{}", ip, port);
            transport = new TSocket(ip, port);
            transport.open();
        } catch (TTransportException e) {
            LOG.error(e.getMessage());
        }
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new LoggingService.Client(protocol);
    }


    @PreDestroy
    public void closeTransport() {
        if (client.getInputProtocol().getTransport().isOpen()) {
            LOG.debug("Closing input transport");
            client.getInputProtocol().getTransport().close();
        }
        if (client.getOutputProtocol().getTransport().isOpen()) {
            LOG.debug("Closing input transport");
            client.getOutputProtocol().getTransport().close();
        }
    }
}
