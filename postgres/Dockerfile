FROM postgres:12
MAINTAINER FG Web (fg-web@samfundet.no)

ENV POSTGRES_DB hilflingdb

RUN mkdir /base-sql

COPY postgres/sql/init.sql /base-sql
