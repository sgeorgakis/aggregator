#!/bin/sh

cd "$(dirname "$0")"
cd ../lib/

java -jar thrift-client-1.0-SNAPSHOT.jar --spring.config.location=../conf/client.yml
