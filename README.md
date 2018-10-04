# Aggregator

The purpose of this excercise is to build an aggregator/digestion system consuming logging events.

###Requirements
* [JDK 1.8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Apache Maven 3.3.9](https://maven.apache.org/download.cgi)
* [Apache Thrift 0.11.0](http://www.apache.org/dyn/closer.cgi?path=/thrift/0.11.0/thrift-0.11.0.tar.gz)
* [Apache Kafka](https://www.apache.org/dyn/closer.cgi?path=/kafka/2.0.0/kafka_2.11-2.0.0.tgz)
* [Apache Cassandra 3.11](http://www.apache.org/dyn/closer.lua/cassandra/3.11.3/apache-cassandra-3.11.3-bin.tar.gz) (Optional)
* [Apache Zookeeper 3.4.13](https://www.apache.org/dyn/closer.cgi/zookeeper/) (Optional)
* [Docker Compose](https://docs.docker.com/compose/install/) (Optional, needs to be installed if Apache Cassandra and Apache Zookeeper are not installed)
---
The system is consisted of 3 modules:
  * The `thrift-client` module
  * The `thrift-server` module
  * The `kafka-consumer` module

Each module is self-contained and only depends on the above requirements.
The only exception is the `thrift-client` module that needs to be connected to `thrift-server` module.

---
####Thrift Client

####Thrift Server

####Kafka Consumer

###Build

###Run

###Project Info
###Thoughts