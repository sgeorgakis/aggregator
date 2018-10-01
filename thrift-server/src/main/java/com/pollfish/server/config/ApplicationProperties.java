package com.pollfish.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private final Server server = new Server();

    private final Kafka kafka = new Kafka();

    public Server getServer() {
        return server;
    }

    public Kafka getKafka() {
        return kafka;
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

    public static class Kafka {

        private String topic = "logging";

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }
    }
}
