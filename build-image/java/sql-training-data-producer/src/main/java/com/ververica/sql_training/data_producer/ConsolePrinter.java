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

import com.ververica.sql_training.data_producer.json_serde.JsonSerializer;
import com.ververica.sql_training.data_producer.records.TaxiRecord;

import java.util.function.Consumer;

/**
 * Prints TaxiRecords as JSON strings on the standard output.
 */
public class ConsolePrinter implements Consumer<TaxiRecord> {

    private final JsonSerializer<TaxiRecord> serializer = new JsonSerializer<>();

    @Override
    public void accept(TaxiRecord record) {
        String jsonString = serializer.toJSONString(record);
        System.out.println(jsonString);
    }
}
