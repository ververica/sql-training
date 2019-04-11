#!/usr/bin/env bash

usage() {
  cat <<HERE
Usage:
  build.sh

HERE
  exit 1
}

USER_NAME="fhueske"
IMAGE_NAME="flink-sql-client-training-1.7.2"
IMAGE_VERSION=`cat VERSION`
BASE_IMAGE_NAME="flink-sql-client"
BASE_IMAGE_VERSION="1.7.2"

KAFKA_DATA_PATH="/opt/data/kafka"
DRIVERS_DATA_FILE="taxi-drivers.txt.tgz"
FARES_DATA_FILE="fares.txt.tgz"
RIDES_DATA_FILE="rides.txt.tgz"
DRIVERS_DATA_URL="https://drive.google.com/uc?export=download&id=1LX-XHPZpwzSf94_CYk0ajEBYbhdeu48R"
FARES_DATA_URL="https://drive.google.com/uc?export=download&id=1n3IPotCnIcQ3vkTkMQ6aTZB8ALNa2HwT"
RIDES_DATA_URL="https://drive.google.com/uc?export=download&id=1_p4NbhEzJr2btRJh4h8eVerc87Tb31vv"

# build base image
docker build --rm=true -t ${USER_NAME}/${BASE_IMAGE_NAME}:${BASE_IMAGE_VERSION} -f ./Dockerfile .

# build UDFs for TaxiRides data
echo "Build TaxiRide UDFs"
mvn -f sql-udfs clean install

# create docker file
echo -e "FROM ${USER_NAME}/${BASE_IMAGE_NAME}:${BASE_IMAGE_VERSION}\n" > Dockerfile.tmp
# copy SQL Client config
echo -e "COPY training-config.yaml /opt/sql-client/conf/config.yaml\n" >> Dockerfile.tmp
# copy UDFs
echo -e "COPY sql-udfs/target/sql-udfs-1.0-SNAPSHOT.jar /opt/sql-client/lib/\n" >> Dockerfile.tmp
# download data files
echo -e "RUN wget -O '${KAFKA_DATA_PATH}/${DRIVERS_DATA_FILE}' '${DRIVERS_DATA_URL}'\n" >> Dockerfile.tmp
echo -e "RUN wget -O '${KAFKA_DATA_PATH}/${FARES_DATA_FILE}' '${FARES_DATA_URL}'\n" >> Dockerfile.tmp
echo -e "RUN wget -O '${KAFKA_DATA_PATH}/${RIDES_DATA_FILE}' '${RIDES_DATA_URL}'\n" >> Dockerfile.tmp

# build image
docker build -t "${USER_NAME}/${IMAGE_NAME}:${IMAGE_VERSION}" -f ./Dockerfile.tmp .

# leave no trace
rm Dockerfile.tmp
