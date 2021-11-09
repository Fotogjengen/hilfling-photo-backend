# hilfling-photo-backend
Photo backend

Resource server for hilfling app

# Installation

`mvn clean install`

# Start the project

Start the database:

`docker-compose up -d postgres`

Start hilfing service
`mvn spring-boot:run`

To use prod database
`mvn spring-boot:run -Drun.profiles=prod`

# Linting
TODO

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

