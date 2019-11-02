DROP DATABASE hilflingdb;
CREATE DATABASE hilflingdb;

\c hilflingdb
/* CREATE TABLE IF NOT EXISTS album ( */
/*     id serial PRIMARY KEY, */
/* 	title text NOT NULL, */
/* 	analog BOOLEAN NOT NULL, */
/* 	date_created DATE NOT NULL */
/* ); */

/* CREATE TABLE IF NOT EXISTS category ( */
/*     id serial PRIMARY KEY, */
/* 	title text NOT NULL */
/* ); */
/* CREATE TABLE IF NOT EXISTS photographer ( */
/*     id serial PRIMARY KEY, */
/* 	name text NOT NULL */
/* ); */
/* CREATE TABLE IF NOT EXISTS security( */
/*     id serial PRIMARY KEY, */
/* 	security_type text NOT NULL */
/* ); */
/* CREATE TABLE IF NOT EXISTS motive ( */
/*     id serial PRIMARY KEY, */
/* 	title text NOT NULL, */
/* 	date_created DATE NOT NULL, */
/* 	album_id INTEGER REFERENCES album(id), */
/* 	category_id INTEGER REFERENCES category(id) */

/* ); */
/* CREATE TABLE IF NOT EXISTS place ( */
/*     id serial PRIMARY KEY, */
/* 	title text NOT NULL */
/* ); */
/* CREATE TABLE IF NOT EXISTS photo ( */
/*     id serial PRIMARY KEY, */
/*     motive text NOT NULL, */
/* 	motive_id INTEGER REFERENCES motive(id) NOT NULL, */
/* 	place_id INTEGER REFERENCES place(id), */
/* 	-- TODO Cange default for security */
/* 	security_id INTEGER REFERENCES security(id) DEFAULT 1, */
/* 	photographer_id INTEGER REFERENCES photographer(id), */
/*     url_large text NOT NULL, */
/*     url_medium text NOT NULL, */
/*     url_small text NOT NULL, */
/* 	good_picture BOOLEAN DEFAULT false */
/* ); */

/* CREATE TABLE IF NOT EXISTS analog ( */
/* 	-- TODO */
/* ) INHERITS (photo); */

/* CREATE TABLE IF NOT EXISTS tag ( */
/*     id serial PRIMARY KEY, */
/* 	title text NOT NULL */
/* ); */
/* CREATE TABLE IF NOT EXISTS tagphoto ( */
/*     id serial PRIMARY KEY, */
/* 	tag_id INTEGER REFERENCES tag(id) NOT NULL, */
/* 	photo_id INTEGER REFERENCES photo(id) NOT NULL */
/* ); */
/* CREATE TABLE IF NOT EXISTS orders( */
/*     id serial PRIMARY KEY, */
/* 	name text NOT NULL, */
/* 	email text  NOT NULL, */
/* 	adress text NOT NULL, */
/* 	place text  NOT NULL, */
/* 	zip_code INTEGER NOT NULL, */
/* 	post_or_get text NOT NULL, */
/* 	comment text, */
/* 	date_created DATE NOT NULL, */
/* 	order_complete BOOLEAN DEFAULT false */

/* ); */
/* CREATE TABLE IF NOT EXISTS orderphotos( */
/*     id serial PRIMARY KEY, */
/* 	photo_id INTEGER REFERENCES photo(id) NOT NULL, */
/* 	order_id INTEGER REFERENCES orders(id) NOT NULL */

/* ); */
