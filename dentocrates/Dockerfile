# build the application without running the tests with  ./mvnw clean package -DskipTests
# to skip the execution of tests during the build process, CI/CD pipeline run before deployment
FROM maven:3.8.3-openjdk-17-slim AS build
WORKDIR /project
COPY mvnw mvnw.cmd .mvn/ ./
COPY . .
RUN ./mvnw clean package -DskipTests -Dmaven.repo.local=/root/.m2

FROM openjdk:17-jdk-slim
COPY --from=build project/target/dentocrates-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD ["java", "-jar", "dentocrates-0.0.1-SNAPSHOT.jar"]
