package com.pollfish.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollfish.consumer.service.LoggingEventService;
import com.pollfish.consumer.service.dto.LevelType;
import com.pollfish.consumer.service.dto.LoggingEventDTO;
import com.pollfish.consumer.service.mapper.LoggingEventMapper;
import com.pollfish.consumer.web.rest.LoggingEventController;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LoggingConsumerApp.class)
@TestPropertySource(locations = "classpath:config/application.yml")
public class LoggingConsumerIntTest extends CassandraLoader {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingConsumerIntTest.class);
    private static final String TOPIC = "logging";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, TOPIC);

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    private LoggingEventService loggingEventService;

    @Autowired
    private LoggingEventMapper loggingEventMapper;

    private KafkaTemplate<String, String> template;
    private MockMvc restMvc;

    @Before
    public void init() throws Exception {
        initKafkaProducer();

        LoggingEventController controller = new LoggingEventController(loggingEventService, loggingEventMapper);

        this.restMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    public void  consumeMessageTest() throws Exception {

        // Send the event
        LoggingEventDTO eventDTO = createLoggingEventDTO();
        template.sendDefault(new ObjectMapper().writeValueAsString(eventDTO));

        // Wait to receive the event
        Thread.sleep(1000);

        // Verify it was persisted in the database
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT")); // Cassandra saves the date in GMT timezone

        restMvc.perform(get("/api/logging-events/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].app").value(hasItem(eventDTO.getApp())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(eventDTO.getId())))
                .andExpect(jsonPath("$.[*].level").value(hasItem(eventDTO.getLevel().name())))
                .andExpect(jsonPath("$.[*].version").value(hasItem(eventDTO.getVersion())))
                .andExpect(jsonPath("$.[*].message").value(hasItem(eventDTO.getMessage())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(dateFormat.format(eventDTO.getTime()))));

    }

    private void initKafkaProducer() throws Exception {

        // set up the Kafka producer properties
        Map<String, Object> senderProperties =
                KafkaTestUtils.senderProps(embeddedKafka.getBrokersAsString());

        // create a Kafka producer factory
        ProducerFactory<String, String> producerFactory =
                new DefaultKafkaProducerFactory<>(senderProperties);

        // create a Kafka template
        template = new KafkaTemplate<>(producerFactory);

        // set the default topic to send to
        template.setDefaultTopic(TOPIC);

        // wait until the partitions are assigned
        for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
                .getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer,
                    embeddedKafka.getPartitionsPerTopic());
        }
    }

    private LoggingEventDTO createLoggingEventDTO() {
        LoggingEventDTO eventDTO = new LoggingEventDTO();
        eventDTO.setApp(1);
        eventDTO.setId(UUID.randomUUID().toString());
        eventDTO.setLevel(LevelType.CRITICAL);
        eventDTO.setTime(new Date());
        eventDTO.setVersion(1);
        eventDTO.setMessage("test message");

        return eventDTO;
    }
}
