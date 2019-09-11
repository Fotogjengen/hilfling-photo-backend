#!/usr/bin/env bash

# Delete and create new DB
docker exec -it photodb psql -U postgres -c "DROP DATABASE IF EXISTS photodb"
docker exec -it photodb psql -U postgres -c "CREATE DATABASE photodb"

# Run init.sql
docker exec -it photodb psql -U postgres -c "\i /base-sql/init.sql"

