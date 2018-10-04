#!/bin/sh

VERSION="1.0-SNAPSHOT"

cd ../src
mvn clean install
mkdir ../lib

mv kafka-consumer/target/kafka-consumer-$VERSION.jar ../lib/
mv thrift-client/target/thrift-client-$VERSION.jar ../lib/
mv thrift-server/target/thrift-server-$VERSION.jar ../lib/

cd ../
rm -r target/