# java-filmorate

A Spring Boot service for managing films rates, user reviews, and recommendations.

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.4-green.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6-red.svg)](https://maven.apache.org)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17.4-blue.svg)](https://www.postgresql.org)
[![H2](https://img.shields.io/badge/H2_Database-2.2.224-lightgrey.svg)](https://www.h2database.com)

## Features

### Core Functionality
- **Film Management**
    - CRUD operations, and validation
    - Metadata handling (MPA ratings, genres, directors), and cross-validation
    - Multi-criteria search
    - Filtering based on popularity and common interests
- **User Interactions**
    - User registration and profile management
    - Friendship system (add/remove friends, view mutual friends)
    - Like/dislike films and reviews
    - Personalized film recommendations
- **Review System**
    - Post and manage film reviews
    - Like/dislike reviews to affect usefulness score
    - View reviews for specific films and all reviews

### Storage Options
- **Database Support**
    - PostgreSQL for production
    - H2 in-memory database for testing
- **Flexible Data Access**
    - JdbcTemplate for database operations
    - In-memory storage classes for testing

### REST API
    - Comprehensive endpoints for all entities
    - JSON request/response format
    - Proper HTTP status codes, error handling, and validation

### Additional Features
    - Event feed tracking user activities
    - Director management system, and common films between friends
    - Film recommendations based on user preferences


## Getting Started

### Prerequisites
- Java 21 or later
- Maven 3.6 or later
- PostgreSQL 17.4 or later (optional, for production)
- Docker 27.4.0 or later (optional, for containerized deployment)

### Clone the Repository
  ```sh
git clone git@github.com:DawydowGerman/java-filmorate.git
  ```
  ```sh
  cd java-filmorate
  ```

### Build with Maven
  ```sh
  mvn clean package
  ```

### Run the Application

- Option 1: With H2 (default)
  ```sh
  mvn spring-boot:run
  ```

- Option 2: With PostgreSQL
  Update application-prod.properties with your PostgreSQL credentials
  ```sh
  mvn spring-boot:run -Dspring.profiles.active=prod
  ```

- Option 3: With Docker
  ```sh
  docker-compose up --build
  ```