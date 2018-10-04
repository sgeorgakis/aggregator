namespace java com.pollfish.core

enum LevelType {
    TRACE,
    DEBUG,
    INFO,
    WARNING,
    ERROR,
    CRITICAL
}

typedef string date

struct LoggingEvent {
    1:string id,       // id of logging event
    2:i16 v=1,         // version of schema
    3:date time,       // time of logging event
    4:string m,        // message of the event
    5:LevelType level, // logging level
    6:i16 app=000      // id of the client app (if used by many)
}

service LoggingService {
    void pushLoggingEvent(1:LoggingEvent event)
}
