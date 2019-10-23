FROM openjdk:11
# Tomcat creates a working dir at /tmp by default. Might be optional
VOLUME /tmp
ARG JAR_FILE
COPY target/*.jar app.jar
# To reduce startup time: adding /dev/urandom as a source of entropy
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
