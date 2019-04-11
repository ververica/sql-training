###############################################################################
#  Licensed to the Apache Software Foundation (ASF) under one
#  or more contributor license agreements.  See the NOTICE file
#  distributed with this work for additional information
#  regarding copyright ownership.  The ASF licenses this file
#  to you under the Apache License, Version 2.0 (the
#  "License"); you may not use this file except in compliance
#  with the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
# limitations under the License.
###############################################################################

FROM flink:1.7.2-scala_2.12

ADD VERSION .

WORKDIR /opt/sql-client

ENV SQL_CLIENT_HOME /opt/sql-client

# Copy sql-client dependencies
COPY sql-client/ /opt/sql-client
COPY kafka-client /opt/kafka-client

# Create data folders
RUN mkdir -p /opt/data
RUN mkdir -p /opt/data/kafka
RUN mkdir -p /opt/data/batch

RUN apt-get update; apt-get -y install python2.7 && \
    chmod +x /opt/sql-client/sql-client.sh && \
    wget -P /opt/sql-client/lib/ http://central.maven.org/maven2/org/apache/flink/flink-json/${FLINK_VERSION}/flink-json-${FLINK_VERSION}.jar && \
    wget -P /opt/sql-client/lib/ http://central.maven.org/maven2/org/apache/flink/flink-connector-kafka-0.11_2.12/${FLINK_VERSION}/flink-connector-kafka-0.11_2.12-${FLINK_VERSION}-sql-jar.jar && \
    wget -P /opt/sql-client/lib/ http://central.maven.org/maven2/org/apache/flink/flink-connector-elasticsearch6_2.12/${FLINK_VERSION}/flink-connector-elasticsearch6_2.12-${FLINK_VERSION}-sql-jar.jar

COPY docker-entrypoint.sh /
# Configure container
ENTRYPOINT ["/docker-entrypoint.sh"]
