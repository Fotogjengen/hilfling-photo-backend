CREATE DATABASE photodb;

\c photodb

CREATE TABLE IF NOT EXISTS photo (
    id serial PRIMARY KEY,
    motive text NOT NULL,
	motive_id INTEGER REFERENCES motive(id)E  PRIMARY KEY,
	place_id INTEGER REFERENCES place(id)
);

CREATE TABLE IF NOT EXISTS analog (
	-- TODO
) INHERITS (photo);

CREATE TABLE IF NOT EXISTS album (
    id serial PRIMARY KEY,
);
CREATE TABLE IF NOT EXISTS place (
    id serial PRIMARY KEY,
);
CREATE TABLE IF NOT EXISTS motive (
    id serial PRIMARY KEY,
);
CREATE TABLE IF NOT EXISTS category (
    id serial PRIMARY KEY,
);
CREATE TABLE IF NOT EXISTS tag (
    id serial PRIMARY KEY,
);
CREATE TABLE IF NOT EXISTS tag-photo (
    id serial PRIMARY KEY,
);
CREATE TABLE IF NOT EXISTS tag (
    id serial PRIMARY KEY,
);
CREATE TABLE IF NOT EXISTS photographer (
    id serial PRIMARY KEY,
);
CREATE TABLE IF NOT EXISTS security(
    id serial PRIMARY KEY,
);
CREATE TABLE IF NOT EXISTS order(
    id serial PRIMARY KEY,
);
CREATE TABLE IF NOT EXISTS orderphotos(
    id serial PRIMARY KEY,
);
CREATE TABLE IF NOT EXISTS orderphotos(
    id serial PRIMARY KEY,
);
