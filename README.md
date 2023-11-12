<div align="center">

[<img src="https://github.com/RMNorbert/Dentocrates/blob/main/dentocrates/frontend/public/dentocrates-dark-logo.png" alt="Dentocrates" width="200">](README.md)

[![Java](https://img.shields.io/badge/Java-009400.svg?logo=openjdk&logoColor=white&labelColor=black&style=for-the-badge)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
[![Spring Boot](https://img.shields.io/badge/-Spring%20Boot-66ff00.svg?logo=spring&labelColor=black&style=for-the-badge)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/-Spring%20Security-darkgreen.svg?logo=springsecurity&labelColor=black&style=for-the-badge)](https://spring.io/projects/spring-security)

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
[![MapBox GL JS](https://img.shields.io/badge/MapBox%20GL%20JS-black.svg?logo=mapbox&logoColor=blue&labelColor=black&style=for-the-badge)](https://www.mapbox.com/)


[![License: Unlicense](https://img.shields.io/badge/-Unlicense-blue.svg?logo=unlicense&logoColor=white&style=for-the-badge)](LICENSE "License")
[![Last Commit](https://img.shields.io/github/last-commit/RMNorbert/Dentocrates?logo=github&label=Last%20Commit&labelColor=323330&style=for-the-badge&display_timestamp=committer&color=darkgreen)](https://github.com/RMNorbert/Dentocrates/commits "Commit History")

</div>

---
# Dentocrates

[Table of content:](#description)
- [Features](#features)
- [Used technologies](#used-technologies)
- [Getting started](#getting-started)
- [Default Login Credentials](#default-login-credentials)
- [License](#license)
---
## Description:

Dentocrates is an online platform for dental appointments designed to streamline the process of finding and booking dental services. 

The application is built using Java, Spring, PostgreSQL, and React, providing a comprehensive and user-friendly web experience. The website allows users to register for an account, a registered customer can search for dentists in their area, view and book available appointments, leave review about clinics after appointments, search and plan routes for clinics on a map, get basic informations from chatbot with switchable response style. A registered dentsist can register clinics, locations for the clinics, manage appointments and leaves.

---
## Features

- **Registration and Authentication for users and dentists**

- **Clinic Registration and Management**

- **Search for Dentists and Clinics**

- **Appointment Booking and Management for users and clinics**

- **2FA & Notifications & Verifications**

- **Chatbot**

- **Reviews & Ratings**

- **Interactive map with route planning to clinics**

- **User logins stored, notification sent upon suspicious login or login attempts** 
  
- **Connect microservices with Eureka (in progress)**
  
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
  - MapBox GL JS
    
 #### Database
  - Liquibase
  - PostgreSQL
  
  #### Others
  - Docker,
  - CI/CD GitHub workflows
  - OpenApi (version 3.0)

---
## Getting Started

Follow these instructions to get a copy of the Dentocrates project up and running on your local machine for development and testing purposes.

---
## Prerequisites

To set up the project, follow these steps:

###  To deploy Dentocrates using Docker containers, follow these steps:

#### [Install Docker](https://www.docker.com/get-started/):
  
  **For Linux**: Follow the instructions on the official Docker website.
  
  **For Windows or macOS**: Install Docker Desktop for an easy-to-use Docker environment.

#### After installing Docker:
 Ensure it's running by opening a terminal or command prompt and running the command 
 ```
 docker --version
 ```

Note: Docker is optional and recommended for deployment scenarios. If you're using Docker, it can help manage dependencies and ensure consistent environments.

---
###  To run Dentocrates without using Docker:

Make sure you have the following dependencies installed before proceeding with the installation:

### 1.   Java Development Kit (JDK):
   Ensure that you have the Java Development Kit installed on your machine. You can [download](https://www.oracle.com/java/technologies/downloads/) the JDK from the Oracle website and install it according to the provided instructions.

### 2.    JDBC Database (e.g., PostgreSQL):
   In order to use a JDBC database with the application, such as PostgreSQL, make sure you have the necessary database server installed on your system. You can [download](https://www.postgresql.org/download/) and install PostgreSQL from the official PostgreSQL website and configure it as required.

### 3.   In case of using Oauth:
  To use Google’s OAuth 2.0 authentication system for login, you must set up a project in the Google API Console and obtain OAuth 2.0 credentials.

---
## Installation:

  Follow these instructions to get a copy of the Dentocrates project up and running on your local machine:

#### 1. Clone the repository
```
git@github.com:RMNorbert/Dentocrates.git
```

---
#### 2. To use the ChatBot
   Clone the following repository as well : [https://github.com/RMNorbert/ConvoCat](https://github.com/RMNorbert/ConvoCat) and follow the instructions in it's **Installation section**.

---
#### 3. Set up the necessary environment variables and configure the database connection details.
   Update the configuration files with the appropriate values.

   ---
  - #### **Without using Docker:**
  
  - **3.1, Configuration for database connection:**
     
     - Located in ```dentocrates/src/main/resources/application.yml```, the following environment variables have to be created or updated according to your Postgresql user: **url,username , password**.

       To run Dentocrates without using Docker you can use:
       ```
       url:jdbc:postgresql://localhost:5432/dentocrates
       ```
   
  - **3.2, Other environment variables to update** before running the application: 
   
     - **3.2.1, Other environment variables:**
        - CHAT_URL : This variable used in the post request to the chatbot : ```http://127.0.0.1:5000/predict```
        
        - REDIRECT_URI: This variable used during OAUTH: ``` http://localhost:3000/login/oauth2/code/ ```
        
        - OAUTH_SECRET and the OAUTH_ID: created for the oauth login, update it according to the related credentials
        
        - SECRET : This variable have to be created for the JWT related key creation 
      
        - SENDER_USERNAME: have to be created to use the email related services, update it according to the email you whish to use
     
     - **3.2.2, In case of using the application-lifecycle.sh** to run the application, the variables in the application-lifecycle.sh file have to be updated. The sh file is located in:
       ```dentocrates/```
       
---  
  - #### **In case of using Docker:**

  - **3.1, Using the docker commpose file** to run the application, the postgres and dentocrates service environment variables in the docker commpose file have to be updated, the file

    located in:
     ```
      dentocrates/
     ```
     
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
       npm start
       ```
       This command will start the frontend server.
   ___

 - #### 4.2, Run the dockerized version with:
  
    - Navigate to the project directory containing the docker-compose.yml file.

      Run the following command to build and start the project:
      ```
      docker-compose up --build
      ```

      The docker-compose.yml file defines the services and configurations needed for running your application in a Docker container.
      It simplifies deployment and ensures consistent setups across environments.
  ___

  - #### 4.3, Run with the application-lifecycle.sh:
  
    **Unix-like Systems:(Linux, macOs)**
    - First make the file executable by running the following command:
      ```
      chmod +x application-lifecycle.sh
      ```
    - Run the sh file with the following command or run it as a program
      ```
      ./application-lifecycle.sh
      ```
---
#### 5. Access the application:

Once the server is up and running, you can access the Dentocrates application through the provided URL. Open a web browser and enter the appropriate URL (e.g. in case of **mvn spring-boot:run** and **npm start** or in case of using the **application-lifecycle.sh** file **http://localhost:3000** , in case of **docker** **http://localhost:8080**) to access the application. You can create an account, explore dentists, schedule appointments, chat with the ChatBot.

Note: The URL and port number may vary depending on your configuration.

---
## Default Login Credentials
   
   For quicker testing purposes, you can login to the application by using the following credentials:

   | Role     | Email                  | Password      | Verification code 1 | Verification code 2 | Verification code 3 | 
   | -------- | ---------------------- | ------------- | ------------------- | ------------------- | ------------------- |
   | Customer | ```test@email.com```   | password      | vc123450            | vc123451            | vc123452            |
   | Dentist  | ```dentist@email.com```| password      | vc123453            | vc123454            | vc123455            |


---
## License

This project is licensed under the Unlicense License - see the [License](LICENSE) file for details.
