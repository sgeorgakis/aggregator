package com.pollfish.consumer.repository;

import com.pollfish.consumer.domain.LoggingEvent;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoggingEventRepository extends CassandraRepository<LoggingEvent> {

    List<LoggingEvent> findAll();

    List<LoggingEvent> findByDatePartitionAndAppAndLevel(String datePartition, int app, String level);
}
