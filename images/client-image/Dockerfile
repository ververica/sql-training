###############################################################################
# Copyright 2020 Ververica GmbH
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#  http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
###############################################################################

###############################################################################
# Build Click Count Job
###############################################################################

FROM maven:3.6-jdk-8-slim AS builder

# Get UDF code and compile it
COPY ./java/sql-training-udfs /opt/sql-udfs
RUN cd /opt/sql-udfs; \
    mvn clean install

# Get data producer code and compile it
COPY ./java/sql-training-data-producer /opt/data-producer
RUN cd /opt/data-producer; \
    mvn clean install

###############################################################################
# Build SQL Playground Image
###############################################################################

FROM flink:1.11.1-scala_2.11

ARG FLINK_VERSION=1.11.1

ADD VERSION .

# Copy sql-client configuration
COPY sql-client/ /opt/sql-client

# Copy playground UDFs
COPY --from=builder /opt/sql-udfs/target/sql-training-udfs-*.jar /opt/sql-client/lib/

# Copy data producer
COPY --from=builder /opt/data-producer/target/sql-training-data-producer-*.jar /opt/data/data-producer.jar

# Download connector libraries
RUN wget -P /opt/sql-client/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-json/${FLINK_VERSION}/flink-json-${FLINK_VERSION}.jar; \
    wget -P /opt/sql-client/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-sql-connector-kafka_2.11/${FLINK_VERSION}/flink-sql-connector-kafka_2.11-${FLINK_VERSION}.jar; \
    wget -P /opt/sql-client/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-connector-filesystem_2.11/${FLINK_VERSION}/flink-connector-filesystem_2.11-${FLINK_VERSION}.jar; \
    wget -P /opt/flink/lib https://repo.maven.apache.org/maven2/org/apache/flink/flink-shaded-hadoop-2-uber/2.7.5-8.0/flink-shaded-hadoop-2-uber-2.7.5-8.0.jar; \
    wget -P /opt/sql-client/lib/ https://repo.maven.apache.org/maven2/org/apache/flink/flink-connector-jdbc_2.11/${FLINK_VERSION}/flink-connector-jdbc_2.11-${FLINK_VERSION}.jar; \
    wget -P /opt/sql-client/lib/ https://repo.maven.apache.org/maven2/mysql/mysql-connector-java/8.0.19/mysql-connector-java-8.0.19.jar; \
# Create data folders
    mkdir -p /opt/data; \
    mkdir -p /opt/data/stream; \
# Download data files
    wget -O /opt/data/driverChanges.txt.gz 'https://drive.google.com/uc?export=download&id=1pf4tfv-YpoVQ9_O0948M8oXeCfVH-0MH'; \
    wget -O /opt/data/fares.txt.gz 'https://drive.google.com/uc?export=download&id=1SriiwcIdMvY7uJsWSY4Hhh32iO3F4ND2'; \
    wget -O /opt/data/rides.txt.gz 'https://drive.google.com/uc?export=download&id=1gY8W07OFvB7_4lHlAyingM4WQzs0_8lT';

# Copy configuration
COPY conf/* /opt/flink/conf/

WORKDIR /opt/sql-client
ENV SQL_CLIENT_HOME /opt/sql-client
