**⚠️ This repository has been archived. ⚠️**

---

# Apache Flink® SQL Training

**This repository provides a training for Flink's SQL API.**

In this training you will learn to:

* run SQL queries on streams.
* use Flink's SQL CLI client.
* perform window aggregations, stream joins, and pattern matching with SQL queries.
* specify a continuous SQL query that maintain a dynamic result table.
* write the result of streaming SQL queries to Kafka and MySQL.

Please find the [training instructions](https://github.com/ververica/sql-training/wiki) in the Wiki of this repository.

### Requirements

The training is based on Flink's SQL CLI client and uses Docker Compose to setup the training environment.

You **only need [Docker](https://www.docker.com/)** to run this training. </br>
You don't need Java, Scala, or an IDE.

## What is Apache Flink?

[Apache Flink](https://flink.apache.org) is a framework and distributed processing engine for stateful computations over unbounded and bounded data streams. Flink has been designed to run in all common cluster environments, perform computations at in-memory speed and at any scale.

## What is SQL on Apache Flink?

Flink features multiple APIs with different levels of abstraction. SQL is supported by Flink as a unified API for batch and stream processing, i.e., queries are executed with the same semantics on unbounded, real-time streams or bounded, recorded streams and produce the same results. SQL on Flink is commonly used to ease the definition of data analytics, data pipelining, and ETL applications.

The following example shows a SQL query that computes the number of departing taxi rides per hour. 

```sql
SELECT
  TUMBLE_START(rowTime, INTERVAL '1' HOUR) AS t,
  COUNT(*) AS cnt
FROM Rides
WHERE
  isStart
GROUP BY 
  TUMBLE(rowTime, INTERVAL '1' HOUR)
```

----

*Apache Flink, Flink®, Apache®, the squirrel logo, and the Apache feather logo are either registered trademarks or trademarks of The Apache Software Foundation.*
