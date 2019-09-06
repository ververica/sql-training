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

import com.ververica.sql_training.data_producer.json_serde.JsonDeserializer;
import com.ververica.sql_training.data_producer.records.TaxiRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

/**
 * Reads JSON-encoded TaxiRecords from a gzipped text file.
 */
public class FileReader implements Supplier<TaxiRecord> {

    private final Iterator<TaxiRecord> records;
    private final String filePath;

    public FileReader(String filePath, Class<? extends TaxiRecord> recordClazz) throws IOException {

        this.filePath = filePath;
        JsonDeserializer<?> deserializer = new JsonDeserializer<>(recordClazz);
        try {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new GZIPInputStream(new FileInputStream(filePath)), StandardCharsets.UTF_8));

            Stream<String> lines = reader.lines().sequential();
            records = lines.map(l -> (TaxiRecord) deserializer.parseFromString(l)).iterator();

        } catch (IOException e) {
            throw new IOException("Error reading TaxiRecords from file: " + filePath, e);
        }
    }

    @Override
    public TaxiRecord get() {

        if (records.hasNext()) {
            return records.next();
        } else {
            throw new NoSuchElementException("All records read from " + filePath);
        }
    }
}
