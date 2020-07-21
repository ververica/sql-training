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

import com.ververica.sql_training.data_producer.records.TaxiRecord;

import java.time.Instant;
import java.util.function.UnaryOperator;

/**
 * Delays forwarding of TaxiRecords based on their timestamp.
 * By default, records that have timestamps that are 10 minutes apart from each other are emitted
 * 10 minutes apart from each other.
 *
 * The emission rate can be adjusted by a speedup factor. With a speedup of 10.0, records that have
 * timestamps which are 10 minutes apart from each other are emitted with a 1 minute gap.
 *
 * The delayer assumes that records are provided with monotonically increasing timestamps.
 */
public class Delayer implements UnaryOperator<TaxiRecord> {

    // the speedup factor
    private final double speedUp;
    // the machine time when the delayer was instantiated.
    private final long startTime;
    // the event time of the first processed record
    private long startEventTime = -1;
    // the event time of the last processed record
    private long prevEventTime;
    // a counter to sync emission on machine time
    private int syncCounter = 0;

    public Delayer() {
        this(1.0);
    }

    public Delayer(double speedUp) {
        this.speedUp = speedUp;
        this.startTime = Instant.now().toEpochMilli();
    }

    @Override
    public TaxiRecord apply(TaxiRecord record) {
        long thisEventTime = record.getEventTime().getTime();

        if (startEventTime < 0) {
            // remember event time of first record
            startEventTime = thisEventTime;
        } else {
            // how much time to wait between the previous and this record
            long gapTime = (long) ((thisEventTime - prevEventTime) / speedUp);

            if (gapTime > 0 || syncCounter > 1000) {
                // syncing on machine time at least every 1000 records

                // compute how many machine time ms to wait before emitting the record
                long currentTime = Instant.now().toEpochMilli();
                long targetEmitTime = (long) ((thisEventTime - startEventTime) / speedUp) + startTime;
                long waitTime = targetEmitTime - currentTime;

                // wait if necessary
                if (waitTime > 0) {
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // reset sync counter
                syncCounter = 0;
            } else {
                // we emitted without syncing on time. Increment counter.
                syncCounter++;
            }
        }

        this.prevEventTime = thisEventTime;

        return record;
    }
}
