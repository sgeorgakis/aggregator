package com.pollfish.server;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollfish.core.LevelType;
import com.pollfish.core.LoggingEvent;
import com.pollfish.core.LoggingService;
import com.pollfish.server.config.ApplicationProperties;
import com.pollfish.server.service.LoggingHandler;
import com.pollfish.server.service.ProducerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LoggingServerApp.class)
@TestPropertySource(locations = "classpath:config/application.yml")
public class LoggingServerIntTest {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingServerIntTest.class);
    private static final String TOPIC = "logging";

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, TOPIC);

    @Autowired
    private ApplicationProperties applicationProperties;

    @MockBean
    private LoggingHandler handler;

    @Autowired
    private ProducerService producerService;

    private LoggingService.Client client;

    private KafkaMessageListenerContainer<String, String> container;

    private BlockingQueue<ConsumerRecord<String, String>> records;

    @Before
    public void init() throws Exception {
        initClient();
        initKafkaConsumer();
    }

    @Test
    public void receivedAndForwardedLoggingEventTest() throws Exception {

        ArgumentCaptor<LoggingEvent> captor = ArgumentCaptor.forClass(LoggingEvent.class);

        // Send the event from the client
        LoggingEvent event = createLoggingEvent();
        client.pushLoggingEvent(event);
        LOG.info("Message was sent by the client {}", event);

        // Verify that it was received
        Mockito.verify(handler).pushLoggingEvent(captor.capture());
        LoggingEvent serverReceivedEvent = captor.getValue();
        assertThat(serverReceivedEvent).isEqualTo(event);
        LOG.info("Message received by the server {}", serverReceivedEvent);

        // Since the handler is being mocked
        // manual forwarding to the stream service must be performed
        producerService.forwardLoggingEvent(event);

        // Verify that event was forwarded to Kafka
        ConsumerRecord<String, String> receivedRecord = records.poll(10, TimeUnit.SECONDS);
        assertThat(receivedRecord).isNotNull();
        LoggingEvent consumerReceivedEvent = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .readValue(receivedRecord.value(), LoggingEvent.class);
        assertThat(consumerReceivedEvent).isNotNull();
        assertThat(consumerReceivedEvent).isEqualTo(event);
        LOG.info("Message Forwarded to Kafka {}", consumerReceivedEvent);
    }

    @After
    public void shutDown() {
        if (client.getInputProtocol().getTransport().isOpen()) {
            client.getInputProtocol().getTransport().close();
        }
        if (client.getOutputProtocol().getTransport().isOpen()) {
            client.getOutputProtocol().getTransport().close();
        }

        // stop the container
        container.stop();
    }

    private LoggingEvent createLoggingEvent() {
        LoggingEvent event = new LoggingEvent();
        event.setId(UUID.randomUUID().toString());
        event.setApp((short) 1);
        event.setLevel(LevelType.CRITICAL);
        event.setM("test message");
        event.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
        event.setV((short) 1);
        return event;
    }

    private void initClient() throws Exception {
        TTransport transport = new TSocket("localhost", applicationProperties.getServer().getPort());
        transport.open();

        TProtocol protocol = new TBinaryProtocol(transport);
        client = new LoggingService.Client(protocol);
    }

    private void initKafkaConsumer() throws Exception {

        // set up the Kafka consumer properties
        Map<String, Object> consumerProperties =
                KafkaTestUtils.consumerProps("logging-event", "false", embeddedKafka);

        // create a Kafka consumer factory
        DefaultKafkaConsumerFactory<String, String> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerProperties);

        // set the topic that needs to be consumed
        ContainerProperties containerProperties = new ContainerProperties(TOPIC);

        // create a Kafka MessageListenerContainer
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

        // create a thread safe queue to store the received message
        records = new LinkedBlockingQueue<>();

        // setup a Kafka message listener
        container.setupMessageListener((MessageListener<String, String>) r -> records.add(r));

        // start the container and underlying message listener
        container.start();

        // wait until the container has the required number of assigned partitions
        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
    }
}
