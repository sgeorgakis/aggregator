CREATE  KEYSPACE IF NOT EXISTS logging
   WITH REPLICATION = {
      'class' : 'SimpleStrategy', 'replication_factor' : 1
   };

CREATE TABLE IF NOT EXISTS logging.logging_event (
  date_partition TEXT,
  app INT,
  level TEXT,
  id TEXT,
  date_created TIMESTAMP,
  message TEXT,
  version INT,
  PRIMARY KEY((date_partition, app, level), id)
);