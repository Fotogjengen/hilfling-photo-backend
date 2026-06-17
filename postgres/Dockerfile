FROM postgres:17
LABEL maintainer="FG Web (fg-web@samfundet.no)"

ENV POSTGRES_DB hilflingdb

# set locale
ENV POSTGRES_INITDB_ARGS="--locale-provider=icu --icu-locale=nb-NO --locale=C.UTF-8 --encoding=UTF8"

