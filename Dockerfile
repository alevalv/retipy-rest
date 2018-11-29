#build the retipy-rest jar
FROM gradle:5.0.0-jdk8 as builder
ARG backendUrl=http://localhost:5000/retipy
USER gradle
RUN mkdir /home/gradle/project
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . /home/gradle/project
RUN sed -i "s,http://localhost:5000/retipy,$backendUrl,g" src/main/resources/application.properties
RUN gradle clean bootJar

#deploy image
FROM openjdk:11.0.1-jdk-slim-sid
LABEL maintainer="Alejandro Valdes <alejandrovaldes@live.com>"
COPY --from=builder /home/gradle/project/build/libs/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
