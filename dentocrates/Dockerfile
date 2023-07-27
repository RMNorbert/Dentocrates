# build the application without running the tests with  ./mvnw clean package -DskipTests
# to skip the execution of tests during the build process, CI/CD pipeline run before deployment
FROM openjdk:17-jdk
WORKDIR /app
COPY target/dentocrates-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD ["java", "-jar", "dentocrates-0.0.1-SNAPSHOT.jar"]
