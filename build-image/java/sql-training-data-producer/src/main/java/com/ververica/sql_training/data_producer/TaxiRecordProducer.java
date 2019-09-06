/*
 * Copyright 2019 Ververica GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ververica.sql_training.data_producer;

import com.ververica.sql_training.data_producer.records.DriverChange;
import com.ververica.sql_training.data_producer.records.Fare;
import com.ververica.sql_training.data_producer.records.Ride;
import com.ververica.sql_training.data_producer.records.TaxiRecord;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Produces TaxiRecords (Ride, Fare, DriverChange) into Kafka topics.
 */
public class TaxiRecordProducer {

    public static void main(String[] args) throws InterruptedException {

        boolean areSuppliersConfigured = false;
        boolean areConsumersConfigured = false;

        Supplier<TaxiRecord> rideSupplier = null;
        Supplier<TaxiRecord> fareSupplier = null;
        Supplier<TaxiRecord> driverChangeSupplier = null;

        Consumer<TaxiRecord> rideConsumer = null;
        Consumer<TaxiRecord> fareConsumer = null;
        Consumer<TaxiRecord> driverChangeConsumer = null;

        double speedup = 1.0d;

        // parse arguments
        int argOffset = 0;
        while(argOffset < args.length) {

            String arg = args[argOffset++];
            switch (arg) {
                case "--input":
                    String source = args[argOffset++];
                    switch (source) {
                        case "file":
                            String basePath = args[argOffset++];
                            try {
                                rideSupplier = new FileReader(basePath + "/rides.txt.gz", Ride.class);
                                fareSupplier = new FileReader(basePath + "/fares.txt.gz", Fare.class);
                                driverChangeSupplier = new FileReader(basePath + "/driverChanges.txt.gz", DriverChange.class);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown input configuration");
                    }
                    areSuppliersConfigured = true;
                    break;
                case "--output":
                    String sink = args[argOffset++];
                    switch (sink) {
                        case "console":
                            rideConsumer = new ConsolePrinter();
                            fareConsumer = new ConsolePrinter();
                            driverChangeConsumer = new ConsolePrinter();
                            break;
                        case "kafka":
                            String brokers = args[argOffset++];
                            rideConsumer = new KafkaProducer("Rides", brokers);
                            fareConsumer = new KafkaProducer("Fares", brokers);
                            driverChangeConsumer = new KafkaProducer("DriverChanges", brokers);
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown output configuration");
                    }
                    areConsumersConfigured = true;
                    break;
                case "--speedup":
                    speedup = Double.parseDouble(args[argOffset++]);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown parameter");
            }
        }

        // check if we have a source and a sink
        if (!areSuppliersConfigured) {
            throw new IllegalArgumentException("Input sources were not properly configured.");
        }
        if (!areConsumersConfigured) {
            throw new IllegalArgumentException("Output sinks were not properly configured");
        }

        // create three threads for each record type
        Thread ridesFeeder = new Thread(new TaxiRecordFeeder(rideSupplier, new Delayer(speedup), rideConsumer));
        Thread faresFeeder = new Thread(new TaxiRecordFeeder(fareSupplier, new Delayer(speedup), fareConsumer));
        Thread driverChangesFeeder = new Thread(new TaxiRecordFeeder(driverChangeSupplier, new Delayer(speedup), driverChangeConsumer));

        // start emitting data
        ridesFeeder.start();
        faresFeeder.start();
        driverChangesFeeder.start();

        // wait for threads to complete
        ridesFeeder.join();
        faresFeeder.join();
        driverChangesFeeder.join();
    }

    public static class TaxiRecordFeeder implements Runnable {

        private final Supplier<TaxiRecord> source;
        private final Delayer delayer;
        private final Consumer<TaxiRecord> sink;

        TaxiRecordFeeder(Supplier<TaxiRecord> source, Delayer delayer, Consumer<TaxiRecord> sink) {
            this.source = source;
            this.delayer = delayer;
            this.sink = sink;
        }

        @Override
        public void run() {
            Stream.generate(source).sequential()
                    .map(delayer)
                    .forEachOrdered(sink);
        }
    }
}
