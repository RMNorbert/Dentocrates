<!-- MANPAGE: BEGIN EXCLUDED SECTION -->
<div align="center">

[<img src="https://github.com/RMNorbert/Dentocrates/blob/main/dentocrates/frontend/public/dentocrates-dark-logo.png" alt="Dentocrates" width="200">](README.md)

[![Java](https://img.shields.io/badge/Java-blue.svg?logo=openjdk&logoColor=white&labelColor=555555&style=for-the-badge)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
[![Spring Boot](https://img.shields.io/badge/-Spring%20Boot-brightgreen.svg?logo=spring&labelColor=555555&style=for-the-badge)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/-Spring%20Security-darkgreen.svg?logo=springsecurity&labelColor=555555&style=for-the-badge)](https://spring.io/projects/spring-security)
[![PostgreSQL](https://img.shields.io/badge/-PostgreSQL-blue.svg?logo=postgresql&logoColor=0197f6&labelColor=555555&style=for-the-badge)](https://www.postgresql.org)
[![Docker](https://img.shields.io/badge/-docker-blue.svg?logo=docker&logoColor=0197f6&labelColor=white&style=for-the-badge)](https://www.docker.com/)
[![Python](https://img.shields.io/badge/Python-00264D.svg?logo=python&logoColor=gold&labelColor=black&style=for-the-badge)](https://www.python.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-00264D.svg?logo=TypeScript&logoColor=gold&labelColor=black&style=for-the-badge)](https://www.typescriptlang.org/)

[<img src="https://github.com/RMNorbert/Dentocrates/blob/main/Oauth.png" alt="oauth" width="115">](https://oauth.net/)
[![GMAIL](https://img.shields.io/badge/gmail-323330.svg?logo=gmail&logoColor=DB4437&labelColor=white&style=for-the-badge)](https://console.cloud.google.com/)
[![OpenAPI 3](https://img.shields.io/badge/OpenApi-323330.svg?logo=openapiinitiative&logoColor=66FF01&labelColor=323330&style=for-the-badge)](https://www.openapis.org/)
[![Hibernate](https://img.shields.io/badge/Hibernate-323330.svg?logo=Hibernate&logoColor=4d6b53&labelColor=748b97&style=for-the-badge)](https://hibernate.org/)
[![JWT](https://img.shields.io/badge/JWT-323330?style=for-the-badge&logo=jsonwebtokens&logoColor=red)](https://jwt.io/)
[![Java Script](https://img.shields.io/badge/JavaScript-323330?style=for-the-badge&logo=javascript&logoColor=F7DF1E)](https://www.javascript.com/)
[![React](https://img.shields.io/badge/React-323330.svg?logo=react&logoColor=blue&labelColor=323330&style=for-the-badge)](https://vitejs.dev/)

[![License: Unlicense](https://img.shields.io/badge/-Unlicense-blue.svg?logo=unlicense&logoColor=white&style=for-the-badge)](LICENSE "License")
[![Last Commit](https://img.shields.io/github/last-commit/RMNorbert/Dentocrates?logo=github&label=Last%20Commit&style=for-the-badge&display_timestamp=committer)](https://github.com/RMNorbert/Dentocrates/commits "Commit History")

</div>
<!-- MANPAGE: END EXCLUDED SECTION -->



# Dentocrates

[Table of content:](#description)
- [Used technologies](#used-technologies)
- [Features](#features)
- [Getting started](#getting-started)

  
## Description:

Dentocrates is an online platform for dental appointments designed to streamline the process of finding and booking dental services. The application is built using Java, Spring, PostgreSQL, and React, providing a comprehensive and user-friendly web experience. The website allows users to register for an account, search for dentists in their area, and view available appointments.

# Dockerized version can be pulled from DockerHub:
https://hub.docker.com/repository/docker/7nrm/dentocrates/general

## Used technologies:

 Backend
  - Java (version 17.0.7),
  - Spring Boot (version 3.1.0),Spring Security (version 3.1.0), JWT, Actuator, Webflux
  - JPA Hibernate (version 3.1.0),
  - Lombok,
  - OAuth 2.0
  - JavaMail & Gmail API
  - 2FA logic
  - Python
      
 Frontend
  - TypeScript (on typescript branch)
  - JavaScript,
  - React,
  
 Database
  - PostgreSQL
  
  Others
  - Docker,
  - CI/CD GitHub workflows
  - OpenApi (version 3.0)

## Features

- **Registration and authentication for users and dentists**

- **Clinic Registration and Management**

- **Search for Dentists and Clinics**

- **Appointment booking and management for users and clinics**

- **2FA & Notifications & Verifications**

- **Chatbot (in progress)**
  
## Getting Started

Follow these instructions to get a copy of the Dentocrates project up and running on your local machine for development and testing purposes.

## Prerequisites

To set up the project, follow these steps:
    
Make sure you have the following dependencies installed before proceeding with the installation:

#### 1.   Java Development Kit (JDK):
   Ensure that you have the Java Development Kit installed on your machine. You can download the JDK from the Oracle website and install it according to the provided instructions.

#### 2.    JDBC Database (e.g., PostgreSQL):
   In order to use a JDBC database with the application, such as PostgreSQL, make sure you have the necessary database server installed on your system. You can download and install PostgreSQL from the official PostgreSQL website and configure it as required.

#### 3.   In case of using Oauth:
  To use Google’s OAuth 2.0 authentication system for login, you must set up a project in the Google API Console to obtain OAuth 2.0 credentials.

## Installation:

  Follow these instructions to get a copy of the Dentocrates project up and running on your local machine:

1. Clone the repository
    
2. Set up the necessary environment variables and configure the database connection details. Update the configuration files with the appropriate values.
   The configuration files located in : dentocrates/src/main/resources/application.yml, an environment variable called SECRET have to created for the key creation,
   an environment variable have to be created for the oauthId and the oauthSecret according to the related credentials. 

4. Build and run the project: Dentocrates can be built and run using your preferred development environment or command-line tools. Here are a few options:

    Using Maven: Open a terminal or command prompt, navigate to the project's root directory, and run the following command:
        ```
        mvn spring-boot:run
        ```
        This command will build the project, resolve the dependencies, and start the server.

    Access the application: Once the server is up and running, you can access the Dentocrates application through the provided URL. Open a web browser and enter the appropriate URL (e.g., http://localhost:8080) to access the application. You can create an account, explore dentists, and schedule appointments.

Note: The URL and port number may vary depending on your configuration.

## License

This project is licensed under the Unlicense License - see the [License](LICENSE) file for details.
