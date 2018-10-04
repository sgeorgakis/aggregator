package com.pollfish.consumer.repository;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.pollfish.consumer.domain.LoggingEvent;
import org.springframework.stereotype.Repository;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Repository
public class LoggingEventRepository {

    private static final String LOGGING_EVENT_TABLE = "logging_event";
    private static final String DATE_PARTITION_COLUMN = "date_partition";
    private static final String APP_COLUMN = "app";
    private static final String LEVEL_COLUMN = "level";

    private final Session session;
    private final Validator validator;

    private Mapper<LoggingEvent> mapper;
    private PreparedStatement findAllStmt;
    private PreparedStatement findByDatePartitionAndAppAndLevelStmt;

    public LoggingEventRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(LoggingEvent.class);
        this.findAllStmt = session.prepare(QueryBuilder.select().all()
                .from(LOGGING_EVENT_TABLE));
        this.findByDatePartitionAndAppAndLevelStmt = session.prepare(QueryBuilder.select()
                .from(LOGGING_EVENT_TABLE)
                .where(QueryBuilder.eq(DATE_PARTITION_COLUMN, QueryBuilder.bindMarker()))
                .and(QueryBuilder.eq(APP_COLUMN, QueryBuilder.bindMarker()))
                .and(QueryBuilder.eq(LEVEL_COLUMN, QueryBuilder.bindMarker())));
    }


    /**
     * Find all the {@link LoggingEvent} objects in the database
     *
     * @return a list containing the {@link LoggingEvent} objects
     */
    public List<LoggingEvent> findAll() {
        return mapper.map(session.execute(findAllStmt.bind())).all();
    }

    /**
     * Find {@link LoggingEvent} objects according to datePartition, app and level
     *
     * @param datePartition the datePartition
     * @param app the app generated the {@link LoggingEvent}
     * @param level the logging level of the event
     * @return a list containing the {@link LoggingEvent} objects
     */
    public List<LoggingEvent> findByDatePartitionAndAppAndLevel(String datePartition, int app, String level) {
        return mapper.map(session.execute(findByDatePartitionAndAppAndLevelStmt.bind()
                .setString(DATE_PARTITION_COLUMN, datePartition)
                .setInt(APP_COLUMN, app)
                .setString(LEVEL_COLUMN, level)))
                .all();
    }

    /**
     * Save a {@link LoggingEvent} object
     *
     * @param event the {@link LoggingEvent} object
     * @return the persisted object
     */
    public LoggingEvent save(LoggingEvent event) {
        Set<ConstraintViolation<LoggingEvent>> violations = validator.validate(event);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(event);
        return event;
    }
}
