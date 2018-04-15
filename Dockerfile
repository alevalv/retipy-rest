FROM openjdk:8-jdk-alpine

LABEL maintainer="Alejandro Valdes <alejandrovaldes@live.com>"

VOLUME /tmp
ADD build/libs/retipy-rest-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]



