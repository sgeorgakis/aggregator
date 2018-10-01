package com.pollfish.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private final Server server = new Server();

    public Server getServer() {
        return server;
    }

    public static class Server {

        private int port = 9090;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}
