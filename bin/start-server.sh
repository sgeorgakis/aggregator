#!/bin/sh

cd ../lib/
java -jar thrift-server-1.0-SNAPSHOT.jar --spring.config.location=../conf/server.yml
