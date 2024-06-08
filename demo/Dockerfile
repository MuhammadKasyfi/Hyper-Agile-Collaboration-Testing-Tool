FROM maven:3.8.5-openjdk-17 AS build
COPY . . 
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
# copy the snapshot jar file to the container
COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar demo.jar 

EXPOSE 8090

ENTRYPOINT ["java","-jar","demo.jar "]

