<!-- MANPAGE: BEGIN EXCLUDED SECTION -->
<div align="center">

[<img src="https://github.com/RMNorbert/Dentocrates/blob/main/dentocrates/frontend/public/dentocrates-dark-logo.png" alt="Dentocrates" width="200">](README.md)

[![Java](https://img.shields.io/badge/Java-009400.svg?logo=openjdk&logoColor=white&labelColor=black&style=for-the-badge)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
[![Spring Boot](https://img.shields.io/badge/-Spring%20Boot-66ff00.svg?logo=spring&labelColor=black&style=for-the-badge)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/-Spring%20Security-darkgreen.svg?logo=springsecurity&labelColor=black&style=for-the-badge)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JWT-323330?style=for-the-badge&logo=jsonwebtokens&logoColor=red&labelColor=black)](https://jwt.io/)
[<img src="https://github.com/RMNorbert/Dentocrates/blob/main/Oauth.png" alt="oauth" width="115">](https://oauth.net/)
[![GMAIL](https://img.shields.io/badge/gmail-323330.svg?logo=gmail&logoColor=DB4437&labelColor=white&style=for-the-badge)](https://console.cloud.google.com/)

[![Hibernate](https://img.shields.io/badge/Hibernate-0CBAA6.svg?logo=Hibernate&logoColor=4d6b53&labelColor=black&style=for-the-badge)](https://hibernate.org/)
[![Liquibase](https://img.shields.io/badge/-Liquibase-blue.svg?logo=Liquibase&logoColor=0197f6&labelColor=black&style=for-the-badge)](https://www.liquibase.com/)
[![PostgreSQL](https://img.shields.io/badge/-PostgreSQL-blue.svg?logo=postgresql&logoColor=0197f6&labelColor=black&style=for-the-badge)](https://www.postgresql.org)
[![Docker](https://img.shields.io/badge/-docker-blue.svg?logo=docker&logoColor=0197f6&labelColor=black&style=for-the-badge)](https://www.docker.com/)
[![OpenAPI 3](https://img.shields.io/badge/OpenApi-323330.svg?logo=openapiinitiative&logoColor=66FF01&labelColor=black&style=for-the-badge)](https://www.openapis.org/)

[![Python](https://img.shields.io/badge/Python-00264D.svg?logo=python&logoColor=gold&labelColor=black&style=for-the-badge)](https://www.python.org/)
[![Flask](https://img.shields.io/badge/Flask-00264D.svg?logo=flask&logoColor=white&labelColor=black&style=for-the-badge)](https://flask.palletsprojects.com/en/2.3.x/)
[![PyTorch](https://img.shields.io/badge/PyTorch-00264D.svg?logo=pytorch&logoColor=DE3412&labelColor=black&style=for-the-badge)](https://pytorch.org/)
[![NumPy](https://img.shields.io/badge/NumPy-00264D.svg?logo=NumPy&logoColor=steelblue&labelColor=black&style=for-the-badge)](https://pytorch.org/)
[![Express](https://img.shields.io/badge/Express-black.svg?logo=Express&logoColor=gold&labelColor=black&style=for-the-badge)](https://expressjs.com/)

[![TypeScript](https://img.shields.io/badge/TypeScript-black.svg?logo=TypeScript&logoColor=gold&labelColor=black&style=for-the-badge)](https://www.typescriptlang.org/)
[![Java Script](https://img.shields.io/badge/JavaScript-black?style=for-the-badge&logo=javascript&logoColor=F7DF1E&labelColor=black)](https://www.javascript.com/)
[![React](https://img.shields.io/badge/React-black.svg?logo=react&logoColor=blue&labelColor=black&style=for-the-badge)](https://vitejs.dev/)


[![License: Unlicense](https://img.shields.io/badge/-Unlicense-blue.svg?logo=unlicense&logoColor=white&style=for-the-badge)](LICENSE "License")
[![Last Commit](https://img.shields.io/github/last-commit/RMNorbert/Dentocrates?logo=github&label=Last%20Commit&labelColor=323330&style=for-the-badge&display_timestamp=committer&color=darkgreen)](https://github.com/RMNorbert/Dentocrates/commits "Commit History")

</div>
<!-- MANPAGE: END EXCLUDED SECTION -->


---
# Dentocrates

[Table of content:](#description)
- [Used technologies](#used-technologies)
- [Features](#features)
- [Getting started](#getting-started)
- [License](#license)
---
## Description:

Dentocrates is an online platform for dental appointments designed to streamline the process of finding and booking dental services. 

The application is built using Java, Spring, PostgreSQL, and React, providing a comprehensive and user-friendly web experience. The website allows users to register for an account, search for dentists in their area, and view available appointments.

---
# Dockerized version can be pulled from DockerHub:
https://hub.docker.com/repository/docker/7nrm/dentocrates/general

---
## Used technologies:

 #### Backend
  - Java (version 17.0.7),
  - Spring Boot (version 3.1.0),Spring Security (version 3.1.0), JWT, Actuator, Webflux
  - JPA Hibernate (version 3.1.0),
  - Lombok,
  - OAuth 2.0
  - JavaMail & Gmail API
  - 2FA logic
  - Python
  - Flask
  - Express
        
 #### Frontend
  - TypeScript (on typescript branch)
  - JavaScript,
  - React,
  
 #### Database
  - Liquibase (in progress)
  - PostgreSQL
  
  #### Others
  - Docker,
  - CI/CD GitHub workflows
  - OpenApi (version 3.0)

---
## Features

- **Registration and Authentication for users and dentists**

- **Clinic Registration and Management**

- **Search for Dentists and Clinics**

- **Appointment Booking and Management for users and clinics**

- **2FA & Notifications & Verifications**

- **Chatbot**
  
- **Proxy Server**

- **Reviews & Ratings(in progress)**

- **Upload & Manage Medical Documents(in progress)**
  
---
## Getting Started

Follow these instructions to get a copy of the Dentocrates project up and running on your local machine for development and testing purposes.

---
## Prerequisites

To set up the project, follow these steps:
    
Make sure you have the following dependencies installed before proceeding with the installation:

### 1.   Java Development Kit (JDK):
   Ensure that you have the Java Development Kit installed on your machine. You can [download](https://www.oracle.com/java/technologies/downloads/) the JDK from the Oracle website and install it according to the provided instructions.

### 2.    JDBC Database (e.g., PostgreSQL):
   In order to use a JDBC database with the application, such as PostgreSQL, make sure you have the necessary database server installed on your system. You can [download](https://www.postgresql.org/download/) and install PostgreSQL from the official PostgreSQL website and configure it as required.

### 3.   In case of using Oauth:
  To use Google’s OAuth 2.0 authentication system for login, you must set up a project in the Google API Console and obtain OAuth 2.0 credentials.

### 4.   To deploy Dentocrates using Docker containers, follow these steps:

#### [Install Docker](https://www.docker.com/get-started/):
  
  **For Linux**: Follow the instructions on the official Docker website.
  
  **For Windows or macOS**: Install Docker Desktop for an easy-to-use Docker environment.

#### After installing Docker:
 Ensure it's running by opening a terminal or command prompt and running the command ```docker --version```.

Note: Docker is optional and recommended for deployment scenarios. If you're using Docker, it can help manage dependencies and ensure consistent environments.

---
## Installation:

  Follow these instructions to get a copy of the Dentocrates project up and running on your local machine:

#### 1. Clone the repository

---
#### 2. To use the ChatBot
   Clone the following repository as well : [https://github.com/RMNorbert/ConvoCat](https://github.com/RMNorbert/ConvoCat) and follow the instructions in it's **Installation section**.

---
#### 3. Set up the necessary environment variables and configure the database connection details.
   Update the configuration files with the appropriate values.
   
  - **3.1, Configuration for database connection:**
     
     - Located in ```dentocrates/src/main/resources/application.yml```, the following variables have to be updated according to your Postgresql user: **username , password**.
   
  - **3.2, Other environment variables to updated** before running the application: 
   
     - **3.2.1, Using the application-lifecycle.sh** to run then application the variables in the file have to be updated. The sh file is located in: ```dentocrates/```
   
     - **3.2.2, Using the docker commpose file** to run the application. The file located in: ```dentocrates/``` , update all services environment variables in the file.
   
     - **3.2.3, In other case:**
       - SECRET : This variable have to be created for the JWT related key creation, it is located in: ```dentocrates/src/main/java/com.rmnnorber.dentocrates/service/JwtService```
      
        - OAUTH_SECRET and the OAUTH_ID: created for the oauth login, update it according to the related credentials, it is located in : ```dentocrates/src/main/java/com.rmnnorber.dentocrates/security/config/SecurityConfiguration```
      
         - SENDER_USERNAME: have to be created to use the email related services, update it according to the email you whish to use, it is located in : ```dentocrates/src/main/java/com.rmnnorber.dentocrates/service/GMailerService``` 

---
### 4. Build and run the project: 
   Dentocrates can be built and run using your preferred development environment or command-line tools. Here are a few options:

  ___
  - #### 4.1,  Using Maven:


     - **4.1.1**, Open a terminal or command prompt, navigate to the project's dentocrates directory, and run the following command:
        ```
        mvn spring-boot:run
        ```
   
       This command will build the project, resolve the dependencies, and start the backend server.

    - **4.1.2**, Navigate to the project's dentocrates/frontend directory, and run the following command:
       ```
       npm run proxy
       ```
       This command will start the proxy server.
   
       Run the following command:
       ```
       npm start
       ```
       This command will start the frontend server.
   ___

 - #### 4.2, Run the dockerized version with:
  
    - Navigate to the project directory containing the docker-deploy.yml file.

      Run the command ```docker-compose up --build``` to build and start the project.

      The docker-deploy.yml file defines the services and configurations needed for running your application in a Docker container.
      It simplifies deployment and ensures consistent setups across environments.
  ___

  - #### 4.3, Run with the application-lifecycle.sh:
  
    **Unix-like Systems:(Linux, macOs)**
    - First make the file executable by running the following command:
      ```chmod +x application-lifecycle.sh```
    - Run the sh file with the following command or run it as a program
      ```./application-lifecycle.sh```
---
#### 5. Access the application:

Once the server is up and running, you can access the Dentocrates application through the provided URL. Open a web browser and enter the appropriate URL (e.g. in case of mvn spring-boot:run or in case of using the application-lifecycle.sh file http://localhost:3000 , in case of docker http://localhost:8080) to access the application. You can create an account, explore dentists, schedule appointments, chat with the ChatBot.

Note: The URL and port number may vary depending on your configuration.

---
## License

This project is licensed under the Unlicense License - see the [License](LICENSE) file for details.
