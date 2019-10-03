DROP DATABASE photodb;
CREATE DATABASE photodb;

\c photodb

CREATE TABLE IF NOT EXISTS photographer (
    id serial PRIMARY KEY
);
CREATE TABLE IF NOT EXISTS security(
    id serial PRIMARY KEY
);
CREATE TABLE IF NOT EXISTS motive (
    id serial PRIMARY KEY
);
CREATE TABLE IF NOT EXISTS place (
    id serial PRIMARY KEY
);
CREATE TABLE IF NOT EXISTS photo (
    id serial PRIMARY KEY,
    motive text NOT NULL,
	motive_id INTEGER REFERENCES motive(id) NOT NULL,
	place_id INTEGER REFERENCES place(id),
	-- TODO Cange default for security
	security_id INTEGER REFERENCES security(id) DEFAULT 1,
	photographer_id INTEGER REFERENCES photographer(id),
    url_large text NOT NULL,
    url_medium text NOT NULL,
    url_small text NOT NULL,
	good_picture BOOLEAN DEFAULT false
);

CREATE TABLE IF NOT EXISTS analog (
	-- TODO
) INHERITS (photo);

CREATE TABLE IF NOT EXISTS album (
    id serial PRIMARY KEY,
	title text,
	analog BOOLEAN DEFAULT false,
	date_created DATE NOT NULL
);
CREATE TABLE IF NOT EXISTS category (
    id serial PRIMARY KEY
);
CREATE TABLE IF NOT EXISTS tag (
    id serial PRIMARY KEY
);
CREATE TABLE IF NOT EXISTS tagphoto (
    id serial PRIMARY KEY
);
CREATE TABLE IF NOT EXISTS tag (
    id serial PRIMARY KEY
);
CREATE TABLE IF NOT EXISTS photoorder(
    id serial PRIMARY KEY
);
CREATE TABLE IF NOT EXISTS orderphotos(
    id serial PRIMARY KEY
);
CREATE TABLE IF NOT EXISTS orderphotos(
    id serial PRIMARY KEY
);
