CREATE DATABASE photodb;

\c photodb

CREATE TABLE IF NOT EXISTS photo (
    id serial PRIMARY KEY,
    motive text NOT NULL
);