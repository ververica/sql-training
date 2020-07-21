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

package com.ververica.sql_training.data_producer.records;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * POJO for a Ride record.
 */
public class Ride implements TaxiRecord {

    @JsonFormat
    private long rideId;
    @JsonFormat
    private boolean isStart;
    @JsonFormat
    private long taxiId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date eventTime;
    @JsonFormat
    private double lon;
    @JsonFormat
    private double lat;
    @JsonFormat
    private byte psgCnt;

    public Ride() {}

    public Ride(long rideId, boolean isStart, long taxiId, Date eventTime, double lon, double lat, byte psgCnt) {
        this.rideId = rideId;
        this.isStart = isStart;
        this.taxiId = taxiId;
        this.eventTime = eventTime;
        this.lon = lon;
        this.lat = lat;
        this.psgCnt = psgCnt;
    }

    @Override
    public Date getEventTime() {
        return eventTime;
    }

}
