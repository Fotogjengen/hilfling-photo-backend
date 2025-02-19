# Stage 1: Build the project

FROM openjdk:21-slim AS MAVEN_BUILD

# Install Maven
RUN apt-get update && apt-get install -y maven

# Tomcat creates a working dir at /tmp by default. Might be optional
VOLUME /tmp
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn package -DskipTests=true
RUN ls /build/target

# Stage 2: prepare launch environment
FROM openjdk:21-slim
# Running with user privileges helts to migate some risks: This is currently disabled bacause
# The application did not have enough privileges to save images to disk
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/*.jar /app/
RUN ls /app/

# To reduce startup time: adding /dev/urandom as a source of entropy
ENV SERVER_PORT=8000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/hilfling-0.0.1-SNAPSHOT.jar"]
EXPOSE 8000