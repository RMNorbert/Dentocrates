FROM maven:3.8.3-openjdk-17-slim AS build
WORKDIR /project
COPY . /project
WORKDIR /project
RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim
COPY --from=build project/target/dentocrates-0.0.1-SNAPSHOT.jar .
RUN apt-get update && apt-get install -y curl
EXPOSE 8080
CMD ["java", "-jar", "dentocrates-0.0.1-SNAPSHOT.jar"]
