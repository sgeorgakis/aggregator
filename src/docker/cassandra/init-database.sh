#!/bin/sh

#CQL="CREATE  KEYSPACE IF NOT EXISTS logging2 WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };"

until cqlsh -f /opt/init-database.cql; do
  echo "cqlsh: Cassandra is unavailable to initialize - will retry later"
  sleep 2
done &

exec /docker-entrypoint.sh "$@"
