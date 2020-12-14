FROM maven:3.6.3-jdk-11-slim AS MAVEN_BUILD
MAINTAINER fg-web@samfundet.no
# Tomcat creates a working dir at /tmp by default. Might be optional
VOLUME /tmp
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn package
RUN ls /build/target

FROM alpine:latest
RUN apk --no-cache add openjdk11
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/*.jar /app/
RUN ls /app/
run ls /app
#ARG JAR_FILE
#COPY target/*.jar app.jar
#COPY .env .env
# To reduce startup time: adding /dev/urandom as a source of entropy
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/hilfing-0.0.1-SNAPSHOT.jar"]
