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

Install Docker with Compose (JDK 21 is only needed for local Java builds).
Copy `.env.example` to `.env`, keep `COMPOSE_PROFILES=dev`, and fill in the JWT keys as described there.
For Docker, set `DATABASE_URL=jdbc:postgresql://postgres:5432/hilflingdb`, then run `docker compose up --build`.

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

### Database

Set the database connection in `.env`. For Docker development:

```dotenv
DATABASE_URL=jdbc:postgresql://postgres:5432/hilflingdb
```

Use `localhost` instead of `postgres` when running the backend outside Docker, and set these variables in your shell or IDE. Production uses the external database credentials from the server's env file.

### Security

To set update CORS origins, set the `CORS_ALLOWED_ORIGINS` env variable. The auth endpoint has its own CORS policy, and can be changed with `AUTH_CORS_ALLOWED_ORIGINS`.

## Build docker image

`mvn spring-boot:build-image`

## PGAdmin

`localhost:5050`
`username: admin@admin.com`
`password: password`

## PostgreSql:

`hostname:hilflingdb`
`username:hilfling`
`password:password`
