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
	1:i16 v=1,      // version of schema
	2:date time,  // time of logging event
	3:string m,     // message of the event
	4:LevelType level, // logging level
	5:i16 app=000   // id of the client app (if used by many)
}

service LoggingService {

  void pushLoggingEvent(1:LoggingEvent event)

}
