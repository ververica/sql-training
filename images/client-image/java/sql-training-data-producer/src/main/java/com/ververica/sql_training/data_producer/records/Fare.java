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
 * POJO for a Fare record.
 */
public class Fare implements TaxiRecord {

    @JsonFormat
    private long rideId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date eventTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private PayMethod payMethod;
    @JsonFormat
    private double fare;
    @JsonFormat
    private double toll;
    @JsonFormat
    private double tip;

    public Fare() {}

    public Fare(long rideId, Date eventTime, PayMethod payMethod, double fare, double toll, double tip) {
        this.rideId = rideId;
        this.eventTime = eventTime;
        this.payMethod = payMethod;
        this.fare = fare;
        this.toll = toll;
        this.tip = tip;
    }

    @Override
    public Date getEventTime() {
        return eventTime;
    }

    public static enum PayMethod {
        CSH,
        CRD,
        DIS,
        NOC,
        UNK
    }
}
