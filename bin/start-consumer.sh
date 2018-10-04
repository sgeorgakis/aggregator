#!/bin/sh

cd ../lib/
java -jar kafka-consumer-1.0-SNAPSHOT.jar --spring.config.location=../conf/consumer.yml
