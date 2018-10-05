Aggregator
=================

The purpose of this exercise is to build an aggregator/digestion system consuming logging events.

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
The system consists of 3 modules:
  * The `thrift-client` module
  * The `thrift-server` module
  * The `kafka-consumer` module

Each module is self-contained and only depends on the above requirements.

---
Thrift Client
----------------
The `thrift-client` module is responsible for generating random logging events in a configurable interval
and forward them to the `thrift-server` module.

Thrift Server
----------------
The `thrift-server` module is responsible for receiving the logging events from the client
and forwarding them to a topic in `Apache Kafka`.

Kafka Consumer
----------------
The `kafka-consumer` module listens the above-mentioned topic and receives the logging events.
The events are persisted in `Apache Cassandra`.
The module exposes a simple REST endpoint to `8080` port in order to validate its functionality.
The url is `/api/logging-events`.
Optional request parameters are `date` (*yyyyMMdd* format), `app`, `level`.
If any of the parameters is missing, the rest are ignored.

Build
----------------
* To build the application, run the `build.sh` script that is located in the `bin` directory.
This will create the `lib` folder in the parent folder that will contain all the necessary jar files.

* If you want to also use the provided `Docker` images for `Apache Cassandra` and `Apache Zookeeper`,
run also the `build-services.sh` script.
In order to achieve the automatic creation of the necessary schema in `Apache Cassandra`,
a custom image was created.

**Please note that there is no `Apache Kafka` image provided and you should install it locally.**
(See [Thoughts](#thoughts-docker))

Test
----------------
To run the integration tests of the project, run the `test.sh` script located in the `bin` directory.

Run
----------------
1. To run the `Docker` images, execute the `start-services.sh` script located in the `bin` folder.
The necessary schema and tables for the `Apache Cassandra` are automatically created at startup.
The `Apache Zookeeper` instance is available at port `2181`.
The `Apache Cassandra` instance is available at port `9042`.

2. To start the `kafka-consumer` module, run the `start-consumer.sh` script located in the `bin` folder.
The consumer expects an `Apache Cassandra` and a `Apache Kafka` instance to be available at ports `9042` and `9092` respectively.
The module will also bind the `8080` port.


3. To start the `thrift-server` module, run the `start-server.sh` script located in the `bin` folder.
The server expects an `Apache Kafka` instance to be available at port `9092`.
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

The user can change various properties using the above files, like the ports of the modules,
the IP/port where external services are available etc.

To change the properties of the services themselves (when utilizing the `Docker` containers)
you must edit the `docker-compose.yml` file located in the `lib/docker` directory.

Project Info
----------------
All the modules are based on the `Spring Boot 1.5.3` framework.
Although there was no such need, at least for the `thrift-client` and `thrift-server` modules,
I used it to explore the `Spring` integration with `Apache Thrift` and `Apache Kafka` and to utilize its functionalities.

#### Core module
The `core` module contains the API schema and the generated code defined in the `loggingEvent.thrift` file
and it is packaged as a jar file for other modules to use it.
Both the `thrift-client` and the `thrift-server` modules depend on it.

#### Thrift Client module
The `thrift-client` module tries to connect to the `thrift-server` module at startup.
If a connection cannot be established, it will try again when a message will be generated to send.
If an error occurs while sending an event, the connection will close and re-establishe with the next event.
This happens to make sure the proper connection between the 2 modules.

A thread is responsible for the generation of the events and it was selected to communicate in a non-blocking way, although this is the main (and only) responsibility of the module.

#### Thrift Server Module
The `thrift-server` module connects to the `Apache Kafka` instance and waits for incoming messages to the specified port.
When a message is received, it is forwarded in a topic in `Apache Kafka` in `JSON` representation.

#### Kafka Consumer Module

The `kafka-consumer` module follows an `n-tier` architecture.
Each layer has its own object, although they have great similarities, in order to achieve decoupling between the layers.
Due to their simplicity, `thrift-client` and `thrift-server` do not follow an `n-tier` architecture,
as their roles are limited to respectively creating/receiving of logging events and forwarding them to the next system.

The `kafka-consumer` module does not depend to the `core` module, as I wanted to completely decouple this module from the thrift API.
It defines its own logging event object.

The module connects to the `Apache Cassandra` and `Apache Kafka` instances and listens to a topic in the latter one for incoming messages.
When a message is received, it is first casted from its `JSON` representation to an object and then persisted in the database. The user can view the persisted objects using a REST endpoint as mentioned above.

### Logging Event
The logging event entity has the following fields:

|field|type  |description                                                                |
|-----|------|---------------------------------------------------------------------------|
|id   |string|a unique identifier for the event                                          |
|v    |i16   |the version of the API schema                                              |
|date |string|the date the event was created in *yyyy-MM-dd HH:mm:ss.SSS* format         |
|m    |string|the message of the event                                                   |
|level|enum  |the logging level of the event (*TRACE, DEBUG, INFO, WARN, ERROR, CRITICAL*)|
|app  |i16   |a unique identifier for the application that created the event             |

The above fields were created in respect to the specifications
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

The Partition key is consisted of the `date_partition`, `app` and `level` fields, while the `id` is a clustering column.
Although most of the fields are self explanatory in respect to the logging event entity, the `date_partition` needs more explanation.

The `date_partition` field is created by the creation date of the logging event and is a string representation if *yyyMMdd* format.

The reasoning behind this design is the following:
Assuming that the system aggregates logs from various applications, I as a user will be able to fetch the logs of a specific app, in a specific day (thus the `date_partition` field) and only for a specific level (e.g. ERROR).

Thoughts
----------------

### [Docker](#thoughts-docker)
`Docker` containers were utilized in order to easily achieve a reproducible environment.

As mentioned above, only `Apache Cassandra` and `Apache Zookeeper` are provided as `Docker` services.
The user has to install `Apache Kafka` locally in order to run the system.
Although there are many images for the `Apache Kafka` and many tutorials on how to use it,
there is no official image and I have found that none of the existing ones functioned properly with the system.
The main problem seems to be in the communication of the `thrift-server` and `kafka-consumer` modules with `Apache Kafka`.

After dedicating a lot of time trying to determine the problem,
I decided it would be better to find a different solution in order to continue the implementation of the exercise.
My first thought was to create my own image using the official `openjdk:8-jre` image as base,
but my knowledge on `Docker` is limited and I felt it would take me longer than is reasonable.

### Implementation
Due to lack of familiarization with the `Apache Thrift` and `Apache Kafka` frameworks,
I believe that best practices were not followed.
Nevertheless, this exercise gave me the oportunity to explore and experiment with these technologies.
