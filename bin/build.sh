#!/bin/sh

VERSION="1.0-SNAPSHOT"

cd "$(dirname "$0")"
cd ../src

mvn clean package -DskipTests

mv kafka-consumer/target/kafka-consumer-$VERSION.jar ../lib/
rm -r kafka-consumer/target
mv thrift-client/target/thrift-client-$VERSION.jar ../lib/
rm -r thrift-client/target
mv thrift-server/target/thrift-server-$VERSION.jar ../lib/
rm -r thrift-server/target

