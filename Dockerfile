FROM ubuntu:latest
LABEL authors="jhonny"

ENTRYPOINT ["top", "-b"]

FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests
RUN mv target/*.jar target/application.jar

FROM amazoncorretto:17.0.14

WORKDIR /app
COPY --from=build /app/target/application.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]