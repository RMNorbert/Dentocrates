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

Dentocrates is an online platform that simplifies the process of finding and booking dental services. 

The application is created using Java, Spring, PostgreSQL, and React, providing a seamless and user-friendly web experience.
The website enables users to create an account, search for dentists in their area, view available appointments, book appointments, leave reviews about clinics they visited, plan routes for clinics on a map, and get basic information from a chatbot with switchable response styles. Additionally, registered dentists can register their clinics and locations, manage appointments and leaves.

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
  
  
---
---
## Used technologies:

 #### Backend
  - Java (version 17.0.7),
  - Spring Boot (version 3.1.0),Spring Security (version 3.1.0), JWT, Actuator, Webflux
  - JPA Hibernate (version 3.1.0),
  - Lombok,
  - OAuth 2.0
  - JavaMail & Gmail API
  - Python
  - Flask
  - Spring Cloud Netflix, Spring Cloud Gateway (in progress on eureka branch)
 
 #### Frontend
  - TypeScript (on typescript branch)
  - JavaScript,
  - React,
  - MapBox GL JS
    
 #### Database
  - Liquibase
  - PostgreSQL
  
  #### Others
  - Docker (refactoring in progress on eureka branch),
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
### 3. To use the email sending services :
  Create a project and an OAuth 2.0 Client ID on Google Cloud APIs & Services Credentials page by clicking on the  CREATE CREDENTIALS button use OAUTH client ID choose the Desktop app and then dowload the json file place it in the following directory: 

  ```
  src/main/resources/
  ```
Then copy the full file name, including the ".json" extension and paste it as a value to the resourcePath variable. Which is **located in the following directory on the 49th line**:

```
  src/main/java/service/client/communicationServices/GMailerService
```

---
#### 4. Set up the necessary environment variables and configure the database connection details.
   Update the configuration files with the appropriate values.

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
