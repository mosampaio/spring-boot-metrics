# spring-boot-metrics

A basic example of usage of Spring boot with Dropwizard metrics.

## Requirements

[Java 8](http://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html)

## Local Development

1. First clone this repository and `cd` into it.

   ```bash
   $ git clone git@github.com:mosampaio/spring-boot-metrics.git
   $ cd spring-boot-metrics
   ```

1. Start the server.

   ```bash
   $ ./gradlew bootRun
   ```

1. Check it out at [http://localhost:8080](http://localhost:8080) and [http://localhost:8080/metrics/admin](http://localhost:8080/metrics/admin).
