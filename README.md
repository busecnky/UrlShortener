# URL Shortener Service

This is a Spring Boot application that provides a URL shortening service with support for custom IDs, TTL (time-to-live) for shortened URLs, and logging.

## Features

- Shorten URLs with unique alphanumeric IDs.
- Custom short IDs can be provided by the client.
- TTL (time-to-live): Shortened URLs can expire after a specified duration.
- Scheduled cleanup of expired URLs.
- RESTful endpoints for creating, retrieving, and deleting short URLs.
- Comprehensive logging using SLF4J.

## Technologies Used

- **Java 17**: The programming language.
- **Spring Boot**: Framework for developing the REST API.
- **PostgreSQL**: The database for storing URLs.
- **Spring Data JPA**: ORM for database interactions.
- **SLF4J**: For logging.
- **JUnit and Mockito**: For unit testing.

## Prerequisites

- Java 17 or higher installed.
- PostgreSQL installed and running.
- Gradle installed.

## Setup

1. Clone the repository:
    ```bash
    git clone https://github.com/your-repo/url-shortener.git
    cd url-shortener
    ```

2. Configure the database:
   Update the `application.yml` file with your PostgreSQL database details:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/url_shortener
       username: your_username
       password: your_password
     jpa:
       hibernate:
         ddl-auto: update
