FROM library/openjdk:8-jre

ENV KAFKA_HOME=/opt/kafka \
    KAFKA_NAME=kafka \
    SCALA_VERSION=2.12 \
    KAFKA_VERSION=2.0.0 \
    KAFKA_USER=kafka \
    KAFKA_UID=10003 \
    KAFKA_GROUP=kafka \
    KAFKA_GID=10003 \
    KAFKA_VOLUME=/opt/tools \
    KAFKA_URL=http://ftp.cc.uoc.gr/mirrors/apache/kafka/ \
    KAFKA_ZOOKEEPER_CONNECT=localhost:2181

ENV KAFKA_RELEASE=kafka_"$SCALA_VERSION"-"$KAFKA_VERSION" \
    KAFKA_CONF=${KAFKA_HOME}/config/server.properties \
    PATH=$PATH:${KAFKA_HOME}/bin

# Install and configure kafka
RUN wget ${KAFKA_URL}/${KAFKA_VERSION}/${KAFKA_RELEASE}.tgz
RUN tar -xzf ${KAFKA_RELEASE}.tgz
RUN mv ${KAFKA_RELEASE} ${KAFKA_HOME}
RUN mkdir ${KAFKA_HOME}/data ${KAFKA_HOME}/logs
RUN sed -i '/zk.connect=localhost:2181/c\"${KAFKA_ZOOKEEPER_CONNECT}""' ${KAFKA_CONF}

RUN chmod +x ${KAFKA_HOME}/bin/*.sh

WORKDIR $KAFKA_HOME

EXPOSE 9092

CMD bin/kafka-server-start.sh ${KAFKA_CONF}
