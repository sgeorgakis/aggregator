package com.pollfish.consumer.domain;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Date;

@Table(name = "logging_event")
public class LoggingEvent {

    @PartitionKey
    @Column(name = "date_partition")
    private String datePartition;

    @PartitionKey(1)
    private int app;

    @PartitionKey(2)
    private String level;

    @ClusteringColumn
    private String id;

    @Column(name = "date_created")
    private Date dateCreated;

    private String message;

    public String getDatePartition() {
        return datePartition;
    }

    public void setDatePartition(String datePartition) {
        this.datePartition = datePartition;
    }

    public int getApp() {
        return app;
    }

    public void setApp(int app) {
        this.app = app;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LoggingEvent{"
                + "datePartition='" + datePartition + '\''
                + ", app=" + app + ", level='" + level + '\''
                + ", id='" + id + '\''
                + ", dateCreated=" + dateCreated
                + ", message='" + message + '\''
                + '}';
    }
}
