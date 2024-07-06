#BUILD STAGE
FROM maven:3.8.3-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#PACKAGE STAGE

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp

COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8081