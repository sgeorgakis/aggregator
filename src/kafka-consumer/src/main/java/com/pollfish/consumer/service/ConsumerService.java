package com.pollfish.consumer.service;

public interface ConsumerService {

    /**
     * Listen for a message to consume
     * @param message the message
     */
    void listen(String message);
}
