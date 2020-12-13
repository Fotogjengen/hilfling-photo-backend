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

## Build docker image
`mvn spring-boot:build-image`



