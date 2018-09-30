package com.pollfish.client;

import com.pollfish.core.LoggingService;
import com.pollfish.core.util.ConfigurationLoader;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingClient {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingClient.class);

    public static void main(String[] args) {
        try {
            TTransport transport;
            String ip = ConfigurationLoader.getIp();
            int port = ConfigurationLoader.getPort();
            LOG.debug("Connecting to server: {}:{}", ip, port);
            transport = new TSocket(ip, port);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            LoggingService.Client client = new LoggingService.Client(protocol);

            perform(client);

            transport.close();
        } catch (TException e) {
            LOG.error(e.getMessage());
        }
    }

    private static void perform(LoggingService.Client client) {

        EventGenerator eventGenerator = new EventGenerator(client);
        eventGenerator.run();

    }
}
