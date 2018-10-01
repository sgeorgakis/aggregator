package com.pollfish.client.config;

import com.pollfish.core.LoggingService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
public class TTransportConfig {

    private static final Logger LOG = LoggerFactory.getLogger(TTransportConfig.class);

    private final ApplicationProperties applicationProperties;
    private TTransport tTransport;

    public TTransportConfig(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    TTransport tTransport() {
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
        tTransport = transport;
        return transport;
    }

    @Bean
    public LoggingService.Client client(TTransport transport) {
        TProtocol protocol = new TBinaryProtocol(transport);
        return new LoggingService.Client(protocol);
    }

    @PreDestroy
    public void destroy() {
        if (tTransport != null) {
            LOG.debug("Closing transport");
            tTransport.close();
        }
    }

}
