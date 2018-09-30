package com.pollfish.thrift.server;

import com.pollfish.core.LoggingService;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class LoggingServer {

    private static LoggingService.Iface handler;

    private static LoggingService.Processor processor;

    public static void main(String[] args) {
        try {
            handler = new LoggingHandler();
            processor = new LoggingService.Processor(handler);

            Runnable simple = new Runnable() {
                public void run() {
                    simple(processor);
                }
            };

            new Thread(simple).start();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public static void simple(LoggingService.Processor loggingProcessor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(loggingProcessor));

            System.out.println("Starting the simple server...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

