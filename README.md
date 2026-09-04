# hilfling-photo-backend

Photo backend.

Resource server for hilfling app>

# Test-users

- gjengsjef
- web
- denye
- pang_gjengsjef
- pang_web

# Installation

First install required developer SDKs

## Prerequisits

First install required packages:

- Kotlin
- Maven
- KTLint

Then run:
`mvn clean install`

# Start the project

## For dev-version

Run the command. This will run all the necessary services for the backend, including the hilfling service itself.
`docker compose up`

## For prod-version

Run this command. Runs everything BUT pgadmin and postgres, as azure will run these in prod
`docker compose --profile prod up`

# Linting

This project use [KTlint](https://github.com/pinterest/ktlint) to keep code formatting consistent.

## Check format

To check linting run:
`ktlint .`

## Fix formatting

To automaticly fix formatting run:
`ktlint --format .`

## Git pre-commit hook

To install git pre-commit hook and avoid commits that do not follow formatting guidelines run:
`ktlint --install-git-pre-commit-hook`

# Build

`mvn package`

run the built package
`java --jar target/hilfling-0.0.1.SNAPSHOT.jar`

## Configuration

### Database

To specify a different database connection

Set these environment variables:

```
export LISTENING_IP=localhost
export LISTENING_PORT=8080

export DATABASE_USERNAME=<username>
export DATABASE_PASSWORD=<password>
export DATABASE_URL=jdbc:postgresql://<ip:port>/<database name>
export DATABASE_DRIVER=org.postgresql.Driver
```

[https://www.baeldung.com/spring-properties-file-outside-jar](https://www.baeldung.com/spring-properties-file-outside-jar)

### Security

To set update CORS origins, set the `CORS_ALLOWED_ORIGINS` env variable. The auth endpoint has its own CORS policy, and can be changed with `AUTH_CORS_ALLOWED_ORIGINS`.

## Build docker image

`mvn spring-boot:build-image`

# Testing

https://www.baeldung.com/kotlin-speek

## PGAdmin

`localhost:5050`
`username: admin@admin.com`
`password: password`

## PostgreSql:

`hostname:hilflingdb`
`username:hilfling`
`password:password`
