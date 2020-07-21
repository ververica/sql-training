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
 * POJO for a DriverChange record.
 */
public class DriverChange implements TaxiRecord {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date eventTime;
    @JsonFormat
    private long taxiId;
    @JsonFormat
    private long driverId;

    public DriverChange() {}

    public DriverChange(Date eventTime, long taxiId, long driverId) {
        this.eventTime = eventTime;
        this.taxiId = taxiId;
        this.driverId = driverId;
    }

    @Override
    public Date getEventTime() {
        return eventTime;
    }

}
