Aggregator
=================

The purpose of this excercise is to build an aggregator/digestion system consuming logging events.

Requirements
----------------
* [JDK 1.8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Apache Maven 3.3.9](https://maven.apache.org/download.cgi)
* [Apache Thrift 0.11.0](http://www.apache.org/dyn/closer.cgi?path=/thrift/0.11.0/thrift-0.11.0.tar.gz)
* [Apache Kafka 2.0.0](https://www.apache.org/dyn/closer.cgi?path=/kafka/2.0.0/kafka_2.11-2.0.0.tgz)
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
Thrift Client
----------------
Thrift Server
----------------
Kafka Consumer
----------------
Build
----------------
* To build the application, run the `build.sh` script that is located in the `bin` directory.
This will create the `lib` folder in the parent folder that will contain all the necessary jar files.

* If you want to also use the provided docker images for `Apache Cassandra` and `Apache Zookeeper`,
run also the `build-services.sh` script.
In order to achieve the automatic creation of the necessary schema in `Apache Cassandra`,
a custom image was created.

**Please note that there is no `Apache Kafka` image provided and you should install it locally.**

Test
----------------
To run the integration tests of the project, run the `test.sh` script located in the `bin` directory.

Run
----------------
1. To run the docker images, execute the `start-services.sh` script located in the `bin` folder.
The necessary schema and tables for the `Apache Cassandra` are automatically created at startup.
The `Apache Zookeeper` instance is available at port `2181`.
The `Apache Cassandra` instance is available at port `9042`.

2. To start the `kafka-consumer` module, run the `start-consumer.sh` script located in the `bin` folder.
The consumer expects an `Apache Cassandra` and a `Apache Kafka` instances to be available at ports `9042` and `9092` respectively.
The module will also bind the `8080` port.


3. To start the `thrift-server` module, run the `start-server.sh` script located in the `bin` folder.
The server expects an `Apache Kafka` instance to be available at port `9092`, although it will not fail to start if there is not.
The module will bind the `9090` port.

4. To start the`thrift-client` module, run the `start-client.sh` script located in the `bin` folder.
The client expects the `thrift-server` module to be running and will try to connect to it at port `9090`.


**It is recommended to start the applications and services in the above order for the proper function of the system.**

Configuration
---
The configuration files are located in the `conf` folder.

The following table shows the correlation of the files with the module.

|module        |configuration file|
|--------------|------------------|
|thrift-client |client.yml        |
|thrift-server |server.yml        |
|kafka-consumer|consumer.yml      |

Project Info
----------------
All the modules are based on the `Spring Boot 1.5.3` framework.
Although there was not such need at least for the `thrift-client` and `thrift-server` modules,
I used it to explore its (and `Spring` framework's) integration with `Apache Thrift`, `Apache Kafka` and to utilize its functionalities.

The `kafka-consumer` module follows an `n-tier` architecture as it can be described as a `web application`.
Each layer has its own object, although they have great similarities.

Due to their simplicity, `thrift-client` and `thrift-server` do not follow an `n-tier` architecture,
as their roles limit to creation/reception of logging events and forward the to the next system.

### Logging Event
The logging event entity has the following fields:

|field|type  |description                                                                |
|-----|------|---------------------------------------------------------------------------|
|id   |string|a unique identifier for the event                                          |
|v    |i16   |the version of the API schema                                              |
|date |string|the date the event was created in *yyyy-MM-dd HH:mm:ss.SSS* format         |
|m    |string|the message of the event                                                   |
|level|enum  |the logging leve of the event (*TRACE, DEBUG, INFO, WARN, ERROR, CRITICAL*)|
|app  |i16   |a unique identifier for the application that created the event             |

The above fields were created in respect with the specifications
and assuming that the logging events are generated from various clients (app)
and contain information about their health or execution.

### Cassandra Schema
For the persistence of the logging events, only the following table was created

#### logging_event
|column        | type     |
|--------------|----------|
|date_partition|TEXT      |
|app           | INT      |
|level         | TEXT     |
|id            | TEXT     |
|date_created  | TIMESTAMP|
|message       | TEXT     |
|version       | INT      |

The Partition key is consisted of the `date_partition`, `app` and `level` fields, while the id is a clustering column.
Although most of the fields are self explanatory in respect with the logging event entity, the `date_partition` needs more explanation.

The `date_partition` field is created by the creation date of the logging event and is a string representation if *yyyMMdd* format.

The thought behind this design is the following:
Assumming that the system aggregates logs from various applications, I as a user will be able to fetch the logs of a specific app, in a specific day (thus the `date_partition` field) and only for a specific level (e.g. ERROR).

Thoughts
----------------

As mentioned above, only `Apache Cassandra` and `Apache Zookeeper` are provided as docker services.
The user has to install localy `Apache Kafka` in order to run the system.
Although there are many images for the `Apache Kafka` and many tutorials on how to use it,
there is no official image and I have found that none of the existing ones functioned properly with the system.
The main problem seems to be in the communication of the `thrift-server` and `kafka-consumer` modules with `Apache Kafka`, but I was not able to specify it.
After that, I considered creating my own image using the official `openjdk:8-jre` image as base,
but my knowledge on docker is limited and it would require much effort to achieve it.
