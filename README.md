# BlackBox

A Spring Boot service that stores and analyses the event logs of Pigeon, a professional messaging app. Built during a one-week school project to learn MongoDB and Spring Data MongoDB.

- Generates 204,000 realistic events (logins, payments, API calls, errors, notifications) over one simulated year, for 2,000 users (20% of them produce 80% of the events).
- Exposes 4 analytics endpoints, all computed by MongoDB aggregation pipelines.
- Speeds up the slowest analysis with a compound index, measured with `explain`.

# Prerequisites

- JDK 21+
- MongoDB running on `localhost:27017` (no login needed)
- `mongosh`, only to run the explain measure

Maven does not need to be installed: the project uses the Maven wrapper (`mvnw`), at the root of the repository. Spring Boot and the other libraries (Spring Data MongoDB, springdoc-openapi, validation) are downloaded by Maven on the first build.

# Run the project

All commands are run from the root of the repository, on Windows.

## 1. Generate the data

```
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=generate"
```

Fills the `blackbox` database with 2,000 users and 204,000 events, then stops. The random generator uses a fixed seed, so every run gives exactly the same data. Running it again empties the collections first, it does not add duplicates.

## 2. Start the API

```
.\mvnw.cmd spring-boot:run
```

The server starts on port 8080 and keeps running (stop it with `Ctrl+C`). The index used by the analytics is created automatically at startup.

## 3. Try the endpoints

Swagger UI: http://localhost:8080/swagger-ui/index.html

Every endpoint takes a period: `from` is included, `to` is excluded.
## Other commands

Run the tests (they use a separate `blackbox-test` database):

```
.\mvnw.cmd test
```

Run the explain measure of the funnel pipeline:

```
mongosh blackbox --file docs\measures\funnel-explain.js
```

# Documentation

- `docs/BlackBox _ Note de cadrage architecturale.pdf`: why a document database instead of a SQL table
- `docs/document-schemas.md`: the document model, with the embedding and referencing choices
- `docs/optimization-note.md`: explain before and after the index, and why the fields are in this order
- `docs/measures/funnel-explain.js`: the script used for the measures

# Limits and future improvements

The generator was built to work first, then to be refined. These simplifications are known:

- Response times are the same for every endpoint (uniform between 20 and 2,000 ms), so the mean and p95 are almost identical everywhere.
- Short periods lose conversions : A user notified at the end of the period has no time to log in and pay before it ends.
- All events are inserted in a single `saveAll` : Fine for 204,000 events, but millions would need batches to limit memory use.
- An invalid period returns HTTP 500 instead of 400. : A global exception handler would turn the `IllegalArgumentException` into a proper 400 response.
- The 4 services repeat the same period validation : It could move to one shared class.
