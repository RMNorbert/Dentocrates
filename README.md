<!-- MANPAGE: BEGIN EXCLUDED SECTION -->
<div align="center">

[<img src="https://github.com/RMNorbert/Dentocrates/blob/main/dentocrates/frontend/public/dentocrates-dark-logo.png" alt="Dentocrates" width="200">](README.md)

<!--[![GitHub latest release version](https://img.shields.io/github/v/release/RMNorbert/InquireNet.svg?style=flat)](https://github.com/RMNorbert/InquireNet/releases/latest) -->


[![Java](https://img.shields.io/badge/Java-blue.svg?logo=openjdk&logoColor=white&labelColor=555555&style=for-the-badge)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
[![Spring Boot](https://img.shields.io/badge/-Spring%20Boot-brightgreen.svg?logo=spring&labelColor=555555&style=for-the-badge)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/-Spring%20Security-darkgreen.svg?logo=springsecurity&labelColor=555555&style=for-the-badge)](https://spring.io/projects/spring-security)
[![PostgreSQL](https://img.shields.io/badge/-PostgreSQL-blue.svg?logo=postgresql&logoColor=0197f6&labelColor=555555&style=for-the-badge)](https://www.postgresql.org)

[![JWT](https://img.shields.io/badge/JWT-323330?style=for-the-badge&logo=jsonwebtokens&logoColor=red)](https://jwt.io/)
[![Java Script](https://img.shields.io/badge/JavaScript-323330?style=for-the-badge&logo=javascript&logoColor=F7DF1E)](https://www.javascript.com/)
[![React](https://img.shields.io/badge/React-grey.svg?logo=react&logoColor=blue&labelColor=323330&style=for-the-badge)](https://vitejs.dev/)

[![License: Unlicense](https://img.shields.io/badge/-Unlicense-blue.svg?logo=unlicense&logoColor=white&style=for-the-badge)](License "License")
[![Last Commit](https://img.shields.io/github/last-commit/RMNorbert/Dentocrates?logo=github&label=Last%20Commit&style=for-the-badge&display_timestamp=committer)](https://github.com/RMNorbert/Dentocrates/commits "Commit History")


</div>
<!-- MANPAGE: END EXCLUDED SECTION -->



# Dentocrates
Dentocrates

[Description for the project](#description)
- [Used technologies](#used-technologies)
- [Features](#features)
- [Getting started](#getting-started)

  
## Description:

Dentocrates is an online platform for dental appointments, designed to streamline the process of finding and booking dental services. The application is built using Java, Spring, PostgreSQL, and React, providing a comprehensive and user-friendly web experience.
Features

## Used technologies:

 Backend
  - Java,
  - Spring Boot,Spring Security, JWT
  - JPA Hibernate,
  - Lombok,
    
 Frontend
  - JavaScript,
  - React,
  
 Database
  - PostgreSQL
  
  Others
  - Docker (In progress),
  - CI/CD GitHub workflows (In progress)

## Features

User Registration and Authentication:

    Users can create accounts and authenticate themselves on the Dentocrates platform.
    Registration can be done using email addresses.

Dentist Registration and Clinic Management:

    Dentists can register their clinics on Dentocrates and provide necessary details such
    as clinic name, address, contact information, working hours.

Search for Dentists and Clinics:

    Users can search for dentists or clinics.

Appointment Booking:

    Users can book appointments with dentists or clinics directly through the Dentocrates
    platform.Dentocrates provides a user-friendly interface where users can select their 
    preferred dentist, clinic, and appointment time slot. The system ensures that available
    appointment slots are displayed accurately based on the dentist or clinic's schedule.

Appointment Management:

    Users can view and manage their booked appointments, including canceling appointments
    if necessary. Dentists and clinics can also manage their appointment schedules, view upcoming
    appointments, and make any necessary updates.

## Getting Started

Follow these instructions to get a copy of the Dentocrates project up and running on your local machine for development and testing purposes.

Prerequisites:
  Before proceeding with the installation, make sure you have the following dependencies installed:

    Java Development Kit (JDK)
    Spring Framework
    React
    PostgreSQL Database

In addition, you will need the following dependencies specified in the project configuration (pom.xml):

```
  <dependency>
    <groupId>org.yaml</groupId>
    <artifactId>snakeyaml</artifactId>
    <version>2.0</version>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  <dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
  </dependency>
  <dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
  </dependency>
  <dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
  </dependency>
  <dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
  </dependency>
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
  </dependency>
  <dependency>
  <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
  </dependency>
```

Additionally, you will need the following dependencies specified in the package.json file:

```
{
  "dependencies": {
    "@testing-library/jest-dom": "^5.16.5",
    "@testing-library/react": "^13.4.0",
    "@testing-library/user-event": "^13.5.0",
    "date-fns": "^2.30.0",
    "jwt-decode": "^3.1.2",
    "react": "^18.2.0",
    "react-calendar": "^4.3.0",
    "react-dom": "^18.2.0",
    "react-router-dom": "^6.12.1",
    "react-scripts": "5.0.1"
  }
}
```

Installation:

  Follow these instructions to get a copy of the Dentocrates project up and running on your local machine:

1. Clone the repository: Start by cloning the Dentocrates repository to your local machine. Open a terminal or command prompt and run the following     command:
    ```
    git clone https://github.com/RMNorbert/Dentocrates.git
    ```
2. Set up the necessary environment variables and configure the database connection details. Update the configuration files with the appropriate values.

3. Build and run the project: Dentocrates can be built and run using your preferred development environment or command-line tools. Here are a few options:

    Using an IDE: Import the project into your favorite IDE (e.g., IntelliJ, Eclipse) as a Maven project. Build the project to resolve the dependencies and then run the main application class (DentocratesApplication.java) to start the server.

    Using Maven: Open a terminal or command prompt, navigate to the project's root directory, and run the following command:
        ```
        mvn spring-boot:run
        ```
        This command will build the project, resolve the dependencies, and start the server.

    Access the application: Once the server is up and running, you can access the Dentocrates application through the provided URL. Open a web browser and enter the appropriate URL (e.g., http://localhost:8080) to access the application. You can create an account, explore dentists, and schedule appointments.

Note: The URL and port number may vary depending on your configuration.
