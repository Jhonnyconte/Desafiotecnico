FROM ubuntu:latest
LABEL authors="jhonny"

ENTRYPOINT ["top", "-b"]

FROM amazoncorretto:17.0.14

WORKDIR /app

COPY target/tecnico-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]