FROM postgres:12
MAINTAINER FG Web (fg-web@samfundet.no)

ENV POSTGRES_DB photodb

RUN mkdir /base-sql

COPY postgres/sql/init.sql /base-sql