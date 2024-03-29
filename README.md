# hilfling-photo-backend
Photo backend.

Resource server for hilfling app>

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

## For front-end developers

Run the command. This will run all the necessary services for the backend, including the hilfling service itself.
`docker-compose up -d`

## For back-end developers

Run the command mentioned below. This will run all necessary services for the backend, except the hilfling service itself. 
`docker-compose -f docker-compose.dev up -d`

This means in order to start the hilfing service you must run the following command:

`mvn spring-boot:run`

To use prod database
`mvn spring-boot:run -Drun.profiles=prod`

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
