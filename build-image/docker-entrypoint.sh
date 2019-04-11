#!/bin/bash

sed -i -e "s/\${ZOOKEEPER}/${ZOOKEEPER_CONNECT}/g" "/opt/sql-client/conf/config.yaml"
sed -i -e "s/\${KAFKA}/${KAFKA_BOOTSTRAP}/g" "/opt/sql-client/conf/config.yaml"
sed -i -e "s/jobmanager.rpc.address: localhost/jobmanager.rpc.address: ${FLINK_JOBMANAGER_HOST}/g" "$FLINK_HOME/conf/flink-conf.yaml"

# creating a Kafka topic
/opt/kafka-client/bin/kafka-topics.sh --create --zookeeper ${ZOOKEEPER_CONNECT}:2181 --replication-factor 1 --partitions 1 --topic Rides
/opt/kafka-client/bin/kafka-topics.sh --create --zookeeper ${ZOOKEEPER_CONNECT}:2181 --replication-factor 1 --partitions 1 --topic Fares
/opt/kafka-client/bin/kafka-topics.sh --create --zookeeper ${ZOOKEEPER_CONNECT}:2181 --replication-factor 1 --partitions 1 --topic DriverChanges
# writing data to Kafka topic
tar xOfz /opt/data/kafka/rides.txt.tgz | /opt/sql-client/delay.py 10 | /opt/kafka-client/bin/kafka-console-producer.sh --broker-list ${KAFKA_BOOTSTRAP}:9092 --topic Rides &
tar xOfz /opt/data/kafka/fares.txt.tgz | /opt/sql-client/delay.py 10 | /opt/kafka-client/bin/kafka-console-producer.sh --broker-list ${KAFKA_BOOTSTRAP}:9092 --topic Fares &
tar xOfz /opt/data/kafka/taxi-drivers.txt.tgz | /opt/sql-client/delay.py 10 | /opt/kafka-client/bin/kafka-console-producer.sh --broker-list ${KAFKA_BOOTSTRAP}:9092 --topic DriverChanges &

tail -f /dev/null
