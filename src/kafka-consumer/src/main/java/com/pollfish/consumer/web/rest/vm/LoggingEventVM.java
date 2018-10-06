package com.pollfish.consumer.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pollfish.consumer.service.dto.LevelType;

import java.util.Date;

public class LoggingEventVM {

    private String id;

    private int version;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date time;

    private String message;

    private LevelType level;

    private int app;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LevelType getLevel() {
        return level;
    }

    public void setLevel(LevelType level) {
        this.level = level;
    }

    public int getApp() {
        return app;
    }

    public void setApp(int app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "LoggingEventVM{"
                + "id='" + id + '\''
                + ", version=" + version
                + ", time=" + time
                + ", message='" + message + '\''
                + ", level=" + level
                + ", app=" + app
                + '}';
    }
}
