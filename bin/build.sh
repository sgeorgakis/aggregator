#!/bin/sh

VERSION="1.0-SNAPSHOT"

cd ../src
mvn clean package -DskipTests
mkdir ../lib

mv kafka-consumer/target/kafka-consumer-$VERSION.jar ../lib/
rm -r kafka-consumer/target
mv thrift-client/target/thrift-client-$VERSION.jar ../lib/
rm -r thrift-client/target
mv thrift-server/target/thrift-server-$VERSION.jar ../lib/
rm -r thrift-server/target
rm -r core/target
rm -r target
