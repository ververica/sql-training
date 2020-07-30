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
# Build Flink images with configured S3 plugin
###############################################################################

FROM flink:1.11.1-scala_2.11

# move and download dependencies
RUN mkdir /opt/flink/plugins/s3; \
    mv /opt/flink/opt/flink-s3-fs-hadoop-1.11.1.jar /opt/flink/plugins/s3; \
    wget -P /opt/flink/lib https://repo.maven.apache.org/maven2/org/apache/flink/flink-shaded-hadoop-2-uber/2.7.5-8.0/flink-shaded-hadoop-2-uber-2.7.5-8.0.jar;

# adjust configuration
RUN echo "s3.access-key: flink-sql" >> /opt/flink/conf/flink-conf.yaml; \
    echo "s3.secret-key: flink-sql" >> /opt/flink/conf/flink-conf.yaml; \
    echo "fs.s3a.endpoint: http://minio:9000" >> /opt/flink/conf/flink-conf.yaml; \
    echo "fs.s3a.path.style.access: true" >> /opt/flink/conf/flink-conf.yaml; \
    sed -i -e 's/taskmanager.memory.process.size: 1568m/taskmanager.memory.process.size: 1728m/g' /opt/flink/conf/flink-conf.yaml; \
    echo "taskmanager.memory.jvm-metaspace.size: 256m" >> /opt/flink/conf/flink-conf.yaml;
